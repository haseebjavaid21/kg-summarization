package org.dice.kgsmrstn.controller;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;
import java.util.ListIterator;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Statement;
import org.dice.kgsmrstn.config.KgsmrstnRunConfig;
import org.dice.kgsmrstn.selector.AbstractSummarizationSelectorSalsa;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KgsmrstnController {

	private static final String DB_ONTOLOGY_PERSON = "<http://dbpedia.org/ontology/Person>";
	private static final String DB_ONTOLOGY_PLACE = "<http://dbpedia.org/ontology/Place>";
	private static final String DB_ONTOLOGY_ORGANISATION = "<http://dbpedia.org/ontology/Organisation>";
	private static final String DB_ENDPOINT = "http://dbpedia.org/sparql";
	private static final String DB_LIVE_ENDPOINT = "http://dbpedia-live.openlinksw.com/sparql";
	private static final String WIKI_ENDPOINT = "https://query.wikidata.org/";

	private static Integer counter = 1;

	private org.slf4j.Logger log = LoggerFactory.getLogger(KgsmrstnController.class);

	@GetMapping(value = "/kgraphsalsa", produces = MediaType.APPLICATION_JSON_VALUE) // , produces = "text/plain"
	public String getKGraphSalsa() {

		log.info("In getKGraph");

		KgsmrstnRunConfig runConfig = new KgsmrstnRunConfig();

		// runConfig.setSelectorType("simple");

		List<Statement> triples;

		AbstractSummarizationSelectorSalsa ass = new AbstractSummarizationSelectorSalsa(runConfig.getSqparqlEndPoint(),
				null);

		// triples = getResources(classes,clazz,topk);
		triples = ass.getResources();

		// Possible Solution #1,but written as a JSON file.
		Model m = ModelFactory.createDefaultModel();
		ListIterator<Statement> StmtIterator = triples.listIterator();
		try {
			while (StmtIterator.hasNext()) {
				Statement stmt = (Statement) StmtIterator.next();
				m.add(stmt);
			}
		} catch (Exception e) {
			return "callback(" + "{" + "'status':" + false + ",'msg' :\"" + e.getMessage() + "\"" + "}" + ")";
		}
		FileOutputStream oFile = null;
		try {
			oFile = new FileOutputStream("./src/main/resources/webapp/output_test2.ttl", false);
		} catch (FileNotFoundException e1) {
			return "callback(" + "{" + "'status':" + false + ",'msg' :\"" + e1.getMessage() + "\"" + "}" + ")";
		}
		m = m.write(oFile, "Turtle");
		if (!(m.isEmpty())) {
			return "callback(" + "{" + "'status':" + true + "}" + ")";
		} else {
			return "callback(" + "{" + "'status':" + false + ",'msg' :\"\"" + "}" + ")";
		}

	}

}
