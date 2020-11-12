create sequence hibernate_sequence start with 1 increment by 1;

create table monster_data
(
    monster_id varchar(255) not null,
    primary key (monster_id)

)