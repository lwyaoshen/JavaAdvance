package ClassInit;

/**
 * 
 * @ClassName:       Child
 * @Description:    ����
 * @author:         yangsheng
 */
public class Child extends Parent {  
  
    private String stringInChild = initStringInChild();  
    private static String staticStringInChild = initStaticStringInChild();  
  
    {  
        System.out.println("���ࣺ��ͨ�����");  
    }  
  
    static {  
        System.out.println("���ࣺ��̬�����");  
    }  
  
    public Child() {  
        System.out.println("���ࣺ���췽��");  
    }  
  
    private static String initStaticStringInChild() {  
        System.out.println("���ࣺ��̬����������̬��Ա������ֵ���á�");  
        return "initStaticStringInChild";  
    }  
  
    private String initStringInChild() {  
        System.out.println("���ࣺ��ͨ��Ա����������ͨ��Ա������ֵ���á�");  
        return "initStringInChild";  
    }  
} 