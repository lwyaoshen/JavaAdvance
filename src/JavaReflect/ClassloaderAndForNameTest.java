package JavaReflect;
/**
 * Java中class.forName()和classLoader都可用来对类进行加载。
	class.forName()前者除了将类的.class文件加载到jvm中之外，还会对类进行解释，执行类中的static块。
	而classLoader只干一件事情，就是将.class文件加载到jvm中，不会执行static中的内容,只有在newInstance才会去执行static块。
	Class.forName(name, initialize, loader)带参函数也可控制是否加载static块。并且只有调用了newInstance()方法采用调用构造函数，创建类的对象
	看下Class.forName()源码:
	
	//Class.forName(String className)  这是1.8的源码  
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
        System.out.println("静态代码块执行：loading line");  
    }  
  
    public static String s = getString();  
  
    private static String getString() {  
        System.out.println("给静态变量赋值的静态方法执行：loading line");  //静态代码块的执行外，竟然还有一个静态方法被执行，就是给静态变量赋值的静态方法被执行了。
        return "ss";  
    }  
  
    public static void test() {  
        System.out.println("普通静态方法执行：loading line");  
    }  
  
    {  
        System.out.println("要是普通的代码块呢？");  
    }  
  
    public Line() {  
        System.out.println("构造方法执行");  
    }  
}  


class Point {  
    static {  
        System.out.println("静态代码块执行：loading point");  
    }  
} 



/**
 * 
 * @ClassName:       ClassloaderAndForNameTest
 * @Description:    Class.forName和classloader的区别 
 * @author:         yangsheng
 */
public class ClassloaderAndForNameTest {  
    public static void main(String[] args) {  
        String wholeNameLine = "JavaReflect.Line";  
        String wholeNamePoint = "JavaReflect.Point";  
        System.out.println("下面是测试Classloader的效果");  
        testClassloader(wholeNameLine, wholeNamePoint);  
        System.out.println("----------------------------------");  
        System.out.println("下面是测试Class.forName的效果");  
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
            //demo = ClassloaderAndForNameTest.class.getClassLoader().loadClass(wholeNamePoint);//这个也是可以的  
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
