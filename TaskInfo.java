/* A Java implementation for "Multiprocessor Scheduling using Particle Swarm Optimization"
 * All classes required are in the PSO package
 * class --- TaskInfo
 * This class holds the  information required about - tasks , processors , paricle_size 
 * - allocation of tasks to processors in a particular particle etc
 * reads the data from the "static_input.txt" file which contains data in the following format
 * task_name arrival_time execution_time 
 */
package PSO;
import java.io.*;
import java.util.*;
class TaskInfo implements Serializable
{
	public int exec_t[];
	public int allocation[];
	public static int no_of_particles;
	public static int no_of_processors;
	private static BufferedReader in;
	public static void init()
	{
		try
		{
			in=new BufferedReader(new FileReader("static_input.txt"));
		}catch(Exception e){}
	}
	//initializes the allocation array to the particle size provided by the "basic_data" file
	TaskInfo()
	{
		allocation=new int[no_of_particles];
		exec_t=new int[no_of_processors];
	}
	/* this method reads one line from the file,tokenizes it and
	 * initializes the data members of the TaskInfo class
	 */
	public void set()
	{
		StringTokenizer st;
		String line;
		try
		{
			st=new StringTokenizer(in.readLine());
			for (int i = 0;st.hasMoreTokens(); i++)
			exec_t[i]=Integer.parseInt(st.nextToken());
		}catch(Exception e){}
	}
	
	public static void close()
	{
		try 
		{
			in.close();
	    }
	    catch (Exception ex) {}
		
	}
}