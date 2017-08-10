package JavaMultiThreading;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;


public class ThreadPool {
	private static int work_num;//工作线程个数
	
	private WorkThread[] workers;//工作线程数组
	
	
	private static volatile int finished_task = 0;//已经完成的任务
	 
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
            System.out.println("此时的工作队列size为" +taskQueue.size() );
            taskQueue.notify();  
		}
    } 
	public void destroy() {  
        while (!taskQueue.isEmpty()) {// 如果还有任务没执行完成，就先睡会吧  
            try {  
                Thread.sleep(100);  
            } catch (InterruptedException e) {  
                e.printStackTrace();  
            }  
        }  
        // 工作线程停止工作，且置为null  
        for (int i = 0; i < work_num; i++) {  
            workers[i].stopWork();  
            workers[i] = null;  
        }  
        threadpool=null;  
        taskQueue.clear();// 清空任务队列  
    } 
	public int getWorkThreadNumber() {  
        return work_num;  
    }  
  
    // 返回已完成任务的个数,这里的已完成是只出了任务队列的任务个数，可能该任务并没有实际执行完成  
    public int getFinishedTasknumber() {  
        return finished_task;  
    }  
  
    // 返回任务队列的长度，即还没处理的任务个数  
    public int getWaitTasknumber() {  
        return taskQueue.size();  
    }  
  
    // 覆盖toString方法，返回线程池信息：工作线程个数和已完成任务个数  
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
                    r = taskQueue.poll();// 取出任务  
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
	
	

