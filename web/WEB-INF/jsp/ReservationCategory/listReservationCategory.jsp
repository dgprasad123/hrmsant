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
        </script>
        <style type="text/css">
            body{
                font-family: Verdana;
                font-size:16px;
            }
            tr.strikeout td:before {
                content: " ";
                position: absolute;
                top: 50%;
                left: 0;
                border-bottom: 1px solid #111;
                width: 100%;
            }
        </style>
    </head>
    <body>        
        <form:form action="GT.htm" method="post" commandName="reservedCategory">
            <div class="container-fluid">
                <div class="panel panel-default">

                </div>
                <div class="panel-body">
                    <table class="table table-bordered">
                        <thead>
                            <tr>
                                <th width="15%"> Sl No </th>
                                <th width="15%"> Order No </th>
                                <th width="10%"> Order Date </th>
                                <th width="10%"> Date Of Entry </th>
                                <th width="20%"> Reserved Category </th>
                                <th width="20%"> Specific Category </th>
                                <th colspan="2" width="15%" align="center">Edit</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${reservationlist}" var="rlist" varStatus="count">

                                <tr>
                                    <td>${count.index+1}</td>
                                    <td>${rlist.orderno}</td>
                                    <td>${rlist.orderdate}</td>
                                    <td>${rlist.doe}</td>
                                    <td>${rlist.reservationCategory}</td>
                                    <td>${rlist.disableCategory}</td>
                                    <td><a href="editReservationCategory.htm?notId=${rlist.notid}">Edit</a></td>
                                </tr>

                          </c:forEach>
                        </tbody>
                    </table>
                </div>
                <%--<div class="panel-footer">
                    <a href="ReservationCategoryInformation.htm">
                        <button type="button" class="btn btn-default">Add New</button>
                    </a>
                </div>--%>

            </div>
        </form:form>
        <script type="text/javascript">
            function confirmDelete(transferId, notID)
            {
                if (confirm("Are you sure you want to delete the Transfer record from the list?"))
                {
                    $.ajax({
                        url: 'DeleteTransferRecord.htm',
                        type: 'get',
                        data: 'transfer_id=' + transferId + '&not_id=' + notID,
                        success: function(retVal) {
                            window.location.reload();
                        }
                    });
                }
            }
        </script>
    </body>
</html>
