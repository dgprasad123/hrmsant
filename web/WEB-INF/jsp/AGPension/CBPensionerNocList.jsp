<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        <link href="css/sb-admin.css" rel="stylesheet">
        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
        <script type="text/javascript" src="js/datagrid-detailview.js"></script>
        <script type="text/javascript" src="js/webcam.js"></script>
        <script type="text/javascript"  src="js/jquery.colorbox-min.js"></script>


    </head>

    <c:choose>
        <c:when test = "${fn:contains(LoginUserBean.loginuserid, 'alf')}">
            <body style="margin-top:0px;background:#188B7A;">
                <jsp:include page="../tab/AlfaMenu.jsp"/>  
                <div id="wrapper"> 
                    <div id="page-wrapper" style="margin-top:145px;z-index:0;">
                    </c:when>
                    <c:when test = "${fn:contains(LoginUserBean.loginusername, 'paradmin')}">
                        <body style="margin-top:0px;background:#188B7A;">
                            <jsp:include page="../tab/ParMenu.jsp"/>  
                            <div id="wrapper"> 
                                <div id="page-wrapper" style="margin-top:145px;z-index:0;">
                                </c:when>
                                <c:otherwise>
                                    <body>
                                        <div id="wrapper">
                                            <jsp:include page="../tab/hrmsadminmenu.jsp"/>
                                            <div id="page-wrapper">
                                            </c:otherwise>
                                        </c:choose>

                                        <div class="container-fluid" style="padding-top: 125px;padding-bottom: 125px;">
                                            <div class="panel panel-default">
                                                <div class="panel-heading">
                                                    <div class="row">
                                                        <div class="col-lg-12">
                                                            <h2 style="color:  #0071c5;" align="center"> Crime Branch NOC Request List</h2>
                                                        </div>
                                                    </div>

                                                </div>
                                                <div class="panel-body">
                                                    <div class="table-responsive">

                                                        <div class="table-responsive">
                                                            <table class="table table-bordered table-hover table-striped">
                                                                <thead>
                                                                    <tr style="background-color: #0071c5;color: #ffffff;">
                                                                        <th >Slno</th>
                                                                        <th >HRMS ID</th>
                                                                        <th >GPF NO</th>
                                                                        <th >Name</th>                                
                                                                        <th >DOB</th>
                                                                        <th >DOS</th>
                                                                        <th >POST</th>
                                                                        <th >Download NOC</th>
                                                                        <th>Action </th>

                                                                    </tr>
                                                                </thead>
                                                                <tbody>
                                                                    <c:forEach items="${pensionList}" var="pnoc" varStatus="count">
                                                                        <tr>                                    
                                                                            <td>${count.index + 1}</td>
                                                                            <td>${pnoc.hrmsid}</td>
                                                                            <td>${pnoc.gpfNo}</td>
                                                                            <td>${pnoc.name}</td>
                                                                            <td>${pnoc.dob}</td>
                                                                            <td>${pnoc.dos}</td>
                                                                            <td>${pnoc.post}</td>
                                                                            <c:if test="${empty pnoc.cNocFileName}">
                                                                                <td><strong class="text-success">NA</strong>   </td>
                                                                            </c:if>
                                                                            <c:if test="${not empty pnoc.cNocFileName}">
                                                                                <td><strong class="text-danger">Download NOC</strong>   </td>
                                                                            </c:if>     


                                                                            <c:if test="${pnoc.vNocStatus == 'N'  }">
                                                                                <td><a href="CBNocUpload.htm?nocId=${pnoc.nocId}&hrmsid=${pnoc.hrmsid}" class='text-danger'><strong class=" text-danger">Upload NOC</strong></a></td>
                                                                            </c:if>
                                                                            <c:if test="${pnoc.vNocStatus == 'Y' }">
                                                                                <td><a href="CBNocUpload.htm?nocId=${pnoc.nocId}&hrmsid=${pnoc.hrmsid}" class='text-info'><strong>View NOC</strong></a></td>
                                                                            </c:if>
                                                                        </tr>

                                                                    </c:forEach>
                                                                </tbody>
                                                            </table>
                                                        </div>                
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                            </body>
                            </html>
