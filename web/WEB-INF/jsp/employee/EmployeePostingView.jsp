<%-- 
    Document   : MyProfile
    Created on : Aug 14, 2018, 2:30:50 PM
    Author     : Manas
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
            <jsp:param name="menuHighlight" value="POSTINGPAGESB" />
        </jsp:include>
        <div id="profile_container">
           
            <div class="row" style="border: 1px solid #ddd;padding: 5px;">
                <table class="table table-bordered">
                    <thead>
                        <tr class="bg-primary text-white">
                            <th>#</th>
                            <th>Date of Joining</th>
                            <th>Post</th>
                            <th>Order No.</th>
                            <th>Order Date</th>
                                                      
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${PostingList}" var="pl" varStatus="cnt">
                            <tr>
                                <th scope="row">${cnt.index+1}</th>
                                <td>${pl.joindate}</td>
                                <td>${pl.joinspn}</td>
                                <td>${pl.orderno}</td>
                                <td>${pl.orderdate}</td>
                                                              
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
              
                             <strong style="color:red">**If your Posting details is missing or incorrect please contact your DDO.  </strong>
                       
            </div>
        </div>
    </body>
    <script type="text/javascript">
        $(document).ready(function () {
            setTimeout(function () {
                $("#notification_blk").slideUp();
            }, 5000);
        });
    </script> 
</html>
