package com.sjqy.query;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.dataUtil.DataUtil;
@Service("ParamMapService")
public class ParamMapService {
	private Set<String> dzpjSet;//����Ʊ��
	private Set<String> wydzSet;//��������
	private Set<String> dzzfSet;//����֧��
	private Set<String> shglSet;//�̻�����
	private Set<String> zfjsSet;//֧������
	private Set<String> bzjSet;//��֤��
	private Set<String> dfgzSet;//��������
	private Set<String> dbxSet;//������
	private Set<String> dsfcgSet;//���������
	private Set<String> jxtSet;//����ͨ
	private Set<String> ttbSet;//���챦
	private Set<String> kfmsszSet;//�۷�ģʽ����
	private Set<String> gzsxeszSet;//��ת˽�޶�����
	private Set<String> jbzhxxcxSet;//�����ʻ���Ϣ��ѯ
	private Set<String> frzhtzSet;//�����˻�͸֧
	private Set<String> wyglSet;//��������
	private Set<String> xxwhSet;//��Ϣά��
	private Set<String> ywfhSet;//ҵ�񸴺�
	
	public Set<String> getDzpjSet() {//����Ʊ��
		if(dzpjSet==null){
			dzpjSet=new HashSet<String>();
			//Ʊ�ݱ༭
			//��Ʊ�Ǽ�
			//��Ʊ
			//�ж�����
			//�ж������ѯ������
			//��ʾ��Ʊ����
			//��Ʊ�����ѯ������
			//��Ʊǩ��
			//��ʾ��������
			//��ʾ���������ѯ������
			//����ת������
			//����ת�������ѯ������
			//����ת��ǩ��
			//��������
			//���ֲ�ѯ������
			//׷��
			//��ѯ
		}
		return dzpjSet;
	}
	public Set<String> getWydzSet() {//��������
		if(wydzSet==null){
			wydzSet=new HashSet<String>();
			wydzSet.add("ent.MCFullPageQuery");//��ѯ���ж��˵�
			wydzSet.add("ent.MCTransStateInfoQuery");//���Ӷ���
			//�������
		}
		return wydzSet;
	}
	public Set<String> getDzzfSet() {//����֧��
		if(dzzfSet==null){
			dzzfSet=new HashSet<String>();
			dzzfSet.add("ent.MCEPaySign");//����֧��ǩԼ
			dzzfSet.add("ent.MCQueryEpayDetail");//֧����ϸ��ѯ
		}
		return dzzfSet;
	}
	public Set<String> getShglSet() {//�̻�����
		if(shglSet==null){
			shglSet=new HashSet<String>();
			shglSet.add("ent.MCMerInfoUpdate");//��Ϣά��
			shglSet.add("ent.MCAccountQuery");//�����ѯ
			shglSet.add("ent.MCReturnTrsQry");//�˻����ײ�ѯ
			//�����ļ����������ѯ�ϲ���
			shglSet.add("ent.MCCertificateUpload");//֤���ϴ�
			shglSet.add("ent.MCMerInfoQry");//�����˻��̻���Ϣ��ѯ
			shglSet.add("ent.MCReturnTrsSub");//�����˻� 
		}
		return shglSet;
	}
	public Set<String> getZfjsSet() {//֧������
		if(zfjsSet==null){
			zfjsSet=new HashSet<String>();
			zfjsSet.add("ent.MCEntInnerTransfer");//����ҵ���ڻ�ת
			zfjsSet.add("ent.MCBankInnerTransfer");//������ת��
			zfjsSet.add("ent.MCBatchTransfer");//����ҵ����ת��
			zfjsSet.add("ent.MCCrossBankTransfer");//����ת��
			zfjsSet.add("ent.MCTransferVoucherInfoQry");//���ӻص���ѯ
			zfjsSet.add("ent.MCPayeeAdd");//�տ�������
			zfjsSet.add("ent.MCQueryTransBankLoanInfo");//���ٿ���ת����ϸ��Ϣ��ѯ
			zfjsSet.add("ent.MCTransInfoQry");//����ת����ϸ��Ϣ��ѯ
			zfjsSet.add("ent.MCQueryTransSpecialInfo");//���ؿ���ת����ϸ��Ϣ��ѯ
			
		}
		return zfjsSet;
	}
	public Set<String> getBzjSet() {//��֤��
		if(bzjSet==null){
			bzjSet=new HashSet<String>();
			bzjSet.add("ent.MCQueryRecognizance");//��ѯ��֤�����
			bzjSet.add("ent.MCQueryRecgMingxi");//��֤����ϸ��ѯ
			bzjSet.add("ent.MCTransferCurToRecg");//��֤��ת��
		}
		return bzjSet;
	}
	public Set<String> getDfgzSet() {//��������
		if(dfgzSet==null){
			dfgzSet=new HashSet<String>();
			dfgzSet.add("ent.MCSalaryRegisterManage");//�ϴ�������ϸ
			dfgzSet.add("ent.MCAgentPayerSalary");//��������
			dfgzSet.add("ent.MCAgentSalaryQuery");//�������ʲ�ѯ
		}
		return dfgzSet;
	}
	public Set<String> getDbxSet() {//������
		if(dbxSet==null){
			dbxSet=new HashSet<String>();
			dbxSet.add("ent.MCAgentReimbursement");//���ñ���
			dbxSet.add("ent.MCAgentReimburseQuery");//���ñ�����ϸ��ѯ
		}
		return dbxSet;
	}
	public Set<String> getDsfcgSet() {//���������
		if(dsfcgSet==null){
			dsfcgSet=new HashSet<String>();
			dsfcgSet.add("ent.MCStockAcctBalanceQuery");//֤ȯ�ʽ��˻�����ѯ
			dsfcgSet.add("ent.MCStockMgrActBalQuery");//��֤ת�˽�����ϸ��ѯ
			dsfcgSet.add("ent.MCTransBankToStock");//����ת֤ȯ
			dsfcgSet.add("ent.MCTransStockToBank");//֤ȯת����
			dsfcgSet.add("ent.MCUpdateAcctRelation");//�˻���ϵ��ѯ
		}
		return dsfcgSet;
	}
	public Set<String> getJxtSet() {//����ͨ
		if(jxtSet==null){
			jxtSet=new HashSet<String>();
			jxtSet.add("ent.MCJxtSign");//����ͨǩԼ
			jxtSet.add("ent.MCJxtSignQry");//����ͨ����
		}
		return jxtSet;
	}
	public Set<String> getTtbSet() {//���챦
		if(ttbSet==null){
			ttbSet=new HashSet<String>();
			ttbSet.add("ent.MCTreasureStatis");//��Ϣ��ѯ
		}
		return ttbSet;
	}
	public Set<String> getKfmsszSet() {//�۷�ģʽ����
		if(kfmsszSet==null){
			kfmsszSet=new HashSet<String>();
			kfmsszSet.add("ent.MCFeeModelSet");//�۷�ģʽ����
		}
		return kfmsszSet;
	}
	public Set<String> getGzsxeszSet() {//��ת˽�޶�����
		if(gzsxeszSet==null){
			gzsxeszSet=new HashSet<String>();
			gzsxeszSet.add("ent.MCECAcLmtListQry");//��ת˽�޶�����
		}
		return gzsxeszSet;
	}
	public Set<String> getJbzhxxcxSet() {//�����ʻ���Ϣ��ѯ
		if(jbzhxxcxSet==null){
			jbzhxxcxSet=new HashSet<String>();
			jbzhxxcxSet.add("ent.MCActDetailInfoQuery");//��ѯ�����˻�
			jbzhxxcxSet.add("ent.MCTimTrsQry");//��ѯ�����˻�
			jbzhxxcxSet.add("ent.MCLoanQry");//��ѯ�˻�������Ϣ
			jbzhxxcxSet.add("ent.MCNoticeActQuery");//��ѯ֪ͨ�����Ϣ
			jbzhxxcxSet.add("ent.MCActTrsInfoQuery");//�˻���ʷ��ϸ��ѯ
		}
		return jbzhxxcxSet;
	}
	public Set<String> getFrzhtzSet() {//�����˻�͸֧
		if(frzhtzSet==null){
			frzhtzSet=new HashSet<String>();
			frzhtzSet.add("ent.MCQueryFatouAcctInfo");//��ѯ����͸֧�˻���Ϣ��ѯ
		}
		return frzhtzSet;
	}
	
	public Set<String> getWyglSet() {//��������
		if(wyglSet==null){
			wyglSet=new HashSet<String>();
			wyglSet.add("ent.MCEntAcRoleConfigQry");//�˻�Ȩ������
			wyglSet.add("ent.MCAccountAuthConfigModify");//������������
			wyglSet.add("ent.MCEntOperatorUpdate");//����Ա����
			wyglSet.add("ent.MCECIFRoleAdd");//��ɫ����
			wyglSet.add("ent.MCEntBasicInfoQuery");//�޸���ҵ�ͻ���Ϣ
			wyglSet.add("ent.MCTransStateInfoQuery");//��ѯ����״̬��Ϣ
			wyglSet.add("ent.MCEntOperatorInfoUpdate");//����Ա����
		}
		return wyglSet;
	}
	public Set<String> getXxwhSet() {//�ͻ�����
		if(xxwhSet==null){
			xxwhSet=new HashSet<String>();
			xxwhSet.add("ent.MCCertUpdate");//��ҵ֤�����
			xxwhSet.add("ent.MCUpdatePassword");//�����޸�
			xxwhSet.add("ent.MCOperatorJnlQry");//��ѯ����������־
			//��ѯ���˲���Ȩ��
			//��ѯɾ��֪ͨ��Ϣ
		}
		return xxwhSet;
	}
	public Set<String> getYwfhSet() {//ҵ�񸴺�
		if(ywfhSet==null){
			ywfhSet=new HashSet<String>();
			ywfhSet.add("ent.MCAEAuthorizeTrsQry");//ҵ�񸴺˲�ѯ
			ywfhSet.add("ent.MCATransactionAuthorized");//ҵ�񸴺�
		}
		return ywfhSet;
	}
	



	private HashMap<String, Object[]> moduleMap;//��������������ϲ�Ʒӳ��
	private Map<String, String> userStateMap;
	private Map<String, String> currencyMap;
	private Map<String, String> crFlagMap;
	private Map<String, String> bankAcTypeMap;
	private Map<String, String> idTypeMap;
	private Map<String, String> payeeMap;
	private Map<String, String> inCityBankMap;

	public void updateModuleCache(){
		moduleMap=new HashMap<String, Object[]>();
		moduleMap.put("3", new Object[]{"����Ʊ��", getDzpjSet()});
		moduleMap.put("4", new Object[]{"��������", getWydzSet()});
		moduleMap.put("6", new Object[]{"����֧��", getDzzfSet()});
		moduleMap.put("7", new Object[]{"�̻�����", getShglSet()});
		moduleMap.put("D", new Object[]{"֧������", getZfjsSet()});
		moduleMap.put("E", new Object[]{"֧������", getZfjsSet()});
		moduleMap.put("H", new Object[]{"֧������", getZfjsSet()});
		moduleMap.put("S", new Object[]{"֧������", getZfjsSet()});
		moduleMap.put("F", new Object[]{"��֤��", getBzjSet()});
		moduleMap.put("G", new Object[]{"��֤��", getBzjSet()});
		moduleMap.put("I", new Object[]{"��������", getDfgzSet()});
		moduleMap.put("J", new Object[]{"������", getDbxSet()});
		moduleMap.put("U", new Object[]{"���������", getDsfcgSet()});
		moduleMap.put("V", new Object[]{"���������", getDsfcgSet()});
		moduleMap.put("W", new Object[]{"���������", getDsfcgSet()});
		moduleMap.put("Z", new Object[]{"����ͨ", getJxtSet()});
		moduleMap.put("b", new Object[]{"���챦", getTtbSet()});
		moduleMap.put("T", new Object[]{"�۷�ģʽ����", getKfmsszSet()});
		moduleMap.put("9", new Object[]{"��ת˽�޶�����", getGzsxeszSet()});
		moduleMap.put("B", new Object[]{"�����ʻ���Ϣ��ѯ", getJbzhxxcxSet()});
		moduleMap.put("5", new Object[]{"�����˻�͸֧", getFrzhtzSet()});
		/*insert into eip.deptprodgroup values('����Ʊ��','0','ent','IBS','0');
		insert into eip.deptprodgroup values('��������','0','ent','IBS','0');
		insert into eip.deptprodgroup values('����֧��','0','ent','IBS','0');
		insert into eip.deptprodgroup values('�̻�����','0','ent','IBS','0');
		insert into eip.deptprodgroup values('֧������','0','ent','IBS','0');
		insert into eip.deptprodgroup values('��֤��','0','ent','IBS','0');
		insert into eip.deptprodgroup values('��������','0','ent','IBS','0');
		insert into eip.deptprodgroup values('������','0','ent','IBS','0');
		insert into eip.deptprodgroup values('���������','0','ent','IBS','0');
		insert into eip.deptprodgroup values('����ͨ','0','ent','IBS','0');
		insert into eip.deptprodgroup values('���챦','0','ent','IBS','0');
		insert into eip.deptprodgroup values('�۷�ģʽ����','0','ent','IBS','0');
		insert into eip.deptprodgroup values('��ת˽�޶�����','0','ent','IBS','0');
		insert into eip.deptprodgroup values('�����˻�͸֧','0','ent','IBS','0');
		insert into eip.deptprodgroup values('�����ʻ���Ϣ��ѯ','0','ent','IBS','0');
		insert into eip.deptprodgroup values('��������','0','ent','IBS','0');
		insert into eip.deptprodgroup values('��Ϣά��','0','ent','IBS','0');
		insert into eip.deptprodgroup values('ҵ�񸴺�','0','ent','IBS','0');
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
			currencyMap.put("01","CNY");//�����
			currencyMap.put("12","GBP");//Ӣ��
			currencyMap.put("13","HKD");//�۱�
			currencyMap.put("14","USD");//��Ԫ
			currencyMap.put("17","KRW");//��Ԫ
			currencyMap.put("18","SGD");//��Ԫ
			currencyMap.put("21","SEK");//��ʿ����
			currencyMap.put("27","JPY");//��Ԫ
			currencyMap.put("28","CAD");//��Ԫ
			currencyMap.put("29","AUD");//��Ԫ
			currencyMap.put("38","EUR");//ŷԪ
		}
		return currencyMap.get(oldCurrency);
	}
	public String getCrFlag(String oldCrFlag){
		if(crFlagMap==null){
			crFlagMap=new DataUtil.MyMap<String, String>("C");
			crFlagMap.put("1","R");//��
			crFlagMap.put("2","C");//��
		}
		return crFlagMap.get(oldCrFlag);
	}
	public String getBankAcTypeByAcNo(String acNo){
		if(acNo.startsWith("625988")||acNo.startsWith("628255")
				||acNo.startsWith("622899")||acNo.startsWith("622868"))
			return "PCRC";//���ÿ�
		if(acNo.startsWith("623112")||acNo.startsWith("621977"))
			return "PSAV";//��ǿ�
		return null;
	}
	public String getBankAcType(int bus_code,String acct_attr,String draw_mode){
		if(bus_code==1)
			return "PMCS";//���ڴ���
		if(acct_attr.equals("6")&&(draw_mode.equals("1")||draw_mode.equals("3")))
			return "PPDC";//ƾ��֧���Ĵ��۽��㻧
		if(bus_code>=91 && bus_code<=95)
			return "PMCT";//����һ��ͨ
		if(bus_code==2 || bus_code==65)
			return "PTIM";//���ڴ浥
		return null;
		/*1-��ǿ�                 ���ݽ�ǿ���bin�ж�
		2-��˽���ڴ���            bus_code=1
		3-ƾ��֧���Ĵ��۽��㻧     basicinfo.acct_attr='6' and basicinfo.draw_mode in ('1','3')
		4-����һ��ͨ              bus_code in (91,92,93,94,95)
		5-���ڴ浥                bus_code in (2,65)
		9-�����˻�*/
		/*if(bankAcTypeMap==null){
			bankAcTypeMap=new HashMap<String,String>();
			bankAcTypeMap.put("1", "PSAV");//��ǿ�
			bankAcTypeMap.put("2", "PMCS");//���ڴ���
			bankAcTypeMap.put("3", "PPDC");//ƾ��֧���Ĵ��۽��㻧
			bankAcTypeMap.put("4", "PMCT");//����һ��ͨ
			bankAcTypeMap.put("5", "PTIM");//���ڴ浥
			bankAcTypeMap.put("91", "PMCT");//����һ��ͨ
		}
		return bankAcTypeMap.get(oldBankAcType);*/
	}
	public String getIdType(String oldIdType) {
		if(idTypeMap==null){
			idTypeMap=new DataUtil.MyMap<String, String>("P00");
			idTypeMap.put("1", "P00");//���֤
			idTypeMap.put("2", "P07");//���ڲ�
			idTypeMap.put("3", "P05");//����
			idTypeMap.put("4", "P01");//����֤
			idTypeMap.put("5", "P03");//����֤
			idTypeMap.put("6", "P08");//�۰ľ���ͨ��֤
			idTypeMap.put("7", "P12");//̨�����ͨ��֤
			idTypeMap.put("0", "P99");//����
		}
		return idTypeMap.get(oldIdType);
	}
	public String getGender(String idNo){
		if(idNo.length()==15){//15λ���֤
			String f=idNo.substring(14);
			if(f.equals("1")||f.equals("3")||f.equals("5")||f.equals("7")||f.equals("9"))
				return "M";
			else
				return "F";
		}
		if(idNo.length()==18){//18λ���֤
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
			inCityBankMap.put("306333000019", "306333003020");//�㷢������֧��
			inCityBankMap.put("306333000027", "306333003062");//�㷢�����ݳ���֧��
			inCityBankMap.put("103333022017", "103333022611");//ũ�п�����֧��
			inCityBankMap.put("306333000035", "306333003054");//�㷢������걺�֧��
			inCityBankMap.put("301333000057", "301333000024");//����ѧԺ·֧��
			inCityBankMap.put("306333000094", "306333003255");//�㷢�������³�֧��
			inCityBankMap.put("306333000060", "306333003159");//�㷢����������֧��
			inCityBankMap.put("306333000051", "306333003118");//�㷢����������֧��
			inCityBankMap.put("306333000086", "306333003191");//�㷢������˫��֧��
			inCityBankMap.put("103333020159", "103333021014");//ũ�г�������
			inCityBankMap.put("1105333000685", "105333000685");//�й��������������޶�֧��
		}
		return inCityBankMap.get(oldBankCode);
	}
	public String getCertState(Object object) {
		if(object==null){//δ����
			return "not_apply";
		}else{
			String state=((String)object).trim();
			if(state=="")//δ����
				return "not_apply";
			if(state.charAt(0)=='0')//
				return "";
			if(state.charAt(0)=='1')//����
				return "N";
			if(state.charAt(0)=='6')//��֤��Ĳο���δ����
				return "U";
			if(state.charAt(0)=='7')//֤���������
				return "N";
			if(state.charAt(0)=='8')//����
				return "F";
			if(state.charAt(0)=='N')//����
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
		if(bus_type.equals("5")){//ˮ��
			payTypes.put("UtilitiesCode", "1");
			payTypes.put("UnitCode", "3330");
			payTypes.put("PaymentCode", "0011");
			payTypes.put("TransCode", "per.MCUtilitiesPayForWater");
			return payTypes;
		}
		if(bus_type.equals("6")){//���
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
		if(bus_type.equals("12")){//ȼ��
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
