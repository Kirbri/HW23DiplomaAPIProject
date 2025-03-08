package api;

import io.qameta.allure.Step;
import models.booking.BookingDates;
import models.booking.GetBookingRequest;
import models.booking.GetBookingResponse;

import static io.restassured.RestAssured.given;
import static specs.GeneralSpec.requestSpecificationWithAuth;
import static specs.GeneralSpec.responseSpecification200;

public class CreateBooking {

    @Step("Создание бронирования со всеми заполненными полями")
    public GetBookingResponse successfulCreateBooking() {
        BookingDates bookingDates = new BookingDates();
        bookingDates.setCheckin("3025-12-31");
        bookingDates.setCheckout("5000-01-01");

        GetBookingRequest data = new GetBookingRequest();
        data.setFirstname("Jimbo");
        data.setLastname("Bird");
        data.setTotalprice(9_548_347);
        data.setDepositpaid(false);
        data.setBookingdates(bookingDates);
        data.setAdditionalneeds("Breakfast, diner");

        GetBookingResponse responseData = given(requestSpecificationWithAuth)
                .body(data)
                .when()
                .post("/booking")
                .then()
                .spec(responseSpecification200)
                .extract().as(GetBookingResponse.class);

        return responseData;
    }

    @Step("Данные для создания бронирования со всеми заполненными полями")
    public GetBookingRequest successfulDataForCreateBooking() {
        BookingDates bookingDates = new BookingDates();
        bookingDates.setCheckin("1025-01-01");
        bookingDates.setCheckout("1050-12-31");

        GetBookingRequest data = new GetBookingRequest();
        data.setFirstname("Update_Jimbo");
        data.setLastname("Update_Bird");
        data.setTotalprice(8_457_923);
        data.setDepositpaid(true);
        data.setBookingdates(bookingDates);
        data.setAdditionalneeds("No");

        return data;
    }
}
