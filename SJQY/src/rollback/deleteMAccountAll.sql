delete from ECUSRMCHACCT where mchannelid='PMBS';
commit;
delete from ECACCTRULE where ruleid like 'PMBS%';
commit;
delete from ECACCTMCH where mchannelid='PMBS';
commit;
delete from ECACCT where UPDATEUSERSEQ='9527';
commit;

update mtmpdata set accountflag='NT' where accountflag!='NT';
commit;

