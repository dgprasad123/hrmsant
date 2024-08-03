<%-- 
    Document   : TrainingProgram
    Created on : 22 Oct, 2016, 11:11:07 AM
    Author     : Manoj PC
--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Manage Training Program</title>
        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
        <script language="javascript" src="js/jquery.datetimepicker.js" type="text/javascript"></script>
        <link href="css/jquery.datetimepicker.css" rel="stylesheet" type="text/css" />
        <link rel="stylesheet" type="text/css" href="resources/css/bootstrap.min.css" />
        <link href="resources/css/colorbox.css" rel="stylesheet">
        <script type="text/javascript" src="js/jquery.colorbox-min.js"></script>
        <style type="text/css">
            h1{font-size:15pt;font-weight:bold;margin-bottom:10px;}
            #training_form td{padding:6px;}
            .form-control{height:30px;}
            .datagrid-header-row{font-weight:bold;}
            .panel-title{margin-bottom:5px;} 
            .window .window-header{background:#5593BC;}
            .panel-header{background:#5593BC;color:#FFFFFF;}
            .panel-title{margin-bottom:5px;}
            .panel-body{font-size:15pt;}
            .custom_table td{padding:4px;}
            .pagination{font-size:11pt;}
            .apply-table td{padding:5px;}
        </style>
        <script type="text/javascript">
            $(document).ready(function() {
                //Get the training detail
                $.ajax({
                    url: "showNISGTrainingDetail.htm?trainingId=" + $('#trainingId').val(),
                    success: function(result) {
                        $('#training_detail').html(result);
                    }});
            });

            function submitTraining() {
                if (obj.highestQualification.value == '')
                {
                    alert("Please enter your Highest Qualification.");
                    obj.highestQualification.focus();
                    return false;
                }
                if (obj.projectsInitiated1.value == '' && obj.projectsInitiated2.value == '' && obj.projectsInitiated3.value == '')
                {
                    alert("Please fill up Section A.");
                    obj.projectsInitiated1.focus();
                    return false;
                }
                if (obj.projectsAssociated1.value == '' && obj.projectsAssociated2.value == '' && obj.projectsAssociated3.value == '')
                {
                    alert("Please fill up Section B.");
                    obj.projectsAssociated1.focus();
                    return false;
                }
                if (obj.topTwoThings1.value == '' && obj.topTwoThings2.value == '' && obj.topTwoThings3.value == '')
                {
                    alert("Please fill up Section C.");
                    obj.topTwoThings1.focus();
                    return false;
                }
                if (obj.conceptualizationPhase1.value == '' && obj.conceptualizationPhase2.value == '' && obj.devPhase1.value == '' && obj.devPhase2.value == '' && obj.impPhase1.value == '' && obj.impPhase2.value == '' && obj.operationPhase1.value == '' && obj.operationPhase2.value == '')
                {
                    alert("Please fill up Section D.");
                    obj.conceptualizationPhase1.focus();
                    return false;
                }
                if (obj.previousTraining1.value == '' && obj.previousTraining2.value == '' && obj.previousTraining3.value == '')
                {
                    alert("Please fill up Section E.");
                    obj.previousTraining1.focus();
                    return false;
                }
                if (obj.skillEnhance1.value == '' && obj.skillEnhance2.value == '' && obj.skillEnhance3.value == '')
                {
                    alert("Please fill up Section F.");
                    obj.skillEnhance1.focus();
                    return false;
                }
            }

        </script>
    </head>
    <body>
        <form id="frmTraining" name="frmTraining" method="POST" commandName="NISGTrainingBean"  action="saveNISGTraining.htm" onsubmit="return submitTraining();">
            <input type="hidden" name="trainingId" id="trainingId" value="${trainingId}"/>
            <input type="hidden" name="empId" id="trainingEmpId" value="${empId}" />
            <input type="hidden" name="cnt" id="cnt" value="${cnt}" />
            <c:choose>
                <c:when test="${result=='success'}">
                    <div id="thank_you" style="margin-top:10px;width:800px;margin:0px auto;text-align:center;"><h1>Thank you for applying 
                            for the Training Program on e-Governance.<br /><br /><span style="font-size:14pt;">You will be intimated with further details upon selection, 
                                for the above Training Program.</span></h1></div>
                            </c:when>    
                            <c:otherwise>

                    <c:choose>
                        <c:when test="${cnt==0}">

                            <div id="training_detail"></div>

                            <table width="800" cellspacing="1" cellpadding="4" border="0" bgcolor="#EAEAEA" style="margin-top:10px;border:1px solid #CCCCCC" align="center" class="apply-table">
                                <tr bgcolor="#006A9D" style="color:#FFFFFF;font-weight:bold;">
                                    <td colspan="2">Please fill out all the information</td>
                                </tr>
                                <tr bgcolor="#FFFFFF">
                                    <td width="250">Name of the Participant:</td>
                                    <td><strong>${empName}</strong></td>
                                </tr>
                                <tr bgcolor="#FFFFFF">
                                    <td>Highest Educational Qualification:</td>
                                    <td><input type="text" name="highestQualification" value="" class="form-control" /></td>
                                </tr>
                                <tr bgcolor="#FFFFFF">
                                    <td>Designation:</td>
                                    <td><strong>${empPost}</strong></td>
                                </tr> 
                                <tr bgcolor="#FFFFFF">
                                    <td>Department/Ministry:</td>
                                    <td><strong>${empDeptName}</strong></td>
                                </tr> 
                            </table>
                            <table width="800" cellspacing="1" cellpadding="4" border="0" bgcolor="#EAEAEA" style="margin-top:10px;" align="center" class="apply-table">
                                <tr style="font-weight:bold;font-style:italic;">
                                    <td colspan="2">The following section will help us understand your previous experience in eGovernance projects and allow us to bring out areas of focus and discussion points for learning during the programme.</td>
                                </tr>
                            </table>
                            <table width="800" cellspacing="1" cellpadding="4" border="0" bgcolor="#EAEAEA" style="border:1px solid #CCCCCC;margin-top:10px;" align="center" class="apply-table">
                                <tr bgcolor="#006A9D" style="color:#FFFFFF">
                                    <td colspan="2"><strong>A.	List any eGovernance projects your department has initiated during the years 2015-17</strong></td>
                                </tr>
                                <tr bgcolor="#FFFFFF">
                                    <td>1:</td>
                                    <td><textarea name="projectsInitiated1" class="form-control" rows="1"></textarea></td>
                                </tr> 
                                <tr bgcolor="#FFFFFF">
                                    <td>2:</td>
                                    <td><textarea name="projectsInitiated2" class="form-control" rows="1"></textarea></td>
                                </tr> 
                                <tr bgcolor="#FFFFFF">
                                    <td>3:</td>
                                    <td><textarea type="text" name="projectsInitiated3" class="form-control" rows="1"></textarea></td>
                                </tr>   
                            </table>
                            <table width="800" cellspacing="1" cellpadding="4" border="0" bgcolor="#EAEAEA" style="border:1px solid #CCCCCC;margin-top:10px;" align="center" class="apply-table">
                                <tr bgcolor="#006A9D" style="color:#FFFFFF">
                                    <td colspan="2"><strong>B.	List any computerization/eGovernance projects you were/are associated with during 2015-17</strong></td>
                                </tr>
                                <tr bgcolor="#FFFFFF">
                                    <td>1:</td>
                                    <td><textarea name="projectsAssociated1" class="form-control" rows="1"></textarea></td>
                                </tr> 
                                <tr bgcolor="#FFFFFF">
                                    <td>2:</td>
                                    <td><textarea name="projectsAssociated2" class="form-control" rows="1"></textarea></td>
                                </tr> 
                                <tr bgcolor="#FFFFFF">
                                    <td>3:</td>
                                    <td><textarea name="projectsAssociated3" value="" class="form-control" rows="1"></textarea></td>
                                </tr>   
                            </table>
                            <table width="800" cellspacing="1" cellpadding="4" border="0" bgcolor="#EAEAEA" style="border:1px solid #CCCCCC;margin-top:10px;" align="center" class="apply-table">
                                <tr bgcolor="#006A9D" style="color:#FFFFFF">
                                    <td colspan="2"><strong>C.	Of the eGovernance projects (mentioned under section B) you have worked on, list top two things you would like to do differently, if you are to design and implement it today</strong></td>
                                </tr>
                                <tr bgcolor="#FFFFFF">
                                    <td>1:</td>
                                    <td><textarea name="topTwoThings1" value="" class="form-control" rows="1"></textarea></td>
                                </tr> 
                                <tr bgcolor="#FFFFFF">
                                    <td>2:</td>
                                    <td><textarea name="topTwoThings2" value="" class="form-control" rows="1"></textarea></td>
                                </tr> 
                                <tr bgcolor="#FFFFFF">
                                    <td>3:</td>
                                    <td><textarea name="topTwoThings3" value="" class="form-control" rows="1"></textarea></td>
                                </tr>   
                            </table>
                            <table width="800" cellspacing="1" cellpadding="4" border="0" bgcolor="#EAEAEA" style="border:1px solid #CCCCCC;margin-top:10px;" align="center" class="apply-table">
                                <tr bgcolor="#006A9D" style="color:#FFFFFF">
                                    <td colspan="2"><strong>D.	List top two challenges you faced during each phase of the eGovernance projects listed by you.  In case your project has not reached any particular phase or you got disassociated write “not reached the level” or “not involved”</strong></td>
                                </tr>
                                <tr bgcolor="#FFFFFF">
                                    <td width="250">Conceptualization phase:</td>
                                    <td><table width="100%" cellspacing="1" cellpadding="4" border="0">
                                            <tr>
                                                <td width="30"><strong>1:</strong></td>
                                                <td><textarea name="conceptualizationPhase1" value="" class="form-control" rows="1"></textarea></td>
                                            </tr>
                                            <tr>
                                                <td width="30"><strong>2:</strong></td>
                                                <td><textarea name="conceptualizationPhase2" value="" class="form-control" rows="1"></textarea></td>
                                            </tr>      
                                        </table>
                                    </td>
                                </tr> 
                                <tr bgcolor="#FFFFFF">
                                    <td>Design & development phase:</td>
                                    <td><table width="100%" cellspacing="1" cellpadding="4" border="0">
                                            <tr>
                                                <td width="30"><strong>1:</strong></td>
                                                <td><textarea name="devPhase1" value="" class="form-control" rows="1"></textarea></td>
                                            </tr>
                                            <tr>
                                                <td width="30"><strong>2:</strong></td>
                                                <td><textarea name="devPhase2" value="" class="form-control" rows="1"></textarea></td>
                                            </tr>      
                                        </table>
                                    </td>
                                </tr> 
                                <tr bgcolor="#FFFFFF">
                                    <td>Implementation phase:</td>
                                    <td><table width="100%" cellspacing="1" cellpadding="4" border="0">
                                            <tr>
                                                <td width="30"><strong>1:</strong></td>
                                                <td><textarea name="impPhase1" value="" class="form-control" rows="1"></textarea></td>
                                            </tr>
                                            <tr>
                                                <td width="30"><strong>2:</strong></td>
                                                <td><textarea name="impPhase2" value="" class="form-control" rows="1"></textarea></td>
                                            </tr>      
                                        </table>
                                    </td>
                                </tr> 
                                <tr bgcolor="#FFFFFF">
                                    <td>Operations & maintenance phase:</td>
                                    <td><table width="100%" cellspacing="1" cellpadding="4" border="0">
                                            <tr>
                                                <td width="30"><strong>1:</strong></td>
                                                <td><textarea name="operationPhase1" value="" class="form-control" rows="1"></textarea></td>
                                            </tr>
                                            <tr>
                                                <td width="30"><strong>2:</strong></td>
                                                <td><textarea name="operationPhase2" value="" class="form-control" rows="1"></textarea></td>
                                            </tr>      
                                        </table>
                                    </td>
                                </tr>   
                            </table>
                            <table width="800" cellspacing="1" cellpadding="4" border="0" bgcolor="#EAEAEA" style="border:1px solid #CCCCCC;margin-top:10px;" align="center" class="apply-table">
                                <tr bgcolor="#006A9D" style="color:#FFFFFF">
                                    <td colspan="2"><strong>E.	Have you undergone any previous training on eGovernance? If yes, please give details.</strong></td>
                                </tr>
                                <tr bgcolor="#FFFFFF">
                                    <td>1:</td>
                                    <td><textarea name="previousTraining1" value="" class="form-control" rows="1"></textarea></td>
                                </tr> 
                                <tr bgcolor="#FFFFFF">
                                    <td>2:</td>
                                    <td><textarea name="previousTraining2" value="" class="form-control" rows="1"></textarea></td>
                                </tr> 
                                <tr bgcolor="#FFFFFF">
                                    <td>3:</td>
                                    <td><textarea name="previousTraining3" value="" class="form-control"  rows="1"></textarea></td>
                                </tr>   
                            </table>
                            <table width="800" cellspacing="1" cellpadding="4" border="0" bgcolor="#EAEAEA" style="border:1px solid #CCCCCC;margin-top:10px;" align="center" class="apply-table">
                                <tr bgcolor="#006A9D" style="color:#FFFFFF">
                                    <td colspan="2"><strong>F.	What are the three areas in which you would like to develop/enhance your skill/expertise in eGovernance?</strong></td>
                                </tr>
                                <tr bgcolor="#FFFFFF">
                                    <td>1:</td>
                                    <td><textarea name="skillEnhance1" value="" class="form-control" rows="1"></textarea></td>
                                </tr> 
                                <tr bgcolor="#FFFFFF">
                                    <td>2:</td>
                                    <td><textarea name="skillEnhance2" value="" class="form-control" rows="1"></textarea></td>
                                </tr> 
                                <tr bgcolor="#FFFFFF">
                                    <td>3:</td>
                                    <td><textarea name="skillEnhance3" value="" class="form-control" rows="1"></textarea></td>
                                </tr>   
                            </table>
                            <p align="center" style="margin-top:10px;"><input type="submit" value="Submit Details" class="btn btn-primary btn-success" /></p>

                        </c:when>    
                        <c:otherwise>
                            <div style="width:800px;margin:0px auto;color:#FF0000;font-weight:bold;text-align:center;margin-top:10px;">
                                You have already applied for the Training Program.
                            </div>
                            <div id="training_detail"></div>
                        </c:otherwise>
                    </c:choose>
                </c:otherwise>
            </c:choose>
        </form>
        <script type="text/javascript">
            var obj = document.frmTraining;
        </script>
    </body>

</html>
