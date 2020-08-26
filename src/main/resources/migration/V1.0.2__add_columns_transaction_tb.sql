alter table transaction add column broker_comission_cost  decimal(19, 2);
alter table transaction add column seller_broker_comission_cost int  default 0;
alter table transaction add column seller_comfirms_transaction BOOL default FALSE;
alter table transaction add column buyer_comfirms_transaction  BOOL default FALSE;


