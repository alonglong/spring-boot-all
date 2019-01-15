package com.along.controller;

import com.along.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @Description: controller
 * @Author along
 * @Date 2018/12/28 18:02
 */
@RestController
@RequestMapping("person")
public class PersonController {

    private final Logger logger = LoggerFactory.getLogger(PersonController.class);

    private PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("export")
    public String exportPersons() throws Exception {
        logger.info("导出[全量用户信息]");
        String fileName = personService.exportPersons();
        logger.info("导出[全量用户信息] 成功 FileName : {} ", fileName);
        //接下来就是将fileName组装成ftp服务器的下载地址传回前端，前端再请求下载地址下载excel文件
        String tmp = getUrlCode(fileName);
        logger.info("组装后的文件名称: {}", tmp);
        return tmp;

    }

    private String getUrlCode(String fileName) throws UnsupportedEncodingException {
        String tmp = fileName.replace("export/", "");
        String[] fs = tmp.split("\\^");
        tmp = fs[0] + fs[2];
        tmp = "servletExportUtil/" + "export" + File.separator + URLEncoder.encode(tmp, "utf-8")
                + "?timeId=" + fs[1];
        return tmp;
    }


}
