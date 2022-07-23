package cn.com.chiner.java.dialect.impl;

import cn.com.chiner.java.dialect.DBDialect;
import cn.com.chiner.java.model.TableEntity;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2021/6/14
 * @desc : Hive使用MySQL作元数据存储
 */
public class DBDialectHiveMySQL extends DBDialect {
    private static String QUERY_TABLE_SQL = "SELECT  tbl.TBL_NAME                                  as tbl_name             -- 表名\n" +
            "        ,tbl.TBL_TYPE                                                       as tbl_type             -- 表类型\n" +
            "        ,if(partkey.PKEY_NAME is not null,partkey.PKEY_NAME,'')             as partion_key_name     -- 分区字段名\n" +
            "        ,if(partkey.PKEY_COMMENT is not null,partkey.PKEY_COMMENT,'')       as partion_key_comment  -- 分区注释\n" +
            "        ,if(partkey.PKEY_TYPE is not null,partkey.PKEY_TYPE,'')             as partion_key_type     -- 分区类型\n" +
            "        ,if(partkey.INTEGER_IDX is not null,partkey.INTEGER_IDX,'')         as partion_key_sequence -- 分区值\n" +
            "        ,if(tbl_params.PARAM_VALUE is not null,tbl_params.PARAM_VALUE,'')   as tbl_comment          -- 表注释\n" +
            "        ,db.`name` as db_name\n" +
            "FROM\n" +
            "\tSDS sds\n" +
            "\tLEFT JOIN TBLS tbl ON sds.SD_ID = tbl.SD_ID\n" +
            "\tLEFT JOIN TABLE_PARAMS tbl_params ON tbl.TBL_ID = tbl_params.TBL_ID \n" +
            "\tAND tbl_params.PARAM_KEY = 'comment'\n" +
            "\tLEFT JOIN PARTITION_KEYS partkey ON tbl.TBL_ID = partkey.TBL_ID\n" +
            "\tLEFT JOIN DBS db ON tbl.DB_ID = db.DB_ID\n" +
            "WHERE\n" +
            "\ttbl.TBL_NAME IS NOT NULL \n" +
            "\tAND db.`name` = ?";

    @Override
    public List<TableEntity> getAllTables(Connection conn) throws SQLException {
        return super.getAllTables(conn);
    }
}
