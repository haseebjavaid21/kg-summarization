package org.dice.kgsmrstn.selector;

import static java.util.stream.Collectors.toCollection;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.Syntax;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;
import org.dice.kgsmrstn.controller.KgsmrstnController;
import org.dice.kgsmrstn.graph.BreadthFirstSearch;
import org.dice.kgsmrstn.graph.Node;
import org.dice.kgsmrstn.graph.PageRank;
//import org.dice.kgsmrstn.util.Triple;
import org.dice.kgsmrstn.util.TripleIndex;
import org.slf4j.LoggerFactory;

import edu.uci.ics.jung.graph.DirectedSparseGraph;

public class EntityTriplesSelector {

	private static final String DB_RESOURCE = "http://dbpedia.org/resource/";
	private static final String DB_ONTOLOGY = "http://dbpedia.org/ontology/";
	private static final String DB_LABEL = "http://www.w3.org/2000/01/rdf-schema#label";
	private static final String ALGORITHM = "linksum";

	private org.slf4j.Logger log = LoggerFactory.getLogger(KgsmrstnController.class);

	private String endpoint;
	private String entity;

	private Double alpha = 0.5;
	private Integer k;
	private String predicateSelectionMode;

	private Map<Node, List<String>> allAssociationsToAnEntity = new HashMap<Node, List<String>>();
	private Map<String, Long> globalPredicateFrequency = new HashMap<String, Long>();

	private DirectedSparseGraph<Node, String> g = new DirectedSparseGraph<Node, String>();
	private Model model = ModelFactory.createDefaultModel();

	public EntityTriplesSelector(String endpoint, String entity, Integer k, String mode) {
		this.endpoint = endpoint;
		this.entity = entity;
		this.k = k;
		this.predicateSelectionMode = mode;
	}

	public LinkedList<Triple> getTriples() {
		try {
			return getAllTriples();
		} catch (Exception e) {
			return null;
		}
	}

	public Model getModel() {
		return model;
	}

	private LinkedList<Triple> getAllTriples() throws Exception {

		List<String> nodesReversed = new ArrayList<>();
		String resource = "<http://dbpedia.org/resource/" + entity + ">";
		log.info("Current resource..." + resource);

		// original query
		/*
		 * String query = "SELECT ?s ?p  WHERE {" + "?s ?p ?o ." +
		 * "FILTER (?o =" + resource + ")" +
		 * "FILTER (!regex(?p,'wikiPageWikiLink'))" +
		 * "FILTER (!regex(?p,'wikiPageRedirects'))" +
		 * "FILTER (!regex(?p,'wikiPageDisambiguates'))" +
		 * "FILTER (!regex(?p,'primaryTopic'))" +
		 * "} GROUP BY ?s ?p ORDER BY asc(?s)";
		 */

		String query = "SELECT ?s ?p  ?o WHERE { {" + "?s ?p ?o ." + "FILTER (?o =" + resource + ") }" + "UNION {"
				+ "?s ?p ?o. " + "FILTER (?s =" + resource + ") }" + "FILTER (!regex(?p,'wikiPageWikiLink'))"
				+ "FILTER (!regex(?p,'wikiPageRedirects'))" + "FILTER (!regex(?p,'wikiPageDisambiguates'))"
				+ "FILTER (!regex(?p,'primaryTopic'))" + "FILTER (!regex(?p,'wikiPageExternalLink'))"
				+ "FILTER (!regex(?p,'wikiPageEditLink'))" + "FILTER (!regex(?p,'wikiPageExtracted'))"
				+ "FILTER (!regex(?p,'wikiPageRevisionLink'))" + "FILTER (!regex(?p,'wikiPageRevisionID'))"
				+ "FILTER (!regex(?p,'wikiPageUsesTemplate'))" + "FILTER (!regex(?p,'wikiPageID'))"
				+ "FILTER (!regex(?p,'wikiPageLength'))" + "FILTER (!regex(?p,'wikiPageModified'))"
				+ "FILTER (!regex(?p,'wikiPageOutDegree'))" + "FILTER (!regex(?p,'wikiPageHistoryLink'))"
				+ "FILTER (!regex(?p,'isPrimaryTopicOf'))" + "FILTER (!regex(?p,'owl#sameAs'))"
				+ "FILTER (!regex(?p,'foaf/0.1/depiction'))" + "FILTER (!regex(?p,'dbpedia.org/ontology/thumbnail'))"
				+ "FILTER (!regex(?p,'dbpedia.org/property/'))" + "} GROUP BY ?s ?p ?o ORDER BY asc(?s) LIMIT 100";

		Query sparqlQuery = QueryFactory.create(query, Syntax.syntaxARQ);

		QueryEngineHTTP httpQuery = new QueryEngineHTTP(endpoint, sparqlQuery);
		try {
			ResultSet results = httpQuery.execSelect();
			QuerySolution solution;
			results.getResultVars().stream().forEach(result -> System.out.println(result));
			log.info("Obtained results:", results.getRowNumber());
			while (results.hasNext()) {
				solution = results.next();

				try {
					String s = solution.get("o").toString();
					String p = solution.get("p").toString();

					if (s.equalsIgnoreCase(resource.replace("<", "").replace(">", ""))) {
						s = solution.get("s").toString();
						nodesReversed.add(s);
					}

					/*
					 * System.out.println("Nodes Reversed...");
					 * nodesReversed.stream().forEach(node ->
					 * System.out.println(node));
					 */
					Node subject = new Node(s, 0, 0, ALGORITHM);
					Node object = new Node(resource.replace("<", "").replace(">", ""), 0, 1, ALGORITHM);

					List<String> associations;
					if (!allAssociationsToAnEntity.containsKey(subject)) {
						associations = new ArrayList<String>();
						associations.add(p);
						allAssociationsToAnEntity.put(subject, associations);
					} else {
						associations = allAssociationsToAnEntity.get(subject);
						if (!associations.contains(p))
							associations.add(p);
						// see if this's required
						allAssociationsToAnEntity.replace(subject, associations);
					}
					// initialize the predicate frequency to zero
					if (!globalPredicateFrequency.containsKey(p))
						globalPredicateFrequency.put(p, Long.valueOf(0));

					// add edge between these resources
					if (!(g.containsEdge(p)))
						g.addEdge(p + g.getEdgeCount() + ":", subject, object);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpQuery.close();
		}

		List<Node> allInitialNodes = g.getVertices().stream().collect(toCollection(ArrayList::new));
		// Expand the grph by a hop
		DirectedSparseGraph<Node, String> graph = runBFS(allInitialNodes);

		// Beginning Resource selection
		// Run Pagerank
		List<Node> allRankedNodes = runPageRank(graph);

		// collect pagerank scores for the resources connected to the target
		List<Node> rankedInitialNodes = allRankedNodes.stream().filter(node -> allInitialNodes.contains(node))
				.collect(Collectors.toList());

		// check for the presence of backlink and filter out the ones that do
		// not have it
		rankedInitialNodes = checkForBacklink(rankedInitialNodes, resource);
		List<Node> nodesHavingBacklink = rankedInitialNodes.stream().filter(node -> node.getBacklink())
				.collect(Collectors.toList());

		// Apply LinkSum score for all Resources and sort them by it
		try {
			nodesHavingBacklink = applyLinkSum(rankedInitialNodes, nodesHavingBacklink);
		} catch (Exception e) {
			throw e;
		}
		nodesHavingBacklink.sort(Comparator.comparing(node -> node.getLinksumScore()));
		nodesHavingBacklink.stream().forEach(node -> System.out.println(
				node.getCandidateURI() + " : " + " PR: " + node.getPageRank() + " , LS: " + node.getLinksumScore()));
		// End of Resource Selection

		Map<Node, String> relevantAssocToAnEntity = null;
		// Beginning Predicate selection
		// select predicates by exclusivity
		if (this.predicateSelectionMode.equalsIgnoreCase("EXC"))
			relevantAssocToAnEntity = electPredicatesByExclusivity(allAssociationsToAnEntity);

		// select predicates by description
		if (this.predicateSelectionMode.equalsIgnoreCase("DSC"))
			relevantAssocToAnEntity = electPredicatesByDescription(allAssociationsToAnEntity);

		// select predicates by frequency
		if (this.predicateSelectionMode.equalsIgnoreCase("FRQ"))
			relevantAssocToAnEntity = electPredicatesByFrequency(allAssociationsToAnEntity, globalPredicateFrequency);

		/*
		 * model = createModel(model, relevantAssocToAnEntity, nodesReversed,
		 * resource, k); return model.listStatements().toList();
		 */
		return createModel(model, relevantAssocToAnEntity, nodesReversed, resource, k);
	}

	private LinkedList<Triple> createModel(Model model, Map<Node, String> relevantAssocToAnEntity,
			List<String> nodesReversed, String resource, Integer topk) {

		LinkedList<Triple> triplesRanked = new LinkedList<Triple>();
		Comparator<Entry<Node, String>> valueComparator = new Comparator<Entry<Node, String>>() {
			@Override
			public int compare(Entry<Node, String> e1, Entry<Node, String> e2) {
				Double v1 = e1.getKey().getLinksumScore();
				Double v2 = e2.getKey().getLinksumScore();
				return v2.compareTo(v1);
			}
		};

		List<Entry<Node, String>> listOfEntries = new ArrayList<Entry<Node, String>>(
				relevantAssocToAnEntity.entrySet());
		Collections.sort(listOfEntries, valueComparator);

		LinkedHashMap<Node, String> entityWithRelevantPredicateRanked = new LinkedHashMap<Node, String>();
		int top = (listOfEntries.size() < topk) ? listOfEntries.size() : topk;
		for (int index = 0; index < top; index++) {
			entityWithRelevantPredicateRanked.put(listOfEntries.get(index).getKey(),
					listOfEntries.get(index).getValue());
		}

		entityWithRelevantPredicateRanked.forEach((subject, predicate) -> {
			Triple triple;
			Property pred = model.createProperty(predicate);
			/*
			 * if (nodesReversed.contains(subject)) { Resource sub =
			 * model.createResource(resource.replace("<", "").replace(">", ""));
			 * String obj = subject.getCandidateURI(); model.add(sub, pred,
			 * model.createResource(obj));
			 * 
			 * triple = new Triple(NodeFactory.createURI(resource.replace("<",
			 * "").replace(">", "")), NodeFactory.createURI(predicate),
			 * NodeFactory.createURI(subject.getCandidateURI())); }
			 * 
			 * else { Resource sub =
			 * model.createResource(subject.getCandidateURI()); model.add(sub,
			 * pred, model.createResource(resource));
			 * 
			 * triple = new
			 * Triple(NodeFactory.createURI(subject.getCandidateURI()),
			 * NodeFactory.createURI(predicate),
			 * NodeFactory.createURI(resource.replace("<", "").replace(">",
			 * ""))); }
			 */
			Resource sub = model.createResource(resource.replace("<", "").replace(">", ""));
			String obj = subject.getCandidateURI();
			model.add(sub, pred, model.createResource(obj));
			triple = new Triple(NodeFactory.createURI(resource.replace("<", "").replace(">", "")),
					NodeFactory.createURI(predicate), NodeFactory.createURI(subject.getCandidateURI()));
			triplesRanked.add(triple);
		});

		return triplesRanked;
		// return model;
	}

	private Map<Node, String> electPredicatesByFrequency(Map<Node, List<String>> allAssociationsToAnEntity,
			Map<String, Long> globalPredicateFrequency) {

		// for every predicates in question,find its frequency of occurence in
		// the whole of Db
		globalPredicateFrequency.keySet().stream().forEach((predicate) -> {
			String query = "SELECT (str(COUNT(*))AS ?frequency)" + "WHERE {  ?subject predicate ?object" + "} ";
			query = query.replace("predicate", "<" + predicate + ">");
			Query sparqlQuery = QueryFactory.create(query, Syntax.syntaxARQ);
			QueryEngineHTTP httpQuery = new QueryEngineHTTP(endpoint, sparqlQuery);
			try {
				ResultSet resultSet = httpQuery.execSelect();
				QuerySolution solution = resultSet.next();
				Long frequency = Long.valueOf(solution.get("frequency").toString());
				globalPredicateFrequency.replace(predicate, frequency);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				httpQuery.close();
			}
		});

		Map<Node, String> relevantAssocToAnEntity = new HashMap<Node, String>();

		// place an entity with the most frequent predicate found in a group of
		// predicates linking that entity to target
		allAssociationsToAnEntity.forEach((subject, predicates) -> {
			System.out.println("subject: " + subject);
			Long val = 0L;
			String relPredicate = "";
			for (String predicate : predicates) {
				Long globalFreqVal = globalPredicateFrequency.get(predicate);
				val = (val > globalFreqVal) ? val : globalFreqVal;
				relPredicate = (val > globalFreqVal) ? relPredicate : predicate;
			}

			relevantAssocToAnEntity.put(subject, relPredicate);
		});
		return relevantAssocToAnEntity;
	}

	private Map<Node, String> electPredicatesByExclusivity(Map<Node, List<String>> allAssociationsToAnEntity) {

		Map<Node, String> relevantAssocToAnEntity = new HashMap<Node, String>();
		// for all associations,for every subject resource associated to the
		// target with multiple associations,find the best association by
		// exclusivity
		allAssociationsToAnEntity.forEach((subject, predicates) -> {

			if (predicates.size() > 1) {
				int n = 0;
				int m = 0;
				int exclusiveSum = 9999;
				String relPredicate = "";
				for (String predicate : predicates) {

					if (!(subject.getCandidateURI().contains("http")) || (subject.getCandidateURI().contains("^^http")))
						continue;

					String query = "SELECT (str(COUNT(*))AS ?freqN) ?freqM WHERE { " + "<" + subject.getCandidateURI()
							+ "> " + " <" + predicate + "> " + " ?object {" + "SELECT (str(COUNT(*))AS ?freqM) WHERE { "
							+ "?subject " + " <" + predicate + "> " + " <http://dbpedia.org/resource/" + entity + ">"
							+ "}" + "}" + "} GROUP BY ?freqM ";

					Query sparqlQuery = QueryFactory.create(query, Syntax.syntaxARQ);
					QueryEngineHTTP httpQuery = new QueryEngineHTTP(endpoint, sparqlQuery);
					try {
						ResultSet results = httpQuery.execSelect();
						QuerySolution solution = results.next();
						n = Integer.valueOf(solution.get("freqN").toString());
						m = Integer.valueOf(solution.get("freqM").toString());
						relPredicate = (exclusiveSum < (n + m) ? relPredicate : predicate);
						exclusiveSum = (exclusiveSum < (n + m) ? exclusiveSum : (n + m));
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						httpQuery.close();
					}

				}
				relevantAssocToAnEntity.put(subject, relPredicate);
			} else {
				// Add the only association from the subject resource to the
				// target
				relevantAssocToAnEntity.put(subject, predicates.get(0));
			}
		});

		return relevantAssocToAnEntity;
	}

	private Map<Node, String> electPredicatesByDescription(Map<Node, List<String>> allAssociationsToAnEntity) {

		/*
		 * SELECT (count(?label) as ?lc) (count(?range) as ?rc) (count(?domain)
		 * as ?dc) //OR distinct ?label ?range ?domain WHERE {
		 * 
		 * # # if you would like to restrict to 1 or more properties # values
		 * ?property {tto:sex tto:weight} # or # bind (tto:sex as ?property) #
		 * BIND(dbo:producer as ?property) ?property a rdf:Property. optional
		 * {?property rdfs:label ?label} optional {?property rdfs:range ?range}
		 * #kind of resource expected as the object of this prop optional
		 * {?property rdfs:domain ?domain} #kind of resource expected as the
		 * subject of this prop
		 * 
		 * }
		 */
		Map<Node, String> relevantAssocToAnEntity = new HashMap<Node, String>();
		// for all associations,for every subject resource associated to the
		// target with multiple associations,find the best association by
		// the description they have
		allAssociationsToAnEntity.forEach((subject, predicates) -> {

			if (predicates.size() > 1) {
				int totalDescriptionAvailable = 0;
				String relPredicate = "";
				for (String predicate : predicates) {

					if (!(subject.getCandidateURI().contains("http")) || (subject.getCandidateURI().contains("^^http")))
						continue;

					String query = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
							+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n"
							+ "SELECT (str(count(?label)) as ?lc) (str(count(?range)) as ?rc) (str(count(?domain)) as ?dc) WHERE { \n"
							+ "BIND(" + "<" + predicate + ">" + "as ?property) \n"
							+ "OPTIONAL {?property a rdf:Property.} \n" + "OPTIONAL {?property rdfs:label ?label.} \n"
							+ "OPTIONAL {?property rdfs:range ?range.} \n"
							+ "OPTIONAL {?property rdfs:domain ?domain.} \n }";

					Query sparqlQuery = QueryFactory.create(query, Syntax.syntaxARQ);
					QueryEngineHTTP httpQuery = new QueryEngineHTTP("http://dbpedia-live.openlinksw.com/sparql",
							sparqlQuery);
					try {
						ResultSet results = httpQuery.execSelect();
						QuerySolution solution = results.next();
						int lc = Integer.valueOf(solution.get("lc").toString());
						int rc = Integer.valueOf(solution.get("rc").toString());
						int dc = Integer.valueOf(solution.get("dc").toString());
						relPredicate = (totalDescriptionAvailable > (lc + rc + dc) ? relPredicate : predicate);
						totalDescriptionAvailable = (totalDescriptionAvailable > (lc + rc + dc)
								? totalDescriptionAvailable : (lc + rc + dc));
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						httpQuery.close();
					}

				}
				relevantAssocToAnEntity.put(subject, relPredicate);
			} else {
				// Add the only association from the subject resource to the
				// target
				relevantAssocToAnEntity.put(subject, predicates.get(0));
			}
		});

		return relevantAssocToAnEntity;

	}

	private List<Node> applyLinkSum(List<Node> rankedInitialNodes, List<Node> nodesHavingBacklink)
			throws NoSuchElementException {
		double max = rankedInitialNodes.stream().mapToDouble(node -> node.getPageRank()).max()
				.orElseThrow(NoSuchElementException::new);

		for (Node node : nodesHavingBacklink) {
			double linksumScore = (alpha * (node.getPageRank() / max)) + (1 - alpha);
			node.setLinksumScore(linksumScore);
		}

		return nodesHavingBacklink;
	}

	private List<Node> checkForBacklink(List<Node> rankedNodes, String targetEntity) {

		// check for the presence of backlink between the resources and the
		// target and set accordingly the boolean status
		for (Node node : rankedNodes) {
			if (!(node.getCandidateURI().contains("http")) || (node.getCandidateURI().contains("^^http")))
				continue;
			String query = "PREFIX dbo: <http://dbpedia.org/ontology/> \n" + "ASK "
					+ "FROM <http://dbpedia.org/page_links>"
					+ "{ source dbo:wikiPageWikiLink|^dbo:wikiPageWikiLink target" + "}";
			query = query.replace("source", "<" + node.getCandidateURI() + ">").replace("target", targetEntity);
			Query sparqlQuery = QueryFactory.create(query, Syntax.syntaxARQ);
			QueryEngineHTTP httpQuery = new QueryEngineHTTP(endpoint, sparqlQuery);
			try {
				Boolean truthValue = httpQuery.execAsk();
				if (truthValue)
					node.setBacklink(truthValue);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				httpQuery.close();
			}
		}
		return rankedNodes;
	}

	private List<Node> runPageRank(DirectedSparseGraph<Node, String> g) {

		// run pagerank,sort the nodes according to its score and return it
		PageRank pr = new PageRank();
		pr.runPr(g, 100, 0.001);

		ArrayList<Node> orderedList = new ArrayList<Node>();
		orderedList.addAll(g.getVertices());
		Collections.sort(orderedList);

		return orderedList;
	}

	private DirectedSparseGraph<Node, String> runBFS(List<Node> highRankNodes) {
		Integer maxDepth = 2;
		String edgeType = DB_ONTOLOGY;
		String nodeType = DB_RESOURCE;
		String edgeLabel = DB_LABEL;
		DirectedSparseGraph<Node, String> graph = new DirectedSparseGraph<Node, String>();
		for (Node node : highRankNodes) {
			graph.addVertex(node);
		}
		BreadthFirstSearch bfs = null;
		try {
			bfs = new BreadthFirstSearch(new TripleIndex(), ALGORITHM);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			graph = bfs.run(maxDepth, graph, edgeType, nodeType, edgeLabel);
		} catch (UnsupportedEncodingException e) {
		} catch (IOException e) {
			e.printStackTrace();
		}

		return graph;
	}

}