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
 * @Description:    ����һ�����õ�������ע�� 
 * @author:         yangsheng
 */
@Documented//�ĵ�  
@Retention(RetentionPolicy.RUNTIME)//������ʱ���Ի�ȡ  
@Target({ ElementType.TYPE, ElementType.METHOD })//���õ��࣬�������ӿ��ϵ�  
public @interface MethodType {  
  
    //ö������  
    public enum MethodTypeEnum {  
        TYPE1, TYPE2  
    }  
  
    //ʵ�ʵ�ֵ  
    public MethodTypeEnum methodType() default MethodTypeEnum.TYPE1;  
} 
