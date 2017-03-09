delete from ECUSRCERT where userseq in(select userseq from ecusr where cifseq in(select cifseq from ptmpdata));
commit;
delete from ECUSRAUTHMODMCH where mchannelid='PIBS' and userseq in(select userseq from ecusr where cifseq in(select cifseq from ptmpdata));
commit;
delete from ECUSRAUTHMOD where userseq in(select userseq from ecusr where cifseq in(select cifseq from ptmpdata where newcif='Y'));
commit;
delete from ECUSRLOGINTYPE where mchannelid='PIBS' and userseq in(select userseq from ecusr where cifseq in(select cifseq from ptmpdata));
commit;
delete from ECUSRRULE where ruleid like 'PIBS%' and userseq in(select userseq from ecusr where cifseq in(select cifseq from ptmpdata));
commit;
delete from ECUSRMCH where mchannelid='PIBS' and userseq in(select userseq from ecusr where cifseq in(select cifseq from ptmpdata));;
commit;
delete from ECUSR where cifseq in(select cifseq from ptmpdata where newcif='Y');
commit;

delete from ECCIFTEL where cifseq in(select cifseq from PTMPDATA where newcif='Y');
commit;
delete from ECCIFRULE where cifseq in(select cifseq from PTMPDATA where newcif='Y');
commit;
delete from ECPERS where cifseq in(select cifseq from PTMPDATA where newcif='Y');
commit;
delete from ECCIFPRODSET where mchannelid='PIBS' where cifseq in(select cifseq from PTMPDATA);;
commit;
delete from ECCIFLIMIT where mchannelid='PIBS' where cifseq in(select cifseq from PTMPDATA);;
commit;
delete from ECEXTCIFNO where cifseq in(select cifseq from PTMPDATA where newcif='Y');
commit;
delete from ECCIFID where cifseq in(select cifseq from PTMPDATA where newcif='Y');
commit;
delete from ECCIFMCH where mchannelid='PIBS' where cifseq in(select cifseq from PTMPDATA);
commit;
delete from ECCIFOUTTRANSFERSWITCH where mchannelid='PIBS' and cifseq in(select cifseq from PTMPDATA);
commit;
delete from ECCIF where cifseq in(select cifseq from PTMPDATA where newcif='Y');
commit;

update PTMPDATA set userflag='NT' where userflag!='NT';
commit;




