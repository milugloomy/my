package com.sjqy.pibs;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import com.sjqy.query.ParamMapService;

@Service("PAccount")
public class PAccount extends DataMigrationAction {

	@Autowired
	private DeptService deptService;
	@Autowired
	private ParamMapService paramMapService;
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void execute(Map<String,Object> context){
 		List<Map> list=(List<Map>) context.get("List");
		
		ArrayList<Object[]> ecacctList=new ArrayList<Object[]>();
		ArrayList<Object[]> ecacctMchList=new ArrayList<Object[]>();
		ArrayList<Object[]> ecacctRuleList=new ArrayList<Object[]>();
		ArrayList<Object[]> ecuserMchAcctList=new ArrayList<Object[]>();

		
		for(int i=0;i<list.size();i++){
			String cifSeq=(String) list.get(i).get("CIFSEQ");
			String cifNo=(String) list.get(i).get("CIFNO");
			
			List<Map> oldPAccountList=(List<Map>) sqlMapOLD.queryForList("old.queryOldPAccount", cifNo);
			if(oldPAccountList.size()==0)//15位身份证
				oldPAccountList=(List<Map>) sqlMapOLD.queryForList("old.queryOldPAccount",  DataUtil.get15CifNo(cifNo));
			
			String defaultAcSeq=null;
			Timestamp defaultCreateTime=new Timestamp(System.currentTimeMillis());
			for(int j=0;j<oldPAccountList.size();j++){
				Map oldPAccountMap=oldPAccountList.get(j);
				String acNo=((String) oldPAccountMap.get("ACCT_NO")).trim();
				
				String acState=(String)oldPAccountMap.get("MATCH_STATUS");
				acState=paramMapService.getAcState(acState);
				if(acState.equals("C"))//销户的卡不迁移
					continue;

				//账户类型
				String bankAcType=paramMapService.getBankAcTypeByAcNo(acNo);
				if(bankAcType==null){
					Map acInfoMap=(Map)sqlMapMOBILE.queryForObject("mobile.queryOldBankAcType",DataUtil.getAcNo(acNo,32));
					if(acInfoMap==null){
						log.error("账号的"+acNo+"的bankactype不存在");
						bankAcType="OTH";
					}else{
						int bus_code=((BigDecimal) acInfoMap.get("BUS_CODE")).intValue();
						String acct_attr=(String) acInfoMap.get("ACCT_ATTR");
						String draw_mode=(String) acInfoMap.get("DRAW_MODE");
						bankAcType=paramMapService.getBankAcType(bus_code,acct_attr,draw_mode);
					}
					if(bankAcType==null){
						log.error("账号的"+acNo+"的bankactype不存在");
						bankAcType="OTH";
					}
				}
				
				//开户机构
				String branCode=null;
				String deptSeq="9999";//找不到机构则设为9999
				if(acNo.startsWith("621977")||acNo.startsWith("623112"))
					branCode=(String)sqlMapMOBILE.queryForObject("mobile.queryPDBCBranCode",DataUtil.getAcNo(acNo,32));
				else if(acNo.startsWith("625988")||acNo.startsWith("628255")||acNo.startsWith("622899")||acNo.startsWith("622868"))
					branCode=(String)sqlMapMOBILE.queryForObject("mobile.queryPCRCBranCode",DataUtil.getAcNo(acNo,19));
				else
					branCode=(String)sqlMapMOBILE.queryForObject("mobile.queryACBranCode",DataUtil.getAcNo(acNo,32));
				if(branCode==null){
					log.error("卡号为"+acNo+"的开卡机构不存在");
				}else{
					Map acctDept=deptService.queryDeptById(branCode);
					if(acctDept==null)
						log.error("Dept表缺失deptId为"+branCode+"的映射关系");
					else
						deptSeq=String.valueOf(acctDept.get("DEPTSEQ"));
				}
				
				String acName=(String) oldPAccountMap.get("ACCT_NO_NAME");
				if(acName==null||"".equals(acName)){//取用户名
					acName=(String)sqlMapECIF.queryForObject("ecif.queryCifNameByCifSeq",cifSeq);
				}
				String currency="CNY";
				String crFlag="C";
				String acAlias=(String) oldPAccountMap.get("ACCT_ALIAS");
				
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
				Date openDate = null;
				try {
					long timeMillis=sdf.parse((String)oldPAccountMap.get("APPLY_DATE")).getTime();
					openDate = new Date(timeMillis);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				Timestamp createTime=openDate==null?null:new Timestamp(openDate.getTime());

				Map<String,Object> paramMap=new HashMap<String,Object>();
				paramMap.put("CIFSEQ", cifSeq);
				paramMap.put("ACNO", acNo);
				String acSeq=(String) sqlMapECIF.queryForObject("ecif.queryAcSeqByAcNo",paramMap);
				if(acSeq==null){//新账号
					acSeq=(String) sqlMapECIF.queryForObject("ecif.queryAcSeq");
					ecacctList.add(new Object[]{
							cifSeq,acSeq,deptSeq,bankAcType,acNo,acName,currency,crFlag,
							acAlias,deptSeq,createTime,acState
					});
				}
				ecacctMchList.add(new Object[]{
						acSeq,deptSeq,createTime
				});
				//允许转账
				ecacctRuleList.add(new Object[]{
						acSeq,"#","CrossBankTransfer","1"
				});
				ecacctRuleList.add(new Object[]{
						acSeq,"#","BankInnerTransfer","1"
				});
				String userSeq=(String)sqlMapECIF.queryForObject("ecif.queryUserSeqByCifSeq",cifSeq);
				ecuserMchAcctList.add(new Object[]{
						acSeq,userSeq,deptSeq,createTime
				});
				
				if(createTime==null || createTime.before(defaultCreateTime)){
					defaultCreateTime=createTime;
					defaultAcSeq=acSeq;
				}
			}
			if(defaultAcSeq!=null)
				ecacctRuleList.add(new Object[]{
						defaultAcSeq,"#","PIBS.DefaultAcNo","1"
				});
		}
		
		
		this.batchAction.excuteBatch(sqlMapECIF, insertECACCT, ecacctList);
		this.batchAction.excuteBatch(sqlMapECIF, insertECACCTMCH, ecacctMchList);
		this.batchAction.excuteBatch(sqlMapECIF, insertECACCTRULE, ecacctRuleList);
		this.batchAction.excuteBatch(sqlMapECIF, insertECUSRMCHACCT, ecuserMchAcctList);

		// 将迁移标志更新为成功
		this.batchAction.excuteBatch(this.getSqlMapTMP(), BATCHSQL.updatePAccountTmpData, DataUtil.tmpPList(list,"OK"));
	}

	//账户表
	private final String insertECACCT = "insert into ECACCT "
			+ "(CIFSEQ,ACSEQ,DEPTSEQ,BANKACTYPE,ACNO,ACNAME,CURRENCY,"
			+ " CRFLAG,ACALIAS,CREATEDEPTSEQ,CREATETIME,ACSTATE,ASSOCIFACFLAG,UPDATEUSERSEQ)"
			+ "values(?,?,?,?,?,?,?,?,?,?,?,?,'0','9528')";
	//账户渠道
	private final String insertECACCTMCH = "insert into ECACCTMCH "
			+ "(ACSEQ,CREATEDEPTSEQ,CREATETIME,MCHANNELID)"
			+ "values(?,?,?,'PIBS')";
	//渠道规则
	private final String insertECACCTRULE ="insert into ECACCTRULE "
			+ "(ACSEQ,RULENAMESPACE,RULETYPE,RULEID,RULEDEF)"
			+ "values(?,?,'AcDef',?,?)";

	private final String insertECUSRMCHACCT ="insert into ECUSRMCHACCT "
			+ "(ACSEQ,USERSEQ,CREATEDEPTSEQ,CREATETIME,MCHANNELID)"
			+ "values(?,?,?,?,'PIBS')";

}
