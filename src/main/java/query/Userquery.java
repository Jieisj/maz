package query;
import java.util.Date;
import java.time.LocalDateTime;

public class Userquery {
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

	public void setId(Integer id){
	 	this.id = id; 
	}
	public void setName(String name){
	 	this.name = name; 
	}
	public void setAge(Integer age){
	 	this.age = age; 
	}
	public void setEmail(String email){
	 	this.email = email; 
	}
	public void setAddress(String address){
	 	this.address = address; 
	}
	public void setZipcode(String zipcode){
	 	this.zipcode = zipcode; 
	}
	public void setWeight(Double weight){
	 	this.weight = weight; 
	}
	public void setHaveHouse(String haveHouse){
	 	this.haveHouse = haveHouse; 
	}
	public void setDate(Date date){
	 	this.date = date; 
	}
	public void setTime(LocalDateTime time){
	 	this.time = time; 
	}
	public Integer getId(){
	 	return this.id; 
	}
	public String getName(){
	 	return this.name; 
	}
	public Integer getAge(){
	 	return this.age; 
	}
	public String getEmail(){
	 	return this.email; 
	}
	public String getAddress(){
	 	return this.address; 
	}
	public String getZipcode(){
	 	return this.zipcode; 
	}
	public Double getWeight(){
	 	return this.weight; 
	}
	public String getHaveHouse(){
	 	return this.haveHouse; 
	}
	public Date getDate(){
	 	return this.date; 
	}
	public LocalDateTime getTime(){
	 	return this.time; 
	}
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