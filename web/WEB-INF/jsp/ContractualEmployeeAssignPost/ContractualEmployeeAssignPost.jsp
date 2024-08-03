<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript">
            $(document).ready(function(){
               $("#addNewPostModal").on("show.bs.modal", function (e) {
                    $(this).find(".modal-body").load("openAddNewPost.htm");
                    //$(this).find('iframe').attr('src','GPCWiseSPCList.htm');
                }); 
            });
            function saveCheck(){
                if($('#sltEmployee').val() == ''){
                    alert("Please Select Employee");
                    return false;
                }
                if($('#sltVacantSPC').val() == ''){
                    alert("Please Select Post");
                    return false;
                }
              return true;  
            }
        </script>
    </head>
    <body>
        <form:form action="saveAssignContractualPost.htm" method="POST" commandName="contractualAssignPost">
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        Contractual Employee Assign Post
                    </div>
                    <div class="panel-body">
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="sltEmployee">Employee Name</label>
                            </div>
                            <div class="col-lg-10">   
                                <form:select path="sltEmployee" id="sltEmployee" class="form-control">
                                    <form:option value="" label="--Select Employee--"/>
                                    <form:options items="${emplist}" itemValue="value" itemLabel="label"/>
                                </form:select>
                            </div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="sltVacantSPC">Vacant Post Name</label>
                            </div>
                            <div class="col-lg-10">   
                                <form:select path="sltVacantSPC" id="sltVacantSPC" class="form-control">
                                    <form:option value="" label="--Select Post--"/>
                                    <form:options items="${postlist}" itemValue="value" itemLabel="label"/>
                                </form:select>
                            </div>
                        </div>
                    </div>
                    <div class="panel-footer">
                        <input type="submit" name="submit" value="Assign Post" class="btn btn-default" onclick="return saveCheck();"/>&nbsp;
                        <a href="GPCWiseSPCList.htm" target="_blank"><input type="button" name="submit" value="Add New Post" class="btn btn-default"/></a>
                        (<span style="color: #EE0000;font-size:12px;">Refresh the Page after Adding Post</span>)
                        <c:if test="${not empty isMapped}">
                            <c:if test="${isMapped == 'D'}">
                                &nbsp;<span style="color:#00cc66;font-weight:bold;font-size:16px;">POST ASSIGNED</span>
                            </c:if>
                            <c:if test="${isMapped == 'Y'}">
                                &nbsp;<span style="color:#EE0000;font-weight:bold;font-size:16px;">POST HAS ALREADY BEEN ASSIGNED ASSIGNED TO THE EMPLOYEE</span>
                            </c:if>
                        </c:if>
                    </div>
                </div>
            </div>
        </form:form>

        <div id="addNewPostModal" class="modal" role="dialog">
            <div class="modal-dialog" style="width:1000px;">
                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                    </div>
                    <div class="modal-body">
                        
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
