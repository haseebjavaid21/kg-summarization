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
public abstract class AbstractSummarizationSelector implements TripleSelector{

	private static final String DB_RESOURCE = "http://dbpedia.org/resource/";
	private static final String DB_ONTOLOGY = "http://dbpedia.org/ontology/";

	private static final String ALGORITHM = "pagerank";
	
	//private List<String> topEntitiesBeforeExpansion = new ArrayList<String>();
	private List<String> topEntitiesBeforeExpansion = new ArrayList<String>();
	public List<Model> triples = new ArrayList<Model>();

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

	protected List<Statement> getResources(Set<String> classes) {
		String query =  "select distinct ?s\n"+
	 "where { ?s a <http://dbpedia.org/class/yago/WikicatMemberStatesOfTheUnitedNations>;" 
				+ " ?p ?o.}";
		// }
		log.info("Query " + query);
		Query sparqlQuery = QueryFactory.create(query, Syntax.syntaxARQ);

		QueryEngineHTTP httpQuery = new QueryEngineHTTP(endpoint, sparqlQuery);
		new ArrayList<>();
			Model m = ModelFactory.createDefaultModel();
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
		List<Node> highRankNodes = runPageRank(g);
		
		for(Node node:highRankNodes) {
			topEntitiesBeforeExpansion.add(node.getCandidateURI());
		}
		//run BFS
		DirectedSparseGraph<Node, String> graph = runBFS(highRankNodes);
		
		//Finally run the Page Rank algorithm to the entities after their BFS expansion
		List<Node> topNodes = runPageRank(graph);
		Model model = ModelFactory.createDefaultModel();
		for(Node node: topNodes) {
			
			Collection<Node> neighbourNodes = graph.getNeighbors(node);
			for(Node succesorNode : neighbourNodes ) {
				
				String sub = node.getCandidateURI();
				String pred = graph.findEdge(node, succesorNode);
				String object = succesorNode.getCandidateURI();
				
				Resource subject = model.createResource(sub);
				//TODO NPE
				Property predicate = model.createProperty(pred.split(";")[1]);

				model.add(subject, predicate, object);
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

		List<Node> highRankNodes = new ArrayList<Node>();
		for (Node node : g.getVertices()) {
			if (node.getCandidateURI().contains(DB_RESOURCE)) {
				highRankNodes.add(node);
			}

		}
		highRankNodes = highRankNodes.subList(0, 50);
		return highRankNodes;

	}
	
	private DirectedSparseGraph<Node, String> runBFS(List<Node> highRankNodes) {
    	Integer maxDepth = 2;
    	String edgeType = DB_ONTOLOGY;
    	String nodeType = DB_RESOURCE;
    	DirectedSparseGraph<Node, String> graph = new DirectedSparseGraph<Node, String>();
    	for (Node node: highRankNodes) {
		graph.addVertex(node);	
		}
    	BreadthFirstSearch bfs = null;
		try {
			bfs = new BreadthFirstSearch(new TripleIndex(), ALGORITHM);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			graph = bfs.run(maxDepth, graph, edgeType, nodeType);
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
