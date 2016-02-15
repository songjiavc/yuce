package collectHandler.collect;

import java.sql.Connection;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class App {
	
	static Connection conn = null;
	
	static String maxIssueId = "";
	/*
	 * 执行方法入口
	 */
	public static void main(String[] args) {
		Timer timer = new Timer();
		final App app = new App();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				app.collectData();
			}
		}, new Date(), 1000 * 5);// 每隔20秒输出

	}

	/** 
	  * @Description: 
	  * @author songjia
	  * @date Feb 15, 2016 11:31:49 AM  
	  */
	private void collectData(){
		Data2Db data2Db = new Data2Db();
		String maxIssueNumber = data2Db.findMaxIssueIdFromSrcDb();
		if(!maxIssueId.equals(maxIssueNumber)){
			maxIssueId = maxIssueNumber;
			// 将源数据库中数据插入到目的库中
			SrcDataBean srcDataBean = data2Db.getRecordByIssueId(maxIssueId);
			data2Db.insertBaseData(srcDataBean);
			//设定遗漏统计
			
		}
	}
	

}
