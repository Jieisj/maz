package query;
import java.util.Date;
import java.time.LocalDateTime;

public class UserQuery {
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