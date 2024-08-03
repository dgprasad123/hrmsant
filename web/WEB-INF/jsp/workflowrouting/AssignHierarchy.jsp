<%-- 
    Document   : AssignHierarchy
    Created on : 10 Jan, 2018, 2:56:10 PM
    Author     : Surendra
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <link href="resources/css/bootstrap.min.css" rel="stylesheet">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link rel="stylesheet" href="css/bootstrap.min.css">
        <script src="js/jquery.min.js"></script>
        <script src="js/bootstrap.min.js"></script>
        <style type="text/css">
            ul.source, ul.target {
                min-height: 50px;
                margin: 0px 25px 10px 0px;
                padding: 2px;
                border-width: 1px;
                border-style: solid;
                -webkit-border-radius: 3px;
                -moz-border-radius: 3px;
                border-radius: 3px;
                list-style-type: none;
                list-style-position: inside;
            }
            ul.source {
                border-color: #f8e0b1;
            }
            ul.target {
                border-color: #add38d;
            }
            .source li, .target li {
                margin: 5px;
                padding: 5px;
                -webkit-border-radius: 4px;
                -moz-border-radius: 4px;
                border-radius: 4px;
                text-shadow: 0 1px 0 rgba(255, 255, 255, 0.5);
            }
            .source li {
                background-color: #fcf8e3;
                border: 1px solid #fbeed5;
                color: #c09853;
            }
            .target li {
                background-color: #ebf5e6;
                border: 1px solid #d6e9c6;
                color: #468847;
            }
            .sortable-dragging {
                border-color: #ccc !important;
                background-color: #fafafa !important;
                color: #bbb !important;
            }
            .sortable-placeholder {
                height: 40px;
            }
            .source .sortable-placeholder {
                border: 2px dashed #f8e0b1 !important;
                background-color: #fefcf5 !important;
            }
            .target .sortable-placeholder {
                border: 2px dashed #add38d !important;
                background-color: #f6fbf4 !important;
            }
            .tblprocess td{padding:3px;}
        </style>

        <script type="text/javascript">

            $(document).ready(function() {

                $('#sltDept').change(function() {
                    if ($('#sltDept').val() != '') {
                        $.ajax({
                            type: "GET",
                            url: "getOfficeForDepartment.htm",
                            data: {deptCode: $('#sltDept').val()},
                            success: function(data) {
                                var keys = Object.keys(data);
                                $('#sltOffcode').empty();
                                for (var i = 0; i < keys.length; i++) {
                                    var key = keys[i];
                                    console.log(key, data[key]);
                                    var newOption = $('<option value="' + key + '">' + data[key] + '</option>');
                                    $('#sltOffcode').append(newOption);
                                    $("#processId").val('');
                                }
                                $("#sltOffcode").html($('#sltOffcode option').sort(function(x, y) {
                                    return $(x).text() < $(y).text() ? -1 : 1;
                                }))

                                $("#sltOffcode").get(0).selectedIndex = 0;
                                e.preventDefault();
                            },
                            error: function() {
                                alert('Error occured');
                            }
                        });

                    }
                });
            });

            function assignData(postcode) {
                location.href = "addWorkflowrouting.htm?assignedPostcode=" + postcode + "&postcode=" + $("#gpc").val() + "&processId=" + $("#processId").val();
            }

            function removeData(postcode) {
                location.href = "removeWorkflowrouting.htm?workflowRoutingId=" + postcode + "&postcode=" + $("#gpc").val() + "&processId=" + $("#processId").val();
            }

            function validate() {
                if ($("#sltOffcode").val() == '' || $("#sltOffcode").val() == null) {
                    alert('Please select Office.');
                    return false;
                }

                if ($("#processId").val() == '' || $("#sltOffcode").val() == null) {
                    alert('Please select Process Type.');
                    return false;
                }

            }

        </script>
    </head>
    <body>
        <form:form action="assignworkflowrouting.htm">
            <form:hidden path="gpc" id="gpc"/>
            <h1 style="text-align:center;color:#008900;font-weight:bold;"> ${postname} </h1>
            <div style="margin-top:20px;">
                <div style="margin:10px auto;width:95%" >
                    <table width="70%" cellspacing="0" cellpadding="4" class="tblprocess">
                        <tr>
                            <td width="15%">Select Department</td>
                            <td>
                                <form:select path="sltDept" id="sltDept" style="width:80%">
                                    <form:options items="${deptList}" />
                                </form:select></td>
                        </tr>
                        <tr>
                            <td>Select Office</td>
                            <td>   
                                <form:select path="sltOffcode" id="sltOffcode" style="width:80%">
                                    <form:options items="${offList}" />
                                </form:select></td>
                        </tr>
                        <tr>
                            <td>Select Process</td>
                            <td>
                                <form:select path="processId" id="processId" >
                                    <form:options items="${processList}" />
                                </form:select></td>
                        </tr>
                        <tr>
                            <td></td>
                            <td>
                                <input type="submit" name="btnval" value="GetList" onclick="return validate()" class="btn btn-primary btn-sm"/>
                                <input type="submit" name="btnval" value="Return" class="btn btn-primary btn-sm"/></td>
                        </tr>                        
                    </table>


                </div>


                <div class="sideBySide" style="margin:10px auto;width:95%">
                    <div class="left" style="float:left;width:49%;">

                        <ul class="source connected">
                            <li style="background:#890000;color:#FFFFFF;font-weight:bold;font-size:12pt;">Proposed List</li>
                                <c:forEach var="proposeData" items="${proposeList}">
                                <li><c:out value="${proposeData.postName}"/>

                                    <a href="addWorkflowrouting.htm?assignedPostcode=${proposeData.reportingGpc}&postcode=${command.gpc}&processId=${command.processId}&departmentcode=${command.sltDept}&offCode=${command.sltOffcode}"> 
                                        <img src="images/arrow_r.png"  align="right"/> 
                                    </a>
                                </li> 

                            </c:forEach>

                        </ul>
                        <div style="clear:both;"></div>
                    </div>




                    <div class="right" style="float:right;width:49%;">

                        <ul class="target connected">
                            <li style="background:#008900;color:#FFFFFF;font-weight:bold;font-size:12pt;">Assigned List</li>
                                <c:forEach var="assignedData" items="${assignedList}">
                                <li>
                                    <a href="removeWorkflowrouting.htm?workflowRoutingId=${assignedData.workflowRoutingId}&postcode=${command.gpc}&processId=${command.processId}&departmentcode=${command.sltDept}&offCode=${command.sltOffcode}"> <img src="images/arrow_l.png" style="margin-right:10px;" /></a><c:out value="${assignedData.postName}"/></li> 
                                    </c:forEach>
                        </ul>
                    </div>
                    <div style="clear:both;"></div>
                </div>
            </div>
        </form:form>
    </body>
</html>
