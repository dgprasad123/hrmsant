<%-- 
    Document   : CensusReportOfficewise
    Created on : May 30, 2023, 11:16:35 AM
    Author     : Madhusmita
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" autoFlush="true" buffer="64kb"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        <link href="css/sb-admin.css" rel="stylesheet">

        <script src="js/jquery.min-1.9.1.js"></script>        
        <script src="js/bootstrap.min.js"></script>
        <script src="js/jquery.freezeheader.js"></script>


        <script type="text/javascript">
            $(document).ready(function() {
                $("#dataTab").freezeHeader();
            });

            function generateCensusReport(offcode, ddocode, fyear) {
                //alert("office::" + offcode);
                window.open("GenerateCensusReport.htm?offCode=" + offcode + "&ddoCode=" + ddocode + "&fiyear=" + fyear);

            }
            function generateAllCensusReport() {
                window.open("GenerateAllCensusReport.htm");
            }

            function getDistWiseOfficeList() {
                var distcode;
                distcode = $('#distCode').val();
                $('#sltOffCode').empty();
                $('#sltOffCode').append('<option value="">--Select Office--</option>');
                var url = 'getDistrictWiseOfficesJSON.htm?distcode=' + distcode;
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#sltOffCode').append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');
                    });
                });

            }

        </script>
        <style>
            table th {
                padding-top: 12px;
                padding-bottom: 12px;
                //text-align: left;
                background-color: #a2d4ed;
                //color: white;
            }
        </style>
    </head>
    <body>
        <form:form action="distWiseCensusReport.htm" commandName="censusOffice" method="post">
            <div id="wrapper">
                <jsp:include page="../tab/hrmsadminmenu.jsp"/>        
                <div id="page-wrapper">
                    <div class="container-fluid"> 
                        <div class="row">
                            <div class="form-group">
                                <div style="text-align:center;"><h2><u>Census Report</u></h2> </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="container"> 
                                <table>
                                    <tr>
                                        <td class="col-lg-1">
                                            <label class="control-label">District:</label></td>                      
                                        <td class="col-lg-4">
                                            <form:select path="distCode" id="distCode" class="form-control" onchange="getDistWiseOfficeList();">
                                        <option value="">--Select District--</option>
                                        <form:options items="${distList}" itemValue="distCode" itemLabel="distName"/>
                                    </form:select>
                                    </td> 
                                    <td class="col-lg-2">
                                        <label class="control-label">Financial Year:</label></td>                      
                                    <td class="col-lg-3">
                                        <form:select path="fyYear" id="fyYear" class="form-control">
                                        <option value="">-Select Year-</option>
                                        <option value="2019" selected="selected">2019</option>
                                        <option value="2020" selected="selected">2020</option>
                                        <option value="2021" selected="selected">2021</option>
                                        <option value="2022" selected="selected">2022</option>
                                        <option value="2023" selected="selected">2023</option>
                                        <option value="2024" selected="selected">2024</option>
                                    </form:select>
                                    </td> 
                                    <td class="col-lg-2"> 
                                        <button type="submit" class="btn btn-success" style="width:150px;right:70px"><span style="color:black;">Search</span></button>
                                    </td>
                                    </tr>
                                </table>
                            </div>
                        </div>
                        <br/>
                    </form:form>

                    <div class="panel-body">                                
                        <table  class="table table-bordered table-hover" id="dataTab">
                            <thead>  
                                <tr>
                                    <th  style="text-align:center;">SL NO</th>
                                    <th  style="text-align:center;">DDO CODE</th>
                                    <th  style="text-align:center;">OFFICE NAME</th>
                                    <th  style="text-align:center;">ACTION</th>
                                </tr>
                            </thead>
                            <tbody  style="overflow-y: auto">
                                <c:forEach var="ofclist" items="${officelist}" varStatus="count">
                                    <tr style=" font-size: 13px;font-family:serif; ">
                                        <td style="text-align:center;"><c:out value="${count.index+1}"/></td>
                                        <td><c:out value="${ofclist.ddoCode}"/></td>
                                        <td><c:out value="${ofclist.offName}"/></td>                                            
                                        <td style="width:10%"><a href="javascript:void(0)" onclick="generateCensusReport('${ofclist.offCode}', '${ofclist.ddoCode}', '${fiyear}')" class="btn  btn-default btn-primary"><i class="glyphicon glyphicon-download-alt"> DownLoad Census Report</i></a></td>                                    
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>

    </body>
</html>