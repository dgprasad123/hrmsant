<%-- 
    Document   : mergeDuplicateHrmsid
    Created on : Feb 11, 2022, 4:28:41 PM
    Author     : Madhusmita
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
    <head>
        <<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        <link href="css/sb-admin.css" rel="stylesheet">
        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>
        <script type="text/javascript">
            //var dupDate ='${mform.srcDob}';
            //var orgDate ='${mform.finalDob}';
            function viewDupIDSalary() {
                var dupTrData = "";

                if ($('#srcEmpId').val() == "") {
                    alert("Enter Duplicate Hrmsid");
                    return false;
                } else {
                    $('#lastSalList').empty();
                    $('#empHeading').empty();
                    var url = 'viewLastSalaryJSON.htm?empid=' + $("#srcEmpId").val();
                    $.getJSON(url, function(data) {
                        $.each(data, function(i, obj) {
                            dupTrData = dupTrData + "<tr>" +
                                    "<td>" + (i + 1) + "</td>" +
                                    "<td>" + obj.aqMonth + "</td>" +
                                    "<td>" + obj.aqYear + "</td>" +
                                    " </tr>";
                        });
                        if (dupTrData == "") {
                            alert("SALARY NOT PREPARED");
                        }
                        $('#empHeading').append("HRMS ID:").append($("#srcEmpId").val());
                        $('#lastSalList').append(dupTrData);

                    });

                }
            }

            function viewFinalIDSalary() {
                var finTrData = "";
                if ($('#finalEmpId').val() == "") {
                    alert("Enter Original Hrmsid");
                    return false;
                } else {
                    $('#lastSalList').empty();
                    $('#empHeading').empty();
                    var url = 'viewLastSalaryJSON.htm?empid=' + $("#finalEmpId").val();
                    $.getJSON(url, function(data) {
                        $.each(data, function(i, obj) {
                            finTrData = finTrData + "<tr>" +
                                    "<td>" + (i + 1) + "</td>" +
                                    "<td>" + obj.aqMonth + "</td>" +
                                    "<td>" + obj.aqYear + "</td>" +
                                    "</tr>";
                        });
                        if (finTrData == "") {
                            alert("SALARY NOT PREPARED");
                        }
                        $('#empHeading').append("HRMS ID:").append($("#finalEmpId").val());
                        $('#lastSalList').append(finTrData);
                    });

                }

            }

            function checkEmpId() {

                if ($('#srcEmpId').val() == "") {
                    alert("Enter Duplicate Hrmsid");
                    return false;
                }
                if ($('#finalEmpId').val() == "") {
                    alert("Enter Original Hrmsid");
                    return false;
                }
                if ($('#srcEmpId').val() != "" && $('#finalEmpId').val() != "") {
                    alert('View Last Salary');
                    return true;
                }             
                

            }

            $(document).ready(function() {
                $("#idconfirmmerging").click(function() {
                    var checkStatus = this.checked;
                    if (checkStatus == true) {
                        $("#mergeBtn").removeAttr("disabled");

                    } else {
                        $("#mergeBtn").attr("disabled", "disabled");
                    }
                })
            });

        </script>
        <style>
            .empheading
            {
                font-weight: bold;
                font-size: 25px;
            }

        </style>

    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/hrmsadminmenu.jsp"/>        
            <div id="page-wrapper">    

                <div class="container-fluid">

                    <div class="row">
                        <div class="col-lg-12">                            
                            <ol class="breadcrumb">
                                <li>
                                    <i class="fa fa-dashboard"></i>  <a href="index.html">Dashboard</a>
                                </li>
                                <li class="active">
                                    <i class="fa fa-file"></i> Merging Duplicate HrmsID 
                                </li>                                
                            </ol>
                        </div>
                    </div>              
                    <div style="text-align:center;">
                        <form:form action="mergeDuplicateHrmsid.htm" commandName="mergeDuplicateHrmsid" method="POST">
                            <form:hidden path="usertype" id="hidUsertype"/>
                            <div class="row">
                                <div class="col-lg-12">
                                    <h3 style="margin-top:0px;"><u>Merging Duplicate HrmsID</u></h3> <br/><br/>                                   
                                    <h3 style="color:darkred"><b>${msgPreviouslyMerged}</b></h3>
                                    <h4 style="color:red"><b>${msgForBank}</b></h4>
                                    <h4 style="color:red"><b>${msgForDesignation}</b></h4>
                                    <h4 style="color:green"><b>${msgCompleted}</b></h4>                                    
                                    <div class="panel-body">
                                        <div class="row">
                                            <div class="col-lg-12">

                                                <table border="0" width="60%"  cellspacing="0" style="font-size:12px; font-family:verdana;">
                                                    <tr style="height: 30px; "> 
                                                        <td style="color:black;font-size: 20px;">
                                                            <label for="srcEmpId">Enter Duplicate HrmsId</label>
                                                        </td>
                                                        <td colspan="3" align="center" style="color:red;font-size: 20px;padding-right: 20px;">
                                                            <form:input path="srcEmpId" class="form-control" maxlength="8"/>
                                                        </td>

                                                        <td colspan="4" align="center" >
                                                            <a href="#" onclick="viewDupIDSalary();" data-remote="false" data-toggle="modal" data-target="#viewSalary" >
                                                                <button type="button" style="width:90%" class="btn btn-primary"  >View Last Salary</button> </a>  
                                                        </td>

                                                    </tr> 
                                                    <br/> 
                                                    <tr style="height: 30px; "> 
                                                        <td style="color:black;font-size: 20px;">
                                                            <label for="finalEmpId">Enter Original HrmsId</label>
                                                        </td>
                                                        <td colspan="3" align="center" style="color:red;font-size: 20px; padding-right: 20px;">
                                                            <form:input path="finalEmpId" class="form-control" maxlength="8"/>
                                                        </td>                                                        
                                                        <td colspan="4">
                                                            <a href="#" onclick="viewFinalIDSalary();" data-remote="false" data-toggle="modal" data-target="#viewSalary">
                                                                <button type="button" style="width:90%" class="btn btn-primary" >View Last Salary</button></a>
                                                        </td>
                                                        <td align="right"  class="btn btn-primary">
                                                            <input type="submit" class="form-control" value="Verify Both HrmsID" style="color:black;" onclick="return checkEmpId()"/>                                        
                                                        </td>
                                                    </tr>   
                                                </table> 
                                            </div>
                                        </div>
                                        <br/><br/>

                                        <div>
                                            <c:if test="${not empty mform}" >
                                                <div class="col-lg-6" style="color:red">
                                                    <table  align="left" border="2"  width="100%" height="80%" cellspacing="1" style="font-size:12px; font-family:verdana;">
                                                        <tr style="height:40px;text-align: left">
                                                            <td><b>Duplicate HRMS ID:</b>&nbsp;&nbsp;${mform.srcEmpId} </td>                                                                
                                                        </tr>
                                                        <tr style="height:40px;text-align: left">
                                                            <td><b>Employee Name:</b> &nbsp;&nbsp;${mform.srcEmpNm} </td>

                                                        </tr>
                                                        <tr style="height:40px;text-align: left">
                                                            <td><b>Gpf No:</b> &nbsp;&nbsp;${mform.srcGpfNo} </td>

                                                        </tr>
                                                        <tr style="height:40px;text-align: left">
                                                            <td><b>Date Of Birth:</b> &nbsp;&nbsp;${mform.srcDob} </td>                                                            
                                                        </tr>
                                                        <tr style="height:40px;text-align: left">
                                                            <td><b>Date Of Superannuation:</b> &nbsp;&nbsp;${mform.srcDos} </td>
                                                        </tr>
                                                        <tr style="height:40px;text-align: left">
                                                            <td><b>Employee Type:</b> &nbsp;&nbsp;${mform.srcEmpType} </td>
                                                        </tr>
                                                        <tr style="height:40px;text-align: left">
                                                            <td><b>Designation:</b> &nbsp;&nbsp;${mform.srcDesignation} </td>
                                                        </tr>
                                                        <tr style="height:40px;text-align: left">
                                                            <td><b>Bank Account No:</b>&nbsp;&nbsp;${mform.srcBankAcctNo}</td>
                                                        </tr>
                                                        <tr style="height:40px;text-align: left">
                                                            <td><b>IFSC Code:</b>&nbsp;&nbsp;${mform.srcIfscCode}</td>
                                                        </tr>
                                                        <tr style="height:40px;text-align: left">
                                                            <td><b>Mobile No:</b>&nbsp;&nbsp;${mform.srcMobile}</td>
                                                        </tr>                                                        
                                                    </table>
                                                </div>                                            
                                                <div class="col-lg-6" style="color:green">
                                                    <table align="right" border="2"  width="100%" height="80%" cellspacing="1" style="font-size:12px; font-family:verdana;">
                                                        <tr style="height:40px;text-align: left">
                                                            <td><b>Original HRMS ID: </b>&nbsp;&nbsp;${mform.finalEmpId}</td>
                                                        </tr>
                                                        <tr style="height:40px;text-align: left">
                                                            <td><b>Employee Name:</b> &nbsp;&nbsp;${mform.finalEmpNm} </td>

                                                        </tr>
                                                        <tr style="height:40px;text-align: left">
                                                            <td><b>Gpf No: </b>&nbsp;&nbsp;${mform.finalGpfNo} </td>

                                                        </tr>
                                                        <tr style="height:40px;text-align: left">
                                                            <td><b>Date Of Birth:</b> &nbsp;&nbsp;${mform.finalDob} </td>                                                            
                                                        </tr>
                                                        <tr style="height:40px;text-align: left">
                                                            <td><b>Date Of Superannuation:</b> &nbsp;&nbsp;${mform.finalDos} </td>
                                                        </tr>
                                                        <tr style="height:40px;text-align: left">
                                                            <td><b>Employee Type: </b>&nbsp;&nbsp;${mform.finalEmpType} </td>
                                                        </tr>
                                                        <tr style="height:40px;text-align: left">
                                                            <td><b>Designation:</b> &nbsp;&nbsp;${mform.finalDesignation} </td>
                                                        </tr>
                                                        <tr style="height:40px;text-align: left">
                                                            <td><b>Bank Account No:</b>&nbsp;&nbsp;${mform.finalBankAcctNo}</td>
                                                        </tr>
                                                        <tr style="height:40px;text-align: left">
                                                            <td><b>IFSC Code:</b>&nbsp;&nbsp;${mform.finalIfscCode}</td>
                                                        </tr>
                                                        <tr style="height:40px;text-align: left">
                                                            <td><b>Mobile No:</b>&nbsp;&nbsp;${mform.finalMobile}</td>                                                        
                                                        </tr>                                                   
                                                    </table>
                                                </div>
                                            </c:if>                                            
                                            <div class="panel-footer">
                                                <c:if test="${usrtype=='A'}">

                                                <c:if test="${empty msgPreviouslyMerged}">
                                                    <label>
                                                        <h4><input type="checkbox" name="idconfirmmerging" id="idconfirmmerging" style="margin-left: 1px;" />&nbsp;&nbsp;&nbsp;<span style="margin-right: 100px;">Do You wants to Merge Both HRMS ID ?</span></h4>
                                                    </label>
                                                </c:if>
                                                
                                                    <a href="mergeBothHrmsid.htm?dupId=${mform.srcEmpId}&finId=${mform.finalEmpId}"> <button type="button" style="width:10%" class="btn btn-success" id="mergeBtn" disabled="true" onclick="return checkEmpId();"> MERGE</button></a>                                                         
                                                </c:if>
                                            </div>                                            
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div id="viewSalary" class="modal fade" role="dialog">
                                <div class="modal-dialog">
                                    <!-- Modal content-->
                                    <div class="modal-content modal-lg">
                                        <div class="modal-header">
                                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                                            <h3 class="table-active"><u>Salary Drawn for the Months And Years</u></h3>
                                            <h4 id="empHeading" class="empheading"><B><U></U></B></h4>
                                        </div>
                                        <div class="modal-body" >
                                            <table class="table" border="1"  cellspacing="10"  style="font-size:12px; font-family:verdana;" id="tabl1"> 
                                                <thead>
                                                    <tr >
                                                        <th style="text-align:center;">Sl No</th>
                                                        <th style="text-align:center;">Month</th>
                                                        <th style="text-align:center;">Year</th>
                                                    </tr>
                                                </thead>
                                                <tbody id="lastSalList">                                                    

                                                </tbody>
                                            </table>  

                                        </div>
                                        <div class="modal-footer">                       
                                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="clear"></div>
                        </form:form>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
