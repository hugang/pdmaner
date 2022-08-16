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

import java.util.Iterator;
import java.util.Properties;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2021/9/30
 * @desc :
 */
public class GenDocxTest {
    @Test
    public void genDocxGroupsTest(){
        String[] args =  new String[]{
                "GenDocx",            //执行什么命令
                "sinerFile=/Users/yangsong158/workspace/ws-chiner/chiner-java/src/test/resources/pdma/教学管理系统-标准模板.pdma.json",  //输入的PDMan文件
                "docxTpl=/Users/yangsong158/workspace/ws-chiner/chiner-java/src/test/resources/tpl/siner-docx-tpl.docx",      //文档模板文件
                "imgDir=/Users/asher/workspace/ws-vekai/siner-java/src/test/resources/images/shop",                         //图片文件存放目录
                "imgExt=.png",//图片文件后缀名
                "outFile=/Users/yangsong158/workspace/ws-chiner/chiner-java/src/test/resources/out/gendocx-"+System.nanoTime()+".docx",
                "out=/Users/yangsong158/workspace/ws-chiner/chiner-java/src/test/resources/out/gendocx-"+System.nanoTime()+".json"
        };
        Application.main(args);
    }

    @Test
    public void genDocxTest(){

        String[] args =  new String[]{
                "GenDocx",            //执行什么命令
//                "sinerFile=/Users/asher/workspace/ws-vekai/chiner-java/src/test/resources/siner/教学管理系统.chnr.json",  //输入的PDMan文件
                "sinerFile=/Users/asher/workspace/ws-vekai/chiner-java/src/test/resources/chr/未命名.chnr.json",  //输入的PDMan文件
                "docxTpl=/Users/asher/workspace/ws-vekai/chiner-java/src/test/resources/tpl/siner-docx-tpl.docx",      //文档模板文件
//                "imgDir=/Users/asher/workspace/ws-vekai/chiner-java/src/test/resources/images/smis",                         //图片文件存放目录
                "imgDir=/Users/asher/Library/Application Support/Electron/temp_img",                         //图片文件存放目录
                "imgExt=.png",//图片文件后缀名
                "outFile=/Users/asher/workspace/ws-vekai/chiner-java/src/test/resources/out/chiner-"+System.nanoTime()+".docx",
                "out=/Users/asher/workspace/ws-vekai/chiner-java/src/test/resources/out/gendocx-"+System.nanoTime()+".json"
        };
        Application.main(args);
    }
}
