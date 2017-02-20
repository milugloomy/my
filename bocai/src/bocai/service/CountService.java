package bocai.service;

import org.springframework.stereotype.Service;

import bocai.bean.Bocai;

@Service("countService")
public class CountService {
	public Bocai countOne(long times, double corpus, double rate, double odds1){
		Bocai bocai=new Bocai();
		Double earn=0.0;
		Double earnMax=null;
		Double earnMin=null;
		long win=0;
		long lose=0;
		for(int j=0;j<times;j++){
			if(Math.random()<rate){//单次猜对一个
				earn=earn+corpus*(odds1-1);
				win++;
			}else{
				earn=earn-corpus;
				lose++;
			}
			if(earnMax==null || earnMax<earn)
				earnMax=earn;
			if(earnMin==null || earnMin>earn)
				earnMin=earn;
		}
		bocai.setEarn(earn);
		bocai.setEarnMax(earnMax);
		bocai.setEarnMin(earnMin);
		bocai.setWin(win);
		bocai.setLose(lose);
		return bocai;
	}
	
	public Bocai countTwoOne(long times, double corpus, double rate, double odds1){
		Bocai bocai=new Bocai();
		Double earn=0.0;
		Double earnMax=null;
		Double earnMin=null;
		long win=0;
		long lose=0;
		for(int j=0;j<times;j++){
			if(Math.random()<rate
					&& Math.random()<rate){//2个都要猜对
				earn=earn+corpus*(odds1-1);
				win++;
			}else{
				earn=earn-corpus;
				lose++;
			}
			if(earnMax==null || earnMax<earn)
				earnMax=earn;
			if(earnMin==null || earnMin>earn)
				earnMin=earn;
		}
		bocai.setEarn(earn);
		bocai.setEarnMax(earnMax);
		bocai.setEarnMin(earnMin);
		bocai.setWin(win);
		bocai.setLose(lose);
		return bocai;
	}
	
	public Bocai countThreeOne(long times, double corpus, double rate, double odds1){
		Bocai bocai=new Bocai();
		Double earn=0.0;
		Double earnMax=null;
		Double earnMin=null;
		long win=0;
		long lose=0;
		for(int j=0;j<times;j++){
			if(Math.random()<rate
					&& Math.random()<rate
					&& Math.random()<rate){//3个都要猜对
				earn=earn+corpus*(odds1-1);
				win++;
			}else{
				earn=earn-corpus;
				lose++;
			}
			if(earnMax==null || earnMax<earn)
				earnMax=earn;
			if(earnMin==null || earnMin>earn)
				earnMin=earn;
		}
		bocai.setEarn(earn);
		bocai.setEarnMax(earnMax);
		bocai.setEarnMin(earnMin);
		bocai.setWin(win);
		bocai.setLose(lose);
		return bocai;
	}
	
	public Bocai countThreeTwo(long times, double corpus, double rate, double odds1){
		Bocai bocai=new Bocai();
		Double earn=0.0;
		Double earnMax=null;
		Double earnMin=null;
		long win=0;
		long win32_2=0;
		long lose=0;
		for(int j=0;j<times;j++){
			Double ranA=Math.random();
			Double ranB=Math.random();
			Double ranC=Math.random();
			int numberA=(ranA<rate)==true?1:0;
			int numberB=(ranB<rate)==true?1:0;
			int numberC=(ranC<rate)==true?1:0;
			int number=numberA+numberB+numberC;
			if(number==3){
				earn=earn+corpus*(odds1-1);
				win++;
			}else if(number==2){
				earn=earn+corpus*(odds1-1)*1/3-corpus*2/3;
				win32_2++;
			}else{
				earn=earn-corpus;
				lose++;
			}
			if(earnMax==null || earnMax<earn)
				earnMax=earn;
			if(earnMin==null || earnMin>earn)
				earnMin=earn;
		}
		bocai.setEarn(earn);
		bocai.setEarnMax(earnMax);
		bocai.setEarnMin(earnMin);
		bocai.setWin(win);
		bocai.setWin32_2(win32_2);
		bocai.setLose(lose);
		return bocai;
	}
	
	public Bocai countThreeThree(long times, double corpus, double rate, double odds1, double odds2){
		Bocai bocai=new Bocai();
		Double earn=0.0;
		Double earnMax=null;
		Double earnMin=null;
		long win31=0;//3串1对
		long win32=0;//3串2对3场
		long win32_2=0;//3串2对2场
		for(int j=0;j<times;j++){
			Double ranA=Math.random();
			Double ranB=Math.random();
			Double ranC=Math.random();
			int numberA=(ranA<rate)==true?1:0;
			int numberB=(ranB<rate)==true?1:0;
			int numberC=(ranC<rate)==true?1:0;
			int number=numberA+numberB+numberC;
			//三串一
			if(number==3){
				earn=earn+corpus*(odds1-1)*1/4;
				win31++;
			}else{
				earn=earn-corpus*1/4;
			}
			//三串二
			if(number==3){
				earn=earn+corpus*(odds2-1)*3/4;
				win32++;
			}else if(number==2){
				earn=earn+corpus*(odds2-1)*1/4-corpus*2/4;
				win32_2++;
			}else{
				earn=earn-corpus*3/4;
			}
			if(earnMax==null || earnMax<earn)
				earnMax=earn;
			if(earnMin==null || earnMin>earn)
				earnMin=earn;
		}
		bocai.setEarn(earn);
		bocai.setEarnMax(earnMax);
		bocai.setEarnMin(earnMin);
		bocai.setWin31(win31);
		bocai.setWin32(win32);
		bocai.setWin32_2(win32_2);
		return bocai;
	}
}
