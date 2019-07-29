package com.along.common.util.ftp;

import org.apache.commons.net.ftp.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.SocketException;

/**
 * @Description: FTP 工具类
 * @Author along
 * @Date 2019/4/12 16:10
 */
public class FtpUtil2 {

    private static final Logger logger = LoggerFactory.getLogger(FtpUtil2.class);

    /**
     * 本地字符编码
     */
    private static String LOCAL_CHARSET = "GBK";


    /**
     * 获取FTPClient对象
     *
     * @param ftpHost     FTP 主机服务器
     * @param ftpUserName FTP 登录密码
     * @param ftpPassword FTP 登录用户名
     * @param ftpPort     FTP 端口 默认为21
     * @return
     */
    private static FTPClient getFTPClient(String ftpHost, String ftpUserName, String ftpPassword, int ftpPort) {

        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(ftpHost, ftpPort); // 连接FTP服务器
            ftpClient.login(ftpUserName, ftpPassword); // 登录FTP服务器
            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                logger.error("未连接到FTP，用户名或密码错误。");
                ftpClient.disconnect();
            } else {
                logger.info("FTP连接成功,服务器信息 {}:{}", ftpHost, ftpPort);
            }

        } catch (SocketException e) {
            logger.error("FTP的IP地址可能错误，请正确配置。", e);
        } catch (IOException e) {
            logger.error("FTP的端口错误,请正确配置。", e);
        }

        return ftpClient;
    }

    /**
     * 从FTP服务器下载文件, 输出到本地
     *
     * @param ftpHost     FTP IP地址
     * @param ftpPort     FTP 端口
     * @param ftpUserName FTP 用户名
     * @param ftpPassword FTP 密码
     * @param ftpPath     FTP服务器中文件所在路径 格式： ftptest/aa
     * @param localPath   下载到本地的位置 格式：H:/download
     * @param fileName    文件名称
     */
    public static void download2Local(String ftpHost, int ftpPort, String ftpUserName,
                                      String ftpPassword, String ftpPath, String localPath,
                                      String fileName) {

        FTPClient ftpClient = null;
        try {
            ftpClient = getFTPClient(ftpHost, ftpUserName, ftpPassword, ftpPort);
            charsetConfig(ftpClient); //字符集设置,中文支持

            // 设置上传文件的类型为二进制类型
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            // 允许被动连接--访问远程ftp时需要
            ftpClient.enterLocalPassiveMode();

            // 转移到FTP服务器目录
            ftpClient.changeWorkingDirectory(ftpPath);

            File localFile = new File(localPath + File.separatorChar + fileName);
            OutputStream os = new FileOutputStream(localFile);
            ftpClient.retrieveFile(fileName, os);

            os.close();
            ftpClient.logout();
            logger.info("ftp logout");

        } catch (IOException e) {
            logger.error("文件读取错误。", e);
        } finally {
            if (null != ftpClient && ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException ignored) {

                }
            }
        }

    }

    /**
     * 从FTP服务器下载文件, 返回文件流
     *
     * @param ftpHost     FTP IP地址
     * @param ftpPort     FTP 端口
     * @param ftpUserName FTP 用户名
     * @param ftpPassword FTP 密码
     * @param ftpPath     FTP服务器中文件所在路径 格式： ftptest/aa
     * @param fileName    文件名称
     */
    public static InputStream downloadFileStream(String ftpHost, int ftpPort, String ftpUserName,
                                                 String ftpPassword, String ftpPath, String fileName) throws UnsupportedEncodingException {
        logger.info("下载文件开始，文件路径:{},文件名：{}", ftpPath, fileName);
        ftpPath = new String(ftpPath.getBytes(), FTP.DEFAULT_CONTROL_ENCODING); // 路径包含中文，需要改变编码为默认编码"ISO-8859-1"
        FTPClient ftpClient = null;
        InputStream inputStream = null;
        try {
            ftpClient = getFTPClient(ftpHost, ftpUserName, ftpPassword, ftpPort);
            charsetConfig(ftpClient); //字符集设置,中文支持

            // 设置上传文件的类型为二进制类型
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            // 允许被动连接--访问远程ftp时需要
            ftpClient.enterLocalPassiveMode();

            // 转移到FTP服务器目录
            if (!ftpClient.changeWorkingDirectory(ftpPath)) {
                logger.info("未转移到FTP服务器目录");
                ftpClient.disconnect();
                return null;
            }

            inputStream = ftpClient.retrieveFileStream(fileName);

            ftpClient.logout();
            logger.info("downloadFileStream ftp logout");

        } catch (IOException e) {
            logger.error("文件读取错误。", e);
        } finally {
            if (null != ftpClient && ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException ignored) {

                }
            }
        }

        return inputStream;

    }

    /**
     * Description: 向FTP服务器上传文件
     *
     * @param ftpHost     FTP服务器hostname
     * @param ftpUserName 账号
     * @param ftpPassword 密码
     * @param ftpPort     端口
     * @param ftpPath     FTP服务器中文件所在路径 格式： ftptest/aa
     * @param fileName    ftp文件名称
     * @param input       文件流
     * @return 成功返回true，否则返回false
     */
    public static boolean upload(String ftpHost, String ftpUserName,
                                 String ftpPassword, int ftpPort, String ftpPath,
                                 String fileName, InputStream input) throws UnsupportedEncodingException {
        logger.info("上传文件开始, 服务器：{}:{}, 文件路径:{}, 文件名:{}", ftpHost, ftpPort, ftpPath, fileName);
        ftpPath = new String(ftpPath.getBytes(), FTP.DEFAULT_CONTROL_ENCODING); // 路径包含中文，需要改变编码为默认编码"ISO-8859-1"
        FTPClient ftpClient = null;
        try {
            int reply;
            ftpClient = getFTPClient(ftpHost, ftpUserName, ftpPassword, ftpPort);
            reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                return false;
            }
            charsetConfig(ftpClient); //字符集设置,中文支持
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();
            // 转移到FTP服务器目录，如果目录不存在则创建
            for (String directory : ftpPath.split("/")) {
                if (!"".equals(directory) && !ftpClient.changeWorkingDirectory(directory)) {
                    logger.info("文件夹 {} 不存在,创建文件夹", directory);
                    ftpClient.makeDirectory(directory);
                    ftpClient.changeWorkingDirectory(directory);
                }
            }
            // 检查文件是否已存在，如果存在，先删除
            for (FTPFile ftpFile : ftpClient.listFiles()) {
                if (ftpFile.getName().equals(fileName)) {
                    logger.info("文件 {} 已存在，删除文件", fileName);
                    ftpClient.deleteFile(fileName);
                }
            }
            if (!ftpClient.storeFile(fileName, input)) {
                logger.error("上传文件失败");
                return false;
            }

            ftpClient.logout();
            logger.info("upload ftp logout");

        } catch (IOException e) {
            logger.error("", e);
        } finally {
            if (null != ftpClient && ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException ignored) {
                }
            }
            if (null != input) {
                try {
                    input.close();
                } catch (IOException ignored) {
                }
            }
        }
        return true;
    }

    /**
     * 文件拷贝，将一个服务器上的文件拷贝到另一个服务器上,文件名和路径相同
     *
     * @param ftpHost1     数据源服务器host
     * @param ftpPort1     数据源服务器port
     * @param ftpUserName1 数据源服务器用户名
     * @param ftpPassword1 数据源服务器密码
     * @param ftpHost2     目地服务器host
     * @param ftpPort2     目的服务器port
     * @param ftpUserName2 目的服务器用户名
     * @param ftpPassword2 目的服务器密码
     * @param ftpPath      文件路径
     * @param fileName     文件名
     * @return
     */
    public static boolean copyFile(String ftpHost1, int ftpPort1, String ftpUserName1, String ftpPassword1,
                                   String ftpHost2, int ftpPort2, String ftpUserName2, String ftpPassword2,
                                   String ftpPath, String fileName) throws UnsupportedEncodingException {
        logger.info("文件拷贝开始，数据源服务器地址：{}:{}，目的服务器地址：{}:{}，文件路径：{}/{}",
                ftpHost1, ftpPort1, ftpHost2, ftpPort2, ftpPath, fileName);
        InputStream inputStream = downloadFileStream(ftpHost1, ftpPort1, ftpUserName1, ftpPassword1, ftpPath, fileName);
        if (null == inputStream) {
            logger.info("下载文件失败");
            return false;
        }
        return upload(ftpHost2, ftpUserName2, ftpPassword2, ftpPort2, ftpPath, fileName, inputStream);

    }


    private static void charsetConfig(FTPClient ftpClient) throws IOException {
        // 开启服务器对UTF-8的支持，如果服务器支持就用UTF-8编码，否则就使用本地编码（GBK）.
        if (FTPReply.isPositiveCompletion(ftpClient.sendCommand("OPTS UTF8", "ON"))) {
            LOCAL_CHARSET = "UTF-8";
        }
        ftpClient.setControlEncoding(LOCAL_CHARSET);

        FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_UNIX);
        conf.setServerLanguageCode("zh");
        ftpClient.configure(conf);
    }


}
