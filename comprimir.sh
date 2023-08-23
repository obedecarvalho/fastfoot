#!/bin/bash
[ ! -z $1 ] && zip --symlinks -r fastfoot$(date +'-%Y%m%d-%H%M')-full.zip .git/ .mvn/ .settings/ outros/ src/ .gitignore mvnw mvnw.cmd pom.xml comprimir.sh
[ -z $1 ] && zip --symlinks -r fastfoot$(date +'-%Y%m%d-%H%M').zip .mvn/ .settings/ outros/ src/ .gitignore mvnw mvnw.cmd pom.xml comprimir.sh -x outros/img/\* src/main/resources/static/img/teams/\* outros/teste-cobertura/report/\*
