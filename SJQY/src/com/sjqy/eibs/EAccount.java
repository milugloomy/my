package com.sjqy.eibs;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
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

@Service("EAccount")
public class EAccount extends DataMigrationAction {

	@Autowired
	private DeptService deptService;
	@Autowired
	private ParamMapService paramMapService;
	@Autowired
	private LimitService limitService;
	
	@Override
	public void execute(Map<String,Object> context){
		List<Map> list=(List<Map>) context.get("List");
		
		ArrayList<Object[]> ecacctList=new ArrayList<Object[]>();
		ArrayList<Object[]> ecacctMchList=new ArrayList<Object[]>();
		ArrayList<Object[]> ecacctRuleList=new ArrayList<Object[]>();
		ArrayList<Object[]> ecacLimitList=new ArrayList<Object[]>();
		for(int i=0;i<list.size();i++){
			String cifSeq=(String) list.get(i).get("CIFSEQ");
			String cifNo=(String) list.get(i).get("CIFNO");
			
			//�˻���Ϣ
			List<Map> oldEAccountList=(List<Map>) sqlMapOLD.queryForList("old.queryOldEAccount", cifNo);
			for(int j=0;j<oldEAccountList.size();j++){
				Map oldEAccountMap=oldEAccountList.get(j);
				String acType=(String) oldEAccountMap.get("ac_actype");
				String bankAcType=null;
				String acSeq=(String) sqlMapECIF.queryForObject("ecif.queryAcSeq");
				Map acctDept=deptService.queryDeptById((String)oldEAccountMap.get("dept_id"));
				//������deptid�Ҳ���������deptseq��Ϊ������
				String deptSeq=acctDept==null?"913000":String.valueOf(acctDept.get("DEPTSEQ"));
				String branchId=(String) oldEAccountMap.get("branch_id");
				String acNo=(String) oldEAccountMap.get("acno");
				String acName=(String) oldEAccountMap.get("ac_name");
				String acAlias="";
				String currency=paramMapService.getCurrency((String) oldEAccountMap.get("ac_currency"));
				String crFlag=paramMapService.getCrFlag((String) oldEAccountMap.get("ac_currtype"));
				Date openDate=(Date)oldEAccountMap.get("ac_date");
				Timestamp createTime=openDate==null?null:new Timestamp(openDate.getTime());
				String transfertype=(String) oldEAccountMap.get("ac_transfertype");
				String coreCifno=(String) oldEAccountMap.get("ac_masterid");
				
				ecacctList.add(new Object[]{
						cifSeq,acSeq,deptSeq,bankAcType,acNo,acName,currency,crFlag,
						acAlias,deptSeq,createTime,acType,coreCifno
				});
				ecacctMchList.add(new Object[]{
						acSeq,deptSeq,createTime
				});
				ecacctRuleList.add(new Object[]{
						acSeq,"#","AcBranchId",branchId
				});
				//����ת��
				if("1".equals(transfertype)){
					ecacctRuleList.add(new Object[]{
							acSeq,"#","EIBS.AcRight","FT"
					});
				}
				//ֻ�ܲ�ѯ
				if("0".equals(transfertype)){
					ecacctRuleList.add(new Object[]{
							acSeq,"#","EIBS.AcRight","Q"
					});
				}
				//ȡÿ���ͻ���һ��Ϊ���˻�
				if(j==0){
					ecacctRuleList.add(new Object[]{
							acSeq,"#","EIBS.MasterAcFlag","1"
					});
				}
				
				//��ҵĬ���˻����޶�
				BigDecimal lmtPerTrs = (BigDecimal)limitService.queryCifLimit("LimitPerTrs","EIBS").get("LIMITAMOUNT");
				ecacLimitList.add(new Object[]{
						cifSeq,acNo,"LimitPerTrs",lmtPerTrs,deptSeq,createTime,"NT"
				});
				
				BigDecimal lmtPerDay = (BigDecimal) limitService.queryCifLimit("LimitPerDay","EIBS").get("LIMITAMOUNT");
				ecacLimitList.add(new Object[]{
						cifSeq,acNo,"LimitPerDay",lmtPerDay,deptSeq,createTime,"NT"
				});
			
				//��ҵ��ת˽�޶�
				Map qryE2PMap = new HashMap();
				qryE2PMap.put("CifNo", cifNo);
				qryE2PMap.put("AcNo", acNo);
				Object oldO = sqlMapOLD.queryForObject("old.queryOldE2PLimit", qryE2PMap);
				Map oldE2PLimit = null;
				if(null != oldO){
					oldE2PLimit = (Map) oldO;
				}
				if(null != oldE2PLimit){
					ecacLimitList.add(new Object[]{
							cifSeq, acNo, "LimitPerTrs", oldE2PLimit.get("amt_single"), deptSeq, createTime, "BE"
					});
					ecacLimitList.add(new Object[]{
							cifSeq, acNo, "LimitPerDay", oldE2PLimit.get("amt_total"), deptSeq, createTime, "BE"
					});
				}
				
			}
			
		}
		
		
		batchAction.excuteBatch(sqlMapECIF, insertECACCT, ecacctList);
		batchAction.excuteBatch(sqlMapECIF, insertECACCTMCH, ecacctMchList);
		batchAction.excuteBatch(sqlMapECIF, insertECACCTRULE, ecacctRuleList);
		batchAction.excuteBatch(sqlMapECIF, insertECACLIMIT, ecacLimitList);

		// ��Ǩ�Ʊ�־����Ϊ�ɹ�
		batchAction.excuteBatch(this.getSqlMapTMP(), BATCHSQL.updateEAccountTmpData, DataUtil.tmpEList(list,"OK"));
	}

	//�˻���
	private final String insertECACCT ="insert into ECACCT "
			+ "(CIFSEQ,ACSEQ,DEPTSEQ,BANKACTYPE,ACNO,ACNAME,CURRENCY,CRFLAG,"
			+ "ACALIAS,CREATEDEPTSEQ,CREATETIME,ACTYPE,ASSOCIFACFLAG,ACSTATE,CORECIFNO)"
			+ "values(?,?,?,?,?,?,?,?,?,?,?,?,'0','N',?)";
	//�˻�����
	private final String insertECACCTMCH ="insert into ECACCTMCH "
			+ "(ACSEQ,CREATEDEPTSEQ,CREATETIME,MCHANNELID)"
			+ "values(?,?,?,'EIBS')";
	//��������
	private final String insertECACCTRULE ="insert into ECACCTRULE "
			+ "(ACSEQ,RULENAMESPACE,RULETYPE,RULEID,RULEDEF)"
			+ "values(?,?,'AcDef',?,?)";
	//�˻����޶��
	private final String insertECACLIMIT ="insert into ECACLIMIT"
			+ "(CIFSEQ,ACNO,LIMITTYPE,LMTAMT,CREATEDEPTSEQ,CREATETIME,"
			+ "MCHANNELID,LOGINTYPE,CURRENCY,CIFLEVEL,PRDID,LMTLEVEL,DEPTSEQ)"
			+ "values(?,?,?,?,?,?,'-','U','CNY','?',?,'1','-')";

}
