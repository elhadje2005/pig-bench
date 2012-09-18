package com.tinkerpop.bench;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.extensions.impls.dex.ExtendedDexGraph;
import com.tinkerpop.blueprints.extensions.impls.neo4j.ExtendedNeo4jGraph;
import com.tinkerpop.blueprints.extensions.impls.sql.SqlGraph;
import com.tinkerpop.blueprints.extensions.impls.bdb.BdbGraph;
//import com.tinkerpop.blueprints.pgm.impls.hollow.HollowGraph;


/**
 * A database engine
 * 
 * @author Peter Macko (pmacko@eecs.harvard.edu)
 */
public abstract class DatabaseEngine {
	
	/**
	 * The set of supported database engines
	 */
	public static final Map<String, DatabaseEngine> ENGINES;
	
	static {
		Map<String, DatabaseEngine> engines = new TreeMap<String, DatabaseEngine>();
		engines.put("bdb", new BerkeleyDB());
		engines.put("dex", new DEX());
		//engines.put("hollow", new DatabaseEngine(HollowGraph.class, "hollow", "Hollow", "The hollow implementation with no backing database", false, false));
		engines.put("neo", new Neo4j());
		engines.put("sql", new SQL());
		ENGINES = Collections.unmodifiableMap(engines);
	}
	

	private Class<? extends Graph> blueprintsClass;
	private String shortName;
	private String longName;
	private String description;
	private boolean hasOptionalArgument;
	private boolean persistent;
	
	
	/**
	 * Create an instance of {@link DatabaseEngine}
	 * 
	 * @param blueprintsClass the Blueprints API class
	 * @param shortName the short name
	 * @param longName the long name
	 * @param description the description
	 * @param hasOptionalArgument true if it accepts an optional path/address argument
	 * @param persistent true if the database is persistent
	 */
	public DatabaseEngine(Class<? extends Graph> blueprintsClass, String shortName,
			String longName, String description, boolean hasOptionalArgument,
			boolean persistent) {
		this.blueprintsClass = blueprintsClass;
		this.shortName = shortName;
		this.longName = longName;
		this.description = description;
		this.hasOptionalArgument = hasOptionalArgument;
		this.persistent = persistent;
	}


	/**
	 * Return the Blueprints class
	 * 
	 * @return the blueprints class
	 */
	public Class<? extends Graph> getBlueprintsClass() {
		return blueprintsClass;
	}


	/**
	 * Return the short name
	 * 
	 * @return the short name
	 */
	public String getShortName() {
		return shortName;
	}


	/**
	 * Return the long (pretty) name
	 * 
	 * @return the long name
	 */
	public String getLongName() {
		return longName;
	}


	/**
	 * Return the description
	 * 
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}


	/**
	 * Determine whether the command-line option should take an optional path argument
	 * 
	 * @return true if the command-line option should take an optional path argument
	 */
	public boolean hasOptionalArgument() {
		return hasOptionalArgument;
	}
	
	
	/**
	 * Return the string version of the object
	 * 
	 * @return the string version of the object
	 */
	@Override
	public String toString() {
		return longName;
	}
	
	
	/**
	 * Determine whether this database is persistent
	 * 
	 * @return true if it is persistent
	 */
	public boolean isPersistent() {
		return persistent;
	}
	
	
	/**
	 * Create a new instance of the Graph
	 * 
	 * @param dbDir the database directory
	 * @param configuration the map of database-specific configuration arguments
	 * @return the new instance
	 */
	public abstract Graph newInstance(String dbDir, Map<String, String> configuration);
	
	
	/**
	 * Determine whether the given class is a RDF graph
	 * 
	 * @param g the graph instance
	 * @return true if it is a RDF graph
	 */
	public static boolean isRDFGraph(Graph g) {
		return false;
	}
	
	
	/**
	 * Determine whether the given class is the SQL graph
	 * 
	 * @param g the graph instance
	 * @return true if it is the SQL graph
	 */
	public static boolean isSQLGraph(Graph g) {
		return g instanceof SqlGraph;
	}
	
	
	/**
	 * Determine whether the given class is the Hollow graph
	 * 
	 * @param g the graph instance
	 * @return true if it is the Hollow graph
	 */
	public static boolean isHollowGraph(Graph g) {
		return false;
	}
	
	
	/**
	 * BerkeleyDB
	 */
	public static class BerkeleyDB extends DatabaseEngine {		
		
		/**
		 * Create an instance of this class
		 */
		public BerkeleyDB() {
			super(BdbGraph.class, "bdb", "BerkeleyDB",
					"BerkeleyDB implementation using duplicates on edge lookups and properties",
					false, true);
		}
				
		/**
		 * Create a new instance of the Graph
		 * 
		 * @param dbDir the database directory
		 * @param configuration the map of database-specific configuration arguments
		 * @return the new instance
		 */
		@Override
		public BdbGraph newInstance(String dbDir, Map<String, String> configuration) {
			return new BdbGraph(dbDir);
		}
	}
	
	
	/**
	 * DEX
	 */
	public static class DEX extends DatabaseEngine {		
		
		/**
		 * Create an instance of this class
		 */
		public DEX() {
			super(ExtendedDexGraph.class, "dex", "DEX", "DEX", false, true);
		}
				
		/**
		 * Create a new instance of the Graph
		 * 
		 * @param dbDir the database directory
		 * @param configuration the map of database-specific configuration arguments
		 * @return the new instance
		 */
		@Override
		public ExtendedDexGraph newInstance(String dbDir, Map<String, String> configuration) {
			return new ExtendedDexGraph(dbDir + "/graph.dex");
		}
	}
	
	
	/**
	 * Neo4j
	 */
	public static class Neo4j extends DatabaseEngine {		
		
		/**
		 * Create an instance of this class
		 */
		public Neo4j() {
			super(ExtendedNeo4jGraph.class, "neo", "Neo4j", "Neo4j", false, true);
		}
				
		/**
		 * Create a new instance of the Graph
		 * 
		 * @param dbDir the database directory
		 * @param configuration the map of database-specific configuration arguments
		 * @return the new instance
		 */
		@Override
		public ExtendedNeo4jGraph newInstance(String dbDir, Map<String, String> configuration) {
			return new ExtendedNeo4jGraph(dbDir, configuration);
		}
	}
	
	
	/**
	 * SQL
	 */
	public static class SQL extends DatabaseEngine {		
		
		/**
		 * Create an instance of this class
		 */
		public SQL() {
			super(SqlGraph.class, "sql", "MySQL", "MySQL", true, true);
		}
				
		/**
		 * Create a new instance of the Graph
		 * 
		 * @param dbDir the database directory
		 * @param configuration the map of database-specific configuration arguments
		 * @return the new instance
		 */
		@Override
		public SqlGraph newInstance(String dbDir, Map<String, String> configuration) {
			String dbPath = configuration.get("path");
			if (dbPath == null) {
				throw new IllegalArgumentException("The required \"path\" SQL configuration property is not defined");
			}
			return new SqlGraph(dbPath);
		}
	}
}
