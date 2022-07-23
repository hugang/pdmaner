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
package cn.com.chiner.h2;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2021/9/11
 * @desc :
 */
public class ConnPingTest {
    @Test
    public void connTest() throws SQLException {
        //MODE=MySQL – 兼容模式，H2 兼容多种数据库，该值可以为：DB2、Derby、HSQLDB、MSSQLServer、MySQL、Oracle、PostgreSQL
        Connection conn = DriverManager.getConnection("jdbc:h2:mem:MockChiner;MODE=MySQL;DB_CLOSE_DELAY=-1");
        conn.close();
    }
}
