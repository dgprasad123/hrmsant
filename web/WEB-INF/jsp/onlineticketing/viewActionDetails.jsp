<%-- 
    Document   : OnlineTicketingData
    Created on : 12 Oct, 2017, 11:34:26 AM
    Author     : lenovo pc
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Online Ticket System</title>
        <link rel="stylesheet" type="text/css" href="css/bootstrap.min.css">     
     <link rel="stylesheet" href="css/bootstrap.min.css">
        <script src="js/jquery.min.js"></script>
        <script src="js/bootstrap.min.js"></script>
        <style type="text/css">

        </style>
        <script type="text/javascript">
            function validate() {
                var status = $("#status").val();
                if (status == "") {
                    alert("Please select Ticket Status!!");
                    return false;
                }
                var message = $("#message").val();
                if (message == "") {
                    alert("Reply box cannot be Blank!!");
                    return false;
                }

            }
        </script>    

    </head>
    <body>

        <form:form action="SaveonlineticketDc.htm" commandName="onlineticketing" enctype="multipart/form-data" onsubmit="return validate();">
            <!--<input type='hidden' name='ticketId' value='${onlineticketlist[0].ticketId}'/>-->
            
            <input type='hidden' name='ticketId' value='${onlineticketlist[0].ticketId}'/>
            <input type='hidden' name='userType' value='HRMS Staff(District Team)'/>
            <input type='hidden' name='username' value='${loginName}'/>
             <input type='hidden' name='loginId' value='${loginId}'/>
            <div class="container">
                <c:if test = "${onlineticketlist[0].status != 'Closed'}">
                    <button type="button" class="btn btn-primary" data-toggle="collapse" data-target="#demo" style='width:100%;text-align:left'><strong>Reply Ticket Id (<c:out value="${onlineticketlist[0].topicName}" />) :&nbsp;<c:out value="${onlineticketlist[0].fticketid}" /></strong></button>
                    <div>&nbsp;</div>
                </c:if>
              
                <c:forEach var="ticketlist" items="${onlineticketlist}" varStatus="theCount">
                    <div class="panel panel-info">
                        <div class="panel-heading">&nbsp;</div>

                        <div class="panel-body">

                            <div class="row">
                                <div class="col-xs-8">
                                    <img src="images/cadre_icon.png"/> &nbsp; <c:out value="${ticketlist.userType}" />
                                     <c:if test = "${ticketlist.userType == 'HRMS Employee' || ticketlist.userType == 'HRMS DDO'}">
                                         <c:if test="${not empty ticketlist.userId}">
                                        <strong>(<c:out value="${ticketlist.userId}" />)  </strong>
                                        
                                         </c:if>
                                        
                                    </c:if>
                                    <br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<c:out value="${ticketlist.username}" />

                                </div>
                                <div class="col-xs-4">                              
                                    <c:out value="${ticketlist.createdDateTime}" />
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-xs-8">&nbsp;</div>
                            </div>
                            <div class="row">
                                <div class="col-xs-12"><c:out value="${ticketlist.message}" /></div>
                            </div>  
                            <c:if test = "${ticketlist.attachmentId != 0}">
                                <div class="row">
                                    <div class="col-xs-12"><a href="DownloadTicket.htm?attId=${ticketlist.attachmentId}">Attachment</a></div>
                                </div> 
                            </c:if> 
                            <div style='clear:both'>&nbsp;</div>    

                        </div>


                    </div>

                </c:forEach>


            </div>               
        </form:form>
        <div align="center"> <input type="button" value="Close" name="cancel" class="btn btn-primary" onclick="Cancel_page()"/></div>
        <script>
            function Cancel_page() {
                window.location = "onlineticketlistResolved.htm";
            }
        </script>    

    </body>
</html>
