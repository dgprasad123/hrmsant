<%-- 
    Document   : addNewEquivalantPost
    Created on : 30 Dec, 2021, 11:16:18 AM
    Author     : Devikrushna
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
         <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <link rel="stylesheet" href="css/chosen.css">

        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script type="text/javascript" src="js/chosen.jquery.min.js"></script>
        <script type="text/javascript">
             $(document).ready(function () {
                 $("#current-tab").hide();
                $('#txtNotOrdDt').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });                          
            });       
              function openEquvalentNotifyingModal() {
                var orgType = $('input[name="radnotifyingauthtype"]:checked').val();
                if (orgType == 'GOO') {
                    $("#equvalentAuthorityModalGOO").modal("show");
                } else if (orgType == 'GOI') {
                    $("#equvalentNotifyingGOIModal").modal("show");
                }
            }
            
            function getNotifyingOtherOrgPost() {
                $("#notifyingSpc").val($('#hidNotifyingOthSpc option:selected').text());
                $("#equvalentNotifyingGOIModal").modal("hide");
            }
              
              
              
              function getDistWiseOfficeList(type) {
                var deptcode;
                var distcode;
                if (type == "P") {
                    deptcode = $('#hidPostedDeptCode').val();
                    distcode = $('#hidPostedDistCode').val();
                    $('#hidPostedOffCode').empty();
                    $('#hidPostedOffCode').append('<option value="">--Select Office--</option>');
                }
                var url = "";
                if ($("input[name=rdTransaction]:checked").val() == "S") {
                    url = 'getOfficeListDistrictAndDepartmentForBacklogEntryJSON.htm?deptcode=' + deptcode + '&distcode=' + distcode;
                }else{
                    url = 'getOfficeListDistrictAndDepartmentJSON.htm?deptcode=' + deptcode + '&distcode=' + distcode;
                }
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                         if (type == "P") {
                            $('#hidPostedOffCode').append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');
                        }
                    });
                });
            }
              
              function getOfficeWiseGenericPostList(type) {
                var offcode;
                var url;
                if (type == "P") {
                    offcode = $('#hidPostedOffCode').val();
                    $('#genericpostPosted').empty();
                    $('#genericpostPosted').append('<option value="">--Select Generic Post--</option>');
                    url = "getPostCodeListJSON.htm?offcode=" + offcode;
                }
                //var url = 'getAuthorityPostListJSON.htm?offcode=' + offcode;

                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                         if (type == "P") {
                            $('#genericpostPosted').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                        }
                    });
                });

                //Start Field Off Code
                if (type == "P") {
                    $('#sltPostedFieldOff').empty();
                    var url = 'transferGetFieldOffListJSON.htm?offcode=' + $('#hidPostedOffCode').val();
                    $('#sltPostedFieldOff').append('<option value="">--Select--</option>');
                    $.getJSON(url, function(data) {
                        $.each(data, function(i, obj) {
                            $('#sltPostedFieldOff').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                        });
                    });
                }
                //End Field Off Code
            }
            
              function getGenericPostWiseSPCList(type) {
                var offcode;
                var gpc;
                var url;
                if (type == "P") {
                    offcode = $('#hidPostedOffCode').val();
                    gpc = $('#genericpostPosted').val();
                    if ($("input[name=rdTransaction]:checked").val() == "S") {
                        url = "getSubstantivePostListBacklogEntryJSON.htm?postcode=" + gpc + "&offcode=" + offcode;
                    } 
                    
                    $('#hidPostedSpc').empty();
                    $('#hidPostedSpc').append('<option value="">--Select Substantive Post--</option>');
                }

                //var url = "getVacantPostListJSON.htm?postcode="+gpc+"&offcode="+offcode;
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        if (type == "P") {
                            $('#hidPostedSpc').append('<option value="' + obj.spc + '">' + obj.spn + '</option>');
                        }
                    });
                });
            }
            
              
             function getDeptWiseOfficeList(type) {
                var deptcode;
                if (type == "A") {
                    deptcode = $('#hidNotifyingDeptCode').val();
                    $('#hidNotifyingOffCode').empty();
                    $('#hidNotiSpc').empty();
                } else if (type == "P") {
                    deptcode = $('#hidPostedDeptCode').val();
                    $('#hidPostedOffCode').empty();
                    $('#hidPostedSpc').empty();
                }
                //var url = 'getOfficeListJSON.htm?deptcode=' + deptcode;
                
                var url = "";             
                if ($("input[name=rdTransaction]:checked").val() == "S") {
                    url = 'getofficelistForBacklogEntry.htm?deptcode=' + deptcode;
                } else if($("input[name=rdTransaction]:checked").val() == "C"){
                    url = 'getOfficeListJSON.htm?deptcode=' + deptcode;
                }
                
                
                if (type == "A") {
                    $('#hidNotifyingOffCode').append('<option value="">--Select Office--</option>');
                } else if (type == "P") {
                    $('#hidPostedOffCode').append('<option value="">--Select Office--</option>');
                }
                $.getJSON(url, function (data) {
                    $.each(data, function (i, obj) {
                        if (type == "A") {
                            $('#hidNotifyingOffCode').append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');
                        } else if (type == "P") {
                            $('#hidPostedOffCode').append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');
                        }
                    });
                }).done(function () {
                    if (type == "A") {
                        $('#hidNotifyingOffCode').chosen();
                        $("#hidNotifyingOffCode").trigger("chosen:updated");
                    } else if (type == "P") {
                        $('#hidPostedOffCode').chosen();
                        $("#hidPostedOffCode").trigger("chosen:updated");
                    }
                });
            }

            function getOfficeWisePostList(type) {
                var offcode;
                if (type == "A") {
                    offcode = $('#hidNotifyingOffCode').val();
                    $('#hidNotiSpc').empty();
                } else if (type == "P") {
                    offcode = $('#hidPostedOffCode').val();
                    $('#hidPostedSpc').empty();
                }
                //var url = 'getTransferCadreWisePostListJSON.htm?offcode=' + offcode;
                if (type == "A") {
                    url = 'getSanctioningSPCOfficeWiseListJSON.htm?offcode=' + offcode;
                    $('#hidNotiSpc').append('<option value="">--Select Post--</option>');
                } else if (type == "P") {
                     url = 'getSanctioningSPCOfficeWiseListJSON.htm?offcode=' + offcode;
                    $('#hidPostedSpc').append('<option value="">--Select Post--</option>');
                }
                $.getJSON(url, function (data) {
                    $.each(data, function (i, obj) {
                        if (type == "A") {
                            $('#hidNotiSpc').append('<option value="' + obj.spc + '">' + obj.postname + '</option>');
                        } else if (type == "P") {
                            $('#hidPostedSpc').append('<option value="' + obj.spc + '">' + obj.postname + '</option>');
                        }
                    });
                }).done(function () {
                    if (type == "A") {
                        $('#hidNotiSpc').chosen();
                        $("#hidNotiSpc").trigger("chosen:updated");
                    } else if (type == "P") {
                        $('#hidPostedSpc').chosen();
                        $("#hidPostedSpc").trigger("chosen:updated");
                    }
                });       
            }
            
            function removeDepedentDropdown(type) {
                  if (type == "P") {
                    $('#hidPostedDistCode').val('');
                    $('#hidPostedOffCode').empty();
                    $('#genericpostPosted').empty();
                    $('#hidPostedSpc').empty();
                }
            }
            
             function openEquvalPostingModal() {
                if ($("input[name=rdTransaction]:checked").length == 0) {
                    alert("Please select Transaction type");
                } else {
                    var orgType = $('input[name="radpostingauthtype"]:checked').val();
                    if (orgType == 'GOO') {
                        $("#equvalPostedModal").show();
                    } else if (orgType == 'GOI') {
                        $("#equvalPostedOtherOrgModal").show();
                    }
                }
            }
       
            function closePostingModal() {
                $("#equvalPostedModal").hide();
            }
            function closePostingOtherOrgModal() {
                $("#equvalPostedOtherOrgModal").hide();
                $('#postedSpc').val($('#hidPostedOtherOrgSpc option:selected').text());
            }     
            function closeequvalPostingOtherOrgModal() {
                $("#currentequvalPostedOtherOrgModal").hide();
                $('#equvalpostedSpc').val($('#hidequvalPostedOthSpc option:selected').text());
            }
             
             function getPost(type) {
                if (type == "A") {
                    $('#notifyingSpc').val($('#hidNotiSpc option:selected').text());
                }else if (type == "P") {
                    $('#postedSpc').val($('#hidPostedSpc option:selected').text());
                }else if(type=="E"){
                    $('#hidequvalPostedSpc').val($('#hidequvalPostedSpc option:selected').text());
                }
                
            }
            
            function removePostingData() {
                $('#postedSpc').val('');
                $('#hidPostedSpc').val('');            
            }
            
            function getDeptWiseEqvalPost() {
                $('#hidequvalPostedSpc').empty();
                $('#hidequvalPostedSpc').append('<option value="">--Select Post--</option>');
                var url = 'getDeptWisePostListJSON.htm?deptCode=' + $('#hidequvalPostedDeptCode').val();
                //alert($('#hidequvalPostedDeptCode').val());
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#hidequvalPostedSpc').append('<option value="' + obj.postcode + '">' + obj.post + '</option>');                         
                    })
                });
            }
            
            
            function getDeptWiseCadre() {
                $('#sltCadre').empty();
                $('#sltCadre').append('<option value="">--Select Cadre--</option>');
                var url = 'getCadreListJSON.htm?deptcode=' + $('#sltCadreDept').val();
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#sltCadre').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                    });
                });
            }

    
            function getCadreWiseGrade() {
                $('#sltGrade').empty();
                var url = 'getGradeListCadreWiseJSON.htm?cadreCode=' + $('#sltCadre').val();
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#sltGrade').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                    });
                });
            }
            function getequvalDeptWiseCadre() {
                $('#equvalCadre').empty();
                $('#equvalCadre').append('<option value="">--Select Cadre--</option>');
                var url = 'getCadreListJSON.htm?deptcode=' + $('#equvalCadreDept').val();
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#equvalCadre').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                    });
                });
            }

            function getequvalCadreWiseGrade() {
                $('#equvalGrade').empty();
                var url = 'getGradeListCadreWiseJSON.htm?cadreCode=' + $('#equvalCadre').val();
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#equvalGrade').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                    });
                });
            }
            
            function hidebacklogTab(){
                 $("#cadre-tab").hide();
                 $("#backlog-tab").hide(); 
                 $("#current-tab").show();
            }
            
            function hidecurrentTab(){
                 $("#current-tab").hide();
                 $("#cadre-tab").show();
                 $("#backlog-tab").show(); 
            }
             function savecheck () {
                 
                 if ($("input[name=rdTransaction]:checked").length == 0) {
                    alert("Please select Transaction type");
                    return false;
                }
                if ($('#txtNotOrdNo').val() == '') {
                    alert('Notification Order No should not be blank.');
                    $('#txtNotOrdNo').focus();
                    return false;
                }

                if ($('#txtNotOrdDt').val() == '') {
                    alert('Notification Order Date should not be blank.');
                    $('#txtNotOrdDt').focus();
                    return false;
                } 
                if ($("input[name=rdTransaction]:checked").val() == "S"){
                    if ($('#postedSpc').val() == '') {
                        alert('Select Details of Current Posting.');
                        $('#postedSpc').focus();
                        return false;
                    }
                    if ($('#sltCadreDept').val() == '') {
                        alert('Select Current Cadre Controlling Department.');
                        $('#sltCadreDept').focus();
                        return false;
                    }
                    if ($('#sltCadre').val() == '') {
                        alert('Select Current Cadre.');
                        $('#sltCadre').focus();
                        return false;
                    }
                }
                if ($('#hidequvalPostedDeptCode').val() == '') {
                    alert('Select Equivalent Department.');
                    $('#hidequvalPostedDeptCode').focus();
                    return false;
                }
                if ($('#hidequvalPostedSpc').val() == '') {
                    alert('Select Equivalent Post.');
                    $('#hidequvalPostedSpc').focus();
                    return false;
                }
                if ($('#equvalCadreDept').val() == '') {
                    alert('Select Equivalent Cadre Controlling Department.');
                    $('#equvalCadreDept').focus();
                    return false;
                }
                if ($('#equvalCadre').val() == '') {
                    alert('Select Equivalent Cadre.');
                    $('#equvalCadre').focus();
                    return false;
                }

            } 

            
        </script>
        <style>
            .row-margin{
                margin-bottom: 20px;
            }
        </style>
        
    </head>
    <body>
        <form:form action="saveEquivalentPost.htm" method="post" commandName="equivalpostForm">
             <form:hidden path="hidNotId"/>
              <form:hidden path="hidcurdeptcode"/>
              <form:hidden path="hidcuroffcode"/>
              <form:hidden path="hidcurpostcode"/>
              <form:hidden path="hidcurpostname"/>
              <form:hidden path="hidcurcadredeptcode"/>
              <form:hidden path="hidcurcadre"/>
              <form:hidden path="hidcurgrade"/>
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                       Add New Equivalent Post
                    </div>
                    <div class="panel-body">
                         <form:hidden path="empid" id="empid"/>
                         
                       <div class="row row-margin">
                            <div class="col-lg-4">
                                <label for="chkNotSBPrint">Check Not to Print in Service Book</label>
                            </div>
                            <div class="col-lg-3">   
                                <form:checkbox path="chkNotSBPrint" value="Y" class="form-control"/> 
                            </div>
                            <div class="col-lg-3"></div>
                            <div class="col-lg-2"></div>
                        </div>
                          <div class="row row-margin">
                            <div class="col-lg-2">
                                <label for="rdTransaction">Select type of Transaction</label>
                            </div>
                            <div class="col-lg-3">   
                                <form:radiobutton path="rdTransaction" value="S" id="rdbacklog" onclick="removePostingData();" onchange="hidecurrentTab();"/> Service Book Entry(Backlog)
                            </div>
                            <div class="col-lg-3">
                                <form:radiobutton path="rdTransaction" value="C" id="rdcurrent" onchange="hidebacklogTab();"/> Current Transaction(will effect current Pay or Post)
                            </div>
                            <div class="col-lg-2"></div>
                        </div>   
                            
                        <div class="row row-margin" >
                            <div class="col-lg-2">
                                <label for="txtNotOrdNo">Notification Order No <span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">   
                                <form:input class="form-control" path="txtNotOrdNo" id="txtNotOrdNo"/>
                            </div>
                            <div class="col-lg-2">
                                <label for="txtNotOrdDt"> Notification Order Date <span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">
                                <div class="input-group date" id="processDate">
                                    <form:input class="form-control" path="txtNotOrdDt" id="txtNotOrdDt" readonly="true"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>                                
                            </div>
                        </div>
                                    
                        <div class="row row-margin">
                            <div class="col-lg-2">
                                <label for="radnotifyingauthtype">Details of Notifying Authority </label>
                            </div>
                            <div class="col-lg-2">
                                <form:radiobutton path="radnotifyingauthtype" value="GOO"  id="notifyingGOO"/> 
                                <label for="notifyingGOO"> Government of Odisha </label>
                            </div>
                            <div class="col-lg-2">
                                <form:radiobutton path="radnotifyingauthtype" value="GOI"  id="notifyingGOI"/> 
                                <label for="notifyingGOI"> Government of India </label>
                            </div>                         
                        </div>    
                                
                        <div class="row row-margin">
                            <div class="col-lg-2"></div>
                            <div class="col-lg-6">
                                <form:input class="form-control" path="notifyingSpc" id="notifyingSpc" readonly="true"/>                           
                            </div>
                            <div class="col-lg-1">
                                <button type="button" class="btn btn-primary" onclick="openEquvalentNotifyingModal();">
                                    <span class="glyphicon glyphicon-search"></span> Search
                                </button>
                            </div>
                        </div>  
                         <div class="row-margin" id="backlog-tab">
                         <div class="row row-margin">
                            <div class="col-lg-2">
                                <label for="postedSpc"> Details of Current Post </label> <span style="color: red">*</span>
                            </div>
                            <div class="col-lg-2">
                                <form:radiobutton path="radpostingauthtype" value="GOO"  id="postedGOO"/> 
                                <label for="postedGOO"> Government of Orissa </label>
                            </div>
                            <div class="col-lg-2">
                                <form:radiobutton path="radpostingauthtype" value="GOI" id="postedGOI"/> 
                                <label for="postedGOI"> Government of India </label>
                            </div>
                        </div>    

                        <div class="row row-margin">
                            <div class="col-lg-2">
                                <label for="postedSpc"> </label>
                            </div>
                            <div class="col-lg-6">
                                <form:input class="form-control" path="postedSpc" id="postedSpc" readonly="true"/>                           
                            </div>
                            <div class="col-lg-1">
                                <button  type="button" class="btn btn-primary" onclick="openEquvalPostingModal()">
                                    <span class="glyphicon glyphicon-search"></span> Search
                                </button>
                            </div>
                        </div>
                           </div>    
                         <div class="row row-margin" id="current-tab">
                                <div class="col-lg-2">
                                    <label for="postedSpc"> Details of Current Post </label>
                                 </div>  
                             <div class="row row-margin">
                                        <div class="col-lg-2"></div>
                                        <div class="col-lg-9" style="margin-left: -10px;">
                                            Department Name: 
                                            <strong>                            
                                                <c:out value="${posteddeptname}"/>
                                            </strong>                          
                                        </div>
                                        <div class="col-lg-1"></div>
                                    </div>
                                    <div class="row row-margin">
                                        <div class="col-lg-2"></div>
                                        <div class="col-lg-9">
                                            Office Name: 
                                            <strong>                            
                                                <c:out value="${postedoffice}"/>
                                            </strong>                          
                                        </div>
                                        <div class="col-lg-1"></div>
                                    </div>      

                                    <div class="row row-margin">
                                        <div class="col-lg-2"></div>
                                        <div class="col-lg-9">
                                            Current Post: 
                                            <strong>                            
                                                <c:out value="${equivalpostForm.hidcurpostname}"/>
                                            </strong>                          
                                        </div>
                                        <div class="col-lg-1"></div>
                                    </div>

                                    <div class="row row-margin">
                                        <div class="col-lg-2"></div>
                                        <div class="col-lg-9">
                                            Cadre Department Name: 
                                            <strong>                            
                                                <c:out value="${cadredeptname}"/>
                                            </strong>                          
                                        </div>
                                        <div class="col-lg-1"></div>
                                    </div>
                                    <div class="row row-margin">
                                        <div class="col-lg-2"></div>
                                        <div class="col-lg-9">
                                            Cadre Name: 
                                            <strong>                            
                                                <c:out value="${curcadrename}"/>
                                            </strong>                          
                                        </div>
                                        <div class="col-lg-1"></div>
                                    </div>   
                                     <div class="row row-margin">
                                        <div class="col-lg-2"></div>
                                        <div class="col-lg-9">
                                            Grade Name: 
                                            <strong>                            
                                                <c:out value="${hidcurgrade}"/>
                                            </strong>                          
                                        </div>
                                        <div class="col-lg-1"></div>
                                    </div>         
                                </div>            
                           <div class="row-margin" id="cadre-tab">   
                        <div class="row row-margin">
                            <div class="col-lg-2">
                                <label>Details of Current Cadre</label>
                            </div>
                            <div class="col-lg-8">

                            </div>
                            <div class="col-lg-1">

                            </div>
                        </div>
                                          
                        <div class="row row-margin">
                            <div class="col-lg-2">
                                &nbsp;&nbsp;(a) Cadre Controlling Department <span style="color: red">*</span>
                            </div>
                            <div class="col-lg-6">
                                <form:select path="sltCadreDept" id="sltCadreDept" class="form-control" onchange="getDeptWiseCadre();">
                                    <form:option value="">--Select--</form:option>
                                    <form:options items="${deptlist}" itemValue="deptCode" itemLabel="deptName"/>
                                </form:select>                           
                            </div>                         
                        </div>

                        <div class="row row-margin">
                            <div class="col-lg-2">
                                &nbsp;&nbsp;(b) Name of the Cadre<span style="color: red">*</span>
                            </div>
                            <div class="col-lg-6">
                                <form:select path="sltCadre" id="sltCadre" class="form-control" onchange="getCadreWiseGrade();">
                                    <form:option value="">--Select--</form:option>
                                    <form:options items="${cadrelist}" itemValue="value" itemLabel="label"/>
                                </form:select>                           
                            </div>                          
                        </div>

                        <div class="row row-margin">
                            <div class="col-lg-2">
                                &nbsp;&nbsp;(c) Name of the Grade
                            </div>
                            <div class="col-lg-6">
                                <form:select path="sltGrade" id="sltGrade" class="form-control">
                                    <form:option value="">--Select--</form:option>
                                     <form:options items="${gradelist}" itemValue="value" itemLabel="label"/>
                                </form:select>                           
                            </div>                        
                        </div> 
                         </div> 
                        <div class="row row-margin">
                            <div class="col-lg-2">
                                <label for="equvalpostedSpc"> Details of Equivalent Post </label>
                            </div>
                            <div class="col-lg-2">
                                
                            </div>
                            <div class="col-lg-2">
                               
                            </div>
                        </div>   
                            <div class="row row-margin">
                             <div class="col-lg-2">                                   
                                    &nbsp;&nbsp;(a) Name of the Department <span style="color: red">*</span>
                                </div>
                                <div class="col-lg-6">
                                    <form:select path="hidequvalPostedDeptCode" id="hidequvalPostedDeptCode" class="form-control"  onchange="getDeptWiseEqvalPost()">
                                        <form:option value="">--Select Department--</form:option>
                                        <form:options items="${deptlist}" itemValue="deptCode" itemLabel="deptName"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                           
                            <div class="row row-margin">
                                <div class="col-lg-2">
                                     &nbsp;&nbsp;(b) Name of the Post <span style="color: red">*</span>                                 
                                </div>
                                <div class="col-lg-6">
                                    <form:select path="hidequvalPostedSpc" id="hidequvalPostedSpc" class="form-control">
                                        <form:option value="">--Select Post--</form:option>
                                       <form:options items="${postlist}" itemValue="postcode" itemLabel="post"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                        
                            
                        <div class="row row-margin">
                            <div class="col-lg-3">
                                <label>Details of Equivalent Post Cadre</label>
                            </div>
                            <div class="col-lg-8">

                            </div>
                            <div class="col-lg-1">

                            </div>
                        </div>

                        <div class="row row-margin">
                            <div class="col-lg-2">
                                &nbsp;&nbsp;(a) Cadre Controlling Department <span style="color: red">*</span>
                            </div>
                            <div class="col-lg-6">
                                <form:select path="equvalCadreDept" id="equvalCadreDept" class="form-control" onchange="getequvalDeptWiseCadre();">
                                    <form:option value="">--Select--</form:option>
                                    <form:options items="${deptlist}" itemValue="deptCode" itemLabel="deptName"/>
                                </form:select>                           
                            </div>                         
                        </div>

                        <div class="row row-margin">
                            <div class="col-lg-2">
                                &nbsp;&nbsp;(b) Name of the Cadre<span style="color: red">*</span>
                            </div>
                            <div class="col-lg-6">
                                <form:select path="equvalCadre" id="equvalCadre" class="form-control" onchange="getequvalCadreWiseGrade();">
                                    <form:option value="">--Select--</form:option>
                                    <form:options items="${equvalcadrelist}" itemValue="value" itemLabel="label"/>
                                </form:select>                           
                            </div>                          
                        </div>

                        <div class="row row-margin">
                            <div class="col-lg-2">
                                &nbsp;&nbsp;(c) Name of the Grade
                            </div>
                            <div class="col-lg-6">
                                <form:select path="equvalGrade" id="equvalGrade" class="form-control">
                                    <form:option value="">--Select--</form:option>
                                    <form:options items="${equvalgradelist}" itemValue="value" itemLabel="label"/>
                                </form:select>                           
                            </div>                        
                        </div>  
                             
                        <div class="row row-margin">
                            <div class="col-lg-2">
                                <label for="note">Note(If any)</label>
                            </div>
                            <div class="col-sm-6">
                                    <form:textarea Class="form-control" path="note" id="note" />
                             </div>
                        </div>         
                            
                    </div>
                    <div class="panel-footer">
                             <button type="submit" class="btn btn-primary" name="action" value="Save EquivalentPost" onclick="return savecheck()">Save</button>  
                              <a href="EquivalentPost.htm"> <button type="button" class="btn btn-warning btn-md">&laquo;Back</button></a>
                    </div>
                </div>
            </div>
                        
                 <div id="equvalentAuthorityModalGOO" class="modal" role="dialog">
                <div class="modal-dialog">
                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Notifying Authority</h4>
                        </div>
                        <div class="modal-body">
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="sltDept">Department</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidNotifyingDeptCode" id="hidNotifyingDeptCode" class="form-control" onchange="getDeptWiseOfficeList('A');">
                                        <form:option value="">--Select Department--</form:option>
                                        <form:options items="${deptlist}" itemValue="deptCode" itemLabel="deptName"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="note">Office</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidNotifyingOffCode" id="hidNotifyingOffCode" class="form-control" onchange="getOfficeWisePostList('A');">
                                        <form:option value="">--Select Office--</form:option>
                                        <form:options items="${sancOfflist}" itemValue="offCode" itemLabel="offName"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="note">Post</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidNotiSpc" id="hidNotiSpc" class="form-control" onchange="getPost('A');">
                                        <form:option value="">--Select Post--</form:option>
                                        <form:options items="${sancPostlist}" itemValue="spc" itemLabel="postname"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        </div>
                    </div>
                </div>
            </div>          
                        
              <div id="equvalentNotifyingGOIModal" class="modal" role="dialog">
                <div class="modal-dialog">
                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Notifying Authority</h4>
                        </div>
                        <div class="modal-body">
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="hidNotifyingOthSpc">Authority</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidNotifyingOthSpc" id="hidNotifyingOthSpc" class="form-control" onchange="getNotifyingOtherOrgPost();">
                                        <form:option value="">--Select Post--</form:option>
                                        <form:options items="${otherOrgfflist}" itemValue="value" itemLabel="label"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1"></div>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        </div>
                    </div>
                </div>
            </div>    
                           
            
            <div id="equvalPostedModal" class="modal" role="dialog">
                <div class="modal-dialog">
                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Posted Authority</h4>
                        </div>
                        <div class="modal-body">
                               
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="sltDept">Posted Department</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidPostedDeptCode" id="hidPostedDeptCode" class="form-control" onchange="removeDepedentDropdown('P');">
                                        <form:option value="">--Select Department--</form:option>
                                        <form:options items="${deptlist}" itemValue="deptCode" itemLabel="deptName"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="hidPostedDistCode">District</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidPostedDistCode" id="hidPostedDistCode" class="form-control" onchange="getDistWiseOfficeList('P');">
                                        <option value="">--Select District--</option>
                                        <form:options items="${distlist}" itemValue="distCode" itemLabel="distName"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="hidPostedOffCode">Office</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidPostedOffCode" id="hidPostedOffCode" class="form-control" onchange="getOfficeWiseGenericPostList('P');">
                                        <option value="">--Select Office--</option>
                                        <form:options items="${postedOffList}" itemValue="offCode" itemLabel="offName"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="genericpostPosted">Generic Post</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="genericpostPosted" id="genericpostPosted" class="form-control" onchange="getGenericPostWiseSPCList('P');">
                                        <option value="">--Select Generic Post--</option>
                                        <form:options items="${gpcpostedList}" itemValue="value" itemLabel="label"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="hidPostedSpc">Substantive Post</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidPostedSpc" id="hidPostedSpc" class="form-control" onchange="getPost('P');">
                                        <option value="">--Select Substantive Post--</option>
                                        <form:options items="${postedPostList}" itemValue="spc" itemLabel="spn"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>                                         
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" onclick="closePostingModal()">Select</button>
                        </div>
                    </div>
                </div>
            </div>                 
                        
                             
                             
                             
                             
                             
                             
               <%-- <div id="equvalPostedModal" class="modal" role="dialog">
                <div class="modal-dialog">
                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Posted Authority</h4>
                        </div>
                        <div class="modal-body">
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="sltDept">Posted Department</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidPostedDeptCode" id="hidPostedDeptCode" class="form-control" onchange="getDeptWiseOfficeList('P');">
                                        <form:option value="">--Select Department--</form:option>
                                        <form:options items="${deptlist}" itemValue="deptCode" itemLabel="deptName"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="note">Posted Office</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidPostedOffCode" id="hidPostedOffCode" class="form-control" onchange="getOfficeWisePostList('P');">
                                        <form:option value="">--Select Office--</form:option>
                                        <form:options items="${postedOffList}" itemValue="offCode" itemLabel="offName"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="note">Post</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidPostedSpc" id="hidPostedSpc" class="form-control" onchange="getPost('P');">
                                        <form:option value="">--Select Post--</form:option>                                      
                                         <form:options items="${postedPostList}" itemValue="spc" itemLabel="postname"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" onclick="closePostingModal()">Select</button>
                        </div>
                    </div>
                </div>
            </div> --%>

            <div id="equvalPostedOtherOrgModal" class="modal" role="dialog">
                <div class="modal-dialog">
                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Posting Details</h4>
                        </div>
                        <div class="modal-body">
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="sltDept">Posted To</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidPostedOthSpc" id="hidPostedOtherOrgSpc" class="form-control">
                                        <form:option value="">--Select Post--</form:option>
                                        <form:options items="${otherOrgfflist}" itemValue="value" itemLabel="label"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" onclick="closePostingOtherOrgModal()">Select</button>
                        </div>
                    </div>
                </div>
            </div>    
                             
                             
                        
        </form:form>
    </body>
</html>
