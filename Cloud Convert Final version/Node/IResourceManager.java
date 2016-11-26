
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;


public interface IResourceManager extends Remote
{
    public void IRMtest() throws RemoteException;
    
    public void receiveImage(String filename,byte[] file_byte,int file_len) throws RemoteException;
    
    public String NodeRegisterHere(String node_url,boolean awake) throws RemoteException;
    
    public void receiveImageFromClient(Task task) throws RemoteException;
    
    public void receiveImageFromNode(Task task) throws RemoteException;
    
    public String ClientRegisterHere(String ClientURL) throws RemoteException;
    
    public boolean checkAlive() throws RemoteException;
    
    
}
