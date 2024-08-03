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
        <c:set var="l1total" value="0"/>
        <c:set var="l1tota2" value="0"/>
        <c:set var="l1tota3" value="0"/>
        <c:set var="l1tota4" value="0"/>
        <c:set var="l1tota5" value="0"/>
        <c:set var="l1tota6" value="0"/>
        <c:set var="l1tota7" value="0"/>
        <c:set var="l1tota8" value="0"/>
        <c:set var="l1tota9" value="0"/>
        <c:set var="l1total0" value="0"/>
        <c:set var="l1total1" value="0"/>
        <c:set var="l1total2" value="0"/>
        <c:set var="l1total3" value="0"/>
        <c:set var="l1total4" value="0"/>
        <c:set var="l1total5" value="0"/>
        <c:set var="l1total6" value="0"/>
        <c:set var="l1total7" value="0"/>
        <c:set var="gtotal7" value="0"/>
                <div id="wrapper">
            <jsp:include page="../tab/hrmsadminmenu.jsp"/>
            <c:set var="htotal" value="0"/>
            <c:set var="vtotal" value="0"/>
        <form:form action="newProposalMaster.htm" method="post" commandName="IncrementProposal">
            <div class="container-fluid">
                <div class="panel panel-default">

                                    <div class="row" style="margin:10px 0px;margin-left:20px;font-size:14pt;">
                                        <p><span  style="color:#666666;text-decoration:underline;">Name of the Department: <strong>${deptName}</strong></span>
                                            <select name="departmentId"  class="form-control" id='departmentId' onchange="self.location='empCadreMIS.htm?deptCode='+this.value"<c:if test = "${ldeptCode ne '11'}"> disabled="disabled"</c:if>>
                                                <option value="">-All Departments-</option>
                                                <c:forEach items="${deptlist}" var="dList" varStatus="count">
                                                    <option value="${dList.deptCode}"<c:if test = "${deptCode eq dList.deptCode}"> selected="selected"</c:if>  >${dList.deptName}</option>
                                                </c:forEach>
                                            </select>                                               
                                        </p>
                                    </div>
                    <div class="panel-heading" style="font-weight:bold;font-size:15pt;">
                        Cadre Wise Report
                    </div>
                    <div class="panel-body">
                        <table class="table table-bordered">
                            <thead>
                                <tr>
                                    <th width="100" rowspan="2">Sl No.</th>
                                    <th rowspan="2">Cadre Code</th>
                                    <th rowspan="2">Cadre Name</th>
                                    <th colspan="18" style="background:#FFE3C9">Number of employees drawing salary in HRMS</th>
                                </tr>
                                <tr>
                                    <th>L-1</th>
                                    <th>L-2</th>
                                    <th>L-3</th>
                                    <th>L-4</th>
                                    <th>L-5</th>
                                    <th>L-6</th>
                                    <th>L-7</th>
                                    <th>L-8</th>
                                    <th>L-9</th>
                                    <th>L-10</th>
                                    <th>L-11</th>
                                    <th>L-12</th>
                                    <th>L-13</th>
                                    <th>L-14</th>
                                    <th>L-15</th>
                                    <th>L-16</th>
                                    <th>L-17</th>
                                    <th>Total</th>
                                </tr>
                                    
                            </thead>
                            <tbody>
                                <c:forEach items="${cadreList}" var="dList" varStatus="slnoCnt">
                                    <c:set var="l1total" value="${l1total + dList.l1}" />
                                    <c:set var="l2total" value="${l2total + dList.l2}" />
                                    <c:set var="l3total" value="${l3total + dList.l3}" />
                                    <c:set var="l4total" value="${l4total + dList.l4}" />
                                    <c:set var="l5total" value="${l5total + dList.l5}" />
                                    <c:set var="l6total" value="${l6total + dList.l6}" />
                                    <c:set var="l7total" value="${l7total + dList.l7}" />
                                    <c:set var="l8total" value="${l8total + dList.l8}" />
                                    <c:set var="l9total" value="${l9total + dList.l9}" />
                                    <c:set var="l10total" value="${l01total + dList.l10}" />
                                    <c:set var="l11total" value="${l11total + dList.l11}" />
                                    <c:set var="l12total" value="${l12total + dList.l12}" />
                                    <c:set var="l13total" value="${l13total + dList.l13}" />
                                    <c:set var="l14total" value="${l14total + dList.l14}" />
                                    <c:set var="l15total" value="${l15total + dList.l15}" />
                                    <c:set var="l16total" value="${l16total + dList.l16}" />
                                    <c:set var="l17total" value="${l17total + dList.l17}" />
                                    <c:set var="gtotal" value="${gtotal + dList.total}" />
                                    <tr>
                                        <td>${slnoCnt.index+1}</td>
                                        <td>${dList.cadreCode}</td>
                                        <td>${dList.cadreName}</td>
                                    <th>${dList.l1}</th>
                                    <th>${dList.l2}</th>
                                    <th>${dList.l3}</th>
                                    <th>${dList.l4}</th>
                                    <th>${dList.l5}</th>
                                    <th>${dList.l6}</th>
                                    <th>${dList.l7}</th>
                                    <th>${dList.l8}</th>
                                    <th>${dList.l9}</th>
                                    <th>${dList.l10}</th>
                                    <th>${dList.l11}</th>
                                    <th>${dList.l12}</th>
                                    <th>${dList.l13}</th>
                                    <th>${dList.l14}</th>
                                    <th>${dList.l15}</th>
                                    <th>${dList.l16}</th>
                                    <th>${dList.l17}</th>
                                    <th style="background:#ADD1FF;">${dList.total}</th>
                                        </tr>
                                
                                </c:forEach>
                                        <tr style="background:#ADD1FF;font-weight:bold;">
                                        <td colspan="3" align="right">Grand Total</td>

                                    <th>${l1total}</th>
                                    <th>${l2total}</th>
                                    <th>${l3total}</th>
                                    <th>${l4total}</th>
                                    <th>${l5total}</th>
                                    <th>${l6total}</th>
                                    <th>${l7total}</th>
                                    <th>${l8total}</th>
                                    <th>${l9total}</th>
                                    <th>${l10total}</th>
                                    <th>${l11total}</th>
                                    <th>${l12total}</th>
                                    <th>${l13total}</th>
                                    <th>${l14total}</th>
                                    <th>${l15total}</th>
                                    <th>${l16total}</th>
                                    <th>${l17total}</th>
                                    <th>${gtotal}</th>
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
        </form:form>
                </div>
    </body>
</html>



