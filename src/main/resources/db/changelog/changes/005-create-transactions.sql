-- Table: public.transactions

-- DROP TABLE IF EXISTS public.transactions;

CREATE TABLE IF NOT EXISTS public.transactions
(
    id SERIAL PRIMARY KEY,
    amount bigint NOT NULL,
    created_at timestamp with time zone NOT NULL,
    description character varying NOT NULL,
    from_card_id SERIAL NOT NULL,
    to_card_id SERIAL NOT NULL
);