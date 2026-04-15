-- =============================
-- V6: Create dashboard summary table
-- =============================

CREATE TABLE dashboard_summary
(
    id            VARCHAR(64) PRIMARY KEY,
    total_income  DECIMAL(19, 2) DEFAULT 0                 NOT NULL,
    total_expense DECIMAL(19, 2) DEFAULT 0                 NOT NULL,
    balance       DECIMAL(19, 2) DEFAULT 0                 NOT NULL,
    period        VARCHAR(7)                               NOT NULL,
    user_id       VARCHAR(64)                              NOT NULL,
    account_id    VARCHAR(64),
    created_at    TIMESTAMP      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by    VARCHAR(64)                              NOT NULL,
    updated_at    TIMESTAMP      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by    VARCHAR(64)                              NOT NULL,
    deleted_at    TIMESTAMP                                NULL,
    deleted_by    VARCHAR(64)                              NULL
);

CREATE INDEX idx_dashboard_summary_user_period ON public.dashboard_summary USING btree (user_id, period);
CREATE INDEX idx_dashboard_summary_account_period ON public.dashboard_summary USING btree (account_id, period);
CREATE UNIQUE INDEX idx_dashboard_summary_user_account_period ON public.dashboard_summary USING btree(user_id, account_id, period);