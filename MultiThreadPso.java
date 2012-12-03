package PSO;

import java.io.*;
import java.util.*;

public class MultiThreadPso extends Pso implements Runnable
{
	Thread t;
	MultiThreadPso()
	{
		bInit();
		t=new Thread(this);
		init();
		t.start();
	}
	public double update(double w)
	{
		return ((.9-.4)*cur_iter_no)/max_iterate;
	}
	public void run()
	{
	
		generate();
		iterate();
	}
}