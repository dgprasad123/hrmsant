<%-- 
    Document   : ServiceBookInValidatorAllGroupWise
    Created on : Dec 29, 2021, 12:23:20 PM
    Author     : Madhusmita
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        <link href="css/sb-admin.css" rel="stylesheet">
        <link rel="stylesheet" href="css/chosen.css">

        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>
        <script src="js/chosen.jquery.min.js"></script>
        <script type="text/javascript">
            function invalidateServiceBook() {
                var checkBoxArray = "";
                //var spc = $('#postcode').val();
                //alert("SPC is:" + spc);
                $("input[name='chkInvalidator']:checked").each(function() {
                    if (checkBoxArray == "") {
                        checkBoxArray = $(this).val();
                    } else {
                        checkBoxArray = checkBoxArray + "," + $(this).val();
                    }
                });
                //alert(checkBoxArray);
                if (checkBoxArray == "") {
                    alert("Please Select Service Book Details");
                } else {
                    //alert(checkBoxArray);
                    var url = 'inValidateSBGroupWise.htm?checkedSBModule=' + checkBoxArray + '&empId=' + $('#hidempId').val();
                    //var datamodule = [checkBoxArray.split(",")];
                    //var status = datamodule.length;
                    //alert(status);                    
                    //var url="under_const.jsp";
                    $.ajax({
                        url: url,
                        dataType: "json",
                        success: function(data) {
                            //alert(data.status);
                            if (data.status != '') {
                                $("#msg").addClass("alert-success");
                                $("#msg").text(data.status + " DATA INVALIDATED");
                                $('#inValidatebutton').hide();
                                //$('#hideButton').show();
                                $('#showButton').show();
                            } else {
                                $("#msg").addClass("alert-danger");
                                $("#msg").text("NO DATA FOUND..");
                            }
                        }
                    });
                }
            }
            $(document).ready(function() {
                $('#chkAllSBModule').click(function() {
                    $('#chkNotification').prop('checked', this.checked);
                    $('#chkJoining').prop('checked', this.checked);
                    $('#chkReleive').prop('checked', this.checked);
                    $('#chkSVerification').prop('checked', this.checked);
                    $('#chkExam').prop('checked', this.checked);
                    $('#chkTraining').prop('checked', this.checked);
                    //alert('so');
                })
            });



        </script>
    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/hrmsadminmenu.jsp"/>        
            <div id="page-wrapper">
                <div class="container-fluid">
                    <form:form action="ServiceBookInValidatorAllGroupWise.htm" method="POST" commandName="command">
                        <input type="hidden" id="hidempId" value="${empid}"/>
                        <div>
                            <div class="table-responsive">
                                <h3>INVALIDATE SERVICE BOOK</h3>
                                <div class="row" >
                                    <div class="col-lg-12" style="text-align:center;font-weight: bold;">
                                        <h3><c:out value="${empid}"/>-<c:out value="${empprofile.empName}"/></h3>
                                        <%-- <c:out value="${empprofile.empName}"/>--%>
                                    </div>
                                </div>
                                <table class="table table-bordered table-hover table-striped">
                                    <thead>
                                        <tr style="background-color:lightblue">
                                            <th style="text-align: center;font-size: 17px">Select All<input type="checkbox" name="chkAll" id="chkAllSBModule" value="chkAllSBModule"  class="form-control" /></th>
                                            <th style="font-size: 17px;vertical-align: text-top">SERVICE BOOK DETAILS</th>                                       
                                        </tr>
                                    </thead>

                                    <tbody id="abstractList">
                                        <tr>
                                            <td>
                                                <input type="checkbox" name="chkInvalidator" id="chkNotification" value="EMP_NOTIFICATION~NOTIFICATION" class="form-control" />
                                            </td>
                                            <td>
                                                <label style="color: lightcoral">Notification Data </label><BR/>
                                                <label style="font-size:12px">(PAY FIXATION,PAY REVISION,POSTING,PROMOTION,TRANSFER,ABSORPTION,ADDITIONAL CHARGE,ADMINISTRATIVE ACTION/PUNISHMENT,
                                                    ALLOTMENT TO CADRE,ALLOWANCES,BRASS ALLOTMENT, CANCELLATION,CHANGE IN STRUCTURE,COMPLIANCE,CONFIRMATION,DECEASED,DEPUTATION ,
                                                    DEPUTATION_AG,DETENTION OF VACATION,ENROLLMENT TO INSURANCE SCHEME,FIRST APPOINTMENT,FTC,INCREMENT,JOINING IN CADRE,LEAVE,LOAN SANCTION ,
                                                    REPAYMENT OF LOAN , LTC , LONG TERM TRAINING,MISCELLANEOUS,ALLOWED TO OFFICIATE,PAY ENTITLEMENT,QUARTER ALLOTMENT,QUARTER SURRENDER,
                                                    REDESIGNATION, REGULARIZATION OF SERVICE,REHABITILATION,SUSPENSION,REINSTATEMENT,RELIEVE FROM CADRE,REPATRIATION,RESIGNATION,
                                                    RETIREMENT,REWARD/APPRECIATION,SERVICE DISPOSAL,TRAINING,VALIDATION,DEPARTMENTAL EXAMINATION)</label><br>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <input type="checkbox" name="chkInvalidator" id="chkJoining" value="EMP_JOIN~JOINING" class="form-control" />
                                            </td>
                                            <td>
                                                <label style="color: lightcoral">Joining Data</label><br>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <input type="checkbox" name="chkInvalidator" id="chkReleive" value="EMP_RELIEVE~RELIEVE" class="form-control" />
                                            </td>
                                            <td>
                                                <label style="color: lightcoral">Relieve Data</label><br>
                                            </td>
                                        </tr>
                                       
                                        <tr>
                                            <td>
                                                <input type="checkbox" name="chkInvalidator" id="chkExam" value="EMP_EXAM~EXAMINATION"  class="form-control" />
                                            </td>
                                            <td>
                                                <label style="color: lightcoral">Examination Data</label><br>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <input type="checkbox" name="chkInvalidator" id="chkTraining" value="EMP_TRAIN~TRAINING"   class="form-control" />
                                            </td>
                                            <td>
                                                <label style="color: lightcoral">Training Data</label><br>
                                            </td>
                                        </tr>
                                         <tr>
                                            <td>
                                                <input type="checkbox" name="chkInvalidator"  id="chkSVerification"  value="EMP_SV~SERVICE VERIFICATION" class="form-control" />
                                            </td>
                                            <td>
                                                <label style="color: lightcoral">Service Verification Data</label><br>
                                            </td>
                                        </tr>

                                    </tbody>
                                </table>
                                <table class="table table-bordered table-hover table-striped">
                                    <input type="button" name="btnGroupWisePrivilegeBtn" id="inValidatebutton" value="InValidate" class="btn btn-success" onclick="invalidateServiceBook();"/>&nbsp;
                                    <%--<input type="button" id="hideButton" value="InValidate" class="btn btn-primary" style="display:none;"/>&nbsp;--%>
                                    <button type="button" id="showButton" class="btn btn-success" style="display:none" disabled="true" >InValidate</button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                    <span id="msg" style="font-size: 18px; text-align:center"></span>
                                   
                                </table>
                            </div>
                        </div>
                    </form:form>
                </div>
            </div>
        </div>
    </body>
</html>
