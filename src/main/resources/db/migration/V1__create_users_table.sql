-- =============================
-- V1: Create users table
-- =============================

CREATE TABLE public.users
(
    id            varchar(64)                         NOT NULL,
    username      varchar(50)                         NOT NULL,
    email         varchar(100)                        NOT NULL,
    password_hash varchar(255)                        NOT NULL,
    "role"        varchar(20)                         NOT NULL,
    is_active     bool      DEFAULT true              NOT NULL,
    created_at    timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by    varchar(64)                         NOT NULL,
    updated_at    timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by    varchar(64)                         NOT NULL,
    deleted_at    timestamp NULL,
    deleted_by    varchar(64) NULL,
    CONSTRAINT users_pkey PRIMARY KEY (id),
    CONSTRAINT users_username_key UNIQUE (username),
    CONSTRAINT users_email_key UNIQUE (email)
);

-- Indexes
CREATE INDEX idx_users_role ON public.users USING btree (role);
CREATE UNIQUE INDEX ux_users_email ON public.users USING btree (email);
CREATE UNIQUE INDEX ux_users_username ON public.users USING btree (username);