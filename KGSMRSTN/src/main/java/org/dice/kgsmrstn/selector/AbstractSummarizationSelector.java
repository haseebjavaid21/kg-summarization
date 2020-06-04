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
import java.util.function.Consumer;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

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
public abstract class AbstractSummarizationSelector implements TripleSelector{

	private static final String DB_RESOURCE = "http://dbpedia.org/resource/";
	private static final String DB_ONTOLOGY = "http://dbpedia.org/ontology/";
	private static final String DB_Label = "http://www.w3.org/2000/01/rdf-schema#label";

	private static final String ALGORITHM = "pagerank";

	private org.slf4j.Logger log = LoggerFactory.getLogger(AbstractSummarizationSelector.class);

	private String endpoint;
	private DirectedSparseGraph<Node, String> g = new DirectedSparseGraph<Node, String>();
	HashMap<Node, List<Node>> adjNodes = new HashMap<>();
	HashMap<Integer, List<Node>> component = new HashMap<>();

	public AbstractSummarizationSelector(Set<String> targetClasses, String endpoint, String graph) {
		this.endpoint = endpoint;
	}

	public AbstractSummarizationSelector(Set<String> targetClasses, String endpoint, String graph,
			boolean useSymmetricCbd) {
		this.endpoint = endpoint;
	}

	protected List<Statement> getResources(Set<String> classes, String clazz,int topk) {

		


		// run Page Rank to get the top entities of type 'Person'
		//allNodesRanked = runPageRank(g);

		Model m = ModelFactory.createDefaultModel();
		m.read("mappingbased_objects_en.ttl");

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
		
		highRankNodes = allNodesRanked.parallelStream().filter(node -> node.getLevel() == 1).collect(Collectors.toList());
		
		
		


		ComponentId comp = new ComponentId();

        int topP = 0;
		
		
		topP = (int) g.getEdgeCount()/adjNodes.size();
		
		System.out.println("Average relations is "+topP);



		component = comp.findComponets(adjNodes, g);

System.out.println("Components created");

		//Finally run the Page Rank algorithm to the entities after their BFS expansion
		List<Node> topNodes = runSalsa(g, component);
		Collections.reverse(topNodes);
		int p = 0;
		List<Node> temp = new ArrayList<>();
		
		temp = topNodes.parallelStream().filter(node -> node.getLevel()==1).collect(Collectors.toList());
		double temp_weight = 0.0;
//		for(Node node: temp) {
//			temp_weight = temp_weight + node.getAuthorityWeight();
//		}
		
		//System.out.println("Mean computed traditionally "+temp_weight/temp.size());
		double total_weight = topNodes.parallelStream().filter(node ->node.getLevel()==1).map(node -> node.getAuthorityWeight()).reduce(0.0, Double::sum);
		double meanNodes = total_weight/temp.size();
		System.out.println("Mean of "+temp.size()+" entities is "+meanNodes);
		
		List<Node> temp1 = temp.parallelStream().filter(node -> node.getAuthorityWeight() >= (meanNodes)).collect(Collectors.toList());
		
		System.out.println("final number of entities is "+temp1.size());
		
		//temp = temp.subList(0, 1000);


		Model model = ModelFactory.createDefaultModel();
		//form the triples of all the top nodes 

		for(Node node: temp1) {

            //System.out.println("Node "+node+" Aw: "+node.getAuthorityWeight());
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
			
			//neighbourNodes.subList(0, topP-2);
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

		System.out.println("final number of entities is "+temp1.size());
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
