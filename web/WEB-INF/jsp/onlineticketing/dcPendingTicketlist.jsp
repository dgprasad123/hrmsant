<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Online Ticket System</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="css/sb-admin.css" rel="stylesheet">

        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/hrmsadminmenu.jsp"/>        
            <div id="page-wrapper">
                <form:form action="GetDCMISReport.htm" commandName="onlineticketing">
                    <div class="container-fluid">
                        <div class="panel panel-primary">
                            <div class="panel-heading">Age-Wise Online Ticket Analysis District Wise</div>
                            <div class="panel-body" style="margin-top:20px;">


                                <table id="example" class="table table-striped table-bordered" width="100%" cellspacing="0" style='margin-top:10px'>
                                    <tr>
                                        <td>&nbsp;</td>
                                        <td>&nbsp;</td>
                                        <td>&nbsp;</td>
                                        <th colspan="5">Pending Ticket</th>
                                    </tr>    
                                    <tr>
                                        <th >Slno</th>
                                        <th >Support Id</th>
                                        <th >District Name</th>
                                        <th > Less than 24 Hours</th>
                                        <th >Less than 48 Hours</th>
                                        <th >less than 7 Days</th>
                                        <th >less than 30 Days</th>
                                        <th >More than 30 Days</th>
                                    </tr>
                                    <c:set var="totalticket" value="${0}" />
                                    <c:set var="resolvedTicket" value="${0}" />
                                    <c:set var="stateTicket" value="${0}" />



                                    <c:forEach items="${onlineticketlist}" var="list" varStatus="count">
                                        <c:set var="totalticket" value="${totalticket + list.totalticketReceived}" />
                                        <c:set var="resolvedTicket" value="${resolvedTicket + list.totalticketdisposed}" />
                                        <c:set var="stateTicket" value="${stateTicket + list.totalticketsent}" />
                                        <tr>
                                            <td>
                                                ${count.index + 1}
                                            </td>
                                            <td>
                                                ${list.username}
                                            </td>
                                            <td>
                                                ${list.distCode}
                                            </td>
                                            <td>
                                                ${list.lessthan24Hours}
                                            </td>
                                            <td>
                                                 ${list.lessthan48Hours}
                                            </td>
                                            <td>
                                                ${list.lessthan7days}                

                                            </td>
                                             <td>
                                              ${list.lessthan30days}          

                                            </td>
                                             <td>
                                               ${list.morethan30days}                       

                                            </td>
                                        </tr>
                                    </c:forEach>
                                    <!--  <tr style="background-color: #0071c5;color: #ffffff;">
                                          <th>&nbsp;</th>
                                          <th>&nbsp;</th>
                                          <th>&nbsp;</th>
                                          <th>${totalticket}</th>
                                          <th>${resolvedTicket}</th>
                                          <th>${stateTicket}</th>
  
                                      </tr>     --> 

                                </table>
                            </div>
                            <div class="panel-footer"></div>
                        </div>
                    </div>
                </form:form>
            </div>
        </div>
    </body>
</html>
