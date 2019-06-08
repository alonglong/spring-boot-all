package com.along.common.util.sftp;

import com.along.config.sftp.SftpConfig;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * @Description: sftp 上传下载工具类
 * @Author along
 * @Date 2019/6/8 11:01
 */
public class SFtpUtil {

    private static final Logger logger = LoggerFactory.getLogger(SFtpUtil.class);


    /**
     * 获取Sftp对象
     *
     * @param param
     * @return
     */
    public static SftpConfig getSftpObj(String param) {
        SftpConfig sftpConfig = new SftpConfig();
        if (StringUtils.isNotBlank(param)) {
            JSONObject jsonObj = JSONObject.fromObject(param);
            sftpConfig = (SftpConfig) JSONObject.toBean(jsonObj, SftpConfig.class);
        }
        return sftpConfig;
    }

    /**
     * sftp 上传
     *
     * @param config      sftp配置信息
     * @param filePath    文件的目录，可多级
     * @param fileName    文件名
     * @param inputStream 输入流
     * @return
     */
    public static boolean upload(SftpConfig config, String filePath, String fileName, InputStream inputStream) {
        String fullPath = config.getBasePath() + fileName;
        logger.info("要下载的文件完整路径：" + fullPath);
        SftpChannel sftpChannel = new SftpChannel();
        ChannelSftp sftp = null;
        try {
            if (StringUtils.isNotBlank(config.getPrivateKeyPath())) {
                sftp = sftpChannel.connectByIdentity(config);
            } else {
                sftp = sftpChannel.connectByPwd(config);
            }
            if (sftp.isConnected()) {
                logger.info("连接服务器成功");
            } else {
                logger.error("连接服务器失败");
                return false;
            }
            //检查路径
            if (!isExist(sftp, fullPath)) {
                logger.error("创建sftp服务器路径失败:" + fullPath);
                return false;
            }
            logger.info("开始上传，目标服务器路径：[{}]", fullPath);
            sftp.put(inputStream, fullPath);
            logger.info("上传成功");
            return true;
        } catch (Exception e) {
            logger.error("上传失败", e);
            return false;
        } finally {
            sftpChannel.closeChannel();
        }
    }

    /**
     * sftp 下载（输出到浏览器）
     *
     * @param config       sftp配置信息
     * @param filePath     FTP服务器上的相对路径（相对于SftpConfig中的basePath）
     * @param fileName     服务器文件名
     * @param outputStream 输出流
     * @return
     */
    public static boolean down2Client(SftpConfig config, String filePath, String fileName, OutputStream outputStream) {
        SftpChannel sftpChannel = new SftpChannel();
        ChannelSftp sftp = null;
        try {
            if (StringUtils.isNotBlank(config.getPrivateKeyPath())) {
                sftp = sftpChannel.connectByIdentity(config);
            } else {
                sftp = sftpChannel.connectByPwd(config);
            }
            if (sftp.isConnected()) {
                logger.info("连接服务器成功");
            } else {
                logger.error("连接服务器失败");
                return false;
            }
            String src = config.getBasePath() + filePath + new String(fileName.getBytes(), StandardCharsets.UTF_8);
            logger.info("开始下载，sftp服务器路径：[{}]", src);
            sftp.get(src, outputStream);
            logger.info("下载成功");
            return true;
        } catch (Exception e) {
            logger.error("下载失败", e);
            return false;
        } finally {
            sftpChannel.closeChannel();
        }
    }

    /**
     * sftp 下载（从服务器直接下载到本地目录）
     *
     * @param config    sftp配置信息
     * @param filePath  FTP服务器上的相对路径（相对于SftpConfig中的basePath）
     * @param fileName1 服务器中要下载的文件名
     * @param fileName2 要输出到本地的文件名,为空则取服务器中的文件名
     * @param localPath 下载后保存到本地的路径
     * @return
     */
    public static boolean down2Local(SftpConfig config, String filePath, String fileName1, String fileName2, String localPath) {
        SftpChannel sftpChannel = new SftpChannel();
        ChannelSftp sftp = null;
        try {
            if (StringUtils.isNotBlank(config.getPrivateKeyPath())) {
                sftp = sftpChannel.connectByIdentity(config);
            } else {
                sftp = sftpChannel.connectByPwd(config);
            }
            if (sftp.isConnected()) {
                logger.info("连接服务器成功");
            } else {
                logger.error("连接服务器失败");
                return false;
            }
            String dst = ""; // 本地目录
            if (StringUtils.isBlank(fileName2)) {
                dst = localPath + fileName1;
            } else {
                dst = localPath + fileName2;
            }
            String src = config.getBasePath() + filePath + fileName1;
            logger.info("开始下载，sftp服务器路径：[{}]，目标服务器路径：[{}]", src, localPath);
            sftp.get(src, dst);
            logger.info("下载成功");
            return true;
        } catch (Exception e) {
            logger.error("下载失败", e);
            return false;
        } finally {
            sftpChannel.closeChannel();
        }
    }

    /**
     * 判断文件夹是否存在
     * true 目录创建成功，false 目录创建失败
     *
     * @param sftp
     * @param filePath 文件夹路径
     * @return
     */
    public static boolean isExist(ChannelSftp sftp, String filePath) {
        String[] paths = filePath.split("\\/");
        String dir = paths[0];
        for (int i = 0; i < paths.length - 1; i++) {
            dir = dir + "/" + paths[i + 1];
            try {
                sftp.cd(dir);
            } catch (SftpException sException) {
                if (ChannelSftp.SSH_FX_NO_SUCH_FILE == sException.id) {
                    try {
                        sftp.mkdir(dir);
                    } catch (SftpException e) {
                        e.printStackTrace();
                        return false;
                    }
                }
            }
        }
        return true;
    }

}
