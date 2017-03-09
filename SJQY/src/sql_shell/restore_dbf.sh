rman target / log /oracle/backup/log/rman_full.log append <<EOF
run{
sql 'alter database datafile 7 offline';
restore datafile 7;
recover datafile 7;
sql 'alter database datafile 7 online';
}
EOF