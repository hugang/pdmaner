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
package cn.com.chiner;

import cn.com.chiner.java.Application;
import org.junit.Test;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2021/9/11
 * @desc :
 */
public class DDLParseTest {
    @Test
    public void parseTest(){
        String[] args =  new String[]{
                "ParseDDLToTableImpl",                      //执行什么命令
//                "ddlFile=/Users/asher/workspace/ws-vekai/chiner-java/src/test/resources/sql/mysql-ddl.sql",
                "ddlFile=/Users/asher/workspace/ws-vekai/chiner-java/src/test/resources/sql/oracle-ddl.sql",
//                "ddlFile=/Users/asher/workspace/ws-vekai/chiner-java/src/test/resources/sql/教学管理系统-DDL-2021913181014.sql",
                "out=/Users/asher/workspace/ws-vekai/chiner-java/src/test/resources/out/parse-ddl-"+System.nanoTime()+".json"
        };
        Application.main(args);
    }
}
