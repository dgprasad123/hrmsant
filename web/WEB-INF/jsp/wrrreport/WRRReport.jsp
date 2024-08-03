<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>

        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <!-- Custom CSS -->
        <link href="css/sb-admin.css" rel="stylesheet">

        <!-- Bootstrap Core JavaScript -->
        <script src="js/bootstrap.min.js"></script>

        <script type="text/javascript">
            $(document).ready(function () {
                $('#treasury').combobox({
                    onSelect: function (record) {
                        $('#ddocode').combobox('clear');
                        var url = 'treasuryDDOListJSON.htm?parentTrCode=' + record.treasuryCode + '&subTrCode=';
                        $('#ddocode').combobox('reload', url);
                    }
                });
            });

            function openWRRReport() {
                var calcyear = $('#sltYear').combobox('getValue');
                var calcmonth = $('#sltMonth').combobox('getValue');
                $("#searchbtn").attr("disabled", true);
                if (calcyear == '') {
                    alert("Please select Year");
                } else if (calcmonth == '') {
                    alert("Please select Month");
                } else {
                    $.post("WrrReportDataJson.htm", {calcyear: calcyear, calcmonth: calcmonth})
                            .done(function (data) {
                                var wrrlist = data.wrrdata;
                                populateDataInGrid(wrrlist);
                                //$(".loader").hide();
                                $("#searchbtn").attr("disabled", false);
                            })
                }
            }
            function populateDataInGrid(wrrlist) {
                for (var i = 0; i < wrrlist.length; i++) {
                    var row = '';
                    row = '<tr>';
                    row = row + '<td>' + i + '</td>';
                    row = row + '<td>' + wrrlist[i].hrmsid + '</td>';
                    row = row + '<td>' + wrrlist[i].gpfno + '</td>';
                    row = row + '<td>' + handleUndefind(wrrlist[i].fname) + " " + handleUndefind(wrrlist[i].mname) + " " + handleUndefind(wrrlist[i].lname) + '</td>';
                    row = row + '<td>' + wrrlist[i].wtax + '</td>';
                    row = row + '<td>' + wrrlist[i].swtax + '</td>';
                    row = row + '<td>' + handleUndefind(wrrlist[i].consumerNo) + '</td>';
                    row = row + '<td>' + handleUndefind(wrrlist[i].tvno) + '</td>';
                    row = row + '<td>' + handleUndefind(wrrlist[i].tvdate) + '</td>';
                    row = row + '</tr>';
                    $("#wrrgrid").append(row);
                }
            }
            function handleUndefind(datavar) {
                if (datavar) {
                    return datavar;
                } else {
                    return '';
                }
            }
        </script>
    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/hrmsadminmenu.jsp"/>        
            <div id="page-wrapper">
                <div class="container-fluid">
                    <div align="center" style="margin-top:5px;margin-bottom:7px;">
                        <form:form action="wrrReportData.htm" method="POST" commandName="schedule">
                            <table border="0" cellspacing="0" cellpadding="0" style="font-size:12px; font-family:verdana;">                            
                                <tr style="height:40px;">
                                    <td align="center">
                                        <label>YEAR</label>
                                    </td>
                                    <td>
                                        <form:select path="recMonth" style="width:200px;" cssClass="form-control">
                                            <form:option value="1">JANUARY</form:option>
                                            <form:option value="2">FEBUARY</form:option>
                                            <form:option value="3">MARCH</form:option>
                                            <form:option value="4">APRIL</form:option>
                                            <form:option value="5">MAY</form:option>
                                            <form:option value="6">JUNE</form:option>
                                            <form:option value="7">JULY</form:option>
                                            <form:option value="8">AUGUST</form:option>
                                            <form:option value="9">SEPTEMBER</form:option>
                                            <form:option value="10">OCTOBER</form:option>
                                            <form:option value="11">NOVEMBER</form:option>
                                            <form:option value="12">DECEMBER</form:option>
                                        </form:select>                                    
                                    </td>
                                    <td align="center">
                                        <label>MONTH:</label>
                                    </td>
                                    <td>
                                        <form:select path="recYear" style="width:200px;" cssClass="form-control">
                                            <form:option value="2019">2019</form:option>
                                            <form:option value="2018">2018</form:option>
                                            <form:option value="2017">2017</form:option>
                                            <form:option value="2016">2016</form:option>
                                            <form:option value="2015">2015</form:option>
                                        </form:select>                                                                      
                                    </td>                                                         
                                    <td>
                                        <div class="btn-group" style="padding-left: 50px;">
                                            <button type="submit" value="Search" name="action" class="btn btn-primary"><span class="glyphicon glyphicon-search"></span> Search</button>
                                            <button type="submit" value="Download" name="action" class="btn btn-primary"><span class="glyphicon glyphicon-export"></span> Download</button>
                                        </div>
                                    </td>                                    
                                </tr>
                            </table>
                        </form:form>
                        <div class="table-responsive">
                            <div class="table-responsive">
                                <table class="table table-bordered table-hover table-striped">
                                    <thead>
                                        <tr>
                                            <th>#</th>
                                            <th>Consumer No</th>
                                            <th>HRMS Id</th>
                                            <th>MONTH</th>
                                            <th>YEAR</th>
                                            <th>Employee Name</th>
                                            <th>QRS NO</th>
                                            <th>QRS Type</th>
                                            <th>Unit/Area</th>
                                            <th>DDO Code</th>
                                            <th>Office Name</th>
                                            <th>TV No</th>
                                            <th>TV Date</th>
                                            <th>Water Tax</th>
                                            <th>Sewerage Tax</th>                                            
                                            <th>Date of Allotment</th>
                                        </tr>
                                    </thead>
                                    <tbody id="wrrgrid">
                                        <c:forEach items="${waterRent}" var="wrr" varStatus="cnt">
                                            <tr>
                                                <td>${cnt.index+1}</td> 
                                                <td>${wrr.consumerNo}</td>
                                                <td>${wrr.hrmsid}</td>
                                                <td>${wrr.recoverymonth}</td>
                                                <td>${wrr.recoveryyear}</td>
                                                <td>${wrr.fname} ${wrr.mname} ${wrr.lname}</td>
                                                <td>${wrr.quarterNo}</td>                                                                                                
                                                <td>${wrr.qrtrtype}</td>
                                                <td>${wrr.qrtrunit}</td>
                                                <td>${wrr.ddocode}</td>
                                                <td>${wrr.officename}</td>
                                                <td>${wrr.tvno}</td>
                                                <td>${wrr.tvdate}</td>                                                
                                                <td>${wrr.wtax}</td>
                                                <td>${wrr.swtax}</td>                                                
                                                <th>${wrr.dateofallotment}</th>
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
