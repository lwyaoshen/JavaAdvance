package MyHashMap;

/**
 * ���������Ľṹ
 */

public class MyHashMap implements Map {
    //Ĭ������16
   private final int DEFALUT_CAPACITY=16;
   //�ڲ��洢�ṹ������
   Node[] table=new Node[DEFALUT_CAPACITY];
   private int size=0;
   public int size() {
        return size;
   }

    public boolean isEmpty() {
        return size==0;
    }

    public Object get(Object key) {
        int hashValue=hash(key);
        int i=indexFor(hashValue, table.length);
        for (Node node = table[i]; node!=null; node=node.next) {

           if (node.key.equals(key)&&hashValue==node.hash) {
               return node.value;

          }

        }
        return null;
    }

    public Object put(Object key,Object value) {
        //ͨ��key����hashֵ
        int hashValue=hash(key);
        //ͬ��hashֵ���ҵ����keyӦ�÷���������ĸ�λ�ã�i��
        int i=indexFor(hashValue,table.length);
        //iλ���Ѿ���������
        for (Node node = table[i]; node!=null; node=node.next) {

            Object k;
            //���������������key�ģ�������ԭʼ��value
            if (node.hash==hashValue&&((k=node.key)==key||key.equals(k))) {

                Object oldValue=node.value;
                node.value=value;
                return oldValue;

            }
        }
        //���iλ��û�����ݣ�����iλ�������ݣ�����key���µ�key���������ڵ�
        addEntry(key, value, hashValue, i);
        return null;

    }

    public void addEntry(Object key, Object value, int hashValue, int i) {
        //�������������ԭʼ�Ĵ�С������������
        if (++size==table.length) {
            Node [] newTable=new Node[table.length*2];
            System.arraycopy(table, 0, newTable, 0, table.length);
            table=newTable;
        }
        //�õ�iλ�õ�����
        Node eNode=table[i];
        //�����ڵ㣬���ýڵ��nextֵָ��ǰһ���ڵ�
        table[i]=new Node(hashValue, key, value, eNode);

        /*
         * �������仰����Ҫע����ǣ����iλ��ԭʼλ��table[i]û��ֵ��eNodeΪ�գ���ֱ�ӽ��µ�node����table[i],������node��nextָ���
         * ������ֵ�Ļ�����λ��������һ��node���γ�һ������ṹ���µ�node���뵽ԭʼnode��ǰ�棬�����µ�node��nextָ��ԭʼλ��i��node
         */
    }

    public int indexFor(int hashValue, int length) {


        return hashValue%length;
    }

    public int hash(Object key) {

        return key.hashCode();
    }

    static  class Node implements Map.Entry{
        int hash;
        Object key;
        Object value;
        Node next;//ָ����һ���ڵ�
        Node(int hash, Object key, Object value, Node next) {
                this.hash = hash;
                this.key = key;
                this.value = value;
                this.next = next;
            }
        public Object getKey() {
            return key;
        }

        public Object getValue() {
            return value;
        }



    }

    public static void main(String[] args) {
        MyHashMap hashMap=new MyHashMap();
        hashMap.put("aaa", "1111");
        hashMap.put("bbb", "2222");
        hashMap.put("ccc", "3333");
        hashMap.put("ddd", "4444");
        hashMap.put("eee", "5555");
        hashMap.put("ffff", "9666");
        hashMap.put("ggg", "geg");
        System.out.println(hashMap.get("ggg"));
    }

}
