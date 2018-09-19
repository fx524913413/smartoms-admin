package com.zorkdata.center.admin.agent.core.async;

import java.util.HashMap;
import java.util.Map;

/**
 * 消息体
 * <p>
 * Created by googe on 2016-03-20.
 */

public final class Message {
    /**
     * 用于表示一类消息的唯一标识，handler可以使用它分类处理消息
     */
    public int what;

    /**
     * 可选的标识1，存储整型数据
     */
    public int arg1;

    /**
     * 可选的标识2
     */
    public int arg2;

    /**
     * 消息体数据
     */
    public Object obj;

    /**
     * If set message is in use.
     * This flag is set when the message is enqueued and remains set while it
     * is delivered and afterwards when it is recycled.  The flag is only cleared
     * when a new message is created or obtained since that is the only time that
     * applications are allowed to modify the contents of the message.
     * <p>
     * It is an error to attempt to enqueue or recycle a message that is already in use.
     */
    /*package*/ static final int FLAG_IN_USE = 1 << 0;

    /**
     * Flags to clear in the copyFrom method
     */
    /*package*/ static final int FLAGS_TO_CLEAR_ON_COPY_FROM = FLAG_IN_USE;

    /*package*/ int flags;

    /*package*/ long when;

    /*package*/ Map data;

    /*package*/ Handler target;

    /*package*/ Runnable callback;

    // sometimes we store linked lists of these things
    /*package*/ Message next;

    private static final Object sPoolSync = new Object();
    private static Message sPool;
    private static int sPoolSize = 0;

    private static final int MAX_POOL_SIZE = 50;

    private static boolean gCheckRecycle = true;

    /**
     * 创建消息对象
     */
    public Message() {
    }

    /**
     * 从消息池获取消息对象
     */
    public static Message obtain() {
        synchronized (sPoolSync) {
            if (sPool != null) {
                Message m = sPool;
                sPool = m.next;
                m.next = null;
                m.flags = 0; // clear in-use flag
                sPoolSize--;
                return m;
            }
        }
        return new Message();
    }

    /**
     * 从消息池获取消息对象并把origin消息内容复制给它
     *
     * @param orig Original message to copy.
     * @return 从消息池获取消息对象并设置部分属性
     */
    public static Message obtain(Message orig) {
        Message m = obtain();
        m.what = orig.what;
        m.arg1 = orig.arg1;
        m.arg2 = orig.arg2;
        m.obj = orig.obj;
        if (orig.data != null) {
            m.data = orig.data;
        }
        m.target = orig.target;
        m.callback = orig.callback;

        return m;
    }

    /**
     * 从消息池获取消息对象并设置部分属性
     *
     * @param h 处理消息的handler
     * @return 从消息池获取消息对象并设置部分属性
     */
    public static Message obtain(Handler h) {
        Message m = obtain();
        m.target = h;

        return m;
    }

    /**
     * 从消息池获取消息对象并设置部分属性
     *
     * @param h        处理消息的handler
     * @param callback 处理消息的callback
     * @return 从消息池获取消息对象并设置部分属性
     */
    public static Message obtain(Handler h, Runnable callback) {
        Message m = obtain();
        m.target = h;
        m.callback = callback;

        return m;
    }

    /**
     * 从消息池获取消息对象并设置部分属性
     *
     * @param h    Value to assign to the <em>target</em> member.
     * @param what Value to assign to the <em>what</em> member.
     * @return 从消息池获取消息对象并设置部分属性
     */
    public static Message obtain(Handler h, int what) {
        Message m = obtain();
        m.target = h;
        m.what = what;

        return m;
    }

    /**
     * 从消息池获取消息对象并设置部分属性
     *
     * @param h    The <em>target</em> value to set.
     * @param what The <em>what</em> value to set.
     * @param obj  The <em>object</em> method to set.
     * @return 从消息池返回消息体
     */
    public static Message obtain(Handler h, int what, Object obj) {
        Message m = obtain();
        m.target = h;
        m.what = what;
        m.obj = obj;

        return m;
    }

    /**
     * 从消息池获取消息对象并设置部分属性
     *
     * @param h    The <em>target</em> value to set.
     * @param what The <em>what</em> value to set.
     * @param arg1 The <em>arg1</em> value to set.
     * @param arg2 The <em>arg2</em> value to set.
     * @return 从消息池返回消息体
     */
    public static Message obtain(Handler h, int what, int arg1, int arg2) {
        Message m = obtain();
        m.target = h;
        m.what = what;
        m.arg1 = arg1;
        m.arg2 = arg2;

        return m;
    }

    /**
     * 从消息池获取消息对象并设置部分属性
     *
     * @param h    The <em>target</em> value to set.
     * @param what The <em>what</em> value to set.
     * @param arg1 The <em>arg1</em> value to set.
     * @param arg2 The <em>arg2</em> value to set.
     * @param obj  The <em>obj</em> value to set.
     * @return 从消息池返回消息体
     */
    public static Message obtain(Handler h, int what,
                                 int arg1, int arg2, Object obj) {
        Message m = obtain();
        m.target = h;
        m.what = what;
        m.arg1 = arg1;
        m.arg2 = arg2;
        m.obj = obj;

        return m;
    }

    /**
     * 把消息回收到消息池
     */
    public void recycle() {
        if (isInUse()) {
            if (gCheckRecycle) {
                throw new IllegalStateException("This message cannot be recycled because it "
                        + "is still in use.");
            }
            return;
        }
        recycleUnchecked();
    }

    /**
     * 回收消息
     */
    void recycleUnchecked() {
        // Mark the message as in use while it remains in the recycled object pool.
        // Clear out all other details.
        flags = FLAG_IN_USE;
        what = 0;
        arg1 = 0;
        arg2 = 0;
        obj = null;
        when = 0;
        target = null;
        callback = null;
        data = null;

        synchronized (sPoolSync) {
            if (sPoolSize < MAX_POOL_SIZE) {
                next = sPool;
                sPool = this;
                sPoolSize++;
            }
        }
    }

    /**
     * 复制消息
     */
    public void copyFrom(Message o) {
        this.flags = o.flags & ~FLAGS_TO_CLEAR_ON_COPY_FROM;
        this.what = o.what;
        this.arg1 = o.arg1;
        this.arg2 = o.arg2;
        this.obj = o.obj;

//        if (o.data != null) {
//            this.data = (Bundle) o.data.clone();
//        } else {
//            this.data = null;
//        }
    }

    /**
     * 返回消息处理的延迟时间
     */
    public long getWhen() {
        return when;
    }

    public void setTarget(Handler target) {
        this.target = target;
    }

    /**
     * 获取处理消息的 {@link Handler Handler}
     */
    public Handler getTarget() {
        return target;
    }

    /**
     * 获取处理消息的callback
     */
    public Runnable getCallback() {
        return callback;
    }

    /**
     * 获得message附带的数据
     *
     * @return
     */
    public Map getData() {
        if (data == null) {
            data = new HashMap<String, Object>();
        }

        return data;
    }


    /**
     * 设置message附带的数据
     *
     * @see #getData()
     */
    public void setData(Map data) {
        this.data = data;
    }

    /**
     * 发送消息到指定的handler
     */
    public void sendToTarget() {
        target.sendMessage(this);
    }

    /*package*/ boolean isInUse() {
        return ((flags & FLAG_IN_USE) == FLAG_IN_USE);
    }

    /*package*/ void markInUse() {
        flags |= FLAG_IN_USE;
    }


    @Override
    public String toString() {
        return toString(System.currentTimeMillis());
    }

    String toString(long now) {
        StringBuilder b = new StringBuilder();
        b.append("{ when=");
        b.append((when - now));

        if (target != null) {
            if (callback != null) {
                b.append(" callback=");
                b.append(callback.getClass().getName());
            } else {
                b.append(" what=");
                b.append(what);
            }

            if (arg1 != 0) {
                b.append(" arg1=");
                b.append(arg1);
            }

            if (arg2 != 0) {
                b.append(" arg2=");
                b.append(arg2);
            }

            if (obj != null) {
                b.append(" obj=");
                b.append(obj);
            }

            b.append(" target=");
            b.append(target.getClass().getName());
        } else {
            b.append(" barrier=");
            b.append(arg1);
        }

        b.append(" }");
        return b.toString();
    }

}
