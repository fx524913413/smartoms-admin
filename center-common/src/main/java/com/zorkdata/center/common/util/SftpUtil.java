package com.zorkdata.center.common.util;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import com.jcraft.jsch.*;

import java.util.Vector;


public class SftpUtil {
    public static final Logger LOG = LogManager.getLogger(SftpUtil.class);
    public static SftpUtil sftpUtil;
    public static ChannelSftp sftp;
    public String host;
    public int port;
    public String userName;
    public String password;

    public SftpUtil(String host, int port, String userName, String password) {
        this.host = host;
        this.port = port;
        this.userName = userName;
        this.password = password;
    }

    /**
     * 获得Smb连接对象
     *
     * @return SmbUtil
     * @Title: getInstance
     * @author wangqinghua
     * @date 2015-9-22 下午7:39:41
     */
    public static synchronized SftpUtil getInstance(String host, int port,
                                                    String userName, String password) {
        if (sftpUtil == null) {
            sftpUtil = new SftpUtil(host, port, userName, password);
        }
        return sftpUtil;
    }

    /**
     * 连接初始化
     *
     * @Title: init void
     * @author wangqinghua
     * @date 2015-9-22 下午7:40:50
     */
    public ChannelSftp getSftp() {
        JSch jsch = new JSch();
        Channel channel = null;
        try {
            Session sshSession = jsch.getSession(this.userName, this.host, this.port);
            sshSession.setConfig("StrictHostKeyChecking", "no");
            sshSession.setPassword(password);
//            Properties sshConfig = new Properties();
//            sshConfig.put("StrictHostKeyChecking", "no");
//            sshSession.setConfig(sshConfig);
            sshSession.connect(20000);
            channel = sshSession.openChannel("sftp");
            channel.connect();
        } catch (JSchException e) {
            LOG.info("getSftp exception：", e);
        }
        return (ChannelSftp) channel;
    }

    public void disconnect() throws Exception {
        if (sftp != null) {
            if (sftp.isConnected()) {
                sftp.disconnect();
            } else if (sftp.isClosed()) {
                System.out.println("Session close...........");
            }
        }
    }

    /**
     * 删除文件
     *
     * @param directory  要删除文件所在目录
     * @param deleteFile 要删除的文件
     * @param sftp
     */
    public void delete(String directory, String deleteFile, ChannelSftp sftp) {
        try {
            sftp.cd(directory);
            sftp.rm(deleteFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 列出目录下的文件
     *
     * @param directory 要列出的目录
     * @param sftp
     * @return
     * @throws SftpException
     */
    public Vector listFiles(String directory, ChannelSftp sftp)
            throws SftpException {
        return sftp.ls(directory);
    }
}