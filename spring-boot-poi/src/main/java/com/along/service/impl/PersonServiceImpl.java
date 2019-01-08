package com.along.service.impl;

import com.along.common.export.CellSetter;
import com.along.common.export.CreateExcel;
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
import java.util.ArrayList;
import java.util.List;

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
    public String exportPersons() throws IOException {
        String fileName = "export" + File.separator + "用户信息_"
                + dayFormat.format(System.currentTimeMillis()) + ".xlsx";
        String realFile = "D:\\githubProjects" + File.separator + fileName;
        fileOperate(realFile);
        //导出excel
        createExcelPersons(realFile);

        return fileName;

    }

    /**
     * 导出方法
     * @param realFile
     * @throws IOException
     */
    private void createExcelPersons(String realFile) throws IOException {
        CreateExcel<Person> createExcel = new CreateExcel<>();
        logger.info("开始导出[全量用户信息]");
        List<Person> data = personMapper.selectByExample(new PersonExample());
        List<CellSetter> cellSetter = new ArrayList<>();

        cellSetter.add(new CellSetter("编号", "id", "Long", "0", 6000));
        cellSetter.add(new CellSetter("姓名", "name", "String", "G/通用格式", 6000));
        cellSetter.add(new CellSetter("年龄", "age", "Long", "0", 6000));
        cellSetter.add(new CellSetter("地址", "address", "String", "G/通用格式", 6000));

        OutputStream outputStream = new FileOutputStream(realFile);
        SXSSFWorkbook workbook = createExcel.create(cellSetter, data, "全量用户信息");
        workbook.write(outputStream);
        logger.info("[全量用户信息]导出成功");
        outputStream.flush();
        outputStream.close();
    }

    private void fileOperate(String realFile) {
        File file = new File(realFile);
        if (file.exists()) {
            logger.info("文件已经存在, 执行文件删除操作");
            file.delete();
            logger.info("删除已经存在的文件, 文件名称: {}", realFile);
        }
    }


}
