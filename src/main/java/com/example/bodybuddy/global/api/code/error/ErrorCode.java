package com.example.bodybuddy.global.api.code.error;

import org.springframework.http.HttpStatus;

public interface ErrorCode {

	HttpStatus getHttpStatus();

	String getCode();

	String getMessage();
}
