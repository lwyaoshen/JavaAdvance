package Annotation;

import Annotation.MethodType.MethodTypeEnum;


/**
 *  
 * @ClassName:       Test
 * @Description:    һ��ע��Ĳ�����
 * @author:         yangsheng
 */
//ע��ע��������������  
//����ͨ������ ��ȡ�����Ϣ֮�� ��ȡ�õ����ע���ֵ  
@UserNameAnnotation(value = "initphp")   
public class User {  
	@FieldAnnotation(value="zhuli")  
    private String userName;  
  
    
  //ע�⵽  
    @MethodType(methodType=MethodTypeEnum.TYPE2)  
    public String getUserName() {  
        return userName;  
    }  
  
    public void setUserName(String userName) {  
        this.userName = userName;  
    }  
  
} 