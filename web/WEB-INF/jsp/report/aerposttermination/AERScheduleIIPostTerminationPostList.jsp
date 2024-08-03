<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@page contentType="text/html" pageEncoding="UTF-8" buffer="1024kb"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <title>HRMS</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>        
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript">
            function validate() {
                var checkgpcvalue = $('.chkTerminatedPost:checkbox:checked').length;
                if (checkgpcvalue == 0) {
                    alert("Please select at least one checkbox for Post Termination");
                    return false;
                }
                var checkedValue = $('.chkTerminatedPost:checkbox:checked').val();
                if (checkedValue == "NA") {
                    var con = confirm("Do you want to submit without Post Termination Proposal");
                    if (con) {
                        return true;
                    } else {
                        return false;
                    }
                }

            }
            function back_page() {

                window.location = "PostTerminationNOCOScheduleII.htm?financialYear=${financialYear}";
            }
        </script>
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
                        <button type="button" name="btnPTAer" value="Back" class="btn btn-primary" onclick="back_page()">Back</button>
                    </div>
                    <div class="panel-body">
                        <table class="table table-bordered" width="100%">
                            <thead>
                                <tr>
                                    <th width="10%" rowspan="2">Category of Employee</th>
                                    <th width="25%" rowspan="2">Description of the Posts</th>
                                    <th width="20%" colspan="2">Pay Scale</th>
                                    <th  width="5%" rowspan="2">As per 7th Pay Commission</th>
                                    <th width="8%" rowspan="2">Sanctioned Strength</th>
                                    <th width="8%" rowspan="2">Persons-in-Position</th>
                                    <th width="9%" rowspan="2">Vacancy Position </th>
                                    <th width="5%" rowspan="2"> </th>
                                </tr>
                                <tr>
                                    <th>As per 6th Pay Commission</th>
                                    <th>GP</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="list" items="${scheduleIIPostTerminationList}">
                                    <tr>
                                        <td> Group ${list.group}  </td>
                                        <td> ${list.postname} </td>
                                        <td> 
                                            ${list.payscale}
                                            <form:hidden path="hidPayScale" value="${list.payscale}"/>
                                        </td>
                                        <td> ${list.gp} </td>
                                        <td> ${list.level_7thpay} </td>
                                        <td> ${list.sancStrength} </td>
                                        <td> ${list.menInPosition} </td>
                                        <td> ${list.vacancy} </td>
                                        <td>
                                            <form:checkbox path="chkTerminatedPost" value="${list.reportid}" class="form-control chkTerminatedPost"/>
                                        </td>
                                    </tr>
                                </c:forEach>
                                <tr>
                                    <td>&nbsp;</td>
                                </tr>     
                                <tr>
                                    <td style='color:white;background-color:red;height:50px;padding:5px'> <form:checkbox path="chkTerminatedPost" value="NA" class="form-control chkTerminatedPost"/>   </td>
                                    <td colspan="8" style='color:white;background-color:red;height:50px;padding:5px'> <strong>Click here if there is No POST Termination Proposal</td>
                                </tr>       
                            </tbody>
                        </table>
                    </div>

                    <c:if test="${cntRecord eq '0' || not empty editPropsalId }">   
                        <div class="panel-footer">
                            <h3 style="color:red">Please click Next button for  termination of post proposal </h3>
                            <button type="submit" name="btnPTAer" value="Next" class="btn btn-primary" onclick="return validate()">Next</button>
                        </div>
                    </c:if> 
                    
                </div>
            </div>
        </form:form>
    </body>
</html>
