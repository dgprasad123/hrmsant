<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%
    String contextPath = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + contextPath + "/";

%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet" type="text/css">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="css/sb-admin.css" rel="stylesheet" type="text/css">
        <script src="js/jquery.min.js" type="text/javascript"></script>
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        
        <script type="text/javascript">
            
        </script>

    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/deptadminmenu.jsp"/>        
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
                                    <i class="fa fa-file"></i>Change Password
                                </li>
                                
                            </ol>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-lg-12">
                            <h2>Change password</h2>
                            <span id="error_msg" class="error_msg"> ${message} </span>
                            <div id="changpwddlg" class="easyui-dialog" title="Change Password" data-options="iconCls:'icon-save',modal:true,closed: true" style="width:700px;height:350px;padding:10px">
                                <form method="post" name="loginForm" action="changePasswordmisAction.htm" id="changpwdfm" data-toggle="validator" role="form">
                                    <div align="center" style="color: red;"><span id="msgspan"></span></div>
                                    <div class="easyui-panel" style="width:100%;max-width:700px;padding:10px 20px;"> 
                                        <div style="margin-bottom:20px">
                                            <input placeholder="Current Password" class="easyui-passwordbox" prompt="Current Password" name="userPassword" id="userPassword" label="Current Password:" labelWidth="140" iconWidth="28" style="width:100%;height:34px;padding:10px" required="1">
                                        </div>                    
                                        <div style="margin-bottom:20px">
                                            <input  placeholder="New Password" class="easyui-passwordbox" prompt="New Password" name="newpassword" id="newpassword" label="New Password:" labelWidth="140" iconWidth="28" maxlength='12' style="width:100%;height:34px;padding:10px"  required="1">
                                        </div>
                                        <div style="margin-bottom:20px">
                                            <input  placeholder="Confirm Password" class="easyui-passwordbox" prompt="Confirm Password" name="confirmpassword" id="confirmpassword" label="Confirm Password:" labelWidth="140" iconWidth="28" maxlength='12' style="width:100%;height:34px;padding:10px"  required="1">
                                        </div>
                                        <div style="margin-bottom:20px">
                                            
                                            <input type="submit" value="Change"/>
                                        </div>
                                        <div style="margin-bottom:20px">
                                            <span class="help-block" style="color: red;">Password policy to match 8 characters with alphabets in combination with numbers and special characters. e.g Welcome@12</span>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>

                </div>
            </div>
        </div>
    </body>
</html>