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
package cn.com.chiner.java.log;

import ch.qos.logback.core.PropertyDefinerBase;
import cn.fisok.raw.kit.StringKit;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2021/7/11
 * @desc :
 */
public class LogHomeProperty extends PropertyDefinerBase {

    @Override
    public String getPropertyValue() {
        String userHome = System.getProperty("user.home");
        if(StringKit.isNotBlank(userHome)){
            return userHome;
        }
        return null;
    }
}
