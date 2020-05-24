package org.dice.kgsmrstn.config;

import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

public class ModelDTO {
	
	private Resource subject;
	
	private Resource predicate;
	
	private RDFNode object;

	public Resource getSubject() {
		return subject;
	}

	public void setSubject(Resource subject) {
		this.subject = subject;
	}

	public Resource getPredicate() {
		return predicate;
	}

	public void setPredicate(Resource predicate) {
		this.predicate = predicate;
	}

	public RDFNode getObject() {
		return object;
	}

	public void setObject(RDFNode object) {
		this.object = object;
	}

	@Override
	public String toString() {
		return "ModelDTO [subject=" + subject + ", predicate=" + predicate + ", object=" + object + "]";
	}
	
	
	
	

}
