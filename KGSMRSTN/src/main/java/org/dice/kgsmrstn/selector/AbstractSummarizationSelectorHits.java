package org.dice.kgsmrstn.selector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.dice.kgsmrstn.graph.HITSAlgorithm;
import org.dice.kgsmrstn.graph.Node;
import org.dice.kgsmrstn.util.StatementComparator;
import org.slf4j.LoggerFactory;

import com.github.andrewoma.dexx.collection.HashMap;

import edu.uci.ics.jung.graph.DirectedSparseGraph;

/**
 *
 * @author Haseeb Javaid
 */
public class AbstractSummarizationSelectorHits {
	private static final String ALGORITHM = "pagerank";
	private org.slf4j.Logger log = LoggerFactory.getLogger(AbstractSummarizationSelectorHits.class);

	private String endpoint;
	private DirectedSparseGraph<Node, String> g = new DirectedSparseGraph<Node, String>();

	HashMap<Node, List<Node>> adjNodes = new HashMap<>();

	public AbstractSummarizationSelectorHits(String endpoint, String graph) {
		this.endpoint = endpoint;
	}

	public List<Statement> getResources() {

		this.readFromTTL("D:\\Project Data\\persondata_en4.ttl");

		try {
			HITSAlgorithm ht = new HITSAlgorithm();
			ht.runHits(g, 10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<Node> processedHighRankNodes = this.getHighRankNdes();
		System.out.println("Finished :" + processedHighRankNodes.size());
		return sortStatements(this.generateList(processedHighRankNodes).listStatements());
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
		List<Node> reversedNodeList = new ArrayList<Node>();
		List<Node> NodeIteration = new ArrayList<>();

		reversedNodeList = this.sortAndReverse(g);
		NodeIteration = reversedNodeList.parallelStream().filter(node -> node.getLevel() == 1)
				.collect(Collectors.toList());
		double temp_weight = 0.0;
		double total_weight = reversedNodeList.parallelStream().filter(node -> node.getLevel() == 1)
				.map(node -> node.getAuthorityWeight()).reduce(0.0, Double::sum);
		double meanNodes = total_weight / NodeIteration.size();
		System.out.println("Mean of " + NodeIteration.size() + " entities is " + meanNodes);

		List<Node> filteredList = NodeIteration.parallelStream()
				.filter(node -> node.getAuthorityWeight() >= (meanNodes)).collect(Collectors.toList());

		System.out.println("final number of entities is " + filteredList.size());
		return filteredList;
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

				Node curr_s = new Node(o, 0, 0, ALGORITHM);
				Node curr_o = new Node(s, 0, 1, ALGORITHM);

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
				String pred = g.findEdge(succesorNode, nodetemp);
				String object = succesorNode.getCandidateURI();

				if (pred != null) {
					Resource subject = model.createResource(sub);
					Property predicate = model.createProperty(pred.split(";")[1]);
					model.add(subject, predicate, object);
				}
			}
		}
		System.out.println("Final Size : " + model.size());
		return model;
	}

	protected ArrayList<Node> sortAndReverse(DirectedSparseGraph<Node, String> g) {
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