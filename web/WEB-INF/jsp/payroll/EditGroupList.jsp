<%-- 
    Document   : SectionDefination
    Created on : Nov 21, 2016, 3:12:08 PM
    Author     : Manas Jena
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>      
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript">
            $(document).ready(function () {
                $('#payhead').on("paste", function (e) {
                    e.preventDefault();
                });
            });
            function getMajorHeadList(rec) {
                $('#majorHead').empty();
                if (rec) {
                    var url = 'getMajorHeadList.htm?demandNo=' + rec;

                    $('#majorHead').append('<option value="">-- Select --</option>');
                    $.getJSON(url, function (data) {
                        $.each(data, function (i, obj) {
                            $('#majorHead').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                        });
                        $('#majorHead').val('${billGroup.majorHead}');
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
                        $('#subMajorHead').val('${billGroup.subMajorHead}');
                    });


                }
            }
            function getMinorHeadList(rec) {
                $('#minorHead').empty();
                if (rec) {
                    var majorHead = $('#majorHead').val();

                    if (majorHead) {
                        majorHead = $('#majorHead').val();
                    } else {
                        majorHead = '${billGroup.majorHead}';
                    }

                    var url = 'getMinorHeadList.htm?submajorhead=' + rec + '&majorhead=' + majorHead;
                    // $('#minorHead').combobox('reload', url);
                    $('#minorHead').append('<option value="">-- Select --</option>');
                    $.getJSON(url, function (data) {
                        $.each(data, function (i, obj) {
                            $('#minorHead').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                        });
                        $('#minorHead').val('${billGroup.minorHead}');
                    });

                }
            }
            function getSubMinorHeadList(rec) {
                $('#subMinorHead1').empty();
                if (rec) {
                    var subMajorHead = $('#subMajorHead').val();

                    if (subMajorHead) {
                        subMajorHead = $('#subMajorHead').val();
                    } else {
                        subMajorHead = '${billGroup.subMajorHead}';
                    }
                    var url = 'getSubMinorHeadList.htm?minorHead=' + rec + '&submajorhead=' + subMajorHead;
                    // alert(url);
                    $('#subMinorHead1').append('<option value="">-- Select --</option>');
                    $.getJSON(url, function (data) {
                        $.each(data, function (i, obj) {
                            $('#subMinorHead1').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                        });
                        $('#subMinorHead1').val('${billGroup.subMinorHead1}');
                    });
                }
            }
            function getDetailHeadList(rec) {
                $('#subMinorHead2').empty();
                if (rec) {
                    var minorHead = $('#minorHead').val();
                    if (minorHead) {
                        var minorHead = $('#minorHead').val();
                    } else {
                        var minorHead = '${billGroup.minorHead}';
                    }

                    var url = 'getDetailHeadList.htm?minorhead=' + minorHead + '&subhead=' + rec;
                    //   alert(url);
                    $('#subMinorHead2').append('<option value="">-- Select --</option>');
                    $.getJSON(url, function (data) {
                        $.each(data, function (i, obj) {
                            $('#subMinorHead2').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                        });
                        $('#subMinorHead2').val('${billGroup.subMinorHead2}');
                    });
                }
            }
            function getChargedVotedList(rec) {
                $('#subMinorHead3').empty();
                if (rec) {
                    var subMinorHead1 = $('#subMinorHead1').val();
                    if (subMinorHead1) {
                        subMinorHead1 = $('#subMinorHead1').val();
                    } else {
                        subMinorHead1 = '${billGroup.subMinorHead1}';
                    }

                    var url = 'getChargedVotedList.htm?detailhead=' + rec + '&subminorhead=' + subMinorHead1;
                    $('#subMinorHead3').append('<option value="">-- Select --</option>');
                    $.getJSON(url, function (data) {
                        $.each(data, function (i, obj) {
                            $('#subMinorHead3').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                        });
                        $('#subMinorHead3').val('${billGroup.subMinorHead3}');
                    });
                }
            }
            function AvoidSpace(event) {
                var k = event ? event.which : window.event.keyCode;
                if (k == 32)
                    return false;
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
                    <form class="form-inline" action="updateGroupSection.htm" method="POST" >
                        <input type='hidden' name='billgroupid' value='${billGroup.billgroupid}'/>
                        <table class="table table-bordered">
                            <tr style="font-weight:bold;background:#EAEAEA">
                                <td colspan="4">Manage Group

                            </tr>
                            <tr>
                                <td align="right">Group Description :</td>
                                <td> <input name="billgroupdesc" id="billgroupdesc" class="form-control" value="${billGroup.billgroupdesc}" required="true" ></td>
                                <td align="right">Plan Status :</td>
                                <td>
                                    <select name="plan" id="plan"  size="1" class="form-control"  style="width:80%;"  required>
                                        <option value="">-Select-</option>
                                        <c:forEach items="${planStatusList}" var="plan" >
                                            <option value="${plan.value}"<c:if test="${not empty billGroup.plan && billGroup.plan == plan.value}"> selected</c:if>>${plan.label}</option>
                                        </c:forEach>    

                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td align="right">Sector :</td>
                                <td>
                                    <select name="sector" id="sector"  size="1"  class="form-control"  style="width:80%;"  required>
                                        <option value="">-Select-</option>
                                        <c:forEach items="${billSectorList}" var="sector" >
                                            <option value="${sector.value}"<c:if test="${not empty billGroup.sector && billGroup.sector == sector.value}"> selected</c:if>>${sector.label}</option>
                                        </c:forEach>

                                    </select>
                                </td>
                                <td align="right">Post Class :</td>
                                <td>
                                    <select name="postclass" id="postclass"  size="1"  class="form-control"  style="width:80%;"  required>
                                        <option value="">-Select-</option>
                                        <c:forEach items="${billPostClassList}" var="post" >
                                            <option value="${post.value}"<c:if test="${not empty billGroup.postclass && billGroup.postclass == post.value}"> selected</c:if>>${post.label}</option>
                                        </c:forEach>

                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td align="right">Bill Type :</td>
                                <td >
                                    <select name="billtype" id="billtype"  size="1"  class="form-control"  style="width:80%;"  required>
                                        <option value="">-Select-</option>
                                        <c:forEach items="${billTypeList}" var="bill" >
                                            <option value="${bill.value}"<c:if test="${not empty billGroup.billtype && billGroup.billtype == bill.value}"> selected</c:if>>${bill.label}</option>
                                        </c:forEach>

                                    </select>
                                </td>
                                <td align="right">Pay Head :<strong style='color:red'>*</strong></td>
                                <td>
                                    <input name="payhead" id="payhead" class="form-control" value="${billGroup.payhead}" maxlength="3" onkeypress="return AvoidSpace(event)">
                                </td>

                            </tr>

                            <tr>
                                <th colspan=4> <div style="margin-bottom:10px;font-size:14px;border-bottom:1px solid #ccc">Account Heads</div></th>
                            </tr>
                            <tr>
                                <td align="right">Demand No :</td>
                                <td>
                                    <select name="demandNo" id="demandNo" size="1"  onchange="getMajorHeadList(this.value)"   class="form-control"  style="width:80%;"  required>
                                        <option value="">-Select-</option>
                                        <c:forEach items="${billGroupList}" var="demand" >
                                            <option value="${demand.value}"<c:if test="${not empty billGroup.demandNo && billGroup.demandNo == demand.value}"> selected</c:if>>${demand.label}</option>
                                        </c:forEach>

                                    </select>
                                </td>
                                <td align="right">Major :</td>
                                <td>
                                    <select name="majorHead" id="majorHead"  size="1" style="width:80%;"  class="form-control"  style="width:80%;"  required  onchange="getSubMajorHeadList(this.value)" >
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td align="right">Sub Major :</td>
                                <td>
                                    <select name="subMajorHead" id="subMajorHead" style="width:80%;"  size="1" class="form-control"  style="width:80%;"  required  onchange="getMinorHeadList(this.value)">
                                    </select>
                                </td>
                                <td align="right">Minor :</td>
                                <td>
                                    <select name="minorHead" id="minorHead"  size="1"  class="form-control"  style="width:80%;"  required  onchange="getSubMinorHeadList(this.value)"   >
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td align="right">Sub :</td>
                                <td>
                                    <select  name="subMinorHead1" id="subMinorHead1"  class="form-control"  style="width:80%;"  required  onchange="getDetailHeadList(this.value)" >
                                    </select>
                                </td>
                                <td align="right">Detail :</td>
                                <td>
                                    <select name="subMinorHead2" id="subMinorHead2" class="form-control"  style="width:80%;"  required  onchange="getChargedVotedList(this.value)"  >
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td align="right">Charged(2) Voted(1) :</td>
                                <td colspan=3>
                                    <select  name="subMinorHead3" id="subMinorHead3"  class="form-control"  style="width:80%;"  required  >
                                    </select>
                                </td>

                            </tr>
                            <tr>
                                <td align="right">Select CO Name :</td>
                                <td colspan=3>
                                    <select name="sltCOList" class="form-control">
                                        <option value="">--Select--</option>
                                        <c:if test="${not empty colist}">
                                            <c:forEach items="${colist}" var="list" >
                                                <option value="${list.value}"<c:if test="${not empty billGroup.sltCOList && billGroup.sltCOList == list.value}"> selected</c:if>>${list.label}</option>
                                            </c:forEach>
                                        </c:if>
                                    </select>
                                </td>

                            </tr>
                        </table>
                        <div class="panel-footer">                    
                            <input type="submit" value="Save Group" class="btn btn-success" />
                            <input type="submit" name="action" value="Back" class="btn btn-success"/>
                        </div>
                    </form>
                </div>

            </div>
        </div>
        <script type="text/javascript">
            $(document).ready(function () {
                getMajorHeadList('${billGroup.demandNo}');
                getSubMajorHeadList('${billGroup.majorHead}');
                getMinorHeadList('${billGroup.subMajorHead}');
                getSubMinorHeadList('${billGroup.minorHead}');
                getDetailHeadList('${billGroup.subMinorHead1}');
                getChargedVotedList('${billGroup.subMinorHead2}');
            });
        </script>
    </body>
</html>
