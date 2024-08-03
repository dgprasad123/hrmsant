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
                <table border="0" width="99%" cellpadding="0" cellspacing="0" style="font-size:12px; font-family:verdana;">
                    <tr>
                        <td style="color:#000000;padding:0px;font-weight:bold;" align="center"><h2>Performance Appraisal Report (PAR) for Group 'A' & 'B' officers of Govt. of Odisha</h2></td>
                    </tr>                        
                </table>
            </div>
        </div>
        <div style="width:100%;overflow: auto;margin-top:5px;border:1px">
            <div align="center">
                <div style="width:99%;">                        
                    <div style="width:100%;overflow: auto;margin-top:1px;border:1px solid #5095ce;">
                        <div style="color:#000000;padding:5px;font-weight:bold;" align="left">Details of Transmission / Movement of PAR</div>
                        <table border="0" cellpadding="5" cellspacing="0" width="100%">
                            <tr style="height: 40px">                               
                                <td align="center" valign="top" width="10%"> 1. </td>
                                <td width="20%" valign="top">Reporting Authority</td>
                                <td width="70%">
                                    <c:if test="${not empty pardetail.reportingauth}">
                                        <table border="0" cellpadding="0" cellspacing="0" width="100%">
                                            <c:forEach var="rptauth" items="${pardetail.reportingauth}">
                                                <%slno = slno + 1;%>
                                                <tr>
                                                    <td width="5%"><%=slno%>.</td>
                                                    <c:if test="${rptauth.isPendingReportingAuthority == 'Y'}">
                                                        <td width="95%" style="color:red;">
                                                            <span style="display: block;"><c:out value="${rptauth.authorityname}"/> (<c:out value="${rptauth.authorityspn}"/>)</span>
                                                            (<span style="font-size:10.5px;">From:<c:out value="${rptauth.fromdt}"/> To:<c:out value="${rptauth.todt}"/></span>)
                                                            &nbsp;&nbsp;&nbsp;&nbsp;<span style="color:black;">(Pending at this end)</span>      
                                                        </td>
                                                    </c:if>
                                                    <c:if test="${rptauth.isPendingReportingAuthority != 'Y'}">
                                                        <td width="95%">
                                                            <span style="display: block;"><c:out value="${rptauth.authorityname}"/> (<c:out value="${rptauth.authorityspn}"/>)</span>
                                                            (<span style="font-size:10.5px;">From:<c:out value="${rptauth.fromdt}"/> To:<c:out value="${rptauth.todt}"/></span>)
                                                        </td>
                                                    </c:if>
                                                </tr>
                                            </c:forEach>
                                        </table>
                                    </c:if>
                                </td>
                            </tr>
                            <%slno = 0;%>
                            <tr style="height: 40px">                               
                                <td align="center" valign="top" width="10%"> 2. </td>
                                <td width="20%" valign="top">Reviewing Authority</td>
                                <td width="70%">
                                    <c:if test="${not empty pardetail.reviewingauth}">
                                        <table border="0" cellpadding="0" cellspacing="0" width="100%">
                                            <c:forEach var="rptauth" items="${pardetail.reviewingauth}">
                                                <%slno = slno + 1;%>
                                                <tr>
                                                    <td width="5%"><%=slno%>.</td>
                                                    <c:if test="${rptauth.isPendingReviewingAuthority == 'Y'}">
                                                        <td width="95%" style="color:red;">
                                                            <span style="display: block;"><c:out value="${rptauth.authorityname}"/>(<c:out value="${rptauth.authorityspn}"/>)</span>
                                                            (<span style="font-size:10.5px;">From:<c:out value="${rptauth.fromdt}"/> To:<c:out value="${rptauth.todt}"/></span>
                                                            &nbsp;&nbsp;&nbsp;&nbsp;<span style="color:black;">(Pending at this end)</span>
                                                        </td>
                                                    </c:if>
                                                    <c:if test="${rptauth.isPendingReviewingAuthority != 'Y'}">
                                                        <td width="95%">
                                                            <span style="display: block;"><c:out value="${rptauth.authorityname}"/>(<c:out value="${rptauth.authorityspn}"/>)</span>
                                                            (<span style="font-size:10.5px;">From:<c:out value="${rptauth.fromdt}"/> To:<c:out value="${rptauth.todt}"/></span>
                                                        </td>
                                                    </c:if>
                                                </tr>
                                            </c:forEach>
                                        </table>
                                    </c:if>
                                </td>
                            </tr>
                            <%slno = 0;%>
                            <tr style="height: 40px">
                                <td align="center" valign="top" width="10%"> 3. </td>
                                <td width="20%" valign="top">Accepting Authority</td>
                                <td width="70%">
                                    <c:if test="${not empty pardetail.acceptingauth}">
                                        <table border="0" cellpadding="0" cellspacing="0" width="100%">
                                            <c:forEach var="rptauth" items="${pardetail.acceptingauth}">
                                                <%slno = slno + 1;%>
                                                <tr>
                                                    <td width="5%"><%=slno%>.</td>
                                                    <c:if test="${rptauth.isPendingAcceptingAuthority == 'Y'}">
                                                        <td width="95%" style="color:red;">
                                                            <span style="display: block;"><c:out value="${rptauth.authorityname}"/>(<c:out value="${rptauth.authorityspn}"/></span>
                                                            (<span style="font-size:10.5px;">From:<c:out value="${rptauth.fromdt}"/> To:<c:out value="${rptauth.todt}"/></span>)
                                                            &nbsp;&nbsp;&nbsp;&nbsp;<span style="color:black;">(Pending at this end)</span>
                                                        </td>
                                                    </c:if>
                                                    <c:if test="${rptauth.isPendingAcceptingAuthority != 'Y'}">
                                                        <td width="95%">
                                                            <span style="display: block;"><c:out value="${rptauth.authorityname}"/>(<c:out value="${rptauth.authorityspn}"/></span>
                                                            (<span style="font-size:10.5px;">From:<c:out value="${rptauth.fromdt}"/> To:<c:out value="${rptauth.todt}"/></span>)
                                                        </td>
                                                    </c:if>
                                                </tr>
                                            </c:forEach>
                                        </table>
                                    </c:if>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div style="width:100%;overflow: auto;margin-top:1px;border:1px solid #5095ce;">                                                        
                        <div style="color:#000000;padding:5px;font-weight:bold;" align="left">Personal Information</div>                            
                        <table border="0" cellpadding="5" cellspacing="0" width="100%">
                            <tr style="height: 40px">                               
                                <td align="center" width="10%"> 1. </td>
                                <td width="30%">Applicant</td>
                                <td width="60%" style="padding-left:20px;">
                                    <c:out value="${pardetail.applicant}"/>
                                </td> 
                                <td rowspan="9" valign="top">
                                    <img id="loginUserPhoto" style="border:1px solid #a3a183;padding:3px;" onerror="callNoImage()" alt="ProfileImage" src='displayprofilephoto.htm?empid=${pardetail.applicantempid}' width="100" height="100" />
                                </td>
                            </tr>
                            <tr style="height: 40px">                               
                                <td align="center" width="10%"> 2. </td>
                                <td width="20%">Fiscal Year</td>
                                <td width="70%" style="padding-left:20px;">
                                    <c:out value="${pardetail.fiscalYear}"/>
                                </td> 
                            </tr>
                            <tr style="height: 40px">                               
                                <td align="center" width="10%"> 3. </td>
                                <td width="20%">Appraisal Period .</td>
                                <td width="70%" style="padding-left:20px;">
                                    From : <c:out value="${pardetail.parPeriodFrom}"/> -  To: <c:out value="${pardetail.parPeriodTo}"/>
                                </td>
                            </tr>
                            <tr style="height: 40px">                               
                                <td align="center" width="10%"> 4. </td>
                                <td width="20%">Date of Birth .</td>
                                <td width="70%" style="padding-left:20px;"><c:out value="${pardetail.dob}"/></td>
                            </tr>
                            <tr style="height: 40px">                               
                                <td align="center" width="10%"> 5. </td>
                                <td width="20%">Service to which the officer belongs .</td>
                                <td width="70%" style="padding-left:20px;"><c:out value="${pardetail.empService}"/></td>
                            </tr>
                            <tr style="height: 40px">                               
                                <td align="center" width="10%"> 6. </td>
                                <td width="20%">Group to which the officer belongs .</td>
                                <td width="70%" style="padding-left:20px;"><c:out value="${pardetail.empGroup}"/></td>
                            </tr>
                            <tr style="height: 40px">                               
                                <td align="center" width="10%"> 7. </td>
                                <td width="20%">Designation during the period of report .</td>
                                <td width="70%" style="padding-left:20px;"><c:out value="${pardetail.apprisespn}"/></td>
                            </tr>
                            <tr style="height: 40px">                               
                                <td align="center" width="10%"> 8. </td>
                                <td width="20%">Office to where posted .</td>
                                <td width="70%" style="padding-left:20px;"><c:out value="${pardetail.empOffice}"/></td>                                         
                            </tr>
                            <tr style="height: 40px">                               
                                <td align="center" width="10%"> 9. </td>
                                <td width="20%">Head Quarter(if any) .</td>
                                <td width="70%" style="padding-left:20px;"><c:out value="${pardetail.sltHeadQuarter}"/></td>
                            </tr>
                        </table>
                    </div>
                    <div style="width:100%;overflow: auto;margin-top:5px;border:1px solid #5095ce;">
                        <div style="color:#000000;padding:5px;font-weight:bold;" align="left">Absentee Statement</div>                        
                        <table border="0" cellpadding="5" cellspacing="0" width="100%">             
                            <tr>
                                <th align="center" width="15%"><b>From Date</b></th>
                                <th align="center" width="15%"><b>To Date</b></th>
                                <th align="center" width="15%"><b>Leave/ Training</b></th>
                                <th align="center" width="15%"><b>Type of Leave</b></th>
                            </tr>  
                        </table>

                        <c:if test="${not empty pardetail.leaveAbsentee}">
                            <table border="0" cellpadding="5" cellspacing="0" width="100%">
                                <c:forEach var="AbsenteeBean" items="${pardetail.leaveAbsentee}">
                                    <tr height="40px">
                                        <td width="15%" align="center"><c:out value="${AbsenteeBean.fromDate}"/></td>
                                        <td width="15%" align="center"><c:out value="${AbsenteeBean.toDate}"/></td>
                                        <c:if test="${AbsenteeBean.absenceCause == 'L'}">
                                            <td width="15%" align="center">Leave</td>
                                        </c:if>
                                        <c:if test="${AbsenteeBean.absenceCause == 'T'}">
                                            <td width="15%" align="center">Training</td>
                                        </c:if>
                                        <td width="15%" align="center"><c:out value="${AbsenteeBean.leaveType}"/></td>
                                    </tr>
                                </c:forEach>
                            </table>
                        </c:if>

                    </div>
                    <div style="width:100%;overflow: auto;margin-top:5px;border:1px solid #5095ce;">
                        <div style="color:#000000;padding:5px;font-weight:bold;" align="left">Achievements</div>                            
                        <table border="0" cellpadding="5" cellspacing="0" width="100%">             
                            <tr>
                                <th align="center" width="5%"><b>SL No</b></th>
                                <th align="center" width="15%"><b>Task</b></th>
                                <th align="center" width="15%"><b>Target</b></th>
                                <th align="center" width="15%"><b>Achievement</b></th>
                                <th align="center" width="10%"><b>Achievement(%)</b></th>
                                <th align="center" width="15%"><b>Attachment(if any)</b></th>
                            </tr>  
                        </table>

                        <c:if test="${not empty pardetail.fiscalYear}">
                            <c:set var="fyear" value="${pardetail.fiscalYear}"/>
                            <%
                                fiscalyear = (String) pageContext.getAttribute("fyear");
                            %>
                        </c:if>
                        <c:if test="${not empty pardetail.achivementList}">
                            <table border="0" cellpadding="5" cellspacing="0" width="100%">
                                <c:forEach var="AchievementBean" items="${pardetail.achivementList}">
                                    <c:if test="${not empty AchievementBean.attachmentId}">
                                        <c:set var="attid" value="${AchievementBean.attachmentId}"/>
                                        <%
                                            atchid = (String) pageContext.getAttribute("attid");
                                        %>
                                    </c:if>
                                    <%
                                        downloadlink = "DownloadAchievementAttachment.htm?attId=" + atchid + "&fiscalyr=" + fiscalyear;
                                    %>
                                    <tr height="40px">
                                        <td width="5%" align="center"><c:out value="${AchievementBean.slno}"/></td>
                                        <td width="15%" align="center"><c:out value="${AchievementBean.task}"/></td>
                                        <td width="15%" align="center"><c:out value="${AchievementBean.target}"/></td>
                                        <td width="15%" align="center"><c:out value="${AchievementBean.achievement}"/></td>
                                        <td width="10%" align="center"><c:out value="${AchievementBean.percentAchievement}"/></td>
                                        <td width="15%" align="center">&nbsp;</td>
                                    </tr>
                                </c:forEach>
                            </table>
                        </c:if>
                    </div>
                    <div style="width:100%;overflow: auto;margin-top:5px;border:1px solid #5095ce;">
                        <div style="color:#000000;padding:5px;font-weight:bold;" align="left">Other Details</div>
                        <table border="0" cellpadding="5" cellspacing="0" width="100%">                                         
                            <tr style="height: 40px">                               
                                <td align="center" width="10%"> 1. </td>
                                <td width="30%">Brief description of duties/tasks entrusted.(in about 100 words)</td>
                                <td width="60%">
                                    <c:out value="${pardetail.selfappraisal}" escapeXml="false"/>
                                </td>
                            </tr>
                            <tr style="height: 40px">                               
                                <td align="center"> 2. </td>
                                <td>Significant work, if any, done</td>
                                <td>
                                    <c:out value="${pardetail.specialcontribution}" escapeXml="false"/>
                                </td> 
                            </tr>
                            <tr style="height: 40px">                               
                                <td align="center"> 3. </td>
                                <td>Hinderance</td>
                                <td>
                                    <c:out value="${pardetail.reason}" escapeXml="false"/>
                                </td>
                            </tr>
                            <tr style="height: 40px">                               
                                <td align="center"> 4. </td>
                                <td>Place</td>
                                <td>
                                    <c:out value="${pardetail.place}"/>
                                </td> 
                            </tr>
                        </table>
                    </div>

                    <c:if test="${pardetail.ishideremark != null && pardetail.ishideremark == 'N'}">
                        <div style="width:100%;overflow: auto;margin-top:5px;border:1px solid #5F9B24;">
                            <div style="color:#000000;padding:5px;font-weight:bold;" align="left">Remarks of Reporting Authority</div>
                            <table border="0" cellpadding="5" cellspacing="0" width="100%">
                                <tr style="height: 40px">
                                    <td width="70%">
                                        <c:if test="${not empty pardetail.reportingdata}">
                                            <%slno = 0;%>
                                            <c:forEach var="reportingdt" items="${pardetail.reportingdata}">
                                                <c:if test="${reportingdt.isreportingcompleted == 'F'}">
                                                    <table>
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
                                                    <table>
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
                                                            <td><span>1. Assessment of work output, attributes & functional competencies.</span>(This should be on a relative scale of 1-5, with 1 referring to the lowest level & 5   to the highest level. Please indicate your rating for the officer against each item.)</td>
                                                        </tr>
                                                        <tr>
                                                            <td>
                                                                <table width="60%" style="margin-left:35px;">
                                                                    <tr>
                                                                        <th width="15%">Description</th>
                                                                        <th width="15%">Rating</th>
                                                                        <th width="15%">Description</th>
                                                                        <th width="15%">Rating</th>
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
                                                    <c:if test="${reportingdt.iscurrentreporting == 'Y'}">

                                                        <table>
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
                                                                <td><span>1. Assessment of work output, attributes & functional competencies.</span>(This should be on a relative scale of 1-5, with 1 referring to the lowest level & 5   to the highest level. Please indicate your rating for the officer against each item.) </td>
                                                            </tr>
                                                            <tr>
                                                                <td>
                                                                    <table width="60%" style="margin-left:35px;">
                                                                        <tr>
                                                                            <th width="15%">Description</th>
                                                                            <th width="15%">Rating</th>
                                                                            <th width="15%">Description</th>
                                                                            <th width="15%">Rating</th>
                                                                        </tr>
                                                                        <tr>
                                                                            <td>(a)  Attitude to work    :</td>
                                                                            <td><input type="text" name="ratingattitude" id="ratingattitude" value="${reportingdt.ratingattitude}" maxlength="1" onkeypress="return onlyIntegerRange(event)"/></td>
                                                                            <td>(f) Co-ordination ability:</td>
                                                                            <td><input type="text" name="ratingcoordination" id="ratingcoordination" value="${reportingdt.ratingcoordination}" maxlength="1" onkeypress="return onlyIntegerRange(event)"/></td>
                                                                        </tr>
                                                                        <tr>
                                                                            <td>(b)  Sense of responsibility:    </td>
                                                                            <td><input type="text" name="ratingresponsibility" id="ratingresponsibility" value="${reportingdt.ratingresponsibility}" maxlength="1" onkeypress="return onlyIntegerRange(event)"/></td>
                                                                            <td>(g) Ability to work in a team:</td>
                                                                            <td><input type="text" name="teamworkrating" id="teamworkrating" value="${reportingdt.teamworkrating}" maxlength="1" onkeypress="return onlyIntegerRange(event)"/></td>
                                                                        </tr>
                                                                        <tr>
                                                                            <td>(c)  Communication skill :  </td>
                                                                            <td><input type="text" name="ratingcomskill" id="ratingcomskill" value="${reportingdt.ratingcomskill}" maxlength="1" onkeypress="return onlyIntegerRange(event)"/></td>
                                                                            <td>(h) Knowledge of Rules/Procedures/ IT  Skills/ Relevant Subject :</td>
                                                                            <td><input type="text" name="ratingitskill" id="ratingitskill" value="${reportingdt.ratingitskill}" maxlength="1" onkeypress="return onlyIntegerRange(event)"/></td>
                                                                        </tr>
                                                                        <tr>
                                                                            <td>(d)  Leadership Qualities :  </td>
                                                                            <td><input type="text" name="ratingleadership" id="ratingleadership" value="${reportingdt.ratingleadership}" maxlength="1" onkeypress="return onlyIntegerRange(event)"/></td>
                                                                            <td>(i) Initiative :</td>
                                                                            <td><input type="text" name="ratinginitiative" id="ratinginitiative" value="${reportingdt.ratinginitiative}" maxlength="1" onkeypress="return onlyIntegerRange(event)"/></td>
                                                                        </tr>
                                                                        <tr>
                                                                            <td>(e) Decision-making ability :  </td>
                                                                            <td><input type="text" name="ratingdecisionmaking" id="ratingdecisionmaking" value="${reportingdt.ratingdecisionmaking}" maxlength="1" onkeypress="return onlyIntegerRange(event)"/></td>
                                                                            <td>(j) Quality of Work :</td>
                                                                            <td><input type="text" name="ratequalityofwork" id="ratequalityofwork" value="${reportingdt.ratequalityofwork}" maxlength="1" onkeypress="return onlyIntegerRange(event)"/></td>
                                                                        </tr>
                                                                    </table>
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td><span>2. General Assessment </span>(Please give an overall assessment of the officer including   his/her   attitude towards  S.T/S.C/Weaker Sections &  relation  with public):</td>
                                                            </tr>
                                                            <tr>
                                                                <td><textarea name="authNote" class="textareacolor" rows="4" id="authNote" style="width:80%;height:60px;text-align:left;">${reportingdt.authNote}</textarea></td>
                                                            </tr>
                                                            <tr>
                                                                <td>3. Inadequacies, deficiencies or shortcomings, if any (Remarks to be treated as adverse ):</td>
                                                            </tr>
                                                            <tr>
                                                                <td><textarea name="inadequaciesNote" class="textareacolor" rows="4" id="inadequaciesNote" style="width:80%;height:60px;text-align:left;">${reportingdt.inadequaciesNote}</textarea></td>
                                                            </tr>  
                                                            <tr>
                                                                <td>4. Integrity (If integrity is doubtful or  adverse please write “Not certified” in the space below and justify your remarks in box 4 above):</td>
                                                            </tr>
                                                            <tr>
                                                                <td><textarea name="integrityNote"  class="textareacolor" rows="4" id="inadequaciesNote" style="width:80%;height:60px;text-align:left;">${reportingdt.integrityNote}</textarea></td>
                                                            </tr> 
                                                            <tr>
                                                                <td> 5. Overall Grading :
                                                                    <input type="hidden" id="hidsltGrading" value="${reportingdt.sltGrading}"/>
                                                                    <input name="sltGrading" id="sltGrading" class="easyui-combobox" style="width:20%" data-options="valueField:'value',textField:'label',url:'GetPARGradeListJSON.htm'"/>
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td>6. For  Overall Grading  “Below Average” /  “Outstanding”  please provide justification in the   space below.:</td>
                                                            </tr>
                                                            <tr>
                                                                <td><textarea name="gradingNote" class="textareacolor" rows="4" id="gradingNote" style="width:80%;height:60px;text-align:left;">${reportingdt.gradingNote}</textarea></td>
                                                            </tr> 
                                                        </table>
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
                            <div style="background-color:#5F9B24;color:#FFFFFF;padding:5px;font-weight:bold;" align="left">Remarks of Reviewing Authority</div>
                            <table border="0" cellpadding="5" cellspacing="0" width="100%" class="tableview">                                        
                                <tr style="height: 40px">
                                    <td width="70%">
                                        <c:if test="${not empty pardetail.reviewingdata}">
                                            <%slno = 0;%>
                                            <c:forEach var="reviewingdt" items="${pardetail.reviewingdata}">
                                                <c:if test="${reviewingdt.isreviewingcompleted == 'F'}">
                                                    <table>
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
                                                    <table border="0" cellpadding="5" cellspacing="0" width="100%" class="tableview">
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
                                                        <c:if test="${reviewingdt.iscurrentreviewing == 'Y'}">
                                                            <table border="0" cellpadding="5" cellspacing="0" width="100%" class="tableview">
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
                                                                    <td><textarea name="reviewingNote" class="textareacolor" rows="4" id="reviewingNote" style="width:80%;height:60px;text-align:left;">${reviewingdt.reviewingNote}</textarea></td>
                                                                </tr>
                                                                <tr>
                                                                    <td> <span>2. Overall Grading Given By Reviewing Authority  :</span>
                                                                        <input type="hidden" id="hidsltReviewGrading" value="${reviewingdt.sltReviewGrading}"/>
                                                                        <input name="sltReviewGrading" id="sltReviewGrading" class="easyui-combobox" style="width:20%" data-options="valueField:'value',textField:'label',url:'GetPARGradeListJSON.htm'"/>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </c:if>
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
                            <div style="background-color:#5F9B24;color:#FFFFFF;padding:5px;font-weight:bold;" align="left">Remarks of Accepting Authority</div>
                            <c:if test="${not empty pardetail.acceptingdata}">
                                <%slno = 0;%>
                                <c:forEach var="acceptingdt" items="${pardetail.acceptingdata}">
                                    <table border="0" cellpadding="5" cellspacing="0" width="100%" class="tableview">
                                        <tr style="height: 40px">
                                            <td width="70%">
                                                <span>
                                                    <c:if test="${acceptingdt.isacceptingcompleted == 'Y'}">
                                                        <table width="100%">
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
                                                        </table>
                                                        <div style="padding:5px;"><c:out value="${acceptingdt.acceptingNote}"/></div>
                                                    </c:if>
                                                    <c:if test="${acceptingdt.isacceptingcompleted == 'F'}">
                                                        <table>
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
                                                        <c:if test="${acceptingdt.iscurrentaccepting == 'Y'}">
                                                            <table width="100%">
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
                                                            </table>
                                                            <c:if test="${pardetail.parstatus == '8'}">
                                                                <textarea name="acceptingNote" class="textareacolor" rows="4" id="acceptingNote" style="width:80%;height:60px;text-align:left;">${acceptingdt.acceptingNote}</textarea>
                                                            </c:if>
                                                        </c:if>
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
