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
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <!-- Custom CSS -->
        <link href="css/sb-admin.css" rel="stylesheet">

        <!-- Bootstrap Core JavaScript -->
        <script src="js/bootstrap.min.js"></script>

        <link rel="stylesheet" type="text/css" href="css/dataTables.bootstrap4.min.css"/>
        <script type="text/javascript"  src="js/jquery-1.12.4.js"></script>
        <script type="text/javascript"  src="js/jquery.dataTables.min.js"></script>
        <script type="text/javascript"  src="js/dataTables.bootstrap4.min.js"></script>

        <script type="text/javascript">
            $(document).ready(function () {
                if ($("#quarterunitarea").val() != "") {
                    showQuarterType();

                }
            });

            function showQuarterType() {
                var qrtrunit = $("#qrtrunit").val();
                $('#qrtrtype').empty();
                $.post("unitWiseQuarterTypeDataJson.htm", {quarterunitarea: qrtrunit})
                        .done(function (data) {
                            var unitAreaList = data.unitAreaList;

                            $.each(unitAreaList, function (i, obj) {
                                $('#qrtrtype').append($('<option>').text(obj.label).attr('value', obj.value));
                            });
                            $("#qrtrtype").val($("#tqrtrtype").val());
                        })
            }


            function showbuildingDetails() {
                var qrtrunit = $("#qrtrunit").val();
                var qrtrtype = $("#qrtrtype").val();
                $('#quarterNo').empty();
                $.post("qtrtypeWiseBuildingDataJson.htm", {quarterunitarea: qrtrunit, qrtrtype: qrtrtype})
                        .done(function (data) {
                            var unitAreaList = data.unitAreaList;

                            $.each(unitAreaList, function (i, obj) {
                                $('#quarterNo').append($('<option>').text(obj.label).attr('value', obj.value));
                            });
                            $("#quarterNo").val($("#quarterNo").val());
                        })
            }


            function validate() {
                if ($("#quarterunitarea").val() == "") {
                    alert("Choose Unit/Area");
                    return false;
                }
            }
            function EmployeeList() {
                window.location = "getTransferList.htm";
            }
        </script>
    </head>
    <body>
        <jsp:include page="header_quarter.jsp">
                    <jsp:param name="menuHighlight" value="NDCAPPLICATIONS" />
                </jsp:include> 
        <div id="wrapper">
                
            <div id="page-wrapper">
                <div class="container-fluid">
                    <div align="center" style="margin-top:5px;margin-bottom:7px;">
                        <h2 class="text-info">Ledger Information</h2>
                        <input type="hidden" name="tqrtrtype" id="tqrtrtype" value="${waterRent.qrtrtype}"/>
                        <form:form action="ledgerinfo.htm" method="POST" commandName="empQuarterBean">
                            <table border="0" cellspacing="0" cellpadding="0" style="font-size:12px; font-family:verdana;">                            
                                <tr style="height:40px;">
                                    <td align="center">
                                        <label>Unit/Area: </label>
                                    </td>
                                    <td>
                                        <form:select path="qrtrunit" cssClass="form-control" onchange="showQuarterType()">
                                            <form:option value="">All</form:option>
                                            <c:forEach items="${quarterUnitAreaList}" var="quarterUnitArea">
                                                <form:option value="${quarterUnitArea.value}">${quarterUnitArea.label}</form:option>
                                            </c:forEach>
                                        </form:select>                                                                                                             
                                    </td>
                                    <td>&nbsp;</td>
                                    <td align="center">
                                        <label>Quarter Type: </label>
                                    </td>
                                    <td>
                                        <form:select path="qrtrtype" cssClass="form-control" onchange="showbuildingDetails()">
                                            <form:option value="">All</form:option>
                                            <c:forEach items="${unitAreaList}" var="unitArea">
                                                <form:option value="${unitArea.value}">${unitArea.label}</form:option>
                                            </c:forEach>                                            
                                        </form:select>
                                    </td>
                                    <td>&nbsp;</td>
                                    <td align="center">
                                        <label>Building No: </label>
                                    </td>
                                    <td>
                                        <form:select path="quarterNo" cssClass="form-control">
                                            <form:option value="">All</form:option>
                                            <c:forEach items="${qnoList}" var="qlist">
                                                <form:option value="${qlist.value}">${qlist.label}</form:option>
                                            </c:forEach>                                            
                                        </form:select>
                                    </td>
                                    <td>
                                        <div class="btn-group" style="padding-left: 50px;">
                                            <button type="submit" value="Search" name="action" class="btn btn-primary" onclick="return validate()"><span class="glyphicon glyphicon-search"></span> Search</button>                                            

                                        </div>
                                    </td> 


                                </tr>
                            </table>
                        </form:form>
                        <c:if test="${not empty status && status eq 'Show'}">

                            <div class="table-responsive">
                                <div class="table-responsive">
                                    <table class="table table-bordered table-hover table-striped">
                                        <thead>
                                            <tr>
                                                <th>#</th>
                                                <th>ID</th>
                                                <th>Name</th>
                                                <th>Date of Ocupation</th>                                            
                                                <th>Date of Vacation</th>
                                                <th>Retirement Date</th>
                                                <th>Allotment Date</th>
                                                <th>Order No</th>                                                                                        

                                            </tr>
                                        </thead>
                                        <tbody id="wrrgrid">
                                            <c:forEach items="${quarterList}" var="quarter" varStatus="cnt">

                                                <tr >
                                                    <td>${cnt.index+1}</td> 
                                                    <td>${quarter.occuId}</td> 
                                                    <td><a href="LedgerMonthWiseReport.htm?occuId=${quarter.occuId}"><strong style="color:red">${quarter.empName}</strong></a></td>
                                                    <td>${quarter.orderDate}</td>                                                
                                                    <td>${quarter.relieveDate}</td>
                                                    <td>${quarter.dos}</td>                                                                                                
                                                    <td>${quarter.allotmentDate}</td>
                                                    <td>${quarter.orderNumber}</td>                                                                                                                                              

                                                </tr>
                                            </c:forEach>                                        
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                            
                            <i><strong>Click on the name to view your ledger</strong></i>
                        </c:if>

                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
