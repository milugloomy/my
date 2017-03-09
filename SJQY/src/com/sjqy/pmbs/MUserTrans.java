package com.sjqy.pmbs;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dataUtil.BATCHSQL;
import com.dataUtil.DataUtil;
import com.sjqy.common.DataMigrationAction;
import com.sjqy.common.TransObject;
import com.sjqy.query.DeptService;
import com.sjqy.query.LimitService;
import com.sjqy.query.ParamMapService;

//@Service("MUser")
public class MUserTrans extends DataMigrationAction {
	
	@Autowired
	private DeptService deptService;
	@Autowired
	private LimitService limitService;
	@Autowired
	private ParamMapService paramMapService;
	@Override
	public void execute(Map<String,Object> context){
		List<Map> list=(List<Map>) context.get("List");
		
		for(int i=0;i<list.size();i++){
			ArrayList<TransObject> transList=new ArrayList<TransObject>();
			
			String cifSeq=(String) list.get(i).get("CIFSEQ");
			String cifNo=(String) list.get(i).get("CIFNO");
			String newCif=(String) list.get(i).get("NEWCIF");
			
			Map oldMUserMap=(Map) sqlMapMOBILE.queryForObject("mobile.queryOldMUser", cifNo);
			//组建客户信息
			String idType=paramMapService.getIdType((String)oldMUserMap.get("CERT_TYPE"));
			String idNo=(String) oldMUserMap.get("CERT_NO");
			String mchPhone=(String) oldMUserMap.get("MOBILE");//渠道手机号
			if(mchPhone!=null) mchPhone=mchPhone.trim();
			
			String cifName=(String) oldMUserMap.get("CUST_NAME");
			if(cifName==null&&cifNo.length()==19)
				cifName=(String)sqlMapMOBILE.queryForObject("mobile.queryCifName", DataUtil.get15CifNo(cifNo));
			if(cifName==null){
				log.error("客户号为"+cifNo+"的客户名不存在");
				cifName=idNo;
			}
			
			String branCode=(String)oldMUserMap.get("REG_BRAN_CODE");
			String cifDeptSeq="9999";
			if(branCode==null){
				log.error("客户号为"+cifNo+"的机构不存在");
			}else{
				Map userDept=deptService.queryDeptById(branCode);
				if(userDept==null){
					log.error("Dept表缺失deptId为"+branCode+"的映射关系");
				}else
					cifDeptSeq=String.valueOf(userDept.get("DEPTSEQ"));
			}
			String regType=(String)oldMUserMap.get("REG_TYPE");
			String cifState=paramMapService.getCifState(regType);
			String userId=(String)oldMUserMap.get("APP_NAME");
			if(userId==null)
				userId=cifNo;
			
			String crmNo=(String)oldMUserMap.get("INTRODUCER");//介绍人柜员号
			String gender=paramMapService.getGender(idNo);
			Date openDate=(Date)oldMUserMap.get("REG_DATE");
			if(openDate==null){//取开卡时间
				openDate=(Date) this.sqlMapMOBILE.queryForObject("mobile.queryCardRegDate",cifNo);
			}
			Timestamp openTime=new Timestamp(openDate.getTime());
			
			String mobileSignType="S"+(String)oldMUserMap.get("REG_ADDR");//签约途径 MobileSignType.java
			String reg_oper_no=(String)oldMUserMap.get("REG_OPER_NO");//签约柜员号
			if(reg_oper_no!=null) reg_oper_no=reg_oper_no.trim();
			
			//组建用户信息 
			String password=(String) oldMUserMap.get("PWD");
			Date lastLoginDate=(Date) oldMUserMap.get("LAST_DATE");
			Timestamp lastLoginTime=lastLoginDate==null?null:new Timestamp(lastLoginDate.getTime());
			
			if("Y".equals(newCif)){//新增客户
				String corePhone=(String) oldMUserMap.get("CUST_PHONE");//核心手机号
				if(corePhone==null&&cifNo.length()==19)
					corePhone=(String)sqlMapMOBILE.queryForObject("mobile.queryCifPhone", DataUtil.get15CifNo(cifNo));
				if(corePhone==null){
					log.error("客户号为"+cifNo+"的核心手机号不存在");
					corePhone=mchPhone;
				}
				String userSeq=(String) sqlMapECIF.queryForObject("ecif.queryUserSeq");
				transList.add(new TransObject(insertECCIF,new Object[]{
						cifSeq,cifDeptSeq,cifState,cifName,cifDeptSeq,openTime
				}));
				transList.add(new TransObject(insertECCIFMCH,new Object[]{
						cifSeq,crmNo,cifDeptSeq,openTime,cifState,cifDeptSeq
				}));
				transList.add(new TransObject(insertECCIFID,new Object[]{
						cifSeq,idType,idNo
				}));
				transList.add(new TransObject(insertECEXTCIFNO,new Object[]{
						cifSeq,cifNo
				}));
				transList.add(new TransObject(insertECCIFPRODSET,new Object[]{
						cifSeq,"手机银行功能组",cifDeptSeq,openTime
				}));
				transList.add(new TransObject(insertECPERS,new Object[]{
						cifSeq,gender
				}));
				transList.add(new TransObject(insertECCIFRULE,new Object[]{
						cifSeq,"MobileSignType",mobileSignType
				}));
				if(reg_oper_no!=null && !"".equals(reg_oper_no)){
					transList.add(new TransObject(insertECCIFRULE,new Object[]{
							cifSeq,"CreateEmployeeNo",reg_oper_no
					}));
				}
				BigDecimal lmtPerTrs=(BigDecimal)oldMUserMap.get("MAX_DEBIT_AMT");
				//单笔限额若为0 则用默认限额
				if(lmtPerTrs.compareTo(new BigDecimal(0))==0)
					lmtPerTrs=(BigDecimal)limitService.queryCifLimit("LimitPerTrs","PMBS").get("LIMITAMOUNT");
				transList.add(new TransObject(insertECCIFLIMIT,new Object[]{
						cifSeq,"LimitPerTrs",lmtPerTrs,cifDeptSeq,openTime
				}));
				
				BigDecimal lmtPerDay=(BigDecimal)oldMUserMap.get("MAX_DEBIT_DAYSUM");
				//日累计限额若为0 则用默认限额
				if(lmtPerDay.compareTo(new BigDecimal(0))==0)
					lmtPerDay=(BigDecimal) limitService.queryCifLimit("LimitPerDay","PMBS").get("LIMITAMOUNT");
				transList.add(new TransObject(insertECCIFLIMIT,new Object[]{
						cifSeq,"LimitPerDay",lmtPerDay,cifDeptSeq,openTime
				}));
				transList.add(new TransObject(insertECCIFTEL,new Object[]{
						cifSeq,corePhone,openTime
				}));
			
				transList.add(new TransObject(insertECUSR,new Object[]{
						userSeq,cifSeq,cifName,password,userId,idType,idNo,corePhone,
						cifState,corePhone,openDate,gender,cifDeptSeq
				}));
				transList.add(new TransObject(insertECUSRMCH,new Object[]{
						userSeq,userId,password,cifState,lastLoginTime,mchPhone,openTime,cifDeptSeq
				}));
				if(lastLoginTime!=null && !"".equals(lastLoginTime)){
					transList.add(new TransObject(insertECUSRRULE,new Object[]{
							userSeq,"PMBS.FristLogin","1"
					}));
				}
				//专业版 第二位不为0
				if(regType.charAt(1)!='0'){
					transList.add(new TransObject(insertECUSRAUTHMOD,new Object[]{
							userSeq,"O",cifDeptSeq
					}));
					transList.add(new TransObject(insertECUSRAUTHMODMCH,new Object[]{
							userSeq,"O",cifDeptSeq
					}));
				}
				//登录类型 全设成T
				transList.add(new TransObject(insertECUSRLOGINTYPE,new Object[]{
						userSeq,"T",userId,cifDeptSeq,openTime
				}));
			}else{//客户已存在，新增手机银行渠道
				String userSeq=(String)this.sqlMapECIF.queryForObject("ecif.queryUserSeqByCifSeq",cifSeq);
		
				transList.add(new TransObject(insertECCIFMCH,new Object[]{
						cifSeq,crmNo,cifDeptSeq,openTime,cifState,cifDeptSeq
				}));
				transList.add(new TransObject(insertECCIFPRODSET,new Object[]{
						cifSeq,"手机银行功能组",cifDeptSeq,openTime
				}));
				transList.add(new TransObject(insertECCIFRULE,new Object[]{
						cifSeq,"MobileSignType",mobileSignType
				}));
				if(reg_oper_no!=null && !"".equals(reg_oper_no)){
					transList.add(new TransObject(insertECCIFRULE,new Object[]{
							cifSeq,"CreateEmployeeNo",reg_oper_no
					}));
				}
				BigDecimal lmtPerTrs=(BigDecimal)oldMUserMap.get("MAX_DEBIT_AMT");
				//单笔限额若为0 则用默认限额
				if(lmtPerTrs.compareTo(new BigDecimal(0))==0)
					lmtPerTrs=(BigDecimal)limitService.queryCifLimit("LimitPerTrs","PMBS").get("LIMITAMOUNT");
				transList.add(new TransObject(insertECCIFLIMIT,new Object[]{
						cifSeq,"LimitPerTrs",lmtPerTrs,cifDeptSeq,openTime
				}));
				BigDecimal lmtPerDay=(BigDecimal)oldMUserMap.get("MAX_DEBIT_DAYSUM");
				//日累计限额若为0 则用默认限额
				if(lmtPerDay.compareTo(new BigDecimal(0))==0)
					lmtPerDay=(BigDecimal) limitService.queryCifLimit("LimitPerDay","PMBS").get("LIMITAMOUNT");
				transList.add(new TransObject(insertECCIFLIMIT,new Object[]{
						cifSeq,"LimitPerDay",lmtPerDay,cifDeptSeq,openTime
				}));	
				
				transList.add(new TransObject(insertECUSRMCH,new Object[]{
						userSeq,userId,password,cifState,lastLoginTime,mchPhone,openTime,cifDeptSeq
				}));
				if(lastLoginTime!=null && !"".equals(lastLoginTime)){
					transList.add(new TransObject(insertECUSRRULE,new Object[]{
							userSeq,"PMBS.FristLogin","1"
					}));
				}
				//专业版 第二位不为0
				if(regType.charAt(1)!='0'){
					transList.add(new TransObject(insertECUSRAUTHMODMCH,new Object[]{
							userSeq,"O",cifDeptSeq
					}));
				}
				//登录类型 全设成T
				transList.add(new TransObject(insertECUSRLOGINTYPE,new Object[]{
						userSeq,"T",userId,cifDeptSeq,openTime
				}));
				
			}
			transList.add(new TransObject(BATCHSQL.updateMUserTmpData,new Object[]{
					"OK",cifSeq
			}));
			
			this.batchAction.executeSingleTrans(sqlMapECIF, transList);
		}
	}
	
	//客户表
	private final String insertECCIF="insert into ECCIF"
			+ "(CIFSEQ,CIFDEPTSEQ,CIFSTATE,CIFNAME,CREATEDEPTSEQ,CREATETIME,MODULEID,CIFLEVEL,"
			+ " CIFCONTROL,CIFMONITOR,CIFEXEMPT,CIFLOANFLG,CIFFINVIPFLG,TRANSFERTYPE)"
			+ "values(?,?,?,?,?,?,'per','PCOM','0','0','0','0','0','O')";
	//客户渠道开通表
	private final String insertECCIFMCH="insert into ECCIFMCH"
			+ "(CIFSEQ,CRMNO,OPENDEPTSEQ,OPENTIME,CMCHANNESTATE,CREATEDEPTSEQ,MCHANNELID)"
			+ "values(?,?,?,?,?,?,'PMBS')";
	//客户证件表
	private final String insertECCIFID="insert into ECCIFID"
			+ "(CIFSEQ,IDTYPE,IDNO,PRIMARYFLAG)"
			+ "values(?,?,?,'Y')";
	//银行客户信息表
	private final String insertECEXTCIFNO="insert into ECEXTCIFNO"
			+ "(CIFSEQ,CIFNO,CIFNOTYPE)"
			+ "values(?,?,'C')";
	//客户级限额表
	private final String insertECCIFLIMIT="insert into ECCIFLIMIT"
			+ "(CIFSEQ,LIMITTYPE,LMTAMT,CREATEDEPTSEQ,CREATETIME,MCHANNELID,LOGINTYPE,"
			+ " PAYERACNO,PAYEEACNO,CURRENCY,CIFLEVEL,PRDID,LMTLEVEL)"
			+ "values(?,?,?,?,?,'*','U','-','-','CNY','?','NT','1')";
	//功能组
	private final String insertECCIFPRODSET="insert into ECCIFPRODSET"
			+ "(CIFSEQ,PRDSETID,CREATEDEPTSEQ,CREATETIME,MCHANNELID)"
			+ "values(?,?,?,?,'PMBS')";
	//电子银行个人客户信息表
	private final String insertECPERS="insert into ECPERS (CIFSEQ,GENDER)"
			+ "values(?,?)";
	//电子客户规则表
	private final String insertECCIFRULE="insert into ECCIFRULE "
			+ "(CIFSEQ,MODULEID,RULENAMESPACE,RULEID,RULETYPE,RULEDEF)"
			+ "values(?,'per','#',?,'TrsDef',?)";
	private final String insertECCIFTEL="insert into ECCIFTEL "
			+ "(CIFSEQ,TELTYPE,TELNO,CREATETIME,TELAUTHFLG)"
			+ "values(?,'M',?,?,'Y')";

	//电子银行用户表
	private final String insertECUSR="insert into ECUSR"
			+ "(USERSEQ,CIFSEQ,USERNAME,PASSWORD,USERID,IDTYPE,IDNO,PHONE,"
			+ " USERSTATE,MOBILEPHONE,OPENDATE,GENDER,CREATEDEPTSEQ)"
			+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
	//用户渠道表
	private final String insertECUSRMCH="insert into ECUSRMCH "
			+ "(USERSEQ,USERID,PASSWORD,USERMCHSTATE,LASTLOGINTIME,"
			+ " MOBILEPHONE,CREATETIME,CREATEDEPTSEQ,MCHANNELID)"
			+ "values(?,?,?,?,?,?,?,?,'PMBS')";
	//用户规则（判断用户是否首次登陆，首次登陆需要改密码，不是首次不需要改密码）
	private final String insertECUSRRULE="insert into ECUSRRULE "
			+ "(USERSEQ,RULEID,RULEDEF,MODULEID,RULENAMESPACE,RULETYPE)"
			+ "values(?,?,?,'per','#','TrsDef')";
	//电子银行用户认证信息表
	private final String insertECUSRLOGINTYPE="insert into ECUSRLOGINTYPE "
			+ "(USERSEQ,LOGINTYPE,USERID,CREATEDEPTSEQ,CREATETIME,"
			+ " LOGINTYPESTATE,WRONGPASSCOUNT,MCHANNELID)"
			+ "values(?,?,?,?,?,'N','0','PMBS')";
	//用户认证方式
	private final String insertECUSRAUTHMOD="insert into ECUSRAUTHMOD "
			+ "(USERSEQ,AUTHMOD,CREATEDEPTSEQ,AUTHSTATE) "
			+ "values(?,?,?,'N')";

	private final String insertECUSRAUTHMODMCH="insert into ECUSRAUTHMODMCH "
			+ "(USERSEQ,AUTHMOD,CREATEDEPTSEQ,MCHANNELID) "
			+ "values(?,?,?,'PMBS')";

}
