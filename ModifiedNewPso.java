package PSO;
import java.io.*;
import java.util.*;
class ModifiedNewPso extends Pso
{
	public double update(double w)
	{
		return ((.9-.4)*cur_iter_no)/max_iterate;
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
					v.val=w*v.val+2*(Math.random()*(pc-pb)+Math.random()*(pc-gb));
					((TaskInfo)tasks.get(k)).allocation[j]=(int)(Math.abs(pc+v.val+0.5)%no_of_processors)+1;
				}
				fit_calc(j);
			}
		w=update(w);
		}
	}	public void disp()
	{
		System.out.println ("Best Allocation : ");
		for(int i=0;i<no_of_tasks;i++)
			System.out.print(((Int)gbest.get(i)).val+" ");
		System.out.println("\nCost : "+gbestv+"\nNo. of Iterations : "+gbest_iter_no);
	
		
	}
}