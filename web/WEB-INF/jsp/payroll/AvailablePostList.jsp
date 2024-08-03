<%-- 
    Document   : AvailablePostList
    Created on : 10 Feb, 2021, 12:45:14 PM
    Author     : Surendra
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>      
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <!-- LAYOUT v 1.3.0 -->
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>        
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script src="js/moment.js" type="text/javascript"></script>        

        <script type="text/javascript">
            $(document).on("click", ".open-submitFormModal", function () {
                var chkspcid = $(this).data('id');
                $("#sectionlistModal").find(".modal-body").load("move2sectionModalFromavailableList.htm?spcs="+chkspcid);
                $("#sectionlistModal").modal("show");
            });
            
            function map2section() {
                var allowPostMap = "";
                if ($("input:radio[name='sectionMapId']").is(":checked")) {
                    var dataString = 'spc=' + $("#unmapspcid").val() + '&sectionId=' + $("input[type=radio][name=sectionMapId]:checked").val();
            

                    $.ajax({
                        type: "POST",
                        url: 'move2sectionFromPostList.htm',
                        data: dataString,
                        async: false,
                        cache: false,
                        dataType: 'json',
                        success: function (html) {
                            allowPostMap = html.status;
                            if (allowPostMap == "N") {
                                alert("Account Type Mismatch");
                            } else if (allowPostMap == "Y") {
                                var hidsltId = $("#unmapspcid").val();
                                $("#" + hidsltId).remove();
                                $("#sectionlistModal").modal("hide");
                                $('input[name=chkAssaign]').attr('checked', false);
                                len = 0;
                                $('input[type=text]').each(function () {
                                    len++;
                                    $(this).val(len);
                                });
                            }
                        }
                    });
                } else {
                    alert('Please select Section.');
                }
            }
        </script>
    </head>
    <body>
        <div class="container-fluid">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <div class="row">
                        <div class="col-lg-12">
                            <input type="hidden" name="offCode" id="offCode" value="${selectedOffice}"/>
                        </div>
                    </div>
                </div>
                <div class="panel-body">
                    
                        <div> <b>  Available Post </b> </div>
                        <div id="availemp" style="border:1px solid #5095CE;height:450px;overflow:auto;text-align:left;padding:10px;">
                            <c:forEach items="${postlist}" var="availableEmp" varStatus="listCount">
                                <div style="padding:5px;" id="${availableEmp.value}">
                                     ${availableEmp.label}
                                    <input type="hidden" name="temp" id="temp${listCount.index}" value=""/>
                                    <a href="javascript:void(0);" style="color:#890000;font-weight:bold;" data-remote="false" data-toggle="modal" data-target="#sectionlistModal" data-id="${availableEmp.value}" class="open-submitFormModal">
                                        <button type="button" class="btn btn-default">Move to Section</button>
                                    </a>
                                </div>
                            </c:forEach>
                        </div>
                  
                    
                    
                </div>

                
                <div class="panel-footer">  
                    <a href="GPCWiseSPCList.htm"><button type="button" class="btn btn-primary">Add More Post</button></a>
                </div>
            </div>
        </div>
        <div id="sectionlistModal" class="modal fade" role="dialog">
            <div class="modal-dialog" style="width:1000px;">
                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Section List</h4>
                    </div>
                    <div class="modal-body">

                    </div>
                    <div class="modal-footer">                       
                        <input type="button" value="Map2Section" onclick="map2section()" class="btn btn-default"/> <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
