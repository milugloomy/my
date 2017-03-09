args=MUser

LIB=lib
UIBSPATH=${LIB}/commons-collections.jar
UIBSPATH=${LIB}/commons-dbcp.jar:${UIBSPATH}
UIBSPATH=${LIB}/commons-logging.jar:${UIBSPATH}
UIBSPATH=${LIB}/commons-pool.jar:${UIBSPATH}
UIBSPATH=${LIB}/ibatis-2.3.0.677.jar:${UIBSPATH}
UIBSPATH=${LIB}/log4j-1.2.8.jar:${UIBSPATH}
UIBSPATH=${LIB}/ojdbc14.jar:${UIBSPATH}
UIBSPATH=${LIB}/org.springframework.asm-3.0.6.RELEASE.jar:${UIBSPATH}
UIBSPATH=${LIB}/org.springframework.beans-3.0.6.RELEASE.jar:${UIBSPATH}
UIBSPATH=${LIB}/org.springframework.context-3.0.6.RELEASE.jar:${UIBSPATH}
UIBSPATH=${LIB}/org.springframework.core-3.0.6.RELEASE.jar:${UIBSPATH}
UIBSPATH=${LIB}/org.springframework.expression-3.0.6.RELEASE.jar:${UIBSPATH}
UIBSPATH=${LIB}/org.springframework.jdbc-3.0.6.RELEASE.jar:${UIBSPATH}
UIBSPATH=${LIB}/org.springframework.orm-3.0.6.RELEASE.jar:${UIBSPATH}
UIBSPATH=${LIB}/org.springframework.transaction-3.0.6.RELEASE.jar:${UIBSPATH}

UIBSPATH=SJQY.jar:${UIBSPATH}

JAVA_OPTIONS="-Dfile.encoding=UTF-8  -Xms128m -Xmx512m"
DATE=`date +%Y-%m-%d`
LOGFILENAME=log/$args.$DATE.log
java -cp ${UIBSPATH} ${JAVA_OPTIONS} com.sjqy.common.MainEntry $args >> $LOGFILENAME

