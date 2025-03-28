package com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.validation.annotation;

import com.jerocaller.test.spoonsuits_local_test.spoonsuits_local_test.validation.validator.PhoneValidator;
import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneValidator.class)
public @interface Phone {
    String message() default "전화번호 형식과 일치하지 않습니다. 01x-xxx(x)-xxxx 형태여야 합니다.";
    Class[] groups() default {};
    Class[] payload() default {};
}
