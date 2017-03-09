delete from ECUSRMCHACCT where mchannelid='PIBS' and acseq in(select acseq from ecacct where cifseq in (select cifseq from ptmpdata where accountflag='FL'));
delete from ECACCTRULE where ruleid like 'PIBS%' and acseq in(select acseq from ecacct where cifseq in (select cifseq from ptmpdata where accountflag='FL'));
delete from ECACCTRULE where ruleid = 'CrossBankTransfer' and acseq in(select acseq from ecacct where cifseq in (select cifseq from ptmpdata where accountflag='FL'));
delete from ECACCTRULE where ruleid = 'BankInnerTransfer' and acseq in(select acseq from ecacct where cifseq in (select cifseq from ptmpdata where accountflag='FL'));
delete from ECACCTMCH where mchannelid='PIBS' and acseq in(select acseq from ecacct where cifseq in (select cifseq from ptmpdata where accountflag='FL'));
delete from ECACCT where UPDATEUSERSEQ='9528' and cifseq in (select cifseq from ptmpdata where accountflag='FL');

update ptmpdata set accountflag='NT' where accountflag='FL';
