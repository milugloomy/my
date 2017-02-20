package bocai.batch;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import bocai.service.UpdateService;

@Service("updateBatch")
public class UpdateBatch {
	
	@Autowired
	private UpdateService udateService;
	@Autowired
	private MessageSource messageSource;
	
	@PostConstruct
	public void init(){
		//batch开关
		String batchSwitch=messageSource.getMessage("batchSwitch", null, Locale.SIMPLIFIED_CHINESE);
		if(batchSwitch.equals("off"))
			return;
		
	    //每天18点更新数据
		Calendar calendar = Calendar.getInstance();
		//若部署时间大于18点，则第二天启动
		if(calendar.get(Calendar.HOUR_OF_DAY)>=18)
			calendar.add(Calendar.DAY_OF_YEAR, 1);
	    calendar.set(Calendar.HOUR_OF_DAY, 18); // 控制时
	    calendar.set(Calendar.MINUTE, 0);    // 控制分
	    calendar.set(Calendar.SECOND, 0);    // 控制秒
	    Date qryTime = calendar.getTime();
	    
	    new Timer().scheduleAtFixedRate(new TimerTask() {
	    	public void run() {
	    		udateService.update();
	    	}
	    }, qryTime, 1000 * 60 * 60 * 24);//
	}
	
}


