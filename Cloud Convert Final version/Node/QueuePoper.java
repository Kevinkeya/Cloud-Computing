import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class QueuePoper implements Runnable
{
	//this class keep poping out one task from the queue.
	boolean running  = true;
	//int node_id;
	String node_url;
	String node_ip;
	int rm_master_id;
	String rm_master_url;
	int rm_master_ip;
	NodeProfile nodeprofile;
	
	
	public QueuePoper(NodeProfile nodeprofile,String node_url,int rm_master_id,String rm_master_url)
	{
		//this.node_id = node_id;
		this.node_url = node_url;
		this.rm_master_id = rm_master_id;
		this.rm_master_url = rm_master_url;
		this.nodeprofile = nodeprofile;
	}
	

	@Override
	public void run() 
	{	
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		while(true)
		{
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(running==true)
			{
				nodeprofile.sendTaskToNode();
			}
		}
		
	}
	
	public void stopQueuePoper()
	{
		running = false;
	}

	public void startQueuePoper()
	{
		running = true;
	}
}
