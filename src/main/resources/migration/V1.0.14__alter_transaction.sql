drop index idx_transaction_end_date on transaction;
drop index idx_transaction_start_date on transaction;
drop index idx_transaction_lifecycle_status on transaction;
drop index UK_44jtkuk58kmk5m7hobrk9v7ho on transaction;

alter table transaction change column transaction_status status varchar(30);
alter table transaction modify status varchar(30) not null;

alter table transaction change column transaction_lifecycle_status lifecycle_status varchar(255);
alter table transaction modify lifecycle_status varchar(30) not null;

alter table transaction change column transaction_start_date start_date datetime not null;
alter table transaction change column transaction_end_date end_date datetime;

alter table transaction change column transaction_number number_str varchar(255);

create index idx_start_date_uindex on transaction (start_date);
create index idx_end_date on transaction (end_date);
create index idx_lifecycle_status on transaction (lifecycle_status);
create unique index unique_idx_number_str on transaction (number_str);

