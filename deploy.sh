#!/usr/bin/env sh

hostUser=$1
host=$2
key=$3

# 1. kill existing servers and clean old version
ssh -i $key $hostUser@$host "sudo killall java"
#ssh -i $key $hostUser@$host "rm -rf ~/app"
#ssh -i $key $hostUser@$host "mkdir -p ~/app"

# 2. copy bundle to host
#scp -i $key -r ./build/dist $hostUser@$host:~/app/

# 3. start server
executeCommand="cd ~/app/dist; sudo bash server/server-0.1.1/bin/server"
ssh -i $key $hostUser@$host "$executeCommand"

# (4. kill after closing console, you can comment that and it still will be working)
ssh -i $key $hostUser@$host "sudo killall java"


#!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
#Run this to start a server in Powershell
#.\deploy.sh HOST_USER HOST ----> SSH_KEY <----

#     SEEE local.properties

#!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!