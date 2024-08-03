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
                if ($('#fMemberName').val() == '')
                {
                    alert("Please enter Name of the Family Member.");
                    $('#fMemberName')[0].focus();
                    return false;
                }
                if ($('#fMemberRelationship').val() == '')
                {
                    alert("Please enter the Family Relationship.");
                    $('#fMemberRelationship')[0].focus();
                    return false;
                }
                if ($('#fMemberdob').val() == '')
                {
                    alert("Please enter Age/DOB.");
                    $('#fMemberdob')[0].focus();
                    return false;
                }
                if ($('#fMemberMstatus').val() == '')
                {
                    alert("Please select Marrital Status.");
                    $('#fMemberMstatus')[0].focus();
                    return false;
                }
                if ($('#isStateGovt').val() == '')
                {
                    alert("Please select whether State Govt emp or not.");
                    $('#isStateGovt')[0].focus();
                    return false;
                }
                if($('#monthlyIncome').val() != '' && isNaN($('#monthlyIncome').val()))
                {
                    alert("Please enter only numeric values.");
                    $('#monthlyIncome')[0].focus();
                    return false;
                }
            }
        </script>
    </head>
    <body style="padding-top: 10px;">
        <jsp:include page="ProfileTabs.jsp">
            <jsp:param name="menuHighlight" value="FAMILYMEMBERS" />
            <jsp:param name="ltcID" value="${ltcId}" />
        </jsp:include>
        <div style="width:95%;margin:0px auto;border-top:0px;">
            <div class="row" style="border: 1px solid #ddd;padding: 5px;">
                <form:form action="SaveFamilyMembers.htm" method="post" commandName="LTCBean" id="frmLTC">
                    <input type="hidden" name="ltcId" value="${ltcId}" />
                    <div class="container-fluid">
                        <div class="panel panel-default">
                            <div class="panel-heading" style="margin-bottom:10px;padding-bottom:5px;color:#0052A7">
                                <strong style="font-size:14pt;">Apply for LTC:: <span style="color:#890000;">Family Members</span></strong>
                            </div>
                            <div>
<table class="table table-bordered" style="font-size:10pt;">
                            <thead>
                                <tr bgcolor="#EAEAEA">
                                    <th>Sl No.</th>
                                    <th>Name</th>
                                    <th>Relationship</th>
                                    <th>Age/DOB</th>
                                    <th>Marital Status</th>
                                    <th>State Govt Emp?</th>
                                    <th>Monthly Income</th>
                                    <th style="text-align:center;">Delete</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${fList}" var="fl" varStatus="theCounter">
                                    <tr>
                                        <td>${theCounter.index+1}</td>
                                        <td>${fl.fMemberName}</td>
                                        <td>${fl.fMemberRelationship}</td>
                                        <td>${fl.fMemberdob}</td>
                                        <td>${fl.fMemberMstatus}</td>
                                        <td>${fl.isStateGovt}</td>
                                        <td>${fl.monthlyIncome}</td>
                                        <td></td>
                                        </tr>
                                
                                </c:forEach>
                            </tbody>
                        </table>
                                <div class="row" style="margin-bottom: 7px;" id="blk5">
                                    <div class="col-lg-10">
                                        <table width="100%" class="table tabler-bordered" style="border:1px solid #CCC">
                                            <tr bgcolor="#EAEAEA">
                                                <td></td>
                                                <td colspan="2"><strong>Name of the family member</strong></td>
                                                <td><form:input class="form-control" path="fMemberName" id="fMemberName" /></td>
                                                <td>&nbsp;</td>
                                            </tr>
                                            <tr>
                                                <td width="20"></td>
                                                <td width="20">(a)</td>
                                                <td width="350">Relationship of the applicant</td>
                                                <td width="300">
                                                    
                                <form:select path="fMemberRelationship" id="fMemberRelationship" size="1" class="form-control">
                        <form:option value="" label="--Select--"/>
                        <form:options items="${relationList}" itemValue="value" itemLabel="value"/>
                                </form:select>                                                    
                                                </td>
                                                <td>&nbsp;</td>
                                            </tr> 
                                            <tr>
                                                <td></td>
                                                <td>(b)</td>
                                                <td>Age/Date of Birth</td>
                                                <td><form:input class="form-control" path="fMemberdob" id="fMemberdob" /></td>
                                                <td>&nbsp;</td>
                                            </tr> 
                                            <tr>
                                                <td></td>
                                                <td>(c)</td>
                                                <td>Whether married or unmarried</td>
                                                <td>
                                                    <form:select id="fMemberMstatus" class="form-control" path="fMemberMstatus">
                                                <option value="">--Select--</option>
                                                <option value="Married">Married</option>
                                                <option value="Unmarried">Unmarried</option>
                                            </form:select>                                                     
                                            </td>
                                            <td>&nbsp;</td>
                                            </tr>
                                            <tr>
                                                <td></td>
                                                <td>(d)</td>
                                                <td>Whether a State Govt. servant</td>
                                                <td>
                                                    <form:select id="isStateGovt" class="form-control" path="isStateGovt">
                                                <option value="">--Select--</option>
                                                <option value="No">No</option>
                                                <option value="Yes">Yes</option>
                                            </form:select>                                                    
                                            </td>
                                            <td>&nbsp;</td>
                                            </tr>
                                            <tr>
                                                <td></td>
                                                <td>(e)</td>
                                                <td>Monthly income from all sources, if any</td>
                                                <td><form:input class="form-control" path="monthlyIncome" id="monthlyIncome" /></td>
                                                <td>&nbsp;</td>
                                            </tr>                                    
                                        </table>
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
