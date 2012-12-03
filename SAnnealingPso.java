package PSO;
import java.io.*;
import java.util.*;

class SAnnealingPso extends Pso
{
	
	boolean need_sa;
	int obj;
	
	public double update(double w)
	{
		return ((.9-.4)*cur_iter_no)/max_iterate;
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
	
	public void doSimulatedAnnealing(int index)
	{
		int random_pos1=((int)(Math.random()*no_of_tasks))%no_of_tasks;
		int random_pos2;
		double T=0.6;
		double prob_fn;
		
		if(random_pos1==no_of_tasks-1)	
			random_pos2=random_pos1-1;
		else
			random_pos2=random_pos1+1;
			
		prob_fn=Math.exp((gbestv-obj)/T);
		
		if(prob_fn>0.5)
		{
			int hold=((TaskInfo)tasks.get(random_pos1)).allocation[index];
			((TaskInfo)tasks.get(random_pos1)).allocation[index]=((TaskInfo)tasks.get(random_pos2)).allocation[index];
			((TaskInfo)tasks.get(random_pos2)).allocation[index]=hold;	
			fit_calc(index);
		}
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
				if(need_sa==true)
				doSimulatedAnnealing(j);
			}
		w=update(w);
		}
	}

}
