create table url
(
    id     bigint generated by default as identity primary key,
    value  varchar(255) not null,
    method varchar(255) not null,
    active boolean      not null
);

create table url_stats
(
    id         bigint generated by default as identity primary key,
    created_at timestamp not null,
    available  boolean   not null,
    url_id     bigint    not null references url (id)
);

create index on url_stats (url_id);

create table setting
(
    id    bigint generated by default as identity primary key,
    code  varchar(255)  not null,
    value varchar(1000) not null
);

insert into setting(code, value)
values ('PERIOD', '1'),
       ('AVAILABLE_CODES', '200');