
public class ClientProfile 
{
	int RM_Master_id;
	String RM_Master_url;
	
	//int ClientID;
	String ClientURL;
	
	int hascontent = 1;
	
	public ClientProfile()
	{
		hascontent = 0;
	}
	
	public ClientProfile(String ClientURL)
	{
		//this.ClientID = ClientID;
		this.ClientURL = ClientURL;
	}

}
