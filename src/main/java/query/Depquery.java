package query;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class Depquery extends PaginationQuery{
	private Integer depId;
	private String depName;
	private Integer depPeopNum;
}