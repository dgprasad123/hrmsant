<%-- 
    Document   : submitDefenceStatment 
    Created on : Jul 24, 2018, 4:11:27 PM
    Author     : manisha
--%>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ page contentType="text/html;charset=UTF-8"%>

<%
    String contextPath = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + contextPath + "/";
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
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
        <script type="text/javascript">
            function validation() {
                if ($("#uploadDocument").val() == "") {
                    alert("please upload document");
                    $("#uploadDocument").focus();
                    return false;
                }
            }
            function validation() {
                if ($("#defenceRemark").val() == "") {
                    alert("please enter the Remark");
                    $("#defenceRemark").focus();
                    return false;
                }
            }
        </script>
    </head>
    <body>
        <div id="page-wrapper">
            <form:form action="savedefenceStatementByDo.htm" commandName="defencebean" method="post" class="form-horizontal" enctype="multipart/form-data">
                <div class="container-fluid">
                    <div class="panel panel-default">
                        <div class="panel-heading" align="center">MEMORANDUM</div>
                        <div class="panel-body">
                            <p><b>
                                    <c:forEach items="${dpviewbean.delinquent}" var="delinquent">
                                        ${delinquent.doEmpName}, ${delinquent.doEmpCurDegn}
                                    </c:forEach>
                                </b> 
                                is here by informed that it is proposed to take Departmental Action against him/her under Rule 15 of the Orissa Civil Services (Classification, Control and Appeal)
                                Rules, 1962. The Substance of the imputation in respect of which the inquiry is proposed to be held is set out in the enclosed statement of articles of charges(Annexure-I)
                                . The Statement of imputations in support of the article of charges is enclosed(Annexure-II) along with Memo of Evidence (Annexure-III) .<br><br><br></p>


                            1. <b>${dpviewbean.initAuthName}, ${dpviewbean.initAuthCurDegn}</b> 
                            is hereby given an opportunity to make representations as ${dpviewbean.gender} wishes to make against the Proposed Disciplinary action.<br><br> 


                            2.  ${dpviewbean.gender} may peruse the relevant records in the office of 

                            and take relevant extracts thereof to submit ${dpviewbean.gender1} written statement of defence with permission from the competetent authority.<br><br>

                            3. If ${dpviewbean.gender} fails to submit ${dpviewbean.gender1} representation within 15 days of the receipt of this Memorandum, it will be presumed that  ${dpviewbean.gender} has no representation to submit and 
                            orders as deemed proper will be passed against him/her in accordance of the law.<br><br>


                            4. The receipt of the memorandum should be acknowledged  by sri/smt <b> ${dpviewbean.approveAuthName},${dpviewbean.approveAuthCurDegn}</b>


                        </div>
                    </div>
                    <div class="panel panel-default">
                        <div class="panel-heading" align="center">ANNEXURE-1 <br/> ARTICLE OF CHARGE</div>
                        <div class="panel-body">
                            <p><c:forEach items="${dpviewbean.delinquent}" var="delinquent"  >
                                    ${delinquent.doEmpName}, ${delinquent.doEmpCurDegn}
                                </c:forEach> has committed following irregularities:</p>
                            <p> <b>${dpviewbean.irrgularDetails}</u></b>
                            <p>Thus the following article of charge are framed against him for violation of Rule of the Odisha Government Servants Conduct Rules, 1959.
                                <br><br><br>

                                <c:forEach items="${dpviewbean.chargeListOnly}" var="chargeBean" varStatus="cnt">
                                    ${cnt.index+1}. ${chargeBean.articleOfCharge}<br>
                                </c:forEach>
                            </p>

                        </div>
                    </div>
                    <div class="panel panel-default">
                        <div class="panel-heading" align="center">ANNEXURE-II <br/> STATEMENT of Imputations on MISCONDUCT</div>
                        <div class="panel-body">

                            <p><c:forEach items="${dpviewbean.delinquent}" var="delinquent"  >
                                    ${delinquent.doEmpName}, ${delinquent.doEmpCurDegn}
                                </c:forEach>

                                has committed gross irregularities for which the following articles of charge are framed against him for violating
                                Rule 3 of the Orissa Government servants conduct Rules, 1959.<br><br><br></p>

                            <c:forEach items="${dpviewbean.chargeListOnly}" var="chargeBean" varStatus="cnt">
                                ${cnt.index+1}. ${chargeBean.statementOfImputation}<br>
                            </c:forEach>
                        </div>
                    </div>
                    <div class="panel panel-default">
                        <div class="panel-heading" align="center">ANNEXURE-III <br/> Memo of Evidence</div>
                        <div class="panel-body">                            

                            <p>Documents by which the charge is proposed to be sustained<br><br></p>

                            <c:forEach items="${dpviewbean.chargeListOnly}" var="chargeBean" varStatus="cnt">
                                ${cnt.index+1}. ${chargeBean.memoOfEvidence}<br>
                            </c:forEach>
                            <%-- <p><c:forEach items="${discchargelist}" var="articleOfCharge" varStatus="cnt">
                                    <a href="downloadEmployeeAttachment.htm?attachmentid=${articleOfCharge.uploadFileId}" class="btn btn-default" >
                                        <span class="glyphicon glyphicon-paperclip"></span> ${cnt.index+1} ${articleOfCharge.uploadFileName}
                                    </a>
                                    <br>
                                </c:forEach></p> --%>
                        </div>
                    </div>
                    <div class="panel panel-default">
                        <div class="panel-body">                       
                            <div class="form-group">
                                <label class="control-label col-sm-2" >Defence Statement Remark </label>
                                <div class="col-sm-8">  
                                    <form:hidden path="defid"/>
                                    <form:hidden path="dadid"/>
                                    <form:hidden path="taskId"/>
                                    <form:hidden path="daId"/>                                    
                                    <form:textarea class="form-control" path="defenceRemark"/>                                    
                                </div>
                            </div>



                            <div class="form-group">
                                <label class="control-label col-sm-2">Browse</label>
                                <div class="col-sm-2">  
                                    <input type="file" name="defenceByDOdocument"  id="defenceByDOdocument" class="form-control-file"/>
                                </div>
                            </div>

                        </div>
                        <div class="panel-footer">
                            <c:if test="${empty taskdtls.istaskcompleted}">
                                <input type="submit" name="action" value="Save" class="btn btn-default" onclick="return validation()"/>                            
                                <input type="submit" name="action" value="Delete" class="btn btn-default"  onclick="return confirm('Are you sure to Delete?')"/>
                            </c:if>
                        </div>
                    </div>
                </div>
            </form:form>
        </div>
    </body>
</html>