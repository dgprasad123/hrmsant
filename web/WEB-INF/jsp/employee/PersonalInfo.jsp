<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<% int i = 1;
%>

<html>
    <head>
        <title>Employee Profile</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
    </script>
</head>

<body>
    <form:form  action="personalinfo.htm" method="POST" commandName="emp">
        <table border="0" class="tableview" cellpadding="0" cellspacing="0" style="background: #f2f2f2" width="100%">
            <tr>
                <td width="5%">&nbsp;</td>
                <td width="25%">&nbsp;</td>
                <td width="30%" align="center">&nbsp;</td>
                <td width="15%" align="center">&nbsp;</td>
                <td width="5%" align="center">&nbsp;</td>
                <td width="5%" align="center">&nbsp;</td>
                <td width="5%">&nbsp;</td>

            </tr>
            <tr height="40px">
                <td align="center"><%=i++%>.</td>
                <td>Employee's Full Name:</td>
                <td colspan="3" width="">
                    <c:out value="${emp.empName}" />

                </td>
            </tr>

            <tr height="40px">
                <td align="center"><%=i++%>.</td>
                <td>Employee's GPF No:</td>
                <td colspan="3">
            <html:hidden id="empid" path="empid" value="${emp.empid}"/>
            <span><c:out value="${emp.gpfno}" />&nbsp;(HRMS ID:<c:out value="${emp.empid}" />)</span>
        </td>
        <td>&nbsp; </td>
    </tr>

    <tr height="40px">
        <td align="center"><%=i++%>.</td>
        <td>GIS Type:</td>
        <td>
            <form:select path="gisType" style="width:34%;" class="form-control">
                <form:option value="" label="Select" cssStyle="width:30%"/>
                <c:forEach items="${gisList}" var="gis">
                    <form:option value="${gis.schemeId}" label="${gis.schemeName}"/>
                </c:forEach>                                 
            </form:select> 
        </td>
        <td>&nbsp; </td>
    </tr>
</div>
<tr height="40px">
    <td align="center"><%=i++%>.</td>
    <td>GIS No:</td>
    <td>
        <form:input path="gisNo" class="form-control" style="width:30%;" id="gisNo" placeholder="Enter GIS No"/>
    </td>
    <td>&nbsp; </td>
</tr>
<tr height="40px">
    <td align="center"><%=i++%>.</td>
    <td>
        Date of Birth(dd-MMM-yyyy):
    </td>
    <td>

        <span><c:out value="${emp.dob}" /></span>
        <input type="hidden" name="dob" id="dob"/>
    </td>

    <td align="left" colspan="1" rowspan="4" valign="top">
        <fieldset style="border:  1px solid black;" width="35%">
            <legend align="left"><span style="color: black">Employee Photo</span></legend>
        </fieldset>
    </td>	
    <td>&nbsp; </td>
</tr>
<tr height="40px">
    <td align="center"><%=i++%>.</td>
    <td>
        Select Age of Superannuation (in Years)
    </td>
    <td>
        <input type="radio" name="radyear1" id="radyear1" value="58"  onclick="getSupDate(${emp.empid}, '58', '${emp.txtDos}', '${emp.dob}')"/>58&nbsp;
        <input type="radio" name="radyear1" id="radyear2" value="60"  onclick="getSupDate(${emp.empid}, '60', '${emp.txtDos}', '${emp.dob}')"/>60&nbsp;
        <input type="radio" name="radyear1" id="radyear3" value="62"  onclick="getSupDate(${emp.empid}, '62', '${emp.txtDos}', '${emp.dob}')"/>62&nbsp;
    </td>
    <td>&nbsp;</td>
</tr>
<tr height="40px">
    <td align="center" ><%=i++%>.</td>
    <td>Date of Superannuation(dd-MMM-yyyy): &nbsp;<span style="color: red">*</span></td>
    <td>
        <div class='input-group date' style="width:40%;" id='processDate'>
            <form:input class="form-control"  id="txtDos" path="txtDos" />
            <span class="input-group-addon">
                <span class="glyphicon glyphicon-time"></span>
            </span>
        </div>

    </td>
    <td>&nbsp; </td>
</tr>

<tr height="40px">
    <td align="center" ><%=i++%>.</td>
    <td>Date from which in continuous service with GoO(dd-MMM-yyyy):</td>
    <td>
        <div class='input-group date' style="width:40%;" id='processDate'>
            <form:input class="form-control"  id="joindategoo" path="joindategoo" />
            <span class="input-group-addon">
                <span class="glyphicon glyphicon-time"></span>
            </span>
        </div>
    </td>
    <td>&nbsp; </td>
</tr>
<tr height="40px">
    <td align="center" ><%=i++%>.</td>
    <td>Time</td>
    <td>
        <form:select id="txtwefTime" class="form-control" path="txtwefTime" style="width:30%;">
    <option value="">--Select One--</option>
    <option value="FN">FORENOON</option>
    <option value="AN">AFTERNOON</option>
</form:select> 
</td>
<td>&nbsp; </td>
</tr>
<tr height="40px">
    <td align="center" ><%=i++%>.</td>
    <td>Date of entry into Govt. service(dd-MMM-yyyy):</td>
    <td>
        <div class='input-group date' style="width:40%;" id='processDate'>
            <form:input class="form-control"  id="doeGov" path="doeGov" />
            <span class="input-group-addon">
                <span class="glyphicon glyphicon-time"></span>
            </span>
        </div>

    </td>
    <td>&nbsp; </td>
</tr>
<tr height="40px">
    <td align="center" ><%=i++%>.</td>
    <td>Time</td>
    <td>
        <form:select id="timeOfEntryGoo" class="form-control" path="timeOfEntryGoo" style="width:30%;">
    <option value="">--Select One--</option>
    <option value="FN">FORENOON</option>
    <option value="AN">AFTERNOON</option>
</form:select> 
</td>
<td>&nbsp; </td>
</tr>

<tr height="40px">
    <td align="center"><%=i++%>.</td>
    <td>Gender:${emp.gender}</td>
    <td >
        <c:if test="${emp.gender=='M'}">
            <input type="radio" name="gender" value="M" checked/>
            Male&nbsp;&nbsp;&nbsp;&nbsp;
            <input type="radio"  name="gender" value="F" />Female
        </c:if>
        <c:if test="${emp.gender=='F'}">
            <input type="radio" name="gender" value="M" />
            Male&nbsp;&nbsp;&nbsp;&nbsp;
            <input type="radio"  name="gender" value="F" checked />Female
        </c:if>

        <c:if test="${emp.gender==''}">
            <input type="radio" name="gender" value="M"/>
            Male&nbsp;&nbsp;&nbsp;&nbsp;
            <input type="radio"  name="gender" value="F" />Female
        </c:if>
    </td>
    <td>&nbsp; </td>
</tr>
<tr height="40px">
    <td align="center"><%=i++%>.</td>
    <td>Marital Status:</td>
    <td>
        <form:select path="marital" class="form-control" style="width:60%;">
            <form:option value="" label="Select" cssStyle="width:30%"/>
            <c:forEach items="${maritallist}" var="marital">
                <form:option value="${marital.maritalId}" label="${marital.maritalStatus}"/>
            </c:forEach>                                 
        </form:select> 

    </td>
    <td>&nbsp; </td>
</tr>


<tr height="40px">
    <td align="center"><%=i++%>.</td>
    <td>Category:</td>
    <td>
        <form:select path="category" class="form-control" style="width:60%;">
            <form:option value="" label="Select" cssStyle="width:30%"/>
            <c:forEach items="${categoryList}" var="category">
                <form:option value="${category.categoryid}" label="${category.categoryName}"/>
            </c:forEach>                                 
        </form:select> 
    </td>
    <td>&nbsp; </td>
</tr>
<tr height="40px">
    <td align="center"><%=i++%>.</td>
    <td>Height(in cm):</td>
    <td>
        <form:input path="height" class="form-control" style="width:40%;" id="height" placeholder="Enter Height"/>
    </td>
    <td>&nbsp; </td>
</tr>
<tr height="40px">
    <td align="center"><%=i++%>.</td>
    <td>Blood Group:</td>
    <td>
        <form:select path="bloodgrp" class="form-control" style="width:60%;">
            <form:option value="" label="Select" cssStyle="width:30%"/>
            <c:forEach items="${bloodGrpList}" var="bloodgrp">
                <form:option value="${bloodgrp.bloodgrpId}" label="${bloodgrp.bloodgrp}"/>
            </c:forEach>                                 
        </form:select> 
    </td>
    <td>&nbsp; </td>
</tr>

<!--************ CHANGED BY PKM STARTS *********** -->     
<tr height="40px">
    <td align="center"><%=i++%>.</td>
    <td>Declaration of Home Town:</td>
    <td>
        <form:input path="homeTown" class="form-control" id="homeTown" placeholder="Please Enter HomeTown"/>

    </td>
    <td>&nbsp; </td>
</tr>
<!--************ CHANGED BY PKM ENDS *********** -->     
<tr height="40px">
    <td align="center"><%=i++%>.</td>
    <td>Religion:</td>
    <td>
        <form:select path="religion" class="form-control" style="width:60%;">
            <form:option value="" label="Select" cssStyle="width:30%"/>
            <c:forEach items="${religionList}" var="religion">
                <form:option value="${religion.religionId}" label="${religion.religion}"/>
            </c:forEach>                                 
        </form:select> 
    </td>
    <td>&nbsp; </td>
</tr>
<tr height="40px">
    <td align="center"><%=i++%>.</td>
    <td>Domicile:</td>
    <td>
        <form:input path="domicil" class="form-control" style="width:40%;" id="domicil" placeholder="Please Enter Domicil"/>

    </td>
    <td>&nbsp; </td>
</tr>
<tr height="40px">
    <td align="center"><%=i++%>.</td>
    <td>Personal Identification Mark:</td>
    <td colspan="5">
        <form:input path="idmark" class="form-control" id="idmark" style="width:60%;" placeholder="Please Enter If Any Identification Mark"/>
    </td>
</tr>
<tr height="40px">
    <td align="center"><%=i++%>.</td>
    <td>Bank Name:</td>
    <td colspan="5">
        <form:select path="sltBank" id="sltBank" class="form-control" style="width:60%;" onchange="getBranchList(this);">
            <form:option value="" label="Select" />
            <c:forEach items="${bankList}" var="bank">
                <form:option value="${bank.bankcode}" label="${bank.bankname}"/>
            </c:forEach>                                 
        </form:select> 
    </td>
</tr>
<tr height="40px">
    <td align="center"><%=i++%>.</td>
    <td>Branch Name:</td>
    <td colspan="5">
        <form:select path="sltbranch" id="sltbranch" class="form-control" style="width:60%;" >
            <form:option value="" label="Select" cssStyle="width:30%"/>
            <c:forEach items="${branchList}" var="branch">
                <form:option value="${branch.branchcode}" label="${branch.branchname}"/>
            </c:forEach>                                 
        </form:select> 

    </td>
</tr>
<tr height="40px">
    <td align="center"><%=i++%>.</td>
    <td>Bank Account No:</td>
    <td>
        <form:input path="bankaccno" class="form-control" id="bankaccno" style="width:60%;" placeholder="Please Enter Bank Acc. No"/>
    </td>
    <td>&nbsp; </td>
</tr>
<tr height="40px">
    <td align="center"><%=i++%>.</td>
    <td>Mobile Number:</td>
    <td>
        <form:input path="mobile" class="form-control" id="mobile" style="width:60%;" placeholder="Please Enter Mobile No"/>
    </td>
    <td>&nbsp; </td>
</tr>
</table>


</form:form>
<script type="text/javascript">
    $(function() {
    $('#txtDos').datetimepicker({
    format: 'D-MMM-YYYY',
            useCurrent: false,
            ignoreReadonly: true
    });
     $('#joindategoo').datetimepicker({
    format: 'D-MMM-YYYY',
            useCurrent: false,
            ignoreReadonly: true
    });
     $('#doeGov').datetimepicker({
    format: 'D-MMM-YYYY',
            useCurrent: false,
            ignoreReadonly: true
    });
    });
</script>
</body>
</html>
