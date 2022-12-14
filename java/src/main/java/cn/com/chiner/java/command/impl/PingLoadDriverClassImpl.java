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

import cn.com.chiner.java.command.ExecResult;
import cn.fisok.raw.kit.JdbcKit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2021/6/12
 * @desc : 数据库驱动程序加载测试
 */
public class PingLoadDriverClassImpl extends AbstractDBCommand<ExecResult> {
    @Override
    public ExecResult exec(Map<String, String> params) {
        super.init(params);
        ExecResult ret = new ExecResult();

        try {
            Class.forName(driverClassName);
            ret.setStatus(ExecResult.SUCCESS);
        } catch (ClassNotFoundException e) {
            logger.error("", e);
            ret.setStatus(ExecResult.FAILED);
            ret.setBody("驱动加载失败，驱动类不存在(ClassNotFoundException)！出错消息：" + e.getMessage());
            return ret;
        }

        Connection conn = null;
        try {
            conn = createConnect();
            ret.setStatus(ExecResult.SUCCESS);
            ret.setBody("连接成功");
        } catch (Exception e) {
            ret.setStatus(ExecResult.FAILED);
            ret.setBody("连接失败!出错消息：" + e.getMessage());
            logger.error("", e);
        } finally {
            JdbcKit.close(conn);
        }

        return ret;
    }

}
