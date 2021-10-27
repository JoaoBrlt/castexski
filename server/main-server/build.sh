#!/bin/bash

GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m'

# Build the Docker image.
echo "[Server] Building the Docker image."
cp target/main-server-*.ear docker/server.ear
docker build -t isadevops2021teamf/server docker --quiet
if [ $? != 0 ]; then
  echo -e "${RED}[Server] Failed to build the Docker image.${NC}"
else
  echo -e "${GREEN}[Server] Successfully built the Docker image.${NC}"
fi

# Clean up the environment.
rm -rf docker/server.ear