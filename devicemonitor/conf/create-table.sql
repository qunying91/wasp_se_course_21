create database devicemonitor;

use devicemonitor;

create table customer (
    id bigint not null,
    name varchar(100) not null,
    primary key (id)
    );

create table device (
    id bigint not null,
    description varchar(200) not null,
    customer_id bigint not null,
    update_at bigint not null,
    status varchar(10),
    primary key (id)
    );

create table device_log (
    id bigint not null,
    device_id bigint not null,
    status varchar(10) not null,
    execution_hours int,
    error varchar(1000),
    update_at bigint not null,
    primary key (id)
    );