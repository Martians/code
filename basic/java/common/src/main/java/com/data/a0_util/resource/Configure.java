package com.data.a0_util.resource;

import java.io.*;
import java.net.URL;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;

public class Configure {

    public static String path = "/config.properties";
    private Properties props;

    public Configure() {
        load(path);
    }

    public Configure(String file) {
        load(file);
    }

    /**
     * 将配置文件，一次性load到内存
     */
    public void load(String file) {
        InputStream is = null;
        boolean resource = true;

        try {
            if (resource) {
                /**
                 * 文件在jar包中时，必须用此种方式解析
                 */
                is = Configure.class.getResourceAsStream(file);
            } else {
                is = new FileInputStream(new File(file));
            }
            if (is == null) {
                throw new FileNotFoundException(file);
            }

            props = new Properties();
            props.load(is);

        } catch (Exception e) {
            System.out.println("get resource failed: " + e);

        } finally {
            if (is == null) {
                try{
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getProperty(String key) {
        return props.getProperty(key);
    }
    public Properties getProperties() { return props; }

    public static void main(String[] args) {
        /** 读取配置文件 */
        Configure config = new Configure(path);
        System.out.println(config.getProperty("topic"));

        /** Enumeration 方式遍历 */
        Properties prop = config.getProperties();
        Enumeration iterator = prop.propertyNames();
        while(iterator.hasMoreElements()){
            String key = (String)iterator.nextElement();
            System.out.println(key);
        }

        /** map方式遍历 */
        for (Map.Entry<Object, Object> entry : prop.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }

    }
}
