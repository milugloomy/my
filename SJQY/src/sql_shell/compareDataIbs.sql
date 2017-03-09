--serviceauthmod
--records in sit, not in dev
select * from serviceauthmod a where not exists (select * from serviceauthmod@my_tj_dev_ibs b where a.prdid=b.prdid);
--records in dev, not in sit
select * from serviceauthmod@my_tj_dev_ibs b where not exists (select * from serviceauthmod a where a.prdid=b.prdid);
--records same in primary key,different in others
select * from serviceauthmod a,serviceauthmod@my_tj_dev_ibs b where a.prdid=b.prdid
	and a.authmod!=b.authmod;

--rule
--records in sit, not in dev
select * from rule a where not exists (select * from rule@my_tj_dev_ibs b 
	where a.moduleid=b.moduleid and a.rulenamespace=b.rulenamespace and a.ruletype=b.ruletype and a.ruleid=b.ruleid);
--records in dev, not in sit
select * from rule@my_tj_dev_ibs b where not exists (select * from rule a 
	where a.moduleid=b.moduleid and a.rulenamespace=b.rulenamespace and a.ruletype=b.ruletype and a.ruleid=b.ruleid);
--records same in primary key,different in others
select * from rule a,rule@my_tj_dev_ibs b where (a.moduleid=b.moduleid and a.rulenamespace=b.rulenamespace and a.ruletype=b.ruletype and a.ruleid=b.ruleid)
  and (a.ruledef!=b.ruledef or a.rulescript!=b.rulescript or a.prdid!=b.prdid or a.description!=b.description);
