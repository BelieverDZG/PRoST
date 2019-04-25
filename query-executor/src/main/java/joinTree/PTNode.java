package joinTree;

import java.util.ArrayList;
import java.util.List;

import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.shared.PrefixMapping;
import org.apache.spark.sql.SQLContext;
import stats.DatabaseStatistics;
import utils.Utils;

/**
 * A node of the JoinTree that refers to the Property Table.
 */
public class PTNode extends MVNode {

	/**
	 * The default value is "wide_property_table" when only one PT exists. If an
	 * emergent schema is used, then there exist more than one property tables. Each
	 * of them contains a specific set of predicates. In this case, the default
	 * table name can be changes depending on the list of triples this node
	 * contains.
	 */
	private String tableName = "wide_property_table";

	/*
	 * Alternative constructor, used to instantiate a Node directly with a list of
	 * jena triple patterns.
	 */
	public PTNode(final List<Triple> jenaTriples, final PrefixMapping prefixes, final DatabaseStatistics statistics) {
		super(statistics);
		final ArrayList<TriplePattern> triplePatterns = new ArrayList<>();
		tripleGroup = triplePatterns;
		for (final Triple t : jenaTriples) {
			triplePatterns.add(new TriplePattern(t, prefixes));
		}
		setIsComplex();
	}

	/*
	 * Alternative constructor, used to instantiate a Node directly with a list of
	 * jena triple patterns.
	 */
	public PTNode(final List<Triple> jenaTriples, final PrefixMapping prefixes, final String tableName,
				  final DatabaseStatistics statistics) {
		this(jenaTriples, prefixes, statistics);
		this.tableName = tableName;
	}

	/**
	 * For each triple, set if it contains a complex predicate. A predicate is
	 * complex when there exists at least one subject that has two or more triples
	 * containing the predicate.
	 */
	private void setIsComplex() {
		for (final TriplePattern triplePattern : tripleGroup) {
			triplePattern.isComplex = statistics.getProperties().get(triplePattern.predicate).isComplex();
		}
	}

	@Override
	public void computeNodeData(final SQLContext sqlContext) {
		final ArrayList<String> selectElements = new ArrayList<>();
		final ArrayList<String> whereElements = new ArrayList<>();
		final ArrayList<String> explodedElements = new ArrayList<>();

		if (tripleGroup.get(0).subjectType == ElementType.VARIABLE) {
			selectElements.add("s AS " + Utils.removeQuestionMark(tripleGroup.get(0).subject));
		} else {
			whereElements.add("s='" + tripleGroup.get(0).subject + "'");
		}

		for (final TriplePattern t : tripleGroup) {
			final String columnName = statistics.getProperties().get(t.predicate).getInternalName();
			if (columnName == null) {
				System.err.println("This property does not exists: " + t.predicate);
				return;
			}
			if (t.objectType == ElementType.CONSTANT) {
				if (t.isComplex) {
					whereElements.add("array_contains(" + columnName + ", '" + t.object + "')");
				} else {
					whereElements.add(columnName + "='" + t.object + "'");
				}
			} else if (t.isComplex) {
				selectElements.add("P" + columnName + " AS " + Utils.removeQuestionMark(t.object));
				explodedElements.add("\nlateral view explode(" + columnName + ") exploded" + columnName
						+ " AS P" + columnName);
			} else {
				selectElements.add(columnName + " AS " + Utils.removeQuestionMark(t.object));
				whereElements.add(columnName + " IS NOT NULL");
			}
		}

		String query = "SELECT " + String.join(", ", selectElements);
		query += " FROM " + tableName;
		if (!explodedElements.isEmpty()) {
			query += " " + String.join(" ", explodedElements);
		}
		if (!whereElements.isEmpty()) {
			query += " WHERE " + String.join(" AND ", whereElements);
		}

		sparkNodeData = sqlContext.sql(query);
	}

	@Override
	public String toString() {
		final StringBuilder str = new StringBuilder("{");
		str.append("WPT node: ");
		for (final TriplePattern tpGroup : tripleGroup) {
			str.append(tpGroup.toString()).append(", ");
		}
		str.append(" }");
		return str.toString();
	}
}
