#/bin/sh

sudo yum install -y git vim gcc glibc-static telnet
sudo yum install -y curl policycoreutils-python openssh-server
sudo systemctl enable sshd
sudo systemctl start sshd

sudo yum install postfix
sudo systemctl enable postfix
sudo systemctl start postfix

sudo cp gitlab-ce.repo /etc/yum.repos.d/

sudo yum makecache

sudo EXTERNAL_URL="http://gitlab.example.com" yum install -y gitlab-ce

sudo gitlab-ctl reconfigure
