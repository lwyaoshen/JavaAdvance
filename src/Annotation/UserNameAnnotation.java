package Annotation;

import java.lang.annotation.Documented;  
import java.lang.annotation.ElementType;  
import java.lang.annotation.Inherited;  
import java.lang.annotation.Retention;  
import java.lang.annotation.RetentionPolicy;  
import java.lang.annotation.Target;  
  
/**
 * 1. 元注解
	什么是元注解？你可以这样理解，元注解是自定义注解的注解。元注解主要包含4个。他们主要在java.lang.annotation中可以找到。我们自己要创建注解的时候必须要用到这些元注解。所以必须彻底理解这四个元注解的含义。
	1. @Documented
	2. @Inherited
	3. @Retention
	4. @Target
	
	1. @Documented
	@Documented用于描述其它类型的annotation应该被作为被标注的程序成员的公共API，因此可以被例如javadoc此类的工具文档化。Documented是一个标记注解，没有成员。
	2. @Inherited
	@Inherited 元注解是一个标记注解，@Inherited阐述了某个被标注的类型是被继承的。如果一个使用了@Inherited修饰的annotation类型被用于一个class，则这个annotation将被用于该class的子类。
	3. @Target
	@Target说明了Annotation所修饰的对象范围：Annotation可被用于 packages、types（类、接口、枚举、Annotation类型）、类型成员（方法、构造方法、成员变量、枚举值）、方法参数和本地变量（如循环变量、catch参数）。
	ElementType.CONSTRUCTOR  作用于构造器
	ElementType.FIELD  作用于域/属性
	ElementType.LOCAL_VARIABLE  用于描述局部变量
	ElementType.METHOD  作用于方法
	ElementType.PACKAGE   用于描述包
	ElementType.PARAMETER   用于描述参数
	ElementType.TYPE   用于描述类、接口(包括注解类型) 或enum声明，最常用
	单个修饰对象的范围：
	@Target(ElementType.TYPE)  
	多个：
	@Target({ ElementType.TYPE, ElementType.METHOD})  
	4. Retention
	定义了该Annotation被保留的时间长短：某些Annotation仅出现在源代码中，而被编译器丢弃；而另一些却被编译在class文件中；编译在class文件中的Annotation可能会被虚拟机忽略，而另一些在class被装载时将被读取（请注意并不影响class的执行，因为Annotation与class在使用上是被分离的）。使用这个meta-Annotation可以对 Annotation的“生命周期”限制。
	RetentionPolicy.RUNTIME 注解会在class字节码文件中存在，在运行时可以通过反射获取到
	RetentionPolicy.CLASS 默认的保留策略，注解会在class字节码文件中存在，但运行时无法获得
	RetentionPolicy.SOURCE 注解仅存在于源码中，在class字节码文件中不包含
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
 * @Description:    定义一个用户名的自定义注解 
 * @author:         yangsheng
 */
@Documented  
@Retention(RetentionPolicy.RUNTIME)  
@Target({ ElementType.TYPE, ElementType.METHOD})  
@Inherited  
public @interface UserNameAnnotation {  
  
    public String value() default "";  
  
}  
