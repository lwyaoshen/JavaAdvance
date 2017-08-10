package JavaMultiThreading;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;


public class ThreadPool {
	private static int work_num;//�����̸߳���
	
	private WorkThread[] workers;//�����߳�����
	
	
	private static volatile int finished_task = 0;//�Ѿ���ɵ�����
	 
	 public static BlockingQueue<Callable> taskQueue = new ArrayBlockingQueue<Callable>(2000);
	 
	 private static ThreadPool threadpool = null;
	 
	 private ThreadPool(){
		 this(5);
	 }

	private ThreadPool(int i) {
		work_num = i;
		workers = new WorkThread[i];
		for(int j=0;j<i;j++){
			workers[j] = new WorkThread();
			new Thread(workers[j]).start();
		}
		
	}
	
	
	public static ThreadPool getThreadPool(int worker_num1,BlockingQueue queue) {  
		taskQueue = queue;
        if (worker_num1 <= 0)  
            worker_num1 = ThreadPool.work_num;  
        if (threadpool == null)  
            threadpool = new ThreadPool(worker_num1);
        return threadpool;  
    }
	public void execute(Callable task) {  
		synchronized (taskQueue) {
            taskQueue.add(task);  
            System.out.println("��ʱ�Ĺ�������sizeΪ" +taskQueue.size() );
            taskQueue.notify();  
		}
    } 
	public void destroy() {  
        while (!taskQueue.isEmpty()) {// �����������ûִ����ɣ�����˯���  
            try {  
                Thread.sleep(100);  
            } catch (InterruptedException e) {  
                e.printStackTrace();  
            }  
        }  
        // �����߳�ֹͣ����������Ϊnull  
        for (int i = 0; i < work_num; i++) {  
            workers[i].stopWork();  
            workers[i] = null;  
        }  
        threadpool=null;  
        taskQueue.clear();// ����������  
    } 
	public int getWorkThreadNumber() {  
        return work_num;  
    }  
  
    // �������������ĸ���,������������ֻ����������е�������������ܸ�����û��ʵ��ִ�����  
    public int getFinishedTasknumber() {  
        return finished_task;  
    }  
  
    // ����������еĳ��ȣ�����û������������  
    public int getWaitTasknumber() {  
        return taskQueue.size();  
    }  
  
    // ����toString�����������̳߳���Ϣ�������̸߳�����������������  
    @Override  
    public String toString() {  
        return "WorkThread number:" + work_num + "  finished task number:"  
                + finished_task + "  wait task number:" + getWaitTasknumber();  
    } 
	private class WorkThread implements Runnable{
		private volatile boolean isRunning = true;
		public void run(){
			
			isRunning = true;
			Callable r = null;
			while(isRunning){
				synchronized (taskQueue) {
				while(taskQueue.isEmpty()){
					try {
						taskQueue.wait(20);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (!taskQueue.isEmpty())  
                    r = taskQueue.poll();// ȡ������  
            }  
			Object o=null;
            if (r != null) {  
              try {
            	  o = r.call();
              } catch (Exception e) {
				e.printStackTrace();
              }
              r = null;    
                
            }  
		}
       } 
	public void stopWork(){
			isRunning = false;
	}
}
		
}
	
	

