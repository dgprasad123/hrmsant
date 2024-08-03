<%-- 
    Document   : OfficeWiseEmployee
    Created on : Feb 9, 2017, 4:42:58 PM
    Author     : Manas Jena
--%>

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
        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>
    </head>
    <body>

        <div id="wrapper">
            <div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-default">
                        <div class="panel-heading" align="center" style="background-color: #868686;color: #ffffff;font-size: xx-large;">Bill Details</div>
                        <div class="panel-body">
                           
                                <div class="table-responsive">
                                    <table class="table table-bordered table-hover table-striped">
                                        <thead>
                                            <tr style="background-color: #0071c5;color: #ffffff;">
                                                <th>Sl No</th>
                                                <th>Bill No</th>
                                                <th>Bill Description</th>
                                                <th>Major Head</th>
                                                <th>Treasury Code</th>
                                                <th>Token No</th>
                                                <th>Token Date</th>
                                                <th>Voucher No</th>
                                                <th>Voucher Date</th>                                                
                                            </tr>
                                        </thead>
                                       
                                    <c:forEach items="${billListDetails}" var="blist" varStatus="count">
                                         
                                        <tr>  
                                        <td>${count.index + 1}</td>
                                        <td>${blist.billDesc}</td>
                                        <td>${blist.billGroupDesc}</td>
                                        <td>${blist.majorHead}</td>
                                        <td>${blist.trCode}</td>
                                        <td>${blist.tokenNo}</td>
                                        <td>${blist.tkndate}</td>
                                        <td>${blist.vchNo}</td>
                                        <td>${blist.vdate}</td>                                     
                                        </tr>
                                    </c:forEach>
                                  
                                </table>
                            </div>                
                        </div>
                    </div>
                </div>
            </div>
        </div>

    </body>
</html>
