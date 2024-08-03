<%-- 
    Document   : MultipleCOAERCreate
    Created on : 5 Jun, 2018, 12:13:31 PM
    Author     : Surendra
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>HRMS</title>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>        
        <script type="text/javascript" src="js/bootstrap.min.js"></script>

        <script type="text/javascript">
            $(document).ready(function () {
                if($("#singleCOId").val() == 'Y'){
                    $("form input:checkbox").attr( "checked" , true );
                }

            });

            function onlyIntegerRange(e) {
                var browser = navigator.appName;
                if (browser == "Netscape") {
                    var keycode = e.which;
                    if ((keycode >= 48 && keycode <= 57) || keycode == 8 || keycode == 0)
                        return true;
                    else
                        return false;
                } else {
                    if ((e.keyCode >= 48 && e.keyCode <= 57) || e.keycode == 8 || e.keycode == 0)
                        e.returnValue = true;
                    else
                        e.returnValue = false;
                }
            }

            function validateForm() {
                if ($('#financialYear').val() == '') {
                    alert("Please select Financial Year");
                    return false;
                }
                
                var numberOfChecked = $('input:checkbox:checked').length;
                if (numberOfChecked < 1 && $("#hidAdditionalChargeDDOCode").val() == "") {
                    alert('Please select Bill.');
                    return false;
                }

                return true;
            }

        </script>
    </head>
    <body>
        <form:form action="finishAER.htm" commandName="command">
            <div class="container-fluid">
                <form:hidden path="singleCO" id="singleCOId"/>
                <form:hidden path="aerId"/>
                <input type="hidden" id="hidAdditionalChargeDDOCode" value="${AdditionalChargeDDOCode}"/>

                <div class="panel panel-default">
                    <div class="panel-heading">
                        <div class="row">
                            <div class="col-lg-12">
                                <h1 align="center"> ${OffName} </h1>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-lg-12">
                                <h3 align="center"> Financial Year ${command.financialYear}
                                    <form:hidden path="financialYear"/>
                                </h3> 
                            </div>
                        </div>    
                                
                        <div class="row">
                            <div class="col-xs-6"><input type="submit" name="action" id="btnSave" value="Previous" class="btn btn-primary"/></div>
                            <div class="col-xs-6">
                                <div class="pull-right">
                                    <input type="submit" name="action" id="btnSave" value="Finish" class="btn btn-primary" onclick="return validateForm();"/>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="panel-body">
                        <table class="table table-striped table-bordered" width="90%">
                            <thead>
                                <tr>
                                    <th width="15%">Bill Group Id</th>
                                    <th width="25%">Description</th>
                                    <th width="15%">Demand Number</th>
                                    <th width="15%">Major Head</th>
                                    <th width="15%">Sub Major Head</th>
                                </tr>
                            </thead>
                            <hr />
                            <tbody>
                                <c:if test="${not empty BillGroupList}">
                                    <c:forEach var="grp" items="${BillGroupList}">
                                        <tr>
                                            <td>
                                                <%--<form:checkbox path="billGrpId" value="${grp.billgroupid}"/>--%>
                                                <input type="checkbox" name="billGrpId" id="billGrpId" value="${grp.billgroupid}"/>
                                            </td>
                                            <td>
                                                &nbsp;${grp.billgroupdesc}
                                            </td>
                                            <td>
                                                &nbsp;${grp.demandNo}
                                            </td>
                                            <td>
                                                &nbsp;${grp.majorHead}
                                            </td>
                                            <td>
                                                &nbsp;${grp.subMajorHead}
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:if>
                            </tbody>
                        </table>
                    </div>                

                    <div class="panel-footer">
                        <div class="row">
                            <div class="col-xs-6"><input type="submit" name="action" id="btnSave" value="Previous" class="btn btn-primary"/></div>
                            <div class="col-xs-6">
                                <div class="pull-right">
                                    <input type="submit" name="action" id="btnSave" value="Finish" class="btn btn-primary" onclick="return validateForm();"/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </form:form>
    </body>
</html>
