curl http://instance-data/latest/meta-data/local-ipv4 >private_ip
curl http://instance-data/latest/meta-data/public-ipv4 >public_ip
curl http://instance-data/latest/meta-data/public-hostname >public_dns
curl http://instance-data/latest/meta-data/instance-id >instance_id
