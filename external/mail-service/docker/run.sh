#!/bin/bash

# Run the docker image.
# Options:
# - Adds this container to the jenkins network
# - Names this container
# - Remove the image after exit.
# - Run the image in background.
# - Bind localhost:9292 to container:9292.
docker run --network="jenkins_default" --name isadevops2021teamf-mail-service --rm -d -p 9292:9292 isadevops2021teamf/mail-service
