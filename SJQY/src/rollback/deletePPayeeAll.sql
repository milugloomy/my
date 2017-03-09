delete from ECIFCROSSBANKPAYEE where cifseq in (select cifseq from ptmpdata);
commit;
delete from ECIFBANKINNERPAYEE where cifseq in (select cifseq from ptmpdata);
commit;

update ptmpdata set payeeflag='NT' where payeeflag!='NT';
commit;
