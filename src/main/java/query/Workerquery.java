package query;

public class Workerquery {
	private String name;
	private Double weight;
	private Integer department;
	private String gender;

	public void setName(String name){
	 	this.name = name; 
	}
	public void setWeight(Double weight){
	 	this.weight = weight; 
	}
	public void setDepartment(Integer department){
	 	this.department = department; 
	}
	public void setGender(String gender){
	 	this.gender = gender; 
	}
	public String getName(){
	 	return this.name; 
	}
	public Double getWeight(){
	 	return this.weight; 
	}
	public Integer getDepartment(){
	 	return this.department; 
	}
	public String getGender(){
	 	return this.gender; 
	}
	public String toString(){
		return "WorkerHello{" +
				"name=" + name + "," +
				"weight=" + weight + "," +
				"department=" + department + "," +
				"gender=" + gender + 
				"}";
	}
}