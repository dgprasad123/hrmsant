<%-- 
    Document   : SectionDefination
    Created on : Nov 21, 2016, 3:12:08 PM
    Author     : Manas Jena
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>      
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">
        <!-- LAYOUT v 1.3.0 -->
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>        

        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>

        <script type="text/javascript">

            function getMajorHeadList(rec) {
                $('#majorHead').empty();
                if (rec) {
                    var url = 'getMajorHeadList.htm?demandNo=' + rec;

                    $('#majorHead').append('<option value="">-- Select --</option>');
                    $.getJSON(url, function (data) {
                        $.each(data, function (i, obj) {
                            $('#majorHead').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                        });
                    });

                }
            }
            function getSubMajorHeadList(rec) {
                $('#subMajorHead').empty();
                if (rec) {
                    var demandNo = $('#demandNo').val();
                    var url = 'getSubMajorHeadList.htm?demandNo=' + demandNo + '&majorhead=' + rec;
                    //$('#subMajorHead').combobox('reload', url);
                    $('#subMajorHead').append('<option value="">-- Select --</option>');
                    $.getJSON(url, function (data) {
                        $.each(data, function (i, obj) {
                            $('#subMajorHead').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                        });
                    });


                }
            }
            function getMinorHeadList(rec) {
                $('#minorHead').empty();
                if (rec) {
                    var majorHead = $('#majorHead').val();
                    var url = 'getMinorHeadList.htm?submajorhead=' + rec + '&majorhead=' + majorHead;
                    // $('#minorHead').combobox('reload', url);
                    $('#minorHead').append('<option value="">-- Select --</option>');
                    $.getJSON(url, function (data) {
                        $.each(data, function (i, obj) {
                            $('#minorHead').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                        });
                    });

                }
            }
            function getSubMinorHeadList(rec) {
                $('#subMinorHead1').empty();
                if (rec) {
                    var subMajorHead = $('#subMajorHead').val();
                    var url = 'getSubMinorHeadList.htm?minorHead=' + rec + '&submajorhead=' + subMajorHead;
                   // alert(url);
                    $('#subMinorHead1').append('<option value="">-- Select --</option>');
                    $.getJSON(url, function (data) {
                        $.each(data, function (i, obj) {
                            $('#subMinorHead1').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                        });
                    });
                }
            }
            function getDetailHeadList(rec) {
                $('#subMinorHead2').empty();
                if (rec) {
                    var minorHead = $('#minorHead').val();
                    var url = 'getDetailHeadList.htm?minorhead=' + minorHead + '&subhead=' + rec;
                    $('#subMinorHead2').append('<option value="">-- Select --</option>');
                    $.getJSON(url, function (data) {
                        $.each(data, function (i, obj) {
                            $('#subMinorHead2').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                        });
                    });
                }
            }
            function getChargedVotedList(rec) {
                $('#subMinorHead3').empty();
                if (rec) {
                    var subMinorHead1 = $('#subMinorHead1').val();
                    var url = 'getChargedVotedList.htm?detailhead=' + rec + '&subminorhead=' + subMinorHead1;
                    $('#subMinorHead3').append('<option value="">-- Select --</option>');
                    $.getJSON(url, function (data) {
                        $.each(data, function (i, obj) {
                            $('#subMinorHead3').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                        });
                    });
                }
            }
            function validate(){
                var billgroupdesc=$("#billgroupdesc").val();
                if(billgroupdesc==""){
                    alert("Group Description Cannot be blank");
                    return false;
                } 
                var plan=$("#plan").val();
                if(billgroupdesc==""){
                    alert("Please select Plan");
                    return false;
                }
                var sector=$("#sector").val();
                 if(sector==""){
                    alert("Please select Sector");
                    return false;
                }
                 var postclass=$("#postclass").val();
                 if(postclass==""){
                    alert("Please select Postclass");
                    return false;
                }
                 var payhead=$("#payhead").val();
                if(payhead==""){
                    alert("payhead Cannot be blank");
                    return false;
                }
               
            }

        </script>

    </head>
    <body>
        <div class="container-fluid">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <div class="row">
                        <div class="col-lg-12">

                        </div>
                    </div>
                </div>
                <div class="panel-body">
                    <form:form class="form-inline" action="saveGroupSection.htm" method="POST" onsubmit="return validate();" >
                        <table class="table table-bordered">
                            <tr style="font-weight:bold;background:#EAEAEA">
                                <td colspan="4">Manage Group

                            </tr>
                            <tr>
                                <td align="right">Group Description :<strong style='color:red'>*</strong></td>
                                <td> <input name="billgroupdesc" id="billgroupdesc" class="form-control" ></td>
                                <td align="right">Plan Status :<strong style='color:red'>*</strong></td>
                                <td>
                                    <select name="plan" id="plan"  size="1" class="form-control"  style="width:80%;" >
                                        <option value="">-Select-</option>
                                        <c:forEach items="${planStatusList}" var="plan" >
                                            <option value="${plan.value}">${plan.label}</option>
                                        </c:forEach>    

                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td align="right">Sector :<strong style='color:red'>*</strong></td>
                                <td>
                                    <select name="sector" id="sector"  size="1"  class="form-control"  style="width:80%;">
                                        <option value="">-Select-</option>
                                        <c:forEach items="${billSectorList}" var="sector" >
                                            <option value="${sector.value}">${sector.label}</option>
                                        </c:forEach>

                                    </select>
                                </td>
                                <td align="right">Post Class :<strong style='color:red'>*</strong></td>
                                <td>
                                    <select name="postclass" id="postclass"  size="1"  class="form-control"  style="width:80%;">
                                        <option value="">-Select-</option>
                                        <c:forEach items="${billPostClassList}" var="post" >
                                            <option value="${post.value}">${post.label}</option>
                                        </c:forEach>

                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td align="right">Bill Type :<strong style='color:red'>*</strong></td>
                                <td >
                                    <select name="billtype" id="billtype"  size="1"  class="form-control"  style="width:80%;">
                                        <option value="">-Select-</option>
                                        <c:forEach items="${billTypeList}" var="bill" >
                                            <option value="${bill.value}">${bill.label}</option>
                                        </c:forEach>

                                    </select>
                                </td>
                                <td align="right">Pay Head :<strong style='color:red'>*</strong></td>
                                <td>
                                    <input name="payhead" id="payhead" class="form-control" >
                                </td>

                            </tr>

                            <tr>
                                <th colspan=4> <div style="margin-bottom:10px;font-size:14px;border-bottom:1px solid #ccc">Account Heads</div></th>
                            </tr>
                            <tr>
                                <td align="right">Demand No :<strong style='color:red'>*</strong></td>
                                <td>
                                    <select name="demandNo" id="demandNo" size="1"  onchange="getMajorHeadList(this.value)"   class="form-control"  style="width:80%;">
                                        <option value="">-Select-</option>
                                        <c:forEach items="${billGroupList}" var="demand" >
                                            <option value="${demand.value}">${demand.label}</option>
                                        </c:forEach>

                                    </select>
                                </td>
                                <td align="right">Major :<strong style='color:red'>*</strong></td>
                                <td>
                                    <select name="majorHead" id="majorHead"  size="1" style="width:80%;"  class="form-control"  style="width:80%;"  onchange="getSubMajorHeadList(this.value)" >
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td align="right">Sub Major :<strong style='color:red'>*</strong></td>
                                <td>
                                    <select name="subMajorHead" id="subMajorHead" style="width:80%;"  size="1" class="form-control"  style="width:80%;" onchange="getMinorHeadList(this.value)">
                                    </select>
                                </td>
                                <td align="right">Minor :<strong style='color:red'>*</strong></td>
                                <td>
                                    <select name="minorHead" id="minorHead"  size="1"  class="form-control"  style="width:80%;"  onchange="getSubMinorHeadList(this.value)"   >
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td align="right">Sub :<strong style='color:red'>*</strong></td>
                                <td>
                                    <select  name="subMinorHead1" id="subMinorHead1"  class="form-control"  style="width:80%;"  onchange="getDetailHeadList(this.value)" >
                                    </select>
                                </td>
                                <td align="right">Detail :<strong style='color:red'>*</strong></td>
                                <td>
                                    <select name="subMinorHead2" id="subMinorHead2" class="form-control"  style="width:80%;"  onchange="getChargedVotedList(this.value)"  >
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td align="right">Charged(2) Voted(1) :<strong style='color:red'>*</strong></td>
                                <td colspan=3>
                                    <select  name="subMinorHead3" id="subMinorHead3"  class="form-control"  style="width:80%;"  >
                                    </select>
                                </td>

                            </tr>
                        </table>
                        <div class="panel-footer">                    
                            <input type="submit" name="action" value="Save Group" class="btn btn-success" />
                            <input type="submit" name="action" value="Back" class="btn btn-success"/>
                        </div>
                    </form:form>
                </div>

            </div>
        </div>
    </body>
</html>
