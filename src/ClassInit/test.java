package ClassInit;

/**
 *  1.父类【静态成员】和【静态代码块】，按在代码中出现的顺序依次执行。
	2.子类【静态成员】和【静态代码块】，按在代码中出现的顺序依次执行。
	3.父类的【普通成员变量被普通成员方法赋值】和【普通代码块】，按在代码中出现的顺序依次执行。
	4.执行父类的构造方法。
	5.子类的【普通成员变量被普通成员方法赋值】和【普通代码块】，按在代码中出现的顺序依次执行。
	6.执行子类的构造方法。
 *  
 *  
 *  
 * @ClassName:       test
 * @Description:    TODO
 * @author:         yangsheng
 */
public class test {  
    public static void main(String[] args) {  
        System.out.println("测试代码开始");  
        new Child();  
        System.out.println("测试代码结束");  
    }  
} 
