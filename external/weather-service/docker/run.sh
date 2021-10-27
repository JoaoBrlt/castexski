#!/bin/bash

# Run the docker image.
# Options :
# - Adds this container to the Jenkins network
# - Names the container
# - Remove the image after exit.
# - Run the image in background.
# - Bind localhost:9494 to container:9494.
docker run --network="jenkins_default" --name isadevops2021teamf-weather-service --rm -d -p 9494:9494 isadevops2021teamf/weather-service
