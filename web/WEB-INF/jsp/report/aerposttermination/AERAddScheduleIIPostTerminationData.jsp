<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <title>HRMS</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>

        <script src="js/moment.js" type="text/javascript"></script>
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>        
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script type="text/javascript">
            $(document).ready(function () {
                $('.dateOfTermination').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
                $('.dateOfSanction').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
            });

            function validateSend() {
                var noOfPost = $(".noOfPost");
                for (var i = 0; i < noOfPost.length; i++) {
                    if ($(noOfPost[i]).val() == '') {
                        alert("Please Enter No of Post");
                        return false;
                    }
                }

                var dateOfTermination = $(".dateOfTermination");
                for (var i = 0; i < dateOfTermination.length; i++) {
                    if ($(dateOfTermination[i]).val() == '') {
                        alert("Please Enter Date of Termination");
                        return false;
                    }
                }

                var goNo = $(".goNo");
                for (var i = 0; i < goNo.length; i++) {
                    if ($(goNo[i]).val() == '') {
                        alert("Please Enter Govt No");
                        return false;
                    }
                }

                var dateOfSanction = $(".dateOfSanction");
                for (var i = 0; i < dateOfSanction.length; i++) {
                    if ($(dateOfSanction[i]).val() == '') {
                        alert("Please Enter Date of Sanction");
                        return false;
                    }
                }
            }
            function back_page() {
                window.location = "PostTerminationNOCOScheduleII.htm?financialYear=${financialYear}";
            }
            function validate_gono_go_date(ids) {
               // alert(ids);
                var chkVal = "checkbox_" + ids;
                var gonoVal = "gono_" + ids;
                var godateVal = "godate_" + ids;
                if (document.getElementById(chkVal).checked) {
                    $("#" + gonoVal).val("NA");
                    $("#" + godateVal).val("NA");
                    $("#" + gonoVal).attr('readonly', true);
                } else {
                    $("#" + gonoVal).val("");
                    $("#" + godateVal).val("");
                    $("#" + gonoVal).attr('readonly', false);
                }
            }
        </script>
        <style type="text/css">
            body{
                position:relative;
            }
        </style>
    </head>
    <body>
        <form:form action="PostTerminationCOScheduleII.htm" commandName="command">
            <form:hidden path="financialYear" id="financialYear"/>
             <form:hidden path="editPropsalId" id="editPropsalId"/>
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h2 align="center"><c:out value="${officename}"/></h2>
                        <h4 align="center">Financial Year:${financialYear}</h4>
                        <h6 align="center">SCHEDULE II-A</h6>
                        <div align="center">(Relating to Head of the Department)</div>
                        <button type="button" name="btnPTAer" value="Send" class="btn btn-primary" onclick="return back_page()">Back</button>
                    </div>
                    <div class="panel-body">
                        <table class="table table-bordered" width="100%">
                            <thead>
                                <tr>
                                    <th width="5%">SL No</th>
                                    <th width="30%">Description of Posts</th>
                                     <th width="7%" >GO NO & Date <strong style='color:red'>Not Available</strong></th>
                                    <th width="5%">GO No</th>
                                    <th width="10%">GO Date in which sanctioned</th>
                                    <th width="20%">Pay Scale</th>
                                    <th width="5%">No. of Posts to be terminated</th>
                                    <th width="10%">Date from which posts(s) to be terminated</th>
                                    <th width="30%">Remarks</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${checkedPostList}" var="list" varStatus="count">
                                    <tr>
                                        <td>
                                            ${count.index + 1}
                                        </td>
                                        <td>
                                            <c:out value="${list.postname}"/>
                                            <form:hidden path="hidPostName" value="${list.postname}"/>
                                        </td>
                                         <td><input type="checkbox" name="go_ava" class="form-control"  onclick="validate_gono_go_date(${count.index + 1})" id="checkbox_${count.index + 1}"/></td>
                                        <td>
                                            <form:input path="goNo" class="form-control goNo" maxlength="5" size="7"  id="gono_${count.index + 1}"/>
                                        </td>
                                        <td>
                                            <div style="position:relative;"><form:input path="dateOfSanction" class="form-control dateOfSanction" readonly="true"  id="godate_${count.index + 1}"/></div>
                                        </td>
                                        <td> 
                                            <c:out value="${list.payscale}"/>
                                            <form:hidden path="hidPayScale" value="${list.payscale}"/>
                                        </td>
                                        <td>
                                            <form:input path="noOfPost" class="form-control noOfPost" maxlength="3" size="5"/>
                                        </td>
                                        <td>
                                            <div style="position:relative;">
                                                <form:input path="dateOfTermination" class="form-control dateOfTermination" readonly="true"/>
                                            </div>
                                        </td>    
                                        <td>
                                            <form:input path="remarks" class="form-control"/>
                                        </td>    
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                    <div class="panel-footer">
                        <button type="submit" name="btnPTAer" value="Send" class="btn btn-primary" onclick="return validateSend()">Send</button>
                    </div>
                </div>
            </div>
        </form:form>
    </body>
</html>
