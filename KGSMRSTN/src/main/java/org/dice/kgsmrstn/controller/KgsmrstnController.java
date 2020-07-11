package org.dice.kgsmrstn.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.apache.jena.atlas.io.AWriter;
import org.apache.jena.atlas.io.IndentedWriter;
import org.apache.jena.atlas.lib.CharSpace;
import org.apache.jena.graph.Triple;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.writer.WriterStreamRDFPlain;
import org.dice.kgsmrstn.config.KgsmrstnRunConfig;
import org.dice.kgsmrstn.selector.AbstractSummarizationSelectorHits;
import org.dice.kgsmrstn.selector.AbstractSummarizationSelectorSalsa;
import org.dice.kgsmrstn.selector.EntityTriplesSelector;
import org.dice.kgsmrstn.selector.SimpleSelector;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(origins = "*")
public class KgsmrstnController {

	private static final String DB_ONTOLOGY_PERSON = "<http://dbpedia.org/ontology/Person>";
	private static final String DB_ONTOLOGY_PLACE = "<http://dbpedia.org/ontology/Place>";
	private static final String DB_ONTOLOGY_ORGANISATION = "<http://dbpedia.org/ontology/Organisation>";
	private static final String DB_ENDPOINT = "http://dbpedia.org/sparql";
	private static final String DB_LIVE_ENDPOINT = "http://dbpedia-live.openlinksw.com/sparql";
	private static final String WIKI_ENDPOINT = "https://query.wikidata.org/";
	private static final String tempFilePath = "filefrompost.ttl";

	private static Integer counter = 1;

	private org.slf4j.Logger log = LoggerFactory.getLogger(KgsmrstnController.class);

	public String getKGraph(@PathVariable("type") String type, @PathVariable("clazz") String clazz,
			@PathVariable("topk") int topk) {

		log.info("In getKGraph");

		// JSON Object for AJAX Response
		JSONObject jsonResponse;

		KgsmrstnRunConfig runConfig = new KgsmrstnRunConfig();
		runConfig.setSqparqlEndPoint(DB_ENDPOINT);
		runConfig.setSeed(System.nanoTime());
		runConfig.setSelectorType(type);
		runConfig.setClazz(clazz);
		runConfig.setTopk(topk);

		List<Statement> triples;
		final Set<String> classes = new HashSet<>();
		classes.add(DB_ONTOLOGY_PERSON);
		classes.add(DB_ONTOLOGY_PLACE);
		classes.add(DB_ONTOLOGY_ORGANISATION);

		SimpleSelector simpleSelector = new SimpleSelector(classes, new HashSet<>(), runConfig.getSqparqlEndPoint(),
				null, runConfig.getClazz(), runConfig.getTopk());

		triples = simpleSelector.getNextStatements();

		Model m = ModelFactory.createDefaultModel();
		ListIterator<Statement> StmtIterator = triples.listIterator();
		try {
			while (StmtIterator.hasNext()) {
				Statement stmt = (Statement) StmtIterator.next();
				m.add(stmt);
			}
		} catch (Exception e) {
			jsonResponse = new JSONObject();
			try {
				jsonResponse.put("status", false);
				jsonResponse.put("msg", e.getMessage());
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			return jsonResponse.toString();
		}
		FileOutputStream oFile = null;
		try {
			oFile = new FileOutputStream("./src/main/resources/webapp/output.json", false);
		} catch (FileNotFoundException e1) {
			jsonResponse = new JSONObject();
			try {
				jsonResponse.put("status", false);
				jsonResponse.put("msg", e1.getMessage());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return jsonResponse.toString();
		}
		m = m.write(oFile, "RDF/JSON");
		jsonResponse = new JSONObject();
		if (!(m.isEmpty())) {
			try {
				jsonResponse.put("status", true);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			try {
				jsonResponse.put("status", false);
				jsonResponse.put("msg", " ");
				return jsonResponse.toString();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return jsonResponse.toString();
	}

	@GetMapping(value = "kgraph/type/{type}/name/{entity}/top/{k}/predicatemode/{mode}")
	public String getSummarizedInfoOfAnEntity(@PathVariable("type") String type, @PathVariable("entity") String entity,
			@PathVariable("k") Integer k, @PathVariable("mode") String mode) {

		log.info("In getSummarizedInfoOfAnEntity...");

		// JSON Object for AJAX Response
		JSONObject jsonResponse;

		log.info("Current entity..." + entity);

		entity = (entity.contains(" ") ? entity.replaceAll(" ", "_") : entity);
		List<Triple> triples;
		Model model;

		EntityTriplesSelector selector = new EntityTriplesSelector(DB_ENDPOINT, entity, k, mode);
		triples = selector.getTriples();
		model = selector.getModel();

		if (triples == null) {
			jsonResponse = new JSONObject();
			try {
				jsonResponse.put("status", false);
				jsonResponse.put("msg", "Invalid Input Entity");
				return jsonResponse.toString();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		FileOutputStream oFile = null;
		try {
			oFile = new FileOutputStream("src/main/resources/webapp/output.json", false);
			RDFDataMgr.write(oFile, model, Lang.RDFJSON);

			oFile = new FileOutputStream("src/main/resources/webapp/output.nt", false);
			// RDFDataMgr.write(oFile, model, Lang.NTRIPLES) ;
			AWriter write = new IndentedWriter(oFile);
			WriterStreamRDFPlain writer = new WriterStreamRDFPlain(write, CharSpace.UTF8);

			ListIterator<Triple> tripleIterator = triples.listIterator();
			while (tripleIterator.hasNext()) {
				Triple triad = tripleIterator.next();
				writer.triple(triad);
			}
			write.flush();
			write.close();

			/*
			 * oFile = new
			 * FileOutputStream("src/main/resources/webapp/output.ttl", false);
			 * RDFDataMgr.write(oFile, model, Lang.TTL) ;
			 */

		} catch (FileNotFoundException e1) {
			jsonResponse = new JSONObject();
			try {
				jsonResponse.put("status", true);
				jsonResponse.put("msg", e1.getMessage());
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		jsonResponse = new JSONObject();
		try {
			jsonResponse.put("status", true);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonResponse.toString();

	}

	public void generateTriplesforEvaluation(String type, String entity, Integer k, String mode) {

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

	@PostMapping("/kgraphsalsa")
	public String getKGraphSalsa(@RequestParam(name = "salsa_input") MultipartFile inputFile) throws IOException {

		log.info("In getKGraphSalsa", inputFile);
		this.writeToTempFolder(inputFile);
		// JSON Object for AJAX Response
		JSONObject jsonResponse;

		KgsmrstnRunConfig runConfig = new KgsmrstnRunConfig();

		List<Statement> triples;

		AbstractSummarizationSelectorSalsa ass = new AbstractSummarizationSelectorSalsa(runConfig.getSqparqlEndPoint(),
				null);

		triples = ass.getResources(tempFilePath);
		this.deleteTempFile();

		Model m = ModelFactory.createDefaultModel();
		ListIterator<Statement> StmtIterator = triples.listIterator();

		try {
			while (StmtIterator.hasNext()) {
				Statement stmt = (Statement) StmtIterator.next();
				m.add(stmt);
			}
			triples.clear();

		} catch (Exception e) {
			jsonResponse = new JSONObject();
			try {
				jsonResponse.put("status", false);
				jsonResponse.put("msg", e.getMessage());
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			return jsonResponse.toString();
		}
		FileOutputStream oFile = null;
		try {
			oFile = new FileOutputStream("./src/main/resources/webapp/output_salsa.ttl", false);
		} catch (FileNotFoundException e1) {
			jsonResponse = new JSONObject();
			try {
				jsonResponse.put("status", false);
				jsonResponse.put("msg", e1.getMessage());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return jsonResponse.toString();
		}
		m = m.write(oFile, "Turtle");
		jsonResponse = new JSONObject();
		if (!(m.isEmpty())) {
			try {
				jsonResponse.put("status", true);
				jsonResponse.put("file_path", "src/main/resources/webapp/output_salsa.ttl");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return jsonResponse.toString();
		} else {
			try {
				jsonResponse.put("status", false);
				jsonResponse.put("msg", " ");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return jsonResponse.toString();
		}

	}

	@PostMapping(value = "/kgraphHits")
	public String getKGraphHITS(@RequestParam(name = "hits_input") MultipartFile inputFile) throws IOException {

		log.info("In getKGraphHITS");
		this.writeToTempFolder(inputFile);
		// JSON Object for AJAX Response
		JSONObject jsonResponse;

		KgsmrstnRunConfig runConfig = new KgsmrstnRunConfig();

		List<Statement> triples;
		AbstractSummarizationSelectorHits Ash = new AbstractSummarizationSelectorHits(runConfig.getSqparqlEndPoint(),
				null);
		triples = Ash.getResources(tempFilePath);
		this.deleteTempFile();

		Model m = ModelFactory.createDefaultModel();
		ListIterator<Statement> StmtIterator = triples.listIterator();
		try {
			while (StmtIterator.hasNext()) {
				Statement stmt = (Statement) StmtIterator.next();
				m.add(stmt);
			}
		} catch (Exception e) {
			jsonResponse = new JSONObject();
			try {
				jsonResponse.put("status", false);
				jsonResponse.put("msg", e.getMessage());
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			return jsonResponse.toString();
		}
		FileOutputStream oFile = null;
		try {
			oFile = new FileOutputStream("./src/main/resources/webapp/output_hits.ttl", false);
		} catch (FileNotFoundException e1) {
			jsonResponse = new JSONObject();
			try {
				jsonResponse.put("status", false);
				jsonResponse.put("msg", e1.getMessage());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return jsonResponse.toString();
		}
		m = m.write(oFile, "Turtle");
		jsonResponse = new JSONObject();
		if (!(m.isEmpty())) {
			try {
				jsonResponse.put("status", true);
				jsonResponse.put("file_path", "src/main/resources/webapp/output_hits.ttl");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return jsonResponse.toString();
		} else {
			try {
				jsonResponse.put("status", false);
				jsonResponse.put("msg", " ");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return jsonResponse.toString();
		}

	}

	public Boolean writeToTempFolder(MultipartFile inputFile) throws IOException {
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(tempFilePath);
			fos.write(inputFile.getBytes());
			fos.close();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void deleteTempFile() {
		File ttl = new File(tempFilePath);
		ttl.delete();
	}

}
