-- =============================
-- V1: Create users table
-- =============================

CREATE TABLE public.users
(
    id            VARCHAR(64) PRIMARY KEY,
    username      VARCHAR(50)                         NOT NULL,
    email         VARCHAR(100)                        NOT NULL,
    password_hash VARCHAR(255)                        NOT NULL,
    role          VARCHAR(20)                         NOT NULL,
    is_active     BOOLEAN   DEFAULT true              NOT NULL,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by    VARCHAR(64)                         NOT NULL,
    updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by    VARCHAR(64)                         NOT NULL,
    deleted_at    TIMESTAMP                           NULL,
    deleted_by    VARCHAR(64)                         NULL
);

CREATE UNIQUE INDEX ux_users_username ON public.users USING btree (username);
CREATE UNIQUE INDEX ux_users_email ON public.users USING btree (email);
CREATE INDEX idx_users_created_at ON public.users USING btree (created_at DESC);