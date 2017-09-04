package JavaExceptions;
/**
 * 	finally关键字
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
	        System.out.println("try语句块中");  
	        return 1;  
	    } finally {  
	        System.out.println("finally语句块中");  
	        return 2;  
	    }  
	}
}
//try中的return语句调用的函数先于finally中调用的函数执行，也就是说return语句先执行，finally语句后执行，所以，返回的结果是2。return并不是让函数马上返回，而是return语句执行后，将把返回结果放置进函数栈中，此时函数并不是马上返回，它要执行finally语句后才真正开始返回。

