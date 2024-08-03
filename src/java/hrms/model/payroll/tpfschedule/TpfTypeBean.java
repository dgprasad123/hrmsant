package hrms.model.payroll.tpfschedule;

import java.util.ArrayList;

public class TpfTypeBean {

    private String gpfType;
    private ArrayList empGpfList;
    private String pfcode;
    private String totalamt;
    private int empno;
    private ArrayList gpfabstractList;

    public String getGpfType() {
        return gpfType;
    }

    public void setGpfType(String gpfType) {
        this.gpfType = gpfType;
    }

    public ArrayList getEmpGpfList() {
        return empGpfList;
    }

    public void setEmpGpfList(ArrayList empGpfList) {
        this.empGpfList = empGpfList;
    }

    public String getPfcode() {
        return pfcode;
    }

    public void setPfcode(String pfcode) {
        this.pfcode = pfcode;
    }

    public String getTotalamt() {
        return totalamt;
    }

    public void setTotalamt(String totalamt) {
        this.totalamt = totalamt;
    }

    public int getEmpno() {
        return empno;
    }

    public void setEmpno(int empno) {
        this.empno = empno;
    }

    public ArrayList getGpfabstractList() {
        return gpfabstractList;
    }

    public void setGpfabstractList(ArrayList gpfabstractList) {
        this.gpfabstractList = gpfabstractList;
    }

}
