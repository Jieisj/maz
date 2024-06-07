package mapper;
import org.apache.ibatis.annotations.Param;

public interface WorkerMapper<T,P> extends GenericMapper<T,P>{
	T selectByName(@Param("name") String name);
	Integer updateByName(@Param("name") String name, @Param("WorkerHello") T t);
	Integer deleteByName(@Param("name") String name);
}