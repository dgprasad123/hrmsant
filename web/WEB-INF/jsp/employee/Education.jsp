<%-- 
    Document   : Education
    Created on : 6 Sep, 2017, 6:32:39 PM
    Author     : lenovo pc
--%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">   
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <script type="text/javascript" src="js/jquery.min.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
        <script src="js/moment.js" type="text/javascript"></script>
        <script type="text/javascript" src="js/common.js"></script>
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">
        <link rel="stylesheet" type="text/css" href="resources/css/colorbox.css"/>
        <link rel="stylesheet" type="text/css" href="css/popupmain.css"/>  
        <script type="text/javascript">
            function ViewMessage() {
                $('#mask').hide();
                $('.window').hide();
                //   window.location="viewCommunicationDetails.htm";
            }
            function showInformationWindow()
            {
                var id = '#dialog';

                //Get the screen height and width
                var maskHeight = $(document).height();
                var maskWidth = $(window).width();

                //Set heigth and width to mask to fill up the whole screen
                $('#mask').css({'width': maskWidth, 'height': maskHeight});

                //transition effect		
                $('#mask').fadeIn(500);
                $('#mask').fadeTo("slow", 0.9);

                //Get the window height and width
                var winH = $(window).height() - 100;
                var winW = $(window).width();

                //Set the popup window to center
                $(id).css('top', winH / 2 - $(id).height() / 2);
                $(id).css('left', winW / 2 - $(id).width() / 2);

                $(id).fadeIn(2000);
            }
            $(document).ready(function() {

                $('#mask').click(function() {
                    ViewMessage();
                });
            });
        </script>      
        <style type="text/css">
            #profile_container{width:95%;margin:0px auto;border-top:0px;}
            ol li{width:180px;float:left;text-align:left;}
        </style>  
        <script type="text/javascript">
            $(document).ready(function() {
                $('#subject').keyup(function() {
                    $(this).val($(this).val().toUpperCase());
                });
                $('#institute').keyup(function() {
                    $(this).val($(this).val().toUpperCase());
                });
            });
            function validate() {
                if ($('#qualification').val() == "") {
                    alert("Please select Qualification.");
                    return false;
                }
                if ($('#subject').val() == '') {
                    alert("Please Enter Subject");
                    $('#subject').focus();
                    return false;
                }
                if ($('#institute').val() == '') {
                    alert("Please Enter Institute");
                    $('#lname').focus();
                    return false;
                }
                return true;
            }
        </script>
    </head>
    <body>

        <div id="boxes">
            <div style=" left: 551.5px; display: none;" id="dialog" class="window"> 

                <table class="table-bordered" align='center'>
                    <tr>
                        <td align="center"><h2 class="alert alert-info" style="color:#FF0000;font-size:14pt;font-weight:bold;margin:0px;">
                                You have to submit the following details in order to complete your profile:</h2>
                            <span style="font-size:12pt;font-weight:bold;display:block;text-align:left;color:#008900;">Personal Information</span>
                            <ol style="margin:0px;font-size:10pt;">
                                <li>Employee Name</li>
                                <li>ACCT TYPE</li>
                                <li>GPF NO</li>
                                <li>FIRST NAME</li>
                                <li>LAST NAME</li>
                                <li>GENDER</li>
                                <li>MARITAL STATUS</li>
                                <li>CATEGORY</li>
                                <li>HEIGHT</li>
                                <li>DOB</li>
                                <li>JOIN DATE OF GOO</li>
                                <li>DATE OF ENTRY INTO GOVERNMENT</li>
                                <li>BLOOD GROUP</li>
                                <li>RELIGION</li>
                                <li>MOBILE</li>
                                <li>POST GROUP</li>
                                <li>HOME TOWN</li>
                                <li>DOMICILE</li>
                                <li>ID MARK</li>
                                <li>BANK NAME</li>
                                <li>BRANCH NAME</li>
                                <li>BANK ACCOUNT NUMBER</li>
                            </ol>
                            <div style="clear:both;"></div>
                            <span style="font-size:12pt;font-weight:bold;display:block;text-align:left;color:#008900;">Language</span>
                            <ol style="margin:0px;font-size:10pt;">
                                <li style="width:50%">LANGUAGE AT LEAST ONE</li>
                            </ol>
                            <div style="clear:both;"></div>
                            <span style="font-size:12pt;font-weight:bold;display:block;text-align:left;color:#008900;">Identity</span>
                            <ol style="margin:0px;font-size:10pt;">
                                <li>PAN NUMBER</li>
                                <li>AADHAAR NUMBER</li>
                            </ol>
                            <div style="clear:both;"></div>
                            <span style="font-size:12pt;font-weight:bold;display:block;text-align:left;color:#008900;">Address</span>
                            <ol style="margin:0px;font-size:10pt;">
                                <li style="width:50%">ADDRESS BOTH PERMANENT AND PRESENT</li>
                            </ol>


                            <div style="clear:both;"></div>
                            <span style="font-size:12pt;font-weight:bold;display:block;text-align:left;color:#008900;">Family</span>
                            <ol style="margin:0px;font-size:10pt;">
                                <li style="width:50%">FATHERS NAME/HUSBAND NAME</li>
                            </ol>
                            <div style="clear:both;"></div>
                            <span style="font-size:12pt;font-weight:bold;display:block;text-align:left;color:#008900;">Education</span>
                            <ol style="margin:0px;font-size:10pt;">
                                <li style="width:50%">QUALIFICATION AT LEAST ONE</li>
                            </ol>
                        </td>
                    </tr>

                </table>            
                <div id="popupfoot" style="text-align:center;"> 
                    <a href="javascript:void(0)" onclick="javascript: ViewMessage();" class="close agree btn pri" style="background:#FF0000;color:#FFFFFF;">Close</a> 
                </div>            

            </div>    
            <form:form action="saveeducation.htm" method="post" class="form-horizontal" commandName="education">
                <form:hidden path="deptCode" id='deptCode'/>
                <div style=" margin-bottom: 5px;" class="panel panel-info">
                    <div class="panel-body">
                        <div class="form-group">
                            <label class="control-label col-sm-2" for="email">Qualification:</label>
                            <div class="col-sm-4">
                                <form:hidden path="empId"/>
                                <form:hidden path="qfn_id"/>
                                <form:select path="qualification" id="qualification" class="form-control">
                                    <form:option value="">--Select--</form:option>
                                    <form:options items="${qualificationList}" itemLabel="qualification" itemValue="qualification"/>
                                </form:select>                            
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-2" for="email">Stream:</label>
                            <div class="col-sm-4">           
                                <form:select path="faculty" class="form-control">
                                    <form:option value="">--Select--</form:option>
                                    <form:options items="${facultyList}" itemLabel="facultyType" itemValue="facultyType"/>
                                </form:select>                            
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-2" for="email">Year of Pass:</label>
                            <div class="col-sm-4">
                                <form:select path="yearofpass" class="form-control">
                                    <form:option value="">--Select--</form:option>
                                    <form:options items="${yearofpasslist}" itemValue="value" itemLabel="label"/> 
                                </form:select>                            
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-2" for="email">Degree:</label>
                            <div class="col-sm-4">
                                <form:select path="degree" class="form-control">
                                    <form:option value="">--Select--</form:option>
                                    <form:options items="${degreeList}" itemValue="degreeCode" itemLabel="degreeName"/>
                                </form:select>                            
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-2" for="subject">Subject:</label>
                            <div class="col-sm-4"> 
                                <form:input path="subject" class="form-control" maxlength="200"/>      
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-2" for="email">Institute:</label>
                            <div class="col-sm-4">                            
                                <form:input path="institute" class="form-control" maxlength="100"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-2" for="email">Board / University:</label>
                            <div class="col-sm-4">                            
                                <form:select path="board" class="form-control">
                                    <form:option value="">--Select--</form:option>
                                    <form:options items="${universityList}" itemValue="boardName" itemLabel="boardName"/>
                                </form:select> 
                            </div>
                        </div>
                    </div>
                    <div class="panel-footer">
                        <c:if test="${not empty education.qfn_id && education.qfn_id == 0}">
                            <input type="submit" class="btn btn-success" name="action" value="Save" onclick="return validate();"/>
                        </c:if>
                        <c:if test="${not empty education.qfn_id && education.qfn_id > 0}">
                            <input type="submit" class="btn btn-success" name="action" value="Update" onclick="return validate();"/>
                            <input type="submit" class="btn btn-danger" name="action" value="Delete" onclick="return confirm('Are you sure to Delete?');"/>                        
                            <input type="submit" class="btn btn-primary" name="action" value="Back"/>
                        </c:if>                    
                    </div>
                </div>
            </form:form>    
            <table class="table table-bordered">
                <thead>
                    <tr class="bg-primary text-white">
                        <th>#</th>
                        <th>Qualification</th>
                        <th>Faculty</th>
                        <th>Year of Pass</th>
                        <th>Subject</th>
                        <th>Institute</th>
                        <th>Board</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${educationList}" var="education" varStatus="cnt">
                        <tr>
                            <th scope="row">${cnt.index+1}</th>
                            <td>${education.qualification}</td>
                            <td>${education.faculty}</td>
                            <td>${education.yearofpass}</td>
                            <td>${education.subject}</td>
                            <td>${education.institute}</td>
                            <td>${education.board}</td>
                            <td>
                                <c:if test="${education.isLocked eq 'N' || (not empty AllowUpdate && AllowUpdate eq 'Y')}">
                                    <a href="editEmplyeeEducaton.htm?qfn_id=${education.qfn_id}">Edit</a>
                                </c:if>
                                <c:if test="${empty AllowUpdate}">
                                    <c:if test="${education.isLocked eq 'Y'}">
                                        <img src="images/Lock.png" width="20" height="20"/>
                                    </c:if>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
    </body>
</html>
