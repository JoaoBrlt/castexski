#!/bin/bash

GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m'

# Run the project.
cd docker
docker-compose up -d
if [ $? != 0 ]; then
  echo -e "${RED}[CastexSki] Failed to run the project.${NC}"
  exit
else
  echo -e "${GREEN}[CastexSki] Successfully run the project.${NC}"
fi
