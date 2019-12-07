package org.dice.kgsmrstn.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.jena.rdf.model.Statement;
import org.dice.kgsmrstn.config.KgsmrstnRunConfig;
import org.dice.kgsmrstn.selector.TripleSelector;
import org.dice.kgsmrstn.selector.TripleSelectorFactory;
import org.dice.kgsmrstn.selector.TripleSelectorFactory.SelectorType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KgsmrstnController {
	
	@GetMapping(value = "/kgraph/")//, produces = "text/plain"
    public List<Statement> getBook() {
		final TripleSelectorFactory factory = new TripleSelectorFactory();
		TripleSelector tripleSelector = null;
		KgsmrstnRunConfig runConfig = new KgsmrstnRunConfig();
		runConfig.setSqparqlEndPoint("http://dbpedia.org/sparql");
		runConfig.setMinSentence(3);
		runConfig.setMaxSentence(10);
		runConfig.setSeed(System.nanoTime());
		runConfig.setSelectorType("sym");
		
		List<Statement> triples;
		final Set<String> classes = new HashSet<>();
		classes.add("<http://dbpedia.org/ontology/Person>");
		classes.add("<http://dbpedia.org/ontology/Place>");
		classes.add("<http://dbpedia.org/ontology/Organisation>");
		
		SelectorType selectorType = runConfig.getSelectorTypeEnum();
		
		tripleSelector = factory.create(selectorType, classes,
				 new HashSet<>(), runConfig.getSqparqlEndPoint(), null, runConfig.getMinSentence(), runConfig.getMaxSentence(),
				runConfig.getSeed());
		
		triples = tripleSelector.getNextStatements();
        return triples;
    }

}
