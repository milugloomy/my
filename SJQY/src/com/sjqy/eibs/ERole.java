package com.sjqy.eibs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dataUtil.BATCHSQL;
import com.dataUtil.DataUtil;
import com.sjqy.common.DataMigrationAction;
import com.sjqy.query.BankProdService;
import com.sjqy.query.ParamMapService;

@Service("ERole")
public class ERole extends DataMigrationAction {

	@Autowired
	private BankProdService bankProdService;
	@Autowired
	private ParamMapService paramMapService;

	@Override
	public void execute(Map<String,Object> context){
		List<Map> list=(List<Map>) context.get("List");
		
		ArrayList<Object[]> eccifRoleList=new ArrayList<Object[]>();
		ArrayList<Object[]> ecusrRoleList=new ArrayList<Object[]>();
		ArrayList<Object[]> eccifRolePrdList=new ArrayList<Object[]>();
		ArrayList<Object[]> eccifRolePrdAcList=new ArrayList<Object[]>();
		ArrayList<Object[]> eccifRoleAcList=new ArrayList<Object[]>();
		ArrayList<Object[]> ecusrMchAcctList=new ArrayList<Object[]>();

		Set<String> moduleSet=new HashSet<String>();
		Map<String,Object> paraMap=new HashMap<String,Object>();
		for(int i=0;i<list.size();i++){
			String cifSeq=(String) list.get(i).get("CIFSEQ");
			String cifNo=(String) list.get(i).get("CIFNO");
			//角色信息
			List<Map> oldEUserList=(List<Map>) sqlMapOLD.queryForList("old.queryOldEUser", cifNo);
			for(int j=0;j<oldEUserList.size();j++){
				Map oldEUserMap=oldEUserList.get(j);
				String userId=(String)oldEUserMap.get("user_loginid");
				String roleSeq=(String)sqlMapECIF.queryForObject("ecif.queryRoleSeq");
				
				paraMap=new HashMap<String,Object>();
				paraMap.put("USERID", userId);
				paraMap.put("CIFSEQ", cifSeq);
				String userSeq=(String)sqlMapECIF.queryForObject("ecif.queryUserSeqByUserIdAndCifSeq",paraMap);

				eccifRoleList.add(new Object[]{
						roleSeq,cifSeq,"自定义角色"+(j+1)
				});
				ecusrRoleList.add(new Object[]{
						userSeq,roleSeq
				});
				
				//专业版
				if(oldEUserMap.get("user_modules")!=null){
					String[] oldModuleArr=((String)oldEUserMap.get("user_modules")).split(",");
					for(int m=0;m<oldModuleArr.length;m++){
						if(paramMapService.getModule(oldModuleArr[m])!=null){
							moduleSet.addAll((Set)paramMapService.getModule(oldModuleArr[m])[1]);
						}
					}
				//大众版 暂不迁移角色   登陆时候写死能操作交易
				}else{
					
				}
				//所有人添加信息维护角色
				moduleSet.addAll(paramMapService.getXxwhSet());
				//管理员添加网银管理和业务复核角色
				if("M".equals(oldEUserMap.get("user_type"))){
					moduleSet.addAll(paramMapService.getWyglSet());
					moduleSet.addAll(paramMapService.getYwfhSet());
				}
				
				//获取该用户能操作acseq列表
				paraMap=new HashMap<String,Object>();
				paraMap.put("CIFNO", cifNo);
				paraMap.put("USERID", userId);
				List<Map> userAcctList=sqlMapOLD.queryForList("old.queryEUserAcct", paraMap);
				List<String> acSeqList=new ArrayList<String>();
				for(int m=0;m<userAcctList.size();m++){
					String acNo=(String) userAcctList.get(m).get("acno");
					paraMap=new HashMap<String,Object>();
					paraMap.put("ACNO", acNo);
					paraMap.put("CIFSEQ", cifSeq);
					String acSeq=(String)sqlMapECIF.queryForObject("ecif.queryAcSeqByAcNo",paraMap);
					if(acSeq!=null)
						acSeqList.add(acSeq);
				}
				//添加角色
				Iterator<String> it=moduleSet.iterator();
				while(it.hasNext()){
					String prdid=it.next();
					Map prdMap=bankProdService.queryBankProdById(prdid);
					if(prdMap==null){
						System.out.println(prdid+"产品不存在");
						continue;
					}
					if("1".equals(prdMap.get("ACFLAG"))){
						for(int k=0;k<acSeqList.size();k++){
							String acSeq=acSeqList.get(k);
							if("1".equals(prdMap.get("PRDCHECKFLAG"))){
								eccifRolePrdAcList.add(new Object[]{
										roleSeq,prdid,acSeq,1,1
								});
							}else{
								eccifRolePrdAcList.add(new Object[]{
										roleSeq,prdid,acSeq,0,0
								});
							}
						}
					}else{
						if("1".equals(prdMap.get("PRDCHECKFLAG"))){
							eccifRolePrdList.add(new Object[]{
									roleSeq,prdid,1,1
							});
						}
						else{
							eccifRolePrdList.add(new Object[]{
									roleSeq,prdid,0,0
							});
						}
					}
				}
				//用户、角色 和 账户序号 的对应
				for(int m=0;m<acSeqList.size();m++){
					String acSeq=acSeqList.get(m);
					eccifRoleAcList.add(new Object[]{
							roleSeq,acSeq
					});
					ecusrMchAcctList.add(new Object[]{
							userSeq,acSeq
					});

				}
			}
		}

		batchAction.excuteBatch(sqlMapECIF, insertECCIFROLE, eccifRoleList);
		batchAction.excuteBatch(sqlMapECIF, insertECUSRROLE, ecusrRoleList);
		batchAction.excuteBatch(sqlMapECIF, insertECCIFROLEPRD, eccifRolePrdList);
		batchAction.excuteBatch(sqlMapECIF, insertECCIFROLEPRDAC, eccifRolePrdAcList);
		batchAction.excuteBatch(sqlMapECIF, insertECCIFROLEAC, eccifRoleAcList);
		batchAction.excuteBatch(sqlMapECIF, insertECUSRMCHACCT, ecusrMchAcctList);


		// 将迁移标志更新为成功
		batchAction.excuteBatch(this.getSqlMapTMP(), BATCHSQL.updateERoleTmpData, DataUtil.tmpEList(list,"OK"));
	}

	private final String insertECCIFROLE = "insert into ECCIFROLE "
			+ "(ROLESEQ,CIFSEQ,ROLENAME,ROLESTATE,DEFAULTFLAG,ADMINROLEFLAG)"
			+ "values(?,?,?,'N',1,0)";

	private final String insertECUSRROLE = "insert into ECUSRROLE (USERSEQ,ROLESEQ)"
			+ "values(?,?)";

	private final String insertECCIFROLEPRD = "insert into ECCIFROLEPRD "
			+ "(ROLESEQ,PRDID,MAKERIGHT,AUTHRIGHT,CHECKRIGHT,AUTHGROUP,RELEASERIGHT)"
			+ "values(?,?,?,?,0,'0',0)";

	private final String insertECCIFROLEPRDAC = "insert into ECCIFROLEPRDAC "
			+ "(ROLESEQ,PRDID,ACSEQ,MAKERIGHT,AUTHRIGHT,CHECKRIGHT,AUTHGROUP,RELEASERIGHT)"
			+ "values(?,?,?,?,?,0,'0',0)";

	private final String insertECCIFROLEAC = "insert into ECCIFROLEAC(ROLESEQ,ACSEQ)"
			+ "values(?,?)";

	private final String insertECUSRMCHACCT = "insert into ECUSRMCHACCT "
			+ "(USERSEQ,ACSEQ,MCHANNELID)"
			+ "values(?,?,'EIBS')";

}
