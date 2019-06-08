package com.along.common.util.sftp;

import com.along.config.sftp.SftpConfig;
import com.jcraft.jsch.*;
import org.apache.commons.lang3.StringUtils;

import java.util.Properties;

/**
 * sftp连接
 */
public class SftpChannel {

    private Session session = null;
    private Channel channel = null;

    // 端口默认为22
    private static final int SFTP_DEFAULT_PORT = 22;

    /**
     * 利用JSch包实现SFTP下载、上传文件(秘钥方式登陆)
     */
    public ChannelSftp connectByIdentity(SftpConfig sftpConfig) throws JSchException {
        JSch jsch = new JSch();
        int port = SFTP_DEFAULT_PORT;
        //设置密钥和密码
        //支持密钥的方式登陆，只需在jsch.getSession之前设置一下密钥的相关信息就可以了
        if (StringUtils.isNotBlank(sftpConfig.getPrivateKeyPath())) {
            if (StringUtils.isNotBlank(sftpConfig.getPassphrase())) {
                //设置带口令的密钥
                jsch.addIdentity(sftpConfig.getPrivateKeyPath(), sftpConfig.getPassphrase());
            } else {
                //设置不带口令的密钥
                jsch.addIdentity(sftpConfig.getPrivateKeyPath());
            }
        }
        if (sftpConfig.getPort() != null) {
            port = sftpConfig.getPort();
        }
        if (port > 0) {
            //采用指定的端口连接服务器
            session = jsch.getSession(sftpConfig.getUsername(), sftpConfig.getIp(), port);
        } else {
            //连接服务器，采用默认端口
            session = jsch.getSession(sftpConfig.getUsername(), sftpConfig.getIp());
        }
        if (session == null) {
            throw new JSchException("session为空，连接失败");
        }
        Properties sshConfig = new Properties();
        sshConfig.put("StrictHostKeyChecking", "no");
        session.setConfig(sshConfig);
        session.setTimeout(30000);
        session.connect();
        //创建sftp通信通道
        channel = session.openChannel("sftp");
        channel.connect();
        return (ChannelSftp) channel;
    }

    /**
     * 利用JSch包实现SFTP下载、上传文件(用户名密码方式登陆)
     */
    public ChannelSftp connectByPwd(SftpConfig sftpConfig) throws JSchException {
        JSch jsch = new JSch();
        int port = SFTP_DEFAULT_PORT;
        if (sftpConfig.getPort() != null) {
            port = sftpConfig.getPort();
        }
        if (port > 0) {
            //采用指定的端口连接服务器
            session = jsch.getSession(sftpConfig.getUsername(), sftpConfig.getIp(), port);
        } else {
            //连接服务器，采用默认端口
            session = jsch.getSession(sftpConfig.getUsername(), sftpConfig.getIp());
        }
        if (session == null) {
            throw new JSchException("session为空，连接失败");
        }
        //设置登陆主机的密码
        session.setPassword(sftpConfig.getPwd()); // 设置密码
        Properties sshConfig = new Properties();
        sshConfig.put("StrictHostKeyChecking", "no");
        session.setConfig(sshConfig);
        session.setTimeout(30000);
        session.connect();
        //创建sftp通信通道
        channel = session.openChannel("sftp");
        channel.connect();
        return (ChannelSftp) channel;
    }

    public void closeChannel() {
        if (channel != null) {
            channel.disconnect();
        }
        if (session != null) {
            session.disconnect();
        }
    }

}