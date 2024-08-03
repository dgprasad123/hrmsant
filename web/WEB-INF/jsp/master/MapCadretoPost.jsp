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
            function mapCadre(cadreCode, postCode, mapAction)
            {
                $('#loader_'+cadreCode).css('display', 'block');
                $.ajax({
                  url: 'MapCadreToPostTable.htm',
                  data: 'cadrecode='+cadreCode+'&postcode='+postCode+'&mapAction='+mapAction,
                  type: 'get',
                  success: function(retVal) {
                      if(mapAction == 'unmap')
                      {
                          $('#link_blk_'+cadreCode).html('<a href="javascript:void(0)" onclick="javascript: mapCadre(\''+cadreCode+'\', \''+postCode+'\', \'map\')">Map Cadre</a>');
                      }
                      if(mapAction == 'map')
                      {
                          $('#link_blk_'+cadreCode).html('<a href="javascript:void(0)" style="color:#FF0000;font-weight:bold;" onclick="javascript: mapCadre(\''+cadreCode+'\', \''+postCode+'\', \'unmap\')">Unmap Cadre</a>');
                      }
                      $('#loader_'+cadreCode).css('display', 'none');
                      //alert(retVal);
                       //$('#participant_list').html(retVal); 
                       //$('#loader').css('display', 'none');
                       //self.location = 'mapCadretoPost.htm?postcode='+postCode+'&deptcode='+$('#deptcode').val();
                  }
                }); 
            }
            function filterCadre()
            {
                self.location = 'mapCadretoPost.htm?postCode='+$('#postcode').val()+'&deptCode='+$('#deptcode').val();
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
                        <form:form action="mapCadretoPost.htm" id="frmPost" commandName="post">
                        <div class="col-lg-2">Department Name</div>
                        <div class="col-lg-8">
                            <form:hidden path="postcode" id="postcode" />
                            <form:select path="deptcode" id="deptcode" class="form-control">
                                <form:option value="">Select</form:option>
                                <form:options items="${departmentList}" itemValue="deptCode" itemLabel="deptName"/>                                
                            </form:select>
                        </div>
                        <div class="col-lg-2"><input type="submit" class="form-control" action="Search" value="Search"/> </div>
                        </form:form>
                    </div>
                    <div class="row">
                        <div class="col-lg-12">
                            <input type="button" value="&laquo; Back to Post" class="btn btn-success btn-md" style="font-weight:bold;background:#00629B" onclick="javascript: self.location='postList.htm'" />
                            <h2 style="font-size:15pt;">Map Post <span style="color:#008900;">[${postDetail.post}]</span> to Cadre</h2>
                            <div class="table-responsive" id="cadre_list">
                                <table class="table table-bordered table-hover table-striped">
                                    <thead>
                                        <tr>
                                            <th>Sl No</th>
                                            <th>Cadre Code</th>
                                            <th>Cadre Name</th>
                                            <th style="text-align:center;" width="150">Map Cadre</th>
                                        </tr>
                                    </thead>
                                    <tbody>                                        
                                        <c:forEach items="${cadreList}" var="cadre" varStatus="count">
                                            <tr>
                                                <td>${count.index + 1}</td>
                                                <td>${cadre.postcode}</td>
                                                <td>${cadre.post}</td>
                                                <td align="center"><div id="link_blk_${cadre.postcode}"><c:if test="${cadre.ifael == '0'}"><a href="javascript:void(0)" onclick="javascript: mapCadre('${cadre.postcode}', '${post.postcode}', 'map')">Map Cadre</a></c:if>
                                                        <c:if test="${cadre.ifael == '1'}"><a href="javascript:void(0)" style="color:#FF0000;font-weight:bold;" onclick="javascript: mapCadre('${cadre.postcode}', '${post.postcode}', 'unmap')">Unmap Cadre</a></c:if></div>
                                                        <span id="loader_${cadre.postcode}" style="display:none;color:#777777;font-style:italic;font-size:8pt;"><img src="images/ajax-loader_1.gif" /> Please wait...</span></td>
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


