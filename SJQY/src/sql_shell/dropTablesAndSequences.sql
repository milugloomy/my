--删除ECIF用户下所有表和序列
declare 
  i number:=1;
  v_count number;
  v_current_user varchar2(100);
  
  procedure droptables is
  exesql varchar2(1000);
  cursor c_table is
    select table_name from user_tables;
  begin
    for cur in c_table loop
      exesql:='drop table '||cur.table_name;
      dbms_output.put_line(exesql);
      begin
        execute immediate exesql;
      exception when others then
          dbms_output.put_line('----Foreign Key Constraint,Programme Continue;----');
      end;
    end loop;
  end;
  
  procedure dropsequences is
  	exesql varchar2(1000);
  	cursor c_table is
    	select sequence_name from user_sequences;
  begin
    for cur in c_table loop
      exesql:='drop sequence '||cur.sequence_name;
      dbms_output.put_line(exesql);
      execute immediate exesql;
    end loop;
  end;
begin
  select user into v_current_user from dual;
  if v_current_user <> 'ECIF' then
    raise_application_error(-20001,'只允许ECIF用户执行此程序');
  end if;
  select count(*) into v_count from user_tables;
  
  while v_count>0 loop
    begin
      dbms_output.put_line('---------------Round '||i||'---------------');
      droptables;
      select count(*) into v_count from user_tables;
      i:=i+1;
    end;
  end loop;
  dropsequences;
end;
