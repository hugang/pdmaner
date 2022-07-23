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
package cn.fisok.sqloy.kit;

import cn.fisok.raw.kit.JdbcKit;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.sqloy.core.DBType;
import com.alibaba.druid.util.JdbcUtils;
import com.aliyun.odps.jdbc.OdpsDatabaseMetaData;
import com.kingbase8.jdbc.KbDatabaseMetaData;


import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2020/5/11
 * @desc :
 */
public abstract class DBTypeKit {

    /**
     * 根据连接信息推断数据库类型
     * @param connection
     * @return
     */
    public static DBType getDBType(Connection connection) throws SQLException {
        String dbType = getDBTypeStr(connection);
        return DBType.getDBTypeByName(dbType.toUpperCase());
    }

    public static DBType getDBType(DatabaseMetaData metaData) throws SQLException {
        String dbType = getDBTypeStr(metaData);
        return DBType.getDBTypeByName(dbType.toUpperCase());
    }

    /**
     * 根据数据源推断数据库类型(类型描述字串）
     * @param dataSource
     * @return
     */
    public static String getDBTypeStr(DataSource dataSource){
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            return getDBTypeStr(connection);
        } catch (SQLException e) {
            JdbcKit.close(connection);
            throw new RuntimeException("从datasource中获取数据库类型DBType出错",e);
        } finally {
            JdbcKit.close(connection);
        }
    }

    /**
     * 根据连接信息推断数据库类型(类型描述字串）
     * @param connection
     * @return
     */
    public static String getDBTypeStr(Connection connection) throws SQLException {
        DatabaseMetaData dbMeta = connection.getMetaData();
        return getDBTypeStr(dbMeta);
    }

    /**
     * 根据连接信息推断数据库类型(类型描述字串）
     * @param metaData
     * @return
     */
    public static String getDBTypeStr(DatabaseMetaData metaData){
        try {
            String driver = metaData.getDriverName();
            String url = metaData.getURL();
            String dbType = JdbcUtils.getDbType(url,driver);

            //增加人大金仓支持
            if(StringKit.isBlank(dbType) && metaData instanceof KbDatabaseMetaData){
                dbType = "kingbase";
            }else if(StringKit.isBlank(dbType) && metaData instanceof OdpsDatabaseMetaData){
                dbType = "odps";
            }
            return dbType;
        } catch (SQLException e) {
            throw new RuntimeException("从connection中获取数据库类型MetaData出错",e);
        }
    }
}
