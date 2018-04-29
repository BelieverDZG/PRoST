package JoinTree;

import java.util.List;

import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;

import Executor.Utils;

/**
 * JoinTree definition
 * 
 * @author Matteo Cossu
 *
 */
public class JoinTree {

	private Node root;
	private List<Node> optionalTreeRoots;
	private boolean selectDistinct = false;

	// identifier for the query, useful for debugging
	public String query_name;

	public Node getRoot() {
		return this.root;
	}

	public JoinTree(Node root, List<Node> optionalTreeRoots, String query_name) {
		this.query_name = query_name;
		this.root = root;
		this.optionalTreeRoots = optionalTreeRoots;
	}

	public void computeSingularNodeData(SQLContext sqlContext) {
		this.root.computeSubTreeData(sqlContext);
		for (int i = 0; i < optionalTreeRoots.size(); i++) {
			this.optionalTreeRoots.get(i).computeSubTreeData(sqlContext);
		}
	}

	public Dataset<Row> computeJoins(SQLContext sqlContext) {
		// compute all the joins
		Dataset<Row> results = this.root.computeJoinWithChildren(sqlContext);

		// select only the requested result
		Column[] selectedColumns = new Column[this.root.projection.size()];
		for (int i = 0; i < selectedColumns.length; i++) {
			selectedColumns[i] = new Column(this.root.projection.get(i));
		}
		for (int i = 0; i < optionalTreeRoots.size(); i++) {
			// OPTIONAL
			Node currentOptionalNode = optionalTreeRoots.get(i);
			// compute joins in the optional tree
			Dataset<Row> optionalResults = currentOptionalNode.computeJoinWithChildren(sqlContext);
			// add selection and filter in the optional tree
			// if there is a filter set, apply it
			if(currentOptionalNode.filter == null) {
				optionalResults = optionalResults.filter(currentOptionalNode.filter);
			}

			// add left join with the optional tree
			List<String> joinVariables = Utils.commonVariables(results.columns(), optionalResults.columns());
			results = results.join(optionalResults, scala.collection.JavaConversions.asScalaBuffer(joinVariables).seq(),
					"left_outer");
		}
		
		// if there is a filter set, apply it
		results = this.root.filter == null ? results.select(selectedColumns)
				: results.filter(this.root.filter).select(selectedColumns);

		// if results are distinct
		if (selectDistinct) {
			results = results.distinct();
		}
		return results;

	}

	@Override
	public String toString() {
		return this.root.toString();
	}

	public void setDistinct(boolean distinct) {
		this.selectDistinct = distinct;
	}
}
