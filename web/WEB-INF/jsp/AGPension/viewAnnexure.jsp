<%-- 
    Document   : viewAnnexure
    Created on : 11 Oct, 2022, 11:15:33 AM
    Author     : Devikrushna
--%>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>

        <link rel="stylesheet" href="css/bootstrap.min.css">
        <link rel="stylesheet" type="text/css" href="font-awesome/css/font-awesome.min.css">        
        <link rel="stylesheet" href="css/sb-admin.css">

        <script type="text/javascript" src="js/jquery.min.js"></script>
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript">
           
        </script>
        <style>
            
            .nav-tabs > li.active > a, .nav-tabs > li.active > a:focus, .nav-tabs > li.active > a:hover {
                background-color: #e0eed6;
            }
        </style>
    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/agOdishaMenu.jsp"/>
            <div id="page-wrapper">
                <form:form action="viewAnnexure.htm" commandName="agPensionForm">
                    <div class="container-fluid" style="margin-bottom:40px;">                                           
                            <h3>View Annexure List</h3>                      
                        <div class="panel-body row">                                        
                                    <ul class="nav nav-tabs">
                                        <li class="active"><a data-toggle="tab" href="#basicprofile">Basic Profile</a></li>
                                        <li><a data-toggle="tab" href="#annexureA">Annexure-A </a></li>
                                        <li><a data-toggle="tab" href="#annexureB">Annexure-B </a></li>
                                        <li><a data-toggle="tab" href="#annexureC" >Annexure-C </a></li>
                                        <li><a data-toggle="tab" href="#annexureD">Annexure-D </a></li>                                
                                        <li><a data-toggle="tab" href="#annexureE">Annexure-E </a></li>                                   
                                    </ul>
                                    
                        <div class="tab-content">
                            
                         <div id="basicprofile" class="tab-pane fade in active">                       
                         <form:form action="basicprofile.htm" commandName="agPensionForm">
                         <div class="container-fluid" style="margin-bottom:40px;">             
                            <h3>View Basic Profile</h3>                          
                            <div class="panel-body row">      
                                <table class="table table-bordered table-striped">
                                <thead>
                                    <tr style="background-color: aliceblue;">
                                       <th>Sl No</th>
                                        <th>Description</th>
                                        <th>Details</th>      
                                    </tr>
                                </thead>
                                <tbody>                           
                                    <tr>      
                                        <td>1</td>
                                        <td>HRMS ID</td>
                                        <td>${employeeProfile.getEmpid()}</td>                                            
                                    </tr>                                   
                                    <tr>      
                                        <td>2</td>
                                        <td>Name</td>
                                        <td>${employeeProfile.getEmpName()}</td>                                            
                                    </tr>
                                     <tr>      
                                        <td>3</td>
                                        <td>Father's Name</td>
                                        <td>${fatherDetails}</td>                                            
                                    </tr>
                                     <tr>      
                                        <td>4</td>
                                        <td>Date of Birth</td>
                                        <td>${employeeProfile.getDob()}</td>                                            
                                    </tr>
                                    <tr>      
                                        <td>5</td>
                                        <td>Educational Qualification</td>
                                        <td></td>                                            
                                    </tr>
                                    <tr>      
                                        <td>6</td>
                                        <td>Other Qualification</td>
                                        <td></td>                                            
                                    </tr>
                                    <tr>      
                                        <td>7</td>
                                        <td>Date of Joining in the Service</td>
                                        <td>${employeeProfile.getDoeGov()}</td>                                            
                                    </tr>
                                    <tr>      
                                        <td>8</td>
                                        <td>Office in which employed </td>
                                        <td>${employeeProfile.getOffice()}</td>                                            
                                    </tr>
                                    <tr>      
                                        <td>9</td>
                                        <td>Post Held</td>
                                        <td>${employeeProfile.getSpn()}</td>                                            
                                    </tr>
                                    <tr>      
                                        <td>10</td>
                                        <td>Scale of Pay</td>
                                        <td>${employeeProfile.getPayScale()}</td>                                            
                                    </tr>
                                    <tr>      
                                        <td>11</td>
                                        <td>GPF A/c No.</td>
                                        <td>${employeeProfile.getGpfno()}</td>                                            
                                    </tr>
                                    <tr>      
                                        <td>12</td>
                                        <td>Mobile No.</td>
                                        <td>${employeeProfile.getMobile()}</td>                                            
                                    </tr>
                                    <tr>      
                                        <td>13</td>
                                        <td>Email Id</td>
                                        <td>${employeeProfile.getEmail()}</td>                                            
                                    </tr>
                                    </tbody>
                                </table>
                              </div>
                            </div>
                        </form:form>
                     </div>
                            
                        <div id="annexureA" class="tab-pane fade">                       
                         <form:form action="annexureA.htm" commandName="agPensionForm">
                         <div class="container-fluid" style="margin-bottom:40px;">             
                            <h3>View Annexure-A Statement of service verification   </h3>                          
                            <div class="panel-body row">      
                                <table class="table table-bordered table-striped">
                            <thead>
                                <tr style="background-color: aliceblue;">
                                    <th>Sl No</th>
                                    <th>Chronology of posting(From Date)</th>
                                    <th>Chronology of posting (To Date)</th>
                                    <th>Name of the post</th>                                  
                                    <th>Office where posted </th>        
                                    <th>Whether service verified or not</th>        
                                    <th>Remarks/Reasons for non-verification of service</th>                                         
                                </tr>
                            </thead>
                            <tbody>
                              <c:forEach var="item" items="${annexureAList}" varStatus="count"> 
                                    <tr>      
                                        <td> ${count.index + 1}</td>
                                        <td>${item.frmDate} </td>
                                        <td>${item.toDate} </td>
                                        <td>${item.spn} </td>
                                        <td>${item.offen} </td>
                                        <td>                                          
                                            <c:if test="${empty item.ifSV }">
                                                <span>Not verified</span>
                                            </c:if>
                                            <c:if test="${not empty item.ifSV }">
                                                <span> Verified</span>
                                            </c:if>    
                                         </td>
                                        <td>${item.remarksForNoSV} </td>                                                                          
                                    </tr>
                              </c:forEach>
                            </tbody>
                        </table>                                               
                      </div>
                    </div>
                </form:form>
             </div>
                            
             <div id="annexureB" class="tab-pane fade">            
                  <form:form action="annexureB.htm" commandName="agPensionForm">
                    <div class="container-fluid" style="margin-bottom:40px;">
                         <h3>View Annexure-B Statement of Pay Fixation   </h3>
                            <div class="panel-body row">      
                                <table class="table table-bordered table-striped">
                            <thead>
                                <tr style="background-color: aliceblue;">
                                    <th>Sl No</th>
                                    <th>Events</th>
                                    <th>Date</th>
                                    <th>Office</th>                                  
                                    <th>Post Held  </th>        
                                    <th>Scale of Pay/Pay Band/Pay Level and Cell No.</th>        
                                    <th>Pay fixed(Pay+GP)</th>  
                                    <th>Date of Next Increment (DNI)</th>        
                                    <th>Remarks</th>                                    
                                </tr>
                            </thead>
                            <tbody>
                              <c:forEach var="item" items="${annexureBList}" varStatus="count"> 
                                    <tr>
                                        <td> ${count.index + 1}</td>
                                        <td>${item.tabname} </td>
                                        <td>${item.wefChange} </td>
                                        <td>${item.offen} </td>
                                        <td>${item.spn} </td>                                 
                                        <td>${item.payscale}  </td>                                                                                                                 
                                        <td>${item.pay}</td>
                                        <td>${item.nextwefChange}</td>                                         
                                        <td>${item.remarksForNoSV} </td>                                                                              
                                    </tr>
                              </c:forEach>
                            </tbody>
                        </table>                                               
                      </div>
                    </div>
                </form:form>
             </div> 
                     
             <div id="annexureC" class="tab-pane fade">
                 <div class="container-fluid" style="margin-bottom:40px;">
                   <h3>View Annexure-C Statement of Non-qualifying service</h3>   
                     <h4 style="color: #071c59ab;font-size: 20px;">Statement of Non-qualifying service not Available </h4>
                   </div>
              </div>              
                            
               <div id="annexureD" class="tab-pane fade">            
                  <form:form action="annexureD.htm" commandName="agPensionForm">
                    <div class="container-fluid" style="margin-bottom:40px;">
                         <h3>View Annexure-D Statement of Long-Term Advance   </h3>
                            <div class="panel-body row">      
                                <table class="table table-bordered table-striped">
                            <thead>
                                <tr style="background-color: aliceblue;">
                                    <th>Sl No</th>
                                    <th>Type of Advance </th>
                                    <th>Date of Sanction</th>
                                    <th>Amount of Advance</th>                                  
                                    <th>Amount re-paid</th>        
                                    <th>Amount outstanding</th>        
                                    <th>Remarks</th>                                    
                                </tr>
                            </thead>
                            <tbody>
                              <c:forEach var="item" items="${annexureDList}" varStatus="count"> 
                                    <tr>
                                        <td> ${count.index + 1}</td>
                                        <td>${item.typeOfAdvance} </td>
                                        <td>${item.dateSanction} </td>
                                        <td>${item.advanceAmt} </td>
                                        <td>${item.amtRepaid} </td>                                 
                                        <td>${item.amtOutstanding}  </td>                                                                                                                 
                                        <td>${item.remarksForNoSV}</td>                                                                                                               
                                    </tr>
                              </c:forEach>
                            </tbody>
                        </table>                                               
                      </div>
                    </div>
                </form:form>
             </div>  
                            
               <div id="annexureE" class="tab-pane fade">            
                  <form:form action="annexureE.htm" commandName="agPensionForm">
                    <div class="container-fluid" style="margin-bottom:40px;">
                         <h3>View Annexure-E Details of Disciplinary Proceedings</h3>
                            <div class="panel-body row">      
                                <table class="table table-bordered table-striped">
                            <thead>
                                <tr style="background-color: aliceblue;">
                                    <th>Sl No</th>
                                    <th>Details</th>
                                    <th>Date</th>
                                    <th>Office</th>                                  
                                    <th>Date of finalization of Disciplinary Proceedings</th>        
                                    <th>Remarks/Result of Disciplinary Proceedings</th>                
                                </tr>
                            </thead>
                            <tbody>
                                <c:if test="${empty annexureEList}">
                                    <div class="text-center" style="margin: 30px;">
                                        <h4 style="color: #071c59ab;font-size: 20px;">Disciplinary Proceeding not Available </h4>
                                    </div>
                                </c:if>                                
                                    <c:forEach var="item" items="${annexureEList}" varStatus="count"> 
                                        <tr>
                                            <td> ${count.index + 1}</td>
                                            <td>${item.detailsDisciplinaryProceeding} </td>
                                            <td>${item.dateOfInitiation} </td>
                                            <td>${item.officeDisciplinaryProceeding} </td>
                                            <td>${item.dateOfFinalization} </td>                                 
                                            <td>${item.remarksDisciplinaryProceeding}  </td>                                                                                                                                                                                                  
                                            </tr>
                                    </c:forEach>
                                        </tbody>
                                    </table>                                               
                                  </div>
                                </div>
                            </form:form>
                         </div>                                                                                               
                       </div>                          
                      </div>                       
                    </div>
                </form:form>
            </div>
        </div>
    </body>
</html>

