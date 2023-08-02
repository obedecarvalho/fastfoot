#!/usr/bin/bash
#java -jar lib/jacococli.jar dump --address localhost --port 36320 --destfile report/jacoco-it.exec;
#java -jar lib/jacococli.jar report report/jacoco-it.exec --classfiles ../../target/classes/ --sourcefiles ../../src/main/java/ --html report/jacoco-report;
java -jar lib/jacococli.jar report report/jacoco-it.exec --classfiles ../../target/classes/ --sourcefiles ../../src/main/java/ --html report/jacoco-report;

