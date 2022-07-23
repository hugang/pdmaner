package cn.com.chiner.java.command.impl;

import cn.com.chiner.java.command.Command;
import cn.com.chiner.java.command.ExecResult;
import cn.com.chiner.java.model.Dict;
import cn.com.chiner.java.model.DictItem;
import cn.com.chiner.java.model.excel.DictRow;
import cn.fisok.raw.kit.StringKit;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2022/06/08
 * @desc : EXCEL字典行数据读取命令
 */
public class DictExcelReaderImpl implements Command<ExecResult> {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    public ExecResult exec(Map<String, String> params) {
        String excelFile = params.get("excelFile");

        ExecResult ret = new ExecResult();

        try {
            List<Dict> dicts = readDicts(new File(excelFile));
            ret.setStatus(ExecResult.SUCCESS);
            ret.setBody(dicts);
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

    protected List<Dict> readDicts(File file){
        List<DictRow> rows = new ArrayList<>();
        EasyExcel.read(file, DictRow.class, new ReadListener<DictRow>() {
            public void invoke(DictRow dictRow, AnalysisContext analysisContext) {
                rows.add(dictRow);
            }
            public void doAfterAllAnalysed(AnalysisContext analysisContext) {

            }
        }).autoCloseStream(true).sheet(0).headRowNumber(2).autoTrim(true).doReadSync();

        List<Dict> dictList = new ArrayList<>();
        Dict curDict = null;
        for(DictRow row : rows){
            if(StringKit.isNotBlank(row.getDefKey())){
                curDict = new Dict();
                curDict.setId(StringKit.uuid("-").toUpperCase());
                curDict.setDefKey(row.getDefKey());
                curDict.setDefName(row.getDefName());
                curDict.setIntro(row.getIntro());
                dictList.add(curDict);
            }
            String itemDefKey = row.getItemDefKey();
            //如果没有字典或者字典代码为空，则忽略掉
            if(curDict == null || StringKit.isBlank(itemDefKey)){
                continue;
            }
            DictItem item = new DictItem();
            item.setId(StringKit.uuid("-").toUpperCase());
            item.setDefKey(itemDefKey);
            item.setDefName(row.getItemDefName());
            item.setIntro(row.getItemIntro());
            item.setParentKey(row.getItemParentKey());
            item.setAttr1(row.getItemAttr1());
            item.setAttr2(row.getItemAttr2());
            item.setAttr3(row.getItemAttr3());
            item.setSort(row.getItemSort());
            curDict.getItems().add(item);
        }
        return dictList;
    }
}
