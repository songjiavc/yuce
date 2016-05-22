package yuceHandler.dantuo;

public class Fast3Count implements  Comparable<Fast3Count>{
	
	public String issueId;
	
	public Integer number;
	
	public Integer count20;
	 
	public Integer count14;
	
	public Integer count7;

	public String getIssueId() {
		return issueId;
	}
	
	public void setIssueId(String issueId) {
		this.issueId = issueId;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Integer getCount20() {
		return count20;
	}

	public void setCount20(Integer count20) {
		this.count20 = count20;
	}

	public Integer getCount14() {
		return count14;
	}

	public void setCount14(Integer count14) {
		this.count14 = count14;
	}

	public Integer getCount7() {
		return count7;
	}

	public void setCount7(Integer count7) {
		this.count7 = count7;
	}

	public int compareTo(Fast3Count o) {
		
		int flag = -1;
		flag = o.getCount20().compareTo(this.getCount20());
		if(flag == 0){
			flag = o.getCount14().compareTo(this.getCount14());
		}
		if(flag == 0){
			flag = o.getCount7().compareTo(this.getCount7());
		}
		if(flag == 0){
			flag = o.getNumber().compareTo(this.getNumber());
		}
		return flag;
	}
		
}
