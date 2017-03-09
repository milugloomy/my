delete from ECACLIMIT where cifseq in (
	select cifseq from ECCIFMCH where (MCHANNELID <>  'PWEC' and MCHANNELID <> 'BBMS') and cifseq not in (select cifseq from MTMPDATA)
) and MCHANNELID = 'PMBS';
delete from ECACCTRULE where acseq in (select acseq from ecacct where cifseq in (
	select cifseq from ECCIFMCH where (MCHANNELID <>  'PWEC' and MCHANNELID <> 'BBMS') and cifseq not in (select cifseq from MTMPDATA)
)) and RULEID like 'PMBS%';
delete from ECUSRMCHACCT where userseq in (select userseq from ecusr where cifseq in (
	select cifseq from ECCIFMCH where (MCHANNELID <>  'PWEC' and MCHANNELID <> 'BBMS') and cifseq not in (select cifseq from MTMPDATA)
)) and MCHANNELID = 'PMBS';
delete from ECACCTMCH where acseq in (select acseq from ecacct where cifseq in (
	select cifseq from ECCIFMCH where (MCHANNELID <>  'PWEC' and MCHANNELID <> 'BBMS') and cifseq not in (select cifseq from MTMPDATA)
)) and MCHANNELID = 'PMBS';
delete from ECUSRLOGINTYPE where userseq in (select userseq from ecusr where cifseq in (
	select cifseq from ECCIFMCH where (MCHANNELID <>  'PWEC' and MCHANNELID <> 'BBMS') and cifseq not in (select cifseq from MTMPDATA)
)) and MCHANNELID = 'PMBS';
delete from ECUSRRULE where userseq in (select userseq from ecusr where cifseq in (
	select cifseq from ECCIFMCH where (MCHANNELID <>  'PWEC' and MCHANNELID <> 'BBMS') and cifseq not in (select cifseq from MTMPDATA)
)) and ruleid like 'PMBS%';
delete from ECUSRDEVICE where userseq in (select userseq from ecusr where cifseq in (
	select cifseq from ECCIFMCH where (MCHANNELID <>  'PWEC' and MCHANNELID <> 'BBMS') and cifseq not in (select cifseq from MTMPDATA)
)) and MCHANNELID = 'PMBS';
delete from ECUSRAUTHMODMCH where userseq in (select userseq from ecusr where cifseq in (
	select cifseq from ECCIFMCH where (MCHANNELID <>  'PWEC' and MCHANNELID <> 'BBMS') and cifseq not in (select cifseq from MTMPDATA)
)) and MCHANNELID = 'PMBS' and AUTHMOD ='Q';
delete from ECUSRAUTHMOD where userseq in (select userseq from ecusr where cifseq in (
	select cifseq from ECCIFMCH where (MCHANNELID <>  'PWEC' and MCHANNELID <> 'BBMS') and cifseq not in (select cifseq from MTMPDATA)
)) and AUTHMOD ='Q';
delete from ECUSRMCH where userseq in (select userseq from ecusr where cifseq in (
	select cifseq from ECCIFMCH where (MCHANNELID <>  'PWEC' and MCHANNELID <> 'BBMS') and cifseq not in (select cifseq from MTMPDATA)
)) and MCHANNELID = 'PMBS';
delete from ECCIFLIMIT where cifseq in (
	select cifseq from ECCIFMCH where (MCHANNELID <>  'PWEC' and MCHANNELID <> 'BBMS') and cifseq not in (select cifseq from MTMPDATA)
) and MCHANNELID = 'PMBS';
delete from ECCIFPRODSET where cifseq in (
	select cifseq from ECCIFMCH where (MCHANNELID <>  'PWEC' and MCHANNELID <> 'BBMS') and cifseq not in (select cifseq from MTMPDATA)
) and MCHANNELID = 'PMBS'; 
delete from ECCIFRULE where cifseq in (
	select cifseq from ECCIFMCH where (MCHANNELID <>  'PWEC' and MCHANNELID <> 'BBMS') and cifseq not in (select cifseq from MTMPDATA)
) and ruleid like 'PMBS%';
---delete from ECIFBANKINNERPAYEE where cifseq in (
---	select cifseq from ECCIFMCH where (MCHANNELID <>  'PWEC' and MCHANNELID <> 'BBMS') and cifseq not in (select cifseq from MTMPDATA)
---);
---delete from ECIFCROSSBANKPAYEE where cifseq in (
---	select cifseq from ECCIFMCH where (MCHANNELID <>  'PWEC' and MCHANNELID <> 'BBMS') and cifseq not in (select cifseq from MTMPDATA)
---);
delete from ECCIFMCH where cifseq in (select cifseq from eccif where cifseq in (
	select cifseq from ECCIFMCH where (MCHANNELID <>  'PWEC' and MCHANNELID <> 'BBMS') and cifseq not in (select cifseq from MTMPDATA)
)) and MCHANNELID = 'PMBS';