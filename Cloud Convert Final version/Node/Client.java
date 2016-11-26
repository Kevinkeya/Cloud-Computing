import java.rmi.registry.LocateRegistry;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;

import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Client implements IClient
{
	//int id;
	String public_url;
	String private_url;
	String public_ip;
	String private_ip;
	int RM_id;
	String RM_url;
	String RM_ip;
	String RM_backup_url = "rmi://35.156.35.216:1099/RM";
	IResourceManager rm;
	TaskSender ts;
	
	public Client(String private_url,String public_url,String private_ip,String public_ip,String RM_url,String RM_ip) throws RemoteException
	{
		//this.id = id;
		this.public_url = public_url;
		this.private_url = private_url;
		this.public_ip = public_ip;
		this.private_ip = private_ip;
		this.RM_url = RM_url;
		
		//LocateRegistry.createRegistry(15000+id);
		LocateRegistry.createRegistry(1099);
		IClient stub = (IClient) UnicastRemoteObject.exportObject(this, 0);
		
		
		 //Registry registry = LocateRegistry.getRegistry(6002);
		 try {
			 //rebind private IP
			java.rmi.Naming.rebind(private_url, this);
			rm = (IResourceManager)Naming.lookup(RM_url);
			//inform ResourceManager about the public IP
			this.RM_backup_url = rm.ClientRegisterHere(public_url);
			ts = new TaskSender(this,public_url,RM_url);
			Thread ts_thread = new Thread(ts);
			ts_thread.start();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			this.changeRM(this.RM_backup_url);
			this.RM_backup_url = rm.ClientRegisterHere(public_url);
			ts = new TaskSender(this,public_url,RM_url);
			Thread ts_thread = new Thread(ts);
			ts_thread.start();
			e.printStackTrace();
		}
		//registry.bind("rmi://localhost:6001/RM1", stub);
		//this.send_test();
		 catch (NotBoundException e) {
			// TODO Auto-generated catch block
			 this.changeRM(this.RM_backup_url);
			 this.RM_backup_url = rm.ClientRegisterHere(public_url);
				ts = new TaskSender(this,public_url,RM_url);
				Thread ts_thread = new Thread(ts);
				ts_thread.start();
			e.printStackTrace();
		}
		 
		 catch (Exception e) {
				// TODO Auto-generated catch block
				 this.changeRM(this.RM_backup_url);
				 this.RM_backup_url = rm.ClientRegisterHere(public_url);
					ts = new TaskSender(this,public_url,RM_url);
					Thread ts_thread = new Thread(ts);
					ts_thread.start();
				e.printStackTrace();
			} 
	}
	
	public void changeRM(String RM_url)
	{
		if(!this.RM_url.equals(RM_url))
		{
			this.RM_url = RM_url;
			System.out.println("now change RM to:"+RM_url);
			try {
				rm = (IResourceManager)Naming.lookup(RM_url);
				rm.ClientRegisterHere(this.public_url);
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
	
	public void receiveImageFromRM(Task task)
	{
		System.out.println("the image comes back");
		File file = new File(task.filename);
		try {
			FileOutputStream out = new FileOutputStream(task.filename);
			out.write(task.ImageData, 0, task.file_len);
			out.flush();
			out.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	public static void main(String args[])
	{
		ReadTXT.runScript();
		String publicip = ReadTXT.readPublicIP();
		String privateip = ReadTXT.readPrivateIP();
		String publicdns = ReadTXT.readPublicDNS();
		String instanceid = ReadTXT.readInstanceID();
		
		String privateurl = "rmi://"+privateip+":1099/Client";
		String publicurl = "rmi://"+publicip+":1099/Client";
		String RM_url = "rmi://35.156.3.69:1099/RM";
		String RM_backup_url = "rmi://35.156.40.67:1099/RM";
		String RM_ip = "";
		String RM_backup_ip = "";
		System.setProperty("java.rmi.server.hostname",publicdns);
		try {
			//IClient client = new Client(1,"rmi://localhost:15001/Client1","localhost",1,"rmi://localhost:16001/RM1","localhost");
			IClient client = new Client(privateurl,publicurl,privateip,publicip,RM_url,RM_ip);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
