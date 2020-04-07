package com.along.common;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

/**
 * @Description: mybatis-plus自动代码生成器
 * @Author along
 * @Date 2020/4/5 23:44
 */
public class CodeGenerator {
    // 自动生成代码
    public static void main(String[] args) {

        // 模块名
//        String moduleName = "edu";

        // 1、代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 规则的配置
        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");// 获取当前项目的路径
        gc.setOutputDir(projectPath + "/spring-boot-mybatis-plus/src/main/java");
        gc.setAuthor("Along");
        gc.setOpen(false);
        gc.setFileOverride(false); // 不覆盖之前生成的文件！
        gc.setServiceName("%sService");
        gc.setIdType(IdType.ID_WORKER_STR); // 主键策略
        gc.setDateType(DateType.ONLY_DATE); // 日期类型
        gc.setSwagger2(true); // 自动开启Swagger配置！
        mpg.setGlobalConfig(gc);


        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://localhost:3306/test?useSSL=false&useUnicode=true&characterEncoding=utf8&autoReconnect=true&rewriteBatchedStatements=TRUE&serverTimezone=UTC&allowMultiQueries=true");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("root");
        dsc.setDbType(DbType.MYSQL);
        mpg.setDataSource(dsc);


        // 包配置
        PackageConfig pc = new PackageConfig();
//        pc.setModuleName(moduleName);
        pc.setParent("com.along");
        pc.setController("controller");
        pc.setService("service");
        pc.setEntity("entity");
        pc.setMapper("mapper");
        mpg.setPackageInfo(pc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        // 要生成哪一个表对应的类
//        strategy.setInclude(moduleName+"_\\w*"); // ！！！所有edu 开头的表都自动生成！
//        strategy.setInclude("\\w*"); // 生成所有表
        strategy.setInclude("person"); // 指定生成person表
        strategy.setNaming(NamingStrategy.underline_to_camel); // 数据库表生成到实体类的策略
//        strategy.setTablePrefix(pc.getModuleName()+"_"); // edu_ 这个前缀不生成在类中

        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true); // 自动生成lombok注解

//        strategy.setLogicDeleteFieldName("is_deleted"); // 逻辑删除！
//        strategy.setEntityBooleanColumnRemoveIsPrefix(true); // 去掉布尔值的 is_前缀！

        // 自动填充
//        TableFill gmt_create = new TableFill("gmt_create", FieldFill.INSERT);
//        TableFill gmt_modified = new TableFill("gmt_modified", FieldFill.INSERT_UPDATE);
//        ArrayList<TableFill> tableFills = new ArrayList<>();
//        tableFills.add(gmt_create);
//        tableFills.add(gmt_modified);
//        strategy.setTableFillList(tableFills);

        // 乐观锁
//        strategy.setVersionFieldName("version");
//        strategy.setRestControllerStyle(true); // restful api
//        strategy.setControllerMappingHyphenStyle(true); //  /user/hello_name 使用_连接驼峰！

        mpg.setStrategy(strategy);

        // 2、执行代码生成器
        mpg.execute();

    }
}
