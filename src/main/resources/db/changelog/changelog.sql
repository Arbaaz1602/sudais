-- liquibase formatted sql

-- changeset liquibase:1
create schema if not exists identity_access_management;

create table if not exists identity_access_management.alsudais_user_master
(
    aum_seq_id bigserial not null,
    aum_user_id character varying(50) not null,
    aum_keycloak_id character varying(50) not null,
    aum_email_id character varying(100) not null,
    aum_first_name character varying(150) not null,
    aum_middle_name character varying(150),
    aum_last_name character varying(150),
    aum_gender character varying(6) not null,
    aum_phone_number character varying(15),
    aum_status character varying(10),
    aum_profile_photo text,
    aum_country_id character varying(50),
    aum_state_id character varying(50),
    aum_city_id character varying(50),
    aum_postal_code_id character varying(50),
    aum_nationality_id character varying(50),
    aum_allocated_location_id character varying(50),
    aum_key_responsibility_area_id character varying(80),
    aum_date_of_birth date,
    aum_address text,
    aum_created_by character varying(50),
    aum_created_date timestamp without time zone,
    aum_modified_by character varying(50),
    aum_modified_date timestamp without time zone,
    constraint aum_seq_id_pk primary key(aum_seq_id)
);

-- changeset liquibase:2

create table if not exists identity_access_management.alsudais_module_detail
(
    amd_seq_id bigserial not null,
    amd_module_id character varying(50) not null,
    amd_module_name character varying(150) not null,
    amd_parent_module_id character varying(100),
    amd_sort_order integer,
    amd_status character varying(10),
    amd_created_by character varying(50),
    amd_created_date timestamp without time zone,
    amd_modified_by character varying(50),
    amd_modified_date timestamp without time zone,
    constraint amd_seq_id_pk primary key(amd_seq_id)
);


create table if not exists identity_access_management.alsudais_role_detail
(
    ard_seq_id bigserial not null,
    ard_role_id character varying(50) not null,
    ard_role_name character varying(150) not null,
    ard_platform_name character varying(100) not null,
    ard_status character varying(10),
    ard_created_by character varying(50),
    ard_created_date timestamp without time zone,
    ard_modified_by character varying(50),
    ard_modified_date timestamp without time zone,
    constraint ard_seq_id_pk primary key(ard_seq_id)
);


create table if not exists identity_access_management.alsudais_user_role_mapping
(
    aurm_seq_id bigserial not null,
    aurm_user_id character varying(50) not null,
    aurm_role_id character varying(50) not null,
    aurm_status character varying(10),
    aurm_created_by character varying(50),
    aurm_created_date timestamp without time zone,
    aurm_modified_by character varying(50),
    aurm_modified_date timestamp without time zone,
    constraint aurm_seq_id_pk primary key(aurm_seq_id)
);


create table if not exists identity_access_management.alsudais_user_role_module_mapping
(
    aurmm_seq_id bigserial not null,
    aurmm_user_id character varying(50),
    aurmm_role_id character varying(50),
    aurmm_module_id character varying(50),
    aurmm_parent_module_id character varying(50),
    aurmm_full_control character varying(10),
    aurmm_modify character varying(10),
    aurmm_read_and_execute character varying(10),
    aurmm_approver character varying(10),
    aurmm_read character varying(10),
    aurmm_write character varying(10),
    aurmm_no_access character varying(10),
    aurmm_remark text,
    aurmm_status character varying(15),
    aurmm_created_by character varying(50),
    aurmm_created_date timestamp without time zone,
    aurmm_modified_by character varying(50),
    aurmm_modified_date timestamp without time zone,
    constraint aurmm_seq_id_pk primary key(aurmm_seq_id)
);


-- changeset liquibase:3

create table if not exists identity_access_management.alsudais_platform_detail (
    apd_seq_id bigserial not null,
    apd_platform_id character varying(60) not null,
    apd_platform_name character varying(60),
    apd_status character varying(15),
    apd_created_by character varying(50),
    apd_created_date timestamp without time zone,
    apd_modified_by character varying(50),
    apd_modified_date timestamp without time zone,
    constraint apd_seq_id_pk primary key(apd_seq_id)
);


create table if not exists identity_access_management.alsudais_platform_module_mapping (
    apmm_seq_id bigserial not null,
    apmm_platform_id character varying(60) not null,
    apmm_module_id character varying(60) not null,
    apmm_status character varying(15),
    apmm_created_by character varying(50),
    apmm_created_date timestamp without time zone,
    apmm_modified_by character varying(50),
    apmm_modified_date timestamp without time zone,
    constraint apmm_seq_id_pk primary key(apmm_seq_id)
);

-- changeset liquibase:4

create table if not exists identity_access_management.alsudais_user_online_status_detail (
    auad_seq_id bigserial not null,
    auad_user_id character varying(60),
    auad_status character varying(15),
    auad_created_by character varying(50),
    auad_created_date timestamp without time zone,
    auad_modified_by character varying(50),
    auad_modified_date timestamp without time zone,
    constraint auad_seq_id_pk primary key(auad_seq_id)
);

-- changeset liquibase:5

create sequence if not exists public.revinfo_seq
    increment 1
    start 1
    minvalue 1;

create table if not exists identity_access_management.alsudais_user_master_aud
(
    aum_seq_id bigserial not null,
    rev integer not null,
    revtype smallint,
    aum_user_id character varying(50) not null,
    aum_keycloak_id character varying(50) not null,
    aum_email_id character varying(100) not null,
    aum_first_name character varying(150) not null,
    aum_middle_name character varying(150),
    aum_last_name character varying(150),
    aum_gender character varying(6) not null,
    aum_phone_number character varying(15),
    aum_status character varying(10),
    aum_profile_photo text,
    aum_country_id character varying(50),
    aum_state_id character varying(50),
    aum_city_id character varying(50),
    aum_postal_code_id character varying(50),
    aum_nationality_id character varying(50),
    aum_allocated_location_id character varying(50),
    aum_key_responsibility_area_id character varying(80),
    aum_date_of_birth date,
    aum_address text,
    aum_created_by character varying(50),
    aum_created_date timestamp without time zone,
    aum_modified_by character varying(50),
    aum_modified_date timestamp without time zone,
    constraint aum_seq_id_aud_pk primary key(rev, aum_seq_id)
);

create table if not exists identity_access_management.alsudais_module_detail_aud
(
    amd_seq_id bigserial not null,
    rev integer not null,
    revtype smallint,
    amd_module_id character varying(50) not null,
    amd_module_name character varying(150) not null,
    amd_parent_module_id character varying(100),
    amd_sort_order integer,
    amd_status character varying(10),
    amd_created_by character varying(50),
    amd_created_date timestamp without time zone,
    amd_modified_by character varying(50),
    amd_modified_date timestamp without time zone,
    constraint amd_seq_id_aud_pk primary key(rev, amd_seq_id)
);

create table if not exists identity_access_management.alsudais_role_detail_aud
(
    ard_seq_id bigserial not null,
    rev integer not null,
    revtype smallint,
    ard_role_id character varying(50) not null,
    ard_role_name character varying(150) not null,
    ard_platform_name character varying(100) not null,
    ard_status character varying(10),
    ard_created_by character varying(50),
    ard_created_date timestamp without time zone,
    ard_modified_by character varying(50),
    ard_modified_date timestamp without time zone,
    constraint ard_seq_id_aud_pk primary key(rev, ard_seq_id)
);

create table if not exists identity_access_management.alsudais_user_role_mapping_aud
(
    aurm_seq_id bigserial not null,
    rev integer not null,
    revtype smallint,
    aurm_user_id character varying(50) not null,
    aurm_role_id character varying(50) not null,
    aurm_status character varying(10),
    aurm_created_by character varying(50),
    aurm_created_date timestamp without time zone,
    aurm_modified_by character varying(50),
    aurm_modified_date timestamp without time zone,
    constraint aurm_seq_id_aud_pk primary key(rev, aurm_seq_id)
);

create table if not exists identity_access_management.alsudais_user_role_module_mapping_aud
(
    aurmm_seq_id bigserial not null,
    rev integer not null,
    revtype smallint,
    aurmm_user_id character varying(50),
    aurmm_role_id character varying(50),
    aurmm_module_id character varying(50),
    aurmm_parent_module_id character varying(50),
    aurmm_full_control character varying(10),
    aurmm_modify character varying(10),
    aurmm_read_and_execute character varying(10),
    aurmm_approver character varying(10),
    aurmm_read character varying(10),
    aurmm_write character varying(10),
    aurmm_no_access character varying(10),
    aurmm_remark text,
    aurmm_status character varying(15),
    aurmm_created_by character varying(50),
    aurmm_created_date timestamp without time zone,
    aurmm_modified_by character varying(50),
    aurmm_modified_date timestamp without time zone,
    constraint aurmm_seq_id_aud_pk primary key(rev, aurmm_seq_id)
);

create table if not exists identity_access_management.alsudais_platform_detail_aud (
    apd_seq_id bigserial not null,
    rev integer not null,
    revtype smallint,
    apd_platform_id character varying(60) not null,
    apd_platform_name character varying(60),
    apd_status character varying(15),
    apd_created_by character varying(50),
    apd_created_date timestamp without time zone,
    apd_modified_by character varying(50),
    apd_modified_date timestamp without time zone,
    constraint apd_seq_id_aud_pk primary key(rev, apd_seq_id)
);

create table if not exists identity_access_management.alsudais_platform_module_mapping_aud (
    apmm_seq_id bigserial not null,
    rev integer not null,
    revtype smallint,
    apmm_platform_id character varying(60) not null,
    apmm_module_id character varying(60) not null,
    apmm_status character varying(15),
    apmm_created_by character varying(50),
    apmm_created_date timestamp without time zone,
    apmm_modified_by character varying(50),
    apmm_modified_date timestamp without time zone,
    constraint apmm_seq_id_aud_pk primary key(rev, apmm_seq_id)
);

-- changeset liquibase:6

create table if not exists identity_access_management.alsudais_user_master (
    aucs_seq_id bigserial not null,
    aucs_identifier character varying(10) not null,
    aucs_counter bigint,
    constraint aucs_seq_id_pk primary key(aucs_seq_id)
);

-- changeset liquibase:7

alter table if exists identity_access_management.alsudais_user_master
add column if not exists aum_user_code character varying(10);

alter table if exists identity_access_management.alsudais_user_master_aud
add column if not exists aum_user_code character varying(10);

--changeset liquibase:8

create table if not exists identity_access_management.alsudais_user_online_status_detail_aud (
    auad_seq_id bigserial not null,
    rev integer not null,
    revtype smallint,
    auad_user_id character varying(60),
    auad_status character varying(15),
    auad_created_by character varying(50),
    auad_created_date timestamp without time zone,
    auad_modified_by character varying(50),
    auad_modified_date timestamp without time zone,
    constraint auad_seq_id_aud_pk primary key(rev, auad_seq_id)
);