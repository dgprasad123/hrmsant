<%-- 
    Document   : BranchDetails
    Created on : Jul 3, 2018, 1:22:31 PM
    Author     : Madhusmita
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:::: HRMS, Government of Odisha ::::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        <link href="css/sb-admin.css" rel="stylesheet">
        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>
        <title>Bank Branch Details</title>
    </head>
    <style type="text/css">
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

        .modal-body {padding: 5px 30px;height: 200px;}

        .modal-footer {
            padding: 2px 16px;
            background-color: lightblue;
            color: white;
        }

        body {
            font-family: Arial;
            font-size: 17px;
            padding: 8px;
        }

        * {
            box-sizing: border-box;
        }

        .row {
            display: -ms-flexbox; /* IE10 */
            display: flex;
            -ms-flex-wrap: wrap; /* IE10 */
            flex-wrap: wrap;
            margin: 0 -16px;
        }

        .col-25 {
            -ms-flex: 25%; /* IE10 */
            flex: 25%;
        }

        .col-50 {
            -ms-flex: 50%; /* IE10 */
            flex: 50%;
        }

        .col-75 {
            -ms-flex: 75%; /* IE10 */
            flex: 75%;
        }

        .col-25,
        .col-50,
        .col-75 {
            padding: 0 16px;
        }

        .container {
            background-color: #f2f2f2;
            padding: 5px 20px 15px 20px;
            border: 1px solid lightgrey;
            border-radius: 3px;
        }

        input[type=text] {
            width: 100%;
            margin-bottom: 20px;
            padding: 12px;
            border: 1px solid #ccc;
            border-radius: 3px;
        }

        label {
            margin-bottom: 10px;
            display: block;
        }

        .icon-container {
            margin-bottom: 20px;
            padding: 7px 0;
            font-size: 24px;
        }

        .btn {
            background-color: #4CAF50;
            color: white;
            padding: 12px;
            margin: 10px 0;
            border: none;
            width: 100%;
            border-radius: 3px;
            cursor: pointer;
            font-size: 17px;
        }

        .btn:hover {
            background-color: #45a049;
        }

        a {
            color: #2196F3;
        }

        hr {
            border: 1px solid lightgrey;
        }

        span.price {
            float: right;
            color: grey;
        }        
        @media (max-width: 800px) {
            .row {
                flex-direction: column-reverse;
            }
            .col-25 {
                margin-bottom: 20px;
            }
            table{
                border-collapse: separate;
                border-spacing: 30px; /* Apply cell spacing */
            }
            table, th, td{
                border: 1px solid #666;
            }
            table th, table td,table tr {
                padding: 15px; /* Apply cell padding */
            }
            .center {
                margin: auto;
                width: 50%;
                border: 3px solid green;
                padding: 10px;
            }
            /* The Modal (background) */
            .modal {
                display: none; /* Hidden by default */
                position: fixed; /* Stay in place */
                z-index: 1; /* Sit on top */
                padding-top: 100px; /* Location of the box */
                left: 0;
                top: 0;
                width: 100%; /* Full width */
                height: 100%; /* Full height */
                overflow: auto; /* Enable scroll if needed */
                background-color: rgb(0,0,0); /* Fallback color */
                background-color: rgba(0,0,0,0.4); /* Black w/ opacity */
            }

            /* Modal Content */
            .modal-content {
                position: relative;
                background-color: #fefefe;
                margin: auto;
                padding: 0;
                border: 1px solid #888;
                width: 80%;
                box-shadow: 0 4px 8px 0 rgba(0,0,0,0.2),0 6px 20px 0 rgba(0,0,0,0.19);
                -webkit-animation-name: animatetop;
                -webkit-animation-duration: 0.4s;
                animation-name: animatetop;
                animation-duration: 0.4s
            }

            /* Add Animation */
            @-webkit-keyframes animatetop {
                from {top:-300px; opacity:0} 
                to {top:0; opacity:1}
            }

            @keyframes animatetop {
                from {top:-300px; opacity:0}
                to {top:0; opacity:1}
            }

            /* The Close Button */
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
                background-color: #5cb85c;
                color: white;
            }

            .modal-body {padding: 2px 16px;}

            .modal-footer {
                padding: 2px 16px;
                background-color: #5cb85c;
                color: white;
            }
        </style>
        <script>
            function getBrnchList(obj) {
                $('#branchname').empty();
                var bankcode = obj.value;
                if (bankcode != '') {
                    var url = 'bankbranchlistJSON.htm?bankcode=' + bankcode;
                    $('#branchname').append('<option value="">--Select Branch--</option>');
                    $.getJSON(url, function(data) {
                        $.each(data, function(i, obj) {
                            $('#branchname').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                        });
                    });
                } else {
                    $('#branchname').children().remove().end().append('<option selected value="">--Select Branch--<\/option>');
                }
            }
            function blankfield() {
                if ($("#mdlbranchname").val() == '') {
                    alert("Enter Branch Name");
                    ("#mdlbranchname").focus();
                    return false;
                }
                if ($("#mdlifsccode").val() == '') {
                    alert("Enter IFSC Code");
                    ("#mdlifsccode").focus();
                    return false;
                }

            }
        </script>
        <body>
            <div id="wrapper">
                <jsp:include page="../tab/hrmsadminmenu.jsp"/>       

                <div class="page-wrapper">
                    <div class="panel">
                        <div class="panel panel-default">
                            <div class="panel panel-primary">
                                <div class="panel panel-heading">BANK DETAILS</div>
                                <div class="panel-panel body">
                                    <form:form action="bankbranchController1.htm" commandName="bankbranchModel">
                                        <div class="float:right">
                                            <div class="row">
                                                <div class="col-50">

                                                </div>
                                                <div class="col-50">
                                                    <div class="pull-right">                                                        
                                                        <button type="button" class="btn btn-success" data-toggle="modal" data-target="#newBank"  style="width:150px">Add New Bank</button>
                                                        <button type="button" class="btn btn-success" data-toggle="modal" data-target="#newBranch"  style="width:150px">Add New Branch</button>
                                                    </div>

                                                </div>
                                            </div>
                                        </div>
                                        <div class="align-center">
                                            <div class="container-fluid">                                            
                                                <div class="row">                                                
                                                    <table border="0" width="100%"  cellspacing="0" style="font-size:12px; font-family:verdana;">
                                                        <tr style="height: 50px">
                                                            <td style="text-align:left; font-size: 18px;padding-left:15px">
                                                                <form:label path="bankname">BANK NAME</form:label>
                                                                </td>
                                                                <td>
                                                                <form:select class="form-control" id="bankcode" path="bankcode" onchange="getBrnchList(this)">
                                                                    <form:option value="" label="--Select Bank--"></form:option>
                                                                    <c:forEach items="${bank_list}" var="bank_list">
                                                                        <form:option value="${bank_list.bankcode}" label="${bank_list.bankname}"/>                                                                    
                                                                    </c:forEach>
                                                                </form:select>                                                            
                                                            </td>
                                                        </tr>
                                                        <tr style="height: 50px">
                                                            <td style="text-align:left; font-size: 18px; padding-left:15px">
                                                                <form:label path="branchname">BRANCH NAME</form:label>                                                            
                                                                </td>
                                                                <td>

                                                                <form:select path="branchname" id="branchname" class="form-control">
                                                                    <form:option value="" label="--Select Branch--"></form:option>
                                                                    <c:if test="${not empty branchList}">
                                                                        <c:forEach items="${branchList}" var="branchlist">
                                                                            <form:option value="${branchlist.value}" label="${branchlist.label}"/>                                                                    
                                                                        </c:forEach>
                                                                    </c:if>
                                                                </form:select>

                                                            </td>
                                                            <td style="padding-left:20px;">
                                                                <input type="submit" name="submit" class="form-control" value="Ok"/>                                        
                                                            </td>
                                                        </tr>                                                                                                   
                                                    </table>
                                                </div>
                                            </div>
                                        </div>
                                        <br>
                                        <div class="row">                                        
                                            <div class="col-50">
                                                <div class="pull-right">
                                                    <form:label path="ifsccode">IFSC CODE</form:label>
                                                    <form:input path="ifsccode" class="form-control" size="width:40px" style="width:300px" value="${BranchData.ifsccode}" placeholder="IFSC Code" readonly="true"/>
                                                </div>
                                            </div>
                                            <div class="col-50 pull-left">
                                                <form:label path="micrcode">MICR CODE</form:label>
                                                <form:input path="micrcode" class="form-control" size="width:40px" style="width:300px" value="${BranchData.micrcode}" placeholder="MICR Code" readonly="true"/>
                                            </div>                                        
                                        </div>
                                    </div>
                                    <br><br><br>

                                </div>
                            </div>                        
                        </div>                          
                    </div>                
                </div>
                <div class="container">                
                    <!-- Trigger the modal with a button -->               

                    <!-- Modal -->
                    <div class="modal fade" id="newBranch" role="dialog">
                        <div class="modal-dialog">
                            <!-- Modal content-->

                            <div class="modal-content modal-lg">
                                <div class="modal-header">
                                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                                    <h4 class="modal-title">Add New Branch</h4>
                                </div>
                                <div class="modal-body">
                                    <div class=row">
                                        <table border="0" width="100%"  cellspacing="0" style="font-size:12px; font-family:verdana;">
                                            <tr>                                           
                                                <td style="text-align:left; font-size: 18px;padding-left:15px">
                                                    <form:label path="mdlbankname">BANK NAME</form:label>
                                                    </td>
                                                    <td style="padding-right:20px">
                                                    <form:select class="form-control" id="mdlbankcode" path="mdlbankcode" onchange="getBrnchList(this)">
                                                        <form:option value="" label="--Select Bank--"></form:option>
                                                        <c:forEach items="${bank_list}" var="bank_list">
                                                            <form:option value="${bank_list.bankcode}" label="${bank_list.bankname}"/>                                                                    
                                                        </c:forEach>
                                                    </form:select>                                                       

                                                </td>
                                            </tr>
                                        </table>
                                    </div>
                                    <br>
                                    <div class="row align-center"> 
                                        <table border="0" width="70%"  cellspacing="0" style="font-size:12px; font-family:verdana; alignment-adjust: central">
                                            <tr>
                                                <td style="text-align:left; font-size: 12px;padding-left:70px">
                                                    <form:label path="mdlbranchname">BRANCH NAME <span class="add-on" style="color: red">*</span></form:label>

                                                    </td>
                                                    <td style="padding-right:20px">
                                                    <form:input path="mdlbranchname" class="form-control" size="width:20px"/>
                                                </td>

                                            </tr>
                                            <tr>
                                                <td style="text-align:left; font-size: 12px;padding-left:70px">
                                                    <form:label path="mdlifsccode">IFSC CODE <span class="add-on" style="color: red">*</span></form:label>
                                                    </td>
                                                    <td style="padding-right:20px">
                                                    <form:input path="mdlifsccode" class="form-control" size="width:10px" />
                                                </td>
                                            </tr>
                                            <tr>
                                                <td style="text-align:left; font-size: 12px;padding-left:70px">
                                                    <form:label path="mdlmicrcode">MICR CODE</form:label>
                                                    </td>
                                                    <td style="padding-right:20px">
                                                    <form:input path="mdlmicrcode" class="form-control" size="width:10px"/>
                                                </td>
                                            </tr>
                                        </table>
                                    </div>
                                </div>
                                <div class="modal-footer">
                                    <input type="submit" name="submit" class="btn btn-default" style="width:70px" value="Add" onclick="javascript: blankfield()"/> 
                                    <button type="button" class="btn btn-default" data-dismiss="modal" style="width:70px">Close</button>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="modal fade" id="newBank" role="dialog">
                        <div class="modal-dialog">
                            <!-- Modal content-->

                            <div class="modal-content modal-lg">
                                <div class="modal-header">
                                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                                    <h4 class="modal-title">Add New Bank</h4>
                                </div>
                                <div class="modal-body">
                                    <div class=row">
                                        <table border="0" width="100%"  cellspacing="0" style="font-size:12px; font-family:verdana;">
                                            <tr>                                           
                                                <td style="text-align:left; font-size: 18px;padding-left:5px">
                                                    <form:label path="mdlBnkName">ENTER BANK NAME:</form:label>
                                                    </td>
                                                    <td style="padding-right:5px">
                                                    <form:input path="mdlBnkName" class="form-control" size="width:20px"/>                                                     
                                                </td>
                                            </tr>
                                        </table>
                                    </div>
                                </div>
                                <br>
                                <div class="modal-footer">
                                    <input type="submit" name="submit" class="btn btn-default" style="width:70px" value="Save"/> 

                                </div>
                            </div>
                        </div>
                    </div>
                </form:form>
            </div>
        </div>
    </body>
</html>
