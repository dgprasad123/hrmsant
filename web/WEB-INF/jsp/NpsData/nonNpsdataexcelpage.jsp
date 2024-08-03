<%-- 
    Document   : nonNpsdataexcelpage
    Created on : 23 Sep, 2022, 11:49:15 AM
    Author     : Devikrushna
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
            
            function check(){   
              var alltr= $('#billtype').val();            
               if (alltr == "allbill") {                   
                      $('#trCode').val("alltreasury").show();                       
                } 
            }
            
            function saveCheck() {

                var year = $('#sltYear').val();
                var month = $('#sltMonth').val();
                if ($('#billtype').val() == "") {
                    alert("Select Bill type");
                    return false;
                }
                if ($('#trCode').val() == "") {
                    alert("Select Treasury Name");
                    return false;
                }
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


        </script>
        <title>HRMS NPS DATA</title>
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
                                    <i class="fa fa-file"></i> HRMS NON-NPS DATA 
                                </li>                                
                            </ol>
                        </div>
                    </div>
                    <div style="text-align:center;">
                        <form:form action="nonNpsdataexcelpage.htm" commandName="nonnpsDataForm" method="POST">
                            <form:hidden path="hidtrName" id="hidtrName"/>

                            <div class="panel-body">
                                <div class="row">
                                    <div class="col-lg-12">                                        
                                        <div class="col-50">
                                            <div class="pull-center" style="height:20px;">                                                        
                                                <h3><u><b>HRMS NON-NPS DATA </b></u></h3>
                                            </div>
                                        </div>  
                                        <br/><br/>
                                        <div class="col-lg-12" style="margin-bottom: 7px;">
                                            <div class="col-lg-4">
                                                <form:select path="billtype" id="billtype" class="form-control" onchange="check()">
                                                    <form:option value="">--Select Bill Type--</form:option>
                                                    <form:option value="allbill" >All Bill</form:option>
                                                    <form:option value="42">SALARY (42)</form:option>
                                                    <form:option value="69">GRANT-IN AID (SALARY-69)</form:option>
                                                </form:select>
                                            </div>
                                            <div class="col-lg-4">
                                                <form:select path="trCode" id="trCode" class="form-control">
                                                    <form:option value="">--Select Treasury--</form:option>
                                                    <form:option id="alltreasury" value="alltreasury">All Treasury</form:option>
                                                    <form:options items="${treasurylist}" itemValue="trCode" itemLabel="trName"/>
                                                </form:select>
                                            </div>
                                        </div>
                                        <div class="col-lg-12" style="margin-bottom: 7px;">                                           
                                            <div class="col-lg-4">
                                                <form:select path="sltMonth" id="sltMonth" class="form-control">
                                                    <form:option value="">--Select Month--</form:option>
                                                    <%-- <form:option value="0">JANUARY</form:option>
                                                    <form:option value="1">FEBRUARY</form:option>
                                                    <form:option value="2">MARCH</form:option>
                                                    <form:option value="3">APRIL</form:option>
                                                    <form:option value="4">MAY</form:option>
                                                    <form:option value="5">JUNE</form:option> --%>
                                                    <form:option value="6">JULY</form:option>
                                                    <form:option value="7">AUGUST</form:option>
                                                    <form:option value="8">SEPTEMBER</form:option>
                                                    <form:option value="9">OCTOBER</form:option>
                                                    <form:option value="10">NOVEMBER</form:option>
                                                    <form:option value="11">DECEMBER</form:option>
                                                </form:select>
                                            </div>

                                            <div class="col-lg-4">
                                                <form:select path="sltYear" id="sltYear" class="form-control">
                                                    <form:option value="">--Select Year--</form:option>                                                  
                                                    <form:option value="2022">2022</form:option>
                                                    <form:option value="2023">2023</form:option>
                                                </form:select>
                                            </div>
                                            <div class="col-lg-4 text-left">
                                                <input type="submit" name="DownloadExcel" value="Download Excel" class="btn btn-primary" onclick="return saveCheck()" />                                               
                                            </div>
                                        </div>
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
