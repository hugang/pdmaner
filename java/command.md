# maven 手动安装
**kingbase驱动**
```
mvn install:install-file -Dfile=/Users/asher/workspace/ws-vekai/chiner-java/jdbc/kingbase8-8.6.0.jar -DgroupId=com.kingbase -DartifactId=kingbase8 -Dversion=8.6.0 -Dpackaging=jar
```

# 执行命令
**解析数据库表**
```
java -jar chiner-java.jar DBReverseGetTableDDL driver_class_name=com.kingbase8.Driver url=jdbc:kingbase8://10.211.55.3:54321/test username=system password=system tables=SIMS_CLASS,SIMS_STUDENT out=/Users/asher/workspace/ws-vekai/siner-java/src/test/resources/out/dbrgtddl-123.json
```


# 执行命令
**解析DDL**
```
java -jar chiner-java.jar ParseDDLToTableImpl ddlFile=/Users/asher/workspace/ws-vekai/chiner-java/src/test/resources/sql/oracle-ddl.sql out=/Users/asher/workspace/ws-vekai/chiner-java/src/test/resources/out/parse-ddl-01.json
```

**生成WORD**
```
java -jar chiner-java.jar GenDocx sinerFile=/Users/yangsong158/workspace/ws-chiner/chiner-java/src/test/resources/pdma/教学管理系统-标准模板.pdma.json docxTpl=/Users/yangsong158/workspace/ws-chiner/chiner-java/src/test/resources/tpl/siner-docx-tpl.docx imgDir=/Users/asher/Library/Application Support/Electron/temp_img imgExt=.png outFile=/Users/yangsong158/workspace/ws-chiner/chiner-java/src/test/resources/out/gendocx-x.docx out=/Users/yangsong158/workspace/ws-chiner/chiner-java/src/test/resources/out/gendocx-x.json
```
