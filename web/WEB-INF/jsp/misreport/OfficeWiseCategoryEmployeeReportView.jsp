<%-- 
    Document   : OfficeWiseCategoryEmployeeReportView
    Created on : Jul 18, 2018, 3:20:02 PM
    Author     : Manas
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
        <style type="text/css">
            th {
                text-align: center;
            }
        </style>
    </head>
    <body>
        <div id="container">
            <div align="center" style="font-size: 24px;">EMPLOYEE CATEGORY DATA</div>
            <div align="center" style="font-size: 24px;">OF</div>
            <div align="center" style="font-size: 24px;">${owesb.offen}</div>                    
            <div class="table-responsive" style="padding: 5px;">
                <table class="table table-bordered table-hover table-striped">
                    <thead>                                        
                        <tr>
                            <th colspan="2" align="center"><a href="#" class="btn btn-primary">GENERAL <span class="badge">${owesb.generalmale + owesb.generalfemale}</span></a></th>
                            <th colspan="2" align="center"><a href="#" class="btn btn-primary">SC <span class="badge">${owesb.scmale + owesb.scfemale}</span></a></th>
                            <th colspan="2" align="center"><a href="#" class="btn btn-primary">ST <span class="badge">${owesb.stmale + owesb.stfemale}</span></a></th>
                            <th colspan="2" align="center"><a href="#" class="btn btn-primary">OBC <span class="badge">${owesb.obcmale + owesb.obcfemale}</span></a></th>
                            <th colspan="2" align="center"><a href="#" class="btn btn-primary">SEBC <span class="badge">${owesb.sebcmale + owesb.sebcfemale}</span></a></th>
                            <th colspan="3" align="center"><a href="#" class="btn btn-primary">UNCATEGORIES <span class="badge">${owesb.uncatmale + owesb.uncatfemale + owesb.ucatnogender}</span></a></th>
                        </tr>
                        <tr class="warning">
                            <th>MALE</th>
                            <th>FEMALE</th>
                            <th>MALE</th>
                            <th>FEMALE</th>
                            <th>MALE</th>
                            <th>FEMALE</th>
                            <th>MALE</th>
                            <th>FEMALE</th>
                            <th>MALE</th>
                            <th>FEMALE</th>
                            <th>MALE</th>
                            <th>FEMALE</th>
                            <th>UNKNOWN</th>
                        </tr>
                        <tr>
                            <th><span class="badge">1</span></th>
                            <th><span class="badge">2</span></th>
                            <th><span class="badge">3</span></th>
                            <th><span class="badge">4</span></th>
                            <th><span class="badge">5</span></th>
                            <th><span class="badge">6</span></th>
                            <th><span class="badge">7</span></th>
                            <th><span class="badge">8</span></th>
                            <th><span class="badge">9</span></th>
                            <th><span class="badge">10</span></th>
                            <th><span class="badge">11</span></th>
                            <th><span class="badge">12</span></th>
                            <th><span class="badge">13</span></th>
                        </tr>
                        <tr>
                            <th><a href="CategoryEmployeeReport.htm?offCode=${owesb.offcode}&category=GENERAL&gender=M">${owesb.generalmale}</a></th>
                            <th><a href="CategoryEmployeeReport.htm?offCode=${owesb.offcode}&category=GENERAL&gender=F">${owesb.generalfemale}</a></th>
                            <th><a href="CategoryEmployeeReport.htm?offCode=${owesb.offcode}&category=SC&gender=M">${owesb.scmale}</a></th>
                            <th><a href="CategoryEmployeeReport.htm?offCode=${owesb.offcode}&category=SC&gender=F">${owesb.scfemale}</a></th>
                            <th><a href="CategoryEmployeeReport.htm?offCode=${owesb.offcode}&category=ST&gender=M">${owesb.stmale}</a></th>
                            <th><a href="CategoryEmployeeReport.htm?offCode=${owesb.offcode}&category=ST&gender=F">${owesb.stfemale}</a></th>
                            <th><a href="CategoryEmployeeReport.htm?offCode=${owesb.offcode}&category=OBC&gender=M">${owesb.obcmale}</a></th>
                            <th><a href="CategoryEmployeeReport.htm?offCode=${owesb.offcode}&category=OBC&gender=F">${owesb.obcfemale}</a></th>
                            <th><a href="CategoryEmployeeReport.htm?offCode=${owesb.offcode}&category=SEBC&gender=F">${owesb.sebcmale}</a></th>
                            <th><a href="CategoryEmployeeReport.htm?offCode=${owesb.offcode}&category=SEBC&gender=F">${owesb.sebcfemale}</a></th>
                            <th><a href="CategoryEmployeeReport.htm?offCode=${owesb.offcode}&gender=M">${owesb.uncatmale}</a></th>
                            <th><a href="CategoryEmployeeReport.htm?offCode=${owesb.offcode}&gender=F">${owesb.uncatfemale}</a></th>
                            <th><a href="CategoryEmployeeReport.htm?offCode=${owesb.offcode}">${owesb.ucatnogender}</a></th>
                        </tr>
                    </thead>                                    
                </table>

            </div>


        </div>

    </body>
</html>
