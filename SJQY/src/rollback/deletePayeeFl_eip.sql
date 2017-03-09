delete from UTILITIESQUICKPAYMENT where cifseq in (select cifseq from ecif.mtmpdata where payeeflag='FL');
