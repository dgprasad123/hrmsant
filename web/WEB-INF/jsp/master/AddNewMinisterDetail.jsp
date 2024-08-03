<%-- 
    Document   : AddNewMinisterDetail
    Created on : 3 Feb, 2022, 2:02:14 PM
    Author     : Devi
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
        <script type="text/javascript">
            $(window).load(function() {

                $(".cur-dept").on('click', '.removeBtn', function() {
                    if (confirm("Do you want to Remove this Department?")) {
                        $(this).parent().remove();
                    }
                });
            })
            $(document).ready(function() {
                $("#dep-div").hide();
                selectDept();
            });

            function selectDept() {
                if ($('#off_as').val() == '06' || $('#off_as').val() == '05' || $('#off_as').val() == '02') {
                    $("#dep-div").show();
                }
                else {
                    $("#dep-div").hide();
                    $('#deptname').val('');
                }
            }

            function addDeptDiv() {
                var childlength = $("div.cur-dept").length;
                var rowcount = childlength;
                //$("#clickedrownum").val(rowcount);
                var formstring = "<div id='cur-dept" + childlength + "' style='margin-bottom: 10px;margin-left: -14px;margin-top: 10px;'>";
                formstring = formstring + "<div class='col-md-10 dept-name'><select class='form-control' name='deptname' id='deptname1' style='margin-bottom: 10px;'><option value=''>-Select Current Department-</option>  <option value='01'>AGRICULTURE AND FARMERS EMPOWERMENT</option>  \n\
                <option value='02'>COMMERCE AND TRANSPORT (COMMERCE)</option> <option value='03'>COMMERCE AND TRANSPORT (TRANSPORT)</option> <option value='04'>COOPERATION</option><option value='05'>ENERGY</option>  \n\
                <option value='06'>EXCISE</option> <option value='07'>FINANCE</option><option value='08'>FISHERIES AND ANIMAL RESOURCES DEVELOPMENT</option><option value='09'>FOOD SUPPLIES AND CONSUMER WELFARE</option><option value='10'>FOREST AND ENVIRONMENT</option> \n\
                <option value='11'>GENERAL ADMINISTRATION AND PUBLIC GRIEVANCE</option><option value='12'>HEALTH AND FAMILY WELFARE</option> <option value='13'>HIGHER EDUCATION</option> <option value='14'>HOME</option> <option value='15'>HOUSING AND URBAN DEVELOPMENT</option>    \n\
                <option value='16'>INDUSTRIES</option> <option value='17'>INFORMATION AND PUBLIC RELATIONS</option> <option value='18'>ELECTRONICS AND INFORMATION TECHNOLOGY</option> <option value='19'>LABOUR AND ESI</option> <option value='20'>LAW</option>   \n\
                <option value='21'>PANCHAYATI RAJ AND DRINKING WATER</option> <option value='22'>PARLIAMENTARY AFFAIRS</option> <option value='23'>PLANNING AND CONVERGENCE</option> <option value='24'>PUBLIC ENTERPRISE</option> <option value='26'>REVENUE AND DISASTER MANAGEMENT</option>   \n\
                <option value='27'>RURAL DEVELOPMENT</option> <option value='28'>SCHOOL AND MASS EDUCATION</option> <option value='29'>SCIENCE AND TECHNOLOGY</option> <option value='30'>SPORTS AND YOUTH SERVICES</option> <option value='31'>SCHEDULED TRIBES AND SCHEDULED CASTES DEVELOPMENT, MINORITIES AND BACKWARD CLASSES WELFARE</option> \n\
                <option value='32'>STEEL AND MINES</option> <option value='33'>HANDLOOMS, TEXTILES AND HANDICRAFTS</option> <option value='34'>ODIA LANGUAGE LITERATURE AND CULTURE</option> <option value='35'>TOURISM</option> <option value='36'>WATER RESOURCES</option>\n\
                <option value='37'>WOMEN AND CHILD DEVELOPMENT AND MISSION SHAKTI</option> <option value='38'>WORKS</option> <option value='50'>ODISHA LEGISLATIVE ASSEMBLY</option> <option value='72'>SKILL DEVELOPMENT AND TECHNICAL EDUCATION</option> <option value='73'>MICRO SMALL AND MEDIUM ENTERPRISES</option><option value='74'>SOCIAL SECURITY AND EMPOWERMENT OF PERSON WITH DISABILITIES</option></select></div>";
                formstring = formstring + "<button type='button' class='btn btn-danger add-btn' id='adddep' onclick='addDeptDiv()' style='margin-right: 5px;'>Add</button>"

                formstring = formstring + "<button type='button' class='btn btn-danger removeBtn' id='removedep' onclick='removeDeptDiv()'>Remove</button>";
                formstring = formstring + "</div>";

                $("div.cur-dept > div:last").append(formstring);

            }


            function validateadd() {
                if ($('#off_as').val() == "")
                {
                    alert("please select Minister Type");
                    $('#off_as').focus();
                    return false;
                }
                if ($('#deptname').val()) {
                    if ($('#deptname').val() == "")
                    {
                        alert("please select Current Department");
                        $('#deptname').focus();
                        return false;
                    }
                }
                if ($('#activemember').val() == "")
                {
                    alert("please select Active Member");
                    $('#activemember').focus();
                    return false;
                }
                if ($('#fname').val() == "")
                {
                    alert("please select First Name of Minister");
                    $('#fname').focus();
                    return false;
                }
                if ($('#lname').val() == "")
                {
                    alert("please select Last Name of Minister");
                    $('#lname').focus();
                    return false;
                }
                if ($('#userid').val() == "")
                {
                    alert("please Enter User Id");
                    $('#userid').focus();
                    return false;
                }
                if ($('#password').val() == "")
                {
                    alert("please Enter Password");
                    $('#password').focus();
                    return false;
                }
                // savecheckUid();
            }

            /* function savecheckUid(){
             userId = $("#userid").val();                    
             if (userId ) {
             
             $.post("savegetExistingUserid.htm", {userid: userId}, "json")
             .done(function(data) {                                  
             if (data.msg == "Y") {                                 
             alert("User Id not exist you can keep this one and saved sucessfully");
             } 
             else if (data.msg == "D") {                                          
             alert("User Id already exist, please try another.");
             $('#userid').focus();
             }
             });
             }     
             
             } */

            function checkUid() {
                userId = $("#userid").val();
                if (userId) {
                    $.post("getExistingUserid.htm", {userid: userId}, "json")
                            .done(function(data) {
                                if (data.msg == "Y") {
                                    alert("This User Id available, you can keep this one");
                                    $('#savebtn').show();
                                }
                                else if (data.msg == "D") {
                                    alert("User Id already exist, please try another");
                                    $('#userid').focus();
                                    $('#savebtn').hide();
                                }
                            });
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
        <div id="wrapper">
            <jsp:include page="../tab/hrmsadminmenu.jsp"/>        
            <div id="page-wrapper">
                <div class="container-fluid">
                    <!-- Page Heading -->
                    <div class="row">
                        <div class="col-lg-12">                            
                            <ol class="breadcrumb">
                                <li>
                                    <i class="fa fa-dashboard"></i>  <a href="#">Dashboard</a>
                                </li>
                                <li class="active">
                                    <i class="fa fa-file"></i> Add New Minister Details 
                                </li>

                            </ol>
                        </div>
                    </div>
                    <div class="row">
                        <form:form action="saveMinisterDetail.htm" method="post" commandName="mindetailsForm">   
                            <form:hidden class="form-control" path="hidlmid" id="hidlmid"/>
                            <form:hidden class="form-control" path="deptcode" id="deptcode"/>
                            <form:hidden class="form-control" path="hidoff_as" id="hidoff_as"/>
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    Minister Details
                                </div>
                                <div class="panel-body"> 

                                    <c:if test="${empty mindetailsForm.fname}">
                                        <div class="row row-margin" id="min-type">
                                            <label class="control-label col-sm-2" style="font-size: 15px;">Select Minister Type: <span style="color: red">*</span></label>
                                            <div class="col-sm-6"> 
                                                <form:select class="form-control" path="off_as" id="off_as" onchange="selectDept()" disabled="true">
                                                    <form:option value="">-Select Current Post-</form:option>
                                                    <form:options items="${Officiatinglist}" itemLabel="officiatingName" itemValue="officiatingId"/>                                                                               
                                                </form:select>
                                            </div>                                 
                                        </div>
                                    </c:if>
                                    <c:if test="${not empty mindetailsForm.fname}">
                                        <div class="row row-margin" id="min-type">
                                            <label class="control-label col-sm-2" style="font-size: 15px;">Select Minister Type: <span style="color: red">*</span></label>
                                            <div class="col-sm-6"> 
                                                <form:select class="form-control" path="off_as" id="off_as" onchange="selectDept()">
                                                    <form:option value="">-Select Current Post-</form:option>
                                                    <form:options items="${Officiatinglist}" itemLabel="officiatingName" itemValue="officiatingId"/>                                                                               
                                                </form:select>
                                            </div>                                 
                                        </div>
                                    </c:if>

                                    <c:if test="${not empty mindetailsForm.assigneddeptlist}">
                                        <c:forEach items="${mindetailsForm.assigneddeptlist}" var="dept" varStatus="counter"> 
                                            <div class="row row-margin cur-dept" id="dep-div">
                                                <label class="control-label col-sm-2" style="font-size: 15px;">Current Department: <span style="color: red">*</span></label>
                                                <div class="col-sm-6 dept-name"> 
                                                    <form:select class="form-control" path="deptname" id="deptname" style="margin-bottom: 10px;" multiple="true">
                                                        <option value="">-Select Current Department-</option>
                                                        <c:forEach items="${departmentList}" var="obj">
                                                            <option value="${obj.deptCode}" ${dept.deptcode == obj.deptCode ? 'selected="selected"' : ''}>${obj.deptName}</option>    
                                                        </c:forEach>
                                                    </form:select>
                                                </div>  
                                                <c:if test="${counter.count eq 1}">
                                                    <button type="button" class="btn btn-danger add-btn" id="adddep" onclick="addDeptDiv()">Add</button>
                                                </c:if>
                                                <c:if test="${counter.count ne 1}">
                                                    <button type="button" class="btn btn-danger removeBtn" id="removedep" >Remove</button>
                                                </c:if>
                                            </div>
                                        </c:forEach>                      
                                    </c:if>
                                    <c:if test="${empty mindetailsForm.assigneddeptlist}">
                                        <div class="row row-margin cur-dept" id="dep-div">
                                            <label class="control-label col-sm-2" style="font-size: 15px;">Current Department: <span style="color: red">*</span></label>
                                            <div class="col-sm-6 dept-name"> 
                                                <form:select class="form-control" path="deptname" id="deptname" style="margin-bottom: 10px;" multiple="true">
                                                    <form:option value="">-Select Current Department-</form:option>
                                                    <form:options items="${departmentList}" itemLabel="deptName" itemValue="deptCode"/>                                                                               
                                                </form:select>
                                            </div>   
                                            <button type="button" class="btn btn-danger add-btn" id="adddep" onclick="addDeptDiv()">Add</button>
                                            <button type="button" class="btn btn-danger removeBtn" id="removedep">Remove</button>
                                        </div> 
                                    </c:if>


                                    <div class="row row-margin">
                                        <label class="control-label col-sm-2" style="font-size: 15px;">Active Member <span style="color: red">*</span></label>
                                        <div class="col-sm-6">                                       
                                            <form:select path="activemember" id="activemember" class="form-control">
                                                <form:option value="Y"> Yes </form:option>
                                                <form:option value="N"> No </form:option>
                                            </form:select>
                                        </div>                                   
                                    </div>

                                    <div class="row row-margin">
                                        <label class="control-label col-sm-2" style="font-size: 15px;">Title: </label>
                                        <div class="col-sm-6"> 
                                            <form:select path="initial" id="title" class="form-control"     >
                                                <form:option value="">-Select Title-</form:option>
                                                <form:options items="${empTitleList}" itemLabel="label" itemValue="value"/>                                                                                
                                            </form:select>
                                        </div>                                   
                                    </div>
                                    <div class="row row-margin">
                                        <div class="col-lg-2">
                                            <label for="fname">First Name Of Minister <span style="color: red">*</span></label>
                                        </div>
                                        <div class="col-sm-6">
                                            <form:input cssClass="form-control" path="fname" id="fname" placeholder="Enter First Name" />
                                        </div>
                                    </div>  
                                    <div class="row row-margin">
                                        <div class="col-lg-2">
                                            <label for="mnane">Middle Name Of Minister </label>
                                        </div>
                                        <div class="col-sm-6">
                                            <form:input cssClass="form-control" path="mnane" id="mnane" placeholder="Enter Middle Name" />
                                        </div>
                                    </div>  
                                    <div class="row row-margin">
                                        <div class="col-lg-2">
                                            <label for="lname">Last Name Of Minister <span style="color: red">*</span></label>
                                        </div>
                                        <div class="col-sm-6">
                                            <form:input cssClass="form-control" path="lname" id="lname" placeholder="Enter Last Name"/>
                                        </div>
                                    </div> 
                                    <c:if test="${empty mindetailsForm.userid}">
                                        <div class="row row-margin">
                                            <div class="col-lg-2">
                                                <label for="userid">User Id <span style="color: red">*</span></label>
                                            </div>
                                            <div class="col-sm-6">
                                                <form:input cssClass="form-control" path="userid" id="userid" placeholder="Enter User Id" />
                                            </div>
                                            <button type="button" class="btn btn-primary" onclick="checkUid()">Check User Id</button>
                                        </div> 
                                        <div class="row row-margin">
                                            <div class="col-lg-2">
                                                <label for="password">Password <span style="color: red">*</span></label>
                                            </div>
                                            <div class="col-sm-6">
                                                <form:input cssClass="form-control" path="password" id="password" placeholder="Enter Password" />
                                            </div>
                                        </div>     
                                    </c:if>  
                                    <c:if test="${not empty mindetailsForm.hidlmid && not empty mindetailsForm.userid}">

                                        <div class="row row-margin" id="hide-uid">
                                            <div class="col-lg-2">
                                                <label for="userid">User Id <span style="color: red">*</span></label>
                                            </div>
                                            <div class="col-sm-6">
                                                <form:input cssClass="form-control" path="userid" id="userid" placeholder="Enter User ID" readonly="true"  />
                                            </div>
                                        </div> 
                                        <div class="row row-margin" id="hide-pwd">
                                            <div class="col-lg-2">
                                                <label for="password">Password <span style="color: red">*</span></label>
                                            </div>
                                            <div class="col-sm-6">
                                                <form:input cssClass="form-control" path="password" id="password" placeholder="Enter Password" readonly="true" />
                                            </div>
                                        </div>   
                                    </c:if>    

                                    <div class="row row-margin">
                                        <div class="col-lg-2">
                                            <label for="mobileno">Mobile Number </label>
                                        </div>
                                        <div class="col-sm-6">
                                            <form:input cssClass="form-control" path="mobileno" id="mobileno" placeholder="Enter Mobile Number" />
                                        </div>
                                    </div>       
                                </div>
                                <div class="form-group col-sm-12" style="margin-top:20px">
                                    <label class="control-label col-sm-1"></label>
                                    <div class="text-center col-sm-12" >     

                                        <button type="submit" class="btn btn-primary" id="savebtn" name="action" value="Save Minister" onclick ="return validateadd();">Save</button> 

                                        <a href="MinisterDetails.htm"><button type="button" class="btn btn-warning btn-md">&laquo;Back</button></a>
                                    </div>
                                </div>
                            </div>         
                        </form:form>
                    </div>
                </div>
            </div>
    </body>
</html>

