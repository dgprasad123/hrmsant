<%-- 
    Document   : PostProposal
    Created on : Dec 29, 2018, 5:52:10 PM
    Author     : Manas--%>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <style>
            table, th, td {
                border: 1px solid black;
                border-collapse: collapse;
            }
            th, td {
                padding: 5px;
                text-align: left;    
            }
            .table-responsive {
                max-height:450px;
                font-size: 10px;
            }
            .table-bordered{
                font-size: 12px;
            }
        </style>
    </head>
    <body>
        <div class="container-fluid" style="margin-top: 10px;">
            <div class="panel panel-default">
                <div class="panel-heading">
                    Proposal For Recruitment / Creation of New Post / Abolition of Post
                </div>
                <div class="panel-body">
                    <table class="table table-bordered">
                        <thead>
                            <tr>                                
                                <th width="5%">Sl No</th>                                
                                <th width="30%">Submitted By</th>
                                <th width="30%">Submitted To</th>                                
                                <th width="5%">Submitted On</th>
                                <th width="5%">Edit/View</th>
                                <th width="5%">Submit</th>
                                <th width="15%">Annexure-I</th>
                            </tr>                            
                        </thead>
                        <tbody>
                            <c:forEach items="${ProposalList}" var="prop" varStatus="cnt">
                                <tr>
                                    <td>${cnt.index + 1}</td>
                                    <td>${prop.submittedBySpc}</td>
                                    <td>&nbsp;</td>
                                    <td>&nbsp;</td>
                                    <td><a href="editPostProposalDetail.htm?proposalId=${prop.porposalId}"> Edit </a></td>
                                    <td><a href="chooseAuthority.htm?proposalId=${prop.porposalId}&action=Submit">Submit</a></td>
                                    <td>
                                        <a href="getProposalAnnexure1.htm?proposalId=${prop.porposalId}" target="_blank">Page-I</a> | 
                                        <a href="getProposalAnnexure2.htm?proposalId=${prop.porposalId}" target="_blank">Page-II</a> | 
                                        <a href="getProposalAnnexure3.htm?proposalId=${prop.porposalId}" target="_blank">Page-III</a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
                <div class="panel-footer">
                    <form:form action="PostProposalDetail.htm">
                        <input type="submit" class="btn btn-default" name="action" value="PROPOSAL"/>                        
                    </form:form>
                </div>
            </div>
        </div>
    </body>
</html>
