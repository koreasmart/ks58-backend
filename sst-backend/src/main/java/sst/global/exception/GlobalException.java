package sst.global.exception;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import sst.global.response.ApiResponse;

@RestControllerAdvice
@Slf4j
public class GlobalException {
	/**
	 * 유효성 검사(spring validation) 예외처리
	 * @param MethodArgumentNotValidException
	 * @param request
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<List<Map<String,String>>>> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
		String uri = request.getRequestURI();
		
		StackTraceElement[] stackTrace = ex.getStackTrace();
		StackTraceElement origin = stackTrace[0];
		log.error("[Exception] {}\n[Method]:{} ({}:{}) - message={}"
				  , origin.getClassName()
				  , origin.getMethodName()
				  , origin.getFileName()
				  , origin.getLineNumber()
				  , ex.getMessage()
		);
        // 1. ValidationError 리스트 생성
        List<Map<String,String>> errors = ex.getBindingResult().getFieldErrors().stream()
																                .map(fieldError -> Map.of(
																                							   "field", 	fieldError.getField()
																                							 , "message",	fieldError.getDefaultMessage()
																                ))
																                .collect(Collectors.toList());

        // 2. ApiResponse.errorWithData 사용
        ApiResponse<List<Map<String,String>>> response = ApiResponse.errorWithData(
                400,
                "입력값이 유효하지 않습니다.",
                uri,
                errors
        );

        return ResponseEntity.badRequest().body(response);
    }
	
	
	/**
	 * 인증 실패 예외 추가
	 * @param request
	 * @param AuthenticationException
	 */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccessDeniedException(HttpServletRequest request, AuthenticationException ex ) {
			String uri = request.getRequestURI();
					
			StackTraceElement[] stackTrace = ex.getStackTrace();
			StackTraceElement origin = stackTrace[0];
			log.error("[Exception] {}\n[Method]:{} ({}:{}) - message={}"
					  , origin.getClassName()
					  , origin.getMethodName()
					  , origin.getFileName()
					  , origin.getLineNumber()
					  , ex.getMessage()
			);
					
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                             .body(ApiResponse.error(ErrorCode.UNAUTHORIZED.status().value(), ErrorCode.UNAUTHORIZED.message(), uri));
    }
    
    
    /**
     * 권한 없음 예외 추가
     * @param request
     * @param AccessDeniedException
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccessDeniedException(HttpServletRequest request, AccessDeniedException ex ) {
    	String uri = request.getRequestURI();
    	
    	StackTraceElement[] stackTrace = ex.getStackTrace();
    	StackTraceElement origin = stackTrace[0];
    	log.error("[Exception] {}\n[Method]:{} ({}:{}) - message={}"
    			, origin.getClassName()
    			, origin.getMethodName()
    			, origin.getFileName()
    			, origin.getLineNumber()
    			, ex.getMessage()
    			);
    	
    	return ResponseEntity.status(HttpStatus.FORBIDDEN)
    			.body(ApiResponse.error(ErrorCode.FORBIDDEN.status().value(), ErrorCode.FORBIDDEN.message(), uri));
    }
	
	/**
	 * 사용자 예외처리
	 * @param CustomException
	 * @param request
	 */
	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ApiResponse<?>> handleValidationExceptions(CustomException ex, HttpServletRequest request) {
		
		String uri = request.getRequestURI();
		
		StackTraceElement[] stackTrace = ex.getStackTrace();
		StackTraceElement origin = stackTrace[0];
		log.error("[Exception] {}\n[Method]:{} ({}:{}) - message={}"
				  , origin.getClassName()
				  , origin.getMethodName()
				  , origin.getFileName()
				  , origin.getLineNumber()
				  , ex.getMessage()
		);
		ErrorCode errorCode = ex.getErrorCode();
		ApiResponse<String> response = ApiResponse.error(
				errorCode.status().value(),
				ex.getMessage(),
				uri
		);
		
		return ResponseEntity.status(errorCode.status().value()).body(response);
	}
	/**
	 * 전체 예외처리
	 * @param Exception
	 * @param request
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse<?>> handleValidationExceptions(Exception ex, HttpServletRequest request) {
		
		String uri = request.getRequestURI();
		
		StackTraceElement[] stackTrace = ex.getStackTrace();
		StackTraceElement origin = stackTrace[0];
		log.error("[Exception] {}\n[Method]:{} ({}:{}) - message={}"
				, origin.getClassName()
				, origin.getMethodName()
				, origin.getFileName()
				, origin.getLineNumber()
				, ex.getMessage()
				);
		
		ApiResponse<String> response = ApiResponse.error(
				500,
				"관리자에게 문의하세요",
				uri
				);
		
		return ResponseEntity.internalServerError().body(response);
	}

}
