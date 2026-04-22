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

    @Test @Order(6)
    void listByGenre_returnsFilteredBooks() {
        given().contentType(ContentType.JSON)
            .body("{\"title\":\"Dune\",\"author\":\"Herbert\",\"genre\":\"SciFi\",\"price\":14.99}")
            .when().post("/books").then().statusCode(201);
        given().contentType(ContentType.JSON)
            .body("{\"title\":\"Foundation\",\"author\":\"Asimov\",\"genre\":\"SciFi\",\"price\":12.99}")
            .when().post("/books").then().statusCode(201);
        given().contentType(ContentType.JSON)
            .body("{\"title\":\"Clean Code\",\"author\":\"Martin\",\"genre\":\"Programming\",\"price\":29.99}")
            .when().post("/books").then().statusCode(201);

        given()
            .queryParam("genre", "SciFi")
            .when().get("/books")
            .then()
            .statusCode(200)
            .body("$", hasSize(2))          // "$" = root of JSON response; asserts the array has 2 elements
            .body("genre", everyItem(equalToIgnoringCase("SciFi")));
    }

    @Test @Order(7)
    void listByGenre_noMatch_returnsEmpty() {
        given()
            .queryParam("genre", "NonExistent")
            .when().get("/books")
            .then()
            .statusCode(200)
            .body("$", hasSize(0));
    }

    @Test @Order(8)
    void updateBook_returnsUpdated() {
        Number id = given()
            .contentType(ContentType.JSON)
            .body("{\"title\":\"Old Title\",\"author\":\"Author\",\"genre\":\"Fiction\",\"price\":9.99}")
            .when().post("/books")
            .then().statusCode(201).extract().path("id");

        given()
            .contentType(ContentType.JSON)
            .body("{\"title\":\"New Title\",\"author\":\"Author\",\"genre\":\"Fiction\",\"price\":19.99}")
            .when().put("/books/" + id)
            .then()
            .statusCode(200)
            .body("title", equalTo("New Title"))
            .body("price", equalTo(19.99f));
    }

    @Test @Order(9)
    void updateBook_notFound() {
        given()
            .contentType(ContentType.JSON)
            .body("{\"title\":\"X\",\"author\":\"Y\",\"genre\":\"Z\",\"price\":1.0}")
            .when().put("/books/9999")
            .then()
            .statusCode(404)
            .body("error", notNullValue());
    }

    @Test @Order(10)
    void deleteBook_returns204() {
        Number id = given()
            .contentType(ContentType.JSON)
            .body("{\"title\":\"ToDelete\",\"author\":\"Author\",\"genre\":\"Fiction\",\"price\":5.0}")
            .when().post("/books")
            .then().statusCode(201).extract().path("id");

        given()
            .when().delete("/books/" + id)
            .then()
            .statusCode(204);

        given()
            .when().get("/books/" + id)
            .then()
            .statusCode(404);
    }

    @Test @Order(11)
    void deleteBook_notFound() {
        given()
            .when().delete("/books/9999")
            .then()
            .statusCode(404)
            .body("error", notNullValue());
    }

    @Test @Order(12)
    void createMultiple_listReturnsAll() {
        given().contentType(ContentType.JSON)
            .body("{\"title\":\"Book A\",\"author\":\"A\",\"genre\":\"Fiction\",\"price\":10.0}")
            .when().post("/books").then().statusCode(201);
        given().contentType(ContentType.JSON)
            .body("{\"title\":\"Book B\",\"author\":\"B\",\"genre\":\"Fiction\",\"price\":11.0}")
            .when().post("/books").then().statusCode(201);

        given()
            .when().get("/books")
            .then()
            .statusCode(200)
            .body("$", hasSize(2));
    }
}
