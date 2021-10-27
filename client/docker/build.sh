#!/bin/bash

# Preparing the environment.
cd ..
echo "Compiling the client."
mvn -q -DskipTests clean package
if [ ${?} == 0 ]
then
	echo "Done"
	cp target/client.jar docker/client.jar
	# Building the docker image.
	cd docker
	docker build -t isadevops2021teamf/client .

	# Cleaning up the environment.
	rm -rf client.jar
else
	printf "Couldn't build client.jar\nExiting...\n"
fi