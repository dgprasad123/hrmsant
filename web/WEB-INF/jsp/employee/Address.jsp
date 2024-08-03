<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
    <head>
        <title>Employee Profile</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">      
        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">
        <link rel="stylesheet" type="text/css" href="resources/css/colorbox.css"/>
        <link rel="stylesheet" type="text/css" href="css/popupmain.css"/>  
        <script type="text/javascript">
            function ViewMessage() {
                $('#mask').hide();
                $('.window').hide();
                //   window.location="viewCommunicationDetails.htm";
            }
            function showInformationWindow()
            {
                var id = '#dialog';

                //Get the screen height and width
                var maskHeight = $(document).height();
                var maskWidth = $(window).width();

                //Set heigth and width to mask to fill up the whole screen
                $('#mask').css({'width': maskWidth, 'height': maskHeight});

                //transition effect		
                $('#mask').fadeIn(500);
                $('#mask').fadeTo("slow", 0.9);

                //Get the window height and width
                var winH = $(window).height() - 100;
                var winW = $(window).width();

                //Set the popup window to center
                $(id).css('top', winH / 2 - $(id).height() / 2);
                $(id).css('left', winW / 2 - $(id).width() / 2);

                $(id).fadeIn(2000);
            }
            $(document).ready(function() {

                $('#mask').click(function() {
                    ViewMessage();
                });
            });
        </script>      
        <style type="text/css">
            #profile_container{width:95%;margin:0px auto;border-top:0px;}
            ol li{width:180px;float:left;text-align:left;}
        </style>  
        <script type="text/javascript">
            $(document).ready(function() {
                //$("#prstateCode").val("21");
                $.ajax({
                    method: "POST",
                    url: "getStateWiseDistrictList.htm",
                    data: {stateCode: $("#stateCode").val()}
                }).done(function(data) {
                    $.each(data, function(key, obj) {

                        $('#distCode').append(new Option(obj.distName, obj.distCode));
                        $('#prDistCode').append(new Option(obj.distName, obj.distCode));
                    });
                });

                $("#stateCode").bind("change", function() {
                    $.ajax({
                        method: "POST",
                        url: "getStateWiseDistrictList.htm",
                        data: {stateCode: $(this).val()}
                    }).done(function(data) {
                        $('#distCode').empty();
                        $('#distCode').append(new Option('Select District', ''));
                        $.each(data, function(key, obj) {
                            $('#distCode').append(new Option(obj.distName, obj.distCode));
                        });
                    });
                });





                $("#distCode").bind("change", function() {
                    $.ajax({
                        method: "POST",
                        url: "getDistrictWiseBlockList.htm",
                        data: {distcode: $(this).val()}
                    }).done(function(data) {
                        $('#blockCode').empty();
                        $('#blockCode').append(new Option('Select Block', ''));
                        $.each(data, function(key, obj) {
                            $('#blockCode').append(new Option(obj.blockName, obj.blockCode));
                        });
                    });
                    $.ajax({
                        method: "POST",
                        url: "getDistrictWisePSList.htm",
                        data: {distcode: $(this).val()}
                    }).done(function(data) {
                        $('#psCode').empty();
                        $('#psCode').append(new Option('Select Police Station', ''));
                        $.each(data, function(key, obj) {
                            $('#psCode').append(new Option(obj.psName, obj.psCode));
                        });
                    });

                    $.ajax({
                        method: "POST",
                        url: "getDistrictWisePOList.htm",
                        data: {distcode: $(this).val()}
                    }).done(function(data) {
                        $('#postCode').empty();
                        $('#postCode').append(new Option('Select Post Office', ''));
                        $.each(data, function(key, obj) {
                            $('#postCode').append(new Option(obj.postOfficeName, obj.postOfficeCode));
                        });
                    });

                });
                $("#psCode").bind("change", function() {
                    $.ajax({
                        method: "POST",
                        url: "getDistrictWisePsWiseVillageList.htm",
                        data: {distcode: $("#distCode").val(), pscode: $(this).val()}
                    }).done(function(data) {
                        $('#villageCode').empty();
                        $('#villageCode').append(new Option('Select Village Name', ''));
                        $.each(data, function(key, obj) {
                            $('#villageCode').append(new Option(obj.villageName, obj.villageCode));
                        });
                    });
                });
            });
            function validate() {
                var addressType = $("#addressType").val();
                var addressfield = $("#addressfield").val();
                var stateCode = $("#stateCode").val();
                var distCode = $("#distCode").val();
                var blockCode = $("#blockCode").val();
                var psCode = $("#psCode").val();
                var psCode = $("#psCode").val();
                var postCode = $("#postCode").val();
                var pinText = $("#pin").val();
                if (addressType == "") {
                    alert("Please select Address Type");
                    return false;
                }
                if (addressfield == "") {
                    alert("Permanent Address cannot be blank");
                    return false;
                }
                if (addressfield.length < 20)
                {
                    alert("Permanent Address must have at least 20 characters.")
                    return false;
                }

                if (stateCode == "") {
                    alert("Please select Permanent State");
                    return false;
                }
                if (distCode == "") {
                    alert("Please select Permanent District");
                    return false;
                }
                if (blockCode == "") {
                    alert("Please select Permanent Block");
                    return false;
                }
                if (psCode == "") {
                    alert("Please select Police Permanent Station");
                    return false;
                }
                if (postCode == "") {
                    alert("Please select Post Permanent office");
                    return false;
                }
                if (pinText == "") {
                    alert("Permanent pin cannot be blank");
                    return false;
                }
                if (pinText.length < 6) {
                    alert("Permanent pincode must have 6 characters");
                    return false;
                }
                if (isNaN(pinText)) {
                    alert("Invalid Permanent Pin Number");
                    return false;
                }
                var prAddress = $("#prAddress").val();
                var prStateCode = $("#prStateCode").val();
                var prDistCode = $("#prDistCode").val();
                var prBlockCode = $("#prBlockCode").val();
                var prPsCode = $("#prPsCode").val();
                var prPostCode = $("#prPostCode").val();
                var prPin = $("#prPin").val();
                if (prAddress == "") {
                    alert("Permanent Address cannot be blank");
                    return false;
                }
                if (prAddress.length < 20)
                {
                    alert("Permanent Address must have at least 20 characters.")
                    return false;
                }

                if (prStateCode == "") {
                    alert("Please select Present State");
                    return false;
                }
                if (prDistCode == "") {
                    alert("Please select Present District");
                    return false;
                }
                if (prBlockCode == "") {
                    alert("Please select Present Block");
                    return false;
                }
                if (prPsCode == "") {
                    alert("Please select Police Present Station");
                    return false;
                }
                if (prPostCode == "") {
                    alert("Please select Post Present office");
                    return false;
                }
                if (prPin == "") {
                    alert("Present pin cannot be blank");
                    return false;
                }
                if (prPin.length < 6) {
                    alert("Present pincode must have 6 characters");
                    return false;
                }
                if (isNaN(prPin)) {
                    alert("Invalid Present Pin Number");
                    return false;
                }

                //    return false;

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
                }).done(function(data) {
                    $('#prDistCode').empty();
                    $('#prDistCode').append(new Option('SELECT DISTRICT', ''));
                    $.each(data, function(key, obj) {
                        $('#prDistCode').append(new Option(obj.distName, obj.distCode));
                    });
                });
            }
            function changeprdistrict(vals) {
                $.ajax({
                    method: "POST",
                    url: "getDistrictWiseBlockList.htm",
                    data: {distcode: vals}
                }).done(function(data) {
                    $('#prBlockCode').empty();
                    $('#prBlockCode').append(new Option('SELECT BLOCK ', ''));
                    $.each(data, function(key, obj) {
                        $('#prBlockCode').append(new Option(obj.blockName, obj.blockCode));
                    });
                });
                $.ajax({
                    method: "POST",
                    url: "getDistrictWisePSList.htm",
                    data: {distcode: vals}
                }).done(function(data) {
                    $('#prPsCode').empty();
                    $('#prPsCode').append(new Option('Select Police Station', ''));
                    $.each(data, function(key, obj) {
                        $('#prPsCode').append(new Option(obj.psName, obj.psCode));
                    });
                });

                $.ajax({
                    method: "POST",
                    url: "getDistrictWisePOList.htm",
                    data: {distcode: vals}
                }).done(function(data) {
                    $('#prPostCode').empty();
                    $('#prPostCode').append(new Option('Select Post Office', ''));
                    $.each(data, function(key, obj) {
                        $('#prPostCode').append(new Option(obj.postOfficeName, obj.postOfficeCode));
                    });
                });

            }
            function changePrPoliceStation(vals) {
                $.ajax({
                    method: "POST",
                    url: "getDistrictWisePsWiseVillageList.htm",
                    data: {distcode: $("#prDistCode").val(), pscode: vals}
                }).done(function(data) {
                    $('#prVillageCode').empty();
                    $('#prVillageCode').append(new Option('Select Village Name', ''));
                    $.each(data, function(key, obj) {
                        $('#prVillageCode').append(new Option(obj.villageName, obj.villageCode));
                    });
                });

            }
            function checkAddress(contentId, msgId)
            {
                var area = document.getElementById(contentId);
                var message = document.getElementById(msgId);
                var maxLength = 20;
                if (area.value.length < maxLength) {
                    message.style.color = '#FF0000';
                    message.innerHTML = '<img src="images/cross-icon.png" width="15" /> ' + (maxLength - area.value.length) + " characters remaining";

                }
                else
                {
                    message.innerHTML = '<img src="images/tick-icon.png" width="15" /> ' + (area.value.length) + " characters";
                    message.style.color = '#008900';
                }

            }

        </script>
    </head>
    <body>
        
        <div id="boxes">
            <div style=" left: 551.5px; display: none;" id="dialog" class="window"> 

                <table class="table-bordered" align='center'>
                    <tr>
                        <td align="center"><h2 class="alert alert-info" style="color:#FF0000;font-size:14pt;font-weight:bold;margin:0px;">
                                You have to submit the following details in order to complete your profile:</h2>
                            <span style="font-size:12pt;font-weight:bold;display:block;text-align:left;color:#008900;">Personal Information</span>
                            <ol style="margin:0px;font-size:10pt;">
                                <li>Employee Name</li>
                                <li>ACCT TYPE</li>
                                <li>GPF NO</li>
                                <li>FIRST NAME</li>
                                <li>LAST NAME</li>
                                <li>GENDER</li>
                                <li>MARITAL STATUS</li>
                                <li>CATEGORY</li>
                                <li>HEIGHT</li>
                                <li>DOB</li>
                                <li>DOS</li>
                                <li>JOIN DATE OF GOO</li>
                                <li>DATE OF ENTRY INTO GOVERNMENT</li>
                                <li>BLOOD GROUP</li>
                                <li>RELIGION</li>
                                <li>MOBILE</li>
                                <li>POST GROUP</li>
                                <li>HOME TOWN</li>
                                <li>DOMICILE</li>



                            </ol>
                            <div style="clear:both;"></div>
                            <span style="font-size:12pt;font-weight:bold;display:block;text-align:left;color:#008900;">Language</span>
                            <ol style="margin:0px;font-size:10pt;">
                                <li style="width:50%">LANGUAGE AT LEAST ONE</li>
                            </ol>
                            <div style="clear:both;"></div>
                            <span style="font-size:12pt;font-weight:bold;display:block;text-align:left;color:#008900;">Identity</span>
                            <ol style="margin:0px;font-size:10pt;">
                                <li>PAN NUMBER</li>
                                <li>AADHAR NUMBER</li>
                            </ol>
                            <div style="clear:both;"></div>
                            <span style="font-size:12pt;font-weight:bold;display:block;text-align:left;color:#008900;">Address</span>
                            <ol style="margin:0px;font-size:10pt;">
                                <li style="width:50%">ADDRESS BOTH PERMANENT AND PRESENT</li>
                            </ol>


                            <div style="clear:both;"></div>
                            <span style="font-size:12pt;font-weight:bold;display:block;text-align:left;color:#008900;">Family</span>
                            <ol style="margin:0px;font-size:10pt;">
                                <li style="width:50%">FATHERS NAME/HUSBAND NAME</li>
                                <li style="width:50%">NOMINEE AT LEAST ONE</li>
                                <li style="width:50%">IDENTITY NUMBER</li>
                                <li style="width:50%">IDENTITY TYPE</li>
                            </ol>
                            <div style="clear:both;"></div>
                            <span style="font-size:12pt;font-weight:bold;display:block;text-align:left;color:#008900;">Education</span>
                            <ol style="margin:0px;font-size:10pt;">
                                <li style="width:50%">QUALIFICATION AT LEAST ONE</li>
                            </ol>
                        </td>
                    </tr>

                </table>            
                <div id="popupfoot" style="text-align:center;"> 
                    <a href="javascript:void(0)" onclick="javascript: ViewMessage();" class="close agree btn pri" style="background:#FF0000;color:#FFFFFF;">Close</a> 
                </div>            

            </div>  
        </div>
        <form:form action="saveAddress.htm" method="post" class="form-horizontal" commandName="address">
            <div style=" margin-bottom: 5px;" class="panel panel-info">
                <div class="">
                    <div class="form-group">
                        <label class="control-label col-sm-2" for="email">&nbsp;</label>
                        <div class="col-sm-4">
                            <input type="hidden" name="empId" value="${empId}"/>
                            <!-- <form:hidden path="empId"/>-->
                            <form:hidden path="addressId"/>

                            <form:hidden path="addressType" value="PERMANENT" />
                            <div class="alert alert-info" style="font-weight:bold;"> PERMANENT ADDRESS</div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-sm-2" for="email">House No/Flat No/Plot No/Block &nbsp;<span style="color: red">*</span>:</label>
                        <div class="col-sm-4">
                            <form:textarea path="address" class="form-control" id="addressfield" onkeyup="javascript: checkAddress('addressfield', 'tmessage1')"
                                           onkeydown="javascript: checkAddress('addressfield', 'tmessage1')" />                            
                            <br />                          
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
                            <div class="alert alert-success" style="font-weight:bold;"> PRESENT ADDRESS</div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-sm-2" for="email">House No/Flat No/Plot No/Block &nbsp;<span style="color: red">*</span> :</label>
                        <div class="col-sm-4">
                            <form:textarea path="prAddress" class="form-control" id="prAddress" onkeyup="javascript: checkAddress('prAddress', 'tmessage')"
                                           onkeydown="javascript: checkAddress('prAddress', 'tmessage')" />                            
                            <br />
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-sm-2" for="email">State &nbsp;<span style="color: red">*</span>:</label>
                        <div class="col-sm-3">
                            <form:select path="prStateCode" class="form-control" id="prStateCode" onchange="changeprState(this.value);">
                                <option value="21">ODISHA</option>
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
                                <form:options items="${prBlockList}" itemValue="blockCode" itemLabel="blockName"/>
                                <option value="">SELECT  BLOCK </option>
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
                                <form:options items="${prPostofficeList}" itemValue="postOfficeCode" itemLabel="postOfficeName"/>    
                                <option value="">SELECT  POST OFFICE </option>

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
                </div>
                <div class="panel-footer">
                    <input type="submit" class="btn btn-success" name="action" value="Save" onclick="return validate()"/>
                   
                    <c:if test="${noc eq address.addressId}">
                        <input type="submit" class="btn btn-success" name="action" value="Delete"/>                        
                        <input type="submit" class="btn btn-success" name="action" value="Cancel"/>
                    </c:if>                    
                </div>

            </div>
        </form:form> 
        <table class="table table-bordered" style="display:none;">
            <thead>
                <tr class="bg-primary text-white">
                    <th>#</th>
                    <th>Address Type</th>
                    <th>Address</th>                    
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${empAddress}" var="address" varStatus="cnt">
                    <tr>
                        <th scope="row">${cnt.index+1}</th>
                        <td>${address.addressType}</td>
                        <td>${address.address}</td>                        
                        <td>
                            <c:if test="${address.isLocked eq 'N'}">
                                <a href="editAddress.htm?addressId=${address.addressId}"><span class="glyphicon glyphicon-pencil"></span>Edit</a>
                                &nbsp;
                                <a href=#" onclick="delete_Employee_address(${address.addressId})" class="btn btn-default"><span class="glyphicon glyphicon-remove"></span> Delete</a>
                            </c:if>
                            <c:if test="${address.isLocked eq 'Y'}">
                                <img src="images/Lock.png" width="20" height="20"/>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table> 
    </body>
    <script type="text/javascript">

        function delete_Employee_address(ids) {
            var conf = confirm("Do you want to confirm to delete this address");
            if (conf) {

                window.location = "deleteEmployeeAddress.htm?addressId=" + ids;
            }
        }
    </script> 
</html>
