package com.along.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @Description: 自定义validation校验注解，枚举校验，只能用在String类型字段上
 * @Author along
 * @Date 2019/1/21 15:30
 */
@Documented
@Constraint(validatedBy = {EnumValidator.class})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
public @interface Enum {

    //有效值有多个使用“,”隔开
    String values();

    //提示内容
    String message() default "flag不存在";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
