--�����û�ecif2�û�
create user ecif
  identified by "ecif"
  default tablespace USERS
  temporary tablespace TEMP
  profile DEFAULT;
grant connect to ecif;
grant resource to ecif;