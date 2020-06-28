package org.dice.kgsmrstn.config;

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
	private String clazz;
	private Integer topk;

	private String predicateSelectionMode;
	private String entity;

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

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public Integer getTopk() {
		return topk;
	}

	public void setTopk(Integer topk) {
		this.topk = topk;
	}

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public String getPredicateSelectionMode() {
		return predicateSelectionMode;
	}

	public void setPredicateSelectionMode(String predicateSelectionMode) {
		this.predicateSelectionMode = predicateSelectionMode;
	}

	@Override
	public String toString() {
		return "KgsmrstnRunConfig [seed=" + seed + ", minSentence=" + minSentence + ", maxSentence=" + maxSentence
				+ ", sqparqlEndPoint=" + sqparqlEndPoint + ", selectorType=" + selectorType + ", clazz=" + clazz
				+ ", topk=" + topk + ", entity=" + entity + "]";
	}

}
