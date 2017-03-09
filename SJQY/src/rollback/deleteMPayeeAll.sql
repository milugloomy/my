delete from ECIFCROSSBANKPAYEE where cifseq in (select cifseq from mtmpdata);
commit;
delete from ECIFBANKINNERPAYEE where cifseq in (select cifseq from mtmpdata);
commit;

update mtmpdata set payeeflag='NT' where payeeflag!='NT';
commit;
