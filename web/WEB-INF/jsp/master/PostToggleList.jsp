<%-- 
    Document   : OfficeList
    Created on : Nov 18, 2017, 1:02:16 PM
    Author     : manisha
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
            function toggleUpdate(columnName, spc, colStatus)
            {
                if(colStatus == 'N')
                    newColStatus = 'Y';
                if(colStatus == 'Y')
                    newColStatus = 'N';                
                if(columnName == 'ifuclean')
                    $('#uloader_'+spc).css('display', 'block');
                if(columnName == 'isSanctioned')
                    $('#isloader_'+spc).css('display', 'block');
                $.ajax({
                  url: 'ToggleSPCstatus.htm',
                  data: 'columnName='+columnName+'&spc='+spc+'&colStatus='+colStatus,
                  type: 'get',
                  success: function(retVal) {
                        if(columnName == 'ifuclean')
                        {
                            $('#uloader_'+spc).css('display', 'none');
                            $('#uclean_'+spc).html('<a href="javascript:void(0)" onclick="javascript: toggleUpdate(\'ifuclean\', \''+spc+'\', \''+newColStatus+'\')">'+newColStatus+'</a>');
                        }
                        if(columnName == 'isSanctioned')
                        {
                            $('#isloader_'+spc).css('display', 'none');
                            $('#is_'+spc).html('<a href="javascript:void(0)" onclick="javascript: toggleUpdate(\'isSanctioned\', \''+spc+'\', \''+newColStatus+'\')">'+newColStatus+'</a>');
                        }
                  }
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
                                    <i class="fa fa-file"></i> Post List 
                                </li>
                                <li class="active">
                                    <i class="fa fa-file"></i> <a href="newPost.htm">New Post</a>
                                </li>
                            </ol>
                        </div>
                    </div>
                    
                    <div class="row">
                        <div class="col-lg-12">
                            <input type="button" value="&laquo; Back to Substansive Post" class="btn btn-success btn-md" style="font-weight:bold;background:#00629B" onclick="javascript: self.location='substantivePost.htm'" />
                            <h2 style="font-size:12pt;">Department: <strong style="color:#890000;">${deptName}</strong><br />
                                Office: <strong style="color:#890000;">${officeName}</strong><br />
                                Toggle Status for Post <span style="color:#008900;">${postName}</span></h2>
                            <div class="table-responsive" id="cadre_list">
                                <table class="table table-bordered table-hover table-striped">
                                    <thead>
                                        <tr>
                                            <th width="40">Sl No</th>
                                            <th width="15%">SPC</th>
                                            <th width="15%">SPN</th>
                                            <th>HRMS ID</th>
                                            <th>Employee Name</th>
                                            <th>GPF No.</th>
                                            <th>Is Regular?</th>
                                            <th style="text-align:center;" width="40">Ifuclean</th>
                                            <th style="text-align:center;" width="40">Is Sanctioned?</th>
                                        </tr>
                                    </thead>
                                    <tbody>                                        
                                        <c:forEach items="${spcList}" var="spc" varStatus="count">
                                            <tr>
                                                <td>${count.index + 1}</td>
                                                <td>${spc.spc}</td>
                                                <td>${spc.spn}</td>
                                                <td>${spc.spcHrmsId}</td>
                                                <td>${spc.empName}</td>
                                                <td>${spc.gpfNo}</td>
                                                <td>${spc.isRegular}</td>
                                                <td align="center"><div id="uclean_${spc.spc}"><a href="javascript:void(0)" onclick="javascript: toggleUpdate('ifuclean', '${spc.spc}', '${spc.ifUclean}')">${spc.ifUclean}</a></div>
                                                    <span id="uloader_${spc.spc}" style="display:none;color:#777777;font-style:italic;font-size:8pt;"><img src="images/ajax-loader_1.gif" /> Please wait...</span></td>
                                                <td align="center"><div id="is_${spc.spc}"><a href="javascript:void(0)" onclick="javascript: toggleUpdate('isSanctioned', '${spc.spc}', '${spc.isSanctioned}')">${spc.isSanctioned}</a></div>
                                                    <span id="isloader_${spc.spc}" style="display:none;color:#777777;font-style:italic;font-size:8pt;"><img src="images/ajax-loader_1.gif" /> Please wait...</span></td>                                                
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>

                </div>
            </div>
        </div>
    </body>
</html>


