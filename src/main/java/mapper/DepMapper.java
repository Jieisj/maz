package mapper;
import org.apache.ibatis.annotations.Param;

public interface DepMapper<T,P> extends GenericMapper<T,P>{
	T selectByDepID(@Param("depId") Integer depId);
	Integer updateByDepID(@Param("depId") Integer depId, @Param("DepHello") T t);
	Integer deleteByDepID(@Param("depId") Integer depId);
	T selectByDepName(@Param("depName") String depName);
	Integer updateByDepName(@Param("depName") String depName, @Param("DepHello") T t);
	Integer deleteByDepName(@Param("depName") String depName);
	T selectByDepPeopNum(@Param("depPeopNum") Integer depPeopNum);
	Integer updateByDepPeopNum(@Param("depPeopNum") Integer depPeopNum, @Param("DepHello") T t);
	Integer deleteByDepPeopNum(@Param("depPeopNum") Integer depPeopNum);
}