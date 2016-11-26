import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

//this file contains the constructor of Node class, as well as a main function
//used to set up a Node instance
public class Node implements INode 
{
	//int id;
	String public_url;
	String private_url;
	String public_ip;
	String private_ip;
	//int RM_id;
	String RM_url;
	String RM_backup_url;
	String RM_ip;
	IResourceManager rm;
	boolean awake = true;
	boolean idle = true;
	CrashDetecter crashdetecter;
	RMChecker rmchecker;
	NodeKiller nodekiller;
	public Node(String privateurl,String publicurl,String privateip,String publicip,String RM_url,String RM_backup_url) throws RemoteException
	{
		
		//this.id = id;
		rmchecker = new RMChecker(this,RM_url,RM_backup_url);
		Thread rmchecker_thread = new Thread(rmchecker);
		rmchecker_thread.start();
		crashdetecter = new CrashDetecter();
		Thread crashdetecter_thread = new Thread(crashdetecter);
		crashdetecter_thread.start();
		
		nodekiller = new NodeKiller(this);
		Thread thread_nodekiller = new Thread(nodekiller);
		thread_nodekiller.start();
		this.private_url = privateurl;
		this.public_url = publicurl;
		this.private_ip = privateip;
		this.public_ip = publicip;
		this.RM_url = RM_url;
		this.RM_backup_url = RM_backup_url;
		
		//this.RM_id=RM_id;
		this.RM_url = RM_url;
		LocateRegistry.createRegistry(1099);
		INode stub = (INode) UnicastRemoteObject.exportObject(this, 0);
		
		//Registry registry = LocateRegistry.getRegistry(7001);
		 try {
			 //Here put your private IP, if the Node is run in cloud too.
			java.rmi.Naming.rebind(private_url, this);
			//registry.bind("rmi://localhost:7001/Node1", stub);
			rm = (IResourceManager) Naming.lookup(RM_url);
			rm.IRMtest();
			this.RM_backup_url = rm.NodeRegisterHere(public_url,awake);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			{
				this.changeRM(this.RM_backup_url);
			}
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			this.changeRM(RM_backup_url);
			e.printStackTrace();
		}
	}
	
	public void INodeTest() throws RemoteException
	{
		System.out.println("Here is a node");
	}
	
	public void changeRM(String RM_url)
	{
		this.RM_url = RM_url;
		System.out.println("now change RM to:"+RM_url);
		try {
			rm = (IResourceManager)Naming.lookup(RM_url);
			rm.NodeRegisterHere(this.public_url,awake);
			rmchecker.changeRM(RM_url);
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
	
	public void sendTaskToRM(Task task)
	{
		IResourceManager rm;
		try {
			rm = (IResourceManager)Naming.lookup(RM_url);
			rm.receiveImageFromNode(task);
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
	
	public void killNode()
	{
		try {
			Naming.unbind(private_url);
			String convert_cmd = "shutdown";
			
			Runtime r = Runtime.getRuntime();
			Process p = r.exec(convert_cmd);
			InputStream fis = p.getInputStream();
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			System.out.println("outputs are:");
			while((line=br.readLine())!=null)
			{
				System.out.println("here is a output:");
				System.out.println(line);
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void receiveTask(Task task) throws RemoteException
	{
		nodekiller.countTask();
		this.idle = false;
		Task task_received = new Task(task.filename,task.ImageData,task.file_len,task.ClientURL);
		File currentDirectory = new File(new File(".").getAbsolutePath());
		try {
			System.out.println("the abosolute path is:");
			System.out.println(currentDirectory.getCanonicalPath());
			System.out.println(currentDirectory.getAbsolutePath());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//URL location = Node.class.getProtectionDomain().getCodeSource().getLocation();
		System.out.println("receiving task of Client"+task.ClientID);
		String filename = task.filename;
		String filename_nopostfix = filename.split("\\.")[0];
		File file_received = new File("/home/ubuntu/Node/"+filename);
		String filename_converted = filename_nopostfix+".png";
		
		try {
			FileOutputStream out = new FileOutputStream(file_received);
			System.out.println("start writing in disk");
			out.write(task.ImageData, 0, task.file_len);
			out.flush();
			out.close();
			System.out.println("writing completed");
			
			String[] convert_cmd = {"convert","/home/ubuntu/Node/"+filename,"/home/ubuntu/Node/"+filename_nopostfix+".png"};
			
			Runtime r = Runtime.getRuntime();
			Process p = r.exec(convert_cmd);
			InputStream fis = p.getInputStream();
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			System.out.println("outputs are:");
			while((line=br.readLine())!=null)
			{
				System.out.println("here is a output:");
				System.out.println(line);
			}
			
			System.out.println("time to send image back");
			File file_converted = new File("/home/ubuntu/Node/"+filename_converted);
			FileInputStream in = new FileInputStream(file_converted);
			byte[] file_byte_converted = new byte[(int)file_converted.length()];
			int file_len_converted = in.read(file_byte_converted);
			task.setImageData(file_byte_converted);
			task.setFileName(filename_converted);
			task.setFileLength(file_len_converted);
			rm = (IResourceManager)Naming.lookup(RM_url);
			rm.receiveImageFromNode(task);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			if(crashdetecter.crashCount()==true)
			{
				this.changeRM(this.RM_backup_url);
				this.sendTaskToRM(task);
			}
			this.receiveTask(task_received);
			e.printStackTrace();
		}
		  catch(Exception e){
			  if(crashdetecter.crashCount()==true)
				{
					this.changeRM(this.RM_backup_url);
					this.sendTaskToRM(task);
				}
			  this.receiveTask(task_received);
			  
			  e.printStackTrace();
		  }
		
		this.idle = true;
	}
	
	public void wakeUp() throws RemoteException
	{
		this.awake = true;
	}
	
	public void fallAsleep() throws RemoteException
	{
		this.awake = false;
	}
	
	public void receiveImage(String filename_server,byte[]file_byte,int file_len) throws RemoteException
	{
		System.out.println("start to receive image");
		String filename_client = filename_server;
		File file_client = new File(filename_client);
		try {
			System.out.println("start to establish output stream");
			FileOutputStream out = new FileOutputStream(file_client);
			System.out.println("start to write");
			out.write(file_byte, 0, file_len);
			out.flush();
			out.close();
			System.out.println("writing complete");
			System.out.println("start converting");
			Runtime r = Runtime.getRuntime();
			String[] convert_cmd = {"F:\\tools\\ImageMagick\\convert.exe","H:\\test\\ClientNodeRM\\Node\\1.jpg","H:\\test\\ClientNodeRM\\Node\\7.png"};
			//Process p = r.exec("/usr/bin/convert -debug all ~/client/1.jpg ~/client/5.png");
			//Process p = r.exec("sudo sh client/doconvert.sh");
			Process p = r.exec(convert_cmd);
			InputStream fis = p.getInputStream();
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			System.out.println("outputs are:");
			while((line=br.readLine())!=null)
			{System.out.println("here is a output:");
			System.out.println(line);}
			
			//send image back
			File file_png = new File("7.png");
			FileInputStream in = new FileInputStream(file_png);
			byte[] file_byte_png = new byte[(int)file_png.length()];
			int file_len_png = in.read(file_byte_png);
			IResourceManager rm = (IResourceManager)Naming.lookup("rmi://localhost:16001/RM1");
			rm.receiveImage("7.png", file_byte_png, file_len_png);
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
	
	
	public static void main(String args[])
	{
		ReadTXT.runScript();
		String publicip = ReadTXT.readPublicIP();
		String privateip = ReadTXT.readPrivateIP();
		String publicdns = ReadTXT.readPublicDNS();
		String instanceid = ReadTXT.readInstanceID();
		
		String privateurl = "rmi://"+privateip+":1099/Node";
		String publicurl = "rmi://"+publicip+":1099/Node";
		String RM_url = "rmi://35.156.3.69:1099/RM";
		String RM_backup_url = "rmi://35.156.35.216:1099/RM";
		String RM_ip = "";
		String RM_backup_ip = "";
		System.setProperty("java.rmi.server.hostname",publicdns);
		try {
			INode TestNode = new Node(privateurl,publicurl,privateip,publicip,RM_url,RM_backup_url);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Here put your public IP, if the Node is run in Cloud too, if not, delete this line.
		//System.setProperty("java.rmi.server.hostname","ec2-35-156-16-150.eu-central-1.compute.amazonaws.com");
		//35.156.16.150
		//ec2-35-156-16-150.eu-central-1.compute.amazonaws.com
		System.out.println("node installed");
		
		
	}
	
}
