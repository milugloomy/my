--bankprod
--records in sit, not in dev
select * from bankprod a where not exists (select * from bankprod@my_tj_dev_eip b where a.prdid=b.prdid);
--records in dev, not in sit
select * from bankprod@my_tj_dev_eip b where not exists (select * from bankprod a where a.prdid=b.prdid);
--records same in primary key,different in others
select * from bankprod a,bankprod@my_tj_dev_eip b where a.prdid=b.prdid and        
  (a.moduleid!=b.moduleid or a.prdname!=b.prdname or a.prdstate!=b.prdstate or 
  a.mgmtprdflag!=b.mgmtprdflag or a.prdcheckflag!=b.prdcheckflag or a.prdauthflag!=b.prdauthflag or a.prdreleaseflag!=b.prdreleaseflag or 
  a.checktype!=b.checktype or a.checkdept!=b.checkdept or a.checkdiv!=b.checkdiv or a.prdotpflag!=b.prdotpflag or 
  a.prddigitsignflag!=b.prddigitsignflag or a.prdtrspasswordflag!=b.prdtrspasswordflag or a.acflag!=b.acflag or 
  a.limitflag!=b.limitflag or a.feeflag!=b.feeflag or a.banklimitflag!=b.banklimitflag or a.reportflag!=b.reportflag);

--prodch
--records in sit, not in dev
select * from prodch a where not exists (select * from prodch@my_tj_dev_eip b where a.prodid=b.prodid and a.channelid=b.channelid);
--records in dev, not in sit
select * from prodch@my_tj_dev_eip b where not exists (select * from prodch a where a.prodid=b.prodid and a.channelid=b.channelid);
--records same in primary key,different in others
select * from prodch a,prodch@my_tj_dev_eip b where(a.prodid=b.prodid and a.channelid=b.channelid) 
  and (a.mgmtprdflag!=b.mgmtprdflag or a.authcheck!=b.authcheck);
  
--prdset
--records in sit, not in dev
select * from prdset a where not exists (select * from prdset@my_tj_dev_eip b where a.prdsetid=b.prdsetid);
--records in dev, not in sit
select * from prdset@my_tj_dev_eip b where not exists (select * from prdset a where a.prdsetid=b.prdsetid);
--records same in primary key,different in others
select * from prdset a,prdset@my_tj_dev_eip b where a.prdsetid=b.prdsetid
  and (a.moduleid!=b.moduleid or a.prdsettypeid!=b.prdsettypeid or a.prdsetname!=b.prdsetname or a.parentid!=b.parentid or a.orderindex!=b.orderindex);

--prdsetprd
--records in sit, not in dev
select * from prdsetprd a where not exists (select * from prdsetprd@my_tj_dev_eip b where a.prdsetid=b.prdsetid and a.prdid=b.prdid);
--records in dev, not in sit
select * from prdsetprd@my_tj_dev_eip b where not exists (select * from prdsetprd a where a.prdsetid=b.prdsetid and a.prdid=b.prdid);
  
--mcroleproduct
--records in sit, not in dev
select * from mcroleproduct a where not exists (select * from mcroleproduct@my_tj_dev_eip b 
  where a.mchannelid=b.mchannelid and a.ciftype=b.ciftype and a.roleid=b.roleid and a.prdid=b.prdid);
--records in dev, not in sit
select * from mcroleproduct@my_tj_dev_eip b where not exists (select * from mcroleproduct a 
  where a.mchannelid=b.mchannelid and a.ciftype=b.ciftype and a.roleid=b.roleid and a.prdid=b.prdid);
--records same in primary key,different in others
select * from mcroleproduct a,mcroleproduct@my_tj_dev_eip b where (a.mchannelid=b.mchannelid and a.ciftype=b.ciftype and a.roleid=b.roleid and a.prdid=b.prdid)
  and (a.authflag!=b.authflag or a.enterflag!=b.enterflag);

--deptprodgroupprod
--records in sit, not in dev
select * from deptprodgroupprod a where not exists (select * from deptprodgroupprod@my_tj_dev_eip b 
  where a.prdid=b.prdid and a.prodgroupname=b.prodgroupname and a.deptseq=b.deptseq and a.moduleid=b.moduleid and a.channelid=b.channelid);
--records in dev, not in sit
select * from deptprodgroupprod@my_tj_dev_eip b where not exists (select * from deptprodgroupprod a 
  where a.prdid=b.prdid and a.prodgroupname=b.prodgroupname and a.deptseq=b.deptseq and a.moduleid=b.moduleid and a.channelid=b.channelid);
