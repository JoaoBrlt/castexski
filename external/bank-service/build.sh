#!/bin/bash

GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m'

# Build the Mono project.
echo "[Bank Service] Compiling the Mono project."
bash compile.sh
if [ $? != 0 ]; then
  echo -e "${RED}[Bank Service] Failed to compile the Mono project.${NC}"
  echo -e "${RED}[Bank Service] Please make sure you have installed mono.${NC}"
  exit
else
  echo -e "${GREEN}[Bank Service] Successfully compiled the Mono project.${NC}"
fi

# Build the Docker image.
echo "[Bank Service] Building the Docker image."
cp server.exe docker/server.exe
docker build -t isadevops2021teamf/bank-service docker --quiet
if [ $? != 0 ]; then
  echo -e "${RED}[Bank Service] Failed to build the Docker image.${NC}"
else
  echo -e "${GREEN}[Bank Service] Successfully built the Docker image.${NC}"
fi

# Clean the environment.
rm -rf docker/server.exe