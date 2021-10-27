#!/bin/bash

# Run the docker image.
# Options:
# - Adds this container to the jenkins network
# - Names this container
# - Remove the image after exit.
# - Run the image in background.
# - Bind localhost:9393 to container:9393.
docker run --network="jenkins_default" --name isadevops2021teamf-phone-service --rm -d -p 9393:9393 isadevops2021teamf/phone-service
