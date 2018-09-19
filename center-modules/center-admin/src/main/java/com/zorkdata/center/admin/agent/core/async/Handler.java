package com.zorkdata.center.admin.agent.core.async;


public class Handler {

    private static final String TAG = "Handler";

    final MessageQueue mQueue;
    final Looper mLooper;

    final Callback mCallback;

    /**
     * 实例化Hnalder，不实现Handler.handleMessage处理消息
     * 通过实现Callback接口来处理消息
     */
    public interface Callback {
        public boolean handleMessage(Message msg);
    }

    /**
     * 子类实现处理消息过程
     */
    public void handleMessage(Message msg) {
    }

    /**
     * 派发消息到指定的处理器
     */
    public void dispatchMessage(Message msg) {
        if (msg.callback != null) {
            handleCallback(msg);
        } else {
            if (mCallback != null) {
                if (mCallback.handleMessage(msg)) {
                    return;
                }
            }
            handleMessage(msg);
        }
    }

    /**
     * 默认构造器
     * 使用当前线程的looper创建Handler,若当前线程不存在looper抛出异常
     */
    public Handler() {
        mLooper = Looper.myLooper();
        if (mLooper == null) {
            throw new RuntimeException(
                    "Can't create handler inside thread that has not called Looper.prepare()");
        }
        mQueue = mLooper.mQueue;
        mCallback = null;
    }

    /**
     * 使用指定的looper创建Handler，looper不能为null
     *
     * @param looper 指定的looper
     */
    public Handler(Looper looper) {
        this(looper, null);
    }


    public Handler(Looper looper, Callback callback) {
        mLooper = looper;
        mQueue = looper.mQueue;
        mCallback = callback;
    }


    /**
     * 从消息池中获取一个消息
     */
    public final Message obtainMessage() {
        return Message.obtain(this);
    }

    /**
     * 从消息池中获取一个消息，并设置消息的what属性
     *
     * @param what
     * @return
     */
    public final Message obtainMessage(int what) {
        return Message.obtain(this, what);
    }

    /**
     * 从消息池中获取一个消息，并设置消息的what，obj属性
     *
     * @param what
     * @param obj
     * @return
     */
    public final Message obtainMessage(int what, Object obj) {
        return Message.obtain(this, what, obj);
    }

    /**
     * 从消息池中获取一个消息，并设置消息的what，arg1，arg2属性
     *
     * @param what
     * @param arg1
     * @param arg2
     * @return
     */
    public final Message obtainMessage(int what, int arg1, int arg2) {
        return Message.obtain(this, what, arg1, arg2);
    }

    /**
     * 从消息池中获取一个消息，并设置消息的what，arg1，arg2，obj属性
     *
     * @param what
     * @param arg1
     * @param arg2
     * @param obj
     * @return
     */
    public final Message obtainMessage(int what, int arg1, int arg2, Object obj) {
        return Message.obtain(this, what, arg1, arg2, obj);
    }

    /**
     * 发送Runnable消息，Runnable被附加到message上，message被处理时，
     * Runnable被Handler所在的线程执行
     *
     * @param r 处理message时执行
     */
    public final boolean post(Runnable r) {
        return sendMessageDelayed(getPostMessage(r), 0);
    }

    /**
     * 发送消息
     */
    public final boolean sendMessage(Message msg) {
        return sendMessageDelayed(msg, 0);
    }

    /**
     * 发送空消息，停止looper工作
     *
     * @return
     */
    public final boolean sendEmptyMessage() {
        Message msg = Message.obtain();
        return sendMessageDelayed(msg, 0);
    }

    /**
     * 根据指定的延迟时间发送消息
     *
     * @param msg
     * @param delayMillis
     * @return
     */
    public final boolean sendMessageDelayed(Message msg, long delayMillis) {
        if (delayMillis < 0) {
            delayMillis = 0;
        }
        return sendMessageAtTime(msg, System.currentTimeMillis() + delayMillis);
    }

    /**
     * 在指定时间发送message
     *
     * @param msg
     * @param uptimeMillis
     * @return
     */
    public boolean sendMessageAtTime(Message msg, long uptimeMillis) {
        MessageQueue queue = mQueue;
        if (queue == null) {
            RuntimeException e = new RuntimeException(
                    this + " sendMessageAtTime() called with no mQueue");
            return false;
        }
        return enqueueMessage(queue, msg, uptimeMillis);
    }

    private boolean enqueueMessage(MessageQueue queue, Message msg, long uptimeMillis) {
        msg.target = this;

        return queue.enqueueMessage(msg);
    }

    public final Looper getmLooper() {
        return mLooper;
    }
//
//    public void setmLooper(Looper mLooper) {
//        this.mLooper = mLooper;
//        mQueue = mLooper.mQueue;
//    }

    @Override
    public String toString() {
        return "Handler (" + getClass().getName() + ") {"
                + Integer.toHexString(System.identityHashCode(this))
                + "}";
    }

    private static Message getPostMessage(Runnable r) {
        Message m = Message.obtain();
        m.callback = r;
        return m;
    }


    private static void handleCallback(Message message) {
        message.callback.run();
    }


}
