<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
 <style type="text/css">
            body{margin:0px;font-family: 'Roboto', sans-serif;}
           .training_form td{padding:6px;}
            .form-control{height:30px;}
            body{margin:0px;font-family: 'Arial', sans-serif;background:#F7F7F7}
            #left_container{background:#2A3F54;width:18%;float:left;min-height:700px;color:#FFFFFF;font-size:15pt;font-weight:bold;}
            #left_container ul{list-style-type:none;margin:0px;padding:0px;}
            #left_container ul li a{display:block;color:#EEEEEE;font-weight:normal;font-size:10pt;text-decoration:none;padding:10px 0px;padding-left:15px;}
            #left_container ul li a:hover{background:#465F79;color:#FFFFFF;}
            #left_container ul li a.sel{display:block;color:#EEEEEE;background:#367CAD;font-weight:normal;font-size:10pt;text-decoration:none;padding:10px 0px;padding-left:15px;}            
            table {border:1px solid #DADADA;}
            .panel-header{background:#5593BC;color:#FFFFFF;}
            .panel-title{margin-bottom:5px;}
            .panel-body{font-size:15pt;}
            .datagrid-header{background:#EAEAEA;border-style:none;}
            .datagrid-header-row{font-weight:bold;}
            .datagrid-cell, .datagrid-cell-group, .datagrid-header-rownumber, .datagrid-cell-rownumber{font-size:10pt;}
            .tblres td{padding:5px;}
            #wrapper{padding-left: 0px !important}
            body{background:#2A3F54 !important}
             #wrapper{background: white !important}
        </style>
<div id="left_container">
    <div style="border-bottom:1px solid #1A2734;margin-top:10px;padding:5px 0px;">Manage QMS Portal<br />
        <table width="100%" style="border:0px;">
            <tr>
                <td width="40"><img src="images/photo_icon.png" style="width:32px;" /></td>
                <td style="line-height:15px;"><span style="font-size:9pt;font-style:italic;color:#999999;font-weight:normal;">Welcome ${LoginUserBean.loginname}</span><br />
            <span style="font-size:10pt;font-weight:bold;text-transform:uppercase;color:#FFFFFF;">Welcome ${LoginUserBean.loginname}</span></td>
            </tr>
        </table>
        </div>
    <div style="border-top:1px solid #3B5A7C;font-size:10pt;padding-top:5px;">
        <ul style="">
             <c:forEach items="${Privileges}" var="Privilege">
                    <li>
                        <a href="${Privilege.modurl}"><i class="fa fa-fw fa-table"></i> ${Privilege.modname}</a>
                    </li>
                </c:forEach>
                <li>
                    <a href="showpasswordchange.htm"><i class="fa fa-fw fa-table"></i> Change Password</a>
                </li>     
            <li><a href="logout.htm"><img src="images/logout.png" width="16" style="vertical-align:middle;" /> &nbsp;Logout</a></li>
        </ul>
    </div>
</div>
<div style="float:left;width:82%">
    <div style="height:110px;padding-left:10px;padding-top:10px;background:#3483c5">
        <img src="images/ndc_logo.png" style="margin-left:10px;" />
    </div>
    <div style="min-height:570px;background:#FFFFFF;margin:10px;border:1px solid #DADADA;padding:10px;">