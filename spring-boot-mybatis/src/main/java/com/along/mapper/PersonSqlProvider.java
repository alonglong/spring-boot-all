package com.along.mapper;

import com.along.entity.Person;
import org.apache.ibatis.jdbc.SQL;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

/**
 * @Description: 主要用途：根据复杂的业务需求来动态生成SQL.
 * <p>
 * 目标：使用Java工具类来替代传统的XML文件.(例如：PersonSqlProvider.java <-- PersonMapper.xml)
 * @Author along
 * @Date 2018/12/27 14:34
 */
public class PersonSqlProvider {
    /**
     * 方式1：在工具类的方法里,可以自己手工编写SQL。
     */
    public String listByName(String name) {
        return "select * from person where name = #{name}";
    }

    /**
     * 方式2：也可以根据官方提供的API来编写动态SQL。
     */
    public String getByAddress(String address) {
        return new SQL() {{
            SELECT("*");
            FROM("person");
            if (address != null) {
                WHERE("address = #{address}");
            } else {
                WHERE("1=2");
            }
        }}.toString();
    }


    public String save(Person person) {
        return new SQL() {{
            INSERT_INTO("person");
            if (person.getName() != null) {
                VALUES("name", "#{name}");
            }
            if (person.getAge() != 0) {
                VALUES("age", "#{age}");
            }
            if (person.getAddress() != null) {
                VALUES("address", "#{address");
            }
        }}.toString();
    }

    /**
     * 批量插入
     *
     * @param map
     * @return
     */
    public String saveBatch(Map map) {
        List<Person> list = (List<Person>) map.get("list");
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO person (name,age,address) VALUES ");
        MessageFormat mf = new MessageFormat(
                "(#'{'list[{0}].name}, #'{'list[{0}].age}, #'{'list[{0}].address})"
        );
        for (int i = 0; i < list.size(); i++) {
            sb.append(mf.format(new Object[]{i}));
            if (i < list.size() - 1) {
                sb.append(",");
            }
        }
        return sb.toString();

    }

}
