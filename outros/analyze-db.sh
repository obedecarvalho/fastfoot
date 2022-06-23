#!/bin/bash
#--sem remover jogadores e clubes
psql -h 127.0.0.1 -W -d fastfoot -U fastfoot -c "vacuum";
psql -h 127.0.0.1 -W -d fastfoot -U fastfoot -c "analyze;";
psql -h 127.0.0.1 -W -d fastfoot -U fastfoot -c "REINDEX DATABASE fastfoot";
