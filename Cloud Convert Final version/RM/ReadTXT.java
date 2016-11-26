import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class ReadTXT 
{
	public static void runScript()
	{
		//use run.sh to get public ip, private ip public dns and instance id to txt
		String[] convert_cmd = {"sh","get.sh"};
		Runtime r = Runtime.getRuntime();
		Process p;
		try {
			p = r.exec(convert_cmd);
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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public static String readPublicIP()
	{
		File file = new File("public_ip");
		FileInputStream fis;
		String output = "";
		try {
			fis = new FileInputStream(file);
			InputStreamReader read = new InputStreamReader(fis);
			 BufferedReader bufferedReader = new BufferedReader(read);
			 String line = null;
			 System.out.println("the public ip is:");
			 while((line = bufferedReader.readLine()) != null)
			 {
				 System.out.println(line);
				 output = line;
			 }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return output;
		
	}
	
	public static String readPrivateIP()
	{
		File file = new File("private_ip");
		FileInputStream fis;
		String output = "";
		try {
			fis = new FileInputStream(file);
			InputStreamReader read = new InputStreamReader(fis);
			 BufferedReader bufferedReader = new BufferedReader(read);
			 String line = null;
			 System.out.println("the private ip is:");
			 while((line = bufferedReader.readLine()) != null)
			 {
				 System.out.println(line);
				 output = line;
			 }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return output;
	}
	
	public static String readPublicDNS()
	{
		File file = new File("public_dns");
		FileInputStream fis;
		String output = "";
		try {
			fis = new FileInputStream(file);
			InputStreamReader read = new InputStreamReader(fis);
			 BufferedReader bufferedReader = new BufferedReader(read);
			 String line = null;
			 System.out.println("the public dns is:");
			 while((line = bufferedReader.readLine()) != null)
			 {
				 System.out.println(line);
				 output = line;
			 }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return output;
	}
	
	public static String readInstanceID()
	{
		File file = new File("instance_id");
		FileInputStream fis;
		String output = "";
		try {
			fis = new FileInputStream(file);
			InputStreamReader read = new InputStreamReader(fis);
			 BufferedReader bufferedReader = new BufferedReader(read);
			 String line = null;
			 System.out.println("the instance id is:");
			 while((line = bufferedReader.readLine()) != null)
			 {
				 System.out.println(line);
				 output = line;
			 }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return output;
	}
	
	
	public static void main(String args[])
	{
		readPublicIP();
		readPrivateIP();
		readPublicDNS();
		readInstanceID();
	}
}
