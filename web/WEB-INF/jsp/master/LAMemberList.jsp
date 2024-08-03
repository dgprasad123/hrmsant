<%-- 
    Document   : LAMemberList
    Created on : Aug 19, 2017, 1:25:47 PM
    Author     : Manas
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        <link href="css/sb-admin.css" rel="stylesheet">
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/moment.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
        <script type="text/javascript">
            function showNewLAMemberWindow() {
                clearform("myModal");
                $("#myModal").modal('show');
            }
            function clearform(divid) {
                $("#" + divid).find('input').each(function() {
                    $(this).val("");
                });
            }
            function getMemberList() {
                if ($('#officiating').val() == "") {
                    alert("Choose Officiating");
                    return false;
                }
                $('#memberList').empty();
                var url = 'getLAMemberListJSON.htm?officiating=' + $('#officiating').val();                
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        mname = "";
                        if(obj.mname) {mname = obj.mname;}
                        html = '<tr><td>' + (i + 1) + '</td><td>' + obj.initials + ' ' + obj.fname + ' ' + mname + ' ' + obj.lname + ' ' + '</td><td>' + obj.offName + '</td><td>' + obj.active + '</td><td>'
                                ' <a href="javascript:removeMembers(' + obj.empId + ')" class="btn  btn-default"><span class="glyphicon glyphicons-remove"></span>Remove</a>' + 
                                ' <a href="javascript:editMembers(' + obj.empId + ')" class="btn  btn-default"><span class="glyphicon glyphicons-remove"></span>Edit</a>';
                        if(obj.active == "Y"){
                            html = html + ' <a href="javascript:inActivateMember(' + obj.empId + ')" class="btn  btn-default"><span class="glyphicon glyphicons-remove"></span>Inactivate</a>'
                        }else{
                            html = html + ' <a href="javascript:activateMember(' + obj.empId + ')" class="btn  btn-default"><span class="glyphicon glyphicons-remove"></span>Activate</a>'
                        }
                        html = html + '</td></tr>';
                        $('#memberList').append(html);
                    });
                });

            }
            function saveMember() {                
                $.post('saveLAMember.htm', $('#fm').serialize()).done(function (data) {
                    $('#myModal').modal('close'); // close the dialog                     
                });
            }
            function editMembers(lmid){
                
            }
            function removeMembers(lmid){
                
            }
            function inActivateMember(lmid){
                $.post('inActivateMember.htm',{ lmid: lmid }).done(function (data) {
                    alert("Data Saved");                
                });
            }
            function activateMember(lmid){
                $.post('activateMember.htm',{ lmid: lmid }).done(function (data) {
                     alert("Data Saved");               
                });
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
                                    <i class="fa fa-file"></i> Legislative Assembly Members List 
                                </li>
                                <li class="active">
                                    <i class="fa fa-file"></i> <a href="javascript:showNewLAMemberWindow()">New Legislative Assembly Member</a>
                                </li>
                            </ol>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-lg-12">
                            <div class="form-group">
                                <label>Officiating As</label>
                                <select class="form-control"  name="officiating" id="officiating">
                                    <c:forEach var="officiating" items="${officiatinglist}">
                                        <option value="${officiating.officiatingId}">${officiating.officiatingName}</option>
                                    </c:forEach>                                    
                                </select>
                            </div>
                            <button type="button" class="btn btn-default" onclick="getMemberList()">Show LAMembers</button>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-lg-12">
                            <h2>Legislative Assembly Member List</h2>
                            <div class="table-responsive">
                                <table class="table table-bordered table-hover table-striped">
                                    <thead>
                                        <tr>
                                            <th>Sl No</th>
                                            <th>Full Name</th>
                                            <th>Officiating As</th>
                                            <th>Is Active</th>
                                            <th>Action</th>                                            
                                        </tr>
                                    </thead>
                                    <tbody id="memberList">                                        

                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>

                </div>
            </div>
        </div>
        <!-- Modal -->
        <div id="myModal" class="modal fade" role="dialog">
            <div class="modal-dialog">

                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">New Legislative Member</h4>
                    </div>
                    <div class="modal-body">
                        <form id="fm" method="post" novalidate style="margin:0;padding:10px 50px">
                            <div class="form-group">
                                <label>Initials</label>
                                <select name="initials" id="initials" class="form-control">
                                    <option value="SHRI.">SHRI.</option>
                                    <option value="SMT.">SMT.</option>
                                    <option value="DR.">DR.</option>
                                </select>                            
                            </div>
                            <div class="form-group">
                                <label>First Name</label>                                                        
                                <input type="text" class="form-control"  name="fname" id="fname" />                            
                            </div>
                            <div class="form-group">
                                <label>Middle Name</label>                            
                                <input type="text" class="form-control"  name="mname" id="mname" />                            
                            </div>
                            <div class="form-group">
                                <label>Last Name</label>
                                <input type="text" class="form-control"  name="lname" id="lname" />
                            </div>
                            <div class="form-group">
                                <label>Officiating AS</label>
                                <select name="off_as" id="off_as" class="form-control">
                                    <c:forEach var="officiating" items="${officiatinglist}">
                                        <option value="${officiating.officiatingId}">${officiating.officiatingName}</option>
                                    </c:forEach>
                                </select>
                            </div>                            
                            <div class="form-group">
                                <label>Mobile No</label>
                                <input type="text" class="form-control" maxlength="10"  name="mobile" id="mobile" />
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <span id="msg"></span>
                        <button type="button" class="btn btn-default" onclick="saveMember()">Save</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    </div>

                </div>

            </div>
        </div>
    </body>
</html>
