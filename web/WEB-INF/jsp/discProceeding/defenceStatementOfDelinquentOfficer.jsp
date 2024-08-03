<%-- 
    Document   : defenceStatementRefeaing
    Created on : Aug 24, 2018, 12:35:27 PM
    Author     : manisha
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:HRMS:</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <script src="js/bootstrap-datetimepicker.js" type="text/javascript"></script>

        <style type="text/css">
            .headr{
                font-weight: bold;
                font-family: Verdana, Arial, Helvetica, sans-serif;
                font-size: 16px;
            }
        </style>

        <script language="javascript" type="text/javascript">
            $(document).ready(function() {
                $('#memoDate').datetimepicker({
                    format: 'D-MMM-YYYY'
                });
            });
            function saveMemo() {
                var memoNo = $('#rule15MemoNo').val();
                if (memoNo == '') {
                    alert("Please enter Memorandum No");
                    return false;
                }
                if (memoNo.length >= 18) {
                    alert("Please Enter a Memorandum No between 1 and 18");
                    return false;
                }
                var ordrDate = $('#rule15MemoDate').datebox('getValue');// will get the date value
                if (ordrDate == '') {
                    alert("Please enter Date");
                    return false;
                }
                var charge = $('#annex1Charge').val();
                if (charge.length >= 495) {
                    alert("Please Enter value between 0 and 495");
                    return false;
                }

            }
            function radioClickedondefenceremark() {
                var radioValue = $("input[name='defenceremarkByDO']:checked").val();
                if (radioValue == "Inquiry") {
                    $("#appointmentofIoPODetail").show();
                     $("#MinorPenaltyONpresentationOfDOonIoReport").hide();
                } else if (radioValue == "MinorPenalty") {
                    $("#MinorPenaltyONpresentationOfDOonIoReport").show();
                    $("#appointmentofIoPODetail").hide();
                }
            }
        </script>
    </head>
    <body style="padding:0px;">

        <form:form action="viewdefenceStatementOfDelinquentOfficer.htm" method="POST" commandName="pbean" class="form-inline">
            <div class="panel panel-default">
                <form:hidden path="daId"/>
                <form:hidden path="taskId"/>
                <div class="panel-heading" align="center"> Defence Statement against Article of Charge:<br></div>
                <div class="panel-body">   
                    <table width="100%" border="0" style="font-family: Verdana, Arial, Helvetica, sans-serif;font-size: 13px;">
                        <c:forEach items="${discchargelist}" var="chargeBean" varStatus="cnt">
                            <tr>
                                <td>                                                                                                                        
                                    ${cnt.index+1}.&nbsp;                                        
                                    ${chargeBean.articleOfCharge}&nbsp;
                                </td>
                            </tr>
                        </c:forEach>
                    </table>
                </div>  
            </div>
            <div class="panel panel-default">
                <div class="panel-heading" align="center">Defence Statement Submited by<br>
                    <div>                        
                        ${delinquentOfficerDtls.fname} ${delinquentOfficerDtls.mname} ${delinquentOfficerDtls.lname}  ${delinquentOfficerDtls.spn}                        
                    </div>
                </div>
                <div class="panel-body">

                    ${defencebean.defenceRemark}

                    <h4><b>Document:</b></h4><br>
                    <c:forEach items="${defencebean.attachments}" var="attachment" varStatus="cnt">
                        <tr>
                            <td>${cnt.index + 1}</td>

                            <td>                                    

                                <a href="downloadEmployeeAttachment.htm?attachmentid=${attachment.attachmentId}" class="btn btn-default">
                                    ${attachment.orgFileName}
                                </a>                            
                            </td>

                        </tr>
                    </c:forEach>
                </div>
            </div>



            <div class="panel panel-default">
                <div class="panel-heading" align="center">
                    Remark Of Disciplinary Authority On Defence Statement:
                </div>

                <div class="row" style="margin-bottom: 7px;padding-left: 20px;">
                    <div class="col-lg-2"> 
                        <input type="radio" id="InquiryOnDefenceRemark" name="defenceremarkByDO" value="Inquiry" onclick="radioClickedondefenceremark()"><b>Inquiry</b>
                    </div>
                    <div class="col-lg-2">
                        <input type="radio" id="MinorPenmaltyOnDefenceRemark" name="defenceremarkByDO" value="MinorPenalty" onclick="radioClickedondefenceremark()"><b>Minor Penalty</b>
                    </div> 
                    <div class="col-lg-2">
                        <input type="radio" id="MajorPenmaltyOnDefenceRemark" name="defenceremarkByDO" value="Exoneration" onclick="radioClickedondefenceremark()"><b>Exoneration</b>
                    </div> 

                </div>

                <div class="panel-body">   
                    <div class="panel panel-default" id="appointmentofIoPODetail">
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                I.O(Inquiry Officer),P.O(Presenting Officer) and A.P.O(Additional Presenting Officer) Detail
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label>1.Office Order No<span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-2">
                                    <form:input class="form-control" path="ioAppoinmentOrdNo" maxlength="51"/> 
                                </div>
                                <div class="col-lg-2">
                                    <label> 2.Office Order Date<span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-2">
                                    <div class="input-group date">
                                        <form:input class="form-control datepickerclass" path="ioAppoinmentOrdDt" readonly="true"/> 
                                        <span class="input-group-addon">
                                            <span class="glyphicon glyphicon-time"></span>
                                        </span>
                                    </div>                                
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="spc">3. Details of Inquiry Officer</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:input class="form-control" path="showCauseNotAuthority" readonly="true"/>                          
                                </div>
                                <div class="col-lg-1">
                                    <button type="button" class="btn btn-primary" data-toggle="modal" onclick="selectauthtype('S')" data-target="#authorityModal">
                                        <span class="glyphicon glyphicon-search"></span> Search
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>  
            </div>


            <div class="panel-footer">
                <c:if test="${taskdtls.istaskcompleted eq 'N'}">

                    <input type="submit" class="btn btn-default" name="action" value="submit" onclick="return confirm('Are you sure to Submit?')"/> 
                </c:if>
            </div>
        </div>
    </div>
</form:form>
</body>
</html>
