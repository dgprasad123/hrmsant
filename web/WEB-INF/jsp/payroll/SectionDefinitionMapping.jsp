<%-- 
    Document   : SectionDefinitionMapping
    Created on : Oct 25, 2017, 4:25:39 AM
    Author     : Manas
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>      
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <!-- LAYOUT v 1.3.0 -->
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>        
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script src="js/common.js" type="text/javascript"></script>
        <script src="js/moment.js" type="text/javascript"></script>        
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script type="text/javascript">
            function addDivdata() {
                var checkBoxName = "chkAvail";
                var checkBoxes = $("input[name=" + checkBoxName + "]");
                var checkBoxlength = $("input[name=" + checkBoxName + "]:checked").length;
                var checkBoxName2 = "chkAssaign";
                var chkAssaign = "'chkAssaign'";
                var checkBoxes2 = $("input[name=" + checkBoxName2 + "]");
                var len = checkBoxes2.length;
                var remove = "'remove'";
                var allowPostMap = "N";

                var checkboxarray = new Array();
                var i = 0;
                $("input:checked").each(function()
                {
                    var textboxobj = $(this).next();
                    var txtboxId = textboxobj.attr("id");
                    var va = document.getElementById(txtboxId).value;

                    checkboxarray[i] = va;
                    i++;
                });
                checkboxarray.sort(numOrdA);
                for (i = 0; i < checkboxarray.length; i++) {
                    $("input:checked").each(function() {
                        var textboxobj = $(this).next();
                        var txtboxId = textboxobj.attr("id");
                        var va = document.getElementById(txtboxId).value;
                        if (va == checkboxarray[i]) {
                            var curVal = $(this).val();
                            var verifyurl = "verifyAssignPostAction.htm?spc=" + $(this).val() + "&sectionId=" + $("#hidsecId").val();
                            $.getJSON(verifyurl).done(function(data) {
                                //alert(data.status);
                                allowPostMap = data.status;
                                if (allowPostMap == "N") {
                                    alert("Account Type Mismatch");
                                } else if (allowPostMap == "Y") {
                                    len = parseInt(len) + 1;
                                    var chkId = "chk" + len;
                                    $('#assigned').append('<div id="' + curVal + '" style="padding:5px;"> <input type="checkbox" name="' + checkBoxName2 + '" value="' + curVal + '" id="' + len + '" onclick="restrict2SelectCheckbox(' + chkAssaign + ',' + remove + ',' + len + ')"/>' + $("#" + curVal).text() + '<a href="javascript:showSPCOrder(\'' + curVal + '\')">Update OrderDate<\/a><\/div>');
                                    //$('#assigned').append('<div id="' + curVal + '" style="padding:5px;"> <input type="checkbox" name="' + checkBoxName2 + '" value="' + curVal + '" id="' + len + '" onclick="restrict2SelectCheckbox(' + chkAssaign + ',' + remove + ',' + len + ')"/>' + $("#" + curVal).text() + '<a href="javascript:showSPCOrder(\'' + curVal + '\')">Update OrderDate<\/a><\/div>');
                                    var dataString = 'spc=' + curVal + '&sectionId=' + $("#hidsecId").val();
                                    $.ajax({
                                        type: "POST",
                                        url: 'assignPostAction.htm',
                                        data: dataString,
                                        async: false,
                                        cache: false,
                                        success: function(html) {

                                        }
                                    });
                                    $("#" + curVal).remove();
                                }
                            });
                        }
                    });
                }
                //if (allowPostMap == 'Y') {
                $('input[name=chkAvail]').attr('checked', false);
                len = 1;
                $('input[type=checkbox]').each(function() {
                    len++;
                });
                //}
            }

            function removeDivdata() {
                var checkBoxName = "chkAssaign";
                var checkBoxes = $("input[name=" + checkBoxName + "]");

                var checkBoxName2 = "chkAvail";
                var checkBoxes2 = $("input[name=" + checkBoxName2 + "]");
                var add = "'add'";
                var chkAvail = "'chkAvail'";
                var len = checkBoxes2.length;
                $.each(checkBoxes, function() {
                    if ($(this).prop('checked') == true) {
                        len = parseInt(len) + 1;
                        var divid = $(this).val();
                        $("#" + $(this).val() + " a").remove();
                        var divText = $("#" + $(this).val()).text();
                        var dataString = 'spc=' + $(this).val();
                        var tempid = "temp" + len;
                        $.ajax({
                            type: "POST",
                            url: 'removePostAction.htm',
                            data: dataString,
                            async: false,
                            cache: false,
                            success: function(html) {

                                $("#" + divid).remove();
                                $('#availemp').append('<div id="' + divid + '" style="padding:5px;" > <input type="checkbox" name="' + checkBoxName2 + '" value="' + divid + '" id="' + len + '" onclick="restrict2SelectCheckbox(' + chkAvail + ',' + add + ',' + len + ');getCheckOrder(this)"/>' + divText + ' <input type="hidden" name="temp" id="' + tempid + '" value=""/><\/div>');

                            }
                        });

                    }

                });

                $('input[name=chkAssaign]').attr('checked', false);

                len = 0;
                $('input[type=text]').each(function() {
                    len++;
                    $(this).val(len);
                });
            }



            function restrict2SelectCheckbox(name, value, id) {
                var checkBoxlength = $("input[name=" + name + "]:checked").length;

                if (checkBoxlength > 20) {
                    var str = "You cannot " + value + " more than 20 post";
                    alert(str);
                    document.getElementById(id).checked = false;
                }
            }

            function numOrdA(a, b) {
                return (a - b);
            }
            function numOrdD(a, b) {
                return (b - a);
            }
            function getCheckOrder(obj) {
                if (obj.checked == true) {
                    var objCurr = obj.value;
                    var divId = $("#" + objCurr + " input:first").next();
                    var txtboxid = divId.attr("id");

                    var tempVal = $("#hidPageno").val();
                    document.getElementById(txtboxid).value = tempVal;
                    tempVal = parseInt(tempVal) + 1;
                    $("#hidPageno").val(tempVal);
                }
            }


            function moveupdiv() {

                var checkBoxName = "chkAssaign";
                var checkBoxlength = $("input[name=" + checkBoxName + "]:checked").length;
                if (checkBoxlength == 0) {
                    alert('Please select post to move up');
                    return false;
                }
                if (checkBoxlength > 1) {
                    var str = "You cannot move more than one post at a time";
                    alert(str);
                    return false;
                }



                $("input[name=" + checkBoxName + "]:checked").each(function()
                {
                    var txtObj = $(this).next();
                    var txtId = $(txtObj).attr("id");
                    var txtval = $("#" + txtId).val();
                    if (txtval == '1') {
                        alert('The first entry in the list cannot be moved up.');
                        return false;
                    } else {
                        var objCurr = $(this).parent();

                        var objPrev = $(this).parent().prev();
                        $(objPrev).insertAfter($(objCurr));


                    }
                });

            }


            function movedowndiv() {
                var checkBoxName = "chkAssaign";
                var checkBoxlength = $("input[name=" + checkBoxName + "]:checked").length;
                if (checkBoxlength == 0) {
                    alert('Please select post to move down');
                    return false;
                }
                if (checkBoxlength > 1) {
                    var str = "You cannot move more than one post at a time";
                    alert(str);
                    return false;
                }
                $("input[name=" + checkBoxName + "]:checked").each(function()
                {


                    var objCurr = $(this).parent();
                    var objNext = $(this).parent().next();
                    if (objNext.length > 0) {
                        $(objCurr).insertAfter($(objNext));
                    } else {
                        alert("You have reached last position");
                    }

                });

            }

            function saveHirrarchy() {
                var slno = 0;
                var t = "";
                $("input[name=chkAssaign]").each(function() {
                    tid = $(this).attr("id");
                    if (tid != slno) {
                        if (t != '') {
                            t = t + "-" + $(this).val() + "@" + (slno + 1);
                        } else {
                            t = $(this).val() + "@" + (slno + 1);
                        }


                        //var chkobj = [];
                        //chkobj.push($(this).val(), slno + 1);
                        //favorite.push(chkobj);
                    }
                    slno++;
                });
                var empType = $("#hidEmpType").val();
                if (t != '') {
                    var htmlData = '';

                    $.ajax({
                        type: "GET",
                        contentType: "application/json",
                        dataType: "json",
                        url: 'setPositionPostUpDown.htm?sectionType=' + empType + '&positionNo=' + t + '&sectionId=' + $("#hidsecId").val(),
                        cache: false,
                        //data: dataString,
                        success: function(data) {

                            for (i = 0; i < data.obj.length; i++) {
                                if (htmlData != '') {
                                    htmlData = htmlData + '<div class="ui-state-default" style="padding:5px;margin-bottom:5px;" id="' + data.obj[i].spc + '">' + (parseInt(i) + 1) + ' <input type="checkbox" value="' + data.obj[i].spc + '" id="' + i + '" name="chkAssaign" onclick="restrict2SelectCheckbox(\'chkAssaign\', \'remove\', \'' + i + '\')"/> ' + data.obj[i].spn + ' <\/div>';
                                } else {
                                    htmlData = '<div class="ui-state-default" style="padding:5px;margin-bottom:5px;" id="' + data.obj[i].spc + '">' + (parseInt(i) + 1) + ' <input type="checkbox" value="' + data.obj[i].spc + '" id="' + i + '" name="chkAssaign" onclick="restrict2SelectCheckbox(\'chkAssaign\', \'remove\', \'' + i + '\')"/> ' + data.obj[i].spn + '<\/div>';
                                }

                            }


                            $("#assigned").html(htmlData);
                        }
                    });
                }


            }

            function setPosition(curslno, spc) {
                var dataString = 'spc=' + spc + '&curslno=' + curslno;
                $.ajax({
                    type: "POST",
                    url: 'setPosition.htm',
                    data: dataString,
                    async: false,
                    cache: false,
                    success: function(html) {

                    }
                });
            }


            function openModalForMap2Section() {
                //$("#sectionlistModal").modal("toggle");
            }


            function openModal(spc, spn) {
                //alert("Mode is: "+mode);
                $('#hidden_spc').val(spc);
                $('#hidden_spn').val(spn);
                $('#assigned').children().css('background', '#FFFFFF');
                $('#' + spc).css('background', '#FFDBA2');
                $.ajax({
                    type: "GET",
                    url: 'getGPCDetail.htm',
                    data: 'spc=' + spc,
                    dataType: 'json',
                    success: function(jsonObj) {
                        $('#gp').val(jsonObj.sp[0].gp);
                        $('#postgrp').val(jsonObj.sp[0].postgrp);
                        $('#orderNo').val(jsonObj.sp[0].orderNo);
                        $('#orderDate').val(jsonObj.sp[0].orderDate);
                        $('#scaleofPay').val(jsonObj.sp[0].payscale);
                        $('#paylevel').val(jsonObj.sp[0].paylevel);
                        $('#substantivePostModal').modal("show");
                    }
                });

                /*$('#gpc').val(gpc);
                 $('#mode').val(mode);
                 $('#scaleofPay').val(payscale_6th);
                 $('#postgrp').val(post_group);
                 $('#hidPostGrp').val(post_group);
                 $('#paylevel').val(level);
                 $('#gp').val(gp);*/


            }
            $(function() {

                $('#orderDate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });

            });
            function verifyUpdate() {
                if ($('#gp').val() != '' && $('#gp').val() > 10000) {
                    alert("Grade Pay must be within 10000.");
                    return false;
                }
                /*if ($('#paylevel').val() == '') {
                 alert("Please select Level.");
                 $('#paylevel')[0].focus();
                 return false;
                 }*/
                if ($('#postgrp').val() == '') {
                    alert("Please select Post Group.");
                    $('#postgrp')[0].focus();
                    return false;
                }
                $.ajax({
                    type: "GET",
                    url: 'updateGPCDetail.htm',
                    data: 'spc=' + $('#hidden_spc').val() + '&orderNo=' + $('#orderNo').val() + '&orderDate=' + $('#orderDate').val()
                            + '&payscale=' + $('#scaleofPay').val() + '&gp=' + $('#gp').val()
                            + '&paylevel=' + $('#paylevel').val() + '&postgrp=' + $('#postgrp').val(),
                    success: function(html) {
                        alert(html);
                        var str1 = $('#label_' + $('#hidden_spc').val()).html();
                        updatedStr = '';
                        if ($('#orderNo').val())
                            updatedStr += $('#orderNo').val();
                        if ($('#orderDate').val())
                            updatedStr += (updatedStr == '') ? $('#orderDate').val() : (', ' + $('#orderDate').val());
                        if ($('#scaleofPay').val())
                            updatedStr += (updatedStr == '') ? $('#scaleofPay').val() : (', ' + $('#scaleofPay').val());
                        arrTemp = str1.split('(');
                        var rstr = '';
                        if (arrTemp.length > 3)
                        {
                            for (var i = 0; i < arrTemp.length; i++)
                            {
                                if (i == 1)
                                    rstr += '(' + updatedStr + ') ';
                                else
                                    rstr += '(' + arrTemp[i];
                            }
                        }
                        if (arrTemp.length == 3)
                        {
                            for (var i = 0; i < arrTemp.length; i++)
                            {
                                if (i == 0)
                                    rstr += arrTemp[i] + '(' + updatedStr + ') ';
                                else
                                    rstr += '(' + arrTemp[i];
                            }
                        }
                        $('#label_' + $('#hidden_spc').val()).html(rstr);
                    }
                });
                return false;
            }
            function onlyIntegerRange(e)
            {
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
            $(document).ready(function() {
                $('#substantivePostModal').on('hidden.bs.modal', function() {
                    // do something…
                    //$('#'+$('#hidden_spc').val()).css('background', '#FFFFFF');
                });
            });


            $(document).on("click", ".open-submitFormModal", function() {
                var chkspcid = $(this).data('id');

                //$("#sectionlistModal.modal-header #spcid").val(chkspcid);
                $("#sectionlistModal").find(".modal-body").load("move2sectionModal.htm?sectionId=${sectionId}&spcs=" + chkspcid);
                $("#sectionlistModal").modal("show");
            });

            function map2section() {
                var allowPostMap = "";
                if ($("input:radio[name='sectionMapId']").is(":checked")) {
                    var dataString = 'spc=' + $("#unmapspcid").val() + '&sectionId=' + $("input[type=radio][name=sectionMapId]:checked").val() + '&unMapSectionId=' +${sectionId};

                    $.ajax({
                        type: "POST",
                        url: 'move2sectionAfterFreeze.htm',
                        data: dataString,
                        async: false,
                        cache: false,
                        dataType: 'json',
                        success: function(html) {
                            allowPostMap = html.status;
                            if (allowPostMap == "N") {
                                alert("Account Type Mismatch");
                            } else if (allowPostMap == "I") {
                                alert("Post Not Valid");
                            } else if (allowPostMap == "Y") {
                                var hidsltId = $("#unmapspcid").val();
                                $("#" + hidsltId).remove();
                                $("#sectionlistModal").modal("hide");
                                $('input[name=chkAssaign]').attr('checked', false);
                                len = 0;
                                $('input[type=text]').each(function() {
                                    len++;
                                    $(this).val(len);
                                });
                            }
                        }
                    });
                } else {
                    alert('Please select Section.');
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
                            Section Name : ${sectionName}
                            <input type="hidden" name="hidPageno" value="0" id="hidPageno">
                            <input type="hidden" name="hidsecId" id="hidsecId" value="${sectionId}"/>
                            <input type="hidden" name="hidEmpType" id="hidEmpType" value="${empType}"/>
                            <input type="hidden" name="offCode" id="offCode" value="${selectedOffice}"/>
                        </div>
                    </div>
                </div>
                <div class="panel-body">
                    <c:if test="${aersubmittedstatus eq 'false'}">
                        <div> <b>  Available Post</b> </div>
                        <div id="availemp" style="border:1px solid #5095CE;height:235px;overflow:auto;text-align:left;padding:10px;">
                            <c:forEach items="${availableEmpList}" var="availableEmp" varStatus="listCount">
                                <div style="padding:5px;" id="${availableEmp.value}">
                                    <input type="checkbox" value="${availableEmp.value}" id="${listCount.index}" name="chkAvail" onclick="restrict2SelectCheckbox('chkAvail', 'add', '${listCount.index}');
                                            getCheckOrder(this)"/> ${availableEmp.label}
                                    <input type="hidden" name="temp" id="temp${listCount.index}" value=""/>
                                </div>
                            </c:forEach>
                        </div>
                    </c:if>
                    <div style="padding-top:5px">                        
                        <span style="padding-left:20px">
                            <c:if test="${aersubmittedstatus eq 'false'}">
                                <button type="button" class="btn btn-default" onclick="removeDivdata()">Up</button>
                                <button type="button" class="btn btn-default" onclick="addDivdata()">Down</button>   
                            </c:if>

                        </span>

                        <span style="padding-left:20px">
                            <a href="javascript:moveupdiv()"><span class="glyphicon glyphicon-arrow-up" style="color:#008900;"></span></a>
                            <a href="javascript:movedowndiv()"><span class="glyphicon glyphicon-arrow-down" style="color:#FF0000;"></span></a>
                        </span>
                    </div> 
                    <div> <b>  Sanctioned Post </b> </div>
                    <div id="assigned" style="border:1px solid #5095CE;height:450px;overflow:auto;text-align:left;padding:10px;" class="upDownDiv">
                        <c:forEach items="${assignEmpList}" var="assignEmp" varStatus="listCount"> 
                            <div style="padding:5px;" id="${assignEmp.spc}"> 
                                ${listCount.index+1} <input type="checkbox" value="${assignEmp.spc}" id="${assignEmp.spc}" name="chkAssaign" onclick="restrict2SelectCheckbox('chkAssaign', 'remove', '${listCount.index}')"/> <span id="label_${assignEmp.spc}">${assignEmp.spn}</span>
                                <a href="javascript:void(0)" onclick="javascript: openModal('${assignEmp.spc}', '${assignEmp.spn}')" style="color:#890000;font-weight:bold;">Edit</a>
                                <c:if test="${aersubmittedstatus eq 'true'}">
                                    <a href="javascript:void(0);" style="color:#890000;font-weight:bold;" data-remote="false" data-toggle="modal" data-target="#sectionlistModal" data-id="${assignEmp.spc}" class="open-submitFormModal">
                                        <button type="button" class="btn btn-default">Move to Section</button>
                                    </a>
                                </c:if>
                            </div>
                        </c:forEach>
                    </div>
                </div>

                <div id="substantivePostModal" class="modal" role="dialog">
                    <div class="modal-dialog">
                        <!-- Modal content-->
                        <div class="modal-content">
                            <div class="modal-header">
                                <input type="hidden" id="hidden_spc" value="" />
                                <input type="hidden" id="hidden_spn" value="" />
                                <button type="button" class="close" data-dismiss="modal">&times;</button>
                                <h4 class="modal-title">Select Pay Scale and Post Group</h4>
                            </div>
                            <div class="modal-body">
                                <div class="row" style="margin-bottom: 7px;">
                                    <div class="col-lg-3">
                                        <label for="orderNo">Order No</label>
                                    </div>
                                    <div class="col-lg-9">
                                        <input name="orderNo" id="orderNo" type="text" maxlength="5" class="form-control" />
                                    </div>
                                </div>
                                <div class="row" style="margin-bottom: 7px;">
                                    <div class="col-lg-3">
                                        <label for="orderDate">Order Date</label>
                                    </div>
                                    <div class="col-lg-9">
                                        <input name="orderDate" id="orderDate" type="text" maxlength="5" class="form-control" />
                                    </div>
                                </div>                            
                                <div class="row" style="margin-bottom: 7px;">
                                    <div class="col-lg-3">
                                        <label for="scaleofPay">Pay Scale(6th)</label>
                                    </div>
                                    <div class="col-lg-9">
                                        <select name="scaleofPay" id="scaleofPay" class="form-control">
                                            <option value="">--Select Pay Scale--</option>
                                            <c:forEach items="${payscaleList}" var="payList" varStatus="count">
                                                <option value="${payList.payscale}">${payList.payscale}</option>
                                            </c:forEach>                                        
                                        </select>
                                    </div>
                                </div>
                                <div class="row" style="margin-bottom: 7px;">
                                    <div class="col-lg-3">
                                        <label for="gp">Grade Pay</label>
                                    </div>
                                    <div class="col-lg-9">
                                        <input name="gp" id="gp" type="text" maxlength="5" class="form-control" onkeypress='return onlyIntegerRange(event)'/>
                                    </div>
                                </div>
                                <div class="row" style="margin-bottom: 7px;">
                                    <div class="col-lg-3">
                                        <label for="paylevel">As per 7th Pay(LEVEL)</label>
                                    </div>
                                    <div class="col-lg-9">
                                        <select path="paylevel" id="paylevel" class="form-control">
                                            <option value="">--Select Level--</option>
                                            <option value="1">1</option>
                                            <option value="2">2</option>
                                            <option value="3">3</option>
                                            <option value="4">4</option>
                                            <option value="5">5</option>
                                            <option value="6">6</option>
                                            <option value="7">7</option>
                                            <option value="8">8</option>
                                            <option value="9">9</option>
                                            <option value="10">10</option>
                                            <option value="11">11</option>
                                            <option value="12">12</option>
                                            <option value="13">13</option>
                                            <option value="14">14</option>
                                            <option value="15">15</option>
                                            <option value="16">16</option>
                                            <option value="17">17</option>
                                        </select>
                                    </div>
                                </div>
                                <div class="row" style="margin-bottom: 7px;">
                                    <div class="col-lg-3">
                                        <label for="postgrp">Post Group</label>
                                    </div>
                                    <div class="col-lg-9">
                                        <select path="postgrp" id="postgrp" class="form-control">
                                            <option value="">--Select Post Group--</option>
                                            <option value="A">A</option>
                                            <option value="B">B</option>
                                            <option value="C">C</option>
                                            <option value="D">D</option>
                                        </select>
                                    </div>
                                </div>

                                <div class="row" style="margin-bottom: 7px;">
                                    <div class="col-lg-3">
                                    </div>
                                    <div class="col-lg-3">
                                        <button type="submit" name="btnAer" value="Update" class="btn btn-primary" onclick="return verifyUpdate();">Update</button>
                                    </div>
                                    <div class="col-lg-6">
                                        <span id="msg"></span>
                                    </div>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="panel-footer">  
                    <form:form action="billSectionAction.htm" method="post" commandName="SectionDefinition">
                        <form:hidden path="sectionId"/>
                        <input type="submit" class="btn btn-default" name="action" value="Back"/>
                        <button type="button" class="btn btn-default" onclick="saveHirrarchy()">Save Hierarchy</button>
                        <input type="submit" class="btn btn-default" name="action" value="Reset Position"/>                        
                    </form:form>
                </div>
            </div>
        </div>
        <div id="sectionlistModal" class="modal fade" role="dialog">
            <div class="modal-dialog" style="width:1000px;">
                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">

                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Section List</h4>
                    </div>
                    <div class="modal-body">

                    </div>
                    <div class="modal-footer">                       
                        <input type="button" value="Map2Section" onclick="map2section()" class="btn btn-default"/> <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
