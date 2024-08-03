<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Participant Detail</title>
        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
        <link rel="stylesheet" type="text/css" href="resources/css/bootstrap.min.css" />
        <style type="text/css">
            body{margin:10px;background:#FAFAFA;}
           table td{padding:5px;}
        </style>        
    </head>
    <body>
        <h1 style="font-size:18pt;color:#006CA0;">View Participant Details</h1>
        <table width="100%" cellspacing="1" cellpadding="4" border="0" class="table-bordered">
            <tr>
                <td width="150" align="right">Name:</td>
                <td>${nisgBean.fullName}</td>
            </tr>
            <tr>
                <td align="right">Department:</td>
                <td>${nisgBean.department}</td>
            </tr>            
            <tr>
                <td align="right">Post:</td>
                <td>${nisgBean.designation}</td>
            </tr>  
            <tr>
                <td align="right">Highest Qualification:</td>
                <td>${nisgBean.highestQualification}</td>
            </tr>
           <tr>
                <td align="right">Projects Initiated1:</td>
                <td>${nisgBean.projectsInitiated1}</td>
            </tr>
           <tr>
                <td align="right">Projects Initiated2:</td>
                <td>${nisgBean.projectsInitiated2}</td>
            </tr>
           <tr>
                <td align="right">Projects Initiated3:</td>
                <td>${nisgBean.projectsInitiated3}</td>
            </tr> 
           <tr>
                <td align="right">Projects Associated1:</td>
                <td>${nisgBean.projectsAssociated1}</td>
            </tr>
           <tr>
                <td align="right">Projects Associated2:</td>
                <td>${nisgBean.projectsAssociated2}</td>
            </tr>
           <tr>
                <td align="right">Projects Associated3:</td>
                <td>${nisgBean.projectsAssociated3}</td>
            </tr> 
           <tr>
                <td align="right">Top Two Things1:</td>
                <td>${nisgBean.topTwoThings1}</td>
            </tr>
           <tr>
                <td align="right">Top Two Things2:</td>
                <td>${nisgBean.topTwoThings2}</td>
            </tr>
           <tr>
                <td align="right">Top Two Things3:</td>
                <td>${nisgBean.topTwoThings3}</td>
            </tr>      
          <tr>
                <td align="right">Conceptualization Phase1:</td>
                <td>${nisgBean.conceptualizationPhase1}</td>
            </tr>
           <tr>
                <td align="right">Conceptualization Phase2:</td>
                <td>${nisgBean.conceptualizationPhase2}</td>
            </tr>  
          <tr>
                <td align="right">Development Phase1:</td>
                <td>${nisgBean.devPhase1}</td>
            </tr>
           <tr>
                <td align="right">Development Phase2:</td>
                <td>${nisgBean.devPhase2}</td>
            </tr>
          <tr>
                <td align="right">Implementation Phase1:</td>
                <td>${nisgBean.impPhase1}</td>
            </tr>
           <tr>
                <td align="right">Implementation Phase2:</td>
                <td>${nisgBean.impPhase2}</td>
            </tr> 
         <tr>
                <td align="right">Operation Phase1:</td>
                <td>${nisgBean.operationPhase1}</td>
            </tr>
           <tr>
                <td align="right">Operation Phase2:</td>
                <td>${nisgBean.operationPhase2}</td>
            </tr>   
          <tr>
                <td align="right">Previous Training1:</td>
                <td>${nisgBean.previousTraining1}</td>
            </tr>
           <tr>
                <td align="right">Previous Training2:</td>
                <td>${nisgBean.previousTraining2}</td>
            </tr>
           <tr>
                <td align="right">Previous Training3:</td>
                <td>${nisgBean.previousTraining3}</td>
            </tr>
         <tr>
                <td align="right">Skill Enhance1:</td>
                <td>${nisgBean.skillEnhance1}</td>
            </tr>
           <tr>
                <td align="right">Skill Enhance2:</td>
                <td>${nisgBean.skillEnhance2}</td>
            </tr>
           <tr>
                <td align="right">Skill Enhance3:</td>
                <td>${nisgBean.skillEnhance3}</td>
            </tr>            
            <tr>
                <td align="right">Mobile:</td>
                <td>${nisgBean.mobile}</td>
            </tr> 
           <tr>
                <td align="right">Email:</td>
                <td>${nisgBean.email}</td>
            </tr>             
        </table>
    </body>
</html>
