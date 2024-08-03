<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <title>HRMS</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>        
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
    </head>
    <body>
        <div class="container-fluid">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h2 align="center">Controlling Officer(CO) List</h2>
                    <div class="row">
                        <h3 align="center"> 
                            Financial Year <form:form action="COWiseSubmittedPostTerminationList.htm" commandName="command">
                                <form:select path="fy" id="fy">                                   
                                    
                                      <form:option value="2020-21"> </form:option>
                                       <form:option value="2019-20"> </form:option>
                                       <form:option value="2018-19"> </form:option>
                                        <form:option value="2020-21"> </form:option>
                                </form:select>

                                <input type="submit" name="btnPTAer" value="Show List" class="btn btn-primary"/>
                                &nbsp;&nbsp;<a href="COWiseDeptPostTerminationStatus.htm" target="_blank"><input type="button" name="btnPTAer" value="CO Wise Post Termination Proposal Status" class="btn btn-primary"/></a>
                                
                            </form:form>
                        </h3>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="table table-bordered" width="100%">
                        <thead>
                            <tr>
                                <th width="10%">SL No</th>
                                <th width="30%">Office Name</th>
                                <th width="30%">Submitted By</th>
                                <th width="10%">Submitted On</th>
                                <th width="10%">Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${postterminationcolist}" var="deptlist">
                                <tr>
                                    <td colspan="5" style="font-weight: bold;">
                                        <c:out value="${deptlist.deptName}"/>
                                    </td>
                                </tr>
                                <c:forEach items="${deptlist.offlist}" var="offlist" varStatus="count">
                                    <tr>
                                        <td>
                                            ${count.index + 1}
                                        </td>
                                        <td>
                                            <c:out value="${offlist.offName}"/>
                                        </td>
                                        <td> 
                                            <c:out value="${offlist.empName}"/>&nbsp;(<c:out value="${offlist.post}"/>)
                                        </td>
                                        <td>
                                            <c:out value="${offlist.submittedDate}"/>
                                        </td>
                                        <td>&nbsp;</td>
                                    </tr>
                                </c:forEach>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
                <div class="panel-footer"></div>
            </div>
        </div>
    </body>
</html>
