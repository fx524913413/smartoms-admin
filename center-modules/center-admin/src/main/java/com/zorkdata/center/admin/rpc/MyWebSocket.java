package com.zorkdata.center.admin.rpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/5/31 22:16
 */
@ServerEndpoint(value = "/getJobInfo/{jobid}/{timeout}")
@Component
public class MyWebSocket {
    private static final Logger logger = LoggerFactory.getLogger(MyWebSocket.class);
    /**
     * 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
     */
    private static int onlineCount = 0;

    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
     */
    private static CopyOnWriteArraySet<MyWebSocket> webSocketSet = new CopyOnWriteArraySet<>();

    public static ConcurrentHashMap<String, CopyOnWriteArraySet<MyWebSocket>> JobidUserMap = new ConcurrentHashMap();

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    /**
     * 连接建立成功调用的方法
     *
     * @param session
     */
    @OnOpen
    public void onOpen(@PathParam(value = "jobid") String jobid, @PathParam(value = "timeout") Long timeout, Session session, EndpointConfig config) {
        this.session = session;
        session.setMaxIdleTimeout(timeout);
        // 加入set中
        webSocketSet.add(this);

        if (JobidUserMap != null && JobidUserMap.contains(jobid)) {
            JobidUserMap.get(jobid).add(this);
        } else {
            CopyOnWriteArraySet<MyWebSocket> webSocketSet1 = new CopyOnWriteArraySet<MyWebSocket>();
            webSocketSet1.add(this);
            JobidUserMap.put(jobid, webSocketSet1);
        }
        // 在线数加1
        addOnlineCount();
        logger.info("有新连接加入！当前在线人数为" + getOnlineCount());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);
        // 从set中删除
        // 在线数减1
        subOnlineCount();
        for (String key : JobidUserMap.keySet()) {
            JobidUserMap.get(key).remove(this);
            if (JobidUserMap.get(key).size() == 0) {
                JobidUserMap.remove(key);
            }
        }
        logger.info("有一连接关闭！当前在线人数为" + getOnlineCount());
        if (this.session != null) {
            try {
                this.session.close();
            } catch (IOException e) {
                logger.error("websoket关闭失败", e);
            }
        }
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        logger.info("来自客户端的消息:" + message);

        // 群发消息
        for (MyWebSocket item : webSocketSet) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                logger.error("处理消息出错", e);
            }
        }
    }

    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error) {
        logger.error("发生错误", error);
    }


    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
        // this.session.getAsyncRemote().sendText(message);
    }


    /**
     * 群发自定义消息
     */
    public static void sendInfo(String message) throws IOException {
        for (MyWebSocket item : webSocketSet) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                continue;
            }
        }
    }

    public static void sendInfo(String jobid, String message) throws IOException {
        if (JobidUserMap.containsKey(jobid)) {
            CopyOnWriteArraySet<MyWebSocket> copyOnWriteArraySet = JobidUserMap.get(jobid);
            for (MyWebSocket item : copyOnWriteArraySet) {
                if (webSocketSet.contains(item)) {
                    try {
                        item.sendMessage(message);
                    } catch (IOException e) {
                        continue;
                    }
                }
            }
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        MyWebSocket.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        MyWebSocket.onlineCount--;
    }
}