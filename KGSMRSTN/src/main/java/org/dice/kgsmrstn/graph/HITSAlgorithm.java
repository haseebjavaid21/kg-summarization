package org.dice.kgsmrstn.graph;

import java.util.HashSet;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;

public class HITSAlgorithm {
	private HashSet<String> restrictedEdges;

	/**
	 * 
	 * HITS algorithm that will execute on the provided graph and set authority and
	 * hub score for each node.
	 * 
	 * @param k
	 * 
	 * @param Graph , Iterations
	 * @return Graph
	 * @throws InterruptedException
	 */
	public void runHits(DirectedSparseGraph<Node, String> g, int k)
			throws InterruptedException {
		if (restrictedEdges != null) {
			HashSet<Object> toBeRemoved = new HashSet<Object>();
			for (Object edge : g.getEdges()) {
				String edgeString = (String) edge;
				for (String restrict : restrictedEdges) {
					if (edgeString.contains(restrict)) {
						toBeRemoved.add(edge);
					}
				}
			}
			/**
			 * Remove any edges that have been marked as not required
			 */
			for (Object edge : toBeRemoved) {
				g.removeEdge((String) edge);
			}
		}

		/**
		 * Calculation for score and assigning them to each node
		 */
		Node n;
		for (int iter = 0; iter < k; iter++) {
			for (Object o : g.getVertices()) {
				n = (Node) o;
				double x = 0;
				for (Object inc : g.getPredecessors(n)) {
					x += ((Node) inc).getHubWeight();
				}
				double y = 0;
				for (Object inc : g.getSuccessors(n)) {
					y += ((Node) inc).getAuthorityWeight();
				}
				n.setUnnormalizedAuthorityWeight(x * n.getAuthorityWeightForCalculation());
				n.setUnnormalizedHubWeight(y * n.getHubWeightForCalculation());
			}

			double sumX = 0;
			double sumY = 0;
			for (Object o : g.getVertices()) {
				n = (Node) o;
				sumX += n.getUnnormalizedAuthorityWeight();
				sumY += n.getUnnormalizedHubWeight();
			}
			for (Object o : g.getVertices()) {
				n = (Node) o;
				n.setAuthorityWeight(n.getUnnormalizedAuthorityWeight() / sumX);
				n.setHubWeight(n.getUnnormalizedHubWeight() / sumY);
			}
		}
	}

	/**
	 * Assign the list of edges to be restricted to the class variable
	 * 
	 * @param restrictedEdges
	 */
	public void restrictEdges(HashSet<String> restrictedEdges) {
		this.restrictedEdges = restrictedEdges;

	}
}
