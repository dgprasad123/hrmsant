<%-- 
    Document   : GranteeOfficeList
    Created on : 7 Jan, 2019, 2:12:01 PM
    Author     : Surendra
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
            function delete_giaEst(aerOtherId, aerId, partType) {
                var conf = confirm("Do you want to confirm to delete this Information");
                if (conf) {

                    window.location = "deleteGiaEst.htm?aerOtherId=" + aerOtherId + "&aerId=" + aerId + "&partType=" + partType;
                }
            }
            function saveGiaData() {
                var postname = $('#postname').val();
                if (postname == '') {
                    alert("Please enter post name");
                    return false;
                }
            }

        </script>   
    </head>
    <body>
        <jsp:include page="../report/aerTab.jsp">
            <jsp:param name="menuHighlight" value="PART_B" />
        </jsp:include>

        <form:form action="saveGIAData.htm" method="POST" commandName="command">
            <form:hidden path="aerId" id="aerId"/>
            <form:hidden path="aerOtherId" id="aerOtherId"/>
            <form:hidden path="offCode" id="offCode"/>
            <form:hidden path="partType" id="partType"/>
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <div class="row">
                            <div class="col-lg-12">
                                <h1 align="center"> ${OffName} </h1>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-lg-12">
                                <input type="submit" name="action" id="btnSave" value="Insert Data from Previous Year" class="btn btn-success" onclick="return confirm('Are you sure?')"/>
                            </div>
                        </div>
                    </div>
                    <div class="panel-body">

                        <table class="table table-striped table-bordered" width="100%">
                            <thead>
                                <tr>
                                    <th width="10%" rowspan="2" >Group</th>
                                    <th width="25%" rowspan="2">Description of the Posts</th>
                                    <th width="40%" align="center" colspan="3">Pay Scale</th>
                                    <th width="8%" rowspan="2">Sanctioned Strength</th>
                                    <th width="8%" rowspan="2">Persons-in-Position</th>
                                    <th width="9%" rowspan="2">Vacancy Position </th>
                                    <th width="5%" rowspan="2">Remarks if any</th>
                                </tr>
                                <tr>
                                    <th>&nbsp;</th>
                                    <th>As per 6th Pay Commission</th>
                                    <th>As per 7th Pay Commission</th>
                                </tr>
                            </thead>
                            <tr>
                                <td>
                                    <form:select path="group" class="form-control" id="group" >
                                        <form:option value="">Select  Group </form:option>
                                        <form:option value="A">A</form:option>
                                        <form:option value="B">B</form:option>
                                        <form:option value="C">C</form:option>
                                        <form:option value="D">D</form:option>
                                        <form:option value="R">Consolidated Renumeration</form:option>
                                    </form:select>
                                </td>
                                <td><form:input path="postname" id="postname" class="form-control" maxlength="100"/></td>
                                <td><form:input path="scaleofPay" id="scaleofPay" class="form-control" maxlength="100"/></td>
                                <td><form:input path="gp" id="gp" class="form-control"/></td>
                                <td><form:input path="scaleofPay7th" id="scaleofPay7th" class="form-control"/> </td>
                                <td><form:input path="sanctionedStrength" id="sanctionedStrength" class="form-control"/></td>
                                <td><form:input path="meninPosition" id="meninPosition" class="form-control"/></td>
                                <td><form:input path="vacancyPosition" id="vacancyPosition" class="form-control"/></td>
                                <td> <form:input path="otherRemarks" id="otherRemarks" class="form-control"/></td>
                            </tr>
                        </table>
                        <table>
                            <tr>
                                <td>
                                    <input type="submit" name="action" id="btnSave" value="Save" class="btn btn-primary"  onclick="return saveGiaData();"/> 
                                    <button type="submit" name="action" value="Back" class="btn btn-primary">Back to List</button>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <table class="table table-bordered">
                        <thead>
                            <tr class="bg-info text-white">
                                <th>#</th>
                                <th>Post Description</th>
                                <th>Scale of Pay</th>
                                <th>As per 6th Pay Commission</th>
                                <th>As per 7th Pay Commission</th>
                                <th>Sanction Strength</th>
                                <th>Persons-in-Position</th>
                                <th>Vacancy</th>
                                <th>Group</th>
                                <th>Remarks</th>                          
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${aeDetails}" var="ae" varStatus="cnt">
                                <tr>
                                    <th scope="row">${cnt.index+1}</th>
                                    <td>${ae.otherPost}</td>  
                                    <td>${ae.scaleofPay}</td>  
                                    <td>${ae.gp}</td>
                                    <td>${ae.other7thPay}</td>
                                    <td>${ae.otherSS}</td>
                                    <td>${ae.meninPosition}</td>
                                    <td>${ae.otherVacancy}</td>
                                    <td>${ae.group}</td>
                                    <td>${ae.otherRemarks}</td>
                                    <td>
                                        <a href="editGiaEst.htm?aerOtherId=${ae.aerOtherId}&aerId=${ae.aerId}&offCode=${ae.offCode}&partType=B"><span class="glyphicon glyphicon-pencil"></span>Edit</a>
                                        &nbsp;
                                        <a href="deleteGiaEst.htm?aerOtherId=${ae.aerOtherId}&aerId=${ae.aerId}&offCode=${ae.offCode}&partType=B" onclick="return confirm('Are you sure to Delete?');"><span class="glyphicon glyphicon-remove"></span>Delete</a>
                                       <!--<a href=#" onclick="delete_giaEst(${ae.aerOtherId},${ae.aerId},${ae.offCode}, 'B')" ><span class="glyphicon glyphicon-remove"></span> Delete</a>-->
                                        &nbsp;
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table> 
                    <div class="panel-footer">
                        <%--<input type="submit" name="action" id="btnSave" value="Save" class="btn btn-primary"  onclick="return saveGiaData();"/> 
                        <button type="submit" name="action" value="Back" class="btn btn-primary">Back to List</button>--%>
                    </div>
                </div>
            </div>
        </form:form>
    </body>
</html>
