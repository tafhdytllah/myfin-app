-- =============================
-- V6: Create export jobs table
-- =============================

CREATE TABLE export_jobs
(
    id          VARCHAR(64) PRIMARY KEY,
    user_id     VARCHAR(64)                         NOT NULL,
    file_name   VARCHAR(255),
    file_path   VARCHAR(500),
    export_type VARCHAR(255),
    status      VARCHAR(20)                         NOT NULL,
    description VARCHAR(500),
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by  VARCHAR(64)                         NOT NULL,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by  VARCHAR(64)                         NOT NULL,
    deleted_at  TIMESTAMP                           NULL,
    deleted_by  VARCHAR(64)                         NULL
);

CREATE INDEX idx_export_jobs_user_id ON export_jobs USING btree (user_id);
CREATE INDEX idx_export_jobs_status ON export_jobs USING btree (status);
CREATE INDEX idx_export_jobs_user_created ON export_jobs USING btree (user_id, created_at DESC);
CREATE INDEX idx_export_jobs_status_created ON export_jobs USING btree (status, created_at);
CREATE INDEX idx_export_jobs_not_deleted ON export_jobs USING btree (deleted_at);