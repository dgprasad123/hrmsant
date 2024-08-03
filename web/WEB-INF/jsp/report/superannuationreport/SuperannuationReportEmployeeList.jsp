<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
    <head>

        <style>
            .clearfix::after {
                content: "";
                clear: both;
                display: table;
            }
        </style>

        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript">
            function callNoImage(imgId) {
                var userPhoto = document.getElementById(imgId);
                userPhoto.src = "images/NoEmployee.png";

            }
        </script>
    </head>
    <body>
        

        <div class="panel-body">
            <c:forEach items="${emplist}" var="list" varStatus="count">
                <div class="row text-success" style="border: 3px solid #f47b54; padding: 5px; margin-top: 5px; background: #aadef2;">
                    <div class="col-lg-2">
                        <img src="displayprofilephoto.htm?empid=${list.empid}" id="sbUserPhoto${count.index + 1}" onerror="callNoImage('sbUserPhoto${count.index + 1}')"  height="100px" border="2"/>
                    </div>
                    <div class="col-lg-10">
                        <div class="d-flex p-3 bg-secondary text-white">
                            <h4><span style="color: #005ab6"> ${list.empname}</span> , <spa style="font-size:15px"> ${list.designation} </span> </h4>


                            <p> 
                                HRMS ID: ${list.empid},
                                ${list.accountType} Number: ${list.gpfNo}, 
                                Date of Birth :${list.dateOfJoining}, 
                                
                            </p>

                            <p> 
                                <span style="color:red">DATE OF SUPERANNUATION: ${list.dateOfSuperannuation}, 
                                    <c:if test="${not empty list.postGroup}"> Group-${list.postGroup} </c:if>
                                </span>
                            </p>


                        </div>
                    </div>
                </div>
                <div class="clearfix"></div>
            </c:forEach>    




        </div>

        

    </body>
</html>
