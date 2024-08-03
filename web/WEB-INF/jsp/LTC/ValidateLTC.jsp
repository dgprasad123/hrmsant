<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<% int i = 1;
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Validate LTC</title>
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">   
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <script type="text/javascript" src="js/jquery.min.js"></script>
        <script type="text/javascript" src="js/bootstrap.min.js"></script>   
        <script type="text/javascript">
            function validateForm(sec1, sec2, sec3, sec4, sec5)
            {
                if(sec1 == 'N' || sec2 == 'N' || sec3 == 'N' || sec4 == 'N' || sec5 == 'N')
                {
                    alert("Please fix all the validation errors, then resubmit.");
                    return false;
                }
            }
            </script>
    </head>
    <body style="padding-top: 10px;">
        <jsp:include page="ProfileTabs.jsp">
            <jsp:param name="menuHighlight" value="VALIDATE" />
            <jsp:param name="ltcID" value="${ltcId}" />
        </jsp:include>
        <div style="width:95%;margin:0px auto;border-top:0px;">
            <div class="row" style="border: 1px solid #ddd;padding: 5px;">
                <form:form action="SubmitLTC.htm" method="post" commandName="LTCBean" id="frmLTC">
                    <input type="hidden" name="ltcId" value="${ltcId}" />
                    <div class="container-fluid">
                        <div class="panel panel-default">
                            <div class="panel-heading" style="margin-bottom:10px;padding-bottom:5px;color:#0052A7">
                                
                                <strong style="font-size:14pt;">Validate & Submit LTC Application</strong>
                            </div>
                            <div>


                                <div class="row" style="margin-bottom: 7px;">
                                    <div class="col-lg-10" style="font-size:12pt;">
                                        <ul style="line-height:30px;">
                                            <c:if  test = "${ltcvBean.isSection1=='N'}"><li style="color:#FF0000;"><img src="images/Delete-icon.png" /><strong>Step1: </strong>Filling out Basic Info <a href="BasicInfo.htm?id=${ltcId}" style="text-decoration:underline">Click to Fix</a></li></c:if>
                                            <c:if  test = "${ltcvBean.isSection1=='Y'}"><li style="color:#008900;"><img src="images/pass.png" /><strong>Step1: </strong>Filling out Basic Info</li></c:if>
                                            
                                            <c:if  test = "${ltcvBean.isSection2=='N'}"><li style="color:#FF0000"><img src="images/Delete-icon.png" /><strong>Step2: </strong>Adding at least one Family Member <a href="FamilyMembers.htm?id=${ltcId}" style="text-decoration:underline">Click to Fix</a></li></c:if>
                                            <c:if  test = "${ltcvBean.isSection2=='Y'}"><li style="color:#008900;"><img src="images/pass.png" /><strong>Step2: </strong>Adding at least one Family Member</li></c:if>
                                            
                                            <c:if  test = "${ltcvBean.isSection3=='N'}"><li style="color:#FF0000"><img src="images/Delete-icon.png" /><strong>Step3: </strong>Filling out Reimbursement Details <a href="ReimbursementDetails.htm?id=${ltcId}" style="text-decoration:underline">Click to Fix</a></li></c:if>
                                            <c:if  test = "${ltcvBean.isSection3=='Y'}"><li style="color:#008900;"><img src="images/pass.png" /><strong>Step3: </strong>Filling out Reimbursement Details</li></c:if>
                                            
                                            <c:if  test = "${ltcvBean.isSection4=='N'}"><li style="color:#FF0000"><img src="images/Delete-icon.png" /><strong>Step4: </strong>Selecting Reporting Authority <a href="LTCReportingAuthority.htm?id=${ltcId}" style="text-decoration:underline">Click to Fix</a></li></c:if>
                                            <c:if  test = "${ltcvBean.isSection4=='Y'}"><li style="color:#008900;"><img src="images/pass.png" /><strong>Step4: </strong>Selecting Reporting Authority</li></c:if>
                                            
                                            <c:if  test = "${ltcvBean.isSection5=='N'}"><li style="color:#FF0000"><img src="images/Delete-icon.png" /><strong>Step5: </strong>Selecting Issuing Authority <a href="LTCIssuingAuthority.htm?id=${ltcId}" style="text-decoration:underline">Click to Fix</a></li></c:if>
                                            <c:if  test = "${ltcvBean.isSection5=='Y'}"><li style="color:#008900;"><img src="images/pass.png" /><strong>Step5: </strong>Selecting Issuing Authority</li></c:if>
                                        </ul>
                                    </div>
                                </div> 
                               
                            </div>





                        </div>

                        <div >
                            <button type="submit" name="submit" value="Save" class="btn btn-default btn-xlg" style="background:#008900;color:#FFF;" onclick="return validateForm('${ltcvBean.isSection1}', '${ltcvBean.isSection2}', '${ltcvBean.isSection3}', '${ltcvBean.isSection4}', '${ltcvBean.isSection5}');">Submit Application</button>
                            <a class="btn btn-default btn-xlg" style="background:#008900;color:#FFF;text-align:center;text-decoration:none;" href="EmpLTCList.htm">Cancel</a>


                        </div>
                    </div>
                </div>




            </form:form>
        </div>
    </div>
</body>
</html>
