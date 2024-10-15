# Use the Maven image to build the project
FROM maven:3.9.5 AS build

# Set the working directory
WORKDIR /app

# Copy the pom.xml and source code
COPY pom.xml ./ 
COPY src ./src 

# Copy the config.properties file
COPY config.properties /app/config.properties

# Temporarily comment out the testng.xml line if needed
COPY ./testng.xml /app/testng.xml

# Run Maven to compile the project and run tests
RUN mvn clean test

# Final stage - Just for running the tests again if needed
FROM maven:3.9.5
WORKDIR /app
COPY --from=build /app /app

# Run tests
CMD ["mvn", "test"]