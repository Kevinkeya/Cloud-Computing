import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class WorkLoadMonitor implements Runnable
{
	public boolean running = true;

	NodeProfileGroup nodeprofilegroup;
	int low_workload_count;
	public WorkLoadMonitor(NodeProfileGroup npg)
	{
		this.nodeprofilegroup = npg;
	}
	@Override
	public void run() 
	{
		System.out.println("starting WorkLoadMonitor");
		// TODO Auto-generated method stub
		while(true)
		{
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//this.writeCSV();
			
			if(nodeprofilegroup.getGroupSize()*30<nodeprofilegroup.getTotalWorkLoad())
			{
				System.out.println("The total workload is: "+nodeprofilegroup.getTotalWorkLoad());
				System.out.println("workload exceed threashold");
				nodeprofilegroup.installNewVM();
				try {
					Thread.sleep(120000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if(nodeprofilegroup.getGroupSize()*5>nodeprofilegroup.getTotalWorkLoad())
			{
				System.out.println("very low workload");
				this.low_workload_count++;
				if(low_workload_count>20)
				{
					nodeprofilegroup.removeIdlestNodeProfile();
				}
			}
			
			low_workload_count =0;
			
			
		}
		
	}
	
	public void writeRecord(String line)
	{
		File file_record = new File("/home/ubuntu/RM/WorkLoadRecord.txt");
		FileWriter fw;
		try {
			fw = new FileWriter(file_record);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(line+"\n");
			bw.flush();
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void writeWorkLoadToFile()
	{
		String line ="";
		for(int i=0;i<this.nodeprofilegroup.getGroupSize();i++)
		{
			line = line+"," + this.nodeprofilegroup.getWorkLoadByID(i);
		}
		line = line +"\n";
		this.writeRecord(line);
	}
	
	public void writeOneLineToCSV(String line)
	{
		File file_csv = new File("/home/ubuntu/RM/WorkLoad.csv");
		try {
			FileWriter fw = new FileWriter(file_csv, true);
			fw.append(line);
			fw.append("\n");
			fw.flush();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void writeCSV()
	{
		String line = "";
		for(int i=0;i<this.nodeprofilegroup.getGroupSize();i++)
		{
			line = line+"," + this.nodeprofilegroup.getWorkLoadByID(i);
		}
		line = line;
		this.writeOneLineToCSV(line);
	}

}
