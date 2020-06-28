package org.dice.kgsmrstn.selector;

import edu.uci.ics.jung.graph.DirectedSparseGraph;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.Syntax;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;
import org.dice.kgsmrstn.graph.BreadthFirstSearch;
import org.dice.kgsmrstn.graph.Node;
import org.dice.kgsmrstn.graph.PageRank;
import org.dice.kgsmrstn.util.TripleIndex;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Haseeb Javaid
 */
public abstract class AbstractSummarizationSelector implements TripleSelector {

	private static final String DB_RESOURCE = "http://dbpedia.org/resource/";
	private static final String DB_ONTOLOGY = "http://dbpedia.org/ontology/";
	private static final String DB_Label = "http://www.w3.org/2000/01/rdf-schema#label";

	private static final String ALGORITHM = "pagerank";

	private org.slf4j.Logger log = LoggerFactory.getLogger(AbstractSummarizationSelector.class);

	private String endpoint;
	private DirectedSparseGraph<Node, String> g = new DirectedSparseGraph<Node, String>();

	public AbstractSummarizationSelector(Set<String> targetClasses, String endpoint, String graph) {
		this.endpoint = endpoint;
	}

	public AbstractSummarizationSelector(Set<String> targetClasses, String endpoint, String graph,
			boolean useSymmetricCbd) {
		this.endpoint = endpoint;
	}

	protected List<Statement> getResources(Set<String> classes, String clazz, int topk) {

		String query = "";

		switch (clazz) {
		case "person":
			query = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
					+ "PREFIX dbr: <http://dbpedia.org/resource/>\n" + "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n"
					+ "SELECT DISTINCT  * WHERE {\n" + "?s  rdf:type    foaf:Person ;\n"
					+ "    ?p                    ?o\n" + "FILTER  regex(?o, 'http://xmlns.com/foaf/0.1/')."
					+ "} LIMIT 10000";

			break;

		case "organisation":
			query = "PREFIX  dbo:  <http://dbpedia.org/ontology/>" + "PREFIX  dbr:  <http://dbpedia.org/resource/>"
					+ "SELECT DISTINCT  * WHERE\n" + "{ ?s  a dbo:Organisation ;" + "       ?p   ?o." + "} LIMIT 10000";
			break;

		case "country":
			query = "select distinct ?s ?p ?o\n"
					+ "where { ?s a <http://dbpedia.org/class/yago/WikicatMemberStatesOfTheUnitedNations>;" + "?p ?o. "
					+ "FILTER (lang(?o) = 'en')} order by ?o";
			break;
		default:
			break;
		}

		log.info("Query " + query);
		Query sparqlQuery = QueryFactory.create(query, Syntax.syntaxARQ);

		QueryEngineHTTP httpQuery = new QueryEngineHTTP(endpoint, sparqlQuery);
		List<Node> allNodesRanked = new ArrayList<Node>();
		try {
			ResultSet results = httpQuery.execSelect();
			QuerySolution solution;
			while (results.hasNext()) {
				solution = results.next();
				// get the value of the variables in the select clause
				try {
					String s = solution.get("s").toString();
					String p = solution.get("p").toString();
					String o = solution.get("o").toString();

					Node curr_s = new Node(s, 0, 0, ALGORITHM);
					Node curr_o = new Node(o, 0, 1, ALGORITHM);
					if (!(g.containsEdge(p) && g.containsVertex(curr_s)))
						g.addEdge(g.getEdgeCount() + ";" + p, curr_s, curr_o);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		} finally {
			httpQuery.close();
		}

		// run Page Rank to get the top entities of type 'Person'
		allNodesRanked = runPageRank(g);

		List<Node> highRankNodes = new ArrayList<Node>();
		for (Node node : allNodesRanked) {
			if (node.getLevel() == 0) {
				highRankNodes.add(node);
			}

		}
		// get 'Top-50' nodes
		highRankNodes = highRankNodes.subList(0, topk);

		// run BFS
		DirectedSparseGraph<Node, String> graph = runBFS(highRankNodes);

		// Finally run the Page Rank algorithm to the entities after their BFS
		// expansion
		List<Node> topNodes = runPageRank(graph);
		Model model = ModelFactory.createDefaultModel();
		// form the triples of all the top nodes
		for (Node node : topNodes) {
			if (highRankNodes.contains(node)) {
				Collection<Node> neighbourNodes = graph.getNeighbors(node);
				for (Node succesorNode : neighbourNodes) {

					String sub = node.getCandidateURI();
					String pred = graph.findEdge(node, succesorNode);
					String object = succesorNode.getCandidateURI();

					if (pred != null) {
						Resource subject = model.createResource(sub);
						Property predicate = model.createProperty(pred.split(";")[1]);
						model.add(subject, predicate, object);
					}
				}
			}
		}

		return sortStatements(model.listStatements());
	}

	private List<Node> runPageRank(DirectedSparseGraph<Node, String> g) {
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
		String edgeLabel = DB_Label;
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

	protected List<Statement> sortStatements(StmtIterator stmtIterator) {
		List<Statement> result = new ArrayList<Statement>();
		while (stmtIterator.hasNext()) {
			result.add(stmtIterator.next());
		}
		Collections.sort(result, new StatementComparator());
		return result;
	}

	protected List<Statement> sortStatements(Set<Statement> statements) {
		List<Statement> result = new ArrayList<Statement>(statements);
		Collections.sort(result, new StatementComparator());
		return result;
	}
}
