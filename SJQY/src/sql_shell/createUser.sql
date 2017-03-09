--创建用户ecif2用户
create user ecif
  identified by "ecif"
  default tablespace USERS
  temporary tablespace TEMP
  profile DEFAULT;
grant connect to ecif;
grant resource to ecif;