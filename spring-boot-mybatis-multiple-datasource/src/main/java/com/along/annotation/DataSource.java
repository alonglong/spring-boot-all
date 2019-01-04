package com.along.annotation;

import com.along.common.ContextConst;
import java.lang.annotation.*;

/**
 * 切换数据源的注解
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSource {

    ContextConst.DataSourceType value() default ContextConst.DataSourceType.PRIMARY;

}