package com.zorkdata.center.admin.agent.core.async;


import com.zorkdata.center.admin.agent.core.SaltApi;
import org.slf4j.LoggerFactory;

/**
 * Created by googe on 2016-03-20.
 */
public class LooperThread extends Thread {
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(LooperThread.class);
    /**
     * 当前LooperThread名称
     */
    private String name;

    private Looper mLooper;

    /**
     * 创建指定名称的Looper执行线程
     *
     * @param name
     */
    public LooperThread(String name) {
        this.name = name;
    }

    public LooperThread() {
        this(null);
    }

    @Override
    public void run() {
        // 将当前线程初始化为Looper线程
        if (!Check.isNullOrEmpty(name)) {
            setName(name);
        }
        Looper.prepare();
        mLooper = Looper.myLooper();

        // 开始循环处理消息队列
        Looper.loop();
    }

    /**
     * 获取当前线程looper
     *
     * @return
     */
    public Looper getLooper() {

        try {
            if (mLooper == null) {
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            logger.error("LooperThread,getLooper失败", e);
        }
        return mLooper;
    }

    /**
     * 获取当前looper线程名称
     *
     * @return
     */
    public String getLooperThread() {
        return this.name;
    }

//    public Handler getHandler(Class<? extends Handler> subHandler) {
//        return null;
//    }
}
