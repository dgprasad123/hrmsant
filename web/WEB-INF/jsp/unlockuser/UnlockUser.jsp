<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
        <script type="text/javascript">
            $(document).ready(function() {
                $('#sltEmpidGpf').change(function() {
                    $('#txtEmpidGpf').val('');
                });
            });
        </script>
        <style type="text/css">
            .alink{
                color:#fff !important;
                text-decoration: none;
                font-family: Verdana,sans-serif;
                font-size: 12px;                
                background-color: #286090;
                border-color: #204d74;
                white-space: nowrap;
                border: 1px solid transparent;
                border-radius: 4px;
                display: inline-block;
                padding: 6px 12px;

            }
        </style>
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

                            </ol>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-lg-12" style="height: 500px;">
                            <h2>Unlock User</h2>
                            <div class="table-responsive">
                                <form action="unlockuser.htm">
                                    <table class="table table-hover table-striped" style="height:100px;">
                                        <thead>
                                            <tr style="height: 20%">
                                                <th colspan="3">Select HRMS ID/GPF No</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <tr style="height: 40%">
                                                <td widht="40%" align="center">
                                                    <select name="sltEmpidGpf" id="sltEmpidGpf">
                                                        <option value="empid">HRMS ID</option>
                                                        <option value="gfpno">GPF No</option>
                                                    </select>
                                                </td>
                                                <td widht="40%">
                                                    <input type="text" name="txtEmpidGpf" id="txtEmpidGpf"/>
                                                    &nbsp;&nbsp;&nbsp;&nbsp;
                                                    <button type="submit" class="alink">Unlock</button>
                                                </td>
                                            </tr>
                                            <c:if test="${not empty status}">
                                                <tr>
                                                    <td colspan="3" align="center">
                                                        <c:if test="${status == 1}">
                                                            <span style="font-family: Verdana; color: green;">User Unlocked.</span>
                                                        </c:if>
                                                        <c:if test="${status == 2}">
                                                            <span style="font-family: Verdana; color: red;">User does not exist!</span>
                                                        </c:if>
                                                    </td>
                                                </tr>
                                            </c:if>
                                        </tbody>
                                    </table>
                                </form>
                            </div>
                        </div>
                    </div>

                </div>
            </div>
        </div>
    </body>
</html>
