# quarkus-hello-world-poc

## Bootstrapped with this command

```bash
mvn io.quarkus.platform:quarkus-maven-plugin:3.31.4:create \
  -DprojectGroupId=org.karane \
  -DprojectArtifactId=<my-new-poc> \
  -DnoCode
```

Omit `-DnoCode` to include a generated "Hello World" REST endpoint.

## Prerequisites

- Java 21+
- Maven 3.9+ (or use the included `./mvnw` wrapper)

## Running

### Dev mode (with live reload)

```bash
mvn quarkus:dev
```

The app starts at `http://localhost:8080`. Any source change is picked up automatically without restarting.

### Run the tests

```bash
mvn test
```

### Package and run as JAR

```bash
mvn package
java -jar target/quarkus-app/quarkus-run.jar
```

## Checking results / output

### Hit the endpoint

```bash
curl http://localhost:8080/hello
```

Expected response:

```
Hello from Quarkus REST
```

### Dev UI

While running in dev mode, open the Dev UI in your browser:

```
http://localhost:8080/q/dev/
```

The Dev UI shows installed extensions, CDI beans, configuration, and more — all without any extra setup.

### Test output

After `mvn test` you should see:

```
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
```
