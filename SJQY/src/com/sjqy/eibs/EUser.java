package com.sjqy.eibs;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dataUtil.BATCHSQL;
import com.dataUtil.DataUtil;
import com.sjqy.common.DataMigrationAction;
import com.sjqy.query.DeptService;
import com.sjqy.query.ParamMapService;

@Service("EUser")
public class EUser extends DataMigrationAction {

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
		ArrayList<Object[]> ecOrgList=new ArrayList<Object[]>();
		ArrayList<Object[]> ecOrgPersList=new ArrayList<Object[]>();
		ArrayList<Object[]> eccifProdSetList=new ArrayList<Object[]>();
		ArrayList<Object[]> eccifRuleList=new ArrayList<Object[]>();
		
		ArrayList<Object[]> ecusrList=new ArrayList<Object[]>();
		ArrayList<Object[]> ecusrMchList=new ArrayList<Object[]>();
		ArrayList<Object[]> ecusrRuleList=new ArrayList<Object[]>();
		ArrayList<Object[]> ecusrLoginTypeList=new ArrayList<Object[]>();
		ArrayList<Object[]> ecusrCertList=new ArrayList<Object[]>();
		ArrayList<Object[]> ecusrCertsList=new ArrayList<Object[]>();
		for(int i=0;i<list.size();i++){
			String cifSeq=(String) list.get(i).get("CIFSEQ");
			String cifNo=(String) list.get(i).get("CIFNO");
			String eIdType=(String) list.get(i).get("IDTYPE");
			eIdType=eIdType==null?"E00":eIdType;
			String eIdNo=(String) list.get(i).get("IDNO");
			//רҵ��
			Map oldECifMap=(Map) sqlMapOLD.queryForObject("old.queryOldECif1", cifNo);
			//���ڰ�
			if(oldECifMap==null){
				oldECifMap=(Map) sqlMapOLD.queryForObject("old.queryOldECif2", cifNo);
				//������
				if(oldECifMap==null)
					continue;
			}
			//�齨�ͻ���Ϣ
			Map cifDept=deptService.queryDeptById((String)oldECifMap.get("cif_deptid"));
			//������deptid�Ҳ���������deptseq��Ϊ������
			String cifDeptSeq=cifDept==null?"913000":String.valueOf(cifDept.get("DEPTSEQ"));
			String cifState=paramMapService.getCifState((String) oldECifMap.get("cif_status"));
			String cifName=(String) oldECifMap.get("cif_chinesename");
			String proposerName=(String) oldECifMap.get("cif_proposername");
			String proposerId=(String) oldECifMap.get("cif_proposerid");
			
			Date openDate=(Date)oldECifMap.get("cif_opendate");
			Date closeDate=(Date)oldECifMap.get("cif_closedate");
			Timestamp openTime=openDate==null?null:new Timestamp(openDate.getTime());
			String parentCifno=(String) oldECifMap.get("cif_parent");
			//Timestamp closeTime=closeDate==null?null:new Timestamp(closeDate.getTime());
			
			eccifRuleList.add(new Object[]{
					cifSeq
			});
			
			eccifList.add(new Object[]{
					cifSeq,cifDeptSeq,cifState,cifName,cifDeptSeq
			});
			
			eccifMchList.add(new Object[]{
					cifSeq,cifDeptSeq,openTime,cifState,cifDeptSeq
			});
			eccifIdList.add(new Object[]{
					cifSeq,eIdType,eIdNo
			});
			ecextCifNoList.add(new Object[]{
					cifSeq,cifNo,parentCifno
			});
			ecOrgList.add(new Object[]{
					cifSeq,cifName,cifSeq,cifDeptSeq,openTime
			});
			ecOrgPersList.add(new Object[]{
					cifSeq,"L",proposerName,"P00",proposerId,cifDeptSeq,openTime
			});
			//רҵ�湦����  setȥ�ظ�
			if(oldECifMap.get("cif_modules")!=null){
				HashSet<String> moduleSet=new HashSet<String>();
				String[] moduleArr=((String)oldECifMap.get("cif_modules")).split(",");
				for(int j=0;j<moduleArr.length;j++){
					if(paramMapService.getModule(moduleArr[j])!=null){
						moduleSet.add((String)paramMapService.getModule(moduleArr[j])[0]);
					}
				}
				Iterator<String> it=moduleSet.iterator();
				while(it.hasNext()){
					eccifProdSetList.add(new Object[]{
						cifSeq,it.next()
					});
				}
				//���пͻ������������������
				eccifProdSetList.add(new Object[]{cifSeq,"ҵ�񸴺�"});
				eccifProdSetList.add(new Object[]{cifSeq,"��������"});
				eccifProdSetList.add(new Object[]{cifSeq,"��Ϣά��"});
			}
			
			//�齨�û���Ϣ
			List<Map> oldEUserList=sqlMapOLD.queryForList("old.queryOldEUser", cifNo);
			for(int j=0;j<oldEUserList.size();j++){
				String userSeq=(String) sqlMapECIF.queryForObject("ecif.queryUserSeq");
				Map oldEUserMap=oldEUserList.get(j);
				String userId=(String)oldEUserMap.get("user_loginid");
				String userName=(String)oldEUserMap.get("user_name");
				if(userName==null || "".equals(userName))
					userName="δ�����û���";
				String password=(String)oldEUserMap.get("user_mastpass");
				String email=(String)oldEUserMap.get("user_email");
				String phone=(String)oldEUserMap.get("user_telno");
				String adminUser="";
				String gender=paramMapService.getGender((String)oldEUserMap.get("user_sex"));
				Timestamp lastLoginTime=(Timestamp)oldEUserMap.get("user_lastlogin");
				String certState=(String)oldEUserMap.get("user_cert_status");
				String userState=paramMapService.getUserState((String)oldEUserMap.get("user_status"));
				Date userOpenDate=(Date)oldEUserMap.get("user_opendate");
				//Date userCloseDate=(Date)oldEUserMap.get("user_closedate");
				Timestamp userOpenTime=userOpenDate==null?null:new Timestamp(userOpenDate.getTime());
				
				ecusrList.add(new Object[]{
						userSeq,cifSeq,userName,password,userId,phone,adminUser,email,
						userState,phone,userOpenDate,gender,cifDeptSeq
				});
				ecusrMchList.add(new Object[]{
						userSeq,userId,password,userState,lastLoginTime,phone,userOpenTime,cifDeptSeq
				});
				//�û�״̬�ڶ�λΪ1��ʾ���޸�����
				if("00".equals(oldEUserMap.get("user_status"))||"10".equals(oldEUserMap.get("user_status"))){
					ecusrRuleList.add(new Object[]{
							userSeq,"EIBS.FristLogin","1"
					});
				}
				ecusrRuleList.add(new Object[]{
						userSeq,"EIBS.IsOldPWD","Y"
				});
				ecusrLoginTypeList.add(new Object[]{
						userSeq,"R",userId,cifDeptSeq,userOpenTime
				});
				if("1".equals(certState)){
					String usbKeyCN=(String) oldEUserMap.get("user_cert_cn");
					String certDN="CN="+usbKeyCN+",OU=Individual-1,OU=TPC-S3,O=CFCA OCA21,C=CN";
					Date expireDate=(Date) oldEUserMap.get("user_closedate");
					ecusrCertList.add(new Object[]{
							certDN,usbKeyCN,userSeq,cifDeptSeq,userOpenTime,userOpenTime,expireDate
					});
				}
				if(null==certState){
					String userCertNo = ""+ oldEUserMap.get("user_cert_no");
					String certDN = cifSeq+"&"+ userCertNo;
					Date expireDate=(Date) oldEUserMap.get("user_closedate");
					ecusrCertsList.add(new Object[]{
							certDN,userCertNo,userSeq,cifDeptSeq,userOpenTime,userOpenTime,expireDate
					});
					
				}

			}
		}
		
		this.batchAction.excuteBatch(sqlMapECIF, insertECCIF, eccifList);
		this.batchAction.excuteBatch(sqlMapECIF, insertECCIFMCH, eccifMchList);
		this.batchAction.excuteBatch(sqlMapECIF, insertECCIFID, eccifIdList);
		this.batchAction.excuteBatch(sqlMapECIF, insertECEXTCIFNO, ecextCifNoList);
		this.batchAction.excuteBatch(sqlMapECIF, insertECORG, ecOrgList);
		this.batchAction.excuteBatch(sqlMapECIF, insertECORGPERS, ecOrgPersList);
		this.batchAction.excuteBatch(sqlMapECIF, insertECCIFPRODSET, eccifProdSetList);
		this.batchAction.excuteBatch(sqlMapECIF, insertECCIFRULE, eccifRuleList);
	
		this.batchAction.excuteBatch(sqlMapECIF, insertECUSR, ecusrList);
		this.batchAction.excuteBatch(sqlMapECIF, insertECUSRMCH, ecusrMchList);
		this.batchAction.excuteBatch(sqlMapECIF, insertECUSRRULE, ecusrRuleList);
		this.batchAction.excuteBatch(sqlMapECIF, insertECUSRLOGINTYPE, ecusrLoginTypeList);
		this.batchAction.excuteBatch(sqlMapECIF, insertECUSRCERT, ecusrCertList);

		this.batchAction.excuteBatch(this.getSqlMapTMP(), BATCHSQL.updateEUserTmpData, DataUtil.tmpEList(list,"OK"));
	}

	//�ͻ���Ϣ
	private final String insertECCIF = "insert into ECCIF"
			+ "(CIFSEQ,CIFDEPTSEQ,CIFSTATE,CIFNAME,CREATEDEPTSEQ,MODULEID,CIFLEVEL,"
			+ " CIFCONTROL,CIFMONITOR,CIFEXEMPT,CIFLOANFLG,CIFFINVIPFLG,TRANSFERTYPE)"
			+ "values(?,?,?,?,?,'ent','ECOM','0','0','0','0','0','O')";
	//�ͻ�������ͨ��
	private final String insertECCIFMCH = "insert into ECCIFMCH"
			+ "(CIFSEQ,OPENDEPTSEQ,OPENTIME,CMCHANNESTATE,CREATEDEPTSEQ,MCHANNELID)"
			+ "values(?,?,?,?,?,'EIBS')";
	//�ͻ�֤����
	private final String insertECCIFID = "insert into ECCIFID"
			+ "(CIFSEQ,IDTYPE,IDNO,PRIMARYFLAG)"
			+ "values(?,?,?,'Y')";
	//���пͻ���Ϣ��
	private final String insertECEXTCIFNO = "insert into ECEXTCIFNO"
			+ "(CIFSEQ,CIFNO,CIFNOTYPE,PARENTCIFNO)"
			+ "values(?,?,'C',?)";
	//��֯
	private final String insertECORG = "insert into ECORG"
			+ "(CIFSEQ,CORECIFNAME,PARENTCIFSEQ,CREATEDEPTSEQ,CREATETIME)"
			+ "values(?,?,?,?,?)";
	//��ҵ���˹�ϵ��
	private final String insertECORGPERS = "insert into ECORGPERS"
			+ "(CIFSEQ,RELATIONCD,NAME,IDTYPE,IDNO,CREATEDEPTSEQ,CREATETIME)"
			+ "values(?,?,?,?,?,?,?)";
	//�û������ڵĹ�����
	private final String insertECCIFPRODSET = "insert into ECCIFPRODSET"
			+ "(CIFSEQ,PRDSETID,MCHANNELID)"
			+ "values(?,?,'EIBS')";
	//�ͻ�����
	private final String insertECCIFRULE = "insert into ECCIFRULE "
			+ "(CIFSEQ,RULENAMESPACE,RULETYPE,RULEID,MODULEID,RULEDEF)"
			+ "values(?,'#','TrsDef','EIBS.MgmtPrdSet.AuthMode','ent','D')";
	
	//���������û���
	private final String insertECUSR = "insert into ECUSR"
			+ "(USERSEQ,CIFSEQ,USERNAME,PASSWORD,USERID,PHONE,ADMINUSER,"
			+ " EMAIL,USERSTATE,MOBILEPHONE,OPENDATE,GENDER,CREATEDEPTSEQ)"
			+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
	//�û�������
	private final String insertECUSRMCH = "insert into ECUSRMCH "
			+ "(USERSEQ,USERID,PASSWORD,USERMCHSTATE,LASTLOGINTIME,"
			+ " MOBILEPHONE,CREATETIME,CREATEDEPTSEQ,MCHANNELID)"
			+ "values(?,?,?,?,?,?,?,?,'EIBS')";
	//�û������ж��û��Ƿ��״ε�½���״ε�½��Ҫ�����룬�����״β���Ҫ�����룩
	private final String insertECUSRRULE = "insert into ECUSRRULE "
			+ "(USERSEQ,RULEID,RULEDEF,MODULEID,RULENAMESPACE,RULETYPE)"
			+ "values(?,?,?,'ent','#','TrsDef')";
	//���������û���֤��Ϣ��
	private final String insertECUSRLOGINTYPE = "insert into ECUSRLOGINTYPE "
			+ "(USERSEQ,LOGINTYPE,USERID,CREATEDEPTSEQ,CREATETIME,"
			+ " LOGINTYPESTATE,WRONGPASSCOUNT,MCHANNELID)"
			+ "values(?,?,?,?,?,'N','0','EIBS')";
	//�û�֤����Ϣ��
	private final String insertECUSRCERT = "insert into ECUSRCERT "
			+ "(CERTDN,USBKEYSN,USERSEQ,CREATEDEPTSEQ,CERTAPPLYDATE,CREATETIME,EXPIREDATE,"
			+ " USBKEYFLAG,ISSUERCACODE,CERTSTATE,FEETYPE,BATCHFLAG,UPDATENUM,TOKENTYPE)"
			+ "values(?,?,?,?,?,?,?,'0','CFCA','B','0','0',0,'PC')";

}
