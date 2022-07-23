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
import cn.com.chiner.java.model.*;
import cn.fisok.raw.kit.FileKit;
import cn.fisok.raw.kit.IOKit;
import cn.fisok.raw.kit.JSONKit;
import cn.fisok.raw.lang.ValueObject;
import cn.com.chiner.java.command.ExecResult;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.config.ConfigureBuilder;
import com.deepoove.poi.data.PictureRenderData;
import com.deepoove.poi.data.PictureType;
import com.deepoove.poi.plugin.bookmark.BookmarkRenderPolicy;
import com.deepoove.poi.plugin.table.LoopRowTableRenderPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2021/6/14
 * @desc : 生成WORD文件
 */
public class GenDocxImpl implements Command<ExecResult> {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public ExecResult exec(Map<String, String> params) {
        String sinerFile = params.get("sinerFile");
        String docxTpl = params.get("docxTpl");
        String imgDir = params.get("imgDir");
        String imgExt = params.get("imgExt");
        String outFile = params.get("outFile"); //输出的文档文件
        String out = params.get("out");         //输出的结果json文件

        ExecResult ret = new ExecResult();

        try {
            exec(sinerFile, docxTpl, imgDir, imgExt, outFile,out);
            ret.setStatus(ExecResult.SUCCESS);
            ret.setBody(outFile);
        } catch (Exception e) {
            String message = e.getMessage();
            if(message == null){
                message = e.toString();
            }
            ret.setStatus(ExecResult.FAILED);
            ret.setBody(message);
            logger.error("", e);
        }
        return ret;
    }

    protected void exec(String sinerFile, String docxTpl, String imgDir, String imgExt, String outFile,String out) throws IOException {
        File inDataFile = new File(sinerFile);
        File docTplFile = new File(docxTpl);
        File outDocxFile = new File(outFile);

        String jsonText = parseFile(inDataFile);

        Project project = null;
        ProjectOriginal projectOriginal = JSONKit.jsonToBean(jsonText,ProjectOriginal.class);
        List<Object> objectEntities = projectOriginal.getEntities();
        List<Object> objectViews = projectOriginal.getViews();
        List<Object> objectDicts = projectOriginal.getDicts();
        List<Object> objectDiagrams = projectOriginal.getDiagrams();
        List<Object> viewGroups = projectOriginal.getViewGroups();

        String textEntities = JSONKit.toJsonString(objectEntities);
        String textViews = JSONKit.toJsonString(objectViews);
        String textDicts = JSONKit.toJsonString(objectDicts);
        String textDiagrams = JSONKit.toJsonString(objectDiagrams);

        List<TableEntity> entities = JSONKit.jsonToBeanList(textEntities,TableEntity.class);
        List<View> views = JSONKit.jsonToBeanList(textViews,View.class);
        List<Dict> dicts = JSONKit.jsonToBeanList(textDicts,Dict.class);
        List<Diagram> diagrams = JSONKit.jsonToBeanList(textDiagrams,Diagram.class);
        if(entities == null){
            entities = new ArrayList<>();
        }
        if(views == null){
            views = new ArrayList<>();
        }
        if(dicts == null){
            dicts = new ArrayList<>();
        }
        if(diagrams == null){
            diagrams = new ArrayList<>();
        }

        List<Module> modules = new ArrayList<Module>();
        project = JSONKit.jsonToBean(jsonText,Project.class);
        project.setModules(modules);

        if(viewGroups == null || viewGroups.size() == 0){
            //如果没有分组，则构建一个默认分组
            Module module = new Module();
            modules.add(module);
            module.setDefKey("CHINER");
            module.setDefName(project.getName());


            module.setEntities(entities);
            module.setViews(views);
            module.setDicts(dicts);
            module.setDiagrams(diagrams);
            //处理关系图
            for(Diagram diagram : diagrams){
                PictureRenderData renderData = createPictureRenderData(imgDir,imgExt,diagram.getId());
                if(renderData != null){
                    diagram.setRenderData(renderData);
                }
            }

            for(TableEntity entity : entities){
                entity.setDicts(dicts);
                entity.fillFieldsCalcValue();
            }

            module.fillEntitiesRowNo();
            module.fillDictsRowNo();

        }else{
            List<TableEntity> findedEntities = new ArrayList<>();
            List<Diagram> findDiagrams = new ArrayList<>();
            List<Dict> findedDicts = new ArrayList<>();

            //处理模块中包含的数据表+数据字典
            for(Object viewGroup : viewGroups){
                Map<String,Object> mapModule = ( Map<String,Object>)viewGroup;
                String moduleDefKey = ValueObject.valueOf(mapModule.get("defKey")).strValue("");
                String moduleDefName = ValueObject.valueOf(mapModule.get("defName")).strValue("");

                Module module = new Module();
                modules.add(module);
                module.setDefKey(moduleDefKey);
                module.setDefName(moduleDefName);
                module.setEntities(new ArrayList<>());
                module.setDicts(new ArrayList<>());
                module.setDiagrams(new ArrayList<>());
                //填充好本模块
                fillModule(module,mapModule,entities,dicts,diagrams,findedEntities,findDiagrams,findedDicts,imgDir,imgExt);
            }

            //处理没有包含在模块中的表+关系图+数据字典
            List<TableEntity> remainTableEntities = new ArrayList<>();
            List<Diagram> remainDiagrams = new ArrayList<>();
            List<Dict> remainDicts = new ArrayList<>();
            for(TableEntity entity : entities){
                //没有被找到过，说明不在模块分组里
                if(findedEntities.indexOf(entity) < 0){
                    remainTableEntities.add(entity);
                }
            }
            for(Diagram diagram : diagrams){
                if(findDiagrams.indexOf(diagram) < 0){
                    remainDiagrams.add(diagram);
                }
            }
            for(Dict dict : dicts){
                if(findedDicts.indexOf(dict) < 0){
                    remainDicts.add(dict);
                }
            }
            //其他模块这个分组只有内容不空时，才能加到模型中去
            Module otherModule = new Module();
            otherModule.setDefKey("OTHER");
            otherModule.setDefName("其他补充");
            otherModule.setEntities(new ArrayList<>());
            otherModule.setDiagrams(new ArrayList<>());
            otherModule.setDicts(new ArrayList<>());
            otherModule.setEntities(remainTableEntities);
            otherModule.setDiagrams(remainDiagrams);
            otherModule.setDicts(remainDicts);
            if(!otherModule.isEmpty()){
                modules.add(otherModule);
            }

        }


        LoopRowTableRenderPolicy policy = new LoopRowTableRenderPolicy();
        ConfigureBuilder builder = Configure.newBuilder();
        Configure config = builder
                .bind("entities", policy)
                .bind("fields", policy)
                .bind("items", policy)
//                    .bind("image", new PictureRenderPolicy())
                .addPlugin('>', new BookmarkRenderPolicy())
//                .addPlugin('>', new BookmarkTextRenderData())
                .build();


//        System.out.println(JSONKit.toJsonString(project,true));
        try(OutputStream osOutDocxFile = new FileOutputStream(outDocxFile);){
            XWPFTemplate template = XWPFTemplate.compile(docTplFile,config).render(project);
            template.write(osOutDocxFile);
            osOutDocxFile.flush();
            osOutDocxFile.close();
            template.close();
        }catch (Exception e){
            throw e;
        }


    }

    protected PictureRenderData createPictureRenderData(String imgDir,String imgExt,String diagramId){
        String filePath = imgDir+"/"+diagramId+".png";
        FileInputStream fileIn = null;
        double width = 650.0;
        double height = 400.0;
        try {
            File inFile = new File(filePath);
            if(!inFile.exists()){
                return null;
            }
            fileIn = new FileInputStream(inFile);
            BufferedImage bufferedImage = ImageIO.read(fileIn);
            width = bufferedImage.getWidth();
            height = bufferedImage.getHeight();
        } catch (IOException e) {
            logger.error("读取图片文件出错",e);
            return null;
        } finally {
            IOKit.close(fileIn);
        }

        PictureRenderData renderData = null;
        try {
            fileIn = new FileInputStream(filePath);
//            renderData = new PictureRenderData(650, (int)(height/width*650), imgExt, fileIn);
            PictureType pictureType = PictureType.suggestFileType(imgExt);
            renderData = new PictureRenderData(650, (int)(height/width*650), pictureType, fileIn);
        } catch (FileNotFoundException e) {
            logger.error("图片文件不存在",e);
            return null;
        } finally {
            IOKit.close(fileIn);
        }

        return renderData;

    }

    private void fillModule(Module module,
                            Map<String, Object> mapModule,
                            List<TableEntity> entities,
                            List<Dict> dicts,
                            List<Diagram> diagrams,
                            List<TableEntity> findedEntities,
                            List<Diagram> findDiagrams,
                            List<Dict> findedDicts,
                            String imgDir,
                            String imgExt
                            ) {
        List<String> refEntities = (List<String>) mapModule.get("refEntities");
        List<String> refViews = (List<String>) mapModule.get("refViews");
        List<String> refDiagrams = (List<String>) mapModule.get("refDiagrams");
        List<String> refDicts = (List<String>) mapModule.get("refDicts");

        if (refEntities != null && refEntities.size() >= 0) {
            for (String entityDefKey : refEntities) {
                TableEntity entity = lookupTableEntity(entities, entityDefKey);
                if (entity != null) {
//                    entity.fillFieldsCalcValue();
                    findedEntities.add(entity);
                    module.getEntities().add(entity);
                }
            }
        }

        if (refDicts != null && refDicts.size() > 0) {
            for (String dictDefKey : refDicts) {
                Dict dict = lookupDict(dicts, dictDefKey);
                if(dict != null){
                    findedDicts.add(dict);
                    module.getDicts().add(dict);
                }
            }
        }

        if(refDiagrams != null && refDiagrams.size() > 0){
            for(String diagramId : refDiagrams){
                Diagram diagram = lookupDiagram(diagrams,diagramId);
                if(diagram != null){
                    module.getDiagrams().add(diagram);
                    findDiagrams.add(diagram);
                    PictureRenderData renderData = createPictureRenderData(imgDir,imgExt,diagram.getId());
                    if(renderData != null){
                        diagram.setRenderData(renderData);
                    }
                }
            }
        }

        for(TableEntity entity : entities){
            entity.setDicts(dicts);
            entity.fillFieldsCalcValue();
        }
        module.fillEntitiesRowNo();
        module.fillDictsRowNo();
    }

    private TableEntity lookupTableEntity(List<TableEntity> entities,String entityDefKey){
        for(TableEntity entity : entities){
            if(entityDefKey.equalsIgnoreCase(entity.getId())){
                return entity;
            }
        }
        return null;
    }

    private Diagram lookupDiagram(List<Diagram> diagrams,String diagramId){
        for(Diagram diagram : diagrams){
            if(diagramId.equalsIgnoreCase(diagram.getId())){
                return diagram;
            }
        }
        return null;
    }


    private Dict lookupDict(List<Dict> dicts,String dictDefKey){
        for(Dict dict : dicts){
            if(dictDefKey.equalsIgnoreCase(dict.getId())){
                return dict;
            }
        }
        return null;
    }

    protected String parseFile(File sinerFile) throws IOException {
        String jsonText = null;
        try(InputStream inputStream = FileKit.openInputStream(sinerFile)) {
            jsonText = IOKit.toString(inputStream, "UTF-8");
//            mapObject = JSONKit.jsonToMap(jsonText);
        } catch (IOException e) {
            throw e;
        }
        return jsonText;
    }
}
