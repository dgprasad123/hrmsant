/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.loanworkflow;

import java.util.ArrayList;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author lenovo
 */
public class LoanHBAForm {

    private String empSPC = null;
    private String forwardtoHrmsid = null;
    private int taskid = 0;
    private int loanId = 0;
    private String approvedBy = null;

    private String approvedSpc = null;
    private String diskFileName = null;
    private String diskFileName1 = null;
    private String hidOffCode = null;
    private String hidOffName = null;
    private String hidSPC = null;
    private MultipartFile file_att = null;
    private MultipartFile file_att1 = null;
    private int loan_status = 0;
    private String designation = null;
    private String empName = null;
    private String cursalary = null;
    private String curbasicsalary = null;
    private String netPay = null;
    private String department = null;
    private String adminDept = null;
    private String stationposted = null;
    private String jobtype = null;
    private String per_post = null;
    private String per_appointment = null;
    private String dob = null;
    private String ndob = null;
    private String superannuation = null;
    private String other_govt_ser = null;
    private String address = null;
    private String floor_area = null;
    private String approx_valuation = null;

    private String reason = null;
    private String constructed_area = null;
    private String cost_land = null;
    private String cost_building = null;
    private String total_amount = null;
    private String amount_adv = null;
    private String noofYear = null;
    private String city_name = null;
    private String settle_retir = null;
    private String area_plot = null;
    private String localauthority = null;
    private String propose_acquire = null;
    private String no_rooms = null;
    private String total_floor = null;
    private String additional_storey = null;
    private String addition_room = null;
    private String addition_farea = null;
    private String est_cost = null;
    private String amount_desired = null;
    private String year_repaid = null;
    private String exact_location = null;
    private String exact_floor_area = null;
    private String plinth_area = null;
    private String total_land_cost = null;
    private String parties_name_address = null;
    private String repay_adv_amount = null;
    private String repay_year = null;
    private String readymade_exact_loc = null;
    private String readymade_floor_area = null;
    private String readymade_plinth_area = null;
    private String house_age = null;
    private String valuation_price = null;
    private String owner_name = null;
    private String readymade_appro_amount = null;
    private String readymade_adv_amount = null;
    private String readymade_year = null;
    private String propose_amount = null;
    private String propose_adv = null;
    private String propose_repaid = null;
    private String term_lease = null;
    private String term_expired = null;
    private String lease_condition = null;
    private String premimum_paid = null;
    private String annual_rent = null;
    private String encumb_status = null;
    private String doj = null;
    private String loancomments = null;
    private String letterNo = null;
    private String letterDate = null;
    private String memoNo = null;
    private String memoDate = null;
    private String letterformName = null;
    private String letterformdesignation = null;
    private String letterto = null;
    private String officeAddress = null;
    private String loanpurpose = null;
    private int statusId = 0;

    private String samount = null;
    private String ramount = null;
    private int emiPrincipal;

    private String principalAmount = null;
    private int emiInterest;
    private String interestAmount = null;
    private String interestType = null;
    private String rateInterest = null;
    private String lastInstallment = null;

    private String penalRate = null;
    private String moratoriumRequired = null;
    private String moratoriumPeriod = null;
    private String recDate = null;
    private String insuranceFlag = null;
    private String sanOperator = null;

    private String sanOperatorDDO = null;
    private String sanOperatorLoginId = null;

    private String accountNo = null;
    // private String dob = null;
    private String dos = null;
    private String empType = null;
    //private String address = null;
    private String loanType = null;
    private String pranNo = null;
    private String releaseAmount = null;

    private int principalInstallment;
    private int principalInstAmount;
    private String accountType = null;

    private String sanctionNo = null;
    private String sanctionDate = null;
    private String totalInterestamount = null;
    private String billDescription = null;
    private String billno = null;
    private String billdate = null;
    private String billAmount = null;
    private String chatofAccount = null;

    private String billid;
    private String demandNo;
    private String majorhead;

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public String getEmpSPC() {
        return empSPC;
    }

    public void setEmpSPC(String empSPC) {
        this.empSPC = empSPC;
    }

    public String getForwardtoHrmsid() {
        return forwardtoHrmsid;
    }

    public void setForwardtoHrmsid(String forwardtoHrmsid) {
        this.forwardtoHrmsid = forwardtoHrmsid;
    }

    public int getTaskid() {
        return taskid;
    }

    public String getDiskFileName1() {
        return diskFileName1;
    }

    public void setDiskFileName1(String diskFileName1) {
        this.diskFileName1 = diskFileName1;
    }

    public void setTaskid(int taskid) {
        this.taskid = taskid;
    }

    public int getLoanId() {
        return loanId;
    }

    public String getLocalauthority() {
        return localauthority;
    }

    public void setLocalauthority(String localauthority) {
        this.localauthority = localauthority;
    }

    public void setLoanId(int loanId) {
        this.loanId = loanId;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getApprovedSpc() {
        return approvedSpc;
    }

    public void setApprovedSpc(String approvedSpc) {
        this.approvedSpc = approvedSpc;
    }

    public String getDiskFileName() {
        return diskFileName;
    }

    public void setDiskFileName(String diskFileName) {
        this.diskFileName = diskFileName;
    }

    public String getHidOffCode() {
        return hidOffCode;
    }

    public void setHidOffCode(String hidOffCode) {
        this.hidOffCode = hidOffCode;
    }

    public String getHidOffName() {
        return hidOffName;
    }

    public void setHidOffName(String hidOffName) {
        this.hidOffName = hidOffName;
    }

    public String getHidSPC() {
        return hidSPC;
    }

    public void setHidSPC(String hidSPC) {
        this.hidSPC = hidSPC;
    }

    public MultipartFile getFile_att() {
        return file_att;
    }

    public void setFile_att(MultipartFile file_att) {
        this.file_att = file_att;
    }

    public MultipartFile getFile_att1() {
        return file_att1;
    }

    public void setFile_att1(MultipartFile file_att1) {
        this.file_att1 = file_att1;
    }

    public int getLoan_status() {
        return loan_status;
    }

    public void setLoan_status(int loan_status) {
        this.loan_status = loan_status;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getCursalary() {
        return cursalary;
    }

    public void setCursalary(String cursalary) {
        this.cursalary = cursalary;
    }

    public String getCurbasicsalary() {
        return curbasicsalary;
    }

    public void setCurbasicsalary(String curbasicsalary) {
        this.curbasicsalary = curbasicsalary;
    }

    public String getNetPay() {
        return netPay;
    }

    public void setNetPay(String netPay) {
        this.netPay = netPay;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getAdminDept() {
        return adminDept;
    }

    public void setAdminDept(String adminDept) {
        this.adminDept = adminDept;
    }

    public String getStationposted() {
        return stationposted;
    }

    public void setStationposted(String stationposted) {
        this.stationposted = stationposted;
    }

    public String getJobtype() {
        return jobtype;
    }

    public void setJobtype(String jobtype) {
        this.jobtype = jobtype;
    }

    public String getPer_post() {
        return per_post;
    }

    public void setPer_post(String per_post) {
        this.per_post = per_post;
    }

    public String getPer_appointment() {
        return per_appointment;
    }

    public void setPer_appointment(String per_appointment) {
        this.per_appointment = per_appointment;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getNdob() {
        return ndob;
    }

    public void setNdob(String ndob) {
        this.ndob = ndob;
    }

    public String getSuperannuation() {
        return superannuation;
    }

    public void setSuperannuation(String superannuation) {
        this.superannuation = superannuation;
    }

    public String getOther_govt_ser() {
        return other_govt_ser;
    }

    public void setOther_govt_ser(String other_govt_ser) {
        this.other_govt_ser = other_govt_ser;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFloor_area() {
        return floor_area;
    }

    public void setFloor_area(String floor_area) {
        this.floor_area = floor_area;
    }

    public String getApprox_valuation() {
        return approx_valuation;
    }

    public void setApprox_valuation(String approx_valuation) {
        this.approx_valuation = approx_valuation;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getConstructed_area() {
        return constructed_area;
    }

    public void setConstructed_area(String constructed_area) {
        this.constructed_area = constructed_area;
    }

    public String getCost_land() {
        return cost_land;
    }

    public void setCost_land(String cost_land) {
        this.cost_land = cost_land;
    }

    public String getCost_building() {
        return cost_building;
    }

    public void setCost_building(String cost_building) {
        this.cost_building = cost_building;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getAmount_adv() {
        return amount_adv;
    }

    public void setAmount_adv(String amount_adv) {
        this.amount_adv = amount_adv;
    }

    public String getNoofYear() {
        return noofYear;
    }

    public void setNoofYear(String noofYear) {
        this.noofYear = noofYear;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getSettle_retir() {
        return settle_retir;
    }

    public void setSettle_retir(String settle_retir) {
        this.settle_retir = settle_retir;
    }

    public String getArea_plot() {
        return area_plot;
    }

    public void setArea_plot(String area_plot) {
        this.area_plot = area_plot;
    }

    public String getPropose_acquire() {
        return propose_acquire;
    }

    public void setPropose_acquire(String propose_acquire) {
        this.propose_acquire = propose_acquire;
    }

    public String getNo_rooms() {
        return no_rooms;
    }

    public void setNo_rooms(String no_rooms) {
        this.no_rooms = no_rooms;
    }

    public String getTotal_floor() {
        return total_floor;
    }

    public void setTotal_floor(String total_floor) {
        this.total_floor = total_floor;
    }

    public String getAdditional_storey() {
        return additional_storey;
    }

    public void setAdditional_storey(String additional_storey) {
        this.additional_storey = additional_storey;
    }

    public String getAddition_room() {
        return addition_room;
    }

    public void setAddition_room(String addition_room) {
        this.addition_room = addition_room;
    }

    public String getAddition_farea() {
        return addition_farea;
    }

    public void setAddition_farea(String addition_farea) {
        this.addition_farea = addition_farea;
    }

    public String getEst_cost() {
        return est_cost;
    }

    public void setEst_cost(String est_cost) {
        this.est_cost = est_cost;
    }

    public String getAmount_desired() {
        return amount_desired;
    }

    public void setAmount_desired(String amount_desired) {
        this.amount_desired = amount_desired;
    }

    public String getYear_repaid() {
        return year_repaid;
    }

    public void setYear_repaid(String year_repaid) {
        this.year_repaid = year_repaid;
    }

    public String getExact_location() {
        return exact_location;
    }

    public void setExact_location(String exact_location) {
        this.exact_location = exact_location;
    }

    public String getExact_floor_area() {
        return exact_floor_area;
    }

    public void setExact_floor_area(String exact_floor_area) {
        this.exact_floor_area = exact_floor_area;
    }

    public String getPlinth_area() {
        return plinth_area;
    }

    public void setPlinth_area(String plinth_area) {
        this.plinth_area = plinth_area;
    }

    public String getTotal_land_cost() {
        return total_land_cost;
    }

    public void setTotal_land_cost(String total_land_cost) {
        this.total_land_cost = total_land_cost;
    }

    public String getParties_name_address() {
        return parties_name_address;
    }

    public void setParties_name_address(String parties_name_address) {
        this.parties_name_address = parties_name_address;
    }

    public String getRepay_adv_amount() {
        return repay_adv_amount;
    }

    public void setRepay_adv_amount(String repay_adv_amount) {
        this.repay_adv_amount = repay_adv_amount;
    }

    public String getRepay_year() {
        return repay_year;
    }

    public void setRepay_year(String repay_year) {
        this.repay_year = repay_year;
    }

    public String getReadymade_exact_loc() {
        return readymade_exact_loc;
    }

    public void setReadymade_exact_loc(String readymade_exact_loc) {
        this.readymade_exact_loc = readymade_exact_loc;
    }

    public String getReadymade_floor_area() {
        return readymade_floor_area;
    }

    public void setReadymade_floor_area(String readymade_floor_area) {
        this.readymade_floor_area = readymade_floor_area;
    }

    public String getReadymade_plinth_area() {
        return readymade_plinth_area;
    }

    public void setReadymade_plinth_area(String readymade_plinth_area) {
        this.readymade_plinth_area = readymade_plinth_area;
    }

    public String getHouse_age() {
        return house_age;
    }

    public void setHouse_age(String house_age) {
        this.house_age = house_age;
    }

    public String getValuation_price() {
        return valuation_price;
    }

    public void setValuation_price(String valuation_price) {
        this.valuation_price = valuation_price;
    }

    public String getOwner_name() {
        return owner_name;
    }

    public void setOwner_name(String owner_name) {
        this.owner_name = owner_name;
    }

    public String getReadymade_appro_amount() {
        return readymade_appro_amount;
    }

    public void setReadymade_appro_amount(String readymade_appro_amount) {
        this.readymade_appro_amount = readymade_appro_amount;
    }

    public String getReadymade_adv_amount() {
        return readymade_adv_amount;
    }

    public void setReadymade_adv_amount(String readymade_adv_amount) {
        this.readymade_adv_amount = readymade_adv_amount;
    }

    public String getReadymade_year() {
        return readymade_year;
    }

    public void setReadymade_year(String readymade_year) {
        this.readymade_year = readymade_year;
    }

    public String getPropose_amount() {
        return propose_amount;
    }

    public void setPropose_amount(String propose_amount) {
        this.propose_amount = propose_amount;
    }

    public String getPropose_adv() {
        return propose_adv;
    }

    public void setPropose_adv(String propose_adv) {
        this.propose_adv = propose_adv;
    }

    public String getPropose_repaid() {
        return propose_repaid;
    }

    public void setPropose_repaid(String propose_repaid) {
        this.propose_repaid = propose_repaid;
    }

    public String getTerm_lease() {
        return term_lease;
    }

    public void setTerm_lease(String term_lease) {
        this.term_lease = term_lease;
    }

    public String getTerm_expired() {
        return term_expired;
    }

    public void setTerm_expired(String term_expired) {
        this.term_expired = term_expired;
    }

    public String getLease_condition() {
        return lease_condition;
    }

    public void setLease_condition(String lease_condition) {
        this.lease_condition = lease_condition;
    }

    public String getPremimum_paid() {
        return premimum_paid;
    }

    public void setPremimum_paid(String premimum_paid) {
        this.premimum_paid = premimum_paid;
    }

    public String getAnnual_rent() {
        return annual_rent;
    }

    public void setAnnual_rent(String annual_rent) {
        this.annual_rent = annual_rent;
    }

    public String getEncumb_status() {
        return encumb_status;
    }

    public void setEncumb_status(String encumb_status) {
        this.encumb_status = encumb_status;
    }

    public String getDoj() {
        return doj;
    }

    public void setDoj(String doj) {
        this.doj = doj;
    }

    public String getLoancomments() {
        return loancomments;
    }

    public void setLoancomments(String loancomments) {
        this.loancomments = loancomments;
    }

    public String getLetterNo() {
        return letterNo;
    }

    public void setLetterNo(String letterNo) {
        this.letterNo = letterNo;
    }

    public String getLetterDate() {
        return letterDate;
    }

    public void setLetterDate(String letterDate) {
        this.letterDate = letterDate;
    }

    public String getMemoNo() {
        return memoNo;
    }

    public void setMemoNo(String memoNo) {
        this.memoNo = memoNo;
    }

    public String getMemoDate() {
        return memoDate;
    }

    public void setMemoDate(String memoDate) {
        this.memoDate = memoDate;
    }

    public String getLetterformName() {
        return letterformName;
    }

    public void setLetterformName(String letterformName) {
        this.letterformName = letterformName;
    }

    public String getLetterformdesignation() {
        return letterformdesignation;
    }

    public void setLetterformdesignation(String letterformdesignation) {
        this.letterformdesignation = letterformdesignation;
    }

    public String getLetterto() {
        return letterto;
    }

    public void setLetterto(String letterto) {
        this.letterto = letterto;
    }

    public String getOfficeAddress() {
        return officeAddress;
    }

    public void setOfficeAddress(String officeAddress) {
        this.officeAddress = officeAddress;
    }

    public String getLoanpurpose() {
        return loanpurpose;
    }

    public void setLoanpurpose(String loanpurpose) {
        this.loanpurpose = loanpurpose;
    }

    public String getSamount() {
        return samount;
    }

    public void setSamount(String samount) {
        this.samount = samount;
    }

    public String getRamount() {
        return ramount;
    }

    public void setRamount(String ramount) {
        this.ramount = ramount;
    }

    public int getEmiPrincipal() {
        return emiPrincipal;
    }

    public void setEmiPrincipal(int emiPrincipal) {
        this.emiPrincipal = emiPrincipal;
    }

    public String getPrincipalAmount() {
        return principalAmount;
    }

    public void setPrincipalAmount(String principalAmount) {
        this.principalAmount = principalAmount;
    }

    public int getEmiInterest() {
        return emiInterest;
    }

    public void setEmiInterest(int emiInterest) {
        this.emiInterest = emiInterest;
    }

    public String getInterestAmount() {
        return interestAmount;
    }

    public void setInterestAmount(String interestAmount) {
        this.interestAmount = interestAmount;
    }

    public String getInterestType() {
        return interestType;
    }

    public void setInterestType(String interestType) {
        this.interestType = interestType;
    }

    public String getRateInterest() {
        return rateInterest;
    }

    public void setRateInterest(String rateInterest) {
        this.rateInterest = rateInterest;
    }

    public String getLastInstallment() {
        return lastInstallment;
    }

    public void setLastInstallment(String lastInstallment) {
        this.lastInstallment = lastInstallment;
    }

    public String getPenalRate() {
        return penalRate;
    }

    public void setPenalRate(String penalRate) {
        this.penalRate = penalRate;
    }

    public String getMoratoriumRequired() {
        return moratoriumRequired;
    }

    public void setMoratoriumRequired(String moratoriumRequired) {
        this.moratoriumRequired = moratoriumRequired;
    }

    public String getMoratoriumPeriod() {
        return moratoriumPeriod;
    }

    public void setMoratoriumPeriod(String moratoriumPeriod) {
        this.moratoriumPeriod = moratoriumPeriod;
    }

    public String getRecDate() {
        return recDate;
    }

    public void setRecDate(String recDate) {
        this.recDate = recDate;
    }

    public String getInsuranceFlag() {
        return insuranceFlag;
    }

    public void setInsuranceFlag(String insuranceFlag) {
        this.insuranceFlag = insuranceFlag;
    }

    public String getSanOperator() {
        return sanOperator;
    }

    public void setSanOperator(String sanOperator) {
        this.sanOperator = sanOperator;
    }

    public String getSanOperatorDDO() {
        return sanOperatorDDO;
    }

    public void setSanOperatorDDO(String sanOperatorDDO) {
        this.sanOperatorDDO = sanOperatorDDO;
    }

    public String getSanOperatorLoginId() {
        return sanOperatorLoginId;
    }

    public void setSanOperatorLoginId(String sanOperatorLoginId) {
        this.sanOperatorLoginId = sanOperatorLoginId;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getDos() {
        return dos;
    }

    public void setDos(String dos) {
        this.dos = dos;
    }

    public String getEmpType() {
        return empType;
    }

    public void setEmpType(String empType) {
        this.empType = empType;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public String getPranNo() {
        return pranNo;
    }

    public void setPranNo(String pranNo) {
        this.pranNo = pranNo;
    }

    public String getReleaseAmount() {
        return releaseAmount;
    }

    public void setReleaseAmount(String releaseAmount) {
        this.releaseAmount = releaseAmount;
    }

    public int getPrincipalInstallment() {
        return principalInstallment;
    }

    public void setPrincipalInstallment(int principalInstallment) {
        this.principalInstallment = principalInstallment;
    }

    public int getPrincipalInstAmount() {
        return principalInstAmount;
    }

    public void setPrincipalInstAmount(int principalInstAmount) {
        this.principalInstAmount = principalInstAmount;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getSanctionNo() {
        return sanctionNo;
    }

    public void setSanctionNo(String sanctionNo) {
        this.sanctionNo = sanctionNo;
    }

    public String getSanctionDate() {
        return sanctionDate;
    }

    public void setSanctionDate(String sanctionDate) {
        this.sanctionDate = sanctionDate;
    }

    public String getTotalInterestamount() {
        return totalInterestamount;
    }

    public void setTotalInterestamount(String totalInterestamount) {
        this.totalInterestamount = totalInterestamount;
    }

    public String getBillDescription() {
        return billDescription;
    }

    public void setBillDescription(String billDescription) {
        this.billDescription = billDescription;
    }

    public String getBillno() {
        return billno;
    }

    public void setBillno(String billno) {
        this.billno = billno;
    }

    public String getBilldate() {
        return billdate;
    }

    public void setBilldate(String billdate) {
        this.billdate = billdate;
    }

    public String getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(String billAmount) {
        this.billAmount = billAmount;
    }

    public String getChatofAccount() {
        return chatofAccount;
    }

    public void setChatofAccount(String chatofAccount) {
        this.chatofAccount = chatofAccount;
    }

    public String getBillid() {
        return billid;
    }

    public void setBillid(String billid) {
        this.billid = billid;
    }

    public String getDemandNo() {
        return demandNo;
    }

    public void setDemandNo(String demandNo) {
        this.demandNo = demandNo;
    }

    public String getMajorhead() {
        return majorhead;
    }

    public void setMajorhead(String majorhead) {
        this.majorhead = majorhead;
    }

}
