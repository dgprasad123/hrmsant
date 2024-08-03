<%-- 
    Document   : GranteeOfficeDetail
    Created on : 7 Jan, 2019, 2:12:16 PM
    Author     : Surendra
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

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>        
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
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
                var offadd = $('#offAddress').val();
                if (offadd == "") {
                    alert("Please Enter Office Address");
                    $('#offAddress').focus();
                    return false;
                }

                if ($('#distCode').val()=='') {
                    alert("Please Select District.");
                    $('#distCode').focus();
                    return false;
                }
                
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
                margin-left:0px;
                margin-right:0px;
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
        <jsp:include page="../report/aerTab.jsp">
            <jsp:param name="menuHighlight" value="PART_B" />
        </jsp:include>

        <form:form action="addGranteeDetail.htm" commandName="officeModel">
            <form:hidden path="mode" id="hidMode"/>
            <form:hidden path="aerId"/>
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <div class="row">
                            <div class="col-lg-12">
                                <h1 align="center"> OFFICE ESTABLISHMENT DETAILS </h1>
                            </div>
                        </div>

                    </div>
                    <div class="panel-body">
                        <div class="row bg-primary" style="padding: 5px;">Office Information</div>

                        <div class="row">
                            <label class="control-label col-sm-3">1&emsp;Office code  </label> 
                            <div class="col-sm-6" >
                                <form:hidden path="offStatus"/>
                                <form:hidden path="offCode" value="${officeModel.offCode}"/>
                                ${officeModel.offCode}
                            </div>
                        </div>  
                        <div class="row">
                            <label class="control-label col-sm-3">2&emsp;Office Name  </label> 
                            <div class="col-sm-6" >
                                <form:input class="form-control" style="text-transform: uppercase;" path="offEn" id="offEn"/>


                            </div>
                        </div>
                        <div class="row">     
                            <label class="control-label col-sm-3">3&emsp; Name of the department </label>
                            <p class="form-control-static col-sm-8" style="width:50%">                                    
                                <form:hidden class="form-control" path="deptCode" id="deptCode" />
                                ${officeModel.deptName} 

                            </p>
                        </div>                        



                        <div class="row bg-primary" style="padding: 5px;">Office Address</div>



                        <div class="row">
                            <label class="control-label col-sm-3" >&emsp;&emsp;Address</label>
                            <div class="col-sm-6" >
                                <form:input class="form-control" id="offAddress" path="offAddress" style="text-transform: uppercase;"/>                                    
                            </div>
                        </div>
                        <div class="row">
                            <label class="control-label col-sm-3" >&emsp;&emsp;Village/Town/City:</label>
                            <div class="col-sm-8" style="width:50%">
                                <form:input class="form-control" path="villagename" style="text-transform: uppercase;"/>                                     
                            </div>
                        </div>
                        <div class="row">
                            <label class="control-label col-sm-3" >&emsp;&emsp;District:</label>                                
                            <div class="col-sm-3" style="width:50%">
                                <form:select class="form-control" id="distCode" path="distCode">
                                    <form:option value="" label="Select"/>
                                    <form:options items="${districtList}" itemLabel="distName" itemValue="distCode"/>                                        
                                </form:select>     
                            </div>
                        </div>

                        <div class="row">
                            <label class="control-label col-sm-3" >&emsp;&emsp;Block:</label>
                            <p class="form-control-static col-sm-3">
                                <form:select class="form-control" path="sltBlock">
                                    <form:option value="" label="Select"/>
                                    <form:options items="${blockList}" itemLabel="blockName" itemValue="blockCode"/>                                        
                                </form:select>

                            </p>


                        </div>
                        <div class="row">
                            <label class="control-label col-sm-3" >&emsp;&emsp;Treasury</label>
                            <p class="form-control-static col-sm-3">
                                <form:select class="form-control" path="trCode">
                                    <form:option value="" label="Select"/>
                                    <form:options items="${treasuryList}" itemLabel="treasuryName" itemValue="treasuryCode"/>                                        
                                </form:select>

                            </p>


                        </div>    
                        <div class="row">
                            <label class="control-label col-sm-3" >&emsp;&emsp;Pin Code:</label>
                            <p class="form-control-static col-sm-3">
                                <form:input class="form-control" path="pincode" maxlength="6"/>
                            </p>
                            <p class="form-control-static col-sm-3" style="font-weight: bold">State: ODISHA
                            </p>
                        </div>    
                        <div class="row">
                            <label class="control-label col-sm-3">6&emsp;Office telephone no.</label>

                            <p class="form-control-static col-sm-2"><form:input class="form-control" path="telNo"  maxlength="11"/></p>
                        </div>
                        <div class="row">

                            <label class="control-label col-sm-3">7&emsp;Office Fax no.</label>
                            <div class="col-sm-3" >
                                <form:input class="form-control" path="faxNo" maxlength="10"/>
                            </div>
                        </div>
                        <div class="row">
                            <label class="control-label col-sm-3" >8&emsp;Office email id</label>
                            <div class="col-sm-6" >
                                <form:input class="form-control" path="offEmail" maxlength="50"/>
                            </div>
                        </div>

                    </div>


                    <div class="panel-footer">

                        <c:if test="${empty officeModel.mode}">
                            <input type="submit" value="Add" name="save"  class="btn btn-default btn-primary" style=" text-align: center" onclick="return saveCheck()"/>
                            <a href="aerReportPartB.htm?aerId=${aerId}"><button type="button" value="Back" name="back" class="btn btn-default btn-primary">Back</button></a>
                        </c:if>
                        <c:if test="${officeModel.mode=='U'}">
                            <input type="submit" value="Update" name="save" class="btn btn-default btn-primary" onclick="return saveCheck()"/> 
                            <button type="submit" value="Back" name="back" class="btn btn-default btn-primary">Back</button>
                        </c:if>                                      

                    </div>
                </div>
            </div>
        </form:form>

    </body>
</html>
