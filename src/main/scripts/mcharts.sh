#!/bin/sh

PRODUCT_PATH=/home/endymion/dev/main/mcharts/target

MAIN_CLASS=com.redorb.mcharts.MCharts

MAIN_JAR=`ls -1 ${PRODUCT_PATH}/mcharts-*.jar | head -1`

# add all jar inside lib directory to the classpath

LOCAL_CLASSPATH="$CLASSPATH"

for LOCAL_JAR in `ls -1 ${PRODUCT_PATH}/lib/*.jar`
do
	LOCAL_CLASSPATH="${LOCAL_JAR}:${LOCAL_CLASSPATH}"
done 

java -cp "${LOCAL_CLASSPATH}:${MAIN_JAR}:${PRODUCT_PATH}/classes" ${MAIN_CLASS}
