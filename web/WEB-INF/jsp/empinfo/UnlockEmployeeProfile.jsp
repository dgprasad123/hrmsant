<%-- 
    Document   : UnlockEmployeeProfile
    Created on : 23 Aug, 2022, 12:59:11 PM
    Author     : Devikrushna
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        <link href="css/sb-admin.css" rel="stylesheet">
        <link rel="stylesheet" href="css/chosen.css">

        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>
        <script src="js/chosen.jquery.min.js"></script>
        <script type="text/javascript">
            function unlockEmpProfile() {
                var checkBoxArray = "";              
                $("input[name='chkUnlock']:checked").each(function() {
                    if (checkBoxArray == "") {
                        checkBoxArray = $(this).val();
                    } else {
                        checkBoxArray = checkBoxArray + "," + $(this).val();
                    }
                });           
                if (checkBoxArray == "") {
                    alert("Please Select Employee Profile Details");
                } else {  
                  
                    if (confirm("Are you sure to unlock the profile ?") == true) {
                         var url = 'unlockEmpprofileGroup.htm?checkedEmpProfile=' + checkBoxArray + '&empId=' + $('#hidempId').val();                 
                    $.ajax({
                        url: url,
                        dataType: "json",
                        success: function(data) {                           
                            if (data.status != '') {
                                $("#msg").addClass("alert-success");
                                $("#msg").text(data.status + " Data Unlocked");
                                $('#unlockbutton').hide();                              
                                $('#showButton').show();
                            } else {
                                $("#msg").addClass("alert-danger");
                                $("#msg").text("NO DATA FOUND..");
                            }
                        }
                    });
                      } else {
                        text = "You canceled!";
                      }
                }
            }
            $(document).ready(function() {
                                
              empid = $("#hidempId").val();   
                
               var url = 'getCheckedEmpIdentityProfile.htm?empId=' + empid;                 
                    $.ajax({
                        url: url,
                        dataType: "json",
                        success: function(data) {                                                                                        
                            if (data.msg == 'Y') {                             
                                $("#chkIdentity").prop("disabled", true);
                                 $("#empmsg").text(" Disabled data already unlocked.");
                            }else {                       
                                $("#chkIdentity").prop("disabled", false);                               
                            }
                             }
                    }); 
                    var url = 'getCheckedEmpLanguageProfile.htm?empId=' + empid;                 
                    $.ajax({
                        url: url,
                        dataType: "json",
                        success: function(data) {                                                                                    
                            if (data.msg == 'Y') {                           
                                $("#chkLanguage").prop("disabled", true);
                                 $("#empmsg").text(" Disabled data already unlocked.");
                            }else {                              
                                $("#chkLanguage").prop("disabled", false);         
                            }
                             }
                    }); 
                    
                     var url = 'getCheckedEmpQualificationProfile.htm?empId=' + empid;                 
                    $.ajax({
                        url: url,
                        dataType: "json",
                        success: function(data) {                                                                                   
                            if (data.msg == 'Y') {                               
                                $("#chkQualification").prop("disabled", true);
                                 $("#empmsg").text(" Disabled data already unlocked.");
                            }else {                               
                                $("#chkQualification").prop("disabled", false);    
                            }
                             }
                    }); 
                    
                     var url = 'getCheckedEmpFamilyProfile.htm?empId=' + empid;                 
                    $.ajax({
                        url: url,
                        dataType: "json",
                        success: function(data) {                                                                                      
                            if (data.msg == 'Y') {                              
                                $("#chkFamily").prop("disabled", true);
                                 $("#empmsg").text(" Disabled data already unlocked.");
                            }else {                               
                                $("#chkFamily").prop("disabled", false);
                                
                            }
                             }
                    }); 
                    
                    var url = 'getCheckedEmpAddressProfile.htm?empId=' + empid;                 
                    $.ajax({
                        url: url,
                        dataType: "json",
                        success: function(data) {                                                                                      
                            if (data.msg == 'Y') {                              
                               $("#chkEmpAddress").prop("disabled", true);
                                 $("#empmsg").text(" Disabled data already unlocked.");
                            }else {                            
                                $("#chkEmpAddress").prop("disabled", false);
                                
                            }
                             }
                    }); 
                          
                       
                
                $('#chkAllEmpProfile').click(function() {
                    $('#chkIdentity').prop('checked', this.checked);
                    $('#chkLanguage').prop('checked', this.checked);
                    $('#chkQualification').prop('checked', this.checked);
                    $('#chkFamily').prop('checked', this.checked);
                    $('#chkEmpAddress').prop('checked', this.checked);                                
                })
            });



        </script>
        <style>
            .leble-text{
                color: #800909;
                font-size: 15px;
            }
            .name-div{
                margin-bottom: 20px;
                text-align:center;
                font-weight: bold;
            }
            .heading-th{
                text-align: center;
                font-size: 17px;
            }
            .checkbox-height{
                height: 22px;
            }
            .select-chk{
                height: 24px;
            }
        </style>
    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/hrmsadminmenu.jsp"/>        
            <div id="page-wrapper">
                <div class="container-fluid">
                    <form:form action="UnlockEmployeeProfile.htm" method="POST" commandName="unlockempprofile">
                        <input type="hidden" id="hidempId" value="${empid}"/>
                        <div>
                            <div class="table-responsive">
                                <h3>Unlock Employee Profile</h3>
                                <div class="row" >
                                    <div class="col-lg-12 name-div">
                                        <h3>HRMS ID - <c:out value="${empid}"/> - <c:out value="${empprofile.empName}"/></h3>                                
                                    </div>
                                </div>
                                <table class="table table-bordered table-hover table-striped">
                                    <thead>
                                        <tr style="background-color:lightblue">
                                            <th class="heading-th ">Select All<input type="checkbox" name="chkAll" id="chkAllEmpProfile" value="chkAllEmpProfile"  class="form-control select-chk" /></th>
                                            <th style="font-size: 17px;vertical-align: text-top">Employee Profile Details</th>                                       
                                        </tr>
                                    </thead>

                                    <tbody id="abstractList">
                                        <tr>                                                                                    
                                            <td>                                             
                                                <input type="checkbox" name="chkUnlock" id="chkIdentity" value="emp_id_doc~Identity" class="form-control checkbox-height" />                                              
                                            </td>             
                                            <td>
                                                <label class="leble-text">Identity Data </label><BR/>                                            
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>   
                                                <input type="checkbox" name="chkUnlock" id="chkLanguage" value="emp_language~Language" class="form-control checkbox-height" />                                              
                                                </td>
                                            <td>
                                                <label class="leble-text">Language Data</label><br>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>                
                                                    <input type="checkbox" name="chkUnlock" id="chkQualification" value="emp_qualification~Qualification" class="form-control checkbox-height"  />                                      
                                            </td>
                                            <td>
                                                <label class="leble-text">Qualification Data</label><br>
                                            </td>
                                        </tr>
                                       
                                        <tr>
                                            <td>                                  
                                                    <input type="checkbox" name="chkUnlock" id="chkFamily" value="emp_relation~Family"  class="form-control checkbox-height" />                              
                                            </td>
                                            <td>
                                                <label class="leble-text">Family Data</label><br>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>             
                                                <input type="checkbox" name="chkUnlock" id="chkEmpAddress" value="emp_address~Address"   class="form-control checkbox-height" />                                              
                                                </td>
                                            <td>
                                                <label class="leble-text">Employee Address Data</label><br>
                                            </td>
                                        </tr>                                   
                                    </tbody>
                                </table>
                                    
                                <table class="table table-bordered table-hover table-striped">
                                    <input type="button" name="btnGroupWisePrivilegeBtn" id="unlockbutton" value="UnLock" class="btn btn-success" onclick="unlockEmpProfile();"/>&nbsp;                     
                                    <button type="button" id="showButton" class="btn btn-success" style="display:none" disabled="true" >Unlock</button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                    <span id="msg" style="font-size: 16px; text-align:center"></span> 
                                    <span id="empmsg" style="font-size: 16px; text-align:center;color: #8e4646;font-weight: bold;"></span> 
                                </table>
                            </div>
                        </div>
                    </form:form>
                </div>
            </div>
        </div>
    </body>
</html>
