create table if not exists beshtau_data (
    id bigserial primary key,
    name varchar,
    full_name varchar,
    category varchar,
    os varchar,
    amount decimal
);