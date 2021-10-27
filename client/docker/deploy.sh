#!/bin/bash

# Login to Docker Hub.
#docker login -u isadevops2021teamf --password-stdin

# Backup the previous image.
docker tag isadevops2021teamf/client isadevops2021teamf/client:local

# Deploy the new image.
docker rmi $(docker images 'isadevops2021teamf/client:latest' -a -q)
docker tag isadevops2021teamf/client:local isadevops2021teamf/client:latest
docker push isadevops2021teamf/client:latest

# Remove the previous image.
docker rmi isadevops2021teamf/client:local

# Remove the unused image.
docker rmi $(docker images -f 'dangling=true' -a -q) || echo "Nothing else to remove"
