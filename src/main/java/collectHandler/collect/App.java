package collectHandler.collect;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class App {
	
	static Connection conn = null;
	
	static String maxIssueId = "";
	/*
	 * 执行方法入口
	 */
	public static void main(String[] args) {
		/*
		Timer timer = new Timer();
		final App app = new App();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				app.collectData();
			}
		}, new Date(), 1000 * 5);// 每隔20秒输出
		*/
		/*Data2Db data2Db = new Data2Db();
		List<SrcDataBean> srcList = data2Db.getAllRecord();
		for(SrcDataBean srcDataBean : srcList){
			try {
				Thread.sleep(1000);
				collectData(srcDataBean.getIssueId());
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}*/
		List<SrcDataBean> srcList = new ArrayList<SrcDataBean>();
		SrcDataBean srcDataBean1 = new SrcDataBean();
		srcDataBean1.setIssueId("160218030");
		srcDataBean1.setNo1(1);
		srcDataBean1.setNo2(1);
		srcDataBean1.setNo3(1);
		
		srcList.add(srcDataBean1);
		for(SrcDataBean srcDataBean : srcList){
			try {
				Thread.sleep(1000);
				collectData(srcDataBean.getIssueId());
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
	}

	/** 
	  * @Description: 
	  * @author songjia
	  * @date Feb 15, 2016 11:31:49 AM  
	  */
	private static void collectData(String issueId){
		Data2Db data2Db = new Data2Db();
		String maxIssueNumber = issueId;
		if(!maxIssueId.equals(maxIssueNumber)){
			maxIssueId = maxIssueNumber;
			// 将源数据库中数据插入到目的库中
			SrcDataBean srcDataBean = data2Db.getRecordByIssueId(maxIssueId);
			data2Db.insertBaseData(srcDataBean);
			//设定遗漏统计
			
		}
	}
	

}
