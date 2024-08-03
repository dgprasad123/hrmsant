package hrms.common;

public class MaxObj{
	private String tblname;
	private String fldname;
	private String maxcode;
	public MaxObj(String tblname,String fldname,String maxcode){
		this.tblname=tblname;
		this.fldname=fldname;
		this.maxcode=maxcode;
	}
	public String getFldname() {
		return fldname;
	}
	public void setFldname(String fldname) {
		this.fldname = fldname;
	}
	public String getMaxcode() {
		return maxcode;
	}
	public void setMaxcode(String maxcode) {
		this.maxcode = maxcode;
	}
	public String getTblname() {
		return tblname;
	}
	public void setTblname(String tblname) {
		this.tblname = tblname;
	}
}
