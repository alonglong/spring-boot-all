package com.along.common.util.ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
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
     * FTP协议里面，规定文件名编码为iso-8859-1
     */
    private static String SERVER_CHARSET = "ISO-8859-1";

    /**
     * 获取FTPClient对象
     *
     * @param ftpHost     FTP 主机服务器
     * @param ftpUserName FTP 登录密码
     * @param ftpPassword FTP 登录用户名
     * @param ftpPort     FTP 端口 默认为21
     * @return
     */
    public static FTPClient getFTPClient(String ftpHost, String ftpUserName, String ftpPassword, int ftpPort) {

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
            e.printStackTrace();
            logger.error("FTP的IP地址可能错误，请正确配置。");
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("FTP的端口错误,请正确配置。");
        }

        return ftpClient;
    }

    /**
     * 从FTP服务器下载文件
     *
     * @param ftpHost     FTP IP地址
     * @param ftpPort     FTP 端口
     * @param ftpUserName FTP 用户名
     * @param ftpPassword FTP 密码
     * @param ftpPath     FTP服务器中文件所在路径 格式： ftptest/aa
     * @param localPath   下载到本地的位置 格式：H:/download
     * @param fileName    文件名称
     */
    public static void downloadFtpFile(String ftpHost, int ftpPort, String ftpUserName,
                                       String ftpPassword, String ftpPath, String localPath,
                                       String fileName) {

        FTPClient ftpClient = null;
        try {
            ftpClient = getFTPClient(ftpHost, ftpUserName, ftpPassword, ftpPort);
            ftpClient.setControlEncoding("UTF-8"); // 中文支持

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

        } catch (FileNotFoundException e) {
            System.out.println("没有找到" + ftpPath + "文件");
            e.printStackTrace();
        } catch (SocketException e) {
            System.out.println("连接FTP失败.");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("文件读取错误。");
            e.printStackTrace();
        }

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
    public static boolean uploadFile(String ftpHost, String ftpUserName,
                                     String ftpPassword, int ftpPort, String ftpPath,
                                     String fileName, InputStream input) {
        boolean success = false;
        FTPClient ftpClient = null;
        try {
            int reply;
            ftpClient = getFTPClient(ftpHost, ftpUserName, ftpPassword, ftpPort);
            reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                return success;
            }
            ftpClient.setControlEncoding("UTF-8"); // 中文支持
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();
            ftpClient.changeWorkingDirectory(ftpPath);

            ftpClient.storeFile(fileName, input);

            input.close();
            ftpClient.logout();
            success = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return success;
    }


}
