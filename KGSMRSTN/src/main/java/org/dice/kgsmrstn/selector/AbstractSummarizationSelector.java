/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dice.kgsmrstn.selector;

import com.carrotsearch.hppc.IntObjectOpenHashMap;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.Syntax;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Haseeb Javaid
 */
public abstract class AbstractSummarizationSelector implements TripleSelector {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractSelector.class);

    private Set<String> targetClasses;
    private String endpoint;
    private String graph;
    private boolean useSymmetricCbd = false;

    public AbstractSummarizationSelector(Set<String> targetClasses, String endpoint, String graph) {
        this.targetClasses = targetClasses;
        this.endpoint = endpoint;
        this.graph = graph;
    }

    public AbstractSummarizationSelector(Set<String> targetClasses, String endpoint, String graph, boolean useSymmetricCbd) {
        this.useSymmetricCbd = useSymmetricCbd;
        this.targetClasses = targetClasses;
        this.endpoint = endpoint;
        this.graph = graph;
    }

    /**
     * Sort statements by hash
     *
     * @param statements Set of statements
     * @return List of statements sorted by hash
     */
	//@Deprecated sortStatementsByHash
    /**
     * Get all resources that belong to the union of classes and sort them by
     * URI in desceding order
     *
     * @param classes Set of classes for resources
     * @return Sorted list of resources from the classes
     */
    protected List<Statement> getResources(Set<String> classes) {
        String query = "";
        // if (classes == null && classes.isEmpty()) {
        query = "PREFIX : <http://dbpedia.org/resource/>\n"
                + "SELECT * WHERE {\n"
                + "?s ?p ?o\n"
                + "FILTER (?s=<http://dbpedia.org/resource/Brad_Pitt>)\n"
                + "}";
               // }
        // OR ?o=<http://dbpedia.org/resource/Brad_Pitt>
        System.out.println("Query " + query);
        Query sparqlQuery = QueryFactory.create(query, Syntax.syntaxARQ);

        QueryEngineHTTP httpQuery = new QueryEngineHTTP(endpoint, sparqlQuery);
        List result = new ArrayList<>();
        Model m = ModelFactory.createDefaultModel();
        // execute a Select query
        try {
            ResultSet results = httpQuery.execSelect();
            QuerySolution solution;
            int xx = 0;
            while (results.hasNext()) {
                solution = results.next();
                // get the value of the variables in the select clause
                try {
                    Property p = m.createProperty(solution.get("p").asResource().getURI());
                    if (solution.get("o").isLiteral()) {
                        m.add(solution.getResource("s"), p, solution.getLiteral("o"));
                    } else {
                        m.add(solution.getResource("s"), p, solution.getResource("o"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                xx += 1;
//                if(xx == 10) {
//                    break;
//                }
            }
        } finally {
            httpQuery.close();
        }
        return sortStatements(m.listStatements());
    }

    /**
     * Sort statements by hash
     *
     * @param stmtIterator Iterator which is used to get the statements
     * @return List of statements sorted by hash
     */
	//@Deprecated sortStatementsByHash
    protected List<Statement> sortStatements(StmtIterator stmtIterator) {
        List<Statement> result = new ArrayList<Statement>();
        while (stmtIterator.hasNext()) {
            result.add(stmtIterator.next());
        }
        Collections.sort(result, new StatementComparator());
        return result;
    }

    protected List<Statement> sortStatements(Set<Statement> statements) {
        List<Statement> result = new ArrayList<Statement>(statements);
        Collections.sort(result, new StatementComparator());
        return result;
    }
}
