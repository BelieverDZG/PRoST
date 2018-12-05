package stats;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import stats.ProtobufStats.Graph;
import stats.ProtobufStats.TableStats;

public class StatisticsWriter {

	protected static final Logger logger = Logger.getLogger("PRoST");

	// single instance of the statistics
	private static StatisticsWriter instance = null;
	
	private boolean useStatistics = false;
	private String stats_file_suffix = ".stats";
	private Vector<TableStats> tableStatistics;
	private Vector<CharacteristicSet> characteristicSets;

	protected StatisticsWriter() {
		// Exists only to defeat instantiation.
	}

	public static StatisticsWriter getInstance() {
		if (instance == null) {
			instance.tableStatistics = new Vector<TableStats>();
			instance.setCharacteristicSets(new Vector<CharacteristicSet>());
		}
		return instance;
	}

	/*
	 * Save the statistics in a serialized file
	 */
	public void saveStatistics(final String fileName) {
		// if statistics are not needed
		if (!useStatistics)
			return;

		final Graph.Builder graph_stats_builder = Graph.newBuilder();
		// add table statistics
		if (tableStatistics != null) {
			graph_stats_builder.addAllTables(this.tableStatistics);
		}
		final Graph serialized_stats = graph_stats_builder.build();
		FileOutputStream f_stream; // s
		File file;
		try {
			file = new File(fileName + stats_file_suffix);
			f_stream = new FileOutputStream(file);
			serialized_stats.writeTo(f_stream);
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Calculate the statistics for a single table: size, number of distinct
	 * subjects and isComplex. It returns a protobuf object defined in
	 * ProtobufStats.proto
	 */
	public void addStatsTable(final Dataset<Row> table, final String tableName, final String subjectColumnName) {
		// if statistics are not needed
		if (!useStatistics)
			return;

		final TableStats.Builder table_stats_builder = TableStats.newBuilder();

		// calculate the stats
		final int table_size = (int) table.count();
		final int distinct_subjects = (int) table.select(subjectColumnName).distinct().count();
		final boolean is_complex = table_size != distinct_subjects;

		table_stats_builder.setSize(table_size).setDistinctSubjects(distinct_subjects).setIsComplex(is_complex)
				.setName(tableName);

		if (table.sparkSession().catalog().tableExists("inverse_properties")) {
			final String query = new String("select is_complex from inverse_properties where p='" + tableName + "'");
			final boolean isInverseComplex = table.sparkSession().sql(query.toString()).head().getInt(0) == 1;
			// put them in the protobuf object
			table_stats_builder.setIsInverseComplex(isInverseComplex);
		}

		logger.info(
				"Adding these properties to Protobuf object. Table size:" + table_size + ", " + "Distinct subjects: "
						+ distinct_subjects + ", Is complex:" + is_complex + ", " + "tableName:" + tableName);

		tableStatistics.add(table_stats_builder.build());
	}

	//TODO add comments
	public void computeCharacteristicSets(Dataset<Row> triples) {
		
	}
	
	public List<TableStats> getTableStatistics() {
		return tableStatistics;
	}

	public void setTableStatistics(Vector<TableStats> tableStatistics) {
		this.tableStatistics = tableStatistics;
	}

	public boolean isUseStatistics() {
		return useStatistics;
	}

	public void setUseStatistics(boolean useStatistics) {
		this.useStatistics = useStatistics;
	}

	public Vector<CharacteristicSet> getCharacteristicSets() {
		return characteristicSets;
	}

	public void setCharacteristicSets(Vector<CharacteristicSet> characteristicSets) {
		this.characteristicSets = characteristicSets;
	}
}