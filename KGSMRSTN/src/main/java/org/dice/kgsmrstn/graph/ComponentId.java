package org.dice.kgsmrstn.graph;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.jena.sparql.pfunction.library.listIndex;


import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;

public class ComponentId {
	
	
	HashMap<Integer, List<Node>> components = new HashMap<>();
	int compCount = 0;
	

	
	public HashMap<Integer, List<Node>> findComponets(HashMap<Node, List<Node>> Adjlist, Graph<Node, String> g) {
		
		for (Object	 o : Adjlist.keySet()) {
			Node node = (Node) o;
		    if(node.isVisit())
		    	continue;
		    node.setVisit(true);
		    if(components.containsKey(compCount)) {
		    	components.get(compCount).add(node);
		    }
		    else {
		    	   List<Node> temp = new ArrayList<>();
	    		   temp.add(node);
	    		   components.put(compCount, temp);
		    }
		    
		    
		  if((Adjlist.get(node) != null)) {
			  Iterator<Node>  i = Adjlist.get(node).iterator();
			    while(i.hasNext()) {
			    	Node node1 = i.next();
			    	if(!node1.isVisit()) {
			    		components.get(compCount).add(node1);
			    		node1.setVisit(true);
			    		if((Adjlist.get(node1) != null)) {
			    			Iterator<Node> j = Adjlist.get(node1).iterator();
				    		while(j.hasNext()) {
				    			Node node2 = j.next();
				    			if(node2.isVisit()) {
				    				node2.setVisit(true);
				    				components.get(compCount).add(node2);
				    			}
				    		}
			    		}
			    	}
			    		
			    }
		  }
		    
		    compCount++;
		}
		     
	         
		return components;
	}
	


}
