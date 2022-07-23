--添加表名称注释
EXEC sp_addextendedproperty 'MS_Description', '客户基础信息', 'SCHEMA', dbo, 'table', CUST_BASE, null, null;
--增加字段注释
EXEC sp_addextendedproperty 'MS_Description', '客户ID', 'SCHEMA', dbo, 'table', CUST_BASE, 'column', CUST_ID;
EXEC sp_addextendedproperty 'MS_Description', '客户名', 'SCHEMA', dbo, 'table', CUST_BASE, 'column', CUST_NAME;
EXEC sp_addextendedproperty 'MS_Description', '客户类型', 'SCHEMA', dbo, 'table', CUST_BASE, 'column', CUST_TYPE;
EXEC sp_addextendedproperty 'MS_Description', '证件国别', 'SCHEMA', dbo, 'table', CUST_BASE, 'column', CERT_COUNTRY;
EXEC sp_addextendedproperty 'MS_Description', '证件类型', 'SCHEMA', dbo, 'table', CUST_BASE, 'column', CERT_TYPE;
EXEC sp_addextendedproperty 'MS_Description', '证件号', 'SCHEMA', dbo, 'table', CUST_BASE, 'column', CERT_ID;


--读取注释
SELECT
	表名 =
CASE
		WHEN a.colorder= 1 THEN
		d.name ELSE ''
	END,
	表说明 =
CASE
	WHEN a.colorder= 1 THEN
	ISNULL( f.value, '' ) ELSE ''
	END,
	字段序号 = a.colorder,
	字段名 = a.name,
	标识 =
CASE
	WHEN COLUMNPROPERTY( a.id, a.name, 'IsIdentity' ) = 1 THEN
	'Y' ELSE ''
	END,
	主键 =
CASE
	WHEN EXISTS (
	SELECT
		1
	FROM
		sysobjects
	WHERE
		xtype = 'PK'
		AND name IN ( SELECT name FROM sysindexes WHERE indid IN ( SELECT indid FROM sysindexkeys WHERE id = a.id AND colid = a.colid ) )
		) THEN
		'Y' ELSE ''
	END,
	类型 = b.name,
	占用字节数 = a.length,
	长度 = COLUMNPROPERTY( a.id, a.name, 'PRECISION' ),
	小数位数 = ISNULL( COLUMNPROPERTY( a.id, a.name, 'Scale' ), 0 ),
	允许空 =
CASE
		WHEN a.isnullable= 1 THEN
		'Y' ELSE ''
	END,
	默认值 = ISNULL( e.text, '' ),字段说明 = ISNULL( g.[value], '' )
FROM
	syscolumns a
	LEFT JOIN systypes b ON a.xusertype= b.xusertype
	INNER JOIN sysobjects d ON a.id= d.id
	AND d.xtype= 'U'
	AND d.name<> 'dtproperties'
	LEFT JOIN syscomments e ON a.cdefault= e.id
	LEFT JOIN sys.extended_properties g ON a.id= g.major_id
	AND a.colid= g.minor_id
	LEFT JOIN sys.extended_properties f ON d.id= f.major_id
	AND f.minor_id= 0
--where   d.name='CUST_ENT'
ORDER BY
	a.id,
	a.colorder;

------------
SELECT
	table_code =
CASE
		WHEN a.colorder= 1 THEN
		d.name ELSE ''
	END,
	table_comment =
CASE
	WHEN a.colorder= 1 THEN
	ISNULL( f.value, '' ) ELSE ''
	END,
	column_index = a.colorder,
	column_name = a.name,
	column_is_identity =
CASE
	WHEN COLUMNPROPERTY( a.id, a.name, 'IsIdentity' ) = 1 THEN
	'Y' ELSE ''
	END,
	column_is_primary_key =
CASE
	WHEN EXISTS (
	SELECT
		1
	FROM
		sysobjects
	WHERE
		xtype = 'PK'
		AND name IN ( SELECT name FROM sysindexes WHERE indid IN ( SELECT indid FROM sysindexkeys WHERE id = a.id AND colid = a.colid ) )
		) THEN
		'Y' ELSE ''
	END,
	column_data_type = b.name,
	column_data_bytes = a.length,
	column_data_length = COLUMNPROPERTY( a.id, a.name, 'PRECISION' ),
	column_data_scale = ISNULL( COLUMNPROPERTY( a.id, a.name, 'Scale' ), 0 ),
	column_data_is_required =
CASE
		WHEN a.isnullable= 1 THEN
		'Y' ELSE ''
	END,
	column_default_value = ISNULL( e.text, '' ),
	column_comment = ISNULL( g.[value], '' )
FROM
	syscolumns a
	LEFT JOIN systypes b ON a.xusertype= b.xusertype
	INNER JOIN sysobjects d ON a.id= d.id
	AND d.xtype= 'U'
	AND d.name<> 'dtproperties'
	LEFT JOIN syscomments e ON a.cdefault= e.id
	LEFT JOIN sys.extended_properties g ON a.id= g.major_id
	AND a.colid= g.minor_id
	LEFT JOIN sys.extended_properties f ON d.id= f.major_id
	AND f.minor_id= 0
where   d.name='CUST_BASE'
ORDER BY
	a.id,
	a.colorder;

---------
SELECT
	tb.name AS tableName,
	col.name AS columnName,
	col.max_length AS length,
	col.is_nullable AS isNullable,
	t.name AS type,
	(
	SELECT
		TOP 1 ind.is_primary_key
	FROM
		sys.index_columns ic
		LEFT JOIN sys.indexes ind ON ic.object_id = ind.object_id AND ic.index_id= ind.index_id AND ind.name LIKE 'PK_%'
	WHERE
		ic.object_id = tb.object_id AND ic.column_id= col.column_id
	) AS isPrimaryKey,
	CONVERT(varchar(200), com.value) AS comment
FROM
	sys.TABLES tb
	INNER JOIN sys.columns col ON col.object_id = tb.object_id
	LEFT JOIN sys.types t ON t.user_type_id = col.user_type_id
	LEFT JOIN sys.extended_properties com ON com.major_id = col.object_id
	AND com.minor_id = col.column_id
WHERE
	tb.name = 'CUST_BASE'