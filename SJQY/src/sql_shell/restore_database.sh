--在mount下执行
rman target / log /oracle/backup/log/rman_full.log append <<EOF
run{
restore database;
recover database;
}
EOF