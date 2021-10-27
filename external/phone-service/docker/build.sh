#!/bin/bash

# Building the docker image.
docker build -t isadevops2021teamf/phone-service .

# Cleaning up the environment.
rm -rf server.exe
