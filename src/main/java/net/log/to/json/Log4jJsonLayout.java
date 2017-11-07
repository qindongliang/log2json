package net.log.to.json;

import com.alibaba.fastjson.JSONObject;
import net.tools.HostData;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.LoggingEvent;

import java.util.Map;
import java.util.TimeZone;

/**
 * 自定义的Json输出格式
 * Created by QinDongLiang on 2017/9/13.
 */
public class Log4jJsonLayout extends Layout {



    //组装所有的json数据
    private JSONObject kv;
    //获取主机名，如果不能获取，就设置为未知host
    private String hostName=new HostData().getHostName();
    final TimeZone UTC = TimeZone.getTimeZone("UTC");
    //转成UTC时间，符合logstash的转化标准，能直接插入到es里面
    final FastDateFormat ft = FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", UTC);
    //业务类型
    private String logType;

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public String format(LoggingEvent event) {

        kv=new JSONObject();
        kv.put("@timestamp",ft.format(event.getTimeStamp()));//log发生的时间
        kv.put("source_host",hostName);//运行程序的机器名
        kv.put("thread_name",event.getThreadName());//线程名
        kv.put("level",event.getLevel().toString());//log级别
//        kv.put("logger_name",event.getLoggerName());//logger_name
//        kv.put("file",event.getLocationInformation().getFileName());//file_name
        kv.put("line_number",event.getLocationInformation().getLineNumber());//line_number
        kv.put("class",event.getLocationInformation().getClassName());//class_name
        kv.put("method",event.getLocationInformation().getMethodName());//method_name
        kv.put("logType",logType);

        if(event.getThrowableInformation()!=null){
          String exception= ExceptionUtils.getStackTrace(event.getThrowableInformation().getThrowable());
            kv.put("exception",exception);//异常信息
        }

        //兼容处理自定义的字段
        if(event.getMessage() instanceof Map) {
            Map<String, Object> msgMap = (Map) event.getMessage();
            for (Map.Entry<String, Object> mkv : msgMap.entrySet()) {
                kv.put(mkv.getKey(), mkv.getValue());
            }
        }else{
            kv.put("message", event.getRenderedMessage());
        }

        return kv.toString()+"\n";
    }

    public boolean ignoresThrowable() {
        return false;
    }

    public void activateOptions() {

    }
}
