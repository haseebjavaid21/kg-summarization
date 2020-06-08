package org.dice.kgsmrstn.graph;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.jena.sparql.pfunction.library.listIndex;


import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;

public class ComponentId {
	
	
	HashMap<Integer, List<Node>> components = new HashMap<>();
	HashMap<Node, Boolean> visited = new HashMap<>();
	int compCount = 0;
	
	//LinkedList<Node>[] adjListArray;
	
	public HashMap<Integer, List<Node>> findComponets(HashMap<Node, List<Node>> Adjlist, Graph<Node, String> g) {
		
//		for (Object	 o : g.getVertices()) {
//			
//			Node node = (Node) o;
//			visited.put(node, false);
//			
//			//System.out.println("Node visitied "+node+ " its value is "+visited.get(node));
//			
//			}
		
		for (Object	 o : Adjlist.keySet()) {
			Node node = (Node) o;
			if(!node.isVisit()) {
				DFSUtil(node, Adjlist);
				compCount++;
			}
				
//			if(!visited.get(node)) {
//				DFSUtil(node, Adjlist);
//				compCount++;
//			}
			
		}
		     
	         
		return components;
	}
	
	
	void DFSUtil(Node v, HashMap<Node, List<Node>> adjNodes ) { 
        // Mark the current node as visited and print it 
 
		//visited.put(v, true);
		v.setVisit(true);
  
       if(components.containsKey(compCount)) {
    	   
    	   components.get(compCount).add(v);
       }
       else {
    	   List<Node> temp = new ArrayList<>();
    	   temp.add(v);
    	   components.put(compCount, temp);
    	   
       }
        

       
       if(adjNodes.containsKey(v)) {
    	   
    	   for (Node a : adjNodes.get(v)) {
    			
    		   if(!a.isVisit()) DFSUtil(a, adjNodes);
        	  // if(!visited.get(a)) DFSUtil(a, adjNodes);
    	}
    	   
       }
       
      
        
  
    }
	

}
