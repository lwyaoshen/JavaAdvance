package JavaExceptions;
/**
 * 	finally�ؼ���
 * 
 * @ClassName:       test
 * @Description:    TODO
 * @author:         yangsheng
 */
public class test {
public static void main(String[] args) {
	int i = test1();
	int j = test2();
	System.out.println(i);
	System.out.println(j);
}
	public static int test1() {  
	    try {  
	        return 1;  
	    } finally {  
	        return 2;  
	    }  
	} 
	public static int test2() {  
	    int i = 1;  
	    try {  
	        System.out.println("try������");  
	        return 1;  
	    } finally {  
	        System.out.println("finally������");  
	        return 2;  
	    }  
	}
}
//try�е�return�����õĺ�������finally�е��õĺ���ִ�У�Ҳ����˵return�����ִ�У�finally����ִ�У����ԣ����صĽ����2��return�������ú������Ϸ��أ�����return���ִ�к󣬽��ѷ��ؽ�����ý�����ջ�У���ʱ�������������Ϸ��أ���Ҫִ��finally�����������ʼ���ء�

