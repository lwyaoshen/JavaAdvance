package ClassInit;

/**
 * 
 * @ClassName:       Parent
 * @Description:    ����
 * @author:         yangsheng
 */
public class Parent {  
    static {  
        System.out.println("���ࣺ��̬�����");  
    }  
  
    {  
        System.out.println("���ࣺ��ͨ�����");  
    }  
  
    private static String staticStringInParent = initStaticStringInParent();  
  
    private String stringInParent = initStringInParent();  
  
    public Parent() {  
        System.out.println("���ࣺ���췽��");  
    }  
  
    private static String initStaticStringInParent() {  
        System.out.println("���ࣺ��̬����������̬��Ա������ֵ���á�");  
        return "initStaticStringInParent";  
    }  
    private String initStringInParent() {  
        System.out.println("���ࣺ��ͨ��Ա����������ͨ��Ա������ֵ���á�");  
        return "initStringInParent";  
    }  
  
}  