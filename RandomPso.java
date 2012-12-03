package PSO;

import java.io.*;
import java.util.*;

public class RandomPso extends Pso
{
	int no_of_sets;
	int set_no;
	public void bInit()
	{
		super.bInit();
		for(int i=0;i<no_of_tasks;i++)
			gbest.add(new Int(0));
		try 
		{
			no_of_sets=Integer.parseInt(in.readLine());
	    }
	    catch (Exception ex) {}
		
		max_iterate/=no_of_sets;
	}
	public double update(double w)
	{
		return ((.9-.4)*cur_iter_no)/max_iterate;
	}
	public void init()
	{
		
		TaskInfo task;
		int i,j;
		tasks=new LinkedList();	
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
		
		}
		TaskInfo.close();
		
	}
	
	public void iterate()
	{
	double prevgb=gbestv;
	int cur_set_no;
	for ( cur_set_no = 1; cur_set_no<no_of_sets; cur_set_no++)	
	{
		super.iterate();
	
	
		if(prevgb!=gbestv)
		{
				set_no=cur_set_no;
				prevgb=gbestv;
		}
		
		init();
		generate();
		
	}
	super.iterate();
	if(prevgb!=gbestv)
			set_no=cur_set_no;
		
	}
	
	public void disp()
	{
		super.disp();
		System.out.println("set no :"+set_no );
	}
}