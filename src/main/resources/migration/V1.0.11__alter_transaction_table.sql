drop index idx_transaction_price on transaction;
alter table transaction change column transaction_price price decimal(19, 2)  not null;



