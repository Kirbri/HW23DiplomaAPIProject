package tests.booking;

import api.CreateBooking;
import models.booking.Bookingid;
import models.booking.GetBookingRequest;
import models.booking.GetBookingResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.TestBase;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.GeneralSpec.*;

@DisplayName("Проверка получения списка бронирований")
@Tag("booking_api")
public class GetTests extends TestBase {

    CreateBooking createBooking = new CreateBooking();

    @Test
    @DisplayName("Successful returns the ids of all the bookings that exist within the API. Can take optional query strings to search and return a subset of booking ids.")
    public void successfulGetBookingIdsTest() {
        Bookingid[] responseData = step("Отправить запрос на получение бронирований", () ->
                given(requestSpecificationWithoutAuth)
                        .when()
                        .get("/booking")
                        .then()
                        .spec(responseSpecification200)
                        .extract().as(Bookingid[].class));

        step("Проверить данные в ответе", () -> {
            assertThat(responseData[0].getBookingid()).isNotZero();
            assertThat(responseData.length).isGreaterThan(100);

        });
    }

    @Test
    @DisplayName("Successful with by name returns the ids of all the bookings that exist within the API. Can take optional query strings to search and return a subset of booking ids.")
    public void successfulGetBookingIdsWithFilterNameThenEmptyResultsTest() {
        Bookingid[] responseData = step("Отправить запрос на получение бронирований по имени", () ->

                given(requestSpecificationWithoutAuth)
                        .when()
                        .queryParam("firstname", "sally")
                        .queryParam("lastname", "brown")
                        .get("/booking")
                        .then()
                        .spec(responseSpecification200)
                        .extract().as(Bookingid[].class));

        step("Проверить данные в ответе", () -> {
            assertThat(responseData.length).isEqualTo(0);
        });
    }

    @Test
    @DisplayName("Successful with by name returns the ids of all the bookings that exist within the API. Can take optional query strings to search and return a subset of booking ids.")
    public void successfulGetBookingIdsWithFilterNameThenNotEmptyResultsTest() {
        GetBookingResponse testData = createBooking.successfulCreateBooking();
        Bookingid[] responseData = step("Отправить запрос на получение бронирований по имени", () ->
                given(requestSpecificationWithoutAuth)
                        .when()
                        .queryParam("firstname", testData.getBooking().getFirstname())
                        .queryParam("lastname", testData.getBooking().getLastname())
                        .get("/booking")
                        .then()
                        .spec(responseSpecification200)
                        .extract().as(Bookingid[].class));

        step("Проверить данные в ответе", () -> {
            assertThat(responseData.length).isGreaterThan(0);
            List<Integer> bookingIds = Arrays.stream(responseData)
                    .map(Bookingid::getBookingid)
                    .collect(Collectors.toList());
            assertThat(bookingIds).containsAnyOf(testData.getBookingid());
        });
    }

    @Test
    @DisplayName("Successful with by checkin/checkout date returns the ids of all the bookings that exist within the API. Can take optional query strings to search and return a subset of booking ids.")
    public void successfulGetBookingIdsWithFilterCheckinCheckoutThenNotEmptyResultsTest() {
        Bookingid[] responseData = step("Отправить запрос на получение бронирований по дате заезда и выезда", () ->
                given(requestSpecificationWithoutAuth)
                        .when()
                        .queryParam("checkin", "2014-01-13")
                        .queryParam("checkout", "3014-12-31")
                        .get("/booking")
                        .then()
                        .spec(responseSpecification200)
                        .extract().as(Bookingid[].class));

        step("Проверить данные в ответе", () -> {
            assertThat(responseData.length).isGreaterThan(100);
            assertThat(responseData[0].getBookingid()).isNotZero();
        });
    }

    @Test
    @DisplayName("Unsuccessful with by checkin/checkout date returns the ids of all the bookings that exist within the API. Can take optional query strings to search and return a subset of booking ids.")
    public void unsuccessfulGetBookingIdsWithFilterCheckinCheckoutError500Test() {
        step("Отправить запрос на получение бронирований по дате заезда и выезда и проверка кода ответа", () ->
                given(requestSpecificationWithoutAuth)
                        .when()
                        .queryParam("checkin", "2014-00-13")
                        .queryParam("checkout", "3014-12-31")
                        .get("/booking")
                        .then()
                        .spec(responseSpecification500));
    }

    @Test
    @DisplayName("Unsuccessful returns a specific booking based upon the booking id provided")
    public void unsuccessfulGetBookingById404Test() {
        step("Отправить запрос на получение конкретного бронирования по идентификатору бронирования и проверка кода ответа", () ->
                given(requestSpecificationWithoutAuth)
                        .when()
                        .get("/booking/" + 0)
                        .then()
                        .spec(responseSpecification404));
    }

    @Test
    @DisplayName("Successful returns a specific booking based upon the booking id provided")
    public void successfulGetBookingByIdResult1Test() {
        GetBookingResponse testData = createBooking.successfulCreateBooking();

        GetBookingRequest responseData = step("Отправить запрос на получение конкретного бронирования по идентификатору бронирования", () ->
                given(requestSpecificationWithoutAuth)
                        .when()
                        .get("/booking/" + testData.getBookingid())
                        .then()
                        .spec(responseSpecification200)
                        .extract().as(GetBookingRequest.class));

        step("Проверить данные в ответе", () -> {
            assertThat(responseData.getFirstname()).isEqualTo("Jimbo");
            assertThat(responseData.getLastname()).isEqualTo("Bird");
            assertThat(responseData.getTotalprice()).isEqualTo(9548347);
            assertThat(responseData.getBookingdates().getCheckin()).isEqualTo("3025-12-31");
            assertThat(responseData.getBookingdates().getCheckout()).isEqualTo("5000-01-01");
            assertThat(responseData.isDepositpaid()).isEqualTo(false);
            assertThat(responseData.getAdditionalneeds()).isEqualTo("Breakfast, diner");
        });
    }
}