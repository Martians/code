package com.data.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="settings")
public class Settings {
    private String address;

    /** 必须设置set函数，否则无法从属性文件中，读取配置并设置到这里来 */
    public void setAddress(String address) { this.address = address; }
    public String getAddress() { return address; }
}
