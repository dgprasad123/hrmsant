<%-- 
    Document   : ModuleList
    Created on : Nov 21, 2016, 6:08:30 PM
    Author     : Manas Jena
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
        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>

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
                                    <i class="fa fa-file"></i> Privilege List 
                                </li>                                
                            </ol>
                        </div>
                    </div>
                    <form role="form" action="savePostdataDetails.htm" commandName="substantivePostDetails"  method="post">
                        <input type="hidden" name='deptCode' value='${deptCode}' id='hiddenDeptcode'/>
                        <input type="hidden" name='offCode' value='${officeCode}' id='hiddenOfficcode'/>
                        <input type="hidden" name='postCode' value='${postCode}' id='hiddenpostcode'/>
                        <div class="panel panel-primary">
                            <div class="panel-heading">Add New Post</div>
                            <div class="row">
                                <label class="control-label col-sm-3">Office Name</label> 
                                <div class="col-sm-6" >
                                  ${postDetails.officeName}
                                </div>
                            </div> 
                            <div class="row">
                                <label class="control-label col-sm-3">Office Code</label> 
                                <div class="col-sm-6" >
                                    ${officeCode}
                                </div>
                            </div>    

                            <div class="row">
                                <label class="control-label col-sm-3">Post Name</label> 
                                <div class="col-sm-6" >
                                     ${postDetails.postname}

                                </div>
                            </div>      
                            <div class="row">
                                <label class="control-label col-sm-3">Post Code</label> 
                                <div class="col-sm-6" >
                                    ${postCode}

                                </div>
                            </div>

                            <div class="row">
                                <label class="control-label col-sm-3">No Of Post</label> 
                                <div class="col-sm-6" >
                                    <input id="noofPost" name="txtNoOfPost" type="number" value="" maxlength="2" min="1" max="100" class="form-control" style='margin-bottom:5px' required/>

                                </div>
                            </div> 
                            <div class="row">
                                <label class="control-label col-sm-3">Order No</label> 
                                <div class="col-sm-6" >
                                    <input id="orderNo" name="orderNo" type="text" value="" class="form-control" style='margin-bottom:5px' required/>

                                </div>
                            </div> 
                            <div class="row">
                                <label class="control-label col-sm-3">Order Date</label> 
                                <div class="col-sm-6" >
                                    <input id="orderDate" name="orderDate" type="text" value="" class="form-control" required/>

                                </div>
                            </div>
                              <div>   &nbsp;</div>         
                            <div class="row">  
                                 <label class="control-label col-sm-3">&nbsp;</label> 
                                <div class="col-sm-6" >
                                   <input type='submit' name='Submit' value='Submit' class="btn btn-primary" />
                                   
                                </div>   
                            </div> 
                            <div>   &nbsp;</div>    
                        </div>


                    </form>


                </div>
            </div>
        </div>



        <!-- Modal -->
        <div id="myModal" class="modal fade" role="dialog">
            <div class="modal-dialog">



            </div>
        </div>
        <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
        <link rel="stylesheet" href="/resources/demos/style.css">
        <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
        <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
        <script>
            $(function() {
                //$('#dob').datepicker({dateFormat: 'yy-mm-dd'});
                //$('#doj').datepicker({dateFormat: 'yy-mm-dd'});
                ;
                $("#orderDate").datepicker({
                    changeMonth: true,
                    changeYear: true,
                    dateFormat: 'dd-mm-yy',
                    yearRange: '1955:2025',
                });
            });
          
       </script>     
    </body>
</html>
