<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>        
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript">
            function validateForm(){
                var checkboxlength = $("input[name=chkEmployee]:checked").length;
                //alert("checkboxlength is: "+checkboxlength);
                if(checkboxlength == 0){
                    alert("Please Select any one of the Employees");
                    return false;
                }
            }
        </script>
    </head>
    <body>
        <form:form action="getOfficeAllowanceAndDeductionList.htm" method="POST" commandName="allowanceDeductionForm">
            <div class="container-fluid">
                <div class="panel panel-default" style="margin-top:20px;">
                    <div class="panel-heading">
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                
                            </div>
                            <div class="col-lg-3">
                                Bill Group&nbsp;
                                <form:select path="sltBillGroup" id="sltBillGroup" class="form-control">
                                    <form:option value="">--Select--</form:option>
                                    <form:options items="${billGrpList}" itemValue="billgroupid" itemLabel="billgroupdesc"/>
                                </form:select>
                            </div>
                            <div class="col-lg-4">
                                Deduction&nbsp;
                                <form:select path="sltDeductionName" id="sltDeductionName" class="form-control">
                                    <form:option value="">--Select--</form:option>
                                    <form:options items="${deductionList}" itemValue="value" itemLabel="label"/>
                                </form:select><br />
                                <input type="submit" name="btnSubmit" value="Get List" class="btn btn-default"/>
                            </div>
                            <div class="col-lg-3">
                                
                            </div>
                        </div>
                    </div>
                    <div class="panel-body">
                        <table class="table table-bordered">
                            <thead>
                                <tr>
                                    <th width="5%">Sl No</th>
                                    <th width="5%">&nbsp;</th>
                                    <th width="15%">HRMS ID/GPF No</th>
                                    <th width="15%">Name</th>
                                    <th width="35%">Designation</th>
                                    <th width="10%" align="right">Amount</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:if test="${not empty emplist}">
                                    <c:forEach items="${emplist}" var="list" varStatus="cnt">
                                        <tr>
                                            <td>${cnt.index+1}</td>
                                            <td>
                                                <!--<input type="checkbox" name="chkEmployee" value="${list.empid}"/>-->
                                                <form:checkbox path="chkEmployee" value="${list.chkEmployeeId}"/>
                                            </td>
                                            <td>
                                                <c:out value="${list.empid}"/> / <c:out value="${list.gpfNo}"/>
                                            </td>
                                            <td><c:out value="${list.empname}"/></td>
                                            <td><c:out value="${list.desg}"/></td>
                                            <td align="right"><input type="text" name="adAmt" value="${list.adAmt}" style="text-align: right;" size="7"/></td>
                                            <!--<td align="right"><form:input path="adAmt" style="text-align: right;" size="7"/></td>-->
                                        </tr>
                                    </c:forEach>
                                </c:if>
                            </tbody>
                        </table>
                    </div>
                    <div class="panel-footer">
                        <input type="submit" name="btnSubmit" value="Update" class="btn btn-default" onclick="return validateForm();"/>
                        <input type="submit" name="btnSubmit" value="Delete" class="btn btn-default" onclick="return validateForm();"/>
                    </div>
                </div>
            </div>
        </form:form>
    </body>
</html>
