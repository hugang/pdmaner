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
package cn.com.chiner.java.dialect;

import cn.fisok.raw.kit.JdbcKit;
import cn.fisok.raw.kit.StringKit;
import cn.com.chiner.java.command.kit.ConnParseKit;
import cn.com.chiner.java.model.ColumnField;
import cn.com.chiner.java.model.TableEntity;
import cn.com.chiner.java.model.TableIndex;
import cn.com.chiner.java.model.TableIndexColumnField;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2021/6/14
 * @desc : 数据库方言抽象类
 */
public class DBDialect {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 获取数据库SchemaPattern
     * @param conn
     * @return
     * @throws SQLException
     */
    public String getSchemaPattern(Connection conn) throws SQLException{
        return null;
    };

    /**
     * 获取数据库TableNamePattern
     * @param conn
     * @return
     * @throws SQLException
     */
    public String getTableNamePattern(Connection conn) throws SQLException{
        return null;
    }


    /**
     * 将resultset,主键的resultset装到一个二元组中，并返回
     * @param conn
     * @param tableName
     * @return
     * @throws SQLException
     */
    public Pair getColumnAndPrimaryKeyResultSetPair(Connection conn,String tableName) throws SQLException {
        DatabaseMetaData connMetaData = conn.getMetaData();
        String schema = getSchemaPattern(conn);

        ResultSet rs = connMetaData.getColumns(conn.getCatalog(), schema,tableName, "%");
        ResultSet pkRs = connMetaData.getPrimaryKeys(conn.getCatalog(),schema,tableName);

        Pair<ResultSet,ResultSet> pair = Pair.of(rs,pkRs);

        return pair;
    }

    /**
     * 根据结果集，创建表实体对象，仅填充表名，中文名，注释信息
     * @param connection
     * @param rs
     * @return
     */
    public TableEntity createTableEntity(Connection connection,ResultSet rs) throws SQLException {
        TableEntity entity = new TableEntity();
        fillTableEntityNoColumn(entity, connection,rs);
        if (StringKit.isNotBlank(entity.getDefKey())) {
            return entity;
        } else {
            return null;
        }
    }

    /**
     * 传入一个空对象，填充表名，中文名，注释信息
     * @param tableEntity
     * @param rs
     * @throws SQLException
     */
    public void fillTableEntityNoColumn(TableEntity tableEntity, Connection connection,ResultSet rs) throws SQLException {
        String tableName = rs.getString("TABLE_NAME");
        String remarks = StringKit.trim(rs.getString("REMARKS"));
        String defKey = tableName;
        String defName = remarks;
        String comment = "";

        //如果remark中有分号等分割符，则默认之后的就是注释说明文字
        if(StringKit.isNotBlank(remarks)){
            Pair<String, String> pair = ConnParseKit.parseNameAndComment(remarks);
            defName = pair.getLeft();
            comment = pair.getRight();
        }
        tableEntity.setDefKey(defKey);
        tableEntity.setDefName(defName);
        tableEntity.setComment(comment);

    }

    /**
     * 传入表名，中文名，注释信息，获取字段明细，索引信息
     * @param tableEntity
     * @param conn
     */
    public void fillTableEntity(TableEntity tableEntity, Connection conn) throws SQLException {
        String tableName = tableEntity.getDefKey();

        ResultSet rs = null;
//        Statement stmt = null;
        ResultSet pkRs = null;
//        Statement pkStmt = null;

        try {
            Pair<ResultSet,ResultSet> pair = getColumnAndPrimaryKeyResultSetPair(conn,tableName);
            rs = pair.getLeft();
            pkRs = pair.getRight();
//            stmt = rs.getStatement();
//            pkStmt = pkRs.getStatement();
            Set<String> pkSet = new HashSet<String>();
            while(pkRs.next()){
                String columnName = pkRs.getString("COLUMN_NAME");
                pkSet.add(columnName);
            }

            while(rs.next()){
                ColumnField field = new ColumnField();
                fillColumnField(field,conn,rs,pkSet);
                tableEntity.getFields().add(field);
            }
            fillTableIndexes(tableEntity,conn);
        } catch (SQLException e) {
            logger.error("读取数据表"+tableName+"的字段明细出错",e);
            throw new RuntimeException("读取数据表"+tableName+"的字段明细出错",e);
        } finally {
//            JdbcKit.close(stmt);
            JdbcKit.close(rs);
//            JdbcKit.close(pkStmt);
            JdbcKit.close(pkRs);
        }
    }

    /**
     * 填充列数据
     * TABLE_CAT String => 表类别（可为 null）
     * TABLE_SCHEM String => 表模式（可为 null）
     * TABLE_NAME String => 表名称
     * COLUMN_NAME String => 列名称
     * DATA_TYPE int => 来自 java.sql.Types 的 SQL 类型
     * TYPE_NAME String => 数据源依赖的类型名称，对于 UDT，该类型名称是完全限定的
     * COLUMN_SIZE int => 列的大小。
     * BUFFER_LENGTH 未被使用。
     * DECIMAL_DIGITS int => 小数部分的位数。对于 DECIMAL_DIGITS 不适用的数据类型，则返回 Null。
     * NUM_PREC_RADIX int => 基数（通常为 10 或 2）
     * NULLABLE int => 是否允许使用 NULL。
     * columnNoNulls - 可能不允许使用 NULL 值
     * columnNullable - 明确允许使用 NULL 值
     * columnNullableUnknown - 不知道是否可使用 null
     * REMARKS String => 描述列的注释（可为 null）
     * COLUMN_DEF String => 该列的默认值，当值在单引号内时应被解释为一个字符串（可为 null）
     * SQL_DATA_TYPE int => 未使用
     * SQL_DATETIME_SUB int => 未使用
     * CHAR_OCTET_LENGTH int => 对于 char 类型，该长度是列中的最大字节数
     * ORDINAL_POSITION int => 表中的列的索引（从 1 开始）
     * IS_NULLABLE String => ISO 规则用于确定列是否包括 null。
     * YES --- 如果参数可以包括 NULL
     * NO --- 如果参数不可以包括 NULL
     * 空字符串 --- 如果不知道参数是否可以包括 null
     * SCOPE_CATLOG String => 表的类别，它是引用属性的作用域（如果 DATA_TYPE 不是 REF，则为 null）
     * SCOPE_SCHEMA String => 表的模式，它是引用属性的作用域（如果 DATA_TYPE 不是 REF，则为 null）
     * SCOPE_TABLE String => 表名称，它是引用属性的作用域（如果 DATA_TYPE 不是 REF，则为 null）
     * SOURCE_DATA_TYPE short => 不同类型或用户生成 Ref 类型、来自 java.sql.Types 的 SQL 类型的源类型（如果 DATA_TYPE 不是 DISTINCT 或用户生成的 REF，则为 null）
     * IS_AUTOINCREMENT String => 指示此列是否自动增加
     * YES --- 如果该列自动增加
     * NO --- 如果该列不自动增加
     * 空字符串 --- 如果不能确定该列是否是自动增加参数
     * COLUMN_SIZE 列表示给定列的指定列大小。对于数值数据，这是最大精度。对于字符数据，这是字符长度。对于日期时间数据类型，这是 String 表示形式的字符长度（假定允许的最大小数秒组件的精度）。对于二进制数据，这是字节长度。对于 ROWID 数据类型，这是字节长度。对于列大小不适用的数据类型，则返回 Null。
     *
     *
     * 参数：
     * catalog - 类别名称；它必须与存储在数据库中的类别名称匹配；该参数为 "" 表示获取没有类别的那些描述；为 null 则表示该类别名称不应该用于缩小搜索范围
     * schemaPattern - 模式名称的模式；它必须与存储在数据库中的模式名称匹配；该参数为 "" 表示获取没有模式的那些描述；为 null 则表示该模式名称不应该用于缩小搜索范围
     * tableNamePattern - 表名称模式；它必须与存储在数据库中的表名称匹配
     * columnNamePattern - 列名称模式；它必须与存储在数据库中的列名称匹配
     * @param field
     * @param rs
     * @throws SQLException
     */
    public void fillColumnField(ColumnField field,Connection conn, ResultSet rs, Set<String> pkSet) throws SQLException {

        String colName = rs.getString("COLUMN_NAME");
        String remarks = StringKit.trim(rs.getString("REMARKS"));
        String typeName = rs.getString("TYPE_NAME");
        int dataType = rs.getInt("DATA_TYPE");
        int columnSize = rs.getInt("COLUMN_SIZE");
        Integer decimalDigits = rs.getInt("DECIMAL_DIGITS");
        String defaultValue = rs.getString("COLUMN_DEF");
        String isNullable = rs.getString("IS_NULLABLE");
        String isAutoincrement = "NO";
        defaultValue = StringKit.nvl(parseDefaultValue(dataType,defaultValue),"");


        String label = remarks;
        String comment = null;
        if(StringKit.isNotBlank(remarks)){
            Pair<String,String> columnRemarks = ConnParseKit.parseNameAndComment(remarks);
            label = columnRemarks.getLeft();
            comment = columnRemarks.getRight();
        }

        field.setDefKey(colName);
        field.setDefName(label);
        field.setComment(comment);
        field.setType(typeName);

        if(columnSize > 0){
            field.setLen(columnSize);
        }
        if(decimalDigits<=0){
            field.setScale(null);
        }else{
            field.setScale(decimalDigits);
        }
        //不需要长度的数据类型，把数据类型清除掉，防止部分数据库解析出有长度的情况
        if(withoutLenDataType(dataType)){
            field.setLen(null);
            field.setScale(null);
        }

        field.setPrimaryKey(pkSet.contains(colName));
        field.setNotNull(!"YES".equalsIgnoreCase(isNullable));
        field.setAutoIncrement(!"NO".equalsIgnoreCase(isAutoincrement));
        field.setDefaultValue(defaultValue);
    }

    /**
     * 不含长度的数据类型，这种数据类型不需要设置长度以及小数位
     * @param dataType
     * @return
     */
    protected boolean withoutLenDataType(int dataType) {
        int[] array = {Types.DATE, Types.TIMESTAMP, Types.TIME,
                Types.TIME_WITH_TIMEZONE,Types.TIMESTAMP_WITH_TIMEZONE,
                Types.CLOB,Types.BLOB, Types.NCLOB,
                Types.LONGVARCHAR,Types.LONGVARBINARY
        };
        for(int item : array){
            if(item == dataType){
                return true;
            }
        }
        return false;
    }

    public String parseDefaultValue(Integer dataType,String defaultValue){
        if(StringKit.isNotBlank(defaultValue)){
            if(defaultValue.indexOf("'::") > 0){
                defaultValue = defaultValue.substring(0,defaultValue.indexOf("'::")+1);
            }
            //如果是被''圈住，说明是字串，不需要处理
            if(defaultValue.startsWith("'") && defaultValue.endsWith("'")){
            }else{
                //如果全是数字，并且不以0开始,那么就是数字，不需要加双单引号
//                if(defaultValue.matches("[0-9]+") && !defaultValue.startsWith("0")){
                if(JdbcKit.isNumeric(dataType)){
                }else{
                    defaultValue = "'"+defaultValue+"'";
                }
            }
        }
        return defaultValue;
    }

    /**
     * 填充数据表的索引
     * @param tableEntity
     * @param conn
     * @throws SQLException
     */
    public void fillTableIndexes(TableEntity tableEntity, Connection conn) throws SQLException {
        String table = tableEntity.getDefKey();
        DatabaseMetaData dbMeta = conn.getMetaData();
        String schema = getSchemaPattern(conn);
        ResultSet rs = dbMeta.getIndexInfo(conn.getCatalog(), schema, table, false, false);

        while (rs.next()) {
            String tableName = rs.getString("TABLE_NAME");
            String indexName = rs.getString("INDEX_NAME");
            String columnName = rs.getString("COLUMN_NAME");
            String nonUnique = rs.getString("NON_UNIQUE");
            String ascOrDesc = rs.getString("ASC_OR_DESC");

            if("PRIMARY".equalsIgnoreCase(indexName)){
                continue;
            }
            if(!table.equalsIgnoreCase(tableName)){
                continue;
            }
            if(StringKit.isBlank(indexName)){
                continue;
            }
            if(StringKit.isBlank(columnName)){
                continue;
            }

            TableIndex index = tableEntity.lookupIndex(indexName);
            if(index == null){
                index = new TableIndex();
                index.setDefKey(indexName);
                index.setUnique(!"1".equalsIgnoreCase(nonUnique));
                tableEntity.getIndexes().add(index);
            }

            TableIndexColumnField ticf = index.lookupField(columnName);
            if(ticf != null){
                continue;
            }
            ticf = new TableIndexColumnField();
            ticf.setFieldDefKey(columnName);
            ticf.setAscOrDesc(ascOrDesc);
            index.getFields().add(ticf);

        }

        JdbcKit.close(rs.getStatement());
        JdbcKit.close(rs);
    }

    public List<TableEntity> getAllTables(Connection conn,String schema) throws SQLException {
        DatabaseMetaData meta = conn.getMetaData();

//        String schemaPattern = null;
        String schemaPattern = getSchemaPattern(conn);
        String tableNamePattern = getTableNamePattern(conn);
        String catalog = conn.getCatalog();

        ResultSet rs = meta.getTables(catalog, schemaPattern, tableNamePattern, new String[]{"TABLE"});
        List<TableEntity> tableEntities = new ArrayList<TableEntity>();
        while (rs.next()) {
            String tableName = rs.getString("TABLE_NAME");
            /**
             *  SQL Server系统保留表
             *  trace_xe_action_map,trace_xe_event_map
             */
            if (!"PDMAN_DB_VERSION".equalsIgnoreCase(tableName)
                    && !"trace_xe_action_map".equalsIgnoreCase(tableName)
                    && !"trace_xe_event_map".equalsIgnoreCase(tableName)){
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
    /**
     * 取所有的数据表清单
     * @param conn
     * @return
     */
    public List<TableEntity> getAllTables(Connection conn) throws SQLException {
        return getAllTables(conn,null);
    }

    /**
     * 根据表名，创建数据表实体的字段及索引
     * @param conn
     * @param meta
     * @param tableName
     * @return
     * @throws SQLException
     */
    public TableEntity createTableEntity(Connection conn,DatabaseMetaData meta,String tableName) throws SQLException {
        ResultSet rs = null;
        try{
            String schemaPattern = getSchemaPattern(conn);
//            String schemaPattern = "jence_user";
            rs = meta.getTables(null, schemaPattern, tableName.toLowerCase(), new String[]{"TABLE"});
            if(rs.next()) {
                TableEntity tableEntity = createTableEntity(conn, rs);
                fillTableEntity(tableEntity,conn);
                JdbcKit.close(rs);
                return tableEntity;
            }else{
                //如果全小写不行，就来试试全大写
                rs = meta.getTables(null, schemaPattern, tableName.toUpperCase(), new String[]{"TABLE"});
                if(rs.next()) {
                    TableEntity tableEntity = createTableEntity(conn, rs);
                    fillTableEntity(tableEntity,conn);
                    return tableEntity;
                }
            }
        }catch (SQLException e){
            throw e;
        }finally {
            JdbcKit.close(rs);
        }
        return null;
    }

}
