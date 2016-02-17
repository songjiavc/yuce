package collectHandler.collect;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.mysql.jdbc.PreparedStatement;

/** 
  * @ClassName: Data2Db 
  * @Description: 数据库操作方法全部在这个类中 
  * @author songj@sdfcp.com
  * @date Feb 15, 2016 3:29:15 PM 
  *  
  */
public class Data2Db {

	/** 
	  * @Description: 在源库中查找最新的期号
	  * @author songj@sdfcp.com
	  * @date Feb 15, 2016 3:29:13 PM 
	  * @return 
	  */
	public String findMaxIssueIdFromSrcDb(){
		Connection srcConn = ConnectSrcDb.getSrcConnection();
		String issueId = null;
		PreparedStatement pstmt = null;
		String sql = "SELECT max(issue_id) FROM echart3.echart_anhui_kuai3_t";
		try {
			pstmt = (PreparedStatement) srcConn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				issueId = rs.getString(1);
			}
			if(rs != null && !rs.isClosed()){
				rs.close();
			}
		} catch (SQLException e) {
			LogUtil.error(e.getMessage());
		}
		return issueId;
	}
	
	
	/** 
	  * @Description: 根据期号在源数据库中获取记录
	  * @author songj@sdfcp.com
	  * @date Feb 15, 2016 4:24:40 PM 
	  * @param issueId
	  * @return 
	  */
	public SrcDataBean getRecordByIssueId(String issueId){
		Connection srcConn = ConnectSrcDb.getSrcConnection();
		SrcDataBean srcDataBean = new SrcDataBean();
		PreparedStatement pstmt = null;
		String sql = "SELECT issue_id,no_1,no_2,no_3 FROM echart3.echart_anhui_kuai3_t";
		try {
			pstmt = (PreparedStatement) srcConn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				srcDataBean.setIssueId(rs.getString(1));
				srcDataBean.setNo1(rs.getInt(2));
				srcDataBean.setNo2(rs.getInt(3));
				srcDataBean.setNo3(rs.getInt(4));
			}
			if(rs != null && !rs.isClosed()){
				rs.close();
			}
		} catch (SQLException e) {
			LogUtil.error(e.getMessage());
		}
		return srcDataBean;
	}
	/** 
	  * @Description: 向目标库中插入数据
	  * @author songj@sdfcp.com
	  * @date Feb 15, 2016 4:05:50 PM 
	  * @param issue_id
	  * @param one
	  * @param two
	  * @param three
	  * @return 
	  */
	@SuppressWarnings("finally")
	public boolean insertBaseData(SrcDataBean srcDataBean) {
		boolean flag = true;
		Connection conn = ConnectDesDb.getDesConnection();
		try{
			if(!haveDataInIssueId(srcDataBean.getIssueId(),conn)){
				insertData(srcDataBean,conn);
			}
			batchUpdateMiss();
		}catch (SQLException e) {
			flag = false;
		}finally{
			try {
				ConnectDesDb.closeDesConnection(conn);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				return flag;
			}
		}
	}
	
	/** 
	  * @Description:  
	  * @author songj
	  * @date Feb 15, 2016 4:00:04 PM 
	  * @param issue_id
	  * @param one
	  * @param two
	  * @param three
	  * @param conn
	  * @throws SQLException 
	  */
	private void insertData(SrcDataBean srcDataBean,Connection conn) throws SQLException{
		String sql = "insert into T_ANHUI_KUAI3_NUMBER (issue_number,no1,no2,no3，create_time,origin) values(?,?,?,?,?,?)";
		PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(sql);
		pstmt.setString(1, srcDataBean.getIssueId());
		pstmt.setInt(2, srcDataBean.getNo1());
		pstmt.setInt(3, srcDataBean.getNo2());
		pstmt.setInt(4, srcDataBean.getNo3());
		pstmt.setTimestamp(5, new java.sql.Timestamp(new Date().getTime()));
		pstmt.setInt(6, Constants.ORIGIN_ALIDATABASE);
		pstmt.executeUpdate();
	}
	
	/** 
	  * @Description: 判断库中是否有该条记录
	  * @author songj@sdfcp.com
	  * @date Feb 15, 2016 4:39:24 PM 
	  * @param issueId
	  * @param conn
	  * @return
	  * @throws SQLException 
	  */
	private boolean haveDataInIssueId(String issueId,Connection conn) throws SQLException{
		boolean flag = false;
		int count = 0;
		String sql = "SELECT COUNT(*) FROM T_ANHUI_KUAI3_NUMBER WHERE issue_id = '"+issueId+"'";
		PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()){
			count = rs.getInt(1);
		}
		if(count > 0){
			flag = true;
		}
		return flag;
	}
	
	/** 
	  * @Description: 更新遗漏值
	  * @author songj@sdfcp.com
	 * @throws SQLException 
	  * @date Feb 17, 2016 11:21:29 AM  
	  */
	private void batchUpdateMiss(SrcDataBean srcDataBean,Connection conn) throws SQLException{
		DatabaseMetaData dbmd= conn.getMetaData();
		boolean a = dbmd.supportsBatchUpdates();
		if(a){
			try
			 {
			  //保存当前自动提交模式
			  boolean booleanautoCommit = conn.getAutoCommit();
			  //关闭自动提交
			  conn.setAutoCommit(false);
			  PreparedStatement stmt = (PreparedStatement) conn.prepareStatement(AnalysisMissUtil.getAllGroupMiss(srcDataBean));
			  //使用Statement同时收集多条sql语句
			  stmt.addBatch(AnalysisMissUtil.getAllNumMiss(srcDataBean));
			  stmt.addBatch(AnalysisMissUtil.getFourNumGroupMiss(srcDataBean));
			  stmt.addBatch(AnalysisMissUtil.getOddOrEvenMiss(srcDataBean));
			  stmt.addBatch(AnalysisMissUtil.getSmallOrBigMiss(srcDataBean));
			  stmt.addBatch(AnalysisMissUtil.getTwoSameGroupMiss(srcDataBean));
			  //同时提交所有的sql语句
			  stmt.addBatch("UPDATE T_ANHUI_KUAI3_MISSANALYSIS SET CURRENT_MISS = CURRENT_MISS+1 WHERE CURRENT_MISS <> 0;");
			  stmt.addBatch("UPDATE T_ANHUI_KUAI3_MISSANALYSIS SET CURRENT_MISS = CURRENT_MISS+1 WHERE CURRENT_MISS <> 0;");
			  stmt.executeBatch();
			  
			  //提交修改
			  conn.commit();
			  conn.setAutoCommit(booleanautoCommit);
			 }
			 catch(Exception e)
			 {
			    e.printStackTrace();
			    conn.rollback(); 
			 }
		}
	}
	

}
