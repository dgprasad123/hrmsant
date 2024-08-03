<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript">
            $(document).ready(function() {
                var groupAdata = 0;
                var groupBdata = 0;
                var groupCdata = 0;
                var groupDdata = 0;
                var groupTotal = 0;
                $("#groupTotal").val(groupTotal);
                $("input").blur(function() {
                    groupAdata = $("#groupA").val();
                    groupBdata = $("#groupB").val();
                    groupCdata = $("#groupC").val();
                    groupDdata = $("#groupD").val();
                    if(groupAdata != ""){
                        groupAdata = parseInt(groupAdata);
                    }else{
                        groupAdata = 0;
                    }
                    if(groupBdata != ""){
                        groupBdata = parseInt(groupBdata);
                    }else{
                        groupBdata = 0;
                    }
                    if(groupCdata != ""){
                        groupCdata = parseInt(groupCdata);
                    }else{
                        groupCdata = 0;
                    }
                    if(groupDdata != ""){
                        groupDdata = parseInt(groupDdata);
                    }else{
                        groupDdata = 0;
                    }
                    groupTotal = parseInt(groupAdata) + parseInt(groupBdata) + parseInt(groupCdata) + parseInt(groupDdata);
                    if (isNaN(groupTotal)) {
                        groupTotal = 0;
                    }
                    $("#groupTotal").val(groupTotal);
                });
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

            function validateSave() {
                if ($("#noDPCtobeConducted").val() == "") {
                    alert("Please enter No. of DPCs due to be conducted in Dec 2020");
                    $("#noDPCtobeConducted").focus();
                    return false;
                }
                if ($("#noDPCConducted").val() == "") {
                    alert("Please enter No. of DPCs actually conducted");
                    $("#noDPCConducted").focus();
                    return false;
                }
                if ($("#groupA").val() == "") {
                    alert("Please enter Group A data");
                    $("#groupA").focus();
                    return false;
                }
                if ($("#groupB").val() == "") {
                    alert("Please enter Group B data");
                    $("#groupB").focus();
                    return false;
                }
                if ($("#groupC").val() == "") {
                    alert("Please enter Group C data");
                    $("#groupC").focus();
                    return false;
                }
                if ($("#groupD").val() == "") {
                    alert("Please enter Group D data");
                    $("#groupD").focus();
                    return false;
                }
                if ($("#noDPCNotConducted").val() == "") {
                    alert("Please enter No of DPCs not conducted");
                    $("#noDPCNotConducted").focus();
                    return false;
                }
                if ($("#reason").val() == "") {
                    alert("Please enter Reason(s) thereof");
                    $("#reason").focus();
                    return false;
                }
            }
        </script>
    </head>
    <body>
        <form:form action="saveDPCConductedData.htm" method="POST" commandName="dpcform">
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        DPCs Conducted in DEC-2020 
                    </div>
                    <div class="panel-body">
                        <div class="row">
                            <div class="col-lg-3">
                                Name of the Department/HOD/District:
                            </div>
                            <div class="col-lg-9">
                                <strong><c:out value="${officename}"/></strong>
                            </div>
                        </div>
                        <table class="table table-bordered" style="margin-top:40px;">
                            <thead>
                                <tr>
                                    <th rowspan="2">Sl No</th>
                                    <th rowspan="2">No. of DPCs due to be conducted in Dec-2020</th>
                                    <th rowspan="2">No. of DPCs actually conducted</th>
                                    <th colspan="5" style="text-align: center">No of employees considered for promotion in 2021</th>
                                    <th rowspan="2">No of DPCs not conducted</th>
                                    <th rowspan="2">Reason(s) thereof</th>
                                </tr>
                                <tr>
                                    <th>Group-A</th>
                                    <th>Group-B</th>
                                    <th>Group-C</th>
                                    <th>Group-D</th>
                                    <th>Total</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${dpcconductedList}" var="dlist" varStatus="cnt">
                                    <tr>
                                        <td width="8%">${cnt.index+1}</td>
                                        <td width="8%">
                                            <c:out value="${dlist.noDPCtobeConducted}"/>
                                        </td>
                                        <td width="8%">
                                            <c:out value="${dlist.noDPCConducted}"/>
                                        </td>
                                        <td width="8%">
                                            <c:out value="${dlist.groupAemp}"/>
                                        </td>
                                        <td width="8%">
                                            <c:out value="${dlist.groupBemp}"/>
                                        </td>
                                        <td width="8%">
                                            <c:out value="${dlist.groupCemp}"/>
                                        </td>
                                        <td width="8%">
                                            <c:out value="${dlist.groupDemp}"/>
                                        </td>
                                        <td width="8%">
                                            <c:out value="${dlist.groupTotal}"/>
                                        </td>
                                        <td width="8%">
                                            <c:out value="${dlist.noDPCNotConducted}"/>
                                        </td>
                                        <td width="36%">
                                            <c:out value="${dlist.reason}"/>
                                        </td>
                                    </tr>
                                </c:forEach>
                                <tr>
                                    <td></td>
                                    <td>
                                        <input type="text" name="noDPCtobeConducted" id="noDPCtobeConducted" maxlength="10" class="form-control" onkeypress="return onlyIntegerRange(event)"/>
                                    </td>
                                    <td>
                                        <input type="text" name="noDPCConducted" id="noDPCConducted" maxlength="10" class="form-control" onkeypress="return onlyIntegerRange(event)"/>
                                    </td>
                                    <td>
                                        <input type="text" name="groupA" id="groupA" maxlength="10" class="form-control" onkeypress="return onlyIntegerRange(event)"/>
                                    </td>
                                    <td>
                                        <input type="text" name="groupB" id="groupB" maxlength="10" class="form-control" onkeypress="return onlyIntegerRange(event)"/>
                                    </td>
                                    <td>
                                        <input type="text" name="groupC" id="groupC" maxlength="10" class="form-control" onkeypress="return onlyIntegerRange(event)"/>
                                    </td>
                                    <td>
                                        <input type="text" name="groupD" id="groupD" maxlength="10" class="form-control" onkeypress="return onlyIntegerRange(event)"/>
                                    </td>
                                    <td>
                                        <input type="text" name="groupTotal" id="groupTotal" class="form-control" readonly="true"/>
                                    </td>
                                    <td>
                                        <input type="text" name="noDPCNotConducted" id="noDPCNotConducted" maxlength="10" class="form-control" onkeypress="return onlyIntegerRange(event)"/>
                                    </td>
                                    <td>
                                        <textarea name="reason" rows="3" cols="50" id="reason" class="form-control"></textarea>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                    <div class="panel-footer">
                        <input type="submit" value="Save "class="btn btn-success" onclick="return validateSave();"/>
                        <a href="DPCConductedList.htm">
                            <button type="button" class="btn btn-default">Back</button>
                        </a>
                    </div>
                </div>
            </div>
        </form:form>
    </body>
</html>
