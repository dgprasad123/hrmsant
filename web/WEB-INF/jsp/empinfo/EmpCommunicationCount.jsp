<%-- 
    Document   : SentMessage
    Created on : Jan 12, 2018, 11:56:43 AM
    Author     : manisha
--%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        <link href="css/sb-admin.css" rel="stylesheet">
        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>
        <script type="text/javascript">

        </script>
    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/hrmsadminmenu.jsp"/>        
            <div id="page-wrapper">
                <div class="container-fluid">
                    <!-- Page Heading -->
                    <div class="row">
                        <div class="col-lg-12">                            
                            <ol class="breadcrumb">
                                <li>
                                    <i class="fa fa-dashboard"></i>  <a href="#">Dashboard</a>
                                </li>
                                <li class="active">
                                    <i class="fa fa-file"></i> Total No of Communications
                                </li>

                            </ol>
                        </div>
                    </div>
                    <div class="jumbotron">
                        <table class="table-bordered">
                            <tr>
                                <td colspan="2">
                                    <h2 class="alert alert-info">Total No of Communications: ${totalCnt}</h2> 
                                </td>
                            </tr>
                             <tr>
                                <td width="45%" >
                                    <h2 class="alert alert-success">Viewed Communications: ${totalview}</h2> 
                                </td>
                                 <td width="60%">
                                    <h2 class="alert alert-danger">Waiting For View Communications: ${totalnotview}</h2> 
                                </td>
                            </tr>
                        </table>
                        
                    </div>
                   <c:if test="${totalnotview > 0}">
                    <div class="row">
                        <center><button type="button" class="btn btn-success"><a href="viewCommunicationDetails.htm" style='color:white'>View Messages</a></button></center>
                    </div>
                     </c:if>
                </div>
            </div>
        </div>
    </body>
</html>