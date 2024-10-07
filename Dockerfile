FROM maven:3.8.7-openjdk-17 AS build

WORKDIR /app

# Copy the pom.xml and download dependencies
COPY pom.xml ./
RUN mvn dependency:go-offline

# Copy the project files
COPY src ./src

# Run the tests
CMD ["mvn", "clean", "verify"]