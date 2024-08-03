<%-- 
    Document   : SectionDefination
    Created on : Nov 21, 2016, 3:12:08 PM
    Author     : Manas Jena
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>      
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">
        <!-- LAYOUT v 1.3.0 -->
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>        

        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>

        <script type="text/javascript">
            function display_group_priv(vals){
                window.location="empWiseGroupPrivilege.htm?spc="+vals;
            }
     

        </script>

    </head>
    <body>
        <div class="container-fluid">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <div class="row">
                        <div class="col-lg-12">

                        </div>
                    </div>
                </div>
                <div class="panel-body">
                    <form:form class="form-inline" action="#" method="POST" >
                        <table class="table">
                            <tr style="font-weight:bold;background:#EAEAEA">
                                <td colspan="4">Bill Group Privilege

                            </tr>
                            <tr>
                                 <td >Bill Controller :<strong style='color:red'>*</strong></td>
                                <td>
                                    <select name="empspc" id="empspc"  size="1" class="form-control"  style="width:80%;" onchange="display_group_priv(this.value)" >
                                        <option value="">-Select-</option>
                                        <c:forEach items="${empList}" var="emp" >
                                            <option value="${emp.value}" <c:if test = "${not empty spc && spc==emp.value}"> <c:out value='selected="selected"'/></c:if>>${emp.label}</option>
                                        </c:forEach> 
                                    </select>
                                </td>
                                <td>&nbsp;</td>
                            </tr>
                             
                        </table>
                        
                    </form:form>
                    <div  class="alert alert-success" ><h5 style='success'>Bill Group Privilege Details</h5></div>
                    <table class="table table-bordered">
                        
                        <thead>
                            
                            <tr>
                                <th  width="10%">SL NO</th>
                                <th  width="20%">Bill Group ID</th>
                                <th  width="15%">Description</th>                               
                                <th  width="15%">Status</th>
                            </tr>
                              <c:forEach items="${empWisePrivilege}" var="group" varStatus="cnt">
                                <tr>
                                    <td>${cnt.index+1}</td>
                                    <td>${group.billgroupid}</td>
                                    <td>${group.billgroupdesc}</td>
                                    <td>
                                      <c:if test = "${not empty group.empCode }"> 
                                          <div class="alert alert-danger"> <strong><a href="assignBillPrivilege.htm?spc=${spc}&billId=${group.billgroupid}&type=0">Revoke</a></strong></div>
                                      </c:if>
                                       <c:if test = "${empty group.empCode }"> 
                                           <div class="alert alert-success"> <strong><a href="assignBillPrivilege.htm?spc=${spc}&billId=${group.billgroupid}&type=1">Assign</a></strong></div>
                                      </c:if>    
                                        
                                        
                                    </td>
                                    


                                </tr>
                            </c:forEach>
                        </thead>
                    </table>   
                </div>

            </div>
        </div>
    </body>
</html>
