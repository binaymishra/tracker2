DROP TABLE table_a IF EXISTS;
DROP TABLE table_b IF EXISTS;
DROP TABLE table_c IF EXISTS;

create table table_a(
userIp 	VARCHAR(100) DEFAULT NULL,
userMac VARCHAR(100) DEFAULT NULL,
apName 	VARCHAR(100) DEFAULT NULL,
apMac 	VARCHAR(100) DEFAULT NULL,
startTime 	BIGINT,
endTime		BIGINT,
startTS 	TIMESTAMP,
endTS		TIMESTAMP
);

create table table_b(
userIp 	VARCHAR(100) DEFAULT NULL,
userMac VARCHAR(100) DEFAULT NULL,
apName 	VARCHAR(100) DEFAULT NULL,
apMac 	VARCHAR(100) DEFAULT NULL,
startTime 	BIGINT,
endTime		BIGINT,
startTS 	TIMESTAMP,
endTS		TIMESTAMP
);
create table table_c(
	prefix VARCHAR(100) DEFAULT NULL,
	lat DECIMAL(20,10),
	lon DECIMAL(20,10),
	name VARCHAR(100) DEFAULT NULL,
	category VARCHAR(100) DEFAULT NULL
);


