package pojo;
import java.util.Date;
import java.time.LocalDateTime;

public class UserHello {
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

	public String toString(){
		return "UserHello{" +
				"id=" + id + "," +
				"name=" + name + "," +
				"age=" + age + "," +
				"email=" + email + "," +
				"address=" + address + "," +
				"zipcode=" + zipcode + "," +
				"weight=" + weight + "," +
				"haveHouse=" + haveHouse + "," +
				"date=" + date + "," +
				"time=" + time + 
				"}";
	}
}