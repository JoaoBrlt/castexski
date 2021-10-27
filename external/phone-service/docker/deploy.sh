#!/bin/bash

# Login to Docker Hub.
#docker login -u isadevops2021teamf -p password

# Backup the previous image.
docker tag isadevops2021teamf/phone-service isadevops2021teamf/phone-service:local

# Deploy the new image.
docker rmi $(docker images 'isadevops2021teamf/phone-service:latest' -a -q)
docker tag isadevops2021teamf/phone-service:local isadevops2021teamf/phone-service:latest
docker push isadevops2021teamf/phone-service:latest

# Remove the phone service.
docker rmi isadevops2021teamf/phone-service:local

# Remove the unused the images.
docker rmi $(docker images -f 'dangling=true' -a -q) || echo "Nothing else to remove"
