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
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <script  type="text/javascript">
            function deleteCandidate(candidateId, categoryId)
            {
                if (confirm("Are you sure you want to delete the Candidate?"))
                    self.location = 'deleteCandidate.htm?candidateId=' + candidateId + '&categoryId=' + categoryId;
            }
            function total_grand_report() {
                self.location = 'totalsummarydeptRaSchemeList.htm';

            }

        </script>
        <style type="text/css">
            table.form-table td{padding:5px;}
        </style>
    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/deptmismenu.jsp"/>               
            <!-- Site wrapper -->
            <div class="wrapper">            
                <div id="page-wrapper">
                    <div class="container-fluid">
                        <!-- Page Heading -->
                        <div class="row">
                            <div class="col-lg-12">                            

                            </div>
                        </div>
                        <div class="row" style="min-height:600px">
                            <div class="col-lg-12">
                                <h2 style="margin-top:0px;">Department Wise Rehabilitation Scheme Summary </h2>

                                <form:form role="form" action="summarydeptRaSchemeList.htm" commandName="deptRaSchemeList"  method="post">

                                    <table width="100%" cellspacing="1" cellpadding="4" border="0" bgcolor="#EAEAEA" class="form-table" style="border:1px solid #CCCCCC;" >

                                        <tr>
                                            <td align="right">Month:</td>
                                            <td>
                                                <Select name="month"  class="form-control" required="1">
                                                    <option value="">Select Month</option>
                                                    <option value="January"  <c:if test = "${not empty selectmonth && selectmonth=='January'}"> <c:out value='selected="selected"'/></c:if>>January </option>
                                                    <option value="February"  <c:if test = "${not empty selectmonth && selectmonth=='February'}"> <c:out value='selected="selected"'/></c:if>>February</option>
                                                    <option value="March" <c:if test = "${not empty selectmonth && selectmonth=='March'}"> <c:out value='selected="selected"'/></c:if>>March</option>
                                                    <option value="April" <c:if test = "${not empty selectmonth && selectmonth=='April'}"> <c:out value='selected="selected"'/></c:if>>April</option>
                                                    <option value="May" <c:if test = "${not empty selectmonth && selectmonth=='May'}"> <c:out value='selected="selected"'/></c:if>>May</option>
                                                    <option value="June" <c:if test = "${not empty selectmonth && selectmonth=='June'}"> <c:out value='selected="selected"'/></c:if>>June</option>
                                                    <option value="July" <c:if test = "${not empty selectmonth && selectmonth=='July'}"> <c:out value='selected="selected"'/></c:if>>July</option>
                                                    <option value="August"  <c:if test = "${not empty selectmonth && selectmonth=='August'}"> <c:out value='selected="selected"'/></c:if>>August</option>
                                                    <option value="September" <c:if test = "${not empty selectmonth && selectmonth=='September'}"> <c:out value='selected="selected"'/></c:if>>September</option>
                                                    <option value="October" <c:if test = "${not empty selectmonth && selectmonth=='October'}"> <c:out value='selected="selected"'/></c:if>>October</option>
                                                    <option value="November" <c:if test = "${not empty selectmonth && selectmonth=='November'}"> <c:out value='selected="selected"'/></c:if>>November</option>
                                                    <option value="December" <c:if test = "${not empty selectmonth && selectmonth=='December'}"> <c:out value='selected="selected"'/></c:if>>December</option>
                                                    </select>
                                                </td>
                                                <td align="right">Year:</td>
                                                <td>
                                                    <Select name="year"  class="form-control" required="1">
                                                        <option value="">Select Year</option>
                                                        <option value="2017" <c:if test = "${not empty selectyear && selectyear=='2017'}"> <c:out value='selected="selected"'/></c:if>>2017 </option>
                                                    <option value="2018"  <c:if test = "${not empty selectyear && selectyear=='2018'}"> <c:out value='selected="selected"'/></c:if>>2018 </option>				
                                                    <option value="2019"  <c:if test = "${not empty selectyear && selectyear=='2019'}"> <c:out value='selected="selected"'/></c:if>>2019 </option>
                                                    <option value="2020"  <c:if test = "${not empty selectyear && selectyear=='2020'}"> <c:out value='selected="selected"'/></c:if>>2020 </option>
                                                    </select>   
                                                </td>
                                            </tr>                                         
                                            <tr>
                                                <td colspan="4" align="center"><input type="submit" class="btn btn-primary" value="Submit"  />&nbsp;<input type="button" class="btn btn-primary" value="Total Summary Report" onclick="total_grand_report()" /></td>
                                            </tr>
                                        </table>
                                        <!-- /.box-body -->


                                </form:form>
                                <c:if test = "${not empty selectyear}">        
                                    <table id="account" class="table table-bordered table-striped" >
                                        <thead>

                                            <tr style='background-color:black;color:white;font-size:14px'>
                                                <th>#</th>
                                                <th valign='top'>Department Name</th>
                                                <th valign='top'>Total No.of cases under R.A Scheme</th>
                                                <th valign='top'>Cumulative No. of cases considered for appointment</th>
                                                <th valign='top'>Cumulative No. of cases considered and rejected </th>
                                                <th valign='top'>Total no of cases cleared</th>
                                                <th valign='top'>Balance no of cases pending</th>



                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:set var="totalCaseTotal" value="${0}" />
                                            <c:set var="totalPendingTotal" value="${0}" />
                                            <c:set var="totalAppointmentTotal" value="${0}" />

                                            <c:set var="totalRejectedTotal" value="${0}" />
                                            <c:set var="totalCleared" value="${0}" />
                                            <c:set var="slno" value="${0}" />
                                            <c:set var="totalAppointment" value="${0}" />
                                            <c:set var="totalRejected" value="${0}" />
                                            <c:set var="totalCle" value="${0}" />
                                            <c:set var="totalpend" value="${0}" />

                                            <c:if test = "${not empty raschemeList}"> 
                                                <c:forEach items="${raschemeList}" var="rascheme">
                                                    <c:set var="totalAppointment" value="${ rascheme.totalAppointment + rascheme.totalAppoLastmonth}" />
                                                    <c:set var="totalRejected" value="${rascheme.totalRejected + rascheme.totalRejLastmonth}" />
                                                    <c:set var="totalCle" value="${totalAppointment+totalRejected}" />
                                                    <c:set var="totalpend" value="${rascheme.totalCase-totalCle}" />

                                                    <c:set var="totalCaseTotal" value="${totalCaseTotal + rascheme.totalCase}" />
                                                    <c:set var="totalPendingTotal" value="${totalPendingTotal + totalAppointment}" />

                                                    <c:set var="totalAppointmentTotal" value="${totalAppointmentTotal +  totalRejected}" />
                                                    <c:set var="totalRejectedTotal" value="${totalRejectedTotal + totalCle}" />


                                                    <c:set var="totalCleared" value="${totalCleared +totalpend}" />
                                                    <c:set var="slno" value="${slno +1}" />
                                                    <tr>
                                                        <td>${slno}</td>
                                                        <td>${rascheme.deptName}</td>
                                                        <td>${rascheme.totalCase}</td>
                                                        <td>${totalAppointment}</td>
                                                        <td>${totalRejected}</td>
                                                        <td>${totalCle}</td>
                                                        <td>${totalpend}</td>	


                                                    </tr>
                                                </c:forEach>
                                                <tr style='background-color:black;color:white;font-size:14px'>
                                                    <th>&nbsp;</th>
                                                    <th>&nbsp;</th>
                                                    <th>${totalCaseTotal}</th>
                                                    <th>${totalPendingTotal}</th>
                                                    <th>${totalAppointmentTotal}</th>
                                                    <th>${totalRejectedTotal}</th>
                                                    <th>${totalCleared}</th>

                                                </tr>
                                            </c:if>
                                            <c:if test = "${empty raschemeList}">
                                                <tr>
                                                    <th colspan="10" style='color:red' align='center'><h3>Data is not available</h3></th>
                                            </tr>
                                        </c:if>     
                                        </tbody>	
                                    </table>  
                                </c:if>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
