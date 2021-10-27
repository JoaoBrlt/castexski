#!/bin/bash

# Run the docker image.
# Options:
# - Adds this container to the jenkins network
# - Names this container
# - Remove the image after exit.
# - Run the image in background.
# - Bind localhost:9090 to container:9090.
docker run --network="jenkins_default" --name isadevops2021teamf-bank-service --rm -d -p 9090:9090 isadevops2021teamf/bank-service
