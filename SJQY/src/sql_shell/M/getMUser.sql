---通过sqlplus命令窗口生成标准的待迁移客户名单
---through sqlplus commond get the Muser list for datemove
set term off;
set trimout on;
set heading off;
set echo off;
set termout off;
set feedback off;
set trimspool on;
set pagesize 0 ;
set newpage 1;
set lin 3000;
spool MUser.txt
select cust_NO from extr_mbcustinfo where cust_no not in (select cifno from ecif.MTMPDATA );
spool off;