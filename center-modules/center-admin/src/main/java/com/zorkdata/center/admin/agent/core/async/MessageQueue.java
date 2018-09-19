package com.zorkdata.center.admin.agent.core.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Looper使用的消息队列
 * {@link Looper#myQueue() Looper.myQueue()}.
 * <p>
 * Created by googe on 2016-03-20.
 */
public final class MessageQueue {
    private static final String TAG = "MessageQueue";
    private static final boolean DEBUG = false;

    //阻塞队列
    @Autowired
    private LinkedBlockingQueue<Message> queue = new LinkedBlockingQueue<Message>();

    private Logger logger = LoggerFactory.getLogger(MessageQueue.class);


    /**
     * 获取消息
     *
     * @return
     */
    public Message next() {
        Message message = null;
        try {
            //队列空的时候会阻塞，直到有队列成员被放进来
            message = queue.take();
        } catch (InterruptedException e) {
            logger.error("sleep 异常",e);
        }
        return message;
    }

    /**
     * 插入消息
     *
     * @param msg
     * @return
     */
    public boolean enqueueMessage(Message msg) {
        try {
            //队列满的时候会阻塞直到有队列成员被消费
            queue.put(msg);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 检查指定条件的消息是否存在
     *
     * @param h
     * @param r
     * @param object
     * @return
     * @throws Exception
     */
    boolean hasMessages(Handler h, Runnable r, Object object) throws Exception {
        throw new Exception("尚未实现");
    }
}
