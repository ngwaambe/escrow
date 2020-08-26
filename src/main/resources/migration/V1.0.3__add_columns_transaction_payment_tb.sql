alter table transaction_payment add column description varchar(255) null;
alter table transaction_payment add payment_code bigint null;
alter table transaction_payment add text_key varchar(255) null;


