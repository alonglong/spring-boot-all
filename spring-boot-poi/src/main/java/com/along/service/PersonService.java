package com.along.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @Description: service接口
 * @Author along
 * @Date 2018/12/28 17:42
 */
public interface PersonService {


    /**
     * 全量数据导出
     */
    String exportPersons() throws Exception;


    /**
     * excel文件解析，批量导入数据库
     *
     * @param fileName
     * @param file
     * @return
     */
    boolean batchImport(String fileName, MultipartFile file) throws Exception;
}
