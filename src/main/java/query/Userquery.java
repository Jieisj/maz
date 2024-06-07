package query;
import java.util.Date;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class Userquery extends PaginationQuery{
	private Integer id;
	private String name;
	private Integer age;
	private String email;
	private String address;
	private String zipcode;
	private Double weight;
	private String haveHouse;
	private Date date;
	private LocalDateTime time;
}