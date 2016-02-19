package collectHandler.collect;
import java.io.*; 
import java.util.*; 
import java.sql.*; 

public class ConnectDesDb{
   
    /** 
      * @Description: 给静态变量赋值
      * @author songj@sdfcp.com
      * @date Feb 15, 2016 1:52:27 PM 
      * @return 
      */
    public static synchronized Connection getDesConnection(){ 
        return _getConnection();
    }

    /** 
      * @Description: 查找数据库属性，并创建连接
      * @author songj@sdfcp.com
      * @date Feb 15, 2016 1:52:58 PM 
      * @return 
      */
    private static Connection _getConnection(){ 
    	try{ 
         	String driver = "com.mysql.jdbc.Driver";
			String url = "jdbc:mysql://192.168.1.253:3306/echart3";
			String username = "";
			String password = "";
            Properties p = new Properties();
            InputStream is = ConnectSrcDb.class.getClassLoader().getResourceAsStream("db.properties");
            p.load(is);
            driver = p.getProperty("driver",driver); 
            url = p.getProperty("des.url",url); 
            username = p.getProperty("des.username",username); 
            password = p.getProperty("des.password",password);
            Properties pr = new Properties(); 
            pr.put("user",username); 
            pr.put("password",password); 
            pr.put("characterEncoding", "GB2312"); 
            pr.put("useUnicode", "TRUE"); 
            Class.forName(driver).newInstance(); 
            return DriverManager.getConnection(url,pr); 
        }catch(Exception se){
        	se.printStackTrace();
        	return null;
        } 
    }
    
    /** 
      * @Description: 关闭数据库连接
      * @author songj@sdfcp.com
     * @throws SQLException 
      * @date Feb 15, 2016 2:34:28 PM  
      */
    public static void closeDesConnection(Connection conn) throws SQLException{
		if(!conn.isClosed() && conn != null){
			conn.close();
		}
    }
} 
