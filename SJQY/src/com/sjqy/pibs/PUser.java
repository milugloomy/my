package com.sjqy.pibs;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

@Service("PUser")
public class PUser extends DataMigrationAction {
	
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
		ArrayList<Object[]> ecusrCertList=new ArrayList<Object[]>();
		for(int i=0;i<list.size();i++){
			String cifSeq=(String) list.get(i).get("CIFSEQ");
			String cifNo=(String) list.get(i).get("CIFNO");
			String newCif=(String) list.get(i).get("NEWCIF");
			String idNo=null;
			
			Map oldPUserMap=(Map) sqlMapOLD.queryForObject("old.queryOldPUser", cifNo);
			
			if(oldPUserMap==null){//15位身份证
				oldPUserMap=(Map) sqlMapOLD.queryForObject("old.queryOldPUser", DataUtil.get15CifNo(cifNo));
				idNo=cifNo.substring(1);//15位身份证证件号取核心客户号后18位
			}
			//组建客户信息
			String branCode=((String)oldPUserMap.get("OPEN_BRAN_CODE")).trim();
			Map userDept=deptService.queryDeptById(branCode);
			//若根据userDept找不到机构则deptseq设为分行一
			String cifDeptSeq=userDept==null?"9999":String.valueOf(userDept.get("DEPTSEQ"));
			
			String match_status=(String)oldPUserMap.get("MATCH_STATUS");
			String cifState=paramMapService.getCifState(match_status);
			
			String cifName=(String) oldPUserMap.get("NAME");
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			Date openDate = null;
			try {
				long timeMillis=sdf.parse((String)oldPUserMap.get("APPLY_DATE")).getTime();
				openDate = new Date(timeMillis);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			Timestamp openTime=openDate==null?null:new Timestamp(openDate.getTime());
			String idType=paramMapService.getIdType((String)oldPUserMap.get("CERT_TYPE"));
			if(idNo==null)//不为null表示18位身份证
				idNo=(String) oldPUserMap.get("CERT_NO");
			String gender=paramMapService.getGender(idNo);
			String oper_no=((BigDecimal)oldPUserMap.get("OPER_NO")).toString();//签约柜员号	
			
			//组建用户信息 
			String userId=idNo;
			String password=(String) oldPUserMap.get("PASSWORD");
			String corePhone=(String) oldPUserMap.get("CUST_PHONE");;//核心手机号
			String phone=(String) oldPUserMap.get("MOBILE");//渠道手机号
			if(phone!=null)phone=phone.trim();
			String email=(String) oldPUserMap.get("EMAIL");
			if(email!=null)email=email.trim();
			
			String userSeq=null;
			if("Y".equals(newCif)){//新增客户
				userSeq=(String) sqlMapECIF.queryForObject("ecif.queryUserSeq");
				//客户信息
				eccifList.add(new Object[]{
						cifSeq,cifDeptSeq,cifState,cifName,cifDeptSeq
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
				
				//用户信息
				ecusrList.add(new Object[]{
						userSeq,cifSeq,cifName,password,userId,idType,idNo,corePhone,email,
						cifState,corePhone,openTime,gender,cifDeptSeq
				});
			}else{
				userSeq=(String)this.sqlMapECIF.queryForObject("ecif.queryUserSeqByCifSeq",cifSeq);
				if(userSeq==null){//存在客户，不存在用户（潜在客户）
					userSeq=(String) sqlMapECIF.queryForObject("ecif.queryUserSeq");
					//用户信息
					ecusrList.add(new Object[]{
							userSeq,cifSeq,cifName,password,userId,idType,idNo,corePhone,email,
							cifState,corePhone,openTime,gender,cifDeptSeq
					});
				}
			}
			eccifMchList.add(new Object[]{
					cifSeq,cifDeptSeq,openTime,cifState,cifDeptSeq
			});
			/*eccifRuleList.add(new Object[]{
					cifSeq,"CreateEmployeeNo",oper_no
			});*/
			
			//专业版才有限额
			BigDecimal lmtPerDay=(BigDecimal)oldPUserMap.get("MAX_DEBIT_AMT");
			//日累计限额若为0 则用默认限额
			if(lmtPerDay==null || lmtPerDay.compareTo(new BigDecimal(0))==0)
				lmtPerDay=new BigDecimal(0);
			eccifLimitList.add(new Object[]{
					cifSeq,"LimitPerDay",lmtPerDay,cifDeptSeq,openTime
			});

			BigDecimal lmtPerTrs=(BigDecimal)oldPUserMap.get("PER_DEBIT_AMT");
			//单笔限额若为0 则用默认限额
			if(lmtPerTrs==null || lmtPerTrs.compareTo(new BigDecimal(0))==0)
				lmtPerTrs=lmtPerDay;
			eccifLimitList.add(new Object[]{
					cifSeq,"LimitPerTrs",lmtPerTrs,cifDeptSeq,openTime
			});
				
			eccifProdSetList.add(new Object[]{
					cifSeq,"个人网银功能组",cifDeptSeq,openTime
			});
			
			ecusrMchList.add(new Object[]{
					userSeq,userId,password,cifState,phone,openTime,cifDeptSeq
			});
			ecusrRuleList.add(new Object[]{
					userSeq,"PIBS.FristLogin","1"
			});
			
			if(match_status.charAt(1)!='0'){//专业版
				eccifOutTransferSwitchList.add(new Object[]{
						cifSeq
				});
				ecusrAuthModList.add(new Object[]{//SQL判断有就不插入
						userSeq,"U",cifDeptSeq,userSeq,"U"
				});
				ecusrAuthModMchList.add(new Object[]{
						userSeq,"U",cifDeptSeq
				});
			}
			//默认迁移客户插入AuthMod=Q
			ecusrAuthModList.add(new Object[]{userSeq,"Q",cifDeptSeq,userSeq,"Q"});//SQL判断有就不插入
			ecusrAuthModMchList.add(new Object[]{userSeq,"Q",cifDeptSeq});
			
			//登录类型 全设成 R
			ecusrLoginTypeList.add(new Object[]{
					userSeq,"R",userId,cifDeptSeq,openTime
			});
			
			String certState=paramMapService.getCertState(oldPUserMap.get("ASSUM_STATUS"));
			if(!"not_apply".equals(certState)){
				String usbKeyCN=(String) oldPUserMap.get("CN_NO");
				if(usbKeyCN!=null)usbKeyCN=usbKeyCN.trim();
				String certDN=(String) oldPUserMap.get("DN_NO");
				if(certDN==null || certDN.equals(""))
					continue;
				Date expireDate=(Date) oldPUserMap.get("user_closedate");
				ecusrCertList.add(new Object[]{
						certDN,usbKeyCN,userSeq,cifDeptSeq,openTime,openTime,expireDate,certState
				});
			}
			
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
		this.batchAction.excuteBatch(sqlMapECIF, insertECUSRCERT, ecusrCertList);
		
		this.batchAction.excuteBatch(this.getSqlMapTMP(), BATCHSQL.updatePUserTmpData, DataUtil.tmpPList(list,"OK"));
	}
	//客户表
	private final String insertECCIF="insert into ECCIF"
			+ "(CIFSEQ,CIFDEPTSEQ,CIFSTATE,CIFNAME,CREATEDEPTSEQ,MODULEID,CIFLEVEL,"
			+ " CIFCONTROL,CIFMONITOR,CIFEXEMPT,CIFLOANFLG,CIFFINVIPFLG,TRANSFERTYPE)"
			+ "values(?,?,?,?,?,'per','PCOM','0','0','0','0','0','O')";
	//客户渠道开通表
	private final String insertECCIFMCH="insert into ECCIFMCH"
			+ "(CIFSEQ,OPENDEPTSEQ,OPENTIME,CMCHANNESTATE,CREATEDEPTSEQ,MCHANNELID)"
			+ "values(?,?,?,?,?,'PIBS')";
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
			+ "values(?,?,?,?,?,'PIBS','-','-','-','CNY','?','NT','1')";
	//功能组
	private final String insertECCIFPRODSET="insert into ECCIFPRODSET"
			+ "(CIFSEQ,PRDSETID,CREATEDEPTSEQ,CREATETIME,MCHANNELID)"
			+ "values(?,?,?,?,'PIBS')";
	//电子银行个人客户信息表
	private final String insertECPERS="insert into ECPERS (CIFSEQ,GENDER)"
			+ "values(?,?)";
	private final String insertECCIFRULE="insert into ECCIFRULE "
			+ "(CIFSEQ,MODULEID,RULENAMESPACE,RULEID,RULETYPE,RULEDEF)"
			+ "values(?,'per','#',?,'TrsDef',?)";
	private final String insertECCIFTEL="insert into ECCIFTEL "
			+ "(CIFSEQ,TELTYPE,TELNO,CREATETIME,TELAUTHFLG)"
			+ "values(?,'M',?,?,'Y')";
	private final String insertECCIFOUTTRANSFERSWITCH="insert into ECCIFOUTTRANSFERSWITCH "
			+ "(CIFSEQ,MCHANNELID,OUTWAY,OUTFLAG)"
			+ "values(?,'PIBS','Q','1')";

	//电子银行用户表
	private final String insertECUSR="insert into ECUSR"
			+ "(USERSEQ,CIFSEQ,USERNAME,PASSWORD,USERID,IDTYPE,IDNO,PHONE,"
			+ " EMAIL,USERSTATE,MOBILEPHONE,OPENDATE,GENDER,CREATEDEPTSEQ)"
			+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	//用户渠道表
	private final String insertECUSRMCH="insert into ECUSRMCH "
			+ "(USERSEQ,USERID,PASSWORD,USERMCHSTATE,"
			+ " MOBILEPHONE,CREATETIME,CREATEDEPTSEQ,MCHANNELID)"
			+ "values(?,?,?,?,?,?,?,'PIBS')";
	//用户规则（判断用户是否首次登陆，首次登陆需要改密码，不是首次不需要改密码）
	private final String insertECUSRRULE="insert into ECUSRRULE "
			+ "(USERSEQ,RULEID,RULEDEF,MODULEID,RULENAMESPACE,RULETYPE)"
			+ "values(?,?,?,'per','#','TrsDef')";
	//电子银行用户认证信息表
	private final String insertECUSRLOGINTYPE="insert into ECUSRLOGINTYPE "
			+ "(USERSEQ,LOGINTYPE,USERID,CREATEDEPTSEQ,CREATETIME,"
			+ " LOGINTYPESTATE,WRONGPASSCOUNT,MCHANNELID)"
			+ "values(?,?,?,?,?,'N','0','PIBS')";
	//用户认证方式
	private final String insertECUSRAUTHMOD="insert into ecif.ECUSRAUTHMOD "
			+ "(USERSEQ,AUTHMOD,CREATEDEPTSEQ,AUTHSTATE) "
			+ "	select ?,?,?,'N' from dual where not exists("
			+ "		select * from ecif.ECUSRAUTHMOD where USERSEQ=? and AUTHMOD=?"
			+ ")";

	private final String insertECUSRAUTHMODMCH="insert into ECUSRAUTHMODMCH "
			+ "(USERSEQ,AUTHMOD,CREATEDEPTSEQ,MCHANNELID) "
			+ "values(?,?,?,'PIBS')";
	//usekey用户证书
	private final String insertECUSRCERT="insert into ECUSRCERT "
			+ "(CERTDN,USBKEYSN,USERSEQ,CREATEDEPTSEQ,CERTAPPLYDATE,CREATETIME,EXPIREDATE,"
			+ " ISSUERCACODE,USBKEYFLAG,CERTSTATE,FEETYPE,BATCHFLAG,UPDATENUM,TOKENTYPE)"
			+ "values(?,?,?,?,?,?,?,'CFCA','1',?,'0','0',0,'PC')";

}
