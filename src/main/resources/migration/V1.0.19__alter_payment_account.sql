ALTER TABLE bank_account DROP COLUMN account_number;
ALTER TABLE bank_account DROP COLUMN routing_number;
ALTER TABLE contact_payment_account RENAME COLUMN contact_id TO customer_id;
ALTER TABLE contact_payment_account RENAME COLUMN defaultAccount TO default_account;
ALTER TABLE contact_payment_account RENAME customer_payment_account;



