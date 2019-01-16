package com.along.service.impl;

import com.along.common.export.*;
import com.along.dao.PersonMapper;
import com.along.entity.Person;
import com.along.entity.PersonExample;
import com.along.service.PersonService;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @Description: service实现
 * @Author along
 * @Date 2018/12/28 17:44
 */
@Service(value = "personService")
@Transactional
public class PersonServiceImpl implements PersonService {

    private final Logger logger = LoggerFactory.getLogger(PersonServiceImpl.class);

    private final FastDateFormat dateFormat = FastDateFormat.getInstance("yyyyMMddHHmmss");
    private final FastDateFormat dayFormat = FastDateFormat.getInstance("yyyyMMdd^yyyyMMddHHmmss^");
    private final FastDateFormat nodayFormat = FastDateFormat.getInstance("^yyyyMMddHHmmss^");
    private final FastDateFormat format = FastDateFormat.getInstance("yyyyMMdd");

    private PersonMapper personMapper;

    @Autowired
    public PersonServiceImpl(@Qualifier("personMapper") PersonMapper personMapper) {
        this.personMapper = personMapper;
    }

    @Override
    public String exportPersons() throws Exception {
        // 设置导出的文件名
        String fileName = "用户信息_" + dayFormat.format(System.currentTimeMillis()) + ".xlsx";
        // 设置导出文件路径
        String realFile = "spring-boot-poi\\src\\main\\resources\\export" + File.separator + fileName;
        // 验证
        fileOperate(realFile);

        // 导出简单excel
        realFile = "spring-boot-poi\\src\\main\\resources\\export" + File.separator + "简单Excel_" + fileName;
        createExcelPersons(realFile);

        //导出复杂excel(二级)
        realFile = "spring-boot-poi\\src\\main\\resources\\export" + File.separator + "两层表头Excel_" + fileName;
        createTwoMapExcel(realFile);

        //导出复杂excel(三级)
        realFile = "spring-boot-poi\\src\\main\\resources\\export" + File.separator + "三级表头Excel_" + fileName;
        createThreeMapExcel(realFile);

        //导出复杂excel(任意)
        realFile = "spring-boot-poi\\src\\main\\resources\\export" + File.separator + "任意表头Excel_" + fileName;
        createAnyMapExcel(realFile);

        return fileName;
    }

    /**
     * 简单表格导出方法
     *
     * @param realFile 导出文件的路径
     * @throws IOException
     */
    private void createExcelPersons(String realFile) throws IOException {
        CreateExcel<Person> createExcel = new CreateExcel<>();
        logger.info("开始导出[全量用户信息]");
        // 获取数据
        List<Person> data = personMapper.selectByExample(new PersonExample());
        List<CellSetter> cellSetter = new ArrayList<>();

        cellSetter.add(new CellSetter("编号", "id", "Long", "0", 6000));
        cellSetter.add(new CellSetter("姓名", "name", "String", "G/通用格式", 6000));
        cellSetter.add(new CellSetter("年龄", "age", "Long", "0", 6000));
        cellSetter.add(new CellSetter("地址", "address", "String", "G/通用格式", 6000,
                // 在这里做数据处理，这里只做演示，java8 lambda写法
                o -> "闵行".equals(String.valueOf(o)) ? "上海" : "北京")
        );

        OutputStream outputStream = new FileOutputStream(realFile);
        SXSSFWorkbook workbook = createExcel.create(cellSetter, data, "全量用户信息");
        workbook.write(outputStream);
        logger.info("[全量用户信息]导出成功");
        outputStream.flush();
        outputStream.close();
    }

    /**
     * 判断路径下文件是否已经存在
     *
     * @param realFile
     */
    private void fileOperate(String realFile) {
        File file = new File(realFile);
        if (file.exists()) {
            logger.info("文件已经存在, 执行文件删除操作");
            file.delete();
            logger.info("删除已经存在的文件, 文件名称: {}", realFile);
        }
    }

    /**
     * 复杂表格导出方法
     *
     * @param realFile 导出文件的路径
     */
    private void createTwoMapExcel(String realFile) throws Exception {
        logger.info("开始导出[全量用户信息]");

        //参数设置
        List<PropSetter> props = new ArrayList<>();
        props.add(new PropSetter("编号", null, "id", "Long", "0", 4000));
        props.add(new PropSetter("姓名", null, "name", "String", "G/通用格式", 4000));
        props.add(new PropSetter("年龄", null, "age", "Long", "0", 4000));
        props.add(new PropSetter("地址", null, "address", "String", "G/通用格式", 4000));
        //下面的字段数据表里没有，只做各种情况的演示
        props.add(new PropSetter("生日", null, "birth", "date", "yyyy-MM-dd", 4000));
        props.add(new PropSetter("创建日期", null, "created", "date", "yyyy-MM-dd HH:mm:ss", 4000));
        props.add(new PropSetter("更新日期", null, "updated", "date", "yyyy-MM-dd HH:mm:ss", 4000));
        //下面是两层的演示
        props.add(new PropSetter("编号+姓名", "编号", "id", "Long", "0", 4000));
        props.add(new PropSetter("编号+姓名", "姓名", "name", "String", "G/通用格式", 4000));

        props.add(new PropSetter("年龄+地址", "年龄", "age", "Long", "0", 4000));
        props.add(new PropSetter("年龄+地址", "地址", "address", "String", "G/通用格式", 4000,
                // 在这里做数据处理，这里只做演示，正常写法
                new ExcelColRnderer() {
                    @Override
                    public String view(Object o) {
                        return "闵行".equals(String.valueOf(o)) ? "上海" : "北京";
                    }
                })
        );

        props.add(new PropSetter("生日+创建日期+更新日期", "生日", "birth", "date", "yyyy-MM-dd", 4000));
        props.add(new PropSetter("生日+创建日期+更新日期", "创建日期", "created", "date", "yyyy-MM-dd", 4000));
        props.add(new PropSetter("生日+创建日期+更新日期", "更新日期", "updated", "date", "yyyy-MM-dd", 4000));

        //设置要合并单元格坐标值,可以用for循环写，这里每条举例是为了看的清晰
        List<FourPoint> fps = new ArrayList<>();
        fps.add(new FourPoint(0, 1, 0, 0));
        fps.add(new FourPoint(0, 1, 1, 1));
        fps.add(new FourPoint(0, 1, 2, 2));
        fps.add(new FourPoint(0, 1, 3, 3));
        fps.add(new FourPoint(0, 1, 4, 4));
        fps.add(new FourPoint(0, 1, 5, 5));
        fps.add(new FourPoint(0, 1, 6, 6));

        fps.add(new FourPoint(0, 0, 7, 8));
        fps.add(new FourPoint(0, 0, 9, 10));
        fps.add(new FourPoint(0, 0, 11, 13));

        // 获取数据
        List<Person> data = personMapper.selectByExample(new PersonExample());
        // 将lists转为map列表
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (Person person : data) {
            Map<String, Object> stringObjectMap = this.objectToMap(person);
            mapList.add(stringObjectMap);
        }

        OutputStream outputStream = new FileOutputStream(realFile);
        SXSSFWorkbook workbook = CreateTwoMapExcel.create(mapList, props, fps, "全量用户信息");
        workbook.write(outputStream);
        logger.info("用户全量数据导出成功");
        outputStream.flush();
        outputStream.close();
    }

    /**
     * bean对象转map的方法
     * 利用反射获取类里面的值和名称
     *
     * @param obj
     * @return
     * @throws IllegalAccessException
     */
    private Map<String, Object> objectToMap(Object obj) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<>();
        Class<?> clazz = obj.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object value = field.get(obj);
            map.put(fieldName, value);
        }
        return map;
    }

    /**
     * 复杂表格导出方法（三层）
     *
     * @param realFile 导出文件的路径
     */
    private void createThreeMapExcel(String realFile) throws Exception {
        logger.info("开始导出[全量用户信息]");

        //参数设置
        List<PropSetter> props = new ArrayList<>();
        props.add(new PropSetter("编号", null, null, "id", "Long", "0", 4000));
        props.add(new PropSetter("姓名", null, null, "name", "String", "G/通用格式", 4000));
        props.add(new PropSetter("年龄", null, null, "age", "Long", "0", 4000));
        props.add(new PropSetter("地址", null, null, "address", "String", "G/通用格式", 4000));
        props.add(new PropSetter("生日", null, null, "birth", "String", "G/通用格式", 4000));
        props.add(new PropSetter("创建日期", null, null, "created", "date", "yyyy-MM-dd HH:mm:ss", 4000));
        props.add(new PropSetter("更新日期", null, null, "updated", "date", "yyyy-MM-dd HH:mm:ss", 4000));
        props.add(new PropSetter("编号+姓名", "编号", null, "id", "Long", "0", 4000));
        props.add(new PropSetter("编号+姓名", "姓名", null, "name", "String", "G/通用格式", 4000));
        props.add(new PropSetter("年龄+地址", "年龄", null, "age", "Long", "0", 4000));
        props.add(new PropSetter("年龄+地址", "地址", null, "address", "Long", "0", 4000));
        props.add(new PropSetter("生日+创建日期+更新日期", "生日", null, "birth", "date", "yyyy-MM-dd", 4000));
        props.add(new PropSetter("生日+创建日期+更新日期", "创建日期", null, "created", "date", "yyyy-MM-dd HH:mm:ss", 4000));
        props.add(new PropSetter("生日+创建日期+更新日期", "更新日期", null, "updated", "date", "yyyy-MM-dd HH:mm:ss", 4000));
        props.add(new PropSetter("编号+姓名+年龄+地址", "编号+姓名", "编号", "id", "Long", "0", 4000));
        props.add(new PropSetter("编号+姓名+年龄+地址", "编号+姓名", "姓名", "id", "Long", "0", 4000));
        props.add(new PropSetter("编号+姓名+年龄+地址", "年龄+地址", "年龄", "age", "Long", "0", 4000));
        props.add(new PropSetter("编号+姓名+年龄+地址", "年龄+地址", "地址", "address", "address", "G/通用格式", 4000));


        //设置要合并单元格坐标值,可以用for循环写，这里每条举例是为了看的清晰
        List<FourPoint> fps = new ArrayList<>();
        fps.add(new FourPoint(0, 2, 0, 0));
        fps.add(new FourPoint(0, 2, 1, 1));
        fps.add(new FourPoint(0, 2, 2, 2));
        fps.add(new FourPoint(0, 2, 3, 3));
        fps.add(new FourPoint(0, 2, 4, 4));
        fps.add(new FourPoint(0, 2, 5, 5));
        fps.add(new FourPoint(0, 2, 6, 6));

        fps.add(new FourPoint(0, 0, 7, 8));
        fps.add(new FourPoint(0, 0, 9, 10));
        fps.add(new FourPoint(0, 0, 11, 13));
        fps.add(new FourPoint(0, 0, 14, 17));

        fps.add(new FourPoint(1, 2, 7, 7));
        fps.add(new FourPoint(1, 2, 8, 8));
        fps.add(new FourPoint(1, 2, 9, 9));
        fps.add(new FourPoint(1, 2, 10, 10));
        fps.add(new FourPoint(1, 2, 11, 11));
        fps.add(new FourPoint(1, 2, 12, 12));
        fps.add(new FourPoint(1, 2, 13, 13));

        fps.add(new FourPoint(1, 1, 14, 15));
        fps.add(new FourPoint(1, 1, 16, 17));


        // 获取数据
        List<Person> data = personMapper.selectByExample(new PersonExample());
        // 将lists转为map列表
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (Person person : data) {
            Map<String, Object> stringObjectMap = this.objectToMap(person);
            mapList.add(stringObjectMap);
        }

        OutputStream outputStream = new FileOutputStream(realFile);
        SXSSFWorkbook workbook = CreateThreeMapExcel.create(mapList, props, fps, "全量用户信息");
        workbook.write(outputStream);
        logger.info("用户全量数据导出成功");
        outputStream.flush();
        outputStream.close();
    }

    /**
     * 复杂表格导出方法,支持任意层数
     *
     * @param realFile 导出文件的路径
     */
    private void createAnyMapExcel(String realFile) throws Exception {
        logger.info("开始导出[全量用户信息]");

        //参数设置
        List<MoreSetter> props = new ArrayList<>();
        // props.add(new MoreSetter(new ArrayList<String>(){{add("编号");add(null);add(null);}}, "id", "Long", "0", 4000));
        // 由于不会对MoreSetter的rowList做add和remove操作，所以用Arrays.asList创建List
        props.add(new MoreSetter(Arrays.asList("编号", null, null), "id", "Long", "0", 4000));
        props.add(new MoreSetter(Arrays.asList("姓名",null,null), "name","String", "G/通用格式", 4000));
        props.add(new MoreSetter(Arrays.asList("年龄",null,null), "age", "Long", "0", 4000));
        props.add(new MoreSetter(Arrays.asList("地址",null,null), "address", "String", "G/通用格式", 4000));
        props.add(new MoreSetter(Arrays.asList("生日",null,null), "birth", "String", "G/通用格式", 4000));
        props.add(new MoreSetter(Arrays.asList("创建日期",null,null), "created", "date", "yyyy-MM-dd HH:mm:ss", 4000));
        props.add(new MoreSetter(Arrays.asList("更新日期",null,null), "updated", "date", "yyyy-MM-dd HH:mm:ss", 4000));
        props.add(new MoreSetter(Arrays.asList("编号+姓名","编号",null), "id", "Long", "0", 4000));
        props.add(new MoreSetter(Arrays.asList("编号+姓名","姓名",null), "name", "String", "G/通用格式", 4000));
        props.add(new MoreSetter(Arrays.asList("年龄+地址","年龄",null), "age", "Long", "0", 4000));
        props.add(new MoreSetter(Arrays.asList("年龄+地址","地址",null), "address", "Long", "0", 4000));
        props.add(new MoreSetter(Arrays.asList("生日+创建日期+更新日期","生日",null), "birth", "date", "yyyy-MM-dd", 4000));
        props.add(new MoreSetter(Arrays.asList("生日+创建日期+更新日期","创建日期",null), "created", "date", "yyyy-MM-dd HH:mm:ss", 4000));
        props.add(new MoreSetter(Arrays.asList("生日+创建日期+更新日期","更新日期",null), "updated", "date", "yyyy-MM-dd HH:mm:ss", 4000));
        props.add(new MoreSetter(Arrays.asList("编号+姓名+年龄+地址","编号+姓名","编号"), "id", "Long", "0", 4000));
        props.add(new MoreSetter(Arrays.asList("编号+姓名+年龄+地址","编号+姓名","姓名"), "id", "Long", "0", 4000));
        props.add(new MoreSetter(Arrays.asList("编号+姓名+年龄+地址","年龄+地址","年龄"), "age", "Long", "0", 4000));
        props.add(new MoreSetter(Arrays.asList("编号+姓名+年龄+地址","年龄+地址","地址"), "address", "address", "G/通用格式", 4000));


        //设置要合并单元格坐标值,可以用for循环写，这里每条举例是为了看的清晰
        List<FourPoint> fps = new ArrayList<>();
        fps.add(new FourPoint(0, 2, 0, 0));
        fps.add(new FourPoint(0, 2, 1, 1));
        fps.add(new FourPoint(0, 2, 2, 2));
        fps.add(new FourPoint(0, 2, 3, 3));
        fps.add(new FourPoint(0, 2, 4, 4));
        fps.add(new FourPoint(0, 2, 5, 5));
        fps.add(new FourPoint(0, 2, 6, 6));

        fps.add(new FourPoint(0, 0, 7, 8));
        fps.add(new FourPoint(0, 0, 9, 10));
        fps.add(new FourPoint(0, 0, 11, 13));
        fps.add(new FourPoint(0, 0, 14, 17));

        fps.add(new FourPoint(1, 2, 7, 7));
        fps.add(new FourPoint(1, 2, 8, 8));
        fps.add(new FourPoint(1, 2, 9, 9));
        fps.add(new FourPoint(1, 2, 10, 10));
        fps.add(new FourPoint(1, 2, 11, 11));
        fps.add(new FourPoint(1, 2, 12, 12));
        fps.add(new FourPoint(1, 2, 13, 13));

        fps.add(new FourPoint(1, 1, 14, 15));
        fps.add(new FourPoint(1, 1, 16, 17));


        // 获取数据
        List<Person> data = personMapper.selectByExample(new PersonExample());
        // 将lists转为map列表
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (Person person : data) {
            Map<String, Object> stringObjectMap = this.objectToMap(person);
            mapList.add(stringObjectMap);
        }

        OutputStream outputStream = new FileOutputStream(realFile);
        SXSSFWorkbook workbook = CreateMoreMapExcel.create(mapList, props, fps, "全量用户信息",3);
        workbook.write(outputStream);
        logger.info("用户全量数据导出成功");
        outputStream.flush();
        outputStream.close();
    }


}
