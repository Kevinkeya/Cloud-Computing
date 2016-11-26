import java.util.ArrayList;

public class ClientProfileGroup 
{
	ArrayList<ClientProfile> list = new ArrayList<ClientProfile>();
	
	public ClientProfileGroup()
	{
		
	}
	
	public void addNodeProfile(ClientProfile clientprofile)
	{
		list.add(clientprofile);
	}
	
	public ClientProfile getNodeProfileByURL(String url)
	{
		for(int i=0;i<list.size();i++)
		{
			if(list.get(i).ClientURL.equals(url))
			{
				return list.get(i);
			}
		}
		
		return new ClientProfile();
	}

}
