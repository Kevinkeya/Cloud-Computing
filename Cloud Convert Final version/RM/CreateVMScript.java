import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CreateVMScript 
{
	public static void main(String args[])
	{
		System.out.println("starting new VM");
		//String[] create_cmd = {"java","-jar","/home/ubuntu/RM","checkME.jar","AddSure"};
		String create_cmd = "java -jar /home/ubuntu/RM/checkME.jar AddSure";
		
		
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
