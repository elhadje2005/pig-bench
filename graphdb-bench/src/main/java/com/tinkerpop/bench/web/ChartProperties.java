package com.tinkerpop.bench.web;


/**
 * A collection of properties shared by different implementations of charts
 * 
 * @author Peter Macko (pmacko@eecs.harvard.edu)
 */
public class ChartProperties {
	
	/*
	 * Data source, transformation, and filtering
	 */
	
	public String source = null;
	public String value = "d.time";
	
	public String foreach = "";
	
	public boolean dropTopBottomExtremes = false;
	public String filter = "true";
	
	
	/*
	 * HTML hooks
	 */
	
	public String attach = null;
	
	
	/*
	 * Chart appearance 
	 */
	
	public String scale = "linear";
	public String ylabel = "";
	
	
	/*
	 * Group by
	 */
	
	public String group_by = null;
	public String group_label_function = null;
	public String category_label_function = null;

}