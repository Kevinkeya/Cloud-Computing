import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IClient extends Remote
{
	public void receiveImageFromRM(Task task) throws RemoteException;
}
