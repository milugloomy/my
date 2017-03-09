rman target / log /oracle/backup/log/rman_full.log append <<EOF
run
{allocate channel c1 type disk;
allocate channel c2 type disk;
backup database filesperset 4 format '/oracle/backup/full_%d_%T_%s_%p';
sql 'alter system archive log current';
backup archivelog all format '/oracle/backup/arch_%d_%T_%s_%p' delete input;
backup current controlfile format '/oracle/backup/ctl_%d_%T_%s_%p';
release channel c1;
release channel c2;
}
EOF
