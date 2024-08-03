<%-- 
    Document   : ImportEmployee
    Created on : Sep 7, 2018, 1:01:19 PM
    Author     : Madhusmita
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        <link href="css/sb-admin.css" rel="stylesheet">
        <link rel="stylesheet" href="css/chosen.css">
        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>
        <script src="js/chosen.jquery.min.js"></script>
        <script type="text/javascript">
            function onSubmit()
            {
                alert("Excel File Imported Successfully");
            }
        </script>
    </head>
    <body>
        <form:form class="form-horizontal" action="importEmployeXL.htm" enctype="multipart/form-data" commandName="importEmpExcelData" >
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
                                        <i class="fa fa-file"></i> Import Excel Employee
                                    </li>                                
                                </ol>
                            </div>
                        </div> 
                    </div>
                    <div style=" margin-bottom: 5px;" class="panel panel-info">
                        <div class="panel-body">
                            <table>
                                <tr>
                                <div class="form-group">
                                    <label class="control-label col-sm-3">Attach Excel:</label>
                                    <div class="col-sm-9">
                                        <input type="file" name="documentFile" id="documentFile" accept=".xls,.xlsx"/>
                                        <span style="color:#FF0000;font-weight:bold;">( Please attach Excel (.xls ) file only )</span>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-3"></label>
                                    <div class="col-sm-9">
                                        <%--<img src="images/Sample_ImportExcel_Data.jpg" alt="" height="200" width="800" /><br />--%>
                                        <span style="color:#008900;font-weight:bold;"><br /><br /><a href="images/ExcelFormatSampleData.xls">Download Sample Excel File</a></span>
                                    </div>

                                </div>
                                </tr>
                                <tr class="col-sm-3">
                                <input type="submit" class="btn btn-default btn-success" name="action"  value="Import"/>
                                </tr>
                            </table>
                            <c:if test="${not empty imprtMsg}">
                                <h4  style="width: 600px ; color: #008900; text-align: center">${imprtMsg}</h4>
                            </c:if>
                                
                        </div>
                    </div>
                </div>
            </div>            
        </form:form>
    </body>
</html>
