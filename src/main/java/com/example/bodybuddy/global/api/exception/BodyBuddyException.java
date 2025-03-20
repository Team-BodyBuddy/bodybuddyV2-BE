package com.example.bodybuddy.global.api.exception;

import com.example.bodybuddy.global.api.code.error.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BodyBuddyException extends RuntimeException {
	private final ErrorCode errorCode;
}
