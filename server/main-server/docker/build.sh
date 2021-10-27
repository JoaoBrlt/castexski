#!/bin/bash

# Preparing the environment.
cd ..
echo "Compiling the server."
mvn -q -DskipTests clean package
echo "Done"
cp target/main-server-*.ear docker/server.ear

# Building the docker image.
cd docker
docker build -t isadevops2021teamf/server .

# Cleaning up the environment.
rm -rf server.ear