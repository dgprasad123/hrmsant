/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.payroll.billbrowser;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Manas Jena
 */
@XmlRootElement(name = "loan")
@XmlAccessorType(XmlAccessType.FIELD)
public class LtaBeanForAG {

    private String LOAN_TYP;
    private String LOAN_STYP;
    private String SNC_NO;
    private String SNC_DT;
    private String FIN_YR;
    private String ACC_MN;
    private String ACC_TYP;
    private String SEC_NM;
    private String SAL_MN;
    private String ADJ_MN;
    private String PI_FLG;
    private String RECV_ORD_NO;
    private String RECV_ORD_DT;
    private String RECV_TYP;
    private String PAY_MODE;
    private String TR_FIN_YR;
    private String TR_MN;
    private String TR_CD;
    private String RMJHCD;
    private String RSMJHCD;
    private String RMIHCD;
    private String RSBHCD;
    private String RDTLHCD;
    private String RSDTLHCD;
    private String LOPCAC;
    private String TVTC_NO;
    private String TVTC_DT;
    private String TVTC_AMT;
    private String DDO_CD;
    private int INST_NO;
    private String AMT_PAID;
    private String BASE_HD_CD;
    private String REF_UP_TE_NO;
    private String REF_ROWID;
    private String WHO_USER;
    private String WHO_DT;
    private String AMT_TYP;
    private String LN_IDNO;
    private String LOANWISESUM;
    private String UNITNO = null;
    private String AD_REF_ID=null;
    private String IS_VERIFIED=null;

    public String getLOANWISESUM() {
        return LOANWISESUM;
    }

    public void setLOANWISESUM(String LOANWISESUM) {
        this.LOANWISESUM = LOANWISESUM;
    }

    public String getLN_IDNO() {
        return LN_IDNO;
    }

    public void setLN_IDNO(String LN_IDNO) {
        this.LN_IDNO = LN_IDNO;
    }

    public String getLOAN_TYP() {
        return LOAN_TYP;
    }

    public void setLOAN_TYP(String LOAN_TYP) {
        this.LOAN_TYP = LOAN_TYP;
    }

    public String getLOAN_STYP() {
        return LOAN_STYP;
    }

    public void setLOAN_STYP(String LOAN_STYP) {
        this.LOAN_STYP = LOAN_STYP;
    }

    public String getSNC_NO() {
        return SNC_NO;
    }

    public void setSNC_NO(String SNC_NO) {
        this.SNC_NO = SNC_NO;
    }

    public String getSNC_DT() {
        return SNC_DT;
    }

    public void setSNC_DT(String SNC_DT) {
        this.SNC_DT = SNC_DT;
    }

    public String getFIN_YR() {
        return FIN_YR;
    }

    public void setFIN_YR(String FIN_YR) {
        this.FIN_YR = FIN_YR;
    }

    public String getACC_MN() {
        return ACC_MN;
    }

    public void setACC_MN(String ACC_MN) {
        this.ACC_MN = ACC_MN;
    }

    public String getACC_TYP() {
        return ACC_TYP;
    }

    public void setACC_TYP(String ACC_TYP) {
        this.ACC_TYP = ACC_TYP;
    }

    public String getSEC_NM() {
        return SEC_NM;
    }

    public void setSEC_NM(String SEC_NM) {
        this.SEC_NM = SEC_NM;
    }

    public String getSAL_MN() {
        return SAL_MN;
    }

    public void setSAL_MN(String SAL_MN) {
        this.SAL_MN = SAL_MN;
    }

    public String getADJ_MN() {
        return ADJ_MN;
    }

    public void setADJ_MN(String ADJ_MN) {
        this.ADJ_MN = ADJ_MN;
    }

    public String getPI_FLG() {
        return PI_FLG;
    }

    public void setPI_FLG(String PI_FLG) {
        this.PI_FLG = PI_FLG;
    }

    public String getRECV_ORD_NO() {
        return RECV_ORD_NO;
    }

    public void setRECV_ORD_NO(String RECV_ORD_NO) {
        this.RECV_ORD_NO = RECV_ORD_NO;
    }

    public String getRECV_ORD_DT() {
        return RECV_ORD_DT;
    }

    public void setRECV_ORD_DT(String RECV_ORD_DT) {
        this.RECV_ORD_DT = RECV_ORD_DT;
    }

    public String getRECV_TYP() {
        return RECV_TYP;
    }

    public void setRECV_TYP(String RECV_TYP) {
        this.RECV_TYP = RECV_TYP;
    }

    public String getPAY_MODE() {
        return PAY_MODE;
    }

    public void setPAY_MODE(String PAY_MODE) {
        this.PAY_MODE = PAY_MODE;
    }

    public String getTR_FIN_YR() {
        return TR_FIN_YR;
    }

    public void setTR_FIN_YR(String TR_FIN_YR) {
        this.TR_FIN_YR = TR_FIN_YR;
    }

    public String getTR_MN() {
        return TR_MN;
    }

    public void setTR_MN(String TR_MN) {
        this.TR_MN = TR_MN;
    }

    public String getTR_CD() {
        return TR_CD;
    }

    public void setTR_CD(String TR_CD) {
        this.TR_CD = TR_CD;
    }

    public String getRMJHCD() {
        return RMJHCD;
    }

    public void setRMJHCD(String RMJHCD) {
        this.RMJHCD = RMJHCD;
    }

    public String getRSMJHCD() {
        return RSMJHCD;
    }

    public void setRSMJHCD(String RSMJHCD) {
        this.RSMJHCD = RSMJHCD;
    }

    public String getRMIHCD() {
        return RMIHCD;
    }

    public void setRMIHCD(String RMIHCD) {
        this.RMIHCD = RMIHCD;
    }

    public String getRSBHCD() {
        return RSBHCD;
    }

    public void setRSBHCD(String RSBHCD) {
        this.RSBHCD = RSBHCD;
    }

    public String getRDTLHCD() {
        return RDTLHCD;
    }

    public void setRDTLHCD(String RDTLHCD) {
        this.RDTLHCD = RDTLHCD;
    }

    public String getRSDTLHCD() {
        return RSDTLHCD;
    }

    public void setRSDTLHCD(String RSDTLHCD) {
        this.RSDTLHCD = RSDTLHCD;
    }

    public String getTVTC_NO() {
        return TVTC_NO;
    }

    public void setTVTC_NO(String TVTC_NO) {
        this.TVTC_NO = TVTC_NO;
    }

    public String getTVTC_DT() {
        return TVTC_DT;
    }

    public void setTVTC_DT(String TVTC_DT) {
        this.TVTC_DT = TVTC_DT;
    }

    public String getTVTC_AMT() {
        return TVTC_AMT;
    }

    public void setTVTC_AMT(String TVTC_AMT) {
        this.TVTC_AMT = TVTC_AMT;
    }

    public String getDDO_CD() {
        return DDO_CD;
    }

    public void setDDO_CD(String DDO_CD) {
        this.DDO_CD = DDO_CD;
    }

    public int getINST_NO() {
        return INST_NO;
    }

    public void setINST_NO(int INST_NO) {
        this.INST_NO = INST_NO;
    }

    public String getAMT_PAID() {
        return AMT_PAID;
    }

    public void setAMT_PAID(String AMT_PAID) {
        this.AMT_PAID = AMT_PAID;
    }

    public String getBASE_HD_CD() {
        return BASE_HD_CD;
    }

    public void setBASE_HD_CD(String BASE_HD_CD) {
        this.BASE_HD_CD = BASE_HD_CD;
    }

    public String getREF_UP_TE_NO() {
        return REF_UP_TE_NO;
    }

    public void setREF_UP_TE_NO(String REF_UP_TE_NO) {
        this.REF_UP_TE_NO = REF_UP_TE_NO;
    }

    public String getREF_ROWID() {
        return REF_ROWID;
    }

    public void setREF_ROWID(String REF_ROWID) {
        this.REF_ROWID = REF_ROWID;
    }

    public String getWHO_USER() {
        return WHO_USER;
    }

    public void setWHO_USER(String WHO_USER) {
        this.WHO_USER = WHO_USER;
    }

    public String getWHO_DT() {
        return WHO_DT;
    }

    public void setWHO_DT(String WHO_DT) {
        this.WHO_DT = WHO_DT;
    }

    public String getAMT_TYP() {
        return AMT_TYP;
    }

    public void setAMT_TYP(String AMT_TYP) {
        this.AMT_TYP = AMT_TYP;
    }

    public String getUNITNO() {
        return UNITNO;
    }

    public void setUNITNO(String UNITNO) {
        this.UNITNO = UNITNO;
    }

    public String getAD_REF_ID() {
        return AD_REF_ID;
    }

    public void setAD_REF_ID(String AD_REF_ID) {
        this.AD_REF_ID = AD_REF_ID;
    }

    public String getIS_VERIFIED() {
        return IS_VERIFIED;
    }

    public void setIS_VERIFIED(String IS_VERIFIED) {
        this.IS_VERIFIED = IS_VERIFIED;
    }

    public String getLOPCAC() {
        return LOPCAC;
    }

    public void setLOPCAC(String LOPCAC) {
        this.LOPCAC = LOPCAC;
    }
    
}
