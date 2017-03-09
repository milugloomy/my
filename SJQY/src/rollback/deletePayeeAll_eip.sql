delete from UTILITIESQUICKPAYMENT where cifseq in (select cifseq from mtmpdata);
commit;

