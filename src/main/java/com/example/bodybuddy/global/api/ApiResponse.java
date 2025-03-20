package com.example.bodybuddy.global.api;

import com.example.bodybuddy.global.api.code.error.ErrorCode;
import com.example.bodybuddy.global.api.code.success.CommonSuccessCode;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonPropertyOrder({"success", "code", "message", "data"})
public class ApiResponse<T> {

	private final boolean success;
	private final String code;
	private final String message;
	private final T data;

	public static <T> ApiResponse<T> success(T data) {
		return new ApiResponse<>(true, CommonSuccessCode.SUCCESS.getCode(),
			CommonSuccessCode.SUCCESS.getMessage(), data);
	}

	public static ApiResponse<Void> successWithNoData() {
		return new ApiResponse<>(true, CommonSuccessCode.SUCCESS.getCode(),
			CommonSuccessCode.SUCCESS.getMessage(), null);
	}

	public static <T> ApiResponse<T> created(T data) {
		return new ApiResponse<>(true, CommonSuccessCode.CREATED.getCode(),
			CommonSuccessCode.CREATED.getMessage(), data);
	}

	public static ApiResponse<Void> createdWithNoData() {
		return new ApiResponse<>(true, CommonSuccessCode.CREATED.getCode(),
			CommonSuccessCode.CREATED.getMessage(), null);
	}

	public static ApiResponse<?> createFail(ErrorCode errorCode) {
		return new ApiResponse<>(false, errorCode.getCode(), errorCode.getMessage(), null);
	}

	public static ApiResponse<?> createFail(ErrorCode errorCode, String message) {
		return new ApiResponse<>(false, errorCode.getCode(), message, null);
	}
}
