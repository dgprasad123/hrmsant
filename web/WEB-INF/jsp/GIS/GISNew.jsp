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
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>

        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script type="text/javascript">
            $(document).ready(function() {
                $('#txtDepositDate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
            })
            function getDistWiseOfficeList() {
                var deptcode = $("#hidDeptCode").val();
                var distcode = $("#hidDistCode").val();
                $("#hidOffCode").empty();
                $("#hidOffCode").append('<option value="">--Select Office--</option>');

                var url = "getOfficeListDistrictAndDepartmentJSON.htm?deptcode=" + deptcode + "&distcode=" + distcode;
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $("#hidOffCode").append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');
                    });
                });
            }

            function getOfficeWiseGenericPostList() {
                var offcode = $("#hidOffCode").val();
                $("#hidGPC").empty();
                $("#hidGPC").append('<option value="">--Select Generic Post--</option>');
                var url = "getAuthorityPostListJSON.htm?offcode=" + offcode;

                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $("#hidGPC").append('<option value="' + obj.value + '">' + obj.label + '</option>');
                    });
                });
            }

            function getGenericPostWiseSPCList() {
                var offcode = $("#hidOffCode").val();
                var gpc = $("#hidGPC").val();
                var url = "getAuthoritySubstantivePostListJSON.htm?postcode=" + gpc + "&offcode=" + offcode;

                $("#authSpc").empty();
                $("#authSpc").append('<option value="">--Select Substantive Post--</option>');

                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $("#authSpc").append('<option value="' + obj.spc + '">' + obj.spn + '</option>');
                    });
                });
            }

            function getPost() {
                //$("#spn").val($("#spc option:selected").text());
                $("#authorityPostName").val($("#authSpc option:selected").text());
            }
        </script>
    </head>
    <body>
        <form:form action="saveGISData.htm" method="POST" commandName="gisForm">
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        Employee Group Insurance Scheme
                    </div>
                    <div class="panel-body">
                        <form:hidden path="gisid"/>                     
                                                            

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-1">1.</div>
                            <div class="col-lg-4">
                                <label for="txtInstrumentNo">Instrument No.through which the Premium is deposited(e.g:Treasury Challan,Bank Draft,etc)<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">   
                                <form:input class="form-control" path="txtInstrumentNo" id="txtInstrumentNo"/>
                            </div>
                            <div class="col-lg-5"></div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-1">2.</div>
                            <div class="col-lg-4">
                                <label for="txtDepositDate">Date of Deposit of Premium</label>
                            </div>
                            <div class="col-lg-2">
                                <div class="input-group date" id="processDate">
                                    <form:input class="form-control" path="txtDepositDate" id="txtDepositDate" readonly="true"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>                           
                            </div>
                            <div class="col-lg-5"></div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-1">3.</div>
                            <div class="col-lg-4">
                                <label for="authorityPostName">Authority Details who Deposited the Premium<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-5">
                                <form:input class="form-control" path="authorityPostName" id="authorityPostName" readonly="true"/>                           
                            </div>
                            <div class="col-lg-2">
                                <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#authorityModal">
                                    <span class="glyphicon glyphicon-search"></span> Search
                                </button>
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-1">4.</div>
                            <div class="col-lg-4">
                                <label for="txtVoucherNo">Treasury Voucher No<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">   
                                <form:input class="form-control" path="txtVoucherNo" id="txtVoucherNo"/>
                            </div>
                            <div class="col-lg-5"></div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-1">5.</div>
                            <div class="col-lg-4">
                                <label for="sltSchemeName">Scheme Name<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">   
                                <form:select path="sltSchemeName" id="sltSchemeName" class="form-control">
                                    <form:option value="">--Select--</form:option>
                                    <form:options items="${schemeList}" itemValue="value" itemLabel="label"/>
                                </form:select>
                            </div>
                            <div class="col-lg-5"></div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-1">6.</div>
                            <div class="col-lg-4">
                                <label for="sltTreasuryName">Treasury Name<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">   
                                <form:select path="sltTreasuryName" id="sltTreasuryName" class="form-control">
                                    <form:option value="">--Select--</form:option>
                                    <form:options items="${treasuryList}" itemValue="treasuryCode" itemLabel="treasuryName"/>
                                </form:select>
                            </div>
                            <div class="col-lg-5"></div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-1">7.</div>
                            <div class="col-lg-4">
                                <label for="txtPremiumAmount">Premium Amount Deposited in Rs.<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">   
                                <form:input class="form-control" path="txtPremiumAmount" id="txtPremiumAmount"/>
                            </div>
                            <div class="col-lg-5"></div>
                        </div>
                    </div>
                    <div class="panel-footer">
                        <input type="submit" name="btnGIS" value="Save" class="btn btn-default" onclick="return saveCheck();"/>
                        <c:if test="${not empty gisForm.gisid}">
                            <a href="#"> <input type="button" name="action" value="Delete" class="btn btn-default" onclick="deleteGIS(${gisForm.gisid})"/></a>
                        </c:if>
                    </div>
                </div>
            </div>

            <div id="authorityModal" class="modal" role="dialog">
                <div class="modal-dialog">
                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Authority Details</h4>
                        </div>
                        <div class="modal-body">
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="sltDept">Department</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidDeptCode" id="hidDeptCode" class="form-control">
                                        <option value="">--Select Department--</option>
                                        <form:options items="${deptlist}" itemValue="deptCode" itemLabel="deptName"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="hidDistCode">District</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidDistCode" id="hidDistCode" class="form-control" onchange="getDistWiseOfficeList();">
                                        <option value="">--Select District--</option>
                                        <form:options items="${distlist}" itemValue="distCode" itemLabel="distName"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="hidOffCode">Office</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidOffCode" id="hidOffCode" class="form-control" onchange="getOfficeWiseGenericPostList();">
                                        <form:option value="">--Select Office--</form:option>
                                        <form:options items="${officeList}" itemValue="offCode" itemLabel="offName"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="hidGPC">Generic Post</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidGPC" id="hidGPC" class="form-control" onchange="getGenericPostWiseSPCList();">
                                        <form:option value="">--Select Generic Post--</form:option>
                                        <form:options items="${gpclist}" itemValue="value" itemLabel="label"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="authSpc">Post</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="authSpc" id="authSpc" class="form-control" onchange="getPost();">
                                        <form:option value="">--Select Post--</form:option>
                                        <form:options items="${spclist}" itemValue="spc" itemLabel="spn"/>
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
        
        <script type="text/javascript">
                function deleteGIS(gisid){
                    var con=confirm("Do you want to delete this Insurance Premium ?");
                    if(con){
                        window.location="deleteGIS.htm?gisid="+gisid;
                    }                  
                }
        </script>  
    </body>
</html>
