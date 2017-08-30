package Annotation;

import Annotation.MethodType.MethodTypeEnum;


/**
 *  
 * @ClassName:       Test
 * @Description:    一个注解的测试类
 * @author:         yangsheng
 */
//注入注解作用于类上面  
//可以通过反射 获取类的信息之后 获取得到这个注解的值  
@UserNameAnnotation(value = "initphp")   
public class User {  
	@FieldAnnotation(value="zhuli")  
    private String userName;  
  
    
  //注解到  
    @MethodType(methodType=MethodTypeEnum.TYPE2)  
    public String getUserName() {  
        return userName;  
    }  
  
    public void setUserName(String userName) {  
        this.userName = userName;  
    }  
  
} 