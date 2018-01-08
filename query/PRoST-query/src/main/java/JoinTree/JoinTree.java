package JoinTree;

import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;


/**
 * JoinTree definition
 * @author Matteo Cossu
 *
 */
public class JoinTree {
	
	private Node node;
	private String filter;
	
	private boolean selectDistinct = false;
	
	// identifier for the query, useful for debugging
	public String query_name;
	
	public Node getNode() {
		return node;
	}

	
	public JoinTree(Node node, String query_name){
		this.query_name = query_name;
		this.node = node;
	}
	
	public void computeSingularNodeData(SQLContext sqlContext){
		node.computeSubTreeData(sqlContext);		
	}
	
	public Dataset<Row> computeJoins(SQLContext sqlContext){
		// compute all the joins
		Dataset<Row> results = node.computeJoinWithChildren(sqlContext);
		// select only the requested result
		Column [] selectedColumns = new Column[node.projection.size()];
		for (int i = 0; i < selectedColumns.length; i++) {
			selectedColumns[i]= new Column(node.projection.get(i));
		}

		// if there is a filter set, apply it
		results =  filter == null ? results.select(selectedColumns) : results.filter(filter).select(selectedColumns);
		
		// if results are distinct
		if(selectDistinct) results = results.distinct();
		
		return results;
		
	}
	
	
	@Override
	public String toString(){
		
		return this.node.toString();
	}


	public String getFilter() {
		return filter;
	}

	// set the filter condition translated in SQL
	public void setFilter(String filter) {
		this.filter = sparqlFilterToSQL(filter);
	}
	
	// set the filter condition translated in SQL
	public void setDistinct(boolean distinct) {
		this.selectDistinct = distinct;
	}
	
	/*
	 * this method translates a SPARQL filter condition into a SQL where condition
	 */
	private String sparqlFilterToSQL(String sparqlFilter) {
		//TODO: actual translation missing 
		return sparqlFilter;
	}

}