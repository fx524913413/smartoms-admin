package com.zorkdata.center.admin.agent.core;

import com.google.gson.reflect.TypeToken;
import com.zorkdata.center.admin.agent.core.async.LooperThread;
import com.zorkdata.center.admin.agent.core.async.Message;
import com.zorkdata.center.admin.agent.core.handler.JobNewEventHandler;
import com.zorkdata.center.admin.agent.core.handler.JobReturnEventHandler;
import com.zorkdata.center.admin.agent.core.handler.MinionStartEventHandler;
import com.zorkdata.center.admin.agent.core.handler.RunnerReturnEventHandler;
import com.zorkdata.center.admin.vo.SaltProperties;
import com.zorkdata.center.common.salt.netapi.AuthModule;
import com.zorkdata.center.common.salt.netapi.calls.LocalAsyncResult;
import com.zorkdata.center.common.salt.netapi.calls.LocalCall;
import com.zorkdata.center.common.salt.netapi.calls.RunnerCall;
import com.zorkdata.center.common.salt.netapi.calls.WheelResult;
import com.zorkdata.center.common.salt.netapi.calls.modules.Cmd;
import com.zorkdata.center.common.salt.netapi.calls.modules.Grains;
import com.zorkdata.center.common.salt.netapi.calls.modules.State;
import com.zorkdata.center.common.salt.netapi.calls.runner.Jobs;
import com.zorkdata.center.common.salt.netapi.calls.runner.Manage;
import com.zorkdata.center.common.salt.netapi.calls.wheel.Key;
import com.zorkdata.center.common.salt.netapi.client.SaltClient;
import com.zorkdata.center.common.salt.netapi.datatypes.Event;
import com.zorkdata.center.common.salt.netapi.datatypes.target.MinionList;
import com.zorkdata.center.common.salt.netapi.datatypes.target.Target;
import com.zorkdata.center.common.salt.netapi.event.EventListener;
import com.zorkdata.center.common.salt.netapi.event.*;
import com.zorkdata.center.common.salt.netapi.exception.SaltException;
import com.zorkdata.center.common.salt.netapi.results.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.websocket.CloseReason;
import java.io.Closeable;
import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @description: SaltApi工具类，为单独的一个Master提供接口调用
 * @author: zhuzhigang
 * @create: 2018/4/9 13:40
 */
public class SaltApi implements Closeable {
    private static final Logger logger = LoggerFactory.getLogger(SaltApi.class);
    private static final String MINIONS = "minions";
    private static final String MINIONS_PRE = "minions_pre";
    private static final String GRAINS_ITEMS = "grains.items";
    private static final String CMD_EXEC_CODE = "cmd.exec_code";
    private static final String CP_GET_FILE = "cp.get_file";
    private static final String EVENT_SEND = "event.send";
    private static final String EVENT = "event";
    private static final String COMPUTERIP = "computerIP";
    private static final String PORTS = "ports";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    public static Map<String, Map<String, Integer>> saltStatusMap = new ConcurrentHashMap<>();
    private static Map<String, SaltApi> saltApiMap = new HashMap<>();
    private JobReturnEventHandler jobReturnEventHandler = null;
    private RunnerReturnEventHandler runnerReturnEventHandler = null;
    private MinionStartEventHandler minionStartEventHandler = null;
    private JobNewEventHandler jobNewEventHandler = null;
    private String computerIP;
    private int ports;
    private String username;
    private String password;
    private SaltClient client;
    private EventStream eventStream;


    private SaltApi(String computerIP, int ports, String username, String password) throws SaltException {
        LooperThread jobReturnEventThread = new LooperThread("jobReturnEvent");
        jobReturnEventThread.start();
        LooperThread jobNewEventThread = new LooperThread("jobNewEvent");
        jobNewEventThread.start();
        LooperThread minionStartEventThread = new LooperThread("minionStartEvent");
        minionStartEventThread.start();
        LooperThread runnerReturnEventThread = new LooperThread("runnerReturnEvent");
        runnerReturnEventThread.start();
        jobReturnEventHandler = new JobReturnEventHandler(jobReturnEventThread.getLooper());
        jobNewEventHandler = new JobNewEventHandler(jobNewEventThread.getLooper());
        minionStartEventHandler = new MinionStartEventHandler(minionStartEventThread.getLooper());
        runnerReturnEventHandler = new RunnerReturnEventHandler(runnerReturnEventThread.getLooper());


        this.computerIP = computerIP;
        this.ports = ports;
        String url = "http://" + computerIP + ":" + ports;
        this.username = username;
        this.password = password;
        String key = url + "_" + username + "_" + password;
        this.client = new SaltClient(URI.create(url));
        try {
            token();
            this.eventStream = new EventStream(this.client.getConfig());
            ManageStatusEventListener manageStatusEventListener = new ManageStatusEventListener(this, key);
            eventStream.addEventListener(manageStatusEventListener);
        } catch (SaltException e) {
            logger.error("[zorkdata]" + computerIP + "websocket初始化失败", e);
            throw e;
        }
    }

    public static SaltApi getSaltApi(String computerip, int ports, String username, String password) throws SaltException {
        String url = "http://" + computerip + ":" + ports;
        String key = url + "_" + username + "_" + password;
        SaltApi saltApi = null;
        try {
            if (saltApiMap != null) {
                if (saltApiMap.containsKey(key)) {
                    if (saltApiMap.get(key).eventStream != null && !saltApiMap.get(key).eventStream.isEventStreamClosed()) {
                        return saltApiMap.get(key);
                    }
                }
                saltApi = new SaltApi(computerip, ports, username, password);
                saltApiMap.put(key, saltApi);
            }
        } catch (Exception e) {
            logger.error("获取saltapi失败" + computerip, e);
            throw e;
        }
        return saltApi;
    }


    private static void onRunnerReturn(RunnerReturnEvent rre, String computerip, int ports, String username, String password) {
        Message message = new Message();
        Map<String, Object> map = new HashMap<>(16);
        map.put(EVENT, rre);
        map.put(COMPUTERIP, computerip);
        map.put(PORTS, ports);
        map.put(USERNAME, username);
        map.put(PASSWORD, password);
        message.setData(map);
        try {
            SaltApi.getSaltApi(computerip, ports, username, password).runnerReturnEventHandler.sendMessage(message);
        } catch (SaltException e) {
            e.printStackTrace();
        }
    }

    private static void onJobReturn(JobReturnEvent jre, String computerip, int ports, String username, String password) {
        Message message = new Message();
        Map<String, Object> map = new HashMap<>(16);
        map.put(EVENT, jre);
        map.put(COMPUTERIP, computerip);
        map.put(PORTS, ports);
        map.put(USERNAME, username);
        map.put(PASSWORD, password);
        message.setData(map);
        if (jre.getData().getFun().contentEquals(GRAINS_ITEMS) || jre.getData().getFun().contentEquals(CMD_EXEC_CODE) ||
                jre.getData().getFun().contentEquals(CP_GET_FILE) || jre.getData().getFun().contentEquals(EVENT_SEND)) {
            try {
                SaltApi.getSaltApi(computerip, ports, username, password).jobReturnEventHandler.sendMessage(message);
            } catch (SaltException e) {
                e.printStackTrace();
            }
        }
    }

    private static void onJobNew(JobNewEvent jne, String computerIP, int ports, String username, String password) {
        Message message = new Message();
        Map<String, Object> map = new HashMap<>(16);
        map.put(EVENT, jne);
        map.put(COMPUTERIP, computerIP);
        map.put(PORTS, ports);
        map.put(USERNAME, username);
        map.put(PASSWORD, password);
        message.setData(map);
        try {
            SaltApi.getSaltApi(computerIP, ports, username, password).jobNewEventHandler.sendMessage(message);
        } catch (SaltException e) {
            e.printStackTrace();
        }
    }

    private static void onMinionStartReturn(MinionStartEvent mse, String computerip, int ports, String username, String password) {
        Message message = new Message();
        Map<String, Object> map = new HashMap<>(16);
        map.put(EVENT, mse);
        map.put(COMPUTERIP, computerip);
        map.put(PORTS, ports);
        map.put(USERNAME, username);
        map.put(PASSWORD, password);
        message.setData(map);
        try {
            SaltApi.getSaltApi(computerip, ports, username, password).minionStartEventHandler.sendMessage(message);
        } catch (SaltException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, Map<String, Integer>> getSaltStatusMap() {
        return saltStatusMap;
    }

    /**
     * token
     */
    private void token() throws SaltException {
        try {
            // Get a login token
            client.login(username, password, AuthModule.PAM);
        } catch (Exception e) {
            logger.error("获取token失败", e);
            throw e;
        }
    }

    /**
     * 获取包括认证、未认证salt主机
     *
     * @param type 类型( minions 或者 minions_pre)
     */
    public List<String> listAllKey(String type) throws SaltException {
        try {
            // List accepted and pending minion keys
            WheelResult<Result<Key.Names>> keyResults = Key.listAll().callSync(
                    client, username, password, AuthModule.PAM);
            Result<Key.Names> resultKeys = keyResults.getData().getResult();
            if (resultKeys.result().isPresent()) {
                Key.Names keys = resultKeys.result().get();

                if (MINIONS.equals(type)) {
                    return keys.getMinions();
                } else if (MINIONS_PRE.equals(type)) {
                    return keys.getUnacceptedMinions();
                }
            }
        } catch (Exception e) {
            logger.error("获取获取包括认证、未认证salt主机失败", e);
            throw e;
        }
        return null;
    }

    /**
     * 删除salt key
     *
     * @param nodeName 需要输出的key
     */
    public boolean deleteKey(String nodeName) throws SaltException {
        WheelResult<Result<Object>> result = Key.delete(nodeName).callSync(client, username, password, AuthModule.PAM);
        return result.getData().isSuccess();
    }

    public boolean acceptKey(String nodeName) throws SaltException {
        WheelResult<Result<Object>> result = Key.accept(nodeName).callSync(client, username, password, AuthModule.PAM);
        return result.getData().isSuccess();
    }

    /**
     * 通过jid获取执行结果
     *
     * @param jid jobID
     */
    public Map<String, Object> saltRunner(String jid) throws SaltException {
        Result<Map<String, Object>> result = Jobs.lookupJid(jid).callSync(client, username, password, AuthModule.PAM);
        if (result.result().isPresent()) {
            return result.result().get();
        } else {
            return null;
        }
    }

    /**
     * 获取运行中的任务
     */
    public Map<String, Object> saltRunningJobs() throws SaltException {
        Result<Map<String, Object>> result = Jobs.salt_running_jobs().callSync(client, username, password, AuthModule.PAM);
        if (result.result().isPresent()) {
            return result.result().get();
        } else {
            return null;
        }
    }

    /**
     * 同步调用执行命令
     *
     * @param minions minionID列表
     * @param lang    语言
     * @param code    代码
     */
    public Map<String, Result<String>> execCode(List<String> minions, String lang, String code, LinkedHashMap<String, Object> args) throws InterruptedException, SaltException, IOException {
        if (args == null) {
            args = new LinkedHashMap<>();
        }
        Target<List<String>> minionList = new MinionList(minions);
        return Cmd.execCode(lang, code, args).callSync(client, minionList, username, password, AuthModule.PAM);
    }

    /**
     * 异步调用执行命令
     *
     * @param minions minionid列表
     * @param lang    语言
     * @param code    代码
     */
    public Map<String, List<String>> asyncExecCode(List<String> minions, String lang, String code, LinkedHashMap<String, Object> args) throws InterruptedException, SaltException, IOException {
        if (args == null) {
            args = new LinkedHashMap<>();
        }
        Target<List<String>> minionList = new MinionList(minions);
        LocalAsyncResult<String> results = Cmd.execCode(lang, code, args).callAsync(client,
                minionList,
                username,
                password,
                AuthModule.PAM);
        Map<String, List<String>> map = new HashMap<>(16);
        map.put(results.getJid(), results.getMinions());
        return map;
    }

    public Map<String, Result<Map<String, State.ApplyResult>>> stateApply(List<String> minions,List<String> mods) throws SaltException {
        Target<List<String>> minionList = new MinionList(minions);
        return State.apply(mods).callSync(client, minionList, username, password, AuthModule.PAM);
    }
    /**
     * 查询agent在线
     */
    private void manageUp() throws SaltException, InterruptedException {
        Manage.up().callAsync(client, username, password, AuthModule.PAM);
    }

    /**
     * 查询agent离线，不移除key
     */
    private void manageDown() throws SaltException, InterruptedException {
        Manage.down(false).callAsync(client, username, password, AuthModule.PAM);
    }

    public void manageStatus() throws SaltException, InterruptedException {
        Manage.status().callAsync(client, username, password, AuthModule.PAM);
    }

    public void asyncGrainsItems(List<String> minions) throws SaltException {
        Target<List<String>> minionList = new MinionList(minions);
        Grains.items(false).callAsync(client, minionList, username, password, AuthModule.PAM);
    }

    public String getAgentPath(String minionID,String destPath) throws SaltException {
        List<String> minions = new ArrayList<>();
        minions.add(minionID);
        Target<List<String>> minionList = new MinionList(minions);
        Map<String, Result<Map<String, Object>>> saltpaths = Grains.item(false, "saltpath","kernel").callSync(client, minionList, username, password, AuthModule.PAM);
        if(saltpaths.get(minionID).result().isPresent()){
            Result<Map<String, Object>> sysAndPath = saltpaths.get(minionID);
            Map<String, Object> stringObjectMap = sysAndPath.result().get();
            String sys = stringObjectMap.get("kernel").toString();
            String path = stringObjectMap.get("saltpath").toString();
            if(sys.equals("Linux")){
               String[] path4Linux=path.split("lib");
               return path4Linux[0]+"3rdplugins/"+destPath+"/"+ destPath + ".yml";
            }
            if(sys.equals("Windows")){
                String[] path4Windows=path.split("bin");
                return path4Windows[0]+"3rdplugins\\"+destPath+"\\"+ destPath + ".yml";
            }
        }
        return null;
    }

    @Override
    public void close() throws IOException {
        eventStream.close();
    }

    private String getComputerIP() {
        return computerIP;
    }


    private int getPorts() {
        return ports;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Map<String, String> sendFile(String scrPath, String destPath, List<String> minionIDList) throws Exception {
        System.out.println("saltapisendfile");
        Map<String, String> result = new HashMap<>(16);
        Target<List<String>> minionList = new MinionList(minionIDList);
        LinkedHashMap<String, Object> args = new LinkedHashMap<>();
        Map<String, Result<String>> response;
        Map<String, Result<String>> response2;
        System.out.println(scrPath);
        System.out.println(destPath);
        try {
            new RunnerCall<>("fileserver.update", Optional.empty(), new TypeToken<List<String>>() {}).callSync(this.client);
            response = new LocalCall<>(CP_GET_FILE, Optional.of(Arrays.asList(scrPath, destPath)),
                    Optional.of(args), new TypeToken<String>() {
            }).callSync(this.client, minionList);
        } catch (SaltException e) {
            e.printStackTrace();
            throw new Exception("执行sendFile时发生异常");
        }
        response.forEach((k, v) -> result.put(k, v.result().isPresent() ? v.result().get() : ""));
        return result;
    }

    /**
     * 通过git异步分发文件给Agent
     *
     * @param scrPath      源路径
     * @param destPath     目标路径
     * @param minionIDList minionID列表
     */
    public Map<String, List<String>> asyncSendFile(String scrPath, String destPath, List<String> minionIDList) throws Exception {
        //本方法暂时一次仅处理一个Agent,也就是说targets length为1
        Target<List<String>> minionList = new MinionList(minionIDList);

        LinkedHashMap<String, Object> args = new LinkedHashMap<>();

        LocalCall<String> call = new LocalCall<>(CP_GET_FILE, Optional.of(Arrays.asList(scrPath, destPath)),
                Optional.of(args), new TypeToken<String>() {
        });
        try {
            LocalAsyncResult<String> result = call.callAsync(this.client, minionList);
            Map<String, List<String>> map = new HashMap<>(16);
            map.put(result.getJid(), result.getMinions());
            return map;
        } catch (SaltException e) {
            throw new Exception("执行asyncSendFile时发生异常", e);
        }
    }

    /**
     * Simple Event ListenerClient
     */
    private class ManageStatusEventListener implements EventListener {
        final String computerIP;
        final int ports1;
        final String username1;
        final String password1;
        final String key;
        CloseReason closeReason;

        ManageStatusEventListener(SaltApi saltApi, String key) {
            this.computerIP = saltApi.getComputerIP();
            this.ports1 = saltApi.getPorts();
            this.username1 = saltApi.getUsername();
            this.password1 = saltApi.getPassword();
            this.key = key;
        }

        @Override
        public void notify(Event event) {
            try {
                Optional<JobReturnEvent> jobReturnEvent = JobReturnEvent.parse(event);
                Optional<RunnerReturnEvent> runnerReturnEvent = RunnerReturnEvent.parse(event);
                Optional<MinionStartEvent> minionStartEvent = MinionStartEvent.parse(event);
                Optional<JobNewEvent> jobNewEvent = JobNewEvent.parse(event);
                if (jobReturnEvent.isPresent()) {
                    jobReturnEvent.ifPresent(e ->
                            onJobReturn(e, computerIP, ports1, username1, password1)
                    );
                } else if (runnerReturnEvent.isPresent()) {
                    runnerReturnEvent.ifPresent(e ->
                            onRunnerReturn(e, computerIP, ports1, username1, password1)
                    );
                } else if (minionStartEvent.isPresent()) {
                    minionStartEvent.ifPresent(e ->
                            onMinionStartReturn(e, computerIP, ports1, username1, password1)
                    );
                } else if (jobNewEvent.isPresent()) {
                    jobNewEvent.ifPresent(e ->
                            onJobNew(e, computerIP, ports1, username1, password1)
                    );
                }
            } catch (Exception e) {
                logger.error("处理event错误", e);
            }
        }

        @Override
        public void eventStreamClosed(CloseReason closeReason) {
            this.closeReason = closeReason;
            eventStream.removeEventListener(this);
        }
    }

    public static void main(String[] args) throws InterruptedException, SaltException, IOException {
        List<String> minions = new ArrayList<>();
        minions.add("192.168.189.131");
        SaltApi saltApi = SaltApi.getSaltApi("192.168.1.94", 8000, "zork-omsapi", "zorkdata.8888");
        LinkedHashMap<String, Object> args1 = new LinkedHashMap<>();
        Map<String, Result<String>> sss = saltApi.execCode(minions, "python", "# coding=utf-8\n" +
                "from __future__ import print_function\n" +
                "from collections import OrderedDict\n" +
                "import pprint\n" +
                "import ctypes\n" +
                "import os\n" +
                "import platform\n" +
                "import sys\n" +
                "import json\n" +
                "import datetime\n" +
                "import psutil\n" +
                "\n" +
                "reload(sys)\n" +
                "sys.setdefaultencoding( \"utf-8\" )\n" +
                "\n" +
                "if platform.system() == 'Windows':\n" +
                "    import wmi\n" +
                "if platform.system() == 'Linux':\n" +
                "    from subprocess import Popen, PIPE\n" +
                "    \n" +
                "def getCpuModel():\n" +
                "    if platform.system() == 'Windows':\n" +
                "        b = 'wmic cpu get name'\n" +
                "        output = os.popen(b)\n" +
                "        lines = output.readlines()\n" +
                "        str1 = lines[1]\n" +
                "        return str1.strip()\n" +
                "    else:\n" +
                "        CPUinfo = OrderedDict()\n" +
                "        procinfo = OrderedDict()\n" +
                "        nprocs = 0\n" +
                "        with open('/proc/cpuinfo') as f:\n" +
                "            for line in f:\n" +
                "                if not line.strip():\n" +
                "                    CPUinfo['proc%s' % nprocs] = procinfo\n" +
                "                    nprocs = nprocs + 1\n" +
                "                    procinfo = OrderedDict()\n" +
                "                else:\n" +
                "                    if len(line.split(':')) == 2:\n" +
                "                        procinfo[line.split(':')[0].strip()] = line.split(':')[1].strip()\n" +
                "                    else:\n" +
                "                        procinfo[line.split(':')[0].strip()] = ''\n" +
                "        model = ''\n" +
                "        for processor in CPUinfo.keys():\n" +
                "            model = CPUinfo[processor]['model name']\n" +
                "        return model\n" +
                "        \n" +
                "def getCpuCores():\n" +
                "    if platform.system() == 'Windows':\n" +
                "        def num_cores():\n" +
                "            b = 'wmic cpu get NumberOfCores'\n" +
                "            output = os.popen(b)\n" +
                "            lines = output.readlines()\n" +
                "            str2 = lines[1]\n" +
                "            return str2.strip()\n" +
                "\n" +
                "        def num_physicalcpu():\n" +
                "            b = 'wmic cpu get name'\n" +
                "            output = os.popen(b)\n" +
                "            lines = output.readlines()\n" +
                "            a = len(lines) - 2\n" +
                "            return a\n" +
                "\n" +
                "        return str(num_physicalcpu()) + \"*\" + str(num_cores())\n" +
                "    else:\n" +
                "        CPUinfo = OrderedDict()\n" +
                "        procinfo = OrderedDict()\n" +
                "        nprocs = 0\n" +
                "        with open('/proc/cpuinfo') as f:\n" +
                "            for line in f:\n" +
                "                if not line.strip():\n" +
                "                    CPUinfo['proc%s' % nprocs] = procinfo\n" +
                "                    nprocs = nprocs + 1\n" +
                "                    procinfo = OrderedDict()\n" +
                "                else:\n" +
                "                    if len(line.split(':')) == 2:\n" +
                "                        procinfo[line.split(':')[0].strip()] = line.split(':')[1].strip()\n" +
                "                    else:\n" +
                "                        procinfo[line.split(':')[0].strip()] = ''\n" +
                "        cores = 0\n" +
                "        for processor in CPUinfo.keys():\n" +
                "            cores = cores + 1\n" +
                "        return \"1*\" + str(cores)\n" +
                "\n" +
                "\n" +
                "def getMeminfo():\n" +
                "    if platform.system() == 'Windows':\n" +
                "        pc_mem = psutil.virtual_memory()\n" +
                "        div_gb_factor = (1024.0 ** 3)\n" +
                "        return str(int(round(float(pc_mem.total / div_gb_factor)))) + 'GB'\n" +
                "    else:\n" +
                "        meminfo = OrderedDict()\n" +
                "        with open('/proc/meminfo') as f:\n" +
                "            for line in f:\n" +
                "                meminfo[line.split(':')[0]] = line.split(':')[1].strip()\n" +
                "            totalkb = meminfo['MemTotal']\n" +
                "            totalkbNum = long(totalkb[:-2])\n" +
                "            if totalkbNum / 1024 % 1024 == 0:\n" +
                "                totalGB = str(totalkbNum / 1024 / 1024) + \"GB\"\n" +
                "            else:\n" +
                "                totalGB = str((totalkbNum / 1024 / 1024) + 1) + \"GB\"\n" +
                "        return totalGB\n" +
                "        \n" +
                "def getHostName():\n" +
                "    return platform.node()\n" +
                "    \n" +
                "def getDiskInfo():\n" +
                "    if platform.system() == 'Windows':\n" +
                "        c = wmi.WMI()\n" +
                "        arr = []\n" +
                "        for physical_disk in c.Win32_DiskDrive():\n" +
                "            arr.append(int(physical_disk.Size) / 1024 / 1024 / 1024)\n" +
                "        b = 0\n" +
                "        for i in range(len(arr)):\n" +
                "            b += arr[i]\n" +
                "        return str(b) + 'GB'\n" +
                "    else:\n" +
                "        st = os.statvfs('/')\n" +
                "        return str(st.f_bavail * st.f_frsize / 1024 / 1024 / 1024) + \"GB\"\n" +
                "        \n" +
                "def getIPList():\n" +
                "    allIPList = psutil.net_if_addrs()\n" +
                "    IPList = []\n" +
                "    try:\n" +
                "        for IPGroup in allIPList:\n" +
                "            for IP in allIPList[IPGroup]:\n" +
                "                if IP[0] == 2 and IP[1] != '127.0.0.1' and IP[1] != '0.0.0.0':\n" +
                "                    IPList.append(IP[1])\n" +
                "    except:\n" +
                "        pass\n" +
                "    return IPList\n" +
                "    \n" +
                "def getMACList():\n" +
                "    allIPList = psutil.net_if_addrs()\n" +
                "    IPList = []\n" +
                "    try:\n" +
                "        for IPGroup in allIPList:\n" +
                "            ipAndMacDict = {}\n" +
                "            singleIpList = []\n" +
                "            singleMacList = \"\"\n" +
                "            for IP in allIPList[IPGroup]:\n" +
                "                if IP[0] == 2 and IP[1] != '127.0.0.1' and IP[1] != '0.0.0.0':\n" +
                "                    singleIpList.append(IP[1])\n" +
                "                if IP[0] == -1:\n" +
                "                    singleMacList = IP[1]\n" +
                "            if len(singleIpList) > 0:\n" +
                "                IPList.append(singleMacList)\n" +
                "    except:\n" +
                "        pass\n" +
                "    return IPList\n" +
                "    \n" +
                "def getOSType():\n" +
                "    return platform.system()\n" +
                "    \n" +
                "def getOS():\n" +
                "    return platform.platform()\n" +
                "    \n" +
                "def getSerialNumber():\n" +
                "    if platform.system() == 'Windows':\n" +
                "        b = 'wmic bios get serialnumber'\n" +
                "        output = os.popen(b)\n" +
                "        lines = output.readlines()\n" +
                "        return lines[1].strip()\n" +
                "    else:\n" +
                "        data_dmi = getDmi()\n" +
                "        parsed_data_dmi = parseData(data_dmi)\n" +
                "        dmi = parseDmi(parsed_data_dmi)\n" +
                "        return dmi\n" +
                "        \n" +
                "def getDmi():\n" +
                "    p = Popen(['dmidecode'], stdout=PIPE)\n" +
                "    data = p.stdout.read()\n" +
                "    return data\n" +
                "    \n" +
                "def parseData(data):\n" +
                "    parsed_data = []\n" +
                "    new_line = ''\n" +
                "    data = [i for i in data.split('\\n') if i]\n" +
                "    for line in data:\n" +
                "        if line[0].strip():\n" +
                "            parsed_data.append(new_line)\n" +
                "            new_line = line + '\\n'\n" +
                "        else:\n" +
                "            new_line += line + '\\n'\n" +
                "    parsed_data.append(new_line)\n" +
                "    return [i for i in parsed_data if i]\n" +
                "\n" +
                "\n" +
                "def parseDmi(parsed_data):\n" +
                "    # dic = {}\n" +
                "    parsed_data = [i for i in parsed_data if i.startswith('System Information')]\n" +
                "    parsed_data = [i for i in parsed_data[0].split('\\n')[1:] if i]\n" +
                "    dmi_dic = dict([i.strip().split(':') for i in parsed_data])\n" +
                "    # dic['vender'] = dmi_dic['Manufacturer'].strip()\n" +
                "    # dic['product'] = dmi_dic['Product Name'].strip()\n" +
                "    # dic['serialNumber'] = dmi_dic['Serial Number'].strip()\n" +
                "    return dmi_dic['Serial Number'].strip()\n" +
                "    \n" +
                "# final result\n" +
                "def getResult():\n" +
                "    resultDict = {}\n" +
                "    resultDict.__setitem__(\"code\", \"000000\")\n" +
                "    resultDict.__setitem__(\"appsystem\", \"\")\n" +
                "    resultDict.__setitem__(\"hostname\", getHostName())\n" +
                "    resultDict.__setitem__(\"ip\", getIPList())\n" +
                "    resultDict.__setitem__(\"datatype\", \"pcserver\")\n" +
                "    resultDict.__setitem__(\"modulename\", \"主机\")\n" +
                "    datas = []\n" +
                "    data = {}\n" +
                "    data.__setitem__(\"CPU\", getCpuModel())\n" +
                "    data.__setitem__(\"CPUcores\", getCpuCores())\n" +
                "    data.__setitem__(\"memory\", getMeminfo())\n" +
                "    data.__setitem__(\"disk\", getDiskInfo())\n" +
                "    data.__setitem__(\"hostname\", getHostName())\n" +
                "    data.__setitem__(\"ip\", getIPList())\n" +
                "    data.__setitem__(\"mac\", getMACList())\n" +
                "    data.__setitem__(\"os\", getOS())\n" +
                "    data.__setitem__(\"osType\", getOSType())\n" +
                "    data.__setitem__(\"serialNumber\", getSerialNumber())\n" +
                "    datas.append(data)\n" +
                "    resultDict.__setitem__(\"data\", datas)\n" +
                "    return resultDict\n" +
                "    \n" +
                "if __name__ == \"__main__\":\n" +
                "    print(json.dumps(getResult(),ensure_ascii=False))", args1);
        saltApi.asyncGrainsItems(minions);
        saltApi.manageDown();
        saltApi.manageUp();
        saltApi.saltRunningJobs();
        saltApi.saltRunner("111");
        boolean aaa = saltApi.deleteKey("tttttttt");
        saltApi.acceptKey("192.168.1.92");
        saltApi.saltRunningJobs();
        if (aaa) {
            System.out.println(123);
        }
        Thread.sleep(10000000);
    }

}
