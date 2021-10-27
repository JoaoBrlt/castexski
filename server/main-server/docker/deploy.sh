#!/bin/bash

# Push the image to Docker Hub.
#docker login -u isadevops2021teamf -p password
# Save this one for later
docker tag isadevops2021teamf/server isadevops2021teamf/server:local
# Delete the old image
docker rmi $(docker images 'isadevops2021teamf/server:latest' -a -q)
docker tag isadevops2021teamf/server:local isadevops2021teamf/server:latest
docker push isadevops2021teamf/server:latest
# Untag this one
docker rmi isadevops2021teamf/server:local
# Just in case, we delete the unused images
docker rmi $(docker images -f 'dangling=true' -a -q) || echo "Nothing else to remove"
