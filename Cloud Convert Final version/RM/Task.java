
//task containing image byte[] and description data
public class Task implements java.io.Serializable 
{
	byte[] ImageData;
	String filename;
	int file_len;
	int ClientID;
	String ClientURL;
	String ClientIP;
	
	//indicates that the task is not empty
	int hascontent = 1;
	
	public Task(String filename,byte[] ImageData,int file_len,String ClientURL)
	{
		this.filename = filename;
		this.file_len = file_len;
		this.ImageData = ImageData;
		this.ClientID = ClientID;
		this.ClientURL = ClientURL;
		//this.ClientIP = ClientIP;
	}
	
	public Task()
	{
		hascontent = 0;
	}
	
	public void setEmpty()
	{
		hascontent = 0;
	}
	
	public void setImageData(byte[] ImageData)
	{
		this.ImageData = ImageData;
	}
	
	public void setFileName(String filename)
	{
		this.filename = filename;
	}
	
	public void setFileLength(int file_len)
	{
		this.file_len = file_len;
	}
	
}
