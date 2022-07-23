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
package cn.com.chiner.java.command.impl;

import cn.com.chiner.java.command.Command;
import cn.com.chiner.java.command.ExecResult;
import cn.com.chiner.java.command.kit.ConnParseKit;
import cn.com.chiner.java.model.*;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.raw.lang.ValueObject;
import cn.com.chiner.java.pdm.model.PDColumn;
import cn.com.chiner.java.pdm.model.PDDomain;
import cn.com.chiner.java.pdm.model.PDKey;
import cn.com.chiner.java.pdm.model.PDTable;
import org.apache.commons.lang3.tuple.Pair;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.dom4j.tree.FlyweightProcessingInstruction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2021/6/14
 * @desc : 解析PDM文件
 */
public class ParsePDMFileImpl implements Command<ExecResult> {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public ExecResult exec(Map<String, String> params) {

        String pdmFile = params.get("pdmFile");

        SAXReader reader = new SAXReader();
        File inFile = new File(pdmFile);

        ExecResult ret = new ExecResult();
        try {
            Document document = reader.read(inFile);
            List<Node> contentList = document.content();
            if(contentList == null || contentList.size() == 0){
                throw new IllegalStateException("文件"+pdmFile+"格式不正确");
            }
            FlyweightProcessingInstruction declearNode = (FlyweightProcessingInstruction)contentList.get(0);
            String projectName = declearNode.getValue("Name");
            String version = declearNode.getValue("version");
            if(version.length()>3){
                version = version.substring(0,4);
            }
            //低于16.5版本
            if(version.compareTo("16.5") < 0){
                throw new IllegalStateException("文件["+pdmFile+"]版本为["+version+"],不正确，请使用PowerDesigner-16.5以上版本的结果文件，如果没有，请你先用16.5的打开旧文件后另存为。");
            }


            List<Node> domainNodeList = document.selectNodes("/Model/o:RootObject/c:Children/o:Model/c:Domains/o:PhysicalDomain");
//            List<Node> tableNodeList = document.selectNodes("/Model/o:RootObject/c:Children/*/c:Tables/o:Table");
            List<Node> tableNodeList = document.selectNodes("//c:Tables/o:Table");  //使用通配置查找所有表

            List<PDTable> tableList = new ArrayList<>();
            List<PDDomain> domainList = new ArrayList<>();
            for(Node domainNode : domainNodeList){
                PDDomain domain = createDomain(domainNode);
                if(domain == null){
                    continue;
                }
                domainList.add(domain);
            }
            for(Node tableNode : tableNodeList){
                PDTable table = createTable(tableNode,domainList);
                if(table == null){
                    continue;
                }
                tableList.add(table);
            }


            List<TableEntity> tableEntities = new ArrayList<>();
            List<Domain> domains = new ArrayList<Domain>();

            //填充模型数据
            fillTableEntities(tableList,tableEntities);
            fillDomains(domainList,domains);

            ret.setBody(new HashMap<String,Object>(){{
                put("projectName",projectName);
                put("tables",tableEntities);
                put("domains",domains);
            }});
            ret.setStatus(ExecResult.SUCCESS);
        } catch (DocumentException e) {
            String message = e.getMessage();
            if(StringKit.isBlank(message)){
                message = e.toString();
            }
            ret.setBody(message);
            ret.setStatus(ExecResult.FAILED);
            logger.error("",e);
        }

        return ret;
    }

    private void fillTableEntities(List<PDTable> tableList,List<TableEntity> tableEntities){
        for(PDTable pdTable : tableList){
            TableEntity te = new TableEntity();
            te.setDefKey(pdTable.getCode());
            te.setDefName(pdTable.getName());
            te.setComment(pdTable.getComment());
            te.setRowNo(tableList.indexOf(pdTable)+1);

            List<PDColumn> pdColumns = pdTable.getColumns();
            for(int i=0;i<pdColumns.size();i++){
                PDColumn pdColumn = pdColumns.get(i);
                ColumnField field = new ColumnField();
                field.setDefKey(pdColumn.getCode());
                field.setDefName(pdColumn.getName());
                field.setComment(pdColumn.getComment());
                field.setDomain(pdColumn.getDomain());
                field.setType(pdColumn.getDataType());
                field.setLen(pdColumn.getLength());
                field.setScale(pdColumn.getPrecision());
                field.setPrimaryKey(pdColumn.getPrimaryKey());
                field.setNotNull(pdColumn.getMandatory());
                field.setHideInGraph(i>15);
                //名称和注释相同的情况下，只保留名称
                if(StringKit.nvl(field.getComment(),"").equalsIgnoreCase(field.getDefName())){
                    field.setComment("");
                }
                te.getFields().add(field);
            }
            List<TableIndex> indexes = new ArrayList<>();
            List<PDKey> pdKeys = pdTable.getKeys();
            for(PDKey pdKey : pdKeys){
                TableIndex index = new TableIndex();
                index.setDefKey(pdKey.getCode());
                index.setDefName(pdKey.getName());
                index.setUnique(false);

                List<PDColumn> pdkColumns = pdKey.getColumns();
                for(PDColumn pdkColumn : pdkColumns){
                    TableIndexColumnField idxField = new TableIndexColumnField();
                    idxField.setFieldDefKey(pdkColumn.getCode());
                    index.getFields().add(idxField);
                }
                indexes.add(index);
            }
            te.setIndexes(indexes);
            te.fillFieldsCalcValue();
            tableEntities.add(te);
        }
    }

    private Map<String,String> dataTypeMap = new HashMap<String,String>(){{
        put("VARCHAR","string");
        put("NVARCHAR","string");
        put("VARCHAR2","string");
        put("NVARCHAR2","string");
        put("NUMBER","double");
        put("NUMERIC","double");
        put("DECIMAL","double");
        put("INTEGER","int");
        put("INT","int");
        put("DATE","date");
        put("DATETIME","date");
        put("TIMESTAMP","date");
        put("BLOB","bytes");
        put("VARBINARY","bytes");
        put("BYTEA","bytes");
        put("TEXT","largeText");
        put("CLOB","largeText");
    }};
    private String matchDataType(String dataType){
        String retType = dataTypeMap.get(dataType.toUpperCase());
        return retType == null?"string":retType;
    }
    private void fillDomains(List<PDDomain> domainList,List<Domain> domains){
        for(PDDomain pdDomain : domainList){
            String dataType = pdDomain.getDataType();
            String applyFor = matchDataType(dataType);

            Domain domain = new Domain();
            domain.setApplyFor(applyFor);
            domain.setDefKey(pdDomain.getCode());
            domain.setDefName(pdDomain.getName());
            domain.setLen(pdDomain.getLength());
            domain.setScale(pdDomain.getPrecision());
            domain.setUiHint(new HashMap<>());

            domains.add(domain);
        }
    }

    public PDTable createTable(Node node, List<PDDomain> domains){
        String id = getAttributeValue(node,"Id");
        String objectId = getChildNodeValue(node,"a:ObjectID").strValue();
        String name = getChildNodeValue(node,"a:Name").strValue("");
        String code = getChildNodeValue(node,"a:Code").strValue("");
        String comment = getChildNodeValue(node,"a:Comment").strValue("");
        Date creationDate = getChildNodeValue(node,"a:CreationDate").dateValue();
        String creator = getChildNodeValue(node,"a:Creator").strValue();
        Date modificationDate = getChildNodeValue(node,"a:ModificationDate").dateValue();
        String modifier = getChildNodeValue(node,"a:Modifier").strValue();
        if(StringKit.isBlank(code) && StringKit.isBlank(name)){
            return null;
        }

        PDTable table = new PDTable();
        table.setId(id);
        table.setObjectID(objectId);
        table.setCode(code);
        table.setName(name);
        table.setComment(comment);
        table.setCreationDate(creationDate);
        table.setCreator(creator);
        table.setModificationDate(modificationDate);
        table.setModifier(modifier);
        //当代码和名称一样，且注释不为空时，作下转换
        if(code.equalsIgnoreCase(name) && StringKit.isNotBlank(comment)){
            Pair<String,String> columnRemarks = ConnParseKit.parseNameAndComment(comment);
            name = columnRemarks.getLeft();
            comment = columnRemarks.getRight();
            table.setName(name);
            table.setComment(comment);
        }

        //处理列
        List<Node> columnNodeList = node.selectNodes("c:Columns/o:Column");
        for(Node columnNode : columnNodeList){
            PDColumn column = createColumn(columnNode);
            if(column == null){
                continue;
            }
            table.getColumns().add(column);
            //处理关联的数据域
            Node domainNode = columnNode.selectSingleNode("c:Domain/o:PhysicalDomain");
            String domainRef = getAttributeValue(domainNode,"Ref");
            if(StringKit.isNotBlank(domainRef)){
                for(PDDomain domain : domains){
                    if(domainRef.equalsIgnoreCase(domain.getId())){
                        column.setDomain(domain.getCode());
                    }
                }
            }
        }

        //处理索引
        List<Node> keyNodeList = node.selectNodes("c:Keys/o:Key");
        for(Node keyNode : keyNodeList){
            String keyId = getAttributeValue(keyNode,"Id");
            String keyObjectId = getChildNodeValue(keyNode,"a:ObjectID").strValue();
            String keyName = getChildNodeValue(keyNode,"a:Name").strValue("");
            String keyCode = getChildNodeValue(keyNode,"a:Code").strValue("");
            Date keyCreationDate = getChildNodeValue(keyNode,"a:CreationDate").dateValue();
            String keyCreator = getChildNodeValue(keyNode,"a:Creator").strValue();
            Date keyModificationDate = getChildNodeValue(keyNode,"a:ModificationDate").dateValue();
            String keyModifier = getChildNodeValue(keyNode,"a:Modifier").strValue();
            if(StringKit.isBlank(code) && StringKit.isBlank(name)){
                continue;
            }

            PDKey key = new PDKey();
            key.setId(keyId);
            key.setObjectID(keyObjectId);
            key.setCode(keyCode);
            key.setName(keyName);
            key.setCreationDate(keyCreationDate);
            key.setCreator(keyCreator);
            key.setModificationDate(keyModificationDate);
            key.setModifier(keyModifier);

            List<Node> keyColList = keyNode.selectNodes("c:Key.Columns/o:Column");
            for(Node colNode : keyColList){
                String colRef = getAttributeValue(colNode,"Ref");
                for(PDColumn column : table.getColumns()){
                    if(colRef.equalsIgnoreCase(column.getId())){
                        key.getColumns().add(column);
                    }
                }
            }
            table.getKeys().add(key);
        }

        //处理是否主键
        List<Node> pkNodeList = node.selectNodes("c:PrimaryKey/o:Key");
        for(Node pkNode : pkNodeList){
            String pkRef = getAttributeValue(pkNode,"Ref");
            if(StringKit.isNotBlank(pkRef)){
                for(PDKey key : table.getKeys()){
                    if(pkRef.equalsIgnoreCase(key.getId())){
                        for(PDColumn column : key.getColumns()){
                            column.setPrimaryKey(true);
                        }
                    }
                }
            }
        }
        return table;
    }

    public PDColumn createColumn(Node node){
        String id = getAttributeValue(node,"Id");
        String objectId = getChildNodeValue(node,"a:ObjectID").strValue();
        String code = getChildNodeValue(node,"a:Code").strValue("");
        String name = getChildNodeValue(node,"a:Name").strValue("");
        String comment = getChildNodeValue(node,"a:Comment").strValue("");
        Date creationDate = getChildNodeValue(node,"a:CreationDate").dateValue();
        String creator = getChildNodeValue(node,"a:Creator").strValue();
        Date modificationDate = getChildNodeValue(node,"a:ModificationDate").dateValue();
        String modifier = getChildNodeValue(node,"a:Modifier").strValue();
        String dataType = getChildNodeValue(node,"a:DataType").strValue("");
        Integer length = getChildNodeValue(node,"a:Length").intValue();
        Integer precision = getChildNodeValue(node,"a:Precision").intValue();
        if(StringKit.isBlank(code) && StringKit.isBlank(name)){
            return null;
        }


        PDColumn column = new PDColumn();
        column.setId(id);
        column.setObjectID(objectId);
        column.setCode(code);
        column.setName(name);
        column.setComment(comment);
        column.setCreationDate(creationDate);
        column.setCreator(creator);
        column.setModificationDate(modificationDate);
        column.setModifier(modifier);
        column.setDataType(cleanDataType(dataType));
        column.setLength(length);
        column.setPrecision(precision);
        //当代码和名称一样，且注释不为空时，作下转换
        if(code.equalsIgnoreCase(name) && StringKit.isNotBlank(comment)){
            Pair<String,String> columnRemarks = ConnParseKit.parseNameAndComment(comment);
            name = columnRemarks.getLeft();
            comment = columnRemarks.getRight();
            column.setName(name);
            column.setComment(comment);
        }

        return column;
    }

    public PDDomain createDomain(Node node){
        String id = getAttributeValue(node,"Id");
        String objectId = getChildNodeValue(node,"a:ObjectID").strValue();
        String name = getChildNodeValue(node,"a:Name").strValue();
        String code = getChildNodeValue(node,"a:Code").strValue();
        Date creationDate = getChildNodeValue(node,"a:CreationDate").dateValue();
        String creator = getChildNodeValue(node,"a:Creator").strValue();
        Date modificationDate = getChildNodeValue(node,"a:ModificationDate").dateValue();
        String modifier = getChildNodeValue(node,"a:Modifier").strValue();
        String dataType = getChildNodeValue(node,"a:DataType").strValue();
        Integer length = getChildNodeValue(node,"a:Length").intValue();
        Integer precision = getChildNodeValue(node,"a:Precision").intValue();
        if(StringKit.isBlank(code) && StringKit.isBlank(name)){
            return null;
        }

        PDDomain domain = new PDDomain();
        domain.setId(id);
        domain.setObjectID(objectId);
        domain.setCode(code);
        domain.setName(name);
        domain.setCreationDate(creationDate);
        domain.setCreator(creator);
        domain.setModificationDate(modificationDate);
        domain.setModifier(modifier);
        domain.setDataType(cleanDataType(dataType));
        domain.setLength(length);
        domain.setPrecision(precision);
        return domain;
    }

    /**
     * 取节点字串值
     * @param node
     * @param childNodeName
     * @return
     */
    public ValueObject getChildNodeValue(Node node, String childNodeName){
        Node childNode = node.selectSingleNode(childNodeName);
        if(childNode != null && childNode instanceof Element){
            Element element = (Element)childNode;
            return ValueObject.valueOf(element.getStringValue());
        }
        return ValueObject.valueOf(null);
    }

    /**
     * 取节点属性值
     * @param node
     * @param attributeName
     * @return
     */
    public String getAttributeValue(Node node,String attributeName){
        if(node != null && node instanceof Element){
            Element element = (Element)node;
            return element.attribute(attributeName).getValue();
        }
        return null;
    }

    public String cleanDataType(String dataType){
        if(dataType.indexOf("(")>0 && dataType.indexOf(")")>0){
            return dataType.substring(0,dataType.indexOf("("));
        }
        return dataType;
    }
}
