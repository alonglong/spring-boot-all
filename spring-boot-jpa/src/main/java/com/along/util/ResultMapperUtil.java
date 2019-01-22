package com.along.util;

import com.along.model.vo.ResultMapper;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author along
 * @Date 2019/1/9 15:47
 */
public class ResultMapperUtil {
    public static <T> ResultMapper<T> success(T object, String msg) {
        ResultMapper<T> rm = new ResultMapper<>();
        rm.setData(object);
        rm.setCode(200);
        rm.setMsg(msg);
        return rm;
    }

    public static <T> ResultMapper<T> success(T object) {
        ResultMapper<T> rm = new ResultMapper<>();
        rm.setData(object);
        rm.setCode(200);
        rm.setMsg("success");
        return rm;
    }

    public static <T> ResultMapper<T> success() {
        return success(null);
    }

    public static <T> ResultMapper<T> error(Integer code, String msg) {
        ResultMapper<T> rm = new ResultMapper<>();
        rm.setCode(code);
        rm.setMsg(msg);
        return rm;
    }

    public static <T> ResultMapper<T> error(Integer code, String msg, T object) {
        ResultMapper<T> rm = new ResultMapper<>();
        rm.setCode(code);
        rm.setMsg(msg);
        rm.setData(object);
        return rm;
    }
}
