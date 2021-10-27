#!/bin/bash

GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m'

# Build the Maven project.
echo "[Server] Compiling the Maven project."
mvn clean package -DskipTests --quiet
if [ $? != 0 ]; then
  echo -e "${RED}[Server] Failed to compile the Maven project.${NC}"
  exit
else
  echo -e "${GREEN}[Server] Successfully compiled the Maven project.${NC}"
fi

# Build the Docker image.
(cd main-server && bash build.sh)