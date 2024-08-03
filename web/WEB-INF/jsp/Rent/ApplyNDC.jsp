<%-- 
    Document   : ApplyNDC
    Created on : 1 Sep, 2020, 11:04:55 AM
    Author     : Manoj PC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Apply Quarter NDC</title>
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
            function getDeptWiseOfficeList() {
                var deptcode = $('#sltDept').val();

                $('#sltOffice').empty();
                $('#sltPost').empty();

                var url = 'getOfficeListJSON.htm?deptcode=' + deptcode;

                $('#sltOffice').append('<option value="">--Select Office--</option>');
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#sltOffice').append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');
                    });
                });
            }

            function getOfficeWisePostList(type) {
                var offcode = $('#sltOffice').val();
                $('#sltPost').empty();

                var url = 'getSanctioningSPCOfficeWiseListJSON.htm?offcode=' + offcode;
                $('#sltPost').append('<option value="">--Select Post--</option>');
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#sltPost').append('<option value="' + obj.spc + '">' + obj.postname + '</option>');
                    });
                });
            }

            function getPost() {
                $('#pensionSanctioningAuthority').val($('#sltPost option:selected').text());
            }
            function checkTwinCity(isChecked)
            {
                $('#twin_city_blk').css('display', 'none');
                if(isChecked == 'Y')
                {
                    $('#twin_city_blk').css('display', 'block');
                }
            }
            function checkOccupy(isChecked)
            {
                $('#has_occupied_blk').css('display', 'none');
                if(isChecked == 'Y')
                {
                    $('#has_occupied_blk').css('display', 'block');
                }
            }
            function checkOutstanding(isChecked)
            {
                $('#has_cleared_outstanding_blk').css('display', 'none');
                $('#not_cleared_blk').css('display', 'none');
                if(isChecked == 'Y')
                {
                    $('#has_cleared_outstanding_blk').css('display', 'block');
                }
                if(isChecked == 'N')
                {
                    $('#not_cleared_blk').css('display', 'block');                   
                }
            } 
            function gratuityChecked(isChecked)
            {
                $('#rent_deposit').css('display', 'none');
                $('#btnSubmit').attr('disabled', false);
                if(isChecked == 'N')
                {
                    $('#rent_deposit').css('display', 'block');
                    $('#btnSubmit').attr('disabled', true);
                }                
            }
            function validateForm()
            {
                if($('#pensionSanctioningAuthority').val() == '')
                {
                    alert("Please select Pension Sanctioning Authority.");
                    return false;
                }
                if(!$('#is_twin_city1')[0].checked && !$('#is_twin_city2')[0].checked)
                {
                    alert("Please check whether you have ever been posted at Cuttack/Bhubaneswar during service period?")
                    return false;
                }
                if($('#is_twin_city1')[0].checked)
                {
                    if($('#tcdesignation').val() == '')
                    {
                        alert('Please enter Designation where you had been posted at Cuttack/Bhubaneswar.');
                        return false;
                    }
                    if($('#tcoffice').val() == '')
                    {
                        alert('Please enter Office where you had been posted at Cuttack/Bhubaneswar.');
                        return false;
                    }                    
                }
                if(!$('#is_ga_qtr1')[0].checked && !$('#is_ga_qtr2')[0].checked)
                {
                    alert("Please check whether you are now in occupation of GA Quarters?")
                    return false;
                }
                if($('#is_ga_qtr1')[0].checked)
                {
                    alert('You have not vacated the GA Quarter.');
                    return false;
                }                

                if(!$('#has_occupied1')[0].checked && !$('#has_occupied2')[0].checked)
                {
                    alert("Please check whether you ever occupy any GA quarters in Cuttack/Bhubaneswar during your service period?")
                    return false;
                }
                if($('#has_occupied1')[0].checked)
                {
                    isEntered = false;
                    for(i = 1; i <= 5; i++)
                    {
                        if($('#place'+i).val() == '' && $('#area'+i).val() == '' && $('#quarterType'+i).val() == '' && $('#buildingNo'+i).val() == '')
                        {
                        }
                        else
                        {
                            isEntered = true;
                            break;
                        }
                    }
                    if(!isEntered)
                    {
                        alert("Please select at least one details of the GA Quarters occuplied.");
                        return false;
                    }
                }
                if($('#has_occupied1')[0].checked && isEntered)
                {
                    for(i = 1; i <= 5; i++)
                    {
                        if($('#place'+i).val() == '' && $('#area'+i).val() == '' && $('#quarterType'+i).val() == '' && $('#buildingNo'+i).val() == '')
                        {
                        }
                        else
                        {
                            if($('#place'+i).val() == '')
                            {
                                alert("Please enter Place.");
                                $('#place'+i)[0].focus();
                                return false;
                            }
                            if($('#area'+i).val() == '')
                            {
                                alert("Please enter area.");
                                $('#area'+i)[0].focus();
                                return false;
                            }                           
                            if($('#quarterType'+i).val() == '')
                            {
                                alert("Please enter Quarter Type.");
                                $('#quarterType'+i)[0].focus();
                                return false;
                            }
                            if($('#buildingNo'+i).val() == '')
                            {
                                alert("Please enter Building No.");
                                $('#buildingNo'+i)[0].focus();
                                return false;
                            }
                        }
                    }
                }  
                if(!$('#has_cleared_outstanding1')[0].checked && !$('#has_cleared_outstanding2')[0].checked)
                {
                    alert("Please check whether you have cleared your outstanding HLF?")
                    return false;
                }   
                if($('#has_cleared_outstanding1')[0].checked && $('#ccFile').val() == '')
                {
                    alert("Please attach the Outstanding Clearance Certificate.");
                    $('#ccFile')[0].focus();
                    return false;
                }
                if($('#has_cleared_outstanding2')[0].checked && !$('#gratuityRecovery1')[0].checked && !$('#gratuityRecovery2')[0].checked)
                {
                    alert("Please check whether the outstanding dues to be recovered from your DCRG?");
                    return false;
                }                
                if(!$('#is_ack_checked')[0].checked)
                {
                    alert("Please aknowledge that the above information submitted by you is true best of your knowledge.");
                    $('#is_ack_checked')[0].focus();
                    return false;
                }
                //return false
            }
            
        </script>
    </head>
    <body>
        <c:if test="${!qBean.isApplied}">
        <form:form action="SaveNDCApplication.htm" method="POST" commandName="QuarterBean" enctype="multipart/form-data" onsubmit="javascript: return validateForm()">
            <div class="container">
                <h2>Applying for Quarters NDC (GA Pool Quarters)</h2>
                <div class="panel-group">

                    <div class="panel panel-success">
                        <div class="panel-heading" style="font-weight:bold;font-size:13pt;">Personal Details:</div>
                        <div class="panel-body">
                            <table class="table" style="font-size:12pt;">
                                <tr>
                                    <td align="right" width="25%">Full Name:</td>
                                    <td><strong>${qBean1.fullName}</strong><input type="hidden" name="fullName" value="${qBean1.fullName}" /></td>
                                </tr>
                                <tr>
                                    <td align="right">Father&rsquo;s Name:</td>
                                    <td><strong>${user.fname}</strong><input type="hidden" name="fatherName" value="${user.fname}" /></td>
                                </tr>
                                <tr>
                                    <td align="right">Present Designation at the time of Retirement:</td>
                                    <td><strong>${user.postname}</strong><input type="hidden" name="designation" value="${user.postname}" /></td>
                                </tr>
                                <tr>
                                    <td align="right">Date of Retirement:</td>
                                    <td><strong>${user.empDos}</strong><input type="hidden" name="dateOfRetirement" value="${user.empDos}" /></td>
                                </tr>
                                <tr>
                                    <td align="right">Department from which retired:</td>
                                    <td><strong>${user.deptName}</strong><input type="hidden" name="retirementDept" value="${user.deptName}" /></td>
                                </tr> 
                                <tr>
                                    <td align="right">Pension Sanctioning Authority:</td>
                                    <td><input type="text" class="form-control" path="pensionSanctioningAuthority" id="pensionSanctioningAuthority" readonly="true"/>
                                        <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#pensionAuthorityModal">
                                            <span class="glyphicon glyphicon-search"></span> Search
                                        </button></td>
                                </tr>
                                <tr>
                                    <td align="right" width="30%">Mobile:</td>
                                    <td><input type="text" name="mobile" id="mobile" class="form-control" value="${user.mobile}" readonly /></td>
                                </tr>
                                <tr>
                                    <td align="right">Email:</td>
                                    <td><input type="text" name="email" id="email" class="form-control" value="${user.lname}" /></td>
                                </tr>  
                            </table>      

                        </div>
                    </div>

                    <div class="panel panel-success">
                        <div class="panel-heading" style="font-weight:bold;font-size:13pt;">Other Information:</div>
                        <div class="panel-body">
                            <table class="table" style="font-size:12pt;">
                                <tr>
                                    <td colspan="2">Have you ever been posted at Cuttack/Bhubaneswar during service period? 
                                        (If yes, give details of your Designation/Office)
                                        <span style="font-weight:bold;"><input type="radio" value="Y" name="isTwinCity" id="is_twin_city1" onclick="javascript: checkTwinCity(this.value)" /> <label for="is_twin_city1">Yes</label>
                                            <input type="radio" value="N" name="isTwinCity" id="is_twin_city2" onclick="javascript: checkTwinCity(this.value)" /> <label for="is_twin_city2">No</label>
                                            <div class="row" id="twin_city_blk" style="display:none;">
                                                <div class="col-lg-2" style="text-align:right;"><strong>Designation:</strong></div>
                                                <div class="col-lg-4"><input type="text" name="tcdesignation" id="tcdesignation" class="form-control" /></div>
                                                <div class="col-lg-2" style="text-align:right;"><strong>Office:</strong></div>
                                                <div class="col-lg-4"><input type="text" class="form-control" name="tcoffice" id="tcoffice" /></div>                                                
                                            </div>
                                        </td>
                                </tr> 
                                <tr>
                                    <td colspan="2">Are you now in occupation of GA Quarters?
                                        <span style="font-weight:bold;"><input type="radio" name="isGaQtr" id="is_ga_qtr1" value="Y" /> <label for="is_ga_qtr1">Yes</label>
                                            <input type="radio" name="isGaQtr" id="is_ga_qtr2" value="N" /> <label for="is_ga_qtr2">No</label></span></td>
                                </tr>
                                <tr>
                                    <td colspan="2">Did you ever occupy any GA quarters in Cuttack/Bhubaneswar during your service period?
                                        <span style="font-weight:bold;"><input type="radio" name="hasOccupied" id="has_occupied1" value="Y" onclick="javascript: checkOccupy(this.value)" /> <label for="has_occupied1">Yes</label>
                                            <input type="radio" name="hasOccupied" id="has_occupied2" value="N" onclick="javascript: checkOccupy(this.value)" /> <label for="has_occupied2">No</label></span></td>
                                </tr>  
                                <tr>
                                    <td colspan="2">
                                        <div id="has_occupied_blk" style="display:none;">
                                        <table class="table table-bordered">
                                            <tr style="background:#EAEAEA;font-weight:bold;">
                                                <td>Sl#</td>
                                                <td>Place</td>
                                                <td>Area</td>
                                                <td>Quarter Type</td>
                                                <td>Building No</td>
                                            </tr>
      <c:forEach var = "i" begin = "1" end = "5">
                                            <tr>
                                                <td>${i}</td>
                                                <td><input type="text" class="form-control" name="place" id="place${i}" /></td>
                                                <td><input type="text" class="form-control" name="area" id="area${i}" /></td>
                                                <td><input type="text" class="form-control" name="quarterType" id="quarterType${i}" /></td>
                                                <td><input type="text" class="form-control" name="buildingNo" id="buildingNo${i}" /></td>
                                            </tr>             
      </c:forEach>                                            

                                                                                    
                                        </table>
                                            </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="2">Have you cleared your outstanding HLF?
                                        <span style="font-weight:bold;">
                                            <input type="radio" name="hasClearedOutstanding" id="has_cleared_outstanding1" value="Y" onclick="javascript: checkOutstanding(this.value)" /> <label for="has_cleared_outstanding1">Yes</label>
                                            <input type="radio" name="hasClearedOutstanding" id="has_cleared_outstanding2" value="N" onclick="javascript: checkOutstanding(this.value)" /> <label for="has_cleared_outstanding2">No</label>
                                            <div class="row" id="has_cleared_outstanding_blk" style="display:none;">
                                                <div class="col-lg-4" style="text-align:right;"><strong>Attach Clearance Certificate:</strong></div>
                                                <div class="col-lg-6"><input type="file" class="form-control" name="ccFile" id="ccFile" /></div>
                                            </div> 
                                            <div class="row" id="not_cleared_blk" style="display:none;">
                                                
                                                <div class="col-lg-12">Do you want the said outstanding dues to be recovered from your DCRG (Death Cum Retirement Gratuity)?
                                            <input type="radio" name="gratuityRecovery" id="gratuityRecovery1" value="Y" onclick="javascript: gratuityChecked(this.value)" /> <label for="gratuityRecovery1">Yes</label>
                                            <input type="radio" name="gratuityRecovery" id="gratuityRecovery2" value="N" onclick="javascript: gratuityChecked(this.value)"  /> <label for="gratuityRecovery2">No</label>
                                            <div style="color:#FF0000;display:none;" id="rent_deposit">Kindly deposit the outstanding dues in the Rent Office and apply for NDC</div>
                                                </div>
                                            </div>                                              
                                    </td>
                                </tr>  
                                <tr>
                                    <td colspan="2" style="font-weight:bold;">
                                        <label><input type="checkbox" name="is_ack_checked" id="is_ack_checked" /> 
                                            <label for="is_ack_checked">Certified that the above information submitted by me is true to the best of my knowledge</label></label>

                                        <!--Application No. Generation on submission to the mobile/email--></td>
                                </tr>
                                <tr>
                                    <td colspan="2" style="font-weight:bold;" align="center"><input type="submit" value="Submit" id="btnSubmit" class="btn btn-success" />
                                        <input type="button" value="Cancel" class="btn btn-danger" /></td>
                                </tr>  
                            </table>

                        </div>
                    </div>    




                </div>
            </div>

            <div id="pensionAuthorityModal" class="modal" role="dialog">
                <div class="modal-dialog">
                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Pension Authority</h4>
                        </div>
                        <div class="modal-body">
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="sltDept">Department</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="sltDept" id="sltDept" class="form-control" onchange="getDeptWiseOfficeList();">
                                        <form:option value="">--Select Department--</form:option>
                                        <form:options items="${deptlist}" itemValue="deptCode" itemLabel="deptName"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="sltOffice">Office</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="sltOffice" id="sltOffice" class="form-control" onchange="getOfficeWisePostList();">
                                        <form:option value="">--Select Office--</form:option>
                                        <form:options items="${offList}" itemValue="offCode" itemLabel="offName"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="sltPost">Post</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="sltPost" id="sltPost" class="form-control" onchange="getPost();">
                                        <form:option value="">--Select Post--</form:option>
                                        <form:options items="${postList}" itemValue="spc" itemLabel="postname"/>
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
        </c:if>
            <c:if test="${qBean.isApplied}">
                 <div class="container">
                <h2>Application Detail for Quarters NDC (GA Pool Quarters)</h2>
                <h3><input type="button" value="Print Application" class="btn btn-primary" onclick="javascript: window.open('PrintNDC.htm', '', 'width=800,height=700')" /></h3>
                <div class="panel-group">

                    <div class="panel panel-success">
                        <div class="panel-heading" style="font-weight:bold;font-size:13pt;">Personal Details:</div>
                        <div class="panel-body">
                            <table class="table" style="font-size:12pt;">
                                <tr>
                                    <td align="right" width="25%">Full Name:</td>
                                    <td><strong>${qBean1.fullName}</strong><input type="hidden" name="fullName" value="${qBean1.fullName}" /></td>
                                </tr>
                                <tr>
                                    <td align="right">Father&rsquo;s Name:</td>
                                    <td><strong>${user.fname}</strong><input type="hidden" name="fatherName" value="${user.fname}" /></td>
                                </tr>
                                <tr>
                                    <td align="right">Present Designation at the time of Retirement:</td>
                                    <td><strong>${user.postname}</strong><input type="hidden" name="designation" value="${user.postname}" /></td>
                                </tr>
                                <tr>
                                    <td align="right">Date of Retirement:</td>
                                    <td><strong>${user.empDos}</strong><input type="hidden" name="dateOfRetirement" value="${user.empDos}" /></td>
                                </tr>
                                <tr>
                                    <td align="right">Department from which retired:</td>
                                    <td><strong>${user.deptName}</strong><input type="hidden" name="retirementDept" value="${user.deptName}" /></td>
                                </tr> 
                                <tr>
                                    <td align="right">Pension Sanctioning Authority:</td>
                                    <td>${qBean.pensionSanctioningAuthority}</td>
                                </tr>
                                <tr>
                                    <td align="right" width="30%">Mobile:</td>
                                    <td>${user.mobile}</td>
                                </tr>
                                <tr>
                                    <td align="right">Email:</td>
                                    <td>${user.lname}</td>
                                </tr>  
                            </table>      

                        </div>
                    </div>

                    <div class="panel panel-success">
                        <div class="panel-heading" style="font-weight:bold;font-size:13pt;">Other Information:</div>
                        <div class="panel-body">
                            <table class="table" style="font-size:12pt;">
                                <tr>
                                    <td colspan="2">Have you ever been posted at Cuttack/Bhubaneswar during service period? 
                                        
                                        <span style="font-weight:bold;">${qBean.isTwinCity}</span>
                                        <c:if test="${qBean.isTwinCity == 'Yes'}">
                                            <div class="row" id="twin_city_blk">
                                                <div class="col-lg-2" style="text-align:right;"><strong>Designation:</strong></div>
                                                <div class="col-lg-4">${qBean.tcdesignation}</div>
                                                <div class="col-lg-2" style="text-align:right;"><strong>Office:</strong></div>
                                                <div class="col-lg-4">${qBean.tcoffice}</div>                                                
                                            </div>
                                        </c:if>
                                        </td>
                                </tr> 
                                <tr>
                                    <td colspan="2">Are you now in occupation of GA Quarters?
                                        <span style="font-weight:bold;">${qBean.isGaQtr}</span></td>
                                </tr>
                                <tr>
                                    <td colspan="2">Did you ever occupy any GA quarters in Cuttack/Bhubaneswar during your service period?
                                        <span style="font-weight:bold;">${qBean.hasOccupied}</span></td>
                                </tr>
<c:if test="${qBean.hasOccupied == 'Yes'}">                                
                                <tr>
                                    <td colspan="2">
                                        <div id="has_occupied_blk">
                                        <table class="table table-bordered">
                                            <tr style="background:#EAEAEA;font-weight:bold;">
                                                <td>Sl#</td>
                                                <td>Place</td>
                                                <td>Area</td>
                                                <td>Quarter Type</td>
                                                <td>Building No</td>
                                            </tr>
      <c:forEach items="${qBean.quarterList}" var="qList" varStatus="count">
                                            <tr>
                                                <td>${count.index+1}</td>
                                                <td>${qList.place}</td>
                                                <td>${qList.area}</td>
                                                <td>${qList.quarterType}</td>
                                                <td>${qList.buildingNo}</td>
                                            </tr>             
      </c:forEach>                                            

                                                                                    
                                        </table>
                                            </div>
                                    </td>
                                </tr>
</c:if>                                
                                <tr>
                                    <td colspan="2">Have you cleared your outstanding HLF?
                                        <span style="font-weight:bold;">${qBean.hasClearedOutstanding}</span>
                                        <c:if test="${qBean.hasClearedOutstanding == 'Yes'}">
                                            <br /><a href="downloadCC.htm?applicationId=${qBean.applicationId}" target="_blank">Download Document</a>
                                        </c:if>
                                        <c:if test="${qBean.hasClearedOutstanding == 'No'}">
                                            <c:if test="${qBean.gratuityRecovery == 'Yes'}">
                                                <br /><span style="color:#008900;font-weight:bold;">The said outstanding dues to be recovered from your DCRG (Death Cum Retirement Gratuity)</span>
                                            </c:if>
                                        </c:if>                                            
                                            <div class="row" id="has_cleared_outstanding_blk" style="display:none;">
                                                <div class="col-lg-4" style="text-align:right;"><strong>Attach Clearance Certificate:</strong></div>
                                                <div class="col-lg-6"><input type="file" class="form-control" name="ccFile" id="ccFile" /></div>
                                            </div>                                            
                                    </td>
                                </tr>  

                            </table>

                        </div>
                    </div>    

                    <div class="panel panel-danger">
                        <div class="panel-heading" style="font-weight:bold;font-size:13pt;">Application Status:</div>
                        <div class="panel-body">
                            ${qBean.applicationStatus}
                            <c:if test="${qBean.hasFinalNDC == 'Y'}">
                                    <br /><a href="downloadNDC.htm?applicationId=${qBean.applicationId}" target="_blank">Download Final NDC</a>

                            </c:if>

                           
                        </div>                                        
                    </div> 


                </div>
            </div>

            </c:if>
    </body>
</html>
