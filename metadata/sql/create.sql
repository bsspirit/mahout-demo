use mahout;

CREATE TABLE t_data1(
	user_id bigint NOT NULL,
	item_id bigint NOT NULL,
	preference float NOT NULL,
	primary key(user_id, item_id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;
