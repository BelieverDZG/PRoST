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
public class InnerFilterTest extends JavaDataFrameSuiteBase implements Serializable {
	private static final long serialVersionUID = 1329L;
	private static final Encoder<TripleBean> triplesEncoder = Encoders.bean(TripleBean.class);

	@Ignore("Operation not supported.")
	@Test
	public void queryTest1() throws Exception {
		final DatabaseStatistics statistics = new DatabaseStatistics("queryTestInnerFilter1_db");
		initializeDb1(statistics);
		queryOnTT1(statistics);
		queryOnVp1(statistics);
		queryOnWpt1(statistics);
		queryOnIwpt1(statistics);
		queryOnJwptOuter1(statistics);
		queryOnJwptLeftOuter1(statistics);
	}

	private void queryOnTT1(final DatabaseStatistics statistics) throws Exception {
		final Settings settings = new Settings.Builder("queryTestInnerFilter1_db").usingTTNodes().usingCharacteristicSets().build();
		final ClassLoader classLoader = getClass().getClassLoader();
		final Query query = new Query(classLoader.getResource("queryTestInnerFilter1.q").getPath(), statistics, settings);

		//EXPECTED
		StructType schema = DataTypes.createStructType(new StructField[]{
				DataTypes.createStructField("num", DataTypes.StringType, true),
				DataTypes.createStructField("a", DataTypes.StringType, true),
		});
		Row row1 = RowFactory.create("<http://example.org/secondnumber>", "5.0");
		List<Row> rowList = ImmutableList.of(row1);
		Dataset<Row> expectedResult = spark().createDataFrame(rowList, schema);

		//ACTUAL
		final Dataset<Row> actualResult = query.compute(spark().sqlContext()).orderBy("num", "a");
		final Dataset<Row> nullableActualResult = sqlContext().createDataFrame(actualResult.collectAsList(),
				actualResult.schema().asNullable());
		System.out.print("InnerFilterTest: queryTest1");
		expectedResult.printSchema();
		expectedResult.show();

		nullableActualResult.printSchema();
		nullableActualResult.show();
		assertDataFrameEquals(expectedResult, nullableActualResult);
	}

	private void queryOnVp1(final DatabaseStatistics statistics) throws Exception {
		final Settings settings = new Settings.Builder("queryTestInnerFilter1_db").usingVPNodes().build();
		final ClassLoader classLoader = getClass().getClassLoader();
		final Query query = new Query(classLoader.getResource("queryTestInnerFilter1.q").getPath(), statistics, settings);

		//EXPECTED
		StructType schema = DataTypes.createStructType(new StructField[]{
				DataTypes.createStructField("num", DataTypes.StringType, true),
				DataTypes.createStructField("a", DataTypes.StringType, true),
		});
		Row row1 = RowFactory.create("<http://example.org/secondnumber>", "5.0");
		List<Row> rowList = ImmutableList.of(row1);
		Dataset<Row> expectedResult = spark().createDataFrame(rowList, schema);

		//ACTUAL
		final Dataset<Row> actualResult = query.compute(spark().sqlContext()).orderBy("num", "a");
		final Dataset<Row> nullableActualResult = sqlContext().createDataFrame(actualResult.collectAsList(),
				actualResult.schema().asNullable());

		assertDataFrameEquals(expectedResult, nullableActualResult);
	}

	private void queryOnWpt1(final DatabaseStatistics statistics) throws Exception {
		final Settings settings = new Settings.Builder("queryTestInnerFilter1_db").usingWPTNodes().build();
		final ClassLoader classLoader = getClass().getClassLoader();
		final Query query = new Query(classLoader.getResource("queryTestInnerFilter1.q").getPath(), statistics, settings);

		//EXPECTED
		StructType schema = DataTypes.createStructType(new StructField[]{
				DataTypes.createStructField("num", DataTypes.StringType, true),
				DataTypes.createStructField("a", DataTypes.StringType, true),
		});
		Row row1 = RowFactory.create("<http://example.org/secondnumber>", "5.0");
		List<Row> rowList = ImmutableList.of(row1);
		Dataset<Row> expectedResult = spark().createDataFrame(rowList, schema);

		//ACTUAL
		final Dataset<Row> actualResult = query.compute(spark().sqlContext()).orderBy("num", "a");
		final Dataset<Row> nullableActualResult = sqlContext().createDataFrame(actualResult.collectAsList(),
				actualResult.schema().asNullable());

		assertDataFrameEquals(expectedResult, nullableActualResult);
	}

	private void queryOnIwpt1(final DatabaseStatistics statistics) throws Exception {
		final Settings settings = new Settings.Builder("queryTestInnerFilter1_db").usingIWPTNodes().build();
		final ClassLoader classLoader = getClass().getClassLoader();
		final Query query = new Query(classLoader.getResource("queryTestInnerFilter1.q").getPath(), statistics, settings);

		//EXPECTED
		StructType schema = DataTypes.createStructType(new StructField[]{
				DataTypes.createStructField("num", DataTypes.StringType, true),
				DataTypes.createStructField("a", DataTypes.StringType, true),
		});
		Row row1 = RowFactory.create("<http://example.org/secondnumber>", "5.0");
		List<Row> rowList = ImmutableList.of(row1);
		Dataset<Row> expectedResult = spark().createDataFrame(rowList, schema);

		//ACTUAL
		final Dataset<Row> actualResult = query.compute(spark().sqlContext()).orderBy("num", "a");
		final Dataset<Row> nullableActualResult = sqlContext().createDataFrame(actualResult.collectAsList(),
				actualResult.schema().asNullable());

		assertDataFrameEquals(expectedResult, nullableActualResult);
	}

	private void queryOnJwptOuter1(final DatabaseStatistics statistics) throws Exception {
		final Settings settings = new Settings.Builder("queryTestInnerFilter1_db").usingJWPTOuterNodes().build();
		final ClassLoader classLoader = getClass().getClassLoader();
		final Query query = new Query(classLoader.getResource("queryTestInnerFilter1.q").getPath(), statistics, settings);

		//EXPECTED
		StructType schema = DataTypes.createStructType(new StructField[]{
				DataTypes.createStructField("num", DataTypes.StringType, true),
				DataTypes.createStructField("a", DataTypes.StringType, true),
		});
		Row row1 = RowFactory.create("<http://example.org/secondnumber>", "5.0");
		List<Row> rowList = ImmutableList.of(row1);
		Dataset<Row> expectedResult = spark().createDataFrame(rowList, schema);

		//ACTUAL
		final Dataset<Row> actualResult = query.compute(spark().sqlContext()).orderBy("num", "a");
		final Dataset<Row> nullableActualResult = sqlContext().createDataFrame(actualResult.collectAsList(),
				actualResult.schema().asNullable());

		assertDataFrameEquals(expectedResult, nullableActualResult);
	}

	private void queryOnJwptLeftOuter1(final DatabaseStatistics statistics) throws Exception {
		final Settings settings = new Settings.Builder("queryTestInnerFilter1_db").usingJWPTLeftouterNodes().build();
		final ClassLoader classLoader = getClass().getClassLoader();
		final Query query = new Query(classLoader.getResource("queryTestInnerFilter1.q").getPath(), statistics, settings);

		//EXPECTED
		StructType schema = DataTypes.createStructType(new StructField[]{
				DataTypes.createStructField("num", DataTypes.StringType, true),
				DataTypes.createStructField("a", DataTypes.StringType, true),
		});
		Row row1 = RowFactory.create("<http://example.org/secondnumber>", "5.0");
		List<Row> rowList = ImmutableList.of(row1);
		Dataset<Row> expectedResult = spark().createDataFrame(rowList, schema);

		//ACTUAL
		final Dataset<Row> actualResult = query.compute(spark().sqlContext()).orderBy("num", "a");
		final Dataset<Row> nullableActualResult = sqlContext().createDataFrame(actualResult.collectAsList(),
				actualResult.schema().asNullable());

		assertDataFrameEquals(expectedResult, nullableActualResult);
	}

	private void initializeDb1(final DatabaseStatistics statistics) {
		spark().sql("DROP DATABASE IF EXISTS queryTestInnerFilter1_db CASCADE");
		spark().sql("CREATE DATABASE IF NOT EXISTS  queryTestInnerFilter1_db");
		spark().sql("USE queryTestInnerFilter1_db");

		// creates test tt table
		final TripleBean t1 = new TripleBean();
		t1.setS("<http://example.org/firstnumber>");
		t1.setP("<http://example.org/firstnum>");
		t1.setO("2");

		final TripleBean t2 = new TripleBean();
		t2.setS("<http://example.org/firstnumber>");
		t2.setP("<http://example.org/firstnum>");
		t2.setO("2");

		final TripleBean t3 = new TripleBean();
		t3.setS("<http://example.org/firstnumber>");
		t3.setP("<http://example.org/firstnum>");
		t3.setO("3");

		final TripleBean t4 = new TripleBean();
		t4.setS("<http://example.org/secondnumber>");
		t4.setP("<http://example.org/secondnum>");
		t4.setO("5.0");

		final TripleBean t5 = new TripleBean();
		t5.setS("<http://example.org/secondnumber>");
		t5.setP("<http://example.org/secondnum>");
		t5.setO("6.0");

		final ArrayList<TripleBean> triplesList = new ArrayList<>();
		triplesList.add(t1);
		triplesList.add(t2);
		triplesList.add(t3);
		triplesList.add(t4);
		triplesList.add(t5);

		final Dataset<Row> ttDataset = spark().createDataset(triplesList, triplesEncoder).select("s", "p", "o").orderBy(
				"s", "p", "o");
		ttDataset.write().saveAsTable("tripletable");

		final loader.Settings loaderSettings =
				new loader.Settings.Builder("queryTestInnerFilter1_db").withInputPath((System.getProperty(
						"user.dir") + "\\target\\test_output\\InnerFilterTest").replace('\\', '/'))
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

	@Ignore("Operation not supported")
	@Test
	public void queryTest2() throws Exception {
		final DatabaseStatistics statistics = new DatabaseStatistics("queryTestInnerFilter2_db");
		initializeDb2(statistics);
		queryOnTT2(statistics);
		queryOnVp2(statistics);
		queryOnWpt2(statistics);
		queryOnIwpt2(statistics);
		queryOnJwptOuter2(statistics);
		queryOnJwptLeftOuter2(statistics);
	}

	private void queryOnTT2(final DatabaseStatistics statistics) throws Exception {
		final Settings settings = new Settings.Builder("queryTestInnerFilter2_db").usingTTNodes().usingCharacteristicSets().build();
		final ClassLoader classLoader = getClass().getClassLoader();
		final Query query = new Query(classLoader.getResource("queryTestInnerFilter2.q").getPath(), statistics, settings);

		//EXPECTED
		StructType schema = DataTypes.createStructType(new StructField[]{
				DataTypes.createStructField("num", DataTypes.StringType, true),
				DataTypes.createStructField("a", DataTypes.StringType, true),
		});
		Row row1 = RowFactory.create("<http://example.org/secondnumber>", "5.0");
		Row row2 = RowFactory.create("<http://example.org/firstnumber>", "2");
		List<Row> rowList = ImmutableList.of(row1, row2);
		Dataset<Row> expectedResult = spark().createDataFrame(rowList, schema);

		//ACTUAL
		final Dataset<Row> actualResult = query.compute(spark().sqlContext()).orderBy("num", "a");
		final Dataset<Row> nullableActualResult = sqlContext().createDataFrame(actualResult.collectAsList(),
				actualResult.schema().asNullable());
		System.out.print("InnerFilterTest: queryTest2");
		expectedResult.printSchema();
		expectedResult.show();

		nullableActualResult.printSchema();
		nullableActualResult.show();
		assertDataFrameEquals(expectedResult, nullableActualResult);
	}

	private void queryOnVp2(final DatabaseStatistics statistics) throws Exception {
		final Settings settings = new Settings.Builder("queryTestInnerFilter2_db").usingVPNodes().build();
		final ClassLoader classLoader = getClass().getClassLoader();
		final Query query = new Query(classLoader.getResource("queryTestInnerFilter2.q").getPath(), statistics, settings);

		//EXPECTED
		StructType schema = DataTypes.createStructType(new StructField[]{
				DataTypes.createStructField("num", DataTypes.StringType, true),
				DataTypes.createStructField("a", DataTypes.StringType, true),
		});
		Row row1 = RowFactory.create("<http://example.org/secondnumber>", "5.0");
		Row row2 = RowFactory.create("<http://example.org/firstnumber>", "2");
		List<Row> rowList = ImmutableList.of(row1, row2);
		Dataset<Row> expectedResult = spark().createDataFrame(rowList, schema);

		//ACTUAL
		final Dataset<Row> actualResult = query.compute(spark().sqlContext()).orderBy("num", "a");
		final Dataset<Row> nullableActualResult = sqlContext().createDataFrame(actualResult.collectAsList(),
				actualResult.schema().asNullable());

		assertDataFrameEquals(expectedResult, nullableActualResult);
	}

	private void queryOnWpt2(final DatabaseStatistics statistics) throws Exception {
		final Settings settings = new Settings.Builder("queryTestInnerFilter2_db").usingWPTNodes().build();
		final ClassLoader classLoader = getClass().getClassLoader();
		final Query query = new Query(classLoader.getResource("queryTestInnerFilter2.q").getPath(), statistics, settings);

		//EXPECTED
		StructType schema = DataTypes.createStructType(new StructField[]{
				DataTypes.createStructField("num", DataTypes.StringType, true),
				DataTypes.createStructField("a", DataTypes.StringType, true),
		});
		Row row1 = RowFactory.create("<http://example.org/secondnumber>", "5.0");
		Row row2 = RowFactory.create("<http://example.org/firstnumber>", "2");
		List<Row> rowList = ImmutableList.of(row1, row2);
		Dataset<Row> expectedResult = spark().createDataFrame(rowList, schema);

		//ACTUAL
		final Dataset<Row> actualResult = query.compute(spark().sqlContext()).orderBy("num", "a");
		final Dataset<Row> nullableActualResult = sqlContext().createDataFrame(actualResult.collectAsList(),
				actualResult.schema().asNullable());

		assertDataFrameEquals(expectedResult, nullableActualResult);
	}

	private void queryOnIwpt2(final DatabaseStatistics statistics) throws Exception {
		final Settings settings = new Settings.Builder("queryTestInnerFilter2_db").usingIWPTNodes().build();
		final ClassLoader classLoader = getClass().getClassLoader();
		final Query query = new Query(classLoader.getResource("queryTestInnerFilter2.q").getPath(), statistics, settings);

		//EXPECTED
		StructType schema = DataTypes.createStructType(new StructField[]{
				DataTypes.createStructField("num", DataTypes.StringType, true),
				DataTypes.createStructField("a", DataTypes.StringType, true),
		});
		Row row1 = RowFactory.create("<http://example.org/secondnumber>", "5.0");
		Row row2 = RowFactory.create("<http://example.org/firstnumber>", "2");
		List<Row> rowList = ImmutableList.of(row1, row2);
		Dataset<Row> expectedResult = spark().createDataFrame(rowList, schema);

		//ACTUAL
		final Dataset<Row> actualResult = query.compute(spark().sqlContext()).orderBy("num", "a");
		final Dataset<Row> nullableActualResult = sqlContext().createDataFrame(actualResult.collectAsList(),
				actualResult.schema().asNullable());

		assertDataFrameEquals(expectedResult, nullableActualResult);
	}

	private void queryOnJwptOuter2(final DatabaseStatistics statistics) throws Exception {
		final Settings settings = new Settings.Builder("queryTestInnerFilter2_db").usingJWPTOuterNodes().build();
		final ClassLoader classLoader = getClass().getClassLoader();
		final Query query = new Query(classLoader.getResource("queryTestInnerFilter2.q").getPath(), statistics, settings);

		//EXPECTED
		StructType schema = DataTypes.createStructType(new StructField[]{
				DataTypes.createStructField("num", DataTypes.StringType, true),
				DataTypes.createStructField("a", DataTypes.StringType, true),
		});
		Row row1 = RowFactory.create("<http://example.org/secondnumber>", "5.0");
		Row row2 = RowFactory.create("<http://example.org/firstnumber>", "2");
		List<Row> rowList = ImmutableList.of(row1, row2);
		Dataset<Row> expectedResult = spark().createDataFrame(rowList, schema);

		//ACTUAL
		final Dataset<Row> actualResult = query.compute(spark().sqlContext()).orderBy("num", "a");
		final Dataset<Row> nullableActualResult = sqlContext().createDataFrame(actualResult.collectAsList(),
				actualResult.schema().asNullable());

		assertDataFrameEquals(expectedResult, nullableActualResult);
	}

	private void queryOnJwptLeftOuter2(final DatabaseStatistics statistics) throws Exception {
		final Settings settings = new Settings.Builder("queryTestInnerFilter2_db").usingJWPTLeftouterNodes().build();
		final ClassLoader classLoader = getClass().getClassLoader();
		final Query query = new Query(classLoader.getResource("queryTestInnerFilter2.q").getPath(), statistics, settings);

		//EXPECTED
		StructType schema = DataTypes.createStructType(new StructField[]{
				DataTypes.createStructField("num", DataTypes.StringType, true),
				DataTypes.createStructField("a", DataTypes.StringType, true),
		});
		Row row1 = RowFactory.create("<http://example.org/secondnumber>", "5.0");
		Row row2 = RowFactory.create("<http://example.org/firstnumber>", "2");
		List<Row> rowList = ImmutableList.of(row1, row2);
		Dataset<Row> expectedResult = spark().createDataFrame(rowList, schema);

		//ACTUAL
		final Dataset<Row> actualResult = query.compute(spark().sqlContext()).orderBy("num", "a");
		final Dataset<Row> nullableActualResult = sqlContext().createDataFrame(actualResult.collectAsList(),
				actualResult.schema().asNullable());

		assertDataFrameEquals(expectedResult, nullableActualResult);
	}

	private void initializeDb2(final DatabaseStatistics statistics) {
		spark().sql("DROP DATABASE IF EXISTS queryTestInnerFilter2_db CASCADE");
		spark().sql("CREATE DATABASE IF NOT EXISTS  queryTestInnerFilter2_db");
		spark().sql("USE queryTestInnerFilter2_db");

		// creates test tt table
		final TripleBean t1 = new TripleBean();
		t1.setS("<http://example.org/firstnumber>");
		t1.setP("<http://example.org/firstnum>");
		t1.setO("2");

		final TripleBean t2 = new TripleBean();
		t2.setS("<http://example.org/firstnumber>");
		t2.setP("<http://example.org/firstnum>");
		t2.setO("2");

		final TripleBean t3 = new TripleBean();
		t3.setS("<http://example.org/firstnumber>");
		t3.setP("<http://example.org/firstnum>");
		t3.setO("3");

		final TripleBean t4 = new TripleBean();
		t4.setS("<http://example.org/secondnumber>");
		t4.setP("<http://example.org/secondnum>");
		t4.setO("5.0");

		final TripleBean t5 = new TripleBean();
		t5.setS("<http://example.org/secondnumber>");
		t5.setP("<http://example.org/secondnum>");
		t5.setO("6.0");

		final ArrayList<TripleBean> triplesList = new ArrayList<>();
		triplesList.add(t1);
		triplesList.add(t2);
		triplesList.add(t3);
		triplesList.add(t4);
		triplesList.add(t5);

		final Dataset<Row> ttDataset = spark().createDataset(triplesList, triplesEncoder).select("s", "p", "o").orderBy(
				"s", "p", "o");
		ttDataset.write().saveAsTable("tripletable");

		final loader.Settings loaderSettings =
				new loader.Settings.Builder("queryTestInnerFilter2_db").withInputPath((System.getProperty(
						"user.dir") + "\\target\\test_output\\InnerFilterTest").replace('\\', '/'))
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
ex:firstnumber		| ex:firstnum		| "2"
ex:firstnumber		| ex:firstnum		| "2"
ex:firstnumber		| ex:firstnum		| "3"

ex:secondnumber		| ex:secondnum		| "5.0"
ex:secondnumber		| ex:secondnum		| "6.0"
================================================================================================================

-----------------------------------------------------------------------------------------------------------------

SELECT * 
WHERE {
        ?num <http://example.org/firstnum> ?a
        FILTER NOT EXISTS {
                ?num <http://example.org/secondnum> ?b.
                FILTER(?a = ?b)
        }
}

-----------------------------------------------------------------------------------------------------------------
RESULT:
-----------------------------------------------------------------------------------------------------------------
Expected:
+----------------------------------+----+
|                               num|   a|
+----------------------------------+----+
| <http://example.org/secondnumber>| 5.0|
+----------------------------------+----+

Actual:

-----------------------------------------------------------------------------------------------------------------

SELECT * 
WHERE {
        ?num <http://example.org/firstnum> ?a
        MINUS {
                ?num <http://example.org/secondnum> ?b.
                FILTER(?a = ?b)
        }
}

-----------------------------------------------------------------------------------------------------------------
RESULT:
-----------------------------------------------------------------------------------------------------------------
Expected:
+----------------------------------+----+
|                               num|   a|
+----------------------------------+----+
| <http://example.org/secondnumber>| 5.0|
|  <http://example.org/firstnumber>|   2|
+----------------------------------+----+

Actual:


-----------------------------------------------------------------------------------------------------------------

*/
