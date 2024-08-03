/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.master;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Surendra
 */
public class GPost implements Serializable {

    private String postcode;

    private String deptcode;

    private String post;

    private String ifael;

    private String vacation;

    private String postgrpType;

    private String isauth;

    private String numEmployees;

    private String postCodes;

    private String isCorrect;

    private String sltForeignBody;
    private String totNumPost;

    private String dupPostCode;
    private String finPostCode;
    private String dupVacation = null;
    private String dupPostGrp = null;
    private String dupIsAuthority = null;

    private String finVacation = null;
    private String finPostGrp = null;
    private String finIsAuthority = null;
    private String availEmpOnGenPostName = null;

    private String verifiedMergedPost = null;
    private String hiddeptCode = null;
    private String mergingMsg = null;
    private int totSpc;
    private String chkPost;
    private String hidDupPostCode=null;
    private String hidFinPostCode=null;

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getDeptcode() {
        return deptcode;
    }

    public void setDeptcode(String deptcode) {
        this.deptcode = deptcode;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getIfael() {
        return ifael;
    }

    public void setIfael(String ifael) {
        this.ifael = ifael;
    }

    public String getVacation() {
        return vacation;
    }

    public void setVacation(String vacation) {
        this.vacation = vacation;
    }

    public String getPostgrpType() {
        return postgrpType;
    }

    public void setPostgrpType(String postgrpType) {
        this.postgrpType = postgrpType;
    }

    public String getIsauth() {
        return isauth;
    }

    public void setIsauth(String isauth) {
        this.isauth = isauth;
    }

    public String getNumEmployees() {
        return numEmployees;
    }

    public void setNumEmployees(String numEmployees) {
        this.numEmployees = numEmployees;
    }

    public String getPostCodes() {
        return postCodes;
    }

    public void setPostCodes(String postCodes) {
        this.postCodes = postCodes;
    }

    public String getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(String isCorrect) {
        this.isCorrect = isCorrect;
    }

    public String getSltForeignBody() {
        return sltForeignBody;
    }

    public void setSltForeignBody(String sltForeignBody) {
        this.sltForeignBody = sltForeignBody;
    }

    public String getTotNumPost() {
        return totNumPost;
    }

    public void setTotNumPost(String totNumPost) {
        this.totNumPost = totNumPost;
    }

    public String getDupPostCode() {
        return dupPostCode;
    }

    public void setDupPostCode(String dupPostCode) {
        this.dupPostCode = dupPostCode;
    }

    public String getDupVacation() {
        return dupVacation;
    }

    public void setDupVacation(String dupVacation) {
        this.dupVacation = dupVacation;
    }

    public String getDupPostGrp() {
        return dupPostGrp;
    }

    public void setDupPostGrp(String dupPostGrp) {
        this.dupPostGrp = dupPostGrp;
    }

    public String getDupIsAuthority() {
        return dupIsAuthority;
    }

    public void setDupIsAuthority(String dupIsAuthority) {
        this.dupIsAuthority = dupIsAuthority;
    }

    public String getFinVacation() {
        return finVacation;
    }

    public void setFinVacation(String finVacation) {
        this.finVacation = finVacation;
    }

    public String getFinPostGrp() {
        return finPostGrp;
    }

    public void setFinPostGrp(String finPostGrp) {
        this.finPostGrp = finPostGrp;
    }

    public String getFinIsAuthority() {
        return finIsAuthority;
    }

    public void setFinIsAuthority(String finIsAuthority) {
        this.finIsAuthority = finIsAuthority;
    }

    public String getAvailEmpOnGenPostName() {
        return availEmpOnGenPostName;
    }

    public void setAvailEmpOnGenPostName(String availEmpOnGenPostName) {
        this.availEmpOnGenPostName = availEmpOnGenPostName;
    }

    public String getVerifiedMergedPost() {
        return verifiedMergedPost;
    }

    public void setVerifiedMergedPost(String verifiedMergedPost) {
        this.verifiedMergedPost = verifiedMergedPost;
    }

    public String getHiddeptCode() {
        return hiddeptCode;
    }

    public void setHiddeptCode(String hiddeptCode) {
        this.hiddeptCode = hiddeptCode;
    }

    public String getMergingMsg() {
        return mergingMsg;
    }

    public void setMergingMsg(String mergingMsg) {
        this.mergingMsg = mergingMsg;
    }

    public int getTotSpc() {
        return totSpc;
    }

    public void setTotSpc(int totSpc) {
        this.totSpc = totSpc;
    }

    public String getChkPost() {
        return chkPost;
    }

    public void setChkPost(String chkPost) {
        this.chkPost = chkPost;
    }

    public String getHidDupPostCode() {
        return hidDupPostCode;
    }

    public void setHidDupPostCode(String hidDupPostCode) {
        this.hidDupPostCode = hidDupPostCode;
    }

    public String getHidFinPostCode() {
        return hidFinPostCode;
    }

    public void setHidFinPostCode(String hidFinPostCode) {
        this.hidFinPostCode = hidFinPostCode;
    }

    public String getFinPostCode() {
        return finPostCode;
    }

    public void setFinPostCode(String finPostCode) {
        this.finPostCode = finPostCode;
    }
    

}
