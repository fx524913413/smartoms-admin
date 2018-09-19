package com.zorkdata.center.admin.agent.core.async;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/5/30 12:58
 */
public class Main {
    public static void main(String[] args) {
        LooperThread looperThread = new LooperThread("test");
        looperThread.start();

        Looper looper = looperThread.getLooper();
        TestHandler testHandler = new TestHandler(looper);


    }
}
