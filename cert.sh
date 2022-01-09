#!/bin/bash

# Use to resolve certificate issues caused by the use of certbot

sudo mkdir -p /etc/docker/certs.d/harbor.dndev.pw
sudo openssl s_client -showcerts -connect harbor.dndev.pw:4443 < /dev/null | sudo sed -ne '/-BEGIN CERTIFICATE-/,/-END CERTIFICATE-/p' > dndev.crt
sudo cat dndev.crt
sudo cp dndev.crt /usr/local/share/ca-certificates/
sudo update-ca-certificates
sudo systemctl restart docker
sudo docker info