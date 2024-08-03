<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>        
        <script type="text/javascript" src="js/bootstrap.min.js"></script>

        <script type="text/javascript">
            $(document).ready(function() {
                $(".txtNum").keypress(function(e) {
                    //if the letter is not digit then display error and don't type anything
                    if (e.which != 8 && e.which != 0 && (e.which < 48 || e.which > 57)) {
                        //display error message
                        $("#errmsg").html("Digits Only").show().fadeOut("slow");
                        return false;
                    }
                });
            });
            function confirmDelete(notificationId)
            {
                if(confirm("Are you sure you want to delete?"))
                {
                    $.ajax({
                    url: 'DeleteMiscInfo.htm',
                    type: 'get',
                    data: 'notId='+notificationId,
                    success: function(retVal) {
                        self.location = 'MiscInfoList.htm';
                    }
                });
                    
                }
            }
        </script>
        <style type="text/css">
            body{
                font-family: Verdana;
                font-size:16px;
            }
        </style>
    </head>
    <body>
        <form:form action="AddMiscInfo.htm" method="post" commandName="MiscInfoForm">
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <div class="row">
                            <div class="col-lg-12">
                                <h1 style="font-size:15pt;margin:0px;">Miscellaneous</h1>
                            </div>
                        </div>
                    </div>
                    <div class="panel-body">
                        <table class="table table-bordered" style="font-size:10pt;">
                            <thead>
                                <tr style="background:#EAEAEA;">
                                    <th>Date of Entry</th>
                                    <th>Order No</th>
                                    <th>Order Date</th>
                                        <th style="text-align:center;">View</th>
                                        <th style="text-align:center;">Edit</th>
                                        <th style="text-align:center;">Supercede</th>
                                        <th style="text-align:center;">Cancel</th>
                                        <th style="text-align:center;">Delete</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${miscInfoList}" var="tlist">
                                    <tr>
                                        <td>${tlist.dateOfEntry}</td>
                                        <td>${tlist.orderNumber}</td>
                                        <td>${tlist.orderDate}</td>
                                        <td align="center"><a href="viewMiscInfo.htm?notId=${tlist.notificationId}">View</a></td>
                                        <td align="center">
                                            <c:if test="${tlist.isValidated eq 'N'}">
                                                <a href="editMiscInfo.htm?notId=${tlist.notificationId}">Edit</a>
                                            </c:if>
                                        </td>
                                        <td align="center"><a href="">Supercede</a></td>
                                        <td align="center"><a href="">Cancel</a></td>
                                        <td align="center"><a href="javascript: void(0)" onclick="javascript: confirmDelete(${tlist.notificationId})">Delete</a></td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                    <div class="panel-footer">
                        <form:hidden class="form-control" path="empId" id="empId"/>
                        <button type="button" class="btn btn-default" onclick="self.location='AddMiscInfo.htm'">Add New</button>  
                    </div>
                </div>
            </div>
        </form:form>
    </body>
</html>
