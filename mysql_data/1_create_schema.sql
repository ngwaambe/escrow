create schema if not exists sicuro collate latin1_swedish_ci;

create table if not exists binary_data
(
	id bigint auto_increment
		primary key,
	created datetime not null,
	last_modified datetime not null,
	binary_contnet longblob null,
	byte_size bigint null,
	file_name varchar(255) null,
	mime_type varchar(255) null
);

create table if not exists comment
(
	id bigint auto_increment
		primary key,
	created datetime not null,
	last_modified datetime not null,
	autor varchar(255) not null,
	message longtext not null,
	objectId varchar(255) not null,
	type varchar(255) not null
);

create index idx_objectId_typename
	on comment (objectId, type);

create index idx_objectId_typename_autor
	on comment (objectId, type, autor);

create table if not exists configuration_properties
(
	id bigint auto_increment
		primary key,
	created datetime not null,
	last_modified datetime not null,
	description varchar(255) null,
	group_name varchar(255) not null,
	name varchar(255) not null,
	value varchar(255) not null
);

create index idx_description
	on configuration_properties (description);

create index idx_group_name
	on configuration_properties (group_name);

create index idx_name
	on configuration_properties (name);

create table if not exists country
(
	id bigint auto_increment
		primary key,
	created datetime not null,
	last_modified datetime not null,
	currency varchar(255) null,
	dialing_country_code varchar(255) null,
	international_prefix varchar(255) null,
	iso varchar(255) not null,
	language varchar(255) null,
	name varchar(255) not null,
	european_union_member tinyint(1) default 0 not null,
	paypal_fee int(10) default 6 not null,
	sepa tinyint(1) default 0 not null,
	constraint UK_2cfxo3eh7tywg545x36fugy7i
		unique (iso),
	constraint UK_llidyp77h6xkeokpbmoy710d4
		unique (name)
);

create table if not exists address
(
	id bigint auto_increment
		primary key,
	created datetime not null,
	last_modified datetime not null,
	city varchar(255) null,
	country_iso varchar(255) null,
	house_number varchar(255) null,
	postal_code varchar(255) null,
	region varchar(255) null,
	street varchar(255) null,
	street_extension varchar(255) null,
	country_id bigint null,
	constraint FK_nyyg5dlcs74rm1girctd3mubi
		foreign key (country_id) references country (id)
);

create table if not exists freqeuntly_asked_questions
(
	id bigint auto_increment
		primary key,
	created datetime not null,
	last_modified datetime not null,
	answer varchar(2024) not null,
	category varchar(255) null,
	language varchar(255) null,
	question varchar(2024) not null,
	tags varchar(255) null
);

create index idx_category
	on freqeuntly_asked_questions (category);

create index idx_language
	on freqeuntly_asked_questions (language);


create index idx_tags
	on freqeuntly_asked_questions (tags);

create table if not exists invoice_number
(
	id bigint auto_increment
		primary key,
	created datetime not null,
	last_modified datetime not null,
	counter decimal(19,2) not null,
	number varchar(255) not null,
	constraint UK_1m0b2tm6qr4d2kc3bf0rbyn7x
		unique (number),
	constraint UK_3acby3r445i9586nv3nk1414s
		unique (counter)
);

create table if not exists language
(
	id bigint auto_increment
		primary key,
	created datetime not null,
	last_modified datetime not null,
	iso varchar(255) not null,
	name varchar(255) not null,
	system_supported_Language tinyint(1) not null,
	constraint UK_g8hr207ijpxlwu10pewyo65gv
		unique (name),
	constraint UK_o83xgpu4ac8l67eox06kg7qcw
		unique (iso)
);

create table if not exists notification_number
(
	id bigint auto_increment
		primary key,
	created datetime not null,
	last_modified datetime not null,
	counter decimal(19,2) not null,
	number varchar(255) not null,
	constraint UK_458jyjpiku9xmcws52s56oqbm
		unique (number),
	constraint UK_jffwm8cb6loaishv4on253sj5
		unique (counter)
);

create table if not exists payment_account
(
	id bigint auto_increment
		primary key,
	created datetime not null,
	last_modified datetime not null,
	owner varchar(255) not null,
	type varchar(255) not null
);

create table if not exists bank_account
(
	account_number varchar(255) null,
	bank_name varchar(255) not null,
	city varchar(255) not null,
	country_iso varchar(255) not null,
	iban varchar(255) not null,
	postal_code varchar(255) not null,
	routing_number varchar(255) null,
	swiftBic varchar(255) not null,
	id bigint not null
		primary key,
	constraint FK_asjgaeteungo7k3ub3pde5gbm
		foreign key (id) references payment_account (id)
);

create table if not exists paypal_account
(
	paypal_account varchar(255) not null,
	id bigint not null
		primary key,
	constraint FK_qphybw2ts4nu4rks34eho5xom
		foreign key (id) references payment_account (id)
);

create table if not exists contacts
(
	id bigint auto_increment
		primary key,
	created datetime not null,
	last_modified datetime not null,
	email varchar(255) not null,
	fax varchar(255) null,
	phone_number varchar(255) null,
	tax_number varchar(255) null,
	website varchar(255) null,
	organisation varchar(255) null,
	birth_day date null,
	email2 varchar(255) null,
	first_name varchar(255) not null,
	gender varchar(255) null,
	last_name varchar(255) not null,
	married tinyint(1) null,
	mobile_number varchar(255) null,
	status varchar(255) not null,
	title varchar(255) null,
	address_id bigint null,
	bank_id bigint null,
	paypal_id bigint null,
	created_by varchar(255) null,
	last_modified_by varchar(255) null,
	allow_newsletter_notification tinyint(1) not null,
	apply_vat tinyint(1) not null,
	dtype varchar(255) not null,
	id_number varchar(255) not null,
	prefered_language_iso varchar(255) not null,
	partner_id varchar(255) null,
	id_card_number varchar(255) null,
	constraint UK_728mksvqr0n907kujew6p3jc0
		unique (email),
	constraint FK_ms607pmyhg52aneyg4lrh0k1f
		foreign key (paypal_id) references paypal_account (id),
	constraint FK_n212qvf6geg7an12wpnlnekyk
		foreign key (address_id) references address (id)
);

create table if not exists activation_link
(
	DTYPE varchar(31) not null,
	id bigint auto_increment
		primary key,
	created datetime not null,
	last_modified datetime not null,
	uuid varchar(255) null,
	customer_id bigint null,
	active tinyint(1) not null,
	constraint FK_2ctq8fwyp2j92v3bo3lmql0n3
		foreign key (customer_id) references contacts (id)
);

create table if not exists contact_payment_account
(
	id bigint auto_increment
		primary key,
	created datetime not null,
	last_modified datetime not null,
	defaultAccount tinyint(1) null,
	payment_account_id bigint null,
	contact_id bigint null,
	constraint UK_neow30u1amqinadeuw1irpgpe
		unique (contact_id, payment_account_id),
	constraint FK_eypvqbrk1narrdyleqjhdwucw
		foreign key (contact_id) references contacts (id),
	constraint FK_mitdt0w99u8exvn2sqxcqybei
		foreign key (payment_account_id) references payment_account (id)
);

create index FK_8j9hg26lspijvnkox0sxgg4kt
	on contacts (bank_id);

create table if not exists customer_details
(
	apply_vat tinyint(1) null,
	customer_number varchar(255) not null,
	prefered_language_iso varchar(255) null,
	id bigint not null
		primary key,
	prefered_language bigint null,
	allow_newsletter_nottification tinyint(1) not null,
	constraint FK_7n1n6m5u4og0u4b64an74yh79
		foreign key (id) references contacts (id),
	constraint FK_b78tb93ct62rb2vl85qqr90y4
		foreign key (prefered_language) references language (id)
);

create table if not exists employee_details
(
	abbreviation varchar(255) null,
	department varchar(255) null,
	entryDate date null,
	position varchar(255) null,
	quittingDate date null,
	id bigint not null
		primary key,
	constraint UK_9ijdtybjyjmtljekxjnt0dfmv
		unique (abbreviation),
	constraint FK_ijwcc3ema6gaig8g96autph4j
		foreign key (id) references contacts (id)
);

create table if not exists employee_customers
(
	employee_id bigint not null,
	customer_id bigint not null,
	constraint idx_employeeId_customerId
		unique (employee_id, customer_id),
	constraint FK_25xowpycw7qkgp7mb5tdmyj1i
		foreign key (employee_id) references employee_details (id),
	constraint FK_3him9aq5d3go6e1wk3i8fpdj1
		foreign key (customer_id) references customer_details (id)
);

create table if not exists organisation
(
	id bigint auto_increment
		primary key,
	created datetime not null,
	last_modified datetime not null,
	email varchar(255) not null,
	fax varchar(255) null,
	phone_number varchar(255) null,
	tax_number varchar(255) null,
	website varchar(255) null,
	ustIdNr varchar(255) null,
	hrb varchar(255) null,
	name varchar(255) not null,
	address_id bigint null,
	bank_id bigint null,
	paypal_id bigint null,
	created_by varchar(255) null,
	last_modified_by varchar(255) null,
	constraint UK_4cj3idr72jukvc49m5dgo9jmo
		unique (name),
	constraint UK_cfq807n4gn0sdif3ddj4enb05
		unique (email),
	constraint FK_hg50ucgllihvhptsu9dnvxauv
		foreign key (address_id) references address (id),
	constraint FK_jbcc8rsx3g1orb70pvr1bw97o
		foreign key (paypal_id) references paypal_account (id)
);

create index FK_j8sc6lrb37uatja0wu6ilfj6r
	on organisation (bank_id);

create table if not exists partner_details
(
	id bigint not null
		primary key,
	logo_id bigint null,
	small_logo_id bigint null,
	partner_fee_percentage int not null,
	partner_notification_data_type varchar(4) default 'JSON' not null,
	partner_notification_url varchar(255) null,
	constraint FK_2rjfmbd841rjgk3d0459rrle0
		foreign key (small_logo_id) references binary_data (id),
	constraint FK_6sihhyebd41r5sms9ltmu46tj
		foreign key (logo_id) references binary_data (id),
	constraint FK_p1ob7721yt08333ylydtx66hw
		foreign key (id) references contacts (id)
);

create table if not exists paypal_payment_info
(
	id bigint auto_increment
		primary key,
	created datetime not null,
	last_modified datetime not null,
	document_id bigint null,
	payerId varchar(255) null,
	payment_date varchar(255) null,
	payment_status varchar(255) null,
	token varchar(255) null
);

create table if not exists request_number
(
	id bigint auto_increment
		primary key,
	created datetime not null,
	last_modified datetime not null,
	counter decimal(19,2) not null,
	number varchar(255) not null,
	constraint UK_5jtmr50g140cvt1l1caff0wb7
		unique (counter),
	constraint UK_k7ow57p1sm6g40eh76csf0lmc
		unique (number)
);

create table if not exists roles
(
	id bigint auto_increment
		primary key,
	created datetime not null,
	last_modified datetime not null,
	description varchar(255) not null,
	name varchar(80) not null,
	constraint UK_ofx66keruapi6vyqpv6f2or37
		unique (name)
);

create index idx_description
	on roles (description);

create index idx_name
	on roles (name);

create table if not exists transaction
(
	DTYPE varchar(31) not null,
	id bigint auto_increment
		primary key,
	created datetime not null,
	last_modified datetime not null,
	additional_cost decimal(19,2) null,
	agreement_date datetime null,
	broker_email varchar(255) null,
	buyer_email varchar(255) not null,
	comission_cost decimal(19,2) null,
	delivery_cost decimal(19,2) null,
	transaction_end_date datetime null,
	inspection_period int null,
	transaction_lifecycle_status varchar(255) null,
	name varchar(255) null,
	seller_commission_percent int null,
	seller_email varchar(255) not null,
	sicuro_email varchar(255) null,
	transaction_start_date datetime not null,
	transaction_status varchar(255) null,
	transaction_number varchar(255) null,
	transaction_price decimal(19,2) not null,
	shipment_paid_by_buyer tinyint(1) null,
	shipment_price decimal(19,2) null,
	shipment_type varchar(255) null,
	current_event_id bigint null,
	partner_number varchar(255) null,
	created_by varchar(255) null,
	last_modified_by varchar(255) null,
	interface_type varchar(8) default 'WEB' not null,
	constraint UK_44jtkuk58kmk5m7hobrk9v7ho
		unique (transaction_number)
);

create table if not exists employee_transactions
(
	employee_id bigint not null,
	transaction_id bigint not null,
	constraint idx_employeeId_transactionId
		unique (employee_id, transaction_id),
	constraint FK_c4e1j82ns0xqspgmssv11q1wp
		foreign key (transaction_id) references transaction (id),
	constraint FK_q9ce1jb2ph1ra8qq6o53cf8qj
		foreign key (employee_id) references employee_details (id)
);

create index idx_agreement_date
	on transaction (agreement_date);

create index idx_broker_email
	on transaction (broker_email);

create index idx_buyer_email
	on transaction (buyer_email);

create index idx_comission_cost
	on transaction (comission_cost);

create index idx_created
	on transaction (created);

create index idx_last_modified
	on transaction (last_modified);

create index idx_name
	on transaction (name);

create index idx_seller_email
	on transaction (seller_email);

create index idx_sicuro_email
	on transaction (sicuro_email);

create index idx_transaction_end_date
	on transaction (transaction_end_date);

create index idx_transaction_lifecycle_status
	on transaction (transaction_lifecycle_status);

create index idx_transaction_price
	on transaction (transaction_price);

create index idx_transaction_start_date
	on transaction (transaction_start_date);

create table if not exists transaction_event
(
	id bigint auto_increment
		primary key,
	created datetime not null,
	last_modified datetime not null,
	message mediumtext null,
	notificationCount int default 0 not null,
	event_type varchar(255) null,
	trigger_role varchar(255) null,
	transaction_id bigint not null,
	fail_notification_count int default 0 not null,
	notification_mode varchar(80) default 'ALL' null,
	api_fail_notification_count int default 0 not null,
	api_notification_count int default 0 not null,
	interface_type varchar(8) default 'WEB' not null,
	constraint UK_fbvv3vjhvy4k2tonyu04g8qm4
		unique (transaction_id, event_type),
	constraint FK_kinkebwju8hjiq2gkfnmowpip
		foreign key (transaction_id) references transaction (id)
);

alter table transaction
	add constraint FK_c983fisqa4m4wyj8qj19wmgyp
		foreign key (current_event_id) references transaction_event (id);

create table if not exists transaction_event_message
(
	id bigint auto_increment
		primary key,
	created datetime not null,
	last_modified datetime not null,
	current_user_role varchar(15) not null,
	language_iso varchar(2) not null,
	message longtext null,
	message_key varchar(200) not null,
	object_type varchar(15) null,
	special_case varchar(80) null,
	status varchar(100) not null,
	trigger_role varchar(15) null,
	constraint default_unique_message
		unique (trigger_role, status, current_user_role, special_case, language_iso),
	constraint object_type_unique_message
		unique (object_type, trigger_role, status, current_user_role, special_case, language_iso),
	constraint unique_message_key
		unique (message_key, language_iso)
);

create table if not exists transaction_object
(
	id bigint auto_increment
		primary key,
	created datetime not null,
	last_modified datetime not null,
	description varchar(255) null,
	name varchar(255) null,
	price decimal(19,3) null,
	transaction_id bigint not null,
	constraint FK_sp7by4dhs9tgfmwmwwn1in8qf
		foreign key (transaction_id) references transaction (id)
);

create table if not exists transaction_object_domain
(
	seller_domain_outcode varchar(255) null,
	system_domain_outcode varchar(255) null,
	term_length_months int null,
	type varchar(255) null,
	with_content tinyint(1) null,
	id bigint not null
		primary key,
	binary_data_id bigint null,
	constraint FK_e77ah4iut9x5vl2tuje7m1o9q
		foreign key (id) references transaction_object (id),
	constraint FK_jo0bw4lv2havh8tluc8t0mwhb
		foreign key (binary_data_id) references binary_data (id)
);

create table if not exists transaction_object_merchandise
(
	quantity bigint null,
	unit_price decimal(19,2) null,
	id bigint not null
		primary key,
	constraint FK_ddtaao5urt1iig932kqxobbwu
		foreign key (id) references transaction_object (id)
);

create table if not exists transaction_object_service
(
	quantity bigint null,
	unit_price decimal(19,2) null,
	id bigint not null
		primary key,
	constraint FK_ee259qiajvjwuuwimtma2mb95
		foreign key (id) references transaction_object (id)
);

create table if not exists transaction_participant
(
	DTYPE varchar(31) not null,
	id bigint auto_increment
		primary key,
	created datetime not null,
	last_modified datetime not null,
	customer_email varchar(255) null,
	initiator tinyint(1) not null,
	payment_payment_type varchar(255) null,
	refund_payment_type varchar(255) null,
	customer_id bigint null,
	payment_account bigint null,
	refund_account bigint null,
	transaction_id bigint null,
	languageIso varchar(255) null,
	customer_address_city varchar(255) null,
	customer_address_country_iso varchar(255) null,
	customer_number varchar(255) null,
	customer_name varchar(255) null,
	customer_organisation varchar(255) null,
	customer_address_postal_code varchar(255) null,
	customer_address_street varchar(255) null,
	customer_tax_number varchar(255) null,
	customer_title varchar(255) null,
	constraint UK_qunwt4vjvr83h0wbe6lyblijf
		unique (customer_email, transaction_id),
	constraint FK_1v35yg5ecdy7fcy1wrvc8gec7
		foreign key (customer_id) references contacts (id),
	constraint FK_qtlucmrnt9k913rvv482wk9b1
		foreign key (transaction_id) references transaction (id)
);

create table if not exists transaction_extra_data
(
	id bigint auto_increment
		primary key,
	created datetime not null,
	last_modified datetime not null,
	data tinyblob null,
	description varchar(255) null,
	name varchar(255) null,
	owner_id bigint not null,
	constraint FK_n553ujri6hii9qmr1rn2ljmni
		foreign key (owner_id) references transaction_participant (id)
);

create index FK_9ahru4nho5k6vldfylrlxh009
	on transaction_participant (refund_account);

create index FK_saftrfubfad1jwgxrrk8qplp5
	on transaction_participant (payment_account);

create table if not exists transaction_payment_account
(
	type varchar(20) not null,
	id bigint auto_increment
		primary key,
	created datetime not null,
	last_modified datetime not null,
	owner varchar(255) not null,
	bank_name varchar(255) null,
	city varchar(255) null,
	country_iso varchar(255) null,
	iban varchar(255) null,
	postal_code varchar(255) null,
	swiftBic varchar(255) null,
	paypal_account varchar(255) null
);

create table if not exists transaction_payment_document
(
	DTYPE varchar(31) not null,
	id bigint auto_increment
		primary key,
	created datetime not null,
	last_modified datetime not null,
	apply_payment_fee tinyint(1) null,
	apply_vat tinyint(1) null,
	commission_fee decimal(19,2) null,
	paid_amount decimal(19,2) null,
	payment_transaction_fee decimal(19,2) null,
	payment_type varchar(255) null,
	status varchar(255) null,
	transaction_name varchar(255) null,
	transaction_nr varchar(255) null,
	customer_id bigint null,
	transaction_id bigint null,
	partner_id varchar(255) null,
	other_id bigint not null,
	owner_id bigint not null,
	issuer_id bigint not null,
	issuer_addres_id bigint not null,
	payment_partner_id bigint not null,
	constraint FK_46s44lwmnu2i6cxox9nwq4f55
		foreign key (payment_partner_id) references organisation (id),
	constraint FK_996v7rl7ythesbidtt35nst2o
		foreign key (transaction_id) references transaction (id),
	constraint FK_evba65baae4l8jgm3kvaivaxc
		foreign key (owner_id) references transaction_participant (id),
	constraint FK_lpurbcy8vamh92f4uxs7g5t62
		foreign key (issuer_id) references organisation (id),
	constraint FK_oslw5xwupc0eo2632pa2d5w83
		foreign key (issuer_addres_id) references address (id),
	constraint FK_uhrt8fuvtkkfi7ebtsbspk1b
		foreign key (other_id) references transaction_participant (id)
);

create table if not exists transaction_payment
(
	DTYPE varchar(31) not null,
	id bigint auto_increment
		primary key,
	created datetime not null,
	last_modified datetime not null,
	amount decimal(19,2) null,
	net_amount decimal(19,2) null,
	payment_number varchar(255) null,
	status varchar(255) null,
	payment_document_id bigint not null,
	constraint FK_jcsr3fqbodombdri2b1p1en6v
		foreign key (payment_document_id) references transaction_payment_document (id)
);

create table if not exists transaction_startlink
(
	id bigint auto_increment
		primary key,
	created datetime not null,
	last_modified datetime not null,
	customer_email varchar(255) not null,
	domain_name varchar(255) not null,
	partner_id varchar(255) null,
	price decimal(19,2) not null,
	status varchar(255) not null,
	link_id bigint null,
	transaction_name varchar(255) not null,
	interface_type varchar(255) not null,
	constraint UK_ecv9p2m8fod4jio8981gr83jd
		unique (domain_name, transaction_name, price),
	constraint FK_i223ib7wmh4l013r7nnuowsoo
		foreign key (link_id) references activation_link (id)
);

create index idx_created
	on transaction_startlink (created);

create index idx_domain_name
	on transaction_startlink (domain_name);

create index idx_last_modified
	on transaction_startlink (last_modified);

create index idx_price
	on transaction_startlink (price);

create index idx_status
	on transaction_startlink (status);

create index idx_transaction_name
	on transaction_startlink (transaction_name);

create table if not exists transaction_startlink_trans
(
	startlink_id bigint not null,
	transaction_id bigint not null,
	constraint idx_startlink_transactions
		unique (startlink_id, transaction_id)
);

create index FK_756yej8l5tobfmrldy45jtj8d
	on transaction_startlink_trans (transaction_id);

create index FK_lqylm0nqr6498s9lcm60isqtj
	on transaction_startlink_trans (startlink_id);

create table if not exists users
(
	id bigint auto_increment
		primary key,
	created datetime not null,
	last_modified datetime not null,
	password varchar(255) not null,
	secuirty_question varchar(255) null,
	security_answer varchar(255) null,
	status varchar(255) not null,
	username varchar(255) not null,
	contact_id bigint null,
	created_by varchar(255) null,
	last_modified_by varchar(255) null,
	constraint UK_lgls6n8uui866b9vov06liae7
		unique (contact_id),
	constraint UK_r43af9ap4edm43mmtq01oddj6
		unique (username),
	constraint FK_lgls6n8uui866b9vov06liae7
		foreign key (contact_id) references contacts (id)
);

create table if not exists password_state_info
(
	id bigint auto_increment
		primary key,
	created datetime not null,
	last_modified datetime not null,
	status tinyint(1) not null,
	valid tinyint(1) not null,
	user_id bigint null,
	constraint FK_1rggt216mr4g7c2rw4w4691a5
		foreign key (user_id) references users (id)
);

create table if not exists user_login_info
(
	id bigint auto_increment
		primary key,
	created datetime not null,
	last_modified datetime not null,
	login_event varchar(255) null,
	user_id bigint null,
	constraint FK_nrc5la2c8pfquqe0uhef2unpu
		foreign key (user_id) references users (id)
);

create table if not exists user_roles
(
	user_id bigint not null,
	role_id bigint not null,
	primary key (user_id, role_id),
	constraint FK_5q4rc4fh1on6567qk69uesvyf
		foreign key (role_id) references roles (id),
	constraint FK_g1uebn6mqk9qiaw45vnacmyo2
		foreign key (user_id) references users (id)
);

create index idx_password
	on users (password);

create index idx_status
	on users (status);

create index idx_username
	on users (username);
