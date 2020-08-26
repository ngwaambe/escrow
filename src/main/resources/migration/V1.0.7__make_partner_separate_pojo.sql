CREATE TEMPORARY TABLE  partner_details_tmp
SELECT * FROM partner_details;

DROP TABLE partner_details;

CREATE TABLE partner_details(
    id                             bigint     auto_increment primary key,
    contact_id                     bigint                    not null,
    logo_id                        bigint                    null,
    small_logo_id                  bigint                    null,
    partner_fee_percentage         int                       not null,
    partner_notification_data_type varchar(4) default 'JSON' not null,
    partner_notification_url       varchar(255)              null,
    constraint FK_small_logo_id foreign key (small_logo_id) references binary_data (id),
    constraint FK_logo_id foreign key (logo_id) references binary_data (id),
    constraint FK_contact_id foreign key (contact_id) references contacts (id),
    UNIQUE(contact_id)
);

insert into partner_details(contact_id, logo_id, small_logo_id, partner_fee_percentage, partner_notification_data_type, partner_notification_url)
select * from partner_details_tmp;

DROP TEMPORARY TABLE partner_details_tmp;

