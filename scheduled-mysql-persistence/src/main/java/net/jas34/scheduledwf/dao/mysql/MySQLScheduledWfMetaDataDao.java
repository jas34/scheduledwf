package net.jas34.scheduledwf.dao.mysql;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.netflix.conductor.core.execution.ApplicationException;
import com.netflix.conductor.dao.mysql.MySQLBaseDAO;

import net.jas34.scheduledwf.dao.ScheduledWfMetadataDAO;
import net.jas34.scheduledwf.metadata.ScheduleWfDef;
import net.jas34.scheduledwf.metadata.ScheduleWfDef.Status;

public class MySQLScheduledWfMetaDataDao extends MySQLBaseDAO implements ScheduledWfMetadataDAO {

	protected MySQLScheduledWfMetaDataDao(ObjectMapper om, DataSource dataSource) {
		super(om, dataSource);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void saveScheduleWorkflow(ScheduleWfDef def) {
		validate(def);
		withTransaction(tx -> {
			if (scheduleWorkflowDefExists(tx, def)) {
				throw new ApplicationException(ApplicationException.Code.CONFLICT,
						"ScheduleWfDef with " + def.toString() + " already exists!");
			}

			insertOrUpdatescheduleWorkflowDef(tx, def);
		});

	}

	@Override
	public void updateScheduleWorkflow(ScheduleWfDef def) {
		validate(def);
		withTransaction(tx -> insertOrUpdatescheduleWorkflowDef(tx, def));

	}

	/*
	 * @Override public void removeScheduleWorkflow(String name) { final String
	 * DELETE_WORKFLOW_QUERY = "DELETE from schedule_wf_def WHERE name = ? ";
	 * 
	 * withTransaction(tx -> { execute(tx, DELETE_WORKFLOW_QUERY, q -> { if
	 * (!q.addParameter(name).executeDelete()) { throw new
	 * ApplicationException(ApplicationException.Code.NOT_FOUND,
	 * String.format("No such ScheduleWfDef : %s ", name)); } });
	 * 
	 * });
	 * 
	 * }
	 */
	@Override
	public Optional<ScheduleWfDef> getScheduledWorkflowDef(String name, int version) {
		final String GET_WORKFLOW_DEF_QUERY = "SELECT json_input FROM schedule_wf_def WHERE name = ? AND version = ?";
		return Optional.ofNullable(queryWithTransaction(GET_WORKFLOW_DEF_QUERY, q -> q.addParameter(name)
				.addParameter(String.valueOf(version)).executeAndFetchFirst(ScheduleWfDef.class)));

	}

	@Override
	public Optional<List<ScheduleWfDef>> getAllScheduledWorkflowDefsByStatus(Status status) {
		final String FIND_ALL_WORKFLOW_DEF_QUERY = "SELECT json_input FROM schedule_wf_def where status =?";

		return Optional.of(queryWithTransaction(FIND_ALL_WORKFLOW_DEF_QUERY,
				q -> q.addParameter(status.name()).executeAndFetch(ScheduleWfDef.class)));

	}

	private Boolean scheduleWorkflowDefExists(Connection connection, ScheduleWfDef def) {
		final String CHECK_WORKFLOW_DEF_EXISTS_QUERY = "SELECT COUNT(*) FROM schedule_wf_def WHERE name = ? AND "
				+ "version = ?";

		return query(connection, CHECK_WORKFLOW_DEF_EXISTS_QUERY,
				q -> q.addParameter(def.getWfName()).addParameter(def.getWfVersion()).exists());
	}

	private void insertOrUpdatescheduleWorkflowDef(Connection tx, ScheduleWfDef def) {
		final String INSERT_WORKFLOW_DEF_QUERY = "INSERT INTO schedule_wf_def (name, version,status, json_input,cron_expression) VALUES (?,"
				+ " ?, ?,?,?)";

		Optional<Integer> version = getLatestVersion(tx, def);
		if (!version.isPresent() || version.get() < def.getWfVersion()) {
			execute(tx, INSERT_WORKFLOW_DEF_QUERY,
					q -> q.addParameter(def.getWfName()).addParameter(def.getWfVersion())
							.addParameter(def.getStatus() != null ? def.getStatus().name() : null).addJsonParameter(def)
							.addParameter(def.getCronExpression()).executeUpdate());
		} else {
			// @formatter:off
			final String UPDATE_WORKFLOW_DEF_QUERY = "UPDATE schedule_wf_def "
					+ "SET json_input = ?,status=?, cron_expression=?, updated_on = CURRENT_TIMESTAMP "
					+ "WHERE name = ? AND version = ?";
			// @formatter:on

			execute(tx, UPDATE_WORKFLOW_DEF_QUERY,
					q -> q.addJsonParameter(def).addParameter(def.getStatus() != null ? def.getStatus().name() : null)
							.addParameter(def.getCronExpression()).addParameter(def.getWfName())
							.addParameter(def.getWfVersion()).executeUpdate());
		}

	}

	private Optional<Integer> getLatestVersion(Connection tx, ScheduleWfDef def) {
		final String GET_LATEST_WORKFLOW_DEF_VERSION = "SELECT max(version) AS version FROM schedule_wf_def WHERE "
				+ "name = ?";

		Integer val = query(tx, GET_LATEST_WORKFLOW_DEF_VERSION, q -> {
			q.addParameter(def.getWfName());
			return q.executeAndFetch(rs -> {
				if (!rs.next()) {
					return null;
				}

				return rs.getInt(1);
			});
		});

		return Optional.ofNullable(val);
	}

	private void validate(ScheduleWfDef def) {
		Preconditions.checkNotNull(def, "ScheduleWfDef object cannot be null");
		Preconditions.checkNotNull(def.getWfName(), "ScheduleWfDef name cannot be null");
	}

}
