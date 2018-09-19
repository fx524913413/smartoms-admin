//package com.zorkdata.center.admin.rpc;
//
//import com.smartoms.automation.core.entiry.OperationExcuteHistoryArg;
//import com.smartoms.domain.MybatisEntityRepository;
//import org.dayatang.domain.InstanceFactory;
////import org.junit.Test;
//import org.openkoala.security.core.domain.SecurityAbstractEntity;
//
//import java.sql.Timestamp;
//import java.text.SimpleDateFormat;
//import java.util.*;
//import java.util.List;
//
///**
// * 操作历史
// * @author peichanglei
// * @date 2018/1/6
// *
// */
//public class OperationExcuteHistory extends SecurityAbstractEntity {
//    private static MybatisEntityRepository repository;
//    private Long id;
//    private Long extId;
//    /**
//     * 操作类型
//     * 0是内置操作 1是私有操作,2是快速执行脚本,3是快速执行SQL脚本
//     */
//    private String operationType;
//    private String name;
//    private String account;
//    /**
//     * 执行方式
//     *0是手动  1是自动
//     */
//    private String excuteType;
//    private Date beginTimeBak;
//    private Date endTimeBak;
//    private Date timeConsumingBak;
//    private String beginTime;
//    private String endTime;
//    private String timeConsuming;
//    /**
//     *0是失败  1是成功
//     */
//    private String status;
//    private Object msg;
//    /**
//     * 快速执行脚本,快速执行SQL脚本的内容
//     */
//    private String content;
//    /**
//     * 快速执行脚本,快速执行SQL脚本的执行账户
//     */
//    private String root;
//    /**
//     * 快速执行SQL脚本的选择数据库
//     */
//    private String dbName;
//    /**
//     * 执行主机
//     */
//    private String excuteHost;
//
//    public static List<OperationExcuteHistory> findAll(){
//        List<OperationExcuteHistory> findAll = getRepository().createNamedQuery(OperationExcuteHistory.class, "findAll").list();
//        for (OperationExcuteHistory item : findAll) {
//            if("0".equals(item.getExcuteType())){
//                item.setExcuteType("手动");
//            }else{
//                item.setExcuteType("自动");
//            }
//
//            if("0".equals(item.getOperationType())){
//                item.setOperationType("内置");
//            }else{
//                item.setOperationType("私有");
//            }
//
//            if("0".equals(item.getStatus())){
//                item.setStatus("异常");
//            }else{
//                item.setStatus("成功");
//            }
//            //后期修改时间保存的格式,是处理好的字符串,还是原始时间毫秒
//            item.setBeginTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(item.getBeginTimeBak().getTime()));
//            item.setEndTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(item.getEndTimeBak()));
//            item.setTimeConsuming(formatDuring(item.getTimeConsumingBak().getTime()));
//        }
//
//        return findAll;
//    }
//    @Override
//    public void save(){
//        super.save();
//    }
//
//
//    public void saveOperationExcuteHistory(String status, Long id, String name, String operationType, String excuteType, String account, Date beginTime, Date endTime, Map<String, String> args,Object msg){
//        OperationExcuteHistory operationExcuteHistory = new OperationExcuteHistory();
//        save(status,id,name,operationType,excuteType,account,beginTime,endTime,msg,operationExcuteHistory);
//        //保存执行参数
//        OperationExcuteHistoryArg.saveArgs(Integer.parseInt(operationExcuteHistory.getId().toString()),args);
//    }
//
//    public void saveBuildInExcuteHistory(String status, Long id, String name, String operationType, String excuteType, String account, Date beginTime, Date endTime, List<Map<String, Object>> list,Object msg){
//        OperationExcuteHistory operationExcuteHistory = new OperationExcuteHistory();
//        save(status,id,name,operationType,excuteType,account,beginTime,endTime,msg,operationExcuteHistory);
//
//        //保存执行参数
//        OperationExcuteHistoryArg  arg;
//        for (Map<String, Object> item : list) {
//            arg = new OperationExcuteHistoryArg();
//            arg.setArg(item.get("arg").toString());
//            arg.setArgValue(item.get("value").toString());
//            arg.setDisplayName(item.get("displayName").toString());
//            arg.setExtId(Integer.parseInt(operationExcuteHistory.getId().toString()));
//            arg.save();
//        }
//    }
//
//    public void saveFastExcuteHistory(String status,Integer id, String name,Date beginTimeBak, Date endTimeBak, String excuteType,String operationType,String account,String content, Object msg) {
//        Timestamp consuming = new Timestamp(endTimeBak.getTime() - beginTimeBak.getTime());
//        getRepository().createNamedQuery(OperationExcuteHistory.class,"saveFastExcuteHistory").setParameters(status,id,name,beginTimeBak,endTimeBak,consuming,excuteType,operationType,account,content,msg).executeUpdate();
//    }
//
//    public void saveSqlExcuteHistory(String status,Integer id, String name,Date beginTimeBak, Date endTimeBak, String excuteType,String operationType,String account,String content, Object msg,String database) {
//        Timestamp consuming = new Timestamp(endTimeBak.getTime() - beginTimeBak.getTime());
//        getRepository().createNamedQuery(OperationExcuteHistory.class,"saveSqlExcuteHistory").setParameters(status,id,name,beginTimeBak,endTimeBak,consuming,excuteType,operationType,account,content,msg,database).executeUpdate();
//    }
//
//    public void save(String status, Long id, String name, String operationType, String excuteType, String account, Date beginTime, Date endTime,Object msg,OperationExcuteHistory operationExcuteHistory){
//        operationExcuteHistory.setBeginTimeBak(beginTime);
//        operationExcuteHistory.setEndTimeBak(endTime);
//        operationExcuteHistory.setTimeConsumingBak(new Timestamp(endTime.getTime() - beginTime.getTime()));
//        operationExcuteHistory.setAccount(account);
//        operationExcuteHistory.setExcuteType(excuteType);
//        operationExcuteHistory.setExtId(id);
//        operationExcuteHistory.setName(name);
//        operationExcuteHistory.setOperationType(operationType);
//        operationExcuteHistory.setStatus(status);
//        operationExcuteHistory.setMsg(msg.toString());
//        operationExcuteHistory.save();
//    }
//
//    public static OperationExcuteHistory findById(Integer id) {
//        OperationExcuteHistory operationExcuteHistory = getRepository().createNamedQuery(OperationExcuteHistory.class, "findById").setParameters(id).singleResult();
//        operationExcuteHistory.setBeginTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(operationExcuteHistory.getBeginTimeBak().getTime()));
//        operationExcuteHistory.setEndTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(operationExcuteHistory.getEndTimeBak()));
//        operationExcuteHistory.setTimeConsuming(formatDuring(operationExcuteHistory.getTimeConsumingBak().getTime()));
//        return operationExcuteHistory;
//    }
//
//    private static String formatDuring(long mss) {
//        long days = mss / (1000 * 60 * 60 * 24);
//        long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
//        long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
//        double seconds = (mss % (1000 * 60)) / 1000 + (mss % 1000)/1000.0;
//        if(minutes == 0){
//            return seconds + "s " ;
//        }
//        if(hours == 0){
//            return minutes + "m " + seconds + "s ";
//        }
//        if(days == 0){
//            return hours + "h " + minutes + "m " + seconds + "s " ;
//        }
//        return days + "day " + hours + "h " + minutes + "m " + seconds + "s " ;
//    }
//
//    public static MybatisEntityRepository getRepository() {
//        if (repository == null) {
//            repository = InstanceFactory.getInstance(MybatisEntityRepository.class);
//        }
//        return repository;
//    }
//
//    public Long getExtId() {
//        return extId;
//    }
//
//    public void setExtId(Long extId) {
//        this.extId = extId;
//    }
//
//    @Override
//    public Long getId() {
//        return id;
//    }
//
//    @Override
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getAccount() {
//        return account;
//    }
//
//    public void setAccount(String account) {
//        this.account = account;
//    }
//
//    public String getOperationType() {
//        return operationType;
//    }
//
//    public void setOperationType(String operationType) {
//        this.operationType = operationType;
//    }
//
//    public String getExcuteType() {
//        return excuteType;
//    }
//
//    public void setExcuteType(String excuteType) {
//        this.excuteType = excuteType;
//    }
//
//    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }
//
//    public Date getBeginTimeBak() {
//        return beginTimeBak;
//    }
//
//    public void setBeginTimeBak(Date beginTimeBak) {
//        this.beginTimeBak = beginTimeBak;
//    }
//
//    public Date getEndTimeBak() {
//        return endTimeBak;
//    }
//
//    public void setEndTimeBak(Date endTimeBak) {
//        this.endTimeBak = endTimeBak;
//    }
//
//    public Date getTimeConsumingBak() {
//        return timeConsumingBak;
//    }
//
//    public void setTimeConsumingBak(Date timeConsumingBak) {
//        this.timeConsumingBak = timeConsumingBak;
//    }
//
//    public String getBeginTime() {
//        return beginTime;
//    }
//
//    public void setBeginTime(String beginTime) {
//        this.beginTime = beginTime;
//    }
//
//    public String getEndTime() {
//        return endTime;
//    }
//
//    public void setEndTime(String endTime) {
//        this.endTime = endTime;
//    }
//
//    public String getTimeConsuming() {
//        return timeConsuming;
//    }
//
//    public void setTimeConsuming(String timeConsuming) {
//        this.timeConsuming = timeConsuming;
//    }
//
//    @Override
//    public String[] businessKeys() {
//        return new String[0];
//    }
//
//    public Object getMsg() {
//        return msg;
//    }
//
//    public void setMsg(Object msg) {
//        this.msg = msg;
//    }
//
//    public String getContent() {
//        return content;
//    }
//
//    public void setContent(String content) {
//        this.content = content;
//    }
//
//    public String getRoot() {
//        return root;
//    }
//
//    public void setRoot(String root) {
//        this.root = root;
//    }
//
//    public String getDbName() {
//        return dbName;
//    }
//
//    public void setDbName(String dbName) {
//        this.dbName = dbName;
//    }
//
//    public String getExcuteHost() {
//        return excuteHost;
//    }
//
//    public void setExcuteHost(String excuteHost) {
//        this.excuteHost = excuteHost;
//    }
//
////    @Test
////    public void test1(){
////        Map<String,String> map = new HashMap<>();
////        map.put("a","1");
////        map.put("b","2");
////        map.put("c","3");
////        System.out.println(map.toString());
////        System.out.println(JSON.toJSONString(map));
////        System.out.println(JSON.toJSONString(map.toString()));
////    }
//}
//
