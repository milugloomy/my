package com.sjqy.pmbs;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dataUtil.BATCHSQL;
import com.dataUtil.DataUtil;
import com.sjqy.common.DataMigrationAction;
import com.sjqy.query.ParamMapService;
import com.sjqy.query.ProvCityBankService;

@Service("MPayee")
public class MPayee extends DataMigrationAction {
	
	@Autowired
	private ProvCityBankService provCityBankService;
	@Autowired
	private ParamMapService paramMapService;

	@Override
	public void execute(Map<String,Object> context){
		List<Map> list=(List<Map>) context.get("List");
		
		List<Object[]> ecifBankInnerPayeeList=new ArrayList<Object[]>();
		List<Object[]> ecifCrossBankPayeeList=new ArrayList<Object[]>();
		List<Object[]> utilitiesQuickPaymentList=new ArrayList<Object[]>();
		for(int i=0;i<list.size();i++){
			String cifSeq=(String) list.get(i).get("CIFSEQ");
			String cifNo=(String) list.get(i).get("CIFNO");
			
			//转账收款人
			Map<String,Object> paraMap=new HashMap<String,Object>();
			paraMap.put("CifNo", cifNo);
			paraMap.put("CifSeq", cifSeq);
			//18位身份证
			List<Map> oldMPayeeList = (List<Map>) this.sqlMapMOBILE.queryForList("mobile.queryOldMPayee", paraMap);
			//X结尾身份证
   			if(oldMPayeeList.size()==0 && cifNo.endsWith("X")){
				paraMap.put("CifNo", DataUtil.getxCifNo(cifNo));
				oldMPayeeList=(List<Map>) this.sqlMapMOBILE.queryForList("mobile.queryOldMPayee", paraMap);
			//15位身份证
			}else if(oldMPayeeList.size()==0 && cifNo.length()==19){
				paraMap.put("CifNo", DataUtil.get15CifNo(cifNo));
				oldMPayeeList=(List<Map>) this.sqlMapMOBILE.queryForList("mobile.queryOldMPayee", paraMap);
			}
			for(int j=0;j<oldMPayeeList.size();j++){
				Map oldMPayeeMap=oldMPayeeList.get(j);
				String infoType=((BigDecimal) oldMPayeeMap.get("INFO_TYPE")).toString();
				String payeeAcNo=((String) oldMPayeeMap.get("RCV_ACCT_NO")).trim();
				String payeeAcName=(String) oldMPayeeMap.get("RCV_ACCT_NAME");
				//行内收款人
				if("1".equals(infoType)){
					ecifBankInnerPayeeList.add(new Object[]{
							cifSeq,payeeAcNo,payeeAcName
					});
				}else{//跨行
					String payeeDeptName=(String) oldMPayeeMap.get("OPEN_BRAN_NAME");
					if(payeeDeptName!=null) payeeDeptName=payeeDeptName.trim();
					String bankCode=(String)oldMPayeeMap.get("OPEN_BRAN_CODE");
					if(bankCode!=null) 
						bankCode=bankCode.trim();
					else{
						log.error("收款账户为"+payeeAcNo+"infoType为"+infoType+"的行号不存在");
						continue;
					}
					
					if("2".equals(infoType)){//同城跨行收款人
						if(paramMapService.getInCityBankCode(bankCode)!=null)
							bankCode=paramMapService.getInCityBankCode(bankCode);
						
						Map bankMap=(Map)this.sqlMapEIP.queryForObject("eip.queryApsRtgsNode",bankCode);
						
						String payeeBankId=null;
						String payeeBankName=null;
						String cityCode=null;
						String cityName=(String) oldMPayeeMap.get("CITY_NAME");
						String provinceCode=null;
						String provinceName=(String) oldMPayeeMap.get("PROVICE");
						if(bankMap==null){
							log.error("同城bankcode为"+bankCode+"的收款行不存在");
						}else{
							payeeBankId=(String) bankMap.get("CLSCODE");
							Map bankNameMap=provCityBankService.queryBankNameByBankId(payeeBankId);
							payeeBankName=(String) bankNameMap.get("BANKNAME");
							cityCode=(String) bankMap.get("CITYCODE");
							
							Map cityProvMap=provCityBankService.queryCityProvByCityCode(cityCode);
							cityName=(String) cityProvMap.get("CITYNAME");
							provinceCode=(String) cityProvMap.get("PROVCD");
							provinceName=(String) cityProvMap.get("PROVNAME");
						}
						
						ecifCrossBankPayeeList.add(new Object[]{
								cifSeq,payeeAcNo,payeeAcName,cityCode,cityName,provinceCode,provinceName,
								payeeBankId,payeeBankName,bankCode,payeeDeptName,bankCode,"D"
						});
					}
					if("3".equals(infoType)){//异地跨行收款人
						Map bankMap=(Map)this.sqlMapEIP.queryForObject("eip.queryApsRtgsNode",bankCode);
						String payeeBankId=null;
						String payeeBankName=null;
						String cityCode=null;
						String cityName=(String) oldMPayeeMap.get("CITY_NAME");
						String provinceCode=null;
						String provinceName=(String) oldMPayeeMap.get("PROVICE");
						if(bankMap==null){
							log.error("bankcode为"+bankCode+"的收款行不存在");
						}else{
							payeeBankId=(String) bankMap.get("CLSCODE");
							Map bankNameMap=provCityBankService.queryBankNameByBankId(payeeBankId);
							if (null==bankNameMap) {
								continue;
							}
							payeeBankName=(String) bankNameMap.get("BANKNAME");						
							cityCode=(String) bankMap.get("CITYCODE");
							
							Map cityProvMap=provCityBankService.queryCityProvByCityCode(cityCode);
							cityName=(String) cityProvMap.get("CITYNAME");
							provinceCode=(String) cityProvMap.get("PROVCD");
							provinceName=(String) cityProvMap.get("PROVNAME");
						}
						ecifCrossBankPayeeList.add(new Object[]{
								cifSeq,payeeAcNo,payeeAcName,cityCode,cityName,provinceCode,provinceName,
								payeeBankId,payeeBankName,bankCode,payeeDeptName,bankCode,"D"
						});
					}
					//超级网银收款人
					if("8".equals(infoType)){
						ecifCrossBankPayeeList.add(new Object[]{
								cifSeq,payeeAcNo,payeeAcName,null,null,null,null,
								null,null,bankCode,payeeDeptName,null,"P"
						});
					}
				}
				
			}
			//缴费收款人
			List<Map> oldPayList = (List<Map>) this.sqlMapMOBILE.queryForList("mobile.queryOldPay", cifNo);
			for (Map oldPayMap : oldPayList) {
				String jnlNo=(String)this.sqlMapEIP.queryForObject("eip.queryJnlNo");
				
				String bus_type=((String)oldPayMap.get("BUS_TYPE")).trim();
				String area_type=(String)oldPayMap.get("AREA_TYPE");
				Map<String,String> payTypes=paramMapService.getPayTypes(bus_type,area_type);
				if(payTypes==null)//此类数据不迁移
					continue;
				String utilitiesCode=payTypes.get("UtilitiesCode");
				String unitCode=payTypes.get("UnitCode");
				String paymentCode=payTypes.get("PaymentCode");
				String transCode=payTypes.get("TransCode");
				if(transCode==null||transCode.equals(""))transCode="o_O";

				String bus_no=(String)oldPayMap.get("BUS_NO");
				if(bus_no==null || bus_no.equals(""))//缴费号为空
					continue;
				bus_no=bus_no.trim();
				
				String cifName=(String) this.sqlMapECIF.queryForObject("ecif.queryCifNameByCifSeq",cifSeq);
				Date date=new Date(System.currentTimeMillis());
				utilitiesQuickPaymentList.add(new Object[]{
					jnlNo,cifSeq,bus_no,utilitiesCode,paymentCode,
					unitCode,date,transCode,cifName
				});
			}
		}
		this.batchAction.excuteBatch(sqlMapECIF, insertECIFBANKINNERPAYEE, ecifBankInnerPayeeList);
		this.batchAction.excuteBatch(sqlMapECIF, insertECIFCROSSBANKPAYEE, ecifCrossBankPayeeList);
		this.batchAction.excuteBatch(sqlMapEIP, insertUTILITIESQUICKPAYMENT, utilitiesQuickPaymentList);
		
		this.batchAction.excuteBatch(this.getSqlMapTMP(), BATCHSQL.updateMPayeeTmpData, DataUtil.tmpMList(list,"OK"));

	}
	//行内收款人
	private final String insertECIFBANKINNERPAYEE ="insert into ECIFBANKINNERPAYEE "
			+ "(CIFSEQ,PAYEEACNO,PAYEEACNAME,USETYPE,PAYEENO)"
			+ "values(?,?,?,'I','_P'||PayeeNo.nextval)";
	//跨行收款人
	private final String insertECIFCROSSBANKPAYEE ="insert into ECIFCROSSBANKPAYEE "
			+ "(CIFSEQ,PAYEEACNO,PAYEEACNAME,CITYCODE,CITYNAME,CTRYSUBDIVCD,"
			+ " CTRYSUBDIVCDNAME,PAYEEBANKID,PAYEEBANKNAME,PAYEEDEPTID,PAYEEDEPTNAME,"
			+ " PAYEEEXNO,USETYPE,LIMITPERTRS,LIMITPERDAY,PAYEENO)"
			+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,0.00,0.00,'_P'||PayeeNo.nextval)";
	//缴费收款人
	private final String insertUTILITIESQUICKPAYMENT="insert into UTILITIESQUICKPAYMENT "
			+ "(JNLNO,CIFSEQ,AUTHCUSNO,CITYCODE,UTILITIESCODE,PAYMENTCODE,UNITCODE,PAYAMT,"
			+ "PAYDATE,TRANSCODE,CHANNELID,RCVCUSTNAME)"
			+ "values(?,?,?,'3330',?,?,?,0.00,?,?,'PIBS',?)";
}
