#!/bin/bash

# Build the external services.
(cd external && bash build.sh)

# Build the server.
(cd server && bash build.sh)

# Build the client.
(cd client && bash build.sh)
