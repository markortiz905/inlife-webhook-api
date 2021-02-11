create table json_webhook (
   id bigint not null,
    date_opened date,
    expected_time_to_onboard varchar(255),
    full_name varchar(255),
    json_object json,
    nature_of_disability varchar(255),
    primary_inlife_contact varchar(255),
    status varchar(255),
    suburb varchar(255),
    your_column_name LONGTEXT,
    primary key (id)
)