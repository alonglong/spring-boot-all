package com.along.annotation;

import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.invoke.MethodHandles;

/**
 * @Description: 长度校验处理类，根据字节数校验，utf-8编码下一个汉字为3个字节，gbk编码下一个汉字为2个字节
 * @Author along
 * @Date 2019/1/21 15:37
 */
public class ByteLengthValidator implements ConstraintValidator<ByteLength, String> {

    private static final Log LOG = LoggerFactory.make( MethodHandles.lookup() );

    private int min;
    private int max;

    @Override
    public void initialize(ByteLength byteLength) {
        this.max = byteLength.max();
        this.min = byteLength.min();
        validateParameters();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {

        if ( s == null ) {
            return true;
        }
        int length = s.getBytes().length;
        return length >= min && length <= max;
    }

    private void validateParameters() {
        if ( min < 0 ) {
            throw LOG.getMinCannotBeNegativeException();
        }
        if ( max < 0 ) {
            throw LOG.getMaxCannotBeNegativeException();
        }
        if ( max < min ) {
            throw LOG.getLengthCannotBeNegativeException();
        }
    }


}
