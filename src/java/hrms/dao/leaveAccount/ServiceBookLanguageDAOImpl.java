/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.leaveAccount;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.model.EmpQuarterAllotment.EmpQuarterBean;
import hrms.model.EnrollmentToInsurance.Enrollmenttoinsurance;
import hrms.model.EquivalentPost.EquivalentPost;
import hrms.model.FTC.sFTCBean;
import hrms.model.LTC.sLTCBean;
import hrms.model.absorption.AbsorptionModel;
import hrms.model.additionalCharge.AdditionalCharge;
import hrms.model.allotmentToCadre.AllotmentToCadreForm;
import hrms.model.allowances.AllowanceModel;
import hrms.model.allowedToOfficiate.AllowedToOfficiateModel;
import hrms.model.brassallot.BrassAllotList;
import hrms.model.conclusionproceedings.ConclusionProceedings;
import hrms.model.confirmationofservice.ConfirmationOfServiceForm;
import hrms.model.departmentalExam.DepartmentalExamForm;
import hrms.model.deputation.DeputationDataForm;
import hrms.model.emppayrecord.EmpPayRecordForm;
import hrms.model.headquarterleaving.HeadQuarterLeaving;
import hrms.model.incrementsanction.IncrementForm;
import hrms.model.joiningCadre.JoiningCadreForm;
import hrms.model.leaveSanction.LeaveSanctionForm;
import hrms.model.loan.Loan;
import hrms.model.loanrelease.LoanRelease;
import hrms.model.notification.NotificationBean;
import hrms.model.payentitlement.PayEntitlementForm;
import hrms.model.payfixation.PayFixation;
import hrms.model.placementofservice.PlacementOfServiceForm;
import hrms.model.posting.PostingForm;
import hrms.model.promotion.PromotionForm;
import hrms.model.punishment.PunishmentBean;
import hrms.model.recruitment.RecruitmentModel;
import hrms.model.redeployment.Redeployment;
import hrms.model.redesignation.Redesignation;
import hrms.model.regularizeService.RegularizeServiceContractualForm;
import hrms.model.regularizeService.RegularizeServiceForm;
import hrms.model.reinstatement.Reinstatement;
import hrms.model.relieveCadre.RelieveCadreForm;
import hrms.model.repatriation.RepatriationForm;
import hrms.model.repayment.LoanRepayment;
import hrms.model.reward.Reward;
import hrms.model.servicecloser.Retirement;
import hrms.model.servicerecord.ServiceRecordForm;
import hrms.model.suspension.Suspension;
import hrms.model.trainingschedule.TrainingSchedule;
import hrms.model.transfer.TransferForm;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Surendra
 */
public class ServiceBookLanguageDAOImpl implements ServiceBookLanguageDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public String getSuspDetails(Suspension sfb) {
        StringBuffer outputSuspDetails = new StringBuffer();

        String wefDate = null;
        String wefTime = null;
        String amount = null;
        String hqCode = null;
        String hqName = null;
        String hqfix = null;
        String authName = null;
        String offName = null;
        String deptName = null;
        String othSpn = "";
        String notficationString = "";
        String proNoun = null;

        Connection con = null;
        PreparedStatement ps = null;

        try {

            con = dataSource.getConnection();

            proNoun = getPronoun(sfb.getEmpid());
            //othSpn = getOtherSPN(sfb.getTxtothDeptName(), sfb.getTxtothOffName(), sfb.getTxtothAuthName());
            if (!othSpn.equals("")) {
                authName = othSpn;
            } else {
                if (sfb.getHidTempAuthSpc() != null && !sfb.getHidTempAuthSpc().equals("")) {
                    authName = getSPN(sfb.getHidTempAuthSpc());
                } else if (sfb.getHidTempAuthOffice() != null && !sfb.getHidTempAuthOffice().equals("")) {
                    offName = getOffName(sfb.getHidTempAuthOffice());
                } else if (sfb.getSltDept() != null && !sfb.getSltDept().equals("")) {
                    deptName = getDeptName(sfb.getSltDept());
                }
            }
            notficationString = getNotificationString(sfb.getOrdno(), sfb.getOrdDate(), deptName, offName, authName);
            if (sfb.getWefdate() != null && !sfb.getWefdate().equals("")) {
                wefDate = sfb.getWefdate().toUpperCase();
            }
            if (sfb.getSltweftime() != null && !sfb.getSltweftime().trim().equals("")) {
                wefTime = sfb.getSltweftime().toUpperCase();
            }
            if (sfb.getTxtallowance() != null && !sfb.getTxtallowance().trim().equals("0") && !sfb.getTxtallowance().trim().equals("")) {
                amount = sfb.getTxtallowance();
            }
            if (sfb.getSlthqoffice() != null && !sfb.getSlthqoffice().trim().equals("")) {
                hqCode = sfb.getSlthqoffice();
            }

            /*if (sfb.getHidsusptype() != null && !sfb.getHidsusptype().trim().equals("SUSPENSION")) {
             hqfix = "Y";
             }*/
            if (hqCode != null) {
                hqName = getOffName(hqCode);
            }
            String tempPronoun = null;
            if (proNoun.trim().equalsIgnoreCase("HE")) {
                tempPronoun = " HIS ";
            } else if (proNoun.trim().equalsIgnoreCase("SHE")) {
                tempPronoun = " HER ";
            } else {
                tempPronoun = " HIS/ HER ";
            }

            if (hqfix != null && !hqfix.equals("")) {

                outputSuspDetails.append("HEAD QUARTER IS FIXED ");
                if (hqName != null) {
                    outputSuspDetails.append(" AT ").append(hqName).append(" DURING THE PERIOD OF ").append(tempPronoun).append(" SUSPENSION.");
                }

                if (wefDate != null) {
                    outputSuspDetails.append(" WEF ").append(wefDate);
                }
                if (wefTime != null) {
                    outputSuspDetails.append(", ").append(wefTime);
                }

                if (notficationString != null && !notficationString.equals("")) {
                    outputSuspDetails.append(notficationString);
                }

                outputSuspDetails.append(".");

                if (amount != null) {
                    outputSuspDetails.append(" ").append(proNoun).append(" IS ALLOWED TO DRAW SUSPENSION ALLOWANCE OF RS.").append(CommonFunctions.formatNumber(amount)).append("/-.");
                }
            } else {
                outputSuspDetails.append("PLACED UNDER SUSPENSION ");
                if (wefDate != null) {
                    outputSuspDetails.append(" WEF ").append(wefDate);
                }
                if (wefTime != null) {
                    outputSuspDetails.append(", ").append(wefTime);
                }

                if (notficationString != null && !notficationString.equals("")) {
                    outputSuspDetails.append(notficationString);
                }

                outputSuspDetails.append(".");

                if (amount != null) {
                    outputSuspDetails.append(" ").append(proNoun).append(" IS ALLOWED TO DRAW SUSPENSION ALLOWANCE OF RS.").append(CommonFunctions.formatNumber(amount)).append("/-.");
                }
                if (hqName != null) {
                    outputSuspDetails.append(" ").append(tempPronoun).append(" HEAD QUARTER IS FIXED AT ").append(hqName).append(" DURING THE PERIOD OF ").append(tempPronoun).append(" SUSPENSION.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }

        return outputSuspDetails.toString();
    }

    @Override
    public String getPayFixationDetails(PayFixation prf, int notid, String payType) {
        StringBuffer payRev = new StringBuffer();
        ResultSet rsInside = null;
        ResultSet rs2 = null;
        Statement st = null;
        double rBasic = 0;
        String rScale = null;
        String wefd = null;
        String weft = null;
        double gradePay = 0;
        double perPay = 0;
        double sPay = 0;
        double othPay = 0;
        String otherDesc = null;
        String doni = null;
        String payRevId = null;
        String superNote = null;
        String authName = null;
        String offName = null;
        String deptName = null;
        String othSpn = "";
        String notficationString = "";
        String proNoun = null;

        Connection con = null;
        try {

            con = dataSource.getConnection();
            st = con.createStatement();

            othSpn = getOtherSpn(prf.getHidNotifyingOthSpc());

            proNoun = getPronoun(prf.getEmpid());

            if (!othSpn.equals("")) {
                authName = othSpn;
            } else {
                if (prf.getNotifyingSpc() != null && !prf.getNotifyingSpc().equals("")) {
                    authName = getSPN(prf.getNotifyingSpc());
                } else if (prf.getHidNotifyingOffCode() != null && !prf.getHidNotifyingOffCode().equals("")) {
                    offName = getOffName(prf.getHidNotifyingOffCode());
                } else if (prf.getHidNotifyingDeptCode() != null && !prf.getHidNotifyingDeptCode().equals("")) {
                    deptName = getDeptName(prf.getHidNotifyingDeptCode());
                }
            }
            notficationString = getNotificationString(prf.getTxtNotOrdNo(), prf.getTxtNotOrdDt(), deptName, offName, authName);
            ///This query is for getting the Pay Details from EMP_PAY_RECORD Table

            if (!prf.getTxtBasic().equals("") && !prf.getTxtBasic().equals("0") && prf.getTxtBasic() != null)//EMP_PAY_RECORD
            {
                rBasic = Double.parseDouble(prf.getTxtBasic());
            }

            if (!prf.getTxtGP().equals("") && !prf.getTxtGP().equals("0") && prf.getTxtGP() != null)//EMP_PAY_RECORD
            {
                gradePay = Double.parseDouble(prf.getTxtGP());
            }

            if (!prf.getTxtSP().equals("") && !prf.getTxtSP().equals("0") && prf.getTxtSP() != null)//EMP_PAY_RECORD
            {
                sPay = Double.parseDouble(prf.getTxtSP());
            }

            if (!prf.getTxtPP().equals("") && !prf.getTxtPP().equals("0") && prf.getTxtPP() != null)//EMP_PAY_RECORD
            {
                perPay = Double.parseDouble(prf.getTxtPP());
            }
            if (!prf.getTxtOP().equals("") && !prf.getTxtOP().equals("0") && prf.getTxtOP() != null)//EMP_PAY_RECORD
            {
                othPay = Double.parseDouble(prf.getTxtOP());
            }
            if (prf.getTxtDescOP() != null && !prf.getTxtDescOP().equalsIgnoreCase(""))//EMP_PAY_RECORD
            {
                otherDesc = prf.getTxtDescOP().toUpperCase();
            }
            if (prf.getSltPayScale() != null && !prf.getSltPayScale().equalsIgnoreCase(""))//EMP_PAY_RECORD
            {
                rScale = prf.getSltPayScale().toUpperCase();//EMP_PAY_RECORD
            }

            if (prf.getSltWEFTime() != null && !prf.getSltWEFTime().equals(""))//EMP_PAY_RECORD
            {
                weft = prf.getSltWEFTime().toUpperCase();
            }
            if (prf.getTxtWEFDt() != null && !prf.getTxtWEFDt().equalsIgnoreCase(""))//EMP_PAY_RECORD
            {
                wefd = prf.getTxtWEFDt().toUpperCase();//EMP_PAY_RECORD
            }

            ///This query is for getting the Notication Details from EMP_NOTIFICATION Table
            ///This query is for getting the Next Increment Date from EMP_PAY Table  
            rs2 = st.executeQuery("SELECT * FROM EMP_PAY WHERE EMP_ID='" + prf.getEmpid() + "' AND NOT_ID='" + notid + "'");
            while (rs2.next()) {
                payRevId = rs2.getString("PRID");
                if (rs2.getDate("DONI") != null && !rs2.getString("DONI").equals(""))//EMP_NOTIFICATION
                {
                    doni = CommonFunctions.getFormattedOutputDate1(rs2.getDate("DONI"));
                }
            }
            DataBaseFunctions.closeSqlObjects(rs2);

            if (payType.equals("PAYREVISION")) {
                payRev.append(" PAY REVISED ");

                if (rBasic != 0) {
                    payRev.append(" @ Rs.").append(CommonFunctions.formatNumber(rBasic)).append("/- PM ");
                }

                if (gradePay != 0) {
                    payRev.append("WITH GRADE PAY Rs.").append(CommonFunctions.formatNumber(gradePay)).append("/- PM ");
                }
                if (wefd != null && !wefd.equals("")) {
                    payRev.append(" WEF ").append(wefd);
                }
                if (rScale != null) {
                    payRev.append(" IN THE REVISED SCALE OF PAY OF RS. ").append(rScale).append("/-");
                } else if (prf.getPayCell() != null && !prf.getPayCell().equals("")) {
                    payRev.append(" IN CELL " + prf.getPayCell() + " OF LEVEL " + prf.getPayLevel() + " ");
                }

            } else if (payType.equals("PAYFIXATION")) {
                payRev.append(" PAY FIXED ");
                if (rBasic != 0) {
                    payRev.append(" @ Rs.").append(CommonFunctions.formatNumber(rBasic)).append("/- PM ");
                }
                if (gradePay != 0) {
                    payRev.append("WITH GRADE PAY Rs.").append(CommonFunctions.formatNumber(gradePay)).append("/- PM ");
                }
                if (wefd != null && !wefd.equals("")) {
                    payRev.append(" WEF ").append(wefd);
                }
                if (rScale != null) {
                    payRev.append(" IN THE SCALE OF PAY OF RS. ").append(rScale).append("/-");
                } else if (prf.getPayCell() != null && !prf.getPayCell().equals("")) {
                    payRev.append(" IN CELL " + prf.getPayCell() + " OF LEVEL " + prf.getPayLevel() + " ");
                }

                if (prf.getSltPayFixationReason() != null) {
                    payRev.append(" ").append(" HAVING REASON ").append(prf.getSltPayFixationReason());
                }
            } else if (payType.equals("STEPUP")) {
                payRev.append(" PAY STEPED UP ");
                if (rBasic != 0) {
                    payRev.append(" AND FIXED AT ").append(" Rs.").append(CommonFunctions.getAbsoluteNumber(rBasic)).append("/- PM ");
                }
                if (gradePay != 0) {
                    payRev.append("WITH GRADE PAY Rs.").append(CommonFunctions.formatNumber(gradePay)).append("/- PM ");
                }

                if (wefd != null && !wefd.equals("")) {
                    payRev.append(" WEF ").append(wefd);
                }
                if (rScale != null) {
                    payRev.append(" IN THE SCALE OF PAY OF RS. ").append(rScale).append("/-");
                } else if (prf.getPayCell() != null && !prf.getPayCell().equals("")) {
                    payRev.append(" IN CELL " + prf.getPayCell() + " OF LEVEL " + prf.getPayLevel() + " ");
                }
            } else {
                payRev.append(" PAY FIXED ");
                if (rBasic != 0) {
                    payRev.append(" @ Rs.").append(CommonFunctions.formatNumber(rBasic)).append("/- PM ");
                }
                if (gradePay != 0) {
                    payRev.append("WITH GRADE PAY Rs.").append(CommonFunctions.formatNumber(gradePay)).append("/- PM ");
                }

                if (wefd != null && !wefd.equals("")) {
                    payRev.append(" WEF ").append(wefd);
                }
                if (rScale != null) {
                    payRev.append(" IN THE SCALE OF PAY OF RS. ").append(rScale).append("/-");
                } else if (prf.getPayCell() != null && !prf.getPayCell().equals("")) {
                    payRev.append(" IN CELL " + prf.getPayCell() + " OF LEVEL " + prf.getPayLevel() + " ");
                }
            }
            if (sPay != 0) {
                payRev.append(" + SPECIAL PAY @ RS. ").append(CommonFunctions.formatNumber(sPay)).append("/- ");
            }
            if (perPay != 0) {
                payRev.append(" + PERSONAL PAY @ RS. ").append(CommonFunctions.formatNumber(perPay)).append("/- ");
            }
            if (otherDesc != null && !otherDesc.equalsIgnoreCase("")) {
                if (othPay != 0) {
                    payRev.append(" + ").append(otherDesc).append(" @ RS. ").append(CommonFunctions.formatNumber(othPay)).append("/- ");
                }
            } else {
                if (othPay != 0) {
                    payRev.append(" + OTHER PAY @ RS. ").append(CommonFunctions.formatNumber(othPay)).append("/- ");
                }
            }
            if (sPay != 0 || perPay != 0 || othPay != 0) {
                payRev.append(" PM ");
            }

            if (notficationString != null && !notficationString.equalsIgnoreCase("")) {
                payRev.append(notficationString);
            }

            if (!payRev.toString().trim().equals("")) {
                payRev.append(".");
            }

            String tempPronoun = null;
            if (proNoun.trim().equalsIgnoreCase("HE")) {
                tempPronoun = " HIS ";
            } else if (proNoun.trim().equalsIgnoreCase("SHE")) {
                tempPronoun = " HER ";
            } else {
                tempPronoun = " HIS/ HER ";
            }
            if (doni != null) {
                payRev.append(" ").append(tempPronoun).append(" DATE OF NEXT INCREMENT WILL FALL DUE ON ").append(doni).append(".");
            }
            //////////////////////////////
            ArrayList basicPay = new ArrayList();
            ArrayList gradPay = new ArrayList();
            ArrayList wefDate = new ArrayList();
            ArrayList otherPay = new ArrayList();
            ArrayList personalPay = new ArrayList();
            ArrayList specialPay = new ArrayList();
            ArrayList otherPayDesc = new ArrayList();
            ArrayList payscale = new ArrayList();
            ArrayList paylevel = new ArrayList();
            ArrayList paycell = new ArrayList();
            String orderIncrNo = null;
            String orderIncrDate = null;
            String queryIncrment = "SELECT * FROM (SELECT E.*,F.INCRID,F.PRID,F.INCR,F.FIRST_INCR,F.second_incr,F.third_incr,F.fourth_incr,F.incr_type FROM EMP_NOTIFICATION E,"
                    + "EMP_INCR F WHERE E.NOT_TYPE='INCREMENT' AND E.EMP_ID='" + prf.getEmpid() + "' AND E.NOT_ID=F.NOT_ID and F.PRID='" + payRevId + "')  T,"
                    + "EMP_PAY_RECORD R WHERE T.NOT_TYPE='INCREMENT' AND T.EMP_ID='" + prf.getEmpid() + "' AND T.NOT_ID=R.NOT_ID";
            rsInside = st.executeQuery(queryIncrment);
            while (rsInside.next()) {
                if (rsInside.getDate("wef") != null && !rsInside.getDate("wef").equals("")) {
                    wefDate.add(CommonFunctions.getFormattedOutputDate1(rsInside.getDate("wef")));
                }
                personalPay.add(rsInside.getString("p_pay"));
                otherPay.add(rsInside.getString("oth_pay"));
                otherPayDesc.add(rsInside.getString("oth_desc"));
                basicPay.add(rsInside.getString("pay"));
                gradPay.add(rsInside.getString("gp"));
                specialPay.add(rsInside.getString("s_pay"));
                payscale.add(rsInside.getString("pay_scale"));
                paylevel.add(rsInside.getString("pay_level"));
                paycell.add(rsInside.getString("pay_cell"));
                if (rsInside.getString("ORDNO") != null && !rsInside.getString("ORDNO").trim().equals("")) {
                    orderIncrNo = rsInside.getString("ORDNO");
                }
                if (rsInside.getDate("ORDDT") != null && !rsInside.getString("ORDDT").equalsIgnoreCase("")) {
                    orderIncrDate = CommonFunctions.getFormattedOutputDate1(rsInside.getDate("ORDDT"));
                }
            }
            DataBaseFunctions.closeSqlObjects(rsInside, st);

            String tempProNoun1 = null;
            if (proNoun.trim().equalsIgnoreCase("HE")) {
                tempProNoun1 = " HIS ";
            } else if (proNoun.trim().equalsIgnoreCase("SHE")) {
                tempProNoun1 = " HER ";
            } else {
                tempProNoun1 = " HIS/ HER ";
            }
            if (wefDate != null) {
                if ((wefDate.size() > 0) || (basicPay != null && basicPay.size() > 0)) {
                    if (wefDate.size() == 1) {
                        if (wefDate.get(0) != null && !wefDate.get(0).toString().trim().equals("")) {
                            payRev.append(" ").append(proNoun).append(" IS ALLOWED TO DRAW ").append(tempProNoun1).append(" NEXT INCREMENT WEF ").append(wefDate.get(0));
                        }
                        if (basicPay != null) {
                            if (basicPay.get(0) != null && !basicPay.get(0).toString().trim().equals("")) {
                                payRev.append(" RAISING PAY TO Rs. ").append(CommonFunctions.formatNumber(basicPay.get(0).toString())).append("/- ");
                            }
                        }
                        if (paylevel != null) {
                            payRev.append(" IN CELL " + paycell.get(0) + " OF LEVEL " + paylevel.get(0) + " ");
                        } else {
                            if (gradPay != null) {
                                payRev.append("WITH GRADE PAY Rs.").append(gradPay.get(0)).append("/- PM ");
                            }
                            if (payscale != null) {
                                payRev.append(" IN THE SCALE OF PAY OF RS. ").append(payscale.get(0)).append("/-");
                            }
                        }
                        if (orderIncrNo != null) {
                            payRev.append(" VIDE ORDER NO. ").append(orderIncrNo);
                            if (orderIncrDate != null) {
                                payRev.append(" DATED ").append(orderIncrDate).append(".");
                            }
                        } else if (orderIncrDate != null) {
                            payRev.append("VIDE OFFICE ORDER, DATED ").append(orderIncrDate).append(".");
                        }

                    } else if (wefDate.size() == 2) {
                        if (wefDate.get(0) != null && !wefDate.get(0).toString().equals("")) {
                            payRev.append(" ").append(proNoun).append(" IS ALLOWED TO DRAW ").append(tempProNoun1).append(" NEXT & SUBSEQUENT INCREMENTS WEF ").append(wefDate.get(0));
                        }
                        if (wefDate.get(1) != null && !wefDate.get(1).toString().equals("")) {
                            payRev.append(" & ").append(wefDate.get(1));
                        }
                        if (basicPay.get(0) != null && !basicPay.get(0).toString().equals("")) {
                            payRev.append(" RAISING PAY TO Rs. ").append(CommonFunctions.formatNumber(basicPay.get(0).toString())).append("/- ");
                        }
                        if (basicPay.get(1) != null && !basicPay.get(1).toString().equals("")) {
                            payRev.append(" & Rs.").append(CommonFunctions.formatNumber(basicPay.get(1).toString())).append("/- RESPECTIVELY ");
                        }
                        if (paylevel.get(0) != null) {
                            payRev.append(" IN CELL " + paycell.get(0) + " OF LEVEL " + paylevel.get(0) + " ");
                        } else {
                            if (gradPay.get(0) != null) {
                                payRev.append("WITH GRADE PAY Rs.").append(gradPay.get(0)).append("/- PM ");
                            }
                            if (payscale.get(0) != null) {
                                payRev.append(" IN THE SCALE OF PAY OF RS. ").append(payscale.get(0)).append("/-");
                            }
                        }
                        if (paylevel.get(1) != null) {
                            payRev.append(" IN CELL " + paycell.get(1) + " OF LEVEL " + paylevel.get(1) + " ");
                        } else {
                            if (gradPay.get(1) != null) {
                                payRev.append("WITH GRADE PAY Rs.").append(gradPay.get(1)).append("/- PM ");
                            }
                            if (payscale.get(1) != null) {
                                payRev.append(" IN THE SCALE OF PAY OF RS. ").append(payscale.get(1)).append("/-");
                            }
                        }
                        if (orderIncrNo != null) {
                            payRev.append(" VIDE ORDER NO. ").append(orderIncrNo);
                            if (orderIncrDate != null) {
                                payRev.append(" DATED ").append(orderIncrDate).append(".");
                            }
                        } else if (orderIncrDate != null) {
                            payRev.append("VIDE OFFICE ORDER, DATED ").append(orderIncrDate).append(".");
                        }
                    } else if (wefDate.size() > 2) {
                        payRev.append(" ").append(proNoun).append(" IS ALLOWED TO DRAW ").append(tempProNoun1).append(" NEXT & SUBSEQUENT INCREMENTS WEF ");
                        for (int i = 0; i < wefDate.size(); i++) {
                            if (wefDate.get(i) != null) {
                                payRev.append(wefDate.get(i));
                                if (i == wefDate.size() - 2) {
                                    payRev.append(" & ");
                                } else if (i < wefDate.size() - 2) {
                                    payRev.append(", ");
                                }
                            }
                        }
                        payRev.append(" RASIING ").append(tempProNoun1).append(" PAY TO ");
                        for (int i = 0; i < basicPay.size(); i++) {
                            if (basicPay.get(i) != null) {
                                payRev.append(" RS. ").append(CommonFunctions.formatNumber(basicPay.get(i).toString())).append("/- PM ");
                                if (i == basicPay.size() - 2) {
                                    payRev.append(" & ");
                                } else if (i < basicPay.size() - 2) {
                                    payRev.append(", ");
                                }
                            }
                        }
                        if (paylevel != null) {
                            for (int i = 0; i < paylevel.size(); i++) {
                                payRev.append(" IN CELL " + paycell.get(i) + " OF LEVEL " + paylevel.get(i) + " ");
                                if (i == paylevel.size() - 2) {
                                    payRev.append(" & ");
                                } else if (i < paylevel.size() - 2) {
                                    payRev.append(", ");
                                }
                            }
                        }
                        if (orderIncrNo != null) {
                            payRev.append(" VIDE ORDER NO. ").append(orderIncrNo);
                            if (orderIncrDate != null) {
                                payRev.append(" DATED ").append(orderIncrDate).append(".");
                            }
                        } else if (orderIncrDate != null) {
                            payRev.append("VIDE OFFICE ORDER, DATED ").append(orderIncrDate).append(".");
                        }
                    }
                }
            }
            /////////////////////////////////
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs2);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return payRev.toString();
    }

    public String getTrainingTitleusingtraingsl(String training_type_id) {

        ResultSet res = null;
        String type = "";
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = this.dataSource.getConnection();
            String sql = "";

            ps = con.prepareStatement("SELECT TRAINING_TITLE FROM G_TRAINING_TITLE WHERE trainingsl=?");
            ps.setInt(1, Integer.parseInt(training_type_id));
            res = ps.executeQuery();
            if (res.next()) {
                type = res.getString("TRAINING_TITLE");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, ps, con);
        }
        return type;
    }

    @Override
    public String getTrainingDetails(TrainingSchedule trinput) {
        StringBuffer outputTraining = new StringBuffer();
        String trainingtitle = null;
        String sDate = null;
        String cDate = null;
        String title = null;
        String coOrdinator = null;
        String place = null;
        String ifUndWent = "";
        try {
            if (trinput.getStartDate() != null && !trinput.getStartDate().equalsIgnoreCase("")) {
                sDate = trinput.getStartDate().toUpperCase();
            }
            if (trinput.getComplDate() != null && !trinput.getComplDate().equalsIgnoreCase("")) {
                cDate = trinput.getComplDate().toUpperCase();
            }

            /* if (trinput.getSlttraintype().equals("OTHER_TR")) {
             title = trinput.getTxtTraingTitl().toUpperCase();
             } else { */
            if (trinput.getSlttitle() != null && !trinput.getSlttitle().trim().equals("")) {
                String[] str = trinput.getSlttitle().split("-");
                trainingtitle = str[1];
                title = getTrainingTitleusingtraingsl(trainingtitle.toUpperCase());
                //System.out.println("title:" + title);
            }

            if (trinput.getTxtcoordinator() != null && !trinput.getTxtcoordinator().equals("")) {
                coOrdinator = trinput.getTxtcoordinator().toUpperCase();
            }
            if (trinput.getTxttrplace() != null && !trinput.getTxttrplace().equals("")) {
                place = trinput.getTxttrplace().toUpperCase();
            }
            if (trinput.getUnderwent() != null && !trinput.getUnderwent().trim().equals("")) {
                ifUndWent = trinput.getUnderwent();
            }

            if (ifUndWent != null) {
                if (ifUndWent.trim().equalsIgnoreCase("U")) {
                    //outputTraining = proNoun+" UNDERWENT THE TRAINING ON";
                    outputTraining.append(" UNDERWENT ");
                } else if (ifUndWent.trim().equalsIgnoreCase("A")) {
                    outputTraining.append(" ALLOWED TO ATTEND ");
                } else {
                    outputTraining.append(" UNDERWENT ");
                }
            }
            if (title != null) {
                outputTraining.append(title);
            }

            if (sDate != null && cDate != null) {
                if (!sDate.equalsIgnoreCase(cDate)) {
                    outputTraining.append(" BETWEEN ").append(sDate).append(" AND ").append(cDate);
                } else {
                    outputTraining.append(" ON ").append(sDate);
                }
            } else if (sDate == null && cDate != null) {
                outputTraining.append(" ON ").append(cDate);
            } else if (sDate != null && cDate == null) {
                outputTraining.append(" ON ").append(sDate);
            }
            if (coOrdinator != null) {
                outputTraining.append(" COORDINATED BY ").append(coOrdinator);
            }
            if (place != null) {
                outputTraining.append(" AT ").append(place);
            }
            if (outputTraining.toString().trim().equalsIgnoreCase("ALLOWED TO ATTEND") || outputTraining.toString().trim().equalsIgnoreCase("UNDERWENT")) {
                outputTraining.append("TRAINING").toString().trim();
            }
            if (trinput.getTxtOrdNo() != null) {
                outputTraining.append(" VIDE ORDER NO. ").append(trinput.getTxtOrdNo());
                if (trinput.getTxtOrdDt() != null) {
                    outputTraining.append(" DATED ").append(trinput.getTxtOrdDt()).append(".");
                }
            } else if (trinput.getTxtOrdDt() != null) {
                outputTraining.append("VIDE OFFICE ORDER, DATED ").append(trinput.getTxtOrdDt()).append(".");
            }
            outputTraining = outputTraining.append(".");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return outputTraining.toString().toUpperCase();
    }

    @Override
    public String getLeaveSanctionDetails(int lvid, String empid, String othspc) {
        StringBuffer outputLeaveDetails = new StringBuffer();
        ResultSet rs = null;
        ResultSet rsLeave = null;
        String leaveSnacSurrender = "";
        String leaveType = null;
        String leaveName = null;
        String noSanctionDays = null;
        String noSurrenderDays = null;
        String fDate = null;
        String tDate = null;
        String tempFDate = null;
        String tempTDate = null;
        String sYear = null;
        String tYear = null;
        String suffixDate = null;
        String prefixDate = null;
        String suffixFrom = null;
        String prefixTo = null;
        String superNote = null;
        String authName = null;
        String offName = null;
        String deptName = null;
        String authCode = null;
        String offCode = null;
        String deptCode = null;
        String othSpn = "";
        String orderDate = null;
        String orderNo = null;
        String notficationString = "";
        String proNoun = null;
        String notid = null;
        String ifcommuted = null;
        String ifLongTermLeave = null;
        String onmedical = null;
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = dataSource.getConnection();
            proNoun = getPronoun(empid);

            ps = con.prepareStatement("SELECT * FROM (SELECT * FROM EMP_LEAVE WHERE LEAVEID=? and EMP_ID=?) EMPLEAVE "
                    + " INNER JOIN EMP_NOTIFICATION ON  EMPLEAVE.NOT_ID = EMP_NOTIFICATION.NOT_ID");
            ps.setInt(1, lvid);
            ps.setString(2, empid);
            rsLeave = ps.executeQuery();
            while (rsLeave.next()) {

                if (rsLeave.getString("NOT_ID") != null && !rsLeave.getString("NOT_ID").equals("")) {
                    notid = rsLeave.getString("NOT_ID");
                }
                if (rsLeave.getString("LSOT_ID") != null && !rsLeave.getString("LSOT_ID").equals("")) {
                    leaveSnacSurrender = rsLeave.getString("LSOT_ID");
                }
                if (rsLeave.getString("TOL_ID") != null && !rsLeave.getString("TOL_ID").equals("")) {
                    leaveType = rsLeave.getString("TOL_ID");
                }
                if (rsLeave.getString("IF_LONGTERM") != null && !rsLeave.getString("IF_LONGTERM").equals("")) {
                    ifLongTermLeave = rsLeave.getString("IF_LONGTERM");
                }
                if (rsLeave.getString("S_DAYS") != null && !rsLeave.getString("S_DAYS").equals("")) {
                    noSurrenderDays = rsLeave.getString("S_DAYS");
                }
                if (rsLeave.getString("S_YEAR") != null && !rsLeave.getString("S_YEAR").equals("")) {
                    sYear = rsLeave.getString("S_YEAR");
                }
                if (rsLeave.getString("T_YEAR") != null && !rsLeave.getString("T_YEAR").equals("")) {
                    tYear = rsLeave.getString("T_YEAR");
                }
                if (rsLeave.getDate("FDATE") != null && !rsLeave.getString("FDATE").equals("")) {
                    tempFDate = rsLeave.getString("FDATE");
                    fDate = CommonFunctions.getFormattedOutputDate1(rsLeave.getDate("FDATE"));
                }
                if (rsLeave.getDate("TDATE") != null && !rsLeave.getString("TDATE").equals("")) {
                    tempTDate = rsLeave.getString("TDATE");
                    tDate = CommonFunctions.getFormattedOutputDate1(rsLeave.getDate("TDATE"));
                }
                if (rsLeave.getDate("SUFFIX_DATE") != null && !rsLeave.getString("SUFFIX_DATE").equals("")) {
                    suffixDate = CommonFunctions.getFormattedOutputDate1(rsLeave.getDate("SUFFIX_DATE"));
                }
                if (rsLeave.getDate("PREFIX_TO") != null && !rsLeave.getString("PREFIX_TO").equals("")) {
                    prefixTo = CommonFunctions.getFormattedOutputDate1(rsLeave.getDate("PREFIX_TO"));
                }
                if (rsLeave.getDate("SUFFIX_FROM") != null && !rsLeave.getString("SUFFIX_FROM").equals("")) {
                    suffixFrom = CommonFunctions.getFormattedOutputDate1(rsLeave.getDate("SUFFIX_FROM"));
                }
                if (rsLeave.getDate("PREFIX_DATE") != null && !rsLeave.getString("PREFIX_DATE").equals("")) {
                    prefixDate = CommonFunctions.getFormattedOutputDate1(rsLeave.getDate("PREFIX_DATE"));
                }
                if (rsLeave.getString("ORDNO") != null && !rsLeave.getString("ORDNO").equals("")) {
                    orderNo = rsLeave.getString("ORDNO").toUpperCase();
                }
                if (rsLeave.getString("ORDDT") != null && !rsLeave.getString("ORDDT").equals("")) {
                    orderDate = CommonFunctions.getFormattedOutputDate1(rsLeave.getDate("ORDDT"));
                }
                if (rsLeave.getString("DEPT_CODE") != null && !rsLeave.getString("DEPT_CODE").equals("")) {
                    deptCode = rsLeave.getString("DEPT_CODE").toUpperCase();
                }
                if (rsLeave.getString("OFF_CODE") != null && !rsLeave.getString("OFF_CODE").equals("")) {
                    offCode = rsLeave.getString("OFF_CODE").toUpperCase();
                }
                if (rsLeave.getString("AUTH") != null && !rsLeave.getString("AUTH").equals("")) {
                    authCode = rsLeave.getString("AUTH").toUpperCase();
                }
                if (rsLeave.getString("IF_MEDICAL") != null && !rsLeave.getString("IF_MEDICAL").equals("")) {
                    onmedical = rsLeave.getString("IF_MEDICAL").toUpperCase();
                }
                if (rsLeave.getString("IF_COMMUTED") != null && !rsLeave.getString("IF_COMMUTED").equals("")) {
                    ifcommuted = rsLeave.getString("IF_COMMUTED").toUpperCase();
                }
            }

            if (tDate != null && fDate != null) {
                ps = con.prepareStatement(" SELECT DATE_PART('day', '" + tempTDate + "'::timestamp - '" + tempFDate + "'::timestamp)+1 DIFFERENCE");
                rs = ps.executeQuery();
                if (rs.next()) {
                    noSanctionDays = rs.getString("DIFFERENCE");
                }
            }
            //                          Closing ResultSet Object
            DataBaseFunctions.closeSqlObjects(rs, ps);

            if (authCode != null && !authCode.equals("")) {
                authName = getSPN(authCode);
            }
            if (offCode != null && !offCode.equals("")) {
                offName = getOffName(offCode);
            }
            if (deptCode != null && !deptCode.equals("")) {
                deptName = getDeptName(deptCode);
            }
            if (othspc != null && !othspc.equals("")) {
                othSpn = getOtherSpn(othspc);
                authName = othSpn;
            }
            notficationString = getNotificationString(orderNo, orderDate, deptName, offName, authName);
            if (notid != null) {
                //superNote = ServiceHistoryData.getSupersseionNote(notid, st);
            }
            if (superNote != null) {
                outputLeaveDetails.append(superNote);
            }

            if (leaveSnacSurrender.equalsIgnoreCase("01")) {

                if (leaveType != null && !leaveType.trim().equals("")) {
                    leaveName = getLeaveType(leaveType);
                }
                outputLeaveDetails.append(" GRANTED ");
                if (noSanctionDays != null && !noSanctionDays.trim().equals("")) {
                    if (Integer.parseInt(noSanctionDays) == 1) {
                        outputLeaveDetails.append(noSanctionDays).append(" DAY ");
                    } else {
                        outputLeaveDetails.append(noSanctionDays).append(" DAYS ");
                    }
                }
                if (leaveName != null) {
                    outputLeaveDetails.append(leaveName);
                }
                if (ifcommuted != null && !ifcommuted.equals("")) {
                    if (ifcommuted.equalsIgnoreCase("Y")) {
                        outputLeaveDetails.append("(COMMUTED)");
                    }
                }
                if (onmedical != null && !onmedical.equals("")) {
                    if (onmedical.equalsIgnoreCase("Y")) {
                        outputLeaveDetails.append(" ON MEDICAL GROUND ");
                    }
                }
                if (noSanctionDays != null && !noSanctionDays.trim().equals("")) {
                    if (Integer.parseInt(noSanctionDays) == 1) {
                        if (fDate != null) {
                            outputLeaveDetails.append(" ON ").append(fDate);
                        }
                    } else {
                        if (fDate != null) {
                            outputLeaveDetails.append(" FROM ").append(fDate);
                        }
                        if (tDate != null) {
                            outputLeaveDetails.append(" TO ").append(tDate);
                        }
                    }
                }
                boolean flagchkPrefix = false;
                if (prefixDate != null && prefixTo != null) {
                    flagchkPrefix = true;
                    outputLeaveDetails.append(" WITH PREFIX FROM ").append(prefixDate).append(" TO ").append(prefixTo);
                } else if (prefixDate != null && prefixTo == null) {
                    flagchkPrefix = true;
                    outputLeaveDetails.append(" WITH  PREFIX ON ").append(prefixDate);
                } else if (prefixDate == null && prefixTo != null) {
                    flagchkPrefix = true;
                    outputLeaveDetails.append(" WITH PREFIX ON ").append(prefixTo);
                }

                if (flagchkPrefix == true && suffixFrom != null && suffixDate != null) {
                    outputLeaveDetails.append(" AND  SUFFIX FROM ").append(suffixFrom).append(" TO ").append(suffixDate);
                } else if (flagchkPrefix == true && suffixFrom != null && suffixDate == null) {
                    outputLeaveDetails.append(" AND SUFFIX ON ").append(suffixFrom);
                } else if (flagchkPrefix == true && suffixFrom == null && suffixDate != null) {
                    outputLeaveDetails.append(" AND SUFFIX ON ").append(suffixDate);
                } else if (flagchkPrefix == false && suffixFrom != null && suffixDate != null) {
                    outputLeaveDetails.append(" WITH SUFFIX FROM ").append(suffixFrom).append(" TO ").append(suffixDate);
                } else if (flagchkPrefix == false && suffixFrom != null && suffixDate == null) {
                    outputLeaveDetails.append(" WITH SUFFIX ON ").append(suffixFrom);
                } else if (flagchkPrefix == false && suffixFrom == null && suffixDate != null) {
                    outputLeaveDetails.append(" WITH SUFFIX ON ").append(suffixDate);
                }
                if (ifLongTermLeave != null) {
                    if (ifLongTermLeave.trim().equals("Y")) {
                        outputLeaveDetails.append(" ON LONG TERM BASIS ");
                    }
                }
            } else if (leaveSnacSurrender.equalsIgnoreCase("02")) {
                if (leaveType != null && !leaveType.trim().equals("")) {
                    leaveName = getLeaveType(leaveType);
                }
                outputLeaveDetails.append(" SURRENDERED ");
                if (leaveName != null) {
                    outputLeaveDetails.append(leaveName);
                }
                if (noSurrenderDays != null && !noSurrenderDays.trim().equals("")) {
                    if (Integer.parseInt(noSurrenderDays) == 1) {
                        outputLeaveDetails.append(" OF ").append(noSurrenderDays).append(" DAY ");
                    } else {
                        outputLeaveDetails.append(" OF ").append(noSurrenderDays).append(" DAYS ");
                    }

                }
                if (fDate != null && tDate != null) {
                    if (fDate.equals(tDate)) {
                        outputLeaveDetails.append(" ON ").append(fDate);
                    } else {
                        outputLeaveDetails.append(" FROM ").append(fDate).append(" TO ").append(tDate);
                    }
                } else if (fDate != null && tDate == null) {
                    outputLeaveDetails.append(" ON ").append(fDate);
                } else if (fDate == null && tDate != null) {
                    outputLeaveDetails.append(" ON ").append(tDate);
                }
                if (sYear != null && !sYear.trim().equals("")) {
                    outputLeaveDetails.append(" DURING THE BLOCK PERIOD ").append(sYear);
                }
                if (tYear != null && !tYear.trim().equals("")) {
                    outputLeaveDetails.append(" TO ").append(tYear);
                }

            } else if (leaveSnacSurrender.equalsIgnoreCase("03")) {
                outputLeaveDetails.append(" CONDONED BREAK IN SERVICE ");
                if (fDate != null && tDate != null) {
                    if (!fDate.equals(tDate)) {
                        outputLeaveDetails.append(" FROM ").append(fDate).append(" TO ").append(tDate);
                    } else {
                        outputLeaveDetails.append(" ON ").append(fDate);
                    }
                } else if (fDate == null && tDate != null) {
                    outputLeaveDetails.append(" ON ").append(tDate);
                } else if (fDate != null && tDate == null) {
                    outputLeaveDetails.append(" ON ").append(fDate);
                }
                outputLeaveDetails.append(" TO MAINTAIN THE CONTINUITY AND THE BREAK PERIOD WILL COUNT TOWARDS INCREMENT & PENSIONARY BENEFITS AS A SPECIAL CASE ");

            } else if (leaveSnacSurrender.equalsIgnoreCase("04")) {
                outputLeaveDetails.append(" TREATED ABSENT FROM DUTY ");
                if (fDate != null && tDate != null) {
                    if (!fDate.equals(tDate)) {
                        outputLeaveDetails.append(" THIS ENTIRE PERIOD FROM ").append(fDate).append(" TO ").append(tDate);
                    } else {
                        outputLeaveDetails.append(" ON ").append(fDate);
                    }
                } else if (fDate == null && tDate != null) {
                    outputLeaveDetails.append(" ON ").append(tDate);
                } else if (fDate != null && tDate == null) {
                    outputLeaveDetails.append(" ON ").append(fDate);
                }
            } else if (leaveSnacSurrender.equalsIgnoreCase("05")) {

                outputLeaveDetails.append(" ALLOWED/AVAILED JOINING TIME ");
                if (fDate != null && tDate != null) {
                    if (!fDate.equals(tDate)) {
                        outputLeaveDetails.append(" FROM ").append(fDate).append(" TO ").append(tDate);
                    } else {
                        outputLeaveDetails.append(" ON ").append(fDate);
                    }
                } else if (fDate == null && tDate != null) {
                    outputLeaveDetails.append(" ON ").append(tDate);
                } else if (fDate != null && tDate == null) {
                    outputLeaveDetails.append(" ON ").append(fDate);
                }

            } else if (leaveSnacSurrender.equalsIgnoreCase("06")) {

                outputLeaveDetails.append(" CREDITED EARNED LEAVE ACCOUNT ");
                if (noSanctionDays != null && !noSanctionDays.trim().equals("")) {
                    if (Integer.parseInt(noSanctionDays) > 0) {
                        if (noSanctionDays.equals("1")) {
                            outputLeaveDetails.append(" BY ").append(noSanctionDays).append(" DAY ");
                        } else {
                            outputLeaveDetails.append(" BY ").append(noSanctionDays).append(" DAYS ");
                        }
                    }
                }
                if (fDate != null && tDate != null) {
                    if (!fDate.equals(tDate)) {
                        outputLeaveDetails.append(" UNAVAILED JOINING TIME FROM ").append(fDate).append(" TO ").append(tDate);
                    } else {
                        outputLeaveDetails.append(" UNAVAILED JOINING TIME ON ").append(fDate);
                    }
                } else if (fDate == null && tDate != null) {
                    outputLeaveDetails.append(" UNAVAILED JOINING TIME ON ").append(tDate);
                } else if (fDate != null && tDate == null) {
                    outputLeaveDetails.append(" UNAVAILED JOINING TIME ON ").append(fDate);
                }
            } else if (leaveSnacSurrender.equalsIgnoreCase("07")) {

                outputLeaveDetails.append(" EXTENSION OF JOINING TIME ");
                if (noSanctionDays != null && !noSanctionDays.trim().equals("")) {
                    if (Integer.parseInt(noSanctionDays) > 0) {
                        if (noSanctionDays.equals("1")) {
                            outputLeaveDetails.append(" FOR ").append(noSanctionDays).append(" DAY ");
                        } else {
                            outputLeaveDetails.append(" FOR A PERIOD OF ").append(noSanctionDays).append(" DAYS ");
                        }
                    }
                }
                if (fDate != null && tDate != null) {
                    if (!fDate.equals(tDate)) {
                        outputLeaveDetails.append(" WEF ").append(fDate).append(" TO ").append(tDate);
                    } else {
                        outputLeaveDetails.append(" WEF ").append(fDate);
                    }
                } else if (fDate == null && tDate != null) {
                    outputLeaveDetails.append(" WEF ").append(tDate);
                } else if (fDate != null && tDate == null) {
                    outputLeaveDetails.append(" WEF ").append(fDate);
                }
            } else if (leaveSnacSurrender.equalsIgnoreCase("08")) {

                outputLeaveDetails.append(" REMAINED ABSENT FROM DUTY ");
                if (noSanctionDays != null && !noSanctionDays.trim().equals("")) {
                    if (Integer.parseInt(noSanctionDays) > 0) {
                        if (noSanctionDays.equals("1")) {
                            outputLeaveDetails.append(" FOR ").append(noSanctionDays).append(" DAY ");
                        } else {
                            outputLeaveDetails.append(" FOR A PERIOD OF ").append(noSanctionDays).append(" DAYS ");
                        }
                    }
                }
                if (fDate != null && tDate != null) {
                    if (!fDate.equals(tDate)) {
                        outputLeaveDetails.append(" FROM ").append(fDate).append(" TO ").append(tDate);
                    } else {
                        outputLeaveDetails.append(" ON ").append(fDate);
                    }
                } else if (fDate == null && tDate != null) {
                    outputLeaveDetails.append(" ON ").append(tDate);
                } else if (fDate != null && tDate == null) {
                    outputLeaveDetails.append(" ON ").append(fDate);
                }
            }

            if (notficationString != null && !notficationString.trim().equals("")) {
                outputLeaveDetails.append(notficationString);
            }
            if (outputLeaveDetails != null && !outputLeaveDetails.equals("")) {
                outputLeaveDetails.append(".");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            DataBaseFunctions.closeSqlObjects(rsLeave);
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return outputLeaveDetails.toString().toUpperCase();
    }

    @Override
    public String getFirstAppointDetails(RecruitmentModel recruitmentModel, int notid, String notType) {
        StringBuffer cadreComp = new StringBuffer();
        String postName = null;
        String postClass = null;
        String postStatus = null;
        String superNote = null;
        String authName = null;
        String offName = null;
        String deptName = null;
        String notString = "";
        String othSpn = "";

        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = dataSource.getConnection();
            NotificationBean notBean = getNotificationDetails(notid, notType);

            othSpn = getOtherSpn(recruitmentModel.getHidOthSpc());
            if (!othSpn.equals("")) {
                authName = othSpn;
            } else {
                if (notBean.getSltauth() != null && !notBean.getSltauth().equals("")) {
                    authName = getSPN(notBean.getSltauth());
                } else if (notBean.getSltoffname() != null && !notBean.getSltoffname().equals("")) {
                    offName = getOffName(notBean.getSltoffname());
                } else if (notBean.getSltdeptname() != null && !notBean.getSltdeptname().equals("")) {
                    deptName = getDeptName(notBean.getSltdeptname());
                }
            }
            notString = getNotificationString(notBean.getOrdno(), notBean.getTxtNotOrdDt(), deptName, offName, authName);

            if (recruitmentModel.getRdPostClassification() != null && !recruitmentModel.getRdPostClassification().equals("")) {
                if (recruitmentModel.getRdPostClassification().equalsIgnoreCase("O")) {
                    postClass = "PROBATION";
                } else if (recruitmentModel.getRdPostClassification().equalsIgnoreCase("T")) {
                    postClass = "TEMPORARY";
                } else if (recruitmentModel.getRdPostClassification().equalsIgnoreCase("P")) {
                    postClass = "PERMANENT";
                }
            }
            if (recruitmentModel.getRdPostStatus() != null && !recruitmentModel.getRdPostStatus().equals("")) {
                if (recruitmentModel.getRdPostStatus().equalsIgnoreCase("S")) {
                    postStatus = "SUBSTANTIVE";
                } else if (recruitmentModel.getRdPostStatus().equalsIgnoreCase("O")) {
                    postStatus = "OFFICIATING";
                }
            }

            cadreComp.append(" APPOINTED ");
            if (recruitmentModel.getSltGenericPost() != null && !recruitmentModel.getSltGenericPost().trim().equals("")) {
                cadreComp.append(" AS ").append(getPostName(recruitmentModel.getSltGenericPost()));
            }
            if (recruitmentModel.getSltCadre() != null && !recruitmentModel.getSltCadre().trim().equals("")) {
                cadreComp.append(" TO ").append(getCadreName(recruitmentModel.getSltCadre()));
                /*LEVEL DESCRIPTTION WILL BE HERE*/

                if (recruitmentModel.getSltCadreLevel() != null && !recruitmentModel.getSltCadreLevel().trim().equals("")) {
                    cadreComp.append(" AND ALLOTED ").append(recruitmentModel.getSltCadreLevel()).append(" CADRE ");
                }

            }
            if (recruitmentModel.getSltGrade() != null && !recruitmentModel.getSltGrade().trim().equals("")) {
                cadreComp.append(" ON ").append(recruitmentModel.getSltGrade());
            }

            cadreComp.append(" THROUGH ");

            if (recruitmentModel.getSltNotificationType().equals("FIRST_APPOINTMENT")) {
                cadreComp.append(" REGULAR RECRUITMENT ");
            }
            if (recruitmentModel.getSltNotificationType().equals("REHABILITATION")) {
                cadreComp.append(" REHABILITATION SCHEME ");
            }
            if (recruitmentModel.getSltNotificationType().equals("VALIDATION")) {
                cadreComp.append(" VALIDATION SCHEME ");
            }
            if (recruitmentModel.getTxtCadreJoiningWEFDt() != null && !recruitmentModel.getTxtCadreJoiningWEFDt().equals("")) {
                cadreComp.append(" WEF  ").append(recruitmentModel.getTxtCadreJoiningWEFDt().toUpperCase());
                if (recruitmentModel.getSltCadreJoiningWEFTime() != null && !recruitmentModel.getSltCadreJoiningWEFTime().trim().equals("")) {
                    cadreComp.append("(").append(recruitmentModel.getSltCadreJoiningWEFTime()).append(")");
                }
            }
            String payDetails = null;
            EmpPayRecordForm epayrecordform = new EmpPayRecordForm();
            epayrecordform.setEmpid(recruitmentModel.getEmpid());

            epayrecordform.setNot_type(recruitmentModel.getSltNotificationType());
            epayrecordform.setPayscale(recruitmentModel.getSltPayScale());
            epayrecordform.setBasic(recruitmentModel.getTxtBasic());
            epayrecordform.setGp(recruitmentModel.getTxtGP());
            epayrecordform.setS_pay(recruitmentModel.getTxtSP());
            epayrecordform.setP_pay(recruitmentModel.getTxtPP());
            epayrecordform.setOth_pay(recruitmentModel.getTxtOP());
            epayrecordform.setOth_desc(recruitmentModel.getTxtDescOP());
            epayrecordform.setWefDt(recruitmentModel.getTxtWEFDt().toUpperCase());
            epayrecordform.setWefTime(recruitmentModel.getSltWEFTime());
            payDetails = getPayDetails(epayrecordform);
            if (payDetails != null) {
                cadreComp.append(" ").append(payDetails);
            }
            if (recruitmentModel.getTxtCadreJoiningWEFDt() != null && !recruitmentModel.getTxtCadreJoiningWEFDt().equals("")) {

                cadreComp.append(" AND JOINED CADRE");

            }

            if (notString != null) {
                cadreComp.append(notString).toString().toUpperCase();
            }

            if (postStatus != null) {
                if (postClass != null) {
                    cadreComp.append(" ON ").append(postClass).append("/ ").append(postStatus);
                } else {
                    cadreComp.append(" ON ").append(postStatus);
                }

                cadreComp.append(" BASIS ");
            } else if (postStatus == null) {

                if (postClass != null) {
                    cadreComp.append(" ON ").append(postClass);
                    if (!postClass.equalsIgnoreCase("PROBATION")) {
                        cadreComp.append(" BASIS ");
                    }
                }
            }

            cadreComp.append(".");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return cadreComp.toString();
    }

    @Override
    public String getJoininginCadreDetails(JoiningCadreForm joiniCadre, int notid, String notificationtype) {
        StringBuffer promDetails = new StringBuffer();

        PreparedStatement ps = null;
        ResultSet rs2 = null;
        ArrayList addspcList = new ArrayList();
        ArrayList addspnList = new ArrayList();
        String superNote = "";
        String postClass1 = null;
        String postStatus1 = null;
        String authName = null;
        String offName = null;
        String deptName = null;

        String notString = "";
        String othSpn = "";

        Connection con = null;

        try {
            con = dataSource.getConnection();

            NotificationBean notBean = getNotificationDetails(notid, notificationtype);

            if (notBean.getSltauth() != null && !notBean.getSltauth().equals("")) {
                authName = getSPN(notBean.getSltauth());
            } else if (notBean.getSltoffname() != null && !notBean.getSltoffname().equals("")) {
                offName = getOffName(notBean.getSltoffname());
            } else if (notBean.getSltdeptname() != null && !notBean.getSltdeptname().equals("")) {
                deptName = getDeptName(notBean.getSltdeptname());
            }

            notString = getNotificationString(notBean.getOrdno(), notBean.getTxtNotOrdDt(), deptName, offName, authName);

            if (joiniCadre.getRdPostClassification() != null) {
                if (joiniCadre.getRdPostClassification().equalsIgnoreCase("O")) {
                    postClass1 = "PROBATION";
                } else if (joiniCadre.getRdPostClassification().equalsIgnoreCase("T")) {
                    postClass1 = "TEMPORARY";
                    //eshp.setPostClass(postClass1);
                } else if (joiniCadre.getRdPostClassification().equalsIgnoreCase("P")) {
                    postClass1 = "PERMANENT";
                } else if (joiniCadre.getRdPostClassification().equalsIgnoreCase("A")) {
                    postClass1 = "ADHOC";
                }
            }
            if (joiniCadre.getRdPostStatus() != null) {
                if (joiniCadre.getRdPostStatus().equalsIgnoreCase("S")) {
                    postStatus1 = "SUBSTANTIVE";
                } else if (joiniCadre.getRdPostStatus().equalsIgnoreCase("O")) {
                    postStatus1 = "OFFICIATING";
                }
            }

            ps = con.prepareStatement("SELECT SPC FROM EMP_JOIN WHERE NOT_ID=? ");
            ps.setInt(1, notid);
            rs2 = ps.executeQuery();
            while (rs2.next()) {
                if (rs2.getString("SPC") != null && !rs2.getString("SPC").equals("")) {
                    addspcList.add(rs2.getString("SPC"));
                }

            }

            if (addspcList.size() != 0) {
                for (int i = 0; i < addspcList.size(); i++) {
                    addspnList.add(getSPN((String) addspcList.get(i)));
                }
            }

            //if (notid != null) {
            //superNote = ServiceHistoryData.getSupersseionNote(notid, st);
            //}
            if (superNote != null) {
                promDetails.append(superNote);
            }

            if (notificationtype != null && (notificationtype.equalsIgnoreCase("JOIN_CADRE") || notificationtype.equalsIgnoreCase("RELIEVE_CADRE"))) {

                if (notificationtype.equalsIgnoreCase("JOIN_CADRE")) {
                    promDetails.append(" JOINED TO ");
                } else if (notificationtype.equalsIgnoreCase("RELIEVE_CADRE")) {
                    promDetails.append(" RELIEVED FROM ");
                }
                if (joiniCadre.getSltCadre() != null && !joiniCadre.getSltCadre().equals("")) {
                    promDetails.append(getCadreName(joiniCadre.getSltCadre()));
                    if (joiniCadre.getSltDescription() != null) {
                        promDetails.append(" OF ").append((joiniCadre.getSltDescription()));
                    }
                }
                if (joiniCadre.getSltGrade() != null && !joiniCadre.getSltGrade().equals("")) {
                    promDetails.append(", ").append(joiniCadre.getSltGrade());
                }
                if (postClass1 != null) {
                    promDetails.append(" ON ").append(postClass1);
                    if (postStatus1 != null) {
                        promDetails.append(" (").append(postStatus1).append(")");
                    }
                }

                if (joiniCadre.getTxtCadreJoiningWEFDt() != null && !joiniCadre.getTxtCadreJoiningWEFDt().equals("")) {
                    promDetails.append(" WEF ").append(joiniCadre.getTxtCadreJoiningWEFDt());
                    if (joiniCadre.getSltCadreJoiningWEFTime() != null && !joiniCadre.getSltCadreJoiningWEFTime().equals("")) {

                        promDetails.append(" (").append(joiniCadre.getSltCadreJoiningWEFTime()).append(")");
                    }
                }
                if (notString != null) {
                    promDetails.append(notString);
                }

            }

            promDetails.append(". ");

            if ((joiniCadre.getTxtSP() != null && !joiniCadre.getTxtSP().trim().equals("") && !joiniCadre.getTxtSP().trim().equals("0"))
                    || (joiniCadre.getTxtBasic() != null && !joiniCadre.getTxtBasic().trim().equals("") && !joiniCadre.getTxtBasic().trim().equals("0"))
                    || (joiniCadre.getTxtGP() != null && !joiniCadre.getTxtGP().trim().equals("") && !joiniCadre.getTxtGP().trim().equals("0"))
                    || (joiniCadre.getTxtPP() != null && !joiniCadre.getTxtPP().trim().equals("") && !joiniCadre.getTxtPP().trim().equals("0"))
                    || (joiniCadre.getTxtOP() != null && !joiniCadre.getTxtOP().trim().equals("") && !joiniCadre.getTxtOP().trim().equals("0"))
                    || (joiniCadre.getSltPayScale() != null && !joiniCadre.getSltPayScale().trim().equals("") && !joiniCadre.getSltPayScale().trim().equals("0"))) {

                promDetails.append(" ALLOWED TO DRAW ");

                if (joiniCadre.getTxtBasic() != null && !joiniCadre.getTxtBasic().trim().equals("") && !joiniCadre.getTxtBasic().trim().equals("0")) {
                    promDetails.append(" PAY @ RS. ").append(CommonFunctions.formatNumber(joiniCadre.getTxtBasic())).append("/- PM ");
                }

                if (joiniCadre.getTxtGP() != null && !joiniCadre.getTxtGP().trim().equals("") && !joiniCadre.getTxtGP().trim().equals("0")) {
                    promDetails.append(" GRADE PAY @ RS. ").append(CommonFunctions.formatNumber(joiniCadre.getTxtGP())).append("/- PM ");
                }
                if (joiniCadre.getSltPayScale() != null && !joiniCadre.getSltPayScale().trim().equals("")) {
                    promDetails.append(" IN THE SCALE OF PAY RS. ").append(joiniCadre.getSltPayScale().toUpperCase()).append("/-");
                }
                if (joiniCadre.getTxtSP() != null && !joiniCadre.getTxtSP().trim().equals("") && !joiniCadre.getTxtSP().trim().equals("0")) {
                    promDetails.append(" + SPECIAL PAY @ RS. ").append(CommonFunctions.formatNumber(joiniCadre.getTxtSP())).append("/- ");
                }
                if (joiniCadre.getTxtPP() != null && !joiniCadre.getTxtPP().trim().equals("") && !joiniCadre.getTxtPP().trim().equals("0")) {
                    promDetails.append(" + PERSONAL PAY @ RS. ").append(CommonFunctions.formatNumber(joiniCadre.getTxtPP())).append("/- ");
                }
                if (joiniCadre.getTxtDescOP() != null && !joiniCadre.getTxtDescOP().trim().equals("")) {
                    if (joiniCadre.getTxtOP() != null && !joiniCadre.getTxtOP().trim().equals("") && !joiniCadre.getTxtOP().trim().equals("0")) {
                        promDetails.append(" ").append(joiniCadre.getTxtDescOP().toUpperCase()).append(" @ RS. ").append(CommonFunctions.formatNumber(joiniCadre.getTxtOP())).append("/- ");
                    }
                } else {
                    if (joiniCadre.getTxtOP() != null && !joiniCadre.getTxtOP().trim().equals("") && !joiniCadre.getTxtOP().trim().equals("0")) {
                        promDetails.append(" OTHER PAY @ RS. ").append(CommonFunctions.formatNumber(joiniCadre.getTxtOP())).append("/- ");
                    }
                }
                if ((joiniCadre.getTxtSP() != null && !joiniCadre.getTxtSP().trim().equals("") && !joiniCadre.getTxtSP().trim().equals("0"))
                        || (joiniCadre.getTxtBasic() != null && !joiniCadre.getTxtBasic().trim().equals("") && !joiniCadre.getTxtBasic().trim().equals("0"))
                        || (joiniCadre.getTxtGP() != null && !joiniCadre.getTxtGP().trim().equals("") && !joiniCadre.getTxtGP().trim().equals("0"))
                        || (joiniCadre.getTxtPP() != null && !joiniCadre.getTxtPP().trim().equals("") && !joiniCadre.getTxtPP().trim().equals("0"))
                        || (joiniCadre.getTxtOP() != null && !joiniCadre.getTxtOP().trim().equals("") && !joiniCadre.getTxtOP().trim().equals("0"))) {
                    promDetails.append(" PM ");
                }
                if (joiniCadre.getTxtWEFDt() != null && !joiniCadre.getTxtWEFDt().trim().equals("")) {
                    promDetails.append(" WEF ").append(joiniCadre.getTxtWEFDt());
                    if (joiniCadre.getSltWEFTime() != null && !joiniCadre.getSltWEFTime().equals("")) {
                        promDetails.append(" (" + joiniCadre.getSltWEFTime() + ")");
                    }
                }
                promDetails.append(".");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs2, ps);
            DataBaseFunctions.closeSqlObjects(con);

        }

        return promDetails.toString().toUpperCase();
    }

    @Override
    public String getDeputationDetails(DeputationDataForm deputationForm, String notType, int notId) {

        StringBuffer outputDeputationDetails = new StringBuffer();
        String authName = null;
        String offName = null;
        String deptName = null;
        String superNote = null;
        String notString = "";
        String othSpn = "";
        Connection con = null;

        try {
            con = dataSource.getConnection();

            if (deputationForm.getHidNotiSpc() != null && !deputationForm.getHidNotiSpc().equals("")) {
                authName = getSPN(deputationForm.getHidNotiSpc());
            } else if (deputationForm.getHidNotifyingOffCode() != null && !deputationForm.getHidNotifyingOffCode().equals("")) {
                offName = getOffName(deputationForm.getHidNotifyingOffCode());
            } else if (deputationForm.getHidNotifyingDeptCode() != null && !deputationForm.getHidNotifyingDeptCode().equals("")) {

                deptName = getDeptName(deputationForm.getHidNotifyingDeptCode());
            }

            notString = getNotificationString(deputationForm.getTxtNotOrdNo(), deputationForm.getTxtNotOrdDt(), deptName, offName, authName);
            //changes supersede done--   

            if (deputationForm.getChkExtnDptnPrd() != null && !deputationForm.getChkExtnDptnPrd().equals("")) {
                if (deputationForm.getChkExtnDptnPrd().equalsIgnoreCase("Y")) {
                    outputDeputationDetails.append("THE DEPUTATION PERIOD HAS BEEN EXTENDED ");
                }
            } else {
                outputDeputationDetails.append("DEPUTATION IS SANCTIONED ");
            }

            if (notString != null && !notString.equals("")) {
                outputDeputationDetails.append(notString);
            }

            if (deputationForm.getTxtWEFrmDt() != null && !deputationForm.getTxtWEFrmDt().trim().equals("")) {
                outputDeputationDetails.append(", WEF ").append(deputationForm.getTxtWEFrmDt().toUpperCase());
            }
            if (deputationForm.getTxtTillDt() != null && !deputationForm.getTxtTillDt().trim().equals("")) {
                outputDeputationDetails.append(" TO ").append(deputationForm.getTxtTillDt().toUpperCase());
            }

            outputDeputationDetails.append(".");

            if (notId > 0) {
                outputDeputationDetails.append(getContributionDetails(notId, deputationForm.getEmpid()));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);

        }

        return outputDeputationDetails.toString();
    }

    public String getContributionDetails(int notid, String empId) {

        ResultSet rs = null;
        StringBuffer outputLPContDetails = new StringBuffer();
        boolean lpcContribution = true;
        boolean penContribution = true;
        boolean payContribution = true;
        boolean perPayContribution = true;
        boolean splPayContribution = true;
        boolean othPayContribution = true;
        Connection con = null;
        Statement st = null;
        try {
            con = dataSource.getConnection();
            st = con.createStatement();
            rs = st.executeQuery("SELECT * FROM EMP_CONTRIBUTION WHERE EMP_ID='" + empId + "' AND NOT_ID='" + notid + "' ORDER BY CONT_TYPE");
            while (rs.next()) {
                String amonut = null;
                String fDate = null;
                String tDate = null;
                String desciption = null;
                if (rs.getString("CONT_TYPE") != null && !rs.getString("CONT_TYPE").trim().equals("")) {
                    if (rs.getString("CONT_TYPE").equalsIgnoreCase("L")) {
                        if (lpcContribution) {
                            lpcContribution = false;
                            outputLPContDetails.append("</br><b><u> Leave Salary Contribution </u></b></br>");
                        }
                    } else if (rs.getString("CONT_TYPE").equalsIgnoreCase("P")) {
                        if (penContribution) {
                            penContribution = false;
                            outputLPContDetails.append("</br><b><u> Pension Contribution </u></b>");
                        }
                    } else if (rs.getString("CONT_TYPE").equalsIgnoreCase("Y")) {
                        if (payContribution) {
                            payContribution = false;
                            outputLPContDetails.append("</br><b><u> Pay Allowances </u></b></br>");
                        }
                    } else if (rs.getString("CONT_TYPE").equalsIgnoreCase("SP")) {
                        if (perPayContribution) {
                            perPayContribution = false;
                            outputLPContDetails.append("</br><b><u> Special Pay Allowances </u></b></br>");
                        }
                    } else if (rs.getString("CONT_TYPE").equalsIgnoreCase("PP")) {
                        if (splPayContribution) {
                            splPayContribution = false;
                            outputLPContDetails.append("</br><b><u> Personal Pay Allowances </u></b></br>");
                        }
                    } else if (rs.getString("CONT_TYPE").equalsIgnoreCase("OP")) {
                        if (othPayContribution) {
                            othPayContribution = false;
                            outputLPContDetails.append("</br><b><u> Other Pay Allowances </u></b></br>");
                        }
                    }
                }

                if (rs.getDate("WEF_DATE") != null && !rs.getString("WEF_DATE").equals("")) {
                    fDate = CommonFunctions.getFormattedOutputDate1(rs.getDate("WEF_DATE"));
                }
                if (rs.getDate("TILL_DATE") != null && !rs.getString("TILL_DATE").equals("")) {
                    tDate = CommonFunctions.getFormattedOutputDate1(rs.getDate("TILL_DATE"));
                }
                if (rs.getString("AMOUNT") != null && !rs.getString("AMOUNT").trim().equals("") && !rs.getString("AMOUNT").trim().equals("0")) {
                    amonut = CommonFunctions.formatNumber(rs.getDouble("AMOUNT"));
                }

                if (fDate != null && tDate != null) {
                    outputLPContDetails.append(fDate).append(" TO ").append(tDate);
                    if (amonut != null) {
                        outputLPContDetails.append(" @Rs. ").append(CommonFunctions.formatNumber(amonut)).append("; ");
                    } else {
                        outputLPContDetails.append(" NIL; ");
                    }
                } else if (fDate == null && tDate != null) {
                    outputLPContDetails.append(" WEF ").append(tDate);
                    if (amonut != null) {
                        outputLPContDetails.append(" @Rs. ").append(CommonFunctions.formatNumber(amonut)).append("; ");
                    } else {
                        outputLPContDetails.append(" NIL; ");
                    }
                } else if (fDate != null && tDate == null) {
                    outputLPContDetails.append(" WEF ").append(fDate);
                    if (amonut != null) {
                        outputLPContDetails.append(" @Rs. ").append(CommonFunctions.formatNumber(amonut)).append("; ");
                    } else {
                        outputLPContDetails.append(" NIL; ");
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);

        }

        return outputLPContDetails.toString();
    }

    @Override
    public String getPromotionDetails(PromotionForm promoForm, int notid, String notType) {
        StringBuffer promDetails = new StringBuffer();

        PreparedStatement ps = null;
        ResultSet rs2 = null;
        ArrayList addspcList = new ArrayList();
        ArrayList addspnList = new ArrayList();
        String superNote = "";
        String postClass1 = null;
        String postStatus1 = null;
        String authName = null;
        String offName = null;
        String deptName = null;

        String notString = "";
        String othSpn = "";

        Connection con = null;

        try {
            con = dataSource.getConnection();

            NotificationBean notBean = getNotificationDetails(notid, notType);

            if (promoForm.getHidNotifyingOthSpc() != null && !promoForm.getHidNotifyingOthSpc().equals("")) {
                othSpn = getOtherSpn(promoForm.getHidNotifyingOthSpc());
                authName = othSpn;
            } else {
                if (notBean.getSltauth() != null && !notBean.getSltauth().equals("")) {
                    authName = getSPN(notBean.getSltauth());
                } else if (notBean.getSltoffname() != null && !notBean.getSltoffname().equals("")) {
                    offName = getOffName(notBean.getSltoffname());
                } else if (notBean.getSltdeptname() != null && !notBean.getSltdeptname().equals("")) {
                    deptName = getDeptName(notBean.getSltdeptname());
                }
            }
            notString = getNotificationString(notBean.getOrdno(), notBean.getTxtNotOrdDt(), deptName, offName, authName);

            if (promoForm.getRdPostClassification() != null) {
                if (promoForm.getRdPostClassification().equalsIgnoreCase("O")) {
                    postClass1 = "PROBATION";
                } else if (promoForm.getRdPostClassification().equalsIgnoreCase("T")) {
                    postClass1 = "TEMPORARY";
                    //eshp.setPostClass(postClass1);
                } else if (promoForm.getRdPostClassification().equalsIgnoreCase("P")) {
                    postClass1 = "PERMANENT";
                } else if (promoForm.getRdPostClassification().equalsIgnoreCase("A")) {
                    postClass1 = "ADHOC";
                }
            }
            if (promoForm.getRdPostStatus() != null) {
                if (promoForm.getRdPostStatus().equalsIgnoreCase("S")) {
                    postStatus1 = "SUBSTANTIVE";
                } else if (promoForm.getRdPostStatus().equalsIgnoreCase("O")) {
                    postStatus1 = "OFFICIATING";
                }
            }

            ps = con.prepareStatement("SELECT SPC FROM EMP_JOIN WHERE NOT_ID=? ");
            ps.setInt(1, notid);
            rs2 = ps.executeQuery();
            while (rs2.next()) {
                if (rs2.getString("SPC") != null && !rs2.getString("SPC").equals("")) {
                    addspcList.add(rs2.getString("SPC"));
                }

            }
            DataBaseFunctions.closeSqlObjects(rs2, ps);

            if (addspcList.size() != 0) {
                for (int i = 0; i < addspcList.size(); i++) {
                    addspnList.add(getSPN((String) addspcList.get(i)));
                }
            }

            //if (notid != null) {
            //superNote = ServiceHistoryData.getSupersseionNote(notid, st);
            //}
            if (superNote != null) {
                promDetails.append(superNote);
            }

            if (notType != null && notType.equalsIgnoreCase("PROMOTION")) {
                if (promoForm.getChkProformaPromotion() != null) {
                    promDetails.append(" ALLOWED PROFORMA PROMOTION ");
                } else if (promoForm.getChkRetroPromotion() != null) {
                    promDetails.append(" ALLOWED RETROSPECTIVE PROMOTION ");
                } else {
                    promDetails.append(" PROMOTED ");
                }
                if (promoForm.getSltGrade() != null && !promoForm.getSltGrade().equalsIgnoreCase("")) {
                    promDetails.append(" TO ").append(promoForm.getGradeName());
                }
                if (promoForm.getSltCadre() != null && !promoForm.getSltCadre().equals("")) {
                    promDetails.append(" IN ").append(getCadreName(promoForm.getSltCadre()));
                }
                if (promoForm.getHidPostedGPC() != null && !promoForm.getHidPostedGPC().equals("")) {
                    promDetails.append(" AND POSTED AS ").append(getPostName(promoForm.getHidPostedGPC()));
                    if (promoForm.getHidPostedDeptCode() != null && !promoForm.getHidPostedDeptCode().equals("")) {
                        promDetails.append(", ").append(getDeptName(promoForm.getHidPostedDeptCode())).append(" DEPARTMENT ");
                    }
                } else if (promoForm.getHidPostingOthSpc() != null && !promoForm.getHidPostingOthSpc().equals("")) {
                    promDetails.append(" AND POSTED AS ").append(getOtherSpn(promoForm.getHidPostingOthSpc()));
                    promDetails.append(", ").append(" GOVERMENT OF INDIA ");
                } else if (promoForm.getChkJoinedAsSuch() != null) {
                    if (promoForm.getChkJoinedAsSuch().equalsIgnoreCase("Y")) {
                        promDetails.append(" AND JOINED AS SUCH ");
                    }
                }
                if (promoForm.getTxtCadreJoiningWEFDt() != null && !promoForm.getTxtCadreJoiningWEFDt().equals("")) {
                    promDetails.append(" WEF ").append(promoForm.getTxtCadreJoiningWEFDt());
                }
                if (promoForm.getSltCadreJoiningWEFTime() != null && !promoForm.getSltCadreJoiningWEFTime().equals("")) {
                    promDetails.append(", ").append(promoForm.getSltCadreJoiningWEFTime());
                }

                if (notString != null) {
                    promDetails.append(notString);
                }

                if (addspnList.size() != 0) {
                    promDetails.append(" ALONG WITH ");
                    for (int i = 0; i < addspnList.size(); i++) {
                        if (i == addspnList.size() - 1) {
                            promDetails.append(addspnList.get(i));
                        } else {
                            promDetails.append(addspnList.get(i)).append(", ");
                        }
                    }
                }

            } else if (notType != null && notType.equalsIgnoreCase("REPATRIATION")) {
                promDetails.append(" ON REPATRIATION/ REVERSION REPORTED UNDER THE CADRE ");
                if (promoForm.getTxtCadreJoiningWEFDt() != null && !promoForm.getTxtCadreJoiningWEFDt().equals("")) {
                    promDetails.append(" ON ").append(promoForm.getTxtCadreJoiningWEFDt());
                }
                if (authName != null && !authName.trim().equals("")) {
                    promDetails.append(" IN PURSUANCE OF ").append(authName);
                } else if (offName != null && !offName.trim().equals("")) {
                    promDetails.append(" IN PURSUANCE OF ").append(offName);
                } else if (deptName != null && !deptName.trim().equals("")) {
                    promDetails.append(" IN PURSUANCE OF ").append(deptName).append(" DEPARTMENT ");
                }
                if ((authName != null && !authName.trim().equals("")) || (offName != null && !offName.trim().equals(""))
                        || (deptName != null && !deptName.trim().equals(""))) {
                    if (notBean.getOrdno() != null && !notBean.getOrdno().trim().equals("")) {
                        promDetails.append(" NOTIFICATION/ OFFICE ORDER NO. ").append(notBean.getOrdno());
                        if (notBean.getTxtNotOrdDt() != null && !notBean.getTxtNotOrdDt().equals("")) {
                            promDetails.append(" DATED ").append(notBean.getTxtNotOrdDt());
                        }
                    } else {
                        promDetails.append(" ORDER");
                        if (notBean.getTxtNotOrdDt() != null && !notBean.getTxtNotOrdDt().equals("")) {
                            promDetails.append(" DATED ").append(notBean.getTxtNotOrdDt());
                        }
                    }
                } else {
                    if (notBean.getOrdno() != null && !notBean.getOrdno().trim().equals("")) {
                        promDetails.append(" NOTIFICATION/ OFFICE ORDER NO. ").append(notBean.getOrdno().toUpperCase());
                        if (notBean.getTxtNotOrdDt() != null && !notBean.getTxtNotOrdDt().trim().equals("")) {
                            promDetails.append(" DATED ").append(notBean.getTxtNotOrdDt().toUpperCase());
                        }
                    } else if (notBean.getTxtNotOrdDt() != null && !notBean.getTxtNotOrdDt().trim().equals("")) {
                        promDetails.append(" NOTIFICATION/ OFFICE ORDER, DATED ").append(notBean.getTxtNotOrdDt().toUpperCase());
                    }

                }
            } else if (notType != null && (notType.equalsIgnoreCase("JOIN_CADRE") || notType.equalsIgnoreCase("RELIEVE_CADRE"))) {

                if (notType.equalsIgnoreCase("JOIN_CADRE")) {
                    promDetails.append(" JOINED ");
                } else if (notType.equalsIgnoreCase("RELIEVE_CADRE")) {
                    promDetails.append(" RELIEVED ");
                }
                if (promoForm.getSltCadre() != null && !promoForm.getSltCadre().equals("")) {
                    promDetails.append(" FROM ").append(getCadreName(promoForm.getSltCadre()));
                    if (promoForm.getSltDescription() != null) {
                        promDetails.append(" OF ").append((promoForm.getSltDescription()));
                    }
                }
                if (promoForm.getSltGrade() != null && !promoForm.getSltGrade().equals("")) {
                    promDetails.append(", ").append(promoForm.getSltGrade());
                }
                if (postClass1 != null) {
                    promDetails.append(" ON ").append(postClass1);
                    if (postStatus1 != null) {
                        promDetails.append(" (").append(postStatus1).append(")");
                    }
                }

                if (promoForm.getTxtCadreJoiningWEFDt() != null && !promoForm.getTxtCadreJoiningWEFDt().equals("")) {
                    promDetails.append(" WEF ").append(promoForm.getTxtCadreJoiningWEFDt());
                    if (promoForm.getSltCadreJoiningWEFTime() != null && !promoForm.getSltCadreJoiningWEFTime().equals("")) {

                        promDetails.append(" (").append(promoForm.getSltCadreJoiningWEFTime()).append(")");
                    }
                }
                if (notString != null) {
                    promDetails.append(notString);
                }

            } else if (notType != null && notType.equalsIgnoreCase("OFFICIATE")) {
                promDetails.append(" ALLOWED TO OFFICIATE ");

                if (promoForm.getSltGrade() != null && !promoForm.getSltGrade().equals("")) {
                    promDetails.append(" IN THE ").append(promoForm.getSltGrade());
                }
                if (promoForm.getSltCadre() != null && !promoForm.getSltCadre().equals("")) {
                    promDetails.append(" OF ").append(getCadreName(promoForm.getSltCadre()));
                }
                if (promoForm.getTxtWEFDt() != null && !promoForm.getTxtWEFDt().equals("")) {
                    promDetails.append(" WEF ").append(promoForm.getTxtWEFDt());

                    if (promoForm.getSltWEFTime() != null && !promoForm.getSltWEFTime().equals("")) {
                        promDetails.append(" (").append(promoForm.getSltWEFTime()).append(")");
                    }
                }
                if (notString != null) {
                    promDetails.append(notString);
                }
            }

            promDetails.append(". ");

            if ((promoForm.getTxtSP() != null && !promoForm.getTxtSP().trim().equals("") && !promoForm.getTxtSP().trim().equals("0"))
                    || (promoForm.getTxtBasic() != null && !promoForm.getTxtBasic().trim().equals("") && !promoForm.getTxtBasic().trim().equals("0"))
                    || (promoForm.getTxtGP() != null && !promoForm.getTxtGP().trim().equals("") && !promoForm.getTxtGP().trim().equals("0"))
                    || (promoForm.getTxtPP() != null && !promoForm.getTxtPP().trim().equals("") && !promoForm.getTxtPP().trim().equals("0"))
                    || (promoForm.getTxtOP() != null && !promoForm.getTxtOP().trim().equals("") && !promoForm.getTxtOP().trim().equals("0"))
                    || (promoForm.getSltPayScale() != null && !promoForm.getSltPayScale().trim().equals("") && !promoForm.getSltPayScale().trim().equals("0"))) {

                promDetails.append(" ALLOWED TO DRAW ");
                if (promoForm.getTxtBasic() != null && !promoForm.getTxtBasic().trim().equals("") && !promoForm.getTxtBasic().trim().equals("0")) {
                    promDetails.append(" PAY @ RS. ").append(CommonFunctions.formatNumber(promoForm.getTxtBasic())).append("/- PM ");
                }

                if (promoForm.getRdoPaycomm() != null && promoForm.getRdoPaycomm().equals("6")) {
                    if (promoForm.getTxtGP() != null && !promoForm.getTxtGP().trim().equals("") && !promoForm.getTxtGP().trim().equals("0")) {
                        promDetails.append(" GRADE PAY @ RS. ").append(CommonFunctions.formatNumber(promoForm.getTxtGP())).append("/- PM ");
                    }
                    if (promoForm.getSltPayScale() != null && !promoForm.getSltPayScale().trim().equals("")) {
                        promDetails.append(" IN THE SCALE OF PAY RS. ").append(promoForm.getSltPayScale().toUpperCase()).append("/-");
                    }
                } else if (promoForm.getRdoPaycomm() != null && promoForm.getRdoPaycomm().equals("7")) {
                    if (promoForm.getPayLevel() != null && !promoForm.getPayLevel().equals("")) {
                        promDetails.append(" IN CELL " + promoForm.getPayCell() + " OF LEVEL " + promoForm.getPayLevel() + " ");
                    }
                }

                if (promoForm.getTxtSP() != null && !promoForm.getTxtSP().trim().equals("") && !promoForm.getTxtSP().trim().equals("0")) {
                    promDetails.append(" + SPECIAL PAY @ RS. ").append(CommonFunctions.formatNumber(promoForm.getTxtSP())).append("/- ");
                }
                if (promoForm.getTxtPP() != null && !promoForm.getTxtPP().trim().equals("") && !promoForm.getTxtPP().trim().equals("0")) {
                    promDetails.append(" + PERSONAL PAY @ RS. ").append(CommonFunctions.formatNumber(promoForm.getTxtPP())).append("/- ");
                }
                if (promoForm.getTxtDescOP() != null && !promoForm.getTxtDescOP().trim().equals("")) {
                    if (promoForm.getTxtOP() != null && !promoForm.getTxtOP().trim().equals("") && !promoForm.getTxtOP().trim().equals("0")) {
                        promDetails.append(" ").append(promoForm.getTxtDescOP().toUpperCase()).append(" @ RS. ").append(CommonFunctions.formatNumber(promoForm.getTxtOP())).append("/- ");
                    }
                } else {
                    if (promoForm.getTxtOP() != null && !promoForm.getTxtOP().trim().equals("") && !promoForm.getTxtOP().trim().equals("0")) {
                        promDetails.append(" OTHER PAY @ RS. ").append(CommonFunctions.formatNumber(promoForm.getTxtOP())).append("/- ");
                    }
                }
                if ((promoForm.getTxtSP() != null && !promoForm.getTxtSP().trim().equals("") && !promoForm.getTxtSP().trim().equals("0"))
                        || (promoForm.getTxtBasic() != null && !promoForm.getTxtBasic().trim().equals("") && !promoForm.getTxtBasic().trim().equals("0"))
                        || (promoForm.getTxtGP() != null && !promoForm.getTxtGP().trim().equals("") && !promoForm.getTxtGP().trim().equals("0"))
                        || (promoForm.getTxtPP() != null && !promoForm.getTxtPP().trim().equals("") && !promoForm.getTxtPP().trim().equals("0"))
                        || (promoForm.getTxtOP() != null && !promoForm.getTxtOP().trim().equals("") && !promoForm.getTxtOP().trim().equals("0"))) {
                    //promDetails.append(" PM ");
                }
                if (promoForm.getTxtWEFDt() != null && !promoForm.getTxtWEFDt().trim().equals("")) {
                    promDetails.append(" WEF ").append(promoForm.getTxtWEFDt());
                    if (promoForm.getSltWEFTime() != null && !promoForm.getSltWEFTime().equals("")) {
                        promDetails.append(" (" + promoForm.getSltWEFTime() + ")");
                    }
                }
                promDetails.append(".");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs2, ps);
            DataBaseFunctions.closeSqlObjects(con);

        }

        return promDetails.toString().toUpperCase();
    }


    /*@Override
     public String getPromotionDetails(PromotionForm promoForm, String notid, String notType) {
     StringBuffer promDetails = new StringBuffer();

     PreparedStatement ps = null;
     ResultSet rs2 = null;
     ArrayList addspcList = new ArrayList();
     ArrayList addspnList = new ArrayList();
     String superNote = "";
     String postClass1 = null;
     String postStatus1 = null;
     String authName = null;
     String offName = null;
     String deptName = null;

     String notString = "";
     String othSpn = "";

     Connection con = null;

     try {
     con = dataSource.getConnection();

     NotificationBean notBean = getNotificationDetails(notid, notType);

     if (notBean.getSltauth() != null && !notBean.getSltauth().equals("")) {
     authName = getSPN(notBean.getSltauth());
     } else if (notBean.getSltoffname() != null && !notBean.getSltoffname().equals("")) {
     offName = getOffName(notBean.getSltoffname());
     } else if (notBean.getSltdeptname() != null && !notBean.getSltdeptname().equals("")) {
     deptName = getDeptName(notBean.getSltdeptname());
     }

     notString = getNotificationString(notBean.getOrdno(), notBean.getTxtNotOrdDt(), deptName, offName, authName);

     if (promoForm.getRdPostClassification() != null) {
     if (promoForm.getRdPostClassification().equalsIgnoreCase("O")) {
     postClass1 = "PROBATION";
     } else if (promoForm.getRdPostClassification().equalsIgnoreCase("T")) {
     postClass1 = "TEMPORARY";
     } else if (promoForm.getRdPostClassification().equalsIgnoreCase("P")) {
     postClass1 = "PERMANENT";
     } else if (promoForm.getRdPostClassification().equalsIgnoreCase("A")) {
     postClass1 = "ADHOC";
     }
     }
     if (promoForm.getRdPostStatus() != null) {
     if (promoForm.getRdPostStatus().equalsIgnoreCase("S")) {
     postStatus1 = "SUBSTANTIVE";
     } else if (promoForm.getRdPostStatus().equalsIgnoreCase("O")) {
     postStatus1 = "OFFICIATING";
     }
     }

     ps = con.prepareStatement("SELECT SPC FROM EMP_JOIN WHERE NOT_ID=? ");
     ps.setString(1, notid);
     rs2 = ps.executeQuery();
     while (rs2.next()) {
     if (rs2.getString("SPC") != null && !rs2.getString("SPC").equals("")) {
     addspcList.add(rs2.getString("SPC"));
     }

     }

     if (addspcList.size() != 0) {
     for (int i = 0; i < addspcList.size(); i++) {
     addspnList.add(getSPN((String) addspcList.get(i)));
     }
     }

     if (notid != null) {
     //superNote = ServiceHistoryData.getSupersseionNote(notid, st);
     }

     if (superNote != null) {
     promDetails.append(superNote);
     }

     if (notType != null && notType.equalsIgnoreCase("PROMOTION")) {
     if (promoForm.getChkProformaPromotion() != null) {
     promDetails.append(" ALLOWED PROFORMA PROMOTION ");
     } else if (promoForm.getChkRetroPromotion() != null) {
     promDetails.append(" ALLOWED RETROSPECTIVE PROMOTION ");
     } else {
     promDetails.append(" PROMOTED ");
     }
     if (promoForm.getSltGrade() != null && !promoForm.getSltGrade().equalsIgnoreCase("")) {
     promDetails.append(" TO ").append(promoForm.getSltGrade());
     }
     if (promoForm.getSltCadre() != null && !promoForm.getSltCadre().equals("")) {
     promDetails.append(" IN ").append(getCadreName(promoForm.getSltCadre()));
     }
     if (promoForm.getSltGenericPost() != null && !promoForm.getSltGenericPost().equals("")) {
     promDetails.append(" AND POSTED AS ").append(getPostName(promoForm.getSltGenericPost()));
     if (promoForm.getSltPostingDept() != null && !promoForm.getSltPostingDept().equals("")) {
     promDetails.append(", ").append(getDeptName(promoForm.getSltPostingDept())).append(" DEPARTMENT ");
     }
     } else if (promoForm.getChkJoinedAsSuch() != null) {
     if (promoForm.getChkJoinedAsSuch().equalsIgnoreCase("Y")) {
     promDetails.append(" AND JOINED AS SUCH ");
     }
     }
     if (promoForm.getTxtCadreJoiningWEFDt() != null && !promoForm.getTxtCadreJoiningWEFDt().equals("")) {
     promDetails.append(" WEF ").append(promoForm.getTxtCadreJoiningWEFDt());
     }
     if (promoForm.getSltCadreJoiningWEFTime() != null && !promoForm.getSltCadreJoiningWEFTime().equals("")) {
     promDetails.append(", ").append(promoForm.getSltCadreJoiningWEFTime());
     }

     if (notString != null) {
     promDetails.append(notString);
     }

     if (addspnList.size() != 0) {
     promDetails.append(" ALONG WITH ");
     for (int i = 0; i < addspnList.size(); i++) {
     if (i == addspnList.size() - 1) {
     promDetails.append(addspnList.get(i));
     } else {
     promDetails.append(addspnList.get(i)).append(", ");
     }
     }
     }

     } else if (notType != null && notType.equalsIgnoreCase("REPATRIATION")) {
     promDetails.append(" ON REPATRIATION/ REVERSION REPORTED UNDER THE CADRE ");
     if (promoForm.getTxtCadreJoiningWEFDt() != null && !promoForm.getTxtCadreJoiningWEFDt().equals("")) {
     promDetails.append(" ON ").append(promoForm.getTxtCadreJoiningWEFDt());
     }
     if (authName != null && !authName.trim().equals("")) {
     promDetails.append(" IN PURSUANCE OF ").append(authName);
     } else if (offName != null && !offName.trim().equals("")) {
     promDetails.append(" IN PURSUANCE OF ").append(offName);
     } else if (deptName != null && !deptName.trim().equals("")) {
     promDetails.append(" IN PURSUANCE OF ").append(deptName).append(" DEPARTMENT ");
     }
     if ((authName != null && !authName.trim().equals("")) || (offName != null && !offName.trim().equals(""))
     || (deptName != null && !deptName.trim().equals(""))) {
     if (notBean.getOrdno() != null && !notBean.getOrdno().trim().equals("")) {
     promDetails.append(" NOTIFICATION/ OFFICE ORDER NO. ").append(notBean.getOrdno());
     if (notBean.getTxtNotOrdDt()!= null && !notBean.getTxtNotOrdDt().equals("")) {
     promDetails.append(" DATED ").append(notBean.getTxtNotOrdDt());
     }
     } else {
     promDetails.append(" ORDER");
     if (notBean.getTxtNotOrdDt() != null && !notBean.getTxtNotOrdDt().equals("")) {
     promDetails.append(" DATED ").append(notBean.getTxtNotOrdDt());
     }
     }
     } else {
     if (notBean.getOrdno() != null && !notBean.getOrdno().trim().equals("")) {
     promDetails.append(" NOTIFICATION/ OFFICE ORDER NO. ").append(notBean.getOrdno().toUpperCase());
     if (notBean.getTxtNotOrdDt() != null && !notBean.getTxtNotOrdDt().trim().equals("")) {
     promDetails.append(" DATED ").append(notBean.getTxtNotOrdDt().toUpperCase());
     }
     } else if (notBean.getTxtNotOrdDt() != null && !notBean.getTxtNotOrdDt().trim().equals("")) {
     promDetails.append(" NOTIFICATION/ OFFICE ORDER, DATED ").append(notBean.getTxtNotOrdDt().toUpperCase());
     }

     }
     } else if (notType != null && (notType.equalsIgnoreCase("JOIN_CADRE") || notType.equalsIgnoreCase("RELIEVE_CADRE"))) {

     if (notType.equalsIgnoreCase("JOIN_CADRE")) {
     promDetails.append(" JOINED ");
     } else if (notType.equalsIgnoreCase("RELIEVE_CADRE")) {
     promDetails.append(" RELIEVED ");
     }
     if (promoForm.getSltCadre() != null && !promoForm.getSltCadre().equals("")) {
     promDetails.append(" FROM ").append(getCadreName(promoForm.getSltCadre()));
     if (promoForm.getSltDescription()!= null) {
     promDetails.append(" OF ").append((promoForm.getSltDescription()));
     }
     }
     if (promoForm.getSltGrade() != null && !promoForm.getSltGrade().equals("")) {
     promDetails.append(", ").append(promoForm.getSltGrade());
     }
     if (postClass1 != null) {
     promDetails.append(" ON ").append(postClass1);
     if (postStatus1 != null) {
     promDetails.append(" (").append(postStatus1).append(")");
     }
     }

     if (promoForm.getTxtCadreJoiningWEFDt() != null && !promoForm.getTxtCadreJoiningWEFDt().equals("")) {
     promDetails.append(" WEF ").append(promoForm.getTxtCadreJoiningWEFDt());
     if (promoForm.getSltCadreJoiningWEFTime() != null && !promoForm.getSltCadreJoiningWEFTime().equals("")) {

     promDetails.append(" (").append(promoForm.getSltCadreJoiningWEFTime()).append(")");
     }
     }
     if (notString != null) {
     promDetails.append(notString);
     }

     } else if (notType != null && notType.equalsIgnoreCase("OFFICIATE")) {
     promDetails.append(" ALLOWED TO OFFICIATE ");

     if (promoForm.getSltGrade() != null && !promoForm.getSltGrade().equals("")) {
     promDetails.append(" IN THE ").append(promoForm.getSltGrade());
     }
     if (promoForm.getSltCadre() != null && !promoForm.getSltCadre().equals("")) {
     promDetails.append(" OF ").append(getCadreName(promoForm.getSltCadre()));
     }
     if (promoForm.getTxtWEFDt() != null && !promoForm.getTxtWEFDt().equals("")) {
     promDetails.append(" WEF ").append(promoForm.getTxtWEFDt());

     if (promoForm.getSltWEFTime() != null && !promoForm.getSltWEFTime().equals("")) {
     promDetails.append(" (").append(promoForm.getSltWEFTime()).append(")");
     }
     }
     if (notString != null) {
     promDetails.append(notString);
     }
     }

     promDetails.append(". ");

     if ((promoForm.getTxtSP() != null && !promoForm.getTxtSP().trim().equals("") && !promoForm.getTxtSP().trim().equals("0"))
     || (promoForm.getTxtBasic() != null && !promoForm.getTxtBasic().trim().equals("") && !promoForm.getTxtBasic().trim().equals("0"))
     || (promoForm.getTxtGP() != null && !promoForm.getTxtGP().trim().equals("") && !promoForm.getTxtGP().trim().equals("0"))
     || (promoForm.getTxtPP() != null && !promoForm.getTxtPP().trim().equals("") && !promoForm.getTxtPP().trim().equals("0"))
     || (promoForm.getTxtOP() != null && !promoForm.getTxtOP().trim().equals("") && !promoForm.getTxtOP().trim().equals("0"))
     || (promoForm.getSltPayScale() != null && !promoForm.getSltPayScale().trim().equals("") && !promoForm.getSltPayScale().trim().equals("0"))) {

     promDetails.append(" ALLOWED TO DRAW ");

     if (promoForm.getTxtBasic() != null && !promoForm.getTxtBasic().trim().equals("") && !promoForm.getTxtBasic().trim().equals("0")) {
     promDetails.append(" PAY @ RS. ").append(CommonFunctions.formatNumber(promoForm.getTxtBasic())).append("/- PM ");
     }

     if (promoForm.getTxtGP() != null && !promoForm.getTxtGP().trim().equals("") && !promoForm.getTxtGP().trim().equals("0")) {
     promDetails.append(" GRADE PAY @ RS. ").append(CommonFunctions.formatNumber(promoForm.getTxtGP())).append("/- PM ");
     }
     if (promoForm.getSltPayScale() != null && !promoForm.getSltPayScale().trim().equals("")) {
     promDetails.append(" IN THE SCALE OF PAY RS. ").append(promoForm.getSltPayScale().toUpperCase()).append("/-");
     }
     if (promoForm.getTxtSP() != null && !promoForm.getTxtSP().trim().equals("") && !promoForm.getTxtSP().trim().equals("0")) {
     promDetails.append(" + SPECIAL PAY @ RS. ").append(CommonFunctions.formatNumber(promoForm.getTxtSP())).append("/- ");
     }
     if (promoForm.getTxtPP() != null && !promoForm.getTxtPP().trim().equals("") && !promoForm.getTxtPP().trim().equals("0")) {
     promDetails.append(" + PERSONAL PAY @ RS. ").append(CommonFunctions.formatNumber(promoForm.getTxtPP())).append("/- ");
     }
     if (promoForm.getTxtDescOP() != null && !promoForm.getTxtDescOP().trim().equals("")) {
     if (promoForm.getTxtOP() != null && !promoForm.getTxtOP().trim().equals("") && !promoForm.getTxtOP().trim().equals("0")) {
     promDetails.append(" ").append(promoForm.getTxtDescOP().toUpperCase()).append(" @ RS. ").append(CommonFunctions.formatNumber(promoForm.getTxtOP())).append("/- ");
     }
     } else {
     if (promoForm.getTxtOP() != null && !promoForm.getTxtOP().trim().equals("") && !promoForm.getTxtOP().trim().equals("0")) {
     promDetails.append(" OTHER PAY @ RS. ").append(CommonFunctions.formatNumber(promoForm.getTxtOP())).append("/- ");
     }
     }
     if ((promoForm.getTxtSP() != null && !promoForm.getTxtSP().trim().equals("") && !promoForm.getTxtSP().trim().equals("0"))
     || (promoForm.getTxtBasic() != null && !promoForm.getTxtBasic().trim().equals("") && !promoForm.getTxtBasic().trim().equals("0"))
     || (promoForm.getTxtGP() != null && !promoForm.getTxtGP().trim().equals("") && !promoForm.getTxtGP().trim().equals("0"))
     || (promoForm.getTxtPP() != null && !promoForm.getTxtPP().trim().equals("") && !promoForm.getTxtPP().trim().equals("0"))
     || (promoForm.getTxtOP() != null && !promoForm.getTxtOP().trim().equals("") && !promoForm.getTxtOP().trim().equals("0"))) {
     promDetails.append(" PM ");
     }
     if (promoForm.getTxtWEFDt() != null && !promoForm.getTxtWEFDt().trim().equals("")) {
     promDetails.append(" WEF ").append(promoForm.getTxtWEFDt());
     if (promoForm.getSltWEFTime() != null && !promoForm.getSltWEFTime().equals("")) {
     promDetails.append(" (" + promoForm.getSltWEFTime() + ")");
     }
     }
     promDetails.append(".");
     }
     

     } catch (Exception e) {
     e.printStackTrace();
     } finally {
     DataBaseFunctions.closeSqlObjects(con);

     }

     return promDetails.toString().toUpperCase();
     }*/
    @Override
    public String getJoinDetails(String joinId, String empid) {
        //String outputJoinDetails ="";

        String jspc = null;
        ArrayList joinspc = new ArrayList();
        ArrayList joinspn = new ArrayList();
        int not_id = 0;
        String emp_id = null;
        String ordNo = null;
        String ordDate = null;
        String joinDate = null;
        String notificationNo = null;
        String notificationDate = null;
        String joinTime = null;
        String authCode = null;
        String authName = null;
        String deptCode = null;
        String deptName = null;
        String offCode = null;
        String offName = null;
        String lcrId = null;
        String orgType = null;
        String joinSpcGOI = null;
        String creditedUJTFDate = null;
        String creditedUJTTDate = null;
        String unavailedDays = null;
        ResultSet rs1 = null;
        ResultSet rs = null;

        StringBuffer outputJoinDetails = new StringBuffer();
        String fieldoffcode = "";

        Connection con = null;
        PreparedStatement ps = null;

        try {

            con = dataSource.getConnection();
            ps = con.prepareStatement("SELECT * FROM EMP_JOIN WHERE JOIN_ID=? AND EMP_ID=? ");
            ps.setInt(1, Integer.parseInt(joinId));
            ps.setString(2, empid);
            rs = ps.executeQuery();

            while (rs.next()) {
                if (rs.getString("SPC") != null && !rs.getString("SPC").trim().equals("")) {
                    jspc = rs.getString("SPC");
                    joinspc.add(rs.getString("SPC"));
                }
                fieldoffcode = rs.getString("FIELD_OFF_CODE");
                if (rs.getDate("JOIN_DATE") != null) {
                    joinDate = CommonFunctions.getFormattedOutputDate1(rs.getDate("JOIN_DATE"));
                }
                if (rs.getString("EMP_ID") != null && !rs.getString("EMP_ID").trim().equals("")) {
                    emp_id = rs.getString("EMP_ID");
                }
                if (rs.getInt("NOT_ID") > 0) {
                    not_id = rs.getInt("NOT_ID");
                }
                if (rs.getString("JOIN_TIME") != null && !rs.getString("JOIN_TIME").trim().equals("")) {
                    joinTime = rs.getString("JOIN_TIME");
                }
                if (rs.getString("MEMO_NO") != null && !rs.getString("MEMO_NO").trim().equals("")) {
                    ordNo = rs.getString("MEMO_NO").toUpperCase();
                }
                if (rs.getDate("MEMO_DATE") != null) {
                    ordDate = CommonFunctions.getFormattedOutputDate1(rs.getDate("MEMO_DATE"));
                }
                if (rs.getString("LCR_ID") != null && !rs.getString("LCR_ID").trim().equals("")) {
                    lcrId = rs.getString("LCR_ID");
                }
                if (rs.getString("organization_type") != null && rs.getString("organization_type").equals("GOI")) {
                    orgType = rs.getString("organization_type");
                    joinSpcGOI = rs.getString("SPC");

                }
            }
            DataBaseFunctions.closeSqlObjects(rs, ps);

            ps = con.prepareStatement("SELECT ORDNO,ORDDT,DEPT_CODE,OFF_CODE,AUTH FROM EMP_NOTIFICATION WHERE NOT_ID=? AND EMP_ID=? ");
            ps.setInt(1, not_id);
            ps.setString(2, empid);
            rs1 = ps.executeQuery();
            while (rs1.next()) {
                if (rs1.getString("ORDNO") != null && !rs1.getString("ORDNO").trim().equals("")) {
                    notificationNo = rs1.getString("ORDNO");
                }
                if (rs1.getDate("ORDDT") != null) {
                    notificationDate = CommonFunctions.getFormattedOutputDate1(rs1.getDate("ORDDT"));
                }
                if (rs1.getString("DEPT_CODE") != null && !rs1.getString("DEPT_CODE").trim().equals("")) {
                    deptCode = rs1.getString("DEPT_CODE");
                }
                if (rs1.getString("OFF_CODE") != null && !rs1.getString("OFF_CODE").trim().equals("")) {
                    offCode = rs1.getString("OFF_CODE");
                }
                if (rs1.getString("AUTH") != null && !rs1.getString("AUTH").trim().equals("")) {
                    authCode = rs1.getString("AUTH");
                }
            }
            DataBaseFunctions.closeSqlObjects(rs1, ps);

            if (authCode != null && !authCode.equalsIgnoreCase("")) {
                authName = getSPN(authCode);
            }
            if (offCode != null && !offCode.equalsIgnoreCase("")) {
                offName = getOffName(offCode);
            }
            if (deptCode != null && !deptCode.equalsIgnoreCase("")) {
                deptName = getDeptName(deptCode);
            }
            //                      jspn = ServiceHistoryReport.getSPN(jspc,st);
            if (joinspc.size() > 0) {
                for (int i = 0; i < joinspc.size(); i++) {
                    joinspn.add((String) getSPN((String) joinspc.get(i)));
                }
            }
            if (authName != null && !authName.trim().equals("")) {
                outputJoinDetails.append("IN PURSUANCE OF ").append(authName).append(" ");
            } else if (offName != null && !offName.trim().equals("")) {
                outputJoinDetails.append("IN PURSUANCE OF ").append(offName).append(" ");
            } else if (deptName != null && !deptName.trim().equals("")) {
                outputJoinDetails.append("IN PURSUANCE OF ").append(deptName).append(" DEPARTMENT ");
            } else if (orgType != null && orgType.equals("GOI")) {
                outputJoinDetails.append("IN PURSUANCE OF ").append("GOVERNMENT OF INDIA ");
            }
            if ((authName != null && !authName.trim().equals("")) || (offName != null && !offName.trim().equals("")) || (deptName != null && !deptName.trim().equals("")) || (orgType != null && orgType.equals("GOI"))) {
                if (notificationNo != null && !notificationNo.trim().equals("")) {
                    outputJoinDetails.append(" NOTIFICATION NO. ").append(notificationNo);
                    if (notificationDate != null && !notificationDate.trim().equals("")) {
                        outputJoinDetails.append(" DATED ").append(notificationDate).append(", ");
                    }
                }
            } else {
                if (notificationNo != null && !notificationNo.trim().equals("")) {
                    outputJoinDetails.append("IN PURSUANCE OF NOTIFICATION NO. ").append(notificationNo);
                    if (notificationDate != null && !notificationDate.trim().equals("")) {
                        outputJoinDetails.append(" DATED ").append(notificationDate).append(", ");
                    }
                }
            }
            if (joinspn.size() > 0 && (joinSpcGOI == null || joinSpcGOI.equals(""))) {
                outputJoinDetails.append(" JOINED AS ");
                for (int j = 0; j < joinspn.size(); j++) {
                    if (j == 0) {
                        outputJoinDetails.append((String) joinspn.get(j));
                    } else {
                        outputJoinDetails.append(", ").append((String) joinspn.get(j));
                    }
                }
            } else {
                if (joinSpcGOI != null && !joinSpcGOI.equals("")) {
                    String goiSpn = getOtherSpn(joinSpcGOI);
                    outputJoinDetails.append(" JOINED AS ").append(goiSpn);
                }
            }
            if (fieldoffcode != null && !fieldoffcode.equals("")) {
                String fieldoffname = getOffName(fieldoffcode);
                outputJoinDetails.append("( " + fieldoffname + " )");
            }

            if (joinDate != null && !joinDate.trim().equals("")) {

                outputJoinDetails.append(" ON ").append(joinDate);
                if (joinTime != null && !joinTime.trim().equals("")) {
                    outputJoinDetails.append("(").append(joinTime).append(")");
                }

            }

            if (ordNo != null && !ordNo.trim().equals("")) {
                if (!ordNo.trim().equalsIgnoreCase(notificationNo)) {
                    outputJoinDetails.append(" VIDE ORDER NO. ").append(ordNo);
                    if (ordDate != null && !ordDate.trim().equals("")) {
                        outputJoinDetails.append(" DATED ").append(ordDate);
                    }
                }
            }
            outputJoinDetails.append(".");
            if (lcrId != null) {

                //ps = con.prepareStatement("SELECT SP_FROM,SP_TO, (DECODE(SP_TO,NULL,SP_FROM,SP_TO)-SP_FROM)+1 NODAYS FROM EMP_LEAVE_CR WHERE LCR_ID=? AND SP_FROM IS NOT NULL");
                ps = con.prepareStatement("SELECT SP_FROM,SP_TO, ((CASE WHEN SP_TO IS NULL THEN SP_FROM ELSE SP_TO END)-SP_FROM) + interval '1 day' AS NODAYS FROM EMP_LEAVE_CR WHERE LCR_ID=? AND SP_FROM IS NOT NULL");
                ps.setString(1, lcrId);

                rs1 = ps.executeQuery();

                while (rs1.next()) {
                    if (rs1.getDate("SP_FROM") != null && !rs1.getString("SP_FROM").trim().equals("")) {
                        creditedUJTFDate = CommonFunctions.getFormattedOutputDate1(rs1.getDate("SP_FROM"));
                    }
                    if (rs1.getDate("SP_TO") != null && !rs1.getString("SP_TO").trim().equals("")) {
                        creditedUJTTDate = CommonFunctions.getFormattedOutputDate1(rs1.getDate("SP_TO"));
                    }
                    if (rs1.getString("NODAYS") != null && !rs1.getString("NODAYS").trim().equals("")) {
                        unavailedDays = rs1.getString("NODAYS");
                    }
                }
                DataBaseFunctions.closeSqlObjects(rs1, ps);

                if (unavailedDays != null) {
                    if (unavailedDays.equals("1")) {
                        outputJoinDetails.append(" THE UNAVAILED JOINING TIME FOR A PERIOD OF 1 DAY ON ").append(creditedUJTFDate);
                    } else {
                        outputJoinDetails.append(" THE UNAVAILED JOINING TIME FOR A PERIOD OF ").append(unavailedDays).append(" DAYS FROM ").append(creditedUJTFDate).append(" TO ").append(creditedUJTTDate);
                    }
                } else {
                    outputJoinDetails.append(" THE UNAVAILED JOINING TIME ");
                }
                outputJoinDetails.append(" IS CREDITED TO THE EARNED LEAVE ACCOUNT.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs1, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return outputJoinDetails.toString().toUpperCase();
    }

    @Override
    public String getRelieveDetails(int notId, String empid) {

        String rspc = null;
        String rspn = null;
        String jspc = null;
        String jspn = null;
        String rDate = null;
        String rTime = null;
        String auth = null;
        String authName = null;
        String deptCode = null;
        String deptName = null;
        String offCode = null;
        String offName = null;
        String ordNo = null;
        String ordDate = null;
        String notificationNo = null;
        String notificationDate = null;
        int not_id = 0;
        String emp_id = null;
        String ifRelnq = null;

        StringBuffer outputRelieveDetails = new StringBuffer();

        Connection con = null;
        PreparedStatement ps = null;

        ResultSet rs = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;

        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("SELECT * FROM  EMP_RELIEVE WHERE NOT_ID=? AND EMP_ID=? ");
            ps.setInt(1, notId);
            ps.setString(2, empid);
            rs = ps.executeQuery();

            while (rs.next()) {
                if (rs.getString("SPC") != null && !rs.getString("SPC").equals("")) {
                    rspc = rs.getString("SPC");
                }
                not_id = rs.getInt("NOT_ID");
                emp_id = rs.getString("EMP_ID");
                if (rs.getDate("RLV_DATE") != null) {
                    rDate = CommonFunctions.getFormattedOutputDate1(rs.getDate("RLV_DATE"));
                }
                if (rs.getString("RLV_TIME") != null && !rs.getString("RLV_TIME").equals("")) {
                    rTime = rs.getString("RLV_TIME");
                }
                if (rs.getString("MEMO_NO") != null && !rs.getString("MEMO_NO").equals("")) {
                    ordNo = rs.getString("MEMO_NO").toUpperCase();
                }
                if (rs.getDate("MEMO_DATE") != null) {
                    ordDate = CommonFunctions.getFormattedOutputDate1(rs.getDate("MEMO_DATE"));
                }
                if (rs.getString("IF_RLNQ") != null && !rs.getString("IF_RLNQ").equals("")) {
                    ifRelnq = rs.getString("IF_RLNQ");
                }
            }
            DataBaseFunctions.closeSqlObjects(rs, ps);

            ps = con.prepareStatement("SELECT ORDNO,ORDDT,DEPT_CODE,OFF_CODE,AUTH FROM EMP_NOTIFICATION WHERE NOT_ID=? AND EMP_ID=?");
            ps.setInt(1, notId);
            ps.setString(2, empid);
            rs1 = ps.executeQuery();

            //rs1 = ps.executeQuery();
            while (rs1.next()) {
                if (rs1.getString("ORDNO") != null && !rs1.getString("ORDNO").trim().equals("")) {
                    notificationNo = rs1.getString("ORDNO");
                }
                if (rs1.getDate("ORDDT") != null && !rs1.getDate("ORDDT").equals("")) {
                    notificationDate = CommonFunctions.getFormattedOutputDate1(rs1.getDate("ORDDT"));
                }
                if (rs1.getString("DEPT_CODE") != null && !rs1.getString("DEPT_CODE").trim().equals("")) {
                    deptCode = rs1.getString("DEPT_CODE");
                }
                if (rs1.getString("OFF_CODE") != null && !rs1.getString("OFF_CODE").trim().equals("")) {
                    offCode = rs1.getString("OFF_CODE");
                }
                if (rs1.getString("AUTH") != null && !rs1.getString("AUTH").trim().equals("")) {
                    auth = rs1.getString("AUTH");
                }
            }
            DataBaseFunctions.closeSqlObjects(rs1, ps);

            if (auth != null) {
                authName = getSPN(auth);
            }
            if (offCode != null && !offCode.equalsIgnoreCase("")) {
                offName = getOffName(offCode);
            } else if (deptCode != null && !deptCode.equalsIgnoreCase("")) {
                deptName = getDeptName(deptCode);
            }

            ps = con.prepareStatement("SELECT SPC FROM EMP_JOIN WHERE NOT_ID=? AND EMP_ID=?");
            ps.setInt(1, notId);
            ps.setString(2, empid);

            rs2 = ps.executeQuery();
            while (rs2.next()) {
                jspc = rs2.getString("SPC");
            }
            DataBaseFunctions.closeSqlObjects(rs2, ps);
            rspn = getSPN(rspc);
            jspn = getSPN(jspc);

            if (authName != null) {
                //outputRelieveDetails = outputRelieveDetails+" IN PURSUANCE OF " + authName+" ";
                outputRelieveDetails.append(" IN PURSUANCE OF ").append(authName).append(" ");
            } else if (offName != null) {
                //outputRelieveDetails = outputRelieveDetails+" IN PURSUANCE OF " + offName+" ";
                outputRelieveDetails.append(" IN PURSUANCE OF ").append(offName).append(" ");
            } else if (offName == null && deptName != null) {
                //outputRelieveDetails = outputRelieveDetails+" IN PURSUANCE OF " + deptName + " DEPARTMENT ";
                outputRelieveDetails.append(" IN PURSUANCE OF ").append(deptName).append(" DEPARTMENT ");

            }
            if (authName != null || offName != null || deptName != null) {
                if (notificationNo != null) {
                    //outputRelieveDetails =outputRelieveDetails+ " NOTIFICATION NO. "+notificationNo;
                    outputRelieveDetails.append(" NOTIFICATION NO. ").append(notificationNo);
                    if (notificationDate != null) {
                        //outputRelieveDetails =outputRelieveDetails+" DATED "+notificationDate+", ";
                        outputRelieveDetails.append(" DATED ").append(notificationDate).append(", ");
                    }
                }
            } else {
                if (notificationNo != null) {
                    //outputRelieveDetails =outputRelieveDetails+ " IN PURSUANCE OF NOTIFICATION NO. "+notificationNo;
                    outputRelieveDetails.append(" IN PURSUANCE OF NOTIFICATION NO. ").append(notificationNo);
                    if (notificationDate != null) {
                        //outputRelieveDetails =outputRelieveDetails+" DATED "+notificationDate+", ";
                        outputRelieveDetails.append(" DATED ").append(notificationDate).append(", ");
                    }
                }
            }
            if (ifRelnq != null) {
                if (ifRelnq.equalsIgnoreCase("Y")) {
                    //outputRelieveDetails = outputRelieveDetails+" RELINQUISHED THE CHARGE OF ";
                    outputRelieveDetails.append(" RELINQUISHED THE CHARGE OF ");
                } else if (ifRelnq.equalsIgnoreCase("N")) {
                    //outputRelieveDetails = outputRelieveDetails+" RELIEVED FROM ";
                    outputRelieveDetails.append(" RELIEVED FROM ");
                }
            } else {
                //outputRelieveDetails = outputRelieveDetails+" RELINQUISHED/RELIEVED THE CHARGE OF";
                outputRelieveDetails.append(" RELINQUISHED/RELIEVED THE CHARGE OF");
            }
            if (rspn != null && !rspn.trim().equals("")) {
                //outputRelieveDetails = outputRelieveDetails+" "+rspn;
                outputRelieveDetails.append(" ").append(rspn);
            }
            if (rDate != null) {
                //outputRelieveDetails = outputRelieveDetails+" ON "+rDate;
                outputRelieveDetails.append(" ON ").append(rDate);
                if (rTime != null && !rTime.trim().equals("")) {
                    //outputRelieveDetails = outputRelieveDetails+"("+rTime+")";
                    outputRelieveDetails.append("(").append(rTime).append(")");
                }
            }

            if (jspn != null && !jspn.trim().equals("")) {
                //outputRelieveDetails = outputRelieveDetails+", IN ORDER TO JOIN AS "+jspn;
                outputRelieveDetails.append(", IN ORDER TO JOIN AS ").append(jspn);
            }
            if (ordNo != null) {
                if (!ordNo.trim().equalsIgnoreCase(notificationNo)) {
                    //outputRelieveDetails =outputRelieveDetails+" VIDE ORDER NO. "+ordNo;
                    outputRelieveDetails.append(" VIDE ORDER NO. ").append(ordNo);

                    if (ordDate != null) {
                        //outputRelieveDetails =outputRelieveDetails+" DATED "+ordDate;
                        outputRelieveDetails.append(" DATED ").append(ordDate);
                    }
                }
            }
            //outputRelieveDetails = outputRelieveDetails+".";
            outputRelieveDetails.append(".");
//                   
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, rs1, rs2);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return outputRelieveDetails.toString().toUpperCase();
    }

    @Override
    public String getIncrementSanctionDetails(IncrementForm incfb, int notid, String payCommission) {
        StringBuffer outputIncrDetails = new StringBuffer();
        String wefd = null;
        String weft = null;
        String payScale = null;

        double newBasic = 0;
        double gradePay = 0;
        double incrAmt = 0;
        double prevBasic = 0;
        double othPay = 0;
        double perPay = 0;
        double spPay = 0;
        String otherDesc = null;
        String firstInc = null;
        String secInc = null;
        String thirdInc = null;
        String fourthInc = null;
        String incType = null;
        String incrementName = null;
        String superNote = null;
        String notficationString = "";

        String authName = null;
        String offName = null;
        String deptName = null;
        String othSpn = "";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = dataSource.getConnection();

            othSpn = getOtherSpn(incfb.getHidSancAuthorityOthSpc());

            if (!othSpn.equals("")) {
                authName = othSpn;
            } else {
                ps = con.prepareStatement("select getdeptname('" + incfb.getDeptCode() + "') deptName, getofficeen('" + incfb.getHidOffCode() + "') offName,  getspn('" + incfb.getHidSpc() + "') authName ");
                rs = ps.executeQuery();
                if (rs.next()) {
                    deptName = rs.getString("deptName");
                    offName = rs.getString("offName");
                    authName = rs.getString("authName");
                }
                DataBaseFunctions.closeSqlObjects(rs, ps);
            }

            notficationString = getNotificationString(incfb.getTxtSanctionOrderNo(), incfb.getTxtSanctionOrderDt(), deptName, offName, authName);

            if (incfb.getTxtIncrAmt() != null && !incfb.getTxtIncrAmt().equals("") && !incfb.getTxtIncrAmt().equals("0"))//EMP_PAY_RECORD
            {
                incrAmt = Double.parseDouble(incfb.getTxtIncrAmt());
            }

            if (incfb.getIncrementLvl() != null && !incfb.getIncrementLvl().equals("")) {

                if (incfb.getIncrementLvl().equalsIgnoreCase("1")) {
                    firstInc = incfb.getIncrementLvl();
                } else if (incfb.getIncrementLvl().equalsIgnoreCase("2")) {
                    secInc = incfb.getIncrementLvl();
                } else if (incfb.getIncrementLvl().equalsIgnoreCase("3")) {
                    thirdInc = incfb.getIncrementLvl();
                } else if (incfb.getIncrementLvl().equalsIgnoreCase("4")) {
                    fourthInc = incfb.getIncrementLvl();
                }

            }

            if (incfb.getTxtWEFDt() != null)//EMP_PAY_RECORD
            {
                wefd = incfb.getTxtWEFDt();
            }
            if (incfb.getTxtWEFTime() != null && !incfb.getTxtWEFTime().trim().equals("")) {
                weft = incfb.getTxtWEFTime();//EMP_PAY_RECORD
            }
            if (incfb.getSltPayScale() != null && !incfb.getSltPayScale().trim().equals("")) {
                payScale = incfb.getSltPayScale().toUpperCase();//EMP_PAY_RECORD
            }
            if (!incfb.getTxtNewBasic().equals("0") && incfb.getTxtNewBasic() != null && !incfb.getTxtNewBasic().trim().equals(""))//EMP_PAY_RECORD
            {
                newBasic = Double.parseDouble(incfb.getTxtNewBasic());//EMP_PAY_RECORD
            }
            //newBasic1 = rs2.getString("BASIC");//EMP_PAY_RECORD
            if (incfb.getTxtGradePay() != null && !incfb.getTxtGradePay().trim().equals("") && !incfb.getTxtGradePay().equals("0"))//EMP_PAY_RECORD
            {
                gradePay = Double.parseDouble(incfb.getTxtGradePay());
            }
            if (incfb.getTxtSPay() != null && !incfb.getTxtSPay().equals("0") && !incfb.getTxtSPay().trim().equals(""))//EMP_PAY_RECORD
            {
                spPay = Double.parseDouble(incfb.getTxtSPay());
            }
            if (incfb.getTxtP_pay() != null && !incfb.getTxtP_pay().equals("0") && !incfb.getTxtP_pay().trim().equals(""))//EMP_PAY_RECORD
            {
                perPay = Double.parseDouble(incfb.getTxtP_pay());
            }
            if (incfb.getTxtOthPay() != null && !incfb.getTxtOthPay().equals("0") && !incfb.getTxtOthPay().trim().equals(""))//EMP_PAY_RECORD
            {
                othPay = Double.parseDouble(incfb.getTxtOthPay());
            }
            if (incfb.getTxtDescOth() != null && incfb.getTxtDescOth().trim().equals(""))//EMP_PAY_RECORD
            {
                otherDesc = incfb.getTxtDescOth();
            }

            if (incfb.getIncrementType() != null) {
                if (incfb.getIncrementType().equalsIgnoreCase("S")) {
                    incrementName = " STAGNATION INCREMENT ";
                } else if (incfb.getIncrementType().equalsIgnoreCase("A")) {
                    incrementName = " ANNUAL INCREMENT ";
                } else if (incfb.getIncrementType().equalsIgnoreCase("D")) {
                    incrementName = " ADVANCE INCREMENT ";
                } else if (incfb.getIncrementType().equalsIgnoreCase("T")) {
                    incrementName = " ANTEDATED INCREMENT ";
                } else if (incfb.getIncrementType().equalsIgnoreCase("P")) {
                    incrementName = " PREVIOUS INCREMENT ";
                }
            }

            //if (notid != null) {
            //superNote = ServiceHistoryData.getSupersseionNote(notid, st);
            //}
            if (superNote != null) {
                outputIncrDetails.append(superNote);
            }

            outputIncrDetails.append(" ALLOWED ");

            if (firstInc != null && firstInc.equalsIgnoreCase("Y") && !secInc.equalsIgnoreCase("Y") && thirdInc != null && !thirdInc.equalsIgnoreCase("Y") && fourthInc != null && !fourthInc.equalsIgnoreCase("Y")) {
                incType = " 1ST " + incrementName;
                prevBasic = newBasic - incrAmt;
            } else if (firstInc != null && !firstInc.equalsIgnoreCase("Y") && secInc != null && secInc.equalsIgnoreCase("Y") && thirdInc != null && !thirdInc.equalsIgnoreCase("Y") && fourthInc != null && !fourthInc.equalsIgnoreCase("Y")) {
                incType = " 2ND " + incrementName;
                prevBasic = newBasic - incrAmt;
            } else if (firstInc != null && firstInc.equalsIgnoreCase("Y") && secInc != null && secInc.equalsIgnoreCase("Y") && thirdInc != null && !thirdInc.equalsIgnoreCase("Y") && fourthInc != null && !fourthInc.equalsIgnoreCase("Y")) {
                incType = " 1ST & 2ND " + incrementName;
                prevBasic = newBasic - 2 * (incrAmt);
            } else if (firstInc != null && !firstInc.equalsIgnoreCase("Y") && secInc != null && !secInc.equalsIgnoreCase("Y") && thirdInc != null && thirdInc.equalsIgnoreCase("Y") && fourthInc != null && !fourthInc.equalsIgnoreCase("Y")) {
                incType = " 3RD " + incrementName;
                prevBasic = newBasic - incrAmt;
            } else if (firstInc != null && !firstInc.equalsIgnoreCase("Y") && secInc != null && secInc.equalsIgnoreCase("Y") && thirdInc != null && thirdInc.equalsIgnoreCase("Y") && fourthInc != null && !fourthInc.equalsIgnoreCase("Y")) {
                incType = " 2ND & 3RD " + incrementName;
                prevBasic = newBasic - 2 * (incrAmt);
            } else if (firstInc != null && !firstInc.equalsIgnoreCase("Y") && secInc != null && !secInc.equalsIgnoreCase("Y") && thirdInc != null && !thirdInc.equalsIgnoreCase("Y") && fourthInc != null && fourthInc.equalsIgnoreCase("Y")) {
                incType = " 4TH " + incrementName;
                prevBasic = newBasic - incrAmt;
            } else if (firstInc != null && !firstInc.equalsIgnoreCase("Y") && secInc != null && !secInc.equalsIgnoreCase("Y") && thirdInc != null && thirdInc.equalsIgnoreCase("Y") && fourthInc != null && fourthInc.equalsIgnoreCase("Y")) {
                incType = " 3RD & 4TH " + incrementName;
                prevBasic = newBasic - 2 * (incrAmt);
            } else if (firstInc != null && firstInc.equalsIgnoreCase("Y") && secInc != null && secInc.equalsIgnoreCase("Y") && thirdInc != null && thirdInc.equalsIgnoreCase("Y") && fourthInc != null && !fourthInc.equalsIgnoreCase("Y")) {
                incType = " 1ST, 2ND & 3RD " + incrementName;
                prevBasic = newBasic - 3 * (incrAmt);
            } else if (firstInc != null && !firstInc.equalsIgnoreCase("Y") && secInc != null && secInc.equalsIgnoreCase("Y") && thirdInc != null && thirdInc.equalsIgnoreCase("Y") && fourthInc != null && fourthInc.equalsIgnoreCase("Y")) {
                incType = " 2ND, 3RD & 4TH " + incrementName;
                prevBasic = newBasic - 3 * (incrAmt);
            } else if (firstInc != null && firstInc.equalsIgnoreCase("Y") && secInc != null && secInc.equalsIgnoreCase("Y") && thirdInc != null && thirdInc.equalsIgnoreCase("Y") && fourthInc != null && fourthInc.equalsIgnoreCase("Y")) {
                incType = " 1ST, 2ND, 3RD & 4TH " + incrementName;
                prevBasic = newBasic - 4 * (incrAmt);
            } else {
                incType = incrementName;
                prevBasic = newBasic - incrAmt;
            }
            if (incType != null) {
                outputIncrDetails.append(incType);
            }
            if (incfb.getRdoPaycomm() != null && incfb.getRdoPaycomm().equals("REM")) {
                outputIncrDetails.append(" CONSOLIDATED REMUNERATION ");
                if (incfb.getContRemAmount() != null && !incfb.getContRemAmount().equals("")) {
                    outputIncrDetails.append(" TO @ Rs. ").append(CommonFunctions.formatNumber(incfb.getContRemAmount())).append("/-");
                }
                if (incfb.getContRemStages() != null && !incfb.getContRemStages().equals("")) {
                    outputIncrDetails.append(" IN Stage-").append(incfb.getContRemStages());
                }
            }

            if (incrAmt != 0) {

                outputIncrDetails.append("@ Rs. ").append(CommonFunctions.formatNumber(incrAmt)).append("/-");
                if (incType != null && !incType.equalsIgnoreCase(" ANNUAL INCREMENT ") && !incType.equalsIgnoreCase(" 1ST " + incrementName) && !incType.equalsIgnoreCase(" 2ND " + incrementName) && !incType.equalsIgnoreCase(" 3RD " + incrementName) && !incType.equalsIgnoreCase(" 4TH " + incrementName) && !incType.equalsIgnoreCase(" STAGNATION INCREMENT ") && !incType.equalsIgnoreCase(" ANTEDATED INCREMENT ") && !incType.equalsIgnoreCase(" PREVIOUS INCREMENT ")) {
                    outputIncrDetails.append(" EACH ");
                }
                if (prevBasic != 0 && newBasic != 0) {
                    outputIncrDetails.append(" RAISING PAY FROM Rs. ").append(CommonFunctions.formatNumber(prevBasic)).append("/- TO Rs. ").append(CommonFunctions.formatNumber(newBasic)).append("/- PM ");
                }
                if (payCommission.equals("6")) {
                    // RS. 10000-325-15200/- WEF 29-APR-2020, FN
                    payScale = " IN THE SCALE OF PAY RS. " + incfb.getSltPayScale() + " ";

                } else if (payCommission.equals("7")) {
                    payScale = " IN CELL " + incfb.getPayCell() + " OF LEVEL " + incfb.getPayLevel() + " ";
                }
                if (payScale != null && !payScale.equalsIgnoreCase("") && !payScale.trim().equalsIgnoreCase("0")) {
                    outputIncrDetails.append(payScale);
                }

            }
            if (wefd != null && !wefd.trim().equals("")) {
                outputIncrDetails.append(" WEF ").append(wefd);
                if (weft != null && !weft.trim().equals("")) {
                    outputIncrDetails.append(", ").append(weft);
                }
            }
            if (gradePay != 0) {
                outputIncrDetails.append("  GRADE PAY Rs. ").append(gradePay).append("/-");
            }

            if (perPay != 0) {
                outputIncrDetails.append(" + PERSONAL PAY RS.").append(CommonFunctions.formatNumber(perPay)).append("/-");
            }

            if (spPay != 0) {
                outputIncrDetails.append(" + SPECIAL PAY RS.").append(CommonFunctions.formatNumber(spPay)).append("/-");
            }
            if (otherDesc != null && !otherDesc.equalsIgnoreCase("")) {
                if (othPay != 0) {
                    outputIncrDetails.append(" + ").append(otherDesc).append(" ").append(CommonFunctions.formatNumber(othPay));
                }
            } else {
                if (othPay != 0) {
                    outputIncrDetails.append(" + OTHER PAY RS.").append(CommonFunctions.formatNumber(othPay)).append("/-");
                }
            }

            if (perPay != 0 || gradePay != 0 || spPay != 0 || othPay != 0) {
                outputIncrDetails.append(" PM, ");
            }
            if (notficationString != null) {
                outputIncrDetails.append(notficationString);
            }

            if (outputIncrDetails.toString().trim().equals("")) {
                outputIncrDetails.append("INCREMENT");
            }
            outputIncrDetails.append(".");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return outputIncrDetails.toString().toUpperCase();
    }

    @Override
    public NotificationBean getNotificationDetails(int notId, String notType) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = null;

        NotificationBean nfb = new NotificationBean();
        try {
            sql = "SELECT NOT_ID,NOT_TYPE,EMP_ID,DOE,ORDNO,ORDDT,DEPT_CODE,OFF_CODE,AUTH,NOTE,TOE"
                    + ",IF_ASSUMED,IF_VISIBLE,ENT_DEPT,ENT_OFF,ENT_AUTH,ACS"
                    + ",ASCS FROM EMP_NOTIFICATION WHERE NOT_ID=? AND NOT_TYPE=?";
            con = this.dataSource.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, notId);
            ps.setString(2, notType);
            rs = ps.executeQuery();
            if (rs.next()) {
                nfb.setNotid(notId);
                nfb.setDateofEntry(rs.getDate("DOE"));
                nfb.setDeptCode(rs.getString("DEPT_CODE"));
                nfb.setEmpId(rs.getString("EMP_ID"));
                nfb.setEntryAuthCode(rs.getString("ENT_AUTH"));
                nfb.setEntryDeptCode(rs.getString("ENT_DEPT"));
                nfb.setEntryOffCode(rs.getString("ENT_OFF"));
                nfb.setIfAssumed(rs.getString("IF_ASSUMED"));
                nfb.setIfVisible(rs.getString("IF_VISIBLE"));
                nfb.setNote(rs.getString("NOTE"));
                nfb.setNottype(rs.getString("NOT_TYPE"));
                nfb.setOffCode(rs.getString("OFF_CODE"));
                nfb.setOrdDate(rs.getDate("ORDDT"));
                nfb.setTxtNotOrdDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("orddt")));
                nfb.setOrdno(rs.getString("ORDNO"));
                nfb.setSancAuthCode(rs.getString("AUTH"));
                nfb.setSancDeptCode(rs.getString("DEPT_CODE"));
                nfb.setSancOffCode(rs.getString("OFF_CODE"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return nfb;
    }

    @Override
    public String getTransferDetails(TransferForm tcfb, int notid, String notType) {
        StringBuffer outputTransferDetails = new StringBuffer();
        String nxtSpn = null;
        String superNote = null;
        ArrayList addspcList = new ArrayList();
        ArrayList addspnList = new ArrayList();
        ResultSet rs1 = null;
        String proNoun = null;
        String offName = null;
        String deptName = null;
        String authName = null;
        String notString = "";
        String othSpn = "";
        //String othNextSpn = "";
        String fieldoffname = "";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            con = dataSource.getConnection();

            if (tcfb.getHidSanctioningOthSpc() != null && !tcfb.getHidSanctioningOthSpc().equals("")) {
                othSpn = getOtherSpn(tcfb.getHidSanctioningOthSpc());
                authName = othSpn;
            } else {
                ps = con.prepareStatement("select getdeptname('" + tcfb.getHidAuthDeptCode() + "') deptName, getofficeen('" + tcfb.getHidAuthOffCode() + "') offName,  getspn('" + tcfb.getAuthSpc() + "') authName ");
                rs = ps.executeQuery();
                if (rs.next()) {
                    deptName = rs.getString("deptName");
                    offName = rs.getString("offName");
                    authName = rs.getString("authName");
                }
                DataBaseFunctions.closeSqlObjects(rs, ps);
            }
            NotificationBean nfb = getNotificationDetails(notid, notType);

            notString = getNotificationString(nfb.getOrdno(), nfb.getTxtNotOrdDt(), deptName, offName, authName);

            proNoun = getPronoun(tcfb.getEmpid());

            ps = con.prepareStatement("SELECT SPC FROM EMP_JOIN WHERE NOT_ID=? ORDER BY JOIN_ID");
            ps.setInt(1, notid);
            rs1 = ps.executeQuery();
            while (rs1.next()) {
                if (rs1.getString("SPC") != null && !rs1.getString("SPC").equalsIgnoreCase("")) {
                    addspcList.add(rs1.getString("SPC"));
                }
            }
            DataBaseFunctions.closeSqlObjects(rs1, ps);

            if (addspcList.size() != 0) {
                for (int i = 0; i < addspcList.size(); i++) {
                    addspnList.add(getSPN((String) addspcList.get(i)));
                }
            }

            /*
             if (notid != null) {
             superNote = ServiceHistoryData.getSupersseionNote(notid, st);
             }
             */
            if (superNote != null && !superNote.equals("")) {
                outputTransferDetails.append(superNote);
            }

            if (notType != null) {

                if (notType.trim().equalsIgnoreCase("TRANSFER") || notType.trim().equalsIgnoreCase("POSTING")) {

                    if (notType.trim().equalsIgnoreCase("TRANSFER")) {
                        outputTransferDetails.append(" TRANSFERED AND POSTED ");
                    } else if (notType.trim().equalsIgnoreCase("POSTING")) {
                        outputTransferDetails.append(" APPOINTED ");
                    }

                    if (tcfb.getPostedspc() != null && !tcfb.getPostedspc().equalsIgnoreCase("")) {

                        nxtSpn = getSPN(tcfb.getPostedspc());
                        outputTransferDetails.append(" AS ").append(nxtSpn);

                        if (tcfb.getSltPostedFieldOff() != null && !tcfb.getSltPostedFieldOff().equals("")) {
                            outputTransferDetails.append(" ( " + getOffName(tcfb.getSltPostedFieldOff()) + " ) ");
                        }
                    } else if (tcfb.getHidPostedOffCode() != null && !tcfb.getHidPostedOffCode().equals("")) {
                        outputTransferDetails.append(" TO ").append(getOffName(tcfb.getHidPostedOffCode()));

                    } else if (tcfb.getHidPostedDeptCode() != null && !tcfb.getHidPostedDeptCode().equals("")) {
                        outputTransferDetails.append(" TO ").append(getDeptName(tcfb.getHidPostedDeptCode())).append(" DEPARTMENT ");
                    }
                } else if (notType.trim().equalsIgnoreCase("SERVICE_DISPOSAL")) {
                    outputTransferDetails.append(" SERVICES PLACED ");

                    /*
                     if ((notBean.getSltnpoffname() != null && !notBean.getSltnpoffname().equals("")) || (notBean.getTxtpostingoff() != null && !notBean.getTxtpostingoff().equals(""))) {
                     if (notBean.getSltnpoffname() != null && !notBean.getSltnpoffname().equals("")) {
                     outputTransferDetails.append(" AT THE DISPOSAL OF ").append(getOffName(notBean.getSltnpoffname(), st));
                     } else if (notBean.getTxtpostingoff() != null && !notBean.getTxtpostingoff().equals("")) {
                     outputTransferDetails.append(" AT THE DISPOSAL OF ").append(notBean.getTxtpostingoff());
                     }
                     } else if ((notBean.getSltnpdeptname() != null && !notBean.getSltnpdeptname().equals("")) || (notBean.getTxtpostingdept() != null && !notBean.getTxtpostingdept().equals(""))) {
                     if (notBean.getSltnpdeptname() != null && !notBean.getSltnpdeptname().equals("")) {
                     outputTransferDetails.append(" AT THE DISPOSAL OF ").append(ServiceHistoryData.getDeptName(notBean.getSltnpdeptname(), st)).append(" DEPARTMENT ");
                     } else if (notBean.getTxtpostingdept() != null && !notBean.getTxtpostingdept().equals("")) {
                     outputTransferDetails.append(" AT THE DISPOSAL OF ").append(notBean.getTxtpostingdept()).append(" DEPARTMENT ");
                     }
                     }

                     if ((notBean.getSltnpspc() != null && !notBean.getSltnpspc().equalsIgnoreCase("")) || (notBean.getTxtpostingauth() != null && !notBean.getTxtpostingauth().equalsIgnoreCase(""))) {
                     if (notBean.getTxtpostingauth() != null && !notBean.getTxtpostingauth().equalsIgnoreCase("")) {
                     outputTransferDetails.append(" AS ").append(notBean.getTxtpostingauth());
                     } else if (notBean.getSltnpspc() != null && !notBean.getSltnpspc().equalsIgnoreCase("")) {
                     nxtSpn = getSPN(notBean.getSltnpspc());
                     outputTransferDetails.append(" AS ").append(nxtSpn);
                     }
                     }
                            
                     */
                }

                if (notString != null) {
                    outputTransferDetails.append(notString);
                    outputTransferDetails.append(".");
                }
                if (addspnList.size() != 0 && addspnList.size() > 1) {
                    outputTransferDetails.append(" ALONG WITH ");
                    for (int i = 1; i < addspnList.size(); i++) {
                        if (i == addspnList.size() - 1) {
                            outputTransferDetails.append(addspnList.get(i));
                        } else {
                            outputTransferDetails.append(addspnList.get(i)).append(", ");
                        }
                    }

                }

            }

            if (tcfb.getTxtBasic() != null && !tcfb.getTxtBasic().trim().equals("") && !tcfb.getTxtBasic().trim().equals("0")) {
                outputTransferDetails.append(proNoun).append(" IS ALLOWED TO DRAW PAY @ RS. ").append(CommonFunctions.formatNumber(tcfb.getTxtBasic())).append("/- PM ");
            }
            if (tcfb.getPayLevel() != null && !tcfb.getPayLevel().equals("")) {
                outputTransferDetails.append(" IN CELL " + tcfb.getPayCell() + " OF LEVEL " + tcfb.getPayLevel() + " ");
            } else {
                if (tcfb.getSltPayScale() != null && !tcfb.getSltPayScale().trim().equals("")) {
                    outputTransferDetails.append(" IN THE SCALE OF PAY RS. ").append(tcfb.getSltPayScale().toUpperCase()).append("/-");
                }

                if (tcfb.getTxtGP() != null && !tcfb.getTxtGP().trim().equals("") && !tcfb.getTxtGP().trim().equals("0")) {
                    outputTransferDetails.append(" WITH GRADE PAY RS. ").append(tcfb.getTxtGP().toUpperCase()).append("/-");
                }
            }
            if (tcfb.getTxtSP() != null && !tcfb.getTxtSP().trim().equals("") && !tcfb.getTxtSP().trim().equals("0")) {
                outputTransferDetails.append(" + SPECIAL PAY @ RS. ").append(CommonFunctions.formatNumber(tcfb.getTxtSP().toUpperCase())).append("/- ");
            }
            if (tcfb.getTxtPP() != null && !tcfb.getTxtPP().trim().equals("") && !tcfb.getTxtPP().trim().equals("0")) {
                outputTransferDetails.append(" + PERSONAL PAY @ RS. ").append(CommonFunctions.formatNumber(tcfb.getTxtPP().toUpperCase())).append("/- ");
            }
            if (tcfb.getTxtDescOP() != null && !tcfb.getTxtDescOP().trim().equals("")) {
                if (tcfb.getTxtOP() != null && !tcfb.getTxtOP().trim().equals("") && !tcfb.getTxtOP().equals("0")) {
                    outputTransferDetails.append(" ALONG WITH ").append(tcfb.getTxtDescOP().toUpperCase()).append(" @ RS. ").append(CommonFunctions.formatNumber(tcfb.getTxtOP())).append("/-");
                }
            } else {
                if (tcfb.getTxtOP() != null && !tcfb.getTxtOP().equals("") && !tcfb.getTxtOP().equals("0")) {
                    outputTransferDetails.append(" ALONG WITH OTHER PAY @ RS. ").append(CommonFunctions.formatNumber(tcfb.getTxtOP())).append("/-");
                }
            }
            if ((tcfb.getTxtSP() != null && !tcfb.getTxtSP().equals("") && !tcfb.getTxtSP().equals("0"))
                    || (tcfb.getTxtBasic() != null && !tcfb.getTxtBasic().trim().equals("") && !tcfb.getTxtBasic().trim().equals("0"))
                    || (tcfb.getTxtGP() != null && !tcfb.getTxtGP().trim().equals("") && !tcfb.getTxtGP().trim().equals("0"))
                    || (tcfb.getTxtPP() != null && !tcfb.getTxtPP().trim().equals("") && !tcfb.getTxtPP().trim().equals("0"))
                    || (tcfb.getTxtOP() != null && !tcfb.getTxtOP().trim().equals("") && !tcfb.getTxtOP().trim().equals("0"))) {
                outputTransferDetails.append(" PM ");
            }
            if (tcfb.getTxtWEFDt() != null && !tcfb.getTxtWEFDt().equals("")) {
                outputTransferDetails.append(" WEF ").append(tcfb.getTxtWEFDt().toUpperCase());
                if (tcfb.getSltWEFTime() != null && !tcfb.getSltWEFTime().equals("")) {
                    outputTransferDetails.append(" (" + tcfb.getSltWEFTime().toUpperCase() + ")");
                }
            }

            if (outputTransferDetails.toString() != null && !outputTransferDetails.equals("")) {
                if (!outputTransferDetails.toString().trim().endsWith(".")) {
                    outputTransferDetails.append(".");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }

        return outputTransferDetails.toString().toUpperCase();
    }

    public String getReinstateLanguage(Reinstatement re) {
        StringBuffer outputReinDetails = new StringBuffer();
        String authName = null;
        String offName = null;
        String deptName = null;
        String othSpn = "";
        String notficationString = "";
        Connection con = null;

        try {
            con = dataSource.getConnection();

            if (re.getNotifyingPostName() != null && !re.getNotifyingPostName().equals("")) {
                authName = getSPN(re.getNotifyingPostName());
            } else if (re.getSltOffice() != null && !re.getSltOffice().equals("")) {
                offName = getOffName(re.getSltOffice());
            } else if (re.getSltDept() != null && !re.getSltDept().equals("")) {
                deptName = getDeptName(re.getSltDept());
            }

            notficationString = getNotificationString(re.getTxtNotOrdNo(), re.getTxtNotOrdDt(), deptName, offName, authName);

            outputReinDetails.append("REINSTATED IN SERVICE  ");
            if (notficationString != null && !notficationString.equals("")) {
                outputReinDetails.append(notficationString);
            }
            outputReinDetails.append(".");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }

        return outputReinDetails.toString();
    }

    public String getNotificationString(String ordNo, String ordDate, String deptName, String offName, String spn) throws Exception {

        StringBuffer notString = new StringBuffer();
        try {
//				

            if (spn != null && !spn.trim().equals("")) {
                notString.append(" VIDE ").append(spn);
            } else if (offName != null && !offName.trim().equals("")) {
                notString.append(" VIDE ").append(offName);
            } else if (deptName != null && !deptName.trim().equals("")) {
                notString.append(" VIDE ").append(deptName).append(" DEPARTMENT ");
            }
            if ((spn != null && !spn.equals("")) || (offName != null && !offName.equals("")) || (deptName != null && !deptName.equals(""))) {
                if (ordNo != null && !ordNo.trim().equals("")) {
                    notString.append(" NOTIFICATION/ OFFICE ORDER NO. ").append(ordNo.toUpperCase());
                    if (ordDate != null && !ordDate.trim().equals("")) {
                        notString.append(" DATED ").append(ordDate.toUpperCase());
                    }
                } else {
                    notString.append(" ORDER");
                    if (ordDate != null && !ordDate.trim().equals("")) {
                        notString.append(", DATED ").append(ordDate.toUpperCase());
                    }
                }
            } else {
                if (ordNo != null && !ordNo.trim().equals("")) {
                    notString.append(" VIDE NOTIFICATION/ OFFICE ORDER NO. ").append(ordNo.toUpperCase());
                    if (ordDate != null && !ordDate.trim().equals("")) {
                        notString.append(" DATED ").append(ordDate.toUpperCase());
                    }
                } else if (ordDate != null && !ordDate.trim().equals("")) {
                    notString.append(" VIDE NOTIFICATION/ OFFICE ORDER, DATED ").append(ordDate.toUpperCase());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return notString.append("").toString().toUpperCase();
    }

    public void getDepartmentNameWithOffAndPostName(String spc) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {

            con = dataSource.getConnection();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public String getPronoun(String empId) {
        String proNoun = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("SELECT GENDER FROM EMP_MAST WHERE EMP_ID=? ");
            ps.setString(1, empId);
            rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString("GENDER") != null) {
                    if (rs.getString("GENDER").equalsIgnoreCase("M")) {
                        proNoun = "HE";
                    } else if (rs.getString("GENDER").equalsIgnoreCase("F")) {
                        proNoun = "SHE";
                    } else {
                        proNoun = "HE/ SHE";
                    }
                } else {
                    proNoun = "HE/ SHE";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return proNoun;
    }

    public String getSPN(String spc) {
        String spn = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();

            if (spc != null && !spc.trim().equals("")) {
                ps = con.prepareStatement("Select SPN from G_SPC where SPC=? AND SPN IS NOT NULL ");
                ps.setString(1, spc);
                rs = ps.executeQuery();
                if (rs.next()) {
                    spn = rs.getString("SPN");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return spn;
    }

    public String getPostName(String gpc) {
        String spn = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();

            if (gpc != null && !gpc.equals("")) {
                ps = con.prepareStatement("Select post from G_post where post_code=? ");
                ps.setString(1, gpc);
                rs = ps.executeQuery();
                if (rs.next()) {
                    spn = rs.getString("post");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return spn;
    }

    public String getOffName(String offCode) {
        String offName = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            if (offCode.length() == 2) {
                ps = con.prepareStatement("SELECT OFF_NAME FROM g_officiating WHERE OFF_ID=?");
                ps.setString(1, offCode);
                rs = ps.executeQuery();
                if (rs.next()) {
                    offName = rs.getString("OFF_NAME") + " Office ";
                }
            } else {
                ps = con.prepareStatement("SELECT OFF_EN FROM G_OFFICE WHERE OFF_CODE=? ");
                ps.setString(1, offCode);
                rs = ps.executeQuery();
                if (rs.next()) {
                    offName = rs.getString("OFF_EN");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return offName;
    }

    public String getDeptName(String deptCode) throws Exception {
        String deptName = null;

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("SELECT DEPARTMENT_NAME FROM G_DEPARTMENT WHERE DEPARTMENT_CODE=?");
            ps.setString(1, deptCode);
            rs = ps.executeQuery();
            while (rs.next()) {
                deptName = rs.getString("DEPARTMENT_NAME").toUpperCase();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return deptName;
    }

    public String getCadreName(String cadreCode) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String cadreName = null;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("SELECT CADRE_NAME FROM G_CADRE WHERE CADRE_CODE=?");
            ps.setString(1, cadreCode);
            rs = ps.executeQuery();

            if (rs.next()) {
                if (rs.getString("CADRE_NAME") != null) {
                    cadreName = rs.getString("CADRE_NAME");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return cadreName;
    }

    public String getLeaveType(String leaveCode) {
        String leaveName = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("SELECT DISTINCT TOL FROM G_LEAVE WHERE TOL_ID=?");
            ps.setString(1, leaveCode);
            rs = ps.executeQuery();
            if (rs.next()) {
                leaveName = rs.getString("TOL").toUpperCase();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return leaveName;
    }

    public String getTrainingTitle1(String training_type_id) {

        ResultSet res = null;
        String type = "";
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = this.dataSource.getConnection();
            String sql = "";
            ps = con.prepareStatement("SELECT TRAINING_TITLE FROM G_TRAINING_TITLE WHERE TRAINING_TITLE_ID=?");
            ps.setString(1, training_type_id);
            res = ps.executeQuery();
            if (res.next()) {
                type = res.getString("TRAINING_TITLE");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, ps, con);
        }
        return type;
    }

    public String getPayDetails(EmpPayRecordForm epayrecordform) throws Exception {
        String firstAppDetails = "";

        try {

            if (epayrecordform.getBasic() != null && !epayrecordform.getBasic().trim().equals("") && !epayrecordform.getBasic().trim().equals("0")) {
                firstAppDetails = firstAppDetails + " WITH PAY @ RS. " + CommonFunctions.formatNumber(epayrecordform.getBasic()) + "/- PM ";
            }
            if (epayrecordform.getPayscale() != null && !epayrecordform.getPayscale().trim().equals("")) {
                firstAppDetails = firstAppDetails + " IN THE SCALE OF PAY RS. " + epayrecordform.getPayscale() + "/-";
            }
            if (epayrecordform.getGp() != null && !epayrecordform.getGp().trim().equals("") && !epayrecordform.getGp().trim().equals("0")) {
                firstAppDetails = firstAppDetails + " + GRADE PAY RS. " + epayrecordform.getGp() + "/-";
            }

            if (epayrecordform.getS_pay() != null && !epayrecordform.getS_pay().trim().equals("") && !epayrecordform.getS_pay().trim().equals("0")) {
                firstAppDetails = firstAppDetails + " + SPECIAL PAY @ RS. " + CommonFunctions.formatNumber(epayrecordform.getS_pay()) + "/- ";
            }
            if (epayrecordform.getP_pay() != null && !epayrecordform.getP_pay().trim().equals("") && !epayrecordform.getP_pay().trim().equals("0")) {
                firstAppDetails = firstAppDetails + " + PERSONAL PAY @ RS. " + CommonFunctions.formatNumber(epayrecordform.getP_pay()) + "/- ";
            }
            if (epayrecordform.getOth_desc() != null && !epayrecordform.getOth_desc().trim().equals("")) {
                if (epayrecordform.getOth_pay() != null && !epayrecordform.getOth_pay().trim().equals("") && !epayrecordform.getOth_pay().trim().equals("0")) {
                    firstAppDetails = firstAppDetails + " ALONG WITH " + epayrecordform.getOth_desc().toUpperCase() + " @ RS. " + CommonFunctions.formatNumber(epayrecordform.getOth_pay()) + "/- ";
                }
            } else {
                if (epayrecordform.getOth_pay() != null && !epayrecordform.getOth_pay().trim().equals("") && !epayrecordform.getOth_pay().trim().equals("0")) {
                    firstAppDetails = firstAppDetails + " ALONG WITH OTHER PAY @ RS. " + CommonFunctions.formatNumber(epayrecordform.getOth_pay()) + "/- ";
                }
            }
            if ((epayrecordform.getS_pay() != null && !epayrecordform.getS_pay().trim().equals("") && !epayrecordform.getS_pay().trim().equals("0"))
                    || (epayrecordform.getP_pay() != null && !epayrecordform.getP_pay().trim().equals("") && !epayrecordform.getP_pay().trim().equals("0"))
                    || (epayrecordform.getGp() != null && !epayrecordform.getGp().trim().equals("") && !epayrecordform.getGp().trim().equals("0"))
                    || (epayrecordform.getOth_pay() != null && !epayrecordform.getOth_pay().trim().equals("") && !epayrecordform.getOth_pay().trim().equals("0"))) {
                firstAppDetails = firstAppDetails + " PM ";
            }
            if (epayrecordform.getWefDt() != null && !epayrecordform.getWefDt().trim().equals("")) {
                firstAppDetails = firstAppDetails + " WEF " + epayrecordform.getWefDt();
                if (epayrecordform.getWefTime() != null && !epayrecordform.getWefTime().trim().equals("")) {
                    firstAppDetails = firstAppDetails + " (" + epayrecordform.getWefTime() + ")";
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return firstAppDetails;
    }

    /*public String getPostName(String gpc) {
     String spn = "";
     Connection con = null;
     PreparedStatement ps = null;
     ResultSet rs = null;
     try {
     con = dataSource.getConnection();

     if (gpc != null && !gpc.equals("")) {
     ps = con.prepareStatement("Select post from G_post where post_code=? ");
     ps.setString(1, gpc);
     rs = ps.executeQuery();
     if (rs.next()) {
     spn = rs.getString("post");
     }
     }

     } catch (Exception e) {
     e.printStackTrace();
     } finally {
     DataBaseFunctions.closeSqlObjects(con);
     }
     return spn;
     }

     public String getCadreName(String cadreCode) {
     Connection con = null;
     PreparedStatement ps = null;
     ResultSet rs = null;
     String cadreName = null;
     try {
     ps = con.prepareStatement("SELECT CADRE_NAME FROM G_CADRE WHERE CADRE_CODE=?");
     ps.setString(1, cadreCode);
     rs = ps.executeQuery();

     while (rs.next()) {
     if (rs.getString("CADRE_NAME") != null) {
     cadreName = rs.getString("CADRE_NAME");
     }
     }
     } catch (Exception e) {
     e.printStackTrace();
     } finally {
     DataBaseFunctions.closeSqlObjects(rs, ps);
     DataBaseFunctions.closeSqlObjects(con);
     }
     return cadreName;
     }*/
    public String getDeputationAGLanguage(DeputationDataForm deput) {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        StringBuffer outputTransferDetails = new StringBuffer();
        try {

            con = dataSource.getConnection();
            String language = "ENDORSEMENT OF AG ON DEPUTATION<br/>";

            if (deput.getHidNotifyingOthSpc() != null && !deput.getHidNotifyingOthSpc().equals("")) {
                String otherpn = getOtherSpn(deput.getHidNotifyingOthSpc());
                language += " DEPUTATION IS SANCTIONED VIDE " + otherpn + " NOTIFICATION/ OFFICE ORDER NO. " + deput.getTxtNotOrdNo() + " DATED " + deput.getTxtNotOrdDt() + ", WEF  " + deput.getTxtWEFrmDt() + " TO  " + deput.getTxtTillDt() + ".";
            } else {
                language += " DEPUTATION IS SANCTIONED VIDE " + getOffName(deput.getHidNotifyingOffCode()) + " NOTIFICATION/ OFFICE ORDER NO. " + deput.getTxtNotOrdNo() + " DATED " + deput.getTxtNotOrdDt() + ", WEF  " + deput.getTxtWEFrmDt() + " TO  " + deput.getTxtTillDt() + ".";
            }
            outputTransferDetails.append(language);
            int notId = deput.getHidNotId();
            String empid = deput.getEmpid();
            String sql = "select *,to_char( amount, 'FM999999999.00') as amountFormat FROM emp_contribution WHERE EMP_ID=?  AND NOT_ID=? AND not_type=? ORDER BY cont_type";
            // System.out.println("empid==" + empid + "notid==" + notId);
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            pst.setInt(2, notId);
            pst.setString(3, "DEPUTATION_AG");
            rs = pst.executeQuery();
            String conTypeH = "<br/>Leave Salary Contribution<br/>";
            String conTypeL = "";

            String conTypeP = "<br/>Pension Contribution<br/>";
            String conTypeLP = "";

            String conTypeY = "<br/>Pay History (Per Month)<br/>";
            String conTypeLY = "";

            String conTypeSP = "<br/>Special Pay<br/>";
            String conTypeLSP = "";

            String conTypePP = "<br/>Personal Pay<br/>";
            String conTypeLPP = "";

            String conTypeOP = "<br/>Other Pay<br/>";
            String conTypeLOP = "";

            while (rs.next()) {
                String conType = rs.getString("cont_type");
                String sdate = CommonFunctions.getFormattedOutputDate1(rs.getDate("wef_date"));
                String edate = CommonFunctions.getFormattedOutputDate1(rs.getDate("till_date"));
                String pay = rs.getString("amountFormat");
                if (conType != null && conType.equals("L")) {
                    conTypeL += sdate + " TO " + edate + " @Rs. " + pay + "; ";
                }
                if (conType != null && conType.equals("P")) {
                    conTypeLP += sdate + " TO " + edate + " @Rs. " + pay + "; ";
                }
                if (conType != null && conType.equals("Y")) {
                    conTypeLY += sdate + " TO " + edate + " @Rs. " + pay + "; ";
                }
                if (conType != null && conType.equals("SP")) {
                    conTypeLSP += sdate + " TO " + edate + " @Rs. " + pay + "; ";
                }
                if (conType != null && conType.equals("PP")) {
                    conTypeLPP += sdate + " TO " + edate + " @Rs. " + pay + "; ";
                }
                if (conType != null && conType.equals("OP")) {
                    conTypeLOP += sdate + " TO " + edate + " @Rs. " + pay + "; ";
                }

            }
            DataBaseFunctions.closeSqlObjects(pst);
            if (!conTypeL.equals("")) {
                outputTransferDetails.append(conTypeH);
                outputTransferDetails.append(conTypeL);
            }
            if (!conTypeLP.equals("")) {
                outputTransferDetails.append(conTypeP);
                outputTransferDetails.append(conTypeLP);
            }
            if (!conTypeLY.equals("")) {
                outputTransferDetails.append(conTypeY);
                outputTransferDetails.append(conTypeLY);
            }
            if (!conTypeLSP.equals("")) {
                outputTransferDetails.append(conTypeSP);
                outputTransferDetails.append(conTypeLSP);
            }
            if (!conTypeLPP.equals("")) {
                outputTransferDetails.append(conTypePP);
                outputTransferDetails.append(conTypeLPP);
            }
            if (!conTypeLOP.equals("")) {
                outputTransferDetails.append(conTypeOP);
                outputTransferDetails.append(conTypeLOP);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return outputTransferDetails.toString().toUpperCase();
    }

    @Override
    public String generateLTCLanguage(sLTCBean slBean) {
        String language = "";
        String homeTown = "";
        if (slBean.getChkHomeTown().equals("H")) {
            homeTown = "HOME TOWN";
        }
        if (slBean.getChkHomeTown().equals("A")) {
            homeTown = "ALL INDIA";
        }
        if (slBean.getRadpostingauthtype() != null && slBean.getRadpostingauthtype().equals("GOI")) {
            language = "ALLOWED TO AVAIL THE BENEFIT OF " + homeTown + " LTC DURING THE BLOCK PERIOD COMMENCING FROM " + slBean.getFblYear() + " TO " + slBean.getTblYear() + " FROM " + slBean.getFromDate() + " TO " + slBean.getToDate() + "  VIDE " + slBean.getHidPostedOffCode() + " NOTIFICATION/ OFFICE ORDER NO. " + slBean.getTxtNotOrdNo().toUpperCase() + " DATED " + slBean.getTxtNotOrdDt().toUpperCase() + ".";
        } else {
            language = "ALLOWED TO AVAIL THE BENEFIT OF " + homeTown + " LTC DURING THE BLOCK PERIOD COMMENCING FROM " + slBean.getFblYear() + " TO " + slBean.getTblYear() + " FROM " + slBean.getFromDate() + " TO " + slBean.getToDate() + "  VIDE " + getOffName(slBean.getHidNotifyingOffCode()) + " NOTIFICATION/ OFFICE ORDER NO. " + slBean.getTxtNotOrdNo().toUpperCase() + " DATED " + slBean.getTxtNotOrdDt().toUpperCase() + ".";
        }
        return language;
    }

    @Override
    public String getConfirmationOfServiceDetails(ConfirmationOfServiceForm confirmationOfServiceForm, int notid, String notificationtype) {

        StringBuffer confMessage = new StringBuffer();
        String authName = null;
        String offName = null;
        String deptName = null;

        String notString = "";
        String othSpn = "";
        String superNote = null;
        try {

            if (confirmationOfServiceForm.getNotifyingSpc() != null && !confirmationOfServiceForm.getNotifyingSpc().equals("")) {
                authName = getSPN(confirmationOfServiceForm.getNotifyingSpc());
            } else if (confirmationOfServiceForm.getHidNotifyingOffCode() != null && !confirmationOfServiceForm.getHidNotifyingOffCode().equals("")) {
                offName = getOffName(confirmationOfServiceForm.getHidNotifyingOffCode());
            } else if (confirmationOfServiceForm.getHidNotifyingDeptCode() != null && !confirmationOfServiceForm.getHidNotifyingDeptCode().equals("")) {
                deptName = getDeptName(confirmationOfServiceForm.getHidNotifyingDeptCode());
            }

            notString = getNotificationString(confirmationOfServiceForm.getTxtNotOrdNo(), confirmationOfServiceForm.getTxtNotOrdDt(), deptName, offName, authName);

            confMessage.append("CONFIRMED ");

            if (confirmationOfServiceForm.getSltGenericPost() != null && !confirmationOfServiceForm.getSltGenericPost().trim().equals("")) {
                confMessage.append(" AS ").append(getPostName(confirmationOfServiceForm.getSltGenericPost()));
            }
            if (confirmationOfServiceForm.getSltCadre() != null && !confirmationOfServiceForm.getSltCadre().trim().equals("")) {
                confMessage.append(" IN THE ").append(getCadreName(confirmationOfServiceForm.getSltCadre()));
            }
            if (confirmationOfServiceForm.getTxtCadreJoiningWEFDt() != null && !confirmationOfServiceForm.getTxtCadreJoiningWEFDt().trim().equals("")) {
                confMessage.append(" WEF ").append(confirmationOfServiceForm.getTxtCadreJoiningWEFDt());
            }
            if (confirmationOfServiceForm.getSltCadreJoiningWEFTime() != null && !confirmationOfServiceForm.getSltCadreJoiningWEFTime().trim().equals("")) {
                if (confirmationOfServiceForm.getSltCadreJoiningWEFTime().equals("FN")) {
                    confMessage.append(", ").append(" FORE NOON");
                } else {
                    confMessage.append(", ").append(" AFTER NOON");
                }
            }

            if (notString != null && !notString.trim().equals("")) {
                confMessage.append(notString);
            }

            confMessage.append(".");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return confMessage.toString().toUpperCase();

    }

    @Override
    public String getPostingDetails(PostingForm postingform, int notid, String notType) {

        StringBuffer outputPostingDetails = new StringBuffer();
        String nxtSpn = null;
        String superNote = null;
        ArrayList addspcList = new ArrayList();
        ArrayList addspnList = new ArrayList();
        ResultSet rs1 = null;
        String proNoun = null;
        String offName = null;
        String deptName = null;
        String authName = null;
        String notString = "";
        String othSpn = "";
        //String othNextSpn = "";
        String fieldoffname = "";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            con = dataSource.getConnection();

            ps = con.prepareStatement("select getdeptname('" + postingform.getHidAuthDeptCode() + "') deptName, getofficeen('" + postingform.getHidAuthOffCode() + "') offName,  getspn('" + postingform.getAuthSpc() + "') authName ");
            rs = ps.executeQuery();
            if (rs.next()) {
                deptName = rs.getString("deptName");
                offName = rs.getString("offName");
                authName = rs.getString("authName");
            }
            DataBaseFunctions.closeSqlObjects(rs, ps);

            NotificationBean nfb = getNotificationDetails(notid, notType);

            notString = getNotificationString(nfb.getOrdno(), nfb.getTxtNotOrdDt(), deptName, offName, authName);

            proNoun = getPronoun(postingform.getEmpid());

            ps = con.prepareStatement("SELECT SPC FROM EMP_JOIN WHERE NOT_ID=? ORDER BY JOIN_ID");
            ps.setInt(1, notid);
            rs1 = ps.executeQuery();
            while (rs1.next()) {
                if (rs1.getString("SPC") != null && !rs1.getString("SPC").equalsIgnoreCase("")) {
                    addspcList.add(rs1.getString("SPC"));
                }
            }
            DataBaseFunctions.closeSqlObjects(rs1, ps);

            if (addspcList.size() != 0) {
                for (int i = 0; i < addspcList.size(); i++) {
                    addspnList.add(getSPN((String) addspcList.get(i)));
                }
            }

            /*
             if (notid != null) {
             superNote = ServiceHistoryData.getSupersseionNote(notid, st);
             }
             */
            if (superNote != null && !superNote.equals("")) {
                outputPostingDetails.append(superNote);
            }

            if (notType != null) {

                if (notType.trim().equalsIgnoreCase("TRANSFER") || notType.trim().equalsIgnoreCase("POSTING")) {

                    if (notType.trim().equalsIgnoreCase("TRANSFER")) {
                        outputPostingDetails.append(" TRANSFERED AND POSTED ");
                    } else if (notType.trim().equalsIgnoreCase("POSTING")) {
                        outputPostingDetails.append(" APPOINTED ");
                    }

                    if (postingform.getPostedspc() != null && !postingform.getPostedspc().equalsIgnoreCase("")) {

                        nxtSpn = getSPN(postingform.getPostedspc());
                        outputPostingDetails.append(" AS ").append(nxtSpn);

                        if (postingform.getSltPostedFieldOff() != null && !postingform.getSltPostedFieldOff().equals("")) {
                            outputPostingDetails.append(" ( " + getOffName(postingform.getSltPostedFieldOff()) + " ) ");
                        }
                    } else if (postingform.getHidPostedOffCode() != null && !postingform.getHidPostedOffCode().equals("")) {
                        outputPostingDetails.append(" TO ").append(getOffName(postingform.getHidPostedOffCode()));

                    } else if (postingform.getHidPostedDeptCode() != null && !postingform.getHidPostedDeptCode().equals("")) {
                        outputPostingDetails.append(" TO ").append(getDeptName(postingform.getHidPostedDeptCode())).append(" DEPARTMENT ");
                    }
                } else if (notType.trim().equalsIgnoreCase("SERVICE_DISPOSAL")) {
                    outputPostingDetails.append(" SERVICES PLACED ");

                    /*
                     if ((notBean.getSltnpoffname() != null && !notBean.getSltnpoffname().equals("")) || (notBean.getTxtpostingoff() != null && !notBean.getTxtpostingoff().equals(""))) {
                     if (notBean.getSltnpoffname() != null && !notBean.getSltnpoffname().equals("")) {
                     outputTransferDetails.append(" AT THE DISPOSAL OF ").append(getOffName(notBean.getSltnpoffname(), st));
                     } else if (notBean.getTxtpostingoff() != null && !notBean.getTxtpostingoff().equals("")) {
                     outputTransferDetails.append(" AT THE DISPOSAL OF ").append(notBean.getTxtpostingoff());
                     }
                     } else if ((notBean.getSltnpdeptname() != null && !notBean.getSltnpdeptname().equals("")) || (notBean.getTxtpostingdept() != null && !notBean.getTxtpostingdept().equals(""))) {
                     if (notBean.getSltnpdeptname() != null && !notBean.getSltnpdeptname().equals("")) {
                     outputTransferDetails.append(" AT THE DISPOSAL OF ").append(ServiceHistoryData.getDeptName(notBean.getSltnpdeptname(), st)).append(" DEPARTMENT ");
                     } else if (notBean.getTxtpostingdept() != null && !notBean.getTxtpostingdept().equals("")) {
                     outputTransferDetails.append(" AT THE DISPOSAL OF ").append(notBean.getTxtpostingdept()).append(" DEPARTMENT ");
                     }
                     }

                     if ((notBean.getSltnpspc() != null && !notBean.getSltnpspc().equalsIgnoreCase("")) || (notBean.getTxtpostingauth() != null && !notBean.getTxtpostingauth().equalsIgnoreCase(""))) {
                     if (notBean.getTxtpostingauth() != null && !notBean.getTxtpostingauth().equalsIgnoreCase("")) {
                     outputTransferDetails.append(" AS ").append(notBean.getTxtpostingauth());
                     } else if (notBean.getSltnpspc() != null && !notBean.getSltnpspc().equalsIgnoreCase("")) {
                     nxtSpn = getSPN(notBean.getSltnpspc());
                     outputTransferDetails.append(" AS ").append(nxtSpn);
                     }
                     }
                            
                     */
                }

                if (notString != null) {
                    outputPostingDetails.append(notString);
                    outputPostingDetails.append(".");
                }
                if (addspnList.size() != 0 && addspnList.size() > 1) {
                    outputPostingDetails.append(" ALONG WITH ");
                    for (int i = 1; i < addspnList.size(); i++) {
                        if (i == addspnList.size() - 1) {
                            outputPostingDetails.append(addspnList.get(i));
                        } else {
                            outputPostingDetails.append(addspnList.get(i)).append(", ");
                        }
                    }

                }

            }

            if (postingform.getTxtBasic() != null && !postingform.getTxtBasic().trim().equals("") && !postingform.getTxtBasic().trim().equals("0")) {
                outputPostingDetails.append(proNoun).append(" IS ALLOWED TO DRAW PAY @ RS. ").append(CommonFunctions.formatNumber(postingform.getTxtBasic())).append("/- PM ");
            }
            if (postingform.getSltPayScale() != null && !postingform.getSltPayScale().trim().equals("")) {
                outputPostingDetails.append(" IN THE SCALE OF PAY RS. ").append(postingform.getSltPayScale().toUpperCase()).append("/-");
            }

            if (postingform.getTxtGP() != null && !postingform.getTxtGP().trim().equals("") && !postingform.getTxtGP().trim().equals("0")) {
                outputPostingDetails.append(" WITH GRADE PAY RS. ").append(postingform.getTxtGP().toUpperCase()).append("/-");
            }

            if (postingform.getTxtSP() != null && !postingform.getTxtSP().trim().equals("") && !postingform.getTxtSP().trim().equals("0")) {
                outputPostingDetails.append(" + SPECIAL PAY @ RS. ").append(CommonFunctions.formatNumber(postingform.getTxtSP().toUpperCase())).append("/- ");
            }
            if (postingform.getTxtPP() != null && !postingform.getTxtPP().trim().equals("") && !postingform.getTxtPP().trim().equals("0")) {
                outputPostingDetails.append(" + PERSONAL PAY @ RS. ").append(CommonFunctions.formatNumber(postingform.getTxtPP().toUpperCase())).append("/- ");
            }
            if (postingform.getTxtDescOP() != null && !postingform.getTxtDescOP().trim().equals("")) {
                if (postingform.getTxtOP() != null && !postingform.getTxtOP().trim().equals("") && !postingform.getTxtOP().equals("0")) {
                    outputPostingDetails.append(" ALONG WITH ").append(postingform.getTxtDescOP().toUpperCase()).append(" @ RS. ").append(CommonFunctions.formatNumber(postingform.getTxtOP())).append("/-");
                }
            } else {
                if (postingform.getTxtOP() != null && !postingform.getTxtOP().equals("") && !postingform.getTxtOP().equals("0")) {
                    outputPostingDetails.append(" ALONG WITH OTHER PAY @ RS. ").append(CommonFunctions.formatNumber(postingform.getTxtOP())).append("/-");
                }
            }
            if ((postingform.getTxtSP() != null && !postingform.getTxtSP().equals("") && !postingform.getTxtSP().equals("0"))
                    || (postingform.getTxtBasic() != null && !postingform.getTxtBasic().trim().equals("") && !postingform.getTxtBasic().trim().equals("0"))
                    || (postingform.getTxtGP() != null && !postingform.getTxtGP().trim().equals("") && !postingform.getTxtGP().trim().equals("0"))
                    || (postingform.getTxtPP() != null && !postingform.getTxtPP().trim().equals("") && !postingform.getTxtPP().trim().equals("0"))
                    || (postingform.getTxtOP() != null && !postingform.getTxtOP().trim().equals("") && !postingform.getTxtOP().trim().equals("0"))) {
                outputPostingDetails.append(" PM ");
            }
            if (postingform.getTxtWEFDt() != null && !postingform.getTxtWEFDt().equals("")) {
                outputPostingDetails.append(" WEF ").append(postingform.getTxtWEFDt().toUpperCase());
                if (postingform.getSltWEFTime() != null && !postingform.getSltWEFTime().equals("")) {
                    outputPostingDetails.append(" (" + postingform.getSltWEFTime().toUpperCase() + ")");
                }
            }

            if (outputPostingDetails.toString() != null && !outputPostingDetails.equals("")) {
                if (!outputPostingDetails.toString().trim().endsWith(".")) {
                    outputPostingDetails.append(".");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs1);
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return outputPostingDetails.toString().toUpperCase();

    }

    public String getRetirementDetails(Retirement retire) {
        Connection con = null;
        //PreparedStatement ps = null;
        // ResultSet rs = null;
        StringBuffer retResDetails = new StringBuffer();
        String notString = "";
        String authName = null;
        String offName = null;
        String deptName = null;
        String retType = "";
        String dateOfRetRes = "";
        try {

            con = dataSource.getConnection();

            if (retire.getAuthSpc() != null && !retire.getAuthSpc().equals("")) {
                authName = getSPN(retire.getAuthSpc());
            } else if (retire.getAuthOfficeCode() != null && !retire.getAuthOfficeCode().equals("")) {
                offName = getOffName(retire.getAuthOfficeCode());
            } else if (retire.getAuthSpc() != null && !retire.getAuthSpc().equals("")) {
                deptName = getDeptName(retire.getAuthSpc());
            }

            notString = getNotificationString(retire.getOrdno(), retire.getOrdDate(), deptName, offName, authName);

            if (retire.getRettype() != null && !retire.getRettype().equals("")) {
                retType = retire.getRettype();
            }

            if (retire.getDuedate() != null && !retire.getDuedate().equals("")) {
                dateOfRetRes = retire.getDuedate().toUpperCase();
            }

            //DataBaseFunctions.closeSqlObjects(rs);
            if (retType != null) {
                if (retType.equalsIgnoreCase("SR")) {
                    retResDetails.append("RETIRED ");

                    retResDetails.append(" FROM THE POST OF ").append(getSPN(retire.getRetiredfromspc()));

                    retResDetails.append(" ON REACHING SUPERANNUATION ");
                    if (notString != null && !notString.equals("")) {
                        retResDetails.append(notString);
                    }

                    if (dateOfRetRes != null) {
                        retResDetails.append(" WEF ").append(dateOfRetRes).append(".");
                    }
                } else if (retType.equalsIgnoreCase("VR")) {
                    retResDetails.append("ALLOWED TO TAKE VOLUNTARY RETIREMENT ");
                    //

                    retResDetails.append(" FROM THE POST OF ").append(getSPN(retire.getRetiredfromspc()));

                    if (notString != null && !notString.equals("")) {
                        retResDetails.append(notString);
                    }
                    if (dateOfRetRes != null) {
                        retResDetails.append(" WEF ").append(dateOfRetRes).append(".");
                    }
                } else if (retType.equalsIgnoreCase("CR")) {
                    retResDetails.append("IN PURSUANCE OF ");
                    if (authName != null && !authName.trim().equals("")) {
                        retResDetails.append(authName);
                    } else if (offName != null && !offName.trim().equals("")) {
                        retResDetails.append(offName);

                    } else if (deptName != null && !deptName.trim().equals("")) {
                        retResDetails.append(deptName + " DEPARTMENT ");
                    }
                    if ((authName != null && !authName.trim().equals("")) || (offName != null && !offName.trim().equals("")) || (deptName != null && !deptName.trim().equals(""))) {
                        if (retire.getOrdno() != null && !retire.getOrdno().equals("")) {
                            retResDetails.append(" VIDE NOTIFICATION/ OFFICE ORDER NO. ").append(retire.getOrdno());
                            if (retire.getOrdDate() != null && !retire.getOrdDate().equals("")) {
                                retResDetails.append(" DATED ").append(retire.getOrdDate());
                            }
                        }
                    } else {
                        if (retire.getOrdno() != null && !retire.getOrdno().equals("")) {
                            retResDetails.append(" NOTIFICATION/ OFFICE ORDER NO. ").append(retire.getOrdno());
                            if (retire.getOrdDate() != null && !retire.getOrdDate().equals("")) {
                                retResDetails.append(" DATED ").append(retire.getOrdDate());
                            }
                        }
                    }

                    retResDetails.append(" COMPULSORILY RETIRED FROM THE POST OF ").append(getSPN(retire.getRetiredfromspc()));

                    if (dateOfRetRes != null) {
                        retResDetails.append(" WEF ").append(dateOfRetRes).append(".");
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }

        return retResDetails.toString().toUpperCase();
    }

    @Override
    public String getRewardDetails(Reward prfb) {
        StringBuffer rewardDetails = new StringBuffer();
        String rewardlevel = null;
        String ifOther = null;
        String othAuthName = null;
        String authName = null;
        String offName = null;
        String deptName = null;
        String othSpn = "";
        String proNoun = "";

        try {
            proNoun = getPronoun(prfb.getEmpid());
            //othSpn = getOtherSPN(prfb.getTxtdept(), prfb.getTxtoff(), prfb.getTxtauth());
            othSpn = getOtherSpn(prfb.getHidOthSpc());
            if (!othSpn.equals("")) {
                authName = othSpn;
            } else {
                if (prfb.getAuthSpc() != null && !prfb.getAuthSpc().equals("")) {
                    authName = getSPN(prfb.getAuthSpc());
                } else if (prfb.getHidAuthOffCode() != null && !prfb.getHidAuthOffCode().equals("")) {
                    offName = getOffName(prfb.getHidAuthOffCode());
                } else if (prfb.getHidAuthDeptCode() != null && !prfb.getHidAuthDeptCode().equals("")) {
                    deptName = getDeptName(prfb.getHidAuthDeptCode());
                }
            }

            if (prfb.getRewardLevel() != null && !prfb.getRewardLevel().trim().equals("")) {
                rewardlevel = prfb.getRewardLevel().toUpperCase();
            }

            rewardDetails.append(proNoun).append(" IS REWARDED ");

            if (prfb.getRewardType() != null && !prfb.getRewardType().equals("")) {
                rewardDetails.append("WITH ").append(getRewardType(prfb.getRewardType()));
            }

            if (prfb.getMoneyReward() != null && !prfb.getMoneyReward().equals("") && !prfb.getMoneyReward().equals("0")) {
                rewardDetails.append(" AMOUNT OF Rs.").append(prfb.getMoneyReward());
            }

            if (rewardlevel != null) {
                rewardDetails.append(" AT THE ").append(rewardlevel).append(" LEVEL ");
            }

            if (othAuthName != null) {
                rewardDetails.append(" BY ").append(othAuthName);
            } else if (authName != null) {
                rewardDetails.append(" BY ").append(authName);
            } else if (offName != null) {
                rewardDetails.append(" BY ").append(offName);
            } else if (deptName != null) {
                rewardDetails.append(" BY ").append(deptName).append(" DEPARTMENT ");
            }
            if (othAuthName != null || authName != null || offName != null || deptName != null) {
                if (prfb.getTxtNotOrdNo() != null && !prfb.getTxtNotOrdNo().trim().equals("")) {
                    rewardDetails.append(" NOTIFICATION/ OFFICE ORDER NO. ").append(prfb.getTxtNotOrdNo());
                }
                if (prfb.getTxtNotOrdDt() != null && !prfb.getTxtNotOrdDt().trim().equals("")) {
                    rewardDetails.append(" DATED ").append(prfb.getTxtNotOrdDt());
                }
            } else {
                if (prfb.getTxtNotOrdNo() != null && !prfb.getTxtNotOrdNo().trim().equals("")) {
                    rewardDetails.append(" VIDE NOTIFICATION/ OFFICE ORDER NO. ").append(prfb.getTxtNotOrdNo());
                }
                if (prfb.getTxtNotOrdDt() != null && !prfb.getTxtNotOrdDt().trim().equals("")) {
                    rewardDetails.append(" DATED ").append(prfb.getTxtNotOrdDt());
                }
            }

            rewardDetails.append(".");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rewardDetails.toString().toUpperCase();
    }

    public String getRewardType(String rewardTypeId) {
        String reward = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();

            if (rewardTypeId != null && !rewardTypeId.equals("")) {
                ps = con.prepareStatement("SELECT reward_type_id,reward_type  FROM g_reward_type where reward_type_id=? ");
                ps.setString(1, rewardTypeId);
                rs = ps.executeQuery();
                if (rs.next()) {
                    reward = rs.getString("reward_type");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return reward;
    }

    @Override
    public String getDeptExamDetails(DepartmentalExamForm efb) {

        Connection con = null;

        StringBuffer outputDeptExam = new StringBuffer();

        PreparedStatement pst1 = null;

        ResultSet rs1 = null;
        String examName = null;
        String fdate = null;
        String tdate = null;
        String fromDate = null;
        String toDate = null;
        String result = null;
        String othSpn = "";
        String authName = null;
        String offName = null;
        String deptName = null;

        try {
            con = dataSource.getConnection();

            if (efb.getAuthPost() != null && !efb.getAuthPost().equals("")) {
                authName = getPostName(efb.getAuthPost());
            } else if (efb.getHidAuthOffCode() != null && !efb.getHidAuthOffCode().equals("")) {
                offName = getOffName(efb.getHidAuthOffCode());
            } else if (efb.getHidAuthDeptCode() != null && !efb.getHidAuthDeptCode().equals("")) {
                deptName = getDeptName(efb.getHidAuthDeptCode());
            }

            if (efb.getRdExamResult() != null && !efb.getRdExamResult().equals("")) {
                result = efb.getRdExamResult();
            }
            if (efb.getTxtExamName() != null && !efb.getTxtExamName().equals("")) {
                examName = efb.getTxtExamName();
            }
            if (efb.getTxtFromDate() != null && !efb.getTxtFromDate().equals(""))//changed
            {
                fdate = efb.getTxtFromDate();
                fromDate = efb.getTxtFromDate();
            }
            if (efb.getTxtToDate() != null && !efb.getTxtToDate().equals(""))//changed
            {
                tdate = efb.getTxtToDate();
                toDate = efb.getTxtToDate();
            }

            if (result != null && !result.equals("")) {
                if (result.equals("Y")) {
                    outputDeptExam.append("PASSED ");
                } else {
                    outputDeptExam.append("FAILED ");
                }
            }
            if (examName != null && !examName.equals("")) {
                outputDeptExam.append(examName);
            }
            if (fdate != null && tdate != null) {
                String diffrence = null;
                pst1 = con.prepareStatement("SELECT '" + tdate + "'::DATE-'" + fdate + "'::DATE+1 DIFF");
                rs1 = pst1.executeQuery();
                if (rs1.next()) {
                    diffrence = rs1.getString("DIFF");
                }
                DataBaseFunctions.closeSqlObjects(rs1);
                if (diffrence != null) {
                    if (diffrence.equalsIgnoreCase("1")) {
                        if (fdate != null) {
                            outputDeptExam.append(" HELD ON ").append(fdate);
                        }
                    } else {
                        if (fdate != null) {
                            outputDeptExam.append(" HELD FROM ").append(fdate);
                        }
                        if (tdate != null) {
                            outputDeptExam.append(" TO ").append(tdate);
                        }
                    }
                }
            }

            if (authName != null && !authName.trim().equals("")) {
                outputDeptExam.append(" VIDE ").append(authName);
            } else if (offName != null && !offName.trim().equals("")) {
                outputDeptExam.append(" VIDE ").append(offName);
            } else if (deptName != null && !deptName.trim().equals("")) {
                outputDeptExam.append(" VIDE ").append(deptName).append(" DEPARTMENT ");
            }
            if ((authName != null && !authName.trim().equals("")) || (offName != null && !offName.trim().equals("")) || (deptName != null && !deptName.trim().equals("")))//changed
            {
                if (efb.getTxtNotNo().toUpperCase() != null && !efb.getTxtNotNo().toUpperCase().equals("")) {
                    outputDeptExam.append(" NOTIFICATION/ OFFICE ORDER NO. ").append(efb.getTxtNotNo().toUpperCase());
                    if (efb.getTxtNotDate() != null && !efb.getTxtNotDate().trim().equals("")) {
                        outputDeptExam.append(" DATED ").append(efb.getTxtNotDate().toUpperCase());
                    }
                } else if (efb.getTxtNotDate() != null && !efb.getTxtNotDate().trim().equals("")) {
                    outputDeptExam.append(" NOTIFICATION/ OFFICE ORDER, DATED ").append(efb.getTxtNotDate().toUpperCase());
                }
            } else {
                if (efb.getTxtNotNo().toUpperCase() != null && !efb.getTxtNotNo().toUpperCase().equals("")) {
                    outputDeptExam.append(" VIDE NOTIFICATION/ OFFICE ORDER NO. ").append(efb.getTxtNotNo().toUpperCase());
                    if (efb.getTxtNotDate() != null && !efb.getTxtNotDate().trim().equals("")) {
                        outputDeptExam.append(" DATED ").append(efb.getTxtNotDate().toUpperCase());
                    }
                } else if (efb.getTxtNotDate() != null && !efb.getTxtNotDate().trim().equals("")) {
                    outputDeptExam.append(" VIDE NOTIFICATION/ OFFICE ORDER, DATED ").append(efb.getTxtNotDate().toUpperCase());
                }
            }
            if (outputDeptExam.toString().trim().equals("")) {
                outputDeptExam.append("PASSED THE EXAM");
            }
            outputDeptExam.append(".");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs1, pst1);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return outputDeptExam.toString().toUpperCase();
    }

    @Override
    public String getLoanSancDetails(Loan lfb) {
        StringBuffer loanDetails = new StringBuffer();
        ResultSet rs2 = null;
        String loanType = null;
        String loanName = null;
        String lonaAmt = null;
        String authName = null;
        String offName = null;
        String deptName = null;

        String notString = "";
        try {

            if (lfb.getHidSpcAuthCode() != null && !lfb.getHidSpcAuthCode().equals("")) {
                authName = getSPN(lfb.getHidSpcAuthCode());
            } else if (lfb.getHidAuthOffCode() != null && !lfb.getHidAuthOffCode().equals("")) {
                offName = getOffName(lfb.getHidAuthOffCode());
            } else if (lfb.getHidAuthDeptCode() != null && !lfb.getHidAuthDeptCode().equals("")) {
                deptName = getDeptName(lfb.getHidAuthDeptCode());
            }

            notString = getNotificationString(lfb.getOrderno(), lfb.getOrderdate(), deptName, offName, authName);

            if (lfb.getTxtamount() > 0) {
                lonaAmt = lfb.getTxtamount() + "";
            }

            loanName = getLoanName(lfb.getSltloan());

            loanDetails.append("SANCTIONED ");
            if (loanName != null && !loanName.trim().equals("")) {
                loanDetails.append(loanName);
            } else {
                loanDetails.append(" LOAN ");
            }
            if (lonaAmt != null && !lonaAmt.trim().equals("") && !lonaAmt.trim().equals("0")) {
                loanDetails.append(" OF RS. ").append(CommonFunctions.formatNumber(lonaAmt)).append("/- ");
            }
            if (notString != null && !notString.equals("")) {
                loanDetails.append(notString);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs2);
        }
        return loanDetails.toString().toUpperCase();
    }

    public String getLoanName(String loantp) {
        String loanName = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();

            if (loantp != null && !loantp.equals("")) {
                ps = con.prepareStatement("SELECT LOAN_NAME FROM G_LOAN WHERE LOAN_TP=? ");
                ps.setString(1, loantp);
                rs = ps.executeQuery();
                if (rs.next()) {
                    loanName = rs.getString("LOAN_NAME");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return loanName;
    }

    @Override
    public void createTransactionLog(String userid, String targethrmsid, String modulename, String querystring) {

        Connection con = null;

        PreparedStatement pst = null;

        String startTime = "";
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
        startTime = dateFormat.format(cal.getTime());
        try {
            con = this.dataSource.getConnection();

            String sql = "insert into transaction_entry_log(user_id,entry_taken_on,entry_taken_for_hrmsid,module_name,query_string) values(?,?,?,?,?)";
            pst = con.prepareStatement(sql);
            pst.setString(1, userid);
            pst.setTimestamp(2, new Timestamp(dateFormat.parse(startTime).getTime()));
            pst.setString(3, targethrmsid);
            pst.setString(4, modulename);
            pst.setString(5, querystring);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, con);
        }
    }

    @Override
    public String generateFTCLanguage(sFTCBean slBean) {
        String language = "";
        String homeTown = "";
        if (slBean.getChkHomeTown().equals("H")) {
            homeTown = "HOME TOWN";
        }
        if (slBean.getChkHomeTown().equals("A")) {
            homeTown = "ALL INDIA";
        }
        if (slBean.getRadpostingauthtype() != null && slBean.getRadpostingauthtype().equals("GOO")) {
            language = "ALLOWED TO AVAIL THE BENEFIT OF " + homeTown + " FTC DURING THE BLOCK PERIOD COMMENCING FROM " + slBean.getFblYear() + " TO " + slBean.getTblYear() + " FROM " + slBean.getFromDate().toUpperCase() + " TO " + slBean.getToDate().toUpperCase() + "  VIDE " + getOffName(slBean.getHidNotifyingOffCode()) + " NOTIFICATION/ OFFICE ORDER NO. " + slBean.getTxtNotOrdNo().toUpperCase() + " DATED " + slBean.getTxtNotOrdDt().toUpperCase() + ".";
        } else {
            language = "ALLOWED TO AVAIL THE BENEFIT OF " + homeTown + " FTC DURING THE BLOCK PERIOD COMMENCING FROM " + slBean.getFblYear() + " TO " + slBean.getTblYear() + " FROM " + slBean.getFromDate().toUpperCase() + " TO " + slBean.getToDate().toUpperCase() + "  VIDE " + slBean.getHidPostedOffCode() + " NOTIFICATION/ OFFICE ORDER NO. " + slBean.getTxtNotOrdNo().toUpperCase() + " DATED " + slBean.getTxtNotOrdDt().toUpperCase() + ".";

        }
        return language;
    }

    @Override
    public String getPlacementOfServiceDetails(PlacementOfServiceForm placementOfServiceForm, int notid, String notificationtype) {

        StringBuffer confMessage = new StringBuffer();
        String authName = null;
        String offName = null;
        String deptName = null;

        String notString = "";
        String othSpn = "";
        String superNote = null;
        try {

            if (placementOfServiceForm.getNotifyingSpc() != null && !placementOfServiceForm.getNotifyingSpc().equals("")) {
                authName = getSPN(placementOfServiceForm.getNotifyingSpc());
            } else if (placementOfServiceForm.getHidNotifyingOffCode() != null && !placementOfServiceForm.getHidNotifyingOffCode().equals("")) {
                offName = getOffName(placementOfServiceForm.getHidNotifyingOffCode());
            } else if (placementOfServiceForm.getHidNotifyingDeptCode() != null && !placementOfServiceForm.getHidNotifyingDeptCode().equals("")) {
                deptName = getDeptName(placementOfServiceForm.getHidNotifyingDeptCode());
            }

            notString = getNotificationString(placementOfServiceForm.getTxtNotOrdNo(), placementOfServiceForm.getTxtNotOrdDt(), deptName, offName, authName);

            confMessage.append("SERVICES PLACED ");

            if (placementOfServiceForm.getSltGenericPost() != null && !placementOfServiceForm.getSltGenericPost().trim().equals("")) {
                confMessage.append(" AS ").append(getPostName(placementOfServiceForm.getSltGenericPost()));
            }
            if (placementOfServiceForm.getSltCadre() != null && !placementOfServiceForm.getSltCadre().trim().equals("")) {
                confMessage.append(" IN THE ").append(getCadreName(placementOfServiceForm.getSltCadre()));
            }
            if (placementOfServiceForm.getTxtCadreJoiningWEFDt() != null && !placementOfServiceForm.getTxtCadreJoiningWEFDt().trim().equals("")) {
                confMessage.append(" WEF ").append(placementOfServiceForm.getTxtCadreJoiningWEFDt());
            }
            if (placementOfServiceForm.getSltCadreJoiningWEFTime() != null && !placementOfServiceForm.getSltCadreJoiningWEFTime().trim().equals("")) {
                if (placementOfServiceForm.getSltCadreJoiningWEFTime().equals("FN")) {
                    confMessage.append(", ").append(" FORE NOON");
                } else {
                    confMessage.append(", ").append(" AFTER NOON");
                }
            }

            if (notString != null && !notString.trim().equals("")) {
                confMessage.append(notString);
            }

            confMessage.append(".");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return confMessage.toString().toUpperCase();

    }

    @Override
    public String getPayEntitlementDetails(PayEntitlementForm payEntitlementForm, int notid, String notificationtype) {

        StringBuffer confMessage = new StringBuffer();
        String authName = null;
        String offName = null;
        String deptName = null;
        String proNoun = null;
        String notString = "";
        String othSpn = "";
        String superNote = null;
        try {

            proNoun = getPronoun(payEntitlementForm.getEmpid());

            if (payEntitlementForm.getNotifyingSpc() != null && !payEntitlementForm.getNotifyingSpc().equals("")) {
                authName = getSPN(payEntitlementForm.getNotifyingSpc());
                confMessage.append("IN PURSUANCE OF ").append(authName).append(" ");
            } else if (payEntitlementForm.getHidNotifyingOffCode() != null && !payEntitlementForm.getHidNotifyingOffCode().equals("")) {
                offName = getOffName(payEntitlementForm.getHidNotifyingOffCode());
                confMessage.append("IN PURSUANCE OF ").append(offName).append(" ");
            } else if (payEntitlementForm.getHidNotifyingDeptCode() != null && !payEntitlementForm.getHidNotifyingDeptCode().equals("")) {
                deptName = getDeptName(payEntitlementForm.getHidNotifyingDeptCode());
                confMessage.append("IN PURSUANCE OF ").append(deptName).append(" DEPARTMENT ");
            }

            if ((authName != null && !authName.trim().equals("")) || (offName != null && !offName.trim().equals("")) || (deptName != null && !deptName.trim().equals(""))) {
                if (payEntitlementForm.getTxtNotOrdNo() != null && !payEntitlementForm.getTxtNotOrdNo().equals("")) {
                    confMessage.append(" VIDE NOTIFICATION/ OFFICE ORDER NO. ").append(payEntitlementForm.getTxtNotOrdNo());
                    if (payEntitlementForm.getTxtNotOrdDt() != null && !payEntitlementForm.getTxtNotOrdDt().equals("")) {
                        confMessage.append(" DATED ").append(payEntitlementForm.getTxtNotOrdDt()).append(".");
                    }
                }
            } else {
                if (payEntitlementForm.getTxtNotOrdNo() != null && !payEntitlementForm.getTxtNotOrdNo().equals("")) {
                    confMessage.append(" NOTIFICATION/ OFFICE ORDER NO. ").append(payEntitlementForm.getTxtNotOrdNo());
                    if (payEntitlementForm.getTxtNotOrdDt() != null && !payEntitlementForm.getTxtNotOrdDt().equals("")) {
                        confMessage.append(" DATED ").append(payEntitlementForm.getTxtNotOrdDt()).append(".");
                    }
                }
            }

            //notString = getNotificationString(payEntitlementForm.getTxtNotOrdNo(), payEntitlementForm.getTxtNotOrdDt(), deptName, offName, authName);
            confMessage.append("PAY IS ENTITLED ");
            confMessage.append("AND ");

            if (payEntitlementForm.getTxtBasic() != null && !payEntitlementForm.getTxtBasic().trim().equals("") && !payEntitlementForm.getTxtBasic().trim().equals("0")) {
                confMessage.append(proNoun).append(" IS ALLOWED TO DRAW PAY @ RS. ").append(CommonFunctions.formatNumber(payEntitlementForm.getTxtBasic())).append("/- PM ");
            }
            if (payEntitlementForm.getSltPayScale() != null && !payEntitlementForm.getSltPayScale().trim().equals("")) {
                confMessage.append(" IN THE SCALE OF PAY RS. ").append(payEntitlementForm.getSltPayScale().toUpperCase()).append("/-");
            }

            if (payEntitlementForm.getTxtGP() != null && !payEntitlementForm.getTxtGP().trim().equals("") && !payEntitlementForm.getTxtGP().trim().equals("0")) {
                confMessage.append(" WITH GRADE PAY RS. ").append(payEntitlementForm.getTxtGP().toUpperCase()).append("/-");
            }

            if (payEntitlementForm.getTxtSP() != null && !payEntitlementForm.getTxtSP().trim().equals("") && !payEntitlementForm.getTxtSP().trim().equals("0")) {
                confMessage.append(" + SPECIAL PAY @ RS. ").append(CommonFunctions.formatNumber(payEntitlementForm.getTxtSP().toUpperCase())).append("/- ");
            }
            if (payEntitlementForm.getTxtPP() != null && !payEntitlementForm.getTxtPP().trim().equals("") && !payEntitlementForm.getTxtPP().trim().equals("0")) {
                confMessage.append(" + PERSONAL PAY @ RS. ").append(CommonFunctions.formatNumber(payEntitlementForm.getTxtPP().toUpperCase())).append("/- ");
            }
            if (payEntitlementForm.getTxtDescOP() != null && !payEntitlementForm.getTxtDescOP().trim().equals("")) {
                if (payEntitlementForm.getTxtOP() != null && !payEntitlementForm.getTxtOP().trim().equals("") && !payEntitlementForm.getTxtOP().equals("0")) {
                    confMessage.append(" ALONG WITH ").append(payEntitlementForm.getTxtDescOP().toUpperCase()).append(" @ RS. ").append(CommonFunctions.formatNumber(payEntitlementForm.getTxtOP())).append("/-");
                }
            } else {
                if (payEntitlementForm.getTxtOP() != null && !payEntitlementForm.getTxtOP().equals("") && !payEntitlementForm.getTxtOP().equals("0")) {
                    confMessage.append(" ALONG WITH OTHER PAY @ RS. ").append(CommonFunctions.formatNumber(payEntitlementForm.getTxtOP())).append("/-");
                }
            }
            if ((payEntitlementForm.getTxtSP() != null && !payEntitlementForm.getTxtSP().equals("") && !payEntitlementForm.getTxtSP().equals("0"))
                    || (payEntitlementForm.getTxtBasic() != null && !payEntitlementForm.getTxtBasic().trim().equals("") && !payEntitlementForm.getTxtBasic().trim().equals("0"))
                    || (payEntitlementForm.getTxtGP() != null && !payEntitlementForm.getTxtGP().trim().equals("") && !payEntitlementForm.getTxtGP().trim().equals("0"))
                    || (payEntitlementForm.getTxtPP() != null && !payEntitlementForm.getTxtPP().trim().equals("") && !payEntitlementForm.getTxtPP().trim().equals("0"))
                    || (payEntitlementForm.getTxtOP() != null && !payEntitlementForm.getTxtOP().trim().equals("") && !payEntitlementForm.getTxtOP().trim().equals("0"))) {
                confMessage.append(" PM ");
            }
            if (payEntitlementForm.getTxtWEFDt() != null && !payEntitlementForm.getTxtWEFDt().equals("")) {
                confMessage.append(" WEF ").append(payEntitlementForm.getTxtWEFDt().toUpperCase());
                if (payEntitlementForm.getSltWEFTime() != null && !payEntitlementForm.getSltWEFTime().equals("")) {
                    confMessage.append(" (" + payEntitlementForm.getSltWEFTime().toUpperCase() + ")");
                }
            }

//            if (notString != null && !notString.trim().equals("")) {
//                confMessage.append(notString);
//            }
            confMessage.append(".");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return confMessage.toString().toUpperCase();

    }

    @Override
    public String getRedesignationDetails(Redesignation redesig, int notid, String notType) {
        StringBuffer outputRedesignationDetails = new StringBuffer();
        String nxtSpn = null;
        String superNote = null;
        ArrayList addspcList = new ArrayList();
        ArrayList addspnList = new ArrayList();
        ResultSet rs1 = null;
        String proNoun = null;
        String offName = null;
        String deptName = null;
        String authName = null;
        String notString = "";
        String othSpn = "";
        //String othNextSpn = "";
        String fieldoffname = "";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            con = dataSource.getConnection();

            ps = con.prepareStatement("select getdeptname('" + redesig.getHidAuthDeptCode() + "') deptName, getofficeen('" + redesig.getHidAuthOffCode() + "') offName,  getspn('" + redesig.getAuthSpc() + "') authName ");
            rs = ps.executeQuery();
            if (rs.next()) {
                deptName = rs.getString("deptName");
                offName = rs.getString("offName");
                authName = rs.getString("authName");
            }
            DataBaseFunctions.closeSqlObjects(rs, ps);

            NotificationBean nfb = getNotificationDetails(notid, notType);

            notString = getNotificationString(nfb.getOrdno(), nfb.getTxtNotOrdDt(), deptName, offName, authName);

            proNoun = getPronoun(redesig.getEmpid());

            ps = con.prepareStatement("SELECT SPC FROM EMP_JOIN WHERE NOT_ID=? ORDER BY JOIN_ID");
            ps.setInt(1, notid);
            rs1 = ps.executeQuery();
            while (rs1.next()) {
                if (rs1.getString("SPC") != null && !rs1.getString("SPC").equalsIgnoreCase("")) {
                    addspcList.add(rs1.getString("SPC"));
                }
            }
            DataBaseFunctions.closeSqlObjects(rs1, ps);

            if (addspcList.size() != 0) {
                for (int i = 0; i < addspcList.size(); i++) {
                    addspnList.add(getSPN((String) addspcList.get(i)));
                }
            }

            /*
             if (notid != null) {
             superNote = ServiceHistoryData.getSupersseionNote(notid, st);
             }
             */
            if (superNote != null && !superNote.equals("")) {
                outputRedesignationDetails.append(superNote);
            }
            System.out.println("notType is:" + notType);
            if (notType != null) {

                if (notType.trim().equalsIgnoreCase("TRANSFER") || notType.trim().equalsIgnoreCase("POSTING") || notType.trim().equalsIgnoreCase("REDESIGNATION")) {

                    if (notType.trim().equalsIgnoreCase("TRANSFER")) {
                        outputRedesignationDetails.append("TRANSFER AND POSTED ");
                    } else if (notType.trim().equalsIgnoreCase("POSTING")) {
                        outputRedesignationDetails.append(" APPOINTED ");
                    } else if (notType.trim().equalsIgnoreCase("REDESIGNATION")) {
                        outputRedesignationDetails.append(" REDESIGNATED AND POSTED ");
                    }

                    if (redesig.getPostedspc() != null && !redesig.getPostedspc().equalsIgnoreCase("")) {

                        nxtSpn = getSPN(redesig.getPostedspc());
                        outputRedesignationDetails.append(" AS ").append(nxtSpn);

                        if (redesig.getSltPostedFieldOff() != null && !redesig.getSltPostedFieldOff().equals("")) {
                            outputRedesignationDetails.append(" ( " + getOffName(redesig.getSltPostedFieldOff()) + " ) ");
                        }
                    } else if (redesig.getHidPostedOffCode() != null && !redesig.getHidPostedOffCode().equals("")) {
                        outputRedesignationDetails.append(" TO ").append(getOffName(redesig.getHidPostedOffCode()));

                    } else if (redesig.getHidPostedDeptCode() != null && !redesig.getHidPostedDeptCode().equals("")) {
                        outputRedesignationDetails.append(" TO ").append(getDeptName(redesig.getHidPostedDeptCode())).append(" DEPARTMENT ");
                    }
                } else if (notType.trim().equalsIgnoreCase("SERVICE_DISPOSAL")) {
                    outputRedesignationDetails.append(" SERVICES PLACED ");

                    /*
                     if ((notBean.getSltnpoffname() != null && !notBean.getSltnpoffname().equals("")) || (notBean.getTxtpostingoff() != null && !notBean.getTxtpostingoff().equals(""))) {
                     if (notBean.getSltnpoffname() != null && !notBean.getSltnpoffname().equals("")) {
                     outputTransferDetails.append(" AT THE DISPOSAL OF ").append(getOffName(notBean.getSltnpoffname(), st));
                     } else if (notBean.getTxtpostingoff() != null && !notBean.getTxtpostingoff().equals("")) {
                     outputTransferDetails.append(" AT THE DISPOSAL OF ").append(notBean.getTxtpostingoff());
                     }
                     } else if ((notBean.getSltnpdeptname() != null && !notBean.getSltnpdeptname().equals("")) || (notBean.getTxtpostingdept() != null && !notBean.getTxtpostingdept().equals(""))) {
                     if (notBean.getSltnpdeptname() != null && !notBean.getSltnpdeptname().equals("")) {
                     outputTransferDetails.append(" AT THE DISPOSAL OF ").append(ServiceHistoryData.getDeptName(notBean.getSltnpdeptname(), st)).append(" DEPARTMENT ");
                     } else if (notBean.getTxtpostingdept() != null && !notBean.getTxtpostingdept().equals("")) {
                     outputTransferDetails.append(" AT THE DISPOSAL OF ").append(notBean.getTxtpostingdept()).append(" DEPARTMENT ");
                     }
                     }

                     if ((notBean.getSltnpspc() != null && !notBean.getSltnpspc().equalsIgnoreCase("")) || (notBean.getTxtpostingauth() != null && !notBean.getTxtpostingauth().equalsIgnoreCase(""))) {
                     if (notBean.getTxtpostingauth() != null && !notBean.getTxtpostingauth().equalsIgnoreCase("")) {
                     outputTransferDetails.append(" AS ").append(notBean.getTxtpostingauth());
                     } else if (notBean.getSltnpspc() != null && !notBean.getSltnpspc().equalsIgnoreCase("")) {
                     nxtSpn = getSPN(notBean.getSltnpspc());
                     outputTransferDetails.append(" AS ").append(nxtSpn);
                     }
                     }
                            
                     */
                }

                if (notString != null) {
                    outputRedesignationDetails.append(notString);
                    outputRedesignationDetails.append(".");
                }

                if (addspnList.size() != 0 && addspnList.size() > 1) {
                    outputRedesignationDetails.append(" ALONG WITH ");
                    for (int i = 1; i < addspnList.size(); i++) {
                        if (i == addspnList.size() - 1) {
                            outputRedesignationDetails.append(addspnList.get(i));
                        } else {
                            outputRedesignationDetails.append(addspnList.get(i)).append(", ");
                        }
                    }

                }

            }

            if (redesig.getTxtBasic() != null && !redesig.getTxtBasic().trim().equals("") && !redesig.getTxtBasic().trim().equals("0")) {
                outputRedesignationDetails.append(proNoun).append(" IS ALLOWED TO DRAW PAY @ RS. ").append(CommonFunctions.formatNumber(redesig.getTxtBasic())).append("/- PM ");
            }
            if (redesig.getSltPayScale() != null && !redesig.getSltPayScale().trim().equals("")) {
                outputRedesignationDetails.append(" IN THE SCALE OF PAY RS. ").append(redesig.getSltPayScale().toUpperCase()).append("/-");
            }

            if (redesig.getTxtGP() != null && !redesig.getTxtGP().trim().equals("") && !redesig.getTxtGP().trim().equals("0")) {
                outputRedesignationDetails.append(" WITH GRADE PAY RS. ").append(redesig.getTxtGP().toUpperCase()).append("/-");
            }

            if (redesig.getTxtSP() != null && !redesig.getTxtSP().trim().equals("") && !redesig.getTxtSP().trim().equals("0")) {
                outputRedesignationDetails.append(" + SPECIAL PAY @ RS. ").append(CommonFunctions.formatNumber(redesig.getTxtSP().toUpperCase())).append("/- ");
            }
            if (redesig.getTxtPP() != null && !redesig.getTxtPP().trim().equals("") && !redesig.getTxtPP().trim().equals("0")) {
                outputRedesignationDetails.append(" + PERSONAL PAY @ RS. ").append(CommonFunctions.formatNumber(redesig.getTxtPP().toUpperCase())).append("/- ");
            }
            if (redesig.getTxtDescOP() != null && !redesig.getTxtDescOP().trim().equals("")) {
                if (redesig.getTxtOP() != null && !redesig.getTxtOP().trim().equals("") && !redesig.getTxtOP().equals("0")) {
                    outputRedesignationDetails.append(" ALONG WITH ").append(redesig.getTxtDescOP().toUpperCase()).append(" @ RS. ").append(CommonFunctions.formatNumber(redesig.getTxtOP())).append("/-");
                }
            } else {
                if (redesig.getTxtOP() != null && !redesig.getTxtOP().equals("") && !redesig.getTxtOP().equals("0")) {
                    outputRedesignationDetails.append(" ALONG WITH OTHER PAY @ RS. ").append(CommonFunctions.formatNumber(redesig.getTxtOP())).append("/-");
                }
            }
            if ((redesig.getTxtSP() != null && !redesig.getTxtSP().equals("") && !redesig.getTxtSP().equals("0"))
                    || (redesig.getTxtBasic() != null && !redesig.getTxtBasic().trim().equals("") && !redesig.getTxtBasic().trim().equals("0"))
                    || (redesig.getTxtGP() != null && !redesig.getTxtGP().trim().equals("") && !redesig.getTxtGP().trim().equals("0"))
                    || (redesig.getTxtPP() != null && !redesig.getTxtPP().trim().equals("") && !redesig.getTxtPP().trim().equals("0"))
                    || (redesig.getTxtOP() != null && !redesig.getTxtOP().trim().equals("") && !redesig.getTxtOP().trim().equals("0"))) {
                outputRedesignationDetails.append(" PM ");
            }
            if (redesig.getTxtWEFDt() != null && !redesig.getTxtWEFDt().equals("")) {
                outputRedesignationDetails.append(" WEF ").append(redesig.getTxtWEFDt().toUpperCase());
                if (redesig.getSltWEFTime() != null && !redesig.getSltWEFTime().equals("")) {
                    outputRedesignationDetails.append(" (" + redesig.getSltWEFTime().toUpperCase() + ")");
                }
            }

            if (outputRedesignationDetails.toString() != null && !outputRedesignationDetails.equals("")) {
                if (!outputRedesignationDetails.toString().trim().endsWith(".")) {
                    outputRedesignationDetails.append(".");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs1);
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return outputRedesignationDetails.toString().toUpperCase();
    }

    public String getMiscllaneousString(NotificationBean nfb, int notid, String notificationtype) {
        StringBuffer miscllaneouString = new StringBuffer();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        String authName = null;
        String offName = null;
        String deptName = null;
        String proNoun = null;
        String notString = "";
        String othSpn = "";
        String superNote = null;
        String orddt1 = null;

        try {

            proNoun = getPronoun(nfb.getEmpId());
            orddt1 = CommonFunctions.getFormattedOutputDate1(nfb.getOrdDate());

            if (nfb.getSancAuthCode() != null && !nfb.getSancAuthCode().equals("")) {
                authName = getSPN(nfb.getSancAuthCode());
                miscllaneouString.append("IN PURSUANCE OF ").append(authName).append(" ");
            } else if (nfb.getSancOffCode() != null && !nfb.getSancOffCode().equals("")) {
                offName = getOffName(nfb.getSancOffCode());
                miscllaneouString.append("IN PURSUANCE OF ").append(offName).append(" ");
            } else if (nfb.getSancDeptCode() != null && !nfb.getSancDeptCode().equals("")) {
                deptName = getDeptName(nfb.getSancDeptCode());
                miscllaneouString.append("IN PURSUANCE OF ").append(deptName).append(" DEPARTMENT ");
            }

            if ((authName != null && !authName.trim().equals("")) || (offName != null && !offName.trim().equals("")) || (deptName != null && !deptName.trim().equals(""))) {
                if (nfb.getOrdno() != null && !nfb.getOrdno().equals("")) {
                    miscllaneouString.append(" VIDE NOTIFICATION/ OFFICE ORDER NO. ").append(nfb.getOrdno().toUpperCase());
                    if (nfb.getOrdDate() != null && !nfb.getOrdDate().equals("")) {
                        miscllaneouString.append(" DATED ").append(orddt1).append(" .");
                    }
                }
            } else {
                if (nfb.getOrdno() != null && !nfb.getOrdno().equals("")) {
                    miscllaneouString.append(" NOTIFICATION/ OFFICE ORDER NO. ").append(nfb.getOrdno().toUpperCase());
                    if (nfb.getOrdDate() != null && !nfb.getOrdDate().equals("")) {
                        miscllaneouString.append(" DATED ").append(orddt1).append(" .");
                    }
                }

            }

            miscllaneouString.append(".");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return miscllaneouString.toString().toUpperCase();
    }

    @Override
    public String getAllowedToOfficiateDetails(AllowedToOfficiateModel officiation, int notid, String notType) {

        StringBuffer outputofficiate = new StringBuffer();
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String notString = "";
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT * FROM EMP_CADRE WHERE NOT_ID=? ");
            pst.setInt(1, notid);
            rs = pst.executeQuery();
            if (rs.next()) {

                if (notType != null && notType.equalsIgnoreCase("OFFICIATE")) {
                    outputofficiate.append(" ALLOWED TO OFFICIATE ");

                    if (officiation.getSltGrade() != null && !officiation.getSltGrade().equalsIgnoreCase("")) {
                        outputofficiate.append(" IN THE ").append(officiation.getSltGrade());
                    }
                    if (officiation.getSltCadre() != null && !officiation.getSltCadre().equalsIgnoreCase("")) {
                        outputofficiate.append(" OF ").append(getCadreName(officiation.getSltCadre()));
                    }
                    if (officiation.getTxtCadreJoiningWEFDt() != null && !officiation.getTxtCadreJoiningWEFDt().equals("")) {
                        outputofficiate.append(" WEF ").append(officiation.getTxtCadreJoiningWEFDt().toUpperCase());

                        if (officiation.getSltCadreJoiningWEFTime() != null && !officiation.getSltCadreJoiningWEFTime().equals("")) {
                            //outputofficiate.append("(").append(officiation.getSltCadreJoiningWEFTime()).append(")");
                            if (officiation.getSltCadreJoiningWEFTime().equals("FN")) {
                                outputofficiate.append(", ").append(" FORE NOON");
                            } else {
                                outputofficiate.append(", ").append(" AFTER NOON");
                            }
                            outputofficiate.append(" VIDE ");

                        }
                        if (officiation.getSltCadreDept() != null && !officiation.getSltCadreDept().equals("")) {
                            outputofficiate.append(getDeptName(officiation.getSltCadreDept())).append(" DEPARTMENT, GOVERNMENT OF ODISHA ");
                        }
                        if (officiation.getOrdno() != null && !officiation.getOrdno().equals("")) {
                            outputofficiate.append(" NOTIFICATION OFFICE ORDER NO. ").append(officiation.getOrdno().toUpperCase());

                        }
                        if (officiation.getOrdDate() != null && !officiation.getOrdDate().equals("")) {
                            outputofficiate.append(" DATED ").append(officiation.getOrdDate().toUpperCase());
                        }
                        outputofficiate.append(".");

                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return outputofficiate.toString();
    }

    @Override
    public String getPunishmentDetails(PunishmentBean pb, int notid, String notType) {
        StringBuffer punishmentdetails = new StringBuffer();
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT PUNISHMENT,PUNISH_ID FROM G_PUNISHMENT WHERE PUNISH_ID=?");
            pst.setString(1, pb.getPunishId());
            rs = pst.executeQuery();
            if (rs.next()) {
                String punishmentName = rs.getString("PUNISHMENT");

                if (notType != null && notType.equalsIgnoreCase("ADM_ACTION")) {
                    punishmentdetails.append(" TAKEN ADMINISTRATIVE ACTION/PUNISHMENT ON ");
                    if (rs.getString("punish_id") != null && rs.getString("punish_id").equals("23")) {
                        punishmentdetails.append(pb.getOthCategory().toUpperCase().trim()).append(" (OTHERS) ");
                    } else {
                        punishmentdetails.append(punishmentName);
                    }
                    if (pb.getReason() != null && !pb.getReason().equals("")) {
                        punishmentdetails.append(" FOR ").append(pb.getReason().toUpperCase().trim());
                    }

                    if (pb.getAuthPostName() != null && !pb.getAuthPostName().equals("")) {
                        punishmentdetails.append(" VIDE ").append(pb.getAuthPostName());
                    }

                    if (pb.getOrderNumber() != null && !pb.getOrderNumber().equals("")) {
                        punishmentdetails.append("  NOTIFICATION NO ").append(pb.getOrderNumber().toUpperCase()).append(" DATE ").append(pb.getOrderDate().toUpperCase());
                    }

                }
            }
            punishmentdetails.append(".");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return punishmentdetails.toString();
    }

    @Override
    public String getBrassAllotmentDetails(BrassAllotList bsallot) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String wef = null;
        String weft = null;
        String distCode = null;
        String distName = null;
        String brassNo = null;

        StringBuffer brassallotmentInfo = new StringBuffer();

        String authName = null;
        String offName = null;
        String deptName = null;
        try {
            con = this.dataSource.getConnection();

            pst = con.prepareStatement("select getdeptname('" + bsallot.getHidVerifyingDeptCode() + "') deptName, getofficeen('" + bsallot.getHidVerifyingOffCode() + "') offName,  getspn('" + bsallot.getHidVerifyingSpc() + "') authName ");
            rs = pst.executeQuery();
            if (rs.next()) {
                deptName = rs.getString("deptName");
                offName = rs.getString("offName");
                authName = rs.getString("authName");
            }
            DataBaseFunctions.closeSqlObjects(rs, pst);

            if (bsallot.getWefDt() != null && !bsallot.getWefDt().trim().equals("")) {
                wef = bsallot.getWefDt().toUpperCase();
            }
            if (bsallot.getWefTime() != null && !bsallot.getWefTime().trim().equals("")) {
                weft = bsallot.getWefTime();
            }
            if (bsallot.getSltDist() != null && !bsallot.getSltDist().trim().equals("")) {
                distCode = bsallot.getSltDist();
            }
            if (bsallot.getBrassNo() != null && !bsallot.getBrassNo().trim().equals("")) {
                brassNo = bsallot.getBrassNo();
            }

            if (distCode != null) {
                distName = getDistrictName(distCode);
            }

            brassallotmentInfo.append("ALLOTED ");
            if (distName != null && !distName.trim().equals("")) {
                brassallotmentInfo.append("").append(distName).append(" DISTRICT, ");
                if (brassNo != null) {
                    brassallotmentInfo.append(" BRASS NO. ").append(brassNo).append(" ");
                }
            } else {
                if (brassNo != null) {
                    brassallotmentInfo.append(" BRASS NO. ").append(brassNo).append(" ");
                }
            }

            if (authName != null && !authName.trim().equals("")) {
                brassallotmentInfo.append(" ").append(authName).append(",");
            } else if (offName != null && !offName.trim().equals("")) {
                brassallotmentInfo.append(offName).append(", ");
            } else if (deptName != null && !deptName.trim().equals("")) {
                brassallotmentInfo.append(deptName).append(" DEPARTMENT ");
            }

            if ((authName != null && !authName.trim().equals("")) || (offName != null && !offName.trim().equals("")) || (deptName != null && !deptName.trim().equals(""))) {
                if (bsallot.getOrdno() != null && !bsallot.getOrdno().trim().equals("")) {
                    brassallotmentInfo.append(" VIDE NOTIFICATION/ OFFICE ORDER NO. ").append(bsallot.getOrdno());
                    if (bsallot.getOrdDate() != null && !bsallot.getOrdDate().trim().equals("")) {
                        brassallotmentInfo.append(" DATED ").append(bsallot.getOrdDate());
                        if (wef != null && !wef.trim().equals("")) {
                            brassallotmentInfo.append(" WEF ").append(wef);
                        }
                    }
                }
            } else {
                if (bsallot.getOrdno() != null && !bsallot.getOrdno().trim().equals("")) {
                    brassallotmentInfo.append(" VIDE NOTIFICATION/ OFFICE ORDER NO. ").append(bsallot.getOrdno());
                    if (bsallot.getOrdDate() != null && !bsallot.getOrdDate().trim().equals("")) {
                        brassallotmentInfo.append(" DATED ").append(bsallot.getOrdDate());
                        if (wef != null && !wef.trim().equals("")) {
                            brassallotmentInfo.append(" WEF ").append(wef);
                        }
                    }
                }
            }
            brassallotmentInfo.append(".");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return brassallotmentInfo.toString().toUpperCase();
    }

    private String getDistrictName(String distcode) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String distname = "";
        try {
            con = this.dataSource.getConnection();

            String sql = "select dist_name from g_district where dist_code=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, distcode);
            rs = pst.executeQuery();
            if (rs.next()) {
                distname = rs.getString("dist_name");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return distname;
    }

    private String getOtherSpn(String othSpc) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;
        String spn = "";
        try {
            con = this.dataSource.getConnection();

            if (othSpc != null && !othSpc.equals("")) {
                pst = con.prepareStatement("SELECT oth_spc, off_en FROM g_oth_spc WHERE other_spc_id=? and is_active='Y'");
                pst.setInt(1, Integer.parseInt(othSpc));
                rs = pst.executeQuery();
                if (rs.next()) {
                    spn = rs.getString("off_en");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return spn;
    }

    @Override
    public String getAllowancesDeductionsLangDetails(AllowanceModel allowBean) {
        StringBuffer allowDedDetailsList = new StringBuffer();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("Select ad_code,ad_desc from g_ad_list where ad_code=?");
            pst.setString(1, allowBean.getSltAllowanceCode());
            rs = pst.executeQuery();
            if (rs.next()) {
                String ADDesc = rs.getString("ad_desc");
                if (ADDesc != null && !ADDesc.equals("")) {
                    allowDedDetailsList.append("GRANTED ").append(ADDesc).append(" OF ");
                }
                if (allowBean.getAllowanceAmt() != null && !allowBean.getAllowanceAmt().equals("")) {
                    allowDedDetailsList.append("RS. ").append(allowBean.getAllowanceAmt()).append("/-");
                }
                allowDedDetailsList.append(" VIDE ");
                if (allowBean.getNotifyingSpc() != null && !allowBean.getNotifyingSpc().equals("")) {
                    allowDedDetailsList.append(allowBean.getNotifyingPostName());
                }
                if (allowBean.getOrdno() != null && !allowBean.getOrdno().equals("")) {
                    allowDedDetailsList.append(" NOTIFICATION/ OFFICE ORDER NO. ").append(allowBean.getOrdno().toUpperCase()).append(" DATED ").append(allowBean.getOrdDate().toUpperCase());
                }
                allowDedDetailsList.append(".");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);

        }
        return allowDedDetailsList.toString();

    }

    @Override
    public String getRelieveFromCadreDetails(RelieveCadreForm relieveCadreForm, int notid, String notType) {
        StringBuffer relievefromCadreLang = new StringBuffer();
        Connection con = null;
        ResultSet rs = null;
        NotificationBean nb = new NotificationBean();
        String postClassification = null;
        String postStatus = null;
        PreparedStatement pst = null;
        try {
            con = this.dataSource.getConnection();
            if (notType != null && notType.equalsIgnoreCase("RELIEVE_CADRE")) {
                if (relieveCadreForm.getSltCadre() != null && !relieveCadreForm.getSltCadre().equals("")) {
                    relievefromCadreLang.append("RELIEVED FROM ").append(getCadreName(relieveCadreForm.getSltCadre()).toUpperCase());
                }
                if (relieveCadreForm.getRdPostClassification() != null && !relieveCadreForm.getRdPostClassification().equals("")) {
                    if (relieveCadreForm.getRdPostClassification().equals("A")) {
                        postClassification = "ADHOC";
                    } else if (relieveCadreForm.getRdPostClassification().equals("T")) {
                        postClassification = "TEMPORARY";
                    } else if (relieveCadreForm.getRdPostClassification().equals("O")) {
                        postClassification = "ON PROBATION";
                    } else if (relieveCadreForm.getRdPostClassification().equals("P")) {
                        postClassification = "PERMANENT";
                    }
                    relievefromCadreLang.append(" ON ").append(postClassification);
                }
                if (relieveCadreForm.getRdPostStatus() != null && !relieveCadreForm.getRdPostStatus().equals("")) {
                    if (relieveCadreForm.getRdPostStatus().equals("O")) {
                        postStatus = "OFFICIATING";
                    } else if (relieveCadreForm.getRdPostStatus().equals("S")) {
                        postStatus = "SUBSTANTIVE";
                    } else if (relieveCadreForm.getRdPostStatus().equals("A")) {
                        postStatus = "";
                    }
                    relievefromCadreLang.append("(").append(postStatus).append(")");
                }
            }
            relievefromCadreLang.append(" VIDE ");
            if (relieveCadreForm.getSltCadreDept() != null && !relieveCadreForm.getSltCadreDept().equals("")) {
                relievefromCadreLang.append(getDeptName(relieveCadreForm.getSltCadreDept())).append(" DEPARTMENT");
            }
            if (relieveCadreForm.getTxtNotOrdNo() != null && !relieveCadreForm.getTxtNotOrdNo().equals("")) {
                relievefromCadreLang.append(" NOTIFICATION/ OFFICE ORDER NO. ").append(relieveCadreForm.getTxtNotOrdNo().toUpperCase()).append(" DATED ").append(relieveCadreForm.getTxtNotOrdDt().toUpperCase());
            }
            relievefromCadreLang.append(".");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return relievefromCadreLang.toString();

    }

    @Override
    public String getAllotmentToCadreLangDetails(AllotmentToCadreForm allotmentToCadre, int notid, String notType) {
        StringBuffer allotmentToCadreLangDetails = new StringBuffer();
        Connection con = null;
        try {
            con = this.dataSource.getConnection();
            if (notType != null && notType.equals("ALLOT_CADRE")) {
                allotmentToCadreLangDetails.append("ALLOTED TO ");
                if (allotmentToCadre.getSltDescription() != null && !allotmentToCadre.getSltDescription().equals("")) {
                    allotmentToCadreLangDetails.append(allotmentToCadre.getSltDescription());
                }

                if (allotmentToCadre.getSltCadre() != null && !allotmentToCadre.getSltCadre().equals("")) {
                    allotmentToCadreLangDetails.append(getCadreName(allotmentToCadre.getSltCadre()).toUpperCase()).append(" CADRE ");
                } else {
                    allotmentToCadreLangDetails.append(" CADRE ");
                }
                allotmentToCadreLangDetails.append(" VIDE ");
                if (allotmentToCadre.getNotifyingPostName() != null && !allotmentToCadre.getNotifyingPostName().equals("")) {
                    allotmentToCadreLangDetails.append(getSPN(allotmentToCadre.getNotifyingSpc()));
                }
                if (allotmentToCadre.getTxtNotOrdNo() != null && !allotmentToCadre.getTxtNotOrdNo().equals("")) {
                    allotmentToCadreLangDetails.append(" NOTIFICATION/ OFFICE ORDER NO. ").append(allotmentToCadre.getTxtNotOrdNo().toUpperCase()).append(" DATED ").append(allotmentToCadre.getTxtNotOrdDt().toUpperCase());
                }
            }
            allotmentToCadreLangDetails.append(".");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return allotmentToCadreLangDetails.toString();

    }

    @Override
    public String getQtrSurrenderLangDetails(EmpQuarterBean eqBean, String qaID, String empid, String notType) {
        StringBuffer getQtrSurrenderDetails = new StringBuffer();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String orddt1 = null;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("select eq.qa_id,eq.not_type,en.not_id,en.ordno,en.orddt,en.auth,eq.emp_id,quarter_no,allotment_date,possession_date,quarter_rent,address,sewerage_rent,surrender_date from\n"
                    + "(select * from emp_qtr_allot where emp_id=? and  qa_id=?)eq\n"
                    + "inner join emp_qtr_surrender es on eq.emp_id=es.emp_id and eq.qa_id=es.qa_id \n"
                    + "inner join (select ordno,orddt,auth,not_id from emp_notification where not_type='QTR_SURRENDER')en\n"
                    + "on es.not_id=en.not_id");
            ps.setString(1, empid);
            ps.setInt(2, Integer.parseInt(qaID));
            rs = ps.executeQuery();
            if (rs.next()) {
                orddt1 = CommonFunctions.getFormattedOutputDate1(rs.getDate("orddt"));
                if (notType != null && notType.equals("QTR_SURRENDER")) {
                    getQtrSurrenderDetails.append("SURRENDERED QUARTER");
                }
                if (rs.getString("quarter_no") != null && !rs.getString("quarter_no").equals("")) {
                    getQtrSurrenderDetails.append(" NO ").append(rs.getString("quarter_no"));
                }
                if (rs.getDate("orddt") != null && !rs.getDate("orddt").equals("")) {
                    getQtrSurrenderDetails.append(" ON ").append(orddt1.toUpperCase());
                }
                if (rs.getDate("allotment_date") != null && !rs.getDate("allotment_date").equals("")) {
                    getQtrSurrenderDetails.append(" QUARTER WAS ALLOTTED ON ").append(CommonFunctions.getFormattedOutputDate1(rs.getDate("allotment_date")).toUpperCase());
                }
                if (rs.getDate("possession_date") != null && !rs.getDate("possession_date").equals("")) {
                    getQtrSurrenderDetails.append(" AND POSSESSED ON ").append(CommonFunctions.getFormattedOutputDate1(rs.getDate("possession_date")).toUpperCase());
                }
                if (rs.getString("quarter_rent") != null && !rs.getString("quarter_rent").equals("")) {
                    getQtrSurrenderDetails.append(" WITH MONTHLY RENT ").append(rs.getString("quarter_rent"));
                }
                if (rs.getString("sewerage_rent") != null && !rs.getString("sewerage_rent").equals("")) {
                    getQtrSurrenderDetails.append(" AND WATER RENT ").append(rs.getString("sewerage_rent"));
                }
                if (rs.getString("ordno") != null && !rs.getString("ordno").equals("")) {
                    getQtrSurrenderDetails.append(" VIDE NOTIFICATION/ OFFICE ORDER NO.").append(rs.getString("ordno").toUpperCase()).append(" DATED ").append(orddt1.toUpperCase());
                }
            }
            getQtrSurrenderDetails.append(".");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);

        }
        return getQtrSurrenderDetails.toString();
    }

    @Override
    public String getRedeploymentLangDetails(Redeployment redeploymentForm, int notid, String notType) {
        StringBuffer getredploymentDetails = new StringBuffer();
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String postClassification = null;
        try {

            con = this.dataSource.getConnection();
            pst = con.prepareStatement("select not_id,ordno,orddt,dept_code,off_code,auth,note,if_visible,sb_description from emp_notification where EMP_ID=? AND NOT_ID=?");
            pst.setString(1, redeploymentForm.getEmpid());
            pst.setInt(2, notid);
            rs = pst.executeQuery();
            if (rs.next()) {

                if (notType != null && notType.equals("REDEPLOYMENT")) {
                    getredploymentDetails.append("ON REDEPLOYMENT ");
                    if (redeploymentForm.getPostedspc() != null && !redeploymentForm.getPostedspc().equals("")) {
                        getredploymentDetails.append("APPOINTED AS ").append(getSPN(redeploymentForm.getPostedspc()));
                    }
                    if (redeploymentForm.getTxtBasic() != null && !redeploymentForm.getTxtBasic().equals("")) {
                        getredploymentDetails.append(" WITH PAY @ RS.").append(redeploymentForm.getTxtBasic()).append("/- PM");
                    }
                    if (redeploymentForm.getSltPayScale() != null && !redeploymentForm.getSltPayScale().equals("")) {
                        getredploymentDetails.append(" IN THE SCALE OF PAY ").append(redeploymentForm.getSltPayScale());
                    }
                    if (redeploymentForm.getTxtWEFDt() != null && !redeploymentForm.getTxtWEFDt().equals("")) {
                        getredploymentDetails.append(" WEF ").append(redeploymentForm.getTxtWEFDt().toUpperCase());
                    }
                    if (redeploymentForm.getHidNotifyingDeptCode() != null && !redeploymentForm.getHidNotifyingDeptCode().equals("")) {
                        getredploymentDetails.append(" VIDE ").append(getDeptName(redeploymentForm.getHidNotifyingDeptCode())).append(" DEPARTMENT ");
                    }
                    if (rs.getString("ordno") != null && !rs.getString("ordno").equals("")) {
                        getredploymentDetails.append(" NOTIFICATION/ OFFICE ORDER NO. ").append(rs.getString("ordno").toUpperCase());
                    }
                    if (rs.getString("orddt") != null && !rs.getString("orddt").equals("")) {
                        getredploymentDetails.append(" DATED ").append(CommonFunctions.getFormattedOutputDate1(rs.getDate("orddt")).toUpperCase());
                    }
                    if (redeploymentForm.getRdPostClassification() != null && !redeploymentForm.getRdPostClassification().equals("")) {
                        if (redeploymentForm.getRdPostClassification().equals("A")) {
                            postClassification = "ADHOC";
                        } else if (redeploymentForm.getRdPostClassification().equals("T")) {
                            postClassification = "TEMPORARY";
                        } else if (redeploymentForm.getRdPostClassification().equals("O")) {
                            postClassification = "ON PROBATION";
                        } else if (redeploymentForm.getRdPostClassification().equals("P")) {
                            postClassification = "PERMANENT";
                        }
                        getredploymentDetails.append(" ON ").append(postClassification).append(" BASIS");
                    }
                }
                getredploymentDetails.append(".");

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return getredploymentDetails.toString();
    }

    @Override
    public String getDetentionOnVacationLangDetails(HeadQuarterLeaving leavingForm, int notid, String notType) {
        StringBuffer getDetOnVacationDetails = new StringBuffer();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("select ordno,orddt from emp_notification where not_id=? and not_type=?");
            ps.setInt(1, notid);
            ps.setString(2, notType);
            rs = ps.executeQuery();
            if (rs.next()) {
                if (notType != null && notType.equals("DET_VAC")) {
                    getDetOnVacationDetails.append("DETAINED THE VACATION ");
                    if (leavingForm.getFdate() != null && !leavingForm.getFdate().equals("")) {
                        getDetOnVacationDetails.append(" FROM ").append((leavingForm.getFdate()));
                    } else if (leavingForm.getStrFdate() != null && !leavingForm.getStrFdate().equals("")) {
                        getDetOnVacationDetails.append(" FROM ").append((leavingForm.getStrFdate()).toUpperCase());
                    }
                    if (leavingForm.getEdate() != null && !leavingForm.getEdate().equals("")) {
                        getDetOnVacationDetails.append(" TO ").append(leavingForm.getEdate());
                    } else if (leavingForm.getStrEdate() != null && !leavingForm.getStrEdate().equals("")) {
                        getDetOnVacationDetails.append(" TO ").append((leavingForm.getStrEdate()).toUpperCase());
                    }
                    if (leavingForm.getAuthSpc() != null && !leavingForm.getAuthSpc().equals("")) {
                        getDetOnVacationDetails.append(" VIDE ").append(getSPN(leavingForm.getAuthSpc()));
                    }
                    if (rs.getString("ordno") != null && !rs.getString("ordno").equals("")) {
                        getDetOnVacationDetails.append(" NOTIFICATION/ OFFICE ORDER NO. ").append(rs.getString("ordno").toUpperCase());
                    }
                    if (rs.getString("orddt") != null && !rs.getString("orddt").equals("")) {
                        getDetOnVacationDetails.append(" DATED ").append(CommonFunctions.getFormattedOutputDate1(rs.getDate("orddt")).toUpperCase());
                    }
                    getDetOnVacationDetails.append(".");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return getDetOnVacationDetails.toString();
    }

    @Override
    public String getRepatriationLangDetails(RepatriationForm repatriationForm, int notid, String notType) {
        StringBuffer getRepatriationDetails = new StringBuffer();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("select ordno,orddt from emp_notification where not_id=? and not_type=?");
            ps.setInt(1, notid);
            ps.setString(2, notType);
            rs = ps.executeQuery();
            if (rs.next()) {
                getRepatriationDetails.append("ON REPATRIATION/ REVERSION REPORTED UNDER THE CADRE ");
                if (repatriationForm.getNotifyingPostName() != null && !repatriationForm.getNotifyingPostName().equals("")) {
                    getRepatriationDetails.append(" IN PURSUANCE OF ").append(getSPN(repatriationForm.getNotifyingSpc()));
                }
                if (rs.getString("ordno") != null && !rs.getString("ordno").equals("")) {
                    getRepatriationDetails.append(" VIDE NOTIFICATION/ OFFICE ORDER NO. ").append(rs.getString("ordno").toUpperCase());
                }
                if (rs.getDate("orddt") != null && !rs.getDate("orddt").equals("")) {
                    getRepatriationDetails.append(" DATED ").append(CommonFunctions.getFormattedOutputDate1(rs.getDate("orddt")).toUpperCase());
                }
                if (repatriationForm.getTxtBasic() != null && !repatriationForm.getTxtBasic().equals("")) {
                    getRepatriationDetails.append(" ALLOWED TO DRAW @ RS. ").append(repatriationForm.getTxtBasic()).append("/- PM");
                }
                if (repatriationForm.getSltPayScale() != null && !repatriationForm.getSltPayScale().equals("")) {
                    getRepatriationDetails.append(" IN THE SCALE OF PAY RS. ").append(repatriationForm.getSltPayScale()).append(" /-");
                }
                getRepatriationDetails.append(".");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);

        }
        return getRepatriationDetails.toString();
    }

    @Override
    public String getInitProceedingsDetails(ConclusionProceedings cps, String proceedingcause) {

        StringBuffer initString = new StringBuffer();
        String proNoun = null;
        String offName = null;
        String deptName = null;
        String authName = null;
        String notString = "";
        String othSpn = "";
        String curSpn = null;
        String tempPronoun = null;
        String tempPronoun1 = null;

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();
            //othSpn = getOtherSPN(cps.getTxtothDeptName1(), cps.getTxtothOffName1(), cps.getTxtothAuthName1());
            if (!othSpn.equals("")) {
                authName = othSpn.toUpperCase();
            } else {
                pst = con.prepareStatement("select getdeptname('" + cps.getNotdept() + "') deptName, getofficeen('" + cps.getNotoffice() + "') offName,  getspn('" + cps.getNotspc() + "') authName ");
                rs = pst.executeQuery();
                if (rs.next()) {
                    deptName = rs.getString("deptName");
                    offName = rs.getString("offName");
                    authName = rs.getString("authName");
                }
            }
            notString = getNotificationString(cps.getInitNotOrdNo(), cps.getInitNotOrdDt(), deptName, offName, authName);

            proNoun = getPronoun(cps.getEmpid());

            if (proNoun != null && !proNoun.equals("")) {
                if (proNoun.trim().equalsIgnoreCase("HE")) {
                    tempPronoun = " HIS ";
                    tempPronoun1 = " HIM ";
                } else if (proNoun.trim().equalsIgnoreCase("SHE")) {
                    tempPronoun = " HER ";
                    tempPronoun1 = " HER ";
                }
            } else {
                tempPronoun = " HIS/ HER ";
                tempPronoun1 = " HIM/ HER ";
            }

            initString.append(" DEPARTMENTAL PROCEEDING INITIATED ");
            if (notString != null && !notString.trim().equals("")) {
                initString.append(notString);
            }
            initString.append(" DURING ");
            if (tempPronoun != null) {
                initString.append(tempPronoun);
            }
            initString.append(" INCUMBENCY ");
            if (curSpn != null) {
                initString.append(" AS ").append(curSpn);
            }
            if (proceedingcause != null) {
                initString.append(" CAUSE: ").append(proceedingcause.toUpperCase());
            }
            initString.append(". ");

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);

        }
        return initString.toString().toUpperCase();
    }

    @Override
    public String getFinalResultProceedingsDetails(ConclusionProceedings cps, String proceedingcause) {
        StringBuffer conclusionString = new StringBuffer();
        String proNoun = null;
        String offName = null;
        String deptName = null;
        String authName = null;
        String notString = "";
        String othSpn = "";
        String curSpn = null;
        String tempPronoun = null;
        String tempPronoun1 = null;

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();
            //othSpn = getOtherSPN(cps.getTxtothDeptName1(), cps.getTxtothOffName1(), cps.getTxtothAuthName1());
            if (!othSpn.equals("")) {
                authName = othSpn.toUpperCase();
            } else {
                pst = con.prepareStatement("select getdeptname('" + cps.getNotdept() + "') deptName, getofficeen('" + cps.getNotoffice() + "') offName,  getspn('" + cps.getNotspc() + "') authName ");
                rs = pst.executeQuery();
                if (rs.next()) {
                    deptName = rs.getString("deptName");
                    offName = rs.getString("offName");
                    authName = rs.getString("authName");
                }
            }
            notString = getNotificationString(cps.getConclusionOrdNo(), cps.getConclusionOrdDt(), deptName, offName, authName);

            proNoun = getPronoun(cps.getEmpid());

            if (proNoun != null && !proNoun.equals("")) {
                if (proNoun.trim().equalsIgnoreCase("HE")) {
                    tempPronoun = " HIS ";
                    tempPronoun1 = " HIM ";
                } else if (proNoun.trim().equalsIgnoreCase("SHE")) {
                    tempPronoun = " HER ";
                    tempPronoun1 = " HER ";
                }
            } else {
                tempPronoun = " HIS/ HER ";
                tempPronoun1 = " HIM/ HER ";
            }

            if (cps.getFreefromcharges() != null && cps.getFreefromcharges().trim().equalsIgnoreCase("Y")) {
                conclusionString.append("FREE");
                conclusionString.append("THE PROCEEDING WAS CONCLUDED ");
                // if(freeNotString!=null)
                //{
                //   conclusionString.append(freeNotString);
                //}
                conclusionString.append(" WITH A DECISION TO ");
                if (tempPronoun1 != null) {
                    conclusionString.append(" MAKE ").append(tempPronoun1);
                }
                conclusionString.append(" EXONERATE FROM ALL CHARGES. ");

            }
            if (cps.getIfCensure() != null && cps.getIfCensure().trim().equalsIgnoreCase("Y")) {
                conclusionString.append("CENSURE");
                conclusionString.append(" THE PROCEEDING WAS CONCLUDED ");
                //if(freeNotString!=null && !freeNotString.equals(""))
                //{
                //  conclusionString.append(freeNotString);
                // }
                conclusionString.append(" WITH A WARNING TO BE CAUTIOUS IN FUTURE. ");
            }
            if (cps.getIfPunishment() != null && cps.getIfPunishment().trim().equalsIgnoreCase("Y")) {
                conclusionString.append("PUNISHMENT");
                conclusionString.append(" THE PROCEEDING WAS CONCLUDED ");
                //if(freeNotString!=null && !freeNotString.equals(""))
                //{
                //  conclusionString.append(freeNotString);
                // }
                conclusionString.append(" WITH THE PUNISHMENT. ");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);

        }
        return conclusionString.toString().toUpperCase();
    }

    @Override
    public String getRegularizationLangDetails(RegularizeServiceForm regularizeService, int notid) {
        StringBuffer regularizationDetails = new StringBuffer();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("select not_id,not_type,ordno,orddt,dept_code,off_code,auth,note,if_visible,auth from emp_notification where not_id=?");
            ps.setInt(1, notid);
            rs = ps.executeQuery();
            if (rs.next()) {
                regularizationDetails.append("CONDONED THE SERVICE PERIOD");
                if (regularizeService.getTxtFrmDt() != null && !regularizeService.getTxtFrmDt().equals("")) {
                    regularizationDetails.append(" FROM ").append(CommonFunctions.getFormattedOutputDate1(sdf.parse(regularizeService.getTxtFrmDt())));
                }
                if (regularizeService.getTxtToDt() != null && !regularizeService.getTxtToDt().equals("")) {
                    regularizationDetails.append(" TO ").append(CommonFunctions.getFormattedOutputDate1(sdf.parse(regularizeService.getTxtToDt())));
                }
                regularizationDetails.append(" VIDE ");
                if (rs.getString("auth") != null && !rs.getString("auth").equals("")) {
                    regularizationDetails.append(getSPN(rs.getString("auth")));
                }
                if (rs.getString("ordno") != null && !rs.getString("ordno").equals("")) {
                    regularizationDetails.append(" NOTIFICATION/ OFFICE ORDER NO. ").append(rs.getString("ordno").toUpperCase());
                }
                if (rs.getDate("orddt") != null && !rs.getDate("orddt").equals("")) {
                    regularizationDetails.append(" DATED ").append(CommonFunctions.getFormattedOutputDate1(rs.getDate("orddt")).toUpperCase());
                }
                regularizationDetails.append(".");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return regularizationDetails.toString().toUpperCase();
    }

    @Override
    public String getReleaseOfLoanLngDetails(LoanRelease loanrelease, int notid) {
        StringBuffer getReleaseOfLoanData = new StringBuffer();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement(" select repid,emp_loan_tran.not_id ,emp_loan_tran.not_type,emp_loan_tran.emp_id,g_loan.loan_tp ,loan_name,rep_type,tran_type,"
                    + "inst_no,amount,ordno,orddt,dept_code,off_code,auth,note  FROM \n"
                    + "emp_loan_tran\n"
                    + "inner join emp_notification on emp_loan_tran.not_id=emp_notification.not_id \n"
                    + "inner join g_loan on emp_loan_tran.loan_tp=g_loan.loan_tp\n"
                    + "where  emp_loan_tran.not_id=?");
            ps.setInt(1, notid);
            rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getString("tran_type") != null && rs.getString("tran_type").equals("RELEASE")) {
                    getReleaseOfLoanData.append("RELEASED ");
                    if (rs.getString("rep_type").equals("F")) {
                        getReleaseOfLoanData.append("FINAL ").append("INSTALLMENT ");
                    } else {
                        getReleaseOfLoanData.append(rs.getString("inst_no")).append(" INSTALLMENT ");
                    }
                    if (rs.getString("loan_name") != null && !rs.getString("loan_name").equals("")) {
                        getReleaseOfLoanData.append("OF ").append(rs.getString("loan_name"));
                    }
                    if (rs.getString("amount") != null && !rs.getString("amount").equals("")) {
                        getReleaseOfLoanData.append(" OF RS. ").append(rs.getString("amount")).append("/-");
                    }
                    if (rs.getString("auth") != null && !rs.getString("auth").equals("")) {
                        getReleaseOfLoanData.append(" AS REPORTED BY ").append(getSPN(rs.getString("auth")));
                    }
                    if (rs.getString("ordno") != null && !rs.getString("ordno").equals("")) {
                        getReleaseOfLoanData.append(" OFFICE ORDER/LETTER NO. ").append(rs.getString("ordno").toUpperCase());
                    }
                    if (rs.getString("orddt") != null && !rs.getString("orddt").equals("")) {
                        getReleaseOfLoanData.append(" DATED ").append(CommonFunctions.getFormattedOutputDate1(rs.getDate("orddt")));
                    }
                }
                getReleaseOfLoanData.append(".");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return getReleaseOfLoanData.toString();
    }

    @Override
    public String getRepaymentOfLoanLangDetails(LoanRepayment loanrepayment, int notid) {
        StringBuffer getRepaymentofLoanData = new StringBuffer();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("select repid,emp_loan_tran.not_id ,emp_loan_tran.not_type,emp_loan_tran.emp_id,g_loan.loan_tp ,loan_name,rep_type,tran_type,inst_no,amount,\n"
                    + "ordno,orddt,dept_code,off_code,auth,note  FROM \n"
                    + " emp_loan_tran\n"
                    + "inner join emp_notification on emp_loan_tran.not_id=emp_notification.not_id\n"
                    + "inner join g_loan on emp_loan_tran.loan_tp=g_loan.loan_tp\n"
                    + "where  emp_loan_tran.tran_type='REPAYMENT' and emp_loan_tran.not_id=?");
            ps.setInt(1, notid);
            rs = ps.executeQuery();
            if (rs.next()) {
                String postFix = null;
                if (loanrepayment.getTxtinstno().equals("1")) {
                    postFix = "ST";
                } else if (loanrepayment.getTxtinstno().equals("2")) {
                    postFix = "ND";
                } else if (loanrepayment.getTxtinstno().equals("3")) {
                    postFix = "RD";
                } else {
                    postFix = "TH";
                }

                if (rs.getString("rep_type") != null && rs.getString("rep_type").equals("I")) {
                    getRepaymentofLoanData.append("THE ").append(loanrepayment.getTxtinstno()).append(postFix).append(" INSTALLMENT ");
                    if (rs.getString("loan_name") != null && !rs.getString("loan_name").equals("")) {
                        getRepaymentofLoanData.append("OF ").append(rs.getString("loan_name"));
                    }
                    if (rs.getString("amount") != null && !rs.getString("amount").equals("")) {
                        getRepaymentofLoanData.append(" OF RS. ").append(rs.getString("amount")).append("/-");
                    }
                    if (rs.getString("auth") != null && !rs.getString("auth").equals("")) {
                        getRepaymentofLoanData.append(" HAS BEEN REPAID AS REPORTED BY ").append(getSPN(rs.getString("auth")));
                    }
                    if (rs.getString("ordno") != null && !rs.getString("ordno").equals("")) {
                        getRepaymentofLoanData.append(" OFFICE ORDER/LETTER NO. ").append(rs.getString("ordno").toUpperCase());
                    }
                    if (rs.getString("orddt") != null && !rs.getString("orddt").equals("")) {
                        getRepaymentofLoanData.append(" DATED ").append(CommonFunctions.getFormattedOutputDate1(rs.getDate("orddt")));
                    }

                } else if (rs.getString("rep_type") != null && rs.getString("rep_type").equals("F")) {
                    if (rs.getString("loan_name") != null && !rs.getString("loan_name").equals("")) {
                        getRepaymentofLoanData.append("THE ").append(rs.getString("loan_name"));
                    }
                    if (rs.getString("amount") != null && !rs.getString("amount").equals("")) {
                        getRepaymentofLoanData.append(" OF RS. ").append(rs.getString("amount")).append("/-");
                    }
                    getRepaymentofLoanData.append(" HAS BEEN ADJUSTED/FULLY PAID BACK WITH INTEREST AND NO AMOUNT IS OUTSTANDING AGAINST HIM/HER ON THAT ACCOUNT ");
                    if (rs.getString("auth") != null && !rs.getString("auth").equals("")) {
                        getRepaymentofLoanData.append("AS REPORTED BY ").append(getSPN(rs.getString("auth")));
                    }
                    if (rs.getString("ordno") != null && !rs.getString("ordno").equals("")) {
                        getRepaymentofLoanData.append(" OFFICE ORDER/LETTER NO. ").append(rs.getString("ordno").toUpperCase());
                    }
                    if (rs.getString("orddt") != null && !rs.getString("orddt").equals("")) {
                        getRepaymentofLoanData.append(" DATED ").append(CommonFunctions.getFormattedOutputDate1(rs.getDate("orddt")));
                    }
                }
                getRepaymentofLoanData.append(".");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return getRepaymentofLoanData.toString();
    }

    @Override
    public String getAbsorptionLangDetails(AbsorptionModel absorptionForm, int notid) {
        StringBuffer getAbsorptionData = new StringBuffer();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        String postClassification = null;
        String postStatus = null;
        try {
            con = this.dataSource.getConnection();

            getAbsorptionData.append("ON ABSORPTION ");
            if (absorptionForm.getSltGenericPost() != null && !absorptionForm.getSltGenericPost().equals("")) {
                getAbsorptionData.append("APPOINTED AS ").append(getPostName(absorptionForm.getSltGenericPost()));
            }
            if (absorptionForm.getSltCadre() != null && !absorptionForm.getSltCadre().equals("")) {
                getAbsorptionData.append(" AND JOINED CADRE ");
            }
            if (absorptionForm.getTxtBasic() != null && !absorptionForm.getTxtBasic().equals("")) {
                getAbsorptionData.append(" WITH PAY @ RS. ").append(absorptionForm.getTxtBasic()).append("/- PM");
            }
            if (absorptionForm.getSltPayScale() != null && !absorptionForm.getSltPayScale().equals("")) {
                getAbsorptionData.append(" IN THE SCALE OF PAY ").append(absorptionForm.getSltPayScale());
            }
            getAbsorptionData.append(" VIDE ");
            if (absorptionForm.getNotifyingSpc() != null && !absorptionForm.getNotifyingSpc().equals("")) {
                getAbsorptionData.append(absorptionForm.getNotifyingPostName());
            }
            if (absorptionForm.getOrdno() != null && !absorptionForm.getOrdno().equals("")) {
                getAbsorptionData.append(" NOTIFICATION/ OFFICE ORDER NO. ").append(absorptionForm.getOrdno().toUpperCase());
            }
            if (absorptionForm.getOrdDate() != null && !absorptionForm.getOrdDate().equals("")) {
                getAbsorptionData.append(" DATED ").append(absorptionForm.getOrdDate().toUpperCase());
            }
            if ((absorptionForm.getRdPostClassification() != null && !absorptionForm.getRdPostClassification().equals(""))
                    && (absorptionForm.getRdPostStatus() != null && !absorptionForm.getRdPostStatus().equals(""))) {
                if (absorptionForm.getRdPostClassification().equals("A")) {
                    postClassification = "ADHOC";
                } else if (absorptionForm.getRdPostClassification().equals("T")) {
                    postClassification = "TEMPORARY";
                } else if (absorptionForm.getRdPostClassification().equals("O")) {
                    postClassification = "ON PROBATION";
                } else if (absorptionForm.getRdPostClassification().equals("P")) {
                    postClassification = "PERMANENT";
                }
                if (absorptionForm.getRdPostStatus().equals("O")) {
                    postStatus = "Officiating";
                }
                if (absorptionForm.getRdPostStatus().equals("S")) {
                    postStatus = "Substantive";
                }
                if (absorptionForm.getRdPostStatus().equals("A")) {
                    postStatus = "None";
                }

                getAbsorptionData.append(" ON ").append(postClassification).append(" / " + postStatus.toUpperCase()).append(" BASIS");
            }
            getAbsorptionData.append(".");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);

        }
        return getAbsorptionData.toString();
    }

    @Override
    public String getAdditionalChargeLangDetails(AdditionalCharge additionChargeForm, int notid) {
        StringBuffer getADChargeDetails = new StringBuffer();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            con = this.dataSource.getConnection();
            getADChargeDetails.append("ALLOWED TO REMAIN IN CHARGE OF");
            if (additionChargeForm.getPostedspc() != null && !additionChargeForm.getPostedspc().equals("")) {
                getADChargeDetails.append(" THE POST OF ").append(getSPN(additionChargeForm.getPostedspc()));
            }
            getADChargeDetails.append(" IN ADDITION TO HIS OWN DUTY VIDE ");
            if (additionChargeForm.getAuthSpc() != null && !additionChargeForm.getAuthSpc().equals("")) {
                getADChargeDetails.append(getSPN(additionChargeForm.getAuthSpc()));
            }
            if (additionChargeForm.getTxtNotOrdNo() != null && !additionChargeForm.getTxtNotOrdNo().equals("")) {
                getADChargeDetails.append(" NOTIFICATION/ OFFICE ORDER NO. ").append(additionChargeForm.getTxtNotOrdNo().toUpperCase());
            }
            if (additionChargeForm.getTxtNotOrdDt() != null && !additionChargeForm.getTxtNotOrdDt().equals("")) {
                getADChargeDetails.append(" DATED ").append(additionChargeForm.getTxtNotOrdDt().toUpperCase());
            }
            getADChargeDetails.append(".");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return getADChargeDetails.toString();
    }

    @Override
    public String getQtrAllotLangDetails(EmpQuarterBean eqBean, String qaID, String empid, String notType) {
        StringBuffer getQtrAllotDetails = new StringBuffer();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String orddt1 = null;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("SELECT EN.if_visible,EN.NOT_TYPE,EN.auth,EA.qa_id, EA.quarter_no, allotment_date, possession_date\n"
                    + ", quarter_rent, address, water_rent, q_id, is_get_hra, if_surrendered, sewerage_rent, EN.ordno, EN.orddt FROM emp_qtr_allot EA\n"
                    + " INNER JOIN emp_notification EN ON EA.not_id = EN.not_id\n"
                    + "WHERE EA.qa_id = ? and EN.emp_id=? ");
            ps.setInt(1, Integer.parseInt(qaID));
            ps.setString(2, empid);
            rs = ps.executeQuery();
            if (rs.next()) {
                orddt1 = CommonFunctions.getFormattedOutputDate1(rs.getDate("orddt"));
                if (notType != null && notType.equals("QTR_ALLOT")) {

                    if (rs.getDate("allotment_date") != null && !rs.getDate("allotment_date").equals("")) {
                        getQtrAllotDetails.append("QUARTER ALLOTTED NO. ").append(rs.getString("address")).append(" ON ").append(CommonFunctions.getFormattedOutputDate1(rs.getDate("allotment_date")).toUpperCase());
                    }
                    if (rs.getDate("possession_date") != null && !rs.getDate("possession_date").equals("")) {
                        getQtrAllotDetails.append(" AND POSSESSED ON ").append(CommonFunctions.getFormattedOutputDate1(rs.getDate("possession_date")).toUpperCase());
                    }
                    if (rs.getString("quarter_rent") != null && !rs.getString("quarter_rent").equals("")) {
                        getQtrAllotDetails.append(" WITH MONTHLY RENT ").append(rs.getString("quarter_rent"));
                    }
                    if (rs.getString("sewerage_rent") != null && !rs.getString("sewerage_rent").equals("")) {
                        getQtrAllotDetails.append(" AND WATER RENT ").append(rs.getString("sewerage_rent"));
                    }
                    if (rs.getString("ordno") != null && !rs.getString("ordno").equals("")) {
                        getQtrAllotDetails.append(" VIDE NOTIFICATION/ OFFICE ORDER NO.").append(rs.getString("ordno").toUpperCase()).append(" DATED ").append(orddt1.toUpperCase());
                    }
                }
            }
            getQtrAllotDetails.append(".");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return getQtrAllotDetails.toString();
    }

    @Override
    public String getCancelLangDetails(NotificationBean nb, int notid, String othspc) {

        StringBuffer cancelDetails = new StringBuffer();

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String notType = null;
        String notificationName = null;
        String authName = null;
        String offName = null;
        String deptName = null;
        String othSpn = null;
        try {
            con = this.dataSource.getConnection();

            if (othspc != null && !othspc.equals("")) {
                othSpn = getOtherSpn(othspc);
                authName = othSpn;
            } else {
                if (nb.getSancAuthCode() != null && !nb.getSancAuthCode().equals("")) {
                    authName = getSPN(nb.getSancAuthCode());
                } else if (nb.getSancOffCode() != null && !nb.getSancOffCode().equals("")) {
                    offName = getOffName(nb.getSancOffCode());
                } else if (nb.getSancDeptCode() != null && !nb.getSancDeptCode().equals("")) {
                    deptName = getDeptName(nb.getSancDeptCode());
                }
            }

            notType = nb.getNottype();
            if (notType != null) {
                if (notType.equals("FIRST_APPOINTMENT")) {
                    notificationName = " REGULAR RECRUITMENT ";
                } else if (notType.equals("BRASS_ALLOT")) {
                    notificationName = " BRASS ALLOTMENT ";
                } else if (notType.equals("ALLOT_CADRE")) {
                    notificationName = " ALLOTMENT TO CADRE ";
                } else if (notType.equals("ADDITIONAL_CHARGE")) {
                    notificationName = "ADDITIONAL CHARGE ";
                } else if (notType.equals("EQ_STAT")) {
                    notificationName = "EQUIVALENT POST ";
                } else if (notType.equals("PAY_ENTITLEMENT")) {
                    notificationName = "PAY ENTITLEMENT";
                } else if (notType.equals("PAYREVISION")) {
                    notificationName = " PAY REVISION ";
                } else if (notType.equals("PAYFIXATION")) {
                    notificationName = " PAY FIXATION ";
                } else if (notType.equals("DEPUTATION_AG")) {
                    notificationName = " DEPUTATION AG ";
                } else if (notType.equals("STEPUP")) {
                    notificationName = " STEP UP ";
                } else if (notType.equals("RELIEVE_CADRE")) {
                    notificationName = " RELIEVE FROM CADRE ";
                } else if (notType.equals("JOIN_CADRE")) {
                    notificationName = " JOINING TO CADRE ";
                } else if (notType.equals("SERVICE_DISPOSAL")) {
                    notificationName = " SERVICE ";
                } else if (notType.equals("LEAVE")) {
                    pst = con.prepareStatement("SELECT LSOT FROM "
                            + " (SELECT LSOT_ID FROM (SELECT * FROM EMP_NOTIFICATION WHERE NOT_ID=?) "
                            + " EMP_NOTIFICATION "
                            + " LEFT OUTER JOIN "
                            + " EMP_LEAVE "
                            + " ON EMP_NOTIFICATION.NOT_ID = EMP_LEAVE.NOT_ID) EMPLEAVENOTIFICATION "
                            + " LEFT OUTER JOIN "
                            + " G_LSOT "
                            + " ON EMPLEAVENOTIFICATION.LSOT_ID = G_LSOT.LSOT_ID");
                    pst.setInt(1, notid);
                    rs = pst.executeQuery();
                    if (rs.next()) {
                        notificationName = rs.getString("LSOT");
                    }
                } else {
                    notificationName = notType;
                }
            }
            if (authName != null) {
                cancelDetails.append(" IN PURSUANCE OF ").append(authName);
            } else if (offName != null) {
                cancelDetails.append(" IN PURSUANCE OF ").append(offName);
            } else if (deptName != null) {
                cancelDetails.append(" IN PURSUANCE OF " + deptName + " DEPARTMENT");
            }
            if (authName != null || offName != null || deptName != null) {
                if (nb.getOrdno() != null) {
                    cancelDetails.append(" NOTIFICATION/ OFFICE ORDER NO. ").append(nb.getOrdno());
                    if (nb.getOrdDate() != null) {
                        cancelDetails.append(" DATED ").append(nb.getOrdDate());
                    }
                } else {
                    cancelDetails.append(" ORDER ");
                }
            } else {
                if (nb.getOrdno() != null) {
                    cancelDetails.append(" IN PURSUANCE OF NOTIFICATION/ OFFICE ORDER NO. ").append(nb.getOrdno());
                    if (nb.getOrdDate() != null) {
                        cancelDetails.append(" DATED ").append(nb.getOrdDate());
                    }
                }
            }
            if (notificationName != null) {
                cancelDetails.append(" THE ").append(notificationName).append(" IS CANCELLED. ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return cancelDetails.toString().toUpperCase();
    }

    @Override
    public String getSupersedeLangDetails(int notid, String othspc) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String linkId = null;
        String linkNotNo = null;
        String linkNotDate = null;
        String linkNotdept = null;
        String linkNotOff = null;
        String linkNotAuth = null;
        String superNote = null;

        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT * FROM EMP_NOTIFICATION WHERE ISCANCELED='Y' AND not_id=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, notid);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getString("ORDNO") != null && !rs.getString("ORDNO").equals("")) {
                    linkNotNo = rs.getString("ORDNO");
                }
                if (rs.getDate("ORDDT") != null) {
                    linkNotDate = CommonFunctions.getFormattedOutputDate1(rs.getDate("ORDDT"));
                }
                if (rs.getString("NOT_ID") != null && !rs.getString("NOT_ID").equals("")) {
                    linkId = rs.getString("NOT_ID");
                }
                if (othspc != null && !othspc.equals("")) {
                    linkNotAuth = getOtherSpn(othspc);
                } else {
                    if (rs.getString("AUTH") != null && !rs.getString("AUTH").equals("")) {
                        linkNotAuth = rs.getString("AUTH");
                    }
                    if (rs.getString("DEPT_CODE") != null && !rs.getString("DEPT_CODE").equals("")) {
                        linkNotdept = rs.getString("DEPT_CODE");
                    }
                    if (rs.getString("OFF_CODE") != null && !rs.getString("OFF_CODE").equals("")) {
                        linkNotOff = rs.getString("OFF_CODE");
                    }
                }
            }
            System.out.println("linkId is: " + linkId);
            if (linkId != null) {
                superNote = "IN SUPERSESSION ";

                if (linkNotAuth != null && !linkNotAuth.trim().equals("")) {
                    superNote = superNote + " OF " + getSPN(linkNotAuth);
                    if (linkNotNo != null && !linkNotNo.trim().equals("")) {
                        superNote = superNote + " ORDER NO. " + linkNotNo.toUpperCase();
                        if (linkNotDate != null && !linkNotDate.trim().equals("")) {
                            superNote = superNote + " DATED " + linkNotDate.toUpperCase();
                        }
                    } else {
                        superNote = superNote + " ORDER ";
                    }
                } else if (linkNotOff != null && !linkNotOff.trim().equals("")) {
                    superNote = superNote + " OF " + getOffName(linkNotOff);
                    if (linkNotNo != null && !linkNotNo.trim().equals("")) {
                        superNote = superNote + " ORDER NO. " + linkNotNo.toUpperCase();
                        if (linkNotDate != null && !linkNotDate.trim().equals("")) {
                            superNote = superNote + " DATED " + linkNotDate.toUpperCase();
                        }
                    } else {
                        superNote = superNote + " ORDER ";
                    }
                } else if (linkNotdept != null && !linkNotdept.trim().equals("")) {
                    superNote = superNote + " OF " + getDeptName(linkNotdept) + " DEPARTMENT ";
                    if (linkNotNo != null && !linkNotNo.trim().equals("")) {
                        superNote = superNote + " ORDER NO." + linkNotNo.toUpperCase();
                        if (linkNotDate != null && !linkNotDate.trim().equals("")) {
                            superNote = superNote + " DATED " + linkNotDate.toUpperCase();
                        }
                    } else {
                        superNote = superNote + " ORDER ";
                    }
                } else if (linkNotNo != null && !linkNotNo.trim().equals("")) {
                    superNote = superNote + " OF ORDER NO. " + linkNotNo.toUpperCase();
                    if (linkNotDate != null && !linkNotDate.trim().equals("")) {
                        superNote = superNote + " DATED " + linkNotDate.toUpperCase();
                    }
                }
            }

            if (superNote != null && !superNote.trim().equals("")) {
                superNote = superNote.trim() + ", ";
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return superNote;
    }

    @Override
    public String getLongTermTrainingDetails(TrainingSchedule training, int notid) {
        StringBuffer lttrainingDetails = new StringBuffer();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("select empnt.emp_id,empnt.not_id,ordno,orddt,empnt.dept_code notdept,empnt.off_code notofc,auth notspc,g_spc.spn notpostname,note,fdate,tdate,days,\n"
                    + "training_type_id,training_type,coordinator,if_underwent,place,organization_type_posting,\n"
                    + "if_visible,et.next_spc postedspc,g1.spn postedspn,et.off_code postedofc,et.dept_code posteddept from\n"
                    + "(select * from emp_notification where not_id=? and not_type='LT_TRAINING')empnt\n"
                    + "inner join emp_lt_training emplt\n"
                    + "on empnt.not_id=emplt.not_id\n"
                    + "inner join G_TRAINING_TYPE trngType on\n"
                    + "trngType.TRAINING_TYPE_ID=emplt.training_name\n"
                    + "left outer join g_spc on g_spc.spc=empnt.auth\n"
                    + "left outer join g_office on g_office.off_code=empnt.off_code\n"
                    + "left outer join (select * from emp_transfer where not_type='LT_TRAINING')et\n"
                    + "on et.not_id=empnt.not_id\n"
                    + "left outer join g_spc g1 on g1.spc=et.next_spc");
            ps.setInt(1, notid);
            rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getString("if_underwent") != null && rs.getString("if_underwent").equals("A")) {
                    lttrainingDetails.append(" ALLOWED TO ATTEND ");
                } else if (rs.getString("if_underwent") != null && rs.getString("if_underwent").equals("U")) {
                    lttrainingDetails.append(" UNDERWENT ");
                }
                if (rs.getString("training_type") != null && !rs.getString("training_type").equals("")) {
                    lttrainingDetails.append(rs.getString("training_type"));
                }

                if (rs.getString("fdate") != null && !rs.getString("fdate").equals("")) {
                    lttrainingDetails.append(" FROM ").append(CommonFunctions.getFormattedOutputDate1(rs.getDate("fdate")));
                }
                if (rs.getString("tdate") != null && !rs.getString("tdate").equals("")) {
                    lttrainingDetails.append(" TO ").append(CommonFunctions.getFormattedOutputDate1(rs.getDate("tdate")));
                }
                if (rs.getString("postedofc") != null && !rs.getString("postedofc").equals("")) {
                    lttrainingDetails.append(" AT ").append(getOffName(rs.getString("postedofc")));
                }
                lttrainingDetails.append(" VIDE ");
                if (rs.getString("notofc") != null && !rs.getString("notofc").equals("")) {
                    lttrainingDetails.append(getOffName(rs.getString("notofc")));
                }
                if (rs.getString("ordno") != null && !rs.getString("ordno").equals("")) {
                    lttrainingDetails.append(" NOTIFICATION/ OFFICE ORDER NO. ").append(rs.getString("ordno").toUpperCase());
                }
                if (rs.getDate("orddt") != null && !rs.getDate("orddt").equals("")) {
                    lttrainingDetails.append(" DATED ").append(CommonFunctions.getFormattedOutputDate1(rs.getDate("orddt")).toUpperCase());
                }
                lttrainingDetails.append(".");

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return lttrainingDetails.toString().toUpperCase();
    }

    @Override
    public String getRegularisationServiceContractual6Yrs(String empid) {
        StringBuffer getCont6YrToRegularDetails = new StringBuffer();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        String gender = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("select emp_regularization_contractual.*,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,\n"
                    + "cur_salary,matrix_level,matrix_cell,substr(spc,1,13) off_code from\n"
                    + "emp_regularization_contractual \n"
                    + "inner join emp_mast on emp_regularization_contractual.emp_id=emp_mast.emp_id\n"
                    + "where emp_regularization_contractual.emp_id=?");
            ps.setString(1, empid);
            rs = ps.executeQuery();
            if (rs.next()) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(sdf.parse(CommonFunctions.getFormattedOutputDate1(rs.getDate("tdate"))));
                cal.add(Calendar.DAY_OF_MONTH, 1);
                String wefDate = sdf.format(cal.getTime());

                getCont6YrToRegularDetails.append(" The Service of ").append(rs.getString("EMP_NAME"));
                if (rs.getString("gpc") != null && !rs.getString("gpc").equals("")) {
                    getCont6YrToRegularDetails.append(" ").append(getPostName(rs.getString("gpc"))).append(" (Contractual 6Yrs/Initial Appointees)");
                    getCont6YrToRegularDetails.append(" has been regularized as regular ").append(getPostName(rs.getString("gpc")));
                }
                if (rs.getDate("tdate") != null && !rs.getDate("tdate").equals("")) {
                    getCont6YrToRegularDetails.append(" with effect from ").append(wefDate);
                }
                if (rs.getDate("fdate") != null && !rs.getDate("fdate").equals("")) {
                    getCont6YrToRegularDetails.append(" on completion ");

                    // her 6 Yr. contractual service period from ").append(CommonFunctions.getFormattedOutputDate1(rs.getDate("fdate")));
                    if (rs.getString("gender").equals("M")) {
                        gender = " HIS ";
                    } else if (rs.getString("gender").equals("F")) {
                        gender = " HER ";
                    }
                    getCont6YrToRegularDetails.append("of").append(gender).append("period from ").append(CommonFunctions.getFormattedOutputDate1(rs.getDate("fdate")));
                }
                if (rs.getDate("tdate") != null && !rs.getDate("tdate").equals("")) {
                    getCont6YrToRegularDetails.append(" to ").append(CommonFunctions.getFormattedOutputDate1(rs.getDate("tdate")));
                }
                getCont6YrToRegularDetails.append(" against the regular vacant post in this establishment in Group ").append(rs.getString("post_group")).append(" Category ");
                if (rs.getString("pay_commission") != null && rs.getString("pay_commission").equals("7")) {
                    if (rs.getInt("matrix_level") != 0 || rs.getInt("matrix_cell") != 0) {
                        getCont6YrToRegularDetails.append(" in ").append("level-").append(rs.getString("matrix_level")).append(" and ").append("cell-").append(rs.getString("matrix_cell"));
                    }
                    getCont6YrToRegularDetails.append(" as per pay matrix order rule of ORSP-2017");
                } else if (rs.getString("pay_commission") != null && rs.getString("pay_commission").equals("6")) {
                    getCont6YrToRegularDetails.append(" in the scale of pay of Rs. ").append(rs.getString("cur_salary"));
                    if (rs.getInt("gp") != 0) {
                        getCont6YrToRegularDetails.append(" with grade pay ").append(rs.getInt("gp"));
                    }
                }
                if (rs.getString("ord_no") != null && !rs.getString("ord_no").equals("")) {
                    getCont6YrToRegularDetails.append(" vide notification/ office order no. ").append(rs.getString("ord_no"));
                }
                if (rs.getDate("ord_date") != null && !rs.getDate("ord_date").equals("")) {
                    getCont6YrToRegularDetails.append(" dated ").append(CommonFunctions.getFormattedOutputDate1(rs.getDate("ord_date")));
                }
                if (rs.getString("off_code") != null && !rs.getString("off_code").equals("")) {
                    getCont6YrToRegularDetails.append(" of ").append(getOffName(rs.getString("off_code")));
                }
                getCont6YrToRegularDetails.append(".");

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return getCont6YrToRegularDetails.toString().toUpperCase();
    }

    @Override
    public String getEnrollmentSbDetails(Enrollmenttoinsurance enrollmentForm, String notType, int notId) {

        StringBuffer outputEnrollmentDetails = new StringBuffer();
        String authName = null;
        String offName = null;
        String deptName = null;
        String superNote = null;
        String notString = "";
        String othSpn = "";
        Connection con = null;

        try {
            con = dataSource.getConnection();

            NotificationBean notBean = getNotificationDetails(notId, notType);

            if (enrollmentForm.getHidNotiSpc() != null && !enrollmentForm.getHidNotiSpc().equals("")) {
                authName = getSPN(enrollmentForm.getHidNotiSpc());
            } else if (enrollmentForm.getHidNotifyingOffCode() != null && !enrollmentForm.getHidNotifyingOffCode().equals("")) {
                offName = getOffName(enrollmentForm.getHidNotifyingOffCode());
            } else if (enrollmentForm.getHidNotifyingDeptCode() != null && !enrollmentForm.getHidNotifyingDeptCode().equals("")) {

                deptName = getDeptName(enrollmentForm.getHidNotifyingDeptCode());
            }

            notString = getNotificationString(enrollmentForm.getTxtNotOrdNo(), enrollmentForm.getTxtNotOrdDt(), deptName, offName, authName);

            if (notId > 0) {
                if (enrollmentForm.getSchemename() != null && !enrollmentForm.getSchemename().trim().equals("")) {
                    outputEnrollmentDetails.append(" AS A MEMBER OF ").append(enrollmentForm.getSchemetype());
                }
                if (enrollmentForm.getWitheffectdate() != null && !enrollmentForm.getWitheffectdate().trim().equals("")) {
                    outputEnrollmentDetails.append(" WEF ").append(enrollmentForm.getWitheffectdate().toUpperCase());
                }
                if (enrollmentForm.getSubamount() != null && !enrollmentForm.getSubamount().trim().equals("") && !enrollmentForm.getSubamount().trim().equals("0")) {
                    outputEnrollmentDetails.append(" WITH SUBSCRIPTION AMOUNT RS. ").append(CommonFunctions.formatNumber(enrollmentForm.getSubamount())).append("/- ");
                }
                if (enrollmentForm.getInsaccountno() != null && !enrollmentForm.getInsaccountno().trim().equals("")) {
                    outputEnrollmentDetails.append(" HAVING ACCOUNT NUMBER ").append(enrollmentForm.getInsaccountno());
                }
                if (enrollmentForm.getNotifyingSpc() != null && !enrollmentForm.getNotifyingSpc().equals("")) {
                    if (enrollmentForm.getHidNotifyingOthSpc() != null && !enrollmentForm.getHidNotifyingOthSpc().equals("")) {
                        outputEnrollmentDetails.append(" VIDE ").append(getOtherSpn(enrollmentForm.getHidNotifyingOthSpc()));
                    } else {
                        outputEnrollmentDetails.append(" VIDE ").append(getDeptName(enrollmentForm.getHidNotifyingDeptCode())).append(" DEPARTMENT ");
                    }
                    outputEnrollmentDetails.append(" NOTIFICATION/ OFFICE ORDER NO - ").append(enrollmentForm.getTxtNotOrdNo());
                    outputEnrollmentDetails.append(" DATED ").append(enrollmentForm.getTxtNotOrdDt().toUpperCase());
                }
            }
            outputEnrollmentDetails.append(".");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return outputEnrollmentDetails.toString();
    }

    @Override
    public String getEquivalentSbDetails(EquivalentPost equivalpostForm, String notType, int notId) {

        StringBuffer outputEquivalentDetails = new StringBuffer();
        String authName = null;
        String offName = null;
        String deptName = null;
        String superNote = null;
        String notString = "";
        String othSpn = "";
        Connection con = null;

        try {
            con = dataSource.getConnection();

            NotificationBean notBean = getNotificationDetails(notId, notType);

            if (equivalpostForm.getHidNotiSpc() != null && !equivalpostForm.getHidNotiSpc().equals("")) {
                authName = getSPN(equivalpostForm.getHidNotiSpc());
            } else if (equivalpostForm.getHidNotifyingOffCode() != null && !equivalpostForm.getHidNotifyingOffCode().equals("")) {
                offName = getOffName(equivalpostForm.getHidNotifyingOffCode());
            } else if (equivalpostForm.getHidNotifyingDeptCode() != null && !equivalpostForm.getHidNotifyingDeptCode().equals("")) {

                deptName = getDeptName(equivalpostForm.getHidNotifyingDeptCode());
            }

            notString = getNotificationString(equivalpostForm.getTxtNotOrdNo(), equivalpostForm.getTxtNotOrdDt(), deptName, offName, authName);

            if (notId > 0) {

                if (equivalpostForm.getRdTransaction() != null && equivalpostForm.getRdTransaction().equals("C")) {
                    if (equivalpostForm.getHidcurpostcode() != null && !equivalpostForm.getHidcurpostcode().equals("")) {
                        outputEquivalentDetails.append(" THE POST OF  ").append((equivalpostForm.getHidcurpostname()));
                    }
                    if (equivalpostForm.getHidcuroffcode() != null && !equivalpostForm.getHidcuroffcode().equals("")) {
                        outputEquivalentDetails.append(" , ").append(getOffName(equivalpostForm.getHidcuroffcode()));
                    }
                    if (equivalpostForm.getHidcurdeptcode() != null && !equivalpostForm.getHidcurdeptcode().equals("")) {
                        outputEquivalentDetails.append(" , ").append(getDeptName(equivalpostForm.getHidcurdeptcode()));
                    }
                } else {
                    if (equivalpostForm.getPostedSpc() != null && !equivalpostForm.getPostedSpc().equals("")) {
                        outputEquivalentDetails.append(" THE POST OF  ").append((equivalpostForm.getPostedSpc()));
                    }
                    if (equivalpostForm.getHidPostedOffCode() != null && !equivalpostForm.getHidPostedOffCode().equals("")) {
                        outputEquivalentDetails.append(" , ").append(getOffName(equivalpostForm.getHidPostedOffCode()));
                    }
                    if (equivalpostForm.getHidPostedDeptCode() != null && !equivalpostForm.getHidPostedDeptCode().equals("")) {
                        outputEquivalentDetails.append(" , ").append(getDeptName(equivalpostForm.getHidPostedDeptCode()));
                    }
                }

                if (equivalpostForm.getHidequvalPostedSpc() != null && !equivalpostForm.getHidequvalPostedSpc().equals("")) {
                    outputEquivalentDetails.append(" IS DECLARED EQUIVALENT IN STATUS AND RESPONSIBILITY TO THE POST ").append(getPostName(equivalpostForm.getHidequvalPostedSpc()));
                }
                if (equivalpostForm.getHidequvalPostedDeptCode() != null && !equivalpostForm.getHidequvalPostedDeptCode().equals("")) {
                    outputEquivalentDetails.append(" , ").append(getDeptName(equivalpostForm.getHidequvalPostedDeptCode())).append(" DEPARTMENT");
                }
                if (equivalpostForm.getNotifyingSpc() != null && !equivalpostForm.getNotifyingSpc().equals("")) {
                    if (equivalpostForm.getHidNotifyingOthSpc() != null && !equivalpostForm.getHidNotifyingOthSpc().equals("")) {
                        outputEquivalentDetails.append(" VIDE ").append(getOtherSpn(equivalpostForm.getHidNotifyingOthSpc()));
                    } else {
                        outputEquivalentDetails.append(" VIDE ").append(getDeptName(equivalpostForm.getHidNotifyingDeptCode())).append(" DEPARTMENT ");
                    }
                    outputEquivalentDetails.append(" NOTIFICATION/ OFFICE ORDER NO - ").append(equivalpostForm.getTxtNotOrdNo());
                    outputEquivalentDetails.append(" DATED ").append(equivalpostForm.getTxtNotOrdDt().toUpperCase());
                }
            }
            outputEquivalentDetails.append(".");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return outputEquivalentDetails.toString();
    }

    /* @Override
     public String getServiceRecordDetails(ServiceRecordForm serviceRecordForm) {

     StringBuffer outputServiceRecord = new StringBuffer();
     String details = null;
     String fromDate = null;
     String toDate = null;
     String fromTime = null;
     String toTime = null;
     String preFrom = null;
     String preTo = null;
     String suffFrom = null;
     String suffTo = null;
     String lsot = null;
     String syear = null;
     String tyear = null;
     String sdays = null;
        
     String notString = "";
     String othSpn = "";
     Connection con = null;

     try {
     con = dataSource.getConnection();
     if(serviceRecordForm.getSrid()!= null && !serviceRecordForm.getSrid().equals("")){
     if (serviceRecordForm.getRdModule() != null && serviceRecordForm.getRdModule().equalsIgnoreCase("J")) {
     details = serviceRecordForm.getTxtJoiningDetails();
     if (serviceRecordForm.getTxtJoiningFromDate() != null && !serviceRecordForm.getTxtJoiningFromDate().trim().equals("")) {
     fromDate = serviceRecordForm.getTxtJoiningFromDate();
     }
     if (serviceRecordForm.getTxtJoiningToDate() != null && !serviceRecordForm.getTxtJoiningToDate().trim().equals("")) {
     toDate = serviceRecordForm.getTxtJoiningToDate();
     }
     if (serviceRecordForm.getSltJoiningFromTime() != null && !serviceRecordForm.getSltJoiningFromTime().equals("")) {
     fromTime = serviceRecordForm.getSltJoiningFromTime();
     }
     if (serviceRecordForm.getSltJoiningToTime() != null && !serviceRecordForm.getSltJoiningToTime().equals("")) {
     toTime = serviceRecordForm.getSltJoiningToTime();
     }
     } 
     outputServiceRecord.append(details).append(fromDate).append(toDate).append(fromTime).append(toTime);
     }if (serviceRecordForm.getRdModule() != null && serviceRecordForm.getRdModule().equalsIgnoreCase("S")) {
     details = serviceRecordForm.getTxtServiceRecordDetails();
     if (serviceRecordForm.getTxtServiceRecordFromDate() != null && !serviceRecordForm.getTxtServiceRecordFromDate().trim().equals("")) {
     fromDate = serviceRecordForm.getTxtServiceRecordFromDate();
     }
     if (serviceRecordForm.getTxtServiceRecordToDate() != null && !serviceRecordForm.getTxtServiceRecordToDate().trim().equals("")) {
     toDate = serviceRecordForm.getTxtServiceRecordToDate();
     }
     if (serviceRecordForm.getSltServiceRecordFromTime() != null && !serviceRecordForm.getSltServiceRecordFromTime().equals("")) {
     fromTime = serviceRecordForm.getSltServiceRecordFromTime();
     }
     if (serviceRecordForm.getSltServiceRecordToTime() != null && !serviceRecordForm.getSltServiceRecordToTime().equals("")) {
     toTime = serviceRecordForm.getSltServiceRecordToTime();
     }
     outputServiceRecord.append(details).append(fromDate).append(toDate).append(fromTime).append(toTime);
     }
     if (serviceRecordForm.getRdModule() != null && serviceRecordForm.getRdModule().equalsIgnoreCase("L")) {
     details = serviceRecordForm.getSltLeaveType();
     preFrom = serviceRecordForm.getTxtLeaveSanctionPrefixFrom();
     preTo = serviceRecordForm.getTxtLeaveSanctionPrefixTo();
     suffFrom = serviceRecordForm.getTxtLeaveSanctionSuffixFrom();
     suffTo = serviceRecordForm.getTxtLeaveSanctionSuffixTo();
     lsot = "01";
     if (serviceRecordForm.getTxtLeaveSanctionFromDate() != null && !serviceRecordForm.getTxtLeaveSanctionFromDate().trim().equals("")) {
     fromDate = serviceRecordForm.getTxtLeaveSanctionFromDate();
     }
     if (serviceRecordForm.getTxtLeaveSanctionToDate() != null && !serviceRecordForm.getTxtLeaveSanctionToDate().trim().equals("")) {
     toDate = serviceRecordForm.getTxtLeaveSanctionToDate();
     }
     if (serviceRecordForm.getSltLeaveSanctionFromTime() != null && !serviceRecordForm.getSltLeaveSanctionFromTime().equals("")) {
     fromTime = serviceRecordForm.getSltLeaveSanctionFromTime();
     }
     if (serviceRecordForm.getSltLeaveSanctionToTime() != null && !serviceRecordForm.getSltLeaveSanctionToTime().equals("")) {
     toTime = serviceRecordForm.getSltLeaveSanctionToTime();
     }
     outputServiceRecord.append(details).append(fromDate).append(fromTime).append(toDate).append(toTime).append(preFrom).append(preFrom).append(suffFrom).append(suffTo);
     } 
     if (serviceRecordForm.getRdModule() != null && serviceRecordForm.getRdModule().equalsIgnoreCase("LS")) {
     details = "EL";
     syear = serviceRecordForm.getTxtLeaveSurrenderFromYear();
     tyear = serviceRecordForm.getTxtLeaveSurrenderToYear();
     sdays = serviceRecordForm.getTxtLeaveSurrenderNoDays();
     lsot = "02";
     if (serviceRecordForm.getTxtLeaveSurrenderFromDate() != null && !serviceRecordForm.getTxtLeaveSurrenderFromDate().trim().equals("")) {
     fromDate = serviceRecordForm.getTxtLeaveSurrenderFromDate();
     }
     if (serviceRecordForm.getTxtLeaveSurrenderToDate() != null && !serviceRecordForm.getTxtLeaveSurrenderToDate().trim().equals("")) {
     toDate = serviceRecordForm.getTxtLeaveSurrenderToDate();
     }
     outputServiceRecord.append(details).append(fromDate).append(toDate).append(syear).append(tyear).append(sdays);
     }
     if (serviceRecordForm.getRdModule() != null && serviceRecordForm.getRdModule().equalsIgnoreCase("UJTL")) {
     details = "EL";
     lsot = "06";
     if (serviceRecordForm.getTxtUnavailedJoiningFromDate() != null && !serviceRecordForm.getTxtUnavailedJoiningFromDate().trim().equals("")) {
     fromDate = serviceRecordForm.getTxtUnavailedJoiningFromDate();
     }
     if (serviceRecordForm.getTxtUnavailedJoiningToDate() != null && !serviceRecordForm.getTxtUnavailedJoiningToDate().trim().equals("")) {
     toDate = serviceRecordForm.getTxtUnavailedJoiningToDate();
     }
     if (serviceRecordForm.getSltUnavailedJoiningFromTime() != null && !serviceRecordForm.getSltUnavailedJoiningFromTime().equals("")) {
     fromTime = serviceRecordForm.getSltUnavailedJoiningFromTime();
     }
     if (serviceRecordForm.getSltUnavailedJoiningToTime() != null && !serviceRecordForm.getSltUnavailedJoiningToTime().equals("")) {
     toTime = serviceRecordForm.getSltUnavailedJoiningToTime();
     }
     outputServiceRecord.append(details).append(fromDate).append(fromTime).append(toDate).append(toTime);
     }

     // notString = getNotificationString( details, fromDate, toDate,fromTime,toTime);

       
     outputServiceRecord.append(".");

     } catch (Exception e) {
     e.printStackTrace();
     } finally {
     DataBaseFunctions.closeSqlObjects(con);
     }
     return outputServiceRecord.toString();
     } */
    @Override
    public String getPayFixationDetailsSBCorrection(PayFixation prf, int notid, String payType) {

        StringBuffer payRev = new StringBuffer();
        ResultSet rsInside = null;
        ResultSet rs2 = null;
        Statement st = null;
        double rBasic = 0;
        String rScale = null;
        String wefd = null;
        String weft = null;
        double gradePay = 0;
        double perPay = 0;
        double sPay = 0;
        double othPay = 0;
        String otherDesc = null;
        String doni = null;
        String payRevId = null;
        String superNote = null;
        String authName = null;
        String offName = null;
        String deptName = null;
        String othSpn = "";
        String notficationString = "";
        String proNoun = null;

        Connection con = null;
        try {

            con = dataSource.getConnection();
            st = con.createStatement();

            othSpn = getOtherSpn(prf.getHidNotifyingOthSpc());

            proNoun = getPronoun(prf.getEmpid());

            if (!othSpn.equals("")) {
                authName = othSpn;
            } else {
                if (prf.getNotifyingSpc() != null && !prf.getNotifyingSpc().equals("")) {
                    authName = getSPN(prf.getNotifyingSpc());
                } else if (prf.getHidNotifyingOffCode() != null && !prf.getHidNotifyingOffCode().equals("")) {
                    offName = getOffName(prf.getHidNotifyingOffCode());
                } else if (prf.getHidNotifyingDeptCode() != null && !prf.getHidNotifyingDeptCode().equals("")) {
                    deptName = getDeptName(prf.getHidNotifyingDeptCode());
                }
            }
            notficationString = getNotificationString(prf.getTxtNotOrdNo(), prf.getTxtNotOrdDt(), deptName, offName, authName);
            ///This query is for getting the Pay Details from EMP_PAY_RECORD Table

            if (!prf.getTxtBasic().equals("") && !prf.getTxtBasic().equals("0") && prf.getTxtBasic() != null)//EMP_PAY_RECORD
            {
                rBasic = Double.parseDouble(prf.getTxtBasic());
            }

            if (!prf.getTxtGP().equals("") && !prf.getTxtGP().equals("0") && prf.getTxtGP() != null)//EMP_PAY_RECORD
            {
                gradePay = Double.parseDouble(prf.getTxtGP());
            }

            if (!prf.getTxtSP().equals("") && !prf.getTxtSP().equals("0") && prf.getTxtSP() != null)//EMP_PAY_RECORD
            {
                sPay = Double.parseDouble(prf.getTxtSP());
            }

            if (!prf.getTxtPP().equals("") && !prf.getTxtPP().equals("0") && prf.getTxtPP() != null)//EMP_PAY_RECORD
            {
                perPay = Double.parseDouble(prf.getTxtPP());
            }
            if (!prf.getTxtOP().equals("") && !prf.getTxtOP().equals("0") && prf.getTxtOP() != null)//EMP_PAY_RECORD
            {
                othPay = Double.parseDouble(prf.getTxtOP());
            }
            if (prf.getTxtDescOP() != null && !prf.getTxtDescOP().equalsIgnoreCase(""))//EMP_PAY_RECORD
            {
                otherDesc = prf.getTxtDescOP().toUpperCase();
            }
            if (prf.getSltPayScale() != null && !prf.getSltPayScale().equalsIgnoreCase(""))//EMP_PAY_RECORD
            {
                rScale = prf.getSltPayScale().toUpperCase();//EMP_PAY_RECORD
            }

            if (prf.getSltWEFTime() != null && !prf.getSltWEFTime().equals(""))//EMP_PAY_RECORD
            {
                weft = prf.getSltWEFTime().toUpperCase();
            }
            if (prf.getTxtWEFDt() != null && !prf.getTxtWEFDt().equalsIgnoreCase(""))//EMP_PAY_RECORD
            {
                wefd = prf.getTxtWEFDt().toUpperCase();//EMP_PAY_RECORD
            }

            ///This query is for getting the Notication Details from EMP_NOTIFICATION Table
            ///This query is for getting the Next Increment Date from EMP_PAY Table  
            rs2 = st.executeQuery("SELECT * FROM EMP_PAY_LOG WHERE EMP_ID='" + prf.getEmpid() + "' AND NOT_ID='" + notid + "'");
            while (rs2.next()) {
                payRevId = rs2.getString("PRID");
                if (rs2.getDate("DONI") != null && !rs2.getString("DONI").equals(""))//EMP_NOTIFICATION
                {
                    doni = CommonFunctions.getFormattedOutputDate1(rs2.getDate("DONI"));
                }
            }
            DataBaseFunctions.closeSqlObjects(rs2);

            if (payType.equals("PAYREVISION")) {
                payRev.append(" PAY REVISED ");

                if (rBasic != 0) {
                    payRev.append(" @ Rs.").append(CommonFunctions.formatNumber(rBasic)).append("/- PM ");
                }

                if (gradePay != 0) {
                    payRev.append("WITH GRADE PAY Rs.").append(CommonFunctions.formatNumber(gradePay)).append("/- PM ");
                }
                if (wefd != null && !wefd.equals("")) {
                    payRev.append(" WEF ").append(wefd);
                }
                if (rScale != null) {
                    payRev.append(" IN THE REVISED SCALE OF PAY OF RS. ").append(rScale).append("/-");
                } else if (prf.getPayCell() != null && !prf.getPayCell().equals("")) {
                    payRev.append(" IN CELL " + prf.getPayCell() + " OF LEVEL " + prf.getPayLevel() + " ");
                }

            } else if (payType.equals("PAYFIXATION")) {
                payRev.append(" PAY FIXED ");
                if (rBasic != 0) {
                    payRev.append(" @ Rs.").append(CommonFunctions.formatNumber(rBasic)).append("/- PM ");
                }
                if (gradePay != 0) {
                    payRev.append("WITH GRADE PAY Rs.").append(CommonFunctions.formatNumber(gradePay)).append("/- PM ");
                }
                if (wefd != null && !wefd.equals("")) {
                    payRev.append(" WEF ").append(wefd);
                }
                if (rScale != null) {
                    payRev.append(" IN THE SCALE OF PAY OF RS. ").append(rScale).append("/-");
                } else if (prf.getPayCell() != null && !prf.getPayCell().equals("")) {
                    payRev.append(" IN CELL " + prf.getPayCell() + " OF LEVEL " + prf.getPayLevel() + " ");
                }

                if (prf.getSltPayFixationReason() != null) {
                    payRev.append(" ").append(" HAVING REASON ").append(prf.getSltPayFixationReason());
                }
            } else if (payType.equals("STEPUP")) {
                payRev.append(" PAY STEPED UP ");
                if (rBasic != 0) {
                    payRev.append(" AND FIXED AT ").append(" Rs.").append(CommonFunctions.getAbsoluteNumber(rBasic)).append("/- PM ");
                }
                if (gradePay != 0) {
                    payRev.append("WITH GRADE PAY Rs.").append(CommonFunctions.formatNumber(gradePay)).append("/- PM ");
                }

                if (wefd != null && !wefd.equals("")) {
                    payRev.append(" WEF ").append(wefd);
                }
                if (rScale != null) {
                    payRev.append(" IN THE SCALE OF PAY OF RS. ").append(rScale).append("/-");
                } else if (prf.getPayCell() != null && !prf.getPayCell().equals("")) {
                    payRev.append(" IN CELL " + prf.getPayCell() + " OF LEVEL " + prf.getPayLevel() + " ");
                }
            } else {
                payRev.append(" PAY FIXED ");
                if (rBasic != 0) {
                    payRev.append(" @ Rs.").append(CommonFunctions.formatNumber(rBasic)).append("/- PM ");
                }
                if (gradePay != 0) {
                    payRev.append("WITH GRADE PAY Rs.").append(CommonFunctions.formatNumber(gradePay)).append("/- PM ");
                }

                if (wefd != null && !wefd.equals("")) {
                    payRev.append(" WEF ").append(wefd);
                }
                if (rScale != null) {
                    payRev.append(" IN THE SCALE OF PAY OF RS. ").append(rScale).append("/-");
                } else if (prf.getPayCell() != null && !prf.getPayCell().equals("")) {
                    payRev.append(" IN CELL " + prf.getPayCell() + " OF LEVEL " + prf.getPayLevel() + " ");
                }
            }
            if (sPay != 0) {
                payRev.append(" + SPECIAL PAY @ RS. ").append(CommonFunctions.formatNumber(sPay)).append("/- ");
            }
            if (perPay != 0) {
                payRev.append(" + PERSONAL PAY @ RS. ").append(CommonFunctions.formatNumber(perPay)).append("/- ");
            }
            if (otherDesc != null && !otherDesc.equalsIgnoreCase("")) {
                if (othPay != 0) {
                    payRev.append(" + ").append(otherDesc).append(" @ RS. ").append(CommonFunctions.formatNumber(othPay)).append("/- ");
                }
            } else {
                if (othPay != 0) {
                    payRev.append(" + OTHER PAY @ RS. ").append(CommonFunctions.formatNumber(othPay)).append("/- ");
                }
            }
            if (sPay != 0 || perPay != 0 || othPay != 0) {
                payRev.append(" PM ");
            }

            if (notficationString != null && !notficationString.equalsIgnoreCase("")) {
                payRev.append(notficationString);
            }

            if (!payRev.toString().trim().equals("")) {
                payRev.append(".");
            }

            String tempPronoun = null;
            if (proNoun.trim().equalsIgnoreCase("HE")) {
                tempPronoun = " HIS ";
            } else if (proNoun.trim().equalsIgnoreCase("SHE")) {
                tempPronoun = " HER ";
            } else {
                tempPronoun = " HIS/ HER ";
            }
            if (doni != null) {
                payRev.append(" ").append(tempPronoun).append(" DATE OF NEXT INCREMENT WILL FALL DUE ON ").append(doni).append(".");
            }
            //////////////////////////////
            ArrayList basicPay = new ArrayList();
            ArrayList gradPay = new ArrayList();
            ArrayList wefDate = new ArrayList();
            ArrayList otherPay = new ArrayList();
            ArrayList personalPay = new ArrayList();
            ArrayList specialPay = new ArrayList();
            ArrayList otherPayDesc = new ArrayList();
            ArrayList payscale = new ArrayList();
            ArrayList paylevel = new ArrayList();
            ArrayList paycell = new ArrayList();
            String orderIncrNo = null;
            String orderIncrDate = null;
            String queryIncrment = "SELECT * FROM (SELECT E.*,F.INCRID,F.PRID,F.INCR,F.FIRST_INCR,F.second_incr,F.third_incr,F.fourth_incr,F.incr_type FROM EMP_NOTIFICATION_LOG E,"
                    + "EMP_INCR_LOG F WHERE E.NOT_TYPE='INCREMENT' AND E.EMP_ID='" + prf.getEmpid() + "' AND E.NOT_ID=F.NOT_ID and F.PRID='" + payRevId + "')  T,"
                    + "EMP_PAY_RECORD_LOG R WHERE T.NOT_TYPE='INCREMENT' AND T.EMP_ID='" + prf.getEmpid() + "' AND T.NOT_ID=R.NOT_ID";
            rsInside = st.executeQuery(queryIncrment);
            while (rsInside.next()) {
                if (rsInside.getDate("wef") != null && !rsInside.getDate("wef").equals("")) {
                    wefDate.add(CommonFunctions.getFormattedOutputDate1(rsInside.getDate("wef")));
                }
                personalPay.add(rsInside.getString("p_pay"));
                otherPay.add(rsInside.getString("oth_pay"));
                otherPayDesc.add(rsInside.getString("oth_desc"));
                basicPay.add(rsInside.getString("pay"));
                gradPay.add(rsInside.getString("gp"));
                specialPay.add(rsInside.getString("s_pay"));
                payscale.add(rsInside.getString("pay_scale"));
                paylevel.add(rsInside.getString("pay_level"));
                paycell.add(rsInside.getString("pay_cell"));
                if (rsInside.getString("ORDNO") != null && !rsInside.getString("ORDNO").trim().equals("")) {
                    orderIncrNo = rsInside.getString("ORDNO");
                }
                if (rsInside.getDate("ORDDT") != null && !rsInside.getString("ORDDT").equalsIgnoreCase("")) {
                    orderIncrDate = CommonFunctions.getFormattedOutputDate1(rsInside.getDate("ORDDT"));
                }
            }
            DataBaseFunctions.closeSqlObjects(rsInside, st);

            String tempProNoun1 = null;
            if (proNoun.trim().equalsIgnoreCase("HE")) {
                tempProNoun1 = " HIS ";
            } else if (proNoun.trim().equalsIgnoreCase("SHE")) {
                tempProNoun1 = " HER ";
            } else {
                tempProNoun1 = " HIS/ HER ";
            }
            if (wefDate != null) {
                if ((wefDate.size() > 0) || (basicPay != null && basicPay.size() > 0)) {
                    if (wefDate.size() == 1) {
                        if (wefDate.get(0) != null && !wefDate.get(0).toString().trim().equals("")) {
                            payRev.append(" ").append(proNoun).append(" IS ALLOWED TO DRAW ").append(tempProNoun1).append(" NEXT INCREMENT WEF ").append(wefDate.get(0));
                        }
                        if (basicPay != null) {
                            if (basicPay.get(0) != null && !basicPay.get(0).toString().trim().equals("")) {
                                payRev.append(" RAISING PAY TO Rs. ").append(CommonFunctions.formatNumber(basicPay.get(0).toString())).append("/- ");
                            }
                        }
                        if (paylevel != null) {
                            payRev.append(" IN CELL " + paycell.get(0) + " OF LEVEL " + paylevel.get(0) + " ");
                        } else {
                            if (gradPay != null) {
                                payRev.append("WITH GRADE PAY Rs.").append(gradPay.get(0)).append("/- PM ");
                            }
                            if (payscale != null) {
                                payRev.append(" IN THE SCALE OF PAY OF RS. ").append(payscale.get(0)).append("/-");
                            }
                        }
                        if (orderIncrNo != null) {
                            payRev.append(" VIDE ORDER NO. ").append(orderIncrNo);
                            if (orderIncrDate != null) {
                                payRev.append(" DATED ").append(orderIncrDate).append(".");
                            }
                        } else if (orderIncrDate != null) {
                            payRev.append("VIDE OFFICE ORDER, DATED ").append(orderIncrDate).append(".");
                        }

                    } else if (wefDate.size() == 2) {
                        if (wefDate.get(0) != null && !wefDate.get(0).toString().equals("")) {
                            payRev.append(" ").append(proNoun).append(" IS ALLOWED TO DRAW ").append(tempProNoun1).append(" NEXT & SUBSEQUENT INCREMENTS WEF ").append(wefDate.get(0));
                        }
                        if (wefDate.get(1) != null && !wefDate.get(1).toString().equals("")) {
                            payRev.append(" & ").append(wefDate.get(1));
                        }
                        if (basicPay.get(0) != null && !basicPay.get(0).toString().equals("")) {
                            payRev.append(" RAISING PAY TO Rs. ").append(CommonFunctions.formatNumber(basicPay.get(0).toString())).append("/- ");
                        }
                        if (basicPay.get(1) != null && !basicPay.get(1).toString().equals("")) {
                            payRev.append(" & Rs.").append(CommonFunctions.formatNumber(basicPay.get(1).toString())).append("/- RESPECTIVELY ");
                        }
                        if (paylevel.get(0) != null) {
                            payRev.append(" IN CELL " + paycell.get(0) + " OF LEVEL " + paylevel.get(0) + " ");
                        } else {
                            if (gradPay.get(0) != null) {
                                payRev.append("WITH GRADE PAY Rs.").append(gradPay.get(0)).append("/- PM ");
                            }
                            if (payscale.get(0) != null) {
                                payRev.append(" IN THE SCALE OF PAY OF RS. ").append(payscale.get(0)).append("/-");
                            }
                        }
                        if (paylevel.get(1) != null) {
                            payRev.append(" IN CELL " + paycell.get(1) + " OF LEVEL " + paylevel.get(1) + " ");
                        } else {
                            if (gradPay.get(1) != null) {
                                payRev.append("WITH GRADE PAY Rs.").append(gradPay.get(1)).append("/- PM ");
                            }
                            if (payscale.get(1) != null) {
                                payRev.append(" IN THE SCALE OF PAY OF RS. ").append(payscale.get(1)).append("/-");
                            }
                        }
                        if (orderIncrNo != null) {
                            payRev.append(" VIDE ORDER NO. ").append(orderIncrNo);
                            if (orderIncrDate != null) {
                                payRev.append(" DATED ").append(orderIncrDate).append(".");
                            }
                        } else if (orderIncrDate != null) {
                            payRev.append("VIDE OFFICE ORDER, DATED ").append(orderIncrDate).append(".");
                        }
                    } else if (wefDate.size() > 2) {
                        payRev.append(" ").append(proNoun).append(" IS ALLOWED TO DRAW ").append(tempProNoun1).append(" NEXT & SUBSEQUENT INCREMENTS WEF ");
                        for (int i = 0; i < wefDate.size(); i++) {
                            if (wefDate.get(i) != null) {
                                payRev.append(wefDate.get(i));
                                if (i == wefDate.size() - 2) {
                                    payRev.append(" & ");
                                } else if (i < wefDate.size() - 2) {
                                    payRev.append(", ");
                                }
                            }
                        }
                        payRev.append(" RASIING ").append(tempProNoun1).append(" PAY TO ");
                        for (int i = 0; i < basicPay.size(); i++) {
                            if (basicPay.get(i) != null) {
                                payRev.append(" RS. ").append(CommonFunctions.formatNumber(basicPay.get(i).toString())).append("/- PM ");
                                if (i == basicPay.size() - 2) {
                                    payRev.append(" & ");
                                } else if (i < basicPay.size() - 2) {
                                    payRev.append(", ");
                                }
                            }
                        }
                        if (paylevel != null) {
                            for (int i = 0; i < paylevel.size(); i++) {
                                payRev.append(" IN CELL " + paycell.get(i) + " OF LEVEL " + paylevel.get(i) + " ");
                                if (i == paylevel.size() - 2) {
                                    payRev.append(" & ");
                                } else if (i < paylevel.size() - 2) {
                                    payRev.append(", ");
                                }
                            }
                        }
                        if (orderIncrNo != null) {
                            payRev.append(" VIDE ORDER NO. ").append(orderIncrNo);
                            if (orderIncrDate != null) {
                                payRev.append(" DATED ").append(orderIncrDate).append(".");
                            }
                        } else if (orderIncrDate != null) {
                            payRev.append("VIDE OFFICE ORDER, DATED ").append(orderIncrDate).append(".");
                        }
                    }
                }
            }
            /////////////////////////////////
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs2);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return payRev.toString();

    }

    @Override
    public String getSuperanuationFormDetails(int notid) {
        StringBuffer spForm = new StringBuffer();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        //Retirement retirement = new Retirement();
        String gender = null;
        String gender1 = null;
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("SELECT not_id,emp_notification.emp_id,gender,ordno,orddt,dos,doe,toe,emp_notification.ent_dept,department_name,emp_notification.ent_off,\n"
                    + "off_en,ent_auth,spn,ret_pay FROM emp_notification\n"
                    + "left outer join g_spc on g_spc.spc=emp_notification.ent_auth\n"
                    + "left outer join g_office on g_office.off_code=emp_notification.ent_off\n"
                    + "left outer join g_department on g_department.department_code=emp_notification.ent_dept\n"
                    + "left outer join emp_mast on emp_mast.emp_id=emp_notification.emp_id\n"
                    + "WHERE emp_notification.not_id=? AND NOT_TYPE='SUPERANNUATION'");
            pst.setInt(1, notid);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getString("gender") != null && rs.getString("gender").equals("M")) {
                    gender = "HE";
                    gender1 = "HIS";
                } else if (rs.getString("gender") != null && rs.getString("gender").equals("F")) {
                    gender = "SHE";
                    gender1 = "HER";
                } else {
                    gender = "HE/SHE";
                    gender1 = "HIS/HER";
                }
                spForm.append(gender).append("  IS GOING TO BE RETIRED FROM GOVERNMENT SERVICES");
                if (rs.getString("dos") != null && !rs.getString("dos").equals("")) {
                    spForm.append(" W.E.F. ").append(CommonFunctions.getFormattedOutputDate1(rs.getDate("dos"))).append(" ON ATTAINING THE AGE OF SUPERANNUATION");

                    if (rs.getString("ret_pay") != null) {
                        spForm.append(" AND ").append(gender1).append(" LAST PAY AS ON ").append(CommonFunctions.getFormattedOutputDate1(rs.getDate("dos"))).append(" WILL BE Rs." + rs.getString("ret_pay") + "/-");
                        spForm.append(" UNDER NORMAL CIRCUMSTANCES");
                    }
                }
                if (rs.getString("ordno") != null && rs.getString("orddt") != null) {
                    spForm.append(" VIDE NOTIFICATION/OFFICE ORDER NO. " + rs.getString("ordno") + " DATED " + CommonFunctions.getFormattedOutputDate1(rs.getDate("orddt")));
                }
                if (rs.getString("SPN") != null && !rs.getString("SPN").equals("")) {
                    spForm.append(" ISSUED BY " + rs.getString("SPN")+".");
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return spForm.toString();
    }

}
