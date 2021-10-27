#!/bin/bash

# Run the docker image.
# Options:
# - Remove the image after exit.
# - Bind the current folder to the container.
docker run --name isadevops2021teamf-client --rm -d isadevops2021teamf/client
