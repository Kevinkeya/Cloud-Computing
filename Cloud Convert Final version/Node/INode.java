import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface INode extends Remote
{
	public void INodeTest() throws RemoteException;
	
	public void receiveImage(String filename_server,byte[] file_byte,int file_len) throws RemoteException;
	
	public void receiveTask(Task task) throws RemoteException;
	
	public void wakeUp() throws RemoteException;
	
	public void fallAsleep() throws RemoteException;
	
	//public void killNode() throws RemoteException;

}
