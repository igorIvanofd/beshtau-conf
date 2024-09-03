create table if not exists users (
    id bigserial primary key,
    username varchar not null,
    password varchar,
    role varchar
);

insert into users(username, password, role) values ('terra_tech', 'passw0rd1728!', 'ADMIN');