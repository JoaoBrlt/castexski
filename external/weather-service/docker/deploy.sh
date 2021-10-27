#!/bin/bash

# Login to Docker Hub.
#docker login -u isadevops2021teamf -p password

# Backup the previous image.
docker tag isadevops2021teamf/weather-service isadevops2021teamf/weather-service:local

# Deploy the new image.
docker rmi $(docker images 'isadevops2021teamf/weather-service:latest' -a -q)
docker tag isadevops2021teamf/weather-service:local isadevops2021teamf/weather-service:latest
docker push isadevops2021teamf/weather-service:latest

# Remove the previous image.
docker rmi isadevops2021teamf/weather-service:local

# Remove the unused images.
docker rmi $(docker images -f 'dangling=true' -a -q) || echo "Nothing else to remove"
