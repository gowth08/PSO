package PSO;
import java.io.*;
import java.util.*;
import java.net.*;

class ObjToClient implements Serializable
{
	int no_of_processors;
	int max_iterate;
	int no_of_tasks;
	int no_of_particles;
	LinkedList tasks;
	LinkedList gbest;
	double gbestv=0;
	LinkedList pbest[];
	double pbestv[];
	
	ObjToClient(int p,int t,int pa,int mi,LinkedList ll,LinkedList gb,LinkedList pb[],double gv,double pv[])
	{
		max_iterate=mi;
		no_of_processors=p;
		no_of_tasks=t;
		no_of_particles=pa;
		tasks=new LinkedList(ll);
		pbest=pb;
		gbest=gb;
		gbestv=gv;
		pbestv=pv;
	}
}

public class ParallelPso_Server extends Pso
{
	
	Socket client[];
	ServerSocket ser;
	ObjectOutputStream oos[];
	ObjectInputStream ois[];
		
	

		
	public ParallelPso_Server(int noc)
	{
		try
		{
			bInit();
		 	ser=new ServerSocket(2000);
			client=new Socket[noc];
			new Server(client,ser);
		}catch(Exception e){System.out.println (e);}
	}
	
	public void init()
	{
		super.init();
		generate();
	}
	
	public void writeInitData()
	{
		ObjToClient obc;
		oos=new ObjectOutputStream[client.length];
		ois=new ObjectInputStream[client.length];
		try 
		{
			obc=new ObjToClient(no_of_processors,no_of_tasks,no_of_particles,max_iterate,tasks,gbest,pbest,gbestv,pbestv);
 			for (int i = 0; i<client.length; i++)	
			{
			    
		 		oos[i]=new ObjectOutputStream(client[i].getOutputStream());
				oos[i].writeObject(obc);
			}
 			
	    }
	    catch (Exception ex) {ex.printStackTrace();System.exit(0);}
	}
	public void interactWithClient()
	{
		ObjToClient obc;
		GbestInfo obs;
		int i;
		try 
 		{
 		ois=new ObjectInputStream[client.length];
		
		for (i = 0; i<client.length; i++)
			ois[i]=new ObjectInputStream(client[i].getInputStream());
		
		for (int j = 1; j<=max_iterate;j++)
			{
			
					for(i=0;i<client.length;i++)
 					{
 						obs=(GbestInfo)ois[i].readObject();
 						if(gbestv<obs.cost)
 						{
 							gbestv=obs.cost;
 							gbest=obs.gb;	
 							gbest_iter_no=j;
 						}
 					}
				
					obs=new GbestInfo(gbest,gbestv);
					for( i=0;i<client.length;i++)
 					{
 						oos[i].writeObject(obs);
 					}
 			
				
			}
		ser.close();	
 		}catch (Exception ex) {System.out.println (ex);}		
	}
}

class Server implements Runnable
{
	Socket s[];
	ServerSocket ser;
	Thread t;
	Server(Socket s[],ServerSocket ser)	
	{
		
		this.s=s;
		this.ser=ser;
		t=new Thread(this);
		t.start();
	}
	
	public void run()
	{
		try 
		{
			for (int i = 0; i<s.length; i++)
			{
			
					s[i]=ser.accept();
			}		
		
	    }catch (Exception ex) {System.out.println (ex);}
	}
}



