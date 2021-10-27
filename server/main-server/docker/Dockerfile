FROM tomee:8-jre-7.0.2-plus
MAINTAINER Team F (isa.devops.2021.team.f@gmail.com)

# Navigate to the TomEE+ folder.
WORKDIR /usr/local/tomee/

# Deploy the server.
COPY ./server.ear ./apps/

# Configure the deployments.
COPY ./resources/tomee.xml ./conf/tomee.xml

# Configure the users.
COPY ./resources/tomcat-user.xml ./conf/tomcat-users.xml

# Configure the access.
COPY ./resources/manager-context.xml ./webapps/manager/META-INF/context.xml

# Get the environment variables.
# For the Bank Service
ENV BANK_HOST=bank-service
ENV BANK_PORT=9090

# For the Display Service
ENV DISPLAY_HOST=display-service
ENV DISPLAY_PORT=9191

# For the Mail Service
ENV MAIL_HOST=mail-service
ENV MAIL_PORT=9292

# For the Phone Service
ENV PHONE_HOST=phone-service
ENV PHONE_PORT=9393

# For the Weather Service
ENV WEATHER_HOST=weather-service
ENV WEATHER_PORT=9494

# Configure the health check.
HEALTHCHECK --interval=5s CMD curl --fail http://localhost:8080/ || exit 1

# Expose the port.
EXPOSE 8888

# Start the TomEE+ server.
ENTRYPOINT ["catalina.sh", "run"]
