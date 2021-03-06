package query.run;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.holdenkarau.spark.testing.JavaDataFrameSuiteBase;
import loader.InverseWidePropertyTableLoader;
import loader.JoinedWidePropertyTableLoader;
import loader.VerticalPartitioningLoader;
import loader.WidePropertyTableLoader;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoder;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.junit.Ignore;
import org.junit.Test;
import org.spark_project.guava.collect.ImmutableList;
import query.utilities.TripleBean;
import statistics.DatabaseStatistics;
import translator.Query;
import utils.Settings;

/**
 * This class tests represents the highest level of testing, i.e. given a query
 * it checks that results are correctly and consistently returned according to
 * ALL supported logical partitioning strategies (at the moment WPT, IWPT, JWPT,
 * and VP?), i.e. these tests verify are about SPARQL semantics.
 *
 * @author Kristin Plettau
 */
public class FilterIsIntegerTest extends JavaDataFrameSuiteBase implements Serializable {
	private static final long serialVersionUID = 1329L;
	private static final Encoder<TripleBean> triplesEncoder = Encoders.bean(TripleBean.class);

	@Ignore("Operation not supported.")
	@Test
	public void queryTest2() throws Exception {
		final DatabaseStatistics statistics = new DatabaseStatistics("queryTestFilterIsInteger1_db");
		initializeDb2(statistics);
		queryOnTT2(statistics);
		queryOnVp2(statistics);
		queryOnWpt2(statistics);
		queryOnIwpt2(statistics);
		queryOnJwptOuter2(statistics);
		queryOnJwptLeftOuter2(statistics);
	}

	private void queryOnTT2(final DatabaseStatistics statistics) throws Exception {
		final Settings settings = new Settings.Builder("queryTestFilterIsInteger1_db").usingTTNodes().usingCharacteristicSets().build();
		final ClassLoader classLoader = getClass().getClassLoader();
		final Query query = new Query(classLoader.getResource("queryTestFilterIsInteger1.q").getPath(), statistics, settings);

		//EXPECTED
		StructType schema = DataTypes.createStructType(new StructField[]{
				DataTypes.createStructField("name", DataTypes.StringType, true),
				DataTypes.createStructField("age", DataTypes.StringType, true),
		});
		Row row1 = RowFactory.create("A", "20");
		List<Row> rowList = ImmutableList.of(row1);
		Dataset<Row> expectedResult = spark().createDataFrame(rowList, schema);

		//ACTUAL
		final Dataset<Row> actualResult = query.compute(spark().sqlContext()).orderBy("name", "age");
		final Dataset<Row> nullableActualResult = sqlContext().createDataFrame(actualResult.collectAsList(),
				actualResult.schema().asNullable());
		System.out.print("FilterIsIntegerTest: queryTest1");
		expectedResult.printSchema();
		expectedResult.show();

		nullableActualResult.printSchema();
		nullableActualResult.show();
		assertDataFrameEquals(expectedResult, nullableActualResult);
	}

	private void queryOnVp2(final DatabaseStatistics statistics) throws Exception {
		final Settings settings = new Settings.Builder("queryTestFilterIsInteger1_db").usingVPNodes().build();
		final ClassLoader classLoader = getClass().getClassLoader();
		final Query query = new Query(classLoader.getResource("queryTestFilterIsInteger1.q").getPath(), statistics, settings);

		//EXPECTED
		StructType schema = DataTypes.createStructType(new StructField[]{
				DataTypes.createStructField("name", DataTypes.StringType, true),
				DataTypes.createStructField("age", DataTypes.StringType, true),
		});
		Row row1 = RowFactory.create("A", "20");
		List<Row> rowList = ImmutableList.of(row1);
		Dataset<Row> expectedResult = spark().createDataFrame(rowList, schema);

		//ACTUAL
		final Dataset<Row> actualResult = query.compute(spark().sqlContext()).orderBy("name", "age");
		final Dataset<Row> nullableActualResult = sqlContext().createDataFrame(actualResult.collectAsList(),
				actualResult.schema().asNullable());

		assertDataFrameEquals(expectedResult, nullableActualResult);
	}

	private void queryOnWpt2(final DatabaseStatistics statistics) throws Exception {
		final Settings settings = new Settings.Builder("queryTestFilterIsInteger1_db").usingWPTNodes().build();
		final ClassLoader classLoader = getClass().getClassLoader();
		final Query query = new Query(classLoader.getResource("queryTestFilterIsInteger1.q").getPath(), statistics, settings);

		//EXPECTED
		StructType schema = DataTypes.createStructType(new StructField[]{
				DataTypes.createStructField("name", DataTypes.StringType, true),
				DataTypes.createStructField("age", DataTypes.StringType, true),
		});
		Row row1 = RowFactory.create("A", "20");
		List<Row> rowList = ImmutableList.of(row1);
		Dataset<Row> expectedResult = spark().createDataFrame(rowList, schema);

		//ACTUAL
		final Dataset<Row> actualResult = query.compute(spark().sqlContext()).orderBy("name", "age");
		final Dataset<Row> nullableActualResult = sqlContext().createDataFrame(actualResult.collectAsList(),
				actualResult.schema().asNullable());

		assertDataFrameEquals(expectedResult, nullableActualResult);
	}

	private void queryOnIwpt2(final DatabaseStatistics statistics) throws Exception {
		final Settings settings = new Settings.Builder("queryTestFilterIsInteger1_db").usingIWPTNodes().build();
		final ClassLoader classLoader = getClass().getClassLoader();
		final Query query = new Query(classLoader.getResource("queryTestFilterIsInteger1.q").getPath(), statistics, settings);

		//EXPECTED
		StructType schema = DataTypes.createStructType(new StructField[]{
				DataTypes.createStructField("name", DataTypes.StringType, true),
				DataTypes.createStructField("age", DataTypes.StringType, true),
		});
		Row row1 = RowFactory.create("A", "20");
		List<Row> rowList = ImmutableList.of(row1);
		Dataset<Row> expectedResult = spark().createDataFrame(rowList, schema);

		//ACTUAL
		final Dataset<Row> actualResult = query.compute(spark().sqlContext()).orderBy("name", "age");
		final Dataset<Row> nullableActualResult = sqlContext().createDataFrame(actualResult.collectAsList(),
				actualResult.schema().asNullable());

		assertDataFrameEquals(expectedResult, nullableActualResult);
	}

	private void queryOnJwptOuter2(final DatabaseStatistics statistics) throws Exception {
		final Settings settings = new Settings.Builder("queryTestFilterIsInteger1_db").usingJWPTOuterNodes().build();
		final ClassLoader classLoader = getClass().getClassLoader();
		final Query query = new Query(classLoader.getResource("queryTestFilterIsInteger1.q").getPath(), statistics, settings);

		//EXPECTED
		StructType schema = DataTypes.createStructType(new StructField[]{
				DataTypes.createStructField("name", DataTypes.StringType, true),
				DataTypes.createStructField("age", DataTypes.StringType, true),
		});
		Row row1 = RowFactory.create("A", "20");
		List<Row> rowList = ImmutableList.of(row1);
		Dataset<Row> expectedResult = spark().createDataFrame(rowList, schema);

		//ACTUAL
		final Dataset<Row> actualResult = query.compute(spark().sqlContext()).orderBy("name", "age");
		final Dataset<Row> nullableActualResult = sqlContext().createDataFrame(actualResult.collectAsList(),
				actualResult.schema().asNullable());

		assertDataFrameEquals(expectedResult, nullableActualResult);
	}

	private void queryOnJwptLeftOuter2(final DatabaseStatistics statistics) throws Exception {
		final Settings settings = new Settings.Builder("queryTestFilterIsInteger1_db").usingJWPTLeftouterNodes().build();
		final ClassLoader classLoader = getClass().getClassLoader();
		final Query query = new Query(classLoader.getResource("queryTestFilterIsInteger1.q").getPath(), statistics, settings);

		//EXPECTED
		StructType schema = DataTypes.createStructType(new StructField[]{
				DataTypes.createStructField("name", DataTypes.StringType, true),
				DataTypes.createStructField("age", DataTypes.StringType, true),
		});
		Row row1 = RowFactory.create("A", "20");
		List<Row> rowList = ImmutableList.of(row1);
		Dataset<Row> expectedResult = spark().createDataFrame(rowList, schema);

		//ACTUAL
		final Dataset<Row> actualResult = query.compute(spark().sqlContext()).orderBy("name", "age");
		final Dataset<Row> nullableActualResult = sqlContext().createDataFrame(actualResult.collectAsList(),
				actualResult.schema().asNullable());

		assertDataFrameEquals(expectedResult, nullableActualResult);
	}

	private void initializeDb2(final DatabaseStatistics statistics) {
		spark().sql("DROP DATABASE IF EXISTS queryTestFilterIsInteger1_db CASCADE");
		spark().sql("CREATE DATABASE IF NOT EXISTS  queryTestFilterIsInteger1_db");
		spark().sql("USE queryTestFilterIsInteger1_db");

		// creates test tt table
		final TripleBean t1 = new TripleBean();
		t1.setS("<http://example.org/name1>");
		t1.setP("<http://example.org/name>");
		t1.setO("A");

		final TripleBean t2 = new TripleBean();
		t2.setS("<http://example.org/name2>");
		t2.setP("<http://example.org/name>");
		t2.setO("B");

		final TripleBean t3 = new TripleBean();
		t3.setS("<http://example.org/name1>");
		t3.setP("<http://example.org/age>");
		t3.setO("'20'^^<http://example.org/integer>");

		final TripleBean t4 = new TripleBean();
		t4.setS("<http://example.org/name2>");
		t4.setP("<http://example.org/age>");
		t4.setO("'30.5'^^<http://example.org/float>");

		final ArrayList<TripleBean> triplesList = new ArrayList<>();
		triplesList.add(t1);
		triplesList.add(t2);
		triplesList.add(t3);
		triplesList.add(t4);

		final Dataset<Row> ttDataset = spark().createDataset(triplesList, triplesEncoder).select("s", "p", "o").orderBy(
				"s", "p", "o");
		ttDataset.write().saveAsTable("tripletable");

		final loader.Settings loaderSettings =
				new loader.Settings.Builder("queryTestFilterIsInteger1_db").withInputPath((System.getProperty(
						"user.dir") + "\\target\\test_output\\FilterIsIntegerTest").replace('\\', '/'))
						.generateVp().generateWpt().generateIwpt().generateJwptOuter()
						.generateJwptLeftOuter().generateJwptInner().build();

		final VerticalPartitioningLoader vpLoader = new VerticalPartitioningLoader(loaderSettings, spark(), statistics);
		vpLoader.load();

		statistics.computePropertyStatistics(spark());

		final WidePropertyTableLoader wptLoader = new WidePropertyTableLoader(loaderSettings, spark(), statistics);
		wptLoader.load();

		final InverseWidePropertyTableLoader iwptLoader = new InverseWidePropertyTableLoader(loaderSettings, spark(),
				statistics);
		iwptLoader.load();

		final JoinedWidePropertyTableLoader jwptOuterLoader = new JoinedWidePropertyTableLoader(loaderSettings,
				spark(), JoinedWidePropertyTableLoader.JoinType.outer, statistics);
		jwptOuterLoader.load();

		final JoinedWidePropertyTableLoader jwptLeftOuterLoader = new JoinedWidePropertyTableLoader(loaderSettings,
				spark(), JoinedWidePropertyTableLoader.JoinType.leftouter, statistics);
		jwptLeftOuterLoader.load();

		/*final JoinedWidePropertyTableLoader jwptInnerLoader = new JoinedWidePropertyTableLoader(loaderSettings,
				spark(), JoinedWidePropertyTableLoader.JoinType.inner, statistics);
		jwptLeftOuterLoader.load();*/
	}
}

/*
PREFIX ex: <http://example.org/#>.

TABLE:
================================================================================================================
ex:name1		| ex:name			| "A"
ex:name2		| ex:name			| "B"
ex:name1		| ex:age			| "20"^^ex:integer
ex:name2		| ex:age			| "30.5"^^ex:float

================================================================================================================

QUERY:
-----------------------------------------------------------------------------------------------------------------
SELECT ?name ?age
WHERE
{ ?x <http://example.org/name> ?name. 
  ?x <http://example.org/age> ?age. 
  FILTER ( datatype(?age) = <http://example.org/integer> ) }

-----------------------------------------------------------------------------------------------------------------
RESULT:
-----------------------------------------------------------------------------------------------------------------
Expected:
+----+---+
|name|age|
+----+---+
|   A| 20|
+----+---+

Actual:

final TripleBean t1 = new TripleBean();
t1.setS("<http://example.org/name2>");
t1.setP("<http://example.org/age>");
t1.setO("30.5"^^<http://example.org/integer>); -> not possible
-----------------------------------------------------------------------------------------------------------------
*/
