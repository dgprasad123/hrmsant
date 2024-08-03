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
            function getDDOList() {
                $('#sltDDO').empty();
                $('#sltDDO').append('<option value="">--Select DDO--</option>');
                var url = 'getDistrictWiseDDOList.htm?distCode=' + $("#sltDistrict").val();
                $.getJSON(url, function(result) {
                    $.each(result, function(i, field) {
                        $('#sltDDO').append($('<option>', {
                            value: field.value,
                            text: field.label
                        }));
                    });
                });
            }
            function getBranchList(me) {

                $('option', $('#sltBranch')).not(':eq(0)').remove();
                $.ajax({
                    type: "POST",
                    url: "bankbranchlistJSON.htm?bankcode=" + $(me).val(),
                    success: function(data) {
                        $.each(data, function(i, obj)
                        {
                            $('#sltBranch').append($('<option>', {
                                value: obj.branchcode,
                                text: obj.branchname

                            }));

                        });
                    }
                });
            }
            
            function saveCheck(){
                if($('#sltDistrict').val() == ""){
                    alert("Please select District");
                    return false;
                }
                if($('#sltDDO').val() == ""){
                    alert("Please select DDO");
                    return false;
                }
                if($('#sltBank').val() == ""){
                    alert("Please select Bank");
                    return false;
                }
                if($('#sltBranch').val() == ""){
                    alert("Please select Branch");
                    return false;
                }
                if($('#bankAccNo').val() == ""){
                    alert("Please enter Bank Acc No");
                    return false;
                }
                
                if($('#amount').val() == ""){
                    alert("Please enter Amount");
                    return false;
                }else if( parseInt($('#restAmount').val()) < parseInt($('#amount').val())){
                    alert("Entered Amount must be less than "+$('#restAmount').val());
                    return false;
                }
            }
            
            function onlyIntegerRange(e){
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
        </script>
    </head>
    <body>
        <form:form action="savePvtDeductionAccount.htm" method="POST" commandName="command">
            <form:hidden path="billNo"/>
            <form:hidden path="pvtdednid"/>
            <input type="hidden" id="restAmount" value="${restAmount}"/>

            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        Add More Private Deduction Amount
                    </div>
                    <div class="panel-body">
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="sltDistrict">Select District</label>
                            </div>
                            <div class="col-lg-3">
                                <form:select path="sltDistrict" id="sltDistrict" class="form-control" onchange="getDDOList();">
                                    <form:option value="">--Select District--</form:option>
                                    <c:forEach items="${districtList}" var="district">
                                        <form:option value="${district.distCode}" label="${district.distName}"/>
                                    </c:forEach>
                                </form:select>
                            </div>
                            <div class="col-lg-2">
                                <label for="sltDDO">Select DDO</label>
                            </div>
                            <div class="col-lg-5">   
                                <form:select path="sltDDO" id="sltDDO" class="form-control">
                                    <form:option value="">--Select DDO--</form:option>
                                    <c:forEach items="${ddoList}" var="ddo">
                                        <form:option value="${ddo.value}" label="${ddo.label}"/>
                                    </c:forEach>
                                </form:select>
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="sltBank">Select Bank</label>
                            </div>
                            <div class="col-lg-3">
                                <form:select path="sltBank" id="sltBank" class="form-control" onchange="getBranchList(this);">
                                    <form:option value="" label="Select" />
                                    <c:forEach items="${bankList}" var="bank">
                                        <form:option value="${bank.bankcode}" label="${bank.bankname}"/>
                                    </c:forEach>
                                </form:select>
                            </div>
                            <div class="col-lg-2">
                                <label for="sltBranch">Select Branch</label>
                            </div>
                            <div class="col-lg-5">   
                                <form:select path="sltBranch" id="sltBranch" class="form-control">
                                    <form:option value="" label="Select" cssStyle="width:30%"/>
                                    <c:forEach items="${branchList}" var="branch">
                                        <form:option value="${branch.branchcode}" label="${branch.branchname}"/>
                                    </c:forEach>
                                </form:select>
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="bankAccNo">Account No</label>
                            </div>
                            <div class="col-lg-3">
                                <form:input path="bankAccNo" id="bankAccNo" class="form-control" maxlength="25"/>
                            </div>
                            <div class="col-lg-2">
                                <label for="amount">Amount</label>
                            </div>
                            <div class="col-lg-5">   
                                <form:input path="amount" id="amount" class="form-control" onkeypress="return onlyIntegerRange(event)" maxlength="10"/>
                                <span style="color: #ff0000">Not More than ${restAmount}</span>
                            </div>
                        </div>
                    </div>
                    <div class="panel-footer">
                        <input type="submit" name="btnAddAccount" value="Save" class="btn btn-primary" onclick="return saveCheck();"/>
                        <c:if test="${not empty command.pvtdednid}">
                            <input type="submit" name="btnAddAccount" value="Delete" class="btn btn-danger" onclick="return confirm('Are you sure to Delete?')"/>
                        </c:if>
                        <input type="submit" name="btnAddAccount" value="Back" class="btn btn-default"/>
                    </div>
                </div>
            </div>
        </form:form>
    </body>
</html>
