<%-- 
    Document   : addNewChartofAccount
    Created on : 20 Nov, 2021, 11:54:21 AM
    Author     : Devi
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
        <script src="js/moment.js" type="text/javascript"></script>
        <link href="css/sb-admin.css" rel="stylesheet">
        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>       
        <script type="text/javascript">
            function validatechat(){
               if($('#demandNo').val() =="") 
               {
                   alert("please enter Demand Number") ;
                   $('#demandNo').focus();
                   return false;
               }
               if($('#majorHead').val() =="") 
               {
                   alert("please enter Major Head") ;
                   $('#majorHead').focus();
                   return false;
               }
               if($('#subMajorHead').val() =="") 
               {
                   alert("please enter Sub Major Head") ;
                   $('#subMajorHead').focus();
                   return false;
               }
               if($('#minorHead').val() =="") 
               {
                   alert("please enter Minor Head") ;
                   $('#minorHead').focus();
                   return false;
               }               
               if($('#subHead').val() =="") 
               {
                   alert("please enter Sub Head") ;
                   $('#subHead').focus();
                   return false;
               }
               if($('#DetailHead').val() =="") 
               {
                   alert("please enter Detail Head") ;
                   $('#DetailHead').focus();
                   return false;
               }
               if($('#ChargeVoted').val() =="") 
               {
                   alert("please enter Charge Voted") ;
                   $('#ChargeVoted').focus();
                   return false;
               }
                if($('#ObjectHead').val() =="") 
               {
                   alert("please enter Object Head") ;
                   $('#ObjectHead').focus();
                   return false;
               }       
                    return true;             
            }
      
        </script>
    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/hrmsadminmenu.jsp"/> 
            <div id="page-wrapper" style="padding: 30px">
                <div class="row" style="margin-top:40px">
                    <form:form action="addallNewChartofAccount.htm" commandName="chatOfAccount" method="post">                        
                        <div class="container-fluid">
                         <form:hidden id='hiddenddoCode'  path='hiddenddoCode'/>                    
                          <form:hidden id='chatId'  path='chatId'/>                   
                          <div class="row" style="margin-bottom:20px">
                              <label class="control-label col-sm-1 ">Department Name</label>
                              <div class="col-sm-4" style="font-weight: bold;">
                                  ${chatOfAccount.hiddeptName}                                    
                              </div>
                              <label class="control-label col-sm-1">Office Name</label>
                              <div class="col-sm-4" style="font-weight: bold;">
                                  ${chatOfAccount.hidddoName}       
                              </div> 
                          </div>
                            <div class="row">
                                <label class="control-label col-sm-1 ">Demand Number</label>
                                <div class="col-sm-4">
                                     <form:input cssClass="form-control" path="demandno" id="demandNo" maxlength="2"/>         
                                </div>
                                <label class="control-label col-sm-1">Major Head</label>
                                <div class="col-sm-4">                                  
                                    <form:input cssClass="form-control" path="majorHead" id="majorHead" maxlength="4"/>                                       
                                </div>
                            </div><br>
                            <div class="row">
                                <label class="control-label col-sm-1">Sub Major Head</label>
                                <div class="col-sm-4">
                                    <form:input cssClass="form-control" path="subMajorHead" id="subMajorHead" maxlength="2"/>
                                </div>
                                <label class="control-label col-sm-1">Minor Head</label>
                                <div class="col-sm-4">
                                    <form:input cssClass="form-control" path="minorHead"  id="minorHead" maxlength="3" />                                      
                                </div>
                            </div><br>
                            <div class="row">
                                <label class="control-label col-sm-1">Sub Head</label>
                                <div class="col-sm-4">
                                    <form:input cssClass="form-control" path="subHead" id="subHead" maxlength="4"/>                                       
                                </div>
                                <label class="control-label col-sm-1" >Detail Head</label>
                                <div class="col-sm-4" >
                                    <form:input cssClass="form-control" path="DetailHead" id="DetailHead" maxlength="5"/>                              
                                </div>
                            </div><br>
                            <div class="row">
                                <label class="control-label col-sm-1">Charge Voted</label>
                                <div class="col-sm-4">
                                    <form:input cssClass="form-control" path="ChargeVoted" id="ChargeVoted" maxlength="1"/>            
                                </div>
                            </div><br>
                            <div class="row">   
                                <div class="form-group">
                                    <label class="control-label col-sm-1">Object Head</label>
                                    <div class="col-sm-4">
                                        <form:input cssClass="form-control" path="ObjectHead" id="ObjectHead" maxlength="3"/>
                                    </div>
                                </div>
                            </div><br>
                            <div class="form-group col-sm-12" style="margin-top:20px">
                                <label class="control-label col-sm-1"></label>
                                <div class="text-center col-sm-12" >                                   
                                    <input type="submit" name="action" value="Save" onclick = "return validatechat();" style="width: 5%;" class="btn btn-primary"/>                                   
                                    <input type="submit" name="action" value="Cancel" class="btn btn-primary"/>
                                </div>
                            </div>
                        </div> 
                    </form:form> 
                </div>                  
            </div>
        </div>
    </body>
</html>

