package Annotation;

import java.lang.reflect.Field;

public class FieldAnnotationTest {  
	  
    public static void main(String[] args) {  
        User test = new User();  
        Class<User> testClass = User.class;  
        try {  
            //��Ϊ��ע�⵽Field�ϵģ���������Ҫ��ȡ����ֶ�  
            Field field = testClass.getDeclaredField("userName");  
  
            //�ж����Field���Ƿ������ע��  
            if (field.isAnnotationPresent(FieldAnnotation.class)) {  
                System.out.println("this is a field Annotation");  
  
                //��������ע�⣬���ȡע����  
                FieldAnnotation fieldAnnotations = (FieldAnnotation) field.getAnnotation(FieldAnnotation.class);  
                if (fieldAnnotations != null) {  
                    //ͨ�������˽�б�����ֵ  
                    field.setAccessible(true);  
                    field.set(test, fieldAnnotations.value());  
                    System.out.println("value:" + test.getUserName());  
                }  
            } else {  
                System.out.println("this is not  a field Annotation");  
            }  
  
        } catch (Exception e) {  
        	System.out.println(e);
        }  
  
    }  
}  
