package com.sjqy.psms;

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
import com.sjqy.query.LimitService;
import com.sjqy.query.ParamMapService;

@Service("PSms")
public class PSms extends DataMigrationAction {
	
	@Autowired
	private DeptService deptService;
	@Autowired
	private LimitService limitService;
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
		
		ArrayList<Object[]> ecusrList=new ArrayList<Object[]>();
		ArrayList<Object[]> ecusrMchList=new ArrayList<Object[]>();
		ArrayList<Object[]> ecusrRuleList=new ArrayList<Object[]>();
		ArrayList<Object[]> ecusrLoginTypeList=new ArrayList<Object[]>();
		ArrayList<Object[]> ecusrAuthModList=new ArrayList<Object[]>();
		ArrayList<Object[]> ecusrAuthModMchList=new ArrayList<Object[]>();
		for(int i=0;i<list.size();i++){
			String cifSeq=(String) list.get(i).get("CIFSEQ");
			String cifNo=(String) list.get(i).get("CIFNO");
			
			Map oldMUserMap=(Map) sqlMapMOBILE.queryForObject("mobile.queryOldMUser", cifNo);
			//�齨�ͻ���Ϣ
			String idType=paramMapService.getIdType((String)oldMUserMap.get("CERT_TYPE"));
			String idNo=(String) oldMUserMap.get("CERT_NO");
			
			String branCode=(String)oldMUserMap.get("REG_BRAN_CODE");
			Map userDept=deptService.queryDeptById(branCode);
			//������userDept�Ҳ���������deptseq��Ϊ����һ
			String cifDeptSeq=userDept==null?"9999":String.valueOf(userDept.get("DEPTSEQ"));
			
			String cifState=paramMapService.getCifState((String)oldMUserMap.get("REG_TYPE"));
			String userId=(String)oldMUserMap.get("APP_NAME");
			String cifName=(String)oldMUserMap.get("CUST_NAME");
			if(cifName==null)
				cifName=idNo;
			String crmNo=(String)oldMUserMap.get("INTRODUCER");//�����˹�Ա��
			String gender=(String) oldMUserMap.get("CUST_SEX");
			if(gender==null||"".equals(gender)||"N".equals(gender))
				gender="U";
			Date openDate=(Date)oldMUserMap.get("REG_DATE");
			Timestamp openTime=openDate==null?null:new Timestamp(openDate.getTime());
			
			String mobileSignType="S"+(String)oldMUserMap.get("REG_ADDR");//ǩԼ;�� MobileSignType.java
			String reg_oper_no=(String)oldMUserMap.get("REG_OPER_NO");//ǩԼ��Ա��
			
			eccifList.add(new Object[]{
					cifSeq,cifDeptSeq,cifState,cifName,cifDeptSeq
			});
			eccifMchList.add(new Object[]{
					cifSeq,crmNo,cifDeptSeq,openTime,cifState,cifDeptSeq
			});
			eccifIdList.add(new Object[]{
					cifSeq,idType,idNo
			});
			ecextCifNoList.add(new Object[]{
					cifSeq,cifNo
			});
			eccifProdSetList.add(new Object[]{
					cifSeq,"�ֻ����й�����",cifDeptSeq,openTime
			});
			ecPersList.add(new Object[]{
					cifSeq,gender
			});
			eccifRuleList.add(new Object[]{
					cifSeq,"MobileSignType",mobileSignType
			});
			if(reg_oper_no!=null && !"".equals(reg_oper_no)){
				eccifRuleList.add(new Object[]{
						cifSeq,"CreateEmployeeNo",reg_oper_no
				});
			}
			BigDecimal lmtPerTrs=(BigDecimal)oldMUserMap.get("MAX_DEBIT_AMT");
			//�����޶���Ϊ0 ����Ĭ���޶�
			if(lmtPerTrs.compareTo(new BigDecimal(0))==0)
				lmtPerTrs=(BigDecimal)limitService.queryCifLimit("LimitPerTrs","PMBS").get("LIMITAMOUNT");
			eccifLimitList.add(new Object[]{
					cifSeq,"LimitPerTrs",lmtPerTrs,cifDeptSeq,openTime
			});
			
			BigDecimal lmtPerDay=(BigDecimal)oldMUserMap.get("MAX_DEBIT_DAYSUM");
			//���ۼ��޶���Ϊ0 ����Ĭ���޶�
			if(lmtPerDay.compareTo(new BigDecimal(0))==0)
				lmtPerDay=(BigDecimal) limitService.queryCifLimit("LimitPerDay","PMBS").get("LIMITAMOUNT");
			eccifLimitList.add(new Object[]{
					cifSeq,"LimitPerDay",lmtPerDay,cifDeptSeq,openTime
			});
				
			
			
			//�齨�û���Ϣ 
			String userSeq=(String) sqlMapECIF.queryForObject("ecif.queryUserSeq");
			String password=(String) oldMUserMap.get("PWD");
			String phone=(String) oldMUserMap.get("MOBILE");
			Date lastLoginDate=(Date) oldMUserMap.get("LAST_DATE");
			Timestamp lastLoginTime=lastLoginDate==null?null:new Timestamp(lastLoginDate.getTime());
			ecusrList.add(new Object[]{
					userSeq,cifSeq,cifName,password,userId,idType,idNo,phone,
					cifState,phone,openDate,gender,cifDeptSeq
			});
			ecusrMchList.add(new Object[]{
					userSeq,userId,password,cifState,lastLoginTime,phone,openTime,cifDeptSeq
			});
			if(lastLoginTime!=null && !"".equals(lastLoginTime)){
				ecusrRuleList.add(new Object[]{
						userSeq,"PMBS.FristLogin","1"
				});
			}
			ecusrRuleList.add(new Object[]{
					userSeq,"PMBS.IsOldPWD","Y"
			});
			//רҵ��
			/*ecusrAuthModList.add(new Object[]{
					userSeq,"U",cifDeptSeq
			});
			ecusrAuthModMchList.add(new Object[]{
					userSeq,"U",cifDeptSeq
			});*/
			//��¼���� ȫ���T
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
		
		this.batchAction.excuteBatch(sqlMapECIF, insertECUSR, ecusrList);
		this.batchAction.excuteBatch(sqlMapECIF, insertECUSRMCH, ecusrMchList);
		this.batchAction.excuteBatch(sqlMapECIF, insertECUSRRULE, ecusrRuleList);
		this.batchAction.excuteBatch(sqlMapECIF, insertECUSRLOGINTYPE, ecusrLoginTypeList);
		this.batchAction.excuteBatch(sqlMapECIF, insertECUSRAUTHMOD, ecusrAuthModList);
		this.batchAction.excuteBatch(sqlMapECIF, insertECUSRAUTHMODMCH, ecusrAuthModMchList);
		
		this.batchAction.excuteBatch(this.getSqlMapTMP(), BATCHSQL.updateMUserTmpData, DataUtil.tmpMList(list,"OK"));
	}
	//�ͻ���
	private final String insertECCIF="insert into ECCIF"
			+ "(CIFSEQ,CIFDEPTSEQ,CIFSTATE,CIFNAME,CREATEDEPTSEQ,MODULEID,CIFLEVEL,"
			+ " CIFCONTROL,CIFMONITOR,CIFEXEMPT,CIFLOANFLG,CIFFINVIPFLG,TRANSFERTYPE)"
			+ "values(?,?,?,?,?,'per','PCOM','0','0','0','0','0','O')";
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
			+ "values(?,?,?,?)";

}
