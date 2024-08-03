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
        <script>
                function validate(){
                    var topic=document.getElementById('topicid').value;
                    var mess=document.getElementById('message').value;
                    if(topic==""){
                        alert("Please select Topic");
                        return false;
                    }
                     if(mess==""){
                        alert("Message field cannot be blank");
                        return false;
                    }
                    
                }
        </script>    

    </head>
    <body>

        <form:form action="onlineticket.htm" commandName="onlineticketing" enctype="multipart/form-data" onsubmit="return validate();">
         <!--  <input type='hidden' name='deptCode' value='${deptCode}'/>
            <input type='hidden' name='distCode' value='${distCode}'/>
            <input type='hidden' name='username' value='${onlineticketing.username}'/>
              <input type='hidden' name='userId' value='${onlineticketing.userId}'/>-->
            <div class="container">
            <div class="panel panel-primary">
                <div class="panel-heading">Online Ticket</div>
                <div class="row">
                    <div class="col-md-4 col-md-push-4">
                           
                        <div class="form-group" style='margin-top:10px'>
                            <label for="form_name">Ticket By :</label><br>
                            <c:out value="${onlineticketing.username}" />
                            <html:hidden path="userId"/>
                            <!-- <form:input path="userId" class="form-control" id="userId" placeholder="Enter Ticket No"/>-->
                        </div>
                        <div class="form-group">
                            <label for="form_name">Topic</label>
                            <form:select path="topicId" class="form-control" id='topicid'>
                                <form:option value="" label="Select"/>
                                <c:forEach items="${topiclist}" var="topic">
                                    <form:option value="${topic.topicId}" label="${topic.topic}"/>
                                </c:forEach>                                 
                            </form:select> 
                        </div>
                        <div class="form-group">
                            <label for="form_name">Message</label>
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
        </form:form>
            <script>
            function Cancel_page() {
                window.location = "onlineticketlist.htm";
            }
        </script>    


    </body>
</html>
