# Workout Tracker GraphQL API (Spring Boot)

## Overview

This Spring Boot application provides a GraphQL API for logging workouts and exercises per user. The primary goal is to
track the progress of workout data. Key features and details of this project are outlined below:

- **GraphQL API**: This application exposes a GraphQL API for querying and mutating workout data, enabling users to log
  their workouts and exercises.

- **Technology Stack**:
    - Java 17
    - Spring Boot 2.6.8
    - Spring 1.0.11
    - Gradle

- **Database**: The backend database is MongoDB, a NoSQL database, used to store and retrieve workout-related data.

- **Configuration**: This application relies on environment variables for configuration, including:
    - AWS Cognito: Identity Provider for authentication and authorization
    - CORS Allowed Origins: Security configuration for cross-origin requests
    - MongoDB Credentials: Database connection information
    - Sentry: Issue tracking and error monitoring

- **GraphQL Schema**: The GraphQL schemas use default types along with an added scalar, `LocalDate`, for date-related
  data.

- **Authentication and Authorization**: Authentication and authorization are handled by AWS Cognito, and API requests
  require a valid JWT token to access protected resources.

- **Unit Tests**: Unit tests are not implemented yet but are planned for future development after updates to Spring,
  Gradle, Spring Boot, and GraphQL libraries.

- **Deployment**: Deployment is currently performed by building the Gradle project and manually uploading it to an EC2
  instance. There are plans to containerize the application and deploy it via Docker with Kubernetes in the future.

- **Continuous Integration/Continuous Deployment (CI/CD)**: No CI/CD pipelines are implemented at this time. Future
  considerations may include implementing CI/CD pipelines, although costs and resources need to be taken into account.

- **License**: This project is licensed under [MIT License](LICENSE.md).

- **Contact Information**: For any questions or inquiries, you can contact the developer at skaedin@gmail.com.

## Libraries and Frameworks

This project relies on the following libraries and frameworks:

- Spring
  Boot: [spring-boot-starter-web](https://docs.spring.io/spring-boot/docs/current/reference/html/getting-started.html#getting-started-system-requirements)
- Spring
  Security: [spring-boot-starter-security](https://docs.spring.io/spring-security/site/docs/current/reference/html5/)
- Auth0 JWT Authorization: [jwks-rsa](https://github.com/auth0/java-jwt)
- Guava: [guava](https://github.com/google/guava)
- Friendly ID: [friendly-id-spring-boot-starter](https://github.com/devskiller/friendly-id)
- MongoDB: [spring-boot-starter-data-mongodb](https://spring.io/projects/spring-data-mongodb)
- GraphQL: [graphql-spring-boot-starter](https://github.com/graphql-java-kickstart/graphql-spring-boot)
- GraphQL Tools: [graphql-java-tools](https://github.com/graphql-java-kickstart/graphql-java-tools)
- GraphQL Date and
  Time: [graphql-datetime-spring-boot-starter](https://github.com/zhokhov/graphql-datetime-spring-boot-starter)
- Lombok: [lombok](https://projectlombok.org/)
- JUnit: [junit](https://junit.org/junit4/)
- AWS SDK: [software.amazon.awssdk](https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/get-started.html)
- Spring Security
  Test: [spring-security-test](https://docs.spring.io/spring-security/site/docs/current/reference/html5/#test)
- Spring Boot
  Test: [spring-boot-starter-test](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-testing.html)
- GraphQL Spring Boot
  Test: [graphql-spring-boot-starter-test](https://github.com/graphql-java-kickstart/graphql-spring-boot)

## Changelog

The changelog for this project is available in the [CHANGELOG.md](CHANGELOG.md) file.

---

Thank you for your interest in this project! If you have any questions or feedback, please feel free to reach out to the
developer at **skaedin@gmail.com**.
