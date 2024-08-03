<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<% int i = 1;
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Apply LTC: Basic Info</title>
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">   
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <script src="js/moment.js" type="text/javascript"></script>
        <script type="text/javascript" src="js/jquery.min.js"></script>
        <script type="text/javascript" src="js/bootstrap.min.js"></script>  
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script type="text/javascript">
            function validateForm() {
                if ($('#visitPlace').val() == "") {
                    alert("Please enter Visit Place.");
                    $('#visitPlace').focus();
                    return false;
                }
                if ($('#dateOfCommencement').val() == "") {
                    alert("Please select Date of Commencement.");
                    $('#dateOfCommencement').focus();
                    return false;
                }
                if ($('#leaveType').val() == "") {
                    alert("Please select Leave Type.");
                    return false;
                }
                if ($('#fromDate').val() == "") {
                    alert("Please select From Date.");
                    return false;
                }
                if ($('#toDate').val() == "") {
                    alert("Please select To Date.");
                    return false;
                }
                if ($('#placeofVisit').val() == "") {
                    alert("Please enter Place of Visit.");
                    return false;
                }
                if ($('#visitState').val() == "") {
                    alert("Please enter Visit State.");
                    return false;
                }
                if ($('#modeOfJourney').val() == "") {
                    alert("Please enter Mode of Journey.");
                    return false;
                }
                if ($('#appropriateDistance').val() == "") {
                    alert("Please enter Appropriate Distance.");
                    return false;
                }
                //$('#btn_save')[0].disabled = true;
                return true;
            }
        </script>
    </head>
    <body style="padding-top: 10px;">
        <jsp:include page="ProfileTabs.jsp">
            <jsp:param name="menuHighlight" value="BASICINFO" />
            <jsp:param name="ltcID" value="${ltcId}" />
        </jsp:include>
        <div style="width:95%;margin:0px auto;border-top:0px;">
            <div class="row" style="border: 1px solid #ddd;padding: 5px;">
                <form:form action="SaveBasicInfo.htm" method="post" commandName="LTCBean" id="frmLTC">
                    <form:hidden path="ltcId"/>
                    <div class="container-fluid">
                        <div class="panel panel-default">
                            <div class="panel-heading" style="margin-bottom:10px;padding-bottom:5px;color:#0052A7">

                                <strong style="font-size:14pt;">Apply for LTC:: <span style="color:#890000;">Basic Information</span></strong>
                            </div>
                            <div>

                                <div class="row" style="margin-bottom: 7px;">
                                    <div class="col-lg-2">
                                        <label for="orderNumber">Name:<span style="color: red">*</span></label>
                                    </div>
                                    <div class="col-lg-2">   
                                        ${empName}
                                    </div>
                                    <div class="col-lg-2">
                                        <label for="orderDate"> Designation<span style="color: red">*</span></label>
                                    </div>
                                    <div class="col-lg-2">${designation}</div>
                                </div>
                                <!--<div class="row" style="margin-bottom: 7px;">
                                    <div class="col-lg-2">
                                        <label for="orderNumber">Date of Birth:<span style="color: red">*</span></label>
                                    </div>
                                    <div class="col-lg-2">   
        
                                    </div>
                                    <div class="col-lg-2">
                                        <label for="orderDate"> Date of First Entry to Govt.<span style="color: red">*</span></label>
                                    </div>
                                </div>-->                     
                                <div class="row" style="margin-bottom: 7px;">
                                    <div class="col-lg-2">
                                        <label for="orderNumber">Intended Place of Visit:<span style="color: red">*</span></label>
                                    </div>
                                    <div class="col-lg-2">   
                                        <form:input class="form-control" path="visitPlace" id="visitPlace" />
                                    </div>
                                    <div class="col-lg-2">
                                        <label for="orderDate"> Date of Commencement of outward journey (proposed):<span style="color: red">*</span></label>
                                    </div>
                                    <div class="col-lg-2">
                                        <div class='input-group date' id='processDate'>
                                            <form:input class="form-control" path="dateOfCommencement" id="dateOfCommencement" readonly="true"/>
                                            <span class="input-group-addon">
                                                <span class="glyphicon glyphicon-time"></span>
                                            </span>
                                        </div>
                                    </div>                            
                                </div>
                                <div class="row" style="margin-bottom: 7px;">
                                    <div class="col-lg-2">
                                        <label for="orderNumber">Leave Type:<span style="color: red">*</span></label>
                                    </div>
                                    <div class="col-lg-6">   
                                        <form:select path="leaveType" id="leaveType" size="1" class="form-control">
                                            <form:option value="" label="--Select--"/>
                                            <form:options items="${leaveTypes}" itemValue="value" itemLabel="value"/>
                                        </form:select>
                                    </div>

                                </div>   
                                <div class="row" style="margin-bottom: 7px;">
                                    <div class="col-lg-2">
                                        <label for="durationFrom">From Date:<span style="color: red">*</span></label>
                                    </div>
                                    <div class="col-lg-2">
                                        <div class='input-group date' id='processDate'>
                                            <form:input class="form-control" path="fromDate" id="fromDate" readonly="true"/>
                                            <span class="input-group-addon">
                                                <span class="glyphicon glyphicon-time"></span>
                                            </span>
                                        </div>
                                    </div>
                                    <div class="col-lg-2">
                                        <label for="durationFrom">To Date:<span style="color: red">*</span></label>
                                    </div>
                                    <div class="col-lg-2">
                                        <div class='input-group date' id='processDate'>
                                            <form:input class="form-control" path="toDate" id="toDate" readonly="true"/>
                                            <span class="input-group-addon">
                                                <span class="glyphicon glyphicon-time"></span>
                                            </span>
                                        </div>
                                    </div>                                    
                                </div>


                                <div class="row" style="margin-bottom: 7px;">
                                    <div class="col-lg-10">
                                        <table width="100%" class="table tabler-bordered" style="border:1px solid #CCC">
                                            <tr bgcolor="#EAEAEA">
                                                <td></td>
                                                <td colspan="2"><strong>Details of Visit</strong></td>
                                                <td></td>
                                                <td>&nbsp;</td>
                                            </tr>
                                            <tr>
                                                <td width="20"></td>
                                                <td width="20">(a)</td>
                                                <td width="350">Place of Visit</td>
                                                <td width="300"><form:input class="form-control" path="placeofVisit" /></td>
                                                <td>&nbsp;</td>
                                            </tr> 
                                            <tr>
                                                <td></td>
                                                <td>(b)</td>
                                                <td>State of Visit</td>
                                                <td><form:input class="form-control" path="visitState" /></td>
                                                <td>&nbsp;</td>
                                            </tr> 
                                            <tr>
                                                <td></td>
                                                <td>(c)</td>
                                                <td>Mode of Journey</td>
                                                <td><form:input class="form-control" path="modeOfJourney" /></td>
                                                <td>&nbsp;</td>
                                            </tr>
                                            <tr>
                                                <td></td>
                                                <td>(d)</td>
                                                <td>Approximate Distance both ways</td>
                                                <td><form:input class="form-control" path="appropriateDistance" /></td>
                                                <td>&nbsp;</td>
                                            </tr>                                  
                                        </table>
                                    </div>
                                </div>


                            </div>

                            <div >
                                <button type="submit" name="submit" value="Save" id="btn_save" class="btn btn-default btn-xlg" style="background:#008900;color:#FFF;" onclick="return validateForm();">Save</button>
                                <a class="btn btn-default btn-xlg" style="background:#008900;color:#FFF;text-align:center;text-decoration:none;" href="EmpLTCList.htm">Cancel</a>


                            </div>
                        </div>
                    </div>




                </form:form>
            </div>
        </div>
        <script type="text/javascript">
            $(function() {
                $('#fromDate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
                $('#toDate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
                $('#dateOfCommencement').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
            });
        </script>            
    </script>
</body>
</html>
