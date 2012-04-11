package com.tinkerpop.bench.generator;

import com.tinkerpop.bench.Bench;
import com.tinkerpop.bench.ConsoleUtils;
import com.tinkerpop.bench.StatisticsHelper;
import com.tinkerpop.bench.cache.Cache;
import com.tinkerpop.bench.evaluators.Evaluator;
import com.tinkerpop.bench.evaluators.EvaluatorDegree;
import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.Vertex;

import edu.harvard.pass.cpl.CPL;
import edu.harvard.pass.cpl.CPLObject;


/**
 * A simple Barabasi graph generator for the common case with power=1
 *
 * @author Peter Macko <pmacko@eecs.harvard.edu>
 */
public class SimpleBarabasiGenerator extends GraphGenerator {

	protected int N;
	protected int M;
	protected CPLObject cplObject = null;


	/**
	 * Create an instance of the SimpleBarabasiGenerator
	 *
	 * @param N the number of vertices to generate
	 * @param M the degree of the generated vertices
	 */
	public SimpleBarabasiGenerator(int N, int M) {
		this.N = N;
		this.M = M;
	}


	/**
	 * Generate the graph
	 *
	 * @param graph the graph
	 * @see com.tinkerpop.bench.generator.GraphGenerator#generate()
	 */
	@Override
	public void generate(Graph graph) {
		
		int n = N;
		int m = M;
		
		
		// Create a vertex if the graph is empty
		
		boolean empty = true;
		for (@SuppressWarnings("unused") Vertex v : graph.getVertices()) {
			empty = false;
			break;
		}
		
		Cache c = Cache.getInstance(graph);
		
		if (empty) {
			Vertex v = graph.addVertex(null);
			c.addVertex(v);
			n--;
		}

		
		// Create more vertices
		
		Object[] otherVertices = new Object[m];
		for (int i = 0; i < n; i++) {
			
			// Get an array of vertices to connect to
			
			Evaluator evaluator = new EvaluatorDegree(1, 8);
			otherVertices = StatisticsHelper
					.getSampleVertexIds(graph, evaluator, m);
			for (int j = 0; j < otherVertices.length; j++) {
				otherVertices[j] = graph.getVertex(otherVertices[j]);
			}
			
			
			// Create the new vertex and the appropriate edges
			
			Vertex v = graph.addVertex(null);
			c.addVertex(v);
			
			for (Object o : otherVertices) {
				Edge e = graph.addEdge(null, v, (Vertex) o, "");
				c.addEdge(e);
			}
			
			
			if ((i & 7) == 0 || i == n-1) ConsoleUtils.printProgressIndicator(i+1, n);
		}
	}
	
	
	/**
	 * Return (or create) the generator's CPL object
	 * 
	 * @return the CPL object
	 */
	public CPLObject getCPLObject() {
		
		if (cplObject != null) return cplObject;
		if (!CPL.isAttached()) return null;
		
		cplObject = new CPLObject(Bench.ORIGINATOR,
				getClass().getCanonicalName() + " N=" + N + " M=" + M,
				Bench.TYPE_OPERATION);
		cplObject.addProperty("N", "" + N);
		cplObject.addProperty("M", "" + M);
		
		return cplObject;
	}
}
