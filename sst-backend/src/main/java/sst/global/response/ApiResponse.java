package sst.global.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;

/**
 * API 공통 응답 클래스
 * @param <T> 응답 데이터의 타입 (성공, 실패)
 */
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

	private final LocalDateTime timestamp;		// 서버 응답시간
	private final String status;				// 서버 응답 상태 (성공: success, 실패: error)
	private final int httpCode;					// HTTP 상태코드
	private final String message;				// 서버 응답 메시지
	private final String path;					// 서버 요청 주소
	private final T data;						// 서버 응답 데이터
	
	private ApiResponse(String status, int httpCode, String message, String path, T data) {
		this.timestamp = LocalDateTime.now();	// 서버 응답 시간 자동 할당
		this.status = status;
		this.httpCode = httpCode;
		this.message = message;
		this.path = path;
		this.data = data;
	}
	
	/*===========================
	 * 성공 응답
	 *=========================== */
	
	/**
	 * 성공 응답 기본 메소드
	 */
	public static <T> ApiResponse<T> success(){
		return new ApiResponse<T>("success", 200, "ok", null, null);
	}
	
	/**
	 * 성공 응답(데이터) 메소드
	 */
	public static <T> ApiResponse<T> success(T data){
		return new ApiResponse<T>("success", 200, "ok", null, data);
	}
	
	/**
	 *  성공 응답(생성) 기본 메소드
	 */
	public static <T> ApiResponse<T> created(){
		return new ApiResponse<T>("success", 201, "ok", null, null);
	}
	
	/**
	 *  성공 응답(생성, 데이터) 기본 메소드	
	 */
	public static <T> ApiResponse<T> created(T data){
		return new ApiResponse<T>("success", 201, "ok", null, data);
	}
	
	/*===========================
	 * 실패 응답
	 *=========================== */
	/**
	 * 실패시 응답 작성 요청
	 * ApiResponse.error(ErrorCode.INTERNAL_SERVER_ERROR.code, ErrorCode.INTERNAL_SERVER_ERROR.message, RequestURI)
	 * @param httpCode		// 에러응답 상태코드
	 * @param message		// 에러응답 메시지
	 * @param path			// 요청 주소
	 */
	public static <T> ApiResponse<T> error(int httpCode, String message, String path){
		return new ApiResponse<T>("error", httpCode, message, path, null);
	}
	
	/**
	 * 실패시 응답(데이터) 작성 요청
	 * ApiResponse.error(ErrorCode.INTERNAL_SERVER_ERROR.code, ErrorCode.INTERNAL_SERVER_ERROR.message, RequestURI, Data)
	 * @param httpCode 		// 에러응답 상태코드
	 * @param message		// 에러응답 메시지
	 * @param path			// 요청 주소
	 * @param data			// 에러응답시 데이터
	 */
	public static <T> ApiResponse<T> errorWithData(int httpCode, String message, String path, T data){
		return new ApiResponse<T>("error", httpCode, message, path, data);
	}
}


















