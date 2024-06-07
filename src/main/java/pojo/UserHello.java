package pojo;
import java.util.Date;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class UserHello{
	/** 主键ID */
	private Integer id;
	/** 姓名 */
	private String name;
	/** 年龄 */
	private Integer age;
	/** 邮箱 */
	private String email;
	/**  */
	private String address;
	/**  */
	private String zipcode;
	/**  */
	private Double weight;
	/**  */
	private String haveHouse;
	/**  */
	private Date date;
	/**  */
	private LocalDateTime time;
}