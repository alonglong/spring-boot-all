package com.along.service;

import java.io.IOException;

/**
 * @Description: service接口
 * @Author along
 * @Date 2018/12/28 17:42
 */
public interface PersonService {


    /**
     * 全量数据导出
     */
    String exportPersons() throws IOException;


}
