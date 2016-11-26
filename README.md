# CloudComputing
## Cloud Computing Lab

This the final code of our lab.

- Cloud Convert Final Version

	This is the codes of clients, nodes and resource managers.
	
- Create

	Codes using AWS JDK to create or terminate instance.
	
- python draw

	Codes used to draw experiment figures.	






-------


## Method of Calling checkME.jar

- The checkME.jar file is delete to prevent others access to the instance.
- JDK 1.8 required.

-------
 - List all running instances and their information, write it in the cvs file (no input)
 
 ***java -jar checkME.jar***
 
	- This will list information of all running instances. And put al information in a cvs file named `runningInstance.csv`, which is under the **same path** of the jar file.
	
	- The cvs file will be update every time.
	
	- Attention: as shown in **runningInstance.csv**, the order of the attribute is `publicDNS`, `publicIP`, `privateIP` and `instanceId`.

Output:

\<CurrentPath>/runningInstance.csv

ec2-35-156-45-8.eu-central-1.compute.amazonaws.com     	35.156.45.8    	172.31.16.73   	i-07567ea986e7cfd23    	[{Key: Name,Value: New3}]

ec2-35-156-40-67.eu-central-1.compute.amazonaws.com    	35.156.40.67   	172.31.27.153  	i-01c89a4ac6d06997b    	[{Key: Name,Value: Linux2}]

ec2-35-156-73-82.eu-central-1.compute.amazonaws.com    	35.156.73.82   	172.31.28.252  	i-0a7a2e8cd2903c222    	[{Key: Name,Value: New2}]

ec2-35-156-77-133.eu-central-1.compute.amazonaws.com   	35.156.77.133  	172.31.25.81   	i-0f73e92cb135f557b    	[{Key: Name,Value: New4}]

ec2-35-156-3-69.eu-central-1.compute.amazonaws.com     	35.156.3.69    	172.31.28.83   	i-0bb4ed78efed00dbc    	[{Key: Name,Value: Linux1}]

ec2-35-156-78-126.eu-central-1.compute.amazonaws.com   	35.156.78.126  	172.31.22.16   	i-0d2b26e524a232f0c    	[{Key: Name,Value: New5}]


-------


- List all instances and their information (two inputs)
	
	***java -jar checkME.jar  list all***	
	
	This is useless, just checking the instance, including stopped, running, terminated instances that you can see in the dashboard. It will printout every instance with some information.

-------	
- Start a new instance with default AMI(currently ami-5579bc3a) (**One** input)

	***java -jar checkME.jar AddSure***
	
	- This will create an instance based on one AMI. And put it's information in a cvs file named 	`createInstance.csv`, which is under the **same path** of the jar file.
	
	- If cvs does not exist, it will create the cvs file and put it in the first line.
	
	- If the cvs file does exist, it will add the information in the last line.
	
	- How to read the IP and other information from cvs file is already implemented int the code, please refer to `readCSV()` in `EC2_Wrapper.java` under **Create/src** directory.
		
	- `Attention`: as shown in **createInstance.csv**, the order of the attribute is `publicDNS`, `publicIP`, `privateIP` and `instanceId`.
	
	After `30` seconds,
	
	**output**:
	
	


	Creating instance...
	
	this.instanceId = i-094cb3a22d2c13803

	this.publicDNS = ec2-35-156-16-234.eu-central-1.compute.amazonaws.com

	this.privateIP = 172.31.20.218

	this.pubpublicIP = 35.156.16.234

	\<CurrentPath>/createInstance.csv

	You have 11 Amazon EC2 instance(s). 
	
- Start a new instance with user defined AMI (**Two** input)

	***java -jar checkME.jar AddSure \<amiID\>**
	
	ie: *java -jar checkME.jar AddSure ami-da7cb9b5*
	
	Output: Creating one instance with AMI= ami-da7cb9b5...
	
	Other behaviour is the same as the default *AddSure*
	
-------
	
- Check the status of one instance (**Two** inputs) 

	***java -jar checkME.jar list \<ID\>***
	
	ie: java -jar checkME.jar list *i-0f73e92cb135f557b*

	- If the instance is running,
	 
		**output**: `running` 
	
	- If the instance is **terminated or stop**,  after `5` seconds,
	
		**output**: `Error: instance ID may be wrong or instance is not running...`
	
-------
	
- Terminate the instance (**Two** inputs) 
	
	***java -jar checkME.jar delete \<ID>***
	
	ie: java -jar checkME.jar delete 	*i-0130f2bd929431aee*
	
	If terminate successfully
	
	**output**: `Terminate i-0130f2bd929431aee successfully.`
	
	



	
	
