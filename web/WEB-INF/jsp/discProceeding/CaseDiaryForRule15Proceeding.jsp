<%-- 
    Document   : CaseDiaryForRule15Proceeding
    Created on : 11 Feb, 2021, 3:26:33 PM
    Author     : Manisha
--%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ page contentType="text/html;charset=UTF-8" autoFlush="true" buffer="64kb"%>

<%
    String contextPath = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + contextPath + "/";
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>        
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <script type="text/javascript"  src="js/basicjavascript.js"></script>
        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <script src="js/bootstrap-datetimepicker.js" type="text/javascript"></script>
        <style>
            table, th, td {
                border: 1px solid black;
                border-collapse: collapse;
            }
            th, td {
                padding: 5px;
                text-align: left;    
            }
            .table-responsive {
                max-height:450px;
                font-size: 10px;
            }
            .table-bordered{
                font-size: 12px;
            }
        </style>
    </head>
    <body>
        <div class="container-fluid">
            <div class="panel panel-default">
                <form:form action="caseDiaryForRule15ProceedingForOngoingDPRule15.htm" method="post" commandName="pbean" enctype="multipart/form-data">
                    <form:hidden path="dadid"/>
                    <form:hidden path="daId" />
                    <form:hidden path="dpStatus"/>
                    <form:hidden path="isAuthorityApprove"/>
                    <form:hidden path="hasSendNoticetoDO"/>
                    <input type="hidden" name="hasReplyByDelinquentOfficer" value="${defencebean.hasReplyByDelinquentOfficer}"/>
                    <input type="hidden" name="defid" value="${defencebean.defid}"/>
                    <input type="hidden" name="daioid" value="${ioBean.daioid}"/>
                    <form:hidden path="daioid"/>
                    <form:hidden path="hasIoAppointed"/>
                    <form:hidden path="hasIoRemarks"/>
                    <form:hidden path="hasSendSecondNotice"/>
                    <form:hidden path="hasDoRepresentOnsecondshowCause"/>
                    <form:hidden path="hasSendthirdshowCause"/>

                    <div class="form-group">
                        <label class="control-label col-sm-2">Delinquent Officer Name:</label>
                        <c:forEach items="${delinquentofficerList}" var="delinquent">
                            <input type="hidden" name="delinquent" value="${delinquent.empid}-${delinquent.spc}"/>
                            ${delinquent.fullname},${delinquent.spn}<br/>
                        </c:forEach>
                    </div>

                    <div class="panel panel-default" id="articleOfCharge">
                        <div class="panel-heading">
                            Draft Charge
                        </div>
                        <div class="panel-body">
                            <table class="table table-bordered table-hover table-striped">
                                <thead>
                                    <tr>
                                        <th width="3%">#</th>
                                        <th width="25%">Article of Charge</th>
                                        <th width="25%">Statement Of Imputation</th>
                                        <th width="25%">Memo Of Evidence</th>
                                    </tr>
                                </thead>
                                <tbody>                                        
                                    <c:forEach items="${articleOfChargeList}" var="articleOfCharge" varStatus="cnt">
                                        <tr>
                                            <td>${cnt.index + 1}</td>
                                            <td>
                                                ${articleOfCharge.articleOfCharge} ${articleOfCharge.articlesofChargeoriginalfilename}
                                                <c:if test="${not empty articleOfCharge.articlesofChargeoriginalfilename}">
                                                    <a href="downloadFileForArticleOfCharge.htm?dacid=${articleOfCharge.dacid}&documentTypeName=articlecharge" class="btn btn-default">
                                                        <span class="glyphicon glyphicon-paperclip"></span> ${articleOfCharge.articlesofChargeoriginalfilename}</a>
                                                    </c:if>
                                            </td>
                                            <td>
                                                ${articleOfCharge.statementOfImputation}
                                                <c:if test="${not empty articleOfCharge.statementOfImputationoriginalfilename}">
                                                    <a href="downloadFileForArticleOfCharge.htm?dacid=${articleOfCharge.dacid}&documentTypeName=statementimputation" class="btn btn-default">
                                                        <span class="glyphicon glyphicon-paperclip"></span> ${articleOfCharge.statementOfImputationoriginalfilename}</a>
                                                    </c:if>
                                            </td>
                                            <td>
                                                ${articleOfCharge.memoOfEvidence}
                                                <c:if test="${not empty articleOfCharge.memoofEvidenceoriginalfilename}">
                                                    <a href="downloadFileForArticleOfCharge.htm?dacid=${articleOfCharge.dacid}&documentTypeName=memoevidence" class="btn btn-default" >
                                                        <span class="glyphicon glyphicon-paperclip"></span> ${articleOfCharge.memoofEvidenceoriginalfilename}</a>
                                                    </c:if>
                                            </td>

                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>

                    <div class="panel panel-default">
                        <div class="panel panel-default" id="alreadyDAApproval">
                            <div class="panel-heading">
                                Memorandum Of Charge
                            </div>
                            <div class="form-group row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label>1.Memorandum No<span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-2">
                                    ${pbean.memoNo}
                                </div>

                                <div class="col-lg-2">
                                    <label for="showcauseOrdDt"> 2.Memorandum Date<span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-2">
                                    ${pbean.memoDate}                                
                                </div>

                            </div>
                            <div class="row" style="margin-bottom: 7px;">  
                                <div class="col-lg-2">
                                    <label for="document">3.Document(Memorandum Copy)<span style="color: red">*</span></label>
                                </div>
                                <a href="downloadMemorandumAttachment.htm?daId=${pbean.daId}">
                                        <span class="glyphicon glyphicon-paperclip"></span> ${pbean.memorandumoriginalFileName}</a>

                                
                            </div> 
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="spc">4.Disciplinary Authority Detail<span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-9">
                                    <form:hidden path="dahrmsid"/>
                                    <form:hidden path="daspc"/>
                                    <form:hidden path="daoffice"/>
                                    ${pbean.disciplinaryauthority}
                                </div>
                            </div>

                        </div>
                        <div class="row" id="sendForApproval" style="margin-bottom: 7px;">
                        </div>
                    </div>
                    <c:if test="${pbean.isAuthorityApprove eq 'Y'}">
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                Whether Served to Delinquent Officer
                            </div>

                            <div class="panel-body" id="alreadyservedDiv" >
                                <div class="row" style="margin-bottom: 7px;"> 
                                    <div class="col-lg-2">
                                        <label> 1.Date Of Receipt<span style="color: red">*</span></label>
                                    </div>

                                    <div class="col-lg-2">
                                        ${pbean.showCauseOrdDt}
                                    </div> 
                                </div>
                                <div class="row" style="margin-bottom: 7px;">  
                                    <div class="col-lg-2">
                                        <label for="document">2.Document(Served Copy)<span style="color: red">*</span></label>
                                    </div>
                                    <c:if test="${not empty pbean.firstshowcauseoriginalFileName}">
                                        <a href="">
                                            <span class="glyphicon glyphicon-paperclip"></span> ${pbean.firstshowcauseoriginalFileName}</a>
                                        </c:if>
                                </div>
                            </div>
                            
                        </div>
                    </c:if>

                    <c:if test="${pbean.hasSendNoticetoDO eq 'Y'}"> 
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                Written Statement By Delinquent Officer
                            </div>
                            <div class="panel-body">
                                <div class="row" style="margin-bottom: 7px;"> 
                                    <div class="col-lg-2">
                                        <label> 1.Date Of Receipt</label>
                                    </div>
                                    <div class="col-lg-2">
                                        ${pbean.writtenStatemenyByDOOnDt}
                                    </div>
                                </div>
                            </div>

                            <div class="row" style="margin-bottom: 7px;">  
                                <div class="col-lg-2">
                                    <label for="document">2.Document<span style="color: red">*</span></label>
                                </div>

                                <c:if test="${not empty pbean.defenceByDOoriginalFileName}">
                                    <a href="">
                                        <span class="glyphicon glyphicon-paperclip"></span>${pbean.defenceByDOoriginalFileName}</a>
                                    </c:if>
                            </div>
                        </div>
                    </c:if>

                    <c:if test="${pbean.hasReplyByDelinquentOfficer eq 'Y'}">
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
                                        ${pbean.ioAppoinmentOrdNo}
                                    </div>
                                    <div class="col-lg-2">
                                        <label> 2.Office Order Date<span style="color: red">*</span></label>
                                    </div>
                                    <div class="col-lg-2">
                                        ${pbean.ioAppoinmentOrdDt}
                                    </div>
                                </div>
                                <div class="row" style="margin-bottom: 7px;">
                                    <div class="col-lg-2">
                                        <label for="spc">3. Details of Inquiry Officer<span style="color: red">*</span></label>
                                    </div>
                                    <div class="col-lg-9">
                                        <form:hidden path="ioEmpHrmsId"/>
                                        <form:hidden path="ioEmpSPC"/>
                                        ${pbean.inquiryauthority}
                                    </div>
                                    
                                </div>
                                <div class="row" style="margin-bottom: 7px;">
                                    <div class="col-lg-2">
                                        <label for="spc">4. Details of Presenting Officer<span style="color: red">*</span></label>
                                    </div>
                                    <div class="col-lg-9">
                                        <form:hidden path="poHrmsId"/>
                                        <form:hidden path="poSPC"/>
                                        ${pbean.presentingauthority}
                                    </div>

                                </div>
                                <div class="row" style="margin-bottom: 7px;">
                                    <div class="col-lg-2">
                                        <label for="spc">5. Details of Additional Presenting Officer</label>
                                    </div>
                                    <div class="col-lg-9">
                                        <form:hidden path="apoHrmsId"/>
                                        <form:hidden path="apoSPC"/>
                                        ${pbean.additionalpresentingauthority}
                                    </div>
                                </div>
                            </div>
                        </div>
                        <c:if test="${pbean.hasIoAppointed ne 'Y'}">
                            <!--Minor penalty-->
                            <div class="panel panel-default" id="MinorPenaltyONpresentationOfDO">
                                <div class="panel-heading">
                                    OPSC Consultation
                                </div>
                                <div class="panel-body">
                                    <div class="row" style="margin-bottom: 7px;" id="serveDelinquentDetail">
                                        <div class="col-lg-2">
                                            <label>1.Letter No</label>
                                        </div>
                                        <div class="col-lg-2">
                                            ${pbean.consultationOrdnoOnRepresentationOfDoOnIoReport}
                                        </div>
                                        <div class="col-lg-2">
                                            <label> 2.Date</label>
                                        </div>
                                        <div class="col-lg-2">
                                            ${pbean.consultationOrddateOnRepresentationOfDoOnIoReport}
                                        </div>
                                    </div>

                                    <div class="col-lg-2">
                                        <label for="document">2.Document</label>
                                    </div>
                                    <c:if test="${not empty pbean.consultationOriginalfilenameOnRepresentationOfDoOnIoReport}">
                                        <a href="">
                                            <span class="glyphicon glyphicon-paperclip"></span> ${pbean.firstshowcauseoriginalFileName}</a>
                                        </c:if>

                                </div>

                                <div class="panel panel-default">
                                    <div class="panel-heading">
                                        OPSC Concurrence
                                    </div>
                                    <div class="panel-body">
                                        <div class="row" style="margin-bottom: 7px;" id="serveDelinquentDetail">
                                            <div class="col-lg-2">
                                                <label>1.Letter No</label>
                                            </div>
                                            <div class="col-lg-2">
                                                ${pbean.concurranceOrdnoOnRepresentationOfDoOnIoReport}
                                            </div>
                                            <div class="col-lg-2">
                                                <label> 2.Date</label>
                                            </div>
                                            <div class="col-lg-2">
                                                ${pbean.concurranceOrddateOnRepresentationOfDoOnIoReport}

                                            </div>
                                        </div>
                                        <div class="panel-body">
                                            <div class="row" style="margin-bottom: 7px;">  
                                                <div class="col-lg-2">
                                                    <label for="document">Document</label>
                                                </div>
                                                <c:if test="${not empty pbean.concurranceOriginalfilenameOnRepresentationOfDoOnIoReport}">
                                                    <a href="" class="btn btn-default">
                                                        <span class="glyphicon glyphicon-paperclip"></span> ${pbean.concurranceOriginalfilenameOnRepresentationOfDoOnIoReport}</a>
                                                    </c:if>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div class="panel panel-default">
                                    <div class="panel-heading">
                                        Final Order
                                    </div>
                                    <div class="row" style="margin-bottom: 7px;">
                                        <div class="col-lg-2">
                                            <label>1.Order No<span style="color: red">*</span></label>
                                        </div>
                                        <div class="col-lg-2">
                                            ${pbean.finalOrdnoOnRepresentationOfDoOnIoReport}
                                        </div>
                                        <div class="col-lg-2">
                                            <label> 2.Date<span style="color: red">*</span></label>
                                        </div>
                                        <div class="col-lg-2">
                                            ${pbean.finalOrddateOnRepresentationOfDoOnIoReport}
                                        </div>
                                    </div>
                                    <div class="panel-body">
                                        <div class="row" style="margin-bottom: 7px;">  
                                            <div class="col-lg-2">
                                                <label for="document">Document<span style="color: red">*</span></label>
                                            </div>

                                            <c:if test="${not empty pbean.finalOriginalfilenameOnRepresentationOfDoOnIoReport}">
                                                <a href="" class="btn btn-default">
                                                    <span class="glyphicon glyphicon-paperclip"></span> ${pbean.finalOriginalfilenameOnRepresentationOfDoOnIoReport}</a>
                                                </c:if>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:if>             


                        <div class="panel panel-default" id="exanaurationOnRepresentationOfDO">
                            <div class="panel-heading">
                                Final Order
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label>1.Order No<span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-2">
                                    ${pbean.exanaurationfinalOrdnoOnRepresentationOfDoOnIoReport}
                                </div>
                                <div class="col-lg-2">
                                    <label> 2.Date<span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-2">
                                    ${pbean.exanaurationfinalOrddateOnRepresentationOfDoOnIoReport}

                                </div>
                            </div>
                            <div class="panel-body">
                                <div class="row" style="margin-bottom: 7px;">  
                                    <div class="col-lg-2">
                                        <label for="document">Document<span style="color: red">*</span></label>
                                    </div>

                                    <c:if test="${not empty pbean.exanaurationfinalOriginalfilenameOnRepresentationOfDoOnIoReport}">
                                        <a href="">
                                            <span class="glyphicon glyphicon-paperclip"></span> ${pbean.exanaurationfinalOriginalfilenameOnRepresentationOfDoOnIoReport}</a>
                                        </c:if>
                                </div>
                            </div>
                        </div>
                    </c:if>

                    <c:if test="${pbean.hasIoAppointed eq 'Y'}">
                        <div class="panel panel-default" id="ioandPoandAporemarksDiv">
                            <div class="panel-heading">
                                I.O'S Findings
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label>1.Letter Number<span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-2">
                                    ${pbean.ioRemarksOrdNo}
                                </div>
                                <div class="col-lg-2">
                                    <label>2.Date<span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-2">
                                    ${pbean.ioRemarksOrdDt}
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;"> 
                                <div class="col-lg-2">
                                    <label for="document">3.Document(IO's Report)<span style="color: red">*</span></label>
                                </div>
                                <c:if test="${not empty pbean.remarksByIOoriginalFileName}">
                                    <a href="">
                                        <span class="glyphicon glyphicon-paperclip"></span>${pbean.defenceByDOoriginalFileName}</a>
                                    </c:if>
                            </div>
                        </div>
                       
                    </c:if>
                    <c:if test="${pbean.hasIoRemarks eq 'Y'}">
                        <div class="panel panel-default" id="serveToDelinquentOnIoRemarks">
                            <div class="panel-heading">
                                Notice to the Delinquent Officer
                            </div>
                            <div class="form-group row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label>1. Letter No<span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-2">
                                    ${pbean.ordNoForNoticetoDOOnIoRemark}
                                </div>
                                <div class="col-lg-2">
                                    <label> 2. Date<span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-2">
                                    ${pbean.ordDtForNoticeOnTODOIoRemark}
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;"> 
                                <div class="col-lg-2">
                                    <label for="document">3. Document(Notice to Delinquent)<span style="color: red">*</span></label>
                                </div>
                                <c:if test="${not empty pbean.noticetoDOOnIoRemarkoriginalFileName}">
                                    <a href="">
                                        <span class="glyphicon glyphicon-paperclip"></span>${pbean.noticetoDOOnIoRemarkoriginalFileName}</a>
                                    </c:if>
                            </div>

                        </div>
                        <div class="row" id="sendserveDelinquentOnIoRemarks" style="margin-bottom: 7px;">
                        </div>
                        <div class="panel panel-default" id="serveDelinquentOnIoRemarksDiv">                            
                            <div class="row" style="margin-bottom: 7px;" id="serveDelinquentDetail">
                                <div class="col-lg-2">
                                    <label>Date Of Service<span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-2">
                                    ${pbean.secondshowCauseOrdDt}
                                </div>
                            </div>
                        </div>
                        <div class="panel panel-default" id="reinquiryonDelinquentOfficerDiv" >
                            <div class="panel-heading">
                                Reinquiry against Delinquent Officer
                            </div>
                            <div class="form-group row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label>1.Office Order No<span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-2">
                                     ${pbean.showCauseOrdNo}
                                </div>
                                <div class="col-lg-2">
                                    <label> 2.Office Order Date<span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-2">
                                    ${pbean.showCauseOrdDt}
                                </div>
                            </div>
                        </div>
                    </c:if>

                    <c:if test="${pbean.hasSendSecondNotice eq 'Y'}">
                        <div class="panel panel-default" id="representationOfDelinquentOnIoRemarks">
                            <div class="panel-heading">
                                Representation  By Delinquent Officer
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label>1.Letter No</label>
                                </div>
                                <div class="col-lg-2">
                                    ${pbean.doRepresentationOnsecondshowCauseOrdNo}
                                </div>
                                <div class="col-lg-2">
                                    <label> 2.Date Of Receipt<span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-2">
                                    ${pbean.doRepresentationOnsecondshowCauseOrdDt}
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;"> 
                                <div class="col-lg-2">
                                    <label for="document">3.Document (Representation against IO's Report)<span style="color: red">*</span></label>
                                </div>
                                <c:if test="${not empty pbean.secondshowcauseoriginalFileNameondoRepresentation}">
                                    <a href="">
                                        <span class="glyphicon glyphicon-paperclip"></span>${pbean.secondshowcauseoriginalFileNameondoRepresentation}</a>
                                    </c:if>
                            </div>
                        </div>
                    </c:if>

                    <c:if test="${pbean.hasDoRepresentOnsecondshowCause eq 'Y'}">
                        <div class="panel panel-default" id="considerationByDA">
                            <div class="panel-heading">
                                Consideration By the Disciplinary Authority
                            </div>
                            <div class="panel panel-default" id="exanaurationDetailONpresentationOfDOonIoReport">
                                <div class="panel-heading">
                                    Final Order
                                </div>
                                <div class="row" style="margin-bottom: 7px;" id="serveDelinquentDetail">
                                    <div class="col-lg-2">
                                        <label>1.Order No<span style="color: red">*</span></label>
                                    </div>
                                    <div class="col-lg-2">
                                        ${pbean.exanaurationfinalOrdnoOnRepresentationOfDoOnIoReport}
                                    </div>
                                    <div class="col-lg-2">
                                        <label> 2.Date<span style="color: red">*</span></label>
                                    </div>
                                    <div class="col-lg-2">
                                        ${pbean.exanaurationfinalOrddateOnRepresentationOfDoOnIoReport}
                                    </div>
                                </div>
                                <div class="panel-body">
                                    <div class="row" style="margin-bottom: 7px;">  
                                        <div class="col-lg-2">
                                            <label for="document">Document<span style="color: red">*</span></label>
                                        </div>


                                        <c:if test="${not empty pbean.exanaurationfinalOriginalfilenameOnRepresentationOfDoOnIoReport}">
                                            <a href="">
                                                <span class="glyphicon glyphicon-paperclip"></span> ${pbean.exanaurationfinalOriginalfilenameOnRepresentationOfDoOnIoReport}</a>
                                            </c:if>
                                    </div>
                                </div>

                            </div>
                            <c:if test="${not empty pbean.doRepresentationOnsecondshowCauseOrdDt && not empty pbean.secondshowcauseoriginalFileNameondoRepresentation}">
                                <div class="row" style="margin-bottom: 7px;" id="PunishmentDetailONpresentationOfDOonIoReport">
                                    <table class="table table-bordered">
                                        <thead>
                                            <tr>
                                                <th>#</th>
                                                <th>Punishment Awarded</th>
                                                <th>With Effect Date</th>
                                                <th>Till Date</th>
                                                <th>Narration</th> 
                                            </tr>
                                        </thead>
                                        <tbody id="punishmenttbl">
                                            <c:forEach items="${punishdetailsList}" var="punishdetails" varStatus="cnt">
                                                <tr>
                                                    <td>${cnt.index+1}</td>
                                                    <td>${punishdetails.punishmenttypedesc}</td>
                                                    <td>${punishdetails.wefdate}</td>
                                                    <td>${punishdetails.tilldate}</td>
                                                    <td>${punishdetails.narration}</td>  
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>

                                </div> 
                            </c:if>

                        </div>
                    </c:if>
                    <div class="panel panel-default" id="thirdNoticeToDelinquentForPunishmentProposed">
                        <div class="panel-heading">
                            Notice Regarding Proposed Punishment
                        </div>
                        <div class="form-group row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label>1.Letter No<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">
                                 ${pbean.ordNoForthirdNoticetoDOOnForPunishment} 
                            </div>
                            <div class="col-lg-2">
                                <label> 2.Date<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">
                                ${pbean.ordDtForthirdNoticetoDOOnForPunishment}
                            </div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;"> 
                            <div class="col-lg-2">
                                <label for="document">3.Document(document For notice to delinquent)<span style="color: red">*</span></label>
                            </div>
                            <c:if test="${not empty pbean.thirdNoticetoDOOnForPunishmentorgFileName}">
                                <a href="">
                                    <span class="glyphicon glyphicon-paperclip"></span>${pbean.thirdNoticetoDOOnForPunishmentorgFileName}</a>
                                </c:if>

                        </div>
                        <div class="row" id="sendDelinquentthirdNotice" style="margin-bottom: 7px;">
                        </div>
                        <div class="panel panel-default" id="serveDelinquentthirdNoticeDetail">
                            <div class="row" style="margin-bottom: 7px;" id="serveDelinquentDetail">
                                <div class="col-lg-2">
                                    <label>Date Of Service<span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-2">
                                    ${pbean.thirdshowCauseOrdDt}
                                </div>
                            </div>
                        </div>
                    </div>

                    <c:if test="${pbean.hasSendthirdshowCause eq 'Y'}">
                        <div class="panel panel-default" id="representationOfDelinquentthirdShowcause">
                            <div class="panel-heading">
                                Representation Against Proposed Punishment
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label>1.Letter No</label>
                                </div>
                                <div class="col-lg-2">
                                    ${pbean.thirdshowCauseReplyByDAordNo}
                                </div>
                                <div class="col-lg-2">
                                    <label> 2.Date Of Receipt<span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-2">
                                    ${pbean.thirdshowCauseReplyByDAOrdDt}
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;"> 
                                <div class="col-lg-2">
                                    <label for="document">3.Document (Representation against IO's Report)<span style="color: red">*</span></label>
                                </div>
                                <c:if test="${not empty pbean.thirdshowcauseOriginalfilenameOnRepresentationOfDoOnIoReport}">
                                    <a href="" class="btn btn-default">
                                        <span class="glyphicon glyphicon-paperclip"></span>${pbean.thirdshowcauseOriginalfilenameOnRepresentationOfDoOnIoReport}</a>
                                    </c:if>
                            </div>
                        </div>
                    </c:if>


                    <c:if test="${pbean.hasReplyByDothirdshowCause eq 'Y'}">
                        <div class="panel panel-default" id="considerationByDA">
                            <div class="panel-heading">
                                Consideration By the Government
                            </div>
                            <div class="row" style="margin-bottom: 7px;" id="PunishmentAssignmentBygovt">
                                <table class="table table-bordered">
                                    <thead>
                                        <tr>
                                            <th>#</th>
                                            <th>Punishment Awarded</th>
                                            <th>With Effect Date</th>
                                            <th>Till Date</th>
                                            <th>Narration</th> 
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${punishdetailsList}" var="punishdetails" varStatus="cnt">
                                            <tr>
                                                <td>${cnt.index+1}</td>
                                                <td>${punishdetails.punishmenttypedesc}</td>
                                                <td>${punishdetails.wefdate}</td>
                                                <td>${punishdetails.tilldate}</td>
                                                <td>${punishdetails.narration}</td>  
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div> 

                            <div class="panel panel-default" id="PunishmentDetailONpresentationOfDOonafterAssignpunishment">
                                <div class="panel-heading">
                                    OPSC Consultation
                                </div>
                                <div class="panel-body">
                                    <div class="row" style="margin-bottom: 7px;" id="serveDelinquentDetail">
                                        <div class="col-lg-2">
                                            <label>1.Letter No</label>
                                        </div>
                                        <div class="col-lg-2">
                                            ${pbean.consultationOrdnoOnRepresentationOfDoOnIoReport}
                                        </div>
                                        <div class="col-lg-2">
                                            <label> 2.Date</label>
                                        </div>
                                        <div class="col-lg-2">
                                            ${pbean.consultationOrddateOnRepresentationOfDoOnIoReport}
                                        </div>
                                    </div>

                                    <div class="col-lg-2">
                                        <label for="document">2.Document</label>
                                    </div>
                                    <c:if test="${not empty pbean.consultationOriginalfilenameOnRepresentationOfDoOnIoReport}">
                                        <a href="" class="btn btn-default">
                                            <span class="glyphicon glyphicon-paperclip"></span> ${pbean.firstshowcauseoriginalFileName}</a>
                                        </c:if>
                                </div>

                                <div class="panel panel-default">
                                    <div class="panel-heading">
                                        OPSC Concurrence
                                    </div>
                                    <div class="panel-body">
                                        <div class="row" style="margin-bottom: 7px;" id="serveDelinquentDetail">
                                            <div class="col-lg-2">
                                                <label>1.Letter No</label>
                                            </div>
                                            <div class="col-lg-2">
                                                ${pbean.concurranceOrdnoOnRepresentationOfDoOnIoReport}
                                            </div>
                                            <div class="col-lg-2">
                                                <label> 2.Date</label>
                                            </div>
                                            <div class="col-lg-2">
                                                ${pbean.concurranceOrddateOnRepresentationOfDoOnIoReport}
                                            </div>
                                        </div>
                                        <div class="panel-body">
                                            <div class="row" style="margin-bottom: 7px;">  
                                                <div class="col-lg-2">
                                                    <label for="document">Document</label>
                                                </div>

                                                <c:if test="${not empty pbean.concurranceOriginalfilenameOnRepresentationOfDoOnIoReport}">
                                                    <a href="" class="btn btn-default">
                                                        <span class="glyphicon glyphicon-paperclip"></span> ${pbean.concurranceOriginalfilenameOnRepresentationOfDoOnIoReport}</a>
                                                    </c:if>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="panel panel-default">
                                    <div class="panel-heading">
                                        Final Order
                                    </div>
                                    <div class="row" style="margin-bottom: 7px;" id="serveDelinquentDetail">
                                        <div class="col-lg-2">
                                            <label>1.Order No<span style="color: red">*</span></label>
                                        </div>
                                        <div class="col-lg-2">
                                            ${pbean.finalOrdnoOnRepresentationOfDoOnIoReport}
                                        </div>
                                        <div class="col-lg-2">
                                            <label> 2.Date<span style="color: red">*</span></label>
                                        </div>
                                        <div class="col-lg-2">
                                            ${pbean.finalOrddateOnRepresentationOfDoOnIoReport}

                                        </div>
                                    </div>
                                    <div class="panel-body">
                                        <div class="row" style="margin-bottom: 7px;">  
                                            <div class="col-lg-2">
                                                <label for="document">Document<span style="color: red">*</span></label>
                                            </div>

                                            <c:if test="${not empty pbean.finalOriginalfilenameOnRepresentationOfDoOnIoReport}">
                                                <a href="" class="btn btn-default">
                                                    <span class="glyphicon glyphicon-paperclip"></span> ${pbean.finalOriginalfilenameOnRepresentationOfDoOnIoReport}</a>
                                                </c:if>
                                        </div>
                                    </div>
                                </div>
                            </div> 
                        </c:if>

                        <div class="panel panel-default" id="ExanaurationDetailONpresentationOfAfterAssignPunishment">
                            <div class="panel-heading">
                                Final Order
                            </div>
                            <div class="row" style="margin-bottom: 7px;" id="serveDelinquentDetail">
                                <div class="col-lg-2">
                                    <label>1.Order No<span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-2">
                                    ${pbean.exanaurationfinalOrdnoOnRepresentationOfDoOnIoReport}
                                </div>
                                <div class="col-lg-2">
                                    <label> 2.Date<span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-2">
                                    ${pbean.exanaurationfinalOrddateOnRepresentationOfDoOnIoReport}
                                </div>
                            </div>
                            <div class="panel-body">
                                <div class="row" style="margin-bottom: 7px;">  
                                    <div class="col-lg-2">
                                        <label for="document">Document<span style="color: red">*</span></label>
                                    </div>
                                    <c:if test="${not empty pbean.exanaurationfinalOriginalfilenameOnRepresentationOfDoOnIoReport}">
                                        <a href="" class="btn btn-default">
                                            <span class="glyphicon glyphicon-paperclip"></span> ${pbean.exanaurationfinalOriginalfilenameOnRepresentationOfDoOnIoReport}</a>
                                        </c:if>
                                </div>
                            </div>
                        </div>
                    </div>

                </div>

                <div class="panel-footer">
                    <input type="submit" name="action" value="Back" class="btn btn-default"/>
                </div>
            </div>
        </div>

    </form:form>
</body>
</html>



