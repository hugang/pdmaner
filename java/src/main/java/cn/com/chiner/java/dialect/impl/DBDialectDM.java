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
package cn.com.chiner.java.dialect.impl;

import cn.com.chiner.java.dialect.DBDialect;
import cn.com.chiner.java.model.TableEntity;
import cn.fisok.raw.kit.StringKit;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2021/6/19
 * @desc : 达梦数据库方言
 */
public class DBDialectDM extends DBDialect {
    @Override
    public String getSchemaPattern(Connection conn) throws SQLException {
        String schemaPattern = conn.getMetaData().getUserName().toUpperCase();
        if (StringKit.isNotBlank(schemaPattern)) {
            schemaPattern = schemaPattern.toUpperCase();
        }
        return schemaPattern;
    }

    /**
     * 取所有的数据表清单
     * @param conn
     * @return
     */
    public List<TableEntity> getAllTables(Connection conn) throws SQLException {
        DatabaseMetaData meta = conn.getMetaData();

//        String schemaPattern = null;
        String schemaPattern = getSchemaPattern(conn);
        String tableNamePattern = getTableNamePattern(conn);
        String catalog = conn.getCatalog();

        ResultSet rs = meta.getTables(catalog, schemaPattern, tableNamePattern, new String[]{"TABLE"});
        List<TableEntity> tableEntities = new ArrayList<TableEntity>();
        while (rs.next()) {
            String tableName = rs.getString("TABLE_NAME");
            if(tableName.startsWith("#")){
                continue;
            }
            if (!tableName.equalsIgnoreCase("PDMAN_DB_VERSION")){
                TableEntity entity = createTableEntity(conn,rs);
                if(entity != null){
                    tableEntities.add(entity);
                }
            }else{
                continue;
            }
        }
        return tableEntities;
    }
}
