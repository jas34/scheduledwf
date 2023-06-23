package io.github.jas34.scheduledwf.dao.postgres;

import com.google.common.base.Preconditions;
import com.netflix.conductor.postgres.dao.PostgresBaseDAO;
import com.netflix.conductor.postgres.util.Query;
import io.github.jas34.scheduledwf.metadata.ScheduleWfDef;
import org.springframework.retry.support.RetryTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.jas34.scheduledwf.dao.ScheduledWfMetadataDAO;


import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class PostgreSQLScheduledWfMetaDataDao extends PostgresBaseDAO implements ScheduledWfMetadataDAO{

    public PostgreSQLScheduledWfMetaDataDao(RetryTemplate retryTemplate, ObjectMapper om, DataSource dataSource) {
        //retryTemplate is required in conductor 3.13.5+
        super(retryTemplate, om, dataSource);
    }

    @Override
    public void saveScheduleWorkflow(ScheduleWfDef def) {
        validate(def);
        withTransaction(tx -> {
            insertOrUpdatescheduleWorkflowDef(tx, def);
        });

    }

    @Override
    public void updateScheduleWorkflow(ScheduleWfDef def) {
        validate(def);
        withTransaction(tx -> insertOrUpdatescheduleWorkflowDef(tx, def));

    }

    @Override
    public boolean removeScheduleWorkflow(String name) {
        String REMOVE_WORKFLOW_DEF = "DELETE FROM schedule_wf_def WHERE name = ?";
        AtomicBoolean isRemoved = new AtomicBoolean(false);
        withTransaction(conn -> {
            execute(conn, REMOVE_WORKFLOW_DEF, q -> isRemoved.set(q.addParameter(name).executeDelete()));
        });
        return isRemoved.get();
    }


    @Override
    public boolean removeScheduleWorkflows(List<String> names) {
        String REMOVE_WORKFLOW_DEFS = String.format("DELETE FROM schedule_wf_def WHERE name in (%s)",
                Query.generateInBindings(names.size()));
        AtomicBoolean isRemoved = new AtomicBoolean(false);
        withTransaction(conn -> {
            execute(conn, REMOVE_WORKFLOW_DEFS, q -> isRemoved.set(q.addParameters(names).executeDelete()));
        });
        return isRemoved.get();
    }

    @Override
    public Optional<ScheduleWfDef> getScheduledWorkflowDef(String name) {
        final String GET_WORKFLOW_DEF_QUERY = "SELECT json_input FROM schedule_wf_def WHERE name = ?";

        return Optional.ofNullable(this.queryWithTransaction(GET_WORKFLOW_DEF_QUERY,
                (q) -> q.addParameter(name).executeAndFetchFirst(ScheduleWfDef.class)));
    }

    @Override
    public Optional<List<ScheduleWfDef>> getAllScheduledWorkflowDefsByStatus(ScheduleWfDef.Status... status) {
        List<String> statuses = Arrays.stream(status).map(Enum::name).collect(Collectors.toList());
        final String GET_WORKFLOW_DEFS_BY_STATUS_QUERY =
                String.format("SELECT json_input FROM schedule_wf_def WHERE status in (%s)",
                        Query.generateInBindings(status.length));
        Optional<List<ScheduleWfDef>> scheduleWfDefs =
                Optional.ofNullable(queryWithTransaction(GET_WORKFLOW_DEFS_BY_STATUS_QUERY,
                        q -> q.addParameters(statuses).executeAndFetch(ScheduleWfDef.class)));
        return scheduleWfDefs;
    }


    @Override
    public Optional<List<ScheduleWfDef>> getAllScheduledWorkflowDefs() {
        final String GET_ALL_WORKFLOW_DEFS = "SELECT json_input FROM schedule_wf_def";
        Optional<List<ScheduleWfDef>> scheduleWfDefs = Optional.ofNullable(
                queryWithTransaction(GET_ALL_WORKFLOW_DEFS, q -> q.executeAndFetch(ScheduleWfDef.class)));
        return scheduleWfDefs;
    }


    private Boolean scheduleWorkflowDefExists(Connection connection, ScheduleWfDef def) {
        final String CHECK_WORKFLOW_DEF_EXISTS_QUERY = "SELECT COUNT(*) FROM schedule_wf_def WHERE name = ?";

        return query(connection, CHECK_WORKFLOW_DEF_EXISTS_QUERY,
                q -> q.addParameter(def.getWfName()).exists());
    }


    private void validate(ScheduleWfDef def) {
        Preconditions.checkNotNull(def, "ScheduleWfDef object cannot be null");
        Preconditions.checkNotNull(def.getWfName(), "ScheduleWfDef name cannot be null");
    }

    private void insertOrUpdatescheduleWorkflowDef(Connection tx, ScheduleWfDef def) {
        final String INSERT_WORKFLOW_DEF_QUERY =
                "INSERT INTO schedule_wf_def (name, version,status, json_input,cron_expression) VALUES (?,"
                        + " ?, ?, ?, ?)";

        Optional<ScheduleWfDef> wfDefOptional = getScheduledWorkflowDef(def.getWfName());
        if (!wfDefOptional.isPresent()) {
            execute(tx, INSERT_WORKFLOW_DEF_QUERY,
                    q -> q.addParameter(def.getWfName()).addParameter(def.getWfVersion())
                            .addParameter(def.getStatus() != null ? def.getStatus().name() : null)
                            .addJsonParameter(def).addParameter(def.getCronExpression()).executeUpdate());
        } else {
            // @formatter:off
            final String UPDATE_WORKFLOW_DEF_QUERY = "UPDATE schedule_wf_def "
                    + "SET json_input = ?,status=?, cron_expression=?, version=?, updated_on = CURRENT_TIMESTAMP "
                    + "WHERE name = ?";
            // @formatter:on

            execute(tx, UPDATE_WORKFLOW_DEF_QUERY,
                    q -> q.addJsonParameter(def)
                            .addParameter(def.getStatus() != null ? def.getStatus().name() : null)
                            .addParameter(def.getCronExpression()).addParameter(def.getWfVersion())
                            .addParameter(def.getWfName()).executeUpdate());
        }

    }
}
