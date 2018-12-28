package com.along.mapper;

import com.along.entity.Person;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author along
 * @Date 2018/12/27 14:20
 */
@Mapper
public interface PersonMapper {

    /**
     * 方式1：使用注解编写sql
     *
     * @return
     */
    @Select("select * from person")
    List<Person> list();

    /**
     * 方式2：使用注解指定某个工具类的方法来动态编写SQL.
     *
     * @param name
     * @return
     */
    @SelectProvider(type = PersonSqlProvider.class, method = "listByName")
    List<Person> listByName(String name);

    /**
     * 延伸：上述两种方式都可以附加@Results注解来指定结果集的映射关系.
     * <p>
     * PS：如果符合下划线转驼峰的匹配项可以直接省略不写。
     */
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "age", column = "age"),
            @Result(property = "address", column = "address"),
    })
    @Select("select * from person")
    List<Person> listSample();

    /**
     * 延伸：无论什么方式,如果涉及多个参数,则必须加上@Param注解,否则无法使用EL表达式获取参数。
     */
    @Select("select * from person where name like CONCAT('%',#{name},'%') and address like CONCAT('%',#{address},'%')")
    List<Person> get(@Param("name") String name, @Param("address") String address);

    @SelectProvider(type = PersonSqlProvider.class, method = "getByAddress")
    List<Person> getByAddress(String password);

    @Options(useGeneratedKeys = true, keyProperty = "id") // 主键自增,默认主键名为id
    @Insert("insert into person(name,age,address) values(#{name},#{age},#{address})")
    Integer save(Person person);

    @Options(useGeneratedKeys = true, keyProperty = "id") // 主键自增,默认主键名为id
    @InsertProvider(type = PersonSqlProvider.class, method = "saveBatch")
    Integer saveBatch(@Param("list") List<Person> personList);


}
