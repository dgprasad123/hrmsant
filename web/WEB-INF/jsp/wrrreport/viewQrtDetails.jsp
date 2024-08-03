<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>        
        <link rel="stylesheet" type="text/css" href="css/jquery.datetimepicker.css"/>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>        
        <script language="javascript" src="js/jquery.datetimepicker.js" type="text/javascript"></script>
        <script type="text/javascript" src="js/jquery.colorbox-min.js"></script>

        <!-- Custom CSS -->
        <link href="css/sb-admin.css" rel="stylesheet">
 <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <!-- Bootstrap Core JavaScript -->
              
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        

        <script type="text/javascript">       // allotment                 
            $(document).ready(function () {
                $('#orderDate').datetimepicker({
                    format: 'd-M-Y',
                    timepicker:false,
                });
               

            });
             function saveCheck() {
                 /*var radioValue = $("input[name='vacateStatus']:checked").val();
                 if (radioValue != "Vacation" &&  radioValue != "Occupation") {
                     alert("Please select status");
                     return false;
                 }*/
                  if ($('#orderDate').val() == '') {
                    alert('Date should not be blank.');
                    $('#orderDate').focus();
                    return false;
                }
                 
                 
               //  return false;
             }
           
            
        </script>
    </head>
    <body>
         <jsp:include page="header_quarter.jsp">
                    <jsp:param name="menuHighlight" value="NDCAPPLICATIONS" />
                </jsp:include>    
        <div id="wrapper">
                  
            <div id="page-wrapper">
                <div class="container-fluid">
                    <div class="panel panel-default">
                        <form:form action="saveOccupationVacationQrtDetails.htm" method="POST"  commandName="empQuarterBean">
                            <div class="panel-header">Quarter Details</div>
                            <div class="panel-body">
                                <div class="form-group">
                                    <label class="control-label col-sm-2">Unit/Area</label>
                                    <div  class="col-2">${quarterBeandetail.qrtrunit}</div >
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">Quarter Type</label>
                                    <div  class="col-2">${quarterBeandetail.qrtrtype}</div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">Quarter No</label>
                                    <div  class="col-2">${quarterBeandetail.quarterNo}</div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">Employee Name</label>
                                    <div  class="col-2"><b>${quarterBeandetail.empName}</b>, ${quarterBeandetail.designation}</div>
                                </div>                                
                                <div class="form-group">
                                    <label class="control-label col-sm-2">Mobile No</label>
                                    <div  class="col-2">
                                         <input type="hidden" name="nocStatus" id="nocStatus" value="${nocStatus}"/>
                                        <input type="hidden" name="qaId" id="qaId" value="${quarterBeandetail.qaId}"/>
                                        <input type="hidden" name="empId" id="empId" value="${quarterBeandetail.empId}"/>
                                        <input type="hidden" name="mobileno" id="mobileno" value="${quarterBeandetail.mobileno}"/>
                                        ${quarterBeandetail.mobileno}
                                    </div>
                                </div>

                               <!-- <div class="form-group ">
                                    <label class="control-label col-sm-2">Status</label>
                                    <div  class="col-2">
                                        <c:if  test="${nocStatus eq 'Y'}">
                                             <input type="hidden" name="vacateStatus" value="Vacation"/>
                                           &nbsp;Vacation&nbsp; 
                                        </c:if>
                                         <c:if  test="${nocStatus eq 'N'}">
                                             <input type="hidden" name="vacateStatus" value="Occupation"/>
                                           &nbsp;Occupation&nbsp; 
                                        </c:if>   
                                      
                                        
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">Date</label>
                                    <div  class="col-2">
                                        <input type="text" class="form-control" id="orderDate" name="orderDate" style="width: 250px" readonly="1" >
                                    </div>
                                </div>      
                                
                               
                            </div>
                            <div class="panel-footer">
                              
                                <input type="submit" class="btn btn-primary" value="Save" onclick="return saveCheck()" />  
                            </div>-->
                        </form:form>
                    </div>
                    

                </div>
            </div>
        </div>
    </body>
</html>
