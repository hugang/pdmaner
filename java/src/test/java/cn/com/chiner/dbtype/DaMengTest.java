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
package cn.com.chiner.dbtype;

import cn.com.chiner.java.Application;
import org.junit.Test;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2021/8/7
 * @desc : 达梦数据库的测试
 */
public class DaMengTest {
    @Test
    public void pingDriverLoadTest(){
        String[] args =  new String[]{
                "PingLoadDriverClass",                      //执行什么命令
                "driver_class_name=dm.jdbc.driver.DmDriver",
                "url=jdbc:dm://10.211.55.3:5236/SYSDBA",
                "username=SYSDBA",
                "password=SYSDBA",
                "out=/Users/asher/workspace/ws-vekai/siner-java/src/test/resources/out/pdc-"+System.nanoTime()+".json"
        };
        Application.main(args);
    }

    @Test
    public void listTableTest(){
        String[] args =  new String[]{
                "DBReverseGetAllTablesList",            //执行什么命令
                "driver_class_name=dm.jdbc.driver.DmDriver",
                "url=jdbc:dm://10.211.55.3:5236/SYSDBA",
                "username=SYSDBA",
                "password=SYSDBA",
                "out=/Users/asher/workspace/ws-vekai/siner-java/src/test/resources/out/dbrgatl-"+System.nanoTime()+".json"
        };
        Application.main(args);
    }

    @Test
    public void getTableDDLTest(){
        String[] args =  new String[]{
                "DBReverseGetTableDDL",            //执行什么命令
                "driver_class_name=dm.jdbc.driver.DmDriver",
                "url=jdbc:dm://10.211.55.3:5236/SYSDBA",
                "username=SYSDBA",
                "password=SYSDBA",
                "tables=SIMS_STUDENT,SIMS_TEACHER",
                "out=/Users/asher/workspace/ws-vekai/chiner-java/src/test/resources/out/dbrgtddl-"+System.nanoTime()+".json"
        };
        Application.main(args);
    }
}
