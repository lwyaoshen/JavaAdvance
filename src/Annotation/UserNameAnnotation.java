package Annotation;

import java.lang.annotation.Documented;  
import java.lang.annotation.ElementType;  
import java.lang.annotation.Inherited;  
import java.lang.annotation.Retention;  
import java.lang.annotation.RetentionPolicy;  
import java.lang.annotation.Target;  
  
/**
 * 1. Ԫע��
	ʲô��Ԫע�⣿�����������⣬Ԫע�����Զ���ע���ע�⡣Ԫע����Ҫ����4����������Ҫ��java.lang.annotation�п����ҵ��������Լ�Ҫ����ע���ʱ�����Ҫ�õ���ЩԪע�⡣���Ա��볹��������ĸ�Ԫע��ĺ��塣
	1. @Documented
	2. @Inherited
	3. @Retention
	4. @Target
	
	1. @Documented
	@Documented���������������͵�annotationӦ�ñ���Ϊ����ע�ĳ����Ա�Ĺ���API����˿��Ա�����javadoc����Ĺ����ĵ�����Documented��һ�����ע�⣬û�г�Ա��
	2. @Inherited
	@Inherited Ԫע����һ�����ע�⣬@Inherited������ĳ������ע�������Ǳ��̳еġ����һ��ʹ����@Inherited���ε�annotation���ͱ�����һ��class�������annotation�������ڸ�class�����ࡣ
	3. @Target
	@Target˵����Annotation�����εĶ���Χ��Annotation�ɱ����� packages��types���ࡢ�ӿڡ�ö�١�Annotation���ͣ������ͳ�Ա�����������췽������Ա������ö��ֵ�������������ͱ��ر�������ѭ��������catch��������
	ElementType.CONSTRUCTOR  �����ڹ�����
	ElementType.FIELD  ��������/����
	ElementType.LOCAL_VARIABLE  ���������ֲ�����
	ElementType.METHOD  �����ڷ���
	ElementType.PACKAGE   ����������
	ElementType.PARAMETER   ������������
	ElementType.TYPE   ���������ࡢ�ӿ�(����ע������) ��enum���������
	�������ζ���ķ�Χ��
	@Target(ElementType.TYPE)  
	�����
	@Target({ ElementType.TYPE, ElementType.METHOD})  
	4. Retention
	�����˸�Annotation��������ʱ�䳤�̣�ĳЩAnnotation��������Դ�����У���������������������һЩȴ��������class�ļ��У�������class�ļ��е�Annotation���ܻᱻ��������ԣ�����һЩ��class��װ��ʱ������ȡ����ע�Ⲣ��Ӱ��class��ִ�У���ΪAnnotation��class��ʹ�����Ǳ�����ģ���ʹ�����meta-Annotation���Զ� Annotation�ġ��������ڡ����ơ�
	RetentionPolicy.RUNTIME ע�����class�ֽ����ļ��д��ڣ�������ʱ����ͨ�������ȡ��
	RetentionPolicy.CLASS Ĭ�ϵı������ԣ�ע�����class�ֽ����ļ��д��ڣ�������ʱ�޷����
	RetentionPolicy.SOURCE ע���������Դ���У���class�ֽ����ļ��в�����
 *   
 *   
 *   
 *   
 *   
 *   
 *   
 */




  
/**
 * 
 * @ClassName:       UserNameAnnotations
 * @Description:    ����һ���û������Զ���ע�� 
 * @author:         yangsheng
 */
@Documented  
@Retention(RetentionPolicy.RUNTIME)  
@Target({ ElementType.TYPE, ElementType.METHOD})  
@Inherited  
public @interface UserNameAnnotation {  
  
    public String value() default "";  
  
}  
