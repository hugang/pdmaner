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
package cn.com.chiner.java.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2021/6/20
 * @desc : 索引引用的字段
 */
@JsonPropertyOrder({
        "defKey",
        "ascOrDesc",
})
public class TableIndexColumnField implements Serializable,Cloneable {
    private String id;
    private String fieldDefKey;
    private String ascOrDesc;

    public String getFieldDefKey() {
        return fieldDefKey;
    }

    public void setFieldDefKey(String fieldDefKey) {
        this.fieldDefKey = fieldDefKey;
    }

    public String getAscOrDesc() {
        return ascOrDesc;
    }

    public void setAscOrDesc(String ascOrDesc) {
        this.ascOrDesc = ascOrDesc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
