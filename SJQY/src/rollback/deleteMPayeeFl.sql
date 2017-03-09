delete from ECIFCROSSBANKPAYEE where cifseq in (select cifseq from mtmpdata where payeeflag='FL');
delete from ECIFBANKINNERPAYEE where cifseq in (select cifseq from mtmpdata where payeeflag='FL');

update mtmpdata set payeeflag='NT' where payeeflag='FL';




