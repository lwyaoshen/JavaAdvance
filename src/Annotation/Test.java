package Annotation;



public class Test {  
    public static void main(String[] args) {  
        Class<User> testClass = User.class;  
        //��Ϊע����������������ģ����Կ���ͨ��isAnnotationPresent���ж��Ƿ���һ��  
        //��UserNameAnnotationsע�����  
        if (testClass.isAnnotationPresent(UserNameAnnotation.class)) {  
            System.out.println("this is a Annotations class");  
            //ͨ��getAnnotation���Ի�ȡע�����  
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
