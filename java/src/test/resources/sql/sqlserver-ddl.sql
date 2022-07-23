DROP TABLE IF EXISTS SIMS_CLASS;
CREATE TABLE SIMS_CLASS(
    COLLEGE_ID VARCHAR(32),
    MAJOR_ID VARCHAR(32),
    CLASS_ID VARCHAR(32) NOT NULL,
    CLASS_NAME VARCHAR(90),
    STUDENT_NUMBER INT,
    ADVISER VARCHAR(90),
    ESTAB_DATE DATETIME,
    YEAR_NUMBER INT,
    TENANT_ID VARCHAR(32),
    REVISION INT,
    CREATED_BY VARCHAR(32),
    CREATED_TIME DATETIME,
    UPDATED_BY VARCHAR(32),
    UPDATED_TIME DATETIME,
    PRIMARY KEY (CLASS_ID)
);

EXEC sp_addextendedproperty 'MS_Description', '班级', 'SCHEMA', dbo, 'table', SIMS_CLASS, null, null;
EXEC sp_addextendedproperty 'MS_Description', '所在学院', 'SCHEMA', dbo, 'table', SIMS_CLASS, 'column', COLLEGE_ID;
EXEC sp_addextendedproperty 'MS_Description', '所属专业ID', 'SCHEMA', dbo, 'table', SIMS_CLASS, 'column', MAJOR_ID;
EXEC sp_addextendedproperty 'MS_Description', '班级ID', 'SCHEMA', dbo, 'table', SIMS_CLASS, 'column', CLASS_ID;
EXEC sp_addextendedproperty 'MS_Description', '班级名称', 'SCHEMA', dbo, 'table', SIMS_CLASS, 'column', CLASS_NAME;
EXEC sp_addextendedproperty 'MS_Description', '班级人数', 'SCHEMA', dbo, 'table', SIMS_CLASS, 'column', STUDENT_NUMBER;
EXEC sp_addextendedproperty 'MS_Description', '辅导员', 'SCHEMA', dbo, 'table', SIMS_CLASS, 'column', ADVISER;
EXEC sp_addextendedproperty 'MS_Description', '成立时间', 'SCHEMA', dbo, 'table', SIMS_CLASS, 'column', ESTAB_DATE;
EXEC sp_addextendedproperty 'MS_Description', '学习年数', 'SCHEMA', dbo, 'table', SIMS_CLASS, 'column', YEAR_NUMBER;
EXEC sp_addextendedproperty 'MS_Description', '租户号', 'SCHEMA', dbo, 'table', SIMS_CLASS, 'column', TENANT_ID;
EXEC sp_addextendedproperty 'MS_Description', '乐观锁', 'SCHEMA', dbo, 'table', SIMS_CLASS, 'column', REVISION;
EXEC sp_addextendedproperty 'MS_Description', '创建人', 'SCHEMA', dbo, 'table', SIMS_CLASS, 'column', CREATED_BY;
EXEC sp_addextendedproperty 'MS_Description', '创建时间', 'SCHEMA', dbo, 'table', SIMS_CLASS, 'column', CREATED_TIME;
EXEC sp_addextendedproperty 'MS_Description', '更新人', 'SCHEMA', dbo, 'table', SIMS_CLASS, 'column', UPDATED_BY;
EXEC sp_addextendedproperty 'MS_Description', '更新时间', 'SCHEMA', dbo, 'table', SIMS_CLASS, 'column', UPDATED_TIME;

DROP TABLE IF EXISTS SIMS_STUDENT;
CREATE TABLE SIMS_STUDENT(
    COLLEGE_ID VARCHAR(32) NOT NULL,
    CLASS_ID VARCHAR(32) NOT NULL,
    STUDENT_ID VARCHAR(32) NOT NULL,
    STUDENT_NAME VARCHAR(90),
    ENG_NAME VARCHAR(90),
    ID_CARD_NO VARCHAR(255),
    MOBILE_PHONE VARCHAR(255),
    GENDER VARCHAR(32) DEFAULT  'M',
    MONTHLY_SALARY DECIMAL(24,6),
    BIRTH DATETIME,
    AVATAR INT,
    HEIGHT INT,
    WEIGHT INT,
    NATION VARCHAR(32) DEFAULT  '01',
    POLITICAL VARCHAR(32),
    MARITAL VARCHAR(32) DEFAULT  'UNMARRIED',
    DOMICILE_PLACE_PROVINCE VARCHAR(255),
    DOMICILE_PLACE_CITY VARCHAR(255),
    DOMICILE_PLACE_ADDRESS VARCHAR(255),
    HOBBY VARCHAR(255),
    INTRO VARCHAR(900),
    PRESENT_ADDRESS VARCHAR(255),
    EMAIL VARCHAR(255),
    ENTRY_DATE DATETIME,
    STATUS VARCHAR(32) DEFAULT  'Normal',
    TENANT_ID VARCHAR(32),
    REVISION INT,
    CREATED_BY VARCHAR(32),
    CREATED_TIME DATETIME,
    UPDATED_BY VARCHAR(32),
    UPDATED_TIME DATETIME,
    PRIMARY KEY (STUDENT_ID)
);

EXEC sp_addextendedproperty 'MS_Description', '学生', 'SCHEMA', dbo, 'table', SIMS_STUDENT, null, null;
EXEC sp_addextendedproperty 'MS_Description', '所在学院ID', 'SCHEMA', dbo, 'table', SIMS_STUDENT, 'column', COLLEGE_ID;
EXEC sp_addextendedproperty 'MS_Description', '所在班级ID', 'SCHEMA', dbo, 'table', SIMS_STUDENT, 'column', CLASS_ID;
EXEC sp_addextendedproperty 'MS_Description', '学生ID', 'SCHEMA', dbo, 'table', SIMS_STUDENT, 'column', STUDENT_ID;
EXEC sp_addextendedproperty 'MS_Description', '学生姓名', 'SCHEMA', dbo, 'table', SIMS_STUDENT, 'column', STUDENT_NAME;
EXEC sp_addextendedproperty 'MS_Description', '英文名', 'SCHEMA', dbo, 'table', SIMS_STUDENT, 'column', ENG_NAME;
EXEC sp_addextendedproperty 'MS_Description', '身份证号', 'SCHEMA', dbo, 'table', SIMS_STUDENT, 'column', ID_CARD_NO;
EXEC sp_addextendedproperty 'MS_Description', '手机号', 'SCHEMA', dbo, 'table', SIMS_STUDENT, 'column', MOBILE_PHONE;
EXEC sp_addextendedproperty 'MS_Description', '性别', 'SCHEMA', dbo, 'table', SIMS_STUDENT, 'column', GENDER;
EXEC sp_addextendedproperty 'MS_Description', '月薪', 'SCHEMA', dbo, 'table', SIMS_STUDENT, 'column', MONTHLY_SALARY;
EXEC sp_addextendedproperty 'MS_Description', '出生日期', 'SCHEMA', dbo, 'table', SIMS_STUDENT, 'column', BIRTH;
EXEC sp_addextendedproperty 'MS_Description', '头像', 'SCHEMA', dbo, 'table', SIMS_STUDENT, 'column', AVATAR;
EXEC sp_addextendedproperty 'MS_Description', '身高', 'SCHEMA', dbo, 'table', SIMS_STUDENT, 'column', HEIGHT;
EXEC sp_addextendedproperty 'MS_Description', '体重', 'SCHEMA', dbo, 'table', SIMS_STUDENT, 'column', WEIGHT;
EXEC sp_addextendedproperty 'MS_Description', '名族', 'SCHEMA', dbo, 'table', SIMS_STUDENT, 'column', NATION;
EXEC sp_addextendedproperty 'MS_Description', '政治面貌', 'SCHEMA', dbo, 'table', SIMS_STUDENT, 'column', POLITICAL;
EXEC sp_addextendedproperty 'MS_Description', '婚姻状况', 'SCHEMA', dbo, 'table', SIMS_STUDENT, 'column', MARITAL;
EXEC sp_addextendedproperty 'MS_Description', '籍贯（省）', 'SCHEMA', dbo, 'table', SIMS_STUDENT, 'column', DOMICILE_PLACE_PROVINCE;
EXEC sp_addextendedproperty 'MS_Description', '籍贯（市）', 'SCHEMA', dbo, 'table', SIMS_STUDENT, 'column', DOMICILE_PLACE_CITY;
EXEC sp_addextendedproperty 'MS_Description', '户籍地址', 'SCHEMA', dbo, 'table', SIMS_STUDENT, 'column', DOMICILE_PLACE_ADDRESS;
EXEC sp_addextendedproperty 'MS_Description', '爱好', 'SCHEMA', dbo, 'table', SIMS_STUDENT, 'column', HOBBY;
EXEC sp_addextendedproperty 'MS_Description', '简要介绍', 'SCHEMA', dbo, 'table', SIMS_STUDENT, 'column', INTRO;
EXEC sp_addextendedproperty 'MS_Description', '居住地址', 'SCHEMA', dbo, 'table', SIMS_STUDENT, 'column', PRESENT_ADDRESS;
EXEC sp_addextendedproperty 'MS_Description', '电子邮件', 'SCHEMA', dbo, 'table', SIMS_STUDENT, 'column', EMAIL;
EXEC sp_addextendedproperty 'MS_Description', '入学日期', 'SCHEMA', dbo, 'table', SIMS_STUDENT, 'column', ENTRY_DATE;
EXEC sp_addextendedproperty 'MS_Description', '状态', 'SCHEMA', dbo, 'table', SIMS_STUDENT, 'column', STATUS;
EXEC sp_addextendedproperty 'MS_Description', '租户号', 'SCHEMA', dbo, 'table', SIMS_STUDENT, 'column', TENANT_ID;
EXEC sp_addextendedproperty 'MS_Description', '乐观锁', 'SCHEMA', dbo, 'table', SIMS_STUDENT, 'column', REVISION;
EXEC sp_addextendedproperty 'MS_Description', '创建人', 'SCHEMA', dbo, 'table', SIMS_STUDENT, 'column', CREATED_BY;
EXEC sp_addextendedproperty 'MS_Description', '创建时间', 'SCHEMA', dbo, 'table', SIMS_STUDENT, 'column', CREATED_TIME;
EXEC sp_addextendedproperty 'MS_Description', '更新人', 'SCHEMA', dbo, 'table', SIMS_STUDENT, 'column', UPDATED_BY;
EXEC sp_addextendedproperty 'MS_Description', '更新时间', 'SCHEMA', dbo, 'table', SIMS_STUDENT, 'column', UPDATED_TIME;


CREATE INDEX idx_smis_student_01 ON SIMS_STUDENT(STUDENT_NAME,ENG_NAME);
CREATE INDEX idx_smis_student_cert ON SIMS_STUDENT(ID_CARD_NO);

DROP TABLE IF EXISTS SIMS_TEACHER;
CREATE TABLE SIMS_TEACHER(
    COLLEGE_ID VARCHAR(32) NOT NULL,
    TEACHER_ID VARCHAR(32) NOT NULL,
    TEACHER_NAME VARCHAR(90),
    GENDER VARCHAR(32) DEFAULT  'M',
    BIRTH DATETIME,
    GRADUATE_INSTITUTION VARCHAR(90),
    PRACTICE_YEARS INT,
    POLITICAL VARCHAR(32),
    MARITAL VARCHAR(32) DEFAULT  'UNMARRIED',
    AVATAR VARCHAR(255),
    INTRO VARCHAR(900),
    TENANT_ID VARCHAR(32),
    REVISION INT,
    CREATED_BY VARCHAR(32),
    CREATED_TIME DATETIME,
    UPDATED_BY VARCHAR(32),
    UPDATED_TIME DATETIME,
    PRIMARY KEY (TEACHER_ID)
);

EXEC sp_addextendedproperty 'MS_Description', '教师', 'SCHEMA', dbo, 'table', SIMS_TEACHER, null, null;
EXEC sp_addextendedproperty 'MS_Description', '所在学院ID', 'SCHEMA', dbo, 'table', SIMS_TEACHER, 'column', COLLEGE_ID;
EXEC sp_addextendedproperty 'MS_Description', '教师ID', 'SCHEMA', dbo, 'table', SIMS_TEACHER, 'column', TEACHER_ID;
EXEC sp_addextendedproperty 'MS_Description', '姓名', 'SCHEMA', dbo, 'table', SIMS_TEACHER, 'column', TEACHER_NAME;
EXEC sp_addextendedproperty 'MS_Description', '性别', 'SCHEMA', dbo, 'table', SIMS_TEACHER, 'column', GENDER;
EXEC sp_addextendedproperty 'MS_Description', '出生日期', 'SCHEMA', dbo, 'table', SIMS_TEACHER, 'column', BIRTH;
EXEC sp_addextendedproperty 'MS_Description', '毕业院校', 'SCHEMA', dbo, 'table', SIMS_TEACHER, 'column', GRADUATE_INSTITUTION;
EXEC sp_addextendedproperty 'MS_Description', '从业年限', 'SCHEMA', dbo, 'table', SIMS_TEACHER, 'column', PRACTICE_YEARS;
EXEC sp_addextendedproperty 'MS_Description', '政治面貌', 'SCHEMA', dbo, 'table', SIMS_TEACHER, 'column', POLITICAL;
EXEC sp_addextendedproperty 'MS_Description', '婚姻状况', 'SCHEMA', dbo, 'table', SIMS_TEACHER, 'column', MARITAL;
EXEC sp_addextendedproperty 'MS_Description', '头像', 'SCHEMA', dbo, 'table', SIMS_TEACHER, 'column', AVATAR;
EXEC sp_addextendedproperty 'MS_Description', '介绍', 'SCHEMA', dbo, 'table', SIMS_TEACHER, 'column', INTRO;
EXEC sp_addextendedproperty 'MS_Description', '租户号', 'SCHEMA', dbo, 'table', SIMS_TEACHER, 'column', TENANT_ID;
EXEC sp_addextendedproperty 'MS_Description', '乐观锁', 'SCHEMA', dbo, 'table', SIMS_TEACHER, 'column', REVISION;
EXEC sp_addextendedproperty 'MS_Description', '创建人', 'SCHEMA', dbo, 'table', SIMS_TEACHER, 'column', CREATED_BY;
EXEC sp_addextendedproperty 'MS_Description', '创建时间', 'SCHEMA', dbo, 'table', SIMS_TEACHER, 'column', CREATED_TIME;
EXEC sp_addextendedproperty 'MS_Description', '更新人', 'SCHEMA', dbo, 'table', SIMS_TEACHER, 'column', UPDATED_BY;
EXEC sp_addextendedproperty 'MS_Description', '更新时间', 'SCHEMA', dbo, 'table', SIMS_TEACHER, 'column', UPDATED_TIME;

