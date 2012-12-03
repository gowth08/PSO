/*A driver class for all the classes
 */
package PSO;
class Driver
{
	public static void main(String a[])
	{
		
		int i;
		Pso p=new Pso();
		int iterate_sum=0;
		double gbest_sum=0;
		System.out.println ("Pso : ");
		System.out.println ("Pso\ncost: "+call(new Pso()));
		System.out.println ("\nVariableInertiaPso:\ncost: "+call(new VariableInertiaPso()));
		System.out.println ("\n\nModifiedNewPso :\ncost "+call(new ModifiedNewPso()));
		System.out.println ("\n\nRandomPso :\ncost "+call(new RandomPso()));
		System.out.println ("\n\nOrthogonalPso :\ncost "+call(new OrthogonalPso()));
		System.out.println ("\n\nSAPso :\ncost "+call(new SAnnealingPso()));
		
		System.out.println ("\n\nMultiThreadedPso : ");
		for(i=0;i<10;i++)
		{
		MTPso_driver();
			iterate_sum+=p.gbest_iter_no;
			gbest_sum+=p.gbestv;
		}
		System.out.println ("Iterations: "+iterate_sum/10+"\nGbest : "+gbest_sum/10);
		iterate_sum=0;
		 gbest_sum=0;
		System.out.println ("\n\nParallelPso : ");
		for(i=0;i<10;i++)
		{
		ParallelPso_driver(new ParallelPso_Server(10),10);
			iterate_sum+=p.gbest_iter_no;
			gbest_sum+=p.gbestv;
		}
		System.out.println ("Iterations: "+iterate_sum/10+"\nGbest : "+gbest_sum/10);
		System.out.println ("\n\nParallelOrthogonalPso : ");
		iterate_sum=0;
		 gbest_sum=0;
		for(i=0;i<10;i++)
		{
		ParallelPso_driver(new ParallelOrthogonalPso_Server(10),10);
			iterate_sum+=p.gbest_iter_no;
			gbest_sum+=p.gbestv;
		
		}
		System.out.println ("Iterations: "+iterate_sum/10+"\nGbest : "+gbest_sum/10);
	}
	
	private static double call(Pso p)
	{
		
		int iterate_sum=0;
		double gbest_sum=0;
		p.bInit();
		p.init();
		p.generate();
		for(int i=0;i<10;i++)
		{
			p.iterate();
			iterate_sum+=p.gbest_iter_no;
			gbest_sum+=p.gbestv;
		}
		
	//	p.disp();		
	
	return gbest_sum/10;
		
	}
	private static void MTPso_driver()
	{
		MultiThreadPso p1=new MultiThreadPso();
		MultiThreadPso p2=new MultiThreadPso();
		MultiThreadPso p3=new MultiThreadPso();
		try 
		{
			p1.t.join();
			p2.t.join();
			p3.t.join();
	    }
	    catch (Exception ex) {}
		p1.disp();
		p2.disp();
		p3.disp();
		
	}

	public static void ParallelPso_driver(ParallelPso_Server p,int no_of_clients)
	{
		
		
		ParallelPso_Client c[]=new ParallelPso_Client[no_of_clients];
		p.init();
		
		
		for (int i = 0; i<no_of_clients; i++)
		{
				c[i]=new ParallelPso_Client();
				c[i].init();
				
		}
		
		
		p.writeInitData();
	
		for (int i = 0; i<no_of_clients; i++)
			c[i].t.start();
		p.interactWithClient();
		
		for (int i = 0; i<no_of_clients; i++)
		{
			try 
			{
				c[i].t.join();	
		    }
		    catch (Exception ex) {}
		    
		}
		p.disp();
		
	}
		
}