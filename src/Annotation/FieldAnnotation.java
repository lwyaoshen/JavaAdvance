package Annotation;

import java.lang.annotation.Documented;  
import java.lang.annotation.ElementType;  
import java.lang.annotation.Retention;  
import java.lang.annotation.RetentionPolicy;  
import java.lang.annotation.Target;  
  
/**
 * 
 * @ClassName:       FieldAnnotations
 * @Description:    ����һ�����õ����ϵ��Զ���ע�� 
 * @author:         yangsheng
 */
@Documented//�ĵ�  
@Retention(RetentionPolicy.RUNTIME)//������ʱ���Ի�ȡ  
@Target({ ElementType.FIELD })//���õ����������  
public @interface FieldAnnotation {  
  
    public String value() default ""; //ʹ�õ�ʱ�� @FieldAnnotations(value="xxx")  
  
}  