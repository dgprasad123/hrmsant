<%-- 
    Document   : EmployeePensionAcknowledgement
    Created on : 29 Jan, 2024, 12:45:42 PM
    Author     : Adarsh
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<% int i = 1;%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Employee Pension Acknowledgement</title>
        <link href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <style>


        </style>
        <script type="text/javascript">
            $(document).ready(function() {
                $('#chkPensionAcknowledge').change(function() {
                    if (this.checked) {
                        $('#btnSubmit').prop("disabled", false);
                    } else {
                        $('#btnSubmit').prop("disabled", true);
                    }
                }).change()

                pensionAcknowledgementValidation();
            });
            var flag = false;
            function pensionAcknowledgementValidation() {

            }
            function confirmSubmit()
            {
                if (flag)
                {
                    alert("Please complete all your profile data before submitting.");
                    window.scrollTo(0, 0);
                    return false;
                }
                if (confirm('Are you sure to Submit Profile Data?'))
                {

                }
            }
        </script>

    </head>
    <body class="bg-light">

        <jsp:include page="ProfileTabs.jsp">
            <jsp:param name="menuHighlight" value="EMPENSIONACKNOWLEDGE" />
        </jsp:include>


        <form:form name="searchForm" action="PensionEmpDetails.htm" method="POST" modelAttribute="searchForm">
            <div class="container mt-5">
                <div class="form-row align-items-center">
                    <div class="col-sm-8">
                        <label for="hrmsEmpId">Search by HRMS ID/GPF NO:</label>
                        <div class="input-group">
                            <input type="text" class="form-control" id="hrmsEmpId" name="empid" autocomplete="off" required value="${loginid}" readonly>

                        </div>

                    </div>
                    <div class="col-sm-4" style="margin-top: 24px;">
                        <button class="btn btn-success" type="submit">Search</button>
                    </div>

                </div>
            </div>
        </form:form>

        <c:if test="${not empty employee}">
            <div class="container-fluid">
                <form:form name="myForm" action="SubmitPensionAcknowledgementByEmployee.htm" method="POST"
                           commandName="employee">

                    <div class="alert alert-danger" style="border: 2px solid #F00; display: none;" id="error_wrapper">
                        <h4 class="alert-heading" style="font-size: 14pt; font-weight: bold;">
                            Check the following details in order to Complete your PensionDetails!
                        </h4>
                        <div class="row" id="error_msg" style="color: #FF0000; font-size: 12pt;"></div>
                    </div>

                    <div class="border mt-4">
                        <table class="table table-striped">
                            <h4>Account No. Details</h4>
                            <tr>
                                <td width="25%" align="left">Provident Fund Series & A/c No.:<strong
                                        style="color: red">*</strong></td>
                                <td width="20%" style="font-weight: bold;">${employee.tpfSeries} ${employee.tpfAcNo}</td>
                                <td width="25%" align="right">HRMS Employee Id :</td>
                                <td><input type="text" readonly="readonly" style="width: 20%; font-weight: bold;"
                                           value="${employee.hrmsEmpId}" /></td>
                            </tr>
                        </table>
                    </div>

                    <div class="border mt-4">
                        <table class="table table-striped">
                            <h4 align="center">Employee Details</h4>
                            <tr>
                                <td width="30%" align="right">Employee Name:<strong style="color: red">*</strong>
                                    <select name="salutationEmp" id="salutationEmp">
                                        <option value="Mr." <c:if
                                                    test="${not empty employee.salutationEmp && employee.salutationEmp eq 'Mr.'}">
                                                    <c:out value='selected="selected"' /></c:if>>Mr.</option>
                                        <option value="Mrs." <c:if
                                                    test="${not empty employee.salutationEmp && employee.salutationEmp eq 'Mrs.'}">
                                                    <c:out value='selected="selected"' /></c:if>>Mrs.</option>
                                        <option value="Other" <c:if
                                                    test="${not empty employee.salutationEmp && employee.salutationEmp eq 'Other'}">
                                                    <c:out value='selected="selected"' /></c:if>>Other</option>
                                        </select>
                                    </td>
                                    <td align="left" style="font-weight: bold;">${employee.employeeFirstName}
                                    ${employee.employeeMiddleName} ${employee.employeeLastName}</td>
                            </tr>

                            <tr>
                                <td width="25%" align="right">Designation:<strong style="color: red">*</strong></td>
                                <td width="20%" style="font-weight:bold;">${employee.designationId}</td>
                            </tr>
                            <tr>
                                <td width="25%" align="right">Mail Id:</td>
                                <td align="left"><input type="text" style="width: 70%; font-weight: bold;" value="${employee.mailId}" readonly />
                                </td>
                                <td width="25%" align="right">Mobile No:<strong style="color: red">*</strong></td>
                                <td><input type="text" style="width: 50%; font-weight: bold;" value="${employee.mobileNo}" readonly /></td>
                            </tr>

                            <!-- Continue adding other details as needed -->

                            <tr>
                                <td width="25%" align="right">Identification Mark1:<strong style="color: red">*</strong></td>
                                <td align="left"><textarea style="width: 70%; font-weight: bold;" readonly>${employee.penIdnMark}</textarea>
                                </td>
                                <td width="25%" align="right">Date of Birth:<strong style="color: red">*</strong></td>
                                <td><input type="date" style="width: 50%; font-weight: bold;" value="${employee.dob}" readonly /></td>
                            </tr>
                            <tr>
                                <td width="25%" align="right">Identification Mark2:<strong style="color: red">*</strong></td>
                                <td align="left"><textarea style="width: 70%; font-weight: bold;" readonly>${employee.penIdnMark2}</textarea>
                                </td>
                                <td width="25%" align="right">Pan No.:<strong style="color: red">*</strong></td>
                                <td><input type="text" style="width: 50%; font-weight: bold;" value="${employee.panNo}" readonly /></td>
                            </tr>

                            <!-- Continue adding other details as needed -->

                            <tr>
                                <td width="25%" align="right">Religion:</td>
                                <td><input type="text" style="width: 50%; font-weight: bold;" value="${employee.religionId}" readonly />
                                </td>
                                <td width="25%" align="right">Nationality:<strong style="color: red">*</strong></td>
                                <td><input type="text" style="width: 50%; font-weight: bold;" value="${employee.nationalityId}" readonly /></td>
                            </tr>

                            <tr>
                                <td width="25%" align="right">Cvp Applied:<strong style="color:red">*</strong>
                                    <select name="cvp" id="cvp">
                                        <option value="Y" <c:if test="${not empty employee.cvp && employee.cvp eq 'Y'}"> <c:out
                                                        value='selected="selected"' /></c:if>>Yes</option>
                                        <option value="N" <c:if test="${not empty employee.cvp && employee.cvp eq 'N'}"> <c:out
                                                        value='selected="selected"' /></c:if>>No</option>
                                        </select>
                                    </td>
                                    <td width="25%" align="right">Cvp Percentage:<strong style="color: red">*</strong></td>
                                    <td><input type="text" style="width: 50%; font-weight: bold;" value="${employee.cvpPercentage}" /></td>
                            </tr>

                            <!-- Continue adding other details as needed -->

                            <tr>
                                <td width="25%" align="right">Height(in feet & inches)</td>
                                <td><input type="text" style="width: 50%; font-weight: bold;" value="${employee.height}" readonly />
                                </td>
                                <td width="25%" align="right">Final Gpf Applied Flag<strong style="color:red">*</strong>
                                    <select name="finalGpfAppliedFlag" id="finalGpfAppliedFlag">
                                        <option value="Y" <c:if test="${not empty employee.finalGpfAppliedFlag && employee.finalGpfAppliedFlag eq 'Y'}"> <c:out
                                                        value='selected="selected"' /></c:if>>Yes</option>
                                        <option value="N" <c:if test="${not empty employee.finalGpfAppliedFlag && employee.finalGpfAppliedFlag eq 'N'}"> <c:out
                                                        value='selected="selected"' /></c:if>>No</option>
                                        </select>
                                    </td>
                                </tr>

                                <tr>
                                    <td width="25%" align="right">Marital Status<strong style="color: red">*</strong>  <select name="intMaritalStatusTypeId" id="intMaritalStatusTypeId">
                                            <option value="">select</option>
                                            <option value="UNMARRIED">UNMARRIED</option>
                                            <option value="MARRIED">MARRIED</option>

                                        </select></td>
                                    <td width="25%" align="right">Gender</td>
                                    <td><input type="text" style="width: 50%; font-weight: bold;" value="${employee.sex}" /></td>
                            </tr>

                            <tr>
                                <td width="25%" align="right">Pension Category:
                                    <select name="penCategoryId" id="penCategoryId">

                                        <option value="2" <c:if test="${not empty employee.penCategoryId && employee.penCategoryId==2}"> <c:out
                                                        value='selected="selected"' /></c:if>>Family Pension</option>
                                        <option value="1" <c:if test="${not empty employee.penCategoryId && employee.penCategoryId==1}"> <c:out
                                                        value='selected="selected"' /></c:if>>Normal Pension</option>
                                        </select>
                                    </td>
                                    <td width="25%" align="right">Payable Treasury<strong style="color: red">*</strong></td>
                                    <td><input type="text" style="width: 50%; font-weight: bold;" value="${employee.treasuryCode}" /></td>
                            </tr>

                            <tr>
                                <td width="25%" align="right">IFSC Code<strong style="color: red">*</strong></td>
                                <td><input type="text" style="width: 50%; font-weight: bold;" value="${employee.ifscCode}" /></td>
                                <td width="25%" align="right">Bank Account No.<strong style="color: red">*</strong></td>
                                <td><input type="text" style="width: 50%; font-weight: bold;" value="${employee.bankAcctNo}" /></td>
                            </tr>

                            <tr>
                                <td width="25%" align="right">Bank Branch<strong style="color: red">*</strong></td>
                                <td><input type="text" style="width: 50%; font-weight: bold;" value="${employee.bankBranch}" /></td>
                            </tr>

                            <tr>
                                <td width="25%" align="right">Reason for Gpf Not Applied<strong style="color: red">*</strong></td>
                                <td><textarea style="width: 50%; font-weight: bold;">${employee.reason}</textarea></td>
                                <td width="25%" align="right">Bank Bsr Code:</td>
                                <td><input type="text" style="width: 50%; font-weight: bold;" value="${employee.bsrCode}" /></td>
                            </tr>

                            <!-- Continue adding other details as needed -->

                            <tr>
                                <td width="25%" align="right">DDO Code/DDO Name<strong style="color: red">*</strong></td>
                                <td><textarea style="width: 50%; font-weight: bold;">${employee.ddoName}</textarea></td>
                            </tr>

                        </table>
                    </div>

                    <div class="border mt-4">
                        <h4>Employee Permanent Address</h4>
                        <table class="table table-striped">
                            <tr>
                                <td width="25%" align="right">City/Village:<strong style="color: red">*</strong></td>
                                <td><input type="text" style="width: 50%; font-weight: bold;" value="${employee.perAddcity}" />
                                </td>
                            <tr>
                                <td width="25%" align="right">Town:<strong style="color: red">*</strong></td>
                                <td><input type="text" style="width: 50%; font-weight: bold;" value="${employee.perAddtown}" /></td>
                                <td width="25%" align="right">Police Station:<strong style="color: red">*</strong></td>
                                <td><input type="text" style="width: 50%; font-weight: bold;" value="${employee.perAddpoliceStation}" />
                                </td>
                            </tr>
                            <tr>
                                <td width="25%" align="right">State:<strong style="color: red">*</strong></td>
                                <td><input type="text" style="width: 50%; font-weight: bold;" value="${employee.perAddstateId}" /></td>
                                <td width="25%" align="right">District:<strong style="color: red">*</strong></td>
                                <td><input type="text" style="width: 50%; font-weight: bold;" value="${employee.districtCode}" />
                                </td>
                            </tr>
                            <tr>
                                <td width="25%" align="right">Pin:<strong style="color: red">*</strong></td>
                                <td><input type="text" style="width: 50%; font-weight: bold;" value="${employee.perAddpin}" /></td>
                            </tr>
                            </tr>
                        </table>
                    </div>



                    <div class="border mt-4">
                        <h4>Employee Communication Address</h4>
                        <table class="table table-striped">
                            <tr>
                                <td width="25%" align="right">City/Village:<strong style="color: red">*</strong></td>
                                <td><input type="text" style="width: 50%; font-weight: bold;" value="${employee.commAddcity}" />
                                </td>
                                <td width="25%" align="right">Town:<strong style="color: red">*</strong></td>
                                <td><input type="text" style="width: 50%; font-weight: bold;" value="${employee.commAddtown}" /></td>
                            </tr>
                            <tr>
                                <td width="25%" align="right">Police Station:<strong style="color: red">*</strong></td>
                                <td><input type="text" style="width: 50%; font-weight: bold;" value="${employee.commAddpoliceStation}" />
                                </td>
                                <td width="25%" align="right">State:<strong style="color: red">*</strong></td>
                                <td><input type="text" style="width: 50%; font-weight: bold;" value="${employee.commAddstateId}" /></td>
                            </tr>
                            <tr>
                                <td width="25%" align="right">District:<strong style="color: red">*</strong></td>
                                <td><input type="text" style="width: 50%; font-weight: bold;" value="${employee.commDistrictCode}" />
                                </td>
                                <td width="25%" align="right">Pin:<strong style="color: red">*</strong></td>
                                <td><input type="text" style="width: 50%; font-weight: bold;" value="${employee.commAddpin}" /></td>
                            </tr>
                        </table>
                    </div>



                    <div class="border mt-4">
                        <h4>Previous Pension Details</h4>
                        <table class="table table-striped">
                            <tr>
                                <td width="25%" align="right">Pension Type:
                                    <select name="prevPenType" id="prevPenType">
                                        <!-- Add options for pension type -->
                                        <option value="2" <c:if test="${not empty employee.prevPenType && employee.prevPenType==2}"> <c:out
                                                        value='selected="selected"' /></c:if>>Family Pension</option>
                                        <option value="1" <c:if test="${not empty employee.prevPenType && employee.prevPenType==1}"> <c:out
                                                        value='selected="selected"' /></c:if>>Normal Pension</option>
                                        </select>
                                    </td>
                                    <td width="25%" align="right">Source
                                    <td><input type="text" style="width: 50%; font-weight: bold;" value="${employee.prevPenSource}" />
                                </td>

                            </tr>
                            <tr>
                                <td width="25%" align="right">PPO/FPPO No.:</td>
                                <td><input type="text" style="width: 50%; font-weight: bold;" value="${employee.prevPPOOrFPPONo}" />
                                </td>
                                <td width="25%" align="right">Pension Amount</td>
                                <td><input type="text" style="width: 50%; font-weight: bold;" value="${employee.prevPenAmt}" /></td>
                            </tr>
                            <tr>
                                <td width="25%" align="right">Pension Effective From Date</td>
                                <td><input type="date" style="width: 50%; font-weight: bold;"
                                           value="${employee.prevPensionEfffromDate}" /></td>
                                <td width="25%" align="right">Previous Payable Treasury</td>
                                <td><input type="text" style="width: 50%; font-weight: bold;" value="${employee.prevPenPayTresCode}" />
                                </td>
                            </tr>
                            <tr>
                                <td width="25%" align="right">IFSC Code</td>
                                <td><input type="text" style="width: 50%; font-weight: bold;" value="${employee.prevPenBankIfscCd}" />
                                </td>
                                <td width="25%" align="right">Bank Branch</td>
                                <td><input type="text" style="width: 50%; font-weight: bold;" value="${employee.prevPenBankBranch}" />
                                </td>
                            </tr>
                            <tr>
                                <td width="25%" align="right">Pension issuing Authority</td>
                                <td><input type="text" style="width: 50%; font-weight: bold;" value="${employee.prevPenPia}" />
                                </td>
                            </tr>
                        </table>
                    </div>



                    <div class="row" style="border: 1px solid #ddd;padding: 5px;">
                        <table class="table table-bordered">
                            <tr>
                                <td colspan="8">Family Details:</td>
                            </tr>
                            <tr class="bg-primary text-white">
                                <th>Sl No.</th>
                                <th>Salutation</th>
                                <th>Relation</th>
                                <th>Name</th>
                                <th>Gender</th>
                                <th>Marital Status</th>
                                <th>Nominee</th>
                            </tr>
                            <tbody> 
                                <c:set var="nomineeStaus" value="N" />
                                <c:forEach items="${empfamilyList}" var="familyRelation" varStatus="cnt">
                                    <tr>
                                        <td scope="row">${cnt.index+1}</td>
                                        <td>${familyRelation.salutationFamily}</td>
                                        <td>${familyRelation.relation_type}</td>
                                        <td>${familyRelation.dependentName}</td>
                                        <td>${familyRelation.sex}</td>
                                        <td>${familyRelation.intMaritalStatusTypeId}</td>
                                        <td>${familyRelation.isNominee}
                                            <c:if test="${familyRelation.isNominee eq 'YES'}">
                                                <c:set var="nomineeStaus" value="Y" />
                                                <img src="images/verified.png" width="20" height="20"/>
                                            </c:if>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                        <div align="center">
                            <c:if test="${nomineeStaus eq 'N'}">
                                <strong style="color:red">**Please update your Nominee Details  </strong>
                            </c:if>
                        </div>

                    </div>
                </div>


                <div class="row" style="border: 1px solid #ddd;padding: 5px;">
                    <table class="table table-bordered">
                        <tr style="background:#004085;color:#FFFFFF;font-weight:bold;">
                            <td colspan="12"> Nominee Details</td>
                        </tr>
                        <tr class="bg-primary text-white">
                            <th>Sl No.</th>
                            <th>Salutation</th>
                            <th>Relation</th>
                            <th>Name</th>
                            <th>Gender</th>
                            <th>Marital Status</th>
                            <th>Mobile No</th>
                            <th>Account Number</th>
                            <th>Bank Branch</th>
                            <th>Ifsc Code</th>
                            <th>Share Percentage</th>

                        </tr>
                        <tbody>
                            <c:forEach items="${nomineeList}" var="nomineeRelation" varStatus="cnt">
                                <tr>
                                    <td scope="row">${cnt.index+1}</td>
                                    <td>${nomineeRelation.salutationNominee}</td>
                                    <td>${nomineeRelation.relation}</td>
                                    <td>${nomineeRelation.salutationNominee}</td>
                                    <td>${nomineeRelation.sex}</td>
                                    <td>${nomineeRelation.nomineeMaritalStatusId}</td>
                                    <td>${nomineeRelation.mobileNo}</td>
                                    <td>${nomineeRelation.bankAccountNo}</td>
                                    <td>${nomineeRelation.bankBranchName}</td>
                                    <td>${nomineeRelation.ifscCode}</td>
                                    <td>${nomineeRelation.sharePercentage}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>


                </div>






                <div class="form-check mt-4">
                    <input class="form-check-input" type="checkbox" name="chkPensionAcknowledge"
                           id="chkPensionAcknowledge" />
                    <label class="form-check-label" for="chkPensionAcknowledge">I hereby declare that all the information
                        furnished by me are true to the best of my knowledge.</label>
                </div>

                <div class="panel panel-secondary mt-4">
                    <div class="panel-body">
                        <div class="text-left">
                            <button type="submit" name="save" id="btnSubmit" class="btn btn-success btn-lg"
                                    onclick="return confirmSubmit();" disabled>
                                Submit
                            </button>
                        </div>
                    </div>
                </div>

            </form:form>
        </div>
    </c:if>
</body>
</html>
