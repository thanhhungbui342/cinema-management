package com.thanhhungbui342.cinema.dto.respone;

import java.math.BigDecimal;

import com.thanhhungbui342.cinema.entity.Seat.SeatStatus;
import com.thanhhungbui342.cinema.entity.SeatType;

public record ShowtimeSeatResponse(
    Long showtimeSeatId,
    Long seatId,
    String seatRow,
    Integer seatColumn,
    SeatType seatType,
    BigDecimal price,
    SeatStatus status
) {

}
