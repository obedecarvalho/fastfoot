#!/bin/bash
#--sem remover jogadores e clubes
echo '	*** VACUUM ***'
psql -h 127.0.0.1 -W -d fastfoot -U fastfoot -c "vacuum";
echo '	*** ANALIZE ***'
psql -h 127.0.0.1 -W -d fastfoot -U fastfoot -c "analyze;";
echo '	*** REINDEX ***'
psql -h 127.0.0.1 -W -d fastfoot -U fastfoot -c "REINDEX DATABASE fastfoot";
