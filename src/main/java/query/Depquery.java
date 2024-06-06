package query;

public class Depquery {
	private Integer depId;
	private String depName;
	private Integer depPeopNum;

	public void setDepID(Integer depId){
	 	this.depId = depId; 
	}
	public void setDepName(String depName){
	 	this.depName = depName; 
	}
	public void setDepPeopNum(Integer depPeopNum){
	 	this.depPeopNum = depPeopNum; 
	}
	public Integer getDepID(){
	 	return this.depId; 
	}
	public String getDepName(){
	 	return this.depName; 
	}
	public Integer getDepPeopNum(){
	 	return this.depPeopNum; 
	}
	public String toString(){
		return "DepHello{" +
				"depId=" + depId + "," +
				"depName=" + depName + "," +
				"depPeopNum=" + depPeopNum + 
				"}";
	}
}