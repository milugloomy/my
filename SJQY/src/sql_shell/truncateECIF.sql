declare
	cifArray varchar2(100) :='(800000000,800000001)';
	procedure truncateEcif(
		cifArray in varchar2,
		tableName in varchar2
	)is
	exesql varchar2(1000);
	wstr   varchar2(1000);
	table_doesnot_exists exception;
	cursor c_table(v_table_name varchar2) is
		select distinct a.table_name from user_constraints a,user_constraints b 
		where a.r_constraint_name=b.constraint_name and b.table_name=v_table_name and b.constraint_type='P';
	begin
		if tableName='ECACCT' then
			wstr:=' where acseq in(select acseq from ECACCT where cifseq in'||cifArray||')';
		elsif tableName='ECUSR' then
			wstr:=' where userseq in(select userseq from ECUSR where cifseq in'||cifArray||')';
		elsif tableName='ECCIFROLE' then
			wstr:=' where roleseq in(select roleseq from ECCIFROLE where cifseq in'||cifArray||')';
		else
			wstr:=' where cifseq in'||cifArray;
		end if;    
		for cur in c_table(tableName)
		loop
			exesql:='delete from '||cur.table_name||wstr; 
			dbms_output.put_line(exesql);
			execute immediate exesql;
			dbms_output.put_line(sql%rowcount||' row delete'); 
		end Loop;
		exesql:='delete from '||tableName||' where cifseq in'||cifArray;
		dbms_output.put_line(exesql);
		execute immediate exesql;
		dbms_output.put_line(sql%rowcount||' row delete');
  	exception
    	when table_doesnot_exists then
		begin
			rollback;
      raise_application_error(-20001,'±í²»´æÔÚ');
    end;
    end truncateEcif;
begin  
  truncateEcif(cifArray,'ECACCT');
  truncateEcif(cifArray,'ECCIFROLE');
  truncateEcif(cifArray,'ECUSR');
  truncateEcif(cifArray,'ECORG');
  truncateEcif(cifArray,'ECCIF');
end;
