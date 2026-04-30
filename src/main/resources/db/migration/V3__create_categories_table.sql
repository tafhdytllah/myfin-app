-- =============================
-- V3: Create categories table
-- =============================

CREATE TABLE categories
(
    id         VARCHAR(64) PRIMARY KEY,
    name       VARCHAR(100)                        NOT NULL,
    type       VARCHAR(20)                         NOT NULL,
    is_active  BOOLEAN   DEFAULT TRUE              NOT NULL,
    user_id    VARCHAR(64)                         NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by VARCHAR(64)                         NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by VARCHAR(64)                         NOT NULL,
    deleted_at TIMESTAMP                           NULL,
    deleted_by VARCHAR(64)                         NULL,
    constraint fk_categories_user FOREIGN KEY (user_id)
        REFERENCES public.users (id)
        ON DELETE CASCADE
);

CREATE INDEX idx_categories_user_id ON public.categories USING btree (user_id);
CREATE INDEX idx_categories_type ON public.categories USING btree (type);