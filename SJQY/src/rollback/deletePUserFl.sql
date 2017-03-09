delete from ECUSRCERT where userseq in(select userseq from ecusr where cifseq in(select cifseq from ptmpdata where userflag='FL'));
delete from ECUSRAUTHMODMCH where mchannelid='PIBS' and userseq in(select userseq from ecusr where cifseq in(select cifseq from PTMPDATA where userflag='FL'));
delete from ECUSRAUTHMOD where AUTHMOD = 'Q' and userseq in(select userseq from ecusr where cifseq in(select cifseq from PTMPDATA where userflag='FL' and newcif='Y'));
delete from ECUSRAUTHMOD where authmod='U' and userseq in(select userseq from ecusr where cifseq in(select cifseq from PTMPDATA where userflag='FL'));
delete from ECUSRLOGINTYPE where mchannelid='PIBS' and userseq in(select userseq from ecusr where cifseq in(select cifseq from PTMPDATA where userflag='FL'));
delete from ECUSRRULE where ruleid like 'PIBS%' and userseq in(select userseq from ecusr where cifseq in(select cifseq from PTMPDATA where userflag='FL'));
delete from ECUSRMCH where mchannelid='PIBS' and userseq in(select userseq from ecusr where cifseq in(select cifseq from PTMPDATA where userflag='FL'));
delete from ECUSR where cifseq in(select cifseq from PTMPDATA where userflag='FL' and newcif='Y');


delete from ECCIFTEL where cifseq in(select cifseq from PTMPDATA where userflag='FL' and newcif='Y');
delete from ECCIFRULE where cifseq in(select cifseq from PTMPDATA where userflag='FL');
delete from ECPERS where cifseq in(select cifseq from PTMPDATA where userflag='FL' and newcif='Y');
delete from ECCIFPRODSET where cifseq in(select cifseq from PTMPDATA where userflag='FL') and mchannelid='PIBS';
delete from ECCIFLIMIT where cifseq in(select cifseq from PTMPDATA where userflag='FL') and mchannelid='PIBS';
delete from ECEXTCIFNO where cifseq in(select cifseq from PTMPDATA where userflag='FL' and newcif='Y');
delete from ECCIFID where cifseq in(select cifseq from PTMPDATA where userflag='FL' and newcif='Y');
delete from ECCIFMCH where cifseq in(select cifseq from PTMPDATA where userflag='FL') and mchannelid='PIBS';
delete from ECCIFOUTTRANSFERSWITCH where mchannelid='PIBS' and cifseq in(select cifseq from PTMPDATA where userflag='FL');
delete from ECCIF where cifseq in(select cifseq from PTMPDATA where userflag='FL' and newcif='Y');

update PTMPDATA set userflag='NT'where userflag='FL';
