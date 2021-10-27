#!/bin/bash

# Run the docker image.
# Options:
# - Remove the image after exit.
# - Run the image in background.
# - Bind localhost:8888 to container:8888.
docker run --name isadevops2021teamf-server --rm -d -p 8888:8080 isadevops2021teamf/server
