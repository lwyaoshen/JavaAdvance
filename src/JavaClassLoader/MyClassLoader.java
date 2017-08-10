package JavaClassLoader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;

/**
 * 
 * @ClassName:       MyClassLoader
 * @Description:    �Զ����������
 * @author:         yangsheng
 */
public class MyClassLoader extends ClassLoader{
	
	public MyClassLoader()  {  
    }  
	
    public MyClassLoader(ClassLoader parent)  {  
        super(parent);  
    }
    /**
     * 
     * <p>Title: findClass</p>
     * <p>Description: </p>
     * @param name
     * @return
     * @throws ClassNotFoundException
     * @see java.lang.ClassLoader#findClass(java.lang.String)
     */
    protected Class<?> findClass(String name) throws ClassNotFoundException  {  
        File file = new File("D:/People.class");  
        try{  
            byte[] bytes = getClassBytes(file);  
            //defineClass�������԰Ѷ��������ֽ���ɵ��ļ�ת��Ϊһ��java.lang.Class  
            Class<?> c = this.defineClass(name, bytes, 0, bytes.length);  
            return c;  
        }   
        catch (Exception e)  {  
            e.printStackTrace();  
        }  
          
        return super.findClass(name);  
    } 
    /**
     * 
     * @Title:             getClassBytes
     * @Description:     �õ�.class�ļ����ֽ�����
     * @param:               
     * @return:         byte[]   
     * @throws
     */
    private byte[] getClassBytes(File file) throws Exception  {  
        // ����Ҫ����.class���ֽڣ����Ҫʹ���ֽ���  
        FileInputStream fis = new FileInputStream(file);  
        FileChannel fc = fis.getChannel();  
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        WritableByteChannel wbc = Channels.newChannel(baos);  
        ByteBuffer by = ByteBuffer.allocate(1024);  
        while (true){  
            int i = fc.read(by);  
            if (i == 0 || i == -1)  
            break;  
            by.flip();  
            wbc.write(by);  
            by.clear();  
        }  
        fis.close();  
        return baos.toByteArray();  
    }
}
