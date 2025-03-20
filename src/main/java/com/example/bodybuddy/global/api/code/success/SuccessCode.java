package com.example.bodybuddy.global.api.code.success;

import org.springframework.http.HttpStatus;

public interface SuccessCode {

	HttpStatus getHttpStatus();

	String getCode();

	String getMessage();
}
