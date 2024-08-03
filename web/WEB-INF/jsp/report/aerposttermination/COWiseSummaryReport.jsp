<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <title>HRMS</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>        
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript">
            function validateSend() {

                var con = confirm("Do ypu want to send this report to Finance Department");
                if (!con) {

                    return false;
                }
            }
        </script>

        <style type="text/css">
            .dropdown-submenu {
                position: relative;
            }

            .dropdown-submenu .dropdown-menu {
                top: 0;
                left: 100%;
                margin-top: -1px;
            }
            .dropdown-menu{
                min-width: 120px;
            }
        </style>
    </head>
    <body>
        <div class="container-fluid">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h2 align="center">Consolidated Report</h2>
                    <div class="row">
                        <h3 align="center"> 
                            Financial Year ${financialYear}                              

                        </h3>
                    </div>
                    <a href="COViewPostTerminationList.htm?fy=${financialYear}"><button type="submit" name="btnPTAer" value="Back" class="btn btn-primary">Back</button></a>          
                </div>
                <form:form action="#" method="POST" commandName="command">               
                    <input type="hidden" name="financialYear" value="${financialYear}"/>
                    <div class="panel-body">
                        <table class="table table-bordered" width="100%">
                            <thead>
                                <tr>
                                    <th width="10%">SL No</th>
                                    <th width="30%">Post Name</th>
                                    <th width="30%">Pay Scale</th>
                                    <th width="10%">No Of Post Terminated</th>

                                </tr>
                            </thead>
                            <tbody>
                                <c:set var="GtotalPost" value="${0}" />
                                <c:set var="status" value="N" />
                                <c:forEach items="${postterminationcolist}" var="offlist" varStatus="count">
                                    <c:set var="GtotalPost" value="${GtotalPost+offlist.noOfPost}" />
                                    <c:set var="status" value="Y" />
                                    <tr>
                                        <td>
                                            ${count.index + 1}
                                        </td>
                                        <td>
                                            <c:out value="${offlist.post}"/>
                                        </td>
                                        <td> 
                                            <c:out value="${offlist.payscale}"/>
                                        </td>
                                        <td>
                                            <c:out value="${offlist.noOfPost}"/>
                                        </td>

                                    </tr>
                                </c:forEach>
                                <tr>
                                    <td>&nbsp;</td>
                                    <td>&nbsp;</td>    

                                    <td><strong>Total Post Terminated</strong></td>   
                                    <td><strong style="color:green">${GtotalPost}</strong></td>    

                                </tr>     
                            </tbody>
                        </table>
                               
                        
                                  
                </form:form>
                <div class="panel-footer"></div>
            </div>
        </div>

    </body>
</html>
