package cn.com.chiner.excel;

import cn.com.chiner.java.model.Dict;
import cn.com.chiner.java.model.DictItem;
import cn.com.chiner.java.model.excel.DictRow;
import cn.fisok.raw.kit.JSONKit;
import cn.fisok.raw.kit.StringKit;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ExcelDictReadTest {
    @Test
    public void test01() {
        List<DictRow> rows = new ArrayList<>();
        File file = new File("/Users/yangsong158/workspace/ws-chiner/chiner-java/src/test/resources/数据字典模板.xlsx");
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
            DictItem item = new DictItem();
            item.setId(StringKit.uuid("-").toUpperCase());
            item.setDefKey(row.getItemDefKey());
            item.setDefName(row.getItemDefName());
            item.setIntro(row.getItemIntro());
            item.setParentKey(row.getItemParentKey());
            item.setAttr1(row.getItemAttr1());
            item.setAttr2(row.getItemAttr2());
            item.setAttr3(row.getItemAttr3());
            item.setSort(row.getItemSort());
            curDict.getItems().add(item);
        }
        System.out.println(JSONKit.toJsonString(dictList,true));
    }
}
