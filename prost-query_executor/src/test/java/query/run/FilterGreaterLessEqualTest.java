package query.run;

import com.holdenkarau.spark.testing.JavaDataFrameSuiteBase;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
public class FilterGreaterLessEqualTest extends JavaDataFrameSuiteBase implements Serializable {
	private static final long serialVersionUID = 1329L;
	private static final Encoder<TripleBean> triplesEncoder = Encoders.bean(TripleBean.class);

	
	@Test
	//@Ignore("Optionals are not fully implemented yet.")
	public void queryTest1() throws Exception {
		final DatabaseStatistics statistics = new DatabaseStatistics("queryTestGreaterLessEqual1_db");
		Dataset<Row> fullDataset = initializeDb(statistics);
		fullDataset = fullDataset.orderBy("s", "p", "o");
		queryOnTT(statistics, fullDataset);
		queryOnVp(statistics, fullDataset);
		queryOnWpt(statistics, fullDataset);
		queryOnIwpt(statistics, fullDataset);
		queryOnJwptOuter(statistics, fullDataset);
		queryOnJwptLeftOuter(statistics, fullDataset);
	}	
	private void queryOnTT(final DatabaseStatistics statistics, final Dataset<Row> fullDataset)  throws Exception {
		final Settings settings = new Settings.Builder("queryTestGreaterLessEqual1_db").usingTTNodes().build();
		final ClassLoader classLoader = getClass().getClassLoader();
		
		final Query query = new Query(classLoader.getResource("queryTestFilter1.q").getPath(), statistics, settings);
		

		//EXPECTED
		StructType schema = DataTypes.createStructType(new StructField[]{
				DataTypes.createStructField("title", DataTypes.StringType, true),
				DataTypes.createStructField("price", DataTypes.StringType, true),
				});
		Row row1 = RowFactory.create("Title2", "20");
		List<Row> rowList = ImmutableList.of(row1);
		Dataset<Row> expectedResult = spark().createDataFrame(rowList, schema);
		
		//ACTUAL
		final Dataset<Row> actualResult = query.compute(spark().sqlContext()).orderBy("title", "price");
		final Dataset<Row> nullableActualResult = sqlContext().createDataFrame(actualResult.collectAsList(),
				actualResult.schema().asNullable());
		System.out.print("FilterGreaterLessEqualTest: queryTest1");
		expectedResult.printSchema();
		expectedResult.show();

		nullableActualResult.printSchema();
		nullableActualResult.show();
		assertDataFrameEquals(expectedResult, nullableActualResult);
	}
	
	private void queryOnVp(final DatabaseStatistics statistics, final Dataset<Row> fullDataset)  throws Exception {
		final Settings settings = new Settings.Builder("queryTestGreaterLessEqual1_db").usingVPNodes().build();
		final ClassLoader classLoader = getClass().getClassLoader();
		
		final Query query = new Query(classLoader.getResource("queryTestFilter1.q").getPath(), statistics, settings);
		
		
		//EXPECTED
		StructType schema = DataTypes.createStructType(new StructField[]{
				DataTypes.createStructField("title", DataTypes.StringType, true),
				DataTypes.createStructField("price", DataTypes.StringType, true),
				});
		Row row1 = RowFactory.create("Title2", "20");
		List<Row> rowList = ImmutableList.of(row1);
		Dataset<Row> expectedResult = spark().createDataFrame(rowList, schema);
		
		//ACTUAL
		final Dataset<Row> actualResult = query.compute(spark().sqlContext()).orderBy("title", "price");
		final Dataset<Row> nullableActualResult = sqlContext().createDataFrame(actualResult.collectAsList(),
				actualResult.schema().asNullable());
		
		assertDataFrameEquals(expectedResult, nullableActualResult);
	}

	private void queryOnWpt(final DatabaseStatistics statistics, final Dataset<Row> fullDataset)  throws Exception {
		final Settings settings = new Settings.Builder("queryTestGreaterLessEqual1_db").usingWPTNodes().build();
		final ClassLoader classLoader = getClass().getClassLoader();
		
		final Query query = new Query(classLoader.getResource("queryTestFilter1.q").getPath(), statistics, settings);
		
		
		//EXPECTED
		StructType schema = DataTypes.createStructType(new StructField[]{
				DataTypes.createStructField("title", DataTypes.StringType, true),
				DataTypes.createStructField("price", DataTypes.StringType, true),
				});
		Row row1 = RowFactory.create("Title2", "20");
		List<Row> rowList = ImmutableList.of(row1);
		Dataset<Row> expectedResult = spark().createDataFrame(rowList, schema);
		
		//ACTUAL
		final Dataset<Row> actualResult = query.compute(spark().sqlContext()).orderBy("title", "price");
		final Dataset<Row> nullableActualResult = sqlContext().createDataFrame(actualResult.collectAsList(),
				actualResult.schema().asNullable());		
		
		assertDataFrameEquals(expectedResult, nullableActualResult);
	}

	private void queryOnIwpt(final DatabaseStatistics statistics, final Dataset<Row> fullDataset)  throws Exception {
		final Settings settings = new Settings.Builder("queryTestGreaterLessEqual1_db").usingIWPTNodes().build();
		final ClassLoader classLoader = getClass().getClassLoader();
		
		final Query query = new Query(classLoader.getResource("queryTestFilter1.q").getPath(), statistics, settings);
		

		//EXPECTED
		StructType schema = DataTypes.createStructType(new StructField[]{
				DataTypes.createStructField("title", DataTypes.StringType, true),
				DataTypes.createStructField("price", DataTypes.StringType, true),
				});
		Row row1 = RowFactory.create("Title2", "20");
		List<Row> rowList = ImmutableList.of(row1);
		Dataset<Row> expectedResult = spark().createDataFrame(rowList, schema);
		
		//ACTUAL
		final Dataset<Row> actualResult = query.compute(spark().sqlContext()).orderBy("title", "price");
		final Dataset<Row> nullableActualResult = sqlContext().createDataFrame(actualResult.collectAsList(),
				actualResult.schema().asNullable());
		
		assertDataFrameEquals(expectedResult, nullableActualResult);
	}

	private void queryOnJwptOuter(final DatabaseStatistics statistics, final Dataset<Row> fullDataset)  throws Exception {
		final Settings settings = new Settings.Builder("queryTestGreaterLessEqual1_db").usingJWPTOuterNodes().build();
		final ClassLoader classLoader = getClass().getClassLoader();
		
		final Query query = new Query(classLoader.getResource("queryTestFilter1.q").getPath(), statistics, settings);
		
		
		//EXPECTED
		StructType schema = DataTypes.createStructType(new StructField[]{
				DataTypes.createStructField("title", DataTypes.StringType, true),
				DataTypes.createStructField("price", DataTypes.StringType, true),
				});
		Row row1 = RowFactory.create("Title2", "20");
		List<Row> rowList = ImmutableList.of(row1);
		Dataset<Row> expectedResult = spark().createDataFrame(rowList, schema);
		
		//ACTUAL
		final Dataset<Row> actualResult = query.compute(spark().sqlContext()).orderBy("title", "price");
		final Dataset<Row> nullableActualResult = sqlContext().createDataFrame(actualResult.collectAsList(),
				actualResult.schema().asNullable());
		
		assertDataFrameEquals(expectedResult, nullableActualResult);
	}

	private void queryOnJwptLeftOuter(final DatabaseStatistics statistics, final Dataset<Row> fullDataset)  throws Exception {
		final Settings settings = new Settings.Builder("queryTestGreaterLessEqual1_db").usingJWPTLeftouterNodes().build();
		final ClassLoader classLoader = getClass().getClassLoader();
		
		final Query query = new Query(classLoader.getResource("queryTestFilter1.q").getPath(), statistics, settings);
		
		
		//EXPECTED
		StructType schema = DataTypes.createStructType(new StructField[]{
				DataTypes.createStructField("title", DataTypes.StringType, true),
				DataTypes.createStructField("price", DataTypes.StringType, true),
				});
		Row row1 = RowFactory.create("Title2", "20");
		List<Row> rowList = ImmutableList.of(row1);
		Dataset<Row> expectedResult = spark().createDataFrame(rowList, schema);
		
		//ACTUAL
		final Dataset<Row> actualResult = query.compute(spark().sqlContext()).orderBy("title", "price");
		final Dataset<Row> nullableActualResult = sqlContext().createDataFrame(actualResult.collectAsList(),
				actualResult.schema().asNullable());
		
		assertDataFrameEquals(expectedResult, nullableActualResult);
	}

	private Dataset<Row> initializeDb(final DatabaseStatistics statistics) {
		spark().sql("DROP DATABASE IF EXISTS queryTestGreaterLessEqual1_db CASCADE");
		spark().sql("CREATE DATABASE IF NOT EXISTS  queryTestGreaterLessEqual1_db");
		spark().sql("USE queryTestGreaterLessEqual1_db");

				
		// creates test tt table
		final TripleBean t1 = new TripleBean();
		t1.setS("<http://example.org/book1>");
		t1.setP("<http://example.org/title>");
		t1.setO("Title1");
		
		final TripleBean t2 = new TripleBean();
		t2.setS("<http://example.org/book2>");
		t2.setP("<http://example.org/title>");
		t2.setO("Title2");
		
		final TripleBean t3 = new TripleBean();
		t3.setS("<http://example.org/book1>");
		t3.setP("<http://example.org/price>");
		t3.setO("50");
		
		final TripleBean t4 = new TripleBean();
		t4.setS("<http://example.org/book2>");
		t4.setP("<http://example.org/price>");
		t4.setO("20");

		final ArrayList<TripleBean> triplesList = new ArrayList<>();
		triplesList.add(t1);
		triplesList.add(t2);
		triplesList.add(t3);
		triplesList.add(t4);

		final Dataset<Row> ttDataset = spark().createDataset(triplesList, triplesEncoder).select("s", "p", "o").orderBy(
				"s", "p", "o");
		ttDataset.write().saveAsTable("tripletable");
		
		final loader.Settings loaderSettings =
				new loader.Settings.Builder("queryTestGreaterLessEqual1_db").withInputPath((System.getProperty(
						"user.dir") + "\\target\\test_output\\FilterGreaterLessEqualTest").replace('\\', '/'))
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

		final JoinedWidePropertyTableLoader jwptInnerLoader = new JoinedWidePropertyTableLoader(loaderSettings,
				spark(), JoinedWidePropertyTableLoader.JoinType.inner, statistics);
		jwptLeftOuterLoader.load();

		return ttDataset;
	}
	
	@Test
	//@Ignore("Optionals are not fully implemented yet.")
	public void queryTest2() throws Exception {
		final DatabaseStatistics statistics = new DatabaseStatistics("queryTestGreaterLessEqual2_db");
		Dataset<Row> fullDataset = initializeDb2(statistics);
		fullDataset = fullDataset.orderBy("s", "p", "o");
		queryOnTT2(statistics, fullDataset);
		queryOnVp2(statistics, fullDataset);
		queryOnWpt2(statistics, fullDataset);
		queryOnIwpt2(statistics, fullDataset);
		queryOnJwptOuter2(statistics, fullDataset);
		queryOnJwptLeftOuter2(statistics, fullDataset);
	}	
	private void queryOnTT2(final DatabaseStatistics statistics, final Dataset<Row> fullDataset)  throws Exception {
		final Settings settings = new Settings.Builder("queryTestGreaterLessEqual2_db").usingTTNodes().build();
		final ClassLoader classLoader = getClass().getClassLoader();
		
		final Query query = new Query(classLoader.getResource("queryTestFilter4.q").getPath(), statistics, settings);
		

		//EXPECTED
		StructType schema = DataTypes.createStructType(new StructField[]{
				DataTypes.createStructField("title", DataTypes.StringType, true),
				DataTypes.createStructField("price", DataTypes.StringType, true),
				});
		Row row1 = RowFactory.create("Title1", "50");
		List<Row> rowList = ImmutableList.of(row1);
		Dataset<Row> expectedResult = spark().createDataFrame(rowList, schema);
		
		//ACTUAL
		final Dataset<Row> actualResult = query.compute(spark().sqlContext()).orderBy("title", "price");
		final Dataset<Row> nullableActualResult = sqlContext().createDataFrame(actualResult.collectAsList(),
				actualResult.schema().asNullable());
		System.out.print("FilterGreaterLessEqualTest: queryTest2");
		expectedResult.printSchema();
		expectedResult.show();

		nullableActualResult.printSchema();
		nullableActualResult.show();
		assertDataFrameEquals(expectedResult, nullableActualResult);
	}
	
	private void queryOnVp2(final DatabaseStatistics statistics, final Dataset<Row> fullDataset)  throws Exception {
		final Settings settings = new Settings.Builder("queryTestGreaterLessEqual2_db").usingVPNodes().build();
		final ClassLoader classLoader = getClass().getClassLoader();
		
		final Query query = new Query(classLoader.getResource("queryTestFilter4.q").getPath(), statistics, settings);
		
		
		//EXPECTED
		StructType schema = DataTypes.createStructType(new StructField[]{
				DataTypes.createStructField("title", DataTypes.StringType, true),
				DataTypes.createStructField("price", DataTypes.StringType, true),
				});
		Row row1 = RowFactory.create("Title1", "50");
		List<Row> rowList = ImmutableList.of(row1);
		Dataset<Row> expectedResult = spark().createDataFrame(rowList, schema);
		
		//ACTUAL
		final Dataset<Row> actualResult = query.compute(spark().sqlContext()).orderBy("title", "price");
		final Dataset<Row> nullableActualResult = sqlContext().createDataFrame(actualResult.collectAsList(),
				actualResult.schema().asNullable());
		
		assertDataFrameEquals(expectedResult, nullableActualResult);
	}

	private void queryOnWpt2(final DatabaseStatistics statistics, final Dataset<Row> fullDataset)  throws Exception {
		final Settings settings = new Settings.Builder("queryTestGreaterLessEqual2_db").usingWPTNodes().build();
		final ClassLoader classLoader = getClass().getClassLoader();
		
		final Query query = new Query(classLoader.getResource("queryTestFilter4.q").getPath(), statistics, settings);
		
		
		//EXPECTED
		StructType schema = DataTypes.createStructType(new StructField[]{
				DataTypes.createStructField("title", DataTypes.StringType, true),
				DataTypes.createStructField("price", DataTypes.StringType, true),
				});
		Row row1 = RowFactory.create("Title1", "50");
		List<Row> rowList = ImmutableList.of(row1);
		Dataset<Row> expectedResult = spark().createDataFrame(rowList, schema);
		
		//ACTUAL
		final Dataset<Row> actualResult = query.compute(spark().sqlContext()).orderBy("title", "price");
		final Dataset<Row> nullableActualResult = sqlContext().createDataFrame(actualResult.collectAsList(),
				actualResult.schema().asNullable());		
		
		assertDataFrameEquals(expectedResult, nullableActualResult);
	}

	private void queryOnIwpt2(final DatabaseStatistics statistics, final Dataset<Row> fullDataset)  throws Exception {
		final Settings settings = new Settings.Builder("queryTestGreaterLessEqual2_db").usingIWPTNodes().build();
		final ClassLoader classLoader = getClass().getClassLoader();
		
		final Query query = new Query(classLoader.getResource("queryTestFilter4.q").getPath(), statistics, settings);
		

		//EXPECTED
		StructType schema = DataTypes.createStructType(new StructField[]{
				DataTypes.createStructField("title", DataTypes.StringType, true),
				DataTypes.createStructField("price", DataTypes.StringType, true),
				});
		Row row1 = RowFactory.create("Title1", "50");
		List<Row> rowList = ImmutableList.of(row1);
		Dataset<Row> expectedResult = spark().createDataFrame(rowList, schema);
		
		//ACTUAL
		final Dataset<Row> actualResult = query.compute(spark().sqlContext()).orderBy("title", "price");
		final Dataset<Row> nullableActualResult = sqlContext().createDataFrame(actualResult.collectAsList(),
				actualResult.schema().asNullable());
		
		assertDataFrameEquals(expectedResult, nullableActualResult);
	}

	private void queryOnJwptOuter2(final DatabaseStatistics statistics, final Dataset<Row> fullDataset)  throws Exception {
		final Settings settings = new Settings.Builder("queryTestGreaterLessEqual2_db").usingJWPTOuterNodes().build();
		final ClassLoader classLoader = getClass().getClassLoader();
		
		final Query query = new Query(classLoader.getResource("queryTestFilter4.q").getPath(), statistics, settings);
		
		
		//EXPECTED
		StructType schema = DataTypes.createStructType(new StructField[]{
				DataTypes.createStructField("title", DataTypes.StringType, true),
				DataTypes.createStructField("price", DataTypes.StringType, true),
				});
		Row row1 = RowFactory.create("Title1", "50");
		List<Row> rowList = ImmutableList.of(row1);
		Dataset<Row> expectedResult = spark().createDataFrame(rowList, schema);
		
		//ACTUAL
		final Dataset<Row> actualResult = query.compute(spark().sqlContext()).orderBy("title", "price");
		final Dataset<Row> nullableActualResult = sqlContext().createDataFrame(actualResult.collectAsList(),
				actualResult.schema().asNullable());
		
		assertDataFrameEquals(expectedResult, nullableActualResult);
	}

	private void queryOnJwptLeftOuter2(final DatabaseStatistics statistics, final Dataset<Row> fullDataset)  throws Exception {
		final Settings settings = new Settings.Builder("queryTestGreaterLessEqual2_db").usingJWPTLeftouterNodes().build();
		final ClassLoader classLoader = getClass().getClassLoader();
		
		final Query query = new Query(classLoader.getResource("queryTestFilter4.q").getPath(), statistics, settings);
		
		
		//EXPECTED
		StructType schema = DataTypes.createStructType(new StructField[]{
				DataTypes.createStructField("title", DataTypes.StringType, true),
				DataTypes.createStructField("price", DataTypes.StringType, true),
				});
		Row row1 = RowFactory.create("Title1", "50");
		List<Row> rowList = ImmutableList.of(row1);
		Dataset<Row> expectedResult = spark().createDataFrame(rowList, schema);
		
		//ACTUAL
		final Dataset<Row> actualResult = query.compute(spark().sqlContext()).orderBy("title", "price");
		final Dataset<Row> nullableActualResult = sqlContext().createDataFrame(actualResult.collectAsList(),
				actualResult.schema().asNullable());
		
		assertDataFrameEquals(expectedResult, nullableActualResult);
	}

	private Dataset<Row> initializeDb2(final DatabaseStatistics statistics) {
		spark().sql("DROP DATABASE IF EXISTS queryTestGreaterLessEqual2_db CASCADE");
		spark().sql("CREATE DATABASE IF NOT EXISTS  queryTestGreaterLessEqual2_db");
		spark().sql("USE queryTestGreaterLessEqual2_db");

				
		// creates test tt table
		final TripleBean t1 = new TripleBean();
		t1.setS("<http://example.org/book1>");
		t1.setP("<http://example.org/title>");
		t1.setO("Title1");
		
		final TripleBean t2 = new TripleBean();
		t2.setS("<http://example.org/book2>");
		t2.setP("<http://example.org/title>");
		t2.setO("Title2");
		
		final TripleBean t3 = new TripleBean();
		t3.setS("<http://example.org/book1>");
		t3.setP("<http://example.org/price>");
		t3.setO("50");
		
		final TripleBean t4 = new TripleBean();
		t4.setS("<http://example.org/book2>");
		t4.setP("<http://example.org/price>");
		t4.setO("20");

		final ArrayList<TripleBean> triplesList = new ArrayList<>();
		triplesList.add(t1);
		triplesList.add(t2);
		triplesList.add(t3);
		triplesList.add(t4);

		final Dataset<Row> ttDataset = spark().createDataset(triplesList, triplesEncoder).select("s", "p", "o").orderBy(
				"s", "p", "o");
		ttDataset.write().saveAsTable("tripletable");
		
		final loader.Settings loaderSettings =
				new loader.Settings.Builder("queryTestGreaterLessEqual2_db").withInputPath((System.getProperty(
						"user.dir") + "\\target\\test_output\\FilterGreaterLessEqualTest").replace('\\', '/'))
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

		final JoinedWidePropertyTableLoader jwptInnerLoader = new JoinedWidePropertyTableLoader(loaderSettings,
				spark(), JoinedWidePropertyTableLoader.JoinType.inner, statistics);
		jwptLeftOuterLoader.load();

		return ttDataset;
	}
	
	@Test
	//@Ignore("Optionals are not fully implemented yet.")
	public void queryTest3() throws Exception {
		final DatabaseStatistics statistics = new DatabaseStatistics("queryTestGreaterLessEqual3_db");
		Dataset<Row> fullDataset = initializeDb3(statistics);
		fullDataset = fullDataset.orderBy("s", "p", "o");
		queryOnTT3(statistics, fullDataset);
		queryOnVp3(statistics, fullDataset);
		queryOnWpt3(statistics, fullDataset);
		queryOnIwpt3(statistics, fullDataset);
		queryOnJwptOuter3(statistics, fullDataset);
		queryOnJwptLeftOuter3(statistics, fullDataset);
	}	
	private void queryOnTT3(final DatabaseStatistics statistics, final Dataset<Row> fullDataset)  throws Exception {
		final Settings settings = new Settings.Builder("queryTestGreaterLessEqual3_db").usingTTNodes().build();
		final ClassLoader classLoader = getClass().getClassLoader();
		
		final Query query = new Query(classLoader.getResource("queryTestFilter5.q").getPath(), statistics, settings);
		

		//EXPECTED
		StructType schema = DataTypes.createStructType(new StructField[]{
				DataTypes.createStructField("title", DataTypes.StringType, true),
				DataTypes.createStructField("price", DataTypes.StringType, true),
				});
		Row row1 = RowFactory.create("Title2", "30");
		List<Row> rowList = ImmutableList.of(row1);
		Dataset<Row> expectedResult = spark().createDataFrame(rowList, schema);
		
		//ACTUAL
		final Dataset<Row> actualResult = query.compute(spark().sqlContext()).orderBy("title", "price");
		final Dataset<Row> nullableActualResult = sqlContext().createDataFrame(actualResult.collectAsList(),
				actualResult.schema().asNullable());
		System.out.print("FilterGreaterLessEqualTest: queryTest3");
		expectedResult.printSchema();
		expectedResult.show();

		nullableActualResult.printSchema();
		nullableActualResult.show();
		assertDataFrameEquals(expectedResult, nullableActualResult);
	}
	
	private void queryOnVp3(final DatabaseStatistics statistics, final Dataset<Row> fullDataset)  throws Exception {
		final Settings settings = new Settings.Builder("queryTestGreaterLessEqual3_db").usingVPNodes().build();
		final ClassLoader classLoader = getClass().getClassLoader();
		
		final Query query = new Query(classLoader.getResource("queryTestFilter5.q").getPath(), statistics, settings);
		
		
		//EXPECTED
		StructType schema = DataTypes.createStructType(new StructField[]{
				DataTypes.createStructField("title", DataTypes.StringType, true),
				DataTypes.createStructField("price", DataTypes.StringType, true),
				});
		Row row1 = RowFactory.create("Title2", "30");
		List<Row> rowList = ImmutableList.of(row1);
		Dataset<Row> expectedResult = spark().createDataFrame(rowList, schema);
		
		//ACTUAL
		final Dataset<Row> actualResult = query.compute(spark().sqlContext()).orderBy("title", "price");
		final Dataset<Row> nullableActualResult = sqlContext().createDataFrame(actualResult.collectAsList(),
				actualResult.schema().asNullable());
		
		assertDataFrameEquals(expectedResult, nullableActualResult);
	}

	private void queryOnWpt3(final DatabaseStatistics statistics, final Dataset<Row> fullDataset)  throws Exception {
		final Settings settings = new Settings.Builder("queryTestGreaterLessEqual3_db").usingWPTNodes().build();
		final ClassLoader classLoader = getClass().getClassLoader();
		
		final Query query = new Query(classLoader.getResource("queryTestFilter5.q").getPath(), statistics, settings);
		
		
		//EXPECTED
		StructType schema = DataTypes.createStructType(new StructField[]{
				DataTypes.createStructField("title", DataTypes.StringType, true),
				DataTypes.createStructField("price", DataTypes.StringType, true),
				});
		Row row1 = RowFactory.create("Title2", "30");
		List<Row> rowList = ImmutableList.of(row1);
		Dataset<Row> expectedResult = spark().createDataFrame(rowList, schema);
		
		//ACTUAL
		final Dataset<Row> actualResult = query.compute(spark().sqlContext()).orderBy("title", "price");
		final Dataset<Row> nullableActualResult = sqlContext().createDataFrame(actualResult.collectAsList(),
				actualResult.schema().asNullable());		
		
		assertDataFrameEquals(expectedResult, nullableActualResult);
	}

	private void queryOnIwpt3(final DatabaseStatistics statistics, final Dataset<Row> fullDataset)  throws Exception {
		final Settings settings = new Settings.Builder("queryTestGreaterLessEqual3_db").usingIWPTNodes().build();
		final ClassLoader classLoader = getClass().getClassLoader();
		
		final Query query = new Query(classLoader.getResource("queryTestFilter5.q").getPath(), statistics, settings);
		

		//EXPECTED
		StructType schema = DataTypes.createStructType(new StructField[]{
				DataTypes.createStructField("title", DataTypes.StringType, true),
				DataTypes.createStructField("price", DataTypes.StringType, true),
				});
		Row row1 = RowFactory.create("Title2", "30");
		List<Row> rowList = ImmutableList.of(row1);
		Dataset<Row> expectedResult = spark().createDataFrame(rowList, schema);
		
		//ACTUAL
		final Dataset<Row> actualResult = query.compute(spark().sqlContext()).orderBy("title", "price");
		final Dataset<Row> nullableActualResult = sqlContext().createDataFrame(actualResult.collectAsList(),
				actualResult.schema().asNullable());
		
		assertDataFrameEquals(expectedResult, nullableActualResult);
	}

	private void queryOnJwptOuter3(final DatabaseStatistics statistics, final Dataset<Row> fullDataset)  throws Exception {
		final Settings settings = new Settings.Builder("queryTestGreaterLessEqual3_db").usingJWPTOuterNodes().build();
		final ClassLoader classLoader = getClass().getClassLoader();
		
		final Query query = new Query(classLoader.getResource("queryTestFilter5.q").getPath(), statistics, settings);
		
		
		//EXPECTED
		StructType schema = DataTypes.createStructType(new StructField[]{
				DataTypes.createStructField("title", DataTypes.StringType, true),
				DataTypes.createStructField("price", DataTypes.StringType, true),
				});
		Row row1 = RowFactory.create("Title2", "30");
		List<Row> rowList = ImmutableList.of(row1);
		Dataset<Row> expectedResult = spark().createDataFrame(rowList, schema);
		
		//ACTUAL
		final Dataset<Row> actualResult = query.compute(spark().sqlContext()).orderBy("title", "price");
		final Dataset<Row> nullableActualResult = sqlContext().createDataFrame(actualResult.collectAsList(),
				actualResult.schema().asNullable());
		
		assertDataFrameEquals(expectedResult, nullableActualResult);
	}

	private void queryOnJwptLeftOuter3(final DatabaseStatistics statistics, final Dataset<Row> fullDataset)  throws Exception {
		final Settings settings = new Settings.Builder("queryTestGreaterLessEqual3_db").usingJWPTLeftouterNodes().build();
		final ClassLoader classLoader = getClass().getClassLoader();
		
		final Query query = new Query(classLoader.getResource("queryTestFilter5.q").getPath(), statistics, settings);
		
		
		//EXPECTED
		StructType schema = DataTypes.createStructType(new StructField[]{
				DataTypes.createStructField("title", DataTypes.StringType, true),
				DataTypes.createStructField("price", DataTypes.StringType, true),
				});
		Row row1 = RowFactory.create("Title2", "30");
		List<Row> rowList = ImmutableList.of(row1);
		Dataset<Row> expectedResult = spark().createDataFrame(rowList, schema);
		
		//ACTUAL
		final Dataset<Row> actualResult = query.compute(spark().sqlContext()).orderBy("title", "price");
		final Dataset<Row> nullableActualResult = sqlContext().createDataFrame(actualResult.collectAsList(),
				actualResult.schema().asNullable());
		
		assertDataFrameEquals(expectedResult, nullableActualResult);
	}

	private Dataset<Row> initializeDb3(final DatabaseStatistics statistics) {
		spark().sql("DROP DATABASE IF EXISTS queryTestGreaterLessEqual3_db CASCADE");
		spark().sql("CREATE DATABASE IF NOT EXISTS  queryTestGreaterLessEqual3_db");
		spark().sql("USE queryTestGreaterLessEqual3_db");

				
		// creates test tt table
		final TripleBean t1 = new TripleBean();
		t1.setS("<http://example.org/book1>");
		t1.setP("<http://example.org/title>");
		t1.setO("Title1");
		
		final TripleBean t2 = new TripleBean();
		t2.setS("<http://example.org/book2>");
		t2.setP("<http://example.org/title>");
		t2.setO("Title2");
		
		final TripleBean t3 = new TripleBean();
		t3.setS("<http://example.org/book1>");
		t3.setP("<http://example.org/price>");
		t3.setO("50");
		
		final TripleBean t4 = new TripleBean();
		t4.setS("<http://example.org/book2>");
		t4.setP("<http://example.org/price>");
		t4.setO("30");

		final ArrayList<TripleBean> triplesList = new ArrayList<>();
		triplesList.add(t1);
		triplesList.add(t2);
		triplesList.add(t3);
		triplesList.add(t4);

		final Dataset<Row> ttDataset = spark().createDataset(triplesList, triplesEncoder).select("s", "p", "o").orderBy(
				"s", "p", "o");
		ttDataset.write().saveAsTable("tripletable");
		
		final loader.Settings loaderSettings =
				new loader.Settings.Builder("queryTestGreaterLessEqual3_db").withInputPath((System.getProperty(
						"user.dir") + "\\target\\test_output\\FilterGreaterLessEqualTest").replace('\\', '/'))
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

		final JoinedWidePropertyTableLoader jwptInnerLoader = new JoinedWidePropertyTableLoader(loaderSettings,
				spark(), JoinedWidePropertyTableLoader.JoinType.inner, statistics);
		jwptLeftOuterLoader.load();

		return ttDataset;
	}
	
	@Test
	//@Ignore("Optionals are not fully implemented yet.")
	public void queryTest4() throws Exception {
		final DatabaseStatistics statistics = new DatabaseStatistics("queryTestGreaterLessEqual4_db");
		Dataset<Row> fullDataset = initializeDb4(statistics);
		fullDataset = fullDataset.orderBy("s", "p", "o");
		queryOnTT4(statistics, fullDataset);
		queryOnVp4(statistics, fullDataset);
		queryOnWpt4(statistics, fullDataset);
		queryOnIwpt4(statistics, fullDataset);
		queryOnJwptOuter4(statistics, fullDataset);
		queryOnJwptLeftOuter4(statistics, fullDataset);
	}	
	private void queryOnTT4(final DatabaseStatistics statistics, final Dataset<Row> fullDataset)  throws Exception {
		final Settings settings = new Settings.Builder("queryTestGreaterLessEqual4_db").usingTTNodes().build();
		final ClassLoader classLoader = getClass().getClassLoader();
		
		final Query query = new Query(classLoader.getResource("queryTestFilter6.q").getPath(), statistics, settings);
		

		//EXPECTED
		StructType schema = DataTypes.createStructType(new StructField[]{
				DataTypes.createStructField("title", DataTypes.StringType, true),
				DataTypes.createStructField("price", DataTypes.StringType, true),
				});
		Row row1 = RowFactory.create("Title1", "50");
		Row row2 = RowFactory.create("Title2", "30");
		List<Row> rowList = ImmutableList.of(row1, row2);
		Dataset<Row> expectedResult = spark().createDataFrame(rowList, schema);
		
		//ACTUAL
		final Dataset<Row> actualResult = query.compute(spark().sqlContext()).orderBy("title", "price");
		final Dataset<Row> nullableActualResult = sqlContext().createDataFrame(actualResult.collectAsList(),
				actualResult.schema().asNullable());
		System.out.print("FilterGreaterLessEqualTest: queryTest4");
		expectedResult.printSchema();
		expectedResult.show();

		nullableActualResult.printSchema();
		nullableActualResult.show();
		assertDataFrameEquals(expectedResult, nullableActualResult);
	}
	
	private void queryOnVp4(final DatabaseStatistics statistics, final Dataset<Row> fullDataset)  throws Exception {
		final Settings settings = new Settings.Builder("queryTestGreaterLessEqual4_db").usingVPNodes().build();
		final ClassLoader classLoader = getClass().getClassLoader();
		
		final Query query = new Query(classLoader.getResource("queryTestFilter6.q").getPath(), statistics, settings);
		
		
		//EXPECTED
		StructType schema = DataTypes.createStructType(new StructField[]{
				DataTypes.createStructField("title", DataTypes.StringType, true),
				DataTypes.createStructField("price", DataTypes.StringType, true),
				});
		Row row1 = RowFactory.create("Title1", "50");
		Row row2 = RowFactory.create("Title2", "30");
		List<Row> rowList = ImmutableList.of(row1, row2);
		Dataset<Row> expectedResult = spark().createDataFrame(rowList, schema);
		
		//ACTUAL
		final Dataset<Row> actualResult = query.compute(spark().sqlContext()).orderBy("title", "price");
		final Dataset<Row> nullableActualResult = sqlContext().createDataFrame(actualResult.collectAsList(),
				actualResult.schema().asNullable());
		
		assertDataFrameEquals(expectedResult, nullableActualResult);
	}

	private void queryOnWpt4(final DatabaseStatistics statistics, final Dataset<Row> fullDataset)  throws Exception {
		final Settings settings = new Settings.Builder("queryTestGreaterLessEqual4_db").usingWPTNodes().build();
		final ClassLoader classLoader = getClass().getClassLoader();
		
		final Query query = new Query(classLoader.getResource("queryTestFilter6.q").getPath(), statistics, settings);
		
		
		//EXPECTED
		StructType schema = DataTypes.createStructType(new StructField[]{
				DataTypes.createStructField("title", DataTypes.StringType, true),
				DataTypes.createStructField("price", DataTypes.StringType, true),
				});
		Row row1 = RowFactory.create("Title1", "50");
		Row row2 = RowFactory.create("Title2", "30");
		List<Row> rowList = ImmutableList.of(row1, row2);
		Dataset<Row> expectedResult = spark().createDataFrame(rowList, schema);
		
		//ACTUAL
		final Dataset<Row> actualResult = query.compute(spark().sqlContext()).orderBy("title", "price");
		final Dataset<Row> nullableActualResult = sqlContext().createDataFrame(actualResult.collectAsList(),
				actualResult.schema().asNullable());		
		
		assertDataFrameEquals(expectedResult, nullableActualResult);
	}

	private void queryOnIwpt4(final DatabaseStatistics statistics, final Dataset<Row> fullDataset)  throws Exception {
		final Settings settings = new Settings.Builder("queryTestGreaterLessEqual4_db").usingIWPTNodes().build();
		final ClassLoader classLoader = getClass().getClassLoader();
		
		final Query query = new Query(classLoader.getResource("queryTestFilter6.q").getPath(), statistics, settings);
		

		//EXPECTED
		StructType schema = DataTypes.createStructType(new StructField[]{
				DataTypes.createStructField("title", DataTypes.StringType, true),
				DataTypes.createStructField("price", DataTypes.StringType, true),
				});
		Row row1 = RowFactory.create("Title1", "50");
		Row row2 = RowFactory.create("Title2", "30");
		List<Row> rowList = ImmutableList.of(row1, row2);
		Dataset<Row> expectedResult = spark().createDataFrame(rowList, schema);
		
		//ACTUAL
		final Dataset<Row> actualResult = query.compute(spark().sqlContext()).orderBy("title", "price");
		final Dataset<Row> nullableActualResult = sqlContext().createDataFrame(actualResult.collectAsList(),
				actualResult.schema().asNullable());
		
		assertDataFrameEquals(expectedResult, nullableActualResult);
	}

	private void queryOnJwptOuter4(final DatabaseStatistics statistics, final Dataset<Row> fullDataset)  throws Exception {
		final Settings settings = new Settings.Builder("queryTestGreaterLessEqual4_db").usingJWPTOuterNodes().build();
		final ClassLoader classLoader = getClass().getClassLoader();
		
		final Query query = new Query(classLoader.getResource("queryTestFilter6.q").getPath(), statistics, settings);
		
		
		//EXPECTED
		StructType schema = DataTypes.createStructType(new StructField[]{
				DataTypes.createStructField("title", DataTypes.StringType, true),
				DataTypes.createStructField("price", DataTypes.StringType, true),
				});
		Row row1 = RowFactory.create("Title1", "50");
		Row row2 = RowFactory.create("Title2", "30");
		List<Row> rowList = ImmutableList.of(row1, row2);
		Dataset<Row> expectedResult = spark().createDataFrame(rowList, schema);
		
		//ACTUAL
		final Dataset<Row> actualResult = query.compute(spark().sqlContext()).orderBy("title", "price");
		final Dataset<Row> nullableActualResult = sqlContext().createDataFrame(actualResult.collectAsList(),
				actualResult.schema().asNullable());
		
		assertDataFrameEquals(expectedResult, nullableActualResult);
	}

	private void queryOnJwptLeftOuter4(final DatabaseStatistics statistics, final Dataset<Row> fullDataset)  throws Exception {
		final Settings settings = new Settings.Builder("queryTestGreaterLessEqual4_db").usingJWPTLeftouterNodes().build();
		final ClassLoader classLoader = getClass().getClassLoader();
		
		final Query query = new Query(classLoader.getResource("queryTestFilter6.q").getPath(), statistics, settings);
		
		
		//EXPECTED
		StructType schema = DataTypes.createStructType(new StructField[]{
				DataTypes.createStructField("title", DataTypes.StringType, true),
				DataTypes.createStructField("price", DataTypes.StringType, true),
				});
		Row row1 = RowFactory.create("Title1", "50");
		Row row2 = RowFactory.create("Title2", "30");
		List<Row> rowList = ImmutableList.of(row1, row2);
		Dataset<Row> expectedResult = spark().createDataFrame(rowList, schema);
		
		//ACTUAL
		final Dataset<Row> actualResult = query.compute(spark().sqlContext()).orderBy("title", "price");
		final Dataset<Row> nullableActualResult = sqlContext().createDataFrame(actualResult.collectAsList(),
				actualResult.schema().asNullable());
		
		assertDataFrameEquals(expectedResult, nullableActualResult);
	}

	private Dataset<Row> initializeDb4(final DatabaseStatistics statistics) {
		spark().sql("DROP DATABASE IF EXISTS queryTestGreaterLessEqual4_db CASCADE");
		spark().sql("CREATE DATABASE IF NOT EXISTS  queryTestGreaterLessEqual4_db");
		spark().sql("USE queryTestGreaterLessEqual4_db");

				
		// creates test tt table
		final TripleBean t1 = new TripleBean();
		t1.setS("<http://example.org/book1>");
		t1.setP("<http://example.org/title>");
		t1.setO("Title1");
		
		final TripleBean t2 = new TripleBean();
		t2.setS("<http://example.org/book2>");
		t2.setP("<http://example.org/title>");
		t2.setO("Title2");
		
		final TripleBean t3 = new TripleBean();
		t3.setS("<http://example.org/book1>");
		t3.setP("<http://example.org/price>");
		t3.setO("50");
		
		final TripleBean t4 = new TripleBean();
		t4.setS("<http://example.org/book2>");
		t4.setP("<http://example.org/price>");
		t4.setO("30");

		final ArrayList<TripleBean> triplesList = new ArrayList<>();
		triplesList.add(t1);
		triplesList.add(t2);
		triplesList.add(t3);
		triplesList.add(t4);

		final Dataset<Row> ttDataset = spark().createDataset(triplesList, triplesEncoder).select("s", "p", "o").orderBy(
				"s", "p", "o");
		ttDataset.write().saveAsTable("tripletable");
		
		final loader.Settings loaderSettings =
				new loader.Settings.Builder("queryTestGreaterLessEqual4_db").withInputPath((System.getProperty(
						"user.dir") + "\\target\\test_output\\FilterGreaterLessEqualTest").replace('\\', '/'))
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

		final JoinedWidePropertyTableLoader jwptInnerLoader = new JoinedWidePropertyTableLoader(loaderSettings,
				spark(), JoinedWidePropertyTableLoader.JoinType.inner, statistics);
		jwptLeftOuterLoader.load();

		return ttDataset;
	}
	
}

/*
PREFIX ex: <http://example.org/#>.

TABLE:
================================================================================================================
ex:book1		| ex:title			| "Title1"
ex:book1		| ex:price			| "50"

ex:book2		| ex:title			| "Title2"
ex:book2		| ex:price			| "20"
================================================================================================================

QUERY:
-----------------------------------------------------------------------------------------------------------------
SELECT ?title ?price
WHERE
{
	?book <http://example.org/title> ?title.
	?book <http://example.org/price> ?price.FILTER(?price < 30)
}
-----------------------------------------------------------------------------------------------------------------

RESULT:
-----------------------------------------------------------------------------------------------------------------
Expected:
+------+-----+
| title|price|
+------+-----+
|Title2|   20|
+------+-----+

Actual:
+------+-----+
| title|price|
+------+-----+
|Title2|   20|
+------+-----+
-----------------------------------------------------------------------------------------------------------------
*/

/*
PREFIX ex: <http://example.org/#>.

TABLE:
================================================================================================================
ex:book1		| ex:title			| "Title1"
ex:book1		| ex:price			| "50"

ex:book2		| ex:title			| "Title2"
ex:book2		| ex:price			| "20"
================================================================================================================

QUERY:
-----------------------------------------------------------------------------------------------------------------
SELECT ?title ?price
WHERE
{
	?book <http://example.org/title> ?title.
	?book <http://example.org/price> ?price.FILTER(?price > 30)
}
-----------------------------------------------------------------------------------------------------------------

RESULT:
-----------------------------------------------------------------------------------------------------------------
Expected:
+------+-----+
| title|price|
+------+-----+
|Title1|   50|
+------+-----+

Actual:
+------+-----+
| title|price|
+------+-----+
|Title2|   50|
+------+-----+
-----------------------------------------------------------------------------------------------------------------
*/

/*
PREFIX ex: <http://example.org/#>.

TABLE:
================================================================================================================
ex:book1		| ex:title			| "Title1"
ex:book1		| ex:price			| "50"

ex:book2		| ex:title			| "Title2"
ex:book2		| ex:price			| "30"
================================================================================================================

QUERY:
-----------------------------------------------------------------------------------------------------------------
SELECT ?title ?price
WHERE
{
	?book <http://example.org/title> ?title.
	?book <http://example.org/price> ?price.FILTER(?price <= 30)
}
-----------------------------------------------------------------------------------------------------------------

RESULT:
-----------------------------------------------------------------------------------------------------------------
Expected:
+------+-----+
| title|price|
+------+-----+
|Title2|   30|
+------+-----+

Actual:
+------+-----+
| title|price|
+------+-----+
|Title2|   30|
+------+-----+
-----------------------------------------------------------------------------------------------------------------
*/

/*
PREFIX ex: <http://example.org/#>.

TABLE:
================================================================================================================
ex:book1		| ex:title			| "Title1"
ex:book1		| ex:price			| "50"

ex:book2		| ex:title			| "Title2"
ex:book2		| ex:price			| "30"
================================================================================================================

QUERY:
-----------------------------------------------------------------------------------------------------------------
SELECT ?title ?price
WHERE
{
	?book <http://example.org/title> ?title.
	?book <http://example.org/price> ?price.FILTER(?price >= 30)
}
-----------------------------------------------------------------------------------------------------------------

RESULT:
-----------------------------------------------------------------------------------------------------------------
Expected:
+------+-----+
| title|price|
+------+-----+
|Title1|   50|
|Title2|   30|
+------+-----+

Actual:
+------+-----+
| title|price|
+------+-----+
|Title1|   50|
|Title2|   30|
+------+-----+
-----------------------------------------------------------------------------------------------------------------
*/
