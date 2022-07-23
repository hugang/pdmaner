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
import java.util.ArrayList;
import java.util.List;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2021/6/20
 * @desc : 数据表的索引
 */
@JsonPropertyOrder({
        "defKey",
        "unique",
        "defName",
        "comment",
        "fields",
})
public class TableIndex implements Serializable,Cloneable {
    private String id;
    private String defKey;          //索引代码
    private boolean unique = false; //索引是否唯一
    private String defName;         //索引名称
    private String comment = "";    //索引注释说明
    private List<TableIndexColumnField> fields = new ArrayList<>(); //索引下的字段明细

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDefKey() {
        return defKey;
    }

    public void setDefKey(String defKey) {
        this.defKey = defKey;
    }

    public boolean isUnique() {
        return unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    public String getDefName() {
        return defName;
    }

    public void setDefName(String defName) {
        this.defName = defName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<TableIndexColumnField> getFields() {
        return fields;
    }

    public void setFields(List<TableIndexColumnField> fields) {
        this.fields = fields;
    }

    /**
     * 查找索引
     * @param fieldDefKey
     * @return
     */
    public TableIndexColumnField lookupField(String fieldDefKey){
        List<TableIndexColumnField> fields = getFields();
        for(TableIndexColumnField field : fields){
            if(fieldDefKey.equalsIgnoreCase(field.getFieldDefKey())){
                return field;
            }
        }
        return null;
    }
}
