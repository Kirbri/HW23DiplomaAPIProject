package tests.booking;

import models.booking.BookingDates;
import models.booking.GetBookingRequest;
import models.booking.GetBookingResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.TestBase;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.GeneralSpec.*;

@DisplayName("Проверка создания бронирования")
@Tag("booking_api")
public class PostTests extends TestBase {

    @Test
    @DisplayName("Successful creates a new booking in the API")
    public void successfulPostCreateBookingTest() {
        GetBookingRequest requestData = step("Подготовить данные для создания бронирования", () -> {
            BookingDates bookingDates = new BookingDates();
            bookingDates.setCheckin("2025-01-01");
            bookingDates.setCheckout("2026-01-01");

            GetBookingRequest data = new GetBookingRequest();
            data.setFirstname("Jim");
            data.setLastname("Brown");
            data.setTotalprice(111);
            data.setDepositpaid(true);
            data.setBookingdates(bookingDates);
            data.setAdditionalneeds("Breakfast");
            return data;
        });

        GetBookingResponse responseData = step("Отправить запрос на создание нового бронирования", () ->
                given(requestSpecificationWithoutAuth)
                        .body(requestData)
                        .when()
                        .post("/booking")
                        .then()
                        .spec(responseSpecification200)
                        .extract().as(GetBookingResponse.class));

        step("Проверить данные в ответе", () -> {
            assertThat(responseData.getBookingid()).isNotEqualTo(0);
            assertThat(responseData.getBooking().getFirstname()).isEqualTo(requestData.getFirstname());
            assertThat(responseData.getBooking().getLastname()).isEqualTo(requestData.getLastname());
            assertThat(responseData.getBooking().getTotalprice()).isEqualTo(requestData.getTotalprice());
            assertThat(responseData.getBooking().isDepositpaid()).isEqualTo(requestData.isDepositpaid());
            assertThat(responseData.getBooking().getBookingdates().getCheckin()).isEqualTo(requestData.getBookingdates().getCheckin());
            assertThat(responseData.getBooking().getBookingdates().getCheckout()).isEqualTo(requestData.getBookingdates().getCheckout());
            assertThat(responseData.getBooking().getAdditionalneeds()).isEqualTo(requestData.getAdditionalneeds());
        });
    }

    @Test
    @DisplayName("Successful creates a new booking in the API")
    public void successfulPostCreateBookingWithEmptyFieldsTest() {
        GetBookingRequest requestData = step("Подготовить данные для создания бронирования", () -> {
            BookingDates bookingDates = new BookingDates();
            bookingDates.setCheckin("");
            bookingDates.setCheckout("");

            GetBookingRequest data = new GetBookingRequest();
            data.setFirstname("");
            data.setLastname("");
            data.setBookingdates(bookingDates);
            data.setAdditionalneeds("");
            return data;
        });

        GetBookingResponse responseData = step("Отправить запрос на создание нового бронирования", () ->
                given(requestSpecificationWithoutAuth)
                        .body(requestData)
                        .when()
                        .post("/booking")
                        .then()
                        .spec(responseSpecification200)
                        .extract().as(GetBookingResponse.class));

        step("Проверить данные в ответе", () -> {
            assertThat(responseData.getBookingid()).isNotEqualTo(0);
            assertThat(responseData.getBooking().getFirstname()).isEmpty();
            assertThat(responseData.getBooking().getLastname()).isEmpty();
            assertThat(responseData.getBooking().getTotalprice()).isEqualTo(requestData.getTotalprice());
            assertThat(responseData.getBooking().isDepositpaid()).isEqualTo(requestData.isDepositpaid());
            assertThat(responseData.getBooking().getBookingdates().getCheckin()).isEqualTo("0NaN-aN-aN");
            assertThat(responseData.getBooking().getBookingdates().getCheckout()).isEqualTo("0NaN-aN-aN");
            assertThat(responseData.getBooking().getAdditionalneeds()).isEmpty();
        });
    }

    @Test
    @DisplayName("Unsuccessful creates a new booking in the API")
    public void unsuccessfulPostCreateBooking500Test() {
        GetBookingRequest requestData = step("Подготовить данные для создания бронирования", () -> {
            BookingDates bookingDates = new BookingDates();

            GetBookingRequest data = new GetBookingRequest();
            data.setBookingdates(bookingDates);
            return data;
        });

        step("Отправить запрос на создание нового бронирования и проверить код ответа", () ->
                given(requestSpecificationWithoutAuth)
                        .body(requestData)
                        .when()
                        .post("/booking")
                        .then()
                        .spec(responseSpecification500));
    }
}
