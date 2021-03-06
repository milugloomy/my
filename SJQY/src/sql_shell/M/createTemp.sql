﻿create table TMPTIME
(
  	T_INDEX		VARCHAR(32),
	TRANSCODE	VARCHAR(32),
	STARTTIME	TIMESTAMP,
	ENDTIME		TIMESTAMP,
	TOTALMINUTE		NUMBER(8,2)
);
--手机银行临时表
create table MTMPDATA(	
  CIFSEQ  		VARCHAR(32) not null,
  CIFNO   		VARCHAR(64) not null,
  NEWCIF		VARCHAR(1),
  USERFLAG     	VARCHAR(2),
  ACCOUNTFLAG	VARCHAR(2),
  PAYEEFLAG		VARCHAR(2)
);
ALTER TABLE MTMPDATA ADD CONSTRAINT PK_MTMPDATA PRIMARY KEY(CIFSEQ);
ALTER TABLE MTMPDATA ADD CONSTRAINT MTMPDATA_U_CIFNO UNIQUE(CIFNO);
