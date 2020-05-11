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
		
		for (Object	 o : g.getVertices()) {
			
			Node node = (Node) o;
			visited.put(node, false);
			//System.out.println("Node visitied "+node+ " its value is "+visited.get(node));
			
			}
		
		for (Object	 o : Adjlist.keySet()) {
			Node node = (Node) o;
			if(!visited.get(node)) {
				DFSUtil(node, Adjlist);
				compCount++;
			}
			
		}
		     
	         
		return components;
	}
	
	
	void DFSUtil(Node v, HashMap<Node, List<Node>> adjNodes ) { 
        // Mark the current node as visited and print it 
    //    visited[v] = true; 
		visited.put(v, true);
       // System.out.print(v+" "); 
        //components.get(comp_count).add(v);
       if(components.containsKey(compCount)) {
    	   
    	   components.get(compCount).add(v);
       }
       else {
    	   List<Node> temp = new ArrayList<>();
    	   temp.add(v);
    	   components.put(compCount, temp);
    	   
       }
        
        // Recur for all the vertices 
        // adjacent to this vertex 
//        for (int x : adjListArray[v]) { 
//            if(!visited[x]) DFSUtil(x,visited); 
//        } 
        
//        for(Integer a : g.get(key)) {
//        	 if(!visited[a]) DFSUtil(a,visited); 
//        }
       
       if(adjNodes.containsKey(v)) {
    	   
    	   for (Node a : adjNodes.get(v)) {
    			
        	   if(!visited.get(a)) DFSUtil(a, adjNodes);
    	}
    	   
       }
       
      
        
  
    }
	

}
