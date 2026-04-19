package org.karane;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class ConfigResourceTest {

    @Test
    void testProperties() {
        given()
            .when().get("/config/properties")
            .then()
            .statusCode(200)
            .body("greeting", equalTo("Hello from Quarkus TEST!"))
            .body("maxItems", equalTo(5))
            .body("featureFlag", equalTo(false))
            .body("activeProfile", equalTo("test"));
    }

    @Test
    void testMapping() {
        given()
            .when().get("/config/mapping")
            .then()
            .statusCode(200)
            .body("greeting", equalTo("Hello from Quarkus TEST!"))
            .body("maxItems", equalTo(5))
            .body("database.host", equalTo("localhost"))
            .body("database.port", equalTo(5432))
            .body("database.name", equalTo("mydb"));
    }
}
