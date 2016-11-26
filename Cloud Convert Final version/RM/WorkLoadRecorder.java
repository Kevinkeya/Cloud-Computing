import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class WorkLoadRecorder implements Runnable
{
	
	public boolean running = true;

	NodeProfileGroup nodeprofilegroup;
	
	public WorkLoadRecorder(NodeProfileGroup npg)
	{
		this.nodeprofilegroup = npg;
	}

	@Override
	public void run() 
	{
		// TODO Auto-generated method stub
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while(true)
		{
			try {
			Thread.sleep(1000);
			} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
			this.writeCSV();
		}
		
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
