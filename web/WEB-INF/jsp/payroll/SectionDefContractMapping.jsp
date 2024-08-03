<%-- 
    Document   : SectionDefContractMapping
    Created on : 11 Apr, 2018, 5:24:05 PM
    Author     : Surendra
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
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link rel="stylesheet" href="css/jquery-ui.css">
        <link rel="stylesheet" href="css/style_drag.css">
        <!-- LAYOUT v 1.3.0 -->
        <style>
            #sortable { list-style-type: none; margin: 0; padding: 0; width: 60%; }
            #sortable div { margin: 0 3px 3px 3px; padding: 0.4em; padding-left: 1.5em; font-size: 12pt; height: 35px; cursor: all-scroll; }
            #sortable div span { position: absolute; margin-left: -1.3em; }
        </style>

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>        
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
        <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
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

                var checkboxarray = new Array();
                var i = 0;
                $("input:checked").each(function ()
                {
                    var textboxobj = $(this).next();
                    var txtboxId = textboxobj.attr("id");
                    var va = document.getElementById(txtboxId).value;

                    checkboxarray[i] = va;
                    i++;
                });
                checkboxarray.sort(numOrdA);
                for (i = 0; i < checkboxarray.length; i++) {
                    $("input:checked").each(function () {
                        var textboxobj = $(this).next();
                        var txtboxId = textboxobj.attr("id");
                        var va = document.getElementById(txtboxId).value;
                        //alert(txtboxId);
                        if (va == checkboxarray[i]) {
                            len = parseInt(len) + 1;
                            var chkId = "chk" + len;
                            $('#assigned').append('<div id="' + $(this).val() + '" style="padding:5px;"> <input type="checkbox" name="' + checkBoxName2 + '" value="' + $(this).val() + '" id="' + len + '" onclick="restrict2SelectCheckbox(' + chkAssaign + ',' + remove + ',' + len + ')"/>' + $("#" + $(this).val()).text() + '<a href="javascript:showSPCOrder(\'' + $(this).val() + '\')">Update OrderDate<\/a><\/div>');
                            var dataString = 'spc=' + $(this).val() + '&sectionId=' + $("#hidsecId").val();
                            //alert($("#hidsecId").val());
                            $.ajax({
                                type: "POST",
                                url: 'assignPostAction.htm',
                                data: dataString,
                                async: false,
                                cache: false,
                                success: function (html) {

                                }
                            });
                            $("#" + $(this).val()).remove();

                        }
                    });
                }

                $('input[name=chkAvail]').attr('checked', false);
                len = 1;
                $('input[type=checkbox]').each(function () {
                    len++;
                });
            }


            function scrollUp() {
                alert('Scroll Up');
            }

            function scrollDown() {
                alert('Scroll Down');
            }

            function removeDivdata() {
                var checkBoxName = "chkAssaign";
                var checkBoxes = $("input[name=" + checkBoxName + "]");

                var checkBoxName2 = "chkAvail";
                var checkBoxes2 = $("input[name=" + checkBoxName2 + "]");
                var add = "'add'";
                var chkAvail = "'chkAvail'";
                var len = checkBoxes2.length;
                $.each(checkBoxes, function () {
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
                            success: function (html) {

                                $("#" + divid).remove();
                                $('#availemp').append('<div id="' + divid + '" style="padding:5px;" > <input type="checkbox" name="' + checkBoxName2 + '" value="' + divid + '" id="' + len + '" onclick="restrict2SelectCheckbox(' + chkAvail + ',' + add + ',' + len + ');getCheckOrder(this)"/>' + divText + ' <input type="hidden" name="temp" id="' + tempid + '" value=""/><\/div>');

                            }
                        });

                    }

                });

                $('input[name=chkAssaign]').attr('checked', false);

                len = 0;
                $('input[type=text]').each(function () {
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



                $("input[name=" + checkBoxName + "]:checked").each(function ()
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

                $("input[name=" + checkBoxName + "]:checked").each(function ()
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
                var favorite = [];
                var t = "";
                $("input[name=chkAssaign]").each(function () {
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
                        success: function (data) {

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
                            <input type="hidden" name="hidEmpType" id="hidEmpType" value="${empType}"/>
                            <input type="hidden" name="hidsecId" id="hidsecId" value="${sectionId}"/>
                            <input type="hidden" name="offCode" id="offCode" value="${selectedOffice}"/>
                        </div>
                    </div>
                </div>
                <div class="panel-body">
                    <div> <b>  Available Post</b> </div>
                    <div id="availemp" style="border:1px solid #5095CE;height:235px;overflow:auto;text-align:left;padding:10px;">
                        <c:forEach items="${availableEmpList}" var="availableEmp" varStatus="listCount">
                            <div class="ui-state-default" style="padding:5px;margin-bottom:5px;" id="${availableEmp.value}">
                                <input type="checkbox" value="${availableEmp.value}" id="${listCount.index}" name="chkAvail" onclick="restrict2SelectCheckbox('chkAvail', 'add', '${listCount.index}');
                                        getCheckOrder(this)"/> ${availableEmp.label}
                                <input type="hidden" name="temp" id="temp${listCount.index}" value=""/>
                            </div>
                        </c:forEach>
                    </div>
                    <div style="padding-top:5px">                        
                        <span style="padding-left:20px">
                            <button type="button" class="btn btn-default" onclick="removeDivdata()">Up</button>
                            <button type="button" class="btn btn-default" onclick="addDivdata()">Down</button>                                                    
                        </span>

                        <span style="padding-left:20px">
                            <a href="javascript:moveupdiv()"><span class="glyphicon glyphicon-arrow-up" style="color:#008900;"></span></a>
                            <a href="javascript:movedowndiv()"><span class="glyphicon glyphicon-arrow-down" style="color:#FF0000;"></span></a>
                        </span>
                    </div> 
                    <div> <b>  Assigned Post </b> </div>
                    <div id="assigned" style="border:1px solid #5095CE;height:235px;overflow:auto;text-align:left;padding:10px;" class="upDownDiv">


                        <c:forEach items="${assignEmpList}" var="assignEmp" varStatus="listCount" >

                            <div class="ui-state-default" style="padding:5px;margin-bottom:5px;" id="${assignEmp.spc}">
                                ${listCount.index+1}   <input type="checkbox" value="${assignEmp.spc}" id="${listCount.index}" name="chkAssaign" onclick="restrict2SelectCheckbox('chkAssaign', 'remove', '${listCount.index}')"/> ${assignEmp.spn} 
                            </div>
                        </c:forEach>

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
    </body>
</html>
