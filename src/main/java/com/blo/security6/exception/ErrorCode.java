package com.blo.security6.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;


@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(999,"Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_EXISTED(101,"User already exists",HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(103,"Username already exists",HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(104,"User already exists",HttpStatus.BAD_REQUEST),
    NOTBANK(100,"The field cannot be left blank",HttpStatus.BAD_REQUEST),
    EMAIL(102,"Wrong email format",HttpStatus.BAD_REQUEST),
    UNAUTHENTICATION(106,"Unauthentication",HttpStatus.UNAUTHORIZED),
    USER_NOTEXITED(105,"User not found",HttpStatus.NOT_FOUND),
    UNAUTHORIZED(107,"YOU DO NOT PERMISSION",HttpStatus.FORBIDDEN)


            ;

    private int code;
    private String message;
    private HttpStatus httpStatus;


}
