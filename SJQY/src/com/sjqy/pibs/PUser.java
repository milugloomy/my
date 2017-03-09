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
			
			if(oldPUserMap==null){//15λ���֤
				oldPUserMap=(Map) sqlMapOLD.queryForObject("old.queryOldPUser", DataUtil.get15CifNo(cifNo));
				idNo=cifNo.substring(1);//15λ���֤֤����ȡ���Ŀͻ��ź�18λ
			}
			//�齨�ͻ���Ϣ
			String branCode=((String)oldPUserMap.get("OPEN_BRAN_CODE")).trim();
			Map userDept=deptService.queryDeptById(branCode);
			//������userDept�Ҳ���������deptseq��Ϊ����һ
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
			if(idNo==null)//��Ϊnull��ʾ18λ���֤
				idNo=(String) oldPUserMap.get("CERT_NO");
			String gender=paramMapService.getGender(idNo);
			String oper_no=((BigDecimal)oldPUserMap.get("OPER_NO")).toString();//ǩԼ��Ա��	
			
			//�齨�û���Ϣ 
			String userId=idNo;
			String password=(String) oldPUserMap.get("PASSWORD");
			String corePhone=(String) oldPUserMap.get("CUST_PHONE");;//�����ֻ���
			String phone=(String) oldPUserMap.get("MOBILE");//�����ֻ���
			if(phone!=null)phone=phone.trim();
			String email=(String) oldPUserMap.get("EMAIL");
			if(email!=null)email=email.trim();
			
			String userSeq=null;
			if("Y".equals(newCif)){//�����ͻ�
				userSeq=(String) sqlMapECIF.queryForObject("ecif.queryUserSeq");
				//�ͻ���Ϣ
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
				
				//�û���Ϣ
				ecusrList.add(new Object[]{
						userSeq,cifSeq,cifName,password,userId,idType,idNo,corePhone,email,
						cifState,corePhone,openTime,gender,cifDeptSeq
				});
			}else{
				userSeq=(String)this.sqlMapECIF.queryForObject("ecif.queryUserSeqByCifSeq",cifSeq);
				if(userSeq==null){//���ڿͻ����������û���Ǳ�ڿͻ���
					userSeq=(String) sqlMapECIF.queryForObject("ecif.queryUserSeq");
					//�û���Ϣ
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
			
			//רҵ������޶�
			BigDecimal lmtPerDay=(BigDecimal)oldPUserMap.get("MAX_DEBIT_AMT");
			//���ۼ��޶���Ϊ0 ����Ĭ���޶�
			if(lmtPerDay==null || lmtPerDay.compareTo(new BigDecimal(0))==0)
				lmtPerDay=new BigDecimal(0);
			eccifLimitList.add(new Object[]{
					cifSeq,"LimitPerDay",lmtPerDay,cifDeptSeq,openTime
			});

			BigDecimal lmtPerTrs=(BigDecimal)oldPUserMap.get("PER_DEBIT_AMT");
			//�����޶���Ϊ0 ����Ĭ���޶�
			if(lmtPerTrs==null || lmtPerTrs.compareTo(new BigDecimal(0))==0)
				lmtPerTrs=lmtPerDay;
			eccifLimitList.add(new Object[]{
					cifSeq,"LimitPerTrs",lmtPerTrs,cifDeptSeq,openTime
			});
				
			eccifProdSetList.add(new Object[]{
					cifSeq,"��������������",cifDeptSeq,openTime
			});
			
			ecusrMchList.add(new Object[]{
					userSeq,userId,password,cifState,phone,openTime,cifDeptSeq
			});
			ecusrRuleList.add(new Object[]{
					userSeq,"PIBS.FristLogin","1"
			});
			
			if(match_status.charAt(1)!='0'){//רҵ��
				eccifOutTransferSwitchList.add(new Object[]{
						cifSeq
				});
				ecusrAuthModList.add(new Object[]{//SQL�ж��оͲ�����
						userSeq,"U",cifDeptSeq,userSeq,"U"
				});
				ecusrAuthModMchList.add(new Object[]{
						userSeq,"U",cifDeptSeq
				});
			}
			//Ĭ��Ǩ�ƿͻ�����AuthMod=Q
			ecusrAuthModList.add(new Object[]{userSeq,"Q",cifDeptSeq,userSeq,"Q"});//SQL�ж��оͲ�����
			ecusrAuthModMchList.add(new Object[]{userSeq,"Q",cifDeptSeq});
			
			//��¼���� ȫ��� R
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
	//�ͻ���
	private final String insertECCIF="insert into ECCIF"
			+ "(CIFSEQ,CIFDEPTSEQ,CIFSTATE,CIFNAME,CREATEDEPTSEQ,MODULEID,CIFLEVEL,"
			+ " CIFCONTROL,CIFMONITOR,CIFEXEMPT,CIFLOANFLG,CIFFINVIPFLG,TRANSFERTYPE)"
			+ "values(?,?,?,?,?,'per','PCOM','0','0','0','0','0','O')";
	//�ͻ�������ͨ��
	private final String insertECCIFMCH="insert into ECCIFMCH"
			+ "(CIFSEQ,OPENDEPTSEQ,OPENTIME,CMCHANNESTATE,CREATEDEPTSEQ,MCHANNELID)"
			+ "values(?,?,?,?,?,'PIBS')";
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
			+ "values(?,?,?,?,?,'PIBS','-','-','-','CNY','?','NT','1')";
	//������
	private final String insertECCIFPRODSET="insert into ECCIFPRODSET"
			+ "(CIFSEQ,PRDSETID,CREATEDEPTSEQ,CREATETIME,MCHANNELID)"
			+ "values(?,?,?,?,'PIBS')";
	//�������и��˿ͻ���Ϣ��
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

	//���������û���
	private final String insertECUSR="insert into ECUSR"
			+ "(USERSEQ,CIFSEQ,USERNAME,PASSWORD,USERID,IDTYPE,IDNO,PHONE,"
			+ " EMAIL,USERSTATE,MOBILEPHONE,OPENDATE,GENDER,CREATEDEPTSEQ)"
			+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	//�û�������
	private final String insertECUSRMCH="insert into ECUSRMCH "
			+ "(USERSEQ,USERID,PASSWORD,USERMCHSTATE,"
			+ " MOBILEPHONE,CREATETIME,CREATEDEPTSEQ,MCHANNELID)"
			+ "values(?,?,?,?,?,?,?,'PIBS')";
	//�û������ж��û��Ƿ��״ε�½���״ε�½��Ҫ�����룬�����״β���Ҫ�����룩
	private final String insertECUSRRULE="insert into ECUSRRULE "
			+ "(USERSEQ,RULEID,RULEDEF,MODULEID,RULENAMESPACE,RULETYPE)"
			+ "values(?,?,?,'per','#','TrsDef')";
	//���������û���֤��Ϣ��
	private final String insertECUSRLOGINTYPE="insert into ECUSRLOGINTYPE "
			+ "(USERSEQ,LOGINTYPE,USERID,CREATEDEPTSEQ,CREATETIME,"
			+ " LOGINTYPESTATE,WRONGPASSCOUNT,MCHANNELID)"
			+ "values(?,?,?,?,?,'N','0','PIBS')";
	//�û���֤��ʽ
	private final String insertECUSRAUTHMOD="insert into ecif.ECUSRAUTHMOD "
			+ "(USERSEQ,AUTHMOD,CREATEDEPTSEQ,AUTHSTATE) "
			+ "	select ?,?,?,'N' from dual where not exists("
			+ "		select * from ecif.ECUSRAUTHMOD where USERSEQ=? and AUTHMOD=?"
			+ ")";

	private final String insertECUSRAUTHMODMCH="insert into ECUSRAUTHMODMCH "
			+ "(USERSEQ,AUTHMOD,CREATEDEPTSEQ,MCHANNELID) "
			+ "values(?,?,?,'PIBS')";
	//usekey�û�֤��
	private final String insertECUSRCERT="insert into ECUSRCERT "
			+ "(CERTDN,USBKEYSN,USERSEQ,CREATEDEPTSEQ,CERTAPPLYDATE,CREATETIME,EXPIREDATE,"
			+ " ISSUERCACODE,USBKEYFLAG,CERTSTATE,FEETYPE,BATCHFLAG,UPDATENUM,TOKENTYPE)"
			+ "values(?,?,?,?,?,?,?,'CFCA','1',?,'0','0',0,'PC')";

}
