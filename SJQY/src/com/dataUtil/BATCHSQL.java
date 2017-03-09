package com.dataUtil;

public class BATCHSQL {
	final public static String updateMUserTmpData=
		"update MTMPDATA set USERFLAG=? where CIFSEQ=?";
	final public static String updateMAccountTmpData=
		"update MTMPDATA set ACCOUNTFLAG=? where CIFSEQ=?";
	final public static String updateMPayeeTmpData=
		"update MTMPDATA set PAYEEFLAG=? where CIFSEQ=?";
	
	final public static String updatePUserTmpData=
		"update PTMPDATA set USERFLAG=? where CIFSEQ=?";
	final public static String updatePAccountTmpData=
		"update PTMPDATA set ACCOUNTFLAG=? where CIFSEQ=?";
	final public static String updatePPayeeTmpData=
		"update PTMPDATA set PAYEEFLAG=? where CIFSEQ=?";
	
	final public static String updateERoleTmpData=
		"update ETMPDATA set ROLEFLAG=? where CIFSEQ=?";
	final public static String updateEUserTmpData=
		"update ETMPDATA set USERFLAG=? where CIFSEQ=?";
	final public static String updateEAccountTmpData=
		"update ETMPDATA set ACCOUNTFLAG=? where CIFSEQ=?";
	final public static String updateEPayeeTmpData=
		"update TMPDATA set PAYEEFLAG=? where CIFSEQ=?";
}
