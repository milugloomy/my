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
import com.sjqy.query.DeptService;
import com.sjqy.query.ParamMapService;

@Service("MUser")
public class MUser extends DataMigrationAction {
	
	@Autowired
	private DeptService deptService;
	@Autowired
	private ParamMapService paramMapService;
	@Override
	public void execute(Map<String,Object> context){
		List<Map> list=(List<Map>) context.get("List");
		
		ArrayList<Object[]> eccifList=new ArrayList<Object[]>();
		ArrayList<Object[]> eccifMchList=new ArrayList<Object[]>();
		ArrayList<Object[]> eccifIdList=new ArrayList<Object[]>();
		ArrayList<Object[]> ecextCifNoList=new ArrayList<Object[]>();
		ArrayList<Object[]> eccifLimitList=new ArrayList<Object[]>();
		ArrayList<Object[]> eccifProdSetList=new ArrayList<Object[]>();
		ArrayList<Object[]> ecPersList=new ArrayList<Object[]>();
		ArrayList<Object[]> eccifRuleList=new ArrayList<Object[]>();
		ArrayList<Object[]> eccifTelList=new ArrayList<Object[]>();
		ArrayList<Object[]> eccifOutTransferSwitchList=new ArrayList<Object[]>();
		
		ArrayList<Object[]> ecusrList=new ArrayList<Object[]>();
		ArrayList<Object[]> ecusrMchList=new ArrayList<Object[]>();
		ArrayList<Object[]> ecusrRuleList=new ArrayList<Object[]>();
		ArrayList<Object[]> ecusrLoginTypeList=new ArrayList<Object[]>();
		ArrayList<Object[]> ecusrAuthModList=new ArrayList<Object[]>();
		ArrayList<Object[]> ecusrAuthModMchList=new ArrayList<Object[]>();
		
		for(int i=0;i<list.size();i++){
			
			String cifSeq=(String) list.get(i).get("CIFSEQ");
			String cifNo=(String) list.get(i).get("CIFNO");
			String newCif=(String) list.get(i).get("NEWCIF");
			
			Map oldMUserMap=(Map) sqlMapMOBILE.queryForObject("mobile.queryOldMUser", cifNo);
			if(oldMUserMap==null){
				log.error("客户号为"+cifNo+"的客户不存在");
				continue;
			}
			//组建客户信息
			String idType=paramMapService.getIdType((String)oldMUserMap.get("CERT_TYPE"));
			String idNo=(String) oldMUserMap.get("CERT_NO");
			String mchPhone=(String) oldMUserMap.get("MOBILE");//渠道手机号
			if(mchPhone!=null) mchPhone=mchPhone.trim();
			
			String corePhone=(String) oldMUserMap.get("CUST_PHONE");//核心手机号
			if(corePhone==null&&cifNo.length()==19)
				corePhone=(String)sqlMapMOBILE.queryForObject("mobile.queryCifPhone", DataUtil.get15CifNo(cifNo));
			if(corePhone==null){
				log.error("客户号为"+cifNo+"的核心手机号不存在");
//				corePhone=mchPhone;
			}
			
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
				userId=idNo;
			
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
			
			String userSeq=null;
			if("Y".equals(newCif)){//新增客户
				userSeq=(String) sqlMapECIF.queryForObject("ecif.queryUserSeq");
				eccifList.add(new Object[]{
						cifSeq,cifDeptSeq,cifState,cifName,cifDeptSeq,openTime
				});
				eccifIdList.add(new Object[]{
						cifSeq,idType,idNo
				});
				ecextCifNoList.add(new Object[]{
						cifSeq,cifNo
				});
				ecPersList.add(new Object[]{
						cifSeq,gender
				});
				eccifTelList.add(new Object[]{
						cifSeq,corePhone,openTime
				});
			
				ecusrList.add(new Object[]{
						userSeq,cifSeq,cifName,password,userId,idType,idNo,corePhone,
						cifState,corePhone,openDate,gender,cifDeptSeq
				});
			}else{//客户已存在，新增手机银行渠道
				userSeq=(String)this.sqlMapECIF.queryForObject("ecif.queryUserSeqByCifSeq",cifSeq);
				if(userSeq==null){//存在客户，不存在用户（潜在客户）
					userSeq=(String) sqlMapECIF.queryForObject("ecif.queryUserSeq");
					//用户信息
					ecusrList.add(new Object[]{
							userSeq,cifSeq,cifName,password,userId,idType,idNo,corePhone,
							cifState,corePhone,openDate,gender,cifDeptSeq
					});
				}
			}
			
			eccifMchList.add(new Object[]{
					cifSeq,crmNo,cifDeptSeq,openTime,cifState,cifDeptSeq
			});
			eccifProdSetList.add(new Object[]{
					cifSeq,"手机银行功能组",cifDeptSeq,openTime
			});
			eccifRuleList.add(new Object[]{
					cifSeq,"MobileSignType",mobileSignType
			});
			if(reg_oper_no!=null && !"".equals(reg_oper_no)){
				eccifRuleList.add(new Object[]{
						cifSeq,"CreateEmployeeNo",reg_oper_no
				});
			}
			BigDecimal lmtPerDay=(BigDecimal)oldMUserMap.get("MAX_DEBIT_DAYSUM");
			//日累计限额若为0 则用默认限额
//			if(lmtPerDay.compareTo(new BigDecimal(0))==0)
//				lmtPerDay=(BigDecimal) limitService.queryCifLimit("LimitPerDay","PMBS").get("LIMITAMOUNT");
			eccifLimitList.add(new Object[]{
					cifSeq,"LimitPerDay",lmtPerDay,cifDeptSeq,openTime
			});
			
			BigDecimal lmtPerTrs=(BigDecimal)oldMUserMap.get("MAX_DEBIT_AMT");
			//单笔限额若为0 则用日累计限额
			if(lmtPerTrs.compareTo(new BigDecimal(0))==0)
				lmtPerTrs=lmtPerDay;
			eccifLimitList.add(new Object[]{
					cifSeq,"LimitPerTrs",lmtPerTrs,cifDeptSeq,openTime
			});
			
			ecusrMchList.add(new Object[]{
					userSeq,userId,password,cifState,lastLoginTime,mchPhone,openTime,cifDeptSeq
			});
			if(lastLoginTime!=null && !"".equals(lastLoginTime)){
				ecusrRuleList.add(new Object[]{
						userSeq,"PMBS.FristLogin","1"
				});
			}
			//默认迁移客户插入AuthMod=Q
			ecusrAuthModList.add(new Object[]{userSeq,"Q",cifDeptSeq,userSeq,"Q"});
			//默认迁移客户插入AuthMod=Q
			ecusrAuthModMchList.add(new Object[]{userSeq,"Q",cifDeptSeq});

			//专业版 第二位不为0 && 核心手机号和手机银行手机号一致
			if(regType.charAt(1)!='0' && corePhone!=null && corePhone.equals(mchPhone)){
				ecusrAuthModList.add(new Object[]{
						userSeq,"O",cifDeptSeq,userSeq,"O"
				});
				ecusrAuthModMchList.add(new Object[]{
						userSeq,"O",cifDeptSeq
				});
				eccifOutTransferSwitchList.add(new Object[]{
						cifSeq
				});
			}
			//登录类型 全设成T
			ecusrLoginTypeList.add(new Object[]{
					userSeq,"T",userId,cifDeptSeq,openTime
			});
		}
		
		this.batchAction.excuteBatch(sqlMapECIF, insertECCIF, eccifList);
		this.batchAction.excuteBatch(sqlMapECIF, insertECCIFMCH, eccifMchList);
		this.batchAction.excuteBatch(sqlMapECIF, insertECCIFID, eccifIdList);
		this.batchAction.excuteBatch(sqlMapECIF, insertECEXTCIFNO, ecextCifNoList);
		this.batchAction.excuteBatch(sqlMapECIF, insertECCIFLIMIT, eccifLimitList);
		this.batchAction.excuteBatch(sqlMapECIF, insertECCIFPRODSET, eccifProdSetList);
		this.batchAction.excuteBatch(sqlMapECIF, insertECPERS, ecPersList);
		this.batchAction.excuteBatch(sqlMapECIF, insertECCIFRULE, eccifRuleList);
		this.batchAction.excuteBatch(sqlMapECIF, insertECCIFTEL, eccifTelList);
		this.batchAction.excuteBatch(sqlMapECIF, insertECCIFOUTTRANSFERSWITCH, eccifOutTransferSwitchList);
		
		this.batchAction.excuteBatch(sqlMapECIF, insertECUSR, ecusrList);
		this.batchAction.excuteBatch(sqlMapECIF, insertECUSRMCH, ecusrMchList);
		this.batchAction.excuteBatch(sqlMapECIF, insertECUSRRULE, ecusrRuleList);
		this.batchAction.excuteBatch(sqlMapECIF, insertECUSRLOGINTYPE, ecusrLoginTypeList);
		this.batchAction.excuteBatch(sqlMapECIF, insertECUSRAUTHMOD, ecusrAuthModList);
		this.batchAction.excuteBatch(sqlMapECIF, insertECUSRAUTHMODMCH, ecusrAuthModMchList);
		
		this.batchAction.excuteBatch(this.getSqlMapTMP(), BATCHSQL.updateMUserTmpData, DataUtil.tmpMList(list,"OK"));
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
			+ "values(?,?,?,?,?,'PMBS','-','-','-','CNY','?','NT','1')";
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
	private final String insertECCIFOUTTRANSFERSWITCH="insert into ECCIFOUTTRANSFERSWITCH "
			+ "(CIFSEQ,MCHANNELID,OUTWAY,OUTFLAG)"
			+ "values(?,'PMBS','Q','1')";

	//电子银行用户表
	private final String insertECUSR="insert into ECUSR"
			+ "(USERSEQ,CIFSEQ,USERNAME,PASSWORD,USERID,IDTYPE,IDNO,PHONE,"
			+ " USERSTATE,MOBILEPHONE,OPENDATE,GENDER,CREATEDEPTSEQ)"
			+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
	//用户渠道表
	private final String insertECUSRMCH="insert into ECUSRMCH "
			+ "(USERSEQ,USERID,PASSWORD,USERMCHSTATE,LASTLOGINTIME,"
			+ " MOBILEPHONE,CREATETIME,CREATEDEPTSEQ,MCHANNELID,LIMITWRONGPASS)"
			+ "values(?,?,?,?,?,?,?,?,'PMBS',0)";
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
	private final String insertECUSRAUTHMOD="insert into ecif.ECUSRAUTHMOD "
			+ "(USERSEQ,AUTHMOD,CREATEDEPTSEQ,AUTHSTATE) "
			+ "	select ?,?,?,'N' from dual where not exists("
			+ "		select * from ecif.ECUSRAUTHMOD where USERSEQ=? and AUTHMOD=?"
			+ ")";

	private final String insertECUSRAUTHMODMCH="insert into ECUSRAUTHMODMCH "
			+ "(USERSEQ,AUTHMOD,CREATEDEPTSEQ,MCHANNELID) "
			+ "values(?,?,?,'PMBS')";

}
 