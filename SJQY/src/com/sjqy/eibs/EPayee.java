package com.sjqy.eibs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dataUtil.BATCHSQL;
import com.dataUtil.DataUtil;
import com.sjqy.common.DataMigrationAction;
import com.sjqy.query.ParamMapService;
import com.sjqy.query.ProvCityBankService;

@Service("EPayee")
public class EPayee extends DataMigrationAction {
	
	@Autowired
	private ProvCityBankService provCityBankService;
	@Autowired
	private ParamMapService paramService;

	@Override
	public void execute(Map<String,Object> context){
		List<Map> list=(List<Map>) context.get("List");
		
		List<Object[]> ecifBankInnerPayeeList=new ArrayList<Object[]>();
		List<Object[]> ecifCrossBankPayeeList=new ArrayList<Object[]>();
		for(int i=0;i<list.size();i++){
			String cifSeq=(String) list.get(i).get("CIFSEQ");
			String cifNo=(String) list.get(i).get("CIFNO");
			List<Map> oldPayeeList = (List<Map>) this.sqlMapOLD.queryForList("old.queryOldPayee", cifNo);
			for(int j=0;j<oldPayeeList.size();j++){
				Map oldPAccountMap=oldPayeeList.get(j);
				String sysFlag=(String) oldPAccountMap.get("payee_sysflag");
				String payeeAcNo=(String) oldPAccountMap.get("payee_acno");
				//账号太长不迁移
				if(payeeAcNo.getBytes().length>40)
					continue;

				String payeeAcName=(String) oldPAccountMap.get("payee_acname");
				String payeeDeptName=(String) oldPAccountMap.get("payee_bank");
				String payeeType=paramService.getPayeeType((String) oldPAccountMap.get("payee_type"));
				//行内收款人
				if("0".equals(sysFlag)){
					ecifBankInnerPayeeList.add(new Object[]{
							cifSeq,payeeAcNo,payeeAcName,payeeType
					});
				}
				//跨行收款人
				if("1".equals(sysFlag)){
					//有重复记录取第一条
					Map bankMap;
					List<Map> bankMapList=(List<Map>)this.sqlMapIBS.queryForList("ibs.queryApsRtgsNodeByLName",payeeDeptName);
					if(bankMapList==null || bankMapList.size()==0)
						continue;
					else
						bankMap=bankMapList.get(0);
					String bankCode=(String) bankMap.get("BANKCODE");
					String payeeBankId=(String) bankMap.get("CLSCODE");
					if(provCityBankService.queryBankNameByBankId(payeeBankId)==null){
						System.out.println(payeeBankId);
					}
					String payeeBankName=(String) provCityBankService.queryBankNameByBankId(payeeBankId).get("BANKNAME");
					String cityCode=(String) bankMap.get("CITYCODE");
					
					Map cityProvMap=provCityBankService.queryCityProvByCityCode(cityCode);
					String cityName=(String) cityProvMap.get("CITYNAME");
					String provinceCode=(String) cityProvMap.get("PROVINCECODE");
					String provinceName=(String) cityProvMap.get("PROVINCENAME");
					
					ecifCrossBankPayeeList.add(new Object[]{
							cifSeq,payeeAcNo,payeeAcName,cityCode,cityName,provinceCode,provinceName,
							payeeBankId,payeeBankName,bankCode,bankCode,payeeDeptName,"D",null,null,payeeType
					});
				}
				//超级网银收款人
				if("2".equals(sysFlag)){
					String payeeIbsBankId=(String)oldPAccountMap.get("payee_bank_code");
					ecifCrossBankPayeeList.add(new Object[]{
							cifSeq,payeeAcNo,payeeAcName,null,null,null,null,
							null,null,null,null,null,"P",payeeIbsBankId,payeeDeptName,payeeType
					});
				}
			}
			
		}
		this.batchAction.excuteBatch(sqlMapECIF, insertECIFBANKINNERPAYEE, ecifBankInnerPayeeList);
		this.batchAction.excuteBatch(sqlMapECIF, insertECIFCROSSBANKPAYEE, ecifCrossBankPayeeList);
		
		this.batchAction.excuteBatch(this.getSqlMapTMP(), BATCHSQL.updateEPayeeTmpData, DataUtil.tmpEList(list,"OK"));
	}

	//行内收款人
	private final String insertECIFBANKINNERPAYEE = "insert into ECIFBANKINNERPAYEE "
			+ "(CIFSEQ,PAYEEACNO,PAYEEACNAME,USETYPE,PAYEENO,PAYEETYPE)"
			+ "values(?,?,?,'I','_E'||PayeeNo.nextval,?)";
	//跨行收款人
	private final String insertECIFCROSSBANKPAYEE = "insert into ECIFCROSSBANKPAYEE "
			+ "(CIFSEQ,PAYEEACNO,PAYEEACNAME,CITYCODE,CITYNAME,CTRYSUBDIVCD,CTRYSUBDIVCDNAME,"
			+ " PAYEEBANKID,PAYEEBANKNAME,PAYEEDEPTID,PAYEEEXNO,PAYEEDEPTNAME,USETYPE,"
			+ " PAYEEIBSBANKID,PAYEEIBSBANKNAME,LIMITPERTRS,LIMITPERDAY,PAYEENO,PAYEETYPE)"
			+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,0.00,0.00,'_E'||PayeeNo.nextval,?)";

}
