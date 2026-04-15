-- =============================
-- V5: Create transactions table
-- =============================

CREATE TABLE transactions
(
    id          VARCHAR(64) PRIMARY KEY,
    amount      DECIMAL(19, 2) DEFAULT 0                 NOT NULL,
    type        VARCHAR(10)                              NOT NULL,
    description TEXT,
    account_id  VARCHAR(64)                              NOT NULL,
    category_id VARCHAR(64)                              NOT NULL,
    created_at  TIMESTAMP      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by  VARCHAR(64)                              NOT NULL,
    updated_at  TIMESTAMP      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by  VARCHAR(64)                              NOT NULL,
    deleted_at  TIMESTAMP                                NULL,
    deleted_by  VARCHAR(64)                              NULL,
    CONSTRAINT fk_transactions_account FOREIGN KEY (account_id)
        REFERENCES public.accounts (id)
        ON DELETE CASCADE,
    CONSTRAINT fk_transactions_categories FOREIGN KEY (category_id)
        REFERENCES public.categories (id)
);

CREATE INDEX idx_transactions_category_id ON public.transactions USING btree (category_id);
CREATE INDEX idx_transactions_account_id ON public.transactions USING btree (account_id);
CREATE INDEX idx_transactions_type ON public.transactions USING btree (type);
CREATE INDEX idx_transactions_created_at ON public.transactions USING btree (created_at);