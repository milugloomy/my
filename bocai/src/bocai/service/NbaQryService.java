package bocai.service;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import bocai.common.Util;

@Service("nbaQryService")
public class NbaQryService {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	private DecimalFormat df;
	private SimpleDateFormat sdf=new SimpleDateFormat("yy年MM月dd日 HH:mm");
	
	public NbaQryService(){
		df=new DecimalFormat();
		df.applyPattern("#.00");
	}
	
	public Map<String,Object> totalRateQry(){
		//总数
		String totalSql="select count(*) from score where matchTime>?";
		Integer total=jdbcTemplate.queryForObject(totalSql,new Object[]{
				Util.seasonStart()
		} ,Integer.class);
		
		//各胜率
		String baseSql="select count(*) count from "
				+"(select substring_index(score,'-',1) as homeScore,totalPoints,"
				+"substring_index(score,'-',-1) as visitScore from score "
				+"where matchTime>?) x ";
		Map<String,Object> map=new HashMap<String,Object>();
		String qrySql=baseSql+" where CONVERT(homeScore,DECIMAL)>CONVERT(visitScore,DECIMAL)"
				+" union "+baseSql+" where CONVERT(homeScore,DECIMAL)<CONVERT(visitScore,DECIMAL)"
				+" union "+baseSql+" where CONVERT(homeScore+visitScore,DECIMAL)>CONVERT(totalPoints,DECIMAL)"
				+" union "+baseSql+" where CONVERT(homeScore+visitScore,DECIMAL)<CONVERT(totalPoints,DECIMAL)";
		List<Integer> list=jdbcTemplate.queryForList(qrySql,Integer.class, 
				new Object[]{Util.seasonStart(),Util.seasonStart(),Util.seasonStart(),Util.seasonStart()});
		int homeCount=list.get(0);
		int visitCount=list.get(1);
		int smallCount=list.get(2);
		int bigCount=list.get(3);
		map.put("homeCount", homeCount);
		map.put("visitCount", visitCount);
		map.put("smallCount", smallCount);
		map.put("bigCount", bigCount);
		Double homeRate=new Double(df.format(100.0*homeCount/total));
		Double visitRate=new Double(df.format(100.0*visitCount/total));
		Double smallRate=new Double(df.format(100.0*smallCount/total));
		Double bigRate=new Double(df.format(100.0*bigCount/total));
		map.put("homeRate", homeRate);
		map.put("visitRate", visitRate);
		map.put("smallRate", smallRate);
		map.put("bigRate", bigRate);
		
		//让分胜负
		String letBaseSql=" select count(*) count from "
				+"(select substring_index(score,'-',1)+letpoints as homeScore,"
				+"substring_index(score,'-',-1) as visitScore from score "
				+"where matchTime>?) x ";
		String letQrySql=letBaseSql+" where homeScore>visitScore"
				+" union "+letBaseSql+" where homeScore<visitScore";
		List<Integer> letList=jdbcTemplate.queryForList(letQrySql,Integer.class, 
				new Object[]{Util.seasonStart(),Util.seasonStart()});
		int letHomeCount=letList.get(0);
		int letVisitCount=letList.get(1);
		map.put("letHomeCount", letHomeCount);
		map.put("letVisitCount", letVisitCount);
		Double letHomeRate=new Double(df.format(100.0*letHomeCount/total));
		Double letVisitRate=new Double(df.format(100.0*letVisitCount/total));
		map.put("letHomeRate", letHomeRate);
		map.put("letVisitRate", letVisitRate);

		return map;
	}
	
	public Map<String,Object> teamRateQry(String teamName){
		Map<String,Object> map=new HashMap<String,Object>();
		String baseSql="select ROUND(AVG(homeScore),2) homeScore,ROUND(AVG(visitScore),2) visitScore from("
				+" select substring_index(score,'-',1) as homeScore,substring_index(score,'-',-1) as visitScore from score"
				+" where WHERE_CLAUSE )x";
		String qrySql=baseSql.replace("WHERE_CLAUSE", "homeTeam=? and matchTime>?")
				+" union "
				+baseSql.replace("WHERE_CLAUSE", "visitteam=? and matchTime>?")
				+" union "
				+baseSql.replace("WHERE_CLAUSE", "homeTeam=? order by matchTime desc limit 5")
				+" union "
				+baseSql.replace("WHERE_CLAUSE", "visitteam=? order by matchTime desc limit 5");
				
		List<Map<String, Object>> list=jdbcTemplate.queryForList(qrySql,new Object[]{
				teamName,Util.seasonStart(),
				teamName,Util.seasonStart(),
				teamName,
				teamName
		});
		//主场场均得分
		Double homeAvg=(Double)list.get(0).get("homeScore");
		//主场场均失分
		Double homeLostAvg=(Double)list.get(0).get("visitScore");
		//客场场均得分
		Double visitAvg=(Double)list.get(1).get("visitScore");
		//客场场均失分
		Double visitLostAvg=(Double)list.get(1).get("homeScore");
		//最近5场主场场均得分
		Double last5HomeAvg=(Double)list.get(2).get("homeScore");
		//最近5场主场场均失分
		Double last5HomeLostAvg=(Double)list.get(2).get("visitScore");
		//最近5场客场场均得分
		Double last5VisitAvg=(Double)list.get(3).get("visitScore");
		//最近5场客场场均失分
		Double last5VisitLostAvg=(Double)list.get(3).get("homeScore");
		map.put("homeAvg", homeAvg);
		map.put("homeLostAvg", homeLostAvg);
		map.put("visitAvg", visitAvg);
		map.put("visitLostAvg", visitLostAvg);
		map.put("last5HomeAvg", last5HomeAvg);
		map.put("last5HomeLostAvg", last5HomeLostAvg);
		map.put("last5VisitAvg", last5VisitAvg);
		map.put("last5VisitLostAvg", last5VisitLostAvg);
		
		String last5GameSql="select matchTime,homeTeam,visitTeam,score from score "
				+" where visitTeam=? or homeTeam=? order by matchTime desc limit 5";
		List<Map<String, Object>> gameList=jdbcTemplate.queryForList(last5GameSql,new Object[]{
				teamName,teamName
		});
		for(Map<String,Object> gameMap:gameList){
			Timestamp matchTime=(Timestamp)gameMap.get("matchTime");
			String dateStr=sdf.format(new Date(matchTime.getTime()));
			gameMap.put("matchTime", dateStr);
			if(gameMap.get("homeTeam").equals(teamName))
				gameMap.put("isHome", true);
			else
				gameMap.put("isHome", false);
		}
		map.put("last5Game", gameList);
		return map;
	}

	public Double caculate(String teamName, String rhName, String hvName, String wlName) {
		String sql="select avg(ARG1) from "
				+" (select substring_index(score,'-',1) as homeScore,"
				+" substring_index(score,'-',-1) as visitScore "
				+ "from score where ARG2=? ARG3) x ";
		String arg1,arg2,arg3 = null;
		if(hvName.equals("主场")){
			arg2="homeTeam";
			if(wlName.equals("得分")){
				arg1="homeScore";
			}else{
				arg1="visitScore";
			}
		}else{
			arg2="visitTeam";
			if(wlName.equals("得分")){
				arg1="visitScore";
			}else{
				arg1="homeScore";
			}
		}
		if(rhName.equals("最近五场")){
			arg3=" order by matchTime desc limit 5";
		}else{
			arg3=" and matchTime>"+Util.seasonStart();
		}
		sql=sql.replace("ARG1", arg1).replace("ARG2", arg2).replace("ARG3", arg3);
		
		Double res=jdbcTemplate.queryForObject(sql, new Object[]{teamName},Double.class);
		res=new Double(df.format(res));
		
		return res;
	}
}


