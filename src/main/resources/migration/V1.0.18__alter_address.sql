ALTER TABLE address ADD COLUMN phone_number VARCHAR(30) NOT NULL;
UPDATE address a , customer c SET a.phone_number = c.phone_number WHERE a.id = c.address_id;
ALTER TABLE customer DROP COLUMN phone_number;
