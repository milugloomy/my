package com.sjqy.query;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.dataUtil.DataUtil;
@Service("ParamMapService")
public class ParamMapService {
	private Set<String> dzpjSet;//电子票据
	private Set<String> wydzSet;//网银对账
	private Set<String> dzzfSet;//电子支付
	private Set<String> shglSet;//商户管理
	private Set<String> zfjsSet;//支付结算
	private Set<String> bzjSet;//保证金
	private Set<String> dfgzSet;//代发工资
	private Set<String> dbxSet;//代报销
	private Set<String> dsfcgSet;//第三方存管
	private Set<String> jxtSet;//金信通
	private Set<String> ttbSet;//天天宝
	private Set<String> kfmsszSet;//扣费模式设置
	private Set<String> gzsxeszSet;//公转私限额设置
	private Set<String> jbzhxxcxSet;//基本帐户信息查询
	private Set<String> frzhtzSet;//法人账户透支
	private Set<String> wyglSet;//网银管理
	private Set<String> xxwhSet;//信息维护
	private Set<String> ywfhSet;//业务复核
	
	public Set<String> getDzpjSet() {//电子票据
		if(dzpjSet==null){
			dzpjSet=new HashSet<String>();
			//票据编辑
			//出票登记
			//撤票
			//承兑申请
			//承兑申请查询及撤销
			//提示收票申请
			//收票申请查询及撤销
			//收票签收
			//提示付款申请
			//提示付款申请查询及撤销
			//背书转让申请
			//背书转让申请查询及撤销
			//背书转让签收
			//贴现申请
			//贴现查询及撤销
			//追索
			//查询
		}
		return dzpjSet;
	}
	public Set<String> getWydzSet() {//网银对账
		if(wydzSet==null){
			wydzSet=new HashSet<String>();
			wydzSet.add("ent.MCFullPageQuery");//查询银行对账单
			wydzSet.add("ent.MCTransStateInfoQuery");//电子对账
			//银企对账
		}
		return wydzSet;
	}
	public Set<String> getDzzfSet() {//电子支付
		if(dzzfSet==null){
			dzzfSet=new HashSet<String>();
			dzzfSet.add("ent.MCEPaySign");//网上支付签约
			dzzfSet.add("ent.MCQueryEpayDetail");//支付明细查询
		}
		return dzzfSet;
	}
	public Set<String> getShglSet() {//商户管理
		if(shglSet==null){
			shglSet=new HashSet<String>();
			shglSet.add("ent.MCMerInfoUpdate");//信息维护
			shglSet.add("ent.MCAccountQuery");//账务查询
			shglSet.add("ent.MCReturnTrsQry");//退货交易查询
			//对账文件（与账务查询合并）
			shglSet.add("ent.MCCertificateUpload");//证书上传
			shglSet.add("ent.MCMerInfoQry");//交易退货商户信息查询
			shglSet.add("ent.MCReturnTrsSub");//交易退货 
		}
		return shglSet;
	}
	public Set<String> getZfjsSet() {//支付结算
		if(zfjsSet==null){
			zfjsSet=new HashSet<String>();
			zfjsSet.add("ent.MCEntInnerTransfer");//本企业帐内互转
			zfjsSet.add("ent.MCBankInnerTransfer");//本行内转账
			zfjsSet.add("ent.MCBatchTransfer");//本企业批量转账
			zfjsSet.add("ent.MCCrossBankTransfer");//跨行转账
			zfjsSet.add("ent.MCTransferVoucherInfoQry");//电子回单查询
			zfjsSet.add("ent.MCPayeeAdd");//收款人名册
			zfjsSet.add("ent.MCQueryTransBankLoanInfo");//快速跨行转账详细信息查询
			zfjsSet.add("ent.MCTransInfoQry");//跨行转账详细信息查询
			zfjsSet.add("ent.MCQueryTransSpecialInfo");//蓟县跨行转账详细信息查询
			
		}
		return zfjsSet;
	}
	public Set<String> getBzjSet() {//保证金
		if(bzjSet==null){
			bzjSet=new HashSet<String>();
			bzjSet.add("ent.MCQueryRecognizance");//查询保证金余额
			bzjSet.add("ent.MCQueryRecgMingxi");//保证金明细查询
			bzjSet.add("ent.MCTransferCurToRecg");//保证金转账
		}
		return bzjSet;
	}
	public Set<String> getDfgzSet() {//代发工资
		if(dfgzSet==null){
			dfgzSet=new HashSet<String>();
			dfgzSet.add("ent.MCSalaryRegisterManage");//上传工资明细
			dfgzSet.add("ent.MCAgentPayerSalary");//代发工资
			dfgzSet.add("ent.MCAgentSalaryQuery");//代发工资查询
		}
		return dfgzSet;
	}
	public Set<String> getDbxSet() {//代报销
		if(dbxSet==null){
			dbxSet=new HashSet<String>();
			dbxSet.add("ent.MCAgentReimbursement");//费用报销
			dbxSet.add("ent.MCAgentReimburseQuery");//费用报销明细查询
		}
		return dbxSet;
	}
	public Set<String> getDsfcgSet() {//第三方存管
		if(dsfcgSet==null){
			dsfcgSet=new HashSet<String>();
			dsfcgSet.add("ent.MCStockAcctBalanceQuery");//证券资金账户余额查询
			dsfcgSet.add("ent.MCStockMgrActBalQuery");//银证转账交易明细查询
			dsfcgSet.add("ent.MCTransBankToStock");//银行转证券
			dsfcgSet.add("ent.MCTransStockToBank");//证券转银行
			dsfcgSet.add("ent.MCUpdateAcctRelation");//账户关系查询
		}
		return dsfcgSet;
	}
	public Set<String> getJxtSet() {//金信通
		if(jxtSet==null){
			jxtSet=new HashSet<String>();
			jxtSet.add("ent.MCJxtSign");//金信通签约
			jxtSet.add("ent.MCJxtSignQry");//金信通管理
		}
		return jxtSet;
	}
	public Set<String> getTtbSet() {//天天宝
		if(ttbSet==null){
			ttbSet=new HashSet<String>();
			ttbSet.add("ent.MCTreasureStatis");//信息查询
		}
		return ttbSet;
	}
	public Set<String> getKfmsszSet() {//扣费模式设置
		if(kfmsszSet==null){
			kfmsszSet=new HashSet<String>();
			kfmsszSet.add("ent.MCFeeModelSet");//扣费模式设置
		}
		return kfmsszSet;
	}
	public Set<String> getGzsxeszSet() {//公转私限额设置
		if(gzsxeszSet==null){
			gzsxeszSet=new HashSet<String>();
			gzsxeszSet.add("ent.MCECAcLmtListQry");//公转私限额设置
		}
		return gzsxeszSet;
	}
	public Set<String> getJbzhxxcxSet() {//基本帐户信息查询
		if(jbzhxxcxSet==null){
			jbzhxxcxSet=new HashSet<String>();
			jbzhxxcxSet.add("ent.MCActDetailInfoQuery");//查询活期账户
			jbzhxxcxSet.add("ent.MCTimTrsQry");//查询定期账户
			jbzhxxcxSet.add("ent.MCLoanQry");//查询账户贷款信息
			jbzhxxcxSet.add("ent.MCNoticeActQuery");//查询通知存款信息
			jbzhxxcxSet.add("ent.MCActTrsInfoQuery");//账户历史明细查询
		}
		return jbzhxxcxSet;
	}
	public Set<String> getFrzhtzSet() {//法人账户透支
		if(frzhtzSet==null){
			frzhtzSet=new HashSet<String>();
			frzhtzSet.add("ent.MCQueryFatouAcctInfo");//查询法人透支账户信息查询
		}
		return frzhtzSet;
	}
	
	public Set<String> getWyglSet() {//网银管理
		if(wyglSet==null){
			wyglSet=new HashSet<String>();
			wyglSet.add("ent.MCEntAcRoleConfigQry");//账户权限设置
			wyglSet.add("ent.MCAccountAuthConfigModify");//审批流程设置
			wyglSet.add("ent.MCEntOperatorUpdate");//操作员设置
			wyglSet.add("ent.MCECIFRoleAdd");//角色设置
			wyglSet.add("ent.MCEntBasicInfoQuery");//修改企业客户信息
			wyglSet.add("ent.MCTransStateInfoQuery");//查询交易状态信息
			wyglSet.add("ent.MCEntOperatorInfoUpdate");//操作员管理
		}
		return wyglSet;
	}
	public Set<String> getXxwhSet() {//客户服务
		if(xxwhSet==null){
			xxwhSet=new HashSet<String>();
			xxwhSet.add("ent.MCCertUpdate");//企业证书更新
			xxwhSet.add("ent.MCUpdatePassword");//密码修改
			xxwhSet.add("ent.MCOperatorJnlQry");//查询网银交易日志
			//查询本人操作权限
			//查询删除通知信息
		}
		return xxwhSet;
	}
	public Set<String> getYwfhSet() {//业务复核
		if(ywfhSet==null){
			ywfhSet=new HashSet<String>();
			ywfhSet.add("ent.MCAEAuthorizeTrsQry");//业务复核查询
			ywfhSet.add("ent.MCATransactionAuthorized");//业务复核
		}
		return ywfhSet;
	}
	



	private HashMap<String, Object[]> moduleMap;//新老网银功能组合产品映射
	private Map<String, String> userStateMap;
	private Map<String, String> currencyMap;
	private Map<String, String> crFlagMap;
	private Map<String, String> bankAcTypeMap;
	private Map<String, String> idTypeMap;
	private Map<String, String> payeeMap;
	private Map<String, String> inCityBankMap;

	public void updateModuleCache(){
		moduleMap=new HashMap<String, Object[]>();
		moduleMap.put("3", new Object[]{"电子票据", getDzpjSet()});
		moduleMap.put("4", new Object[]{"网银对账", getWydzSet()});
		moduleMap.put("6", new Object[]{"电子支付", getDzzfSet()});
		moduleMap.put("7", new Object[]{"商户管理", getShglSet()});
		moduleMap.put("D", new Object[]{"支付结算", getZfjsSet()});
		moduleMap.put("E", new Object[]{"支付结算", getZfjsSet()});
		moduleMap.put("H", new Object[]{"支付结算", getZfjsSet()});
		moduleMap.put("S", new Object[]{"支付结算", getZfjsSet()});
		moduleMap.put("F", new Object[]{"保证金", getBzjSet()});
		moduleMap.put("G", new Object[]{"保证金", getBzjSet()});
		moduleMap.put("I", new Object[]{"代发工资", getDfgzSet()});
		moduleMap.put("J", new Object[]{"代报销", getDbxSet()});
		moduleMap.put("U", new Object[]{"第三方存管", getDsfcgSet()});
		moduleMap.put("V", new Object[]{"第三方存管", getDsfcgSet()});
		moduleMap.put("W", new Object[]{"第三方存管", getDsfcgSet()});
		moduleMap.put("Z", new Object[]{"金信通", getJxtSet()});
		moduleMap.put("b", new Object[]{"天天宝", getTtbSet()});
		moduleMap.put("T", new Object[]{"扣费模式设置", getKfmsszSet()});
		moduleMap.put("9", new Object[]{"公转私限额设置", getGzsxeszSet()});
		moduleMap.put("B", new Object[]{"基本帐户信息查询", getJbzhxxcxSet()});
		moduleMap.put("5", new Object[]{"法人账户透支", getFrzhtzSet()});
		/*insert into eip.deptprodgroup values('电子票据','0','ent','IBS','0');
		insert into eip.deptprodgroup values('网银对账','0','ent','IBS','0');
		insert into eip.deptprodgroup values('电子支付','0','ent','IBS','0');
		insert into eip.deptprodgroup values('商户管理','0','ent','IBS','0');
		insert into eip.deptprodgroup values('支付结算','0','ent','IBS','0');
		insert into eip.deptprodgroup values('保证金','0','ent','IBS','0');
		insert into eip.deptprodgroup values('代发工资','0','ent','IBS','0');
		insert into eip.deptprodgroup values('代报销','0','ent','IBS','0');
		insert into eip.deptprodgroup values('第三方存管','0','ent','IBS','0');
		insert into eip.deptprodgroup values('金信通','0','ent','IBS','0');
		insert into eip.deptprodgroup values('天天宝','0','ent','IBS','0');
		insert into eip.deptprodgroup values('扣费模式设置','0','ent','IBS','0');
		insert into eip.deptprodgroup values('公转私限额设置','0','ent','IBS','0');
		insert into eip.deptprodgroup values('法人账户透支','0','ent','IBS','0');
		insert into eip.deptprodgroup values('基本帐户信息查询','0','ent','IBS','0');
		insert into eip.deptprodgroup values('网银管理','0','ent','IBS','0');
		insert into eip.deptprodgroup values('信息维护','0','ent','IBS','0');
		insert into eip.deptprodgroup values('业务复核','0','ent','IBS','0');
		*/
	}
	public void updateUserStateCache(){
		userStateMap=new DataUtil.MyMap<String, String>("N");
		userStateMap.put("00", "N");
		userStateMap.put("01", "N");
		userStateMap.put("10", "L");
		userStateMap.put("11", "L");
	}
	public Object[] getModule(String oldModule){
		if(moduleMap==null)
			updateModuleCache();
		return moduleMap.get(oldModule);
	}
	public String getUserState(String oldState){
		if(userStateMap==null)
			updateUserStateCache();
		return userStateMap.get(oldState);
	}
	public String getCifState(String oldState){
		if(oldState.indexOf("1")!=-1)
			return "N";
		for(int i=0;i<oldState.length();i++){
			if(oldState.charAt(i)=='0'){
				continue;
			}
			if(oldState.charAt(i)=='8'){
				return "F";
			}
			if(oldState.charAt(i)=='9'){
				return "C";
			}
		}
		return "N";
	}
	public String getAcState(String oldState){
		if(oldState.indexOf("1")!=-1)
			return "N";
		for(int i=0;i<oldState.length();i++){
			if(oldState.charAt(i)=='0'){
				continue;
			}
			if(oldState.charAt(i)=='8'){
				return "D";
			}
			if(oldState.charAt(i)=='9'){
				return "C";
			}
		}
		return "N";
	}
	public String getCurrency(String oldCurrency){
		if(currencyMap==null){
			currencyMap=new DataUtil.MyMap<String, String>("CNY");
			currencyMap.put("01","CNY");//人民币
			currencyMap.put("12","GBP");//英镑
			currencyMap.put("13","HKD");//港币
			currencyMap.put("14","USD");//美元
			currencyMap.put("17","KRW");//韩元
			currencyMap.put("18","SGD");//新元
			currencyMap.put("21","SEK");//瑞士法郎
			currencyMap.put("27","JPY");//日元
			currencyMap.put("28","CAD");//加元
			currencyMap.put("29","AUD");//澳元
			currencyMap.put("38","EUR");//欧元
		}
		return currencyMap.get(oldCurrency);
	}
	public String getCrFlag(String oldCrFlag){
		if(crFlagMap==null){
			crFlagMap=new DataUtil.MyMap<String, String>("C");
			crFlagMap.put("1","R");//汇
			crFlagMap.put("2","C");//钞
		}
		return crFlagMap.get(oldCrFlag);
	}
	public String getBankAcTypeByAcNo(String acNo){
		if(acNo.startsWith("625988")||acNo.startsWith("628255")
				||acNo.startsWith("622899")||acNo.startsWith("622868"))
			return "PCRC";//信用卡
		if(acNo.startsWith("623112")||acNo.startsWith("621977"))
			return "PSAV";//借记卡
		return null;
	}
	public String getBankAcType(int bus_code,String acct_attr,String draw_mode){
		if(bus_code==1)
			return "PMCS";//活期存折
		if(acct_attr.equals("6")&&(draw_mode.equals("1")||draw_mode.equals("3")))
			return "PPDC";//凭密支付的存折结算户
		if(bus_code>=91 && bus_code<=95)
			return "PMCT";//定期一本通
		if(bus_code==2 || bus_code==65)
			return "PTIM";//定期存单
		return null;
		/*1-借记卡                 根据借记卡卡bin判断
		2-对私活期存折            bus_code=1
		3-凭密支付的存折结算户     basicinfo.acct_attr='6' and basicinfo.draw_mode in ('1','3')
		4-定期一本通              bus_code in (91,92,93,94,95)
		5-定期存单                bus_code in (2,65)
		9-卡内账户*/
		/*if(bankAcTypeMap==null){
			bankAcTypeMap=new HashMap<String,String>();
			bankAcTypeMap.put("1", "PSAV");//借记卡
			bankAcTypeMap.put("2", "PMCS");//活期存折
			bankAcTypeMap.put("3", "PPDC");//凭密支付的存折结算户
			bankAcTypeMap.put("4", "PMCT");//定期一本通
			bankAcTypeMap.put("5", "PTIM");//定期存单
			bankAcTypeMap.put("91", "PMCT");//定期一本通
		}
		return bankAcTypeMap.get(oldBankAcType);*/
	}
	public String getIdType(String oldIdType) {
		if(idTypeMap==null){
			idTypeMap=new DataUtil.MyMap<String, String>("P00");
			idTypeMap.put("1", "P00");//身份证
			idTypeMap.put("2", "P07");//户口簿
			idTypeMap.put("3", "P05");//护照
			idTypeMap.put("4", "P01");//军官证
			idTypeMap.put("5", "P03");//警官证
			idTypeMap.put("6", "P08");//港澳居民通行证
			idTypeMap.put("7", "P12");//台湾居民通行证
			idTypeMap.put("0", "P99");//其他
		}
		return idTypeMap.get(oldIdType);
	}
	public String getGender(String idNo){
		if(idNo.length()==15){//15位身份证
			String f=idNo.substring(14);
			if(f.equals("1")||f.equals("3")||f.equals("5")||f.equals("7")||f.equals("9"))
				return "M";
			else
				return "F";
		}
		if(idNo.length()==18){//18位身份证
			String f=idNo.substring(16,17);
			if(f.equals("1")||f.equals("3")||f.equals("5")||f.equals("7")||f.equals("9"))
				return "M";
			else
				return "F";
		}
		return "U";
	}
	public String getPayeeType(String oldPayeeType){
		if(payeeMap==null){
			payeeMap=new HashMap<String,String>();
			payeeMap.put("0", "E");
			payeeMap.put("1", "P");
			payeeMap.put("2", "P");
			payeeMap.put("3", "P");
		}
		return payeeMap.get(oldPayeeType);
	}
	public String getInCityBankCode(String oldBankCode){
		if(inCityBankMap==null){
			inCityBankMap=new HashMap<String,String>();
			inCityBankMap.put("306333000019", "306333003020");//广发行温州支行
			inCityBankMap.put("306333000027", "306333003062");//广发行温州城南支行
			inCityBankMap.put("103333022017", "103333022611");//农行开发区支行
			inCityBankMap.put("306333000035", "306333003054");//广发行温州瓯海支行
			inCityBankMap.put("301333000057", "301333000024");//交行学院路支行
			inCityBankMap.put("306333000094", "306333003255");//广发行温州新城支行
			inCityBankMap.put("306333000060", "306333003159");//广发行温州南浦支行
			inCityBankMap.put("306333000051", "306333003118");//广发行温州龙湾支行
			inCityBankMap.put("306333000086", "306333003191");//广发行温州双屿支行
			inCityBankMap.put("103333020159", "103333021014");//农行城区分理处
			inCityBankMap.put("1105333000685", "105333000685");//中国建设银行温州罗东支行
		}
		return inCityBankMap.get(oldBankCode);
	}
	public String getCertState(Object object) {
		if(object==null){//未申请
			return "not_apply";
		}else{
			String state=((String)object).trim();
			if(state=="")//未申请
				return "not_apply";
			if(state.charAt(0)=='0')//
				return "";
			if(state.charAt(0)=='1')//正常
				return "N";
			if(state.charAt(0)=='6')//新证书的参考号未下载
				return "U";
			if(state.charAt(0)=='7')//证书更新申请
				return "N";
			if(state.charAt(0)=='8')//冻结
				return "F";
			if(state.charAt(0)=='N')//正常
				return "N";
		}
		return "not_apply";
	}
	public Map<String,String> getPayTypes(String bus_type, String area_type) {
		Map<String,String> payTypes=new HashMap<String,String>();
		/*if(bus_type.equals("1")){
			payTypes.put("UtilitiesCode", "10");
			payTypes.put("UnitCode", "1001");
			payTypes.put("PaymentCode", "0042");
			payTypes.put("TransCode", "per.MCUtilitiesChinaTelecomPhone");
			return payTypes;
		}
		if(bus_type.equals("2")){
			payTypes.put("UtilitiesCode", "13");
			payTypes.put("UnitCode", "1301");
			payTypes.put("PaymentCode", "0006");
			payTypes.put("TransCode", "per.MCUtilitiesChinaUnicomPhone");
			return payTypes;
		}*/
		if(bus_type.equals("5")){//水费
			payTypes.put("UtilitiesCode", "1");
			payTypes.put("UnitCode", "3330");
			payTypes.put("PaymentCode", "0011");
			payTypes.put("TransCode", "per.MCUtilitiesPayForWater");
			return payTypes;
		}
		if(bus_type.equals("6")){//电费
			if(area_type==null || !(area_type.equals("336001")
									||area_type.equals("336002")
									||area_type.equals("336003")  )){
				return null;
			}
			payTypes.put("UtilitiesCode", "2");
			payTypes.put("UnitCode", area_type);
			payTypes.put("PaymentCode", "0004");
			payTypes.put("TransCode", "per.MCUtilitiesPayForEletroc");
			return payTypes;
		}
		/*if(bus_type.equals("8")){
			payTypes.put("UtilitiesCode", "4");
			payTypes.put("UnitCode", "WZ007101");
			payTypes.put("PaymentCode", "0071");
			payTypes.put("TransCode", "");
			return payTypes;
		}
		if(bus_type.equals("9")){
			payTypes.put("UtilitiesCode", "13");
			payTypes.put("UnitCode", "1304");
			payTypes.put("PaymentCode", "0006");
			payTypes.put("TransCode", "per.MCUtilitiesChinaTelecomPhone");
			return payTypes;
		}*/
		if(bus_type.equals("12")){//燃气
			payTypes.put("UtilitiesCode", "3");
			payTypes.put("UnitCode", "3330"); 
			payTypes.put("PaymentCode", "0064"); 
			payTypes.put("TransCode", "per.MCUtilitiesPayForGas"); 
			return payTypes;
		}
		/*if(bus_type.equals("13")){
			payTypes.put("UtilitiesCode", "12");
			payTypes.put("UnitCode", "01");
			payTypes.put("PaymentCode", "0");
			payTypes.put("TransCode", "0");
			return payTypes;
		}*/
		
		return null;
	}
}
