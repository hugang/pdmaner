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
import cn.com.chiner.java.model.ColumnField;

import java.sql.*;
import java.util.Set;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2021/6/14
 * @desc : MySQL数据库方言
 */
public class DBDialectMySQL extends DBDialect {

    @Override
    public String getSchemaPattern(Connection conn) throws SQLException {
        return conn.getCatalog() == null ? conn.getSchema() : conn.getCatalog();
    }

    @Override
    public void fillColumnField(ColumnField field, Connection conn, ResultSet rs, Set<String> pkSet) throws SQLException {
        super.fillColumnField(field, conn, rs, pkSet);
        int dataType = rs.getInt("DATA_TYPE");
        if (isInteger(dataType)) {
            int columnSize = rs.getInt("COLUMN_SIZE")+1;
            field.setLen(columnSize);
        }
        String isAutoincrement = rs.getString("IS_AUTOINCREMENT");
        field.setAutoIncrement(!"NO".equalsIgnoreCase(isAutoincrement));
    }

    /**
     * MySQL的整数长度比实际要少一位
     * @param dataType
     * @return
     */
    private static boolean isInteger(int dataType) {
//        int[] array = {Types.NUMERIC, Types.DECIMAL, Types.TINYINT, Types.SMALLINT, Types.INTEGER, Types.BIGINT, Types.REAL, Types.FLOAT, Types.DOUBLE};
        int[] array = {Types.TINYINT, Types.SMALLINT, Types.INTEGER, Types.BIGINT};
        for(int item : array){
            if(item == dataType){
                return true;
            }
        }
        return false;
    }
}
