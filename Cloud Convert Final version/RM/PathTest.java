import java.io.File;
import java.io.IOException;

public class PathTest 
{
	public static void main(String args[])
	{
		File currentDirectory = new File(new File(".").getAbsolutePath());
		try {
			System.out.println("the abosolute path is:");
			System.out.println(currentDirectory.getCanonicalPath());
			System.out.println(currentDirectory.getAbsolutePath());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		File testFile = new File("/home/ubuntu/Node/5000.jpg");
		try {
			testFile.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
