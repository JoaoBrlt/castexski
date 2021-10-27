FROM openjdk:8
MAINTAINER Team F (isa.devops.2021.team.f@gmail.com)

# Navigate to the application folder.
WORKDIR /usr/src/app

# Deploy the client.
COPY ./client.jar ./

# Configure a volume.
VOLUME /host

# Start the client.
ENTRYPOINT ["java", "-jar", "./client.jar"]

# Give arguments.
CMD ["localhost", "8080"]