<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Task Manager:: LTC</title>
        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <script src="js/moment.js" type="text/javascript"></script>
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
        <link rel="stylesheet" type="text/css" href="resources/css/bootstrap.min.css" />
        <link href="resources/css/colorbox.css" rel="stylesheet">
        <script type="text/javascript" src="js/common.js"></script>
        <script type="text/javascript" src="js/bootstrap.min.js"></script>  
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script type="text/javascript">
            $(function() {
                /*Call After Load*/

                $('#dept').combobox('clear');
                $('#office').combobox('clear');
                $('#post').combobox('clear');
                $('#auth').combobox('clear');

                $('#dept').combobox({url: 'getDeptListJSON.htm',
                    onSelect: function(record) {
                        $('#office').combobox('clear');
                        $('#post').combobox('clear');
                        $('#auth').combobox('clear');
                        var url = 'getOfficeListJSON.htm?deptcode=' + record.deptCode;
                        $('#office').combobox('reload', url);
                    }
                });
                $('#office').combobox({
                    onSelect: function(record) {
                        $('#post').combobox('clear');
                        $('#auth').combobox('clear');
                        var url = 'getPostCodeListJSON.htm?offcode=' + record.offCode;
                        $('#post').combobox('reload', url);
                    }
                });
                $('#post').combobox({
                    onSelect: function(record) {
                        $('#auth').combobox('clear');
                        var url = 'getPostCodeWiseEmployeeListSPCJSON.htm?postcode=' + record.value;
                        $('#auth').combobox('reload', url);
                    }
                });
            });

            function formatItem(row) {
                var s = '<span style="font-weight:bold">' + row.label + '</span><br/>' +
                        '<span style="color:#228B22">' + row.desc + '</span>';
                return s;
            }
            function saveApproval() {
                var status = $('#sltApproveStatus').combobox('getValue');
                $('#taskStatus').val(status);
                $('#status_id').val(status);
                if (status == '') {
                    alert("Please select Action.");
                    return false;
                } else if (status == 86) {
                    $('#winApplyTraining').window('open');
                } else {
                    formsubmit();
                }
            }
            function saveOrderInfo()
            {
                if ($('#orderNo').val() == '')
                {
                    alert("Please enter Order No.");
                    return false;
                }
                if ($('#orderDate').val() == '')
                {
                    alert("Please enter Order Date.");
                    return false;
                }
                $('#frmLTC').form('submit', {
                    url: 'SaveOrderInfo.htm',
                    success: function(response) {
                        var result = eval('(' + response + ')');
                        if (result.message) {
                            $.messager.show({
                                title: 'Error',
                                msg: result.message
                            });
                        } else {
                            $('#winApplyTraining').window('close');
                            window.parent.$('#win').window('close');
                        }
                    }
                });
            }
            function forwardLTC() {
                $('#taskAuthority').val($('#auth').combobox('getValue'));
                if ($('#auth').combobox('getValue') == '')
                {
                    alert("Please select Authority before forwarding.");
                    return false;
                }

                formsubmit();
            }
            function formsubmit() {
                $('#frmLTC').form('submit', {
                    url: 'saveLTCAction.htm',
                    success: function(response) {
                        var result = eval('(' + response + ')');
                        if (result.message) {
                            $.messager.show({
                                title: 'Error',
                                msg: result.message
                            });
                        } else {
                            $('#winApplyTraining').window('close');
                            window.parent.$('#win').window('close');
                        }
                    }
                });
            }
            function saveVerification()
            {
                if ($('#has_10year1')[0].checked == false && $('#has_10year2')[0].checked == false)
                {
                    alert("Please check, Whether the Applicant is applying LTC after 10 years.");
                    return false;
                }
                if ($('#has_due1')[0].checked == false && $('#has_due2')[0].checked == false)
                {
                    alert("Please check, whether the Applicant has any dues pending, from the previous LTC.");
                    return false;
                }
                if ($('#is_journey_eligible1')[0].checked == false && $('#is_journey_eligible2')[0].checked == false)
                {
                    alert("Please check, whether the Applicant is eligible for the Mode of Journey by ${ltBean.modeOfJourney}.");
                    return false;
                }
                if ($('#has_ltc_eligibility1')[0].checked == false && $('#has_ltc_eligibility2')[0].checked == false)
                {
                    alert("Please check, whether the applicant is fulfilling all the criteria for LTC, as per State Govt. LTC Rule.");
                    return false;
                }
                if ($('#has_ltc_eligibility1')[0].checked == false && $('#has_ltc_eligibility2')[0].checked == true && $('#remarks').val() == '')
                {
                    alert("Please enter the reason for non-eligibility in the Remarks.");
                    return false;
                }
                if (confirm("Are you sure you want to submit?"))
                {
                    $('#frmLTC').form('submit', {
                        url: 'SaveVerification.htm',
                        success: function(response) {
                            var result = eval('(' + response + ')');
                            if (result.message) {
                                $.messager.show({
                                    title: 'Error',
                                    msg: result.message
                                });
                            } else {
                                $('#winApplyTraining').window('close');
                                window.parent.$('#win').window('close');
                            }
                        }
                    });
                }
            }
        </script>
        <style type="text/css">
            table tr td{padding:6px;}
        </style>
    </head>
    <body style="padding:0px;">
        <div style="width:95%;margin:0px auto;border-top:0px;">
            <div class="row">

                <div class="container-fluid">
                    <div class="panel panel-default" style="padding-left:10px;">
                        <div class="panel-heading" style="margin-bottom:10px;padding-bottom:5px;color:#0052A7">

                            <strong style="font-size:14pt;">LTC Application Details</strong>
                        </div>
                        <div>
                            <table width="100%" class="table table-bordered" style="font-size:10pt;border:1px solid #CCC;">
                                <tr bgcolor="#EAEAEA">
                                    <td colspan="4"><strong style="font-size:12pt;">Basic Information</strong></td>
                                </tr>                            
                                <tr>
                                    <td><label for="orderNumber">Name:</label></td>
                                    <td colspan="3">${ltBean.empName}</td>
                                </tr>
                                <tr>
                                    <td><label for="orderDate"> Designation:</label></td>
                                    <td colspan="3">${ltBean.designation}</td>
                                </tr>
                                <tr>
                                    <td><label for="orderNumber">Place of Visit:</label></td>
                                    <td>${ltBean.visitPlace}</td>
                                    <td><label for="orderDate">Date of Commencement:</label></td>
                                    <td>${ltBean.dateOfCommencement}</td>
                                </tr>   
                                <tr>
                                    <td><label for="orderNumber">Leave Type:</label></td>
                                    <td colspan="3">${ltBean.leaveType}</td>
                                </tr>                             
                                <tr>
                                    <td><label for="orderNumber">From Date:</label></td>
                                    <td>${ltBean.fromDate}</td>
                                    <td><label for="orderDate">To Date:</label></td>
                                    <td>${ltBean.toDate}</td>
                                </tr>                            
                            </table>
                            <table width="100%" class="table tabler-bordered" style="border:1px solid #CCC;font-size:10pt;">
                                <tr bgcolor="#EAEAEA">
                                    <td colspan="2"><strong style="font-size:12pt;">Details of Place of Visit</strong></td>
                                    <td></td>
                                    <td>&nbsp;</td>
                                </tr>
                                <tr>
                                    <td width="20">(a)</td>
                                    <td width="350">Place of Visit</td>
                                    <td width="300">${ltBean.placeofVisit}</td>
                                    <td>&nbsp;</td>
                                </tr> 
                                <tr>
                                    <td>(b)</td>
                                    <td>Visit State</td>
                                    <td>${ltBean.visitState}</td>
                                    <td>&nbsp;</td>
                                </tr> 
                                <tr>
                                    <td>(c)</td>
                                    <td>Mode of Journey</td>
                                    <td>${ltBean.modeOfJourney}</td>
                                    <td>&nbsp;</td>
                                </tr>
                                <tr>
                                    <td>(d)</td>
                                    <td>Appropriate Distance both ways</td>
                                    <td>${ltBean.appropriateDistance}</td>
                                    <td>&nbsp;</td>
                                </tr>                                  
                            </table>
                            <table class="table table-bordered" style="font-size:10pt;width:100%;border:1px solid #CCC;">
                                <thead>
                                    <tr bgcolor="#EAEAEA">
                                        <td colspan="7"><strong style="font-size:12pt;">Family Members</strong></td>
                                    </tr>                                
                                    <tr bgcolor="#EAEAEA">
                                        <th>Sl No.</th>
                                        <th>Name</th>
                                        <th>Relationship</th>
                                        <th>Age/DOB</th>
                                        <th>Marital Status</th>
                                        <th>State Govt Emp?</th>
                                        <th>Monthly Income</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${ltBean.familyList}" var="fl" varStatus="theCounter">
                                        <tr>
                                            <td>${theCounter.index+1}</td>
                                            <td>${fl.fMemberName}</td>
                                            <td>${fl.fMemberRelationship}</td>
                                            <td>${fl.fMemberdob}</td>
                                            <td>${fl.fMemberMstatus}</td>
                                            <td>${fl.isStateGovt}</td>
                                            <td>${fl.monthlyIncome}</td>
                                        </tr>

                                    </c:forEach>
                                </tbody>
                            </table>     

                            <table width="100%" class="table tabler-bordered" style="font-size:10pt;border:1px solid #CCC">
                                <tr bgcolor="#EAEAEA">
                                    <td colspan="3"><strong style="font-size:12pt;">Total reimbursable estimated cost of journey, both ways</strong></td>
                                    <td>&nbsp;</td>
                                </tr>
                                <tr>
                                    <td width="20">(a)</td>
                                    <td width="350">Approximate fare by train</td>
                                    <td width="300">${ltBean.costByTrain}</td>
                                    <td>&nbsp;</td>
                                </tr> 
                                <tr>

                                    <td>(b)</td>
                                    <td>Approximate fare by Road</td>
                                    <td>${ltBean.costByRoad}</td>
                                    <td>&nbsp;</td>
                                </tr> 
                                <tr>
                                    <td>(c)</td>
                                    <td>Approximate fare by other means of travel</td>
                                    <td>${ltBean.costByOther}</td>
                                    <td>&nbsp;</td>
                                </tr>
                            </table>
                            <table class="table table-bordered" style="font-size:10pt;width:100%;border:1px solid #CCC;">
                                <tr>
                                    <td width="25%" style="font-weight: bold;"><label for="orderNumber">Amount of Advance Applied for:</label></td>
                                    <td>${ltBean.advanceAmount}</td>
                                </tr>
                                <tr>
                                    <td style="font-weight: bold;"><label for="orderNumber">Any other Relevant Information required by Sanctioning Authority:</label></td>
                                    <td> ${ltBean.anyOtherInfo}</td>
                                </tr>                                                    
                            </table>




                        </div>


                    </div>
                </div>





            </div>

            <form id="frmLTC" action="" method="POST" commandName="LTCBean">
                <input type="hidden" name="status_id" id="status_id" value="${statusId}" />
                <input type="hidden" name="ltcId" id="ltcId" value="${ltBean.ltcId}" />
                <input type="hidden" name="taskAuthority" id="taskAuthority"/>
                <input type="hidden" name="taskStatus" id="taskStatus"/>
                <input type="hidden" name="taskId" id="taskId" value="${ltBean.taskId}"/>
                <c:if test = "${statusId == 87}">
                    <table width="100%" class="table tabler-bordered" style="font-size:10pt;border:1px solid #CCC" id="ltc_form">
                        <tr style="font-weight:bold;background:#EAEAEA;">
                            <td colspan="3">Update Order Info</td>
                        </tr>
                        <tr>
                            <td width="180" align="right">Order No:</td>
                            <td><input type="text" name="orderNo" id="orderNo" class="form-control" /></td>
                            <td></td>
                        </tr>
                        <tr>
                            <td width="180" align="right">Order Date:</td>
                            <td width="200">                                <div class='input-group date' id='orderDate1'>
                                    <input type="text" class="form-control" name="orderDate" id="orderDate" readonly="true" />
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div></td>
                            <td></td>
                        </tr>                    
                        <tr>
                            <td>&nbsp;</td>
                            <td>
                                <input type="button" value="Save Order Info" class="btn btn-primary btn-sm" onclick="javascript: saveOrderInfo()" />
                            </td>
                            <td></td>
                        </tr>
                    </table>
                </c:if>
                <c:if test = "${statusId == 85 || statusId == 86}">
                    <table width="100%" class="table tabler-bordered" style="font-size:10pt;border:1px solid #CCC" id="ltc_form">
                        <tr style="font-weight:bold;background:#EAEAEA;">
                            <td colspan="2">Take Action</td>
                        </tr>
                        <tr>
                            <td width="180" align="right">Action:</td>
                            <td>
                                <input id="sltApproveStatus" name="sltApproveStatus" class="easyui-combobox" data-options="
                                       valueField: 'value',
                                       textField: 'label',
                                       url:'getLTCStatusJSON.htm'" />
                            </td>
                        </tr>
                        <tr>
                            <td width="180" align="right">Note:</td>
                            <td><textarea name="note" id="note" rows="4" cols="50"></textarea></td>
                        </tr>                
                        <tr>
                            <td></td>
                            <td>
                                <input type="button" value="Submit" class="btn btn-primary btn-sm" onclick="javascript: saveApproval()" />
                            </td>
                        </tr>
                    </table>
                </c:if>
                <c:if test = "${statusId == 90}">
                    <table width="100%" class="table tabler-bordered" style="font-size:10pt;border:1px solid #CCC" id="ltc_form">
                        <tr style="font-weight:bold;background:#EAEAEA;">
                            <td colspan="2">Verify LTC Application</td>
                        </tr>
                        <tr>
                            <td align="right">Whether the Applicant is applying LTC after 10 years?</td>
                            <td><input type="radio" value="Y" name="has10year" id="has_10year1" /> <label for="has_10year1">Yes</label>
                                <input type="radio" name="has10year" id="has_10year2" value="N" /> <label for="has_10year2">No</label></td>
                        </tr> 
                        <tr>
                            <td align="right">Whether the Applicant has any dues pending from the previous LTC?</td>
                            <td><input type="radio" value="Y" name="hasDue" id="has_due1" /> <label for="has_due1">Yes</label>
                                <input type="radio" id="has_due2" name="hasDue" value="N" /> <label for="has_due2">No</label></td>
                        </tr>  
                        <tr>
                            <td align="right">Whether the Applicant is eligible for the Mode of Journey by <strong>${ltBean.modeOfJourney}</strong>?</td>
                            <td><input type="radio" value="Y" name="isJourneyElitible" id="is_journey_eligible1" /> <label for="is_journey_eligible1">Yes</label>
                                <input type="radio" id="is_journey_eligible2" name="isJourneyElitible" value="N" /> <label for="is_journey_eligible2">No</label></td>
                        </tr> 
                        <tr>
                            <td align="right">Is the applicant fulfilling all the criteria for LTC, as per State Govt. LTC Rule?</td>
                            <td><input type="radio" name="hasLtcEligibility" value="Y" id="has_ltc_eligibility1" /> <label for="has_ltc_eligibility1">Yes</label>
                                <input type="radio" name="hasLtcEligibility" id="has_ltc_eligibility2" value="N" /> <label for="has_ltc_eligibility2">No</label></td>
                        </tr>                      
                        <tr>
                            <td width="450" align="right">Remarks:</td>
                            <td><textarea name="remarks" id="remarks" rows="4" cols="50"></textarea></td>
                        </tr>                   
                        <tr>
                            <td></td>
                            <td>
                                <input type="button" value="Submit" class="btn btn-primary btn-sm" onclick="javascript: saveVerification()" />
                            </td>
                        </tr>
                    </table>
                </c:if>                
                <div id="winApplyTraining" class="easyui-window" title="Select Authority" style="width:700px;height:300px;padding:10px" closed="true">

                    <table width="100%" cellpadding="0" cellspacing="0" style="font-size:9pt;">
                        <tr style="height: 30px;">
                            <td width="30%">Department:</td>
                            <td width="70%">
                                <input class="easyui-combobox" id="dept" style="width:100%;height:26px;" data-options="valueField:'deptCode',textField:'deptName',editable:false"/>
                            </td>
                        </tr>
                        <tr style="height: 30px;">
                            <td>Office</td>
                            <td>
                                <input class="easyui-combobox" id="office" style="width:100%;height:26px;" data-options="valueField:'offCode',textField:'offName',editable:false"/>
                            </td>
                        </tr>
                        <tr>
                            <td>Post:</td>
                            <td>
                                <input class="easyui-combobox" id="post" style="width:100%;height:26px;" data-options="valueField:'value',textField:'label',editable:false"/>
                            </td>
                        </tr>
                        <tr>
                            <td>Authority:</td>
                            <td>
                                <input class="easyui-combobox" id="auth" name="auth_spc" style="width:100%;height:26px;" data-options="valueField:'value',textField:'label',editable:false"/>
                            </td>
                        </tr>                        
                        <tr style="height: 30px;">
                            <td>&nbsp;</td>
                            <td>
                                <button type="button" onclick="javascript: forwardLTC()">Forward</button>
                            </td>
                        </tr>
                    </table>
                </div>
            </form>
        </div>
    </body>
    <script type="text/javascript">
        $(function() {
            $('#orderDate1').datetimepicker({
                format: 'D-MMM-YYYY',
                useCurrent: false,
                ignoreReadonly: true
            });
        });
    </script>
</html>
