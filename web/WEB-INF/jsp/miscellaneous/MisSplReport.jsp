<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        <link href="css/sb-admin.css" rel="stylesheet">
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <script  type="text/javascript">
            function deleteCandidate(candidateId, categoryId)
            {
                if (confirm("Are you sure you want to delete the Candidate?"))
                    self.location = 'deleteCandidate.htm?candidateId=' + candidateId + '&categoryId=' + categoryId;
            }

        </script>
        <style type="text/css">
            table.form-table td{padding:5px;}
        </style>
    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/deptmismenu.jsp"/>               
            <!-- Site wrapper -->
            <div class="wrapper">            
                <div id="page-wrapper">
                    <div class="container-fluid">
                        <!-- Page Heading -->
                        <div class="row">
                            <div class="col-lg-12">                            

                            </div>
                        </div>
                        <div class="row" style="min-height:600px">
                            <div class="col-lg-12">
                                <h2 style="margin-top:0px;">Department Wise Cadre Summary </h2>


                                <c:if test = "${not empty selectyear}">        
                                    <table id="account" class="table table-bordered table-striped" >
                                        <thead>

                                            <tr style='background-color:black;color:white;font-size:14px'>
                                                <th>&nbsp;</th>
                                                <th valign='top'>Department Name</th>
                                                <th>Name Of Cadre </th>
                                                <th>Name Of the Base Level Post </th>
                                                 <th>Group</th>
                                                <th>Sanction Strength</th>
                                                <th>Men in Position</th>
                                                <th>Vacancy</th>
                                                <th>Vacancy to be filled up by direct recruitment</th>
                                                <th>Vacancy to be filled up by promotion</th>




                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:if test = "${not empty vacantPostList}"> 
                                                <c:set var="totalSan" value="${0}" />
                                                <c:set var="minpos" value="${0}" />
                                                <c:set var="tvacancy" value="${0}" />
                                                <c:set var="bvacancy" value="${0}" />
                                                <c:set var="pvacancy" value="${0}" />
                                                <c:set var="gtotalSan" value="${0}" />
                                                <c:set var="gminpos" value="${0}" />
                                                <c:set var="gvacancy" value="${0}" />
                                                <c:set var="gbvacancy" value="${0}" />
                                                <c:set var="gpvacancy" value="${0}" />

                                                <c:set var="slno" value="${0}" />

                                                <c:forEach items="${vacantPostList}" var="vacantPost">
                                                    <c:set var="totalSan" value="${vacantPost.sancPost + vacantPost.sancPostPro}" />
                                                    <c:set var="minpos" value="${vacantPost.maninPost + vacantPost.maninPostPro}" />
                                                    <c:set var="tvacancy" value="${totalSan - minpos}" />
                                                    <c:set var="pvacancy" value="${tvacancy - vacantPost.vacancy}" />
                                                    <c:set var="gminpos" value="${gminpos + minpos}" />
                                                    <c:set var="gtotalSan" value="${gtotalSan + totalSan}" />
                                                    <c:set var="gvacancy" value="${gvacancy + tvacancy}" />
                                                    <c:set var="gbvacancy" value="${gbvacancy + vacantPost.vacancy}" />
                                                    <c:set var="gpvacancy" value="${gpvacancy + pvacancy}" />

                                                    <c:set var="slno" value="${slno +1}" />

                                                    <tr scope="row">
                                                        <td>${slno}</td>
                                                        <td><a href='#'>${vacantPost.deptName}</a></td>
                                                        <td>${vacantPost.cadreId}</td> 
                                                        <td>${vacantPost.postId}</td>
                                                        <td>${vacantPost.groupName}</td>
                                                        <td>${totalSan}</td>
                                                        <td>${minpos}</td>
                                                        <td>${tvacancy}</td>				
                                                        <td>${vacantPost.vacancy}</td>
                                                        <td>${pvacancy}</td>





                                                    </tr>
                                                </c:forEach>
                                                <tr style='background-color:black;color:white;font-size:14px'>
                                                    <th>&nbsp;</th>
                                                    <th>&nbsp;</th>
                                                    <th>&nbsp;</th>
                                                    <th>&nbsp;</th>
                                                    <th>${gtotalSan}</th>
                                                    <th>${gminpos}</th>
                                                    <th>${gvacancy}</th>
                                                    <th>${gbvacancy}</th>
                                                    <th>${gpvacancy}</th>

                                                </tr>
                                            </c:if>
                                            <c:if test = "${empty vacantPostList}">
                                                <tr>
                                                    <th colspan="9" style='color:red' align='center'><h3>Data is not available</h3></th>
                                            </tr>
                                        </c:if>     
                                        </tbody>	
                                    </table>  
                                </c:if>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
