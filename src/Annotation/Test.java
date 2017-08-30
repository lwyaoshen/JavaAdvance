package Annotation;



public class Test {  
    public static void main(String[] args) {  
        Class<User> testClass = User.class;  
        //因为注解是作用于类上面的，所以可以通过isAnnotationPresent来判断是否是一个  
        //有UserNameAnnotations注解的类  
        if (testClass.isAnnotationPresent(UserNameAnnotation.class)) {  
            System.out.println("this is a Annotations class");  
            //通过getAnnotation可以获取注解对象  
            UserNameAnnotation userNameAnnotations = (UserNameAnnotation) testClass.getAnnotation(UserNameAnnotation.class);  
            if (userNameAnnotations != null) {  
                System.out.println("value:" + userNameAnnotations.value());  
            } else {  
                System.out.println("null");  
            }  
        } 
        else {  
            System.out.println("this is not Annotations class");  
        }  
  
    }  
} 
