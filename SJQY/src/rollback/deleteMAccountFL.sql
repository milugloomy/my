delete from ECUSRMCHACCT where mchannelid='PMBS' and acseq in(select acseq from ecacct where cifseq in (select cifseq from mtmpdata where accountflag='FL'));
delete from ECACCTRULE where ruleid like 'PMBS%' and acseq in(select acseq from ecacct where cifseq in (select cifseq from mtmpdata where accountflag='FL'));
delete from ECACCTMCH where mchannelid='PMBS' and acseq in(select acseq from ecacct where cifseq in (select cifseq from mtmpdata where accountflag='FL'));
delete from ECACCT where UPDATEUSERSEQ='9527' and cifseq in (select cifseq from mtmpdata where accountflag='FL');

update mtmpdata set accountflag='NT' where accountflag='FL';
