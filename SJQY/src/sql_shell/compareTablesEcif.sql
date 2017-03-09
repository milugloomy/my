declare 
  v_current_user varchar2(100);
  
  procedure compareTablesEcif is
  begin
    --tables in sit,not in dev
    for curSit in (select table_name from user_tables a where not exists 
      (select * from user_tables@my_tj_dev_ecif where TABLE_NAME=a.TABLE_NAME))
    loop
      dbms_output.put_line('���Կ�ı�'||curSit.table_name||'�ڿ�����û��');
    end loop;
    
    --tables in dev,not in sit
    for curDev in (select table_name from user_tables@my_tj_dev_ecif a where not exists 
      (select * from user_tables where TABLE_NAME=a.TABLE_NAME))
    loop
      dbms_output.put_line('������ı�'||curDev.table_name||'�����ڿ�û��');
    end loop;
  end;
  
  procedure compareColumnsEcif is
    v_count number;
    cursor c_table is select table_name from user_tables;
  begin
    for curT in c_table loop     
      --columns in sit,not in dev
      select count(*) into v_count from user_tab_cols a where a.table_name=curT.table_name and not exists 
        (select * from user_tab_cols@my_tj_dev_ecif where column_name=a.column_name and table_name=a.table_name);
      if v_count>0 then
        for curC in (select column_name from user_tab_cols a where a.table_name=curT.table_name and not exists 
          (select * from user_tab_cols@my_tj_dev_ecif where column_name=a.column_name and table_name=a.table_name))
        loop
          dbms_output.put_line('���Կ��'||curT.table_name||'����'||curC.column_name||'�ڿ�����û��');
        end loop;
      end if;

      --columns in dev,not in sit
      select count(*) into v_count from user_tab_cols@my_tj_dev_ecif a where a.table_name=curT.table_name and not exists 
        (select * from user_tab_cols where column_name=a.column_name and table_name=a.table_name);
      if v_count>0 then
        for curC in (select column_name from user_tab_cols@my_tj_dev_ecif a where a.table_name=curT.table_name and not exists 
          (select * from user_tab_cols where column_name=a.column_name and table_name=a.table_name))
        loop
          dbms_output.put_line('�������'||curT.table_name||'����'||curC.column_name||'�ڲ��Կ�û��');
        end loop;
      end if;
      
      --check whether properties of columns are the same
      select count(*) into v_count from user_tab_cols a,user_tab_cols@my_tj_dev_ecif b 
        where a.TABLE_NAME=b.TABLE_NAME and a.COLUMN_NAME=b.COLUMN_NAME and a.TABLE_NAME=curT.table_name
          and (a.DATA_TYPE!=b.DATA_TYPE or a.DATA_LENGTH!=b.DATA_LENGTH or a.NULLABLE!=b.NULLABLE);
      if v_count>0 then
        for curC in (select a.TABLE_NAME,a.COLUMN_NAME,a.DATA_TYPE SIT_DATA_TYPE,b.DATA_TYPE DEV_DATA_TYPE,
          a.DATA_LENGTH SIT_DATA_LENGTH,b.DATA_LENGTH DEV_DATA_LENGTH,a.NULLABLE SIT_NULLABLE,b.NULLABLE DEV_NULLABLE
          from user_tab_cols a,user_tab_cols@my_tj_dev_ecif b where a.TABLE_NAME=b.TABLE_NAME 
          and a.COLUMN_NAME=b.COLUMN_NAME and a.TABLE_NAME=curT.table_name
          and (a.DATA_TYPE!=b.DATA_TYPE or a.DATA_LENGTH!=b.DATA_LENGTH or a.NULLABLE!=b.NULLABLE))
        loop
          if curC.SIT_DATA_TYPE<>curC.DEV_DATA_TYPE then
            dbms_output.put_line('��'||curT.table_name||'����'||curC.column_name||'����������Data Type��ͬ');
          end if;
          if curC.SIT_DATA_LENGTH<>curC.DEV_DATA_LENGTH then
            dbms_output.put_line('��'||curT.table_name||'����'||curC.column_name||'����������Data Length��ͬ');
          end if;
          if curC.SIT_NULLABLE<>curC.DEV_NULLABLE then
            dbms_output.put_line('��'||curT.table_name||'����'||curC.column_name||'����������Nullable��ͬ');
          end if;          
        end loop;
      end if;
      
    end loop;
  end;
begin
  select user into v_current_user from dual;
  if v_current_user <> 'ECIF' then
    raise_application_error(-20001,'ֻ����ECIF�û�ִ�д˳���');
  end if;
  compareTablesEcif;
  dbms_output.put_line('------------------------------------------------------');
  compareColumnsEcif;
end;
