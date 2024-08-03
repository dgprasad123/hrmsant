<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="css/sb-admin.css" rel="stylesheet">
        
        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript">
            function toggleNewGPHeadBox() {
                $('#newGPHead').val($('#existingGPHead').val());
                $('#newGPHead').val();
            }
            function validateOk() {
                if ($('#txtBillNo').val() == '') {
                    alert("Please enter Bill No");
                    return false;
                }
                return true;
            }
            function validateForm() {
                var flag = true;
                if ($('#txtBillNo').val() == '') {
                    alert("Please enter Bill No");
                    flag = false;
                }
                if ($('#existingGPHead').val() == '') {
                    alert("Please select existing GP Head");
                    flag = false;
                }
                if ($('#newGPHead').val() == '') {
                    alert("Please select New GP Head");
                    flag = false;
                }
                if (flag == true) {
                    if (confirm("Are you sure to Update the GP Head?")) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
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

        </script>
    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/hrmsadminmenu.jsp"/>        
            <div id="page-wrapper">
                <div class="container-fluid">
                    <form:form action="updateGPHead.htm" method="POST" commandName="gpheadform">
                        <form:hidden path="aqyear"/>
                        <form:hidden path="aqmonth"/>
                        <div class="panel panel-default" style="margin-top:20px;">
                            <div class="panel-heading">
                                <div class="row" style="margin-bottom: 7px;">
                                    <div class="col-lg-4" align="right">
                                        <span>Enter Bill No</span>
                                    </div>
                                    <div class="col-lg-3">
                                        <form:input class="form-control" path="txtBillNo" id="txtBillNo" onkeypress="return onlyIntegerRange(event)"/>
                                    </div>
                                    <div class="col-lg-1">
                                        <button type="submit" name="get" class="btn btn-default" onclick="return validateOk();">Ok</button>
                                    </div>
                                </div>
                            </div>
                            <div class="panel-body">
                                <div class="row" style="margin-bottom: 7px;">
                                    <div class="col-lg-3" align="center"></div>
                                    <div class="col-lg-6" align="center">
                                        <c:if test="${gpheadform.billExists == 'Y'}">
                                            <label>Current GP Head</label>
                                            <form:select path="existingGPHead" id="existingGPHead" style="width:100px;">
                                                <option value="">--Select--</option>
                                                <option value="000">000</option>
                                                <option value="136">136</option>
                                            </form:select>&nbsp;&nbsp;&nbsp;&nbsp;
                                            <label>GP Head to be Updated</label>
                                            <form:select path="newGPHead" id="newGPHead" style="width:100px;">
                                                <option value="">--Select--</option>
                                                <option value="000">000</option>
                                                <option value="136">136</option>
                                                <option value="921">921</option>
                                            </form:select>
                                        </c:if>
                                        <c:if test="${gpheadform.billExists == 'N'}">
                                            <span style="color:red;">
                                                Data Not Available
                                            </span>
                                        </c:if>
                                        <c:if test="${not empty status && status > 0}">
                                            <span style="color:red;">
                                                GP Head Updated Successfully.
                                            </span>
                                        </c:if>
                                    </div>
                                    <div class="col-lg-3" align="center"></div>
                                </div>
                            </div>
                            <div class="panel-footer">
                                <button type="submit" name="update" class="btn btn-primary" onclick="return validateForm();">Update</button>
                            </div>
                        </div>
                    </form:form>
                </div>
            </div>
        </div>
    </body>
</html>
