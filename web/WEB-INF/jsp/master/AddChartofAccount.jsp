<%-- 
    Document   : AddChartofAccount
    Created on : oct 27, 2021, 10:57:01 AM
    Author     : Devi
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
        <script src="js/moment.js" type="text/javascript"></script>
        <link href="css/sb-admin.css" rel="stylesheet">
        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>

        <script type="text/javascript">
            function validatechat() {
                if ($('#demandNo').val() == "")
                {
                    alert("please select Demand Number");
                    $('#demandNo').focus();
                    return false;
                }
                if ($('#majorHead').val() == "")
                {
                    alert("please select Major Head");
                    $('#majorHead').focus();
                    return false;
                }
                if ($('#subMajorHead').val() == "")
                {
                    alert("please select Sub Major Head");
                    $('#subMajorHead').focus();
                    return false;
                }
                if ($('#minorHead').val() == "")
                {
                    alert("please select Minor Head");
                    $('#minorHead').focus();
                    return false;
                }

                if ($('#subHead').val() == "")
                {
                    alert("please select Sub Head");
                    $('#subHead').focus();
                    return false;
                }
                if ($('#DetailHead').val() == "")
                {
                    alert("please select Detail Head");
                    $('#DetailHead').focus();
                    return false;
                }
                if ($('#ChargeVoted').val() == "")
                {
                    alert("please select Charge Voted");
                    $('#ChargeVoted').focus();
                    return false;
                }
                if ($('#ObjectHead').val() == "")
                {
                    alert("please enter Object Head");
                    $('#ObjectHead').focus();
                    return false;
                }
                return true;

            }
            
            function checkMajorhead() {
                if ($('#modalmajorHead').val() == "")
                {
                    alert("please enter Major Head");
                    $('#modalmajorHead').focus();
                    return false;
                }
                
            }
            function checksubMajorhead(){
                if ($('#modalsubmajorHead').val() == "")
                {
                    alert("please enter Sub Major Head");
                    $('#modalsubmajorHead').focus();
                    return false;
                }
                
            }
             function checkMinorhead() {
                if ($('#modalminorHead').val() == "")
                {
                    alert("please enter Minor Head");
                    $('#modalminorHead').focus();
                    return false;
                }
                
            }
            function checkSubhead() {
                if ($('#modalsubHead').val() == "")
                {
                    alert("please enter Sub Head");
                    $('#modalsubHead').focus();
                    return false;
                }
                
            }
            function checkDetailhead() {
                if ($('#modaldetailHead').val() == "")
                {
                    alert("please enter Detail Head");
                    $('#modaldetailHead').focus();
                    return false;
                }
                
            }

            function getMajorHeadList(rec) {
                if (rec) {
                    $('#majorHead').empty();
                    var url = 'getMajorHeadList.htm?demandNo=' + rec;
                    $('#majorHead').append('<option value="">-- Select --</option>');
                    $.getJSON(url, function (data) {
                        $.each(data, function (i, obj) {
                            $('#majorHead').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                        });
                    });
                }
                //getSubMajorHeadList
            }

            function getSubMajorHeadListmaster(rec) {
                //$('#subMajorHead').empty();                
                if (rec) {
                    var majorhead = $('#majorHead').val();
                      $('#subMajorHead').empty();
                    var url = 'getSubMajorHeadListmaster.htm?majorhead=' + majorhead ;
                    //$('#subMajorHead').combobox('reload', url);
                    $('#subMajorHead').empty();
                    $('#subMajorHead').append('<option value="">-- Select --</option>');
                    $.getJSON(url, function (data) {
                        $.each(data, function (i, obj) {
                            $('#subMajorHead').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                        });
                    });
                }
            }

            function getMinorHeadListmaster(rec) {
                $('#minorHead').empty();
                if (rec) {
                    var majorHead = $('#majorHead').val();
                    var url = 'getMinorHeadListmaster.htm?submajorhead=' + rec + '&majorhead=' + majorHead;
                    $('#minorHead').empty();
                    $('#minorHead').append('<option value="">-- Select --</option>');
                    $.getJSON(url, function (data) {
                        $.each(data, function (i, obj) {
                            $('#minorHead').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                        });
                    });

                }
            }

            function getSubHeadList(rec) {
                $('#subHead').empty();
                if (rec) {
                    var minorHead = $('#minorHead').val();
                    var subMajorHead = $('#subMajorHead').val();
                    var url = 'getSubMinorHeadList.htm?minorHead=' + minorHead + '&submajorhead=' + subMajorHead;
                    $('#subHead').empty();
                    $('#subHead').append('<option value="">-- Select --</option>');
                    $.getJSON(url, function (data) {
                        $.each(data, function (i, obj) {
                            $('#subHead').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                        });
                    });
                    // alert(subHead);
                }
            }
            function getDetailHeadList(rec) {
                $('#DetailHead').empty();
                if (rec) {
                    var minorHead = $('#minorHead').val();
                    var subHead = $('#subHead').val();


                    var url = 'getDetailHeadList.htm?minorhead=' + minorHead + '&subhead=' + subHead;
                    $('#DetailHead').empty();
                    $('#DetailHead').append('<option value="">-- Select --</option>');
                    $.getJSON(url, function (data) {
                        $.each(data, function (i, obj) {
                            $('#DetailHead').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                        });

                    });
                }
            }
            
         /*  function getObjectheadList(rec){
               $('#ObjectHead').empty();
               if (rec) {
                   var detailhead=$('#DetailHead').val();
                   
                   //alert($('#DetailHead').val());
                    $('#ObjectHead').empty();
                    var url = 'getObjectHeadList.htm?detailhead=' + detailhead;
                    $('#ObjectHead').append('<option value="">-- Select --</option>');
                    $.getJSON(url, function (data) {
                        $.each(data, function (i, obj) {
                            $('#ObjectHead').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                        });
                    });
                }
               
               
           } */
          
        </script>
        
        <style>
            .row-margin{
                margin-bottom: 20px;
                margin-top: 20px;
            }
            .modal-content{
                margin-top: 20%;
            }
         </style>
    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/hrmsadminmenu.jsp"/> 
            <div id="page-wrapper" style="padding: 30px">
                <div class="row" style="margin-top:40px">
                    <form:form action="addChatOfAccount.htm" commandName="chatOfAccount" method="post">                        
                        <div class="container-fluid">
                            <form:hidden id='hiddenddoCode'  path='hiddenddoCode'/>  
                            <form:hidden id='hiddeptName'  path='hiddeptName'/>                           
                            <form:hidden id='chatId'  path='chatId'/>
                            <form:hidden id='hidDemandno'  path='hidDemandno'/>
                            <form:hidden id='hiddendeptCode'  path='hiddendeptCode'/>
                                                       
                            <div class="row" style="margin-bottom:20px">
                                <label class="control-label col-sm-1 ">Department Name</label>
                                <div class="col-sm-4" style="font-weight: bold;">
                                     ${chatOfAccount.hiddeptName}                                     
                                </div> 
                                <label class="control-label col-sm-1">Office Name</label>
                                <div class="col-sm-4" style="font-weight: bold;">
                                    ${chatOfAccount.hidddoName}       
                                </div> 
                            </div>
                                
                            <div class="row">
                                <label class="control-label col-sm-1 ">Demand Number <span style="color: red">*</span></label>
                                <div class="col-sm-4">
                                    <form:select class="form-control" path="demandno" id="demandNo" onclick="getMajorHeadList(this.value)">
                                        <form:option value="" label="Select"/>
                                        <form:options items="${billDemandList}" itemLabel="label" itemValue="value"/>                                     
                                    </form:select>
                                </div>
                                <div class="col-sm-1">                                   
                                </div>
                                <label class="control-label col-sm-1">Major Head <span style="color: red">*</span></label>
                                <div class="col-sm-4">
                                    <form:select class="form-control" path="majorHead" id="majorHead" onclick="getSubMajorHeadListmaster(this.value)">
                                        <form:option value="" label="Select"/>
                                        <form:options items="${majHeadList}" itemLabel="label" itemValue="value"/>                                      
                                    </form:select> 
                                </div>
                                <div class="col-sm-1">
                                     <button type="button" class="btn btn-info" data-toggle="modal" data-target="#myModal" data-backdrop="static" data-keyboard="false">Add</button>
                                </div>
                            </div><br>
                            
                            <div class="row">
                                <label class="control-label col-sm-1">Sub Major Head <span style="color: red">*</span></label>
                                <div class="col-sm-4">
                                    <form:select class="form-control" path="subMajorHead" id="subMajorHead" onclick="getMinorHeadListmaster(this.value)">
                                        <form:option value="" label="Select"/>
                                        <form:options items="${majorHeadList1}" itemLabel="label" itemValue="value"/>                                     
                                    </form:select> 
                                </div>
                                 <div class="col-sm-1">
                                     <button type="button" class="btn btn-info" data-toggle="modal" data-target="#sub-major" data-backdrop="static" data-keyboard="false">Add</button>
                                </div>
                                <label class="control-label col-sm-1">Minor Head <span style="color: red">*</span></label>
                                <div class="col-sm-4">
                                    <form:select class="form-control" path="minorHead"  id="minorHead" onclick="getSubHeadList(this.value)">
                                        <form:option value="" label="Select"/>
                                        <form:options items="${minorHeadList}" itemLabel="label" itemValue="value"/>                                     
                                    </form:select> 
                                </div>
                                 <div class="col-sm-1">
                                     <button type="button" class="btn btn-info" data-toggle="modal" data-target="#minor-head" data-backdrop="static" data-keyboard="false">Add</button>
                                </div>
                            </div><br>
                            
                            <div class="row">
                                <label class="control-label col-sm-1">Sub Head <span style="color: red">*</span></label>
                                <div class="col-sm-4">
                                    <form:select class="form-control" path="subHead" id="subHead" onclick="getDetailHeadList(this.value)">
                                        <form:option value="" label="Select"/>
                                        <form:options items="${subMinorHeadList}" itemLabel="label" itemValue="value"/>  
                                    </form:select>
                                </div>
                                <div class="col-sm-1">
                                     <button type="button" class="btn btn-info" data-toggle="modal" data-target="#sub-head" data-backdrop="static" data-keyboard="false">Add</button>
                                </div>
                                <label class="control-label col-sm-1" >Detail Head <span style="color: red">*</span></label>
                                <div class="col-sm-4" >
                                    <form:select class="form-control" path="DetailHead" id="DetailHead" >
                                        <form:option value="" label="Select"/>

                                        <form:options items="${detailHeadList}" itemLabel="label" itemValue="value"/>  
                                    </form:select> 
                                </div>
                                <div class="col-sm-1">
                                     <button type="button" class="btn btn-info" data-toggle="modal" data-target="#detail-head" data-backdrop="static" data-keyboard="false">Add</button>
                                </div>
                            </div><br>
                            
                            <div class="row">
                                <label class="control-label col-sm-1">Charge Voted <span style="color: red">*</span></label>
                                <div class="col-sm-4">
                                    <form:select class="form-control" path="ChargeVoted" id="ChargeVoted">
                                        <form:option value="" label="Select"/>
                                        <form:options items="${newchargedVotedList}" itemLabel="label" itemValue="value"/>  
                                    </form:select> 
                                </div>
                                <div class="col-sm-1">                                   
                                </div>                             
                                    <label class="control-label col-sm-1">Object Head <span style="color: red">*</span></label>
                                    <div class="col-sm-4">                                      
                                        <%-- <form:select class="form-control" path="ObjectHead" id="ObjectHead">
                                            <form:option value="" label="Select"/>
                                            <form:options items="${objectHeadList}" itemLabel="label" itemValue="value"/>  
                                        </form:select> --%>
                                         <form:input cssClass="form-control" path="ObjectHead" id="ObjectHead" maxlength="3" />
                                    </div>                            
                               <%--   <div class="col-sm-1">
                                     <button type="button" class="btn btn-info" data-toggle="modal" data-target="#object-head" data-backdrop="static" data-keyboard="false">Add</button>
                                </div>   --%>
                            </div><br>
                            
                            <div class="form-group col-sm-12" style="margin-top:20px">
                                <label class="control-label col-sm-1"></label>
                                <div class="text-center col-sm-12" >                                   
                                    <input type="submit" name="action" value="Save" onclick = "return validatechat();" class="btn btn-primary" style="width: 5%;"/>                                   
                                    <input type="submit" name="action" value="Cancel" class="btn btn-primary"/>
                                </div>
                            </div>
                        </div> 
                                    
                            <!-- Major Head Modal -->
                             <div class="modal fade" id="myModal" role="dialog">
                               <div class="modal-dialog">

                                    <!-- Modal content-->
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                                            <h4 class="modal-title">Add Major Head</h4>
                                        </div>
                                        <div class="modal-body">
                                            <div class="row row-margin">   
                                                <div class="form-group">
                                                    <label class="control-label col-sm-4">Add Major Head: <span style="color: red">*</span></label>
                                                    <div class="col-sm-6">
                                                        <form:input cssClass="form-control" path="modalmajorHead" id="modalmajorHead" maxlength="4" placeholder="Enter Major Head"/>
                                                    </div>
                                                </div>
                                            </div>
                                             <div class="row">   
                                                <div class="form-group">
                                                    <label class="control-label col-sm-4">Add Description: </label>
                                                    <div class="col-sm-6">
                                                        <form:input cssClass="form-control" path="majorheadDesc" id="majorheadDesc" placeholder="Enter Description"/>
                                                    </div>
                                                </div>
                                            </div>      
                                        </div>
                                        <div class="modal-footer">
                                            <input type="submit" name="action" value="Save Majorhead" class="btn btn-primary" onclick = "return checkMajorhead();" />
                                            <button type="button" class="btn" data-dismiss="modal">Close</button>
                                        </div>
                                    </div>

                                </div>
                            </div>          
                                    
                       <!--Sub Major Head Modal -->
                             <div class="modal fade" id="sub-major" role="dialog">
                               <div class="modal-dialog">

                                    <!-- Modal content-->
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                                            <h4 class="modal-title">Add Sub Major Head</h4>
                                        </div>
                                        <div class="modal-body">
                                            <div class="row row-margin">   
                                                <div class="form-group">
                                                    <label class="control-label col-sm-4">Add Sub Major Head: <span style="color: red">*</span></label>
                                                    <div class="col-sm-6">
                                                        <form:input cssClass="form-control" path="modalsubmajorHead" id="modalsubmajorHead" maxlength="2" placeholder="Enter Sub Major Head"/>
                                                    </div>
                                                </div>
                                            </div>
                                             <div class="row">   
                                                <div class="form-group">
                                                    <label class="control-label col-sm-4">Add Description: </label>
                                                    <div class="col-sm-6">
                                                        <form:input cssClass="form-control" path="submajorheadDesc" id="submajorheadDesc" placeholder="Enter Description"/>
                                                    </div>
                                                </div>
                                            </div>      
                                        </div>
                                        <div class="modal-footer">
                                            <input type="submit" name="action" value="Save SubMajorhead" class="btn btn-primary" onclick = "return checksubMajorhead();" />
                                            <button type="button" class="btn" data-dismiss="modal">Close</button>
                                        </div>
                                    </div>

                                </div>
                            </div>                        
                            <!--Minor Head Modal -->
                             <div class="modal fade" id="minor-head" role="dialog">
                               <div class="modal-dialog">

                                    <!-- Modal content-->
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                                            <h4 class="modal-title">Add Minor Head</h4>
                                        </div>
                                        <div class="modal-body">
                                            <div class="row row-margin">   
                                                <div class="form-group">
                                                    <label class="control-label col-sm-4">Add Minor Head: <span style="color: red">*</span></label>
                                                    <div class="col-sm-6">
                                                        <form:input cssClass="form-control" path="modalminorHead" id="modalminorHead" maxlength="3" placeholder="Enter Minor Head"/>
                                                    </div>
                                                </div>
                                            </div>
                                             <div class="row">   
                                                <div class="form-group">
                                                    <label class="control-label col-sm-4">Add Description: </label>
                                                    <div class="col-sm-6">
                                                        <form:input cssClass="form-control" path="minorheadDesc" id="minorheadDesc" placeholder="Enter Description"/>
                                                    </div>
                                                </div>
                                            </div>      
                                        </div>
                                        <div class="modal-footer">                                       
                                            <input type="submit" name="action" value="Save Minorhead" class="btn btn-primary" onclick = "return checkMinorhead();" />
                                            <button type="button" class="btn" data-dismiss="modal">Close</button>
                                        </div>
                                    </div>

                                </div>
                            </div>   
                             <!--Sub Head Modal -->
                             <div class="modal fade" id="sub-head" role="dialog">
                               <div class="modal-dialog">

                                    <!-- Modal content-->
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                                            <h4 class="modal-title">Add Sub Head</h4>
                                        </div>
                                        <div class="modal-body">
                                            <div class="row row-margin">   
                                                <div class="form-group">
                                                    <label class="control-label col-sm-4">Add Sub Head: <span style="color: red">*</span></label>
                                                    <div class="col-sm-6">
                                                        <form:input cssClass="form-control" path="modalsubHead" id="modalsubHead" maxlength="4" placeholder="Enter Sub Head"/>
                                                    </div>
                                                </div>
                                            </div>
                                             <div class="row">   
                                                <div class="form-group">
                                                    <label class="control-label col-sm-4">Add Description: </label>
                                                    <div class="col-sm-6">
                                                        <form:input cssClass="form-control" path="subheadDesc" id="subheadDesc" placeholder="Enter Description"/>
                                                    </div>
                                                </div>
                                            </div>      
                                        </div>
                                        <div class="modal-footer">
                                            <input type="submit" name="action" value="Save Subhead" class="btn btn-primary" onclick = "return checkSubhead();" />
                                            <button type="button" class="btn" data-dismiss="modal">Close</button>
                                        </div>
                                    </div>

                                </div>
                            </div>                                       
                                              
                             <!--Detail Head Modal -->
                             <div class="modal fade" id="detail-head" role="dialog">
                               <div class="modal-dialog">

                                    <!-- Modal content-->
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                                            <h4 class="modal-title">Add Detail Head</h4>
                                        </div>
                                        <div class="modal-body">
                                            <div class="row row-margin">   
                                                <div class="form-group">
                                                    <label class="control-label col-sm-4">Add Detail Head: <span style="color: red">*</span></label>
                                                    <div class="col-sm-6">
                                                        <form:input cssClass="form-control" path="modaldetailHead" id="modaldetailHead" maxlength="5" placeholder="Enter Detail Head"/>
                                                    </div>
                                                </div>
                                            </div>
                                             <div class="row">   
                                                <div class="form-group">
                                                    <label class="control-label col-sm-4">Add Description: </label>
                                                    <div class="col-sm-6">
                                                        <form:input cssClass="form-control" path="detailheadDesc" id="detailheadDesc" placeholder="Enter Description"/>
                                                    </div>
                                                </div>
                                            </div>      
                                        </div>
                                        <div class="modal-footer">
                                            <input type="submit" name="action" value="Save Detailhead" class="btn btn-primary" onclick = "return checkDetailhead();"  />
                                            <button type="button" class="btn" data-dismiss="modal">Close</button>
                                        </div>
                                    </div>

                                </div>
                            </div>      
                             <!--Object Head Modal -->
                             <%--    <div class="modal fade" id="object-head" role="dialog">
                               <div class="modal-dialog">

                                    <!-- Modal content-->
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                                            <h4 class="modal-title">Add Object Head</h4>
                                        </div>
                                        <div class="modal-body">
                                            <div class="row row-margin">   
                                                <div class="form-group">
                                                    <label class="control-label col-sm-4">Add Object Head: <span style="color: red">*</span></label>
                                                    <div class="col-sm-6">
                                                        <form:input cssClass="form-control" path="modalobjectHead" id="modalobjectHead" maxlength="3" placeholder="Enter Object Head"/>
                                                    </div>
                                                </div>
                                            </div>
                                             <div class="row">   
                                                <div class="form-group">
                                                    <label class="control-label col-sm-4">Add Description: </label>
                                                    <div class="col-sm-6">
                                                        <form:input cssClass="form-control" path="objectheadDesc" id="objectheadDesc" placeholder="Enter Description"/>
                                                    </div>
                                                </div>
                                            </div>      
                                        </div>
                                        <div class="modal-footer">
                                            <input type="submit" name="action" value="Save Objecthead" class="btn btn-primary"/>
                                            <button type="button" class="btn" data-dismiss="modal">Close</button>
                                        </div>
                                    </div>

                                </div>
                            </div>       --%>                                                                                                        
                    </form:form> 
                </div>                  
            </div>
        </div>                   
    </body>
</html>


