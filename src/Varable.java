/**
 * 
 * @ClassName:       Varable
 * @Description:    Java1.5�����������ԣ��ɱ�����������ڲ���������ȷ��������ȷ���������java�ѿɱ�����������鴦��

					ע�⣺�ɱ��������λ�����һ�

					ԭ�򣺵��ɱ������������һ��ʱ���ؽ���һ���������һ�����ֻ֧����һ���ɱ��������Ϊ�����������������Ե����߻�����ͬ���Ͳ���ʱ��java�޷����ִ���Ĳ�������ǰһ���ɱ�������Ǻ�ߵĲ���������ֻ���ÿɱ����λ�����һ�
 * 					��1��ֻ�ܳ����ڲ����б����� 

					��2��...λ�ڱ������ͺͱ�����֮�䣬ǰ�����޿ո񶼿��ԣ�

					��3�����ÿɱ�����ķ���ʱ��������Ϊ�ÿɱ������������һ�����飬�ڷ����������������ʽ���ʿɱ������
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
