<%-- 
    Document   : SubmitIncrementProposal
    Created on : 11 Jul, 2016, 11:36:43 AM
    Author     : Surendra
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <!-- LAYOUT v 1.3.0 -->
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>

        <script type="text/javascript">
           

            function submitToAuthority() {
                auth = $('#auth').val();
                if (auth == '') {
                    alert('Please Select Approving Authority.');
                    return false;
                }
                auth1 = $('#auth1').val();
                if (auth1 == '') {
                    alert('Please Select Verifying Authority.');
                    return false;
                }
                if (confirm("Are you sure you want to submit the Proposal to the Authority?"))
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }

            $(document).ready(function() {
                //$('#txtdeptname').combobox('setValues', ['11','GENERAL ADMINISTARTION DEPARTMENT']);
                //var deptcode = $('#dept').combobox('getValue');
            });
            //Tushar
            
            function getDeptWiseOfficeList(type) {
                var deptcode;
                if(type == ""){
                    deptcode = $('#dept').val();
                    $('#office').empty();
                    $('#auth').empty();
                    $('#office').append('<option value="">--Select Office--</option>');
                }else if(type == "1"){
                    deptcode = $('#dept1').val();
                    $('#office1').empty();
                    $('#auth1').empty();
                    $('#office1').append('<option value="">--Select Office--</option>');
                }
                var url = 'getOfficeListJSON.htm?deptcode=' + deptcode;
                $.getJSON(url, function (data) {
                    $.each(data, function (i, obj) {
                        if(type == ''){
                            $('#office').append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');
                        }else if(type == 1){
                            $('#office1').append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');
                        }
                    });
                });
            }

            function getOfficeWisePostList(type) {
                var offcode;
                if (type == "") {
                    offcode = $('#office').val();
                    $('#auth').empty();
                } else if (type == "1") {
                    offcode = $('#office1').val();
                    $('#auth1').empty();
                }
                var url = 'getIncrementWisePostListJSON.htm?offcode=' + offcode;
                if (type == "") {
                    $('#auth').append('<option value="">--Select Post--</option>');
                } else if (type == "1") {
                    $('#auth1').append('<option value="">--Select Post--</option>');
                }
                $.getJSON(url, function (data) {
                    $.each(data, function (i, obj) {
                        if (type == "") {
                            $('#auth').append('<option value="' + obj.spc + '">' + obj.postname + '</option>');
                        } else if (type == "1") {
                            $('#auth1').append('<option value="' + obj.spc + '">' + obj.postname + '</option>');
                        }
                    });
                });
            }
        </script>
        <style type="text/css">
            .table td{padding:15px;}
        </style>
    </head>
    <body>
        <form action="submitProposalTask.htm" commandName="IncrementProposal" method="post">
            <input type="hidden" name="proposalId" value="${incrementForm.proposalId}"/>

            <!--start-->
            
            
            
<table class="table" style="font-family:Arial;margin-bottom:20px;font-size:11pt;border:1px solid #95B8E7;background:#FFFFFF;width:60%">
                <tr bgcolor="#E1ECFF" style="font-weight:bold;">
                    <td colspan="2">Verifying Authority</td>
                </tr>
                <tr>
                    <td width="20%" align="right"><label class="label-top">Department :</label></td>
                    <td>                    <select id="dept1" class="form-control" onchange="getDeptWiseOfficeList('1');">
                        <option value="">--Select Department--</option>
                                <c:forEach items="${deptlist}" var="dList">
                                    <option value="${dList.deptCode}">${dList.deptName}</option>
                                </c:forEach>
                     
                    </select></td>
                </tr>
                <tr>
                    <td align="right"><label class="label-top">Office :</label></td>
                    <td><select id="office1" class="form-control" onchange="getOfficeWisePostList('1');">
                        <option value="">--Select Office--</option>
                                                       
                    </select></td>
                </tr>
                <tr>
                    <td align="right"><label class="label-top">Post :</label></td>
                    <td><select id="auth1" name="authspc1" class="form-control">
                        <option value="">--Select Post--</option>
                                                        
                    </select></td>
                </tr>
            </table>    
            
<table class="table" style="font-family:Arial;margin-bottom:20px;font-size:11pt;border:1px solid #95B8E7;background:#FFFFFF;width:60%">
                <tr bgcolor="#E1ECFF" style="font-weight:bold;">
                    <td colspan="2">Approving Authority</td>
                </tr>
                <tr>
                    <td width="20%" align="right"><label class="label-top">Department :</label></td>
                    <td>                    <select id="dept" class="form-control" onchange="getDeptWiseOfficeList('');">
                        <option value="">--Select Department--</option>
                                <c:forEach items="${deptlist}" var="dList">
                                    <option value="${dList.deptCode}">${dList.deptName}</option>
                                </c:forEach>
                     
                    </select></td>
                </tr>
                <tr>
                    <td align="right"><label class="label-top">Office :</label></td>
                    <td><select id="office" class="form-control" onchange="getOfficeWisePostList('');">
                        <option value="">--Select Office--</option>
                                                       
                    </select></td>
                </tr>
                <tr>
                    <td align="right"><label class="label-top">Post :</label></td>
                    <td><select id="auth" name="authspc" class="form-control">
                        <option value="">--Select Post--</option>
                                                        
                    </select></td>
                </tr>
            </table>                
            

            <!--End-->

            

            <div id="dlg-buttons">
                <table cellpadding="0" cellspacing="0" style="width:100%">
                    <tr>
                        <td style="text-align:left">
                            <a href="displayProposalListpage.htm" class="btn btn-danger" iconCls="icon-cancel">Return</a>
                            <button type="submit" class="btn btn-primary" onclick="return submitToAuthority()">Submit to Authority</button>
                        </td>
                    </tr>
                </table>
            </div>
        </form>     

    </body>
</html>
