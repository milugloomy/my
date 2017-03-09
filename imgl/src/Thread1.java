
public class Thread1 implements Runnable {
	private String index;
	public Thread1(String index){
		this.index=index;
	}
    public void run() {  
          synchronized(this.index) {  
               for (int i = 0; i < 5; i++) {  
                    System.out.println(Thread.currentThread().getName() + " synchronized loop " + i);  
               }  
          }  
     }  
     public static void main(String[] args) {  
         Thread1 t1 = new Thread1("C");  
         Thread1 t2 = new Thread1("C");  
         Thread ta = new Thread(t1, "A");  
         Thread tb = new Thread(t2, "B");  
         ta.start();  
         tb.start();  
     } 
}