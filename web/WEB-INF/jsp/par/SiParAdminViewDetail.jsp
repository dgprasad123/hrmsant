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
                $("#collapseOne").on("hide.bs.collapse", function() {
                    $(".divcls").html('<span class="pull-right glyphicon glyphicon-plus"></span> Absentee Statement <span style="font-style: italic;background-color:#ce5050;color:#FFFFFF;"> Please Click here For Details</span>');
                });
                $("#collapseOne").on("show.bs.collapse", function() {
                    $(".divcls").html('<span class="pull-right glyphicon glyphicon-minus"></span> Absentee Statement <span style="font-style: italic;background-color:#ce5050;color:#FFFFFF;"> Please Click here For Details</span>');
                });
                $("#collapsetwo").on("hide.bs.collapse", function() {
                    $(".divcls1").html('<span class="pull-right glyphicon glyphicon-plus"></span> Achievements <span style="font-style: italic;background-color:#ce5050;color:#FFFFFF;">Please Click here For Details</span>');
                });
                $("#collapsetwo").on("show.bs.collapse", function() {
                    $(".divcls1").html('<span class="pull-right glyphicon glyphicon-minus"></span> Achievements <span style="font-style: italic;background-color:#ce5050;color:#FFFFFF;">Please Click here For Details</span>');
                });
                
                $("#collapseThree").on("hide.bs.collapse", function() {
                    $(".divcls2").html('<span class="pull-right glyphicon glyphicon-plus"></span> Other Details <span style="font-style: italic;background-color:#ce5050;color:#FFFFFF;">Please Click here For Details</span>');
                });
                $("#collapseThree").on("show.bs.collapse", function() {
                    $(".divcls2").html('<span class="pull-right glyphicon glyphicon-minus"></span> Other Details <span style="font-style: italic;background-color:#ce5050;color:#FFFFFF;">Please Click here For Details</span>');
                });
                
                
                
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
                            <td style="background-color:#5095ce;color:#FFFFFF;padding:0px;font-weight:bold;" align="center">
                                <h2>Performance Appraisal Report (PAR) for SI & Equivalent Ranks (Group - B)</h2>
                            </td>
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
                                        <table border="0" cellpadding="0" cellspacing="0" width="100%" class="table table-hover table-striped">
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
                                        <table border="0" cellpadding="0" cellspacing="0" width="100%" class="table table-hover table-striped">
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
                                        <table border="0" cellpadding="0" cellspacing="0" width="100%" class="table table-hover table-striped">
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
                                    <td width="70%"> <b> <c:out value="${parApplyForm.empName}"/> </b> </td> 
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
                                    <td width="70%">${parApplyForm.sltHeadQuarter}</td>                                         
                                </tr>
                                <tr style="height: 40px">                               
                                    <td align="center" width="10%"> 10. </td>
                                    <td width="20%"> Sub Inspector Type </td>
                                    <td width="70%"><span><c:out value="${parApplyForm.siType}"/></span></td>
                                </tr>
                                <tr style="height: 40px">                               
                                    <td align="center" width="10%"> 11. </td>
                                    <td width="20%"> Place of Posting </td>
                                    <td width="70%"><span><c:out value="${parApplyForm.placeOfPostingSi}"/></span></td>
                                </tr>
                            </table>
                        </div>
                                
                        
                        <div style="width:100%;overflow: auto;margin-top:5px;border:1px solid #5095ce;">
                            <div  class="divcls" style="background-color:#5095ce;color:#FFFFFF;padding:5px;font-weight:bold;font-size: 17px" align="left" data-toggle="collapse" data-target="#collapseOne">
                                Absentee Statement 
                                <span style="font-style: italic;background-color:#ce5050;color:#FFFFFF;">Please Click here For Details</span>
                                <span class="pull-right glyphicon glyphicon-plus"></span>
                            </div>                        
                            <div class="collapse" id="collapseOne">
                                <table border="0" cellpadding="5" cellspacing="0" width="95%" class="tableview">             
                                    <tr>
                                        <th align="left" width="15%"><b>From Date</b></th>
                                        <th align="left" width="15%"><b>To Date</b></th>
                                        <th align="left" width="15%"><b>Leave/ Training</b></th>
                                        <th align="left" width="15%"><b>Type of Leave</b></th>
                                    </tr>  
                                </table>
                                <table border="0" cellpadding="5" cellspacing="0" width="95%" class="tableview">
                                    <c:if test="${not empty parApplyForm.leaveAbsentee}">
                                        <c:forEach var="AbsenteeBean" items="${parApplyForm.leaveAbsentee}">
                                            <tr height="40px">
                                                <td width="15%" align="left"><c:out value="${AbsenteeBean.fromDate}"/></td>
                                                <td width="15%" align="left"><c:out value="${AbsenteeBean.toDate}"/></td>
                                                <c:if test="${AbsenteeBean.absenceCause == 'L'}">
                                                    <td width="15%" align="left">Leave</td>
                                                </c:if>
                                                <c:if test="${AbsenteeBean.absenceCause == 'T'}">
                                                    <td width="15%" align="left">Training</td>
                                                </c:if>
                                                <td width="15%" align="left"><c:out value="${AbsenteeBean.leaveType}"/></td>
                                            </tr>
                                        </c:forEach>
                                    </c:if>
                                </table>
                            </div>
                        </div>
                        
                        <div style="width:100%;overflow: auto;margin-top:5px;border:1px solid #5095ce;">
                            <div class="divcls1" style="background-color:#5095ce;color:#FFFFFF;padding:5px;font-weight:bold;font-size: 17px" 
                                align="left" data-toggle="collapse" data-target="#collapsetwo"> Achievements
                                <span style="font-style: italic;background-color:#ce5050;color:#FFFFFF;">Please Click here For Details</span>
                                <span class="pull-right glyphicon glyphicon-plus"></span>
                            </div>                            
                            <div id="collapsetwo" class="collapse">
                                <table border="0"  cellpadding="5" cellspacing="0" width="95%" class="tableview">             
                                    <tr>
                                        <th align="left" width="15%"><b>Task</b></th>
                                        <th align="left" width="15%"><b>Target</b></th>
                                        <th align="left" width="15%"><b>Achievement</b></th>
                                        <th align="left" width="15%"><b>Achievement(%)</b></th>
                                    </tr>  
                                </table>
                                <table border="0"  cellpadding="5" cellspacing="0" width="95%" class="tableview"> 
                                    <c:forEach items="${parApplyForm.achivementList}" var="achievementDtl" varStatus="cnt">
                                        <tr>
                                            <td width="15%" align="left">${achievementDtl.task}</td>
                                            <td width="15%" align="left">${achievementDtl.target}</td>
                                            <td width="15%" align="left">${achievementDtl.achievement}</td>
                                            <td width="15%" align="left">${achievementDtl.percentAchievement}</td>
                                        </tr>
                                    </c:forEach>
                                </table>
                            </div>
                        </div>    
                        
                        <div style="width:100%;overflow: auto;margin-top:5px;border:1px solid #5095ce;">
                            <div class="divcls2" style="background-color:#5095ce;color:#FFFFFF;padding:5px;font-weight:bold;font-size: 17px" align="left" data-toggle="collapse" data-target="#collapseThree">
                                Other Details
                                <span style="font-style: italic;background-color:#ce5050;color:#FFFFFF;">Please Click here For Details</span>
                                <span class="pull-right glyphicon glyphicon-plus"></span>
                            </div>
                            <div id="collapseThree" class="collapse">
                                <table border="0"  cellpadding="5" cellspacing="0" width="100%" class="tableview">                                         
                                    <tr style="height: 40px">                               
                                        <td align="center" width="10%"> 1. </td>
                                        <td  width="20%">Brief description of duties/tasks entrusted.<br/>(in about 100 words)</td>
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
                                        <td  width="20%">(ii) Work Done For Implementation of 5TS (Transparency,Teamwork,Technology,<br/>Transformation and Time):</td>
                                        <td width="70%"> <b> ${parApplyForm.fiveTComponentappraise} </b>
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

                                    <tr style="height: 40px">                               
                                        <td align="center" width="10%"> 4. </td>
                                        <td width="20%">Place</td>
                                        <td width="70%">
                                            <table border="0" width="100%">
                                                <tr>
                                                    <td width="50%" colspan="2">${parApplyForm.place}</td>
                                                    <td width="25%"><b>Date of Submission:</b> </td>
                                                    <td width="25%">${parApplyForm.submittedon}</td>
                                                </tr>
                                            </table>    
                                        </td> 
                                    </tr>
                                </table>
                            </div>
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
                                                                    <td style="font-weight:bold;text-decoration:underline;margin-left: 10px;">${reportingdt.reportingauthName}on d.t ${reportingdt.submittedon}</td>
                                                                </c:when>                                                                
                                                                <c:otherwise>
                                                                    <td style="border-top:1px solid black;font-weight:bold;text-decoration:underline;">${reportingdt.reportingauthName} on d.t ${reportingdt.submittedon}</td>                                                                   
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </tr>
                                                        <tr>
                                                            <td><span style="margin-left: 5px;">1. Personal Attribute (On a scale of 1-5 weightage for this section in 40%) “A” </td>
                                                        </tr>
                                                        <tr>
                                                            <td>
                                                                <table width="80%" style="margin-left:35px;">
                                                                    
                                                                    <tr>
                                                                        <td>(a)  Attitude to work : </td>
                                                                        <td><div style="padding:5px;">
                                                                                <c:if test="${reportingdt.ratingattitude eq '1'}"> Below Average </c:if>
                                                                                <c:if test="${reportingdt.ratingattitude eq '2'}"> Average </c:if>
                                                                                <c:if test="${reportingdt.ratingattitude eq '3'}"> Good </c:if>
                                                                                <c:if test="${reportingdt.ratingattitude eq '4'}"> Very Good </c:if>
                                                                                <c:if test="${reportingdt.ratingattitude eq '5'}"> Outstanding </c:if>
                                                                                
                                                                                
                                                                                ${reportingdt.ratingattitude1}</div></td>
                                                                        <td>(b)  Sense of responsibility:    </td>
                                                                        <td><div style="padding:5px;">${reportingdt.ratingresponsibility1}</div></td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td>(c)  Communication skill :  </td>
                                                                        <td><div style="padding:5px;">${reportingdt.ratingcomskill1}</div></td>
                                                                        <td>(d)  Leadership Qualities :  </td>
                                                                        <td><div style="padding:5px;">${reportingdt.ratingleadership1}</div></td>
                                                                    </tr>
                                                                    
                                                                </table>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td style="padding-left: 10px;"><span><b>2.</b> Functional (On a Scale of 1-5,weightage for this section is 20%)“B”</td>
                                                        </tr>
                                                        <tr>
                                                            <td style="padding: 5px;">
                                                                <table width="99%" style="margin-left:35px;" border="0">
                                                                    <tr>
                                                                        <td width="75%">(a)  Knowledge of Criminal Laws/ Police Manuals and rules/ Procedures/IT Skills/ Local norms in the relevant subjects:</td>
                                                                        <td width="20%"> 
                                                                            <c:if test="${reportingdt.ratingitskill eq '1'}"> Below Average </c:if>
                                                                            <c:if test="${reportingdt.ratingitskill eq '2'}"> Average </c:if>
                                                                            <c:if test="${reportingdt.ratingitskill eq '3'}"> Good </c:if>
                                                                            <c:if test="${reportingdt.ratingitskill eq '4'}"> Very Good </c:if>
                                                                            <c:if test="${reportingdt.ratingitskill eq '5'}"> Outstanding </c:if>
                                                                        </td>
                                                                    </tr>
                                                                    <tr><td colspan="2">&nbsp;</td></tr>
                                                                    <tr>
                                                                        <td>(b)  Attitude towards ST/SC/Weaker Sections & relation with Public:    </td>
                                                                        <td> 
                                                                            <c:if test="${reportingdt.ratingAttitudeStScSection eq '1'}"> Below Average </c:if>
                                                                            <c:if test="${reportingdt.ratingAttitudeStScSection eq '2'}"> Average </c:if>
                                                                            <c:if test="${reportingdt.ratingAttitudeStScSection eq '3'}"> Good </c:if>
                                                                            <c:if test="${reportingdt.ratingAttitudeStScSection eq '4'}"> Very Good </c:if>
                                                                            <c:if test="${reportingdt.ratingAttitudeStScSection eq '5'}"> Outstanding </c:if>
                                                                        </td>
                                                                    </tr>
                                                                </table>
                                                            </td>
                                                        </tr>
                                                        
                                                        <tr>
                                                            <td><span style="margin-left: 5px;"> 3. Assessment Of Performance Of 5T (20%) “C”:</td>
                                                        </tr>
                                                        <tr>
                                                            <td style="padding: 5px;">
                                                                <table width="99%" style="margin-left:35px;" border="0">
                                                                    <tr>
                                                                        <td width="75%">(a) 10% on 5T charter of Department: (out of 10%):</td>
                                                                        <td width="20%"> 
                                                                            <c:if test="${reportingdt.fiveTChartertenpercent eq '1'}"> Below Average </c:if>
                                                                            <c:if test="${reportingdt.fiveTChartertenpercent eq '2'}"> Average </c:if>
                                                                            <c:if test="${reportingdt.fiveTChartertenpercent eq '3'}"> Good </c:if>
                                                                            <c:if test="${reportingdt.fiveTChartertenpercent eq '4'}"> Very Good </c:if>
                                                                            <c:if test="${reportingdt.fiveTChartertenpercent eq '5'}"> Outstanding </c:if> 
                                                                        </td>
                                                                    </tr>
                                                                    <tr><td colspan="2">&nbsp;</td></tr>
                                                                    <tr>
                                                                        <td>(b) 5% on 5T charter of Government: (out of 5%):</td>
                                                                        <td><div style="padding:5px;">
                                                                            <c:if test="${reportingdt.fiveTCharterfivePercent eq '1'}"> Below Average </c:if>
                                                                            <c:if test="${reportingdt.fiveTCharterfivePercent eq '2'}"> Average </c:if>
                                                                            <c:if test="${reportingdt.fiveTCharterfivePercent eq '3'}"> Good </c:if>
                                                                            <c:if test="${reportingdt.fiveTCharterfivePercent eq '4'}"> Very Good </c:if>
                                                                            <c:if test="${reportingdt.fiveTCharterfivePercent eq '5'}"> Outstanding </c:if>
                                                                            </div>
                                                                        </td>
                                                                    </tr>
                                                                    <tr><td colspan="2">&nbsp;</td></tr>
                                                                    <tr>
                                                                        <td><span> (c) 5% on Mo Sarkar: (out of 5%):  </span></td>
                                                                        <td><div style="padding:5px;">
                                                                            <c:if test="${reportingdt.fiveTComponentmoSarkar eq '1'}"> Below Average </c:if>
                                                                            <c:if test="${reportingdt.fiveTComponentmoSarkar eq '2'}"> Average </c:if>
                                                                            <c:if test="${reportingdt.fiveTComponentmoSarkar eq '3'}"> Good </c:if>
                                                                            <c:if test="${reportingdt.fiveTComponentmoSarkar eq '4'}"> Very Good </c:if>
                                                                            <c:if test="${reportingdt.fiveTComponentmoSarkar eq '5'}"> Outstanding </c:if>
                                                                            </div>
                                                                        </td>
                                                                    </tr>
                                                                </table>
                                                            </td>
                                                        </tr>
                                                        
                                                            
                                                        
                                                        <tr>
                                                            <td><span style="margin-left: 5px;"> 4.Assessment of Work output (on scale of 1-5 weightage for this section 20%) “D”:</span></td>
                                                        </tr>
                                                        <tr>
                                                            <td style="padding: 5px;">
                                                                <table width="99%" style="margin-left:35px;" border="0">
                                                                    <tr>
                                                                        <td width="75%">
                                                                            <c:if test="${parApplyForm.siType eq 'Sub Inspector (Civil)'}">
                                                                                (a) Quality of output and effectiveness in investigation/ Enquiry (Eg. Convictions):
                                                                            </c:if>
                                                                            <c:if test="${parApplyForm.siType eq 'Sub Inspector (Armed)'}">
                                                                                (a) Quality of output and effectiveness in management of Stores/ Procurement/ Maintenance of Arms & Ammunitions:
                                                                            </c:if>
                                                                            <c:if test="${parApplyForm.siType eq 'Sub Inspector (Equivalent)'}">
                                                                                (a) Quality of output and effectiveness in management of equipment/stores/records pertaining to their field:
                                                                            </c:if>
                                                                        </td>
                                                                        <td width="20%"> 
                                                                            <c:if test="${reportingdt.ratingQualityOfOutput eq '1'}"> Below Average </c:if>
                                                                            <c:if test="${reportingdt.ratingQualityOfOutput eq '2'}"> Average </c:if>
                                                                            <c:if test="${reportingdt.ratingQualityOfOutput eq '3'}"> Good </c:if>
                                                                            <c:if test="${reportingdt.ratingQualityOfOutput eq '4'}"> Very Good </c:if>
                                                                            <c:if test="${reportingdt.ratingQualityOfOutput eq '5'}"> Outstanding </c:if> 
                                                                        </td>
                                                                    </tr>
                                                                    <tr><td colspan="2">&nbsp;</td></tr>
                                                                    <tr>
                                                                        <td>
                                                                            <c:if test="${parApplyForm.siType eq 'Sub Inspector (Civil)'}">
                                                                                (b) Effectiveness in handling Law & Order/ Collection of Intelligence / command /control over Subordinates:
                                                                            </c:if>
                                                                            <c:if test="${parApplyForm.siType eq 'Sub Inspector (Armed)'}">
                                                                                (b) Effectiveness in handling Force management / command /control over Subordinates/ interest in their training /development/ welfare:
                                                                            </c:if>
                                                                            <c:if test="${parApplyForm.siType eq 'Sub Inspector (Equivalent)'}">
                                                                                (b) Effectiveness in handling work pertaining to their field/ command/control over Subordinates/ interest in their training /development/ welfare:
                                                                            </c:if>
                                                                        </td>
                                                                        <td><div style="padding:5px;">
                                                                            <c:if test="${reportingdt.ratingeffectivenessHandlingWork eq '1'}"> Below Average </c:if>
                                                                            <c:if test="${reportingdt.ratingeffectivenessHandlingWork eq '2'}"> Average </c:if>
                                                                            <c:if test="${reportingdt.ratingeffectivenessHandlingWork eq '3'}"> Good </c:if>
                                                                            <c:if test="${reportingdt.ratingeffectivenessHandlingWork eq '4'}"> Very Good </c:if>
                                                                            <c:if test="${reportingdt.ratingeffectivenessHandlingWork eq '5'}"> Outstanding </c:if>
                                                                            </div>
                                                                        </td>
                                                                    </tr>
                                                                </table>
                                                            </td>
                                                        </tr>
                                                          
                                                            
                                                        <tr>
                                                            <td><span style="margin-left: 5px;">5. Pen picture of Officer (not more than 100 words):</span></td>
                                                        </tr>
                                                            <tr>
                                                                <td><div style="padding:5px;">${reportingdt.penPictureOfOficerNote}</div></td>
                                                            </tr>
                                                        
                                                        <tr>
                                                            <td><span style="margin-left: 5px;"> 6.  Inadequacies, deficiencies or shortcomings, if any, not more than 200 words (Remarks to be treated as adverse)</td>
                                                        </tr>
                                                            <tr>
                                                                <td><div style="padding:5px;"> ${reportingdt.inadequaciesNote} </div></td>
                                                            </tr>
                                                        
                                                        <tr>
                                                            <td><span style="margin-left: 5px;"> 7.  Integrity ( If integrity is doubtful or adverse please write “Not certified” in the space below and justify your remarks here </td>
                                                        </tr>
                                                            <tr>
                                                                <td><div style="padding:5px;"> ${reportingdt.integrityNote} </div></td>
                                                            </tr>
                                                        
                                                        <tr>
                                                            <td><span style="margin-left: 5px;"> 8. State of Health </td>
                                                        </tr>
                                                            <tr>
                                                                <td><span>(a) State of Health (please indicate whether the officer’s state of health is) </span></td>
                                                            </tr>
                                                            <tr>
                                                                <td><div style="padding:5px;">${reportingdt.stateOfHealth}</div></td>
                                                            </tr> 
                                                            <tr>
                                                                <td><span>(b) Sick Report (if more than 10 days at one time) mention period of sick report:  </span></td>
                                                            </tr>
                                                            <tr>
                                                                <td><div style="padding:5px;"> ${reportingdt.sickReportOnDate} </div></td>
                                                            </tr> 
                                                            <tr>
                                                                <td><span> (c) Please indicate if appraisee reported sick to avoid posting on transfer or a specific duty:  </span></td>
                                                            </tr>
                                                            <tr>
                                                                <td><div style="padding:5px;"> ${reportingdt.sickDetails} </div></td>
                                                            </tr>
                                                        
                                                            <tr>
                                                                <td><span style="margin-left: 5px;">9. Overall Grading :</span></td>
                                                            </tr>
                                                            <tr>
                                                                <td><div style="padding:5px;">
                                                                        <c:if test="${reportingdt.sltGrading eq '1'}"> Below Average </c:if>
                                                                        <c:if test="${reportingdt.sltGrading eq '2'}"> Average </c:if>
                                                                        <c:if test="${reportingdt.sltGrading eq '3'}"> Good </c:if>
                                                                        <c:if test="${reportingdt.sltGrading eq '4'}"> Very Good </c:if>
                                                                        <c:if test="${reportingdt.sltGrading eq '5'}"> Outstanding </c:if>
                                                                    </div>
                                                                </td>
                                                            </tr>
                                                            
                                                        <tr>
                                                            <td><span style="margin-left: 5px;">10. For Overall Grading “Below Average” / “Outstanding” please provide justification in the space below :</span></td>
                                                        </tr>
                                                        <tr>
                                                            <td><div style="padding:5px;">${reportingdt.gradingNote} </div></td>
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
                                                                    <td><span style="margin-left: 5px;">1. Personal Attribute (On a scale of 1-5 weightage for this section in 40%) “A” </td>
                                                                </tr>
                                                                <tr>
                                                                    <td>
                                                                        <table width="80%" style="margin-left:35px;">
                                                                            
                                                                            <tr>
                                                                                <td>(a)  Attitude to work :</td>
                                                                                <td><div style="padding:5px;">
                                                                                        <c:if test="${reportingdt.ratingattitude eq '1'}"> Below Average </c:if>
                                                                                        <c:if test="${reportingdt.ratingattitude eq '2'}"> Average </c:if>
                                                                                        <c:if test="${reportingdt.ratingattitude eq '3'}"> Good </c:if>
                                                                                        <c:if test="${reportingdt.ratingattitude eq '4'}"> Very Good </c:if>
                                                                                        <c:if test="${reportingdt.ratingattitude eq '5'}"> Outstanding </c:if>
                                                                                    </div>
                                                                                </td>
                                                                                <td>(b)  Sense of responsibility:    </td>
                                                                                <td><div style="padding:5px;">
                                                                                        <c:if test="${reportingdt.ratingresponsibility eq '1'}"> Below Average </c:if>
                                                                                        <c:if test="${reportingdt.ratingresponsibility eq '2'}"> Average </c:if>
                                                                                        <c:if test="${reportingdt.ratingresponsibility eq '3'}"> Good </c:if>
                                                                                        <c:if test="${reportingdt.ratingresponsibility eq '4'}"> Very Good </c:if>
                                                                                        <c:if test="${reportingdt.ratingresponsibility eq '5'}"> Outstanding </c:if>
                                                                                    </div>
                                                                                </td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td>(c)  Communication skill :  </td>
                                                                                <td><div style="padding:5px;">
                                                                                        <c:if test="${reportingdt.ratingcomskill eq '1'}"> Below Average </c:if>
                                                                                        <c:if test="${reportingdt.ratingcomskill eq '2'}"> Average </c:if>
                                                                                        <c:if test="${reportingdt.ratingcomskill eq '3'}"> Good </c:if>
                                                                                        <c:if test="${reportingdt.ratingcomskill eq '4'}"> Very Good </c:if>
                                                                                        <c:if test="${reportingdt.ratingcomskill eq '5'}"> Outstanding </c:if>
                                                                                    </div>
                                                                                </td>
                                                                                <td>(d)  Leadership Qualities :  </td>
                                                                                <td><div style="padding:5px;">
                                                                                        <c:if test="${reportingdt.ratingleadership eq '1'}"> Below Average </c:if>
                                                                                        <c:if test="${reportingdt.ratingleadership eq '2'}"> Average </c:if>
                                                                                        <c:if test="${reportingdt.ratingleadership eq '3'}"> Good </c:if>
                                                                                        <c:if test="${reportingdt.ratingleadership eq '4'}"> Very Good </c:if>
                                                                                        <c:if test="${reportingdt.ratingleadership eq '5'}"> Outstanding </c:if>
                                                                                    </div>
                                                                                </td>
                                                                            </tr>

                                                                        </table>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td style="padding-left: 10px;"><span><b>2.</b> Functional (On a Scale of 1-5,weightage for this section is 20%)“B”</td>
                                                                </tr>
                                                                <tr>
                                                                    <td style="padding: 5px;">
                                                                        <table width="99%" style="margin-left:35px;" border="0">
                                                                            <tr>
                                                                                <td width="75%">(a)  Knowledge of Criminal Laws/ Police Manuals and rules/ Procedures/IT Skills/ Local norms in the relevant subjects:</td>
                                                                                <td width="20%"> 
                                                                                    <c:if test="${reportingdt.ratingitskill eq '1'}"> Below Average </c:if>
                                                                                    <c:if test="${reportingdt.ratingitskill eq '2'}"> Average </c:if>
                                                                                    <c:if test="${reportingdt.ratingitskill eq '3'}"> Good </c:if>
                                                                                    <c:if test="${reportingdt.ratingitskill eq '4'}"> Very Good </c:if>
                                                                                    <c:if test="${reportingdt.ratingitskill eq '5'}"> Outstanding </c:if>
                                                                                </td>
                                                                            </tr>
                                                                            <tr><td colspan="2">&nbsp;</td></tr>
                                                                            <tr>
                                                                                <td>(b)  Attitude towards ST/SC/Weaker Sections & relation with Public:    </td>
                                                                                <td><c:if test="${reportingdt.ratingAttitudeStScSection eq '1'}"> Below Average </c:if>
                                                                                    <c:if test="${reportingdt.ratingAttitudeStScSection eq '2'}"> Average </c:if>
                                                                                    <c:if test="${reportingdt.ratingAttitudeStScSection eq '3'}"> Good </c:if>
                                                                                    <c:if test="${reportingdt.ratingAttitudeStScSection eq '4'}"> Very Good </c:if>
                                                                                    <c:if test="${reportingdt.ratingAttitudeStScSection eq '5'}"> Outstanding </c:if> 
                                                                                </td>
                                                                            </tr>

                                                                        </table>
                                                                    </td>
                                                                </tr>

                                                                <tr>
                                                                    <td><span style="margin-left: 5px;"> 3. Assessment Of Performance Of 5T (20%) “C”:</td>
                                                                </tr>
                                                                <tr>
                                                                    <td style="margin-left: 5px;"><span>  </span></td>
                                                                </tr>
                                                                
                                                                <tr>
                                                                    <td style="padding: 5px;">
                                                                        <table width="99%" style="margin-left:35px;" border="0">
                                                                            <tr>
                                                                                <td width="75%">(a) 10% on 5T charter of Department: (out of 10%):</td>
                                                                                <td width="20%"> 
                                                                                    <c:if test="${reportingdt.fiveTChartertenpercent eq '1'}"> Below Average </c:if>
                                                                                    <c:if test="${reportingdt.fiveTChartertenpercent eq '2'}"> Average </c:if>
                                                                                    <c:if test="${reportingdt.fiveTChartertenpercent eq '3'}"> Good </c:if>
                                                                                    <c:if test="${reportingdt.fiveTChartertenpercent eq '4'}"> Very Good </c:if>
                                                                                    <c:if test="${reportingdt.fiveTChartertenpercent eq '5'}"> Outstanding </c:if>
                                                                                </td>
                                                                            </tr>
                                                                            <tr><td colspan="2">&nbsp;</td></tr>
                                                                            <tr>
                                                                                <td>(b) 5% on 5T charter of Government: (out of 5%): </td>
                                                                                <td> 
                                                                                    <c:if test="${reportingdt.fiveTCharterfivePercent eq '1'}"> Below Average </c:if>
                                                                                    <c:if test="${reportingdt.fiveTCharterfivePercent eq '2'}"> Average </c:if>
                                                                                    <c:if test="${reportingdt.fiveTCharterfivePercent eq '3'}"> Good </c:if>
                                                                                    <c:if test="${reportingdt.fiveTCharterfivePercent eq '4'}"> Very Good </c:if>
                                                                                    <c:if test="${reportingdt.fiveTCharterfivePercent eq '5'}"> Outstanding </c:if>
                                                                                </td>
                                                                            </tr>
                                                                            <tr><td colspan="2">&nbsp;</td></tr>
                                                                            <tr>
                                                                                <td>(c) 5% on Mo Sarkar: (out of 5%): </td>
                                                                                <td><c:if test="${reportingdt.fiveTComponentmoSarkar eq '1'}"> Below Average </c:if>
                                                                                    <c:if test="${reportingdt.fiveTComponentmoSarkar eq '2'}"> Average </c:if>
                                                                                    <c:if test="${reportingdt.fiveTComponentmoSarkar eq '3'}"> Good </c:if>
                                                                                    <c:if test="${reportingdt.fiveTComponentmoSarkar eq '4'}"> Very Good </c:if>
                                                                                    <c:if test="${reportingdt.fiveTComponentmoSarkar eq '5'}"> Outstanding </c:if>
                                                                                </td>
                                                                            </tr>
                                                                        </table>
                                                                    </td>
                                                                </tr>    

                                                                
                                                                <tr>
                                                                    <td><span style="margin-left: 5px;"> 4.Assessment of Work output (on scale of 1-5 weightage for this section 20%) “D”:</span></td>
                                                                </tr>
                                                                <tr>
                                                                    <td style="padding: 5px;">
                                                                        <table width="99%" style="margin-left:35px;" border="0">
                                                                            <tr>
                                                                                <td width="75%">
                                                                                <c:if test="${parApplyForm.siType eq 'Sub Inspector (Civil)'}">
                                                                                    (a) Quality of output and effectiveness in investigation/ Enquiry (Eg. Convictions):
                                                                                </c:if>
                                                                                <c:if test="${parApplyForm.siType eq 'Sub Inspector (Armed)'}">
                                                                                    (a) Quality of output and effectiveness in management of Stores/ Procurement/ Maintenance of Arms & Ammunitions:
                                                                                </c:if>
                                                                                <c:if test="${parApplyForm.siType eq 'Sub Inspector (Equivalent)'}">
                                                                                    (a) Quality of output and effectiveness in management of equipment/stores/records pertaining to their field:
                                                                                </c:if>
                                                                            </td>
                                                                            <td width="20%"> 
                                                                                <c:if test="${reportingdt.ratingQualityOfOutput eq '1'}"> Below Average </c:if>
                                                                                <c:if test="${reportingdt.ratingQualityOfOutput eq '2'}"> Average </c:if>
                                                                                <c:if test="${reportingdt.ratingQualityOfOutput eq '3'}"> Good </c:if>
                                                                                <c:if test="${reportingdt.ratingQualityOfOutput eq '4'}"> Very Good </c:if>
                                                                                <c:if test="${reportingdt.ratingQualityOfOutput eq '5'}"> Outstanding </c:if> 
                                                                                </td>
                                                                            </tr>
                                                                            <tr><td colspan="2">&nbsp;</td></tr>
                                                                            <tr>
                                                                                <td>
                                                                                <c:if test="${parApplyForm.siType eq 'Sub Inspector (Civil)'}">
                                                                                    (b) Effectiveness in handling Law & Order/ Collection of Intelligence / command /control over Subordinates:
                                                                                </c:if>
                                                                                <c:if test="${parApplyForm.siType eq 'Sub Inspector (Armed)'}">
                                                                                    (b) Effectiveness in handling Force management / command /control over Subordinates/ interest in their training /development/ welfare:
                                                                                </c:if>
                                                                                <c:if test="${parApplyForm.siType eq 'Sub Inspector (Equivalent)'}">
                                                                                    (b) Effectiveness in handling work pertaining to their field/ command/control over Subordinates/ interest in their training /development/ welfare:
                                                                                </c:if>
                                                                            </td>
                                                                            <td><div style="padding:5px;">
                                                                                    <c:if test="${reportingdt.ratingeffectivenessHandlingWork eq '1'}"> Below Average </c:if>
                                                                                    <c:if test="${reportingdt.ratingeffectivenessHandlingWork eq '2'}"> Average </c:if>
                                                                                    <c:if test="${reportingdt.ratingeffectivenessHandlingWork eq '3'}"> Good </c:if>
                                                                                    <c:if test="${reportingdt.ratingeffectivenessHandlingWork eq '4'}"> Very Good </c:if>
                                                                                    <c:if test="${reportingdt.ratingeffectivenessHandlingWork eq '5'}"> Outstanding </c:if>
                                                                                </div>
                                                                            </td>
                                                                        </tr>
                                                                    </table>
                                                                </td>
                                                        </tr>
                                                        
                                                                
                                                        <tr>
                                                            <td><span style="margin-left: 5px;">5. Pen picture of Officer (not more than 100 words):</span></td>
                                                        </tr>
                                                        <tr>
                                                            <td><div style="padding:5px;">${reportingdt.penPictureOfOficerNote}</div></td>
                                                        </tr>

                                                        <tr>
                                                            <td><span style="margin-left: 5px;"> 6.  Inadequacies, deficiencies or shortcomings, if any, not more than 200 words (Remarks to be treated as adverse)</td>
                                                        </tr>
                                                        <tr>
                                                            <td><div style="padding:5px;"> ${reportingdt.inadequaciesNote} </div></td>
                                                        </tr>

                                                        <tr>
                                                            <td><span style="margin-left: 5px;"> 7.  Integrity ( If integrity is doubtful or adverse please write “Not certified” in the space below and justify your remarks here </td>
                                                        </tr>
                                                        <tr>
                                                            <td><div style="padding:5px;"> ${reportingdt.integrityNote} </div></td>
                                                        </tr>

                                                        <tr>
                                                            <td><span style="margin-left: 5px;"> 8. State of Health </td>
                                                        </tr>
                                                        <tr>
                                                            <td><span>(a) State of Health (please indicate whether the officer’s state of health is) </span></td>
                                                        </tr>
                                                        <tr>
                                                            <td><div style="padding:5px;">${reportingdt.stateOfHealth}</div></td>
                                                        </tr> 
                                                        <tr>
                                                            <td><span>(b) Sick Report (if more than 10 days at one time) mention period of sick report:  </span></td>
                                                        </tr>
                                                        <tr>
                                                            <td><div style="padding:5px;"> ${reportingdt.sickReportOnDate} </div></td>
                                                        </tr> 
                                                        <tr>
                                                            <td><span> (c) Please indicate if appraisee reported sick to avoid posting on transfer or a specific duty:  </span></td>
                                                        </tr>
                                                        <tr>
                                                            <td><div style="padding:5px;"> ${reportingdt.sickDetails} </div></td>
                                                        </tr>

                                                                <tr>
                                                                    <td><span style="margin-left: 5px;">9. Overall Grading :</span></td>
                                                                </tr>
                                                                <tr>
                                                                    <td>
                                                                        <c:if test="${reportingdt.sltGrading eq '1'}"> Below Average </c:if>
                                                                        <c:if test="${reportingdt.sltGrading eq '2'}"> Average </c:if>
                                                                        <c:if test="${reportingdt.sltGrading eq '3'}"> Good </c:if>
                                                                        <c:if test="${reportingdt.sltGrading eq '4'}"> Very Good </c:if>
                                                                        <c:if test="${reportingdt.sltGrading eq '5'}"> Outstanding </c:if>
                                                                    </td>
                                                                </tr>
                                                                                                                                
                                                                <tr>
                                                                    <td><span style="margin-left: 5px;">10. For Overall Grading “Below Average” / “Outstanding” please provide justification in the space below :</span></td>
                                                                </tr>
                                                                <tr>
                                                                    <td><div style="padding:5px;">${reportingdt.gradingNote} </div></td>
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
                                                                <td>${reviewingbean.reviewingNote}</td>
                                                            </tr>
                                                            <tr>
                                                                <td> <span>2. Overall Grading Given By Reviewing Authority :</span>
                                                                    ${reviewingbean.sltReviewGrading1}
                                                                </td>
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
                                                                    <span> ${acceptingbean.sltAcceptingGrading1}</span>
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
                                                        <div style="padding:5px;"><span>2.Accepting Note :</span> ${acceptingbean.acceptingNote}</div> 

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
                                <div class="col-lg-6">
                                    <a href="SiParPDF.htm?encryptedParid=${parApplyForm.encryptedParid}&encryptedTaskid=${parApplyForm.encryptedTaskid}" class="btn-default" target="_blank"><button type="button" class="btn btn-primary">Download</button></a>
                                     
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
