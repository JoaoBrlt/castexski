#!/bin/bash

GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m'

# Build the Maven project.
echo "[Client] Compiling the Maven project."
mvn clean package -DskipTests --quiet
if [ $? != 0 ]; then
  echo -e "${RED}[Client] Failed to compile the Maven project.${NC}"
  exit
else
  echo -e "${GREEN}[Client] Successfully compiled the Maven project.${NC}"
fi

# Build the Docker image.
echo "[Client] Building the Docker image."
cp target/client.jar docker/client.jar
docker build -t isadevops2021teamf/client docker --quiet
if [ $? != 0 ]; then
  echo -e "${RED}[Client] Failed to build the Docker image.${NC}"
else
  echo -e "${GREEN}[Client] Successfully built the Docker image.${NC}"
fi

# Clean the environment.
rm docker/client.jar