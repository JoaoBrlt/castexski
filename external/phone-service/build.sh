#!/bin/bash

GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m'

# Build the Mono project.
echo "[Phone Service] Compiling the Mono project."
bash compile.sh
if [ $? != 0 ]; then
  echo -e "${RED}[Phone Service] Failed to compile the Mono project.${NC}"
  echo -e "${RED}[Phone Service] Please make sure you have installed mono.${NC}"
  exit
else
  echo -e "${GREEN}[Phone Service] Successfully compiled the Mono project.${NC}"
fi

# Build the Docker image.
echo "[Phone Service] Building the Docker image."
cp server.exe docker/server.exe
docker build -t isadevops2021teamf/phone-service docker --quiet
if [ $? != 0 ]; then
  echo -e "${RED}[Phone Service] Failed to build the Docker image.${NC}"
else
  echo -e "${GREEN}[Phone Service] Successfully built the Docker image.${NC}"
fi

# Clean the environment.
rm -rf docker/server.exe