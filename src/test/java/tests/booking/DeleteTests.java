package tests.booking;

import api.CreateBooking;
import models.booking.GetBookingResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.TestBase;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static specs.GeneralSpec.*;

@DisplayName("Проверка удаления бронирования")
@Tag("booking_api")
public class DeleteTests extends TestBase {
    CreateBooking createBooking = new CreateBooking();

    @Test
    @DisplayName("Successful Returns the ids of all the bookings that exist within the API. Can take optional query strings to search and return a subset of booking ids.")
    public void successfulDeleteBookingByIdTest() {
        GetBookingResponse testData = createBooking.successfulCreateBooking();
        step("Отправить запрос на удаление конкретного бронирования по идентификатору бронирования", () ->
                given(requestSpecificationWithAuth)
                        .when()
                        .delete("/booking/" + testData.getBookingid())
                        .then()
                        .spec(responseSpecification201));

        step("Отправить запрос с идентификатором бронирования для проверки удаления", () ->
                given(requestSpecificationWithAuth)
                        .when()
                        .get("/booking/" + testData.getBookingid())
                        .then()
                        .spec(responseSpecification404));
    }

    @Test
    @DisplayName("Unsuccessful Returns the ids of all the bookings that exist within the API. Can take optional query strings to search and return a subset of booking ids.")
    public void unsuccessfulDeleteBookingById405Test() {
        step("Отправить запрос на удаление конкретного бронирования по идентификатору бронирования", () ->
                given(requestSpecificationWithAuth)
                        .when()
                        .delete("/booking/" + -1)
                        .then()
                        .spec(responseSpecification405));
    }

    @Test
    @DisplayName("Unsuccessful Returns the ids of all the bookings that exist within the API. Can take optional query strings to search and return a subset of booking ids.")
    public void unsuccessfulDeleteBookingById403Test() {
        GetBookingResponse testData = createBooking.successfulCreateBooking();
        step("Отправить запрос на удаление конкретного бронирования по идентификатору бронирования", () ->
                given(requestSpecificationWithoutAuth)
                        .when()
                        .delete("/booking/" + testData.getBookingid())
                        .then()
                        .spec(responseSpecification403));
    }
}
