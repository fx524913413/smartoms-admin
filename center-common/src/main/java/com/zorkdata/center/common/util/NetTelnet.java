package com.zorkdata.center.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class NetTelnet {
    private static final Logger logger = LoggerFactory.getLogger(NetTelnet.class);

    /**
     * telnet Router ip和端口是否可用
     */
    public static boolean telnet(String ip, int port) {

        Socket server = null;
        boolean flag = false;
        try {
            server = new Socket();
            InetSocketAddress address = new InetSocketAddress(ip, port);
            server.connect(address, 1000);
        } catch (UnknownHostException e) {
            logger.error(ip + " telnet出错", e);
            return flag;
        } catch (IOException e) {
            logger.error(ip + " telnet出错", e);
            return flag;
        } finally {
            if (server != null) {
                flag = true;
                try {
                    server.close();
                } catch (IOException e) {
                    logger.error("关闭失败", e);
                }
            }
        }
        return flag;
    }

    public static String getConnectedIp(String ip, String computerType) {
        if (ip != null) {
            boolean flag = false;
            String[] iparray = ip.split(",");
            for (String ipstr : iparray) {
                if (!ipstr.equals("127.0.0.1")) {
                    if (computerType != null && computerType.startsWith("linux")) {
                        if (NetTelnet.telnet(ipstr, 8000) || NetTelnet.telnet(ipstr, 22)) {
                            ip = ipstr;
                            flag = true;
                            break;
                        }
                    } else {
                        if (NetTelnet.telnet(ipstr, 8000) || NetTelnet.telnet(ipstr, 3389)) {
                            ip = ipstr;
                            flag = true;
                            break;
                        }
                    }
                }
            }
            if (!flag) {
                return null;
            }
        } else {
            return null;
        }
        return ip;
    }
}
