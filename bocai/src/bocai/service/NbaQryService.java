package bocai.service;

import java.sql.Timestamp;
import java.text.DecimalFormat;
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
		df.applyPattern("#.0000");
	}
	
	public Map<String,Object> totalRateQry(){
		String baseSql=" select count(*) count from "
				+"(select substring_index(score,'-',1) as homeScore,totalPoints,"
				+"substring_index(score,'-',-1) as visitScore from score) x ";
		Map<String,Object> map=new HashMap<String,Object>();
		String qrySql=baseSql+"where homeScore>visitScore"
				+" union"+baseSql+"where homeScore<visitScore"
				+" union"+baseSql+"where (homeScore+visitScore)>totalPoints"
				+" union"+baseSql+"where(homeScore+visitScore)<totalPoints";
		List<Integer> list=jdbcTemplate.queryForList(qrySql,Integer.class);
		int homeCount=list.get(0);
		int visitCount=list.get(1);
		int smallCount=list.get(2);
		int bigCount=list.get(3);
		map.put("homeCount", homeCount);
		map.put("visitCount", visitCount);
		map.put("smallCount", smallCount);
		map.put("bigCount", bigCount);
		Double homeRate=new Double(df.format(1.0*homeCount/(homeCount+visitCount)));
		Double visitRate=new Double(df.format(1.0*visitCount/(homeCount+visitCount)));
		Double smallRate=new Double(df.format(1.0*smallCount/(smallCount+bigCount)));
		Double bigRate=new Double(df.format(1.0*bigCount/(smallCount+bigCount)));
		map.put("homeRate", homeRate);
		map.put("visitRate", visitRate);
		map.put("smallRate", smallRate);
		map.put("bigRate", bigRate);
		return map;
	}
	
	public Map<String,Object> teamRateQry(String teamName){
		Map<String,Object> map=new HashMap<String,Object>();
		String qrySql="select ROUND(AVG(homeScore),2) from("
				+" select substring_index(score,'-',1) as homeScore from score"
				+" where hometeam=? and matchTime>?"
			+" )x union"
			+" select ROUND(AVG(visitScore),2) from("
				+" select substring_index(score,'-',-1) as visitScore from score"
				+" where visitteam=? and matchTime>?"
			+" )x union"
			+" select ROUND(AVG(homeScore),2) from("
				+" select substring_index(score,'-',1) as homeScore from score s"
				+" where homeTeam=? order by matchTime desc limit 5"
			+" )x union"
			+" select ROUND(AVG(visitScore),2) from("
			+" select substring_index(score,'-',-1) as visitScore from score s"
			+" where visitteam=? order by matchTime desc limit 5"
			+" )x";
		
		List<Double> list=jdbcTemplate.queryForList(qrySql,new Object[]{
				teamName,Util.s2016,teamName,Util.s2016,teamName,teamName
		},Double.class);
		Double homeAvg=list.get(0);
		Double visitAvg=list.get(1);
		Double last5HomeAvg=list.get(2);
		Double last5VisitAvg=list.get(3);
		map.put("homeAvg", homeAvg);
		map.put("visitAvg", visitAvg);
		map.put("last5HomeAvg", last5HomeAvg);
		map.put("last5VisitAvg", last5VisitAvg);
		
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
}


