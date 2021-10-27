#!/bin/bash

# Build the bank service.
(cd bank-service && bash build.sh)

# Build the display service.
(cd display-service && bash build.sh)

# Build the mail service.
(cd mail-service && bash build.sh)

# Build the phone service.
(cd phone-service && bash build.sh)

# Build the weather service.
(cd weather-service && bash build.sh)