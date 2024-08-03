/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.model.leave;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Manoj PC
 */
public class MiscLeaveBean implements Serializable {
    private String leaveSanctionOrderNo=null;
	private String leaveSanctionOrderDt=null;
	private LeaveType leaveType=null;
	private String leaveId[]=null;
	private String leaveId1=null;
	private String fromDate1=null;
	private String toDate1=null;
	private String empName = null;
	private String empId = null;
	private String gpfNo = null;
	private String suffix1 = null;
	private String prefix1 = null;
	private String suffix2 = null;
	private String prefix2 = null;
        private String joinFrom=null;
        private String joinTo=null;
	private String auth = null;
	private String authName = null;
	private LSOrderType ordertype=null;
	private String orderid=null;
	private String servicedate = null;
	private String leaveid1 = null;
	private String note1=null;
	private String vac=null;
	private String deptcode=null;
	private String offcode=null;
	private String offname=null;
	private String spn=null;
	private String syear=null;
	private String tyear=null;
	private String sdays=null;
	private ArrayList deptArray = null;
	private ArrayList offArray = null;
	private ArrayList authArray = null;
	private String notid=null;
	private String nottype=null;
	private String cancelNotId=null;
	private String FromDate1[]=null;
	private String nisb=null;
	private String leaveTypeCode=null;
	private String hidchk=null;
	private String hidSvid=null;
	private String prefixto[]=null;
	private String prefixfrm[]=null;
	private String suffixto[]=null;
	private String suffixfrm[]=null;
	
        private String trdatatype=null;
	
	//-------------for saving Multiple Leaves------------------
	private String fromDate[]=null;
	private String toDate[]=null;
	private String leaveid[] = null;
	private String suffix[] = null;
	private String prefix[] = null;
	private String note[]=null;
	
	private String leaveID[]= null;
	
	private String frmdate=null; 
	//---------------------------------------
	
	//--------------for authority-------------------------------
	private String authtype=null;
	private String txtdept=null;
	private String txtoff=null;
	private String txtauth=null;
	private String hidauth=null;
	
	private String authentype=null;
	private String endept=null;
	private String enoff=null;
	private String enauth=null;
	private String txtendept=null;
	private String txtenoff=null;
	private String txtenauth=null;
	private String hidenauth=null;
        private String chkmedical=null;
        private String chkcommut=null;
        private String chkcadrestatus=null;
        private String chklongleave=null;
	//---------------------------------------------------------
	
	
	
	public String getVac() {
		return vac;
	}
	public void setVac(String vac) {
		this.vac = vac;
	}
	public String[] getNote() {
		return note;
	}
	public void setNote(String[] note) {
		this.note = note;
	}
	public String[] getLeaveid() {
		return leaveid;
	}
	public void setLeaveid(String[] leaveid) {
		this.leaveid = leaveid;
	}
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	
	public String getEmpId() {
		return empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
	}
	public String getLeaveSanctionOrderNo() {
		return leaveSanctionOrderNo;
	}
	public void setLeaveSanctionOrderNo(String leaveSanctionOrderNo) {
		this.leaveSanctionOrderNo = leaveSanctionOrderNo;
	}
	
	public LeaveType getLeaveType() {
		return leaveType;
	}
	public void setLeaveType(LeaveType leaveType) {
		this.leaveType = leaveType;
	}
	public String[] getFromDate() {
		return fromDate;
	}
	public void setFromDate(String[] fromDate) {
		this.fromDate = fromDate;
	}
	public String getLeaveSanctionOrderDt() {
		return leaveSanctionOrderDt;
	}
	public void setLeaveSanctionOrderDt(String leaveSanctionOrderDt) {
		this.leaveSanctionOrderDt = leaveSanctionOrderDt;
	}
	public String[] getToDate() {
		return toDate;
	}
	public void setToDate(String[] toDate) {
		this.toDate = toDate;
	}
	public String[] getLeaveId() {
		return leaveId;
	}
	public void setLeaveId(String[] leaveId) {
		this.leaveId = leaveId;
	}
	public String getGpfNo() {
		return gpfNo;
	}
	public void setGpfNo(String gpfNo) {
		this.gpfNo = gpfNo;
	}
	public String getAuth() {
		return auth;
	}
	public void setAuth(String auth) {
		this.auth = auth;
	}
	
	public LSOrderType getOrdertype() {
		return ordertype;
	}
	public void setOrdertype(LSOrderType ordertype) {
		this.ordertype = ordertype;
	}
	public String[] getPrefix() {
		return prefix;
	}
	public void setPrefix(String[] prefix) {
		this.prefix = prefix;
	}
	public String getServicedate() {
		return servicedate;
	}
	public void setServicedate(String servicedate) {
		this.servicedate = servicedate;
	}
	public String[] getSuffix() {
		return suffix;
	}
	public void setSuffix(String[] suffix) {
		this.suffix = suffix;
	}
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	public String getAuthName() {
		return authName;
	}
	public void setAuthName(String authName) {
		this.authName = authName;
	}
	public String getDeptcode() {
		return deptcode;
	}
	public void setDeptcode(String deptcode) {
		this.deptcode = deptcode;
	}
	public String getOffcode() {
		return offcode;
	}
	public void setOffcode(String offcode) {
		this.offcode = offcode;
	}
	public String getSpn() {
		return spn;
	}
	public void setSpn(String spn) {
		this.spn = spn;
	}
	public String getSdays() {
		return sdays;
	}
	public void setSdays(String sdays) {
		this.sdays = sdays;
	}
	public String getSyear() {
		return syear;
	}
	public void setSyear(String syear) {
		this.syear = syear;
	}
	public String getTyear() {
		return tyear;
	}
	public void setTyear(String tyear) {
		this.tyear = tyear;
	}
	public String getOffname() {
		return offname;
	}
	public void setOffname(String offname) {
		this.offname = offname;
	}
	public ArrayList getAuthArray() {
		return authArray;
	}
	public void setAuthArray(ArrayList authArray) {
		this.authArray = authArray;
	}
	public ArrayList getDeptArray() {
		return deptArray;
	}
	public void setDeptArray(ArrayList deptArray) {
		this.deptArray = deptArray;
	}
	public ArrayList getOffArray() {
		return offArray;
	}
	public void setOffArray(ArrayList offArray) {
		this.offArray = offArray;
	}
	public String getNotid() {
		return notid;
	}
	public void setNotid(String notid) {
		this.notid = notid;
	}
	public String getNottype() {
		return nottype;
	}
	public void setNottype(String nottype) {
		this.nottype = nottype;
	}
	public String getCancelNotId() {
		return cancelNotId;
	}
	public void setCancelNotId(String cancelNotId) {
		this.cancelNotId = cancelNotId;
	}
	public String[] getFromDate1() {
		return FromDate1;
	}
	public void setFromDate1(String[] fromDate1) {
		FromDate1 = fromDate1;
	}
	public String getLeaveid1() {
		return leaveid1;
	}
	public void setLeaveid1(String leaveid1) {
		this.leaveid1 = leaveid1;
	}
	public String getNote1() {
		return note1;
	}
	public void setNote1(String note1) {
		this.note1 = note1;
	}
	public String getPrefix1() {
		return prefix1;
	}
	public void setPrefix1(String prefix1) {
		this.prefix1 = prefix1;
	}
	public String getSuffix1() {
		return suffix1;
	}
	public void setSuffix1(String suffix1) {
		this.suffix1 = suffix1;
	}
	public String getToDate1() {
		return toDate1;
	}
	public void setToDate1(String toDate1) {
		this.toDate1 = toDate1;
	}
	public void setFromDate1(String fromDate1) {
		this.fromDate1 = fromDate1;
	}
	public String getLeaveId1() {
		return leaveId1;
	}
	public void setLeaveId1(String leaveId1) {
		this.leaveId1 = leaveId1;
	}
	public String[] getLeaveID() {
		return leaveID;
	}
	public void setLeaveID(String[] leaveID) {
		this.leaveID = leaveID;
	}
	public String getFrmdate() {
		return frmdate;
	}
	public void setFrmdate(String frmdate) {
		this.frmdate = frmdate;
	}
	public String getLeaveTypeCode() {
		return leaveTypeCode;
	}
	public void setLeaveTypeCode(String leaveTypeCode) {
		this.leaveTypeCode = leaveTypeCode;
	}
	public String getNisb() {
		return nisb;
	}
	public void setNisb(String nisb) {
		this.nisb = nisb;
	}
	public String[] getPrefixfrm() {
		return prefixfrm;
	}
	public void setPrefixfrm(String[] prefixfrm) {
		this.prefixfrm = prefixfrm;
	}
	public String[] getPrefixto() {
		return prefixto;
	}
	public void setPrefixto(String[] prefixto) {
		this.prefixto = prefixto;
	}
	public String[] getSuffixfrm() {
		return suffixfrm;
	}
	public void setSuffixfrm(String[] suffixfrm) {
		this.suffixfrm = suffixfrm;
	}
	public String[] getSuffixto() {
		return suffixto;
	}
	public void setSuffixto(String[] suffixto) {
		this.suffixto = suffixto;
	}
	public String getPrefix2() {
		return prefix2;
	}
	public void setPrefix2(String prefix2) {
		this.prefix2 = prefix2;
	}
	public String getSuffix2() {
		return suffix2;
	}
	public void setSuffix2(String suffix2) {
		this.suffix2 = suffix2;
	}
	
	public String getHidchk() {
		return hidchk;
	}
	public void setHidchk(String hidchk) {
		this.hidchk = hidchk;
	}
	public String getAuthtype() {
		return authtype;
	}
	public void setAuthtype(String authtype) {
		this.authtype = authtype;
	}
	public String getHidauth() {
		return hidauth;
	}
	public void setHidauth(String hidauth) {
		this.hidauth = hidauth;
	}
	public String getTxtauth() {
		return txtauth;
	}
	public void setTxtauth(String txtauth) {
		this.txtauth = txtauth;
	}
	public String getTxtdept() {
		return txtdept;
	}
	public void setTxtdept(String txtdept) {
		this.txtdept = txtdept;
	}
	public String getTxtoff() {
		return txtoff;
	}
	public void setTxtoff(String txtoff) {
		this.txtoff = txtoff;
	}
	public String getAuthentype() {
		return authentype;
	}
	public void setAuthentype(String authentype) {
		this.authentype = authentype;
	}
	public String getEnauth() {
		return enauth;
	}
	public void setEnauth(String enauth) {
		this.enauth = enauth;
	}
	public String getEndept() {
		return endept;
	}
	public void setEndept(String endept) {
		this.endept = endept;
	}
	public String getEnoff() {
		return enoff;
	}
	public void setEnoff(String enoff) {
		this.enoff = enoff;
	}
	public String getHidenauth() {
		return hidenauth;
	}
	public void setHidenauth(String hidenauth) {
		this.hidenauth = hidenauth;
	}
	public String getTxtenauth() {
		return txtenauth;
	}
	public void setTxtenauth(String txtenauth) {
		this.txtenauth = txtenauth;
	}
	public String getTxtendept() {
		return txtendept;
	}
	public void setTxtendept(String txtendept) {
		this.txtendept = txtendept;
	}
	public String getTxtenoff() {
		return txtenoff;
	}
	public void setTxtenoff(String txtenoff) {
		this.txtenoff = txtenoff;
	}


    public void setChkmedical(String chkmedical) {
        this.chkmedical = chkmedical;
    }

    public String getChkmedical() {
        return chkmedical;
    }

    public void setChkcommut(String chkcommut) {
        this.chkcommut = chkcommut;
    }

    public String getChkcommut() {
        return chkcommut;
    }

    public void setTrdatatype(String trdatatype) {
        this.trdatatype = trdatatype;
    }

    public String getTrdatatype() {
        return trdatatype;
    }

    public void setChkcadrestatus(String chkcadrestatus) {
        this.chkcadrestatus = chkcadrestatus;
    }

    public String getChkcadrestatus() {
        return chkcadrestatus;
    }

    public void setChklongleave(String chklongleave) {
        this.chklongleave = chklongleave;
    }

    public String getChklongleave() {
        return chklongleave;
    }

    public void setHidSvid(String hidSvid) {
        this.hidSvid = hidSvid;
    }

    public String getHidSvid() {
        return hidSvid;
    }

    public void setJoinFrom(String joinFrom) {
        this.joinFrom = joinFrom;
    }

    public String getJoinFrom() {
        return joinFrom;
    }

    public void setJoinTo(String joinTo) {
        this.joinTo = joinTo;
    }

    public String getJoinTo() {
        return joinTo;
    }
}
