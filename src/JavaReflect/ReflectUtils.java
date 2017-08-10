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
 * @Description:    Java����İ�����
 * @author:         yangsheng
 */

public class ReflectUtils {
	/**
	 * 
	 * @Title:             getInstance
	 * @Description:     �����������������ʵ��
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
        	//��������Ϊ�����࣬�ӿ�ʱ��������������ԭ����ʵ��ʧ�ܣ����׳����쳣
            e.printStackTrace();
        }
        catch (IllegalAccessException e) {
            //δ���÷���Ȩ��ʱ�����׳����쳣
            e.printStackTrace();
        }
		return instance;
	}  
	/**
	 * 
	 * @Title:             getPublicConstructorInfo
	 * @Description:     ��ȡ���е�Public���췽����Ϣ
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
	 * @Description:     �õ���������Ϣ
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
     * @Description:     ����һ��Ĺ�������Ϣ
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
     * @Description:     ��ȡ�ֶ���Ϣ
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
     * @Description:     ��ȡһ���ֶ���Ϣ
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
     * @Description:     ��ȡ������Ϣ
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
     * @Description:     ��ȡһ�鷽����Ϣ
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
     * @Description:     ��ȡ���η���Ϣ
     * @param:            member   
     * @return:         String   
     * @throws
     */
    private static String getModifiersInfo(Member member) {
        StringBuilder sBuilder = new StringBuilder();
        int modifiers = member.getModifiers();
        sBuilder.append("\ngetModifiers: " + +modifiers + ", ");// �õ����η�����
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
     * @Description:     �ж��Ƿ�Ϊ���о�̬����
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
	 * @Description:     ���þ�̬����
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
     * @Description:     ����˽�з���
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
        // ע�ⲻҪ��getMethod(),��ΪgetMethod()���صĶ���public����
        Method method = cls.getDeclaredMethod(methodName, paramTypes);
        method.setAccessible(true);// ����Java�ķ��ʿ��Ƽ��
        value = method.invoke(obj, params);
        return value;
    }
    /**
     * 
     * @Title:             exists
     * @Description:     �ж����Ƿ������JVM��Path��
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
     * @Description:     ��ָ����classloader�ж����Ƿ������JVM��path��
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
