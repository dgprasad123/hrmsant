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
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <script  type="text/javascript">
            function deleteCandidate(candidateId, categoryId)
            {
                if (confirm("Are you sure you want to delete the Candidate?"))
                    self.location = 'deleteCandidate.htm?candidateId=' + candidateId + '&categoryId=' + categoryId;
            }

        </script>
        <style type="text/css">
            table.form-table td{padding:5px;}
        </style>
    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/commissionmenu.jsp"/>               
            <!-- Site wrapper -->
            <div class="wrapper">            
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
                                        <i class="fa fa-file"></i> <a href="selectedCandidatesCategoryList.htm">Selected Candidates Categories List</a>
                                    </li>                                    
                                    <li class="active">
                                        <i class="fa fa-file"></i> Add Selected Candidate List
                                    </li>
                                </ol>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-lg-12">
                                <h2 style="margin-top:0px;">Add Selected Candidate List For the Following Post</h2>
                                <table width="100%" cellspacing="1" cellpadding="4" border="0" bgcolor="#EAEAEA" class="form-table" style="border:1px solid #CCCCCC;background:#EAEAEA;margin-bottom:10px;" >
                                    <tr>
                                        <td align="right">Department:</td>
                                        <td><strong>${cCategoryDetail.departmentId}</strong></td>
                                        <td align="right">Group Name:</td>
                                        <td><strong>${cCategoryDetail.groupName}</strong></td>
                                    </tr>
                                    <tr>
                                        <td align="right">Cadre:</td>
                                        <td><strong>${cCategoryDetail.cadreId}</strong></td>
                                        <td align="right">Post:</td>
                                        <td><strong>${cCategoryDetail.postId}</strong></td>
                                    </tr> 
                                    <tr>
                                        <td align="right">Order Number:</td>
                                        <td><strong>${cCategoryDetail.orderNumber}</strong></td>
                                        <td align="right">Order Date:</td>
                                        <td><strong>${cCategoryDetail.orderDate}</strong></td>
                                    </tr>                                         
                                    <tr>
                                        <td align="right">No. of Candidates:</td>
                                        <td colspan="3"><strong>${cCategoryDetail.noOfSelectedCanddiates}</strong></td>

                                    </tr>                                         
                                </table>
                                <form:form role="form" action="saveSelectedCandidates.htm" commandName="selectedCandidates"  method="post">
                                    <input type="hidden" name="categoryId" id="categoryId" value="${categoryId}" />
                                    <table width="100%" cellspacing="1" cellpadding="4" border="0" bgcolor="#EAEAEA" class="form-table" style="border:1px solid #CCCCCC;" >
                                        <tr>
                                            <td align="right">Candidate Name:</td>
                                            <td><input type="text" name="candidateName" value="" required class="form-control" required/></td>
                                            <td align="right">Roll Number:</td>
                                            <td><input type="text" name="rollNumber" value="" required class="form-control" required/></td>                                            
                                        </tr> 
                                        <tr>
                                            <td align="right">Gender:</td>
                                            <td><select name="gender" size="1" class="form-control" required>
                                                    <option value="">-Select-</option>
                                                    <option value="Male">Male</option>
                                                    <option value="Female">Female</option>
                                                </select></td>
                                            <td align="right">Category:</td>
                                            <td>
                                                <select name="category" size="1" class="form-control" required>
                                                    <option value="">-Select-</option>
                                                    <option value="UN RESERVED">UN RESERVED</option>
                                                    <option value="SEBC">SEBC</option>
                                                    <option value="SC">SC</option>
                                                    <option value="ST">ST</option>
                                                    <option value="VH">VH</option>
                                                    <option value="NH">NH</option>
                                                    <option value="OH">OH</option>
                                                    <option value="EX-SERVICEMEN">EX-SERVICEMEN</option>
                                                    <option value="SP">SP</option>
                                                </select>
                                            </td>
                                        </tr>                                         
                                        <tr>
                                            <td colspan="4" align="center"><input type="submit" class="btn btn-primary" value="Submit"  /></td>
                                        </tr>
                                    </table>
                                    <!-- /.box-body -->


                                </form:form>
                                <table id="account" class="table table-bordered table-striped" >
                                    <thead>
                                        <tr style='background-color:black;color:white;font-size:14px'>

                                            <th>Candidate Name</th>
                                            <th>Roll Number</th>
                                            <th>Gender</th>
                                            <th>Category</th>
                                            <th>Delete</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${selectedCandidates}" var="cCategory">
                                            <tr scope="row">
                                                <td>${cCategory.candidateName}</td>                                                
                                                <td>${cCategory.rollNumber}</td>                                                
                                                <td>${cCategory.gender}</td>                                                
                                                <td>${cCategory.category}</td>                                                
                                                <td><a href="javascript:void(0)" onclick="javascript: deleteCandidate(${cCategory.candidateId},${cCategory.categoryId})"><img src="images/delete_icon.png" alt="Saction Order" width="20px" title="Delete Candidate" /></a></td>
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
