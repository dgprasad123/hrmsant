<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%
    String contextPath = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + contextPath + "/";

%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        <link href="css/sb-admin.css" rel="stylesheet">
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <script type="text/javascript">
            function viewEmployeeData(empid) {
                var cadrecode = $("#cadrecode").val();
                window.location = "getCadreEmpData.htm?empid=" + empid + "&cadrecode=" + cadrecode;
                // alert(cadrecode);
                /* return false;
                 url = "getCadreEmpData.htm?empid=" + empid;
                 
                 clearform("myModal");
                 $.getJSON(url, function (data) {
                 $("#myModal").find('input:text, input:checkbox').each(function () {
                 var temp = $(this).attr("id");
                 $(this).val(data[temp]);
                 });
                 //fname mname lname
                 });
                 $("#myModal").modal('show');*/
            }
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
                                    <i class="fa fa-dashboard"></i>  <a href="index.html">Dashboard</a>
                                </li>
                                <li class="active">
                                    <i class="fa fa-file"></i> Cadre 
                                </li>
                                <li class="active">
                                    <i class="fa fa-file"></i> Employee Detail 
                                </li>
                            </ol>
                        </div>
                    </div>

                    <div class="row">
                        <form class="form-horizontal" action="cadreEmployeeSearch.htm" method="post">
                            <div class="form-group">                                
                                <label class="control-label col-sm-2">Name:</label>
                                <div class="col-sm-4"> <b class="form-control">${Employee.intitals} ${Employee.fname} ${Employee.mname} ${Employee.lname}</b></div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-sm-2">ID Number:</label>
                                <div class="col-sm-4">
                                    <b class="form-control">${Employee.cadreId}</b>                                        
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-sm-2">Allotment Year:</label>
                                <div class="col-sm-4">                                
                                    <input type="text" id="cardeallotmentyear" name="cardeallotmentyear" size="4" maxlength="4" class="form-control" value="${Employee.cardeallotmentyear}"/>                                
                                </div>
                            </div> 

                            <div class="form-group">
                                <label class="control-label col-sm-2">Recruitment Source:</label>
                                <div class="col-sm-4"> <b class="form-control">${Employee.recsource}</b></div>
                            </div>

                            <div class="form-group">
                                <label class="control-label col-sm-2">DOJ to the Service:</label>
                                <div class="col-sm-4">
                                    <input type="text" name="dojg" value="${Employee.joindategoo}" class="form-control" placeholder="Date of Joining to the Service"/>
                                </div>
                            </div>  
                            <div class="form-group">
                                <label class="control-label col-sm-2">Home State:</label>
                                <div class="col-sm-4">
                                    <input type="text" id="homestate" name="homestate" size="4" class="form-control" value="${Employee.domicile}"/>                         

                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-sm-2">Home District:</label>
                                <div class="col-sm-4">
                                    <input type="text" id="homedist" name="homedist" size="4" class="form-control" value="${Employee.permanentdist}"/>                         

                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-sm-2">Address:</label>
                                <div class="col-sm-4">
                                    <textarea class="form-control" name="permanentaddr" id="permanentaddr">${Employee.permanentaddr}</textarea>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-sm-2">Current Position:</label>
                                <div class="col-sm-8">
                                    <b class="form-control">${Employee.spn}</b>
                                </div>
                            </div> 
                            <div style="margin:20px">&nbsp;</div>                                   
                            <div align="center" >       
                                <button type="submit" class="btn btn-primary">Back</button>
                            </div>
                        </form>      

                    </div>




                </div>
            </div>
        </div>

        <!-- Modal -->


    </body>
</html>
