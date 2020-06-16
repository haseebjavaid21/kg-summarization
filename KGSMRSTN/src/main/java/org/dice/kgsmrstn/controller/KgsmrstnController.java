package org.dice.kgsmrstn.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.Set;

import org.apache.jena.atlas.io.AWriter;
import org.apache.jena.atlas.io.IndentedWriter;
import org.apache.jena.atlas.lib.CharSpace;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.writer.WriterStreamRDFPlain;
import org.dice.kgsmrstn.config.KgsmrstnRunConfig;
import org.dice.kgsmrstn.selector.EntityTriplesSelector;
import org.dice.kgsmrstn.selector.TripleSelector;
import org.dice.kgsmrstn.selector.TripleSelectorFactory;
import org.dice.kgsmrstn.selector.TripleSelectorFactory.SelectorType;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
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

	public String getKGraph(@PathVariable("type") String type, @PathVariable("clazz") String clazz,
			@PathVariable("topk") int topk) {

		log.info("In getKGraph");

		final TripleSelectorFactory factory = new TripleSelectorFactory();
		TripleSelector tripleSelector = null;
		KgsmrstnRunConfig runConfig = new KgsmrstnRunConfig();
		runConfig.setSqparqlEndPoint(DB_ENDPOINT);
		runConfig.setSeed(System.nanoTime());
		runConfig.setSelectorType(type);
		runConfig.setClazz(clazz);
		runConfig.setTopk(topk);
		// runConfig.setSelectorType("simple");

		List<Statement> triples;
		final Set<String> classes = new HashSet<>();
		classes.add(DB_ONTOLOGY_PERSON);
		classes.add(DB_ONTOLOGY_PLACE);
		classes.add(DB_ONTOLOGY_ORGANISATION);

		SelectorType selectorType = runConfig.getSelectorTypeEnum();

		tripleSelector = factory.create(selectorType, classes, new HashSet<>(), runConfig.getSqparqlEndPoint(), null,
				runConfig.getMinSentence(), runConfig.getMaxSentence(), runConfig.getSeed(), runConfig.getClazz(),
				runConfig.getTopk(), null);

		triples = tripleSelector.getNextStatements();

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
			oFile = new FileOutputStream("./src/main/resources/webapp/output.json", false);
		} catch (FileNotFoundException e1) {
			return "callback(" + "{" + "'status':" + false + ",'msg' :\"" + e1.getMessage() + "\"" + "}" + ")";
		}
		m = m.write(oFile, "RDF/JSON");
		if (!(m.isEmpty())) {
			return "callback(" + "{" + "'status':" + true + "}" + ")";
		} else {
			return "callback(" + "{" + "'status':" + false + ",'msg' :\"\"" + "}" + ")";
		}
	}

	@GetMapping(value = "kgraph/type/{type}/name/{entity}/top/{k}/predicatemode/{mode}")
	public String getSummarizedInfoOfAnEntity(@PathVariable("type") String type, @PathVariable("entity") String entity,
			@PathVariable("k") Integer k, @PathVariable("mode") String mode) {

		log.info("In getSummarizedInfoOfAnEntity...");
		log.info("Current entity..." + entity);

		entity = (entity.contains(" ") ? entity.replaceAll(" ", "_") : entity);
		List<Triple> triples;
		Model model;

		EntityTriplesSelector selector = new EntityTriplesSelector(DB_ENDPOINT, entity, k, mode);
		triples = selector.getTriples();
		model = selector.getModel();

		FileOutputStream oFile = null;
		try {
				oFile = new FileOutputStream("src/main/resources/webapp/output.json", false);
				RDFDataMgr.write(oFile, model, Lang.RDFJSON) ;

				oFile = new FileOutputStream("src/main/resources/webapp/output.nt", false);
				//RDFDataMgr.write(oFile, model, Lang.NTRIPLES) ;
				AWriter write = new IndentedWriter(oFile);
				WriterStreamRDFPlain writer = new WriterStreamRDFPlain(write, CharSpace.UTF8);

				ListIterator<Triple> tripleIterator = triples.listIterator();
				while (tripleIterator.hasNext()) {
					Triple triad = tripleIterator.next();
					writer.triple(triad);
				}
				write.flush();
				write.close();

				/*oFile = new FileOutputStream("src/main/resources/webapp/output.ttl", false);
				RDFDataMgr.write(oFile, model, Lang.TTL) ;*/

		} catch (FileNotFoundException e1) {
			return "callback(" + "{" + "'status':" + false + ",'msg' :\"" + e1.getMessage() + "\"" + "}" + ")";
		}
		return "callback(" + "{" + "'status':" + true + "}" + ")";

	}

	public void generateTriplesforEvaluation(String type, String entity, Integer k, String mode, String... format) {

		log.info("In forEvaluation...");
		log.info("Current entity..." + entity);

		entity = (entity.contains(" ") ? entity.replaceAll(" ", "_") : entity);
		List<Triple> triples;

		EntityTriplesSelector selector = new EntityTriplesSelector(DB_ENDPOINT, entity, k, mode);
		triples = selector.getTriples();

		String directoryPath = "./src/main/resources/webapp/output/";
		File file = new File(directoryPath + "/" + counter);
		Boolean directoryCreated = file.mkdir();
		if (directoryCreated) {
			FileOutputStream oFile_nt_10 = null;
			FileOutputStream oFile_nt_5 = null;

			String filename_10 = counter + "_top10.nt";
			String filename_5 = counter + "_top5.nt";
			try {
				oFile_nt_10 = new FileOutputStream("./src/main/resources/webapp/output/" + counter + "/" + filename_10,
						false);
				oFile_nt_5 = new FileOutputStream("./src/main/resources/webapp/output/" + counter + "/" + filename_5,
						false);
			} catch (FileNotFoundException e1) {
				System.out.println(e1.getStackTrace());
			}

			// write top-10
			AWriter write = new IndentedWriter(oFile_nt_10);
			WriterStreamRDFPlain writer = new WriterStreamRDFPlain(write, CharSpace.UTF8);

			ListIterator<Triple> tripleIterator = triples.listIterator();
			while (tripleIterator.hasNext()) {
				Triple triad = tripleIterator.next();
				writer.triple(triad);
			}
			write.flush();
			write.close();

			// write top-5
			write = new IndentedWriter(oFile_nt_5);
			writer = new WriterStreamRDFPlain(write, CharSpace.UTF8);

			tripleIterator = triples.listIterator();

			int breakAfterFive = 1;
			while (tripleIterator.hasNext()) {
				if (breakAfterFive > 5)
					break;
				Triple triad = tripleIterator.next();
				writer.triple(triad);
				breakAfterFive++;
			}
			write.flush();
			write.close();

		}

		if (counter == 100)
			counter = counter + 40;
		counter++;

	}

}
