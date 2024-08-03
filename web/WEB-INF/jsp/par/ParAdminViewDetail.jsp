<%-- 
    Document   : ParAdminViewDetail
    Created on : Jul 2, 2019, 12:40:08 PM
    Author     : manisha
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"  autoFlush="true" buffer="64kb"%>
<!DOCTYPE html>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<html>
    <head>
        <title>::Performance Appraisal::</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css"/>
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css"/>
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="css/sb-admin.css" rel="stylesheet">                        
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <script src="js/jquery.min.js"></script> 
        <script src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
        <script language="javascript"  src="js/basicjavascript.js " type="text/javascript" ></script>
        <script language="javascript" type="text/javascript" >
            $(document).ready(function() {
                if ($('#hidsltAcceptingGrading').val() > 0) {
                    $('#sltAcceptingGrading').combobox('setValue', $('#hidsltAcceptingGrading').val());
                }
            })

            function onlyIntegerRange(e)
            {
                var browser = navigator.appName;
                if (browser == "Netscape") {
                    var keycode = e.which;
                    if ((keycode >= 48 && keycode <= 53) || keycode == 8 || keycode == 0)
                        return true;
                    else
                        return false;
                } else {
                    if ((e.keyCode >= 48 && e.keyCode <= 53) || e.keycode == 8 || e.keycode == 0)
                        e.returnValue = true;
                    else
                        e.returnValue = false;
                }
            }
            function savecheck() {
                if ($('#sltAdminRemark').val() == "") {
                    alert("Please select Remarks");
                    return false;
                }
            }
            function setDivHeight(divId) {
                var containertbl = document.getElementById(divId);
                if ((screen.width == 1024) && (screen.height == 768))
                {
                    containertbl.style.height = "562px";
                }
                else if ((screen.width == 800) && (screen.height == 600))
                {
                    containertbl.style.height = "562px";
                }
                else if ((screen.width == 1280) && (screen.height == 1024))
                {
                    containertbl.style.height = "565px";
                } else {
                    containertbl.style.height = "565px";
                }

            }

            function forceforward() {
                if (confirm("Are you sure to Force Forward this PAR?")) {
                    return true;
                } else {
                    return false;
                }
            }

            function openReviewAdverseWindow() {
                if ($('#sltAcceptingGrading1').val() == "") {
                    alert("Please Choose Accepting Remark");
                }
                $('#setreview').modal('show');

            }
            function saveAcceptingRemarks(me, parId, pactid) {
                var url = "saveAcceptingRemarksForReview.htm";
                var sltObj = $(me).prev();
                var sltLabel = $(sltObj).children(':selected').text();
                var sltval = $(me).prev().val();


                var spnObj = $(me).prev().prev();
                $.post(url, {parId: parId, pactid: pactid, sltAcceptingGrading: sltval})
                        .done(function(data) {
                            console.log($(me).html());
                            //$(me).parent().html("<span>saved</span>");
                            $(spnObj).html(sltLabel);
                            $(me).hide();
                            $(sltObj).hide();
                        });

            }

        </script>
        <style type="text/css">
            .btn1 {
                -webkit-border-radius: 0;
                -moz-border-radius: 0;
                border-radius: 0px;
                color: #262126;
                font-size: 16px;
                background: #ced5d9;
                padding: 1px 6px 1px 6px;
                border: solid #9ccae6 1px;
                text-decoration: none;
            }

            .btn1:hover {
                text-decoration: none;
            }
        </style>
    </head>
    <c:set var = "slno" value = "0"/>
    <body>
        <form:form action="getviewPARAdmindetail.htm" commandName="parApplyForm">
            <form:hidden path="ishideremark"/>
            <form:hidden path="loginId"/> 
            <form:hidden path="adminRemark"/>
            <div align="center" style="margin-top:5px;margin-bottom:10px;">
                <div align="center">
                    <table border="0" width="99%" cellpadding="0"  cellspacing="0" style="font-size:12px; font-family:verdana;">
                        <tr>
                            <td style="background-color:#5095ce;color:#FFFFFF;padding:0px;font-weight:bold;" align="center"><h2>Performance Appraisal Report (PAR) for Group ‘A’ & ‘B’ officers of Govt. of Odisha</h2></td>
                        </tr>                        
                    </table>
                </div>
            </div>
            <div  id="tbl-container" style="width:100%;overflow: auto;margin-top:5px;border:1px ">
                <div  align="center">
                    <div  style="width:99%;">                        
                        <div   style="width:100%;overflow: auto;margin-top:1px;border:1px solid #5095ce;">
                            <div style="background-color:#5095ce;color:#FFFFFF;padding:5px;font-weight:bold;" align="left">Details of Transmission / Movement of PAR</div>
                            <table border="0"  cellpadding="5" cellspacing="0" width="100%" class="tableview">
                                <tr style="height: 40px">                               
                                    <td align="center" valign="top" width="10%"> 1. </td>
                                    <td  width="20%" valign="top">Reporting Authority</td>
                                    <td width="70%">
                                        <table border="0" cellpadding="0" cellspacing="0" width="100%">
                                            <c:forEach items="${reporting}" var="reporting">
                                                <c:set var = "slno" value = "${slno+1}"/>                                                
                                                <tr>
                                                    <td width="5%">${slno}</td>
                                                    <c:if test="${reporting.isPendingReportingAuthority eq 'Y'}">     
                                                        <td width="95%" style="color:red;">
                                                            ${reporting.authorityname}(
                                                            (<span style="font-size:10.5px;font-weight: bold;">From: ${reporting.fromdt}} To:${reporting.todt}}</span>)
                                                            &nbsp;&nbsp;&nbsp;&nbsp;<span style="color:black;">(Pending at this end)</span>
                                                        </td>
                                                    </c:if>
                                                    <c:if test="${reporting.isPendingReportingAuthority ne 'Y'}">                                                        
                                                        <td width="95%">
                                                            ${reporting.authorityname} , ${reporting.authorityspn}
                                                            (<span style="font-size:10.5px;font-weight: bold;">From:${reporting.fromdt} To:${reporting.todt}</span>)
                                                        </td>
                                                    </c:if>
                                                </tr>
                                            </c:forEach>
                                        </table>
                                    </td>
                                </tr>

                                <tr style="height: 40px">                               
                                    <td align="center" valign="top" width="10%"> 2. </td>
                                    <td  width="20%" valign="top">Reviewing Authority</td>
                                    <td width="70%">
                                        <table border="0" cellpadding="0" cellspacing="0" width="100%">
                                            <c:set var = "slno" value = "0"/>
                                            <c:forEach items="${reviewing}" var="reviewingdt">
                                                <c:set var = "slno" value = "${slno+1}"/> 

                                                <tr>
                                                    <td width="5%">${slno}</td>
                                                    <c:if test="${reviewingdt.isPendingReviewingAuthority eq 'Y'}">    
                                                        <td width="95%" style="color:red;">
                                                            ${reviewingdt.authorityname} , ${reviewingdt.authorityspn}
                                                            (<span style="font-size:10.5px;font-weight: bold;">From:${reviewingdt.fromdt} To:${reviewingdt.todt}</span>)
                                                            &nbsp;&nbsp;&nbsp;&nbsp;<span style="color:black;">(Pending at this end)</span>
                                                        </td>
                                                    </c:if>
                                                    <c:if test="${reviewingdt.isPendingReviewingAuthority ne 'Y'}">    
                                                        <td width="95%">
                                                            ${reviewingdt.authorityname} , ${reviewingdt.authorityspn}
                                                            (<span style="font-size:10.5px;font-weight: bold;">From:${reviewingdt.fromdt} To:${reviewingdt.todt}</span>)
                                                        </td>
                                                    </c:if>
                                                </tr>
                                            </c:forEach>
                                        </table>
                                    </td>
                                </tr>

                                <tr style="height: 40px">
                                    <td align="center" valign="top" width="10%"> 3. </td>
                                    <td  width="20%" valign="top">Accepting Authority</td>
                                    <td width="70%">
                                        <table border="0" cellpadding="0" cellspacing="0" width="100%">
                                            <c:set var = "slno" value = "0"/>
                                            <c:forEach items="${accepting}" var="acceptingbean">    
                                                <c:set var = "slno" value = "${slno+1}"/>
                                                <tr>
                                                    <td width="5%">${slno}.</td>
                                                    <c:if test="${acceptingbean.isPendingAcceptingAuthority eq 'Y'}">   
                                                        <td width="95%" style="color:red;">
                                                            ${acceptingbean.authorityname} ${acceptingbean.authorityspn} 
                                                            (<span style="font-size:10.5px;font-weight: bold;">From: ${acceptingbean.fromdt} To:${acceptingbean.todt}</span>)
                                                            &nbsp;&nbsp;&nbsp;&nbsp;<span style="color:black;">(Pending at this end)</span>
                                                        </td>
                                                    </c:if>
                                                    <c:if test="${acceptingbean.isPendingAcceptingAuthority ne 'Y'}">  
                                                        <td width="95%">
                                                            ${acceptingbean.authorityname},  ${acceptingbean.authorityspn} 
                                                            (<span style="font-size:10.5px;font-weight: bold;">From:${acceptingbean.fromdt} To:${acceptingbean.todt}</span>)
                                                        </td>
                                                    </c:if>
                                                </tr>
                                            </c:forEach>
                                        </table>    
                                    </td>
                                </tr>
                            </table>
                        </div>
                        <div   style="width:100%;overflow: auto;margin-top:1px;border:1px solid #5095ce;">                                                        
                            <div style="background-color:#5095ce;color:#FFFFFF;padding:5px;font-weight:bold;" align="left">Personal Information</div>                            
                            <table border="0"  cellpadding="5" cellspacing="0" width="100%" class="tableview">
                                <tr style="height: 40px">                               
                                    <td align="center" width="10%"> 1. </td>
                                    <td  width="20%">Applicant</td>
                                    <td width="70%">
                                        <span>
                                            ${parApplyForm.empName}
                                        </span>
                                    </td> 
                                </tr>
                                <tr style="height: 40px">                               
                                    <td align="center" width="10%"> 2. </td>
                                    <td  width="20%">Fiscal Year</td>
                                    <td width="70%">                                        
                                        <span>
                                            ${parApplyForm.fiscalYear}
                                        </span>
                                    </td> 
                                </tr>
                                <tr style="height: 40px">                               
                                    <td align="center" width="10%"> 3. </td>
                                    <td  width="20%">Appraisal Period .</td>
                                    <td width="70%">
                                        <span>
                                            From :${parApplyForm.prdFrmDate}  -  To: ${parApplyForm.prdToDate}
                                        </span>
                                    </td>
                                </tr>
                                <tr style="height: 40px">                               
                                    <td align="center" width="10%"> 4. </td>
                                    <td  width="20%">Date of Birth .</td>
                                    <td width="70%"> <span> ${parApplyForm.dob}</span></td>                                         
                                </tr>
                                <tr style="height: 40px">                               
                                    <td align="center" width="10%"> 5. </td>
                                    <td  width="20%">Service to which the officer belongs .</td>
                                    <td width="70%"> <span>${parApplyForm.empService}</span></td>                                         
                                </tr>
                                <tr style="height: 40px">                               
                                    <td align="center" width="10%"> 6. </td>
                                    <td  width="20%">Group to which the officer belongs .</td>
                                    <td width="70%"> <span>${parApplyForm.empGroup}</span></td>                                         
                                </tr>
                                <tr style="height: 40px">                               
                                    <td align="center" width="10%"> 7. </td>
                                    <td  width="20%">Designation during the period of report .</td>
                                    <td width="70%"> <span>${parApplyForm.apprisespc}</span></td>                                         
                                </tr>
                                <tr style="height: 40px">                               
                                    <td align="center" width="10%"> 8. </td>
                                    <td  width="20%">Office to where posted .</td>
                                    <td width="70%"> <span>${parApplyForm.appriseOffice}</span></td>                                         
                                </tr>
                                <tr style="height: 40px">                               
                                    <td align="center" width="10%"> 9. </td>
                                    <td  width="20%">Head Quarter(if any) .</td>
                                    <td width="70%">&nbsp;</td>                                         
                                </tr>
                            </table>
                        </div>
                        <div  style="width:100%;overflow: auto;margin-top:5px;border:1px solid #5095ce;">
                            <div  style="background-color:#5095ce;color:#FFFFFF;padding:5px;font-weight:bold;" align="left">Absentee Statement</div>                        
                            <table border="0"  cellpadding="5" cellspacing="0" width="100%" class="tableview">             
                                <tr>
                                    <th align="center" width="15%"><b>From Date</b></th>
                                    <th align="center" width="15%"><b>To Date</b></th>
                                    <th align="center" width="15%"><b>Leave/ Training</b></th>
                                    <th align="center" width="15%"><b>Type of Leave</b></th>

                                </tr>  
                            </table>
                            <table border="0"  cellpadding="5" cellspacing="0" width="100%" class="tableview">  
                                <c:forEach items="${parApplyForm.leaveAbsentee}" var="absenteeDtl" varStatus="cnt">
                                    <tr style="height:40px">
                                        <td width="15%" align="center">${absenteeDtl.fromDate}</td>
                                        <td width="15%" align="center">${absenteeDtl.toDate}</td>
                                        <td width="15%" align="center">${absenteeDtl.absenceCause}</td>
                                        <td width="15%" align="center">${absenteeDtl.leaveType}</td>
                                    </tr>
                                </c:forEach>
                            </table>
                        </div>
                        <div style="width:100%;overflow: auto;margin-top:5px;border:1px solid #5095ce;">
                            <div style="background-color:#5095ce;color:#FFFFFF;padding:5px;font-weight:bold;" align="left">Achievements</div>                            
                            <table border="0"  cellpadding="5" cellspacing="0" width="100%" class="tableview">             
                                <tr>
                                    <th align="center" width="15%"><b>Task</b></th>
                                    <th align="center" width="15%"><b>Target</b></th>
                                    <th align="center" width="15%"><b>Achievement</b></th>
                                    <th align="center" width="15%"><b>Achievement(%)</b></th>
                                </tr>  
                            </table>
                            <table border="0"  cellpadding="5" cellspacing="0" width="100%" class="tableview"> 

                                <c:forEach items="${parApplyForm.achivementList}" var="achievementDtl" varStatus="cnt">

                                    <tr>
                                        <td width="15%" align="center">${achievementDtl.task}</td>
                                        <td width="15%" align="center">${achievementDtl.target}</td>
                                        <td width="15%" align="center">${achievementDtl.achievement}</td>
                                        <td width="15%" align="center">${achievementDtl.percentAchievement}</td>
                                    </tr>
                                </c:forEach>

                            </table>
                        </div>
                        <div style="width:100%;overflow: auto;margin-top:5px;border:1px solid #5095ce;">
                            <div style="background-color:#5095ce;color:#FFFFFF;padding:5px;font-weight:bold;" align="left">Other Details</div>
                            <table border="0"  cellpadding="5" cellspacing="0" width="100%" class="tableview">                                         
                                <tr style="height: 40px">                               
                                    <td align="center" width="10%"> 1. </td>
                                    <td  width="20%">Brief description of duties/tasks entrusted.(in about 100 words)</td>
                                    <td width="70%">
                                        <span>
                                            ${parApplyForm.selfappraisal}
                                        </span>
                                    </td> 
                                </tr>
                                <tr style="height: 40px">                               
                                    <td align="center" width="10%"> 2. </td>
                                    <td  width="20%">(i)Significant work, if any, done</td>
                                    <td width="70%">
                                        <span>
                                            ${parApplyForm.specialcontribution}

                                        </span>
                                    </td> 
                                </tr>

                                <tr style="height: 40px">                               
                                    <td align="center" width="10%">  </td>
                                    <td  width="20%">(ii) Work Done For Implementation of 5TS(Transparency,Teamwork,Technology,Transformation and Time):</td>
                                    <td width="70%">
                                        <span>
                                            ${parApplyForm.fiveTComponentappraise}
                                        </span>
                                    </td> 
                                </tr>. 
                                <tr style="height: 40px">                               
                                    <td align="center" width="10%"> 3. </td>
                                    <td  width="20%">Hindrance</td>
                                    <td width="70%">
                                        <span>
                                            ${parApplyForm.reason}
                                        </span>
                                    </td> 
                                </tr>
                                <%--   <tr style="height: 40px">                               
                                    <td align="center" width="10%"> 4. </td>
                                    <td  width="20%">(ii) Work Done For Implementation of 5TS(Transparency,Teamwork,Technology,Transformation and Time):</td>
                                    <td width="70%">
                                        <span>
                                            ${parApplyForm.fiveTComponent}
                                        </span>
                                    </td> 
                                </tr>. --%>
                                <tr style="height: 40px">                               
                                    <td align="center" width="10%"> 4. </td>
                                    <td  width="20%">Place</td>
                                    <td width="70%">
                                        <span>
                                            ${parApplyForm.place}
                                            &nbsp;&nbsp;&nbsp;&nbsp;
                                            Date of Submission:
                                            ${parApplyForm.submittedon}
                                        </span>
                                    </td> 
                                </tr>
                            </table>
                        </div>

                        <c:if test="${parApplyForm.ishideremark eq 'N'}">
                            <div style="width:100%;overflow: auto;margin-top:5px;border:1px solid #5F9B24;">
                                <div style="background-color:#5F9B24;color:#FFFFFF;padding:5px;font-weight:bold;" align="left">Remarks of Reporting Authority</div>
                                <table border="0"  cellpadding="5" cellspacing="0" width="100%" class="tableview">
                                    <tr style="height: 40px">
                                        <td width="70%">                                
                                            <c:set var = "slno" value = "${slno+1}"/>
                                            <c:forEach items="${parApplyForm.reportingdata}" var="reportingdt">

                                                <c:if test="${reportingdt.isreportingcompleted eq 'Y'}">                                    
                                                    <table>
                                                        <tr>
                                                            <c:choose>
                                                                <c:when test="${slno == 0}">                                                                    
                                                                    <td style="font-weight:bold;text-decoration:underline;">${reportingdt.reportingauthName}on d.t ${reportingdt.submittedon}</td>
                                                                </c:when>                                                                
                                                                <c:otherwise>
                                                                    <td style="border-top:1px solid black;font-weight:bold;text-decoration:underline;">${reportingdt.reportingauthName} on d.t ${reportingdt.submittedon}</td>                                                                   
                                                                </c:otherwise>
                                                            </c:choose>
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
                                                                        <td><div style="padding:5px;">${reportingdt.ratingattitude1}</div></td>
                                                                        <td>(f) Co-ordination ability:</td>
                                                                        <td><div style="padding:5px;">${reportingdt.ratingcoordination1}</div></td>

                                                                    </tr>
                                                                    <tr>
                                                                        <td>(b)  Sense of responsibility:    </td>
                                                                        <td><div style="padding:5px;">${reportingdt.ratingresponsibility1}</div></td>
                                                                        <td>(g) Ability to work in a team:</td>
                                                                        <td><div style="padding:5px;">${reportingdt.teamworkrating1}</div></td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td>(c)  Communication skill :  </td>
                                                                        <td><div style="padding:5px;">${reportingdt.ratingcomskill1}</div></td>
                                                                        <td>(h) Knowledge of Rules/Procedures/ IT  Skills/ Relevant Subject :</td>
                                                                        <td><div style="padding:5px;">${reportingdt.ratingitskill1}</div></td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td>(d)  Leadership Qualities :  </td>
                                                                        <td><div style="padding:5px;">${reportingdt.ratingleadership1}</div></td>
                                                                        <td>(i) Initiative :</td>
                                                                        <td><div style="padding:5px;">${reportingdt.ratinginitiative1}</div></td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td>(e) ) Decision-making ability :  </td>
                                                                        <td><div style="padding:5px;">${reportingdt.ratingdecisionmaking1}</div></td>
                                                                        <td>(j) ) Quality of Work :</td>
                                                                        <td><div style="padding:5px;">${reportingdt.ratequalityofwork1}</div></td>
                                                                    </tr>
                                                                </table>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td><span>2.(i) General Assessment </span>(Please give an overall assessment of the officer including   his/her   attitude towards  S.T/S.C/Weaker Sections &  relation  with public):</td>
                                                        </tr>
                                                        <tr>
                                                            <td><div style="padding:5px;">${reportingdt.authNote}</div></td>
                                                        </tr> 
                                                        <c:if test="${parApplyForm.fiscalYear ne '2014-15' and parApplyForm.fiscalYear ne '2015-16' and parApplyForm.fiscalYear ne '2016-17' and parApplyForm.fiscalYear ne '2017-18'}">

                                                            <tr>
                                                                <td><span>(ii) Assessment Of Performance Of 5t  </span>(out of 20%):</td>
                                                            </tr>
                                                            <tr>
                                                                <td><span>(a) 10% on 5T charter of Department:  </span></td>
                                                            </tr>
                                                            <tr>
                                                                <td><div style="padding:5px;">${reportingdt.fiveTChartertenpercent}</div></td>
                                                            </tr> 
                                                            <tr>
                                                                <td><span>(b) 5% on 5T charter of Government:  </span></td>
                                                            </tr>
                                                            <tr>
                                                                <td><div style="padding:5px;">${reportingdt.fiveTCharterfivePercent}</div></td>
                                                            </tr> 
                                                            <tr>
                                                                <td><span>(c)  5% on Mo Sarkar:  </span></td>
                                                            </tr>
                                                            <tr>
                                                                <td><div style="padding:5px;">${reportingdt.fiveTComponentmoSarkar}</div></td>
                                                            </tr> 
                                                        </c:if>
                                                        <tr>
                                                            <td><span>3. Inadequacies, deficiencies or shortcomings, if any (Remarks to be treated as adverse ):</span></td>
                                                        </tr>
                                                        <tr>
                                                            <td><div style="padding:5px;">${reportingdt.inadequaciesNote}</div></td>
                                                        </tr>  
                                                        <tr>
                                                            <td><span>4. Integrity (If integrity is doubtful or  adverse please write “Not certified” in the space below and justify your remarks in box 4 above):</span></td>
                                                        </tr>
                                                        <tr>
                                                            <td><div style="padding:5px;">${reportingdt.integrityNote}</div></td>
                                                        </tr>
                                                        <c:if test="${LoginUserBean.loginusertype eq 'G'}">
                                                            <tr>
                                                                <td><span> 5. Overall Grading : </span>
                                                                    <span style="padding:5px;">${reportingdt.sltGrading1}</span>
                                                                </td>
                                                            </tr>
                                                        </c:if>

                                                        <c:if test="${LoginUserBean.loginusertype eq 'A'}"> 
                                                            <tr>
                                                                <td><span> 5. Overall Grading : </span>
                                                                    <span style="color: red;font-size: 25px">*******</span>
                                                                </td>
                                                            </tr>
                                                        </c:if>
                                                        <tr>
                                                            <td><span>6. For  Overall Grading  “Below Average” /  “Outstanding”  please provide justification in the   space below.:</span></td>
                                                        </tr>
                                                        <tr>
                                                            ${isAuthRemarksClosed},${isPARClosed}
                                                            <c:if test="${LoginUserBean.loginusertype eq 'A'}">
                                                                <td>
                                                                    <div style="color: red;font-size: 25px">**********</div>
                                                                </td>
                                                            </c:if>
                                                            <c:if test="${LoginUserBean.loginusertype eq 'G'}">
                                                                <td>
                                                                    <div style="padding:5px;">
                                                                        ${reportingdt.gradingNote}
                                                                    </div>
                                                                </td>
                                                            </c:if>

                                                        </tr> 
                                                        <tr>
                                                            <td>7. For  Overall Grading  “Below Average” /  “Outstanding”  Document.:</td>
                                                        </tr>
                                                        <tr>
                                                            <c:if test="${LoginUserBean.loginusertype eq 'A'}"> 
                                                                <td style="padding-left: 20px;"><span style="color:red"><b> Document:</b></span>
                                                                    <span style="color: red;font-size: 25px">*******</span>
                                                                </td>
                                                            </c:if>


                                                        </tr>
                                                    </table>                                                    
                                                </c:if>
                                                <c:if test="${reportingdt.isreportingcompleted eq 'F'}"> 
                                                    <div style="width:100%;overflow: auto;margin-top:5px;border:1px solid #5F9B24;">                                                        
                                                        <table border="0"  cellpadding="5" cellspacing="0" width="100%" class="tableview">
                                                            <tr style="height: 40px">
                                                                <td width="70%">                                
                                                                    <c:set var = "slno" value = "${slno+1}"/>
                                                                    <table>
                                                                        <tr>

                                                                            <td>
                                                                                <div style="font-weight:bold;text-decoration:underline;">${reportingdt.reportingauthName}on d.t ${reportingdt.submittedon}</div>
                                                                                <div style="color: red;"><b>PAR is Force Forwarded On Date: ${reportingdt.submittedon}</b></div>
                                                                            </td>


                                                                        </tr>

                                                                    </table>
                                                                </td>
                                                            </tr>
                                                        </table>
                                                    </div>
                                                </c:if>
                                                <c:if test="${reportingdt.isreportingcompleted eq 'N'}">                                    
                                                    <table>
                                                        <tr>

                                                            <c:choose>
                                                                <c:when test = "${slno == 0}">
                                                                    <td style="font-weight:bold;text-decoration:underline;">${reportingdt.reportingauthName}</td>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <td style="border-top:1px solid black;font-weight:bold;text-decoration:underline;">${reportingdt.reportingauthName}</td>
                                                                </c:otherwise>
                                                            </c:choose>

                                                        </tr>
                                                    </table>
                                                    <c:if test="${reportingdt.iscurrentreporting eq 'Y'}">  
                                                        <c:if test="${reportingdt.hasadminPriv ne 'Y'}">                                                    
                                                            <table>
                                                                <tr>
                                                                    <c:choose>
                                                                        <c:when test = "${slno == 0}">
                                                                            <td style="font-weight:bold;text-decoration:underline;">
                                                                                ${reportingdt.reportingauthName} 
                                                                            </td>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <td style="border-top:1px solid black;font-weight:bold;text-decoration:underline;">
                                                                                ${reportingdt.reportingauthName}
                                                                            </td>
                                                                        </c:otherwise>
                                                                    </c:choose>
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
                                                                            <c:forEach items="${parApplyForm.reportingdata}" var="reportingbean" varStatus="cnt">
                                                                                <tr>
                                                                                    <td>(a)  Attitude to work    :</td>
                                                                                    <td>${reportingdt.ratingattitude}</td>
                                                                                    <td>(f) Co-ordination ability:</td>
                                                                                    <td> ${reportingdt.ratingcoordination}</td>
                                                                                </tr>
                                                                            </c:forEach>
                                                                            <tr>
                                                                                <td>(b)  Sense of responsibility:    </td>
                                                                                <td>${reportingdt.ratingresponsibility1}</td>
                                                                                <td>(g) Ability to work in a team:</td>
                                                                                <td>${reportingdt.teamworkrating1}</td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td>(c)  Communication skill :  </td>
                                                                                <td>${reportingdt.ratingcomskill1}</td>
                                                                                <td>(h) Knowledge of Rules/Procedures/ IT  Skills/ Relevant Subject :</td>
                                                                                <td>${reportingdt.ratingitskill1}</td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td>(d)  Leadership Qualities :  </td>
                                                                                <td>${reportingdt.ratingleadership1}</td>
                                                                                <td>(i) Initiative :</td>
                                                                                <td>${reportingdt.ratinginitiative1}</td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td>(e) ) Decision-making ability :  </td>
                                                                                <td>${reportingdt.ratingdecisionmaking1}</td>
                                                                                <td>(j) ) Quality of Work :</td>
                                                                                <td>${reportingdt.ratequalityofwork1}</td>
                                                                            </tr>
                                                                        </table>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td><span>2. General Assessment </span>(Please give an overall assessment of the officer including   his/her   attitude towards  S.T/S.C/Weaker Sections &  relation  with public):</td>
                                                                </tr>
                                                                <tr>
                                                                    <td>${reportingdt.authNote}</td>
                                                                </tr>
                                                                <c:if test="${parApplyForm.fiscalYear eq '2019-20' or parApplyForm.fiscalYear gt '2019-20'}">
                                                                    <tr>
                                                                        <td><span>(ii) Assessment Of Performance Of 5t  </span>(20%):</td>
                                                                    </tr>

                                                                    <tr>
                                                                        <td><span>(a) 10% on 5T charter of Department:  </span></td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td><div style="padding:5px;">${reportingdt.fiveTChartertenpercent}</div></td>
                                                                    </tr> 
                                                                    <tr>
                                                                        <td><span>(b) 5% on 5T charter of Government:  </span></td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td><div style="padding:5px;">${reportingdt.fiveTCharterfivePercent}</div></td>
                                                                    </tr> 
                                                                    <tr>
                                                                        <td><span>(c)  5% on Mo Sarkar:  </span></td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td><div style="padding:5px;">${reportingdt.fiveTComponentmoSarkar}</div></td>
                                                                    </tr> 
                                                                </c:if>
                                                                <tr>
                                                                    <td>3. Inadequacies, deficiencies or shortcomings, if any (Remarks to be treated as adverse ):</td>
                                                                </tr>
                                                                <tr>
                                                                    <td>${reportingdt.inadequaciesNote}</td>
                                                                </tr>  
                                                                <tr>
                                                                    <td>4. Integrity (If integrity is doubtful or  adverse please write “Not certified” in the space below and justify your remarks in box 4 above):</td>
                                                                </tr>
                                                                <tr>
                                                                    <td>${reportingdt.integrityNote}</td>
                                                                </tr>
                                                                <c:if test="${LoginUserBean.loginusertype eq 'G'}"> 
                                                                    <tr>
                                                                        <td> 5. Overall Grading :
                                                                            ${reportingdt.sltGrading1}
                                                                        </td>
                                                                    </tr>
                                                                </c:if>

                                                                <c:if test="${LoginUserBean.loginusertype eq 'A'}"> 
                                                                    <tr>
                                                                        <td><span> 5. Overall Grading : </span>
                                                                            <span style="color: red;font-size: 25px">*******</span>
                                                                        </td>
                                                                    </tr>
                                                                </c:if>
                                                                <tr>
                                                                    <td>6. For  Overall Grading  “Below Average” /  “Outstanding”  please provide justification in the   space below.:</td>
                                                                </tr>
                                                                <tr>
                                                                    <c:if test="${LoginUserBean.loginusertype eq 'G'}">
                                                                        <td>${reportingdt.gradingNote}</td>
                                                                    </c:if>

                                                                    <c:if test="${LoginUserBean.loginusertype eq 'A'}"> 
                                                                    <span style="color: red;font-size: 25px">**********</span>
                                                                </c:if>
                                                    </tr>
                                                    <tr>
                                                        <td>7. For  Overall Grading  “Below Average” /  “Outstanding”  Document.:</td>
                                                    </tr>
                                                    <tr>
                                                        <td>&nbsp;</td>
                                                    </tr>
                                                    <tr>
                                                        <c:if test="${LoginUserBean.loginusertype eq 'A'}"> 
                                                            <td style="padding-left: 20px;"><span style="color:red"><b> Document:</b></span>
                                                                <span style="color: red;font-size: 25px">*******</span>
                                                            </td>
                                                        </c:if>
                                                        <c:if test="${LoginUserBean.loginusertype ne 'A'}"> 
                                                            <td style="padding-left: 20px;"><span style="color:red"><b> Document:</b></span>
                                                                <span style="color: red;font-size: 25px">*******</span>
                                                            </td>
                                                        </c:if>

                                                    </tr>
                                                </table>
                                            </c:if>
                                        </c:if>
                                    </c:if>


                                </c:forEach>
                                </td>
                                </tr>
                                </table>
                            </div>

                            <div style="width:100%;overflow: auto;margin-top:5px;border:1px solid #5095ce;">
                                <div style="background-color:#5F9B24;color:#FFFFFF;padding:5px;font-weight:bold;" align="left">Remarks of Reviewing Authority</div>
                                <table border="0"  cellpadding="5" cellspacing="0" width="100%" class="tableview">                                        
                                    <tr style="height: 40px">
                                        <td width="70%">
                                            <c:if test="${not empty parApplyForm.reviewingdata}">
                                                <c:set var = "slno" value = "${slno+1}"/>     

                                                <c:forEach items="${parApplyForm.reviewingdata}" var="reviewingbean">


                                                    <c:if test="${reviewingbean.isreviewingcompleted eq 'F'}"> 
                                                        <div style="width:100%;overflow: auto;margin-top:5px;border:1px solid #5F9B24;">                                                        
                                                            <table border="0"  cellpadding="5" cellspacing="0" width="100%" class="tableview">
                                                                <tr style="height: 40px">
                                                                    <td width="70%">                                
                                                                        <c:set var = "slno" value = "${slno+1}"/>
                                                                        <table>
                                                                            <tr>
                                                                                <td>
                                                                                    <div style="font-weight:bold;text-decoration:underline;">${reviewingbean.reviewingauthName} on d.t ${reviewingbean.submittedon}</div>
                                                                                    <div style="color: red;"><b>PAR is Force Forwarded On Date: ${reviewingbean.submittedon}</b></div>
                                                                                </td>
                                                                            </tr>

                                                                        </table>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </div>
                                                    </c:if>
                                                    <c:if test="${reviewingbean.isreviewingcompleted eq 'T'}"> 
                                                        <div style="width:100%;overflow: auto;margin-top:5px;border:1px solid #5F9B24;">                                                        
                                                            <table border="0"  cellpadding="5" cellspacing="0" width="100%" class="tableview">
                                                                <tr style="height: 40px">
                                                                    <td width="70%">                                
                                                                        <c:set var = "slno" value = "${slno+1}"/>
                                                                        <table>
                                                                            <tr>
                                                                                <td>
                                                                                    <div style="font-weight:bold;text-decoration:underline;">${reviewingbean.reviewingauthName}</div>
                                                                                    <div style="color: red;"><b>PAR is Force Forwarded On Date: ${reviewingbean.submittedon} Due to Office Of the Demmitted Authorities in the PAR Recording Change</b></div>
                                                                                </td>
                                                                            </tr>

                                                                        </table>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </div>
                                                    </c:if>
                                                    <c:if test="${reviewingbean.isreviewingcompleted eq 'Y'}"> 
                                                        <table border="0" cellpadding="5" cellspacing="0" width="100%" class="tableview">
                                                            <tr style="height: 40px">
                                                                <td width="70%">                                
                                                                    <c:set var = "slno" value = "${slno+1}"/>
                                                                    <table>
                                                                        <tr>
                                                                            <td>
                                                                                <div style="font-weight:bold;text-decoration:underline;">${reviewingbean.reviewingauthName} on d.t ${reviewingbean.submittedon}</div>
                                                                            </td>
                                                                        </tr>

                                                                    </table>
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td><span>1. Please Indicate if you agree with the general assessment/ adverse remarks/ overall grading  made by the   Reporting Authority, and give your assessment.</span></td>
                                                            </tr>

                                                            <tr>
                                                                <c:if test="${LoginUserBean.loginusertype eq 'A'}">
                                                                    <td>
                                                                        <span style="color: red;font-size: 25px">**********</span>
                                                                    </td>
                                                                </c:if>
                                                                <c:if test="${LoginUserBean.loginusertype eq 'G'}"> 
                                                                    <td>${reviewingbean.reviewingNote}</td>
                                                                </c:if>

                                                            </tr>
                                                            <c:if test="${LoginUserBean.loginusertype eq 'G'}"> 
                                                                <tr>
                                                                    <td> <span>2. Overall Grading Given By Reviewing Authority :</span>
                                                                        ${reviewingbean.sltReviewGrading1}
                                                                    </td>
                                                                </tr>
                                                            </c:if>

                                                            <c:if test="${LoginUserBean.loginusertype eq 'A'}"> 
                                                                <tr>
                                                                    <td><span> 2. Overall Grading Given By Reviewing Authority: </span>
                                                                        <span style="color: red;font-size: 25px">**********</span>
                                                                    </td>
                                                                </tr>
                                                            </c:if>
                                                            <tr>
                                                                <td style="padding-left: 10px;">3. For  Overall Grading  “Below Average” /  “Outstanding”  please upload Document if any.:</td>
                                                            </tr>
                                                            <tr>
                                                                <td>&nbsp;</td>
                                                            </tr>
                                                            <tr>
                                                                <c:if test="${LoginUserBean.loginusertype eq 'A'}"> 
                                                                    <td style="padding-left: 20px;"><span style="color:red"><b> Document:</b></span>
                                                                        <span style="color: red;font-size: 25px">*******</span>
                                                                    </td>
                                                                </c:if>

                                                            </tr>
                                                        </table>                                                
                                                    </c:if> 
                                                    <c:if test="${reviewingbean.isreviewingcompleted eq 'N'}"> 
                                                        <table border="0" cellpadding="5" cellspacing="0" width="100%" class="tableview">
                                                            <tr style="height: 40px">
                                                                <td width="70%">                                
                                                                    <c:set var = "slno" value = "${slno+1}"/>
                                                                    <table>
                                                                        <tr>
                                                                            <td>
                                                                                <div style="font-weight:bold;text-decoration:underline;">${reviewingbean.reviewingauthName} on d.t ${reviewingbean.submittedon}</div>
                                                                            </td>
                                                                        </tr>

                                                                    </table>
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td><span>1. Please Indicate if you agree with the general assessment/ adverse remarks/ overall grading  made by the   Reporting Authority, and give your assessment.</span></td>
                                                            </tr>
                                                            <tr>
                                                                <td></td>
                                                            </tr>
                                                            <tr>
                                                                <td> <span>2. Overall Grading Given By Reviewing Authority:</span>

                                                                </td>
                                                            </tr>
                                                        </table>                                                
                                                    </c:if>


                                                </c:forEach>
                                            </c:if>
                                        </td> 
                                    </tr>
                                </table>
                            </div>

                            <div style="width:100%;overflow: auto;margin-top:5px;border:1px solid #5095ce;">
                                <div style="background-color:#5F9B24;color:#FFFFFF;padding:5px;font-weight:bold;" align="left">Remarks of Accepting Authority</div>
                                <c:if test="${not empty parApplyForm.acceptingdata}">                            

                                    <c:forEach items="${parApplyForm.acceptingdata}" var="acceptingbean" varStatus="loop">                                
                                        <table border="0"  cellpadding="5" cellspacing="0" width="100%" class="tableview">
                                            <tr style="height: 40px">
                                                <td width="70%">

                                                    <c:if test="${acceptingbean.isacceptingcompleted eq 'Y'}">                                                        
                                                        <table width="100%">
                                                            <tr>
                                                                <c:choose>
                                                                    <c:when test="${slno == 0}">
                                                                        <td style="font-weight:bold;text-decoration:underline;">
                                                                            ${acceptingbean.acceptingauthName} on d.t ${acceptingbean.submittedon}
                                                                        </td>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <td style="border-top:1px solid black;font-weight:bold;text-decoration:underline;">
                                                                            ${acceptingbean.acceptingauthName} on d.t ${acceptingbean.submittedon}
                                                                        </td>
                                                                    </c:otherwise>
                                                                </c:choose>

                                                            </tr>

                                                            <tr>
                                                                <td> 
                                                                    <span>1.Overall Grading Given By Accepting Authority :</span>
                                                                    <c:if test="${LoginUserBean.loginusertype eq 'A'}"> 

                                                                        <span style="color: red;font-size: 25px">**********</span>
                                                                    </c:if>
                                                                    <c:if test="${LoginUserBean.loginusertype eq 'G'}"> 
                                                                        <span> ${acceptingbean.sltAcceptingGrading1}</span>
                                                                    </c:if>

                                                                    <c:if test="${LoginUserBean.loginusertype eq 'G' && users.hasparreviewingAuthorization == 'Y' && empty acceptingbean.sltAcceptingGrading1}">                                                                        
                                                                        <select name="sltAcceptingGrading">
                                                                            <option value="">Select</option>
                                                                            <c:forEach items="${gradelist}" var="grade">                                                                        
                                                                                <option value="${grade.value}">${grade.label}</option>
                                                                            </c:forEach>
                                                                        </select>

                                                                        <%--<input type="submit" name="action" value="Save" class="easyui-linkbutton" /> --%>
                                                                        <button type="button" onclick="saveAcceptingRemarks(this, '${parApplyForm.parId}', '${parApplyForm.pactid}')" class="btn btn-default">Save</button>
                                                                    </c:if>  
                                                                </td>
                                                            </tr>
                                                        </table>
                                                        <div style="padding:5px;">
                                                            <c:if test="${LoginUserBean.loginusertype eq 'G'}">
                                                                <span>2.Accepting Note :</span> ${acceptingbean.acceptingNote}
                                                            </c:if>

                                                            <c:if test="${LoginUserBean.loginusertype eq 'A'}">
                                                                <span>2.Accepting Note :</span> <span style="color:red;font-size: 25px"> ***********</span>
                                                            </c:if>
                                                        </div> 
                                                <tr>
                                                    <td style="padding-left: 10px;">3. For  Overall Grading  “Below Average” /  “Outstanding”  please upload Document if any.:</td>
                                                </tr>
                                                <tr>
                                                    <td>&nbsp;</td>
                                                </tr>
                                                <tr>
                                                    <c:if test="${LoginUserBean.loginusertype eq 'A'}"> 
                                                        <td style="padding-left: 20px;"><span style="color:red"><b> Document:</b></span>
                                                            <span style="color: red;font-size: 25px">*******</span>
                                                        </td>
                                                    </c:if>


                                                </tr>
                                            </c:if>
                                            <c:if test="${acceptingbean.isacceptingcompleted eq 'T'}"> 
                                                <div style="width:100%;overflow: auto;margin-top:5px;border:1px solid #5F9B24;">                                                        
                                                    <table border="0"  cellpadding="5" cellspacing="0" width="100%" class="tableview">
                                                        <tr style="height: 40px">
                                                            <td width="70%">                                
                                                                <c:set var = "slno" value = "${slno+1}"/>
                                                                <table>
                                                                    <tr>
                                                                        <td>
                                                                            <div style="font-weight:bold;text-decoration:underline;">${acceptingbean.acceptingauthName}</div>
                                                                            <div style="color: red;"><b>PAR is Force Forwarded On Date: ${acceptingbean.submittedon} Due to Office Of the Demmitted Authorities in the PAR Recording Change</b></div>
                                                                        </td>
                                                                    </tr>

                                                                </table>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </div>
                                            </c:if>

                                            <c:if test="${acceptingbean.isacceptingcompleted ne 'Y'}">                                                    
                                                <table width="100%">
                                                    <tr>
                                                        <td style="border-top:1px solid black;font-weight:bold;text-decoration:underline;">
                                                            ${acceptingbean.acceptingauthName}
                                                        </td>
                                                    </tr>
                                                </table>
                                                <c:if test="${acceptingbean.isacceptingcompleted ne 'Y'}">
                                                    ${acceptingbean.acceptingNote}
                                                </c:if>                                                    
                                            </c:if>

                                            </td>
                                            </tr>
                                        </table> 

                                    </c:forEach>
                                </c:if>
                            </div>
                        </div>
                    </div>
                </div>
            </c:if>

            <div align="center" style = "margin-top: 10px;">
                <div class="controlpanelDiv">	
                    <table width="100%"  cellpadding="0" cellspacing="0" >
                        <tr>
                            <td align="center" class="skinbutton sb_active">
                                <%-- <a href="updateAcceptingRemarksForReview.htm?parId=${parApplyForm.parId}" class="btn-default"><button>Save</button></a> --%>
                                <form:hidden path="parId"/>
                                <div class="col-lg-6">
                                    <a href="parPDF.htm?parId=${parApplyForm.parId}&taskid=${parApplyForm.taskId}&fiscalYear=${parApplyForm.fiscalYear}" class="btn-default" target="_blank"><button type="button" class="btn btn-primary">Download</button></a>
                                    <form:hidden path="hasadminPriv"/>
                                    <c:if test="${(LoginUserBean.loginusertype eq 'G' && users.hasparreviewingAuthorization == 'Y') && (parApplyForm.isreviewed eq 'N' || empty parApplyForm.isreviewed) && (parApplyForm.sltAdminRemark eq 'N' || empty parApplyForm.sltAdminRemark)}"> 
                                        <button type="button" class="btn btn-primary" onclick="openReviewAdverseWindow()">Review</button>
                                    </c:if> 
                                </div>
                            </td>
                        </tr>                        
                    </table>                                                
                </div>
            </div>       
        </form:form>   
        <div id="setreview" class="modal fade" role="dialog">
            <div class="modal-dialog  modal-lg">

                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">View Review Custodian Detail</h4>
                    </div>
                    <div class="modal-body">

                        <%-- <a href="parPDFAdverse.htm?parId=${parApplyForm.parId}&taskid=${parApplyForm.taskId}" class="btn-default" > <button>Reviewed as Adverse</button></a> --%>
                        <a href="parPDFAdverseAppraiseCommunication.htm?parId=${parApplyForm.parId}" class="btn-default" target="_blank"><button>Reviewed as Adverse</button></a>
                        <%--<a href="parPDFNonadverse.htm?parId=${parApplyForm.parId}&taskid=${parApplyForm.taskId}" class="btn-default" target="_blank"><button>Reviewed as Non Adverse</button></a>--%>
                        <a href="parPDFNonadverse.htm?empName=${parApplyForm.empName}&prdFrmDate=${parApplyForm.prdFrmDate}&prdToDate=${parApplyForm.prdToDate}&parId=${parApplyForm.parId}&taskid=${parApplyForm.taskId}" class="btn-default" target="_blank"><button>Reviewed as Non Adverse</button></a>

                    </div>
                    <div class="modal-footer">
                        <a href="#" class="btn btn-info btn-lg">
                            Save
                        </a>
                        <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
