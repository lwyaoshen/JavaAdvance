# ǿ���� ( Strong Reference )
ǿ������ʹ�����ձ�����á����һ���������ǿ���ã�����������������������������ڴ�ռ䲻�㣬Java�������Ը�׳�OutOfMemoryError����ʹ�����쳣��ֹ��Ҳ���῿������վ���ǿ���õĶ���������ڴ治������⡣ ps��ǿ������ʵҲ��������ƽʱA a = new A()�����˼��
## ǿ��������
- ǿ���ÿ���ֱ�ӷ���Ŀ�����
- ǿ������ָ��Ķ������κ�ʱ�򶼲��ᱻϵͳ���ա�
- ǿ���ÿ��ܵ����ڴ�й©��

## Final Reference
- ��ǰ���Ƿ���finalizer�࣬ע������finalizer����JVM����־��( ������f�� )��������ָjava.lang.ref.Fianlizer�ࡣ����f���ǻᱻJVMע�ᵽjava.lang.ref.Fianlizer���еġ�

    1.��ǰ������к���һ������Ϊ�գ�����ֵΪvoid����Ϊfinalize�ķ�����

    2.���Ҹ�finalize��������ǿ�
- GC ��������
    
    1.������ΪFinalizer�����ö������һ����ʱ��ǿ���ã���ʹû��������ǿ���ã������޷����������գ�

    2.�������پ�������GC���ܱ����գ���Ϊֻ����FinalizerThreadִ������f�����finalize����������²��п��ܱ��´�GC���գ����п����ڼ��Ѿ����������GC�ˣ�����һֱ��ûִ�ж����finalize������
    
    3.CPU��Դ�Ƚ�ϡȱ�������FinalizerThread�߳��п�����Ϊ���ȼ��ȽϵͶ��ӳ�ִ�ж����finalize������
    
    4.��Ϊ�����finalize�����ٳ�û��ִ�У��п��ܻᵼ�´󲿷�f������뵽old�ִ�����ʱ��������old�ִ���GC������Full GC��GC��ͣʱ�����Ա䳤����������OOM��
    
    5.�����finalize���������ú����������ʵ����û�б����գ���Ȼ�����ڲ��õĽ����ᱻ���ա�
# ������ ( Soft Reference )
����������һЩ�����õ����Ǳ���Ķ��󡣶��������ù����ŵĶ�����ϵͳ��Ҫ�����ڴ�����쳣֮ǰ���������Щ�����н����շ�Χ֮�н��еڶ��λ��ա������λ��ջ�û���㹻���ڴ棬�Ż��׳��ڴ�����쳣��

���������ù����ŵĶ�������ڴ���㣬������������������ոö�������ڴ治���ˣ��ͻ������Щ������ڴ档�� JDK 1.2 ֮���ṩ�� SoftReference ����ʵ�������á������ÿ�����ʵ���ڴ����еĸ��ٻ��档�����ÿ��Ժ�һ�����ö��У�ReferenceQueue������ʹ�ã���������������õĶ����������������գ�Java������ͻ����������ü��뵽��֮���������ö����С�

ע�⣺Java ����������׼����SoftReference��ָ��Ķ�����л���ʱ�����ö���� finalize() ����֮ǰ��SoftReference��������ᱻ���뵽��� ReferenceQueue �����У���ʱ����ͨ�� ReferenceQueue �� poll() ����ȡ������

/**
  �����ã����������ù����ŵĶ�����ϵͳ��Ҫ�����ڴ�����쳣֮ǰ���������Щ�����н����շ�Χ֮�н��еڶ��λ���( ��Ϊ���ڵ�һ�λ��պ�Żᷢ���ڴ����ɲ����㣬��������ڶ��λ��� )�������λ��ջ�û���㹻���ڴ棬�Ż��׳��ڴ�����쳣��
  ���������ù����ŵĶ�������ڴ���㣬������������������ոö�������ڴ治���ˣ��ͻ������Щ������ڴ档
  ͨ��debug���֣���������pending״̬ʱ��referent���Ѿ���null�ˡ�
 
  ����������-Xmx5m
 
 */
public class SoftReferenceDemo {
 
    private static ReferenceQueue<MyObject> queue = new ReferenceQueue<>();
 
    public static void main(String[] args) throws InterruptedException {
        Thread.sleep(3000);
        MyObject object = new MyObject();
        SoftReference<MyObject> softRef = new SoftReference(object, queue);
        new Thread(new CheckRefQueue()).start();
 
        object = null;
        System.gc();
        System.out.println("After GC : Soft Get = " + softRef.get());
        System.out.println("�������ڴ�");
 
        /**
         * ====================== ����̨��ӡ ======================
         * After GC : Soft Get = I am MyObject.
         * �������ڴ�
         * MyObject's finalize called
         * Object for softReference is null
         * After new byte[] : Soft Get = null
         * ====================== ����̨��ӡ ======================
         *
         * �ܹ������� 3 �� full gc����һ����System.gc();�������ڶ������ڷ���new byte[5*1024*740]ʱ������Ȼ�����ڴ治�������ǽ�softRef������շ��أ����Ž����˵�����full gc��
         */
        byte[] b = new byte[5*1024*740];
 
        /**
         * ====================== ����̨��ӡ ======================
         * After GC : Soft Get = I am MyObject.
         * �������ڴ�
         * Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
         *      at com.bayern.multi_thread.part5.SoftReferenceDemo.main(SoftReferenceDemo.java:21)
         * MyObject's finalize called
         * Object for softReference is null
         * ====================== ����̨��ӡ ======================
         *
         * Ҳ�Ǵ����� 3 �� full gc����һ����System.gc();�������ڶ������ڷ���new byte[5*1024*740]ʱ������Ȼ�����ڴ治�������ǽ�softRef������շ��أ����Ž����˵�����full gc���������� full gc �����ڴ����ɲ������ڷ���new byte[5*1024*740]������׳���OutOfMemoryError�쳣��
         */
        byte[] b = new byte[5*1024*790];
 
        System.out.println("After new byte[] : Soft Get = " + softRef.get());
    }
 
    public static class CheckRefQueue implements Runnable {
 
        Reference<MyObject> obj = null;
 
        @Override
        public void run() {
            try {
                obj = (Reference<MyObject>) queue.remove();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
 
            if (obj != null) {
                System.out.println("Object for softReference is " + obj.get());
            }
 
        }
    }
 
    public static class MyObject {
 
        @Override
        protected void finalize() throws Throwable {
            System.out.println("MyObject's finalize called");
            super.finalize();
        }
 
        @Override
        public String toString() {
            return "I am MyObject.";
        }
    }
}
# ������ ( Weak Reference )
���������Ǳ���Ķ��󣬵�������ǿ�ȱ������ø���һЩ���������ù����Ķ���ֻ�����浽��һ�������ռ�����֮ǰ���������ռ�������ʱ�����۵�ǰ�ڴ��Ƿ��㹻��������յ�ֻ�������ù����Ķ���һ��һ�������ö����������������գ������뵽һ��ע�����ö����С�

ע�⣺Java ����������׼����WeakReference��ָ��Ķ�����л���ʱ�����ö���� finalize() ����֮ǰ��WeakReference��������ᱻ���뵽��� ReferenceQueue �����У���ʱ����ͨ�� ReferenceQueue �� poll() ����ȡ�����ǡ�

/**
  
  ���������Ǳ���Ķ��󣬵�������ǿ�ȱ������ø���һЩ���������ù����Ķ���ֻ�����浽��һ�������ռ�����֮ǰ���������ռ�������ʱ�����۵�ǰ�ڴ��Ƿ��㹻��������յ�ֻ�������ù����Ķ���һ��һ�������ö����������������գ������뵽һ��ע�����ö����С�
 */
 
public class WeakReferenceDemo {
    private static ReferenceQueue<MyObject> queue = new ReferenceQueue<>();
 
    public static void main(String[] args) {
 
        MyObject object = new MyObject();
        Reference<MyObject> weakRef = new WeakReference<>(object, queue);
        System.out.println("������������Ϊ : " + weakRef);
        new Thread(new CheckRefQueue()).start();
 
        object = null;
        System.out.println("Before GC: Weak Get = " + weakRef.get());
        System.gc();
        System.out.println("After GC: Weak Get = " + weakRef.get());
 
        /**
         * ====================== ����̨��ӡ ======================
         * ������������Ϊ : java.lang.ref.WeakReference@1d44bcfa
         * Before GC: Weak Get = I am MyObject
         * After GC: Weak Get = null
         * MyObject's finalize called
         * ɾ����������Ϊ : java.lang.ref.WeakReference@1d44bcfa , ��ȡ���������õĶ���Ϊ : null
         * ====================== ����̨��ӡ ======================
         */
    }
 
    public static class CheckRefQueue implements Runnable {
 
        Reference<MyObject> obj = null;
 
        @Override
        public void run() {
            try {
                obj = (Reference<MyObject>)queue.remove();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(obj != null) {
                System.out.println("ɾ����������Ϊ : " + obj + " , ��ȡ���������õĶ���Ϊ : " + obj.get());
 
            }
 
        }
    }
 
    public static class MyObject {
 
        @Override
        protected void finalize() throws Throwable {
            System.out.println("MyObject's finalize called");
            super.finalize();
        }
 
        @Override
        public String toString() {
            return "I am MyObject";
        }
    }
}

# ������ ( Phantom Reference )
PhantomReference �����С������á����������������͡���ͬ�������ú������ã��������޷�ͨ�� get() ������ȡ��Ŀ������ǿ���ôӶ�ʹ��Ŀ����󣬹۲�Դ����Է��� get() ����дΪ��Զ���� null��

�������õ�����ʲô���ã���ʵ��������Ҫ������ ���ٶ����������յ�״̬��ͨ���鿴���ö������Ƿ������������Ӧ�����������ж����Ƿ� �������������գ��Ӷ���ȡ�ж������������ڴ�����ȡ��Ŀ���������ã���Ŀ����󱻻���ǰ���������ûᱻ����һ�� ReferenceQueue �����У��Ӷ��ﵽ���ٶ����������յ����á�

��phantomReference���������ʱ��˵��referent��finalize()�����Ѿ����ã����������ռ���׼�����������ڴ��ˡ�

ע�⣺PhantomReference ֻ�е� Java ����������������ָ��Ķ����������л���ʱ���Ὣ����뵽��� ReferenceQueue �����У������Ϳ���׷�۶�����������������referent�����finalize()�����Ѿ����ù��ˡ�

���Ծ����÷���֮ǰ����������ͬ�������봫��һ�� ReferenceQueue ���󡣵������������ö���׼������������ʱ�������ûᱻ��ӵ���������С�