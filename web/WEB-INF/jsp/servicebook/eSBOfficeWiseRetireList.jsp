<%-- 
    Document   : Increment Proposal List
    Created on : 20 Jun, 2016, 12:14:12 PM
    Author     : Surendra
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link rel="stylesheet" href="css/sb-admin.css">
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>        
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script src="js/moment.js" type="text/javascript"></script>
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>

        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>        
        <script type="text/javascript">


        </script>




        <script type="text/javascript">
            var monthNames = ["JAN", "FEB", "MAR", "APR", "MAY", "JUN",
                "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"
            ];


            $(document).ready(function() {
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

            });

            function openInputForm(proposalId) {
                $('#dd').dialog('open');
                $('#eventForm').form('clear');
                $('#proposalId').val(proposalId);
            }

            function myformatter(date) {
                var y = date.getFullYear();
                var m = date.getMonth();
                var d = date.getDate();
                //alert(date);
                //(d < 10 ? ('0' + d) : d) + '-' + (m < 10 ? ('0' + m) : m) + '-' + y;
                return (d < 10 ? ('0' + d) : d) + '-' + monthNames[m] + '-' + y;
            }
            function myparser(s) {
                if (!s)
                    return new Date();
                var ss = (s.split('-'));
                var found = $.inArray(ss[1], monthNames);
                var y = parseInt(ss[0], 10);
                var m = parseInt(found + 1, 10);
                var d = parseInt(ss[2], 10);
                if (!isNaN(y) && !isNaN(m) && !isNaN(d)) {
                    return new Date(d, m - 1, y); //d + '-' + monthNames[m - 1] + '-' + y;
                } else {
                    return new Date();
                }
            }




            function openInputForm(proposalId) {
                $('#dd').dialog('open');
                $('#eventForm').form('clear');
                $('#proposalId').val(proposalId);
            }
            function deleteProposal(proposalId)
            {
                if (confirm("Are you sure you want to remove the Proposal?"))
                {
                    $.ajax({
                        type: "get",
                        url: 'DeleteIncrementProposal.htm',
                        data: 'proposalId=' + proposalId,
                        cache: false,
                        success: function(retVal) {
                            self.location = 'displayProposalListpage.htm?offCode=';
                        }
                    });
                }
            }

        </script>
    </head>

    <body>
        <div id="wrapper">
            <jsp:include page="../tab/hrmsadminmenu.jsp"/>
            <input type="hidden" id="deptCode" name="deptCode" value="${deptCode}" />
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="row" style="margin:10px 0px;margin-left:20px;font-size:14pt;">
                        <p><a href="eSBDeptWiseRetireList.htm?year=${year}">Department Wise Report</a> &raquo;
                            <span  style="color:#666666;text-decoration:underline;">${deptName}</span></p>
                    </div>
                    <!--<div class="row" style="margin:10px 0px;">
        <div class="col-lg-1" style="text-align:right;">Year:</div>
        <div class="col-lg-2"><select class="form-control" name="year" id="year"><option value="2020"<c:if test="${year == '2020'}"> selected="selected"</c:if>>2020</option>
            <option value="2021"<c:if test="${year == '2021'}"> selected="selected"</c:if>>2021</option>
            <option value="2022"<c:if test="${year == '2022'}"> selected="selected"</c:if>>2022</option></select></div>
        <div class="col-lg-2"><input type="button" value="Search" class="btn btn-primary" onclick="self.location='eSBOfficeWiseRetireList.htm?deptCode=${deptCode}&year='+$('#year').val()" /></div>
    </div>-->  
                    <div style="position:fixed;top:100px;right:30px;text-align:right;">
                            <%--<input type="button" value="DownloadExcel" 
                                   class="btn btn-md btn-primary"/>--%>
                            <button type="button" class="btn btm-md btn-danger"><a href="downloadeSBOfficeWiseRetireList.htm?deptCode=${deptCode}"><b style="color:black">Download Excel</b></a></button>
                            <%--<input type="submit" name="DownloadExcel" value="DownloadExcel" class="btn btn-md btn-primary"/>--%>
                        </div>
                    <div class="panel-heading" style="font-weight:bold;font-size:15pt;">
                        Service Book Monitoring Office Wise Report
                    </div>
                    <div class="panel-body">
                        <table class="table table-bordered">
                            <thead>
                                <tr>                                   
                                    <th width="100" style="text-align:center" >Sl No.<br>(1)</th>
                                    <th style="text-align:center" >Office Name<br>(2)</th>
                                    <th style="text-align:center">Total Employees<br>(3)</th>
                                    <th style="text-align:center">Total Employees retiring on or before 31st December 2023<br>(4)</th>
                                    <th style="text-align:center">SB Update Completed<br>(5)</th>
                                    <th style="text-align:center">SB Update Not Completed
                                        <br>(Col.4 - Col.5)<br>(6)</th>
                                    <th style="text-align:center">% Completed<br>(7)</th>                                    
                                    <th style="text-align:center">Total Employees retiring after 31st December 2023<br>(8)</th>
                                    <th style="text-align:center">SB Update Completed<br>(9)</th>
                                    <th style="text-align:center">SB Update Not Completed <br>retiring after 31st December 2023
                                        <br style="background-color: blue;align: center">(Col.8 - Col.9)<br>(10)</th>
                                    <th style="text-align:center">Grand Total<br> SB Update Completed<br>(Col.5+Col.9)<br>(11)</th>
                                    <th style="text-align:center">% Completed<br>(12)</th>  
                                    <th style="text-align:center">Grand Total<br> SB Update Not Completed<br>(Col.6+Col.10)<br>(13)</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${officeList}" var="dList" varStatus="slnoCnt">
                                    <tr<c:if test="${dList.retiredEmp > 0}"> bgcolor="#FFEDE0"</c:if>>
                                        
                                        <td>${slnoCnt.index+1}</td>
                                        <td><a href="eSBEmpWiseRetireList.htm?deptCode=${deptCode}&offCode=${dList.offCode}&year=${year}">${dList.officeName} (${dList.offCode})</a></td>
                                        <td><a href="eSBOffAllEmpWiseRetireList.htm?offCode=${dList.offCode}&year=${year}&type=total">${dList.totalEmp}</a></td>
                                        <td>${dList.retiredEmp}</td>
                                        <td><c:if test="${(dList.sbCompleted) ne '0'}"><a href="eSBOffEmpWiseRetireList.htm?offCode=${dList.offCode}&year=${year}&type=current">${dList.sbCompleted}</a></c:if></td>
                                        <td>${dList.retiredEmp-dList.sbCompleted}</td>
                                        <td><c:if test="${dList.retiredEmp ne '0'}"><fmt:formatNumber type = "text" maxFractionDigits="2" value = "${(dList.sbCompleted/dList.retiredEmp)*100}" pattern="#" />%</c:if></td>                                        
                                        <td>${dList.totalEmp - dList.retiredEmp}</td>
                                        <td><c:if test="${(dList.sbCompletedAfter) ne '0'}"><a href="eSBOffEmpWiseRetireList.htm?offCode=${dList.offCode}&year=${year}&type=after">${dList.sbCompletedAfter}</a></c:if></td>
                                        <td>${(dList.totalEmp - dList.retiredEmp)-(dList.sbCompletedAfter)}</td>
                                        <td>${dList.sbCompleted+dList.sbCompletedAfter}</td>
                                        <td><c:if test="${(dList.totalEmp - dList.retiredEmp) ne '0'}"><fmt:formatNumber type = "text" maxFractionDigits="2" value = "${(dList.sbCompletedAfter/(dList.totalEmp - dList.retiredEmp))*100}" pattern="#" />%</c:if></td> 
                                        <td>${(dList.retiredEmp-dList.sbCompleted)+(dList.totalEmp - dList.retiredEmp)-(dList.sbCompletedAfter)}</td>                                     
                                        
                                        </tr>

                                </c:forEach>
                            </tbody>
                        </table>
                    </div>

                    <!-- MODAL WINDOW FOR FORWARD MPR WORK STARTS -->    
                    <div aria-hidden="true" aria-labelledby="myModalLabel" role="dialog" tabindex="-1" id="myModal1" class="modal fade">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <button aria-hidden="true" data-dismiss="modal" class="close" type="button">×</button>
                                    <h4 class="modal-title">  Update Order No & Order Date </h4>
                                </div>
                                <div class="modal-body">
                                    <div class="row">
                                        <div class="col-md-6 form-group">
                                            <span style="color:#FF0000;">* </span> Order Number:<br />
                                            <input type="text" name="orderNumber" id="orderNumber" class="form-control" />
                                        </div>
                                        <div class="col-md-6 form-group">
                                            <span style="color:#FF0000;">* </span> Order Date:</label>

                                            <div class='input-group date' id='processDate'><input type="text"  id="orderDate" name="orderDate" readonly="readonly" class="form-control" />
                                                <span class="input-group-addon">
                                                    <span class="glyphicon glyphicon-time"></span>
                                                </span></div>                                                   
                                        </div>

                                        <div class="col-md-12 form-group" style="text-align:center;"> 
                                            <input type ="button" class="btn btn-primary" value="Save" onclick="javascript: validateUpdate()" />
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div> 

                </div>
            </div>
        </div>
    </body>
</html>



