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
                        <h2 align='center'>Finance Department</h2>                     

                        <div align="center">(Listing All Termination Post Details)</div>
                        <div class="row">
                            <div class="col-lg-12">
                                <h3 align="center"> Financial Year 
                                    <form:select path="financialYear" id="financialYear">
                                     
                                        <form:option value="2018-19"> </form:option>
                                        <form:option value="2019-20"> </form:option>
                                    </form:select>

                                    <button type="submit" name="btnAer" value="Search" class="btn btn-primary" onclick="return validate()">Show Termination Post List</button>   
                                </h3> 
                            </div>
                        </div>   
                        <table class="table table-striped table-bordered" width="90%">
                            <thead>
                                <tr>

                                    <th width="1%" >Demand No</th>
                                    <th >Department</th>
                                    <th >Submitted to Finance Department</th>
                                    <th>Action</th>



                                </tr>

                            </thead>
                            <c:forEach var="partAGrpA" items="${PartAGrouplist}"  varStatus="theCount">

                                <tr>                                  
                                    <td> ${partAGrpA.demandNo} </td>
                                    <td> ${partAGrpA.departmentname} </td>
                                    <c:if test="${not empty partAGrpA.offCode}">
                                        <td> <strong style='color:green'>Yes</strong> </td>
                                        <td><a href="viewDeptWiseTermination.htm?offCode=${partAGrpA.offCode}&financialYear=${financialYear}&departmentname=${partAGrpA.departmentname}">View</a></td>
                                    </c:if>
                                    <c:if test="${ empty partAGrpA.offCode}">
                                        <td> No </td>
                                        <td>&nbsp;</td>
                                    </c:if>     



                                </tr>
                            </c:forEach>      



                        </table>
                        
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

