package Annotation;

import java.lang.reflect.Method;  

import Annotation.MethodType.MethodTypeEnum;

  
public class MethodTypeTest {  
  
    public static void main(String[] args) {  
        Class<User> testClass = User.class;  
        try {  
            //��Ϊ��ע�⵽method�ϵģ���������Ҫ��ȡ�������  
            Method method = testClass.getDeclaredMethod("getUserName");  
              
            //�ж�����������Ƿ������ע��  
            if (method.isAnnotationPresent(MethodType.class)) {  
                System.out.println("this is a method Annotation");  
                  
                //��������ע�⣬���ȡע����  
                MethodType methodType = (MethodType) method.getAnnotation(MethodType.class);  
                if (methodType != null) {  
                    if (MethodTypeEnum.TYPE1.equals(methodType.methodType())) {  
                        System.out.println("this is TYPE1");  
                    } else {  
                        System.out.println("this is TYPE2");  
                    }  
                }  
            } else {  
                System.out.println("this is not  a method Annotation");  
            }  
  
        } catch (Exception e) {  
        }  
  
    }  
}  
