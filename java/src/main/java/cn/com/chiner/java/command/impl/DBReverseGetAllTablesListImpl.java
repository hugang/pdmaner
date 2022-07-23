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

import cn.com.chiner.java.dialect.DBDialect;
import cn.com.chiner.java.dialect.DBDialectMatcher;
import cn.com.chiner.java.model.TableEntity;
import cn.fisok.raw.kit.JdbcKit;

import cn.fisok.sqloy.core.DBType;
import cn.fisok.sqloy.kit.DBTypeKit;
import cn.com.chiner.java.command.ExecResult;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2021/6/12
 * @desc : 数据库逆向，解析表清单功能
 */
public class DBReverseGetAllTablesListImpl extends AbstractDBCommand<ExecResult> {

    @Override
    public ExecResult exec(Map<String, String> params) {
        super.init(params);
        String dbname = params.get("dbname");
        ExecResult ret = new ExecResult();

        //获取连接正常的情况下，进入下一步
        Connection conn = null;
        List<TableEntity> tableEntities = null;
        try {
            conn = createConnect();
            tableEntities = fetchTableEntities(conn);
            ret.setStatus(ExecResult.SUCCESS);
            ret.setBody(tableEntities);
        } catch (Exception e) {
            ret.setStatus(ExecResult.FAILED);
            ret.setBody(e.getMessage());
            logger.error("", e);
        } finally {
            JdbcKit.close(conn);
        }

        return ret;
    }

    /**
     * 获取所有数据表列表
     *
     * @param conn
     * @return
     */
    protected List<TableEntity> fetchTableEntities(Connection conn) throws SQLException {
        List<TableEntity> tableEntities = new ArrayList<>();
        try {
            DBType dbType = DBTypeKit.getDBType(conn);
            DBDialect dbDialect = DBDialectMatcher.getDBDialect(dbType);
            tableEntities = dbDialect.getAllTables(conn);
        } catch (SQLException e) {
            logger.error("读取表清单出错", e);
            throw new RuntimeException(e);
        }

        return tableEntities;
    }
}
