package com.sjqy.pmbs;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.sjqy.common.AbstractParentBatch;
import com.sjqy.common.DataMigrationAction;
import com.sjqy.query.ParamMapService;

@SuppressWarnings({"unchecked","rawtypes"})
@Service("MOBatchTransAction")
public class MOBatchTransAction extends AbstractParentBatch{
	
	@Autowired
	private @Qualifier("MAccount")DataMigrationAction trans;
	@Autowired
	private ParamMapService paramMapService;

	@Override
	public void execute(Map<String,Object> context){
		List<Object[]> utilitiesQuickPaymentList=new ArrayList<Object[]>();
		
		List<Map> oldPayList = (List<Map>) this.sqlMapMOBILE.queryForList("mobile.queryAllOldPay");
		for(Map oldPayMap:oldPayList){
			String jnlNo=(String)this.sqlMapEIP.queryForObject("eip.queryJnlNo");
			String clientId=(String)oldPayMap.get("CLIENTID");
			if(clientId==null)
				continue;
			clientId=clientId.trim();
			
			String cifSeq=(String) this.sqlMapECIF.queryForObject("ecif.queryCifSeqByCifNo", clientId);
			if(cifSeq==null)continue;
			
			String bus_type=((String)oldPayMap.get("BUS_TYPE")).trim();
			String area_type=(String)oldPayMap.get("AREA_TYPE");
			if(area_type!=null)area_type=area_type.trim();
			Map<String,String> payTypes=paramMapService.getPayTypes(bus_type,area_type);
			if(payTypes==null)
				continue;
			String utilitiesCode=payTypes.get("UtilitiesCode");
			String unitCode=payTypes.get("UnitCode");
			String paymentCode=payTypes.get("PaymentCode");
			String transCode=payTypes.get("TransCode");

			String bus_no=(String)oldPayMap.get("BUS_NO");
			if(bus_no==null || bus_no.equals(""))
				continue;
			bus_no=bus_no.trim();
			
			String cifName=(String) this.sqlMapECIF.queryForObject("ecif.queryCifNameByCifSeq",cifSeq);
			Date date=new Date(System.currentTimeMillis());
			utilitiesQuickPaymentList.add(new Object[]{
					jnlNo,cifSeq,bus_no,utilitiesCode,paymentCode,
					unitCode,date,transCode,cifName
				});
		}
		this.batchAction.excuteBatch(sqlMapEIP, insertUTILITIESQUICKPAYMENT, utilitiesQuickPaymentList);
		
		
	}
	private final String insertUTILITIESQUICKPAYMENT="insert into UTILITIESQUICKPAYMENT "
			+ "(JNLNO,CIFSEQ,AUTHCUSNO,CITYCODE,UTILITIESCODE,PAYMENTCODE,UNITCODE,PAYAMT,"
			+ "PAYDATE,TRANSCODE,CHANNELID,RCVCUSTNAME)"
			+ "values(?,?,?,'3330',?,?,?,0.00,?,?,'PIBS',?)";

	@Override
	public List<Map> beforeThreadRun() {
		return null;
	}
	

}
