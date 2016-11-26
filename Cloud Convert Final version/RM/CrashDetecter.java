
public class CrashDetecter implements Runnable
{
	public int count = 0;

	
	@Override
	public void run() 
	{
		// TODO Auto-generated method stub
		System.out.println("crash detecter starts...");
		while(true)
		{
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			count = 0;
		}
		
	}
	
	public boolean crashCount()
	{
		//if fail to connect to ResourceManager over 3 times within 10 seconds
		//this function returns true;
		count++;
		if(count>3)
		{
			return true;
		}
		
		return false;
	}

}
