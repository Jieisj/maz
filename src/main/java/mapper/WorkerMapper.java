package mapper;
import org.apache.ibatis.annotations.Param;

public interface WorkerMapper<T,P> extends GenericMapper.txt<T,P>{
	T selectByName(@Param("name") String name);
	Integer updateByName(@Param("name") String name, @Param("Worker") T t);
	Integer deleteByName(@Param("name") String name);
}