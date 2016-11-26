import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class RMChecker implements Runnable
{
	String RM_url;
	int crashCount=0;
	CrashDetecter crashdetecter;
	Node node;
	String RM_backup_url;
	
	public  RMChecker(Node node,String RM_url,String RM_backup_url)
	{
		this.node = node;
		this.RM_url = RM_url;
		this.RM_backup_url = RM_backup_url;
		crashdetecter = new CrashDetecter();
		Thread crashdetecter_thread = new Thread(crashdetecter);
		crashdetecter_thread.start();
	}
	

	@Override
	public void run() 
	{
		// TODO Auto-generated method stub
		while(true)
		{
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
			
			try {
				IResourceManager rm = (IResourceManager)Naming.lookup(RM_url);
				rm.checkAlive();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				crashdetecter.crashCount();
				if(crashdetecter.count>3)
				{
					this.changeRM(RM_url);
					this.node.changeRM(RM_url);
				}
				e.printStackTrace();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				crashdetecter.crashCount();
				if(crashdetecter.count>2)
				{
					this.changeRM(this.RM_backup_url);
					this.node.changeRM(RM_url);
				}
				e.printStackTrace();
				e.printStackTrace();
			} catch (NotBoundException e) {
				// TODO Auto-generated catch block
				crashdetecter.crashCount();
				if(crashdetecter.count>2)
				{
					this.changeRM(RM_url);
					this.node.changeRM(RM_url);
				}
				e.printStackTrace();
				e.printStackTrace();
			}
			  catch(Exception e){
				  crashdetecter.crashCount();
				  if(crashdetecter.count>2)
					{
						this.changeRM(RM_url);
						this.node.changeRM(RM_url);
					}  
			}
		}
		
	}
	
	public void changeRM(String RM_url)
	{
		this.RM_url = RM_url;
	}
	
}
