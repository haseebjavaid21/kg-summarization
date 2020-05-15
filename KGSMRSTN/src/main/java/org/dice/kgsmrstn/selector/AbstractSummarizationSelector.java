package org.dice.kgsmrstn.selector;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
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
import org.dice.kgsmrstn.graph.HITSAlgorithm;
import org.dice.kgsmrstn.graph.Node;
import org.dice.kgsmrstn.graph.PageRank;
import org.dice.kgsmrstn.util.TripleIndex;
import org.slf4j.LoggerFactory;

import com.github.andrewoma.dexx.collection.HashMap;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFLanguages;

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

	HashMap<Node, List<Node>> adjNodes = new HashMap<>();

	public AbstractSummarizationSelector(Set<String> targetClasses, String endpoint, String graph) {
		this.endpoint = endpoint;
	}

	public AbstractSummarizationSelector(Set<String> targetClasses, String endpoint, String graph,
			boolean useSymmetricCbd) {
		this.endpoint = endpoint;
	}

	protected List<Statement> getResources(Set<String> classes, String clazz, int topk) {

		this.readFromTTL("D:\\Project Data\\persondata_en.ttl\\persondata_en4.ttl");
		
		try {
			HITSAlgorithm ht = new HITSAlgorithm();
			ht.runHits(g, 10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<Node> processedHighRankNodes = this.getHighRankNdes();
		System.out.println("Finished");
		return sortStatements(this.generateList(processedHighRankNodes).listStatements());
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
	
	protected List<Node> getHighRankNdes() {
		List<Node> allNodesRanked = new ArrayList<Node>();
		List<Node> NodeIteration = new ArrayList<>();
		
		allNodesRanked.addAll(g.getVertices());

		List<Node> highRankNodes = new ArrayList<Node>();
		for (Node node : allNodesRanked) {
			if (node.getLevel() == 0 && !highRankNodes.contains(node)) {
				highRankNodes.add(node);
			}
		}
		
		int p = 0;
		for (Node node_iter : this.sortAndReverse(g)) {
			if (highRankNodes.contains(node_iter)) {
				NodeIteration.add(node_iter);
				System.out.println("Weight is " + node_iter + " " + node_iter.getAuthorityWeight());
				p = p + 1;
			}

			if (p > 10)
				break;

		}
		return NodeIteration;
		
	}
	
	protected void readFromTTL(String ttl_path) {
		Model m = ModelFactory.createDefaultModel();
		// read into the model.
		m.read(ttl_path);
		StmtIterator st = m.listStatements();
		while (st.hasNext()) {
			// get the value of the variables in the select clause
			try {
				Statement et = st.next();
				String s = et.getSubject().toString();
				String p = et.getPredicate().toString();
				String o = et.getObject().toString();

				Node curr_s = new Node(s, 0, 0, ALGORITHM);
				Node curr_o = new Node(o, 0, 1, ALGORITHM);

				if (!(g.containsEdge(p) && g.containsVertex(curr_s)))
					g.addEdge(g.getEdgeCount() + ";" + p, curr_s, curr_o);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	protected Model generateList(List<Node> temp) {
		Model model = ModelFactory.createDefaultModel();
		// form the triples of all the top nodes

		for (Node nodetemp : temp) {
			Collection<Node> neighbourNodes1 = g.getNeighbors(nodetemp);
			List<Node> neighbourNodes = new ArrayList<>();
			neighbourNodes.addAll(neighbourNodes1);
			Collections.sort(neighbourNodes, new Comparator<Node>() {

				@Override
				public int compare(Node o1, Node o2) {
					// TODO Auto-generated method stub
					return Double.compare(o1.getHubWeight(), o2.getHubWeight());
				}
			});

			for (Node succesorNode : neighbourNodes) {

				String sub = nodetemp.getCandidateURI();
				String pred = g.findEdge(nodetemp, succesorNode);
				String object = succesorNode.getCandidateURI();

				if (pred != null) {
					Resource subject = model.createResource(sub);
					Property predicate = model.createProperty(pred.split(";")[1]);
					model.add(subject, predicate, object);
				}
			}
		}
		return model;
	}
	
	protected ArrayList<Node> sortAndReverse(DirectedSparseGraph<Node, String>  g) {
		ArrayList<Node> orderedList = new ArrayList<Node>();
		orderedList.addAll(g.getVertices());
		Collections.sort(orderedList, new Comparator<Node>() {

			public int compare(Node o1, Node o2) {
				// TODO Auto-generated method stub
				return Double.compare(o1.getAuthorityWeight(), o2.getAuthorityWeight());
			}
		});

		Collections.reverse(orderedList);
		return orderedList;
		
	}
}
