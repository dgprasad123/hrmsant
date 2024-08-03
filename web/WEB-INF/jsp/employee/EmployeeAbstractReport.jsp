<%-- 
    Document   : MyProfile
    Created on : Aug 14, 2018, 2:30:50 PM
    Author     : Manas
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: Welcome to HRMS Odisha ::</title>
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">   
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <script type="text/javascript" src="js/jquery.min.js"></script>
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">
        <link rel="stylesheet" type="text/css" href="resources/css/colorbox.css"/>
        <link rel="stylesheet" type="text/css" href="css/popupmain.css"/>  
             
        <style type="text/css">
            #profile_container{width:95%;margin:0px auto;border-top:0px;}
            ol li{width:180px;float:left;text-align:left;}
        </style>  
        <script type="text/javascript">
            function callNoImage() {
                var userPhoto = document.getElementById('loginUserPhoto');
                userPhoto.src = "images/NoEmployee.png";

            }
        </script>
        <style type="text/css">
            .card {
                background-color: #ffffff;
                border-radius: 6px;
                box-shadow: 0 2px 2px rgba(204, 197, 185, 0.5);
                color: #252422;
                margin-bottom: 20px;
                position: relative;
                z-index: 1;
            }
            .card .image {
                width: 100%;
                overflow: hidden;
                height: 260px;
                border-radius: 6px 6px 0 0;
                position: relative;
                -webkit-transform-style: preserve-3d;
                -moz-transform-style: preserve-3d;
                transform-style: preserve-3d;
            }
            .card .image img {
                width: 100%;
            }
            .card .content {
                padding: 15px 15px 10px 15px;
            }
            .card .header {
                padding: 20px 20px 0;
            }
            .card .description {
                font-size: 16px;
                color: #66615b;
                text-transform:capitalize;
            }
            .card h6 {
                font-size: 12px;
                margin: 0;
            }
            .card .category,
            .card label {
                font-size: 14px;
                font-weight: 400;
                color: #9A9A9A;
                margin-bottom: 0px;
            }
            .card .category i,
            .card label i {
                font-size: 16px;
            }
            .card label {
                font-size: 15px;
                margin-bottom: 5px;
            }
            .card .title {
                margin: 0;
                color: #252422;
                font-weight: 300;
            }
            .card .avatar {
                width: 50px;
                height: 50px;
                overflow: hidden;
                border-radius: 50%;
                margin-right: 5px;
            }
            .card .footer {
                padding: 0;
                line-height: 30px;
            }
            .card .footer .legend {
                padding: 5px 0;
            }
            .card .footer hr {
                margin-top: 5px;
                margin-bottom: 5px;
            }
            .card .stats {
                color: #a9a9a9;
                font-weight: 300;
            }
            .card .stats i {
                margin-right: 2px;
                min-width: 15px;
                display: inline-block;
            }
            .card .footer div {
                display: inline-block;
            }
            .card .author {
                font-size: 12px;
                font-weight: 600;
                text-transform: uppercase;
            }
            .card .author i {
                font-size: 14px;
            }
            .card.card-separator:after {
                height: 100%;
                right: -15px;
                top: 0;
                width: 1px;
                background-color: #DDDDDD;
                content: "";
                position: absolute;
            }
            .card .ct-chart {
                margin: 30px 0 30px;
                height: 245px;
            }
            .card .table tbody td:first-child,
            .card .table thead th:first-child {
                padding-left: 15px;
            }
            .card .table tbody td:last-child,
            .card .table thead th:last-child {
                padding-right: 15px;
            }
            .card .alert {
                border-radius: 4px;
                position: relative;
            }
            .card .alert.alert-with-icon {
                padding-left: 65px;
            }
            .card .icon-big {
                font-size: 3em;
                min-height: 64px;
            }
            .card .numbers {
                font-size: 2em;
                text-align: right;
            }
            .card .numbers p {
                margin: 0;
            }
            .card ul.team-members li {
                padding: 10px 0px;
            }
            .card ul.team-members li:not(:last-child) {
                border-bottom: 1px solid #F1EAE0;
            }

            .card-user .image {
                border-radius: 8px 8px 0 0;
                height: 150px;
                position: relative;
                overflow: hidden;
            }
            .card-user .image img {
                width: 100%;
            }
            .card-user .image-plain {
                height: 0;
                margin-top: 110px;
            }
            .card-user .author {
                text-align: center;
                text-transform: none;
                margin-top: -65px;
            }
            .card-user .author .title {
                color: #403D39;
            }
            .card-user .author .title small {
                color: #ccc5b9;
            }
            .card-user .avatar {
                width: 140px;
                height: 140px;
                border-radius: 50%;
                position: relative;
                margin-bottom: 15px;
            }
            .card-user .avatar.border-white {
                border: 5px solid #FFFFFF;
            }
            .card-user .avatar.border-gray {
                border: 5px solid #ccc5b9;
            }
            .card-user .title {
                font-weight: 600;
                line-height: 24px;
            }
            .card-user .description {
                text-transform: fullsize-kana;
                margin-top: 10px;
            }
            .card-user .content {
                min-height: 200px;
            }
            .card-user.card-plain .avatar {
                height: 190px;
                width: 190px;
            }

            .card-map .map {
                height: 500px;
                padding-top: 20px;
            }
            .card-map .map > div {
                height: 100%;
            }

            .card-user .footer,
            .card-price .footer {
                padding: 5px 15px 10px;
            }
            .card-user hr,
            .card-price hr {
                margin: 5px 15px;
            }

            .card-plain {
                background-color: transparent;
                box-shadow: none;
                border-radius: 0;
            }
            .card-plain .image {
                border-radius: 4px;
            }
            .card span{
                color: #252422;
                font-weight: 500;
                line-height: 24px;
                font-size: 16px;
            }
            .lblval{
                text-transform: capitalize;
                color: #252422;
                font-weight: 500;
                line-height: 24px;
                font-size: 16px;
            }
            
               hr {
                position: relative;
                top: 20px;
                border: none;
                height: 7px;
                background: black;
                margin-bottom: 50px;
            }
        </style>
    </head>
    <body style="padding-top: 10px;">
        <div class="col-md-2"></div>
            <div class="col-md-8" style="margin:0px auto;"> 
                    <form>
                        <h2 style="text-align:center">ABSTRACT REPORT</h2>
                        <hr >
                      <div class="form-group row">
                        <label for="staticEmail" class="col-sm-4 col-form-label">1. Name Of Government Servant</label>
                        <div class="col-sm-8">
                                 ${employeeProfile.empName}
                        </div>
                      </div>
                      <div class="form-group row">
                        <label for="inputPassword" class="col-sm-4 col-form-label">2. Designation</label>
                        <div class="col-sm-8">
                             ${employeeProfile.spn}
                        </div>
                      </div>
                      <div class="form-group row">
                        <label for="inputPassword" class="col-sm-4 col-form-label">3. GPF/PRAN A/C Number</label>
                        <div class="col-sm-8">
                            ${employeeProfile.gpfno}
                        </div>
                      </div>
                        <div class="form-group row">
                        <label for="inputPassword" class="col-sm-4 col-form-label">4. Date of Birth</label>
                        <div class="col-sm-8">
                            ${employeeProfile.dob}
                        </div>
                      </div>
                         <div class="form-group row">
                        <label for="inputPassword" class="col-sm-4 col-form-label">5. Date of Commencement of Service</label>
                        <div class="col-sm-8">
                        ${employeeProfile.doeGov}
                        </div>
                      </div>
                         <div class="form-group row">
                        <label for="inputPassword" class="col-sm-4 col-form-label">6. Date of Retirement with type</label>
                        <div class="col-sm-8">
                            ${employeeProfile.dor} 
                        </div>
                      </div>
                        <div class="form-group row">
                        <label for="inputPassword" class="col-sm-4 col-form-label">7. Gross qualifying service</label>
                        <div class="col-sm-8">
                             ${noofdaysofserviceperiod} 
                        </div>
                      </div>
                         <div class="form-group row">
                        <label for="inputPassword" class="col-sm-4 col-form-label">8. Non-qualifying service with specification</label>
                        <div class="col-sm-8">
                            ${nonqualifyingdays} 
                        </div>
                      </div>
                         <div class="form-group row">
                        <label for="inputPassword" class="col-sm-4 col-form-label">9. Net qualifying service for calculation of pensionary</label>
                        <div class="col-sm-8">
                        </div>
                      </div>
                         <div class="form-group row">
                        <label for="inputPassword" class="col-sm-4 col-form-label">10. Type of Appointment</label>
                        <div class="col-sm-8">
                             ${employeeProfile.recsource} 
                        </div>
                      </div>
                         <div class="form-group row">
                        <label for="inputPassword" class="col-sm-4 col-form-label">11. Initial Post</label>
                        <div class="col-sm-8">
                            ${employeeProfile.initialpost} 
                        </div>
                      </div>
                         <div class="form-group row">
                        <label for="inputPassword" class="col-sm-4 col-form-label">12. Initial Pay scale</label>
                        <div class="col-sm-8">
                            ${employeeProfile.payScale} 
                        </div>
                      </div>
                        <div class="form-group row">
                        <label for="inputPassword" class="col-sm-4 col-form-label">13. Date of Availing TBA</label>
                        <div class="col-sm-8">
                            ${employeeProfile.txtwefTime} 
                        </div>
                      </div>
                         <div class="form-group row">
                        <label for="inputPassword" class="col-sm-4 col-form-label">14. Pay scale as on 31.12.2005</label>
                        <div class="col-sm-8">
                             ${employeeProfile.payscaleon31122005} 
                        </div>
                      </div>
                         <div class="form-group row">
                        <label for="inputPassword" class="col-sm-4 col-form-label">15. Pay scale as on 01.01.2006</label>
                        <div class="col-sm-8">
                              ${employeeProfile.payscaleon112006} 
                        </div>
                      </div>
                        <div class="form-group row">
                        <label for="inputPassword" class="col-sm-4 col-form-label">16. Date of availing ACP/2nd ACP/3rd ACP</label>
                        <div class="col-sm-8">
                             ${employeeProfile.acp} 
                        </div>
                      </div>
                         <div class="form-group row">
                        <label for="inputPassword" class="col-sm-4 col-form-label">17. Date of availing RACP/2nd RACP/3rd RACP</label>
                        <div class="col-sm-8">   
                           ${employeeProfile.rracp}
                        </div>
                      </div>
                         <div class="form-group row">
                        <label for="inputPassword" class="col-sm-4 col-form-label">18. RACP specifically mention with or without benefit</label>
                        <div class="col-sm-8">
                        </div>
                      </div>
                        <div class="form-group row">
                        <label for="inputPassword" class="col-sm-4 col-form-label">19. Pay/grade pay as on 31.12.2015</label>
                        <div class="col-sm-8">
                                                            ${employeeProfile.payment}
                       

                        </div>
                      </div>
                         <div class="form-group row">
                        <label for="inputPassword" class="col-sm-4 col-form-label">20. Pay fixed on or after 01.01.2016</label>
                        <div class="col-sm-8">
                              ${employeeProfile.payfixed}
                        </div>
                      </div>
                        <div class="form-group row">
                        <label for="inputPassword" class="col-sm-4 col-form-label">21. Pay before fixation under MACP</label>
                        <div class="col-sm-8">
                            ${payBMacp.pay}
                        </div>
                      </div>
                        <div class="form-group row">
                        <label for="inputPassword" class="col-sm-4 col-form-label">22. Pay After fixation of MACP with level</label>
                        <div class="col-sm-8">
                            ${payAMacp.payAfter} (${payAMacp.level})
                        </div>
                      </div>
                         <div class="form-group row">
                        <label for="inputPassword" class="col-sm-4 col-form-label">23. Number of promotion availed with date and designation</label>
                        <div class="col-sm-8">
                            <table class="table" border="2px"   >
                            
                            <thead>
                                <tr border="2px"><td style="width:2%">Sl No.</td>
                                    <td style="width:10%">JOIN DATE</td>
                                     <td style="width:10%">OFFICE NAME</td>
                                    <td style="width:10%">POST</td>
                                </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${promotionList}" var="promotionList" varStatus="count">
                                <tr>
                                    <td>${count.index + 1}</td>
                                    <td style="width:10%">${promotionList.joindategoo}</td>
                                    <td style="width:10%">${promotionList.office}</td>
                                    <td style="width:10%">${promotionList.promotionalpost}</td>

                                </tr>
                            </c:forEach>                            
                            </tbody>
                        </table>
                        </div>
                      </div>
                         <div class="form-group row">
                        <label for="inputPassword" class="col-sm-4 col-form-label">24. Period of Deputation (If any)</label>
                        <div class="col-sm-8">
                        </div>
                      </div>
                        <div class="form-group row">
                        <label for="inputPassword" class="col-sm-4 col-form-label">25. Type of loan availed with amount sanctioned</label>
                        <div class="col-sm-8">
                        </div>
                      </div>
                        <div class="form-group row">
                        <label for="inputPassword" class="col-sm-4 col-form-label">26. Any vigilance/Court proceeding</label>
                        <div class="col-sm-8">
                        </div>
                      </div>
                        <div class="form-group row">
                        <label for="inputPassword" class="col-sm-4 col-form-label">27. Option exercise with date/DNI during financial up-gradation,pay fixation and promotion</label>
                        <div class="col-sm-8">
                            ${revisionDate.revisedate} / ${fixationDni.fixationDni} 
                        </div>
                      </div>
                        <div class="form-group row">
                        <label for="inputPassword" class="col-sm-4 col-form-label">28. Any other special departmental orders</label>
                        <div class="col-sm-8">
                        </div>
                      </div>
                    </form>  
            </div>  
                <div class="col-md-2"></div>

            
                      
    </body>
    
   
</html>
