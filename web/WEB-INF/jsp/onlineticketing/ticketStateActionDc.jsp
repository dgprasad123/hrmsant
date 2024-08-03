<%-- 
    Document   : ModuleList
    Created on : Nov 21, 2016, 6:08:30 PM
    Author     : Manas Jena
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        <link href="css/sb-admin.css" rel="stylesheet">
        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>
        <link rel="stylesheet" type="text/css" href="css/dataTables.bootstrap4.min.css"/>
        <script type="text/javascript"  src="js/jquery-1.12.4.js"></script>
        <script type="text/javascript"  src="js/jquery.dataTables.min.js"></script>
        <script type="text/javascript"  src="js/dataTables.bootstrap4.min.js"></script>
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
            function ticket_assign(ticketId){
                var con=confirm("Do you want to Takeover this Ticket");
                if(con){
                    window.location="ticketTakeOver.htm?ticketId="+ticketId;
                }
            }
        </script>  

    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/hrmsadminmenu.jsp"/>        
            <div id="page-wrapper">
                <div class="container-fluid">
                    <!-- Page Heading -->
                    <div class="row">
                        <div class="col-lg-12">                            
                            <ol class="breadcrumb">
                                <li>
                                    <i class="fa fa-dashboard"></i>  <a href="index.html">Dashboard</a>
                                </li>
                                <li class="active">
                                    <i class="fa fa-file"></i> Online Ticket System 
                                </li>                                
                            </ol>
                        </div>
                    </div>
                    <form:form action="SaveonlineticketState.htm" commandName="onlineticketing" enctype="multipart/form-data" onsubmit="return validate();">
                        <input type='hidden' name='ticketId' value='${onlineticketlist[0].ticketId}'/>
                        <input type='hidden' name='userType' value='HRMS Staff(State Team)'/>
                         <input type='hidden' name='username' value='${loginName}'/>
                          <input type='hidden' name='loginId' value='${loginId}'/>
                    
                      

                        <c:if test = "${onlineticketlist[0].status != 'Closed'}">
                            <button type="button" class="btn btn-primary" data-toggle="collapse" data-target="#demo" style='width:100%;text-align:left'><strong>Reply Ticket Id (<c:out value="${onlineticketlist[0].topicName}" />) :&nbsp;<c:out value="${onlineticketlist[0].fticketid}" /></strong></button>
                            <div>&nbsp;</div>
                        </c:if>
                        <div id="demo" class="collapse">
                            <div class="panel panel-primary"  >
                                <div class="panel-heading " id="Reply">Ticket Id :&nbsp;<c:out value="${onlineticketlist[0].fticketid}" /></div>
                                <div class="row "  >
                                    <div class="col-md-4 col-md-push-4">
                                        <div class="form-group" style='margin-top:10px'>
                                            <label for="form_name">Ticket Id :&nbsp;<c:out value="${onlineticketlist[0].fticketid}" /></label><br>
                                        </div>    

                                        <div class="form-group">
                                            <label for="form_name">Topic:&nbsp;<c:out value="${onlineticketlist[0].topicName}" /></label>

                                        </div>
                                        <div class="form-group">
                                            <label for="form_name">Ticket Status:</label>
                                            <form:select path="status" class="form-control" id='status'>
                                                <form:option value="" label="Select"/>
                                                <form:option value="Open" label="Open"/> 
                                                <form:option value="Resolved" label="Resolved"/>    

                                            </form:select> 
                                        </div>        

                                        <div class="form-group">
                                            <label for="form_name">Reply:</label>
                                            <form:textarea path="message" class="form-control" id="message" rows="3"/>
                                        </div>
                                        <div class="form-group">
                                            <table>
                                                <tr>
                                                    <td><label for="exampleInputFile">File Upload</label></td>

                                                    <td>
                                                        <input type="file" name="file" />
                                                        <!-- <input type="file" class="form-control-file" name="file" id="file" aria-describedby="fileHelp">-->
                                                        <small id="fileHelp" class="form-text text-muted"></small></td>
                                                    <td></td>
                                                <tr>
                                            </table>
                                        </div>
                                        <div class="col-md-8 col-md-push-4" style='margin-bottom:10px;'>
                                            <input type="submit" value="Save" name="save" class="btn btn-primary"/>
                                            <input type="button" value="Cancel" name="cancel" class="btn btn-primary" onclick="Cancel_page()"/>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        
                       <c:forEach var="ticketlist" items="${onlineticketlist}" varStatus="theCount">
                           <c:if test = "${empty ticketlist.assignedToUserId }">                
                             <div> <input type='checkbox' name='takeover' value='1' onclick="ticket_assign(${onlineticketlist[0].ticketId})"/>&nbsp;<strong>Takeover this ticket</strong></div>                   
                           </c:if>
                            <div class="panel panel-info">
                                <div class="panel-heading">&nbsp;</div>

                                <div class="panel-body">

                                    <div class="row">
                                        <div class="col-xs-6">
                                            <img src="images/cadre_icon.png"/> &nbsp; <c:out value="${ticketlist.userType}" />
                                             <c:if test="${not empty ticketlist.userId}">
                                                <c:if test = "${ticketlist.userType == 'HRMS Employee'}">
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
                                        <div class="col-xs-6">&nbsp;</div>
                                    </div>
                                    <div class="row">
                                        <div class="col-xs-10"><c:out value="${ticketlist.message}" /></div>
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
                        window.location = "onlineticketStatelist.htm";
                    }
                    
                </script>    




            </div>
        </div>
    </div>

</body>
</html>
