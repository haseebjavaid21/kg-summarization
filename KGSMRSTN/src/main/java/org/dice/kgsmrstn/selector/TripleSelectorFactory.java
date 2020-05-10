/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dice.kgsmrstn.selector;

import java.util.Set;

/**
 * Factory for generating triple selectors from names
 * 
 * @author ngonga
 */
public class TripleSelectorFactory {

	public enum SelectorType {
		STAR, SIM_STAR, PATH, HYBRID, SIMPLE, ENTITY;
	};

	public TripleSelector create(SelectorType type, Set<String> sourceClasses, Set<String> targetClasses,
			String endpoint, String graph, int minSize, int maxSize, long seed,String clazz,int topk) {
		switch (type) {
		case STAR:
			return new SimpleSummarySelector(sourceClasses, targetClasses, endpoint, graph, minSize, maxSize, seed,
					false);
		case SIM_STAR:
			return new SimpleSummarySelector(sourceClasses, targetClasses, endpoint, graph, minSize, maxSize, seed,
					true);
		case PATH:
			return new PathBasedTripleSelector(sourceClasses, targetClasses, endpoint, graph, minSize, maxSize, seed);
		case HYBRID:
			return new HybridTripleSelector(sourceClasses, targetClasses, endpoint, graph, minSize, maxSize, seed);
        case SIMPLE:
			return new SimpleSelector(sourceClasses, targetClasses, endpoint, graph,clazz,topk);
        case ENTITY:
        	return new EntityTriplesSelector(endpoint,clazz);
		}
		return null;
	}

}
