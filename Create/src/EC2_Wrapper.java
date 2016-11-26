import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
//import com.amazonaws.internal.StaticCredentialsProvider;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.DescribeInstanceStatusRequest;
import com.amazonaws.services.ec2.model.DescribeInstanceStatusResult;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
//import com.amazonaws.services.ec2.model.CreateKeyPairRequest;
//import com.amazonaws.services.ec2.model.CreateKeyPairResult;
//import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.InstanceStatus;
import com.amazonaws.services.ec2.model.KeyPair;
import com.amazonaws.services.ec2.model.KeyPairInfo;
//import com.amazonaws.services.ec2.model.Placement;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;
import com.amazonaws.services.ec2.model.TerminateInstancesRequest;
import com.amazonaws.services.ec2.model.TerminateInstancesResult;
import com.amazonaws.services.ec2.model.DescribeKeyPairsRequest;
import com.amazonaws.services.ec2.model.DescribeKeyPairsResult;
//import com.amazonaws.services.ec2.model.Filter;
//import com.amazonaws.regions.Regions;

public class EC2_Wrapper {

	private static AmazonEC2 ec2Client;
	private String ReservationId;
	private String instanceId;
	private AWSCredentials credentials;
	private String publicDNS;
	private String privateIP;
	private String publicIP;

	/**
	 * @return the reservationId
	 */
	public String getReservationId() {
		return ReservationId;
	}

	/**
	 * @return the instanceId
	 */
	public String getInstanceId() {
		return instanceId;
	}

	public String getPublicDNS() {
		return publicDNS;
	}

	public String getPrivateIP() {
		return privateIP;
	}

	public String getPublicIP() {
		return publicIP;
	}

	private KeyPair keyPair;

	// Constructor
	public EC2_Wrapper(String accKey, String secKey) throws Exception {
		credentials = new BasicAWSCredentials(accKey, secKey);
		ec2Client = new AmazonEC2Client(credentials);
	}

	public String getAMInstanceName() {
		return instanceId;
	}

	public void createAMInstanceSmall(String amiId, String keyPairName) throws Exception {
		createAMInstances(amiId, 1, 1, keyPairName, "t2.small", "eu-central-1");
	}

	public void createAMInstances(String AMId, int min, int max, String keyPairName, String insType,
			String availabilityZone) throws Exception {
		RunInstancesRequest request = new RunInstancesRequest();
		request.setInstanceType(insType);
		request.setMinCount(min);
		request.setMaxCount(max);

		ec2Client.setEndpoint("ec2.eu-central-1.amazonaws.com");
		request.setImageId(AMId);

		request.setKeyName(keyPairName);// assign Keypair name for this request

		RunInstancesResult runInstancesRes = this.ec2Client.runInstances(request);

		this.ReservationId = runInstancesRes.getReservation().getReservationId();
		DescribeInstancesResult describeInstancesRequest = ec2Client.describeInstances();
		List<Reservation> reservations = describeInstancesRequest.getReservations();
		Set<Instance> instances = new HashSet<>();

		for (Reservation reservation : reservations) {
			instances.addAll(reservation.getInstances());
			if (reservation.getReservationId().equals(this.ReservationId)) {
				System.out.println("Creating instance...");
				TimeUnit.SECONDS.sleep(30);
				int flag = 0;
				while (getPrivateIP() == null || getPublicIP() == null || getPublicDNS() == "") {
					this.instanceId = ((Instance) reservation.getInstances().get(0)).getInstanceId();
					this.privateIP = ((Instance) reservation.getInstances().get(0)).getPrivateIpAddress();
					this.publicDNS = ((Instance) reservation.getInstances().get(0)).getPublicDnsName();
					this.publicIP = ((Instance) reservation.getInstances().get(0)).getPublicIpAddress();
					TimeUnit.SECONDS.sleep(1);
					flag++;
					if (flag > 10)
						break;
				}
				System.out.println("this.instanceId = " + this.instanceId);
				System.out.println("this.publicDNS = " + this.publicDNS);
				System.out.println("this.privateIP = " + this.privateIP);
				System.out.println("this.pubpublicIP = " + this.publicIP);
				String csv = writeCSV(getPublicDNS(), getPublicIP(), getPrivateIP(), getInstanceId(), "createInstance",
						true);// We need to add it in the file, not to overwrite
								// it.
				System.out.println(csv);
			}
		}
		System.out.println("You have " + instances.size() + " Amazon EC2 instance(s).");
	}

	public List<String> getKeyName() {
		DescribeKeyPairsRequest dkpr = new DescribeKeyPairsRequest();
		DescribeKeyPairsResult dkpresult = ec2Client.describeKeyPairs(dkpr);

		List<KeyPairInfo> keyPairs = dkpresult.getKeyPairs();
		List<String> keyPairNameList = new ArrayList<String>();

		for (KeyPairInfo keyPairInfo : keyPairs) {
			keyPairNameList.add(keyPairInfo.getKeyName());
			System.out.println("You havekeyPair.getKeyName = " + keyPairInfo.getKeyName()
					+ "\nkeyPair.getKeyFingerprint()=" + keyPairInfo.getKeyFingerprint());
		}

		for (int i = 0; i < keyPairs.size(); i++) {
			System.out.println(keyPairNameList.get(i));
		}

		return keyPairNameList;
	}

	public static void main(String args[]) throws Exception {
		//Here to input personal information, 
		EC2_Wrapper shit = new EC2_Wrapper("", "");

		// java -jar checkME.jar
		if (args.length == 0) {
			listRunningInstance();
		} else if (args.length == 2) {
			if (args[0].equals("list")) {
				// java -jar checkME.jar list all
				if (args[1].equals("all")) {
					listInstance();
				} // java -jar checkME.jar list i-0f73e92cb135f557b
				else {
					System.out.println("Checking status of " + args[1]);
					listInstance(args[1]);
				}
			} // java -jar checkME.jar delete i-0f73e92cb135f557b
			else if (args[0].equals("delete")) {
				System.out.println("Deleteing " + args[1]);
				terminateAMIs(args[1]);
			} // java -jar checkMe.jar Addsure <amiID>
			else if(args[0].equals("AddSure")&&args[1].startsWith("ami-")){
				System.out.println("Creating one instance with AMI=\t" + args[1] +"...");
				shit.createAMInstanceSmall(args[1], "new");
			}
		} // java -jar checkME.jar AddSure
		else if (args.length == 1 && args[0].equals("AddSure")) {
			shit.createAMInstanceSmall("ami-5579bc3a", "new");
		}

		// writeCSV("a","10","11");

		// String[] anotherShit = readCSV();
		// for (int i = 0; i < 3; i++)
		// System.out.println(anotherShit[i]);

	}

	//
	// would give an error if the instances is terminated
	public static void listInstance(String ID) {
		ec2Client.setEndpoint("ec2.eu-central-1.amazonaws.com");
		DescribeInstanceStatusRequest describeInstanceRequest = new DescribeInstanceStatusRequest().withInstanceIds(ID);
		DescribeInstanceStatusResult describeInstanceResult = ec2Client.describeInstanceStatus(describeInstanceRequest);
		List<InstanceStatus> state = describeInstanceResult.getInstanceStatuses();
		int i = 0;
		int count = 5;
		while (state.size() < 1 && i <= count) {
			// Do nothing, just wait, have thread sleep if needed

			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			describeInstanceResult = ec2Client.describeInstanceStatus(describeInstanceRequest);
			state = describeInstanceResult.getInstanceStatuses();
			i++;
		}
		if (i <= count) {
			String status = state.get(0).getInstanceState().getName();
			System.out.println(status);

		} else {
			System.out.println("Error: instance ID may be wrong or instance is not running...");
		}
	}

	// For another function use
	public static boolean checkInstance(String ID) {
		ec2Client.setEndpoint("ec2.eu-central-1.amazonaws.com");
		DescribeInstanceStatusRequest describeInstanceRequest = new DescribeInstanceStatusRequest().withInstanceIds(ID);
		DescribeInstanceStatusResult describeInstanceResult = ec2Client.describeInstanceStatus(describeInstanceRequest);
		List<InstanceStatus> state = describeInstanceResult.getInstanceStatuses();
		int i = 0;
		int count = 2;
		while (state.size() < 1 && i <= count) {
			// Do nothing, just wait, have thread sleep if needed

			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			describeInstanceResult = ec2Client.describeInstanceStatus(describeInstanceRequest);
			state = describeInstanceResult.getInstanceStatuses();
			i++;
		}
		if (i <= count) {
			String status = state.get(0).getInstanceState().getName();
			// System.out.println(status);
			return true;
		} else {
			// System.out.println("Error: instance ID may be wrong or instance
			// is not running...");
			return false;
		}
	}

	// List all instance
	public static void listInstance() {
		ec2Client.setEndpoint("ec2.eu-central-1.amazonaws.com");
		DescribeInstancesRequest request = new DescribeInstancesRequest();
		DescribeInstancesResult result = ec2Client.describeInstances();
		List<Reservation> reservations = result.getReservations();
		for (Reservation reservation : reservations) {
			List<Instance> instances = reservation.getInstances();
			for (Instance instance : instances) {

				System.out.println("ID \t" + instance.getInstanceId() + "\ttags\t" + instance.getTags()
						+ "\tPublic DNS \t" + instance.getPublicDnsName() + "\tPublic IP \t"
						+ instance.getPublicIpAddress() + "\tPrivate IP\t" + instance.getPrivateIpAddress());
			}
		}
	}

	// List all running instances and write information in csv file.
	public static void listRunningInstance() throws IOException {
		ec2Client.setEndpoint("ec2.eu-central-1.amazonaws.com");
		DescribeInstancesRequest request = new DescribeInstancesRequest();
		DescribeInstancesResult result = ec2Client.describeInstances();
		List<Reservation> reservations = result.getReservations();

		boolean addMore = false;
		for (Reservation reservation : reservations) {
			List<Instance> instances = reservation.getInstances();
			for (Instance instance : instances) {
				String Id = instance.getInstanceId();
				if (checkInstance(Id)) {

					String csv = writeCSV(instance.getPublicDnsName(), instance.getPublicIpAddress(),
							instance.getPrivateIpAddress(), instance.getInstanceId(), "runningInstance", addMore);
					if (addMore == false)
						System.out.println(csv);

					System.out.println(instance.getPublicDnsName() + "\t" + instance.getPublicIpAddress() + "\t"
							+ instance.getPrivateIpAddress() + "\t" + instance.getInstanceId() + "\t"
							+ instance.getTags());
					addMore = true;
				}
			}
		}

	}

	public static void terminateAMIs(String ID) throws Exception {
		try {
			TerminateInstancesRequest rq = new TerminateInstancesRequest();
			ec2Client.setEndpoint("ec2.eu-central-1.amazonaws.com");
			// rq.InstanceIds = new List<string>() { instanceId };
			rq.getInstanceIds();
			rq.getInstanceIds().add(ID);
			try {
				TerminateInstancesResult rsp = ec2Client.terminateInstances(rq);
			} catch (Error err) {
				err.printStackTrace();
			}
			System.out.println("Terminate " + ID + " suceesfully.");

		} catch (Exception ex) {
			throw ex;
		}
	}

	// String csvFileName gives the name of csv file
	// addMoreLine is true means, we add the information under the current
	// information
	// addMoreLine is false means, we overwrite the csv file.
	// Return the path of the csv file.
	public static String writeCSV(String publicDNS, String publicIP, String privateIP, String instanceId,
			String csvFileName, boolean addMoreLine) throws IOException {
		String Path = Paths.get(".").toAbsolutePath().normalize().toString();
		String csv = Path + "/" + csvFileName + ".csv";

		FileWriter pw = new FileWriter(csv, addMoreLine);

		// DNS
		pw.append(publicDNS);
		pw.append(",");
		// Public IP
		pw.append(publicIP);
		pw.append(",");
		// Private IP
		pw.append(privateIP);
		pw.append(",");
		pw.append(instanceId);

		pw.append("\n");
		pw.flush();
		pw.close();

		return csv;

	}

	/*
	 * Return String[]
	 */
	public static String[] readCSV() throws FileNotFoundException {
		String Path = Paths.get(".").toAbsolutePath().normalize().toString();
		String csv = Path + "/instance.csv";
		BufferedReader reader = new BufferedReader(new FileReader(csv));

		String line = "";
		String[] Shit = null;

		try {
			while ((line = reader.readLine()) != null) {
				Shit = line.trim().split(",");
				// if you want to check either it contains some name
				// index 0 is first name, index 1 is last name, index 2 is ID
				System.out.println(Shit[0] + "," + Shit[1] + "," + Shit[2] + "," + Shit[3]);

				// line = reader.readLine();

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Shit;

	}

}
