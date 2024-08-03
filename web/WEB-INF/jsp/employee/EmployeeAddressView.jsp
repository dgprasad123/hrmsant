<%-- 
    Document   : MyProfile
    Created on : Aug 14, 2018, 2:30:50 PM
    Author     : Manas
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">   
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <script type="text/javascript" src="js/jquery.min.js"></script>
        <script type="text/javascript" src="js/bootstrap.min.js"></script>        
    </head>
    <body style="padding-top: 10px;">
        <jsp:include page="ProfileTabs.jsp">
            <jsp:param name="menuHighlight" value="ADDRESSPAGESB" />
        </jsp:include>
        <div id="profile_container">

            <c:if test = "${ifProfileCompleted == 'Y'}">
                <div id="notification_blk" style="height:30px;background:#008900;color:#FFFFFF;line-height:30px;text-align:center;font-weight:bold;">Congratulations! Your Profile is complete.</div>
            </c:if>
            <c:if test = "${ifProfileCompleted == 'N'}">
                <div id="notification_blk" style="height:30px;background:#F00;color:#FFFFFF;line-height:30px;text-align:center;font-weight:bold;">Your Profile is not complete. <a href="javascript:void(0)" onclick="javascript: showInformationWindow()" style="color:#EAEAEA;text-decoration:underline;"> Click here to know more...</a></div>
            </c:if> 

            <div class="row" style="border: 1px solid #ddd;padding: 5px;">
                <table class="table table-bordered">
                    <thead>
                        <tr class="bg-primary text-white">
                            <th>#</th>
                            <th>Address Type</th>
                            <th>Address</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${empAddress}" var="address" varStatus="cnt">
                            <tr>
                                <th scope="row">${cnt.index+1}</th>
                                <td>${address.addressType}</td>
                                <td>${address.address}</td>
                                <c:if test="${address.isLocked eq 'N'}">
                                    <td><a href="editEmployeeAddress.htm?addressId=${address.addressId}" class="btn btn-default"><span class="glyphicon glyphicon-pencil"></span> Edit</a>
                                    <a href=#" onclick="delete_profile_address(${address.addressId})" class="btn btn-default"><span class="glyphicon glyphicon-remove"></span> Delete</a>
                                    </td>
                                </c:if>
                                <c:if test="${address.isLocked eq 'Y'}">
                                    <td><img src="images/Lock.png" width="20" height="20"/></td>
                                </c:if>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
            <div class="row" style="border: 1px solid #ddd;padding: 5px;">
                <form:form action="employeeAddressNew.htm" method="post" commandName="address">
                    <table class="table table-bordered">
                        <thead>
                            <tr class="bg-primary text-white">
                                <th><input type="submit" name="action" value="Add New" class="btn btn-default"/></th>
                            </tr>
                        </thead>
                    </table>
                </form:form>
            </div>
        </div>
    </body>
    <script type="text/javascript">
        $(document).ready(function () {
            setTimeout(function () {
                $("#notification_blk").slideUp();
            }, 5000);
        });
        
        function delete_profile_address(ids){
            var conf=confirm("Do you want to confirm to delete this address");
            if(conf){
                
                window.location="deleteProfileAddress.htm?addressId="+ids;
            }
        } 
    </script> 
</html>
