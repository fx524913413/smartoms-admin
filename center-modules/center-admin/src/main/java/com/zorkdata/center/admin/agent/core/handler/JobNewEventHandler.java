package com.zorkdata.center.admin.agent.core.handler;

import com.alibaba.fastjson.JSON;
import com.zorkdata.center.admin.agent.core.async.Handler;
import com.zorkdata.center.admin.agent.core.async.Looper;
import com.zorkdata.center.admin.agent.core.async.Message;
import com.zorkdata.center.admin.biz.SaltJobBiz;
import com.zorkdata.center.admin.entity.SaltJob;
import com.zorkdata.center.admin.util.SpringUtil;
import com.zorkdata.center.common.salt.netapi.event.JobNewEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @description: Job新建时的Handler
 * @author: zhuzhigang
 * @create: 2018/5/30 13:26
 */
public class JobNewEventHandler extends Handler {
    private static final Logger logger = LoggerFactory.getLogger(JobNewEventHandler.class);
    private static final String CMD_EXEC_CODE = "cmd.exec_code";
    private static final String CP_GET_FILE = "cp.get_file";
    private static final String EVENT = "event";
    private SimpleDateFormat format0 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");

    public JobNewEventHandler(Looper looper) {
        super(looper);
    }

    @Override
    public void handleMessage(Message msg) {
        SaltJobBiz saltJobBiz = SpringUtil.getBean(SaltJobBiz.class);
        Map map = msg.getData();
        JobNewEvent jobNewEvent = (JobNewEvent) map.get(EVENT);
        final JobNewEvent.Data data = jobNewEvent.getData();
        if (data.getFun().contentEquals(CMD_EXEC_CODE) || data.getFun().contentEquals(CP_GET_FILE)) {
            SaltJob saltJob;
            int i = 3;
            while (i-- > 0) {
                saltJob = new SaltJob();
                saltJob.setJid(jobNewEvent.getJobId());
                saltJob = saltJobBiz.selectOne(saltJob);
                if (saltJob != null) {
                    saltJob.setArg(JSON.toJSONString(data.getArg()));
                    saltJob.setFun(data.getFun());
                    saltJob.setMinions(JSON.toJSONString(data.getMinions()));
                    saltJob.setMissing(JSON.toJSONString(data.getMissing()));
                    Date date;
                    try {
                        date = format0.parse(data.getTimestamp());
                    } catch (ParseException e) {
                        e.printStackTrace();
                        date = new Date();
                    }
                    saltJob.setStamp(date);
                    saltJob.setTgt(JSON.toJSONString(data.getTgt()));
                    saltJob.setTgt_type(data.getTgt_type());
                    saltJob.setUser(data.getUser());
                    List<SaltJob> saltJobList = new ArrayList<>();
                    saltJobList.add(saltJob);
                    saltJobBiz.insertBatch(saltJobList);
                    break;
                } else {
                    logger.debug("数据库SaltJob没有数据");
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        logger.error("sleep 异常",e);
                    }
                }
            }
        }
    }
}
