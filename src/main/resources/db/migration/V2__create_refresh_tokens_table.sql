-- =============================
-- V2: Create refresh_tokens table
-- =============================

CREATE TABLE public.refresh_tokens
(
    id         varchar(64)                         NOT NULL,
    user_id    varchar(64)                         NOT NULL,
    token_hash varchar(64)                         NOT NULL,
    expires_at timestamp                           NOT NULL,
    revoked    bool      DEFAULT false             NOT NULL,
    created_at timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by varchar(64)                         NOT NULL,
    updated_at timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by varchar(64)                         NOT NULL,
    deleted_at timestamp NULL,
    deleted_by varchar(64) NULL,
    CONSTRAINT fk_refresh_tokens_user FOREIGN KEY (user_id)
        REFERENCES public.users (id)
        ON DELETE CASCADE
);

-- Indexes
CREATE INDEX ix_refresh_tokens_expires_at ON public.refresh_tokens USING btree (expires_at);
CREATE INDEX ix_refresh_tokens_user_id ON public.refresh_tokens USING btree (user_id);
CREATE UNIQUE INDEX ux_refresh_tokens_token_hash ON public.refresh_tokens USING btree (token_hash);