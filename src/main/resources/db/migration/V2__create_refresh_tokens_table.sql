-- =============================
-- V2: Create refresh_tokens table
-- =============================

CREATE TABLE public.refresh_tokens
(
    id         VARCHAR(64) PRIMARY KEY,
    user_id    VARCHAR(64)                         NOT NULL,
    token_hash VARCHAR(64)                         NOT NULL,
    expires_at TIMESTAMP                           NOT NULL,
    revoked    BOOLEAN   DEFAULT FALSE             NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by VARCHAR(64)                         NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by VARCHAR(64)                         NOT NULL,
    deleted_at TIMESTAMP                           NULL,
    deleted_by VARCHAR(64)                         NULL,
    CONSTRAINT fk_refresh_tokens_user FOREIGN KEY (user_id)
        REFERENCES public.users (id)
        ON DELETE CASCADE
);

CREATE INDEX idx_refresh_tokens_user_id ON public.refresh_tokens USING btree (user_id);
CREATE UNIQUE INDEX ux_refresh_tokens_token_hashON ON public.refresh_tokens USING btree (token_hash);
CREATE INDEX idx_refresh_tokens_expires_atON ON public.refresh_tokens USING btree (expires_at);