<%-- 
    Document   : SelectedDelinquentOfficerListForRule16ongoingDP
    Created on : 19 Jan, 2021, 2:48:55 PM
    Author     : Manisha
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

        <script type="text/javascript">
            $(document).ready(function() {
                $("#alreadyservedDiv").hide();
                $("#sendshowcauseDiv").hide();
                $("#exonerationDetail").hide();
                $("#PunishmentDetailONpresentationOfDO").hide();


                $('.datepickerclass').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
            });
            function getDeptWiseOfficeList() {
                var deptcode = $('#hidAuthDeptCode').val();
                $('#hidAuthOffCode').empty();
                var url = 'getOfficeListJSON.htm?deptcode=' + deptcode;
                $('#hidAuthOffCode').append('<option value="">--Select Office--</option>');
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#hidAuthOffCode').append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');
                    });
                });
            }
            function getOfficeWisePostList() {
                var offcode = $('#hidAuthOffCode').val();
                $('#authSpc').empty();
                var url = 'getOfficeWithSPCList.htm?offcode=' + offcode;
                url = 'getSanctioningSPCOfficeWiseListJSON.htm?offcode=' + offcode;
                $('#authSpc').append('<option value="">--Select Post--</option>');
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#authSpc').append('<option value="' + obj.spc + '">' + obj.postname + '</option>');
                    });
                });
            }
            function radioClicked() {
                $("#alreadyservedDiv").hide();
                $("#sendshowcauseDiv").hide();
                var radioValue = $("input[name='showcause']:checked").val();
                if (radioValue == "AlreadyServed") {
                    $("#alreadyservedDiv").show();
                    $("#sendshowcauseDiv").hide();
                } else if (radioValue == "NotServed") {
                    $("#sendshowcauseDiv").show();
                    $("#alreadyservedDiv").hide();
                }
            }
            function radioClickedfordefenceremark() {
                var radioValue = $("input[name='defenceremark']:checked").val();
                if (radioValue == "MinorPenalty") {
                    $("#PunishmentDetailONpresentationOfDO").show();
                    $("#exonerationDetail").hide();
                } else if (radioValue == "Exoneration") {
                    $("#exonerationDetail").show();
                    $("#PunishmentDetailONpresentationOfDO").hide();
                }
            }

        </script>
    </head>
    <body>
        <div class="container-fluid">
            <div class="panel panel-default">
                <form:form action="editRule16ProceedingForOngoingDP.htm" method="post" commandName="ProceedingBean">
                    <div class="form-group">
                        <label class="control-label col-sm-2">Delinquent Officer Name:</label>
                         <c:forEach items="${delinquentofficerList}" var="delinquent">
                            <input type="hidden" name="delinquent" value="${delinquent.empid}-${delinquent.spc}"/>
                            ${delinquent.fullname},${delinquent.spn}<br/>
                        </c:forEach>
                    </div>


                    <div class="panel panel-default">
                        <div class="panel-heading">
                            Details Of Memorandum Of Charge
                        </div>
                        <div class="panel-body">
                            <div class="form-group row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label>1.Memorandum No<span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-2">
                                    <form:input class="form-control" path="showCauseOrdNo" maxlength="51"/> 
                                </div>
                                <div class="col-lg-2">
                                    <label> 2.Memorandum Date<span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-2">
                                    <div class="input-group date" id="processDate">
                                        <form:input class="form-control datepickerclass"  path="showCauseOrdDt" readonly="true"/> 
                                        <span class="input-group-addon">
                                            <span class="glyphicon glyphicon-time"></span>
                                        </span>
                                    </div>                                
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="spc">3.Disciplinary Authority Detail</label>
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


                    <div class="panel panel-default">
                        <div class="panel-heading">
                            Article Of Charge, Statement Of Imputation, Memo Of Evidence
                        </div>
                        <div class="panel-body">
                            <div class="row" style="margin-bottom: 7px;">                            
                                <div class="col-lg-2">
                                    <div class="panel-footer">
                                        <form:hidden path="daId"/>
                                        <c:if test="${iseditable eq true}">
                                            <input type="submit" name="action" class="btn btn-default" value="Add New"/>
                                        </c:if>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>


                    <div class="panel panel-default">
                        <div class="panel-heading">
                             Whether Served Or Not 
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2"> 
                                <input type="radio" id="ShowcauseIssued" name="showcause" value="AlreadyServed" onclick="radioClicked()"> <b>Already Served</b>
                            </div>
                            <div class="col-lg-2">
                                <input type="radio" id="ShowcauseNotIssued" name="showcause" value="NotServed" onclick="radioClicked()">  <b>Not Served</b>
                            </div>  
                        </div>
                        <div class="panel-body" id="alreadyservedDiv" >
                            <div class="row" style="margin-bottom: 7px;"> 
                                <div class="col-lg-2">
                                    <label> 1.Date Of Receipt<span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-2">
                                    <div class="input-group date" id="processDate">
                                        <form:input class="form-control datepickerclass"  path="showCauseOrdDt" readonly="true"/> 
                                        <span class="input-group-addon">
                                            <span class="glyphicon glyphicon-time"></span>
                                        </span>
                                    </div>                                
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">  
                                <div class="col-lg-2">
                                    <label for="document">2.Document</label>
                                </div>
                                <div class="form-group row" id="showcausedocument">                            
                                    <input type="file" name="" id=""  class="form-control-file"/>
                                </div> 
                            </div>
                        </div>
                        <div class="row" id="sendshowcauseDiv" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <input type ="button" value="Send Show Cause" />
                            </div>
                        </div>
                    </div>

                    <div class="panel panel-default">
                        <div class="panel-heading">
                            Written Statement By Delinquent Officer
                        </div>
                        <div class="panel-body">
                            <div class="row" style="margin-bottom: 7px;"> 
                                <div class="col-lg-2">
                                    <label> 1.Date Of Receipt<span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-2">
                                    <div class="input-group date" id="processDate">
                                        <form:input class="form-control datepickerclass"  path="showCauseOrdDt" readonly="true"/> 
                                        <span class="input-group-addon">
                                            <span class="glyphicon glyphicon-time"></span>
                                        </span>
                                    </div>                                
                                </div>
                            </div>

                            <div class="row" style="margin-bottom: 7px;">  
                                <div class="col-lg-2">
                                    <label for="document">2.Document<span style="color: red">*</span></label>
                                </div>
                                <div class="form-group row" id="writtenStatementByDOdocument">                            
                                    <input type="file" name="" id=""  class="form-control-file"/>
                                </div> 
                            </div>
                        </div>
                    </div>


                    <div class="row" style="margin-bottom: 7px;">
                        <div class="col-lg-2">
                            <input type="radio" id="DefenceRemarkRejected" name="defenceremark" value="MinorPenalty" onclick="radioClickedfordefenceremark()"><b>Minor Penalty</b>
                        </div> 
                        <div class="col-lg-2">
                            <input type="radio" id="DefenceRemarkRejected" name="defenceremark" value="Exoneration" onclick="radioClickedfordefenceremark()"><b>Exoneration</b>
                        </div> 
                    </div>



                    <div class="panel panel-default" id="PunishmentDetailONpresentationOfDO">
                        <div class="panel-heading">
                            OPSC Reference
                        </div>
                        <div class="panel-body">
                            <div class="row" style="margin-bottom: 7px;" id="serveDelinquentDetail">
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
                                    <div class="input-group date" id="processDate">
                                        <form:input class="form-control datepickerclass" path="ioAppoinmentOrdDt" readonly="true"/> 
                                        <span class="input-group-addon">
                                            <span class="glyphicon glyphicon-time"></span>
                                        </span>
                                    </div>                                
                                </div>
                            </div>
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    Final Order
                                </div>
                                <div class="panel-body">
                                    <div class="row" style="margin-bottom: 7px;">  
                                        <div class="col-lg-2">
                                            <label for="document">Document</label>
                                        </div>
                                        <div class="form-group row" id="writtenStatementByDOdocument">                            
                                            <input type="file" name="" id=""  class="form-control-file"/>
                                        </div> 
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="panel panel-default" id="exonerationDetail">
                        <div class="panel-heading">
                            Final Order
                        </div>
                        <div class="panel-body">
                            <div class="row" style="margin-bottom: 7px;">  
                                <div class="col-lg-2">
                                    <label for="document">Document</label>
                                </div>
                                <div class="form-group row" id="writtenStatementByDOdocument">                            
                                    <input type="file" name="" id=""  class="form-control-file"/>
                                </div> 
                            </div>
                        </div>
                    </div>

                    <div class="panel-footer">
                        <input type="submit" name="action" value="Save" class="btn btn-default" onclick="return confirm('Are you sure to Choose?')"/>
                        <input type="submit" name="action" value="Back" class="btn btn-default"/>
                    </div>

                </div>
            </div>

            <div id="authorityModal" class="modal" role="dialog">
                <div class="modal-dialog">
                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Sanctioning Authority</h4>
                        </div>
                        <div class="modal-body">
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="sltDept">Department</label>
                                </div>
                                <div class="col-lg-9">
                                    <select name="hidAuthDeptCode" id="hidAuthDeptCode" class="form-control" onchange="getDeptWiseOfficeList();">
                                        <option value="">--Select Department--</option>
                                        <c:forEach items="${deptlist}" var="dept">
                                            <option value="${dept.deptCode}">${dept.deptName}</option>
                                        </c:forEach>                                        
                                    </select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="note">Office</label>
                                </div>
                                <div class="col-lg-9">
                                    <select name="hidAuthOffCode" id="hidAuthOffCode" class="form-control" onchange="getOfficeWisePostList();">
                                        <option value="">--Select Office--</option>
                                        <c:forEach items="${sancOfflist}" var="toffice">
                                            <option value="${toffice.offCode}">${toffice.offName}</option>
                                        </c:forEach>                                        
                                    </select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="note">Post</label>
                                </div>
                                <div class="col-lg-9">
                                    <select path="authSpc" id="authSpc" class="form-control" onchange="getPost();">
                                        <option value="">--Select Post--</option>
                                        <c:forEach items="${sancPostlist}" var="post">
                                            <option value="${post.spc}">${post.postname}</option>
                                        </c:forEach>                                         
                                    </select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        </div>
                    </div>
                </div>
            </div>    
        </form:form>
    </body>
</html>


