#!/bin/sh

#JAVA_HOME=/home/test/jdk1.8.0

if [ -x "$JAVA_HOME/bin/java" ]; then
    JAVA="$JAVA_HOME/bin/java"
else
    JAVA=`which java`
fi

if [ ! -x "$JAVA" ]; then
    echo "Could not find any executable java binary. Please install java in your PATH or set JAVA_HOME"
    exit 1
fi

if [ "x$PAND_MIN_MEM" = "x" ]; then
    PAND_MIN_MEM=2g
fi
if [ "x$PAND_MAX_MEM" = "x" ]; then
    PAND_MAX_MEM=4g
fi

if [ "x$PAND_HEAP_SIZE" != "x" ]; then
    PAND_MIN_MEM=$PAND_HEAP_SIZE
    PAND_MAX_MEM=$PAND_HEAP_SIZE
fi

if [ "x${LOG4J_PROP}" = "x" ]
then
    LOG4J_PROP="WARN,LOGFILE"
fi

if [ ! -d "${PAND_HOME}/temp" ]
then
    mkdir "${PAND_HOME}/temp";
fi

JAVA_DIRECT_SIZE=4096m

#JVM
JAVA_OPTS="-server $JAVA_OPTS"
JAVA_OPTS="$JAVA_OPTS -Xms${PAND_MIN_MEM} -Xmx${PAND_MAX_MEM} -XX:MaxDirectMemorySize=${JAVA_DIRECT_SIZE}"
JAVA_OPTS="$JAVA_OPTS -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=256m"
JAVA_OPTS="$JAVA_OPTS -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=${PAND_HOME}\logs\dump.log"

#PATH
JAVA_OPTS="$JAVA_OPTS -Dpath.home=${PAND_HOME}"
JAVA_OPTS="$JAVA_OPTS -Djava.io.tmpdir=${PAND_HOME}/temp"

#DEBUG
#DEBUG_OPTS="-Xdebug -Xrunjdwp:transport=dt_socket,address=8000,server=y,suspend=n"

if [ "x$GC_OPTS" = "x" ]; then
  GC_OPTS="$GC_OPTS -XX:+UseParNewGC"
  GC_OPTS="$GC_OPTS -XX:+UseConcMarkSweepGC"
  GC_OPTS="$GC_OPTS -XX:CMSInitiatingOccupancyFraction=75"
  GC_OPTS="$GC_OPTS -XX:+UseCMSInitiatingOccupancyOnly"
fi

if [ "x$DEBUG_OPTS" != "x" ]; then
  JAVA_OPTS="$JAVA_OPTS $DEBUG_OPTS"
fi

# GC logging options
if [ -n "$GC_LOG_FILE" ]; then
  JAVA_OPTS="$JAVA_OPTS -XX:+PrintGCDetails"
  JAVA_OPTS="$JAVA_OPTS -XX:+PrintGCTimeStamps"
  JAVA_OPTS="$JAVA_OPTS -XX:+PrintGCDateStamps"
  JAVA_OPTS="$JAVA_OPTS -XX:+PrintClassHistogram"
  JAVA_OPTS="$JAVA_OPTS -XX:+PrintTenuringDistribution"
  JAVA_OPTS="$JAVA_OPTS -XX:+PrintGCApplicationStoppedTime"
  JAVA_OPTS="$JAVA_OPTS -Xloggc:$GC_LOG_FILE"
  # Ensure that the directory for the log file exists: the JVM will not create it.
  mkdir -p "`dirname \"$GC_LOG_FILE\"`"
fi

JAVA_OPTS="$JAVA_OPTS $GC_OPTS -XX:+DisableExplicitGC"

# Ensure UTF-8 encoding by default (e.g. filenames)
JAVA_OPTS="$JAVA_OPTS -Dfile.encoding=UTF-8"

# Set base classpath
CLASSPATH="${PAND_HOME}/lib/*"

# function
stopprocess(){
 pid=$1
 kill $pid
while [ ! -z $pid ]
   do
     pid=`ps ax | awk '{ print $1 }' | grep -e ${pid}`
     if [ -z $pid ] ; then
       echo ""
       echo "STOPPED"
     else
       printf '.'
       sleep 1s
     fi
 done
}
