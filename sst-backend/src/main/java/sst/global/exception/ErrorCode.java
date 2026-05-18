package sst.global.exception;

import org.springframework.http.HttpStatus;

/**
 *  공통 API 에러 코드 정의
 */
public enum ErrorCode {

	/* ==================== 4XX CLIENT ERROR ==================== */

	// 400 Bad Request
	/** 잘못된 요청 (일반 fallback) */
	BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),

	/** 입력값이 유효하지 않음 */
	INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "입력값이 올바르지 않습니다."),

	/** 필수 입력 필드 누락 */
	MISSING_REQUIRED_FIELD(HttpStatus.BAD_REQUEST, "필수 입력값이 누락되었습니다."),

	/** 입력 타입 불일치 */
	INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST, "입력 타입이 올바르지 않습니다."),

	/** 입력 형식 오류 */
	INVALID_FORMAT(HttpStatus.BAD_REQUEST, "입력 형식이 올바르지 않습니다."),

	/** 전화번호 형식 오류 (예: 010-1234-5678) */
	INVALID_PHONE_NUMBER(HttpStatus.BAD_REQUEST, "전화번호 형식이 올바르지 않습니다."),

	/** 이메일 형식 오류 */
	INVALID_EMAIL_FORMAT(HttpStatus.BAD_REQUEST, "이메일 형식이 올바르지 않습니다."),

	/** 비밀번호 형식 오류 (길이, 특수문자 등) */
	INVALID_PASSWORD_FORMAT(HttpStatus.BAD_REQUEST, "비밀번호 형식이 올바르지 않습니다."),

	/** 비밀번호 확인 불일치 */
	PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),

	/** 지원하지 않는 파일 확장자 */
	INVALID_FILE_TYPE(HttpStatus.BAD_REQUEST, "지원하지 않는 파일 형식입니다."),

	/** 파일 크기 제한 초과 */
	FILE_SIZE_EXCEEDED(HttpStatus.BAD_REQUEST, "파일 크기가 제한을 초과했습니다."),

	// 401 Unauthorized
	/** 인증 필요 (일반 fallback) */
	UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),

	/** 유효하지 않은 JWT 토큰 */
	INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),

	/** 만료된 JWT 토큰 */
	EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),

	/** 토큰이 요청 헤더에 없음 */
	TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "토큰이 존재하지 않습니다."),

	/** 로그인 실패 (아이디 또는 비밀번호 불일치) */
	INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 일치하지 않습니다."),

	// 403 Forbidden
	/** 접근 권한 없음 (일반 fallback) */
	FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),

	/** 리소스 소유자가 아님 (본인 글만 수정/삭제 가능) */
	NOT_RESOURCE_OWNER(HttpStatus.FORBIDDEN, "본인의 리소스만 수정/삭제할 수 있습니다."),

	/** 정지된 계정 (신고 등으로 인한 제재) */
	ACCOUNT_SUSPENDED(HttpStatus.FORBIDDEN, "정지된 계정입니다. 관리자에게 문의하세요."),

	/** 잠긴 계정 (로그인 시도 횟수 초과 등) */
	ACCOUNT_LOCKED(HttpStatus.FORBIDDEN, "잠긴 계정입니다."),

	/** 권한 부족 */
	INSUFFICIENT_PERMISSION(HttpStatus.FORBIDDEN, "권한이 부족합니다."),

	/** 관리자 전용 기능 */
	ADMIN_ONLY(HttpStatus.FORBIDDEN, "관리자만 접근할 수 있습니다."),

	// 404 Not Found
	/** 리소스를 찾을 수 없음 (일반 fallback) */
	NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 리소스를 찾을 수 없습니다."),

	/** 사용자 정보 없음 */
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."),

	/** 게시글 정보 없음 */
	POST_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 게시글입니다."),

	/** 댓글 정보 없음 */
	COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 댓글입니다."),

	/** 답글 정보 없음 */
	REPLY_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 답글입니다."),

	/** 파일 정보 없음 */
	FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "파일을 찾을 수 없습니다."),

	/** 쪽지 정보 없음 */
	MESSAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 쪽지입니다."),

	/** 일정 정보 없음 */
	SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 일정입니다."),

	/** 신청 정보 없음 */
	APPLICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 신청 정보입니다."),

	// 405 Method Not Allowed
	/** 허용되지 않은 HTTP 메서드 */
	METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "허용되지 않은 HTTP 메서드입니다."),

	// 408 Request Timeout
	/** 요청 처리 시간 초과 */
	REQUEST_TIMEOUT(HttpStatus.REQUEST_TIMEOUT, "요청 시간이 초과되었습니다."),

	// 409 Conflict
	/** 리소스 상태 충돌 (일반 fallback) */
	CONFLICT(HttpStatus.CONFLICT, "리소스 충돌이 발생했습니다."),

	/** 중복된 아이디 */
	CONFLICT_ID(HttpStatus.CONFLICT, "사용할 수 없는 아이디입니다. 다른 아이디를 입력해 주세요."),

	/** 중복된 이메일 */
	DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다."),

	/** 중복된 닉네임 */
	DUPLICATE_NICKNAME(HttpStatus.CONFLICT, "이미 사용 중인 닉네임입니다."),

	/** 중복된 전화번호 */
	DUPLICATE_PHONE_NUMBER(HttpStatus.CONFLICT, "이미 등록된 전화번호입니다."),

	/** 이미 신청 완료 */
	ALREADY_APPLIED(HttpStatus.CONFLICT, "이미 신청하였습니다."),

	/** 이미 좋아요 완료 */
	ALREADY_LIKED(HttpStatus.CONFLICT, "이미 좋아요를 누른 게시글입니다."),

	/** 이미 처리된 요청 (중복 처리 방지) */
	ALREADY_PROCESSED(HttpStatus.CONFLICT, "이미 처리된 요청입니다."),

	// 410 Gone
	/** 영구 삭제된 리소스 */
	DELETED_RESOURCE(HttpStatus.GONE, "삭제된 리소스입니다."),

	/** 탈퇴한 사용자 */
	WITHDRAWN_USER(HttpStatus.GONE, "탈퇴한 사용자입니다."),

	/** 마감된 게시글 */
	CLOSED_POST(HttpStatus.GONE, "마감된 게시글입니다."),
	
	/** 중복 신고 */
	ALREADY_REPORTED(HttpStatus.CONFLICT, "이미 신고한 대상입니다."),

	// 413 Payload Too Large
	/** 요청 본문 크기 초과 */
	PAYLOAD_TOO_LARGE(HttpStatus.PAYLOAD_TOO_LARGE, "요청 데이터가 너무 큽니다."),

	// 415 Unsupported Media Type
	/** 지원하지 않는 Content-Type */
	UNSUPPORTED_MEDIA_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "지원하지 않는 미디어 타입입니다."),

	// 422 Unprocessable Entity
	/** 의미상 오류가 있는 요청 (일반 fallback) */
	UNPROCESSABLE_ENTITY(HttpStatus.UNPROCESSABLE_ENTITY, "처리할 수 없는 요청입니다."),

	/** 날짜 범위 오류 (시작일 > 종료일) */
	INVALID_DATE_RANGE(HttpStatus.UNPROCESSABLE_ENTITY, "날짜 범위가 올바르지 않습니다."),

	/** 시간 범위 오류 (시작 시간 > 종료 시간) */
	INVALID_TIME_RANGE(HttpStatus.UNPROCESSABLE_ENTITY, "시간 범위가 올바르지 않습니다."),

	/** 과거 날짜 선택 불가 */
	PAST_DATE_NOT_ALLOWED(HttpStatus.UNPROCESSABLE_ENTITY, "과거 날짜는 선택할 수 없습니다."),

	/** 최대 수용 인원 초과 */
	MAX_CAPACITY_EXCEEDED(HttpStatus.UNPROCESSABLE_ENTITY, "최대 인원을 초과했습니다."),

	/** 본인 게시글 신청 불가 */
	CANNOT_APPLY_OWN_POST(HttpStatus.UNPROCESSABLE_ENTITY, "본인의 게시글에는 신청할 수 없습니다."),

	/** 승인 후 수정 불가 */
	CANNOT_MODIFY_AFTER_APPROVAL(HttpStatus.UNPROCESSABLE_ENTITY, "승인 후에는 수정할 수 없습니다."),

	/** 잘못된 상태 전환 */
	INVALID_STATUS_TRANSITION(HttpStatus.UNPROCESSABLE_ENTITY, "잘못된 상태 전환입니다."),

	// 423 Locked
	/** 리소스가 잠김 (동시 수정 방지) */
	RESOURCE_LOCKED(HttpStatus.LOCKED, "다른 사용자가 수정 중인 리소스입니다."),

	// 429 Too Many Requests
	/** 요청 횟수 제한 초과 */
	TOO_MANY_REQUESTS(HttpStatus.TOO_MANY_REQUESTS, "요청이 너무 많습니다. 잠시 후 다시 시도해주세요."),

	/** 로그인 시도 횟수 초과 */
	TOO_MANY_LOGIN_ATTEMPTS(HttpStatus.TOO_MANY_REQUESTS, "로그인 시도 횟수를 초과했습니다. 잠시 후 다시 시도해주세요."),

	/* ==================== 5XX SERVER ERROR ==================== */

	// 500 Internal Server Error
	/** 서버 내부 오류 (일반 fallback) */
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다. 관리자에게 문의하세요."),

	/** 데이터베이스 오류 */
	DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "데이터베이스 오류가 발생했습니다."),

	/** DB 연결 실패 */
	DATABASE_CONNECTION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "데이터베이스 연결에 실패했습니다."),

	/** 트랜잭션 처리 오류 */
	TRANSACTION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "트랜잭션 처리 중 오류가 발생했습니다."),

	/** 파일 업로드 실패 */
	FILE_UPLOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드 중 오류가 발생했습니다."),

	/** 파일 삭제 실패 */
	FILE_DELETE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "파일 삭제 중 오류가 발생했습니다."),

	/** 파일 읽기 실패 */
	FILE_READ_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "파일 읽기 중 오류가 발생했습니다."),

	/** 이미지 처리 실패 (리사이징, 압축 등) */
	IMAGE_PROCESSING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 처리 중 오류가 발생했습니다."),

	/** 이메일 전송 실패 */
	EMAIL_SEND_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "이메일 전송 중 오류가 발생했습니다."),

	/** SMS 전송 실패 */
	SMS_SEND_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "SMS 전송 중 오류가 발생했습니다."),

	/** 암호화 처리 실패 */
	ENCRYPTION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "암호화 처리 중 오류가 발생했습니다."),

	/** 복호화 처리 실패 */
	DECRYPTION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "복호화 처리 중 오류가 발생했습니다."),

	/** JSON 파싱 실패 */
	JSON_PARSING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "JSON 파싱 중 오류가 발생했습니다."),

	/** 데이터 타입 변환 실패 */
	DATA_CONVERSION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "데이터 변환 중 오류가 발생했습니다."),

	// 502 Bad Gateway
	/** 게이트웨이 오류 */
	BAD_GATEWAY(HttpStatus.BAD_GATEWAY, "게이트웨이 오류가 발생했습니다."),

	/** 외부 API 호출 실패 */
	EXTERNAL_API_ERROR(HttpStatus.BAD_GATEWAY, "외부 API 호출 중 오류가 발생했습니다."),

	/** 지도 API 오류 */
	MAP_API_ERROR(HttpStatus.BAD_GATEWAY, "지도 API 호출 중 오류가 발생했습니다."),

	// 503 Service Unavailable
	/** 서비스 일시 중단 */
	SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "서비스를 일시적으로 사용할 수 없습니다."),

	/** 서비스 점검 중 */
	UNDER_MAINTENANCE(HttpStatus.SERVICE_UNAVAILABLE, "서비스 점검 중입니다."),

	/** DB 서버 다운 */
	DATABASE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "데이터베이스에 연결할 수 없습니다."),

	// 504 Gateway Timeout
	/** 게이트웨이 타임아웃 */
	GATEWAY_TIMEOUT(HttpStatus.GATEWAY_TIMEOUT, "게이트웨이 시간 초과입니다."),

	/** 외부 API 응답 지연 */
	EXTERNAL_API_TIMEOUT(HttpStatus.GATEWAY_TIMEOUT, "외부 API 응답 시간이 초과되었습니다.");

	private final HttpStatus status;
	private final String message;

	ErrorCode(HttpStatus status, String message) {
		this.status = status;
		this.message = message;
	}

	/**
	 * HTTP 상태 코드를 반환
	 */
	public HttpStatus status() {
		return status;
	}

	/**
	 * 에러 메시지를 반환
	 */
	public String message() {
		return message;
	}

	/**
	 * HTTP 상태 코드 숫자를 반환
	 */
	public int code() {
		return status.value();
	}
}