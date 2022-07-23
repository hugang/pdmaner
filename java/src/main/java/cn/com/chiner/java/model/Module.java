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

import java.io.Serializable;
import java.util.List;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2021/6/22
 * @desc :
 */
public class Module implements Serializable,Cloneable {
    private String defKey;          //表代码
    private String defName;         //表名称
    private List<TableEntity> entities;
    private List<View> views;
    private List<Dict> dicts;
    private List<Diagram> diagrams;

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

    public List<TableEntity> getEntities() {
        return entities;
    }

    public void setEntities(List<TableEntity> entities) {
        this.entities = entities;
    }

    public List<View> getViews() {
        return views;
    }

    public void setViews(List<View> views) {
        this.views = views;
    }

    public List<Dict> getDicts() {
        return dicts;
    }

    public void setDicts(List<Dict> dicts) {
        this.dicts = dicts;
    }

    public List<Diagram> getDiagrams() {
        return diagrams;
    }

    public void setDiagrams(List<Diagram> diagrams) {
        this.diagrams = diagrams;
    }
    public void fillEntitiesRowNo(){
        if(entities == null){
            return;
        }
        for(int i=1;i<=entities.size();i++){
            TableEntity entity = entities.get(i-1);
            if(entity==null){
                continue;
            }
            entity.setRowNo(i);
        }
    }
    public void fillDictsRowNo(){
        if(dicts == null){
            return;
        }
        for(int i=1;i<=dicts.size();i++){
            Dict dict = dicts.get(i-1);
            if(dict==null){
                continue;
            }
            List<DictItem> items = dict.getItems();
            for(int j=1;j<=items.size();j++){
                DictItem item = items.get(j-1);
                if(item==null){
                    continue;
                }
                item.setRowNo(j);
            }
        }
    }

    public boolean isEmpty(){
        if((entities == null || entities.size() == 0)
        &&(diagrams == null || diagrams.size() == 0)
        &&(dicts == null || dicts.size() == 0)){
            return true;
        }else{
            return false;
        }

    }
}
