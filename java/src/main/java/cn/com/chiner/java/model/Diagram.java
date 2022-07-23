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

import com.deepoove.poi.data.RenderData;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2021/6/22
 * @desc :
 */
@JsonPropertyOrder({
        "defKey",
        "defName",
})
public class Diagram implements Serializable,Cloneable{
    private String id;              //关系图ID
    private String defKey;          //关系图代码
    private String defName;         //关系图名称
    @JsonIgnore
    private RenderData renderData;

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

    public String getDefName() {
        return defName;
    }

    public void setDefName(String defName) {
        this.defName = defName;
    }

    public RenderData getRenderData() {
        return renderData;
    }

    public void setRenderData(RenderData renderData) {
        this.renderData = renderData;
    }
}
