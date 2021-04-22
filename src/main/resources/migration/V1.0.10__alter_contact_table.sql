ALTER TABLE contacts DROP COLUMN status;
ALTER TABLE contacts DROP COLUMN married;
ALTER TABLE contacts DROP COLUMN allow_newsletter_notification;
ALTER TABLE contacts DROP COLUMN fax;
ALTER TABLE contacts DROP COLUMN website;
alter table contacts modify column email varchar(255) not null;
ALTER TABLE contacts RENAME customer;
DROP TABLE customer_details;


