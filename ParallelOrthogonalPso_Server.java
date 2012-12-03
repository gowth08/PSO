package PSO;
import java.io.*;
import java.util.*;
import java.net.*;
public class ParallelOrthogonalPso_Server extends ParallelPso_Server
{
	
	//public ParallelOrthogonalPso(){}
	public ParallelOrthogonalPso_Server(int n)
	{
		super(n);
	}
	
	public void generate()
	{
			int Q,N,M,J,k,j,x,i,s,t;
		TaskInfo TIrhs,TIj,TIs;
		M=no_of_particles;
		N=no_of_tasks;
		Q=no_of_processors;
		J=(int)Math.ceil(Math.log((double)N*(Q-1)+1));
		int y=(int)Math.pow(Q,J);
		for(k=1;k<=J;k++)
		{
			
			j=(int)(Math.pow(Q,k-1)-1)/(Q-1);
			if(j>=N)break;
			for(i=0;i<M;i++)
			{
				((TaskInfo)tasks.get(j)).allocation[i]=(int)Math.floor((i-1)/(Math.pow(Q,J-k)))%Q+1;
			}
		}
		
		for(k=2;k<=J;k++)
		{
			j=(int)(Math.pow(Q,k-1)-1)/(Q-1);
			if(j>=N)break;
			TIj=(TaskInfo)tasks.get(j);
			for(s=1;s<j;s++)
			{
				TIs=(TaskInfo)tasks.get(s);
				for(t=1;t<=Q;t++)
				{
					x=(s-1)*(Q-1)+t+j-1;
					if(x>=N)break;
					TIrhs=(TaskInfo)tasks.get(x);
					for(i=0;i<M;i++)
					{
						TIrhs.allocation[i]=(TIs.allocation[i]*t+TIj.allocation[i])%Q+1;
					}
				}
			}		
		}
	}	
}