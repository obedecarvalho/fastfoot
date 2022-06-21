#!/bin/bash
zip --symlinks -r fastfoot$(date +'-%Y%m%d-%H%M').zip .git/ .mvn/ .settings/ outros/ src/ .gitignore mvnw mvnw.cmd pom.xml comprimir.sh
