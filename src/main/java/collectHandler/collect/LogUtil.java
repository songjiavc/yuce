package collectHandler.collect;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/** 
  * @ClassName: FileUtil 
  * @Description: 存放创建日志文件 
  * @author songj@sdfcp.com
  * @date Feb 15, 2016 1:58:12 PM 
  *  
  */
public class LogUtil {
	/** 
	  * @Description: 打印提示信息
	  * @author songj@sdfcp.com
	  * @date Feb 15, 2016 2:14:06 PM 
	  * @param info 
	  */
	public static synchronized void info(String info){
		File file = new File("logs/info.log");
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file,true);
			fos.write(info.getBytes());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/** 
	  * @Description: 打印错误信息
	  * @author songj@sdfcp.com
	  * @date Feb 15, 2016 2:14:06 PM 
	  * @param info 
	  */
	public static synchronized void error(String error){
		File file = new File("logs/error.log");
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file,true);
			fos.write(error.getBytes());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
