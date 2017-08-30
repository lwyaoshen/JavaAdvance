package Annotation;

import java.lang.reflect.Field;

public class FieldAnnotationTest {  
	  
    public static void main(String[] args) {  
        User test = new User();  
        Class<User> testClass = User.class;  
        try {  
            //因为是注解到Field上的，所以首先要获取这个字段  
            Field field = testClass.getDeclaredField("userName");  
  
            //判断这个Field上是否有这个注解  
            if (field.isAnnotationPresent(FieldAnnotation.class)) {  
                System.out.println("this is a field Annotation");  
  
                //如果有这个注解，则获取注解类  
                FieldAnnotation fieldAnnotations = (FieldAnnotation) field.getAnnotation(FieldAnnotation.class);  
                if (fieldAnnotations != null) {  
                    //通过反射给私有变量赋值  
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
