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
            function getCadrewisePostList() {
                $('#postCode').empty();
                var url = 'getCadrewisePostList.htm?cadreId=' + $('#cadreCode').val();
                $('#postCode').append('<option value="">Select Post</option>');
                $.getJSON(url, function (data) {
                    $.each(data, function (i, obj) {
                        $('#postCode').append('<option value="' + obj.postcode + '">' + obj.post + '</option>');
                    });
                     $('#postCode')[0].value = $('#hpostid').val();
                });
            }
             function validate_textbox(val, id) {
                if (isNaN(val)) {
                    $("#" + id).val('');
                    return false;
                }
                
            }   
        </script>
    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/deptadminmenu.jsp"/>        
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
                                        <i class="fa fa-file"></i> New Block Level Extension Officer
                                    </li>
                                </ol>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-lg-12">
                                <h2>Block Level Extention Officer</h2>
                                <form:form role="form" action="updateBleo.htm"  method="post">
                                     <input type='hidden' name="recruitEoId" value="${bleoForm.recruitEoId}"/>
                                    <div class="form-group">
                                        <label for="exampleInputEmail1">Department Name</label>
                                        <input name="deptId" type="text" class="form-control" id="deptId" value="${LoginUserBean.loginname}"  readonly >
                                    </div>
                                    <div class="form-group">
                                        <label for="exampleInputPassword1">Group Name <span style='color:red; font-weight:bold;'>*</span></label>
                                        <Select name="groupName"  class="form-control" required>
                                            <option value="">Select Group</option>
                                           <option value="A"  <c:if test = "${not empty bleoForm.groupName && bleoForm.groupName=='A'}"> <c:out value='selected="selected"'/></c:if>>Group A </option>
                                                <option value="B"  <c:if test = "${not empty bleoForm.groupName && bleoForm.groupName=='B'}"> <c:out value='selected="selected"'/></c:if>>Group B</option>
                                                <option value="C" <c:if test = "${not empty bleoForm.groupName && bleoForm.groupName=='C'}"> <c:out value='selected="selected"'/></c:if>>Group C</option>
                                                <option value="D" <c:if test = "${not empty bleoForm.groupName && bleoForm.groupName=='D'}"> <c:out value='selected="selected"'/></c:if>>Group D</option>
                                        </select>

                                    </div>
                                       <div class="form-group">
                                            <label for="exampleInputPassword1">Cadre <span style='color:red; font-weight:bold;'>*</span></label>
                                            <Select name="cadreCode"  class="form-control" id="cadreCode" onchange="getCadrewisePostList()" required="1">
                                                <option value="">Select Cadre</option>
                                                <option value="0"  <c:if test = "${not empty bleoForm.cadreCode && bleoForm.cadreCode=='0'}"> <c:out value='selected="selected"'/></c:if>>All</option>
                                                <c:forEach items="${cadreList}" var="cadre">
                                                    <option value="${cadre.value}"  <c:if test = "${not empty bleoForm.cadreCode && bleoForm.cadreCode==cadre.value}"> <c:out value='selected="selected"'/></c:if>>${cadre.label}</option>
                                                </c:forEach>
                                            </select>                 
                                        </div>
                                        <div class="form-group">
                                            <label for="exampleInputPassword1">Post Name <span style='color:red; font-weight:bold;'>*</span></label>
                                             <input type='hidden' id='hpostid' value='${bleoForm.postName}' />
                                            <Select name="postName"  class="form-control" id="postCode" required="1" >
                                                <option value="">Select Post</option>						
                                            </select>                 
                                        </div>   
                                    

                                    <div class="form-group">
                                        <label for="exampleInputPassword1">No. of Sanctioned strength<span style='color:red; font-weight:bold;'>*</span></label>
                                        <input name="sanctionStrength" value="${bleoForm.sanctionStrength}" type="text" class="form-control" id="san_strength" required onkeyup="validate_textbox(this.value, this.id)" >
                                    </div>

                                    <div class="form-group">
                                        <label for="exampleInputPassword1">No. of present vacancy <span style='color:red; font-weight:bold;'>*</span></label>
                                        <input name="previousVacancy" value="${bleoForm.previousVacancy}" type="text" class="form-control" id="pre_vacancy"  required onkeyup="validate_textbox(this.value, this.id)">
                                    </div>
                                    <div class="form-group">
                                        <label for="exampleInputPassword1">Present Status on filling up the vacant posts<span style='color:red; font-weight:bold;'>*</span></label>
                                        <input name="previousStatus" value="${bleoForm.previousStatus}" type="text" class="form-control" id="pre_status">
                                    </div>
                                     <div class="form-group">
                                        <label for="exampleInputPassword1">No of Post Advertised<span style='color:red; font-weight:bold;'>*</span></label>
                                        <input name="postAdv" value="${bleoForm.postAdv}" type="text" class="form-control" id="post_adv">
                                    </div>
                                    <div class="form-group">
                                        <label for="exampleInputPassword1">No of Post filled up<span style='color:red; font-weight:bold;'>*</span></label>
                                        <input name="filledUp" value="${bleoForm.filledUp}" type="text" class="form-control" id="filled_up">
                                    </div>
                                     <div class="form-group">
                                        <label for="exampleInputPassword1">No of Balance<span style='color:red; font-weight:bold;'>*</span></label>
                                        <input name="noBal" value="${bleoForm.noBal}" type="text" class="form-control" id="no_bal">
                                    </div>
                                    <div class="form-group">
                                        <label for="exampleInputPassword1">Reason of Non Recruitment</label>
                                        <input name="reasonRec" value="${bleoForm.reasonRec}" type="text" class="form-control" id="reason_rec"   >
                                    </div>

                                    <div class="box-footer">
                                        <button type="submit" class="btn btn-default">Submit</button>
                                        <button type="reset" class="btn btn-default">Reset</button>
                                    </div>
                                </form:form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
             <script type='text/javascript'>
                if($('#cadreCode').val()!=''){
                    getCadrewisePostList();
                    //alert($('#hpostid').val());
                   
                }
                
                
            </script> 
    </body>
</html>
