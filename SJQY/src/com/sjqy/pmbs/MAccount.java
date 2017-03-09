package com.sjqy.pmbs;

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
import com.sjqy.query.ParamMapService;

@Service("MAccount")
public class MAccount extends DataMigrationAction {

	@Autowired
	private DeptService deptService;
	@Autowired
	private ParamMapService paramMapService;
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
			
			List<Map> oldMAccountList=(List<Map>) sqlMapMOBILE.queryForList("mobile.queryOldMAccount", cifNo);
			
			String defaultAcSeq=null;
			Timestamp defaultCreateTime=new Timestamp(System.currentTimeMillis());
			for(int j=0;j<oldMAccountList.size();j++){
				Map oldMAccountMap=oldMAccountList.get(j);
				String acNo=((String) oldMAccountMap.get("ACCT_NO")).trim();
				
				String regType=(String)oldMAccountMap.get("REG_TYPE");
				String acState=paramMapService.getAcState(regType);
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
				String branCode=(String)oldMAccountMap.get("ACCT_BRAN_CODE");
				if(branCode!=null) branCode=branCode.trim();
				if(branCode==null||"".equals(branCode)){
					if(acNo.startsWith("621977")||acNo.startsWith("623112"))
						branCode=(String)sqlMapMOBILE.queryForObject("mobile.queryPDBCBranCode",DataUtil.getAcNo(acNo,32));
					else if(acNo.startsWith("625988")||acNo.startsWith("628255")||acNo.startsWith("622899")||acNo.startsWith("622868"))
						branCode=(String)sqlMapMOBILE.queryForObject("mobile.queryPCRCBranCode",DataUtil.getAcNo(acNo,19));
					else
						branCode=(String)sqlMapMOBILE.queryForObject("mobile.queryACBranCode",DataUtil.getAcNo(acNo,32));
				}
				String deptSeq="9999";//找不到机构则设为9999
				if(branCode==null){
					log.error("卡号为"+acNo+"的开卡机构不存在");
				}else{
					Map acctDept=deptService.queryDeptById(branCode);
					if(acctDept==null)
						log.error("Dept表缺失deptId为"+branCode+"的映射关系");
					else
						deptSeq=String.valueOf(acctDept.get("DEPTSEQ"));
				}
				
				String acName=(String) oldMAccountMap.get("ACCT_NO_NAME");
				if(acName==null||"".equals(acName)){//取用户名
					acName=(String)sqlMapECIF.queryForObject("ecif.queryCifNameByCifSeq",cifSeq);
				}
				String currency="CNY";
				String crFlag="C";
				Date openDate=(Date)oldMAccountMap.get("REG_DATE");
				Timestamp createTime=openDate==null?null:new Timestamp(openDate.getTime());
				
				Map<String,Object> paramMap=new HashMap<String,Object>();
				paramMap.put("CIFSEQ", cifSeq);
				paramMap.put("ACNO", acNo);
				String acSeq=(String) sqlMapECIF.queryForObject("ecif.queryAcSeqByAcNo",paramMap);
				if(acSeq==null){//账号不存在
					acSeq=(String) sqlMapECIF.queryForObject("ecif.queryAcSeq");
					ecacctList.add(new Object[]{
							cifSeq,acSeq,deptSeq,bankAcType,acNo,acName,currency,crFlag,
							deptSeq,createTime,acState
					});
				}
				ecacctMchList.add(new Object[]{
						acSeq,deptSeq,createTime
				});
				String userSeq=(String)sqlMapECIF.queryForObject("ecif.queryUserSeqByCifSeq",cifSeq);
				ecuserMchAcctList.add(new Object[]{
						acSeq,userSeq,deptSeq,createTime
				});
				//允许转账
				ecacctRuleList.add(new Object[]{
						acSeq,"#","PMBS.CrossBankTransfer","1"
				});
				ecacctRuleList.add(new Object[]{
						acSeq,"#","PMBS.BankInnerTransfer","1"
				});
				//支付认证方式
				ecacctRuleList.add(new Object[]{
						acSeq,"#","PMBS.UserIdPayFlag","1"
				});
				ecacctRuleList.add(new Object[]{
						acSeq,"#","PMBS.AcctPayFlag","1"
				});
				ecacctRuleList.add(new Object[]{
						acSeq,"#","PMBS.EWMPayFlag","1"
				});

				
				if(createTime==null || createTime.before(defaultCreateTime)){
					defaultCreateTime=createTime==null?new Timestamp(System.currentTimeMillis()):createTime;
					defaultAcSeq=acSeq;
				}
			}
			if(defaultAcSeq!=null){//注册时间最早的账户为手机银行默认账户
				ecacctRuleList.add(new Object[]{
						defaultAcSeq,"#","PMBS.DefaultAcNo","1"
				});
			}
		}
		batchAction.excuteBatch(sqlMapECIF, insertECACCT, ecacctList);
		batchAction.excuteBatch(sqlMapECIF, insertECACCTMCH, ecacctMchList);
		batchAction.excuteBatch(sqlMapECIF, insertECACCTRULE, ecacctRuleList);
		batchAction.excuteBatch(sqlMapECIF, insertECUSRMCHACCT, ecuserMchAcctList);

		// 将迁移标志更新为成功
		batchAction.excuteBatch(this.getSqlMapTMP(), BATCHSQL.updateMAccountTmpData, DataUtil.tmpMList(list,"OK"));
		
	}

	//账户表
	private final String insertECACCT = "insert into ECACCT "
			+ "(CIFSEQ,ACSEQ,DEPTSEQ,BANKACTYPE,ACNO,ACNAME,CURRENCY,"
			+ " CRFLAG,CREATEDEPTSEQ,CREATETIME,ASSOCIFACFLAG,ACSTATE,UPDATEUSERSEQ)"
			+ "values(?,?,?,?,?,?,?,?,?,?,'0',?,'9527')";
	//账户渠道
	private final String insertECACCTMCH = "insert into ECACCTMCH "
			+ "(ACSEQ,CREATEDEPTSEQ,CREATETIME,MCHANNELID)"
			+ "values(?,?,?,'PMBS')";
	//渠道规则
	private final String insertECACCTRULE ="insert into ECACCTRULE "
			+ "(ACSEQ,RULENAMESPACE,RULETYPE,RULEID,RULEDEF)"
			+ "values(?,?,'AcDef',?,?)";

	private final String insertECUSRMCHACCT ="insert into ECUSRMCHACCT "
			+ "(ACSEQ,USERSEQ,CREATEDEPTSEQ,CREATETIME,MCHANNELID)"
			+ "values(?,?,?,?,'PMBS')";

}
