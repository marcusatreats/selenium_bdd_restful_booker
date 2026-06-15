package steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;

import static io.restassured.RestAssured.given;

public class BookingSteps {

    private final StepData stepData;
    private Response response;
    private int bookingId;

    private static final String BASE_URL = "https://restful-booker.herokuapp.com";

    public BookingSteps(StepData stepData) {
        this.stepData = stepData;
    }

    @Given("I authenticate into the Restful Booker API")
    public void iAuthenticateIntoTheRestfulBookerAPI() {
        RestAssured.baseURI = BASE_URL;

        response = given()
                .header("Content-Type", "application/json")
                .body("{ \"username\": \"admin\", \"password\": \"password123\" }")
                .post("/auth");

        stepData.authToken = response.jsonPath().getString("token");
        Assertions.assertNotNull(stepData.authToken, "Auth token should not be null");
        System.out.println("Auth token: " + stepData.authToken);
    }

    @When("I send a GET request to retrieve all bookings")
    public void iSendAGETRequestToRetrieveAllBookings() {
        response = given()
                .header("Content-Type", "application/json")
                .get("/booking");
    }

    @Then("I receive a {int} status code")
    public void iReceiveAStatusCode(int statusCode) {
        Assertions.assertEquals(statusCode, response.getStatusCode(),
                "Expected status code " + statusCode + " but got " + response.getStatusCode());
    }

    @Then("the response contains a list of bookings")
    public void theResponseContainsAListOfBookings() {
        Assertions.assertFalse(response.jsonPath().getList("bookingid").isEmpty(),
                "Booking list should not be empty");
        System.out.println("Total bookings found: " + response.jsonPath().getList("bookingid").size());
    }

    @When("I send a POST request with valid booking details")
    public void iSendAPOSTRequestWithValidBookingDetails() {
        String body = """
                {
                    "firstname": "Mark",
                    "lastname": "Treats",
                    "totalprice": 150,
                    "depositpaid": true,
                    "bookingdates": {
                        "checkin": "2026-07-01",
                        "checkout": "2026-07-07"
                    },
                    "additionalneeds": "Breakfast"
                }
                """;

        response = given()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .body(body)
                .post("/booking");

        bookingId = response.jsonPath().getInt("bookingid");
        stepData.bookingId = bookingId;
        System.out.println("Created booking ID: " + bookingId);
    }

    @Then("the response contains the new booking id")
    public void theResponseContainsTheNewBookingId() {
        Assertions.assertTrue(stepData.bookingId > 0,
                "Booking ID should be greater than 0");
        System.out.println("Verified booking ID: " + stepData.bookingId);
    }

    @When("I send a GET request for a specific booking")
    public void iSendAGETRequestForASpecificBooking() {
        createBookingForTest();
        response = given()
                .header("Content-Type", "application/json")
                .get("/booking/" + stepData.bookingId);
    }

    @Then("the response contains the correct booking details")
    public void theResponseContainsTheCorrectBookingDetails() {
        Assertions.assertEquals("Mark", response.jsonPath().getString("firstname"),
                "First name should be Mark");
        Assertions.assertEquals("Treats", response.jsonPath().getString("lastname"),
                "Last name should be Treats");
        System.out.println("Verified booking: " + response.jsonPath().getString("firstname")
                + " " + response.jsonPath().getString("lastname"));
    }

    @When("I send a PUT request with updated booking details")
    public void iSendAPUTRequestWithUpdatedBookingDetails() {
        createBookingForTest();
        String body = """
            {
                "firstname": "Marcus",
                "lastname": "Treats",
                "totalprice": 200,
                "depositpaid": true,
                "bookingdates": {
                    "checkin": "2026-07-01",
                    "checkout": "2026-07-10"
                },
                "additionalneeds": "Breakfast and Dinner"
            }
            """;

        response = given()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Cookie", "token=" + stepData.authToken)
                .body(body)
                .put("/booking/" + stepData.bookingId);
    }

    @Then("the response contains the updated details")
    public void theResponseContainsTheUpdatedDetails() {
        Assertions.assertEquals("Marcus", response.jsonPath().getString("firstname"),
                "First name should be updated to Marcus");
        Assertions.assertEquals(200, response.jsonPath().getInt("totalprice"),
                "Total price should be updated to 200");
        System.out.println("Updated booking verified: "
                + response.jsonPath().getString("firstname") + " - £"
                + response.jsonPath().getInt("totalprice"));
    }

    @When("I send a DELETE request for a booking")
    public void iSendADELETERequestForABooking() {
        createBookingForTest();
        response = given()
                .header("Content-Type", "application/json")
                .header("Cookie", "token=" + stepData.authToken)
                .delete("/booking/" + stepData.bookingId);
    }
    private void createBookingForTest() {
        String body = """
            {
                "firstname": "Mark",
                "lastname": "Treats",
                "totalprice": 150,
                "depositpaid": true,
                "bookingdates": {
                    "checkin": "2026-07-01",
                    "checkout": "2026-07-07"
                },
                "additionalneeds": "Breakfast"
            }
            """;

        Response createResponse = given()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .body(body)
                .post("/booking");

        stepData.bookingId = createResponse.jsonPath().getInt("bookingid");
        System.out.println("Setup booking ID: " + stepData.bookingId);
    }

}