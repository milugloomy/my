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
			//�齨�ͻ���Ϣ
			String idType=paramMapService.getIdType((String)oldMUserMap.get("CERT_TYPE"));
			String idNo=(String) oldMUserMap.get("CERT_NO");
			String mchPhone=(String) oldMUserMap.get("MOBILE");//�����ֻ���
			if(mchPhone!=null) mchPhone=mchPhone.trim();
			
			String cifName=(String) oldMUserMap.get("CUST_NAME");
			if(cifName==null&&cifNo.length()==19)
				cifName=(String)sqlMapMOBILE.queryForObject("mobile.queryCifName", DataUtil.get15CifNo(cifNo));
			if(cifName==null){
				log.error("�ͻ���Ϊ"+cifNo+"�Ŀͻ���������");
				cifName=idNo;
			}
			
			String branCode=(String)oldMUserMap.get("REG_BRAN_CODE");
			String cifDeptSeq="9999";
			if(branCode==null){
				log.error("�ͻ���Ϊ"+cifNo+"�Ļ���������");
			}else{
				Map userDept=deptService.queryDeptById(branCode);
				if(userDept==null){
					log.error("Dept��ȱʧdeptIdΪ"+branCode+"��ӳ���ϵ");
				}else
					cifDeptSeq=String.valueOf(userDept.get("DEPTSEQ"));
			}
			String regType=(String)oldMUserMap.get("REG_TYPE");
			String cifState=paramMapService.getCifState(regType);
			String userId=(String)oldMUserMap.get("APP_NAME");
			if(userId==null)
				userId=cifNo;
			
			String crmNo=(String)oldMUserMap.get("INTRODUCER");//�����˹�Ա��
			String gender=paramMapService.getGender(idNo);
			Date openDate=(Date)oldMUserMap.get("REG_DATE");
			if(openDate==null){//ȡ����ʱ��
				openDate=(Date) this.sqlMapMOBILE.queryForObject("mobile.queryCardRegDate",cifNo);
			}
			Timestamp openTime=new Timestamp(openDate.getTime());
			
			String mobileSignType="S"+(String)oldMUserMap.get("REG_ADDR");//ǩԼ;�� MobileSignType.java
			String reg_oper_no=(String)oldMUserMap.get("REG_OPER_NO");//ǩԼ��Ա��
			if(reg_oper_no!=null) reg_oper_no=reg_oper_no.trim();
			
			//�齨�û���Ϣ 
			String password=(String) oldMUserMap.get("PWD");
			Date lastLoginDate=(Date) oldMUserMap.get("LAST_DATE");
			Timestamp lastLoginTime=lastLoginDate==null?null:new Timestamp(lastLoginDate.getTime());
			
			if("Y".equals(newCif)){//�����ͻ�
				String corePhone=(String) oldMUserMap.get("CUST_PHONE");//�����ֻ���
				if(corePhone==null&&cifNo.length()==19)
					corePhone=(String)sqlMapMOBILE.queryForObject("mobile.queryCifPhone", DataUtil.get15CifNo(cifNo));
				if(corePhone==null){
					log.error("�ͻ���Ϊ"+cifNo+"�ĺ����ֻ��Ų�����");
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
						cifSeq,"�ֻ����й�����",cifDeptSeq,openTime
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
				//�����޶���Ϊ0 ����Ĭ���޶�
				if(lmtPerTrs.compareTo(new BigDecimal(0))==0)
					lmtPerTrs=(BigDecimal)limitService.queryCifLimit("LimitPerTrs","PMBS").get("LIMITAMOUNT");
				transList.add(new TransObject(insertECCIFLIMIT,new Object[]{
						cifSeq,"LimitPerTrs",lmtPerTrs,cifDeptSeq,openTime
				}));
				
				BigDecimal lmtPerDay=(BigDecimal)oldMUserMap.get("MAX_DEBIT_DAYSUM");
				//���ۼ��޶���Ϊ0 ����Ĭ���޶�
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
				//רҵ�� �ڶ�λ��Ϊ0
				if(regType.charAt(1)!='0'){
					transList.add(new TransObject(insertECUSRAUTHMOD,new Object[]{
							userSeq,"O",cifDeptSeq
					}));
					transList.add(new TransObject(insertECUSRAUTHMODMCH,new Object[]{
							userSeq,"O",cifDeptSeq
					}));
				}
				//��¼���� ȫ���T
				transList.add(new TransObject(insertECUSRLOGINTYPE,new Object[]{
						userSeq,"T",userId,cifDeptSeq,openTime
				}));
			}else{//�ͻ��Ѵ��ڣ������ֻ���������
				String userSeq=(String)this.sqlMapECIF.queryForObject("ecif.queryUserSeqByCifSeq",cifSeq);
		
				transList.add(new TransObject(insertECCIFMCH,new Object[]{
						cifSeq,crmNo,cifDeptSeq,openTime,cifState,cifDeptSeq
				}));
				transList.add(new TransObject(insertECCIFPRODSET,new Object[]{
						cifSeq,"�ֻ����й�����",cifDeptSeq,openTime
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
				//�����޶���Ϊ0 ����Ĭ���޶�
				if(lmtPerTrs.compareTo(new BigDecimal(0))==0)
					lmtPerTrs=(BigDecimal)limitService.queryCifLimit("LimitPerTrs","PMBS").get("LIMITAMOUNT");
				transList.add(new TransObject(insertECCIFLIMIT,new Object[]{
						cifSeq,"LimitPerTrs",lmtPerTrs,cifDeptSeq,openTime
				}));
				BigDecimal lmtPerDay=(BigDecimal)oldMUserMap.get("MAX_DEBIT_DAYSUM");
				//���ۼ��޶���Ϊ0 ����Ĭ���޶�
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
				//רҵ�� �ڶ�λ��Ϊ0
				if(regType.charAt(1)!='0'){
					transList.add(new TransObject(insertECUSRAUTHMODMCH,new Object[]{
							userSeq,"O",cifDeptSeq
					}));
				}
				//��¼���� ȫ���T
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
	
	//�ͻ���
	private final String insertECCIF="insert into ECCIF"
			+ "(CIFSEQ,CIFDEPTSEQ,CIFSTATE,CIFNAME,CREATEDEPTSEQ,CREATETIME,MODULEID,CIFLEVEL,"
			+ " CIFCONTROL,CIFMONITOR,CIFEXEMPT,CIFLOANFLG,CIFFINVIPFLG,TRANSFERTYPE)"
			+ "values(?,?,?,?,?,?,'per','PCOM','0','0','0','0','0','O')";
	//�ͻ�������ͨ��
	private final String insertECCIFMCH="insert into ECCIFMCH"
			+ "(CIFSEQ,CRMNO,OPENDEPTSEQ,OPENTIME,CMCHANNESTATE,CREATEDEPTSEQ,MCHANNELID)"
			+ "values(?,?,?,?,?,?,'PMBS')";
	//�ͻ�֤����
	private final String insertECCIFID="insert into ECCIFID"
			+ "(CIFSEQ,IDTYPE,IDNO,PRIMARYFLAG)"
			+ "values(?,?,?,'Y')";
	//���пͻ���Ϣ��
	private final String insertECEXTCIFNO="insert into ECEXTCIFNO"
			+ "(CIFSEQ,CIFNO,CIFNOTYPE)"
			+ "values(?,?,'C')";
	//�ͻ����޶��
	private final String insertECCIFLIMIT="insert into ECCIFLIMIT"
			+ "(CIFSEQ,LIMITTYPE,LMTAMT,CREATEDEPTSEQ,CREATETIME,MCHANNELID,LOGINTYPE,"
			+ " PAYERACNO,PAYEEACNO,CURRENCY,CIFLEVEL,PRDID,LMTLEVEL)"
			+ "values(?,?,?,?,?,'*','U','-','-','CNY','?','NT','1')";
	//������
	private final String insertECCIFPRODSET="insert into ECCIFPRODSET"
			+ "(CIFSEQ,PRDSETID,CREATEDEPTSEQ,CREATETIME,MCHANNELID)"
			+ "values(?,?,?,?,'PMBS')";
	//�������и��˿ͻ���Ϣ��
	private final String insertECPERS="insert into ECPERS (CIFSEQ,GENDER)"
			+ "values(?,?)";
	//���ӿͻ������
	private final String insertECCIFRULE="insert into ECCIFRULE "
			+ "(CIFSEQ,MODULEID,RULENAMESPACE,RULEID,RULETYPE,RULEDEF)"
			+ "values(?,'per','#',?,'TrsDef',?)";
	private final String insertECCIFTEL="insert into ECCIFTEL "
			+ "(CIFSEQ,TELTYPE,TELNO,CREATETIME,TELAUTHFLG)"
			+ "values(?,'M',?,?,'Y')";

	//���������û���
	private final String insertECUSR="insert into ECUSR"
			+ "(USERSEQ,CIFSEQ,USERNAME,PASSWORD,USERID,IDTYPE,IDNO,PHONE,"
			+ " USERSTATE,MOBILEPHONE,OPENDATE,GENDER,CREATEDEPTSEQ)"
			+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
	//�û�������
	private final String insertECUSRMCH="insert into ECUSRMCH "
			+ "(USERSEQ,USERID,PASSWORD,USERMCHSTATE,LASTLOGINTIME,"
			+ " MOBILEPHONE,CREATETIME,CREATEDEPTSEQ,MCHANNELID)"
			+ "values(?,?,?,?,?,?,?,?,'PMBS')";
	//�û������ж��û��Ƿ��״ε�½���״ε�½��Ҫ�����룬�����״β���Ҫ�����룩
	private final String insertECUSRRULE="insert into ECUSRRULE "
			+ "(USERSEQ,RULEID,RULEDEF,MODULEID,RULENAMESPACE,RULETYPE)"
			+ "values(?,?,?,'per','#','TrsDef')";
	//���������û���֤��Ϣ��
	private final String insertECUSRLOGINTYPE="insert into ECUSRLOGINTYPE "
			+ "(USERSEQ,LOGINTYPE,USERID,CREATEDEPTSEQ,CREATETIME,"
			+ " LOGINTYPESTATE,WRONGPASSCOUNT,MCHANNELID)"
			+ "values(?,?,?,?,?,'N','0','PMBS')";
	//�û���֤��ʽ
	private final String insertECUSRAUTHMOD="insert into ECUSRAUTHMOD "
			+ "(USERSEQ,AUTHMOD,CREATEDEPTSEQ,AUTHSTATE) "
			+ "values(?,?,?,'N')";

	private final String insertECUSRAUTHMODMCH="insert into ECUSRAUTHMODMCH "
			+ "(USERSEQ,AUTHMOD,CREATEDEPTSEQ,MCHANNELID) "
			+ "values(?,?,?,'PMBS')";

}
