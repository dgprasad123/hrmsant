<%-- 
    Document   : SectionDefination
    Created on : Nov 21, 2016, 3:12:08 PM
    Author     : Manas Jena
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>      
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">
        <!-- LAYOUT v 1.3.0 -->
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>        

        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>



    </head>
    <body>
        <div class="container-fluid">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <div class="row">
                        <div class="col-lg-12">

                        </div>
                    </div>
                </div>
                <div class="panel-body">
                    <form:form class="form-inline" action="updateEmployeeDataAdmin.htm" method="POST" commandName="EmpoyeeData" onsubmit="return validate();">
                        <form:hidden path="empid"/>
                        <c:if test = "${not empty isduplicate && isduplicate=='Y'}"> 
                            <div  class="alert alert-danger"><strong>This Employee is already exits</strong></div>
                        </c:if>


                        <table class="table table-bordered">
                            <tr style="font-weight:bold;background:#EAEAEA">
                                <td colspan="4">Manage Employee Details
                            </tr>
                            <tr>
                                <td align="right">Title :<strong style='color:red'>*</strong>  </td>
                                <td>
                                    <form:select path="intitals" id="title"  size="1" class="form-control"     >
                                        <form:option value="">-Select-</form:option>
                                        <form:options items="${empTitleList}" itemLabel="label" itemValue="value"/>                                                                                
                                    </form:select> 

                                </td>
                                <td align="right">First Name :<strong style='color:red'>*</strong></td>
                                <td><form:input path="fname" id='firstnmae' class="form-control"/></td>
                            </tr>
                            <tr>
                                <td align="right">Middle Name :</td>
                                <td><form:input path="mname" class="form-control"/></td>
                                <td align="right">Last Name :<strong style='color:red'>*</strong></td>
                                <td><form:input path="lname" id='lname' class="form-control"/></td>
                            </tr>
                            <tr>
                                <td align="right">Gender :<strong style='color:red'>*</strong></td>
                                <td>
                                    <form:select path="gender" id='gender'   class="form-control">
                                        <form:option value="">-Select-</form:option>
                                        <form:option value="M">Male</form:option>
                                        <form:option value="F">Female</form:option>
                                        <form:option value="T">Transgender</form:option>
                                    </form:select>
                                </td> 
                                <td align="right">Mobile :<strong style='color:red'>*</strong></td>
                                <td><form:input path="mobile" id='mobile' class="form-control" maxlength="10"/></td>



                                <!-- <td align="right" ><input type='radio' name='relation' value='Father' required>Father Name/<input type='radio' name='relation' value='Husband' required>Husband Name :<strong style='color:red'>*</strong></td>
                                  <td colspan='4'>
                                      <input name="faname" id="faname" class="form-control" required="true"  type='text'>
                                  </td>-->

                            <tr>
                                <td align="right">Account Type :<strong style='color:red'>*</strong></td>
                                <td>
                                    <form:select path="accttype" id='accountType'  class="form-control"  onchange="display_type(this.value)">
                                        <form:option value="">-Select-</form:option>
                                        <form:option value="GPF">GPF</form:option>
                                        <form:option value="PRAN">PRAN</form:option>
                                        <form:option value="TPF">TPF</form:option>
                                        <form:option value="EPF">EPF</form:option>
                                        <form:option value="GIA">GIA</form:option>
                                    </form:select>
                                </td>  
                                <td align="right">GPF/PRAN/TPF Number :<strong style='color:red'>*</strong></td>
                                <td><form:input path="gpfno" id='gpfno' class="form-control"/></td>

                            </tr>

                            <tr>
                                <td align="right">DOB :</td>
                                <td><form:input path="dob" id='dob' readonly="false" class="form-control" /></td>
                                
                                  <td align="right">Post Group :</td>
                                <td>
                                    <form:select path="postGrpType"   size="1" class="form-control"     >
                                        <form:option value="">-Select-</form:option>
                                        <form:options items="${empPostgrptype}" itemLabel="label" itemValue="value"/>                                                                                
                                    </form:select> 

                                </td>

                            </tr>
                            <tr>
                                <td align="right">Basic Salary :</td>
                                <td><form:input path="basic" class="form-control"/></td>
                                <td align="right">Pay scale :</td>
                                <td>
                                    <form:select path="payScale"   size="1" class="form-control"     >
                                        <form:option value="">-Select-</form:option>
                                        <form:options items="${empPayscaleList}" itemLabel="label" itemValue="value"/>                                                                                
                                    </form:select> 

                                </td>
                                <td align="right">Grade Pay :</td>
                                <td><form:input path="gp"  class="form-control"/></td>
                            </tr>
                            <tr>
                                <td align="right">Current Office Code :(Use 13 digit office code)</td>
                                <td>
                                    <c:if test="${EmpoyeeData.isRegular eq 'N'}">
                                        <form:input path="officecode" class="form-control" maxlength="13"/>
                                    </c:if>
                                    <c:if test="${EmpoyeeData.isRegular ne 'N'}">
                                        <form:hidden path="officecode"/>
                                        <c:out value="${EmpoyeeData.officecode}"/>
                                    </c:if>
                                </td>
                            </tr>



                        </table>
                        <div class="panel-footer">                    
                            <input type="submit" value="Save Employee Details" class="btn btn-success" />
                        </div>
                    </form:form>
                </div>

            </div>
        </div>
        <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
        <link rel="stylesheet" href="/resources/demos/style.css">
        <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
        <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
        <script>
            $(function () {
                //$('#dob').datepicker({dateFormat: 'yy-mm-dd'});
                //$('#doj').datepicker({dateFormat: 'yy-mm-dd'});
                ;
                $("#dob").datepicker({
                    changeMonth: true,
                    changeYear: true,
                    dateFormat: 'yy-mm-dd',
                    yearRange: '1955:2025',
                });
            });
            function validate() {


                var etitle = $("#title").val();


                if (etitle == "") {
                    alert("Please select your title");
                    return false;
                }
                var firstnmae = $("#firstnmae").val();
                if (firstnmae == "") {
                    alert("Please Enter First Name");
                    return false;
                }
                var lname = $("#lname").val();
                if (lname == "") {
                    alert("Please Enter your Last Name");
                    return false;
                }

                var gender = $("#gender").val();
                if (gender == "") {
                    alert("Please select your Gender");
                    return false;
                }
                var mobile = $("#mobile").val();
                if (mobile == "") {
                    alert("Please Enter Your Mobile No");
                    return false;
                }
                if (isNaN(mobile)) {
                    alert("Invalid Mobile No");
                    return false;
                }
                var accountType = $("#accountType").val();
                if (accountType == "") {
                    alert("Please Select your Account Type");
                    return false;
                }
                var gpfno = $("#gpfno").val();
                if (gpfno == "") {
                    alert("Please Enter GPF/PRAN/TPF Number");
                    return false;
                }

                var dob = $("#dob").val();
                ;
                if (dob == "") {
                    alert("Please Select DOB");
                    return false;
                }

                return true;

            }
        </script>
    </body>
</html>
