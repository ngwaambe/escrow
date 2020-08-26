alter table transaction_payment_document
	add payment_number varchar(255) null;

alter table transaction_payment_document
	add value_added_tax decimal(19,2) null;

alter table transaction_payment_document
	drop column customer_id;
