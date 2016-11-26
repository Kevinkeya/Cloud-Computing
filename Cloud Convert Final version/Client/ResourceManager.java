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

public class ResourceManager implements IResourceManager
{
	//int id = 1;
	String private_url;
	String public_url;
	String private_ip;
	String public_ip;
	String RM_backup_url = "rmi://35.156.35.216:1099/RM";
	String url = "rmi://localhost:16001/RM1";
	NodeProfileGroup nodeprofilegroup = new NodeProfileGroup();
	ClientProfileGroup clientprofilegroup = new ClientProfileGroup();
	Queue queue_buffer = new Queue();
	public void IRMtest() throws RemoteException
	{
		System.out.println("here is a ResourceManager");
		//this.send_test();
	}
	
	//used to test image sending
	public void send_test()
	{
		String filename_server_nameonly = "1.jpg";
		String filename_server = "1.jpg";
		File file_server = new File(filename_server);
		byte[] file_byte = null;
		int file_len;
		try {
			FileInputStream in = new FileInputStream(file_server);
			file_byte = new byte[(int)file_server.length()];
			file_len = in.read(file_byte);
			System.out.println("start to lookup");
			INode TestNode = (INode) Naming.lookup("rmi://35.156.16.150:7001/Node1");
			System.out.println("start to send image");
			TestNode.receiveImage(filename_server_nameonly, file_byte, file_len);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public String NodeRegisterHere(String node_url,boolean awake) throws RemoteException
	{
		NodeProfile nodeprofile = new NodeProfile(this,node_url,public_url);
		if(awake==false)
		{
			nodeprofile.BeDisabled();
		}
		for(int i=0;i<this.queue_buffer.getSize();)
		{
			System.out.println("the buffer size is now: "+ queue_buffer.getSize());
			nodeprofile.addTaskToQueue(this.queue_buffer.pop());
		}
		
		if(nodeprofilegroup.getNodeProfileByURL(node_url).hascontent==0)
		{
			//if the node is not in the NodeProfileGroup
			//add the node to it
			nodeprofilegroup.addNodeProfile(nodeprofile);
		}
		
		if(nodeprofilegroup.getNodeProfileByURL(node_url).hascontent==1)
		{
			//if the node is already in NodeprofileGroup
			//merge the two queue
			//nodeprofilegroup.getNodeProfileByURL(node_url).addQueue(nodeprofile.queue);
		}
		
		System.out.println("Node"+node_url+" registers here");
		
		
		return this.RM_backup_url;
		
		
	}
	
	public String ClientRegisterHere(String ClientURL) throws RemoteException
	{
		ClientProfile clientprofile = new ClientProfile(ClientURL);
		if(clientprofilegroup.getNodeProfileByURL(ClientURL).hascontent==0)
		{
			//if this client in not yet in the client profile group
			//add it into
			//ignore it otherwise
			clientprofilegroup.addNodeProfile(clientprofile);
		}
		
		System.out.println("Client"+ClientURL+" register here");
		return this.RM_backup_url;
	}
	
	
	public void assignTask(Task task)
	{
		if(nodeprofilegroup.getIdlestNodeProfile().hascontent!=0)
		{
			nodeprofilegroup.getIdlestNodeProfile().addTaskToQueue(task);
		}
		
		else
		{
			this.queue_buffer.add(task);
		}
		
	}
	
	public ResourceManager(String privateurl,String publicurl,String privateip,String publicip,String RM_backup_url) throws RemoteException
	{
		
		//LocateRegistry.createRegistry(16001);
		LocateRegistry.createRegistry(1099);
		IResourceManager stub = (IResourceManager) UnicastRemoteObject.exportObject(this, 0);
		
		
		 //Registry registry = LocateRegistry.getRegistry(6002);
		 try {
			 //Here put you private IP !!!!!!!!!!!!!!!!!
			java.rmi.Naming.rebind(privateurl, this);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//registry.bind("rmi://localhost:6001/RM1", stub);
		//this.send_test();
	}
	
	public void receiveImage(String filename,byte[] file_byte,int file_len) throws RemoteException
	{
		File file = new File(filename);
		try {
			FileOutputStream out = new FileOutputStream(file);
			out.write(file_byte, 0, file_len);
			out.flush();
			out.close();
			INode node = (INode)Naming.lookup("rmi://localhost:17001/Node1");
			node.receiveImage(filename, file_byte, file_len);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sendImageToClient(Task task)
	{
		try {
			IClient client = (IClient)Naming.lookup(task.ClientURL);
			client.receiveImageFromRM(task);
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
	
	
	public void receiveImageFromClient(Task task)
	{
		this.assignTask(task);
			
	}
	
	public void receiveImageFromNode(Task task)
	{
		this.sendImageToClient(task);
	}
	
	public void addTaskToQueue(Task task)
	{
		
	}
	
	public boolean checkAlive() throws RemoteException
	{
		return true;
	}
	
	
	
	public static void main(String args[])
	{
		ReadTXT.runScript();
		String publicip = ReadTXT.readPublicIP();
		String privateip = ReadTXT.readPrivateIP();
		String publicdns = ReadTXT.readPublicDNS();
		String instanceid = ReadTXT.readInstanceID();
		
		String privateurl = "rmi://"+privateip+":1099/RM";
		String publicurl = "rmi://"+publicip+":1099/RM";
		String RM_url = "rmi://35.156.3.69:1099/RM";
		String RM_backup_url = "rmi://35.156.40.67:1099/RM";
		String RM_ip = "";
		String RM_backup_ip = "";
		System.setProperty("java.rmi.server.hostname",publicdns);
		
		try {
			IResourceManager TestRM = new ResourceManager(privateurl,publicurl,privateip,publicip,RM_backup_url);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//here put your public IP !!!!!!!!!!!!!
		//System.setProperty("java.rmi.server.hostname","ec2-35-156-3-69-eu-central-1.compute.amazonaws.com");
		//35.156.3.69
		//ec2-35-156-3-69-eu-central-1.compute.amazonaws.com
		System.out.println("resource manager installed");
		
		
//		try {
//			Thread.sleep(15000);
//			System.out.println("start testing node");
//			INode TestNode = (INode) Naming.lookup//("rmi://35.156.16.150:7001/Node1");
//			TestNode.INodeTest();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (RemoteException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (NotBoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	
}
