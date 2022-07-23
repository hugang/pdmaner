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
package cn.com.chiner.java.pdm.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2021/7/4
 * @desc :
 */
public class PDTable extends PDBaseObject {
    private String comment;
    private List<PDColumn> columns = new ArrayList<>();
    private List<PDKey> keys = new ArrayList<>();

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<PDColumn> getColumns() {
        return columns;
    }

    public void setColumns(List<PDColumn> columns) {
        this.columns = columns;
    }

    public List<PDKey> getKeys() {
        return keys;
    }

    public void setKeys(List<PDKey> keys) {
        this.keys = keys;
    }
}
