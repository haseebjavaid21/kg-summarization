package org.dice.kgsmrstn.graph;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.dice.kgsmrstn.util.Triple;
import org.dice.kgsmrstn.util.TripleIndex;
import org.slf4j.LoggerFactory;

import edu.uci.ics.jung.graph.DirectedSparseGraph;

public class BreadthFirstSearch {
	private TripleIndex index;
	private String algo;
	
	private org.slf4j.Logger log = LoggerFactory.getLogger(BreadthFirstSearch.class);

	public BreadthFirstSearch(TripleIndex index, String algo) {
		this.index = index;
		this.algo = algo;
	}


	public DirectedSparseGraph<Node, String> run(int maxDepth, DirectedSparseGraph<Node, String> graph, String edgeType, String nodeType, String edgeLabel)
			throws UnsupportedEncodingException, IOException {
		Queue<Node> q = new LinkedList<Node>();
		for (Node node : graph.getVertices()) {
			q.add(node);
		}
		while (!q.isEmpty()) {
			Node currentNode = q.poll();
			int level = currentNode.getLevel();
			if (level < maxDepth) {
				List<Triple> outgoingNodes = null;
				outgoingNodes = index.search(currentNode.getCandidateURI(), null, null);
				if (outgoingNodes == null) {
					continue;
				}
				for (Triple targetNode : outgoingNodes) {
					if (targetNode.getPredicate() == null && targetNode.getObject() == null) {
						continue;
					}
					if ((targetNode.getPredicate().startsWith(edgeType)  ) && targetNode.getObject().startsWith(nodeType)) {
						int levelNow = level + 1;
						Node Node = new Node(targetNode.getObject(), 0, levelNow, algo);
						q.add(Node);
						graph.addEdge(graph.getEdgeCount() + ";" + targetNode.getPredicate(), currentNode, Node);
					
					}
					
					if(targetNode.getPredicate().contains("label")) {
						int level1 = level + 1;
						Node Node = new Node(targetNode.getObject(), 0, level1, algo);
						graph.addEdge(graph.getEdgeCount() + ";" + targetNode.getPredicate(), currentNode, Node);
						
						
						
						
					}
					
				}
			}
		}
		
		log.info("graph is obtained..");
		return graph;
	}
}
