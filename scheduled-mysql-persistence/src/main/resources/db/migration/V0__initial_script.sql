CREATE TABLE `schedule_wf_def`
(
    `id`              int         NOT NULL AUTO_INCREMENT,
    `name`            varchar(45) NOT NULL,
    `version`         int         NOT NULL,
    `status`          varchar(45)      DEFAULT NULL,
    `json_input`      longtext    NOT NULL,
    `cron_expression` varchar(45) NOT NULL,
    `created_on`      timestamp   NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_on`      timestamp   NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `unique_name_version` (`name`, `version`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 4
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `manager_info`
(
    `id`          int          NOT NULL AUTO_INCREMENT,
    `nodeAddress` varchar(100) NOT NULL,
    `json_input`  longtext     NOT NULL,
    `created_on`  timestamp    NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_on`  timestamp    NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 4
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `scheduled_wf`
(
    `id`           int          NOT NULL AUTO_INCREMENT,
    `manager_id`   varchar(255) NOT NULL,
    `scheduler_id` varchar(255) NOT NULL,
    `nodeAddress`  varchar(100) NOT NULL,
    `name`         varchar(45)  NOT NULL,
    `json_input`   longtext     NOT NULL,
    `created_on`   timestamp    NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_on`   timestamp    NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 4
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `scheduled_wf_execution`
(
    `id`           int          NOT NULL AUTO_INCREMENT,
    `manager_id`   varchar(255) NOT NULL,
    `scheduler_id` varchar(255) NOT NULL,
    `nodeAddress`  varchar(100) NOT NULL,
    `name`         varchar(45)  NOT NULL,
    `json_input`   longtext     NOT NULL,
    `created_on`   timestamp    NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_on`   timestamp    NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 4
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;