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
 * @date : 2021/6/12
 * @desc :
 */
public class ApplicationTest {

    @Test
    public void pingDB2DriverLoadTest(){
        String[] args =  new String[]{
                "PingLoadDriverClass",                      //执行什么命令
                "driver_class_name=com.ibm.db2.jcc.DB2Driver",
                "url=jdbc:db2://47.107.253.194:50000/ams5:progressiveStreaming=2;",
                "username=db2inst1",
                "password=db2inst1",
                "out=/Users/asher/workspace/ws-vekai/siner-java/src/test/resources/out/pdc-"+System.nanoTime()+".json"
        };
        Application.main(args);
    }


    @Test
    public void parsePDMFileTest(){
        String[] args =  new String[]{
                "ParsePDMFile",            //执行什么命令
//                "pdmFile=/Users/asher/workspace/ws-vekai/chiner-java/src/test/resources/pdm/供应商管理.pdm",  //输入的PDMan文件
//                "pdmFile=/Users/asher/workspace/ws-vekai/chiner-java/src/test/resources/pdm/JEKI-WIKI文章模块.pdm",  //输入的PDMan文件
//                "pdmFile=/Users/asher/workspace/ws-vekai/chiner-java/src/test/resources/pdm/数据字典.PDM",  //输入的PDMan文件
//                "pdmFile=/Users/asher/workspace/ws-vekai/chiner-java/src/test/resources/pdm/知识管理.pdm",  //输入的PDMan文件
                "pdmFile=/Users/yangsong158/workspace/ws-chiner/chiner-java/src/test/resources/pdm/PhysicalDataModel_1.pdm",  //输入的PDMan文件
                "out=/Users/yangsong158/workspace/ws-chiner/chiner-java/src/test/resources/out/import-pdm-"+System.nanoTime()+".json"
        };
        Application.main(args);
    }

    @Test
    public void parseDictExcel(){
        String[] args =  new String[]{
                "ParseDictExcel",            //执行什么命令
                "excelFile=/Users/yangsong158/workspace/ws-chiner/chiner-java/src/test/resources/数据字典模板.xlsx",
                "out=/Users/yangsong158/workspace/ws-chiner/chiner-java/src/test/resources/out/import-dict-excel-"+System.nanoTime()+".json"
        };

        Application.main(args);
    }
}
