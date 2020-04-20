package org.dice.kgsmrstn.config;

import org.dice.kgsmrstn.selector.TripleSelectorFactory.SelectorType;

public class KgsmrstnRunConfig {
	
	public static final long DEF_SEED = System.currentTimeMillis();
	public static final int DEF_MIN_SENTENCE = 3;
	public static final int DEF_MAX_SENTENCE = 10;
	public static final String DEF_SPARQL_EP = "http://dbpedia.org/sparql";
	
	private long seed = DEF_SEED;
	private int minSentence = DEF_MIN_SENTENCE;
	private int maxSentence = DEF_MAX_SENTENCE;
	private String sqparqlEndPoint = DEF_SPARQL_EP;
	private String selectorType;


	public long getSeed() {
		return seed;
	}

	public void setSeed(long seed) {
		this.seed = seed;
	}

	public int getMinSentence() {
		return minSentence;
	}

	public void setMinSentence(int minSentence) {
		this.minSentence = minSentence;
	}

	public int getMaxSentence() {
		return maxSentence;
	}

	public void setMaxSentence(int maxSentence) {
		this.maxSentence = maxSentence;
	}

	public String getSelectorType() {
		return selectorType;
	}

	public void setSelectorType(String selectorType) {
		this.selectorType = selectorType;
	}
	
	public String getSqparqlEndPoint() {
		return sqparqlEndPoint;
	}

	public void setSqparqlEndPoint(String sqparqlEndPoint) {
		this.sqparqlEndPoint = sqparqlEndPoint;
	}

	public SelectorType getSelectorTypeEnum() {
		SelectorType res = null;
		if (selectorType.matches("star")) {
			res = SelectorType.STAR;
		} else if (selectorType.matches("hybrid")) {
			res = SelectorType.HYBRID;
		} else if (selectorType.matches("path")) {
			res = SelectorType.PATH;
		} else if (selectorType.matches("sym")) {
			res = SelectorType.SIM_STAR;
		} else if (selectorType.matches("simple")) {
			res = SelectorType.SIMPLE;
		}
		return res;
	}

	@Override
	public String toString() {
		return "KgsmrztnRunConfig [seed=" + seed + ", minSentence=" + minSentence + ", maxSentence=" + maxSentence
				+ ", sqparqlEndPoint=" + sqparqlEndPoint + ", selectorType=" + selectorType + "]";
	}

}
