ALTER TABLE  transaction change COLUMN DTYPE  type varchar(31);

update  transaction_participant set DTYPE="BROKER" where DTYPE="ParticipantBroker";
update  transaction_participant set DTYPE="SELLER" where DTYPE="ParticipantSeller";
update  transaction_participant set DTYPE="BUYER" where DTYPE="ParticipantBuyer";
update transaction_participant set DTYPE="SICURO" where DTYPE="ParticipantSicuro";
alter table transaction_participant change column DTYPE role varchar(6);



