import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class TaskSender implements Runnable
{
	//int ClientID;
	String ClientURL;
	int ResourceManagerID;
	String ResourceManagerURL;
	String ResourceManager_backup_url = "rmi://35.156.35.216:1099/RM";
	//IClient client;
	IResourceManager rm;
	Client client;
	CrashDetecter crashdetecter;

	
	public TaskSender(Client client,String ClientURL,String ResourceManagerURL)
	{
		//this.ClientID = ClientID;
		crashdetecter = new CrashDetecter();
		Thread crashdetecter_thread = new Thread(crashdetecter);
		crashdetecter_thread.start();
		this.client = client;
		this.ClientURL = ClientURL;
		this.ResourceManagerID = ResourceManagerID;
		this.ResourceManagerURL = ResourceManagerURL;
		
		try {
			//client = (IClient)Naming.lookup(ClientURL);
			rm = (IResourceManager)Naming.lookup(ResourceManagerURL);
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
	public void setRMurl(String RM_url)
	{
		this.ResourceManagerURL = RM_url;
	}
	
	public void setRMbackupURL(String RM_backup_url)
	{
		this.ResourceManager_backup_url = RM_backup_url;
	}
	
	public void changeRM(String RM_url)
	{
		if(this.ResourceManagerURL.equals(RM_url))
		{
			try {
				rm = (IResourceManager)Naming.lookup(RM_url);
				client.changeRM(RM_url);
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
		for(int i=1;i<=2000;i++)
		{
			try {
				Thread.sleep(400);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			String filename = (i%21)+".jpg";
			File file = new File(filename);
			
		
				try {
					System.out.println("convert image"+i+ "to byte[]");
					FileInputStream in = new FileInputStream(file);
					byte[] file_byte = new byte[(int)file.length()];
					int file_len = in.read(file_byte);
					Task task = new Task(filename,file_byte,file_len,ClientURL);
					rm.receiveImageFromClient(task);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					crashdetecter.crashCount();
					if(crashdetecter.count>2)
					{
						this.changeRM(this.ResourceManager_backup_url);
					}
					i=i-1;
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					crashdetecter.crashCount();
					if(crashdetecter.count>2)
					{
						this.changeRM(this.ResourceManager_backup_url);
					}
					i=i-1;
					e.printStackTrace();
				}
			 
			
		}
		
		
		
	}

}
