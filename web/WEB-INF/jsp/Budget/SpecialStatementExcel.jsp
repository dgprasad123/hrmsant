<%-- 
    Document   : SpecialStatementExcel
    Created on : Apr 13, 2022, 12:28:12 PM
    Author     : Madhusmita
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
            function beforeDownload() {

                var year = $('#sltYear').val();
                var month = $('#sltMonth').val();
                if (year == "") {
                    alert("Please Select Year");
                    return false;
                }
                if (month == "") {
                    alert("Please Select Month");
                    return false;
                }
                return true;

            }

            function downloadSpecialStatement(deptCode,deptName) {
                if (beforeDownload()) {
                    //alert(deptCode);
                    var year = $('#sltYear').val();
                    var month = $('#sltMonth').val();
                    window.open("downloadSpecialStatementExcel.htm?dept=" + deptCode +"&deptname="+deptName+ "&year=" + year + "&month=" + month, "_blank");
                }
            }
        </script>
        <title>Special Statement</title>
    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/hrmsadminmenu.jsp"/>        
            <div id="page-wrapper">    

                <div class="container-fluid">

                    <div class="row">
                        <div class="col-lg-12">                            
                            <ol class="breadcrumb">
                                <li>
                                    <i class="fa fa-dashboard"></i>  <a href="index.html">Dashboard</a>
                                </li>
                                <li class="active">
                                    <i class="fa fa-file"></i> Download Special Statement 
                                </li>                                
                            </ol>
                        </div>
                    </div>
                    <div style="text-align:center;">
                        <form:form action="specialStatement.htm" commandName="BudgetForm" method="POST">
                            <div class="panel-body">
                                <div class="row">
                                    <div class="col-lg-12">
                                        <table border="0" width="60%" cellspacing="0" style="font-size:12px; font-family:verdana;">
                                            <%--<tr style="height:40px;">
                                                <td  width="50%" style="color:black;font-size: 15px;" >
                                                    <label>Select Department</label>
                                                </td>
                                                <td>
                                                    <form:select path="sltDeptCode" id="sltDeptCode" class="form-control" style="width:300px;" >
                                                        <form:option value="">--Select Department Name--</form:option>
                                                        <form:options items="${deptlist}" itemValue="deptCode" itemLabel="deptName"/>
                                                    </form:select>
                                                </td>
                                            </tr>--%>
                                            <tr>
                                               <div class="col-50">
                                                <div class="pull-center" style="height:20px;">                                                        
                                                    <h3><u><b>Special Statement</b></u></h3>
                                                </div>
                                            </div>  
                                            </tr>
                                            <br/><br/>
                                            
                                            <tr style="height: 40px; ">
                                                <td style="color:black;font-size: 15px;" class="col-lg-5">
                                                    <label>Select Month</label>
                                                </td>
                                                <td>
                                                    <form:select path="sltMonth" id="sltMonth" class="form-control" style="width:200px;" >
                                                        <form:option value="">--Select Month--</form:option>
                                                        <form:option value="0">JANUARY</form:option>
                                                        <form:option value="1">FEBRUARY</form:option>
                                                        <form:option value="2">MARCH</form:option>
                                                        <form:option value="3">APRIL</form:option>
                                                        <form:option value="4">MAY</form:option>
                                                        <form:option value="5">JUNE</form:option>
                                                        <form:option value="6">JULY</form:option>
                                                        <form:option value="7">AUGUST</form:option>
                                                        <form:option value="8">SEPTEMBER</form:option>
                                                        <form:option value="9">OCTOBER</form:option>
                                                        <form:option value="10">NOVEMBER</form:option>
                                                        <form:option value="11">DECEMBER</form:option>
                                                    </form:select>
                                                </td>
                                                <td style="color:black;font-size: 15px;" class="col-lg-4"> 
                                                    <label>Select Year</label>
                                                </td>
                                                <td>
                                                    <form:select path="sltYear" id="sltYear" class="form-control"  style="width:200px;">
                                                        <form:option value="">--Select Year--</form:option> 
                                                        <form:option value="2020">2020</form:option>
                                                        <form:option value="2021">2021</form:option>
                                                        <form:option value="2022">2022</form:option>
                                                        <form:option value="2023">2023</form:option>
                                                    </form:select>
                                                </td>
                                                <td width="10%">

                                                </td>

                                            </tr>                                 

                                        </table>
                                        <table id="myTable" class="table table-striped table-bordered table-success" width="100%" cellspacing="0">
                                            <thead>                                    
                                            <div class="col-50">
                                                <div class="pull-center" style="height:20px;">                                                        
                                                    
                                                </div>
                                            </div>                                            
                                            <tr class="header">
                                                <th>SL NO</th>
                                                <th>DEPARTMENT NAME</th>
                                                <th>ACTION</th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach var="deptlist" items="${deptlist}" varStatus="count">
                                                    <tr>
                                                        <td><c:out value="${count.index+1}"/></td>
                                                        <td style="text-align:left;"><c:out value="${deptlist.deptName}"/></td>                                            
                                                        <td style="width:10%"><a href="javascript:void(0)" class="btn  btn-default btn-success" onclick="downloadSpecialStatement('${deptlist.deptCode}','${deptlist.deptName}');"><span class="glyphicon glyphicons-remove" ></span>Download Excel</a></td>                                    
                                                    </tr>
                                                </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </form:form>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
