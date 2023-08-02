#!/usr/bin/bash
#java -javaagent:lib/jacocoagent.jar=address=*,port=36320,destfile=report/jacoco-it.exec,output=tcpserver -jar ../../target/fastfoot-0.0.1-SNAPSHOT.jar;
java -javaagent:lib/jacocoagent.jar=destfile=report/jacoco-it.exec,excludes=org.jacoco.* -jar ../../target/fastfoot-0.0.1-SNAPSHOT.jar;

