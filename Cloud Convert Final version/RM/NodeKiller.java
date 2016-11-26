import java.rmi.RemoteException;

public class NodeKiller implements Runnable
{
	int count =1;
	int previous_count =0;
	Node node;

	public NodeKiller(Node node)
	{
		this.node = node;
	}
	
	
	
	@Override
	public void run() 
	{
		// TODO Auto-generated method stub
		while(true)
		{
			try {
				Thread.sleep(20000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(count==previous_count)
			{
				System.out.println("time to kill node");
				node.killNode();
			}
		}
		
	}
	
	public void countTask()
	{
		this.count++;
	}

}
