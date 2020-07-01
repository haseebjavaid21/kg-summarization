package org.dice.kgsmrstn.selector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.jena.graph.Triple;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.dice.kgsmrstn.graph.ComponentId;
import org.dice.kgsmrstn.graph.Node;
import org.dice.kgsmrstn.graph.NodeURI;
import org.dice.kgsmrstn.graph.SALSA;
import org.dice.kgsmrstn.util.StatementComparator;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import edu.uci.ics.jung.graph.DirectedSparseGraph;

/**
 *
 * @author Haseeb Javaid
 */
public class AbstractSummarizationSelectorSalsa {

	private static final String ALGORITHM = "pagerank";

	private org.slf4j.Logger log = LoggerFactory.getLogger(AbstractSummarizationSelectorSalsa.class);

	private String endpoint;
	private DirectedSparseGraph<Node, String> g = new DirectedSparseGraph<Node, String>();
	HashMap<Node, List<Node>> adjNodes = new HashMap<>();
	HashMap<Integer, List<Node>> component = new HashMap<>();

	public AbstractSummarizationSelectorSalsa(String endpoint, String graph) {
		this.endpoint = endpoint;
	}

	public List<Statement> getResources(String filePath) {

		Model m = ModelFactory.createDefaultModel();
		//m.read("D:\\Project Data\\persondata_en4.ttl");
		m.read(filePath);
		double total_weight;

		Iterator<Statement> st = m.listStatements();

		System.out.println("model obtained");

		int count = 0;

		while (st.hasNext()) {
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
					if (adjNodes.containsKey(curr_o)) {
						adjNodes.get(curr_o).add(curr_s);
					} else {
						adjNodes.put(curr_o, new ArrayList<>());
						adjNodes.get(curr_o).add(curr_s);
					}
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		System.out.println("graph obtained");

		List<Node> allNodesRanked = new ArrayList<>();
		allNodesRanked.addAll(g.getVertices());

		ComponentId comp = new ComponentId();

		component = comp.findComponets1(adjNodes, g);

		adjNodes.clear();

		// Finally run the Page Rank algorithm to the entities after their BFS
		// expansion
		List<Node> topNodes = runSalsa(g, component);
		component.clear();
		Collections.reverse(topNodes);
		System.gc();
		List<Node> temp = new ArrayList<>();

		temp = topNodes.parallelStream().filter(node -> node.getLevel() == 1).collect(Collectors.toList());

		total_weight = topNodes.parallelStream().filter(node -> node.getLevel() == 1)
				.map(node -> node.getAuthorityWeight()).reduce(0.0, Double::sum);

		double mean1 = total_weight / temp.size();

		System.out.println("Mean of " + temp.size() + " entities is " + mean1);

		List<Node> temp1 = temp.parallelStream().filter(node -> node.getAuthorityWeight() >= mean1)
				.collect(Collectors.toList());

		List<Statement> tempstats = new ArrayList<>();
		System.out.println("number of final entities" + temp1.size());

		Model model = ModelFactory.createDefaultModel();
		// form the triples of all the top nodes

		for (Node node : temp1) {

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

			// neighbourNodes.subList(0, topP);
			for (Node succesorNode : neighbourNodes) {

				String sub = node.getCandidateURI();
				String pred = g.findEdge(succesorNode, node);
				String object = succesorNode.getCandidateURI();

				NodeURI sub1 = new NodeURI(sub);
				NodeURI pred1 = new NodeURI(pred);
				NodeURI obj1 = new NodeURI(object);

				Triple t = new Triple(sub1, pred1, obj1);

				if (pred != null) {
					Resource subject = model.createResource(sub);
					Property predicate = model.createProperty(pred.split(";")[1]);

					model.add(subject, predicate, object);
					tempstats.add(model.asStatement(t));

				}
			}

		}

		Collections.sort(tempstats, new StatementComparator());

		return tempstats;
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
