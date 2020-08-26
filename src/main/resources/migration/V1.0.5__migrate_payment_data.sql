UPDATE transaction_payment_document tpd, transaction_payment tp
set tpd.payment_number = tp.payment_number , tpd.value_added_tax=IF(tpd.apply_vat=1,19,0)
where tpd.id = tp.payment_document_id and tpd.DTYPE='PaymentRequest' and tp.DTYPE='PaymentRequest';


UPDATE transaction_payment_document tpd, transaction_payment tp
set tpd.payment_number = tp.payment_number , tpd.value_added_tax=IF(tpd.apply_vat=1,19,0)
where tpd.id = tp.payment_document_id and tpd.DTYPE='PaymentNotification' and tp.DTYPE='PaymentNotification';

UPDATE transaction_payment_document tpd, transaction_payment tp
set tp.payment_code=100201, tp.description='Commission Fee', tp.text_key='CommissionInvoice.Of'
where tpd.id = tp.payment_document_id AND tp.DTYPE='Invoice' and tpd.commission_fee = tp.amount;

UPDATE transaction_payment_document tpd, transaction_payment tp
set tp.payment_code=100202,
tp.description  = IF(tpd.payment_type='BANK_TRANSFER','Bank transfer payment fee','Paypal Payment fee'),
tp.text_key     = IF(tpd.payment_type='BANK_TRANSFER','PaymentTransaction.fee.wire','PaymentTransaction.fee.paypal')
where tpd.id = tp.payment_document_id AND tp.DTYPE='Invoice' and tpd.payment_transaction_fee = tp.amount;

UPDATE transaction_payment set payment_code=100200, description='Transaction Price', text_key='transaction.price' where DTYPE='PaymentRequest';
UPDATE transaction_payment set payment_code=100200, description='Transaction Price', text_key='transaction.price' where DTYPE='PaymentNotification';

INSERT INTO transaction_payment_document
(
DTYPE,
created,
last_modified,
apply_payment_fee,
apply_vat,
commission_fee,
paid_amount,
partner_id,
payment_transaction_fee,
payment_type,
status,
transaction_name,
transaction_nr,
other_id,
owner_id,
transaction_id,
issuer_id,
issuer_addres_id,
payment_partner_id,
payment_number,
value_added_tax
)
select
  'InvoiceRequest' as DTYPE,
  tp.created,
  tp.last_modified,
  tpd.apply_payment_fee,
  tpd.apply_vat,
  tpd.commission_fee,
  tpd.paid_amount,
  tpd.partner_id,
  tpd.payment_transaction_fee,
  tpd.payment_type,
  tpd.status,
  tpd.transaction_name,
  tpd.transaction_nr,
  tpd.other_id,
  tpd.owner_id,
  tpd.transaction_id,
  tpd.issuer_id,
  tpd.issuer_addres_id,
  tpd.payment_partner_id,
  tp.payment_number,
  tpd.value_added_tax
  from transaction_payment tp JOIN transaction_payment_document tpd ON (tp.payment_document_id = tpd.id and tp.DTYPE='Invoice');

update transaction_payment_document tpd, transaction_payment tp
  SET
    tp.payment_document_id= tpd.id
  where tpd.payment_number= tp.payment_number and tpd.DTYPE='InvoiceRequest' and tp.DTYPE='Invoice';


INSERT INTO transaction_payment(DTYPE, payment_number, Amount, net_amount, status, payment_document_id, payment_code, description, text_key, created, last_modified)
SELECT
  'PaymentRequest' as DTYPE,
  payment_number,
  commission_fee as Amount,
  commission_fee as net_amount,
  status,
  id as payment_document_id,
  100201 as payment_code,
  'Commission Fee' as description,
  'CommissionInvoice.Of' as textKey,
   created,
   last_modified
FROM transaction_payment_document where DTYPE='PaymentRequest' and commission_fee>0;


INSERT INTO transaction_payment(
  DTYPE,
  payment_number,
  Amount,
  net_amount,
  status,
  payment_document_id,
  payment_code,
  description,
  text_key,
  created,
  last_modified)
SELECT
  'PaymentRequest' as DTYPE,
  payment_number,
  payment_transaction_fee as Amount,
  payment_transaction_fee as net_amount,
  status,
  id as payment_document_id,
  100202 as payment_code,
  IF(payment_type='BANK_TRANSFER','Bank transfer payment fee','Paypal Payment fee'),
  IF(payment_type='BANK_TRANSFER','PaymentTransaction.fee.wire','PaymentTransaction.fee.paypal'),
   created,
   last_modified
FROM transaction_payment_document where DTYPE='PaymentRequest' and payment_transaction_fee>0;


INSERT INTO transaction_payment
(
  DTYPE,
  payment_number,
  Amount,
  net_amount,
  status,
  payment_document_id,
  payment_code,
  description,
  text_key,
  created,
  last_modified)
SELECT
  'PaymentNotification' as DTYPE,
  payment_number,
  commission_fee * -1 as Amount,
  commission_fee * -1 as net_amount,
  status,
  id as payment_document_id,
  100201 as payment_code,
  'Commission Fee' as description,
  'CommissionInvoice.Of' as textKey,
   created,
   last_modified
FROM transaction_payment_document where DTYPE='PaymentNotification' and commission_fee>0;
--delete from transaction_payment where DTYPE='PaymentNotification' and payment_code=100201:
--commit:

INSERT INTO transaction_payment(DTYPE, payment_number, Amount, net_amount, status, payment_document_id, payment_code, description, text_key, created, last_modified)
SELECT
  'PaymentNotification' as DTYPE,
  payment_number,
  payment_transaction_fee * -1 as Amount,
  payment_transaction_fee * -1 as net_amount,
  status,
  id as payment_document_id,
  100202 as payment_code,
  IF(payment_type='BANK_TRANSFER','Bank transfer payment fee','Paypal Payment fee'),
  IF(payment_type='BANK_TRANSFER','PaymentTransaction.fee.wire','PaymentTransaction.fee.paypal'),
   created,
   last_modified
FROM transaction_payment_document where DTYPE='PaymentNotification' and payment_transaction_fee>0;

