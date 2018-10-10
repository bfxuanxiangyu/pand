#!/bin/sh

#JAVA_HOME=

PAND_HOME=$(dirname $0)/..

PAND_HOME=`cd "$PAND_HOME"; pwd`

. "$PAND_HOME/bin/env.sh"

PAND_JAVA_OPTS="$PAND_JAVA_OPTS -Dspring.profiles.active=$PROFILE"

PAND_CLASSPATH="$CLASSPATH"

PAND_PIDFILE="$PAND_HOME/bin/pand.pid"

PAND_CONSOLEOUT="$PAND_HOME/logs/console.out"

case `uname` in
    CYGWIN*)
        PAND_CLASSPATH=`cygpath -p -w "$PAND_CLASSPATH"`
        PAND_HOME=`cygpath -p -w "$PAND_HOME"`
    ;;
esac

case $1 in
start)
    echo  -n "Starting pand ... "
    if [ -f "$PAND_PIDFILE" ]; then
      if kill -0 `cat "$PAND_PIDFILE"` > /dev/null 2>&1; then
         echo $command already running as process `cat "$PAND_PIDFILE"`. 
         exit 0
      fi
    fi
    nohup "$JAVA" $JAVA_OPTS $PAND_JAVA_OPTS -cp "$PAND_CLASSPATH" \
        com.weeds.pand.Application >> "$PAND_CONSOLEOUT" 2>&1 < /dev/null &
    if [ $? -eq 0 ]
    then
      if /bin/echo -n $! > "$PAND_PIDFILE"
      then
        sleep 1
        echo STARTED
      else
        echo FAILED TO WRITE PID
        exit 1
      fi
    else
      echo SERVER DID NOT START
      exit 1
    fi
    ;;
stop)
    echo -n "Stopping pand"
    if [ ! -f "$PAND_PIDFILE" ]
    then
      echo "no pand to stop (could not find file $PAND_PIDFILE)"
    else
      stopprocess $(cat "$PAND_PIDFILE")
      rm "$PAND_PIDFILE"
    fi
    exit 0
    ;;
restart)
    shift
    "$0" stop ${@}
    sleep 5
    "$0" start ${@}
    ;;
*)
    echo "Usage: $0 {start|stop|restart}" >&2

esac
