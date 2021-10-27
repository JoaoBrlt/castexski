FROM mono:6.12.0
MAINTAINER Team F (isa.devops.2021.team.f@gmail.com)

# Navigate to the application folder.
WORKDIR /usr/src/app

# Deploy the service.
COPY ./server.exe ./

# Expose the port.
EXPOSE 9393

# Start the service.
ENTRYPOINT ["mono", "./server.exe"]
CMD ["/standalone"]