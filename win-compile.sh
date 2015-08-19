#!/bin/sh
CP="lib/*;classes"
SP=src/java/

/bin/rm -f nhz.jar
/bin/rm -f nxtservice.jar
/bin/rm -rf classes
/bin/mkdir -p classes/

javac -encoding utf8 -sourcepath "${SP}" -classpath "${CP}" -d classes/ src/java/nxt/*.java src/java/nxt/*/*.java || exit 1

echo "Horizon class files compiled successfully"
