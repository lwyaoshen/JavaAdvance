package JavaReflect;
/**
 * Java��class.forName()��classLoader��������������м��ء�
	class.forName()ǰ�߳��˽����.class�ļ����ص�jvm��֮�⣬���������н��ͣ�ִ�����е�static�顣
	��classLoaderֻ��һ�����飬���ǽ�.class�ļ����ص�jvm�У�����ִ��static�е�����,ֻ����newInstance�Ż�ȥִ��static�顣
	Class.forName(name, initialize, loader)���κ���Ҳ�ɿ����Ƿ����static�顣����ֻ�е�����newInstance()�������õ��ù��캯����������Ķ���
	����Class.forName()Դ��:
	
	//Class.forName(String className)  ����1.8��Դ��  
   	public static Class<?> forName(String className) throws ClassNotFoundException {  
       Class<?> caller = Reflection.getCallerClass();  
       return forName0(className, true, ClassLoader.getClassLoader(caller), caller);  
   	}  
   
   
 * 
 * 
 * 
 * 
 * @ClassName:       ClassloaderAndForNameTest
 * @Description:    TODO
 * @author:         yangsheng
 */

class Line {  
    static {  
        System.out.println("��̬�����ִ�У�loading line");  
    }  
  
    public static String s = getString();  
  
    private static String getString() {  
        System.out.println("����̬������ֵ�ľ�̬����ִ�У�loading line");  //��̬������ִ���⣬��Ȼ����һ����̬������ִ�У����Ǹ���̬������ֵ�ľ�̬������ִ���ˡ�
        return "ss";  
    }  
  
    public static void test() {  
        System.out.println("��ͨ��̬����ִ�У�loading line");  
    }  
  
    {  
        System.out.println("Ҫ����ͨ�Ĵ�����أ�");  
    }  
  
    public Line() {  
        System.out.println("���췽��ִ��");  
    }  
}  


class Point {  
    static {  
        System.out.println("��̬�����ִ�У�loading point");  
    }  
} 



/**
 * 
 * @ClassName:       ClassloaderAndForNameTest
 * @Description:    Class.forName��classloader������ 
 * @author:         yangsheng
 */
public class ClassloaderAndForNameTest {  
    public static void main(String[] args) {  
        String wholeNameLine = "JavaReflect.Line";  
        String wholeNamePoint = "JavaReflect.Point";  
        System.out.println("�����ǲ���Classloader��Ч��");  
        testClassloader(wholeNameLine, wholeNamePoint);  
        System.out.println("----------------------------------");  
        System.out.println("�����ǲ���Class.forName��Ч��");  
        testForName(wholeNameLine, wholeNamePoint);  
  
    }  
  
    /** 
     * classloader 
     */  
    private static void testClassloader(String wholeNameLine, String wholeNamePoint) {  
        Class<?> line;  
        Class<?> point;  
        ClassLoader loader = ClassLoader.getSystemClassLoader();  
        try {  
            line = loader.loadClass(wholeNameLine);  
            point = loader.loadClass(wholeNamePoint);  
            //demo = ClassloaderAndForNameTest.class.getClassLoader().loadClass(wholeNamePoint);//���Ҳ�ǿ��Ե�  
            System.out.println("line   " + line.getName());  
            System.out.println("point   " + point.getName());  
        } catch (ClassNotFoundException e) {  
            e.printStackTrace();  
        }  
    }  
  
    /** 
     * Class.forName 
     */  
    private static void testForName(String wholeNameLine, String wholeNamePoint) {  
  
        try {  
            Class line = Class.forName(wholeNameLine);  
            Class point = Class.forName(wholeNamePoint);  
            System.out.println("line   " + line.getName());  
            System.out.println("point   " + point.getName());  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
  
  
}  
