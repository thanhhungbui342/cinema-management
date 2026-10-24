package com.thanhhungbui342.cinema.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter 
public enum ErrorCode {
    // 1xxx: System & Validation
    UNCATEGORIZED_EXCEPTION(9999, "Lỗi hệ thống chưa phân loại", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEYWORD(1001, "Từ khóa tìm kiếm không hợp lệ", HttpStatus.BAD_REQUEST),
    
    // Validation Keys (Dùng cho Request DTO)
    MOVIE_TITLE_BLANK(1002, "Tên phim không được để trống", HttpStatus.BAD_REQUEST),
    MOVIE_DURATION_INVALID(1003, "Thời lượng phim phải là số nguyên dương", HttpStatus.BAD_REQUEST),
    GENRE_IDS_INVALID(1004, "Danh sách thể loại không hợp lệ", HttpStatus.BAD_REQUEST),

    // 2xxx: Movie Module
    MOVIE_NOT_FOUND(2001, "Không tìm thấy phim yêu cầu", HttpStatus.NOT_FOUND),
    GENRE_NOT_FOUND(2002, "Một hoặc nhiều thể loại phim không tồn tại", HttpStatus.NOT_FOUND),

    // 3xxx: Booking Module
    BOOKING_CONFLICT(3001, "Xung đột lịch đặt vé", HttpStatus.CONFLICT);

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;

    ErrorCode(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
