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
public class OffsetTest extends JavaDataFrameSuiteBase implements Serializable {
	private static final long serialVersionUID = 1329L;
	private static final Encoder<TripleBean> triplesEncoder = Encoders.bean(TripleBean.class);

	@Test
	@Ignore("Offsets not supported by PRoST and SPARK SQL. Offset support is not reasonable in distributed databases.")
	public void queryTest1() throws Exception {
		final DatabaseStatistics statistics = new DatabaseStatistics("queryTestOffset1_db");
		initializeDb(statistics);

		queryOnTT(statistics);
		queryOnVp(statistics);
		queryOnWpt(statistics);
		queryOnIwpt(statistics);
		queryOnJwptOuter(statistics);
		queryOnJwptLeftOuter(statistics);
	}

	private void queryOnTT(final DatabaseStatistics statistics)  throws Exception {
		final Settings settings = new Settings.Builder("queryTestOffset1_db").usingTTNodes().usingCharacteristicSets().build();
		final ClassLoader classLoader = getClass().getClassLoader();		
		final Query query = new Query(classLoader.getResource("queryTestOffset1.q").getPath(), statistics, settings);		

		//EXPECTED
		final StructType schema = DataTypes.createStructType(new StructField[]{
				DataTypes.createStructField("name", DataTypes.StringType, true),
				DataTypes.createStructField("mail", DataTypes.StringType, true),
				});
		final Row row1 = RowFactory.create("B", "<mailto:b@example.org>");
		final Row row2 = RowFactory.create("C", "<mailto:c1@example.org>");
		final Row row3 = RowFactory.create("C", "<mailto:c2@example.org>");
		final Row row4 = RowFactory.create("D", null);
		final List<Row> rowList = ImmutableList.of(row1, row2, row3, row4);
		final Dataset<Row> expectedResult = spark().createDataFrame(rowList, schema);

		//ACTUAL
		final Dataset<Row> actualResult = query.compute(spark().sqlContext()).orderBy("name", "mail");
		final Dataset<Row> nullableActualResult = sqlContext().createDataFrame(actualResult.collectAsList(),
				actualResult.schema().asNullable());
		System.out.print("OffsetTest: queryTest1");
		expectedResult.printSchema();
		expectedResult.show();

		nullableActualResult.printSchema();
		nullableActualResult.show();
		assertDataFrameEquals(expectedResult, nullableActualResult);
	}
	
	private void queryOnVp(final DatabaseStatistics statistics)  throws Exception {
		final Settings settings = new Settings.Builder("queryTestOffset1_db").usingVPNodes().build();
		final ClassLoader classLoader = getClass().getClassLoader();		
		final Query query = new Query(classLoader.getResource("queryTestOffset1.q").getPath(), statistics, settings);
				
		//EXPECTED
		final StructType schema = DataTypes.createStructType(new StructField[]{
				DataTypes.createStructField("name", DataTypes.StringType, true),
				DataTypes.createStructField("mail", DataTypes.StringType, true),
				});
		final Row row1 = RowFactory.create("B", "<mailto:b@example.org>");
		final Row row2 = RowFactory.create("C", "<mailto:c1@example.org>");
		final Row row3 = RowFactory.create("C", "<mailto:c2@example.org>");
		final Row row4 = RowFactory.create("D", null);
		final List<Row> rowList = ImmutableList.of(row1, row2, row3, row4);
		final Dataset<Row> expectedResult = spark().createDataFrame(rowList, schema);

		//ACTUAL
		final Dataset<Row> actualResult = query.compute(spark().sqlContext()).orderBy("name", "mail");
		final Dataset<Row> nullableActualResult = sqlContext().createDataFrame(actualResult.collectAsList(),
				actualResult.schema().asNullable());
		
		assertDataFrameEquals(expectedResult, nullableActualResult);
	}

	private void queryOnWpt(final DatabaseStatistics statistics)  throws Exception {
		final Settings settings = new Settings.Builder("queryTestOffset1_db").usingWPTNodes().build();
		final ClassLoader classLoader = getClass().getClassLoader();		
		final Query query = new Query(classLoader.getResource("queryTestOffset1.q").getPath(), statistics, settings);		
		
		//EXPECTED
		final StructType schema = DataTypes.createStructType(new StructField[]{
				DataTypes.createStructField("name", DataTypes.StringType, true),
				DataTypes.createStructField("mail", DataTypes.StringType, true),
				});
		final Row row1 = RowFactory.create("B", "<mailto:b@example.org>");
		final Row row2 = RowFactory.create("C", "<mailto:c1@example.org>");
		final Row row3 = RowFactory.create("C", "<mailto:c2@example.org>");
		final Row row4 = RowFactory.create("D", null);
		final List<Row> rowList = ImmutableList.of(row1, row2, row3, row4);
		final Dataset<Row> expectedResult = spark().createDataFrame(rowList, schema);

		//ACTUAL
		final Dataset<Row> actualResult = query.compute(spark().sqlContext()).orderBy("name", "mail");
		final Dataset<Row> nullableActualResult = sqlContext().createDataFrame(actualResult.collectAsList(),
				actualResult.schema().asNullable());		
		
		assertDataFrameEquals(expectedResult, nullableActualResult);
	}

	private void queryOnIwpt(final DatabaseStatistics statistics)  throws Exception {
		final Settings settings = new Settings.Builder("queryTestOffset1_db").usingIWPTNodes().build();
		final ClassLoader classLoader = getClass().getClassLoader();		
		final Query query = new Query(classLoader.getResource("queryTestOffset1.q").getPath(), statistics, settings);
		
		//EXPECTED
		final StructType schema = DataTypes.createStructType(new StructField[]{
				DataTypes.createStructField("name", DataTypes.StringType, true),
				DataTypes.createStructField("mail", DataTypes.StringType, true),
				});
		final Row row1 = RowFactory.create("B", "<mailto:b@example.org>");
		final Row row2 = RowFactory.create("C", "<mailto:c1@example.org>");
		final Row row3 = RowFactory.create("C", "<mailto:c2@example.org>");
		final Row row4 = RowFactory.create("D", null);
		final List<Row> rowList = ImmutableList.of(row1, row2, row3, row4);
		final Dataset<Row> expectedResult = spark().createDataFrame(rowList, schema);

		//ACTUAL
		final Dataset<Row> actualResult = query.compute(spark().sqlContext()).orderBy("name", "mail");
		final Dataset<Row> nullableActualResult = sqlContext().createDataFrame(actualResult.collectAsList(),
				actualResult.schema().asNullable());
		
		assertDataFrameEquals(expectedResult, nullableActualResult);
	}

	private void queryOnJwptOuter(final DatabaseStatistics statistics)  throws Exception {
		final Settings settings = new Settings.Builder("queryTestOffset1_db").usingJWPTOuterNodes().build();
		final ClassLoader classLoader = getClass().getClassLoader();		
		final Query query = new Query(classLoader.getResource("queryTestOffset1.q").getPath(), statistics, settings);		
		
		//EXPECTED
		final StructType schema = DataTypes.createStructType(new StructField[]{
				DataTypes.createStructField("name", DataTypes.StringType, true),
				DataTypes.createStructField("mail", DataTypes.StringType, true),
				});
		final Row row1 = RowFactory.create("B", "<mailto:b@example.org>");
		final Row row2 = RowFactory.create("C", "<mailto:c1@example.org>");
		final Row row3 = RowFactory.create("C", "<mailto:c2@example.org>");
		final Row row4 = RowFactory.create("D", null);
		final List<Row> rowList = ImmutableList.of(row1, row2, row3, row4);
		final Dataset<Row> expectedResult = spark().createDataFrame(rowList, schema);

		//ACTUAL
		final Dataset<Row> actualResult = query.compute(spark().sqlContext()).orderBy("name", "mail");
		final Dataset<Row> nullableActualResult = sqlContext().createDataFrame(actualResult.collectAsList(),
				actualResult.schema().asNullable());
		
		assertDataFrameEquals(expectedResult, nullableActualResult);
	}

	private void queryOnJwptLeftOuter(final DatabaseStatistics statistics)  throws Exception {
		final Settings settings = new Settings.Builder("queryTestOffset1_db").usingJWPTLeftouterNodes().build();
		final ClassLoader classLoader = getClass().getClassLoader();		
		final Query query = new Query(classLoader.getResource("queryTestOffset1.q").getPath(), statistics, settings);
				
		//EXPECTED
		final StructType schema = DataTypes.createStructType(new StructField[]{
				DataTypes.createStructField("name", DataTypes.StringType, true),
				DataTypes.createStructField("mail", DataTypes.StringType, true),
				});
		final Row row1 = RowFactory.create("B", "<mailto:b@example.org>");
		final Row row2 = RowFactory.create("C", "<mailto:c1@example.org>");
		final Row row3 = RowFactory.create("C", "<mailto:c2@example.org>");
		final Row row4 = RowFactory.create("D", null);
		final List<Row> rowList = ImmutableList.of(row1, row2, row3, row4);
		final Dataset<Row> expectedResult = spark().createDataFrame(rowList, schema);

		//ACTUAL
		final Dataset<Row> actualResult = query.compute(spark().sqlContext()).orderBy("name", "mail");
		final Dataset<Row> nullableActualResult = sqlContext().createDataFrame(actualResult.collectAsList(),
				actualResult.schema().asNullable());
		
		assertDataFrameEquals(expectedResult, nullableActualResult);
	}

	private Dataset<Row> initializeDb(final DatabaseStatistics statistics) {
		spark().sql("DROP DATABASE IF EXISTS queryTestOffset1_db CASCADE");
		spark().sql("CREATE DATABASE IF NOT EXISTS  queryTestOffset1_db");
		spark().sql("USE queryTestOffset1_db");

		// creates test tt table
		final TripleBean t1 = new TripleBean();
		t1.setS("<http://example.org/person1>");
		t1.setP("<http://example.org/name>");
		t1.setO("A");
		
		final TripleBean t2 = new TripleBean();
		t2.setS("<http://example.org/person2>");
		t2.setP("<http://example.org/name>");
		t2.setO("B");
		
		final TripleBean t3 = new TripleBean();
		t3.setS("<http://example.org/person3>");
		t3.setP("<http://example.org/name>");
		t3.setO("C");
		
		final TripleBean t4 = new TripleBean();
		t4.setS("<http://example.org/person4>");
		t4.setP("<http://example.org/name>");
		t4.setO("D");
		
		final TripleBean t5 = new TripleBean();
		t5.setS("<http://example.org/person5>");
		t5.setP("<http://example.org/name>");
		t5.setO("E");

		final TripleBean t6 = new TripleBean();
		t6.setS("<http://example.org/person1>");
		t6.setP("<http://example.org/mail>");
		t6.setO("<mailto:a@example.org>");
		
		final TripleBean t7 = new TripleBean();
		t7.setS("<http://example.org/person2>");
		t7.setP("<http://example.org/mail>");
		t7.setO("<mailto:b@example.org>");
		
		final TripleBean t8 = new TripleBean();
		t8.setS("<http://example.org/person3>");
		t8.setP("<http://example.org/mail>");
		t8.setO("<mailto:c1@example.org>");
		
		final TripleBean t9 = new TripleBean();
		t9.setS("<http://example.org/person3>");
		t9.setP("<http://example.org/mail>");
		t9.setO("<mailto:c2@example.org>");
		
		final ArrayList<TripleBean> triplesList = new ArrayList<>();
		triplesList.add(t1);
		triplesList.add(t2);
		triplesList.add(t3);
		triplesList.add(t4);
		triplesList.add(t5);
		triplesList.add(t6);
		triplesList.add(t7);
		triplesList.add(t8);
		triplesList.add(t9);

		final Dataset<Row> ttDataset = spark().createDataset(triplesList, triplesEncoder).select("s", "p", "o").orderBy(
				"s", "p", "o");
		ttDataset.write().saveAsTable("tripletable");
		
		final loader.Settings loaderSettings =
				new loader.Settings.Builder("queryTestOffset1_db").withInputPath((System.getProperty(
						"user.dir") + "\\target\\test_output\\OffsetTest").replace('\\', '/'))
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

		return ttDataset;
	}
	
	@Test
	@Ignore("Offsets not supported by PRoST and SPARK SQL. Offset support is not reasonable in distributed databases.")
	public void queryTest2() throws Exception {
		final DatabaseStatistics statistics = new DatabaseStatistics("queryTestOffset2_db");
		initializeDb2(statistics);
		queryOnTT2(statistics);
		queryOnVp2(statistics);
		queryOnWpt2(statistics);
		queryOnIwpt2(statistics);
		queryOnJwptOuter2(statistics);
		queryOnJwptLeftOuter2(statistics);
	}

	private void queryOnTT2(final DatabaseStatistics statistics)  throws Exception {
		final Settings settings = new Settings.Builder("queryTestOffset2_db").usingTTNodes().usingCharacteristicSets().build();
		final ClassLoader classLoader = getClass().getClassLoader();		
		final Query query = new Query(classLoader.getResource("queryTestOffset2.q").getPath(), statistics, settings);		

		//EXPECTED
		final StructType schema = DataTypes.createStructType(new StructField[]{
				DataTypes.createStructField("name", DataTypes.StringType, true),
				DataTypes.createStructField("mail", DataTypes.StringType, true),
				});
		final Row row1 = RowFactory.create("B", "<mailto:b@example.org>");
		final Row row2 = RowFactory.create("C", "<mailto:c1@example.org>");
		final Row row3 = RowFactory.create("C", "<mailto:c2@example.org>");
		final Row row4 = RowFactory.create("D", null);
		final Row row5 = RowFactory.create("E", null);
		final List<Row> rowList = ImmutableList.of(row1, row2, row3, row4, row5);
		final Dataset<Row> expectedResult = spark().createDataFrame(rowList, schema);

		//ACTUAL
		final Dataset<Row> actualResult = query.compute(spark().sqlContext()).orderBy("name", "mail");
		final Dataset<Row> nullableActualResult = sqlContext().createDataFrame(actualResult.collectAsList(),
				actualResult.schema().asNullable());
		System.out.print("OffsetTest: queryTest2");
		expectedResult.printSchema();
		expectedResult.show();

		nullableActualResult.printSchema();
		nullableActualResult.show();
		assertDataFrameEquals(expectedResult, nullableActualResult);
	}
	
	private void queryOnVp2(final DatabaseStatistics statistics)  throws Exception {
		final Settings settings = new Settings.Builder("queryTestOffset2_db").usingVPNodes().build();
		final ClassLoader classLoader = getClass().getClassLoader();		
		final Query query = new Query(classLoader.getResource("queryTestOffset2.q").getPath(), statistics, settings);
		
		//EXPECTED
		final StructType schema = DataTypes.createStructType(new StructField[]{
				DataTypes.createStructField("name", DataTypes.StringType, true),
				DataTypes.createStructField("mail", DataTypes.StringType, true),
				});
		final Row row1 = RowFactory.create("B", "<mailto:b@example.org>");
		final Row row2 = RowFactory.create("C", "<mailto:c1@example.org>");
		final Row row3 = RowFactory.create("C", "<mailto:c2@example.org>");
		final Row row4 = RowFactory.create("D", null);
		final Row row5 = RowFactory.create("E", null);
		final List<Row> rowList = ImmutableList.of(row1, row2, row3, row4, row5);
		final Dataset<Row> expectedResult = spark().createDataFrame(rowList, schema);

		//ACTUAL
		final Dataset<Row> actualResult = query.compute(spark().sqlContext()).orderBy("name", "mail");
		final Dataset<Row> nullableActualResult = sqlContext().createDataFrame(actualResult.collectAsList(),
				actualResult.schema().asNullable());
		
		assertDataFrameEquals(expectedResult, nullableActualResult);
	}

	private void queryOnWpt2(final DatabaseStatistics statistics)  throws Exception {
		final Settings settings = new Settings.Builder("queryTestOffset2_db").usingWPTNodes().build();
		final ClassLoader classLoader = getClass().getClassLoader();		
		final Query query = new Query(classLoader.getResource("queryTestOffset2.q").getPath(), statistics, settings);
		
		//EXPECTED
		final StructType schema = DataTypes.createStructType(new StructField[]{
				DataTypes.createStructField("name", DataTypes.StringType, true),
				DataTypes.createStructField("mail", DataTypes.StringType, true),
				});
		final Row row1 = RowFactory.create("B", "<mailto:b@example.org>");
		final Row row2 = RowFactory.create("C", "<mailto:c1@example.org>");
		final Row row3 = RowFactory.create("C", "<mailto:c2@example.org>");
		final Row row4 = RowFactory.create("D", null);
		final Row row5 = RowFactory.create("E", null);
		final List<Row> rowList = ImmutableList.of(row1, row2, row3, row4, row5);
		final Dataset<Row> expectedResult = spark().createDataFrame(rowList, schema);

		//ACTUAL
		final Dataset<Row> actualResult = query.compute(spark().sqlContext()).orderBy("name", "mail");
		final Dataset<Row> nullableActualResult = sqlContext().createDataFrame(actualResult.collectAsList(),
				actualResult.schema().asNullable());	
		
		assertDataFrameEquals(expectedResult, nullableActualResult);
	}

	private void queryOnIwpt2(final DatabaseStatistics statistics)  throws Exception {
		final Settings settings = new Settings.Builder("queryTestOffset2_db").usingIWPTNodes().build();
		final ClassLoader classLoader = getClass().getClassLoader();		
		final Query query = new Query(classLoader.getResource("queryTestOffset2.q").getPath(), statistics, settings);

		//EXPECTED
		final StructType schema = DataTypes.createStructType(new StructField[]{
				DataTypes.createStructField("name", DataTypes.StringType, true),
				DataTypes.createStructField("mail", DataTypes.StringType, true),
				});
		final Row row1 = RowFactory.create("B", "<mailto:b@example.org>");
		final Row row2 = RowFactory.create("C", "<mailto:c1@example.org>");
		final Row row3 = RowFactory.create("C", "<mailto:c2@example.org>");
		final Row row4 = RowFactory.create("D", null);
		final Row row5 = RowFactory.create("E", null);
		final List<Row> rowList = ImmutableList.of(row1, row2, row3, row4, row5);
		final Dataset<Row> expectedResult = spark().createDataFrame(rowList, schema);

		//ACTUAL
		final Dataset<Row> actualResult = query.compute(spark().sqlContext()).orderBy("name", "mail");
		final Dataset<Row> nullableActualResult = sqlContext().createDataFrame(actualResult.collectAsList(),
				actualResult.schema().asNullable());
		
		assertDataFrameEquals(expectedResult, nullableActualResult);
	}

	private void queryOnJwptOuter2(final DatabaseStatistics statistics)  throws Exception {
		final Settings settings = new Settings.Builder("queryTestOffset2_db").usingJWPTOuterNodes().build();
		final ClassLoader classLoader = getClass().getClassLoader();		
		final Query query = new Query(classLoader.getResource("queryTestOffset2.q").getPath(), statistics, settings);
		
		//EXPECTED
		final StructType schema = DataTypes.createStructType(new StructField[]{
				DataTypes.createStructField("name", DataTypes.StringType, true),
				DataTypes.createStructField("mail", DataTypes.StringType, true),
				});
		final Row row1 = RowFactory.create("B", "<mailto:b@example.org>");
		final Row row2 = RowFactory.create("C", "<mailto:c1@example.org>");
		final Row row3 = RowFactory.create("C", "<mailto:c2@example.org>");
		final Row row4 = RowFactory.create("D", null);
		final Row row5 = RowFactory.create("E", null);
		final List<Row> rowList = ImmutableList.of(row1, row2, row3, row4, row5);
		final Dataset<Row> expectedResult = spark().createDataFrame(rowList, schema);

		//ACTUAL
		final Dataset<Row> actualResult = query.compute(spark().sqlContext()).orderBy("name", "mail");
		final Dataset<Row> nullableActualResult = sqlContext().createDataFrame(actualResult.collectAsList(),
				actualResult.schema().asNullable());
		
		assertDataFrameEquals(expectedResult, nullableActualResult);
	}

	private void queryOnJwptLeftOuter2(final DatabaseStatistics statistics)  throws Exception {
		final Settings settings = new Settings.Builder("queryTestOffset2_db").usingJWPTLeftouterNodes().build();
		final ClassLoader classLoader = getClass().getClassLoader();		
		final Query query = new Query(classLoader.getResource("queryTestOffset2.q").getPath(), statistics, settings);
		
		//EXPECTED
		final StructType schema = DataTypes.createStructType(new StructField[]{
				DataTypes.createStructField("name", DataTypes.StringType, true),
				DataTypes.createStructField("mail", DataTypes.StringType, true),
				});
		final Row row1 = RowFactory.create("B", "<mailto:b@example.org>");
		final Row row2 = RowFactory.create("C", "<mailto:c1@example.org>");
		final Row row3 = RowFactory.create("C", "<mailto:c2@example.org>");
		final Row row4 = RowFactory.create("D", null);
		final Row row5 = RowFactory.create("E", null);
		final List<Row> rowList = ImmutableList.of(row1, row2, row3, row4, row5);
		final Dataset<Row> expectedResult = spark().createDataFrame(rowList, schema);

		//ACTUAL
		final Dataset<Row> actualResult = query.compute(spark().sqlContext()).orderBy("name", "mail");
		final Dataset<Row> nullableActualResult = sqlContext().createDataFrame(actualResult.collectAsList(),
				actualResult.schema().asNullable());
		
		assertDataFrameEquals(expectedResult, nullableActualResult);
	}

	private Dataset<Row> initializeDb2(final DatabaseStatistics statistics) {
		spark().sql("DROP DATABASE IF EXISTS queryTestOffset2_db CASCADE");
		spark().sql("CREATE DATABASE IF NOT EXISTS  queryTestOffset2_db");
		spark().sql("USE queryTestOffset2_db");

		// creates test tt table
		final TripleBean t1 = new TripleBean();
		t1.setS("<http://example.org/person1>");
		t1.setP("<http://example.org/name>");
		t1.setO("A");
		
		final TripleBean t2 = new TripleBean();
		t2.setS("<http://example.org/person2>");
		t2.setP("<http://example.org/name>");
		t2.setO("B");
		
		final TripleBean t3 = new TripleBean();
		t3.setS("<http://example.org/person3>");
		t3.setP("<http://example.org/name>");
		t3.setO("C");
		
		final TripleBean t4 = new TripleBean();
		t4.setS("<http://example.org/person4>");
		t4.setP("<http://example.org/name>");
		t4.setO("D");
		
		final TripleBean t5 = new TripleBean();
		t5.setS("<http://example.org/person5>");
		t5.setP("<http://example.org/name>");
		t5.setO("E");

		final TripleBean t6 = new TripleBean();
		t6.setS("<http://example.org/person1>");
		t6.setP("<http://example.org/mail>");
		t6.setO("<mailto:a@example.org>");
		
		final TripleBean t7 = new TripleBean();
		t7.setS("<http://example.org/person2>");
		t7.setP("<http://example.org/mail>");
		t7.setO("<mailto:b@example.org>");
		
		final TripleBean t8 = new TripleBean();
		t8.setS("<http://example.org/person3>");
		t8.setP("<http://example.org/mail>");
		t8.setO("<mailto:c1@example.org>");
		
		final TripleBean t9 = new TripleBean();
		t9.setS("<http://example.org/person3>");
		t9.setP("<http://example.org/mail>");
		t9.setO("<mailto:c2@example.org>");
		
		final ArrayList<TripleBean> triplesList = new ArrayList<>();
		triplesList.add(t1);
		triplesList.add(t2);
		triplesList.add(t3);
		triplesList.add(t4);
		triplesList.add(t5);
		triplesList.add(t6);
		triplesList.add(t7);
		triplesList.add(t8);
		triplesList.add(t9);

		final Dataset<Row> ttDataset = spark().createDataset(triplesList, triplesEncoder).select("s", "p", "o").orderBy(
				"s", "p", "o");
		ttDataset.write().saveAsTable("tripletable");
		
		final loader.Settings loaderSettings =
				new loader.Settings.Builder("queryTestOffset2_db").withInputPath((System.getProperty(
						"user.dir") + "\\target\\test_output\\OffsetTest").replace('\\', '/'))
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

		return ttDataset;
	}
}

/*
PREFIX ex: <http://example.org/#>.

TABLE:
================================================================================================================
ex:person1		| ex:name			| "A"
ex:person2		| ex:name			| "B"
ex:person3		| ex:name			| "C"
ex:person4		| ex:name			| "D"
ex:person5		| ex:name			| "E"

ex:person1		| ex:mail			| "<mailto:a@example.org>"
ex:person2		| ex:mail			| "<mailto:b@example.org>"
ex:person3		| ex:mail			| "<mailto:c1@example.org>"
ex:person3		| ex:mail			| "<mailto:c2@example.org>"
================================================================================================================

QUERY:
-----------------------------------------------------------------------------------------------------------------
SELECT ?name ?mail
WHERE
{
	?person <http://example.org/name> ?name.
	OPTIONAL { ?person <http://example.org/mail> ?mail }
}
ORDER BY ?name LIMIT 2 OFFSET 4
-----------------------------------------------------------------------------------------------------------------

RESULT:
-----------------------------------------------------------------------------------------------------------------
Expected:
+-----+-----------------------+
| name|                   mail|
+-----+-----------------------+
|    B| <mailto:b@example.org>|
|    C|<mailto:c1@example.org>|
|    C|<mailto:c2@example.org>|
|    D|                   null|
+-----+-----------------------+

Actual:

-----------------------------------------------------------------------------------------------------------------

QUERY:
-----------------------------------------------------------------------------------------------------------------
SELECT ?name ?mail
WHERE
{ 
	{SELECT DISTINCT ?person ?name 
	WHERE {?person <http://example.org/name> ?name} 
	ORDER BY ?name LIMIT 2 OFFSET 4}
    OPTIONAL { ?person <http://example.org/mail> ?mail}
}

-----------------------------------------------------------------------------------------------------------------

RESULT:
-----------------------------------------------------------------------------------------------------------------
Expected:
+-----+-----------------------+
| name|                   mail|
+-----+-----------------------+
|    B| <mailto:b@example.org>|
|    C|<mailto:c1@example.org>|
|    C|<mailto:c2@example.org>|
|    D|                   null|
|    E|                   null|
+-----+-----------------------+

Actual:

-----------------------------------------------------------------------------------------------------------------
*/
