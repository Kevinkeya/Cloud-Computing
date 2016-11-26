import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TestFaceDetection 
{
	public static void main(String args[])
	{
		String[] convert_cmd = {"python","/home/ubuntu/Node/face_detect.py","/home/ubuntu/Node/1.jpg","/home/ubuntu/Node/1.png"};
		
		Runtime r = Runtime.getRuntime();
		;
		try {
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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
