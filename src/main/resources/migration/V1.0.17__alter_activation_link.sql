ALTER TABLE transaction_startlink DROP FOREIGN KEY FK_i223ib7wmh4l013r7nnuowsoo;
DROP INDEX UK_ecv9p2m8fod4jio8981gr83jd on transaction_startlink;
DROP INDEX FK_i223ib7wmh4l013r7nnuowsoo on transaction_startlink;
TRUNCATE TABLE transaction_startlink;
ALTER TABLE transaction_startlink change column link_id  link_uuid varchar(255) not null;

ALTER TABLE activation_link DROP COLUMN dtype;
ALTER TABLE activation_link MODIFY id BIGINT NOT NULL;
ALTER TABLE activation_link DROP PRIMARY KEY;
ALTER TABLE activation_link DROP COLUMN id;
ALTER TABLE activation_link DROP FOREIGN KEY FK_2ctq8fwyp2j92v3bo3lmql0n3;
DROP INDEX FK_2ctq8fwyp2j92v3bo3lmql0n3 on activation_link;
ALTER TABLE activation_link ADD PRIMARY KEY(uuid);
ALTER TABLE activation_link ADD CONSTRAINT fk_customer_id FOREIGN KEY (customer_id) REFERENCES customer(id);

ALTER TABLE transaction_startlink ADD CONSTRAINT fk_link_id FOREIGN KEY (link_uuid) REFERENCES activation_link(uuid);

DROP TABLE password_state_info;
