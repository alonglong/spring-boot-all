package com.along.controller;

import com.along.common.util.FtpUtil;
import com.along.config.ftp.FtpConfig;
import org.apache.commons.net.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * @Description: ftp上传下载接口
 * @Author along
 * @Date 2019/4/11 18:03
 */
@Controller
public class FtpController {

    private final Logger logger = LoggerFactory.getLogger(FtpController.class);

    @Autowired
    private FtpConfig ftpConfig;


    /**
     * ftp 文件下载
     *
     * @param request
     * @param response
     */
    @GetMapping("/ftp/download")
    public void download(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userAgent = request.getHeader("user-agent").toLowerCase();
        logger.info("头文件: {}", userAgent);
        String url = request.getRequestURI();
        logger.info("下载的url:{}", url);
        String timeId = request.getParameter("timeId");
        logger.info("timeId: {}", timeId);
        String fileNameDecode = url.substring(url.lastIndexOf('/') + 1);
        fileNameDecode = URLDecoder.decode(fileNameDecode, "utf-8");

        String[] fs = fileNameDecode.split("\\.");
        String realFileName = fs[0] + "^" + timeId + "^." + fs[1];
        if (timeId == null || "".equals(timeId)) {
            realFileName = fileNameDecode;
        }
        logger.info("需要下载的文件名称: {}", realFileName);

        logger.info("ContextPath : {}, ServletPath : {}, PathInfo : {}", request.getContextPath(),
                request.getServletPath(), request.getPathInfo());
        String dir = request.getPathInfo().substring(request.getPathInfo().indexOf('/'),
                request.getPathInfo().lastIndexOf('/') + 1);
        logger.info("dir :" + dir);

        if (userAgent.contains("msie") || userAgent.contains("like gecko")) {
            // IE
            logger.info("ie: {}", userAgent);
            fileNameDecode = URLEncoder.encode(fileNameDecode, "UTF-8");
        } else if (userAgent.contains("firefox")) {
            logger.info("fireFox: {}", userAgent);
            fileNameDecode = "=?UTF-8?B?" + (new String(Base64.encodeBase64(fileNameDecode.getBytes("UTF-8")))) + "?=";
        } else {
            // 非IE
            logger.info("非IE: {}", userAgent);
            fileNameDecode = new String(fileNameDecode.getBytes("UTF-8"), "ISO-8859-1");
        }
        // 设置响应类型
        response.setContentType("application/force-download");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileNameDecode);
        // 把本地文件发送给客户端
        OutputStream out = response.getOutputStream();
        boolean result = FtpUtil.downloadByConfig(this.ftpConfig, dir, realFileName, out);
        logger.info("文件处理完成: {}", result);

        out.flush();
        out.close();
    }
}
