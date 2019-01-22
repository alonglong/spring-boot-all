package com.along.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @Description: 枚举校验处理类
 * @Author along
 * @Date 2019/1/21 15:34
 */
public class EnumValidator implements ConstraintValidator<Enum, Object> {

    //临时变量保存values值列表
    private String values;

    //初始化values的值
    @Override
    public void initialize(Enum anEnum) {
        this.values = anEnum.values();
    }

    //实现认证
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {

        if(value == null){
            return true;
        }

        //分割定义的有效值
        String[] value_array = values.split(",");
        boolean isTrue = false;
        //遍历对比有效值
        for (String s : value_array){
            //存在一致就跳出循环，返回true
            if(s.equals(value)){
                isTrue = true;
                break;
            }
        }
        //返回是否存在
        return isTrue;
    }
}
