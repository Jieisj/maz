package mapper;
import org.apache.ibatis.annotations.Param;

public interface UserMapper<T,P> extends GenericMapper.txt<T,P>{
	T selectById(@Param("id") Integer id);
	Integer updateById(@Param("id") Integer id, @Param("User") T t);
	Integer deleteById(@Param("id") Integer id);
	T selectByEmail(@Param("email") String email);
	Integer updateByEmail(@Param("email") String email, @Param("User") T t);
	Integer deleteByEmail(@Param("email") String email);
	T selectByName(@Param("name") String name);
	Integer updateByName(@Param("name") String name, @Param("User") T t);
	Integer deleteByName(@Param("name") String name);
	T selectByAddress(@Param("address") String address);
	Integer updateByAddress(@Param("address") String address, @Param("User") T t);
	Integer deleteByAddress(@Param("address") String address);
	T selectByNameAndAgeAndAddress(@Param("name") String name, @Param("age") Integer age, @Param("address") String address);
	Integer updateByNameAndAgeAndAddress(@Param("name") String name, @Param("age") Integer age, @Param("address") String address, @Param("User") T t);
	Integer deleteByNameAndAgeAndAddress(@Param("name") String name, @Param("age") Integer age, @Param("address") String address);
}