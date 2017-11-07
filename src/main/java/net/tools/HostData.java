package net.tools;

import java.net.UnknownHostException;

/**
 * 获取主机名
 * Created by QinDongLiang on 2017/9/13.
 */
public class HostData {

    //设置主机名
    public String hostName;

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public HostData() {
        try {

            this.hostName = java.net.InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            setHostName("unknown-host");
        }
    }

}
