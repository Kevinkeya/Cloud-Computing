import java.util.ArrayList;

public class Queue 
{
	ArrayList<Task>list = new ArrayList<Task>();
	
	public Queue()
	{
		
	}
	
	
	public Task pop()
	{
		if(list.size()!=0)
		{
			Task task = list.remove(0);
			return task;
		}
		else
		{
			//return a empty task;
			Task task = new Task();
			return task;
		}
		
	}
	
	public Task getByID(int i)
	{
		return list.get(i);
	}
	
	public void add(Task task)
	{
		list.add(task);
	}
	
	public String toString()
	{
		String toPrint="";
		for(int i=0;i<list.size();i++)
		{
			toPrint+=list.get(i).ClientID;
		}
		return toPrint;
	}
	
	public int getSize()
	{
		return list.size();
	}
}
