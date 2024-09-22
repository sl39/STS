package org.ex.back.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "ACCOUNT-001", "사용자를 찾을 수 없습니다."),
    HAS_ID(HttpStatus.BAD_REQUEST, "ACCOUNT-002", "동일한 ID가 이미 존재합니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "ACCOUNT-003", "비밀번호가 일치하지 않습니다."),

    CATEGORY_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "CATEGORY-001", "카테고리를 찾을 수 없습니다."),

    MenuNotFoundException(HttpStatus.NOT_FOUND, "MENU-001", "메뉴를 찾을 수 없습니다."),

    StoreNotFoundException(HttpStatus.NOT_FOUND, "STORE-001", "가게를 찾을 수 없습니다.");


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
