#!/bin/bash

# Run the docker image.
# Options:
# - Adds this container to the jenkins network
# - Names this container
# - Remove the image after exit.
# - Run the image in background.
# - Bind localhost:9191 to container:9191.
docker run --network="jenkins_default" --name isadevops2021teamf-display-service --rm -d -p 9191:9191 isadevops2021teamf/display-service
