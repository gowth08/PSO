package PSO;
import java.io.*;
import java.util.*;
import java.net.*;

class GbestInfo implements Serializable
{
	LinkedList gb;
	double cost;
	GbestInfo(LinkedList gb,double cost)
	{
		this.gb=gb;
		this.cost=cost;	
	}
	
}
class ParallelPso_Client extends Pso implements Runnable
{
	Socket s;
	Thread t;
	ObjToClient otc;
	ObjectOutputStream oos;
	ObjectInputStream ois;
	public void init()
	{
		try 
		{
			s=new Socket(InetAddress.getLocalHost(),2000);
		}
	    catch (Exception ex) {System.out.println (ex);}
	    t=new Thread(this);
	    
		
	}
	

	public double update(double w)
	{
		
   	 	GbestInfo gi=new GbestInfo(gbest,gbestv);
   	 	try 
  		{
  			oos.writeObject(gi);
  			gi=(GbestInfo)ois.readObject();
		}catch (Exception ex) {System.out.println (ex);}
		gbest=new LinkedList(gi.gb);
		gbestv=gi.cost;
			return ((.9-.4)*cur_iter_no)/max_iterate;
	}
	
	public void run()
	{  
		
	    
	    int i,j;
		try
		{
		
		
	    	ois = new ObjectInputStream(s.getInputStream());
		
	    	
			oos = new ObjectOutputStream(s.getOutputStream());
		

	    	otc=(ObjToClient)ois.readObject();
	    	
	    	tasks=otc.tasks;
	    	
		
	    	no_of_processors=otc.no_of_processors;
			no_of_tasks=otc.no_of_tasks;
			no_of_particles=otc.no_of_particles;
			max_iterate=otc.max_iterate;
			gbest=new LinkedList(otc.gbest);
			pbest=new LinkedList[no_of_particles];
			pbestv=new double[no_of_particles];
			gbestv=otc.gbestv;
		
				
		
		}catch(Exception e){System.out.println (e);}
		
		for(i=0;i<no_of_particles;i++)
			pbest[i]=new LinkedList(otc.pbest[i]);
		iterate();
  			
  			
   	}
 }