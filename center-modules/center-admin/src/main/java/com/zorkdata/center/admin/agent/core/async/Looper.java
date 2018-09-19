package com.zorkdata.center.admin.agent.core.async;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * 处理消息队列，把消息派发给处理器handler
 *
 * <pre>
 *  EG:
 *  class LooperThread extends Thread {
 *      public Handler mHandler;
 *
 *      public void run() {
 *          Looper.prepare();
 *
 *          mHandler = new Handler() {
 *              public void handleMessage(Message msg) {
 *                  // process incoming messages here
 *              }
 *          };
 *
 *          Looper.loop();
 *      }
 *  }</pre>
 * <p>
 * Created by googe on 2016-03-20.
 */

public final class Looper {

    private static final String TAG = "Looper";

    // sThreadLocal.get() will return null unless you've called prepare().
    static final ThreadLocal<Looper> sThreadLocal = new ThreadLocal<Looper>();
    private static Looper sMainLooper;  // guarded by Looper.class

    final MessageQueue mQueue;
    final Thread mThread;


    /**
     * 初始化当前线程的looper，要运行该looper，必须调用Looper.loop();
     */
    public static void prepare() {
        if (sThreadLocal.get() != null) {
            throw new RuntimeException("Only one Looper may be created per thread");
        }
        sThreadLocal.set(new Looper());
    }

    /**
     * 初始化当前应用程序主线程looper
     */
    public static void prepareMainLooper() {
        prepare();
        synchronized (Looper.class) {
            if (sMainLooper != null) {
                throw new IllegalStateException("The main Looper has already been prepared.");
            }
            sMainLooper = myLooper();
        }
    }

    /**
     * 获取当前应用程序主线程的looper，必须在程序启动是调用Looper.prepareMainLooper(),否则返回null
     */
    public static Looper getMainLooper() {
        synchronized (Looper.class) {
            return sMainLooper;
        }
    }

    /**
     * 执行looper，looper会不停的消费消息队列的数据
     */
    public static void loop() {
        final Looper me = myLooper();
        if (me == null) {
            throw new RuntimeException("No Looper; Looper.prepare() wasn't called on this thread.");
        }
        final MessageQueue queue = me.mQueue;

        for (; ; ) {
            Message msg = queue.next(); // might block
            if (msg == null) {
                // No message indicates that the message queue is quitting.
                return;
            }

            msg.target.dispatchMessage(msg);

            msg.recycleUnchecked();
        }
    }

    /**
     * 获取当前线程的looper
     */
    public static Looper myLooper() {
        return sThreadLocal.get();
    }

    /**
     * 获取当前线程的消息队列，此方法必须在线程执行looper.prepare()后调用
     * 否则 NullPointerException will be thrown.
     */
    public static MessageQueue myQueue() {
        return myLooper().mQueue;
    }

    private Looper() {
        mQueue = new MessageQueue();
        mThread = Thread.currentThread();
    }

    /**
     * 如果当前线程式looper的工作线程返回true，否则返回false
     */
    public boolean isCurrentThread() {
        return Thread.currentThread() == mThread;
    }

    /**
     * 获取当前looper的工作线程
     *
     * @return looper的线程
     */
    public Thread getThread() {
        return mThread;
    }

    /**
     * 获取looper的消息队列
     *
     * @return 消息队列
     */
    public MessageQueue getQueue() {
        return mQueue;
    }

    @Override
    public String toString() {
        return "Looper (" + mThread.getName() + ", tid " + mThread.getId()
                + ") {" + Integer.toHexString(System.identityHashCode(this)) + "}";
    }
}
