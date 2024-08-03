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
                $.ajax({
                    method: "POST",
                    url: "getStateWiseDistrictList.htm",
                    data: {stateCode: $("#stateCode").val()}
                }).done(function(data) {
                    $.each(data, function(key, obj) {
                        $('#distCode').append(new Option(obj.distName, obj.distCode));
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
                //alert(address);
                var stateCode = $("#stateCode").val();
                var distCode = $("#distCode").val();
                var blockCode = $("#blockCode").val();
                var pinText = $("#pin").val();
                if (addressType == "") {
                    alert("Please select Address Type");
                    return false;
                }
                if (addressfield == "") {
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
            }
        </script>
    </head>
    <body>
        <div id="boxes">
            <form:form action="LocateEmployeeSaveAddress.htm" method="POST" class="form-horizontal" commandName="address">
                <div style=" margin-bottom: 5px;" class="panel panel-info">
                    <div class="panel-header"></div>
                    <div class="panel-body">
                        <div class="form-group">
                            <label class="control-label col-sm-2" for="addressType">Address Type:</label>
                            <div class="col-sm-4">
                                <form:hidden path="empId"/>
                                <form:hidden path="addressId"/>
                                <form:select path="addressType" class="form-control" id="addressType" >
                                    <form:option value="">SELECT ADDRESS TYPE</form:option>
                                    <form:option value="PERMANENT">PERMANENT</form:option>
                                    <form:option value="PRESENT">PRESENT</form:option>
                                </form:select>                            
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-2" for="addressfield">Address:</label>
                            <div class="col-sm-4">
                                <form:textarea path="address" class="form-control" id="addressfield"/>                            
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-2" for="stateCode">State:</label>
                            <div class="col-sm-3">
                                <form:select path="stateCode" class="form-control" id="stateCode">
                                    <form:options items="${stateList}" itemValue="statecode" itemLabel="statename"/>
                                </form:select>                            
                            </div>                    
                            <label class="control-label col-sm-1" for="distCode">District:</label>
                            <div class="col-sm-3">
                                <form:select path="distCode" class="form-control" id="distCode">
                                    <option value="">SELECT DISTRICT</option>
                                    <form:options items="${districtList}" itemValue="distCode" itemLabel="distName"/>
                                </form:select>                            
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-2" for="blockCode">Block:</label>
                            <div class="col-sm-3">                            
                                <form:select path="blockCode" class="form-control" id="blockCode">
                                    <option value="">SELECT BLOCK</option>
                                    <form:options items="${blockList}" itemValue="blockCode" itemLabel="blockName"/>
                                </form:select> 
                            </div>                    
                            <label class="control-label col-sm-1" for="psCode">Police Station:</label>
                            <div class="col-sm-3">                            
                                <form:select path="psCode" class="form-control" id="psCode">
                                    <option value="">SELECT POLICE STATION</option>
                                    <form:options items="${policeList}" itemValue="psCode" itemLabel="psName"/>
                                </form:select> 
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-2" for="postCode">Post Office:</label>
                            <div class="col-sm-3">                            
                                <form:select path="postCode" class="form-control" id="postCode">
                                    <form:option value="">SELECT POST OFFICE</form:option>
                                    <form:options items="${postofficeList}" itemValue="postOfficeCode" itemLabel="postOfficeName"/>    
                                </form:select> 
                            </div>                    
                            <label class="control-label col-sm-1" for="villageCode">Village:</label>
                            <div class="col-sm-3">                            
                                <form:select path="villageCode" class="form-control" id="villageCode">
                                    <form:option value="">SELECT VILLAGE</form:option>
                                    <form:options items="${villageList}" itemValue="villageCode" itemLabel="villageName"/>   
                                </form:select> 
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-2" for="pin">Pin:</label>
                            <div class="col-sm-3">
                                <form:input path="pin" class="form-control" id="pin"/>                            
                            </div>                    
                            <label class="control-label col-sm-1" for="stdCode">Telephone:</label>
                            <div class="col-sm-1">
                                <form:input path="stdCode" class="form-control" maxlength="6"/>
                            </div>
                            <div class="col-sm-2">
                                <form:input path="telephone" class="form-control"/>
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
        </div>
        <table class="table table-bordered">
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
                            <a href="LocateEmployeeEditAddress.htm?addressId=${address.addressId}&empId=${address.empId}"><span class="glyphicon glyphicon-pencil"></span>Edit</a>
                            &nbsp;
                            <a href=#" onclick="delete_Employee_address(${address.addressId})" class="btn btn-default"><span class="glyphicon glyphicon-remove"></span> Delete</a>
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
