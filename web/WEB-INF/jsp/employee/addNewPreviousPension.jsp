<%-- 
    Document   : addNewPreviousPension
    Created on : 28 Sep, 2022, 3:56:17 PM
    Author     : Devikrushna
--%>

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
        <link rel="stylesheet" href="css/chosen.css">

        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script type="text/javascript" src="js/chosen.jquery.min.js"></script>
        <script type="text/javascript">
             $(document).ready(function () {
                $('#PensionEffectiveDate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
                            
            });       
            function getBranchList(me) {

                $('option', $('#branchName')).not(':eq(0)').remove();
                $.ajax({
                    type: "POST",
                    url: "bankbranchlistJSON.htm?bankcode=" + $(me).val(),
                    success: function(data) {
                        $.each(data, function(i, obj)
                        {
                            $('#branchName').append($('<option>', {
                                value: obj.branchcode,
                                text: obj.branchname
                            }));

                        });
                    }});
            }
            
             function saveCheck() {          
                if ($('#pensionType').val() == '') {
                    alert('Please select Pension Type.');
                    $('#pensionType').focus();
                    return false;
                }

                if ($('#pensionAmount').val() == '') {
                    alert('Please enter Pension Amount.');
                    $('#pensionAmount').focus();
                    return false;
                }

                if ($('#payableTreasury').val() == '') {
                    alert('Please select Payable Treasury.');
                    $('#payableTreasury').focus();
                    return false;
                }

                if ($('#bankName').val() == '') {
                    alert('Please select Bank  Name.');
                    $('#bankName').focus();
                    return false;
                }
                 if ($('#branchName').val() == '') {
                    alert('Please select Branch  Name.');
                    $('#branchName').focus();
                    return false;
                }
                if ($('#PensionIssuingAuth').val() == '') {
                    alert('Please enter Pension Issuing Authority.');
                    $('#PensionIssuingAuth').focus();
                    return false;
                }
                if ($('#source').val() == '') {
                    alert('Please Select source.');
                    $('#source').focus();
                    return false;
                }
                if ($('#PPOFPPONo').val() == '') {
                    alert('Please enter PPO/FPPO No.');
                    $('#PPOFPPONo').focus();
                    return false;
                }
                
                if ($('#PensionEffectiveDate').val() == '') {
                    alert('Please Select Pension Effective From Date.');
                    $('#PensionEffectiveDate').focus();
                    return false;
                }
        
            }          
        </script>
        <style>
            .row-margin{
                margin-bottom: 20px;
            }
        </style>
        
    </head>
    <body>
        <form:form action="saveprevPension.htm" method="post" commandName="prevpensionForm">
            
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                       Add New Previous Pension Details
                    </div>
                    <div class="panel-body">
                         <form:hidden path="empid" id="empid"/>
                           <form:hidden id='pensionId'  path='pensionId'/>
                        <div class="row row-margin">
                            <div class="col-lg-2">
                                <label for="type">Pension Type <span style="color: red">*</span></label>
                            </div>
                            <div class="col-sm-3">
                                    <form:select class="form-control" path="pensionType" id="pensionType">
                                        <form:option value="" label="--Select Pension Type--"/>
                                        <form:option value="F"> Family Pension </form:option>
                                        <form:option value="N"> Normal Pension </form:option> 
                                    </form:select> 
                             </div>
                        </div>   
                            
                        <div class="row row-margin">
                            <div class="col-lg-2">
                                <label for="PensionAmount">Pension Amount <span style="color: red">*</span></label>
                            </div>
                            <div class="col-sm-3">
                                <form:input cssClass="form-control" path="pensionAmount" id="pensionAmount" maxlength="6" placeholder="Enter Pension Amount" />
                             </div>
                        </div>  
                        <div class="row row-margin" >
                            <div class="col-lg-2">
                                <label for="paytr">Payable Treasury <span style="color: red">*</span></label>
                            </div>
                            <div class="col-sm-3">
                                  <form:select class="form-control" path="payableTreasury" id="payableTreasury">
                                        <form:option value="" label="--Select Payable Treasury--"/>
                                        <form:options items="${treasurylist}" itemValue="trCode" itemLabel="trName"/>  
                                    </form:select> 
                             </div>
                        </div>        
                         <div class="row row-margin" >
                            <div class="col-lg-2">
                                <label for="bank">Bank Name <span style="color: red">*</span></label>
                            </div>
                            <div class="col-sm-3">
                                  <form:select class="form-control" path="bankName" id="bankName" onchange="getBranchList(this);">
                                        <form:option value="" label="--Select Bank Name--"/>
                                        <form:options items="${bankList}" itemLabel="bankname" itemValue="bankcode"/>  
                                    </form:select> 
                             </div>
                        </div>   
                             
                        <div class="row row-margin" >
                            <div class="col-lg-2">
                                <label for="paytr">Branch Name <span style="color: red">*</span></label>
                            </div>
                            <div class="col-sm-3">
                                  <form:select class="form-control" path="branchName" id="branchName">
                                      <form:option value="" label="--Select Branch Name--"/>
                                        <form:options items="${branchList}" itemLabel="branchname" itemValue="branchcode"/>      
                                    </form:select> 
                             </div>
                        </div>   
                         <div class="row row-margin">
                            <div class="col-lg-2">
                                <label for="Pensionauth">Pension Issuing Authority <span style="color: red">*</span></label>
                            </div>
                            <div class="col-sm-3">
                                    <form:input cssClass="form-control" path="PensionIssuingAuth" id="PensionIssuingAuth" maxlength="100" placeholder="Enter Issuing Authority"/>
                             </div>
                        </div>     
                             
                        <div class="row row-margin">
                            <div class="col-lg-2">
                                <label for="source">Source <span style="color: red">*</span></label>
                            </div>
                            <div class="col-sm-3">
                                    <form:select class="form-control" path="source" id="source">
                                        <form:option value="" label="--Select Source--"/>
                                        <form:option value="C"> Civil </form:option>
                                        <form:option value="M"> Military </form:option> 
                                        <form:option value="O"> Other </form:option> 
                                    </form:select> 
                             </div>
                        </div>       
                             
                         
                             
                         <div class="row row-margin">
                            <div class="col-lg-2">
                                <label for="PensionAmount">PPO/FPPO No <span style="color: red">*</span></label>
                            </div>
                            <div class="col-sm-3">
                                    <form:input cssClass="form-control" path="PPOFPPONo" id="PPOFPPONo" maxlength="20" placeholder="Enter PPO/FPPO No" />
                             </div>
                        </div>      
                             
                         <div class="row row-margin">
                            <div class="col-lg-2">
                                <label for="effectdate">Pension Effective From Date <span style="color: red">*</span></label>
                            </div>
                            <div class="col-sm-3">
                                   <div class="input-group date">
                                    <form:input class="form-control" path="PensionEffectiveDate" id="PensionEffectiveDate" readonly="true" placeholder="Choose Effective Date" />
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>    
                             </div>
                        </div>                 
                    </div>
                                    
                  <c:if test="${empty prevpensionForm.pensionId}">              
                    <div class="panel-footer">
                             <button type="submit" class="btn btn-primary" name="action" value="Save PrevPension" onclick="return saveCheck()">Save</button>  
                              <a href="PreviousPensionDetails.htm"> <button type="button" class="btn btn-warning btn-md">&laquo;Back</button></a>
                    </div>
                    </c:if>   
                    
                    <c:if test="${not empty prevpensionForm.pensionId}">
                        <div class="panel-footer">
                             <button type="submit" class="btn btn-primary" name="action" value="Save PrevPension" onclick="return saveCheck()">Update</button>  
                              <a href="PreviousPensionDetails.htm"> <button type="button" class="btn btn-warning btn-md">&laquo;Back</button></a>
                        </div>
                        
                    </c:if>                                                  
                </div>
            </div>
        </form:form>
    </body>
</html>
