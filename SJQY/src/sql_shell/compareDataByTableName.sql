--此方法只打印sql语句，需再单独执行
declare 
  procedure compareTablesEip(
    v_tableName in varchar2,
    v_user in varchar2
  ) is
  s_exesql_1 varchar2(1000);
  s_exesql_2 varchar2(1000);
  s_exesql_3 varchar2(1000);
	s_wstr_and varchar2(1000):='where';
	s_wstr_or varchar2(1000):='(';
  --type t_curD is ref cursor;
  --c_curD t_curD;
  
  cursor c_table(v_tableName varchar2) is
    select a.column_name from user_cons_columns a,user_constraints b where b.constraint_type='P'
       and b.table_name=v_tableName and a.constraint_name=b.constraint_name;
  
  begin
    s_exesql_1:='select * from '||v_tableName||' a where not exists(select * from '||v_tableName||'@my_tj_dev_'||v_user||' b ';
    for cur in c_table(v_tableName) loop
      s_wstr_and:=s_wstr_and||' a.'||cur.column_name||'=b.'||cur.column_name||' and';
    end loop;
    s_exesql_1:=s_exesql_1||s_wstr_and||' 1=1);';
    dbms_output.put_line(s_exesql_1);
    
    s_exesql_2:='select * from '||v_tableName||'@my_tj_dev_'||v_user||' a where not exists(select * from '||v_tableName||' b ';
    s_exesql_2:=s_exesql_2||s_wstr_and||' 1=1);';
    dbms_output.put_line(s_exesql_2);
    
    s_exesql_3:='select * from '||v_tableName||' a,'||v_tableName||'@my_tj_dev_'||v_user||' b ';
    s_exesql_3:=s_exesql_3||s_wstr_and;
    for cur in(select column_name from user_tab_cols c where table_name=v_tableName and not exists (
      select a.column_name from user_cons_columns a,user_constraints b where b.constraint_type='P'
      and b.table_name=v_tableName and a.constraint_name=b.constraint_name and a.column_name=c.column_name)
    )loop
      s_wstr_or:=s_wstr_or||' a.'||cur.column_name||'!=b.'||cur.column_name||' or';
    end loop;
    s_exesql_3:=s_exesql_3||s_wstr_or||' 1!=1);';
    dbms_output.put_line(s_exesql_3);
   
    /*open c_curD for s_exesql_1;
      loop
        fetch c_curD into r_row;
        exit when c_curD%notfound;
        dbms_output.put_line(1);
      end loop;
    close c_curD;*/
  end;
begin
  compareTablesEip('DEPT','eip');
  compareTablesEip('BANKPROD','eip');
  
end;