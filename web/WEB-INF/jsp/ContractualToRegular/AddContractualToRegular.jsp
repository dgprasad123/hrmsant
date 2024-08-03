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
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>

        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script type="text/javascript">
            $(document).ready(function() {
                $('#txtPostOrdDt').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
                $('#txtWEFDt').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
                $('#txtPayRevisionDate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
            });

            function getDeptWiseOfficeList() {
                var deptcode = $('#hidPostedDeptCode').val();

                var url = 'getOfficeListJSON.htm?deptcode=' + deptcode;

                $('#hidPostedOffCode').empty();
                $('#hidPostedOffCode').append('<option value="">--Select Office--</option>');

                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#hidPostedOffCode').append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');
                    });
                });
            }

            function getOfficeWisePostList() {
                var offcode = $('#hidPostedOffCode').val();
                $('#postedspc').empty();

                var url = 'getPostCodeListJSON.htm?offcode=' + offcode;

                $('#postedspc').append('<option value="">--Select Post--</option>');

                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#postedspc').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                    });
                });
            }

            function getPost() {
                $('#postName7thPay').val($('#postedspc option:selected').text());
            }

            function saveCheck() {
                if ($('#empType').val() == '') {
                    alert("Please select Employee Type");
                    return false;
                }
                if ($('#sltEmployee').val() == '') {
                    alert("Please select Employee");
                    return false;
                }
                if ($('#txtPostOrdNo').val() == '') {
                    alert("Please enter Order No");
                    return false;
                }
                if ($('#txtPostOrdDt').val() == '') {
                    alert("Please enter Order Date");
                    return false;
                }
                if ($('#txtWEFDt').val() == '') {
                    alert("Please enter With Effect Date");
                    return false;
                }
                if ($('#txtBasic').val() == '') {
                    alert("Please enter Basic");
                    return false;
                }
                /*if ($('#txtLevel').val() == '') {
                 alert("Please enter Level");
                 return false;
                 }
                 if ($('#txtCell').val() == '') {
                 alert("Please enter Cell");
                 return false;
                 }*/
                if ($('#convertType1').val() == '' && $('#convertType3').val() == '')
                {
                    alert("Please select the type you want to convert.");
                    return false;
                }
                if ($('#convertType').val() == '') {
                    alert("Please Select Convert to.");
                    return false;
                }
                if ($('#convertType').val() == 'N' && $('#postNomenclature').val() == '') {
                    alert("Please enter the Post.");
                    return false;
                }
                var ifChecked = document.getElementById("chk7thPay").checked;

                if (ifChecked) {
                    if ($('input[name=rdOptionChosen]:checked').length <= 0) {
                        alert("Please Select one option!");
                        return false;
                    }

                    if ($('input[name=rdOptionChosen]:checked').val() == 1) {
                        if ($('#postName7thPay').val() == '') {
                            alert("Please enter Post");
                            return false;
                        }
                        if ($('#sltPayScale').val() == '') {
                            alert("Please select Pay Scale");
                            return false;
                        }
                        if ($('#sltGradePay').val() == '') {
                            alert("Please select Grade Pay");
                            return false;
                        }
                    } else if ($('input[name=rdOptionChosen]:checked').val() == 2) {
                        if ($('#postName7thPay').val() == '') {
                            alert("Please enter Post");
                            return false;
                        }
                        if ($('#sltPayScale').val() == '') {
                            alert("Please select Pay Scale");
                            return false;
                        }
                        if ($('#sltGradePay').val() == '') {
                            alert("Please select Grade Pay");
                            return false;
                        }
                        if ($('#txtPayRevisionDate').val() == '') {
                            alert("Please Enter Date");
                            return false;
                        }
                    }
                }
                return true;
            }
            function changeConvertType(idx, val)
            {
                if (val == 'N')
                {
                    $('#post_blk').css('display', 'block');
                }
                else
                {
                    $('#post_blk').css('display', 'none');
                }
                $('#convertType').val(val);
            }
            function onlyIntegerRange(e) {
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
            function refreshList(empType)
            {
                $('#labelConvertRegular').css('display', 'none');
                $('#labelConvertContractual').css('display', 'none');
                $('#labelConvertContractualSix').css('display', 'none');
                $('#labelConvert' + empType).css('display', 'block');
                //Now fetch the employees
                $.ajax({
                    type: "GET",
                    url: 'getFilteredEmployees.htm',
                    data: 'empType=' + empType,
                    dataType: 'json',
                    success: function(jsonObj) {
                        /*$('#gp').val(jsonObj.sp[0].gp);
                         $('#postgrp').val(jsonObj.sp[0].postgrp);
                         $('#orderNo').val(jsonObj.sp[0].orderNo);
                         $('#orderDate').val(jsonObj.sp[0].orderDate);  
                         $('#scaleofPay').val(jsonObj.sp[0].payscale);
                         $('#paylevel').val(jsonObj.sp[0].paylevel);
                         $('#substantivePostModal').modal("show");*/
                        dropdown = $('#sltEmployee');
                        dropdown.empty();
                        dropdown.append('<option selected="true" value="">--Select--</option>');
                        dropdown.prop('selectedIndex', 0);
                        $.each(jsonObj.empList, function(key, entry) {
                            dropdown.append($('<option></option>').attr('value', entry.value).text(entry.label));
                        })

                    }
                });
            }

            function togglePayFixationArea() {
                var ifChecked = document.getElementById("chk7thPay").checked;

                if (ifChecked) {
                    $('#payfixationarea').show();
                } else {
                    $('#payfixationarea').hide();
                }
            }
        </script>
    </head>
    <body>
        <form:form action="saveContractualToRegular.htm" method="POST" commandName="contractualToRegularForm">
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        Contractual(Six Years) To Regular or Contractual/Regular to Contractual or Contractual(Six Years)
                    </div>
                    <div class="panel-body">
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="empType">Select Employee</label>
                            </div>
                            <div class="col-lg-2">
                                <form:select path="empType" id="empType" class="form-control" onchange="javascript: refreshList(this.value)">
                                    <form:option value="">--Select Employee Type--</form:option>
                                    <form:option value="Regular">Regular</form:option>
                                    <form:option value="ContractualSix">Contractual (Six Years)</form:option>
                                </form:select>
                            </div>
                            <div class="col-lg-5">   
                                <form:select path="sltEmployee" id="sltEmployee" class="form-control">
                                    <form:option value="">--Select--</form:option>
                                    <form:options items="${contractualToRegularDataList}" itemValue="value" itemLabel="label"/>
                                </form:select>
                            </div>
                            <div class="col-lg-2">

                            </div>
                            <div class="col-lg-2">

                            </div>
                        </div>
                        <hr />
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="txtPostOrdNo">Order No<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">   
                                <form:input class="form-control" path="txtPostOrdNo" id="txtPostOrdNo"/>
                            </div>
                            <div class="col-lg-2">
                                <label for="txtPostOrdDt">Order Date<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">
                                <div class="input-group date" id="processDate">
                                    <form:input class="form-control" path="txtPostOrdDt" id="txtPostOrdDt" readonly="true"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>                                
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="txtWEFDt">With Effect From<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">
                                <div class="input-group date" id="processDate">
                                    <form:input class="form-control" path="txtWEFDt" id="txtWEFDt" readonly="true"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>                         
                            </div>
                            <div class="col-lg-2">
                                <label for="txtBasic">Basic<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">
                                <form:input class="form-control" path="txtBasic" id="txtBasic" maxlength="6" onkeypress="return onlyIntegerRange(event)"/>
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="txtLevel">Level</label>
                            </div>
                            <div class="col-lg-2">
                                <form:input class="form-control" path="txtLevel" id="txtLevel" maxlength="2" onkeypress="return onlyIntegerRange(event)"/>
                            </div>
                            <div class="col-lg-2">
                                <label for="txtCell">Cell</label>
                            </div>
                            <div class="col-lg-2">
                                <form:input class="form-control" path="txtCell" id="txtCell" maxlength="2" onkeypress="return onlyIntegerRange(event)"/>                         
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="txtLevel">Convert to<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-3">
                                <input type="hidden" name="convertType" id="convertType" value="" />
                                <span id="labelConvertRegular" style="display:none;">
                                    <form:select path="convertType1" id="convertType1" class="form-control" onchange="javascript: changeConvertType(1, this.value)">
                                        <form:option value="">--Select--</form:option>
                                        <form:option value="N">Regular to Contractual</form:option>
                                        <form:option value="C">Regular to Contractual (Six Years)</form:option>
                                    </form:select>
                                </span>
                                <span id="labelConvertContractual" style="display:none;">
                                    <form:select path="convertType2" id="convertType2" class="form-control" onchange="javascript: changeConvertType(2, this.value)">
                                        <form:option value="">--Select--</form:option>
                                        <form:option value="Y">Contractual to Regular</form:option>
                                        <form:option value="C">Contractual to Contractual (Six Years)</form:option>
                                    </form:select>
                                </span>
                                <span id="labelConvertContractualSix" style="display:none;">
                                    <form:select path="convertType3" id="convertType3" class="form-control" onchange="javascript: changeConvertType(3, this.value)">
                                        <form:option value="">--Select--</form:option>
                                        <form:option value="Y">Contractual (Six Years) to Regular</form:option>
                                        <form:option value="N">Contractual (Six Years) to Contractual</form:option>
                                    </form:select>
                                </span>                                
                            </div>
                            <div class="col-lg-2">
                                <label for="chk7thPay">Eligible for 7th Pay</label>
                            </div>
                            <div class="col-lg-3">
                                <form:checkbox path="chk7thPay" id="chk7thPay" value="Y" class="form-control" onclick="togglePayFixationArea();"/>
                            </div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;display:none;" id="post_blk">
                            <div class="col-lg-2">
                                <label for="postNomenclature">Post<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-3">
                                <form:input class="form-control" path="postNomenclature" id="postNomenclature" />
                            </div>
                            <div class="col-lg-7"></div>
                        </div>
                        <div id="payfixationarea" style="display: none;">
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label>Option 1</label>
                                </div>
                                <div class="col-lg-4">
                                    <form:radiobutton path="rdOptionChosen" value="1"/>&nbsp;Pay Revision from 01-01-2016.
                                </div>
                                <div class="col-lg-2">
                                    <label>Option 2</label>
                                </div>
                                <div class="col-lg-4">
                                    <form:radiobutton path="rdOptionChosen" value="2"/>&nbsp;Pay Revision from opted date.
                                    <span class="input-group date" id="processDate">
                                        <form:input path="txtPayRevisionDate" id="txtPayRevisionDate" size="15" class="form-control" readonly="true"/>
                                    </span>
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="postName7thPay">7th Pay Post</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:input class="form-control" path="postName7thPay" id="postName7thPay" readonly="true"/>
                                </div>
                                <div class="col-lg-1">
                                    <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#post7thPayModal">
                                        <span class="glyphicon glyphicon-search"></span> Search
                                    </button>
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="sltPayScale">Drawing Pay Band</label>
                                </div>
                                <div class="col-lg-3">
                                    <form:select path="sltPayScale" id="sltPayScale" style="width:200px;" class="form-control">
                                        <form:option value="">--Select--</form:option>
                                        <form:option value="4750-14680">4750-14680</form:option>
                                        <form:option value="4930-14680">4930-14680</form:option>
                                        <form:option value="5200-20200">5200-20200</form:option>
                                        <form:option value="9300-34800">9300-34800</form:option>
                                        <form:option value="15600-39100">15600-39100</form:option>
                                        <form:option value="37400-67000">37400-67000</form:option>
                                        <form:option value="67000-79000">67000-79000</form:option>
                                        <form:option value="75500-80000">75500-80000</form:option>
                                        <form:option value="80000">80000</form:option>
                                        <form:option value="90000">90000</form:option>  
                                    </form:select>
                                </div>
                                <div class="col-lg-2">
                                    <label for="sltGradePay">Grade Pay</label>
                                </div>
                                <div class="col-lg-3">
                                    <form:select path="sltGradePay" id="sltGradePay" class="form-control">
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
                                        <form:option value="4600">4600</form:option>
                                        <form:option value="4800">4800</form:option>
                                        <form:option value="5400">5400</form:option>
                                        <form:option value="6000">6000</form:option>
                                        <form:option value="6600">6600</form:option>
                                        <form:option value="7000">7000</form:option>                                            
                                        <form:option value="7600">7600</form:option>
                                        <form:option value="8000">8000</form:option>
                                        <form:option value="8700">8700</form:option>
                                        <form:option value="8800">8800</form:option>
                                        <form:option value="8900">8900</form:option>
                                        <form:option value="9000">9000</form:option>
                                        <form:option value="10000">10000</form:option>
                                    </form:select>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="panel-footer">
                        <input type="submit" name="btnContrToReg" value="Save" class="btn btn-default" onclick="return saveCheck();"/>
                        <%--<input type="submit" name="btnContrToReg" value="Delete" class="btn btn-default" onclick="return confirm('Are you sure to delete?');"/>--%>
                        <c:if test="${not empty status}">
                            <c:if test="${status == 1}">
                                <span style="color:#00cc66;font-weight:bold;">Data Saved</span>
                            </c:if>
                            <c:if test="${status == 2}">
                                <span style="color:#DE3939;font-weight:bold;">Data Deleted</span>
                            </c:if>
                        </c:if>
                    </div>
                </div>
            </div>

            <div id="post7thPayModal" class="modal" role="dialog">
                <div class="modal-dialog">
                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Details of Posting</h4>
                        </div>
                        <div class="modal-body">
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="hidPostedDeptCode">Department</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidPostedDeptCode" id="hidPostedDeptCode" class="form-control" onchange="getDeptWiseOfficeList();">
                                        <form:option value="">--Select Department--</form:option>
                                        <form:options items="${deptlist}" itemValue="deptCode" itemLabel="deptName"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="hidPostedOffCode">Office</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidPostedOffCode" id="hidPostedOffCode" class="form-control" onchange="getOfficeWisePostList();">
                                        <form:option value="">--Select Office--</form:option>
                                        <form:options items="${postedOfflist}" itemValue="offCode" itemLabel="offName"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="postedspc">Post</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="postedspc" id="postedspc" class="form-control" onchange="getPost();">
                                        <form:option value="">--Select Post--</form:option>
                                        <form:options items="${postedPostlist}" itemValue="spc" itemLabel="postname"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        </div>
                    </div>
                </div>
            </div>
        </form:form>
    </body>
</html>
