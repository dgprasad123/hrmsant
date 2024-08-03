<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
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



            });
            function getDeptWiseOfficeList(type) {

                $('#hidAuthOffCode').empty();
                $('#authSpc').empty();

                var url = 'getOfficeListJSON.htm?deptcode=' + $('#hidAuthDeptCode').val();
                $('#hidAuthOffCode').append('<option value="">--Select Office--</option>');

                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#hidAuthOffCode').append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');

                    });
                });
            }

            function getOfficeWisePostList(type) {
                $('#authSpc').empty();
                url = 'getSanctioningSPCOfficeWiseListJSON.htm?offcode=' + $('#hidAuthOffCode').val();
                $('#authSpc').append('<option value="">--Select Post--</option>');

                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#authSpc').append('<option value="' + obj.spc + '">' + obj.postname + '</option>');

                    });
                });



            }

            function getPost(type) {
                $('#authPostName').val($('#authSpc option:selected').text());

                $('#hidTempAuthPost').val($('#authSpc').val());
                $('#hidTempDeptCode').val($('#hidAuthDeptCode').val());
                $('#hidTempAuthOffCode').val($('#hidAuthOffCode').val());

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

            function validateForm() {
                if ($('#visitPlace').val() == "") {
                    alert("Please enter Visit Place.");
                    $('#visitPlace').focus();
                    return false;
                }
                if ($('#dateOfCommencement').val() == "") {
                    alert("Please select Date of Commencement.");
                    $('#dateOfCommencement').focus();
                    return false;
                }
                if ($('#leaveType').val() == "") {
                    alert("Please select Leave Type.");
                    return false;
                }
                if ($('#fromDate').val() == "") {
                    alert("Please select From Date.");
                    return false;
                }
                if ($('#toDate').val() == "") {
                    alert("Please select To Date.");
                    return false;
                }   
                if ($('#placeofVisit').val() == "") {
                    alert("Please enter Place of Visit.");
                    return false;
                } 
                if ($('#visitState').val() == "") {
                    alert("Please enter Visit State.");
                    return false;
                }
                if ($('#modeOfJourney').val() == "") {
                    alert("Please enter Mode of Journey.");
                    return false;
                } 
                if ($('#appropriateDistance').val() == "") {
                    alert("Please enter Appropriate Distance.");
                    return false;
                }                 
               /* if ($('#durationFrom').val() == "") {
                    alert("Please enter Date With Effect From.");
                    $('#durationFrom')[0].focus();
                    return false;
                }
                if ($('#durationFromTime').val() == "") {
                    alert("Please select Time.");
                    $('#durationFromTime')[0].focus();
                    return false;
                }
                if ($('#duration').val() != "" && isNaN($('#duration').val())) {
                    alert("Please Enter a valid Integer value.");
                    $('#duration')[0].focus();
                    $('#duration')[0].select();
                    return false;
                }*/
                for(i = 1; i <= $('#noofMembers').val();i++)
                {
                    if($('#fMemberName'+i).val() == '')
                    {
                        alert("Please enter Name of the Family Member.");
                        $('#fMemberName'+i)[0].focus();
                        return false;
                    }
                    if($('#fMemberRelationship'+i).val() == '')
                    {
                        alert("Please enter the Family Relationship.");
                        $('#fMemberRelationship'+i)[0].focus();
                        return false;
                    } 
                    if($('#fMemberdob'+i).val() == '')
                    {
                        alert("Please enter Age/DOB.");
                        $('#fMemberdob'+i)[0].focus();
                        return false;
                    }
                }
                return true;
            }
        </script>
    </head>
    <body>
        <form:form action="SaveEmpLTC.htm" method="post" commandName="LTCBean" id="frmLTC">
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <strong style="font-size:14pt;">Apply for Leave Travel Concession Scheme</strong>
                    </div>
                    <div class="panel-body">

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="orderNumber">Name:<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">   
                                ${empName}
                            </div>
                            <div class="col-lg-2">
                                <label for="orderDate"> Designation<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">${designation}</div>
                        </div>
                        <!--<div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="orderNumber">Date of Birth:<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">   

                            </div>
                            <div class="col-lg-2">
                                <label for="orderDate"> Date of First Entry to Govt.<span style="color: red">*</span></label>
                            </div>
                        </div>-->
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="orderDate"> Present Scale of Pay:<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2"></div>
                        </div>                        
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="orderNumber">Intended Place of Visit:<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">   
                                <form:input class="form-control" path="visitPlace" id="visitPlace" />
                            </div>
                            <div class="col-lg-2">
                                <label for="orderDate"> Date of Commencement of outward journey (proposed):<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">
                                <div class='input-group date' id='processDate'>
                                    <form:input class="form-control" path="dateOfCommencement" id="dateOfCommencement" readonly="true"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>
                            </div>                            
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="orderNumber">Leave Type:<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">   
                                <select name="leaveType" id="leaveType" size="1" class="form-control">
                        <option value="">-Select-</option>
                        <c:forEach items="${leaveTypes}" var="lType">
                            <option value="${lType.value}">${lType.value}</option>    
                        </c:forEach>
                                </select>
                            </div>

                        </div>   
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="durationFrom">From Date:<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">
                                <div class='input-group date' id='processDate'>
                                    <form:input class="form-control" path="fromDate" id="fromDate" readonly="true"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>
                            </div>
                            <div class="col-lg-2">
                                <label for="durationFrom">To Date:<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">
                                <div class='input-group date' id='processDate'>
                                    <form:input class="form-control" path="toDate" id="toDate" readonly="true"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>
                            </div>                                    
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="orderNumber">No. of accompanying family members:</label>
                            </div>
                            <div class="col-lg-2">   
                                <form:select path="noofMembers" id="noofMembers" class="form-control" onchange="javascript:showBlocks(this.value)">
                                    <option value="1">1</option>
                                    <option value="2">2</option>
                                    <option value="3">3</option>
                                    <option value="4">4</option>
                                    <option value="5">5</option>                                    
                                </form:select>
                            </div>

                        </div> 
                        <div class="row" style="margin-bottom: 7px;" id="blk1">
                            <div class="col-lg-10">
                                <table width="100%" class="table tabler-bordered" style="border:1px solid #CCC">
                                    <tr bgcolor="#EAEAEA">
                                        <td>1.</td>
                                        <td colspan="2"><strong>Name of the first family member</strong></td>
                                        <td><form:input class="form-control" path="fMemberName" id="fMemberName1" /></td>
                                        <td>&nbsp;</td>
                                    </tr>
                                    <tr>
                                        <td width="20"></td>
                                        <td width="20">(a)</td>
                                        <td width="350">Relationship of the applicant</td>
                                        <td width="300"><form:input class="form-control" path="fMemberRelationship" id="fMemberRelationship1" /></td>
                                        <td>&nbsp;</td>
                                    </tr> 
                                    <tr>
                                        <td></td>
                                        <td>(b)</td>
                                        <td>Age/Date of Birth</td>
                                        <td><form:input class="form-control" path="fMemberdob" id="fMemberdob1" /></td>
                                        <td>&nbsp;</td>
                                    </tr> 
                                    <tr>
                                        <td></td>
                                        <td>(c)</td>
                                        <td>Whether married or unmarried</td>
                                        <td><form:input class="form-control" path="fMemberMstatus" id="fMemberMstatus1" /></td>
                                        <td>&nbsp;</td>
                                    </tr>
                                    <tr>
                                        <td></td>
                                        <td>(d)</td>
                                        <td>Whether a State Govt. servant</td>
                                        <td><form:input class="form-control" path="isStateGovt" id="isStateGovt1" /></td>
                                        <td>&nbsp;</td>
                                    </tr>
                                    <tr>
                                        <td></td>
                                        <td>(e)</td>
                                        <td>Monthly income from all sources, if any</td>
                                        <td><form:input class="form-control" path="monthlyIncome" id="monthlyIncome1" /></td>
                                        <td>&nbsp;</td>
                                    </tr>                                    
                                </table>
                            </div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;display:none;" id="blk2">
                            <div class="col-lg-10">
                                <table width="100%" class="table tabler-bordered" style="border:1px solid #CCC">
                                    <tr bgcolor="#EAEAEA">
                                        <td>2.</td>
                                        <td colspan="2"><strong>Name of the second family member</strong></td>
                                        <td><form:input class="form-control" path="fMemberName" id="fMemberName2" /></td>
                                        <td>&nbsp;</td>
                                    </tr>
                                    <tr>
                                        <td width="20"></td>
                                        <td width="20">(a)</td>
                                        <td width="350">Relationship of the applicant</td>
                                        <td width="300"><form:input class="form-control" path="fMemberRelationship" id="fMemberRelationship2" /></td>
                                        <td>&nbsp;</td>
                                    </tr> 
                                    <tr>
                                        <td></td>
                                        <td>(b)</td>
                                        <td>Age/Date of Birth</td>
                                        <td><form:input class="form-control" path="fMemberdob" id="fMemberdob2" /></td>
                                        <td>&nbsp;</td>
                                    </tr> 
                                    <tr>
                                        <td></td>
                                        <td>(c)</td>
                                        <td>Whether married or unmarried</td>
                                        <td><form:input class="form-control" path="fMemberMstatus" id="fMemberMstatus2" /></td>
                                        <td>&nbsp;</td>
                                    </tr>
                                    <tr>
                                        <td></td>
                                        <td>(d)</td>
                                        <td>Whether a State Govt. servant</td>
                                        <td><form:input class="form-control" path="isStateGovt" id="isStateGovt2" /></td>
                                        <td>&nbsp;</td>
                                    </tr>
                                    <tr>
                                        <td></td>
                                        <td>(e)</td>
                                        <td>Monthly income from all sources, if any</td>
                                        <td><form:input class="form-control" path="monthlyIncome" id="monthlyIncome2" /></td>
                                        <td>&nbsp;</td>
                                    </tr>                                    
                                </table>
                            </div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;display:none;" id="blk3">
                            <div class="col-lg-10">
                                <table width="100%" class="table tabler-bordered" style="border:1px solid #CCC">
                                    <tr bgcolor="#EAEAEA">
                                        <td>3.</td>
                                        <td colspan="2"><strong>Name of the third family member</strong></td>
                                        <td><form:input class="form-control" path="fMemberName" id="fMemberName3" /></td>
                                        <td>&nbsp;</td>
                                    </tr>
                                    <tr>
                                        <td width="20"></td>
                                        <td width="20">(a)</td>
                                        <td width="350">Relationship of the applicant</td>
                                        <td width="300"><form:input class="form-control" path="fMemberRelationship" id="fMemberRelationship3" /></td>
                                        <td>&nbsp;</td>
                                    </tr> 
                                    <tr>
                                        <td></td>
                                        <td>(b)</td>
                                        <td>Age/Date of Birth</td>
                                        <td><form:input class="form-control" path="fMemberdob" id="fMemberdob3" /></td>
                                        <td>&nbsp;</td>
                                    </tr> 
                                    <tr>
                                        <td></td>
                                        <td>(c)</td>
                                        <td>Whether married or unmarried</td>
                                        <td><form:input class="form-control" path="fMemberMstatus" id="fMemberMstatus3" /></td>
                                        <td>&nbsp;</td>
                                    </tr>
                                    <tr>
                                        <td></td>
                                        <td>(d)</td>
                                        <td>Whether a State Govt. servant</td>
                                        <td><form:input class="form-control" path="isStateGovt" id="isStateGovt3" /></td>
                                        <td>&nbsp;</td>
                                    </tr>
                                    <tr>
                                        <td></td>
                                        <td>(e)</td>
                                        <td>Monthly income from all sources, if any</td>
                                        <td><form:input class="form-control" path="monthlyIncome" id="monthlyIncome3" /></td>
                                        <td>&nbsp;</td>
                                    </tr>                                    
                                </table>
                            </div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;display:none;" id="blk4">
                            <div class="col-lg-10">
                                <table width="100%" class="table tabler-bordered" style="border:1px solid #CCC">
                                    <tr bgcolor="#EAEAEA">
                                        <td>4.</td>
                                        <td colspan="2"><strong>Name of the fourth family member</strong></td>
                                        <td><form:input class="form-control" path="fMemberName" id="fMemberName4" /></td>
                                        <td>&nbsp;</td>
                                    </tr>
                                    <tr>
                                        <td width="20"></td>
                                        <td width="20">(a)</td>
                                        <td width="350">Relationship of the applicant</td>
                                        <td width="300"><form:input class="form-control" path="fMemberRelationship" id="fMemberRelationship4" /></td>
                                        <td>&nbsp;</td>
                                    </tr> 
                                    <tr>
                                        <td></td>
                                        <td>(b)</td>
                                        <td>Age/Date of Birth</td>
                                        <td><form:input class="form-control" path="fMemberdob" id="fMemberdob4" /></td>
                                        <td>&nbsp;</td>
                                    </tr> 
                                    <tr>
                                        <td></td>
                                        <td>(c)</td>
                                        <td>Whether married or unmarried</td>
                                        <td><form:input class="form-control" path="fMemberMstatus" id="fMemberMstatus4" /></td>
                                        <td>&nbsp;</td>
                                    </tr>
                                    <tr>
                                        <td></td>
                                        <td>(d)</td>
                                        <td>Whether a State Govt. servant</td>
                                        <td><form:input class="form-control" path="isStateGovt" id="isStateGovt4" /></td>
                                        <td>&nbsp;</td>
                                    </tr>
                                    <tr>
                                        <td></td>
                                        <td>(e)</td>
                                        <td>Monthly income from all sources, if any</td>
                                        <td><form:input class="form-control" path="monthlyIncome" id="monthlyIncome4" /></td>
                                        <td>&nbsp;</td>
                                    </tr>                                    
                                </table>
                            </div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;display:none;" id="blk5">
                            <div class="col-lg-10">
                                <table width="100%" class="table tabler-bordered" style="border:1px solid #CCC">
                                    <tr bgcolor="#EAEAEA">
                                        <td>5.</td>
                                        <td colspan="2"><strong>Name of the fifth family member</strong></td>
                                        <td><form:input class="form-control" path="fMemberName" id="fMemberName5" /></td>
                                        <td>&nbsp;</td>
                                    </tr>
                                    <tr>
                                        <td width="20"></td>
                                        <td width="20">(a)</td>
                                        <td width="350">Relationship of the applicant</td>
                                        <td width="300"><form:input class="form-control" path="fMemberRelationship" id="fMemberRelationship5" /></td>
                                        <td>&nbsp;</td>
                                    </tr> 
                                    <tr>
                                        <td></td>
                                        <td>(b)</td>
                                        <td>Age/Date of Birth</td>
                                        <td><form:input class="form-control" path="fMemberdob" id="fMemberdob5" /></td>
                                        <td>&nbsp;</td>
                                    </tr> 
                                    <tr>
                                        <td></td>
                                        <td>(c)</td>
                                        <td>Whether married or unmarried</td>
                                        <td><form:input class="form-control" path="fMemberMstatus" id="fMemberMstatus5" /></td>
                                        <td>&nbsp;</td>
                                    </tr>
                                    <tr>
                                        <td></td>
                                        <td>(d)</td>
                                        <td>Whether a State Govt. servant</td>
                                        <td><form:input class="form-control" path="isStateGovt" id="isStateGovt5" /></td>
                                        <td>&nbsp;</td>
                                    </tr>
                                    <tr>
                                        <td></td>
                                        <td>(e)</td>
                                        <td>Monthly income from all sources, if any</td>
                                        <td><form:input class="form-control" path="monthlyIncome" id="monthlyIncome5" /></td>
                                        <td>&nbsp;</td>
                                    </tr>                                    
                                </table>
                            </div>
                        </div>
                                        
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-10">
                                <table width="100%" class="table tabler-bordered" style="border:1px solid #CCC">
                                    <tr bgcolor="#EAEAEA">
                                        <td></td>
                                        <td colspan="2"><strong>Details of Place of Visit</strong></td>
                                        <td></td>
                                        <td>&nbsp;</td>
                                    </tr>
                                    <tr>
                                        <td width="20"></td>
                                        <td width="20">(a)</td>
                                        <td width="350">Place of Visit</td>
                                        <td width="300"><form:input class="form-control" path="placeofVisit" /></td>
                                        <td>&nbsp;</td>
                                    </tr> 
                                    <tr>
                                        <td></td>
                                        <td>(b)</td>
                                        <td>Visit State</td>
                                        <td><form:input class="form-control" path="visitState" /></td>
                                        <td>&nbsp;</td>
                                    </tr> 
                                    <tr>
                                        <td></td>
                                        <td>(c)</td>
                                        <td>Mode of Journey</td>
                                        <td><form:input class="form-control" path="modeOfJourney" /></td>
                                        <td>&nbsp;</td>
                                    </tr>
                                    <tr>
                                        <td></td>
                                        <td>(d)</td>
                                        <td>Appropriate Distance both ways</td>
                                        <td><form:input class="form-control" path="appropriateDistance" /></td>
                                        <td>&nbsp;</td>
                                    </tr>                                  
                                </table>
                            </div>
                        </div>
                                        
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-10">
                                <table width="100%" class="table tabler-bordered" style="border:1px solid #CCC">
                                    <tr bgcolor="#EAEAEA">
                                        <td></td>
                                        <td colspan="3"><strong>Total reimbursable estimated cost of journey, both ways</strong></td>
                                        <td>&nbsp;</td>
                                    </tr>
                                    <tr>
                                        <td width="20"></td>
                                        <td width="20">(a)</td>
                                        <td width="350">Appropriate fare by train</td>
                                        <td width="300"><form:input class="form-control" path="costByTrain" /></td>
                                        <td>&nbsp;</td>
                                    </tr> 
                                    <tr>
                                        <td></td>
                                        <td>(b)</td>
                                        <td>Appropriate fare by Road</td>
                                        <td><form:input class="form-control" path="costByRoad" /></td>
                                        <td>&nbsp;</td>
                                    </tr> 
                                    <tr>
                                        <td></td>
                                        <td>(c)</td>
                                        <td>Appropriate fare by other means of travel</td>
                                        <td><form:input class="form-control" path="costByOther" /></td>
                                        <td>&nbsp;</td>
                                    </tr>
                                </table>
                            </div>
                        </div> 
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="orderNumber">Amount of Advance Applied for:<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">   
                                <form:input class="form-control" path="advanceAmount" />
                            </div>
                            <div class="col-lg-2">
                                <label for="orderDate"> Any other Relevant Information required by Sanctioning Authority:<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-4">
                                <textarea rows="4" cols="90" class="form-control" name="anyOtherInfo"></textarea>
                            </div>
                        </div>  
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-10">
                                    <input type="checkbox" id="isChecked" value="Y" /> <label for="isChecked">Please accept the below Terms and Conditions</label><br />
                                <p>1. That I am aware of the provisions of the LTC Rules of the State Govt.</p>
                                <p>2. That while on journey and stay during LTC I shall not claim compensation for loss of property/accident unless otherwise admissible.</p>
                                <p>3. That, with or family members, I will abide by restrictions/orders/requisitions as and then necessary during LTC Period.</p>
                                <p>4. That, my husband/wife being a State Government employees, I undertake that he/she has not availed LTC either for self or for family members either to and he/she will not be entitled for the benefit thereafter.</p>
                                </div>
                            </div>
                    </div>

                    <div class="panel-footer">
                        <button type="submit" name="submit" value="Save" class="btn btn-default" onclick="return validateForm();">Apply LTC</button>
                        <c:if test="${not empty PunishmentBean.acId}">
                            <button type="submit" name="submit" value="Delete" class="btn btn-default" onclick="return confirm('Are you sure to delete?');">Delete</button>
                        </c:if>
                        <input type="button" value="Cancel" class="btn btn-default" onclick="self.location = 'PunishmentList.htm'" />
                    </div>
                </div>
            </div>




        </form:form>
    </body>
    <script type="text/javascript">
        function validateForm()
        {
            if($('#visitPlace').val() == '')
            {
                alert("Please enter the Place of Visit.");
                $('#visitPlace')[0].focus();
                return false;
            }
            if($('#dateOfCommencement').val() == '')
            {
                alert("Please select Date of Commencement.");
                $('#dateOfCommencement')[0].focus();
                return false;
            }
            if($('#leaveType').val() == '')
            {
                alert("Please select Leave Type.")
                $('#leaveType')[0].focus();
                return false;
            }
            if($('#fromDate').val() == '')
            {
                alert("Please select From Date.");
                $('#fromDate')[0].focus();
                return false;
            }
            if($('#toDate').val() == '')
            {
                alert("Please select To Date.");
                $('#toDate')[0].focus();
                return false;
            }
            for(var i = 1; i <= $('#noofMembers').val();i++)
            {
                
            }
        }
        $(function() {
            $('#fromDate').datetimepicker({
                format: 'D-MMM-YYYY',
                useCurrent: false,
                ignoreReadonly: true
            });
            $('#toDate').datetimepicker({
                format: 'D-MMM-YYYY',
                useCurrent: false,
                ignoreReadonly: true
            });
            $('#dateOfCommencement').datetimepicker({
                format: 'D-MMM-YYYY',
                useCurrent: false,
                ignoreReadonly: true
            });
        });
        function showBlocks(idx)
        {
            for(var i = 2; i<= 5; i++)
            {
                $('#blk'+i).css('display', 'none');
            }            
            for(var i = 1; i<= idx; i++)
            {
                $('#blk'+i).css('display', 'block');
            }          
        }
    </script>
</html>
