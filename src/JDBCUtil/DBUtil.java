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
 * @Description:    JDBC�İ�����
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
     * @param rs ���صĽ����
     * @param pst Ԥ����
     * @param conn ���ݿ�����
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
        //ȡ��Method   
        Method[] methods = cls.getDeclaredMethods();   
        System.out.println(methods[0].getName());  
        List lst = new ArrayList();  
        // ���ڻ�ȡ����������������  
        ResultSetMetaData meta = rs.getMetaData();  
        Object obj = null;  
        while (rs.next()) {  
            // ��ȡformbeanʵ������  
            obj = cls.newInstance(); // ��Class.forName����ʵ���������new����ʵ�����������кܴ�����ģ���Ҫ��JVM���ȴ���������в����࣬Ȼ����ʵ������������ִ�����еľ�̬��������new�������½�һ������ʵ��  
            // ѭ����ȡָ���е�ÿһ�е���Ϣ  
            for (int i = 1; i <= meta.getColumnCount(); i++) {  
                // ��ǰ����  
                String colName = meta.getColumnName(i);  
                  
                // ���÷�����  
                String setMethodName = "set" + colName;  
                 //����Method   
                for (int j = 0; j < methods.length; j++) {   
                    if (methods[j].getName().equalsIgnoreCase(setMethodName)) {   
                        setMethodName = methods[j].getName();   
                          
                        System.out.println(setMethodName);  
                        // ��ȡ��ǰλ�õ�ֵ������Object����  
                        Object value = rs.getObject(colName);   
                        if(value == null){  
                            continue;  
                        }  
                        //ʵ��Set����   
                        try {   
                            //// ���÷����ȡ����  
                            //JavaBean�ڲ����Ժ�ResultSet��һ��ʱ��   
                            Method setMethod = obj.getClass().getMethod(   
                                    setMethodName, value.getClass());   
                            setMethod.invoke(obj, value);   
                        } catch (Exception e) {   
                            //JavaBean�ڲ����Ժ�ResultSet�в�һ��ʱ��ʹ��String������ֵ��   
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
        //ȡ��Method����   
        Method[] methods = clz.getMethods();   
        Object dto = clz.newInstance();
        //ȡ��ResultSet������   
        ResultSetMetaData rsmd = rs.getMetaData();   
        int columnsCount = rsmd.getColumnCount();   
        String[] columnNames = new String[columnsCount];   
        for (int i = 0; i < columnsCount; i++) {   
            columnNames[i] = rsmd.getColumnLabel(i + 1);   
        }   
        //����ResultSet   
        while (rs.next()) {   
            //����, ��ResultSet�󶨵�JavaBean   
            for (int i = 0; i < columnNames.length; i++) {   
                //ȡ��Set����   
                String setMethodName = "set" + columnNames[i];   
                //����Method   
                for (int j = 0; j < methods.length; j++) {   
                    if (methods[j].getName().equalsIgnoreCase(setMethodName)) {   
                        setMethodName = methods[j].getName();   
                        Object value = rs.getObject(columnNames[i]);   
  
                        //ʵ��Set����   
                        try {   
                            //JavaBean�ڲ����Ժ�ResultSet��һ��ʱ��   
                            Method setMethod = dto.getClass().getMethod(   
                                    setMethodName, value.getClass());   
                            setMethod.invoke(dto, value);   
                        } catch (Exception e) {   
                            //JavaBean�ڲ����Ժ�ResultSet�в�һ��ʱ��ʹ��String������ֵ��   
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
