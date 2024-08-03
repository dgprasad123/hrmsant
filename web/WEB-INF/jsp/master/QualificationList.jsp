<%-- 
    Document   : UniversityList
    Created on : 5 May, 2022, 11:35:22 AM
    Author     : Devikrushna
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
         <script type="text/javascript" language="javascript" src="https://code.jquery.com/jquery-3.5.1.js"></script>
        <script type="text/javascript" language="javascript" src="https://cdn.datatables.net/1.12.1/js/jquery.dataTables.min.js"></script>
        <script type="text/javascript" language="javascript" src="https://cdn.datatables.net/buttons/2.2.3/js/dataTables.buttons.min.js"></script>
        <script type="text/javascript" language="javascript" src="https://cdnjs.cloudflare.com/ajax/libs/jszip/3.1.3/jszip.min.js"></script>
        <script type="text/javascript" language="javascript" src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.1.53/pdfmake.min.js"></script>
        <script type="text/javascript" language="javascript" src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.1.53/vfs_fonts.js"></script>
        <script type="text/javascript" language="javascript" src="https://cdn.datatables.net/buttons/2.2.3/js/buttons.html5.min.js"></script>
        <script type="text/javascript" language="javascript" src="https://cdn.datatables.net/buttons/2.2.3/js/buttons.print.min.js"></script>
        <script type="text/javascript" language="javascript" src="https://cdn.datatables.net/buttons/2.2.3/js/buttons.colVis.min.js"></script>

        <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.12.1/css/jquery.dataTables.min.css">
        <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/buttons/2.2.3/css/buttons.dataTables.min.css">
        <script type="text/javascript">        
        </script>
         <style>
            .row-margin{
                margin-bottom: 20px;
                margin-top: 30px;
            }
            .first-row{
                margin-top: 20px;
            }
            .heading-color{
                color: #337ab7;             
                margin-bottom: 30px;
            }
            .th-bg{
                background-color: oldlace;
            }
        </style>
       

<script type="text/javascript">
            /*$(document).ready(function() {
             $('#datatable').DataTable();
             });*/
            $(document).ready(function() {
                dataTable = $('#example').DataTable({
                    dom: 'Bfrtip',
                    fixedColumns: true, 
                    "pageLength": 50,
                    /*buttons: [
                        'copy', 'csv', 'excel', 'pdf', 'print', 'colvis'
                    ]*/
            buttons: [            
            {
                extend: 'excelHtml5',
                title: 'Universities',
                exportOptions: {
                    columns: ':visible',
                    
                }
            },
            {
                extend: 'pdfHtml5',
                title: 'Universities',
                exportOptions: {
                    columns: [ 0, 1]
                    //columns: [ 0, ':visible']
                }
            },
           
            
        ]
                });
               

            });
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
                                    <i class="fa fa-file"></i> Qualification List 
                                </li>                            
                            </ol>
                        </div>
                    </div>
                    
                    <div class="row">
                        <form:form action="QualificationList.htm" commandName="qualification" method="post">
                            
                        <div class="col-lg-12">
                            <h3 class="heading-color">Board List</h3>
                            <div class="table-responsive">
                                <table id="example" class="table table-bordered table-hover table-striped">
                                    <thead>
                                        <tr>
                                            <th class="th-bg">Sl No</th>                                            
                                            <th class="th-bg">Qualification Name</th>
                                            <th class="th-bg">Action</th>                                           
                                        </tr>
                                    </thead>
                                    <tbody>                                        
                                        <c:forEach items="${qualificationList}" var="qualificationlist" varStatus="count">
                                            <tr>
                                                <td>${count.index + 1}</td>                                               
                                                <td>${qualificationlist.qualification}</td>  
                                                <td><a href="editQualification.htm?qualificationserialNumber=${qualificationlist.qualification_sl_no}">Edit | </a>
                                                <a href="deleteQualification.htm?qualificationserialNumber=${qualificationlist.qualification_sl_no}" onclick="return confirm('Are you sure to Delete?')">Delete</a></td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>   
                            <div class="panel-footer" id="add-new">
                                <input type="submit" name="action" value="Add New Qualification" class="btn btn-info">
                                <%--<a href="getQualificationList.htm"><button type="button" class="btn btn-info">Add New Qualification</button>  </a>--%>
                            </div>
                        </div>
                    </div>
                   </form:form>
                </div>
            </div>
        </div>
    </body>
</html>
