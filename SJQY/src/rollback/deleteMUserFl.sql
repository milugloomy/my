delete from ECUSRAUTHMODMCH where mchannelid='PMBS' and userseq in(select userseq from ecusr where cifseq in(select cifseq from mtmpdata where userflag='FL'));
delete from ECUSRAUTHMOD where userseq in(select userseq from ecusr where cifseq in(select cifseq from mtmpdata where userflag='FL' and newcif='Y'));
delete from ECUSRAUTHMOD where AUTHMOD = 'Q' and userseq in(select userseq from ecusr where cifseq in(select cifseq from mtmpdata where userflag='FL'));
delete from ECUSRLOGINTYPE where mchannelid='PMBS' and userseq in(select userseq from ecusr where cifseq in(select cifseq from mtmpdata where userflag='FL'));
delete from ECUSRRULE where ruleid like 'PMBS%' and userseq in(select userseq from ecusr where cifseq in(select cifseq from mtmpdata where userflag='FL'));
delete from ECUSRMCH where mchannelid='PMBS' and userseq in(select userseq from ecusr where cifseq in(select cifseq from mtmpdata where userflag='FL'));
delete from ECUSR where cifseq in(select cifseq from mtmpdata where userflag='FL' and newcif='Y');


delete from ECCIFTEL where cifseq in(select cifseq from MTMPDATA where userflag='FL' and newcif='Y');
delete from ECCIFRULE where cifseq in(select cifseq from MTMPDATA where userflag='FL');
delete from ECPERS where cifseq in(select cifseq from MTMPDATA where userflag='FL' and newcif='Y');
delete from ECCIFPRODSET where cifseq in(select cifseq from MTMPDATA where userflag='FL') and mchannelid='PMBS';
delete from ECCIFLIMIT where cifseq in(select cifseq from MTMPDATA where userflag='FL');
delete from ECEXTCIFNO where cifseq in(select cifseq from MTMPDATA where userflag='FL' and newcif='Y');
delete from ECCIFID where cifseq in(select cifseq from MTMPDATA where userflag='FL' and newcif='Y');
delete from ECCIFMCH where cifseq in(select cifseq from MTMPDATA where userflag='FL') and mchannelid='PMBS';
delete from ECCIFOUTTRANSFERSWITCH where mchannelid='PMBS' and cifseq in(select cifseq from MTMPDATA where userflag='FL');
delete from ECCIF where cifseq in(select cifseq from MTMPDATA where userflag='FL' and newcif='Y');

update mtmpdata set userflag='NT'where userflag='FL';
