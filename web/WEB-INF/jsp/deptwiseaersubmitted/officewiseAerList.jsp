<%-- 
    Document   : officewiseAerList
    Created on : Jan 24, 2019, 12:53:20 PM
    Author     : manisha
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%
    String contextPath = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + contextPath + "/";
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:HRMS:</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        

        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>
        <style type="text/css">
            .star{
                color:#FF0000;
                font-size:15px;
            }
        </style>
        <script type="text/javascript">

            function getPost() {
                var deptCode = $('#hidDeptCode').val();
                var offCode = $('#hidOffCode').val();
                var offName = $('#officename').textbox('getValue');
                var spc = $('#hidPostCode').val();
                var spn = $('#postname').textbox('getValue');
                if (spc == '') {
                    alert('Please select Post.');
                } else {
                    window.opener.document.getElementById('office').innerHTML = offName;
                    window.opener.document.getElementById('hidOffice').value = offCode;
                    window.opener.document.getElementById('post').innerHTML = spn;
                    window.opener.document.getElementById('hidspc').value = spc;
                    window.close();
                }
            }

            function radioFuncDept(val, row) {
                var data = row.deptCode + ":" + row.deptName;
                return "<input type='radio' name='rddeptid' id='rddeptid' value='" + data + "' onclick='SelectDepartment(\"" + data + "\")'/>";
            }

            function radioFuncOffice(val, row) {
                var data = row.offCode + ":" + row.offName;
                return "<input type='radio' name='rdoffcode' id='rdoffcode' value='" + data + "' onclick='SelectOffice(\"" + data + "\")'/>";
            }

            function radioFuncPost(val, row) {
                var data = row.value + ":" + row.label;
                data = data.replace("'", "&rsquo;");
                return "<input type='radio' name='rdopostcode' id='rdopostcode' value='" + data + "' onclick='SelectPost(\"" + data + "\")'/>";
            }

            function SelectDepartment(data) {

                var radspl = data.split(':');
                var deptcode = radspl[0];
                var deptname = radspl[1];
                $('#deptDialog').dialog('close');
                $('#deptname').textbox('setValue', deptname);
                $('#hidDeptCode').val(deptcode);
            }

            function SelectOffice(data) {

                var radspl = data.split(':');
                var offcode = radspl[0];
                var offname = radspl[1];
                $('#officeDialog').dialog('close');
                $('#officename').textbox('setValue', offname);
                $('#hidOffCode').val(offcode);
            }

            function SelectPost(data) {

                var radspl = data.split(':');
                var spccode = radspl[0];
                var spn = radspl[1];
                $('#postDialog').dialog('close');
                $('#postname').textbox('setValue', spn);
                $('#hidPostCode').val(spccode);
            }

            function getEmployee() {

                var deptName = $('#deptname').val();
                var offCode = $('#offCode').val();
                var postCode = $('#postCode').val();
                if (offCode == '') {
                    alert("Please select an Office");
                    return false;
                }
                if (deptName == '') {
                    alert("Please select a Department");
                    return false;
                }

                if (postCode == '') {
                    alert("Please select a Post");
                    return false;
                }

                if (postCode != '') {
                    var url = 'getEmployeeNameWithSPCJSON.htm?offcode=' + offCode + '&postCode=' + postCode;
                    $('#empspclist').find('tbody').remove();
                    $.getJSON(url, function(result) {
                        $.each(result, function(i, emp) {
                            var trhtml = '<tr>';
                            trhtml = trhtml + '<td><input type="radio" onclick="selectemployee(this)" data-dismiss="modal" name="applyTo" value="' + emp.empid + '-' + emp.spc + '"></td>';
                            trhtml = trhtml + '<td>' + emp.empname + '</td>';
                            trhtml = trhtml + '<td>' + emp.spn + '</td>';
                            trhtml = trhtml + '</tr><input type="hidden" name="post_' + emp.empid + '-' + emp.spc + '" id="post_' + emp.empid + '-' + emp.spc + '" value="' + emp.empname + '<==>' + emp.spn + '" />';
                            $('#empspclist').append(trhtml);
                        });
                    });

                } else {
                    alert("Select a Post");
                    return false;
                }



            }
            function forwardDP() {
                var row = $('#empListDg').datagrid('getSelected');
                if (row) {
                    $('#hidForwardHrmsId').val(row.doHrmsId);
                } else {
                    alert("Select an Employee");
                    return false;
                }
            }

            function radioFuncEmp(val, row) {
                var data = row.doHrmsId;
                return "<input type='radio' name='rdEmpid' id='rdEmpid' value='" + data + "'/>";
            }


            $(document).ready(function() {

                $("#deptCode").change(function() {
                    $('#offCode').empty();
                    $('#offCode').append($('<option>', {
                                value: "",
                                text: "--Select--"
                            }));
                    var url = 'getOfficeListJSON.htm?deptcode=' + this.value;
                    $.getJSON(url, function(result) {
                        $.each(result, function(i, field) {
                            $('#offCode').append($('<option>', {
                                value: field.offCode,
                                text: field.offName
                            }));
                        });
                    });                    
                });
                
                
                $("#offCode").change(function() {
                    var url = 'getOfficeWisePostListJSON.htm?deptCode=' + this.value;
                    $.getJSON(url, function(result) {
                        $('#postCode').empty();
                        $.each(result, function(i, field) {
                            $('#postCode').append($('<option>', {
                                value: field.postcode,
                                text: field.post
                            }));
                        });
                    });
                });

            });

        </script>
    </head>
    <body>

        <form:form action="aerauthorizationsearch.htm" method="POST" class="form-horizontal" commandName="aerAuthorizationBean">


            <form:hidden path="action"/>
            <div class="panel panel-default">
                <div class="panel-heading">Select Emp List</div>
                <div class="panel-body">
                    <div class="form-group">
                        <label class="control-label col-sm-2" for="email">Department:</label>
                        <div class="col-sm-10">
                            <form:select path="deptCode" id="deptCode" cssClass="form-control" style="width:450px;">
                                <form:option value="">Select</form:option>
                                <form:options items="${departmentList}" itemLabel="deptName" itemValue="deptCode"/>                                    
                            </form:select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="control-label col-sm-2" for="email">Office:</label>
                        <div class="col-sm-10">
                            <form:select path="offCode" id="offCode" cssClass="form-control" style="width:450px;">
                                <form:option value="">Select</form:option>
                                <form:options items="${officeList}" itemLabel="offName" itemValue="offCode"/>
                            </form:select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="control-label col-sm-2" for="email">Post:</label>
                        <div class="col-sm-10">
                            <form:select path="postCode" id="postCode" cssClass="form-control" style="width:450px;"> 
                                <form:option value="">Select</form:option>
                                <form:options items="${postList}" itemLabel="post" itemValue="postcode"/>
                            </form:select>
                        </div>
                    </div>

                </div>                    

                <div class="panel-footer"><input type="button" class="btn btn-primary" name="action" value="Search" onClick="getEmployee()"/></div>
            </div>            
            <div class="panel panel-default">
                <div class="panel-heading"><b>NAME LIST</b></div>
                <div class="panel-body">
                    <table class="table table-bordered" id="empspclist">
                        <thead>
                            <tr>
                                <th width="5%">Select</th>
                                <th><span style="font-weight: bold;"> NAME</span></th>
                                <th width="45%">post</th>
                            </tr>
                        </thead>
                        <tbody>                                        

                        </tbody>

                    </table>
                </div>                
            </div>
        </form:form>
    </body>
</html>
