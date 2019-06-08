package com.along.common.util.ftp;

import com.along.config.ftp.FtpConfig;
import org.apache.commons.net.ftp.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * @Description: ftp上传下载工具类
 * @Author along
 * @Date 2019/4/11 17:40
 */
public class FtpUtil {

    private static final Logger logger = LoggerFactory.getLogger(FtpUtil.class);

    /**
     * 本地字符编码
     */
    private static String LOCAL_CHARSET = "GBK";

    /**
     * FTP协议里面，规定文件名编码为iso-8859-1
     */
    private static String SERVER_CHARSET = "ISO-8859-1";


    // ==============上传================

    /**
     * ftp上传文件方法 title:pictureUpload
     *
     * @param ftpConfig    由spring管理的FtpConfig配置，在调用本方法时，可以在使用此方法的类中通过@AutoWared注入该属性。
     *                     由于本方法是静态方法，所以不能在此注入该属性
     * @param fileNewName  文件新名称--防止重名 例如："1.jpg"
     * @param fileSavePath 文件保存路径。注：最后访问路径是
     *                     ftpConfig.getFTP_ADDRESS()+"/images"+fileSavePath
     * @param inputStream  要上传的文件（图片）
     * @return 若上传成功，返回图片的访问路径，若上传失败，返回null
     * @throws IOException
     */
    public static String fileUploadByConfig(FtpConfig ftpConfig, String fileNewName,
                                            String fileSavePath, InputStream inputStream) throws IOException {

        String picHttpPath = null;
        logger.info("=== begin upload file to {}", ftpConfig.getFtp_address());

        boolean flag = uploadFile(ftpConfig.getFtp_address(), ftpConfig.getFtp_port(),
                ftpConfig.getFtp_username(), ftpConfig.getFtp_password(),
                ftpConfig.getFtp_basePath(), fileSavePath, fileNewName, inputStream);

        if (flag) {
            picHttpPath = ftpConfig.getFtp_basePath() + fileSavePath + "/" + fileNewName;
            logger.info("=== Successfully uploaded file to {}", ftpConfig.getFtp_address());
            logger.info("=== {}", picHttpPath);
            return picHttpPath;
        } else {
            logger.error("=== Failed uploading file to {}", ftpConfig.getFtp_address());
            return picHttpPath;
        }
    }

    /**
     * Description: 向FTP服务器上传文件
     *
     * @param host     FTP服务器hostname
     * @param ftpPort  FTP服务器端口
     * @param username FTP登录账号
     * @param password FTP登录密码
     * @param basePath FTP服务器基础目录
     * @param filePath FTP服务器文件存放路径。例如分日期存放：/2015/01/01。文件的路径为basePath+filePath
     * @param filename 上传到FTP服务器上的文件名
     * @param input    输入流
     * @return 成功返回true，否则返回false
     */
    private static boolean uploadFile(String host, int ftpPort, String username,
                                      String password, String basePath, String filePath,
                                      String filename, InputStream input) {

        boolean result = false;
        // 1.创建FTP客户端
        FTPClient ftp = new FTPClient();
        try {
            // 2.连接FTP服务器
            // 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务器
            ftp.connect(host, ftpPort);
            logger.info("Connected to ftp server {}:{}", host, ftpPort);
            // 3.After connection attempt, you should check the reply code to verify success.
            int reply = ftp.getReplyCode();
            logger.info("connect verify --> ftp.getReplyString() : {}", ftp.getReplyString());
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect(); // 断开连接FTP服务器
                logger.info("FTP server refused connection.");
                return result;
            }
            logger.info("FTP server connected.");

            // 4.登录
            boolean isLogin = ftp.login(username, password);
            logger.info("ftp login result is {}, username is {}, password is {}", isLogin, username, password);
            if (!isLogin) {
                // 登录失败， 断开连接
                ftp.disconnect();
                logger.info("ftp login failed");
                return result;
            }

            // 开启服务器对UTF-8的支持，如果服务器支持就用UTF-8编码，否则就使用本地编码（GBK）.
            if (FTPReply.isPositiveCompletion(ftp.sendCommand("OPTS UTF8", "ON"))) {
                LOCAL_CHARSET = "UTF-8";
            }
            ftp.setControlEncoding(LOCAL_CHARSET);

            // 切换到上传目录
            if (!ftp.changeWorkingDirectory(basePath + filePath)) {
                // 如果目录不存在创建目录
                System.out.println("directory is not exist: " + basePath + filePath);
                String[] dirs = filePath.split("/");
                String tempPath = basePath;
                for (String dir : dirs) {
                    if (null == dir || "".equals(dir)) {
                        continue;
                    }
                    tempPath += "/" + dir;
                    if (!ftp.changeWorkingDirectory(tempPath)) {
                        if (!ftp.makeDirectory(tempPath)) {
                            return result;
                        } else {
                            ftp.changeWorkingDirectory(tempPath);
                        }
                    }
                }
            }
            // 设置上传文件的类型为二进制类型
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            // 允许被动连接--访问远程ftp时需要
            ftp.enterLocalPassiveMode();

            // 上传文件
            // 文件名称转换： FTP协议里面，规定文件名编码为iso-8859-1，所以目录名或文件名需要转码。将中文的目录或文件名转为iso-8859-1编码的字符
            if (!ftp.storeFile(new String(filename.getBytes(LOCAL_CHARSET), SERVER_CHARSET), input)) {
                logger.info("ftp storeFile failed");
                return result;
            }

            input.close();
            ftp.logout();
            logger.info("ftp logout");
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                    // do nothing
                }
            }
        }
        return result;
    }


    // ==================下载======================


    /**
     * Description: 从FTP服务器下载文件
     *
     * @param host       FTP服务器hostname
     * @param port       FTP服务器端口
     * @param username   FTP登录账号
     * @param password   FTP登录密码
     * @param remotePath FTP服务器上的相对路径
     * @param fileName   要下载的文件名
     * @param localPath  下载后保存到本地的路径
     * @return
     */
    public static boolean downloadFile(String host, int port, String username, String password,
                                       String remotePath, String fileName, String localPath) {
        boolean result = false;
        // 1.创建FTP客户端
        FTPClient ftp = new FTPClient();
        try {
            // 2.连接FTP服务器
            // 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务器
            ftp.connect(host, port);
            logger.info("Connected to ftp server {}:{}", host, port);
            // 3.After connection attempt, you should check the reply code to verify success.
            int reply = ftp.getReplyCode();
            logger.info("connect verify --> ftp.getReplyString() : {}", ftp.getReplyString());
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                logger.info("FTP server refused connection.");
                return result;
            }
            logger.info("FTP server connected.");

            // 4.登录
            ftp.login(username, password);
            reply = ftp.getReplyCode();
            logger.info("login verify --> ftp.getReplyString() : {}", ftp.getReplyString());
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                logger.info("FTP server refused connection.");
                return result;
            }

            ftp.changeWorkingDirectory(remotePath); // 转移到FTP服务器目录
            FTPFile[] fs = ftp.listFiles();
            for (FTPFile ff : fs) {
                if (ff.getName().equals(fileName)) {
                    File localFile = new File(localPath + "/" + ff.getName());
                    OutputStream is = new FileOutputStream(localFile);
                    ftp.retrieveFile(ff.getName(), is);
                    is.close();
                }
            }

            ftp.logout();
            logger.info("ftp logout");
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                    // do nothing
                }
            }
        }
        return result;
    }

    /**
     * Description: 根据配置文件配置下载文件
     *
     * @param config       由spring管理的FtpConfig配置，在调用本方法时，可以在使用此方法的类中通过@AutoWared注入该属性。
     *                     由于本方法是静态方法，所以不能在此注入该属性
     * @param remotePath   要下载文件的路径
     * @param fileName     要下载的文件名
     * @param outputStream 输出流
     * @return
     */
    public static boolean downloadByConfig(FtpConfig config, String remotePath,
                                           String fileName, OutputStream outputStream) {
        boolean result = false;
        // 1.创建FTP客户端
        FTPClient ftp = new FTPClient();
        try {
            // 2.连接FTP服务器，如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务器
            ftp.connect(config.getFtp_address(), config.getFtp_port());
            logger.info("Connected to ftp server {}:{}", config.getFtp_address(), config.getFtp_port());
            // 3.After connection attempt, you should check the reply code to verify success.
            int reply = ftp.getReplyCode();
            logger.info("connect verify --> ftp.getReplyString() : {}", ftp.getReplyString());
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                logger.info("FTP server refused connection.");
                return result;
            }
            logger.info("FTP server connected.");

            // 4.登录
            ftp.login(config.getFtp_username(), config.getFtp_password());
            reply = ftp.getReplyCode();
            logger.info("login verify --> ftp.getReplyString() : {}", ftp.getReplyString());
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                logger.info("FTP server refused connection.");
                return result;
            }

            charsetConfig(ftp); // 字符集设置

            // 转移到FTP服务器目录
            if (!ftp.changeWorkingDirectory(config.getFtp_basePath() + remotePath)) {
                logger.info("未转移到FTP服务器目录");
                ftp.disconnect();
                return result;
            }

            FTPFile[] fs = ftp.listFiles();
            for (FTPFile ff : fs) {
                if (ff.getName().equals(fileName)) {
                    ftp.setFileType(FTP.BINARY_FILE_TYPE); // 采用二进制方式传输
                    result = ftp.retrieveFile(new String(fileName.getBytes(LOCAL_CHARSET), SERVER_CHARSET), outputStream);
                    String replyInfo = ftp.getReplyString();
                    logger.info("ftp retrieveFile finished,result is {},reply code is {}", result, replyInfo);
                    logger.info("remote file is {}, wanted file is {}",
                            new String(ff.getName().getBytes(SERVER_CHARSET), LOCAL_CHARSET), fileName);
                    break;
                }
            }

            ftp.logout();
            logger.info("ftp logout");
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                    // do nothing
                }
            }
        }
        return result;
    }

    /**
     * 字符集设置
     *
     * @param ftpClient FTP客户端对象
     * @throws IOException
     */
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
