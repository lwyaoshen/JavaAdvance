/**
 * 
 * @ClassName:       Varable
 * @Description:    Java1.5增加了新特性：可变参数：适用于参数个数不确定，类型确定的情况，java把可变参数当做数组处理。

					注意：可变参数必须位于最后一项。

					原因：当可变参数个数多余一个时，必将有一个不是最后一项，所以只支持有一个可变参数。因为参数个数不定，所以当其后边还有相同类型参数时，java无法区分传入的参数属于前一个可变参数还是后边的参数，所以只能让可变参数位于最后一项。
 * 					（1）只能出现在参数列表的最后； 

					（2）...位于变量类型和变量名之间，前后有无空格都可以；

					（3）调用可变参数的方法时，编译器为该可变参数隐含创建一个数组，在方法体中以数组的形式访问可变参数。
 * @author:         yangsheng
 */


public class Varable {
    public static void main(String[] args) {
        System.out.println(add(2, 3));
        System.out.println(add(2, 3, 5));
    }
 
    public static int add(int x, int... args) {
        int sum = x;
        for (int i = 0; i < args.length; i++) {
            sum += args[i];
        }
        return sum;
    }
}
