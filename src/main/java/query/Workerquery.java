package query;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class Workerquery extends PaginationQuery{
	private String name;
	private Double weight;
	private Integer department;
	private String gender;
}