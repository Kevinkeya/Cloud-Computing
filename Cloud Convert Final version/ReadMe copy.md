The RM folder should be deployed in the virtual machine served as Resource Manager. There should be two RM, a Master and a Slave (backup), they use the same codes

The Client folder should be deployed in the virtual machine served as Client.There can be arbitrary number of Clients.

The Node folder should be deployed in the virtual machine served as Node. There should be Arbitrary Nodes, 
but notice that once the workload of the system is too low, the Nodes will terminates itself, the criteria to kill Nodes can be customised in NodeKiller.java

For RM/Client/Node, simply run the run.sh files. But notice that because some softwares we used have problems in supporting relative path, we use absolute path.
You should put the RM,Client and Node folder in /home/ubuntu. One virtual machine should only holds one of RM, Client or Node.

- checkME.jar is used to create new Amazon VM.(see https://github.com/Kevinkeya/Cloud-Computing)
- the face_detect.py is used for face detection(see https://github.com/lidiafgarces/FaceDetect)