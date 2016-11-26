import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class NodeProfileGroup 
{
	ArrayList<NodeProfile> group = new ArrayList<NodeProfile>(); 
	WorkLoadMonitor workloadmonitor;
	WorkLoadRecorder workloadrecorder;
	public NodeProfileGroup()
	{
		workloadmonitor = new WorkLoadMonitor(this);
		Thread thread_wlm = new Thread(workloadmonitor);
		thread_wlm.start();
		
		workloadrecorder = new WorkLoadRecorder(this);
		Thread thread_wlr = new Thread(workloadrecorder);
		thread_wlr.start();
		
	}
	
	public NodeProfile getNodeProfileByID(int index)
	{
		return group.get(index);
	}
	
	public int getWorkLoadByID(int index)
	{
		return group.get(index).getQueueSize();
	}
	
	
	
	public void addNodeProfile(NodeProfile nodeprofile)
	{
		group.add(nodeprofile);
	}
	
	public int getGroupSize()
	{
		return group.size();
	}
	
	public NodeProfile getIdlestNodeProfile()
	{
		int idlest = 999;
		int index = -1;
		for(int i=0;i<group.size();i++)
		{
			if(group.get(i).getQueueSize()<idlest&&group.get(i).awake==true)
			{
				idlest = group.get(i).getQueueSize();
				index = i;
			}
		}
		if(index!=-1)
		{
			return group.get(index);
		}
		
		return new NodeProfile();
	}
	
	public void removeIdlestNodeProfile()
	{
		int idlest = 999;
		int index = -1;
		for(int i=0;i<group.size();i++)
		{
			if(group.get(i).getQueueSize()<idlest&&group.get(i).awake==true)
			{
				idlest = group.get(i).getQueueSize();
				index = i;
			}
		}
		//if(index!=-1&&group.size()>1)
		if(index!=-1)
		{
			
			System.out.println("Now remove the node "+group.get(index).Node_url);
			group.remove(index);
			
		}
		
		
	}
	
	
	public int getTotalWorkLoad()
	{
		int sum = 0;
		for(int i=0;i<group.size();i++)
		{
			sum += group.get(i).getQueueSize();
		}
		return sum;
	}
	
	public NodeProfile getNodeProfileByURL(String url)
	{
		for(int i=0;i<group.size();i++)
		{
			if(group.get(i).Node_url.equals(url))
			{
				return group.get(i);
			}
		}
		
		return new NodeProfile();
	}
	
	public void installNewVM()
	{
		System.out.println("starting new VM");
		//String[] create_cmd = {"java","-cp","/home/ubuntu/RM","checkME.jar","AddSure"};
		//String create_cmd = "java -jar /home/ubuntu/RM/checkME.jar AddSure";
		String create_cmd = "java -jar /home/ubuntu/RM/checkME.jar AddSure ami-99d511f6";
		
		
		try {
			Runtime r = Runtime.getRuntime();
			Process p = r.exec(create_cmd);
			InputStream fis = p.getInputStream();
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			System.out.println("outputs for creating new VM are:");
			while((line=br.readLine())!=null)
			{
				System.out.println("here is a output:");
				System.out.println(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
