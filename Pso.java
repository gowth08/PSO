package PSO;
import java.io.*;
import java.util.*;
class Int implements Serializable
{
	public int val;
	Int(int v)
	{
		val=v;
	}
}
class Doubl implements Serializable
{
	public double val;
	Doubl(double v)
	{
		val=v;
	}
}

public class Pso
{
	int max_iterate;
	int gbest_iter_no; 
	int cur_iter_no;
	int no_of_processors;
	int no_of_tasks;
	int no_of_particles;
	LinkedList tasks;
	LinkedList pbest[];
	double pbestv[];
	LinkedList gbest;
	double gbestv=0;
	
	BufferedReader in;		
	public void bInit()
	{
		try
		{
		in=new BufferedReader(new FileReader("Basic_data.txt"));
		TaskInfo.no_of_processors=no_of_processors=Integer.parseInt(in.readLine());
		no_of_tasks=Integer.parseInt(in.readLine());
		TaskInfo.no_of_particles=no_of_particles=Integer.parseInt(in.readLine());
		max_iterate=Integer.parseInt(in.readLine());
		gbest=new LinkedList();
		pbest=new LinkedList[no_of_particles];
		pbestv=new double[no_of_particles];
		tasks=new LinkedList();
		
		}catch(Exception e){}
	}
	
	public void init()
	{
		
		TaskInfo task;
		int i,j;
		for(i=0;i<no_of_particles;i++)
		{
			pbest[i]=new LinkedList();
			for(j=0;j<no_of_tasks;j++)
				pbest[i].add(new Int(0));
		}
		TaskInfo.init();
		for( i=0;i<no_of_tasks;i++)
		{
			task=new TaskInfo();
			task.set();
			tasks.add(task);
			gbest.add(new Int(0));
		}
		TaskInfo.close();
		
	}
	public boolean unique(int index)
	{
		int i,j;
		for( i=0;i<index;i++)
		{
			for(j=0;j<no_of_tasks;j++)
			{
					if(((TaskInfo)tasks.get(j)).allocation[index]!=((TaskInfo)tasks.get(j)).allocation[i])
					break;
					
			}
			if(j==no_of_tasks)
   				return false;
		}
	return true;
	}
	public void transform_particle(LinkedList proc_assign[],int index)
	{
		int t;
		for(int i=0;i<no_of_tasks;i++)
		{
			t=((TaskInfo)tasks.get(i)).allocation[index]-1;
					if(t<0)t=0;
					proc_assign[t].add(new Int(i));
		}
	}
	public void fit_calc(int index)
	{
		
		int i,j,pos=0;
		TaskInfo ti;
		int maxspan=0;
  		double obj=0;
  		int tim[]=new int[no_of_processors];
  		LinkedList proc_assign[]=new LinkedList[no_of_processors];
   		for(i=0;i<no_of_processors;i++)
			proc_assign[i]=new LinkedList();
		transform_particle(proc_assign,index);   		
   		for(i=0;i<no_of_processors;i++)
  		{
  			for(j=0;j<proc_assign[i].size();j++)
  			{
  				pos=((Int)proc_assign[i].get(j)).val;
  				ti=(TaskInfo)tasks.get(pos);
  				tim[i]+=ti.exec_t[i];
  			}
  			if(maxspan<tim[i])
  				maxspan=tim[i];
  		}
  		for ( i = 0; i<no_of_processors; i++)
  			obj+=(double)tim[i]/maxspan;
  		if(gbestv<obj)
  		{
  			for(i=0;i<no_of_tasks;i++)
  					((Int)pbest[index].get(i)).val=((Int)gbest.get(i)).val=((TaskInfo)tasks.get(i)).allocation[index];
  			pbestv[index]=gbestv=obj;
  			gbest_iter_no=cur_iter_no;
  			
  		}
  		else if(pbestv[index]<obj)
  		{
  			for(i=0;i<no_of_tasks;i++)
  				((Int)pbest[index].get(i)).val=((TaskInfo)tasks.get(i)).allocation[index];
  			pbestv[index]=obj;
  			
  		}
  		
	}
	public void generate()
	{
		
		 for(int i=0;i<no_of_particles;i++)
		 {
		 	
		 	do
  			{
  				form_particle(i);
  			}while(!unique(i));
  			fit_calc(i);
 		}
	}
	
	private void form_particle(int index)
	{
		Random rand=new Random();
		int temp,i;
		for( i=1;i<=no_of_processors;i++)
		{
			do
			{
				temp=rand.nextInt(no_of_tasks);
			}while(((TaskInfo)tasks.get(temp)).allocation[index]!=0);
			((TaskInfo)tasks.get(temp)).allocation[index]=i;
		}
		
		for(i=0;i<no_of_tasks;i++)
		{
			if(((TaskInfo)tasks.get(i)).allocation[index]==0)
				((TaskInfo)tasks.get(i)).allocation[index]=rand.nextInt(no_of_processors)+1;
		}
	}
	public double update(double w)
	{
		return w;
	}
	public void iterate()
	{
		double w=0.9;
		LinkedList vel[]=new LinkedList[no_of_particles];
		Doubl v;
		int i,j,k,pb,gb,pc;
		
		for(i=0;i<no_of_particles;i++)
		{
			vel[i]=new LinkedList();
			for(j=0;j<no_of_tasks;j++)
			{
				vel[i].add(new Doubl(0.0));
			}
		}
		for(cur_iter_no=1;cur_iter_no<=max_iterate;cur_iter_no++)
		{
			for(j=0;j<no_of_particles;j++)
			{
				for(k=0;k<no_of_tasks;k++)
    			{
					v=(Doubl)vel[j].get(k);
					pb=((Int)pbest[j].get(k)).val;
					gb=((Int)gbest.get(k)).val;
					pc=((TaskInfo)tasks.get(k)).allocation[j];
					v.val=w*v.val+2*(Math.random()*(pb-pc)+Math.random()*(gb-pc));
					((TaskInfo)tasks.get(k)).allocation[j]=(int)(Math.abs(pc+v.val+0.5)%no_of_processors)+1;
				}
				fit_calc(j);
			}
		w=update(w);
		}
	}
	public void disp()
	{
		System.out.println ("Best Allocation : ");
		for(int i=0;i<no_of_tasks;i++)
			System.out.print(((Int)gbest.get(i)).val+" ");
		System.out.println("\nCost : "+gbestv+"\nNo. of Iterations : "+gbest_iter_no);
	
		
	}
}

