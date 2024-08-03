<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>::HRMS::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>        
    </head>
    <body>
        <form:form class="form-inline" action="aerstatuslist.htm" commandName="aerstatuslist"  method="GET">
            <div align="center">
                <table  id="example"  width="100%" cellspacing="0">

                    <tr>
                        <td width="40%" align="right">
                            Select Financial Year 
                        </td>
                        <td width="20%">
                            <form:select id="finYear" path="fy" class="form-control">
                                <form:option value="2020-21">2020-21</form:option>
                                 <form:option value="2019-20">2019-20</form:option>
                                  <form:option value="2018-19">2018-19</form:option>
                                <form:option value="2017-18">2017-18</form:option>
                            </form:select>
                        </td>
                        <td align="left">
                            <input type="submit" value="Ok" name="ok" class="btn btn-primary"/>
                        </td>
                    </tr>
                </table>
            </div>
            <table id="example" class="table table-striped table-bordered" width="100%" cellspacing="0">
                <thead>
                    <tr>
                        <th>Sl No</th>
                        <th>Department Name</th>
                        <th>No Of DDOs</th>
                        <th>No Of Offices <br>AER Submitted</th>
                        <th>No Of Offices<br>AER Approved</th>
                    </tr>
                </thead>

                <tbody>
                    <c:set var="aersubmitted" value="0" scope="page" />
                    <c:set var="noofddo" value="0" scope="page" />
                    <c:forEach var="aerstatuslist" items="${aerStatusList}" varStatus="theCount">
                        <tr>
                            <td>${theCount.index + 1}</td>
                            <td><c:out value="${aerstatuslist.deptName}"/></td>
                            <td><c:out value="${aerstatuslist.noOfDDO}"/></td>
                            <td>
                                <a href="aerreportsubmittedofflist.htm?deptCode=${aerstatuslist.deptCode}&finYear=${aerstatuslist.finYear}" target="aerreportsubmitted" ><c:out value="${aerstatuslist.noAerSubmitted}"/></a>
                            </td>

                            <td>
                                <a href="aerreportapprovedofflist.htm?deptCode=${aerstatuslist.deptCode}&finYear=${aerstatuslist.finYear}" target="aerreportsubmitted" ><c:out value="${aerstatuslist.noOfAerAproved}"/></a>
                            </td>
                        </tr>
                        <c:set var="aersubmitted" value="${aersubmitted + aerstatuslist.noAerSubmitted}" scope="page"/>
                        <c:set var="noofddo" value="${noofddo + aerstatuslist.noOfDDO}" scope="page" />
                    </c:forEach>
                    <tr>
                        <td>&nbsp;</td>
                        <td>Total</td>
                        <td>${noofddo}</td>
                        <td>${aersubmitted}</td>
                        <td>&nbsp;</td>
                    </tr>
            </table>
            <div id="myModal" class="modal fade" role="dialog">
                <div class="modal-dialog" style="width:1000px;">
                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-body">

                        </div>
                        <div class="modal-footer">
                            <span id="msg"></span>                        

                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        </div>
                    </div>

                </div>
            </div>

        </form:form>
    </body>
</html>
