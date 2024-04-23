package com.manil.dailywise.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class HostUtil {

    public String getBaseUrl(){
        try {
            InetAddress ip = InetAddress.getLocalHost();
            return ip.getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

}
