<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>

<link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>

<script src="js/moment.js" type="text/javascript"></script>
<script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>

<script type="text/javascript">
    $(document).ready(function() {
        $('.txtDate').datetimepicker({
            format: 'D-MMM-YYYY',
            useCurrent: false,
            ignoreReadonly: true
        });
    });
    function addRow() {
        var rowcount = $("table#incrTable tbody tr").length;
        rowcount = rowcount + 1;
        var markup = "<tr><td><input type='text' name='incrDt' class='txtDate' readonly='true'/></td><td><input type='text' name='incrCell' class='incrCell' id='incrCell_" + rowcount + "' size='5' maxlength='2'/>&nbsp;<input type='text' name='revisedbasic' class='revisedbasic' id='revisedbasic_" + rowcount + "' maxlength='6' size='7' onkeypress='return onlyIntegerRange(event)'/></td><td><input type='text' name='incrLevel' class='incrLevel' id='incrLevel_" + rowcount + "' size='5' maxlength='2'/>&nbsp;<button type=\"button\" id=\"delete\">Delete</button></td></tr>";
        $("table#incrTable tbody").append(markup);
        $('.txtDate').datetimepicker({
            format: 'D-MMM-YYYY',
            useCurrent: false,
            ignoreReadonly: true
        });
    }
    $('body').on('click', '#delete', function(e) {
        if (confirm("Want to delete?")) {
            var cur_tr = $(this).parents('tr');
            cur_tr.remove();
        }
    });

    $('body').on('blur', '.revisedbasic', function(e) {
        //alert($(this).val());
        var basicdata = $(this).attr("id");
        //alert("id is: "+id);
        verifyPayAgainstLevelAndCell(basicdata);
        getRowData();
    });
    function verifyPayAgainstLevelAndCell(basicdata) {
        var basicArr = basicdata.split("_");

        var idval = basicArr[1];

        var cellval = $("#incrCell_" + idval).val();
        var basicval = $("#revisedbasic_" + idval).val();
        var levelval = $("#incrLevel_" + idval).val();

        var url = "ValidatePayAgainstLevelAndCell.htm?cell=" + cellval + "&basic=" + basicval + "&level=" + levelval;
        if (basicval != "") {
            $.getJSON(url, function(data) {
                if (data.status == "N") {
                    alert("Pay entered against Level and Cell is wrong.");
                }
            }).done(function(result) {

            });
        }
    }
    function getRowData() {
        var tempdata1 = 0;
        var finaldata = 0;
        $("input[type='text'][name=revisedbasic]").each(function() {
            var tempdata2 = parseInt($(this).val());
            if (tempdata2 > tempdata1) {
                finaldata = tempdata2;
            } else if (tempdata1 > tempdata2) {
                finaldata = tempdata1;
            }
            tempdata1 = finaldata;
        });
        $("#incrementMsg").text("Now Current Basic is " + finaldata);
    }

    function validateForm() {
        if ($("#hooSpc").val() == "") {
            alert("Please select Name of the Head of Office");
            return false;
        }
        if ($("#officiating").val() == "") {
            alert("Please select Post held by the employee");
            return false;
        }
        if ($("#previousPayScale").val() == "") {
            alert("Please select Existing Pay Band");
            return false;
        }
        if ($("#previousGp").val() == "") {
            alert("Please select Existing Grade Pay");
            return false;
        }
        if ($("#basicpayFixPaymatrix").val() == "") {
            alert("Please enter Pay to be fixed in the Level of Pay Matrix");
            return false;
        }
        if ($("#doeIncr").val() == "") {
            alert("Please enter Date of next increment");
            return false;
        }
        return true;
    }
    function approveForm() {
        if (validateForm()) {
            if (confirm('Are you sure to Approve?')) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
</script>

<div class="container-fluid">
    <form:form action="SaveThirdScheduleContractual6YrsToRegularData.htm" method="post" commandName="thirdScheduleForm">
        <form:hidden path="sltBillGroup"/>
        <form:hidden path="empid"/>        
        <table class="table" width="100%">
            <tr>
                <td width="10%">1.</td>
                <td width="40%">
                    Name of the Employee
                </td>
                <td width="50%">
                    <form:input path="empname" class="form-control"/>
                </td>
            </tr>
            <tr>
                <td>2.</td>
                <td>
                    Name of the Head of Office(Designation only)
                </td>
                <td>
                    <form:select path="hooSpc" class="form-control">
                        <form:option value="">--Select--</form:option>
                        <form:options items="${PostList2nd}" itemLabel="post" itemValue="postcode"/>
                    </form:select>
                </td>
            </tr>
            <tr>
                <td>3.</td>
                <td>
                    Post held by the employee(Substantive/Officiating)
                </td>
                <td>
                    <form:select path="officiating" class="form-control">
                        <form:option value="Substantive">Substantive</form:option>
                        <form:option value="Officiating">Officiating</form:option>
                    </form:select>
                </td>
            </tr>
            <tr>
                <td>4.</td>
                <td>
                    Existing Pay Band and Grade Pay of the Post
                </td>
                <td>
                    <form:select path="previousPayScale" class="form-control">
                        <form:option value="">--Select--</form:option>
                        <form:option value="4750-14680">4750-14680</form:option>
                        <form:option value="4930-14680">4930-14680</form:option>
                        <form:option value="5200-20200">5200-20200</form:option>
                        <form:option value="9300-34800">9300-34800</form:option>
                    </form:select>
                    <form:select path="previousGp" class="form-control">
                        <form:option value="">--Select--</form:option>
                        <form:option value="0">0</form:option>
                        <form:option value="1700">1700</form:option>
                        <form:option value="1775">1775</form:option>
                        <form:option value="1800">1800</form:option>
                        <form:option value="1900">1900</form:option>
                        <form:option value="2000">2000</form:option>
                        <form:option value="2200">2200</form:option>
                        <form:option value="2400">2400</form:option>
                        <form:option value="2800">2800</form:option>
                        <form:option value="4200">4200</form:option>
                    </form:select>
                </td>
            </tr>
            <tr>
                <td>5.</td>
                <td>
                    Pay to be fixed in the Level of Pay Matrix as per provisions of the ORSP Rules, 2017.(Illustration may be referred.)
                </td>
                <td>
                    <form:input path="basicpayFixPaymatrix" class="form-control" size="5" maxlength="2" onkeypress="return onlyIntegerRange(event)"/>&ensp;
                </td>
            </tr>
            <tr>
                <td>6.</td>
                <td>
                    Date of next increment.
                </td>
                <td>
                    <form:input path="doeIncr" class="form-control txtDate" size="20" readonly="true"/>
                </td>
            </tr>
            <tr>
                <td>7.</td>
                <td>
                    Any other relevant information
                </td>
                <td>
                    <form:textarea path="otherInfo" size="10" maxlength="1000"/>
                </td>
            </tr>
        </table>
        <div style="margin-top: 30px;position:relative;">
            <table class="table" width="70%" cellspacing="0" cellpadding="0" border="0" align="center">
                <tr>
                    <td align="center"><b> Pay in the Cell in the Level after increment</b> </td>
                </tr>
                <tr>
                    <td align="center"><span id="incrementMsg" style="color: red;"></span></td>
                </tr>
            </table>
            <table class="table" align="center" id="incrTable">
                <thead>
                    <tr>
                        <td width="25%">Date of increment</td>
                        <td width="30%">Cell no and Pay</td>
                        <td width="15%">Level</td>
                    </tr>
                </thead>
                <tbody>
                    <c:if test="${not empty thirdScheduleForm.incrementList}">
                        <c:forEach var="incr" items="${thirdScheduleForm.incrementList}" varStatus="count">
                            <c:if test="${thirdScheduleForm.isApproved ne 'Y'}">
                                <tr>
                                    <td>
                                        <input type="text" name="incrDt" class="txtDate" size="20" value="${incr.incrDt}" readonly="true"/>
                                    </td>
                                    <td align="center">
                                        <input type="text" name="incrCell" class="incrCell" id="incrCell_${count.index + 1}" maxlength="2" size="5" value="${incr.cell}"/>
                                        <input type="text" name="revisedbasic" class="revisedbasic" id="revisedbasic_${count.index + 1}" size="7" maxlength="6" value="${incr.revisedbasic}" onkeypress="return onlyIntegerRange(event)"/>
                                    </td>
                                    <td>
                                        <input type="text" name="incrLevel" class="incrLevel" id="incrLevel_${count.index + 1}" maxlength="2" size="5" value="${incr.level}" onkeypress="return onlyIntegerRange(event)"/>
                                        &nbsp;<button type="button" id="delete">Delete</button>
                                    </td>
                                </tr>
                            </c:if>
                            <c:if test="${thirdScheduleForm.isApproved eq 'Y'}">
                                <tr>
                                    <td>
                                        <c:out value="${incr.incrDt}"/>
                                    </td>
                                    <td>
                                        <c:out value="${incr.cell}"/>&nbsp;
                                        <c:out value="${incr.revisedbasic}"/>
                                    </td>
                                    <td>
                                        <c:out value="${incr.level}"/>
                                    </td>
                                </tr>
                            </c:if>
                        </c:forEach>
                    </c:if>
                </tbody>
            </table>
            <c:if test="${thirdScheduleForm.isApproved ne 'Y'}">
                <button type="button" onclick="addRow();">Add Row</button>
            </c:if>
        </div><br />
        <c:if test="${thirdScheduleForm.isApproved ne 'Y'}">
            <input type="submit" name="btnSubmit" value="Save" class="btn btn-success" onclick="return validateForm();"/>
            <input type="submit" name="btnSubmit" value="Approve" class="btn btn-danger" onclick="return approveForm();"/>
        </c:if>
        <c:if test="${thirdScheduleForm.isApproved eq 'Y'}">
            <span style="display:block;text-align: center;font-weight: bold;font-size: 16px;color: #FF0000;">APPROVED</span>
        </c:if>
    </form:form>
</div>
