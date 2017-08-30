package Annotation;

import java.lang.annotation.Documented;  
import java.lang.annotation.ElementType;  
import java.lang.annotation.Retention;  
import java.lang.annotation.RetentionPolicy;  
import java.lang.annotation.Target;  
  
/**
 * 
 * 
 * @ClassName:       MethodType
 * @Description:    定义一个作用到方法的注解 
 * @author:         yangsheng
 */
@Documented//文档  
@Retention(RetentionPolicy.RUNTIME)//在运行时可以获取  
@Target({ ElementType.TYPE, ElementType.METHOD })//作用到类，方法，接口上等  
public @interface MethodType {  
  
    //枚举类型  
    public enum MethodTypeEnum {  
        TYPE1, TYPE2  
    }  
  
    //实际的值  
    public MethodTypeEnum methodType() default MethodTypeEnum.TYPE1;  
} 
