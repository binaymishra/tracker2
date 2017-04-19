package tracker2;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import tracker2.domain.TableA;
import tracker2.domain.TableB;
import tracker2.domain.TableC;

@Repository
public class TrackerRepository {

	private final String tableAinsert = "insert into table_a(userIp, userMac, apName, apMac, startTime, endTime, startTS, endTS) values(?, ?, ?, ?, ?, ?, ?, ?)";
	private final String tableBinsert = "insert into table_b(userIp, userMac, apName, apMac, startTime, endTime, startTS, endTS) values(?, ?, ?, ?, ?, ?, ?, ?)";
	private final String tableCinsert = "insert into table_c(prefix, lat, lon, name, category) values(?, ?, ?, ?, ?)";

	//private final String selectFromTableAByStartAndEndTime = "SELECT * FROM TABLE_A WHERE STARTTS BETWEEN (SELECT MIN(STARTTS)  FROM TABLE_A) AND (SELECT MAX(STARTTS) FROM TABLE_A) AND ENDTS BETWEEN (SELECT MIN(ENDTS) FROM TABLE_A) AND (SELECT MAX(ENDTS) FROM TABLE_A) ORDER BY STARTTS";
	private final String selectFromTableA = "select * from table_a order by startTime";
	private final String selectFromTableB = "select * from table_b order by startTime";
	private final String selectFromTableC = "select * from table_c";

	JdbcTemplate template;

	@Autowired
	public TrackerRepository(DataSource dataSource) {
		template = new JdbcTemplate(dataSource);
	}

	@Autowired
	TrackerComponent trackerComponent;

	@Transactional
	public void insertTableA() throws IOException {
		long startTime = System.currentTimeMillis();
		final List<TableA> tableA = trackerComponent.readFileTableA20120407();
		int[] rows = template.batchUpdate(tableAinsert, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				TableA a = tableA.get(i);
				ps.setString(1, a.getUserIp());
				ps.setString(2, a.getUserMac());
				ps.setString(3, a.getApName());
				ps.setString(4, a.getApMac());
				ps.setLong(5, a.getStartTime());
				ps.setLong(6,a.getEndTime()); 
				ps.setTimestamp(7,new Timestamp(a.getEndTime() * 1000L)); 
				ps.setTimestamp(8,new Timestamp(a.getEndTime() * 1000L)); 

			}

			@Override
			public int getBatchSize() {
				return tableA.size();
			}
		});
		System.out.println(String.format("TrackerRepository.insertTableA() -> %d rows are inserted into 'table_a'. time elapsed = %d ms", rows.length, (System.currentTimeMillis() - startTime)));
	}

	@Transactional
	public void insertTableB() throws IOException {
		long startTime = System.currentTimeMillis();
		final List<TableB> tableB = trackerComponent.readFileTableB20120409();
		int[] rows = template.batchUpdate(tableBinsert, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				TableB b = tableB.get(i);
				ps.setString(1, b.getUserIp());
				ps.setString(2, b.getUserMac());
				ps.setString(3, b.getApName());
				ps.setString(4, b.getApMac());
				ps.setLong(5, b.getStartTime());
				ps.setLong(6,b.getEndTime());
				ps.setTimestamp(7,new Timestamp(b.getEndTime() * 1000L)); 
				ps.setTimestamp(8,new Timestamp(b.getEndTime() * 1000L)); 

			}

			@Override
			public int getBatchSize() {
				return tableB.size();
			}
		});
		System.out.println(String.format("TrackerRepository.insertTableB() -> %d rows are inserted into 'table_b'. time elapsed = %d ms", rows.length, (System.currentTimeMillis() - startTime)));
	}

	@Transactional
	public void insertTableC() throws IOException {
		long startTime = System.currentTimeMillis();
		final List<TableC> tableC = trackerComponent.readFileTableC();
		int[] rows = template.batchUpdate(tableCinsert, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				TableC c = tableC.get(i);
				ps.setString(1, c.getPrefix());
				ps.setDouble(2, c.getLat());
				ps.setDouble(3, c.getLon());
				ps.setString(4, c.getName());
				ps.setString(5, c.getCategory());

			}

			@Override
			public int getBatchSize() {
				return tableC.size();
			}

		});

		System.out.println(String.format("TrackerRepository.insertTableC() -> %d rows are inserted into 'table_c'. time elapsed = %d ms", rows.length, (System.currentTimeMillis() - startTime)));
	}

	public List<TableA> fetchAllFromTableA() {
		long startTime = System.currentTimeMillis();
		List<TableA> tableA = Collections.emptyList();
		try {
			tableA =  template.query(selectFromTableA, new RowMapper<TableA>() {

				@Override
				public TableA mapRow(ResultSet rs, int rowNum) throws SQLException {
					return new TableA(
							rs.getString("userIp"), 
							rs.getString("userMac"), 
							rs.getString("apName"), 
							rs.getString("apMac"), 
							rs.getLong("startTime"),
							rs.getLong("endTime")
						);
				}
			});
		} catch (DataAccessException e) {
			ReflectionUtils.rethrowRuntimeException(e);
		}
		System.out.println(String.format("%s . record found = %d. Time elapsed = %d ms.", selectFromTableA, tableA.size(), (System.currentTimeMillis() - startTime)));
		return tableA;
	}

	public List<TableB> fetchAllFromTableB() {
		long startTime = System.currentTimeMillis();
		List<TableB> tableB = Collections.emptyList();
		try {
			tableB =  template.query(selectFromTableB, new RowMapper<TableB>() {

				@Override
				public TableB mapRow(ResultSet rs, int rowNum) throws SQLException {
					return new TableB(
							rs.getString("userIp"), 
							rs.getString("userMac"), 
							rs.getString("apName"), 
							rs.getString("apMac"), 
							rs.getLong("startTime"),
							rs.getLong("endTime")
						);
				}
			});
		} catch (DataAccessException e) {
			ReflectionUtils.rethrowRuntimeException(e);
		}
		System.out.println(String.format("%s . record found = %d. Time elapsed = %d ms.", selectFromTableB, tableB.size(), (System.currentTimeMillis() - startTime)));
		return tableB;
	}

	public List<TableC> fetchAllFromTableC() {
		long startTime = System.currentTimeMillis();
		List<TableC> tableC = Collections.emptyList();

		try {
			tableC =  template.query(selectFromTableC, new RowMapper<TableC>() {

				@Override
				public TableC mapRow(ResultSet rs, int rowNum) throws SQLException {
					return new TableC(
								rs.getString("prefix"), 
								rs.getDouble("lat"), 
								rs.getDouble("lon"),  
								rs.getString("name"), 
								rs.getString("category")
								);
				}
			});
		} catch (DataAccessException e) {
			ReflectionUtils.rethrowRuntimeException(e);
		}
		System.out.println(String.format("%s . record found = %d. Time elapsed = %d ms.", selectFromTableC, tableC.size(), (System.currentTimeMillis() - startTime)));
		return tableC;
	}

}
