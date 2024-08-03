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
                    <div class="panel-heading" style="font-weight:bold;font-size:15pt;">
                        Service Book Monitoring Office Wise Report Employees retiring on or before 31st December 2023
                    </div>
                    <div class="panel-body">
                        <table class="table table-bordered">
                            <thead>
                                <tr>
                                    <th width="100">Sl No.</th>
                                    <th>Office Name</th>
                                    <th>Total Employees</th>
                                    <th colspan="5">No. of Employees whose service book needs to be updated</th>
                                                    <th>No. of SBs updated by DA as on ${curDate}</th>
                                                    <th>No. of SBs authenticated by EO and access given to employees as on ${curDate}</th>
                                                    <th>No. of complaints received</th>
                                                    <th>No. of complaints Redressed</th>                                    
                                    <th>Total SB Update Completed</th>
                                </tr>
                                <tr style="font-weight:bold;">
                                    <td></td>
                                    <td></td>
                                    <td></td>
                                    
                                                    <td>A</td>
                                                    <td>B</td>
                                                    <td>C</td>
                                                    <td>D</td>
                                                    <td>Total</td>
                                                    <td colspan="5"></td>


                                </tr>
                            </thead>
                            <tbody>
                                                                <c:set var="total" value="${0}"/>
                                <c:set var="total1" value="${0}"/>
                                <c:set var="total2" value="${0}"/>
                                <c:set var="total3" value="${0}"/>
                                <c:set var="total4" value="${0}"/>
                                <c:set var="total5" value="${0}"/>
                                <c:forEach items="${officeList}" var="dList" varStatus="slnoCnt">
                                    <tr<c:if test="${dList.retiredEmp > 0}"> bgcolor="#FFEDE0"</c:if>>
                                        <td>${slnoCnt.index+1}</td>
                                        <td><a href="eSBEmpWiseRetireList.htm?deptCode=${deptCode}&offCode=${dList.offCode}&year=${year}">${dList.officeName} (${dList.offCode})</a></td>
                                        <td>${dList.totalEmp}</td>
                                        
                                                    <td>${dList.groupATotal}</td>
                                                    <td>${dList.groupBTotal}</td>
                                                    <td>${dList.groupCTotal}</td>
                                                    <td>${dList.groupDTotal}</td>
                                                    <td>${dList.retiredEmp}</td>
                                                    <td><c:if test="${(dList.sbCompleted) ne '0'}"><a href="eSBOffEmpWiseRetireList.htm?offCode=${dList.offCode}&year=${year}&type=current">${dList.sbCompleted}</a></c:if></td>
                                                    <td>${dList.numValidated}</td>
                                                    <td>0</td>
                                                    <td>0</td>
                                        <td><c:if test="${(dList.sbCompleted) ne '0'}"><a href="eSBOffEmpWiseRetireList.htm?offCode=${dList.offCode}&year=${year}&type=current">${dList.sbCompleted}</a></c:if></td>
                                        </tr>
                                        <c:set var="total" value="${total + dList.groupATotal}" />
                                        <c:set var="total1" value="${total1 + dList.groupBTotal}" />
                                        <c:set var="total2" value="${total2 + dList.groupCTotal}" />
                                        <c:set var="total3" value="${total3 + dList.groupDTotal}" />                                        
                                        <c:set var="total4" value="${total4 + dList.retiredEmp}" />                                        
                                        <c:set var="total5" value="${total5 + dList.totalEmp}" />                                        
                                </c:forEach>
                                        <tr style="font-weight:bold;">
                                            <td></td>
                                            <td>Total</td>
                                            <td>${total5}</td>
                                            <td>${total}</td>
                                            <td>${total1}</td>
                                            <td>${total2}</td>
                                            <td>${total3}</td>
                                            <td>${total4}</td>
                                        </tr>
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



