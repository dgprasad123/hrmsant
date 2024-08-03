<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link rel="stylesheet" type="text/css" href="css/bootstrap.css"/>
        <link rel="stylesheet" type="text/css" href="css/dataTables.bootstrap4.min.css"/>
        <script type="text/javascript"  src="js/jquery-1.12.4.js"></script>
        <script type="text/javascript"  src="js/jquery.dataTables.min.js"></script>
        <script type="text/javascript"  src="js/dataTables.bootstrap4.min.js"></script>
       <link href="css/bootstrap.min.css" rel="stylesheet"> 

       <%-- <script src="js/bootstrap.min.js"></script> --%>

        <script language="javascript" type="text/javascript" >
            $(document).ready(function() {
                $('#example').DataTable({
                    "order": [[0, "desc"]]
                });
            });
        </script>
    </head>
    <body>
        <div class="container-fluid" style="padding-top: 5px;">

            <table class="table table-striped table-bordered">
                <thead>
                    <tr>
                        <th>Sl no</th>                                           
                        <th>Sender Name</th>
                        <th>Title</th>  
                        <th>Message Received date</th>
                        <th>Whether Viewed by Receiver</th>
                        <th>Viewed on date</th>                        
                        <th>Action</th>
                        <th>Whether Replied</th>
                        <th>Date of Reply</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${employeemessagelist}" var="employeemessage" varStatus="cnt">
                        <tr>
                            <td>${cnt.index+1}</td>
                            <td>${employeemessage.senderName}</td> 
                            <td>${employeemessage.msgTitle}</td>
                            <td>${employeemessage.messageondate}</td>

                            <c:if test="${employeemessage.isviewed eq 'Y'}"><td align="center" style="background-color: #99ff99;font-weight: bold;"><span class="glyphicon glyphicon-ok"></span> Yes</td></c:if>
                            <c:if test="${empty employeemessage.isviewed}"><td align="center">Not Yet</td></c:if>

                                <td>${employeemessage.viewondate}</td>                            
                            <td><a href="viewCommunicationMessage.htm?messageId=${employeemessage.messageId}"><span class="badge badge-success" style="background:#008000">View</span></a>
                                <c:if test="${empty employeemessage.repliedondate}">
                                    <a href="replyCommunicationMessage.htm?messageId=${employeemessage.messageId}"><span class="badge badge-success" style="background:#008000">Reply</span></a>
                                </c:if>
                            </td>
                            <c:if test="${not empty employeemessage.repliedondate}"><td align="center" style="background-color: #99ff99;font-weight: bold;"><span class="glyphicon glyphicon-ok"></span> Yes</td></c:if>
                            <c:if test="${empty employeemessage.repliedondate}"><td align="center"><span class="glyphicon glyphicon-remove"></span> No</td></c:if>
                            <c:if test="${empty employeemessage.viewondate}"><td align="center">Not Yet</td></c:if>
                            <td>${employeemessage.repliedondate}</td>
                        </tr>
                    </c:forEach>  
                </tbody>
            </table>
        </div>
    </body>
</html>
