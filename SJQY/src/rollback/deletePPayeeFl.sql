delete from ECIFCROSSBANKPAYEE where cifseq in (select cifseq from ptmpdata where payeeflag='FL');
delete from ECIFBANKINNERPAYEE where cifseq in (select cifseq from ptmpdata where payeeflag='FL');

update ptmpdata set payeeflag='NT' where payeeflag='FL';
commit;



