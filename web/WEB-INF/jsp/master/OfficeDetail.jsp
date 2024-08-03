<%-- 
    Document   : OfficeDetail
    Created on : Nov 22, 2017, 11:09:09 AM
    Author     : manisha
--%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        <link href="css/sb-admin.css" rel="stylesheet">
        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>
        <script type="text/javascript">
            function getOfficeList() {
                var url = 'getOfficeListJSON.htm?deptcode=' + $("#deptCode").val();
                $.getJSON(url, function (data) {
                    $('#poffCode').empty();
                    $('#poffCode').append($('<option>').text("Select").attr('value', ""));
                    $.each(data, function (i, obj) {
                        $('#poffCode').append($('<option>').text(obj.offName).attr('value', obj.offCode));
                    });
                });
            }
            function getPostList() {
                var url = 'getGenericPostListJSON.htm?deptCode=' + $("#deptCode").val();
                $.getJSON(url, function (data) {
                    $('#ddoPost').empty();
                    $('#ddoPost').append($('<option>').text("Select").attr('value', ""));
                    $.each(data, function (i, obj) {
                        $('#ddoPost').append($('<option>').text(obj.post).attr('value', obj.postcode));
                    });
                });
            }
            function empPostUpdate() {
                var url = 'getEmployeeGenericPostListJSON.htm?empid=' + $("#ddoName").val();
                $.getJSON(url, function (data) {
                    $.each(data, function (i, obj) {
                        $('#ddoPost').val(obj.postcode);
                        $('#ddoPostName').val(obj.post);
                    });
                });
            }
            function getBranchList(me) {

                $('option', $('#sltBranch')).not(':eq(0)').remove();
                $.ajax({
                    type: "POST",
                    url: "bankbranchlistJSON.htm?bankcode=" + $(me).val(),
                    success: function (data) {
                        $.each(data, function (i, obj)
                        {
                            $('#sltBranch').append($('<option>', {
                                value: obj.branchcode,
                                text: obj.branchname
                            }));

                        });
                    }
                });
            }
            function saveCheck() {
                var offEn = $('#offEn').val();
                if (offEn == "") {
                    alert("Please Enter Office Name");
                    $('#ddoCode').focus();
                    return false;
                }
                var ddoCode = $('#ddoCode').val();
                if (ddoCode == "") {
                    alert("Please Enter DDO Code");
                    $('#ddoCode').focus();
                    return false;
                }
                var deptCode = $('#deptCode').combobox('getValue');
                if (deptCode == "") {
                    alert("Please Select Department");
                    return false;
                }
                // return true;
            }
            function editAction() {
                $('#hidMode').val('U');
                //alert($('#hidMode').val());

            }


        </script>      
        <style type="text/css">
            .control-label {
                padding-top: 7px;
                margin-bottom: 0;
                text-align: left;
            }
            .row{
                margin-bottom: 5px;
            }
            @media (min-width: 800px) {
                .modal-dialog {
                    width: 600;
                    margin: 30px auto;
                }
                .modal-content {
                    -webkit-box-shadow: 0 5px 15px rgba(0, 0, 0, .5);
                    box-shadow: 0 5px 15px rgba(0, 0, 0, .5);
                }
                .modal-sm {
                    width: 300px;
                }
            }
        </style>
    </head>
    <body>
        <div class="container-fluid">
            <div class="panel panel-default">
                <div class="panel-heading" align="center" style="background-color: #868686;color: #ffffff;font-size: xx-large;"></div>
                <div class="panel-body">
                    <div class="table-responsive">
                        <%--<jsp:include page="../tab/hrmsadminmenu.jsp"/> --%>       
                        <%--<div id="page-wrapper">--%> 
                        <form:form action="getOfficeDetail.htm" commandName="officeModel">
                            <form:hidden path="mode" id="hidMode"/>
                            <div class="container-fluid" >
                                <div style="text-align:center;border:2px;font-weight:bold;font-size: 20px ">
                                    OFFICE ESTABLISHMENT DETAILS

                                </div>
                                <div class="row bg-primary" style="padding: 5px;">Office Information</div>
                                <form action="/action_page.php">
                                    <div class="row">
                                        <label class="control-label col-sm-3">1&emsp;Office code  </label> 
                                        <div class="col-sm-6" >
                                            <form:hidden path="offCode" value="${officeModel.offCode}"/>
                                            ${officeModel.offCode}
                                        </div>
                                    </div>  
                                    <div class="row">
                                        <label class="control-label col-sm-3">2&emsp;Office Name  </label> 
                                        <div class="col-sm-6" >
                                            <%--<form:input class="form-control" path="offEn" id="offEn"/>--%>
                                            <form:label path="offEn" value="${officeModel.offEn}"/>
                                            ${officeModel.offEn}

                                        </div>
                                    </div>
                                    <div class="row">     
                                        <label class="control-label col-sm-3">3&emsp; Name of the department </label>
                                        <p class="form-control-static col-sm-8" style="width:50%">                                    
                                            <form:select class="form-control" path="deptCode" id="deptCode" onchange="getOfficeList()" disabled="true">
                                                <form:option value="" label="Select"/>
                                                <form:options items="${departmentList}" itemLabel="deptName" itemValue="deptCode"/>                                        
                                            </form:select>                                    
                                        </p>
                                    </div>                        

                                    <div class="row">
                                        <label class="control-label col-sm-3" >4&emsp;Establishment Type</label>
                                        <p class="form-control-static col-sm-8">Field office</p>
                                    </div>

                                    <div class="row bg-primary" style="padding: 5px;">Office Address</div>

                                    <div class="row">                            
                                        <label class="control-label col-sm-3">5&emsp; Office Address</label>
                                    </div>                        

                                    <div class="row">
                                        <label class="control-label col-sm-3" >&emsp;&emsp;Address</label>
                                        <div class="col-sm-6" >
                                            <form:input class="form-control" path="offAddress" disabled="true" />                                    
                                        </div>
                                    </div>
                                    <div class="row">
                                        <label class="control-label col-sm-3" >&emsp;&emsp;Village/Town/City:</label>
                                        <div class="col-sm-8" style="width:50%">
                                            <form:input class="form-control" path="villagename" disabled="true" />                                     
                                        </div>
                                    </div>
                                    <div class="row">
                                        <label class="control-label col-sm-3" >&emsp;&emsp;Block:</label>                                
                                        <div class="col-sm-3" style="width:50%">
                                            <form:select class="form-control" path="blockCode" disabled="true">
                                                <form:option value="" label="Select"/>
                                                <form:options items="${blockList}" itemLabel="blockCode" itemValue="blockName"/>                                        
                                            </form:select>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <label class="control-label col-sm-3" >&emsp;&emsp;Pin Code:</label>
                                        <div class="col-sm-6" >
                                            <form:input class="form-control" path="pincode" disabled="true"/>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <label class="control-label col-sm-3" >&emsp;&emsp;District:</label>
                                        <p class="form-control-static col-sm-3">
                                            <form:select class="form-control" path="distCode" disabled="true">
                                                <form:option value="" label="Select"/>
                                                <form:options items="${districtList}" itemLabel="distName" itemValue="distCode"/>                                        
                                            </form:select>                                    
                                        </p>
                                        <p class="form-control-static col-sm-3" style="font-weight: bold">State:<form:input class="form-control" path="stateName" disabled="true"/>
                                        </p>

                                    </div>
                                    <div class="row">
                                        <label class="control-label col-sm-3">6&emsp;Office telephone no.</label>
                                        <p class="form-control-static col-sm-1">Area / std code:</p>
                                        <p class="form-control-static col-sm-3"><form:input class="form-control" path="telStd" disabled="true"/></p>
                                        <p class="form-control-static col-sm-2">Tel
                                            No :<form:input class="form-control" path="telNo" disabled="true"/></p>
                                    </div>
                                    <div class="row">

                                        <label class="control-label col-sm-3">7&emsp;Office Fax no.</label>
                                        <div class="col-sm-3" >
                                            <form:input class="form-control" path="faxNo" disabled="true"/>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <label class="control-label col-sm-3" >8&emsp;Office email id</label>
                                        <div class="col-sm-6" >
                                            <form:input class="form-control" path="offEmail" disabled="true"/>
                                        </div>
                                    </div>

                                    <div class="row bg-primary" style="padding: 5px;">DDO Information</div>

                                    <div class="row">
                                        <label class="control-label col-sm-3">9&emsp;Parent Office</label>
                                        <div class="col-sm-9" style="width:50%">
                                            <form:select class="form-control" id="poffCode" path="poffCode" onchange="getPostList()" disabled="true">
                                                <form:option value="" label="Select"/>
                                                <form:options items="${parentOffList}" itemLabel="offName" itemValue="poffCode"/>                                        
                                            </form:select>
                                        </div>
                                    </div>   
                                    <div class="row">
                                        <label class="control-label col-sm-3">10&emsp;Sub Division</label>
                                        <div class="col-sm-9" style="width:50%">
                                            <form:select class="form-control" path="subDivisionCode" disabled="true">
                                                <form:option value="" label="Select"/>
                                                <form:options items="${subdivisionList}" itemLabel="subDivisionName" itemValue="subDivisionCode"/>                                        
                                            </form:select>
                                        </div>   
                                    </div> 
                                    <div class="row">
                                        <label class="control-label col-sm-3">11&emsp;DDO Name</label>
                                        <%--<div class="col-sm-6" style="width:50%">
                                            <form:input class="form-control" path="ddoName"/>
                                        </div>--%>
                                        <c:if test="${empty officeModel.mode}">
                                            <div class="col-sm-9" style="width:50%">
                                                <form:input class="form-control" path="ddoName" disabled="true"/>
                                            </div>
                                        </c:if>
                                        <c:if test="${officeModel.mode=='U'}">
                                            <div class="col-sm-9" style="width:50%">
                                                <form:select class="form-control" id="ddoName" path="ddoName" onchange="empPostUpdate(); ">
                                                    <form:option value="" label="Select" />
                                                    <c:forEach items="${employeeList}" var="employeeList">
                                                        <option value="${employeeList.empid}" <c:if test="${officeModel.ddoHrmsid eq employeeList.empid}">selected="selected"</c:if>>${employeeList.fname}</option>>
                                                    </c:forEach>                                           
                                                </form:select>
                                            </div>
                                        </c:if>
                                        <%--<div class="col-sm-9">${officeModel.ddoName}</div>--%>
                                    </div>
                                    <div class="row">
                                        <label class="control-label col-sm-3">12&emsp;Designation of DDO</label>
                                        <c:if test="${empty officeModel.mode}">
                                            <div class="col-sm-9" style="width:50%">
                                                <form:input class="form-control" id="ddoPostName" path="ddoPostName" readonly="true"/>      

                                            </div>
                                        </c:if>
                                        <c:if  test="${officeModel.mode=='U'}">
                                            <div class="col-sm-9" style="width:50%">                                                
                                                <form:hidden class="form-control" id="ddoPost" path="ddoPost"/> 
                                                <form:input class="form-control" id="ddoPostName" path="ddoPostName" readonly="true"/>                                                 
                                            </div>
                                        </c:if>
                                    </div>
                                    <div class="row">                            
                                        <label class="control-label col-sm-3">13&emsp;DDO Code</label>
                                        <div class="col-sm-6" >
                                            <form:input class="form-control" path="ddoCode" disabled="true"/>
                                            <%--<form:hidden path="ddoCode" value="${officeModel.ddoCode}"/>
                                            ${officeModel.ddoCode}--%>
                                        </div>
                                    </div>

                                    <c:if test="${empty officeModel.mode}">
                                        <div class="row">
                                            <label class="control-label col-sm-3" >14&emsp;DDO current Acc No</label>
                                            <div class="col-sm-6"> 
                                                <form:input class="form-control" path="ddoCurAccNo"  disabled="true" />                                    
                                            </div>
                                        </div>
                                    </c:if>
                                    <c:if test="${officeModel.mode=='U'}">
                                        <div class="row">
                                            <label class="control-label col-sm-3" >14&emsp;DDO current Acc No</label>
                                            <div class="col-sm-6"> 
                                                <form:input class="form-control" path="ddoCurAccNo" />                                    
                                            </div>
                                        </div>
                                    </c:if>


                                    <c:if test="${empty officeModel.mode}">
                                        <div class="row">
                                            <label class="control-label col-sm-3" >15&emsp;Treasury Sarkar,Name</label>
                                            <div class="col-sm-8" style="width:50%">
                                                <form:input class="form-control" path="recBy" disabled="true" />                                    
                                            </div>
                                        </div>
                                    </c:if>
                                    <c:if test="${officeModel.mode=='U'}">
                                        <div class="row">
                                            <label class="control-label col-sm-3" >15&emsp;Treasury Sarkar,Name</label>
                                            <div class="col-sm-8" style="width:50%">
                                                <form:input class="form-control" path="recBy"  />                                    
                                            </div>
                                        </div>
                                    </c:if>
                                    <c:if test="${empty officeModel.mode}">
                                        <div class="row">
                                            <label class="control-label col-sm-3">16&emsp;Bank Name</label>
                                            <div class="col-sm-9" style="width:50%">
                                                <form:select class="form-control" id="sltBank" path="sltBank" onchange="getBranchList(this);" disabled="true" >
                                                    <form:option value="" label="Select" />
                                                    <c:forEach items="${bankList}" var="bank">
                                                        <form:option value="${bank.bankcode}" label="${bank.bankname}"/>
                                                    </c:forEach>                                           
                                                </form:select>
                                            </div>
                                        </div>
                                    </c:if>
                                    <c:if test="${officeModel.mode=='U'}">
                                        <div class="row">
                                            <label class="control-label col-sm-3">16&emsp;Bank Name</label>
                                            <div class="col-sm-9" style="width:50%">
                                                <form:select class="form-control" id="sltBank" path="sltBank" onchange="getBranchList(this);"  >
                                                    <form:option value="" label="Select" />
                                                    <c:forEach items="${bankList}" var="bank">
                                                        <form:option value="${bank.bankcode}" label="${bank.bankname}"/>
                                                    </c:forEach>                                           
                                                </form:select>
                                            </div>
                                        </div>
                                    </c:if>
                                    <c:if test="${empty officeModel.mode}">
                                        <div class="row">
                                            <label class="control-label col-sm-3">17&emsp;Branch Name</label>
                                            <div class="col-sm-9" style="width:50%">
                                                <form:select id="sltBranch" class="form-control" path="sltBranch"  disabled="true">
                                                    <form:option value="" label="Select" cssStyle="width:30%"/>
                                                    <c:forEach items="${branchList}" var="branch">
                                                        <form:option value="${branch.branchcode}" label="${branch.branchname}"/>
                                                    </c:forEach>                                          
                                                </form:select>
                                            </div>
                                        </div>
                                    </c:if>
                                    <c:if test="${officeModel.mode=='U'}">
                                        <div class="row">
                                            <label class="control-label col-sm-3">17&emsp;Branch Name</label>
                                            <div class="col-sm-9" style="width:50%">
                                                <form:select id="sltBranch" class="form-control" path="sltBranch" >
                                                    <form:option value="" label="Select" cssStyle="width:30%"/>
                                                    <c:forEach items="${branchList}" var="branch">
                                                        <form:option value="${branch.branchcode}" label="${branch.branchname}"/>
                                                    </c:forEach>                                          
                                                </form:select>
                                            </div>
                                        </div>
                                    </c:if>                                  
                                    <div class="row">
                                        <label class="control-label col-sm-3">18&emsp;Office Treasury/sub Treasury Name</label>
                                        <div class="col-sm-9" style="width:50%">
                                            <c:if test="${not empty officeModel.trCode }">
                                                <form:select class="form-control" path="trCode" disabled="true">
                                                    <form:option value="" label="Select"/>
                                                    <form:options items="${treasuryList}" itemLabel="treasuryName" itemValue="treasuryCode"/>                                        
                                                </form:select>
                                                <form:hidden path="trCode"/>
                                            </c:if>
                                            <c:if test="${ empty officeModel.trCode }">
                                                <form:select class="form-control" path="trCode" >
                                                    <form:option value="" label="Select"/>
                                                    <form:options items="${treasuryList}" itemLabel="treasuryName" itemValue="treasuryCode"/>                                        
                                                </form:select>
                                            </c:if>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <label class="control-label col-sm-3" >19&emsp;Office Bank Name</label>
                                        <div class="col-sm-10">
                                            <p class="form-control-static"></p>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <label class="control-label col-sm-3" >20&emsp;Head of Office(HOO) designation</label>
                                        <div class="col-sm-10">
                                            <p class="form-control-static"></p>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <label class="control-label col-sm-3" >21&emsp;Number of Employees</label>
                                        <p class="form-control-static col-sm-3">Group 05</p>
                                        <p class="form-control-static col-sm-3">Group 90</p>
                                        <p class="form-control-static col-sm-3">Group 95</p>
                                    </div>
                                    <c:if test="${empty officeModel.mode}">
                                        <div class="row">
                                            <label class="control-label col-sm-3" >22&emsp;DDO Regd No</label>
                                            <div class="col-sm-6" style="width:20%">
                                                <form:input class="form-control" path="ddoRegNo" disabled="true" />
                                            </div>
                                        </div>
                                        <div class="row">                            
                                            <label class="control-label col-sm-3">23&emsp; Tan Number</label>
                                            <div class="col-sm-6" style="width:20%" >
                                                <form:input class="form-control" path="tanNo" disabled="true"/>
                                            </div>
                                        </div>
                                        <div class="row">                            
                                            <label class="control-label col-sm-3">24&emsp; DTO Regd No</label>
                                            <div class="col-sm-6" style="width:20%">
                                                <form:input class="form-control" path="dtoRegNo" disabled="true"/>
                                            </div>
                                        </div>
                                    </c:if>
                                    <c:if test="${officeModel.mode=='U'}">

                                        <div class="row">
                                            <label class="control-label col-sm-3" >22&emsp;DDO Regd No</label>
                                            <div class="col-sm-6" style="width:20%">
                                                <form:input class="form-control" path="ddoRegNo" />
                                            </div>
                                        </div>
                                        <div class="row">                            
                                            <label class="control-label col-sm-3">23&emsp; Tan Number</label>
                                            <div class="col-sm-6" style="width:20%" >
                                                <form:input class="form-control" path="tanNo"/>
                                            </div>
                                        </div>
                                        <div class="row">                            
                                            <label class="control-label col-sm-3">24&emsp; DTO Regd No</label>
                                            <div class="col-sm-6" style="width:20%">
                                                <form:input class="form-control" path="dtoRegNo"/>
                                            </div>
                                        </div>
                                    </c:if>
                                    <div class="row">                            
                                        <label class="control-label col-sm-3">25&emsp;Total Nu.of Employees(GRA+GRB+GRC+GRD)</label>
                                        <p class="form-control-static col-sm-8"></p>
                                    </div>
                                    <div class="row">                            
                                        <label class="control-label col-sm-3">26&emsp; Lic PA Code</label>
                                        <c:if test="${empty officeModel.mode}">
                                            <div class="col-sm-6" style="width:20%">
                                                <form:input class="form-control" path="paCode" disabled="true" />
                                            </div>
                                        </c:if>
                                        <c:if test="${officeModel.mode=='U'}">
                                            <div class="col-sm-6" style="width:20%">
                                                <form:input class="form-control" path="paCode"/>
                                            </div>
                                        </c:if>

                                    </div>
                                </form>
                                <div class="row bg-primary" style="padding: 5px;">Verify and Certify Information</div>
                                <div class="panel-footer row">
                                    <div class="col-xs-12 text-center">
                                        <c:if test="${empty officeModel.mode}">
                                            <input type="submit" value="Edit" name="edit"  class="btn btn-default btn-primary" style=" text-align: center" onclick="editAction()" />
                                        </c:if>
                                        <c:if test="${officeModel.mode=='U'}">
                                            <input type="submit" value="Update" name="save" class="btn btn-default btn-primary" onclick="saveCheck()"/> 
                                            <button type="submit" value="Back" name="back" class="btn btn-default btn-primary">Back</button>
                                        </c:if>                                      
                                    </div>
                                </div>
                            </form:form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
