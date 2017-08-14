package JDBCUtil;


import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
/**
 * 
 * @ClassName:       DBUtil
 * @Description:    JDBC的帮助类
 * @author:         yangsheng
 */
public class DBUtil {
	private static final String DBDRIVER = "com.mysql.jdbc.Driver" ;			
    private static final String DBURL = "jdbc:mysql://localhost:3306/crawler?useUnicode=true&characterEncoding=UTF-8";
    private static final String DBUSER = "root" ;								
    private static final String DBPASSWORD = "root";	
    public static Connection getConnection(){
		Connection conn = null;													
		try {
			Class.forName(DBDRIVER);											
			conn = DriverManager.getConnection(DBURL,DBUSER,DBPASSWORD);	
		} catch (ClassNotFoundException e) {									
			e.printStackTrace();										
		} catch (SQLException e) {												
			e.printStackTrace();
		}
		return conn;
	}
    /**
     * @param rs 返回的结果集
     * @param pst 预处理
     * @param conn 数据库连接
     */
    public static void close(ResultSet rs,PreparedStatement pst,Connection conn) {
		try {
			if(rs!=null){
				rs.close();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}finally{
			try {
				if(pst!=null){
					pst.close();
				}
			} catch (Exception e2) {
				throw new RuntimeException(e2);
			}
			finally{
				try {
					if(conn!=null){
						conn.close();
					}
				} catch (Exception e3) {
					throw new RuntimeException(e3);
				}
			}
		}
	}
    @SuppressWarnings("unchecked")  
    public static List resultSetToList(ResultSet rs, Class cls)throws Exception {  
        //取得Method   
        Method[] methods = cls.getDeclaredMethods();   
        System.out.println(methods[0].getName());  
        List lst = new ArrayList();  
        // 用于获取列数、或者列类型  
        ResultSetMetaData meta = rs.getMetaData();  
        Object obj = null;  
        while (rs.next()) {  
            // 获取formbean实例对象  
            obj = cls.newInstance(); // 用Class.forName方法实例化对象和new创建实例化对象是有很大区别的，它要求JVM首先从类加载器中查找类，然后再实例化，并且能执行类中的静态方法。而new仅仅是新建一个对象实例  
            // 循环获取指定行的每一列的信息  
            for (int i = 1; i <= meta.getColumnCount(); i++) {  
                // 当前列名  
                String colName = meta.getColumnName(i);  
                  
                // 设置方法名  
                String setMethodName = "set" + colName;  
                 //遍历Method   
                for (int j = 0; j < methods.length; j++) {   
                    if (methods[j].getName().equalsIgnoreCase(setMethodName)) {   
                        setMethodName = methods[j].getName();   
                          
                        System.out.println(setMethodName);  
                        // 获取当前位置的值，返回Object类型  
                        Object value = rs.getObject(colName);   
                        if(value == null){  
                            continue;  
                        }  
                        //实行Set方法   
                        try {   
                            //// 利用反射获取对象  
                            //JavaBean内部属性和ResultSet中一致时候   
                            Method setMethod = obj.getClass().getMethod(   
                                    setMethodName, value.getClass());   
                            setMethod.invoke(obj, value);   
                        } catch (Exception e) {   
                            //JavaBean内部属性和ResultSet中不一致时候，使用String来输入值。   
                           e.printStackTrace();  
                        }   
                    }   
                }   
            }  
            lst.add(obj);  
        }  
        return lst;  
} 
    public static Object bindDataToDTO(ResultSet rs, Class clz) throws Exception {   
        //取得Method方法   
        Method[] methods = clz.getMethods();   
        Object dto = clz.newInstance();
        //取得ResultSet的列名   
        ResultSetMetaData rsmd = rs.getMetaData();   
        int columnsCount = rsmd.getColumnCount();   
        String[] columnNames = new String[columnsCount];   
        for (int i = 0; i < columnsCount; i++) {   
            columnNames[i] = rsmd.getColumnLabel(i + 1);   
        }   
        //遍历ResultSet   
        while (rs.next()) {   
            //反射, 从ResultSet绑定到JavaBean   
            for (int i = 0; i < columnNames.length; i++) {   
                //取得Set方法   
                String setMethodName = "set" + columnNames[i];   
                //遍历Method   
                for (int j = 0; j < methods.length; j++) {   
                    if (methods[j].getName().equalsIgnoreCase(setMethodName)) {   
                        setMethodName = methods[j].getName();   
                        Object value = rs.getObject(columnNames[i]);   
  
                        //实行Set方法   
                        try {   
                            //JavaBean内部属性和ResultSet中一致时候   
                            Method setMethod = dto.getClass().getMethod(   
                                    setMethodName, value.getClass());   
                            setMethod.invoke(dto, value);   
                        } catch (Exception e) {   
                            //JavaBean内部属性和ResultSet中不一致时候，使用String来输入值。   
                            Method setMethod = dto.getClass().getMethod(   
                                    setMethodName, String.class);   
                            setMethod.invoke(dto, value.toString());   
                        }   
                    }   
                }   
            }   
        }   
        return dto; 
    
    }
}
