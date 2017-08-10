package JavaReflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

/**
 * 
 * @ClassName:       ReflectUtils
 * @Description:    Java反射的帮助类
 * @author:         yangsheng
 */

public class ReflectUtils {
	/**
	 * 
	 * @Title:             getInstance
	 * @Description:     传入类名，返回类的实例
	 * @param:           className    
	 * @return:         Object   
	 * @throws
	 */
	public static Object getInstance(String className){
		Object instance = null;
        try {
            Class<?> clazz = Class.forName(className);
            instance = clazz.newInstance();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        catch (InstantiationException e) {
        	//如果这个类为抽象类，接口时，或者由于其他原因导致实例失败，将抛出该异常
            e.printStackTrace();
        }
        catch (IllegalAccessException e) {
            //未设置访问权限时，将抛出该异常
            e.printStackTrace();
        }
		return instance;
	}  
	/**
	 * 
	 * @Title:             getPublicConstructorInfo
	 * @Description:     获取所有的Public构造方法信息
	 * @param:               className
	 * @return:         String   
	 * @throws
	 */
	
	public static String getPublicConstructorInfo(String className) {
        StringBuilder sBuilder = new StringBuilder();

        try {
            Class<?> clazz = Class.forName(className);
            Constructor<?>[] constructors = clazz.getConstructors();
            sBuilder.append(getConstructorInfo(constructors));
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return sBuilder.toString();
    } 
	
	/**
	 * 
	 * @Title:             getConstructorInfo
	 * @Description:     得到构造器信息
	 * @param:              constructor 
	 * @return:         String   
	 * @throws
	 */
    private static String getConstructorInfo(Constructor<?> constructor) {

        StringBuilder sBuilder = new StringBuilder();

        sBuilder.append("name: " + constructor.getName());
        sBuilder.append("\ngetParameterTypes: "
                + Arrays.toString(constructor.getParameterTypes()));
        return sBuilder.toString();
    }
    /**
     * 
     * @Title:             getConstructorInfo
     * @Description:     返回一组的构造器信息
     * @param:               constructors
     * @return:         String   
     * @throws
     */
    private static String getConstructorInfo(Constructor<?>[] constructors) {

        StringBuilder sBuilder = new StringBuilder();
        int i = 0;
        for (Constructor<?> c : constructors) {
            sBuilder.append("method: " + ++i + " : ");
            sBuilder.append("\n" + getConstructorInfo(c));
            sBuilder.append("\n");
        }

        return sBuilder.toString();
    }
	
    /**
     * 
     * @Title:             getFieldInfo
     * @Description:     获取字段信息
     * @param:               filed
     * @return:         String   
     * @throws
     */
    private static String getFieldInfo(Field field) {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append("name: " + field.getName());
        sBuilder.append("\ngetType: " + field.getType());
        sBuilder.append(getModifiersInfo(field));
        return sBuilder.toString();
    }
    /**
     * 
     * @Title:             getFieldInfo
     * @Description:     获取一组字段信息
     * @param:              fields 
     * @return:         String   
     * @throws
     */
    private static String getFieldInfo(Field[] fields) {
        StringBuilder sBuilder = new StringBuilder();
        int i = 0;
        for (Field field : fields) {
            sBuilder.append("field: " + ++i + " : ");
            sBuilder.append("\n" + getFieldInfo(field));
            sBuilder.append("\n");
        }

        return sBuilder.toString();
    }
    /**
     * 
     * @Title:             getMethodInfo
     * @Description:     获取方法信息
     * @param:              method 
     * @return:         String   
     * @throws
     */
    private static String getMethodInfo(Method method) {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append("name: " + method.getName());
        sBuilder.append("\ngetReturnType: " + method.getReturnType());
        sBuilder.append("\ngetParameterTypes: "
                + Arrays.toString(method.getParameterTypes()));
        sBuilder.append(getModifiersInfo(method));
        return sBuilder.toString();
    }
    /**
     * 
     * @Title:             getMethodInfo
     * @Description:     获取一组方法信息
     * @param:             methods  
     * @return:         String   
     * @throws
     */
    private static String getMethodInfo(Method[] methods) {
        StringBuilder sBuilder = new StringBuilder();
        int i = 0;
        for (Method method : methods) {

            sBuilder.append("method: " + ++i + " : ");
            sBuilder.append("\n" + getMethodInfo(method));
            sBuilder.append("\n");

        }

        return sBuilder.toString();
    }
    /**
     * 
     * @Title:             getModifiersInfo
     * @Description:     获取修饰符信息
     * @param:            member   
     * @return:         String   
     * @throws
     */
    private static String getModifiersInfo(Member member) {
        StringBuilder sBuilder = new StringBuilder();
        int modifiers = member.getModifiers();
        sBuilder.append("\ngetModifiers: " + +modifiers + ", ");// 得到修饰符编码
        sBuilder.append("\nisPublic: " + Modifier.isPublic(modifiers) + ", ");
        sBuilder.append("\nisPrivate: " + Modifier.isPrivate(modifiers) + ", ");
        sBuilder.append("\nisStatic: " + Modifier.isStatic(modifiers) + ", ");
        sBuilder.append("\nisFinal: " + Modifier.isFinal(modifiers) + ", ");
        sBuilder.append("\nisAbstract: " + Modifier.isAbstract(modifiers));
        return sBuilder.toString();
    }
    /**
     * 
     * @Title:             isPublicStatic
     * @Description:     判断是否为公有静态方法
     * @param:               member
     * @return:         boolean   
     * @throws
     */
    private static boolean isPublicStatic(Member member) {
        boolean isPS = false;
        int mod = member.getModifiers();
        isPS = Modifier.isPublic(mod) && Modifier.isStatic(mod);
        return isPS;
    }
	/**
	 * 
	 * @Title:             invokePublicStaticMethod
	 * @Description:     调用静态方法
	 * @param:               className
	 * @param:               methodName
	 * @param:               paramTypes
	 * @param:               params
	 * @return:         Object   
	 * @throws
	 */
    public static Object invokePublicStaticMethod(String className,
            String methodName, Class<?>[] paramTypes, Object[] params)
            throws Exception {

        Class<?> cls = Class.forName(className);

        Method method = cls.getMethod(methodName, paramTypes);
        Object value = null;
        if (isPublicStatic(method)) {
            value = method.invoke(null, params);
        }
        return value;
    }
    /**
     * 
     * @Title:             invokePrivateMethod
     * @Description:     调用私有方法
     * @param:               obj
	 * @param:               methodName
	 * @param:               paramTypes
	 * @param:               params              
     * @return:         Object   
     * @throws
     */
    public static Object invokePrivateMethod(Object obj, String methodName,
            Class<?>[] paramTypes, Object[] params) throws Exception {

        Object value = null;
        Class<?> cls = obj.getClass();
        // 注意不要用getMethod(),因为getMethod()返回的都是public方法
        Method method = cls.getDeclaredMethod(methodName, paramTypes);
        method.setAccessible(true);// 抑制Java的访问控制检查
        value = method.invoke(obj, params);
        return value;
    }
    /**
     * 
     * @Title:             exists
     * @Description:     判断类是否存在于JVM的Path中
     * @param:               className
     * @return:         boolean   
     * @throws
     */
    public static boolean exists(String className)
    {      
        try
        {
            Class.forName(className);
            return true;
        }

        catch (ClassNotFoundException error)
        {
            return false;
        }
    }
    /**
     * 
     * @Title:             exists
     * @Description:     在指定的classloader判断类是否存在于JVM的path中
     * @param:               
     * @return:         boolean   
     * @throws
     */
    public static boolean exists(String className, ClassLoader classLoader)
    {
        try
        {
            classLoader.loadClass(className);
            return true;
        }

        catch (ClassNotFoundException error)
        {
            return false;
        }
    }
}
