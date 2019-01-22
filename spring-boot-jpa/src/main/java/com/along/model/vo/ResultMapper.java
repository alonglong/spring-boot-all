package com.along.model.vo;

/**
 * @Description: 返回结果包装类
 * @Author along
 * @Date 2019/1/9 15:47
 */
public class ResultMapper<DataType> {

    private Integer code;
    private String msg;
    private DataType data;


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataType getData() {
        return data;
    }

    public void setData(DataType data) {
        this.data = data;
    }
}
