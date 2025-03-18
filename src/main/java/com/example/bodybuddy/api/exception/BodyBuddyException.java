package com.example.bodybuddy.api.exception;

import com.example.bodybuddy.api.code.error.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BodyBuddyException extends RuntimeException {
	private final ErrorCode errorCode;
}
