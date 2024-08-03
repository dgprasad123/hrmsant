<%-- 
    Document   : OfficeList
    Created on : Nov 18, 2017, 1:02:16 PM
    Author     : manisha
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        <link href="css/sb-admin.css" rel="stylesheet">
        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>
        <style>
            .close {
                color: white;
                float: right;
                font-size: 28px;
                font-weight: bold;
            }

            .close:hover,
            .close:focus {
                color: #000;
                text-decoration: none;
                cursor: pointer;
            }

            .modal-header {
                padding: 2px 16px;
                background-color: lightblue;
                color: white;
            }

            .modal-body {padding: 5px 30px;height: 100px;}

            .modal-footer {
                padding: 2px 16px;
                background-color: lightblue;
                color: white;
            }

            #myInput {
                background-image: url('/css/searchicon.png');
                background-position: 10px 10px;
                background-repeat: no-repeat;
                width: 100%;
                font-size: 16px;
                padding: 12px 20px 12px 40px;
                border: 1px solid #ddd;
                margin-bottom: 12px;
            }

            #myTable {
                border-collapse: collapse;
                width: 100%;
                border: 1px solid #ddd;
                font-size: 16px;
            }

            #myTable th, #myTable td {
                text-align: left;
                padding: 12px;
            }

            #myTable tr {
                border-bottom: 1px solid #ddd;
            }

            #myTable tr.header {
                background-color: darkslategrey;
                color: #FFFFFF;
            }
        </style>
        <script type="text/javascript">
            $(document).ready(function(){
               toggleCategory(); 
            });
            function validate() {
                var offType = $("#offType").val();

                if (offType == "") {
                    alert("Please select Office Type");
                    return false;
                } else if (offType != "") {
                    if (offType == "GOI" && $("#sltCategory").val() == "") {
                        alert("Please select Category");
                        return false;
                    }
                }
                $("#hiddenoffType").val(offType);
                var strOtherSPC = $("#strOtherSPC").val();
                strOtherSPC = strOtherSPC.trim();
                $("#strOtherSPC").val(strOtherSPC);
                var strOtherSPC = $("#strOtherSPC").val();
                //  alert(strOtherSPC.length);
                if (!isNaN(strOtherSPC)) {
                    alert("Invalid Office Name");
                    return false;
                }
                if (strOtherSPC == "") {
                    alert("Please Enter Office Name");
                    return false;
                }
            }

            function toggleCategory() {
                if ($("#offType").val() == "GOI") {
                    $("#categoryTR").show();
                    $("#departmentTR").hide();
                    $("#sltDept").val('');
                } else {
                    $("#departmentTR").show();
                    $("#categoryTR").hide();
                    $("#sltCategory").val('');
                }
            }
        </script>   
    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/hrmsadminmenu.jsp"/>        
            <div id="page-wrapper">
                <div class="container-fluid">
                    <!-- Page Heading -->
                    <div class="row">
                        <div class="col-lg-12">                            
                            <ol class="breadcrumb">
                                <li>
                                    <i class="fa fa-dashboard"></i>  <a href="index.html">Dashboard</a>
                                </li>
                                <li class="active">
                                    <i class="fa fa-file"></i> Other SPC 
                                </li>
                            </ol>
                        </div>
                    </div>
                    <div class="row">
                        <form:form action="getOtherSPCList.htm" commandName="OtherSPC" method="post">
                            <div class="col-lg-2">Office Type</div>
                            <div class="col-lg-6">
                                <form:select path="offType" class="form-control" onchange="toggleCategory();">
                                    <form:option value="">Select</form:option>
                                    <form:option value="SGO">State Govt. Organization</form:option>  
                                    <form:option value="GOI">Government Of India</form:option>  
                                </form:select>                            
                            </div>
                            <div class="col-lg-2"><button type="submit" class="form-control ">Search</button> </div>
                        </form:form>
                    </div>
                    <div class="pull-right"> 
                        <button type="button" class="btn btn-success" data-toggle="modal" data-target="#newSPC"  style="width:150px">Add New Office</button>                                        
                    </div>


                    <div class="row">
                        <div class="col-lg-12">
                            <h2>Other SPC List</h2>
                            <div class="table-responsive">
                                <table id="myTable" class="table table-striped table-bordered table-success" width="100%" cellspacing="0">
                                    <thead>
                                        <tr>
                                            <th>Sl No</th>
                                            <th>SPC</th>
                                            <th>Office Name</th>
                                            <th>Action</th>                                            
                                        </tr>
                                    </thead>
                                    <tbody>                                        
                                        <c:forEach items="${spcList}" var="office" varStatus="count">
                                            <tr>
                                                <td>${count.index + 1}</td>
                                                <td>${office.strOtherSPC}</td>
                                                <td>${office.offName}</td>
                                                <td>&nbsp;</td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                    <form:form action="saveOtherSPCList.htm" commandName="OtherSPC" method="post" onsubmit="return validate()">
                        <input type="hidden" id='hiddenoffType' name='hiddenoffType'/>
                        <div class="modal fade" id="newSPC" role="dialog">
                            <div class="modal-dialog">
                                <!-- Modal content-->

                                <div class="modal-content modal-lg">
                                    <div class="modal-header">
                                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                                        <h4 class="modal-title">Add New Office</h4>
                                    </div>
                                    <div class="modal-body">
                                        <div class=row">
                                            <table border="0" width="100%" cellspacing="0" style="font-size:12px; font-family:verdana;">
                                                <tr id="departmentTR">
                                                    <td style="text-align:left; font-size: 18px;padding-left:5px">
                                                        <label path="sltDept">Department</label>
                                                    </td>
                                                    <td style="padding-right:5px">
                                                        <form:select path="sltDept" id="sltDept" class="form-control" size="width:15px">                                                     
                                                            <form:option value="">--Select Department--</form:option>
                                                            <form:options items="${deptlist}" itemValue="deptCode" itemLabel="deptName"/>
                                                        </form:select>
                                                    </td>
                                                </tr>
                                                <tr id="categoryTR">
                                                    <td style="text-align:left; font-size: 18px;padding-left:5px">
                                                        <label path="sltCategory">Category<span style="color: red">*</span></label>
                                                    </td>
                                                    <td style="padding-right:5px">
                                                        <form:select path="sltCategory" class="form-control" size="width:20px">                                                     
                                                            <form:option value="">--Select--</form:option>
                                                            <form:option value="M">Ministry</form:option>
                                                            <form:option value="O">Others</form:option>
                                                        </form:select>
                                                    </td>
                                                </tr>
                                                <tr><br><br>                                           
                                                    <td style="text-align:left; font-size: 18px;padding-left:5px">
                                                        <form:label path="strOtherSPC">Enter Office Name</form:label>
                                                    </td>
                                                    <td style="padding-right:5px">
                                                        <form:input path="strOtherSPC" class="form-control" size="width:20px"/>                                                     
                                                    </td>
                                                </tr>
                                            </table>
                                        </div>
                                    </div>
                                    <br>
                                    <div class="modal-footer">
                                        <input type="submit" name="submit" class="btn btn-default" style="width:70px" value="Save"/> 
                                        <button type="button" class="btn btn-default" data-dismiss="modal" style="width:70px">Close</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </form:form>                            

                </div>
            </div>
        </div>
    </body>
</html>


