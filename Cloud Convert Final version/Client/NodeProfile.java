import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class NodeProfile 
{
	final static int BUSY = 0;
	final static int IDLE = 1;
	
	final static int ENABLE = 1;
	final static int DISABLE = 0;
	
	
	ResourceManager rm;
	int RM_Master_id;
	String RM_Master_url;
	//the id to of the node
	//int Node_id;
	
	//the url to which the Node binds to
	String Node_url;
	
	//if hascontent = 0, this profile will be ignored
	int hascontent = 1;
	
	//task queue of the node
	public Queue queue;
	
	//work status of the node
	int work_status = this.IDLE;
	
	//awake or sleep?
	boolean awake = true;
	
	QueuePoper queuepoper;
	Thread queuepoper_thread;
	
	public NodeProfile()
	{
		hascontent = 0;
	}
	
	public NodeProfile(ResourceManager rm,String node_url,String RM_Master_url)
	{
		this.rm = rm;
		//this.Node_id = node_id;
		this.Node_url = node_url;
		this.RM_Master_id = RM_Master_id;
		this.RM_Master_url = RM_Master_url;
		queue = new Queue();
		queuepoper = new QueuePoper(this,node_url,RM_Master_id,RM_Master_url);
		queuepoper_thread = new Thread(queuepoper);
		queuepoper_thread.start();
	}
	
	public void changeMasterID(int RM_Master_id)
	{
		this.RM_Master_id = RM_Master_id;
	}
	
	public void changeMasterURL(String RM_Master_url)
	{
		this.RM_Master_url = RM_Master_url;
	}
	
	public void BeBusy()
	{
		this.work_status = this.BUSY;
	}
	
	public void BeIdle()
	{
		this.work_status = this.IDLE;
	}
	
	public void BeEnabled()
	{
		this.awake = true;
	}
	
	public void BeDisabled()
	{
		this.awake = false;
	}
	
	public int getQueueSize()
	{
		return queue.list.size();
	}
	
	public void addTaskToQueue(Task task)
	{
		queue.add(task);
		System.out.println("Task added to queue");
		System.out.println("the current queue is");
		System.out.println(queue);
	}
	
	public void startQueuePoper()
	{
		//queuepoper = new QueuePoper(this,Node_id,Node_url,RM_Master_id,RM_Master_url);
		//queuepoper_thread = new Thread(queuepoper);
		//queuepoper_thread.start();
		
		queuepoper.startQueuePoper();
		queuepoper_thread.start();
	}
	
	public void stopQueuePoper()
	{
		queuepoper.stopQueuePoper();
	}
	
	public void sendTaskToNode()
	{
		System.out.println("try to pop task");
		Task task = queue.pop();
		if(task.hascontent==1)
		{
			try {
				INode node = (INode)Naming.lookup(Node_url);
				node.receiveTask(task);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NotBoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(task.hascontent==0)
		{
			System.out.println("no task in queue");
		}
		
	}
	
	public void addQueue(Queue queue_new)
	{
		for(int i=0;i<queue_new.getSize();i++)
		{
			this.addTaskToQueue(queue_new.getByID(i));
		}
		
	}
	
	
	
	
	
}
