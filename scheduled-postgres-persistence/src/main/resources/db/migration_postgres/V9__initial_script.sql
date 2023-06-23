-- This file name must start with V9 to be the following SQL script of V1..V8 from conductor-postgres-persistence
-- Similar to V0__initial_script.sql from scheduled-mysql-persistence

-- Modification in this file needs deleting this script first on postgres
-- e.g. DELETE FROM flyway_schema_history WHERE script = 'V9__initial_script.sql';
-- Modification on tables or indexes needs deleting too
-- e.g. DROP TABLE schedule_wf_def;

CREATE TABLE IF NOT EXISTS schedule_wf_def
(
    id              serial PRIMARY KEY,
    name            varchar(45) NOT NULL,
    version         integer NOT NULL,
    status          varchar(45) DEFAULT NULL,
    json_input      text NOT NULL,
    cron_expression varchar(45) NOT NULL,
    created_on      timestamp DEFAULT CURRENT_TIMESTAMP,
    updated_on      timestamp DEFAULT CURRENT_TIMESTAMP
);

CREATE UNIQUE INDEX IF NOT EXISTS unique_name_version ON schedule_wf_def (name, version);

--ALTER TABLE schedule_wf_def OWNER TO conductor;


CREATE TABLE IF NOT EXISTS manager_info
(
    id          SERIAL PRIMARY KEY,
    nodeAddress VARCHAR(100) NOT NULL,
    json_input  TEXT NOT NULL,
    created_on  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_on  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS nodeAddress_index ON manager_info (nodeAddress);

CREATE TABLE IF NOT EXISTS scheduled_wf
(
    id           SERIAL PRIMARY KEY,
    manager_id   VARCHAR(255) NOT NULL,
    scheduler_id VARCHAR(255) NOT NULL,
    nodeAddress  VARCHAR(100) NOT NULL,
    name         VARCHAR(45)  NOT NULL,
    json_input   TEXT         NOT NULL,
    created_on   TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    updated_on   TIMESTAMP    DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS scheduler_id_index ON scheduled_wf (scheduler_id);
CREATE INDEX IF NOT EXISTS name_manager_id_nodeAddress_index ON scheduled_wf (name, manager_id, nodeAddress);

CREATE TABLE IF NOT EXISTS scheduled_wf_execution
(
    id           SERIAL PRIMARY KEY,
    manager_id   VARCHAR(255) NOT NULL,
    scheduler_id VARCHAR(255) NOT NULL,
    nodeAddress  VARCHAR(100) NOT NULL,
    name         VARCHAR(45)  NOT NULL,
    json_input   TEXT         NOT NULL,
    created_on   TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    updated_on   TIMESTAMP    DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS scheduler_id_index ON scheduled_wf_execution (scheduler_id);
CREATE INDEX IF NOT EXISTS name_manager_id_nodeAddress_index ON scheduled_wf_execution (name, manager_id, nodeAddress);
