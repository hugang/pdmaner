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
import cn.com.chiner.java.model.TableEntity;
import cn.fisok.raw.kit.FileKit;
import cn.fisok.raw.kit.IOKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2021/9/11
 * @desc : 将DDL语句解析为表结构
 */
public class ParseDDLToTableImpl extends AbstractDBCommand<ExecResult> {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    private static String DB_URL = "jdbc:h2:mem:MockChiner;DB_CLOSE_DELAY=-1";

    public ExecResult exec(Map<String, String> params) {
        String ddlFile = params.get("ddlFile");
        ExecResult ret = new ExecResult();
        Connection conn = null;
        try {
            String ddlContent = readDDLFile(ddlFile);

            conn = DriverManager.getConnection(DB_URL);
            createTable(conn,ddlContent);

            String tables = getALLTablesString(conn);
            IOKit.close(conn);//conn.getMetaData(); 只能使用一次,所以要重新取一次
            conn = DriverManager.getConnection(DB_URL);
            ret = parseTableDDL(conn,tables);
        } catch (IOException | SQLException e) {
            ret.setBody(e.getMessage());
            ret.setStatus(ExecResult.FAILED);
            logger.error(e.getMessage(),e);
        } finally {
            IOKit.close(conn);
        }
        return ret;
    }

    private String readDDLFile(String ddlFile) throws IOException {
        File inFile = new File(ddlFile);
        InputStream inputStream = null;
        try {
            inputStream = FileKit.openInputStream(inFile);
            String ddlContent = IOKit.toString(inputStream,"UTF-8");
            return ddlContent;
        } catch (FileNotFoundException e) {
            logger.error("读取DDL文件出错:"+ddlFile, e);
            throw new RuntimeException(e);
        } finally {
            IOKit.close(inputStream);
        }
    }

    private void createTable(Connection connection,String ddlScript) throws SQLException, IOException {
        Statement stmt = connection.createStatement();
        stmt.execute(ddlScript);
    }

    private String getALLTablesString(Connection connection){
        Map<String,String> params = new HashMap<>();
        DBReverseGetAllTablesListImpl cmd = new DBReverseGetAllTablesListImpl();
        cmd.setDbConn(connection);
        ExecResult ret = cmd.exec(new HashMap<>());
        if(ret.getStatus().equals(ExecResult.SUCCESS)){
            StringBuffer tables = new StringBuffer();
            List<TableEntity> dataList = (List<TableEntity>)ret.getBody();
            dataList.forEach(tableEntity->{
                tables.append(tableEntity.getDefKey()).append(",");
            });
            if(tables.length()>0){
                return tables.substring(0,tables.length()-1);
            }
        }
        return "";
    }

    private ExecResult parseTableDDL(Connection connection,String tables){
        Map<String,String> params = new HashMap<>();
        params.put("tables",tables);

        DBReverseGetTableDDLImpl cmd = new DBReverseGetTableDDLImpl();
        cmd.setDbConn(connection);
        ExecResult ret = cmd.exec(params);
        return ret;
    }
}
