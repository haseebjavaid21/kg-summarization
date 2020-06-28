package org.dice.kgsmrstn.selector;

import edu.uci.ics.jung.graph.DirectedSparseGraph;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.ToIntFunction;

import javax.security.auth.message.callback.PrivateKeyCallback.IssuerSerialNumRequest;

import org.apache.http.util.TextUtils;
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
import org.dice.kgsmrstn.graph.*;
/**
 *
 * @author Haseeb Javaid
 */
public abstract class AbstractSummarizationSelector1 implements TripleSelector{

	private static final String DB_RESOURCE = "http://dbpedia.org/resource/";
	private static final String DB_ONTOLOGY = "http://dbpedia.org/ontology/";
	private static final String DB_Label = "http://www.w3.org/2000/01/rdf-schema#label";

	private static final String ALGORITHM = "pagerank";

	private org.slf4j.Logger log = LoggerFactory.getLogger(AbstractSummarizationSelector.class);

	private String endpoint;
	private DirectedSparseGraph<Node, String> g = new DirectedSparseGraph<Node, String>();
	HashMap<Node, List<Node>> adjNodes = new HashMap<>();
	HashMap<Integer, List<Node>> component = new HashMap<>();

	public AbstractSummarizationSelector1(Set<String> targetClasses, String endpoint, String graph) {
		this.endpoint = endpoint;
	}

	public AbstractSummarizationSelector1(Set<String> targetClasses, String endpoint, String graph,
			boolean useSymmetricCbd) {
		this.endpoint = endpoint;
	}

	protected List<Statement> getResources(Set<String> classes, String clazz,int topk) {

		//		String query = "";
		//
		//		switch (clazz) {
		//		case "person": query = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +  "PREFIX dbr: <http://dbpedia.org/resource/>\n" + "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n"
		//				+ "SELECT DISTINCT  * WHERE {\n" + "?s  rdf:type    foaf:Person ;\n"
		//				+ "    ?p                    ?o\n" 
		//				+"FILTER  regex(?o, 'http://xmlns.com/foaf/0.1/')."
		//				+"} LIMIT 10000";
		//			
		//			break;
		//			
		//		case "organisation":	 query = "PREFIX  dbo:  <http://dbpedia.org/ontology/>" 
		//				+ "PREFIX  dbr:  <http://dbpedia.org/resource/>"
		//				 +"SELECT DISTINCT  * WHERE\n"
		//				+ "{ ?s  a dbo:Organisation ;" 
		//				+"       ?p   ?o."
		//				+"} LIMIT 10000";
		//		break;
		//
		//			case "country":  query =  "select distinct ?s ?p ?o\n"
		//					+ "where { ?s a <http://dbpedia.org/class/yago/WikicatMemberStatesOfTheUnitedNations>;"
		//					+"?p ?o. "
		//					+"FILTER (lang(?o) = 'en')} order by ?o";
		//        break;
		//		default:
		//			break;
		//		}
		//		
		//		//Query for country
		//
		//
		//		//Query for person
		//		
		//
		//
		//
		//
		//		//Query for organisation
		//		
		//
		//
		//
		//
		//
		//
		//		log.info("Query " + query);
		//		Query sparqlQuery = QueryFactory.create(query, Syntax.syntaxARQ);
		//
		//		QueryEngineHTTP httpQuery = new QueryEngineHTTP(endpoint, sparqlQuery);
		//		List<Node> allNodesRanked = new ArrayList<Node>();
		//		try {
		//			ResultSet results = httpQuery.execSelect();
		//			QuerySolution solution;
		//			while (results.hasNext()) {
		//				solution = results.next();
		//				// get the value of the variables in the select clause
		//				try {
		//					String s = solution.get("s").toString();
		//					String p = solution.get("p").toString();
		//					String o = solution.get("o").toString();
		//
		//					Node curr_s = new Node(s, 0, 0, ALGORITHM);
		//					Node curr_o = new Node(o, 0, 1, ALGORITHM);
		//					if (!(g.containsEdge(p) && g.containsVertex(curr_s)))
		//						g.addEdge(g.getEdgeCount() + ";" + p, curr_s, curr_o);
		//				} catch (Exception e) {
		//					e.printStackTrace();
		//				}
		//
		//			}
		//		} finally {
		//			httpQuery.close();
		//		}



		// run Page Rank to get the top entities of type 'Person'
		//allNodesRanked = runPageRank(g);

		Model m = ModelFactory.createDefaultModel();
		m.read("persondata_en3.ttl");

		Iterator<Statement> st = m.listStatements();

		System.out.println("model obtained");

		int count = 0;

		while(st.hasNext()) {
			Statement s = st.next();

			String subject = s.getSubject().toString();
			String predicate = s.getPredicate().toString();
			String object = s.getObject().toString();

			Node curr_s;
			try {
				curr_s = new Node(object, 0, 0, ALGORITHM);
				Node curr_o = new Node(subject, 0, 1, ALGORITHM);
				if (!(g.containsEdge(predicate) && g.containsVertex(curr_s))) {
					g.addEdge(g.getEdgeCount() + ";" + predicate, curr_s, curr_o);
					if(adjNodes.containsKey(curr_o)) {
						adjNodes.get(curr_o).add(curr_s);
					}
					else {
						adjNodes.put(curr_o, new ArrayList<>());
						adjNodes.get(curr_o).add(curr_s);
					}
				}

              count =count + 2;
              System.out.println(count);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}




		}

		System.out.println("graph obtained");

		List<Node> allNodesRanked = new ArrayList<>();
		allNodesRanked.addAll(g.getVertices());


		List<Node> highRankNodes = new ArrayList<Node>();
		for (Node node : allNodesRanked) {
			if ((node.getLevel() == 1 && !(highRankNodes.contains(node)))) {
				highRankNodes.add(node);
			}


		}
		//get 'Top-50' nodes
		//highRankNodes = highRankNodes.subList(0, 50);

		//run BFS
		//	DirectedSparseGraph<Node, String> graph = runBFS(highRankNodes);

		//SALSA
		//		for (Object n : g.getVertices()) {
		//			Node node = (Node) n;
		//			
		////			if(highRankNodes.contains(node)) {
		//			//System.out.println("Node is "+node);
		//			adjNodes.put(node, new ArrayList<>());
		//			for (Object n1 : g.getSuccessors(node)) {
		//				Node neighbourNodes = (Node) n1;
		//				adjNodes.get(node).add(neighbourNodes);
		//			}
		////		}
		//		}

		ComponentId comp = new ComponentId();

        int topP = 0;
		for (Node node : adjNodes.keySet()) {
			
        	topP = topP + adjNodes.get(node).size();
		}
		
		topP = (int) topP/adjNodes.size();
		
		System.out.println("Average relations is "+topP);



		component = comp.findComponets(adjNodes, g);



		//Finally run the Page Rank algorithm to the entities after their BFS expansion
		List<Node> topNodes = runSalsa(g, component);
		Collections.reverse(topNodes);
		int p = 0;
		List<Node> temp = new ArrayList<>();
		for (Node node : topNodes) {

			if(highRankNodes.contains(node)) {
				temp.add(node);
				System.out.println("Weight is "+node+" "+node.getAuthorityWeight());
				p=p+1;
			}	

			if(p>10)
				break;

		}


		Model model = ModelFactory.createDefaultModel();
		//form the triples of all the top nodes 

		for(Node node: temp) {


			Collection<Node> neighbourNodes1 = g.getNeighbors(node);
			List<Node> neighbourNodes = new ArrayList<>();
			neighbourNodes.addAll(neighbourNodes1);
			Collections.sort(neighbourNodes, new Comparator<Node>() {

				@Override
				public int compare(Node o1, Node o2) {
					// TODO Auto-generated method stub
					return Double.compare(o1.getHubWeight(), o2.getHubWeight());
				}
			});
			
			neighbourNodes.subList(0, topP-2);
			for(Node succesorNode : neighbourNodes ) {

				String sub = node.getCandidateURI();
				String pred = g.findEdge(succesorNode, node);
				String object = succesorNode.getCandidateURI();

				if(pred!= null){
					Resource subject = model.createResource(sub);
					Property predicate = model.createProperty(pred.split(";")[1]);
					model.add(subject, predicate, object);
				}
			}





		}

		return sortStatements(model.listStatements());
	}


	private List<Node> runSalsa(DirectedSparseGraph<Node, String> g, HashMap<Integer, List<Node>> component) {

		SALSA sal = new SALSA();
		try {
			sal.runSALSA(g, component);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		ArrayList<Node> orderedList = new ArrayList<Node>();
		orderedList.addAll(g.getVertices());
		Collections.sort(orderedList, new Comparator<Node>() {

			@Override
			public int compare(Node o1, Node o2) {
				// TODO Auto-generated method stub
				return Double.compare(o1.getAuthorityWeight(), o2.getAuthorityWeight());
			}
		});




		return orderedList;

	}

//	private DirectedSparseGraph<Node, String> runBFS(List<Node> highRankNodes) {
//		Integer maxDepth = 2;
//		String edgeType = DB_ONTOLOGY;
//		String nodeType = DB_RESOURCE;
//		String edgeLabel = DB_Label;
//		DirectedSparseGraph<Node, String> graph = new DirectedSparseGraph<Node, String>();
//		for (Node node: highRankNodes) {
//			graph.addVertex(node);	
//		}
//		BreadthFirstSearch bfs = null;
//		try {
//			bfs = new BreadthFirstSearch(new TripleIndex(), ALGORITHM);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		try {
//			graph = bfs.run(maxDepth, graph, edgeType, nodeType, edgeLabel);
//		} catch (UnsupportedEncodingException e) {
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		return graph;
//	}

//	private List<Node> runPageRank(DirectedSparseGraph<Node, String> g) {
//		PageRank pr = new PageRank();
//		pr.runPr(g, 100, 0.001);
//
//		ArrayList<Node> orderedList = new ArrayList<Node>();
//		orderedList.addAll(g.getVertices());
//		Collections.sort(orderedList);
//
//		return orderedList;
//
//	}



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
