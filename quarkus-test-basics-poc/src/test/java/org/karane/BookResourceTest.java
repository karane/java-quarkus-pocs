package org.karane;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import org.junit.jupiter.api.*;
import org.karane.service.BookService;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookResourceTest {

    @Inject
    BookService bookService;

    private static int testsRun = 0;

    @BeforeAll
    static void setupSuite() {
        testsRun = 0;
    }

    @BeforeEach
    void resetStore() {
        bookService.deleteAll();
    }

    @AfterEach
    void countTest() {
        testsRun++;
    }

    @AfterAll
    static void teardownSuite() {
        Assertions.assertTrue(testsRun > 0, "At least one test should have run");
    }

    @Test @Order(1)
    void emptyStoreReturnsEmptyList() {
        given()
            .when().get("/books")
            .then()
            .statusCode(200)
            .body("$", hasSize(0));
    }

    @Test @Order(2)
    void createBook_returnsCreatedWithLocationAndBody() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"title":"Clean Code","author":"Robert Martin","genre":"Programming","price":29.99}
                """)
            .when().post("/books")
            .then()
            .statusCode(201)
            .header("Location", containsString("/books/"))
            .body("id", notNullValue())
            .body("title", equalTo("Clean Code"))
            .body("price", equalTo(29.99f));
    }

    @Test @Order(3)
    void createBook_missingTitle_returns400() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"author":"Unknown","genre":"Fiction","price":9.99}
                """)
            .when().post("/books")
            .then()
            .statusCode(400)
            .body("error", notNullValue());
    }

    @Test @Order(4)
    void getById_returnsBook() {
        Number id = given()
            .contentType(ContentType.JSON)
            .body("""
                {"title":"Dune","author":"Herbert","genre":"SciFi","price":14.99}
                """)
            .when().post("/books")
            .then().statusCode(201).extract().path("id");

        given()
            .when().get("/books/" + id)
            .then()
            .statusCode(200)
            .body("title", equalTo("Dune"))
            .body("author", equalTo("Herbert"));
    }

    @Test @Order(5)
    void getById_notFound() {
        given()
            .when().get("/books/9999")
            .then()
            .statusCode(404)
            .body("error", notNullValue());
    }

}
