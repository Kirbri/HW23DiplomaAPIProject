package models.booking;

import lombok.Data;

@Data
public class GetBookingResponse {
    int bookingid;
    GetBookingRequest booking;
}
