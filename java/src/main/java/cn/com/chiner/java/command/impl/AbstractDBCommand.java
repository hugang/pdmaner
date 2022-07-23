/*
 * Copyright 2019-2029 FISOK(www.fisok.cn).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.com.chiner.java.command.impl;

import cn.com.chiner.java.command.Command;
import cn.fisok.raw.kit.JdbcKit;
import cn.fisok.raw.kit.StringKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2021/6/12
 * @desc : 数据库相关操作命令的抽像
 */
public abstract class AbstractDBCommand<T> implements Command<T> {
    public static final String KEY_DRIVER_CLASS_NAME = "driver_class_name";
    public static final String KEY_URL = "url";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";

    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected String driverClassName;
    protected String url;
    protected String username;
    protected String password;
    private Connection dbConn = null;
    protected Map<String,String> extProps = new LinkedHashMap<>();

    public void init(Map<String,String> params){
        //如果有数据库连接了，就不要初始化了
        if(dbConn != null){
            return;
        }
        driverClassName = params.get(KEY_DRIVER_CLASS_NAME);
        url = params.get(KEY_URL);
        username = params.get(KEY_USERNAME);
        password = params.get(KEY_PASSWORD);
        if(url.indexOf("{and}")>0){
            url = url.replaceAll("\\{and\\}","&");
        }
        Iterator<String> iterator = params.keySet().iterator();
        while (iterator.hasNext()){
            String key = StringKit.nvl(iterator.next(),"");
            if(!inRemainProps(key)){
                String value = StringKit.nvl(params.get(key),"");
                extProps.put(key,value);
            }
        }
    }

    /**
     * 键值是否为保留值
     * @param key
     * @return
     */
    private boolean inRemainProps(String key){
        if(key.equalsIgnoreCase(KEY_DRIVER_CLASS_NAME)
            ||key.equalsIgnoreCase(KEY_URL)
            ||key.equalsIgnoreCase(KEY_USERNAME)
            ||key.equalsIgnoreCase(KEY_PASSWORD)){
            return true;
        }else{
            return false;
        }
    }

    public Connection getDbConn() {
        return dbConn;
    }

    public void setDbConn(Connection dbConn) {
        this.dbConn = dbConn;
    }

    public Connection createConnect(){
        if(dbConn != null){
            return dbConn;
        }else{
            Properties props = new Properties();
            if(StringKit.isNotBlank(username)){
                props.put("user", username);
            }
            if(StringKit.isNotBlank(password)){
                props.put("password", password);
            }
            if(extProps.size() > 0){
                props.putAll(extProps);
            }
            return JdbcKit.getConnection(driverClassName, url, props);
        }
    }
}
