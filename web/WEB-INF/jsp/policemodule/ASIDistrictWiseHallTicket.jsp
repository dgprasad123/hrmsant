<%-- 
    Document   : ASIDistrictWiseHallTicket
    Created on : 23 Nov, 2022, 11:26:12 AM
    Author     : Manisha
--%>


<%@page contentType="text/html" pageEncoding="UTF-8" autoFlush="true" buffer="64kb"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<!doctype html>
<html lang="en">
    <head>
        <!-- Required meta tags -->
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

        <!-- Bootstrap CSS -->
        <link rel="stylesheet" href="/css/bootstrap.min.css">

        <title>admit Card</title>

        <style>
            table, th, td {
                border: 1px solid black;
                border-collapse: collapse;
            }
            th, td {
                padding: 5px;
                text-align: left;    
            }
            .table-responsive {
                max-height:450px;
                font-size: 10px;
            }
            .table-bordered{
                font-size: 12px;
            }

        </style>

    </head>
    <body>

        <section>
            <div class="container">
                <div class="panel-heading" align="center" style="background-color: #868686;color: #ffffff;font-size: xx-large;">ASI Hall Ticket Number List<br>
                    <div class="panel-heading" align="center" style="background-color: #868686;color: #ffffff;font-size: xx-large;">For
                        <div class="panel-heading" align="center" style="background-color: #868686;color: #ffffff;font-size: xx-large;">Financial Year : 2022 <br>
                        </div>
                    </div>
                </div>
                <div class="panel panel-default">

                    <div class="panel-body">  
                        <table class="table table-bordered table-hover table-striped">
                            <thead>
                                <tr>
                                    <th width="3%">#</th>
                                    <th width="25%">District Name</th>
                                    <th width="25%">Employee Name</th>
                                    <th width="25%">Roll Number</th>
                                </tr>
                            </thead>
                            <tbody>                                        
                                <c:forEach items="${candidateList}" var="obj" varStatus="counter">
                                    <tr>
                                        <td>${counter.index + 1}</td>
                                        <td>${obj.officeName} </td>
                                        <td>${obj.fullname}</td>
                                        <td>${obj.admitCardRollNo}</td>

                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                    <div class="panel-footer">

                    </div>
                </div> 
        </section>



    </body>
</html>


