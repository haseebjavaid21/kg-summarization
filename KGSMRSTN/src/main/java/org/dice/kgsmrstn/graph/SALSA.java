package org.dice.kgsmrstn.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.uci.ics.jung.graph.Graph;

public class SALSA {

	public void runSALSA(Graph<Node, String> g, HashMap<Integer, List<Node>> component) throws InterruptedException {

		for (Integer i : component.keySet()) {

			List<Node> val = component.get(i);
			int numberComponentNodes = val.size();
			double d1 = (double) numberComponentNodes;
			int totalGraphNodes = g.getVertices().size();
			double d2 = (double) totalGraphNodes;
			List<Integer> temp = computebacklinkcomponents(val, g);
			int totalBackLink = temp.get(0);
			int totalForwardLink = temp.get(1);
			double d3 = (double) totalBackLink;
			double d5 = (double) totalForwardLink;
			for (Node node : val) {
				int backlinkNode = g.inDegree(node);
				int forwardlinkNode = g.outDegree(node);
				double d4 = (double) backlinkNode;
				double d6 = (double) forwardlinkNode;

				double authorityWeight = ((d1 / d2) * (d4 / d3));
				double hubWeight = ((d1 / d2) * (d6 / d5));

				node.setAuthorityWeight(authorityWeight);

				node.setHubWeight(hubWeight);
			}

		}

	}

	public List<Integer> computebacklinkcomponents(List<Node> componentNodes, Graph<Node, String> g) {

		int sumB = 0;
		int sumF = 0;
		List<Integer> temp = new ArrayList<Integer>();
		for (Node node : componentNodes) {
			sumB = sumB + g.inDegree(node);
			sumF = sumF + g.outDegree(node);

		}

		temp.add(sumB);
		temp.add(sumF);
		return temp;
	}

}
