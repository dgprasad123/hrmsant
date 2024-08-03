<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<% int i = 1;
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">   
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <script type="text/javascript" src="js/jquery.min.js"></script>
        <script type="text/javascript" src="js/bootstrap.min.js"></script>   
        <script type="text/javascript">
            $(document).ready(function () {
                $.ajax({
                    method: "POST",
                    url: "getStateWiseDistrictList.htm",
                    data: {stateCode: $("#stateCode").val()}
                }).done(function (data) {
                    $.each(data, function (key, obj) {
                        $('#distCode').append(new Option(obj.distName, obj.distCode));
                    });
                });
                $("#stateCode").bind("change", function () {
                    $.ajax({
                        method: "POST",
                        url: "getStateWiseDistrictList.htm",
                        data: {stateCode: $(this).val()}
                    }).done(function (data) {
                        $('#distCode').empty();
                        $('#distCode').append(new Option('Select District ', ''));
                        $.each(data, function (key, obj) {
                            $('#distCode').append(new Option(obj.distName, obj.distCode));
                        });
                    });
                });

                $("#distCode").bind("change", function () {
                    $.ajax({
                        method: "POST",
                        url: "getDistrictWiseBlockList.htm",
                        data: {distcode: $(this).val()}
                    }).done(function (data) {
                        $('#blockCode').empty();
                        $('#blockCode').append(new Option('Select Block ', ''));
                        $.each(data, function (key, obj) {
                            $('#blockCode').append(new Option(obj.blockName, obj.blockCode));
                        });
                    });
                    $.ajax({
                        method: "POST",
                        url: "getDistrictWisePSList.htm",
                        data: {distcode: $(this).val()}
                    }).done(function (data) {
                        $('#psCode').empty();
                        $('#psCode').append(new Option('Select Police Station', ''));
                        $.each(data, function (key, obj) {
                            $('#psCode').append(new Option(obj.psName, obj.psCode));
                        });
                    });

                    $.ajax({
                        method: "POST",
                        url: "getDistrictWisePOList.htm",
                        data: {distcode: $(this).val()}
                    }).done(function (data) {
                        $('#postCode').empty();
                        $('#postCode').append(new Option('Select Post Office', ''));
                        $.each(data, function (key, obj) {
                            $('#postCode').append(new Option(obj.postOfficeName, obj.postOfficeCode));
                        });
                    });

                });
                $("#psCode").bind("change", function () {
                    $.ajax({
                        method: "POST",
                        url: "getDistrictWisePsWiseVillageList.htm",
                        data: {distcode: $("#distCode").val(), pscode: $(this).val()}
                    }).done(function (data) {
                        $('#villageCode').empty();
                        $('#villageCode').append(new Option('Select Village Name', ''));
                        $.each(data, function (key, obj) {
                            $('#villageCode').append(new Option(obj.villageName, obj.villageCode));
                        });
                    });
                });
            });
            function validate() {
                var addressType = $("#addressType").val();
                var address = $("#addressfield").val();
                var prAddress = $("#prAddress").val();
                var stateCode = $("#stateCode").val();
                var distCode = $("#distCode").val();
                var blockCode = $("#blockCode").val();
                var pinText = $("#pin").val();

                var address1 = $("#prAddress").val();
                var stateCode1 = $("#prStateCode").val();
                var distCode1 = $("#prDistCode").val();
                var blockCode1 = $("#prBlockCode").val();
                var pinText1 = $("#prPin").val();

                if (addressType == "") {
                    alert("Please select Address Type");
                    return false;
                }
                if (address == "") {
                    alert("Address cannot be blank");
                    return false;
                }
                if (stateCode == "") {
                    alert("Please select State");
                    return false;
                }
                if (distCode == "") {
                    alert("Please select District");
                    return false;
                }
                if (blockCode == "") {
                    alert("Please select Block");
                    return false;
                }
                if (pinText == "") {
                    alert("pin cannot be blank");
                    return false;
                }


                if (address1 == "") {
                    alert("Address cannot be blank");
                    return false;
                }
                if (stateCode1 == "") {
                    alert("Please select State");
                    return false;
                }
                if (distCode1 == "") {
                    alert("Please select District");
                    return false;
                }
                if (blockCode1 == "") {
                    alert("Please select Block");
                    return false;
                }
                if (pinText1 == "") {
                    alert("pin cannot be blank");
                    return false;
                }


            }
            function copyAddress() {
                var checkBox = document.getElementById('sameaspermanent');
                if (checkBox.checked == true)
                {
                    var HouseNo = $("#addressfield").val();
                    var State = $("#stateCode").val();
                    var District = $("#distCode").val();
                    var DistrictText = $("#distCode option:selected").text();
                    var Block = $("#blockCode").val();
                    var BlockText = $("#blockCode option:selected").text();
                    var PoliceStation = $("#psCode").val();
                    var PoliceStationText = $("#psCode option:selected").text();
                    var PostOffice = $("#postCode").val();
                    var PostOfficeText = $("#postCode option:selected").text();
                    var Village = $("#villageCode").val();
                    var VillageText = $("#villageCode option:selected").text();
                    var Pin = $("#pin").val();
                    var StdCode = $("#stdCode").val();
                    var Telephone = $("#telephone").val();
                    $("#prAddress").val(HouseNo);
                    $("#prStateCode").val(State);
                    $('#prDistCode').append(new Option(DistrictText, District));
                    $("#prDistCode").val(District);
                    $('#prBlockCode').append(new Option(BlockText, Block));
                    $("#prBlockCode").val(Block);
                    $('#prPsCode').append(new Option(PoliceStationText, PoliceStation));
                    $("#prPsCode").val(PoliceStation);
                    $('#prPostCode').append(new Option(PostOfficeText, PostOffice));
                    $("#prPostCode").val(PostOffice);
                    $('#prVillageCode').append(new Option(VillageText, Village));
                    $("#prVillageCode").val(Village);
                    $("#prPin").val(Pin);
                    $("#prStdCode").val(StdCode);
                    $("#prTelephone").val(Telephone);
                }
                else {
                    $("#prAddress").val('');
                    $("#prStateCode").val('');
                    $("#prDistCode").val('');
                    $("#prBlockCode").val('');
                    $("#prPsCode").val('');
                    $("#prPostCode").val('');
                    $("#prVillageCode").val('');
                    $("#prPin").val('');
                    $("#prStdCode").val('');
                    $("#prTelephone").val('');
                }
            }
            function changeprState(vals) {
                $.ajax({
                    method: "POST",
                    url: "getStateWiseDistrictList.htm",
                    data: {stateCode: vals}
                }).done(function (data) {
                    $('#prDistCode').empty();
                    $('#prDistCode').append(new Option('SELECT DISTRICT', ''));
                    $.each(data, function (key, obj) {
                        $('#prDistCode').append(new Option(obj.distName, obj.distCode));
                    });
                });
            }
            function changeprdistrict(vals) {
                $.ajax({
                    method: "POST",
                    url: "getDistrictWiseBlockList.htm",
                    data: {distcode: vals}
                }).done(function (data) {
                    $('#prBlockCode').empty();
                    $('#prBlockCode').append(new Option('SELECT BLOCK ', ''));
                    $.each(data, function (key, obj) {
                        $('#prBlockCode').append(new Option(obj.blockName, obj.blockCode));
                    });
                });
                $.ajax({
                    method: "POST",
                    url: "getDistrictWisePSList.htm",
                    data: {distcode: vals}
                }).done(function (data) {
                    $('#prPsCode').empty();
                    $('#prPsCode').append(new Option('Select Police Station', ''));
                    $.each(data, function (key, obj) {
                        $('#prPsCode').append(new Option(obj.psName, obj.psCode));
                    });
                });

                $.ajax({
                    method: "POST",
                    url: "getDistrictWisePOList.htm",
                    data: {distcode: vals}
                }).done(function (data) {
                    $('#prPostCode').empty();
                    $('#prPostCode').append(new Option('Select Post Office', ''));
                    $.each(data, function (key, obj) {
                        $('#prPostCode').append(new Option(obj.postOfficeName, obj.postOfficeCode));
                    });
                });

            }
            function changePrPoliceStation(vals) {
                $.ajax({
                    method: "POST",
                    url: "getDistrictWisePsWiseVillageList.htm",
                    data: {distcode: $("#prDistCode").val(), pscode: vals}
                }).done(function (data) {
                    $('#prVillageCode').empty();
                    $('#prVillageCode').append(new Option('Select Village Name', ''));
                    $.each(data, function (key, obj) {
                        $('#prVillageCode').append(new Option(obj.villageName, obj.villageCode));
                    });
                });

            }
            /*function checkAddress(contentId, msgId)
             {
             var area = document.getElementById(contentId);
             var message = document.getElementById(msgId);
             var maxLength = 50;
             if (area.value.length < maxLength) {
             message.style.color = '#FF0000';
             message.innerHTML = '<img src="images/cross-icon.png" width="15" /> ' + (maxLength - area.value.length) + " characters remaining";
             
             }
             else
             {
             message.innerHTML = '<img src="images/tick-icon.png" width="15" /> ' + (area.value.length) + " characters";
             message.style.color = '#008900';
             }
             
             }  */
        </script>
    </head>
    <body style="padding-top: 10px;">
        <jsp:include page="ProfileTabs.jsp">
            <jsp:param name="menuHighlight" value="ADDRESSPAGESB" />
        </jsp:include>
        <div id="profile_container">
            <div class="row" style="border: 1px solid #ddd;padding: 5px;">
                <form:form id="fm" action="saveEmployeeAddress.htm" method="post" name="myForm" class="form-horizontal" commandName="empaddress">
                    <div style=" margin-bottom: 5px;" class="panel panel-info">
                        <div class="">
                            <div class="form-group">
                                <label class="control-label col-sm-2" for="email">&nbsp;</label>
                                <div class="col-sm-4">

                                    <!-- <form:hidden path="empId"/>-->
                                    <form:hidden path="addressId"/>

                                    <form:hidden path="addressType" value="PERMANENT" />
                                    <div class="alert alert-info"> PERMANENT ADDRESS</div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-sm-2" for="email">House No/Flat No/Plot No/Block &nbsp;<span style="color: red">*</span>:</label>
                                <div class="col-sm-4">
                                    <form:textarea path="address" class="form-control" id="addressfield" />                            
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-sm-2" for="email">State &nbsp;<span style="color: red">*</span>:</label>
                                <div class="col-sm-3">
                                    <form:select path="stateCode" class="form-control" id="stateCode">
                                        <option value="21">ODISHA</option>
                                        <form:options items="${stateList}" itemValue="statecode" itemLabel="statename"/>
                                    </form:select>                            
                                </div>                    
                                <label class="control-label col-sm-1" for="email">District &nbsp;<span style="color: red">*</span>:</label>
                                <div class="col-sm-3">
                                    <form:select path="distCode" class="form-control" id="distCode">
                                        <option value="">SELECT  DISTRICT </option>
                                        <form:options items="${districtList}" itemValue="distCode" itemLabel="distName"/>
                                    </form:select>                            
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-sm-2" for="email">Block &nbsp;<span style="color: red">*</span>:</label>
                                <div class="col-sm-3">                            
                                    <form:select path="blockCode" class="form-control" id="blockCode">
                                        <form:options items="${blockList}" itemValue="blockCode" itemLabel="blockName"/>
                                        <option value="">SELECT  BLOCK </option>
                                    </form:select> 
                                </div>                    
                                <label class="control-label col-sm-1" for="email">Police Station:</label>
                                <div class="col-sm-3">                            
                                    <form:select path="psCode" class="form-control" id="psCode">
                                        <option value="">SELECT  POLICE STATION </option>
                                        <form:options items="${policeList}" itemValue="psCode" itemLabel="psName"/>
                                    </form:select> 
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-sm-2" for="email">Post Office &nbsp;<span style="color: red">*</span>:</label>
                                <div class="col-sm-3">                            
                                    <form:select path="postCode" class="form-control"  id="postCode">
                                        <form:options items="${postofficeList}" itemValue="postOfficeCode" itemLabel="postOfficeName"/>    
                                        <option value="">SELECT  POST OFFICE </option>

                                    </form:select> 
                                </div>                    
                                <label class="control-label col-sm-1" for="email">Village/Location :</label>
                                <div class="col-sm-3">                            
                                    <form:select path="villageCode" class="form-control" id="villageCode">
                                        <option value="">SELECT VILLAGE </option>
                                        <form:options items="${villageList}" itemValue="villageCode" itemLabel="villageName"/>   

                                    </form:select> 
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-sm-2" for="email">Pin &nbsp;<span style="color: red">*</span>:</label>
                                <div class="col-sm-3">
                                    <form:input path="pin" class="form-control" maxlength="6" id="pin"/>                            
                                </div>                    
                                <label class="control-label col-sm-1" for="email">Telephone:</label>
                                <div class="col-sm-1">
                                    <form:input path="stdCode" class="form-control" maxlength="10" id="stdCode"/>
                                </div>
                                <div class="col-sm-2">
                                    <form:input path="telephone" class="form-control" id="telephone"/><br>

                                </div>
                                <br>
                                <div>

                                </div>
                            </div>
                        </div>
                        <!-- <div class="panel-footer">
                             <input type="submit" class="btn btn-success" name="action" value="Save" onclick="return validate()"/>
                        <c:if test="${noc eq address.addressId}">
                            <input type="submit" class="btn btn-success" name="action" value="Delete"/>                        
                            <input type="submit" class="btn btn-success" name="action" value="Cancel"/>
                        </c:if>                    
                    </div>-->

                    </div>
                    <div class="container-fluid ">
                        <input class="form-check-input" type="checkbox" value="" id="sameaspermanent" style="  height: 20px; width: 20px;" onclick="copyAddress();">
                        <label class="form-check-label" for="flexCheckDefault">
                            Same as Permanent Address
                        </label>
                    </div>

                    <div style=" margin-bottom: 5px;" class="panel panel-info">

                        <div class="panel-body">
                            <div class="form-group">
                                <label class="control-label col-sm-2" for="email"><span style="font-weight:bold">&nbsp;</span></label>
                                <div class="col-sm-4">                              
                                    <form:hidden path="prAddressId"/>   
                                    <form:hidden path="prAddressType" value="PRESENT" />
                                    <div class="alert alert-success"> PRESENT ADDRESS</div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-sm-2" for="email">House No/Flat No/Plot No/Block &nbsp;<span style="color: red">*</span> :</label>
                                <div class="col-sm-4">
                                    <form:textarea path="prAddress" class="form-control" id="prAddress" />                            
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-sm-2" for="email">State &nbsp;<span style="color: red">*</span>:</label>
                                <div class="col-sm-3">
                                    <form:select path="prStateCode" class="form-control" id="prStateCode" onchange="changeprState(this.value);">

                                        <form:options items="${stateList}" itemValue="statecode" itemLabel="statename"/>
                                    </form:select>                            
                                </div>                    
                                <label class="control-label col-sm-1" for="email">District &nbsp;<span style="color: red">*</span>:</label>
                                <div class="col-sm-3">
                                    <form:select path="prDistCode" class="form-control" id="prDistCode" onchange="changeprdistrict(this.value);">
                                        <option value="">SELECT  DISTRICT </option>
                                        <form:options items="${prDistrictList}" itemValue="distCode" itemLabel="distName"/>
                                    </form:select>                            
                                </div>

                            </div>
                            <div class="form-group">
                                <label class="control-label col-sm-2" for="email">Block &nbsp;<span style="color: red">*</span>:</label>
                                <div class="col-sm-3">                            
                                    <form:select path="prBlockCode" class="form-control" id="prBlockCode">
                                        <option value="">SELECT  BLOCK </option>
                                        <form:options items="${prBlockList}" itemValue="blockCode" itemLabel="blockName"/>
                                    </form:select> 
                                </div>                    
                                <label class="control-label col-sm-1" for="email">Police Station &nbsp;<span style="color: red">*</span>:</label>
                                <div class="col-sm-3">                            
                                    <form:select path="prPsCode" class="form-control" id="prPsCode"  onchange="changePrPoliceStation(this.value)">
                                        <option value="">SELECT  POLICE STATION </option>
                                        <form:options items="${prPoliceList}" itemValue="psCode" itemLabel="psName"/>
                                    </form:select> 
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-sm-2" for="email">Post Office &nbsp;<span style="color: red">*</span>:</label>
                                <div class="col-sm-3">                            
                                    <form:select path="prPostCode" class="form-control"  id="prPostCode">
                                        <option value="">SELECT  POST OFFICE </option>                                        
                                        <form:options items="${prPostofficeList}" itemValue="postOfficeCode" itemLabel="postOfficeName"/>    


                                    </form:select> 
                                </div>                    
                                <label class="control-label col-sm-1" for="email">Village/Location &nbsp;:</label>
                                <div class="col-sm-3">                            
                                    <form:select path="prVillageCode" class="form-control" id="prVillageCode">
                                        <option value="">SELECT VILLAGE </option>
                                        <form:options items="${prVillageList}" itemValue="villageCode" itemLabel="villageName"/>   

                                    </form:select> 
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-sm-2" for="email">Pin &nbsp;<span style="color: red">*</span>:</label>
                                <div class="col-sm-3">
                                    <form:input path="prPin" class="form-control" maxlength="6" id="prPin"/>                            
                                </div>                    
                                <label class="control-label col-sm-1" for="email">Telephone :</label>
                                <div class="col-sm-1">
                                    <form:input path="prStdCode" class="form-control" maxlength="10" id="prStdCode"/>
                                </div>
                                <div class="col-sm-2">
                                    <form:input path="prTelephone" class="form-control" id="prTelephone"/>
                                </div>
                            </div>
                        </div
                        <div class="panel-footer">

                            <c:if test="${not empty empprofilecompletedstatus.dateOfProfileCompletion}">
                                <span style="display:block;text-align: center;font-weight: bold;font-size: 14px;color: red;">Profile completed on <c:out value="${empprofilecompletedstatus.dateOfProfileCompletion}"/> from IP <c:out value="${empprofilecompletedstatus.ipOfProfileCompletion}"/></span>
                            </c:if>
                            <c:if test="${empty empprofilecompletedstatus.dateOfProfileCompletion}">
                                <input type="submit" class="btn btn-success" name="action" value="Save" onclick="return validate()"/>

                                <c:if test="${address.addressId ne null}">
                                    <input type="submit" class="btn btn-success" name="action" value="Delete"/> 
                                </c:if>
                                <input type="submit" class="btn btn-success" name="action" value="Cancel"/>
                            </c:if>
                        </div>
                    </div>
                </form:form>
            </div>
        </div>

    </body>
</html>

