package query.run;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.holdenkarau.spark.testing.JavaDataFrameSuiteBase;
import joinTree.JoinTree;
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
import translator.Translator;
import utils.Settings;

/**
 * This class tests represents the highest level of testing, i.e. given a query
 * it checks that results are correctly and consistently returned according to
 * ALL supported logical partitioning strategies (at the moment WPT, IWPT, JWPT,
 * and VP?), i.e. these tests verify are about SPARQL semantics.
 *
 * @author Kristin Plettau
 */
public class SingleTriplePatternTest extends JavaDataFrameSuiteBase implements Serializable {
	private static final long serialVersionUID = 1329L;
	private static final Encoder<TripleBean> triplesEncoder = Encoders.bean(TripleBean.class);

	@Test
	public void queryTest() {
		final DatabaseStatistics statistics = new DatabaseStatistics("queryTest01_db");
		Dataset<Row> fullDataset = initializeDb(statistics);
		fullDataset = fullDataset.orderBy("s", "p", "o");
		queryOnTT(statistics, fullDataset);
//		queryOnVp(statistics, fullDataset);
//		queryOnWpt(statistics, fullDataset);
//		queryOnIwpt(statistics, fullDataset);
//		queryOnJwptOuter(statistics, fullDataset);
//		queryOnJwptLeftOuter(statistics, fullDataset);
	}

	private void queryOnTT(final DatabaseStatistics statistics, final Dataset<Row> fullDataset) {
		final Settings settings = new Settings.Builder("queryTest01_db").usingTTNodes().build();
		final ClassLoader classLoader = getClass().getClassLoader();
		final Translator translator = new Translator(settings, statistics,
				classLoader.getResource("queryTestSingleTriple1.q").getPath());
		final JoinTree joinTree = translator.translateQuery();
		
		
		//EXPECTED
		final StructType schema = DataTypes.createStructType(new StructField[]{
				DataTypes.createStructField("book", DataTypes.StringType, true),
				DataTypes.createStructField("p", DataTypes.StringType, true),
				DataTypes.createStructField("title", DataTypes.StringType, true),
				});
		final Row row1 = RowFactory.create("<http://example.org/book1>", "<http://example.org/title>", "Title1");
		final Row row2 = RowFactory.create("<http://example.org/book2>", "<http://example.org/title>", "Title2");
		final List<Row> rowList = ImmutableList.of(row1, row2);
		final Dataset<Row> expectedResult = spark().createDataFrame(rowList, schema);
		
		//ACTUAL
		final Dataset<Row> actualResult = joinTree.compute(spark().sqlContext()).orderBy("book", "a", "title");
		final Dataset<Row> nullableActualResult = sqlContext().createDataFrame(actualResult.collectAsList(),
				actualResult.schema().asNullable());
		System.out.print("SingleTriplePatternTest: queryTest1");
		expectedResult.printSchema();
		expectedResult.show();
		System.out.println(joinTree.toString());	
		nullableActualResult.printSchema();
		nullableActualResult.show();
		assertDataFrameEquals(expectedResult, nullableActualResult);
	}
	
	private void queryOnVp(final DatabaseStatistics statistics, final Dataset<Row> fullDataset) {
		final Settings settings = new Settings.Builder("queryTest01_db").usingVPNodes().build();
		final ClassLoader classLoader = getClass().getClassLoader();
		final Translator translator = new Translator(settings, statistics,
				classLoader.getResource("queryTestSingleTriple1.q").getPath());
		final JoinTree joinTree = translator.translateQuery();
		
		//EXPECTED
		List<String> data = new ArrayList<String>();
	    	data.add("Title1");
	    	data.add("Title2");
	    	// DataFrame
	    	Dataset<Row> singleColResult = spark().createDataset(data, Encoders.STRING()).toDF();
	    	Dataset<Row> expectedResult = singleColResult.selectExpr("split(value, ',')[0] as title");	
		
		//ACTUAL
		final Dataset<Row> actualResult = joinTree.compute(spark().sqlContext()).orderBy("title");
		final Dataset<Row> nullableActualResult = sqlContext().createDataFrame(actualResult.collectAsList(),
				actualResult.schema().asNullable());
		
		assertDataFrameEquals(expectedResult, nullableActualResult);
	}

	private void queryOnWpt(final DatabaseStatistics statistics, final Dataset<Row> fullDataset) {
		final Settings settings = new Settings.Builder("queryTest01_db").usingWPTNodes().build();
		final ClassLoader classLoader = getClass().getClassLoader();
		final Translator translator = new Translator(settings, statistics,
				classLoader.getResource("queryTestSingleTriple1.q").getPath());
		final JoinTree joinTree = translator.translateQuery();
		
		//EXPECTED
		List<String> data = new ArrayList<String>();
	    	data.add("Title1");
	    	data.add("Title2");
	    	// DataFrame
	    	Dataset<Row> singleColResult = spark().createDataset(data, Encoders.STRING()).toDF();
	    	Dataset<Row> expectedResult = singleColResult.selectExpr("split(value, ',')[0] as title");
		
		//ACTUAL
		final Dataset<Row> actualResult = joinTree.compute(spark().sqlContext()).orderBy("title");
		final Dataset<Row> nullableActualResult = sqlContext().createDataFrame(actualResult.collectAsList(),
				actualResult.schema().asNullable());
				
		assertDataFrameEquals(expectedResult, nullableActualResult);
	}

	private void queryOnIwpt(final DatabaseStatistics statistics, final Dataset<Row> fullDataset) {
		final Settings settings = new Settings.Builder("queryTest01_db").usingIWPTNodes().build();
		final ClassLoader classLoader = getClass().getClassLoader();
		final Translator translator = new Translator(settings, statistics,
				classLoader.getResource("queryTestSingleTriple1.q").getPath());
		final JoinTree joinTree = translator.translateQuery();

		//EXPECTED
		List<String> data = new ArrayList<String>();
	    	data.add("Title1");
	    	data.add("Title2");
	    	// DataFrame
	    	Dataset<Row> singleColResult = spark().createDataset(data, Encoders.STRING()).toDF();
	    	Dataset<Row> expectedResult = singleColResult.selectExpr("split(value, ',')[0] as title");
		
		//ACTUAL
		final Dataset<Row> actualResult = joinTree.compute(spark().sqlContext()).orderBy("title");
		final Dataset<Row> nullableActualResult = sqlContext().createDataFrame(actualResult.collectAsList(),
				actualResult.schema().asNullable());
				
		assertDataFrameEquals(expectedResult, nullableActualResult);
	}

	private void queryOnJwptOuter(final DatabaseStatistics statistics, final Dataset<Row> fullDataset) {
		final Settings settings = new Settings.Builder("queryTest01_db").usingJWPTOuterNodes().build();
		final ClassLoader classLoader = getClass().getClassLoader();
		final Translator translator = new Translator(settings, statistics,
				classLoader.getResource("queryTestSingleTriple1.q").getPath());
		final JoinTree joinTree = translator.translateQuery();
		
		//EXPECTED
		List<String> data = new ArrayList<String>();
	    	data.add("Title1");
	    	data.add("Title2");
	    	// DataFrame
	    	Dataset<Row> singleColResult = spark().createDataset(data, Encoders.STRING()).toDF();
	    	Dataset<Row> expectedResult = singleColResult.selectExpr("split(value, ',')[0] as title");
		
		//ACTUAL
		final Dataset<Row> actualResult = joinTree.compute(spark().sqlContext()).orderBy("title");
		final Dataset<Row> nullableActualResult = sqlContext().createDataFrame(actualResult.collectAsList(),
				actualResult.schema().asNullable());
				
		assertDataFrameEquals(expectedResult, nullableActualResult);
	}

	private void queryOnJwptLeftOuter(final DatabaseStatistics statistics, final Dataset<Row> fullDataset) {
		final Settings settings = new Settings.Builder("queryTest01_db").usingJWPTLeftouterNodes().build();
		final ClassLoader classLoader = getClass().getClassLoader();
		final Translator translator = new Translator(settings, statistics,
				classLoader.getResource("queryTestSingleTriple1.q").getPath());
		final JoinTree joinTree = translator.translateQuery();
		
		//EXPECTED
		List<String> data = new ArrayList<String>();
	    	data.add("Title1");
	    	data.add("Title2");
	    	// DataFrame
	    	Dataset<Row> singleColResult = spark().createDataset(data, Encoders.STRING()).toDF();
	    	Dataset<Row> expectedResult = singleColResult.selectExpr("split(value, ',')[0] as title");
		
		//ACTUAL
		final Dataset<Row> actualResult = joinTree.compute(spark().sqlContext()).orderBy("title");
		final Dataset<Row> nullableActualResult = sqlContext().createDataFrame(actualResult.collectAsList(),
				actualResult.schema().asNullable());
				
		assertDataFrameEquals(expectedResult, nullableActualResult);
	}

	private Dataset<Row> initializeDb(final DatabaseStatistics statistics) {
		spark().sql("DROP DATABASE IF EXISTS queryTest01_db CASCADE");
		spark().sql("CREATE DATABASE IF NOT EXISTS  queryTest01_db");
		spark().sql("USE queryTest01_db");

				
		// creates test tt table
		final TripleBean t1 = new TripleBean();
		t1.setS("<http://example.org/book1>");
		t1.setP("<http://example.org/title>");
		t1.setO("Title1");

		final TripleBean t2 = new TripleBean();
		t2.setS("<http://example.org/book2>");
		t2.setP("<http://example.org/title>");
		t2.setO("Title2");

		final ArrayList<TripleBean> triplesList = new ArrayList<>();
		triplesList.add(t1);
		triplesList.add(t2);

		final Dataset<Row> ttDataset = spark().createDataset(triplesList, triplesEncoder).select("s", "p", "o").orderBy(
				"s", "p", "o");
		ttDataset.write().saveAsTable("tripletable");

		final loader.Settings loaderSettings =
				new loader.Settings.Builder("queryTest01_db").withInputPath((System.getProperty(
						"user.dir") + "\\target\\test_output\\SingleTriplePatternTest").replace('\\', '/'))
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
	/*
	@Test
	public void queryTest2() {
		final DatabaseStatistics statistics = new DatabaseStatistics("queryTest02_db");
		Dataset<Row> fullDataset = initializeDb2(statistics);
		fullDataset = fullDataset.orderBy("s", "p", "o");
		queryOnTT2(statistics, fullDataset);
		queryOnVp2(statistics, fullDataset);
		queryOnWpt2(statistics, fullDataset);
		queryOnIwpt2(statistics, fullDataset);
		queryOnJwptOuter2(statistics, fullDataset);
		queryOnJwptLeftOuter2(statistics, fullDataset);
	}
	
	  
	private void queryOnTT2(final DatabaseStatistics statistics, final Dataset<Row> fullDataset) {
		final Settings settings = new Settings.Builder("queryTest02_db").usingTTNodes().build();
		final ClassLoader classLoader = getClass().getClassLoader();
		final Translator translator = new Translator(settings, statistics,
				classLoader.getResource("queryTestSingleTriple2.q").getPath());
		final JoinTree joinTree = translator.translateQuery();
		

		//EXPECTED
		StructType schema = DataTypes.createStructType(new StructField[]{
				DataTypes.createStructField("book", DataTypes.StringType, true),
				DataTypes.createStructField("title", DataTypes.StringType, true),
				});
		Row row1 = RowFactory.create("<http://example.org/book1>", "Title1"); //"Science"
		Row row2 = RowFactory.create("<http://example.org/book2>", "Title2");
		List<Row> rowList = ImmutableList.of(row1, row2);
		Dataset<Row> expectedResult = spark().createDataFrame(rowList, schema);
			    
		//ACTUAL
		final Dataset<Row> actualResult = joinTree.compute(spark().sqlContext()).orderBy("book", "title");
		final Dataset<Row> nullableActualResult = sqlContext().createDataFrame(actualResult.collectAsList(),
				actualResult.schema().asNullable());
		System.out.print("SingleTriplePatternTest: queryTest1");
		expectedResult.printSchema();
		expectedResult.show();
		System.out.println(joinTree.toString());	
		nullableActualResult.printSchema();
		nullableActualResult.show();		
		assertDataFrameEquals(expectedResult, nullableActualResult);
	}
	
	private void queryOnVp2(final DatabaseStatistics statistics, final Dataset<Row> fullDataset) {
		final Settings settings = new Settings.Builder("queryTest02_db").usingVPNodes().build();
		final ClassLoader classLoader = getClass().getClassLoader();
		final Translator translator = new Translator(settings, statistics,
				classLoader.getResource("queryTestSingleTriple2.q").getPath());
		final JoinTree joinTree = translator.translateQuery();
		
		//EXPECTED
		StructType schema = DataTypes.createStructType(new StructField[]{
				DataTypes.createStructField("book", DataTypes.StringType, true),
				DataTypes.createStructField("title", DataTypes.StringType, true),
				});
		Row row1 = RowFactory.create("<http://example.org/book1>", "Title1"); 
		Row row2 = RowFactory.create("<http://example.org/book2>", "Title2");
		List<Row> rowList = ImmutableList.of(row1, row2);
		Dataset<Row> expectedResult = spark().createDataFrame(rowList, schema);
		
		//ACTUAL
		final Dataset<Row> actualResult = joinTree.compute(spark().sqlContext()).orderBy("book", "title");
		final Dataset<Row> nullableActualResult = sqlContext().createDataFrame(actualResult.collectAsList(),
				actualResult.schema().asNullable());
		
		assertDataFrameEquals(expectedResult, nullableActualResult);
	}

	private void queryOnWpt2(final DatabaseStatistics statistics, final Dataset<Row> fullDataset) {
		final Settings settings = new Settings.Builder("queryTest02_db").usingWPTNodes().build();
		final ClassLoader classLoader = getClass().getClassLoader();
		final Translator translator = new Translator(settings, statistics,
				classLoader.getResource("queryTestSingleTriple2.q").getPath());
		final JoinTree joinTree = translator.translateQuery();
		
		//EXPECTED
		StructType schema = DataTypes.createStructType(new StructField[]{
				DataTypes.createStructField("book", DataTypes.StringType, true),
				DataTypes.createStructField("title", DataTypes.StringType, true),
				});
		Row row1 = RowFactory.create("<http://example.org/book1>", "Title1"); //"Science"
		Row row2 = RowFactory.create("<http://example.org/book2>", "Title2");
		List<Row> rowList = ImmutableList.of(row1, row2);
		Dataset<Row> expectedResult = spark().createDataFrame(rowList, schema);
			    
		//ACTUAL
		final Dataset<Row> actualResult = joinTree.compute(spark().sqlContext()).orderBy("book", "title");
		final Dataset<Row> nullableActualResult = sqlContext().createDataFrame(actualResult.collectAsList(),
				actualResult.schema().asNullable());
				
		assertDataFrameEquals(expectedResult, nullableActualResult);
	}

	private void queryOnIwpt2(final DatabaseStatistics statistics, final Dataset<Row> fullDataset) {
		final Settings settings = new Settings.Builder("queryTest02_db").usingIWPTNodes().build();
		final ClassLoader classLoader = getClass().getClassLoader();
		final Translator translator = new Translator(settings, statistics,
				classLoader.getResource("queryTestSingleTriple2.q").getPath());
		final JoinTree joinTree = translator.translateQuery();

		//EXPECTED
		StructType schema = DataTypes.createStructType(new StructField[]{
				DataTypes.createStructField("book", DataTypes.StringType, true),
				DataTypes.createStructField("title", DataTypes.StringType, true),
				});
		Row row1 = RowFactory.create("<http://example.org/book1>", "Title1"); //"Science"
		Row row2 = RowFactory.create("<http://example.org/book2>", "Title2");
		List<Row> rowList = ImmutableList.of(row1, row2);
		Dataset<Row> expectedResult = spark().createDataFrame(rowList, schema);
			    
		//ACTUAL
		final Dataset<Row> actualResult = joinTree.compute(spark().sqlContext()).orderBy("book", "title");
		final Dataset<Row> nullableActualResult = sqlContext().createDataFrame(actualResult.collectAsList(),
				actualResult.schema().asNullable());
				
		assertDataFrameEquals(expectedResult, nullableActualResult);
	}

	private void queryOnJwptOuter2(final DatabaseStatistics statistics, final Dataset<Row> fullDataset) {
		final Settings settings = new Settings.Builder("queryTest02_db").usingJWPTOuterNodes().build();
		final ClassLoader classLoader = getClass().getClassLoader();
		final Translator translator = new Translator(settings, statistics,
				classLoader.getResource("queryTestSingleTriple2.q").getPath());
		final JoinTree joinTree = translator.translateQuery();
		
		//EXPECTED
		StructType schema = DataTypes.createStructType(new StructField[]{
				DataTypes.createStructField("book", DataTypes.StringType, true),
				DataTypes.createStructField("title", DataTypes.StringType, true),
				});
		Row row1 = RowFactory.create("<http://example.org/book1>", "Title1"); //"Science"
		Row row2 = RowFactory.create("<http://example.org/book2>", "Title2");
		List<Row> rowList = ImmutableList.of(row1, row2);
		Dataset<Row> expectedResult = spark().createDataFrame(rowList, schema);
			    
		//ACTUAL
		final Dataset<Row> actualResult = joinTree.compute(spark().sqlContext()).orderBy("book", "title");
		final Dataset<Row> nullableActualResult = sqlContext().createDataFrame(actualResult.collectAsList(),
				actualResult.schema().asNullable());
				
		assertDataFrameEquals(expectedResult, nullableActualResult);
	}

	private void queryOnJwptLeftOuter2(final DatabaseStatistics statistics, final Dataset<Row> fullDataset) {
		final Settings settings = new Settings.Builder("queryTest02_db").usingJWPTLeftouterNodes().build();
		final ClassLoader classLoader = getClass().getClassLoader();
		final Translator translator = new Translator(settings, statistics,
				classLoader.getResource("queryTestSingleTriple2.q").getPath());
		final JoinTree joinTree = translator.translateQuery();
		
		//EXPECTED
		StructType schema = DataTypes.createStructType(new StructField[]{
				DataTypes.createStructField("book", DataTypes.StringType, true),
				DataTypes.createStructField("title", DataTypes.StringType, true),
				});
		Row row1 = RowFactory.create("<http://example.org/book1>", "Title1"); //"Science"
		Row row2 = RowFactory.create("<http://example.org/book2>", "Title2");
		List<Row> rowList = ImmutableList.of(row1, row2);
		Dataset<Row> expectedResult = spark().createDataFrame(rowList, schema);
			    
		//ACTUAL
		final Dataset<Row> actualResult = joinTree.compute(spark().sqlContext()).orderBy("book", "title");
		final Dataset<Row> nullableActualResult = sqlContext().createDataFrame(actualResult.collectAsList(),
				actualResult.schema().asNullable());
				
		assertDataFrameEquals(expectedResult, nullableActualResult);
	}

	private Dataset<Row> initializeDb2(final DatabaseStatistics statistics) {
		spark().sql("DROP DATABASE IF EXISTS queryTest02_db CASCADE");
		spark().sql("CREATE DATABASE IF NOT EXISTS  queryTest02_db");
		spark().sql("USE queryTest02_db");

				
		// creates test tt table
		final TripleBean t1 = new TripleBean();
		t1.setS("<http://example.org/book1>");
		t1.setP("<http://example.org/title>");
		t1.setO("Title1");

		final TripleBean t2 = new TripleBean();
		t2.setS("<http://example.org/book2>");
		t2.setP("<http://example.org/title>");
		t2.setO("Title2");

		final ArrayList<TripleBean> triplesList = new ArrayList<>();
		triplesList.add(t1);
		triplesList.add(t2);

		final Dataset<Row> ttDataset = spark().createDataset(triplesList, triplesEncoder).select("s", "p", "o").orderBy(
				"s", "p", "o");
		ttDataset.write().saveAsTable("tripletable");

		final loader.Settings loaderSettings =
				new loader.Settings.Builder("queryTest02_db").withInputPath((System.getProperty(
						"user.dir") + "\\target\\test_output\\SingleTriplePatternTest").replace('\\', '/'))
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
	public void queryTest() {
		final DatabaseStatistics statistics = new DatabaseStatistics("queryTest01b_db");
		Dataset<Row> fullDataset = initializeDb(statistics);
		fullDataset = fullDataset.orderBy("s", "p", "o");
		queryOnTT(statistics, fullDataset);
		queryOnVp(statistics, fullDataset);
		queryOnWpt(statistics, fullDataset);
		queryOnIwpt(statistics, fullDataset);
		queryOnJwptOuter(statistics, fullDataset);
		queryOnJwptLeftOuter(statistics, fullDataset);
	}

	private void queryOnTT(final DatabaseStatistics statistics, final Dataset<Row> fullDataset) {
		final Settings settings = new Settings.Builder("queryTest01b_db").usingTTNodes().build();
		final ClassLoader classLoader = getClass().getClassLoader();
		final Translator translator = new Translator(settings, statistics,
				classLoader.getResource("queryTestSingleTriple1.q").getPath());
		final JoinTree joinTree = translator.translateQuery();
		
		//EXPECTED
		List<String> data = new ArrayList<String>();
	    	data.add("Title1");
	    	data.add("Title2");
	    	// DataFrame
	    	Dataset<Row> singleColResult = spark().createDataset(data, Encoders.STRING()).toDF();
	    	Dataset<Row> expectedResult = singleColResult.selectExpr("split(value, ',')[0] as title");
		//ACTUAL
		final Dataset<Row> actualResult = joinTree.compute(spark().sqlContext()).orderBy("title");
		final Dataset<Row> nullableActualResult = sqlContext().createDataFrame(actualResult.collectAsList(),
				actualResult.schema().asNullable());
		
		System.out.print("SingleTriplePatternTest: queryTest2");
		expectedResult.printSchema();
		expectedResult.show();
		System.out.println(joinTree.toString());	
		nullableActualResult.printSchema();
		nullableActualResult.show();
		assertDataFrameEquals(expectedResult, nullableActualResult);
	}
	
	private void queryOnVp(final DatabaseStatistics statistics, final Dataset<Row> fullDataset) {
		final Settings settings = new Settings.Builder("queryTest01b_db").usingVPNodes().build();
		final ClassLoader classLoader = getClass().getClassLoader();
		final Translator translator = new Translator(settings, statistics,
				classLoader.getResource("queryTestSingleTriple1.q").getPath());
		final JoinTree joinTree = translator.translateQuery();
		
		//EXPECTED
		List<String> data = new ArrayList<String>();
	    	data.add("Title1");
	    	data.add("Title2");
	    	// DataFrame
	    	Dataset<Row> singleColResult = spark().createDataset(data, Encoders.STRING()).toDF();
	    	Dataset<Row> expectedResult = singleColResult.selectExpr("split(value, ',')[0] as title");	
		
		//ACTUAL
		final Dataset<Row> actualResult = joinTree.compute(spark().sqlContext()).orderBy("title");
		final Dataset<Row> nullableActualResult = sqlContext().createDataFrame(actualResult.collectAsList(),
				actualResult.schema().asNullable());
		
		assertDataFrameEquals(expectedResult, nullableActualResult);
	}

	private void queryOnWpt(final DatabaseStatistics statistics, final Dataset<Row> fullDataset) {
		final Settings settings = new Settings.Builder("queryTest01b_db").usingWPTNodes().build();
		final ClassLoader classLoader = getClass().getClassLoader();
		final Translator translator = new Translator(settings, statistics,
				classLoader.getResource("queryTestSingleTriple1.q").getPath());
		final JoinTree joinTree = translator.translateQuery();
		
		//EXPECTED
		List<String> data = new ArrayList<String>();
	    	data.add("Title1");
	    	data.add("Title2");
	    	// DataFrame
	    	Dataset<Row> singleColResult = spark().createDataset(data, Encoders.STRING()).toDF();
	    	Dataset<Row> expectedResult = singleColResult.selectExpr("split(value, ',')[0] as title");
		
		//ACTUAL
		final Dataset<Row> actualResult = joinTree.compute(spark().sqlContext()).orderBy("title");
		final Dataset<Row> nullableActualResult = sqlContext().createDataFrame(actualResult.collectAsList(),
				actualResult.schema().asNullable());
				
		assertDataFrameEquals(expectedResult, nullableActualResult);
	}

	private void queryOnIwpt(final DatabaseStatistics statistics, final Dataset<Row> fullDataset) {
		final Settings settings = new Settings.Builder("queryTest01b_db").usingIWPTNodes().build();
		final ClassLoader classLoader = getClass().getClassLoader();
		final Translator translator = new Translator(settings, statistics,
				classLoader.getResource("queryTestSingleTriple1.q").getPath());
		final JoinTree joinTree = translator.translateQuery();

		//EXPECTED
		List<String> data = new ArrayList<String>();
	    	data.add("Title1");
	    	data.add("Title2");
	    	// DataFrame
	    	Dataset<Row> singleColResult = spark().createDataset(data, Encoders.STRING()).toDF();
	    	Dataset<Row> expectedResult = singleColResult.selectExpr("split(value, ',')[0] as title");
		
		//ACTUAL
		final Dataset<Row> actualResult = joinTree.compute(spark().sqlContext()).orderBy("title");
		final Dataset<Row> nullableActualResult = sqlContext().createDataFrame(actualResult.collectAsList(),
				actualResult.schema().asNullable());
				
		assertDataFrameEquals(expectedResult, nullableActualResult);
	}

	private void queryOnJwptOuter(final DatabaseStatistics statistics, final Dataset<Row> fullDataset) {
		final Settings settings = new Settings.Builder("queryTest01b_db").usingJWPTOuterNodes().build();
		final ClassLoader classLoader = getClass().getClassLoader();
		final Translator translator = new Translator(settings, statistics,
				classLoader.getResource("queryTestSingleTriple1.q").getPath());
		final JoinTree joinTree = translator.translateQuery();
		
		//EXPECTED
		List<String> data = new ArrayList<String>();
	    	data.add("Title1");
	    	data.add("Title2");
	    	// DataFrame
	    	Dataset<Row> singleColResult = spark().createDataset(data, Encoders.STRING()).toDF();
	    	Dataset<Row> expectedResult = singleColResult.selectExpr("split(value, ',')[0] as title");
		
		//ACTUAL
		final Dataset<Row> actualResult = joinTree.compute(spark().sqlContext()).orderBy("title");
		final Dataset<Row> nullableActualResult = sqlContext().createDataFrame(actualResult.collectAsList(),
				actualResult.schema().asNullable());
				
		assertDataFrameEquals(expectedResult, nullableActualResult);
	}

	private void queryOnJwptLeftOuter(final DatabaseStatistics statistics, final Dataset<Row> fullDataset) {
		final Settings settings = new Settings.Builder("queryTest01b_db").usingJWPTLeftouterNodes().build();
		final ClassLoader classLoader = getClass().getClassLoader();
		final Translator translator = new Translator(settings, statistics,
				classLoader.getResource("queryTestSingleTriple1.q").getPath());
		final JoinTree joinTree = translator.translateQuery();
		
		//EXPECTED
		List<String> data = new ArrayList<String>();
	    	data.add("Title1");
	    	data.add("Title2");
	    	// DataFrame
	    	Dataset<Row> singleColResult = spark().createDataset(data, Encoders.STRING()).toDF();
	    	Dataset<Row> expectedResult = singleColResult.selectExpr("split(value, ',')[0] as title");
		
		//ACTUAL
		final Dataset<Row> actualResult = joinTree.compute(spark().sqlContext()).orderBy("title");
		final Dataset<Row> nullableActualResult = sqlContext().createDataFrame(actualResult.collectAsList(),
				actualResult.schema().asNullable());
				
		assertDataFrameEquals(expectedResult, nullableActualResult);
	}

	private Dataset<Row> initializeDb(final DatabaseStatistics statistics) {
		spark().sql("DROP DATABASE IF EXISTS queryTest01b_db CASCADE");
		spark().sql("CREATE DATABASE IF NOT EXISTS  queryTest01b_db");
		spark().sql("USE queryTest01b_db");

				
		// creates test tt table
		final TripleBean t1 = new TripleBean();
		t1.setS("<http://example.org/book1>");
		t1.setP("<http://example.org/title>");
		t1.setO("Title1");

		final TripleBean t2 = new TripleBean();
		t2.setS("<http://example.org/book2>");
		t2.setP("<http://example.org/title>");
		t2.setO("Title2");

		final ArrayList<TripleBean> triplesList = new ArrayList<>();
		triplesList.add(t1);
		triplesList.add(t2);

		final Dataset<Row> ttDataset = spark().createDataset(triplesList, triplesEncoder).select("s", "p", "o").orderBy(
				"s", "p", "o");
		ttDataset.write().saveAsTable("tripletable");

		final loader.Settings loaderSettings =
				new loader.Settings.Builder("queryTest01b_db").withInputPath((System.getProperty(
						"user.dir") + "\\target\\test_output\\SingleTriplePatternTest").replace('\\', '/'))
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
	*/
}


/*
PREFIX ex: <http://example.org/#>.
PREFIX at: <http://author1.com/#>.
PREFIX sp: <http://springer.com/#>.

TABLE:
================================================================================================================
ex:book1		| ex:title			| "Title1"
ex:book2		| ex:title			| "Title2"
================================================================================================================

QUERY:
-----------------------------------------------------------------------------------------------------------------
SELECT DISTINCT ?title
WHERE
{
    ?book <http://example.org/title> ?title.
}
-----------------------------------------------------------------------------------------------------------------

RESULT:
-----------------------------------------------------------------------------------------------------------------
Expected:
+------+
| title|
+------+
|Title1|
|Title2|
+------+

Actual:
-----------------------------------------------------------------------------------------------------------------
*/

/*
PREFIX ex: <http://example.org/#>.
PREFIX at: <http://author1.com/#>.
PREFIX sp: <http://springer.com/#>.

TABLE:
================================================================================================================
ex:book1		| ex:title			| "Title1"
ex:book2		| ex:title			| "Title2"
================================================================================================================

QUERY:
-----------------------------------------------------------------------------------------------------------------
SELECT DISTINCT ?book ?title
WHERE
{
	?book <http://example.org/title> ?title.
}
-----------------------------------------------------------------------------------------------------------------

RESULT:
-----------------------------------------------------------------------------------------------------------------
Expected:
+--------------------+------+
|                book| title|
+--------------------+------+
|<http://example.o...|Title1|
|<http://example.o...|Title2|
+--------------------+------+

Actual:
-----------------------------------------------------------------------------------------------------------------
*/