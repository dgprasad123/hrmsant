<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%
    int slno = 0;
    String fiscalyear = "";
    String atchid = "";
    String downloadlink = "";
%>
<html>    
    <body>
        <div align="center" style="margin-top:5px;margin-bottom:10px;">
            <div align="center">
                <table border="0" width="99%" cellpadding="0" cellspacing="0" style="font-size:14px;">
                    <tr>
                        <td style="color:#000000;padding:0px;font-weight:bold;" align="center"><h2>Performance Appraisal Report (PAR) for Group 'A' & 'B' officers of Govt. of Odisha</h2></td>
                    </tr>

                    <tr>
                        <td style="color:#000000;padding:0px;font-weight:bold;" align="center"><h2>Transmission Record</h2></td>
                    </tr>
                </table>
            </div>
        </div>

        <div>
            <hr/>
            <table width="100%" border="0" cellpadding="0" cellspacing="0" style="font-size:13px;">
                <tr>
                    <td align="center" colspan="2">
                        (To be filled in by Appraisee)
                    </td>
                </tr>
                <tr>
                    <td width="40%">&nbsp;</td>
                    <td width="60%">&nbsp;</td>
                </tr>
                <tr>
                    <td colspan="2">
                        Financial Year: <u><c:out value="${pardetail.fiscalYear}"/></u>&nbsp;(for the period from <c:out value="${pardetail.parPeriodFrom}"/> to <c:out value="${pardetail.parPeriodTo}"/>)
                </td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td>
                        Name and Designation of the Officer Reported Upon
                    </td>
                    <td style="padding-left:10px;">
                        <b><c:out value="${pardetail.applicant}"/></b>
                    </td>
                </tr>
                <tr style="height:40px;">
                    <td>&nbsp;</td>
                    <td style="padding-left:10px;">
                        <b><c:out value="${pardetail.apprisespn}"/></b>
                    </td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td>
                        Service and Group (A/B) to which the Officer belongs
                    </td>
                    <td style="padding-left:10px;">
                        <c:out value="${pardetail.empService}"/>
                    </td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td style="padding-left:10px;">
                        <b><c:out value="${pardetail.empGroup}"/></b>
                    </td>
                </tr>
            </table>
        </div>

        <div style="width:100%;overflow: auto;margin-top:5px;border:1px">
            <hr/>
            <div align="center">
                <p style="text-align: center">Details of Transmission / Movement of PAR<br />
                    (To be filled in at the time of transmission<br />
                    by respective officer/staff)</p>
            </div>
            <div align="center">
                <div style="width:99%;">                        
                    <div style="width:100%;overflow: auto;margin-top:1px;border:1px solid #5095ce;">
                        <table border="0" cellpadding="5" cellspacing="0" width="100%" style="font-size:14px;">
                            <tr style="height: 40px">                               
                                <td width="20%" style="border:1px solid black;">
                                    Transmission By
                                </td>
                                <td align="center" width="80%" style="border:1px solid black;">
                                    Transmitted to whom (Name,Designation and Address)
                                </td>
                            </tr>
                            <tr style="height: 40px">                               
                                <td style="border:1px solid black;">Reporting Authority</td>
                                <td align="center" style="border:1px solid black;">
                                    <c:if test="${not empty pardetail.reportingauth}">
                                        <table border="0" cellpadding="0" cellspacing="0" width="100%" style="font-size:14px;">
                                            <c:forEach var="rptauth" items="${pardetail.reportingauth}">
                                                <%slno = slno + 1;%>
                                                <tr>
                                                    <td width="5%"><%=slno%>.</td>
                                                    <td width="95%">
                                                        <p style="margin:0;padding:0;"><c:out value="${rptauth.authorityname}"/> (<c:out value="${rptauth.authorityspn}"/>)</p>
                                                        (<span style="font-size:10.5px;">From:<c:out value="${rptauth.fromdt}"/> To:<c:out value="${rptauth.todt}"/></span>)    
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </table>
                                    </c:if>
                                </td>
                            </tr>
                            <%slno = 0;%>
                            <tr style="height: 40px">                               
                                <td style="border:1px solid black;">Reviewing Authority</td>
                                <td style="border:1px solid black;">
                                    <c:if test="${not empty pardetail.reviewingauth}">
                                        <table border="0" cellpadding="0" cellspacing="0" width="100%" style="font-size:14px;">
                                            <c:forEach var="rptauth" items="${pardetail.reviewingauth}">
                                                <%slno = slno + 1;%>
                                                <tr>
                                                    <td width="5%"><%=slno%>.</td>
                                                    <td width="95%">
                                                        <p style="margin:0;padding:0;"><c:out value="${rptauth.authorityname}"/>(<c:out value="${rptauth.authorityspn}"/>)</p>
                                                        (<span style="font-size:10.5px;">From:<c:out value="${rptauth.fromdt}"/> To:<c:out value="${rptauth.todt}"/></span>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </table>
                                    </c:if>
                                </td>
                            </tr>
                            <%slno = 0;%>
                            <tr style="height: 40px">
                                <td style="border:1px solid black;">Accepting Authority</td>
                                <td style="border:1px solid black;">
                                    <c:if test="${not empty pardetail.acceptingauth}">
                                        <table border="0" cellpadding="0" cellspacing="0" width="100%" style="font-size:14px;">
                                            <c:forEach var="rptauth" items="${pardetail.acceptingauth}">
                                                <%slno = slno + 1;%>
                                                <tr>
                                                    <td width="5%"><%=slno%>.</td>
                                                    <td width="95%">
                                                        <p style="margin:0;padding:0;"><c:out value="${rptauth.authorityname}"/>(<c:out value="${rptauth.authorityspn}"/></p>
                                                        (<span style="font-size:10.5px;">From:<c:out value="${rptauth.fromdt}"/> To:<c:out value="${rptauth.todt}"/></span>)
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </table>
                                    </c:if>
                                </td>
                            </tr>
                        </table>
                    </div>        
                    <h1 style="page-break-before: always"></h1>
                    <table border="0" cellspcaing="0" cellpadding="0" width="100%">
                        <tr style="height:30px;font-size:15px;">
                            <td align="center"><b>PERFORMANCE APPRAISAL REPORT</b></td>
                        </tr>
                        <tr style="height:30px;">
                            <td align="center">for</td>
                        </tr>
                        <tr style="height:30px;">
                            <td align="center">Group A and Group B Officers of Govt. of Odisha</td>
                        </tr>
                        <tr style="height:30px;">
                            <td align="center">Report for the financial year <c:out value="${pardetail.fiscalYear}"/></td>
                        </tr>
                        <tr>&nbsp;</tr>
                        <tr style="height:30px;">
                            <td align="center">(Period from <b><c:out value="${pardetail.parPeriodFrom}"/></b> to <b><c:out value="${pardetail.parPeriodTo}"/>)</b></td>
                        </tr>
                    </table>
                    <br />
                    <div style="width:100%;overflow: auto;">
                        <table border="0" cellpadding="0" cellspacing="0" width="100%">
                            <tr>
                                <td colspan="2" style="border-top-width:1px; border-top-style:solid;border-top-color: black;border-left-width:1px;border-left-style: solid;border-left-color: black;border-right-width:1px;border-right-style: solid;border-right-color: black;">&nbsp;</td>
                            </tr>
                            <tr>
                                <td colspan="2" align="center" style="border-left-width:1px;border-left-style: solid;border-left-color: black;border-right-width:1px;border-right-style: solid;border-right-color: black;">
                                    PERSONAL DATA
                                </td>
                            </tr>
                            <tr>
                                <td width="35%" align="center" style="border-left-width:1px;border-left-style: solid;border-left-color: black;">
                                    PART-I
                                </td>
                                <td width="65%" style="border-right-width:1px;border-right-style: solid;border-right-color: black;">
                                    (To be filled in by the Appraisee)
                                </td>
                            </tr>
                        </table>

                        <table border="0" cellpadding="5" cellspacing="0" width="100%" style="font-size:12px;">
                            <tr style="height: 40px">                               
                                <td width="40%" style="border-left-width:1px;border-left-style: solid;border-left-color: black;">1. Full Name of the Officer</td>
                                <td width="60%" style="padding-left:20px;">
                                    <c:out value="${pardetail.applicant}"/>
                                </td> 
                                <td rowspan="17" valign="top" style="border-right-width:1px;border-right-style: solid;border-right-color: black;border-bottom-width:1px;border-bottom-style:solid;border-bottom-color:black;">
                                    <img id="loginUserPhoto" style="border:1px solid #a3a183;padding:3px;" onerror="callNoImage()" alt="ProfileImage" src='displayprofilephoto.htm?empid=${pardetail.applicantempid}' width="100" height="100" />
                                </td>
                            </tr>
                            <tr style="height: 40px">                               
                                <td style="border-left-width:1px;border-left-style: solid;border-left-color: black;">2. Date of Birth</td>
                                <td style="padding-left:20px;"><c:out value="${pardetail.dob}"/></td>
                            </tr>
                            <tr style="height: 40px">                               
                                <td style="border-left-width:1px;border-left-style: solid;border-left-color: black;">3. Service to which the officer belongs</td>
                                <td style="padding-left:20px;"><c:out value="${pardetail.empService}"/></td>
                            </tr>
                            <tr style="height: 40px">                               
                                <td style="border-left-width:1px;border-left-style: solid;border-left-color: black;">4. Group to which the officer belongs</td>
                                <td style="padding-left:20px;"><c:out value="${pardetail.empGroup}"/></td>
                            </tr>
                            <tr style="height: 40px">                               
                                <td style="border-left-width:1px;border-left-style: solid;border-left-color: black;">5. Designation during the period of report</td>
                                <td style="padding-left:20px;"><c:out value="${pardetail.apprisespn}"/></td>
                            </tr>
                            <tr style="height: 40px">                               
                                <td style="border-left-width:1px;border-left-style: solid;border-left-color: black;">6. Office to which posted with Head Quarters</td>
                                <td style="padding-left:20px;">
                                    <c:out value="${pardetail.empOffice}"/>
                                    <c:if test="${not empty pardetail.sltHeadQuarter}">
                                        ,<c:out value="${pardetail.sltHeadQuarter}"/>
                                    </c:if>
                                </td>
                            </tr>
                            <tr style="height: 40px">                               
                                <td style="border-left-width:1px;border-left-style: solid;border-left-color: black;">
                                    7. Period(s) of absence (on leave,training etc,if 30 days or more).Please mention date(s):
                                </td>
                                <td width="70%" style="padding-left:20px;">
                                    <c:if test="${not empty pardetail.leaveAbsentee}">
                                        <c:forEach var="leave" items="${pardetail.leaveAbsentee}">
                                            <c:out value="${leave.fromDate}"/>-<c:out value="${leave.toDate}"/>
                                        </c:forEach>
                                    </c:if>
                                </td>
                            </tr>
                            <tr style="height: 40px">                               
                                <td style="border-left-width:1px;border-left-style: solid;border-left-color: black;">
                                    8. Name and Designation of the Reporting Authority and period worked under him/her:
                                </td>
                                <td width="70%" style="padding-left:20px;">
                                    <c:if test="${not empty pardetail.reportingauth}">
                                        <c:forEach var="rptauth" items="${pardetail.reportingauth}">
                                            <p style="margin:0;padding:0;"><c:out value="${rptauth.authorityname}"/> (<c:out value="${rptauth.authorityspn}"/>)</p>
                                            (<span style="font-size:10.5px;">From:<c:out value="${rptauth.fromdt}"/> To:<c:out value="${rptauth.todt}"/></span>)
                                        </c:forEach>
                                    </c:if>
                                </td>
                            </tr>
                            <tr style="height: 40px">                               
                                <td style="border-left-width:1px;border-left-style: solid;border-left-color: black;">
                                    9. Name and Designation of the Reviewing Authority and period worked under him/her:
                                </td>
                                <td width="70%" style="padding-left:20px;">
                                    <c:if test="${not empty pardetail.reviewingauth}">
                                        <c:forEach var="rptauth" items="${pardetail.reviewingauth}">
                                            <p style="margin:0;padding:0;"><c:out value="${rptauth.authorityname}"/> (<c:out value="${rptauth.authorityspn}"/>)</p>
                                            (<span style="font-size:10.5px;">From:<c:out value="${rptauth.fromdt}"/> To:<c:out value="${rptauth.todt}"/></span>)
                                        </c:forEach>
                                    </c:if>
                                </td>
                            </tr>
                            <tr style="height: 40px">                               
                                <td style="border-left-width:1px;border-left-style: solid;border-left-color: black;">
                                    10. Name and Designation of the Accepting Authority and period worked under him/her:
                                </td>
                                <td width="70%" style="padding-left:20px;">
                                    <c:if test="${not empty pardetail.acceptingauth}">
                                        <c:forEach var="rptauth" items="${pardetail.acceptingauth}">
                                            <p style="margin:0;padding:0;"><c:out value="${rptauth.authorityname}"/> (<c:out value="${rptauth.authorityspn}"/>)</p>
                                            (<span style="font-size:10.5px;">From:<c:out value="${rptauth.fromdt}"/> To:<c:out value="${rptauth.todt}"/></span>)
                                        </c:forEach>
                                    </c:if>
                                </td>
                            </tr>
                            <tr style="height:40px;">
                                <td style="border-left-width:1px;border-left-style: solid;border-left-color: black;">&nbsp;</td>
                                <td style="border-right-width:1px;">&nbsp;</td>
                            </tr>
                            <tr style="height:40px;">
                                <td style="border-left-width:1px;border-left-style: solid;border-left-color: black;">&nbsp;</td>
                                <td>&nbsp;</td>
                            </tr>
                            <tr style="height:40px;">
                                <td style="border-left-width:1px;border-left-style: solid;border-left-color: black;">&nbsp;</td>
                                <td>&nbsp;</td>
                            </tr>
                            <tr>
                                <td style="border-left-width:1px;border-left-style: solid;border-left-color: black;">&nbsp;</td>
                                <td>
                                    <c:out value="${pardetail.applicant}"/>
                                </td>
                            </tr>
                            <tr>
                                <td style="border-left-width:1px;border-left-style: solid;border-left-color: black;">&nbsp;</td>
                                <td>
                                    Signature of the Appraisee
                                </td>
                            </tr>
                            <tr style="height:40px;">
                                <td style="border-left-width:1px;border-left-style: solid;border-left-color: black;">&nbsp;</td>
                                <td>&nbsp;</td>
                            </tr>
                            <tr style="height:40px;">
                                <td style="border-left-width:1px;border-left-style: solid;border-left-color: black;border-bottom-width:1px;border-bottom-style:solid;border-bottom-color:black;">&nbsp;</td>
                                <td style="border-bottom-width:1px;border-bottom-style:solid;border-bottom-color:black;">&nbsp;</td>
                            </tr>
                        </table>
                    </div>
                    <h1 style="page-break-before: always"></h1>
                    <div style="width:100%;overflow: auto;margin-top:5px;">
                        <table border="0" cellpadding="0" cellspacing="0" width="100%" style="font-size:14px;">
                            <tr>
                                <td colspan="3" style="border-top-width:1px;border-top-style: solid;border-top-color: black;border-left-width:1px;border-left-style: solid;border-left-color: black;border-right-width:1px;border-right-style: solid;border-right-color: black;">&nbsp;</td>
                            </tr>
                            <tr>
                                <td width="30%" align="center" style="border-left-width:1px;border-left-style: solid;border-left-color: black;">
                                    PART-II
                                </td>
                                <td width="40%" align="center">
                                    SELF-APPRAISAL
                                </td>
                                <td width="30%" style="border-right-width:1px;border-right-style: solid;border-right-color: black;">&nbsp;</td>
                            </tr>
                        </table>
                        <table border="0" cellpadding="0" cellspacing="0" width="100%" style="font-size:13px;">
                            <tr>
                                <td width="30%" style="border-left-width:1px;border-left-style: solid;border-left-color: black;">&nbsp;</td>
                                <td width="40%" align="center">
                                    (To be filled in by the Appraisee)
                                </td>
                                <td width="30%" style="border-right-width:1px;border-right-style: solid;border-right-color: black;">&nbsp;</td>
                            </tr>
                        </table>
                        <table align="center" border="0" cellpadding="0" cellspacing="0" width="100%" style="font-size:12px;">                                         
                            <tr>                               
                                <td style="border-left-width:1px;border-left-style: solid;border-left-color: black;border-right-width:1px;border-right-style: solid;border-right-color: black;">&nbsp;</td>
                            </tr>
                            <tr>
                                <td style="border-left-width:1px;border-left-style: solid;border-left-color: black;border-right-width:1px;border-right-style: solid;border-right-color: black;">
                                    1. Brief description of duties/tasks entrusted.(in about 100 words)
                                </td>
                            </tr>
                            <tr>
                                <td style="border-left-width:1px;border-left-style: solid;border-left-color: black;border-right-width:1px;border-right-style: solid;border-right-color: black;">
                                    &nbsp;&nbsp;&nbsp;<c:out value="${pardetail.selfappraisal}" escapeXml="false"/>
                                </td>
                            </tr>
                            <tr style="height: 40px;">                               
                                <td style="border-left-width:1px;border-left-style: solid;border-left-color: black;border-right-width:1px;border-right-style: solid;border-right-color: black;">
                                    2. Physical/Financial Targets and Achievements
                                </td>
                            </tr>
                            <tr>
                                <td align="center" style="margin:auto;border-left-width:1px;border-left-style: solid;border-left-color: black;border-right-width:1px;border-right-style: solid;border-right-color: black;">
                                    <table align="center" width="100%" border="0" cellspacing="0" cellpadding="0" style="font-size:12px;">
                                        <tr>
                                            <td width="5%" style="border: 1px solid black;">SL No</td>
                                            <td width="32%" align="center" style="border: 1px solid black;">Task</td>
                                            <td width="12%" align="center" style="border: 1px solid black;">Target</td>
                                            <td width="13%" align="center" style="border: 1px solid black;">Achievement</td>
                                            <td width="15%" align="center" style="border: 1px solid black;">Qualitative % of Achievement</td>
                                            <td width="13%" align="center" style="border: 1px solid black;">% of Achievement</td>
                                        </tr>
                                        <c:if test="${not empty pardetail.achivementList}">
                                            <c:forEach var="AchievementBean" items="${pardetail.achivementList}">
                                                <tr height="40px">
                                                    <td align="center"><c:out value="${AchievementBean.slno}"/></td>
                                                    <td align="center"><c:out value="${AchievementBean.task}"/></td>
                                                    <td align="center"><c:out value="${AchievementBean.target}"/></td>
                                                    <td align="center"><c:out value="${AchievementBean.achievement}"/></td>
                                                    <td align="center"><c:out value="${AchievementBean.percentQualitative}"/></td>
                                                    <td align="center"><c:out value="${AchievementBean.percentAchievement}"/></td>
                                                </tr>
                                            </c:forEach>
                                        </c:if>
                                    </table>
                                </td>
                            </tr>
                            <tr style="height: 40px;">                               
                                <td style="border-left-width:1px;border-left-style: solid;border-left-color: black;border-right-width:1px;border-right-style: solid;border-right-color: black;">
                                    3. Significant work, if any, done
                                </td>
                            </tr>
                            <tr>
                                <td style="border-left-width:1px;border-left-style: solid;border-left-color: black;border-right-width:1px;border-right-style: solid;border-right-color: black;">
                                    &nbsp;&nbsp;&nbsp;<c:out value="${pardetail.specialcontribution}" escapeXml="false"/>
                                </td> 
                            </tr>
                            <c:if test="${not empty pardetail.reason && pardetail.reason != ''}">
                                <tr style="height: 40px;">                               
                                    <td style="border-left-width:1px;border-left-style: solid;border-left-color: black;border-right-width:1px;border-right-style: solid;border-right-color: black;">
                                        4. Hindrance
                                    </td>
                                </tr>
                                <tr>
                                    <td style="border-left-width:1px;border-left-style: solid;border-left-color: black;border-right-width:1px;border-right-style: solid;border-right-color: black;">
                                        &nbsp;&nbsp;&nbsp;<c:out value="${pardetail.reason}" escapeXml="false"/>
                                    </td> 
                                </tr>
                            </c:if>
                            <tr style="height: 40px;">                               
                                <td style="border-left-width:1px;border-left-style: solid;border-left-color: black;border-right-width:1px;border-right-style: solid;border-right-color: black;">
                                    Place:- &nbsp;&nbsp;&nbsp; <c:out value="${pardetail.place}"/>
                                </td>
                            </tr>
                            <tr style="height: 40px;">                               
                                <td style="border-bottom-width:1px;border-bottom-style:solid;border-bottom-color:black;border-left-width:1px;border-left-style: solid;border-left-color: black;border-right-width:1px;border-right-style: solid;border-right-color: black;">&nbsp;</td>
                            </tr>
                        </table>
                    </div>
                    <h1 style="page-break-before: always"></h1>
                    <c:if test="${pardetail.ishideremark != null && pardetail.ishideremark == 'N'}">
                        <div style="width:100%;overflow: auto;margin-top:5px;border:1px solid #5F9B24;">
                            <div style="color:#000000;padding:5px;font-weight:bold;" align="left"><u>Remarks of Reporting Authority</u></div>
                            <table border="0" cellpadding="5" cellspacing="0" width="100%">
                                <tr style="height: 40px">
                                    <td width="70%">
                                        <c:if test="${not empty pardetail.reportingdata}">
                                            <%slno = 0;%>
                                            <c:forEach var="reportingdt" items="${pardetail.reportingdata}">
                                                <c:if test="${reportingdt.isreportingcompleted == 'F'}">
                                                    <table width="100%" border="0" cellspacing="0" cellpadding="0" style="font-size:13px;">
                                                        <tr>
                                                            <%if (slno == 0) {%>
                                                            <td style="font-weight:bold;text-decoration:underline;">
                                                                <c:out value="${reportingdt.reportingauthName}"/>
                                                            </td>
                                                            <%} else {%>
                                                            <td style="border-top:1px solid black;font-weight:bold;text-decoration:underline;">
                                                                <c:out value="${reportingdt.reportingauthName}"/>
                                                            </td>
                                                            <%}%>
                                                        </tr>
                                                        <tr>
                                                            <td>
                                                                No Remarks Given by the Authority.
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </c:if>
                                                <c:if test="${reportingdt.isreportingcompleted == 'Y'}">
                                                    <table width="100%" border="0" cellspacing="0" cellpadding="0" style="font-size:13px;">
                                                        <tr>
                                                            <%if (slno == 0) {%>
                                                            <td style="font-weight:bold;text-decoration:underline;">
                                                                <c:out value="${reportingdt.reportingauthName}"/>
                                                            </td>
                                                            <%} else {%>
                                                            <td style="border-top:1px solid black;font-weight:bold;text-decoration:underline;">
                                                                <c:out value="${reportingdt.reportingauthName}"/>
                                                            </td>
                                                            <%}%>
                                                        </tr>
                                                        <tr style="height:30px;">
                                                            <td>1. Assessment of work output, attributes & functional competencies.(This should be on a relative scale of 1-5, with 1 referring to the lowest level & 5   to the highest level. Please indicate your rating for the officer against each item.)</td>
                                                        </tr>
                                                        <tr>
                                                            <td>
                                                                <table width="100%" style="margin-left:35px;font-size:12px;">
                                                                    <tr>
                                                                        <th width="30%">Description</th>
                                                                        <th width="20%">Rating</th>
                                                                        <th width="30%">Description</th>
                                                                        <th width="20%">Rating</th>
                                                                    </tr>

                                                                    <tr>
                                                                        <td>(a)  Attitude to work    :</td>
                                                                        <td><div style="padding:5px;"><c:out value="${reportingdt.ratingattitude}"/></div></td>
                                                                        <td>(f) Co-ordination ability:</td>
                                                                        <td><div style="padding:5px;"><c:out value="${reportingdt.ratingcoordination}"/></div></td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td>(b)  Sense of responsibility:    </td>
                                                                        <td><div style="padding:5px;"><c:out value="${reportingdt.ratingresponsibility}"/></div></td>
                                                                        <td>(g) Ability to work in a team:</td>
                                                                        <td><div style="padding:5px;"><c:out value="${reportingdt.teamworkrating}"/></div></td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td>(c)  Communication skill :  </td>
                                                                        <td><div style="padding:5px;"><c:out value="${reportingdt.ratingcomskill}"/></div></td>
                                                                        <td>(h) Knowledge of Rules/Procedures/ IT  Skills/ Relevant Subject :</td>
                                                                        <td><div style="padding:5px;"><c:out value="${reportingdt.ratingitskill}"/></div></td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td>(d)  Leadership Qualities :  </td>
                                                                        <td><div style="padding:5px;"><c:out value="${reportingdt.ratingleadership}"/></div></td>
                                                                        <td>(i) Initiative :</td>
                                                                        <td><div style="padding:5px;"><c:out value="${reportingdt.ratinginitiative}"/></div></td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td>(e) ) Decision-making ability :  </td>
                                                                        <td><div style="padding:5px;"><c:out value="${reportingdt.ratingdecisionmaking}"/></div></td>
                                                                        <td>(j) ) Quality of Work :</td>
                                                                        <td><div style="padding:5px;"><c:out value="${reportingdt.ratequalityofwork}"/></div></td>
                                                                    </tr>
                                                                </table>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td><span>2.(i) General Assessment </span>(Please give an overall assessment of the officer including   his/her   attitude towards  S.T/S.C/Weaker Sections &  relation  with public):</td>
                                                        </tr>
                                                        <tr>
                                                            <td><div style="padding:5px;"><c:out value="${reportingdt.authNote}"/></div></td>
                                                        </tr>
                                                           <tr>
                                                            <td><span>(ii) Assessment Of Performance Of 5t  </span>(20%):</td>
                                                        </tr>
                                                        <tr>
                                                            <td><div style="padding:5px;"><c:out value="${reportingdt.fiveTComponentappraise}"/></td>
                                                        </tr>
                                                        <tr>
                                                            <td><span>3. Inadequacies, deficiencies or shortcomings, if any (Remarks to be treated as adverse ):</span></td>
                                                        </tr>
                                                        <tr>
                                                            <td><div style="padding:5px;"><c:out value="${reportingdt.inadequaciesNote}"/></div></td>
                                                        </tr>  
                                                        <tr>
                                                            <td><span>4. Integrity (If integrity is doubtful or  adverse please write "Not certified" in the space below and justify your remarks in box 4 above):</span></td>
                                                        </tr>
                                                        <tr>
                                                            <td><div style="padding:5px;"><c:out value="${reportingdt.integrityNote}"/></div></td>
                                                        </tr> 
                                                        <tr>
                                                            <td><span> 5. Overall Grading : </span><span style="padding:5px;"><c:out value="${reportingdt.sltGradingName}"/></span></td>
                                                        </tr>
                                                        <tr>
                                                            <td><span>6. For  Overall Grading  Below Average /  Outstanding  please provide justification in the   space below.:</span></td>
                                                        </tr>
                                                        <tr>
                                                            <td><div style="padding:5px;"><c:out value="${reportingdt.gradingNote}"/></div></td>
                                                        </tr> 
                                                    </table>
                                                </c:if>
                                                <c:if test="${reportingdt.isreportingcompleted != 'Y'}">

                                                </c:if>
                                                <%slno += 1;%>
                                            </c:forEach>
                                        </c:if>
                                    </td>
                                </tr>
                            </table>
                        </div>

                        <div style="width:100%;overflow: auto;margin-top:5px;border:1px solid #5095ce;">
                            <div style="color:#000000;padding:5px;font-weight:bold;" align="left"><u>Remarks of Reviewing Authority</u></div>
                            <table border="0" cellpadding="5" cellspacing="0" width="100%">                                        
                                <tr style="height: 40px">
                                    <td width="70%">
                                        <c:if test="${not empty pardetail.reviewingdata}">
                                            <%slno = 0;%>
                                            <c:forEach var="reviewingdt" items="${pardetail.reviewingdata}">
                                                <c:if test="${reviewingdt.isreviewingcompleted == 'F'}">
                                                    <table border="0" cellpadding="5" cellspacing="0" width="100%" style="font-size:13px;">
                                                        <tr>
                                                            <%if (slno == 0) {%>
                                                            <td style="font-weight:bold;text-decoration:underline;">
                                                                <c:out value="${reviewingdt.reviewingauthName}"/>
                                                            </td>
                                                            <%} else {%>
                                                            <td style="border-top:1px solid black;font-weight:bold;text-decoration:underline;">
                                                                <c:out value="${reviewingdt.reviewingauthName}"/>
                                                            </td>
                                                            <%}%>
                                                        </tr>
                                                        <tr>
                                                            <td>
                                                                No Remarks Given by the Authority.
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </c:if>
                                                <c:if test="${reviewingdt.isreviewingcompleted == 'Y'}">
                                                    <table border="0" cellpadding="5" cellspacing="0" width="100%" style="font-size:13px;">
                                                        <tr>
                                                            <%if (slno == 0) {%>
                                                            <td style="font-weight:bold;text-decoration:underline;">
                                                                <c:out value="${reviewingdt.reviewingauthName}"/>
                                                            </td>
                                                            <%} else {%>
                                                            <td style="border-top:1px solid black;font-weight:bold;text-decoration:underline;">
                                                                <c:out value="${reviewingdt.reviewingauthName}"/>
                                                            </td>
                                                            <%}%>
                                                        </tr>
                                                        <tr>
                                                            <td><span>1. Please Indicate if you agree with the general assessment/ adverse remarks/ overall grading  made by the   Reporting Authority, and give your assessment.</span></td>
                                                        </tr>
                                                        <tr>
                                                            <td>
                                                                <c:out value="${reviewingdt.reviewingNote}"/>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td> <span>2. Overall Grading Given By Reviewing Authority  :</span>
                                                                <c:out value="${reviewingdt.reviewGrading}"/>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </c:if>
                                                <c:if test="${reviewingdt.isreviewingcompleted != 'Y'}">
                                                    <c:if test="${pardetail.parstatus == '7'}">

                                                    </c:if>
                                                </c:if>
                                                <%slno += 1;%>
                                            </c:forEach>
                                        </c:if>
                                    </td> 
                                </tr>
                            </table>
                        </div>

                        <div style="width:100%;overflow: auto;margin-top:5px;border:1px solid #5095ce;">
                            <div style="color:#000000;padding:5px;font-weight:bold;" align="left"><u>Remarks of Accepting Authority</u></div>
                                    <c:if test="${not empty pardetail.acceptingdata}">
                                        <%slno = 0;%>
                                        <c:forEach var="acceptingdt" items="${pardetail.acceptingdata}">
                                    <table border="0" cellpadding="5" cellspacing="0" width="100%">
                                        <tr style="height: 40px">
                                            <td width="70%">
                                                <span>
                                                    <c:if test="${acceptingdt.isacceptingcompleted == 'Y'}">
                                                        <table width="100%" border="0" cellpadding="5" cellspacing="0" style="font-size:13px;">
                                                            <tr>
                                                                <%if (slno == 0) {%>
                                                                <td style="font-weight:bold;text-decoration:underline;">
                                                                    <c:out value="${acceptingdt.acceptingauthName}"/>
                                                                </td>
                                                                <%} else {%>
                                                                <td style="border-top:1px solid black;font-weight:bold;text-decoration:underline;">
                                                                    <c:out value="${acceptingdt.acceptingauthName}"/>
                                                                </td>
                                                                <%}%>
                                                            </tr>
                                                            <tr>
                                                                <td>
                                                                    <c:out value="${acceptingdt.acceptingNote}"/>
                                                                </td>
                                                            </tr>
                                                        </table>

                                                    </c:if>
                                                    <c:if test="${acceptingdt.isacceptingcompleted == 'F'}">
                                                        <table width="100%" border="0" cellpadding="5" cellspacing="0" style="font-size:13px;">
                                                            <tr>
                                                                <%if (slno == 0) {%>
                                                                <td style="font-weight:bold;text-decoration:underline;">
                                                                    <c:out value="${acceptingdt.acceptingauthName}"/>
                                                                </td>
                                                                <%} else {%>
                                                                <td style="border-top:1px solid black;font-weight:bold;text-decoration:underline;">
                                                                    <c:out value="${acceptingdt.acceptingauthName}"/>
                                                                </td>
                                                                <%}%>
                                                            </tr>
                                                            <tr>
                                                                <td>
                                                                    No Remarks Given by the Authority.
                                                                </td>
                                                            </tr>
                                                        </table>
                                                    </c:if>
                                                    <c:if test="${acceptingdt.isacceptingcompleted != 'Y'}">

                                                    </c:if>
                                                </span>
                                            </td>
                                        </tr>
                                    </table>
                                    <%slno += 1;%>
                                </c:forEach>
                            </c:if>
                        </div>
                    </c:if>
                </div>
            </div>
        </div>
    </body>
</html>
