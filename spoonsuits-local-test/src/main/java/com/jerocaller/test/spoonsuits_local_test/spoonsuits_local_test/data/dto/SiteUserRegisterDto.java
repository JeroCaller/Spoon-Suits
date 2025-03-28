package com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.data.dto;

import com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.validation.annotation.Phone;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 회원 가입을 위한 회원 정보 제출용 DTO 클래스
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SiteUserRegisterDto {

    @NotBlank(message = "아이디는 공백, null일 수 없습니다.")
    @Size(min = 5, max = 20, message = "아이디는 최소 5글자 이상, 최대 20글자 이하여야 합니다.")
    private String username;

    @Min(value = 20, message = "20세 이상만 가입 가능")
    @Max(value = 80, message = "80세 이하만 가입 가능")
    private Integer age;

    @Email
    private String email;

    @Phone
    private String phone;

}
