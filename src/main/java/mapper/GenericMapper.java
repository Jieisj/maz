package mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface GenericMapper<T, P>{
    Integer insert(@Param("insert") T t);
    Integer insertOrUpdate(@Param("inOrUp")T t);
    Integer insertBatch(@Param("insert") List<T> t);
    Integer insertOrUpdateBatch(@Param("inOrUp") List<T> list);
    List<T> selectList(@Param("query") P p);
    Integer selectCount(@Param("query") P p);
}
