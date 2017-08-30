package Annotation;

import java.lang.annotation.Documented;  
import java.lang.annotation.ElementType;  
import java.lang.annotation.Retention;  
import java.lang.annotation.RetentionPolicy;  
import java.lang.annotation.Target;  
  
/**
 * 
 * @ClassName:       FieldAnnotations
 * @Description:    定义一个作用到域上的自定义注解 
 * @author:         yangsheng
 */
@Documented//文档  
@Retention(RetentionPolicy.RUNTIME)//在运行时可以获取  
@Target({ ElementType.FIELD })//作用到类的域上面  
public @interface FieldAnnotation {  
  
    public String value() default ""; //使用的时候 @FieldAnnotations(value="xxx")  
  
}  