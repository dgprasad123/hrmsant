<%-- 
    Document   : SectionDefination
    Created on : Nov 21, 2016, 3:12:08 PM
    Author     : Manas Jena
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>      
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <!-- LAYOUT v 1.3.0 -->
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>        
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript">
            
        </script>
    </head>
    <body>
        <div class="container-fluid">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <div class="row">
                        <div class="col-lg-12">
                            
                        </div>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="table table-bordered">
                        <thead>
                            <tr>
                                <th width="5%">Sl No</th>
                                <th width="30%">Section</th>
                                <th width="15%">No of Post</th>
                                <th width="10%">Men in Position</th>
                                <th width="20%">Bill Group</th>
                                <th width="5%">Edit</th>
                                <th width="5%">Map</th>                                
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${sectionList}" var="section" varStatus="cnt">
                            <tr>
                                <td>${cnt.index+1}</td>
                                <td>${section.section}</td>
                                <td> &nbsp;</td>
                                <td> &nbsp;</td>
                                <td> ${section.billgroup} </td>
                                <td><a href="editBillSection.htm?sectionId=${section.sectionId}">Edit</a></td>
                                <td><a href="sectionMapping.htm?sectionId=${section.sectionId}">Map</a></td>
                            </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
                <div class="panel-footer">                    
                    <button type="submit" class="btn btn-default" onclick="javascript: self.location='addBillSection.htm'">New Section</button>                    
                </div>
            </div>
        </div>
    </body>
</html>
