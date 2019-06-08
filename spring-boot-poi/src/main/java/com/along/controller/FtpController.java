package com.along.controller;

import com.along.common.util.sftp.SFtpUtil;
import com.along.config.ftp.FtpConfig;
import com.along.config.sftp.SftpConfig;
import org.apache.commons.net.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @Description: ftp上传下载接口
 * @Author along
 * @Date 2019/4/11 18:03
 */
@RestController
@RequestMapping("/ftp")
public class FtpController {

    private final Logger logger = LoggerFactory.getLogger(FtpController.class);

    private final FtpConfig ftpConfig;

    private final SftpConfig sftpConfig;

    @Autowired
    public FtpController(FtpConfig ftpConfig, SftpConfig sftpConfig) {
        this.ftpConfig = ftpConfig;
        this.sftpConfig = sftpConfig;

    }

    @GetMapping("/test")
    public String helloWorld() {
        return "hello";
    }


    /**
     * ftp 文件下载
     *
     * @param request
     * @param response
     */
    @GetMapping("/download/**") // 请求样例：http://localhost:8080/ftp/download/export/三级表头Excel_用户信息_20190222.xlsx?timeId=20190222171649
    public void download(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userAgent = request.getHeader("user-agent").toLowerCase();
        logger.info("头文件: {}", userAgent);
        String url = request.getRequestURI(); // /ftp/download/export/%E4%B8%89%E7%BA%A7%E8%A1%A8%E5%A4%B4Excel_%E7%94%A8%E6%88%B7%E4%BF%A1%E6%81%AF_20190222.xlsx
        logger.info("下载的url:{}", url);
        String timeId = request.getParameter("timeId"); // 20190222171649
        logger.info("timeId: {}", timeId);
        // 从新组装正确的文件名
        String fileNameDecode = url.substring(url.lastIndexOf('/') + 1); // 三级表头Excel_用户信息_20190222^20190222171649
        fileNameDecode = URLDecoder.decode(fileNameDecode, "utf-8");
        String[] fs = fileNameDecode.split("\\.");
        String realFileName = fs[0] + "^" + timeId + "^." + fs[1];
        if (timeId == null || "".equals(timeId)) {
            realFileName = fileNameDecode;
        }
        logger.info("需要下载的文件名称: {}", realFileName); // 三级表头Excel_用户信息_20190222^20190222171649^.xlsx

        logger.info("ContextPath : {}, ServletPath : {}, PathInfo : {}", request.getContextPath(), request.getServletPath(), request.getPathInfo());
        // TODO 这里写的不优雅，怎么更简单地得到“**”匹配的url路径呢？
        String dir = url.substring(url.indexOf("ftp/download") + 12, url.lastIndexOf('/') + 1);
        logger.info("需要下载的文件路径: {}", dir); // /export/

        // 根据不同浏览器编码文件名，反正浏览器下载的文件乱码
        if (userAgent.contains("msie") || userAgent.contains("like gecko")) {
            // IE
            logger.info("ie: {}", userAgent);
            fileNameDecode = URLEncoder.encode(fileNameDecode, "UTF-8");
        } else if (userAgent.contains("firefox")) {
            logger.info("fireFox: {}", userAgent);
            fileNameDecode = "=?UTF-8?B?" + (new String(Base64.encodeBase64(fileNameDecode.getBytes(StandardCharsets.UTF_8)))) + "?=";
        } else {
            // 非IE
            logger.info("非IE: {}", userAgent);
            fileNameDecode = new String(fileNameDecode.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        }

        // 设置响应类型
        response.setContentType("application/force-download");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileNameDecode); // 设置文件名
        // 把本地文件发送给客户端
        OutputStream out = response.getOutputStream();
        // 可能服务器是SentOs7，FTPClient连接不上，改用SFTP连接
        // boolean result = FtpUtil.downloadByConfig(ftpConfig, dir, realFileName, out);
        boolean result = SFtpUtil.down2Client(sftpConfig, dir, realFileName, out);
        logger.info("文件处理完成: {}", result);

        out.flush();
        out.close();
    }
}
