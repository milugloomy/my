delete from ECUSRMCHACCT where mchannelid='PIBS';
commit;
delete from ECACCTRULE where ruleid like 'PIBS%';
delete from ECACCTRULE where ruleid = 'CrossBankTransfer';
delete from ECACCTRULE where ruleid = 'BankInnerTransfer';
commit;
delete from ECACCTMCH where mchannelid='PIBS';
commit;
delete from ECACCT where UPDATEUSERSEQ='9528';
commit;

update ptmpdata set accountflag='NT' where accountflag!='NT';
commit;


