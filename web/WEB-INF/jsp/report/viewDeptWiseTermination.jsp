<%-- 
    Document   : ManageSanctionPost
    Created on : 17 May, 2019, 6:22:40 PM
    Author     : Surendra
--%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
          <link href="css/sb-admin.css" rel="stylesheet">

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>        
        <script type="text/javascript" src="js/bootstrap.min.js"></script>



        <style type="text/css">
            .saveSuccess{
                color: #00cc66;
                font-weight: bold;
            }
            .saveError{
                color: #ff3333;
                font-weight: bold;
            }
            .row{
                margin-left:0px;
                margin-right:0px;
            }
        </style>
    </head>
    <body>
        <jsp:include page="../tab/hrmsadminmenu.jsp"/>
        <div id="wrapper">
            <form:form action="listingTerminationPostFD.htm" method="POST" commandName="command">

                <div class="container-fluid">
                    <div class="panel panel-default">
                        <div class="panel-heading"></div>
                        <div class="panel-body">
                            <h2 align='center'>${departmentname} DEPARTMENT</h2>                     

                            <div align="center">(Listing All Termination Post Details)</div>

                            <table class="table table-striped table-bordered" width="90%">
                                <thead>
                                    <tr>

                                        <th width="1%" >Sl No</th>
                                        <th >Post Name</th>
                                        <th >Pay Scale</th>
                                        <th>GP</th>
                                        <th >Total Termination Post</th>


                                    </tr>

                                </thead>
                                 <c:set var="GtotalEmp" value="${0}" /> 
                                <c:forEach var="partAGrpA" items="${PartAGrouplist}"  varStatus="theCount">
                                     <c:set var="GtotalEmp" value="${GtotalEmp + partAGrpA.postTerminated}" />

                                    <tr>
                                        <td>${theCount.index + 1}</td>
                                        <td> ${partAGrpA.postname} </td>
                                        <td> ${partAGrpA.scaleofPay} </td>
                                        <td> ${partAGrpA.gp} </td>
                                        <td>${partAGrpA.postTerminated}</td>

                                    </tr>
                                </c:forEach>  
                                   <tr>
                                        <td>&nbsp;</td>
                                        <td> &nbsp; </td>
                                        <td> &nbsp;</td>
                                        <td> <strong>Total</strong> </td>
                                        <td><strong>${GtotalEmp}</strong></td>
                                   </tr>     
                                        



                            </table>
                            <div class="modal-footer">
                                <a href="listingTerminationPostFD.htm?financialYear=${financialYear}"><button type="button" class="btn btn-primary" >Back</button></a>
                            </div>

                        </div>

                    </div>

                </div>



            </form:form>
        </div>

        <script type="text/javascript">
            function validate() {
                if ($("#financialYear").val() == '') {
                    alert('Please select Financial Year.');
                    $("#financialYear").focus();
                    return false;
                }
            }
        </script>    

    </body>
</html>

