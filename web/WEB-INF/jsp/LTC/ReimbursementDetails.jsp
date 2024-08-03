<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<% int i = 1;
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Apply LTC</title>
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">   
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <script type="text/javascript" src="js/jquery.min.js"></script>
        <script type="text/javascript" src="js/bootstrap.min.js"></script>   
        <script type="text/javascript">
            function validateForm()
            {
                
                if ($('#costByTrain').val() == '')
                {
                    alert("Please enter Cost by Train.");
                    $('#costByTrain')[0].focus();
                    return false;
                }
                if (isNaN($('#costByTrain').val()))
                {
                    alert("Please enter only numeric value.");
                    $('#costByTrain')[0].focus();
                    return false;
                }
                if ($('#costByRoad').val() == '')
                {
                    alert("Please enter Cost by Road.");
                    $('#costByRoad')[0].focus();
                    return false;
                }
                if (isNaN($('#costByRoad').val()))
                {
                    alert("Please enter only numeric value.");
                    $('#costByRoad')[0].focus();
                    return false;
                }
                if ($('#costByOther').val() == '')
                {
                    alert("Please enter Cost by Other.");
                    $('#costByOther')[0].focus();
                    return false;
                }
                if (isNaN($('#costByOther').val()))
                {
                    alert("Please enter only numeric value.");
                    $('#costByOther')[0].focus();
                    return false;
                }
                if ($('#advanceAmount').val() == '')
                {
                    alert("Please enter Advance Amount.");
                    $('#advanceAmount')[0].focus();
                    return false;
                }
                if (isNaN($('#advanceAmount').val()))
                {
                    alert("Please enter only numeric value.");
                    $('#advanceAmount')[0].focus();
                    return false;
                } 
                if(!$('#isChecked')[0].checked)
                {
                    alert("Please accept the terms and conditions.");
                    return false;
                }
            }
        </script>       
    </head>
    <body style="padding-top: 10px;">
        <jsp:include page="ProfileTabs.jsp">
            <jsp:param name="menuHighlight" value="REIMBURSEMENTDETAILS" />
            <jsp:param name="ltcID" value="${ltcId}" />
        </jsp:include>
        <div style="width:95%;margin:0px auto;border-top:0px;">
            <div class="row" style="border: 1px solid #ddd;padding: 5px;">
                <form:form action="SaveRDetails.htm" method="post" commandName="LTCBean" id="frmLTC">
                    <input type="hidden" name="ltcId" value="${ltcId}" />
                    <div class="container-fluid">
                        <div class="panel panel-default">
                            <div class="panel-heading" style="margin-bottom:10px;padding-bottom:5px;color:#0052A7">
                                
                                <strong style="font-size:14pt;">Apply for LTC:: <span style="color:#890000;">Reimbursement Details</span></strong>
                            </div>
                            <div>


                                <div class="row" style="margin-bottom: 7px;">
                                    <div class="col-lg-10">
                                        <table width="100%" class="table tabler-bordered" style="border:1px solid #CCC">
                                            <tr bgcolor="#EAEAEA">
                                                <td></td>
                                                <td colspan="3"><strong>Total reimbursable estimated cost of journey, both ways</strong></td>
                                                <td>&nbsp;</td>
                                            </tr>
                                            <tr>
                                                <td width="20"></td>
                                                <td width="20">(a)</td>
                                                <td width="350">Approximate fare by train</td>
                                                <td width="300"><form:input class="form-control" path="costByTrain" id="costByTrain" /></td>
                                                <td>&nbsp;</td>
                                            </tr> 
                                            <tr>
                                                <td></td>
                                                <td>(b)</td>
                                                <td>Approximate fare by Road</td>
                                                <td><form:input class="form-control" path="costByRoad" id="costByRoad" /></td>
                                                <td>&nbsp;</td>
                                            </tr> 
                                            <tr>
                                                <td></td>
                                                <td>(c)</td>
                                                <td>Approximate fare by other means of travel</td>
                                                <td><form:input class="form-control" path="costByOther" id="costByOther" /></td>
                                                <td>&nbsp;</td>
                                            </tr>
                                        </table>
                                    </div>
                                </div> 
                                <div class="row" style="margin-bottom: 7px;">
                                    <div class="col-lg-2">
                                        <label for="orderNumber">Amount of Advance Applied for:<span style="color: red">*</span></label>
                                    </div>
                                    <div class="col-lg-2">   
                                        <form:input class="form-control" path="advanceAmount" id="advanceAmount" />
                                    </div>
                                    <div class="col-lg-2">
                                        <label for="orderDate"> Any other Relevant Information required by Sanctioning Authority:<span style="color: red">*</span></label>
                                    </div>
                                    <div class="col-lg-4">
                                        <form:textarea rows="4" cols="90" class="form-control" path="anyOtherInfo" id="anyOtherInfo" />
                                    </div>
                                </div>  
                                <div class="row" style="margin-bottom: 7px;">
                                    <div class="col-lg-10">
                                        <input type="checkbox" id="isChecked" value="Y" /> <label for="isChecked">Please accept the below Terms and Conditions</label><br />
                                        <p>1. That I am aware of the provisions of the LTC Rules of the State Govt.</p>
                                        <p>2. That while on journey and stay during LTC I shall not claim compensation for loss of property/accident unless otherwise admissible.</p>
                                        <p>3. That, with or family members, I will abide by restrictions/orders/requisitions as and when necessary during LTC Period.</p>
                                        <p>4. That, my husband/wife being a State Government employee, I undertake that he/she has not availed LTC either for self or for family members either to and he/she will not be entitled for the benefit thereafter.</p>
                                    </div>
                                </div>
                            </div>





                        </div>

                        <div >
                            <button type="submit" name="submit" value="Save" class="btn btn-default btn-xlg" style="background:#008900;color:#FFF;" onclick="return validateForm();">Save</button>
                            <a class="btn btn-default btn-xlg" style="background:#008900;color:#FFF;text-align:center;text-decoration:none;" href="EmpLTCList.htm">Cancel</a>


                        </div>
                    </div>
                </div>




            </form:form>
        </div>
    </div>
</body>
</html>
