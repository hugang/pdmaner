package cn.com.chiner.java.model.excel;

import com.alibaba.excel.annotation.ExcelProperty;

import java.io.Serializable;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2022/06/08
 * @desc : EXCEL字典行数据
 */
public class DictRow implements Serializable,Cloneable{
    @ExcelProperty("字典代码")
    private String defKey;
    @ExcelProperty("显示名称")
    private String defName;
    @ExcelProperty("字典说明")
    private String intro;
    @ExcelProperty("条目代码")
    private String itemDefKey;
    @ExcelProperty("条目显示名称")
    private String itemDefName;
    @ExcelProperty("条目顺序号")
    private String itemSort;
    @ExcelProperty("父条目代码")
    private String itemParentKey;
    @ExcelProperty("条目说明")
    private String itemIntro;
    @ExcelProperty("扩展属性1")
    private String itemAttr1;
    @ExcelProperty("扩展属性2")
    private String itemAttr2;
    @ExcelProperty("扩展属性3")
    private String itemAttr3;

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

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getItemDefKey() {
        return itemDefKey;
    }

    public void setItemDefKey(String itemDefKey) {
        this.itemDefKey = itemDefKey;
    }

    public String getItemDefName() {
        return itemDefName;
    }

    public void setItemDefName(String itemDefName) {
        this.itemDefName = itemDefName;
    }

    public String getItemSort() {
        return itemSort;
    }

    public void setItemSort(String itemSort) {
        this.itemSort = itemSort;
    }

    public String getItemParentKey() {
        return itemParentKey;
    }

    public void setItemParentKey(String itemParentKey) {
        this.itemParentKey = itemParentKey;
    }

    public String getItemIntro() {
        return itemIntro;
    }

    public void setItemIntro(String itemIntro) {
        this.itemIntro = itemIntro;
    }

    public String getItemAttr1() {
        return itemAttr1;
    }

    public void setItemAttr1(String itemAttr1) {
        this.itemAttr1 = itemAttr1;
    }

    public String getItemAttr2() {
        return itemAttr2;
    }

    public void setItemAttr2(String itemAttr2) {
        this.itemAttr2 = itemAttr2;
    }

    public String getItemAttr3() {
        return itemAttr3;
    }

    public void setItemAttr3(String itemAttr3) {
        this.itemAttr3 = itemAttr3;
    }
}
