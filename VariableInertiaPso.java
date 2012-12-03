package PSO;
import java.io.*;
import java.util.*;
class VariableInertiaPso extends Pso
{
	public double update(double w)
	{
		return ((.9-.4)*cur_iter_no)/max_iterate;
	}
}