-- =============================
-- V3: Create accounts table
-- =============================

CREATE TABLE public.accounts
(
    id         VARCHAR(64) PRIMARY KEY,
    name       VARCHAR(100)                             NOT NULL,
    balance    DECIMAL(19, 2) DEFAULT 0                 NOT NULL,
    user_id    VARCHAR(64)                              NOT NULL,
    created_at TIMESTAMP      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by VARCHAR(64)                              NOT NULL,
    updated_at TIMESTAMP      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by VARCHAR(64)                              NOT NULL,
    deleted_at TIMESTAMP NULL,
    deleted_by VARCHAR(64) NULL,
    CONSTRAINT fk_accounts_user FOREIGN KEY (user_id)
        REFERENCES public.users (id)
        ON DELETE CASCADE
);

CREATE INDEX idx_accounts_user_id ON public.accounts USING btree (user_id);
CREATE INDEX idx_accounts_user_id_name ON public.accounts USING btree (user_id, name);