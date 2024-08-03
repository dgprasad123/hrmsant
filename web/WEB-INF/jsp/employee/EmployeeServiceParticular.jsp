<%-- 
    Document   : EmployeeServiceParticular
    Created on : 12 Sep, 2023, 10:57:58 AM
    Author     : Surendra
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" autoFlush="true" buffer="64kb" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
           <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        <link href="css/sb-admin.css" rel="stylesheet">
        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>
        <style>
            hr {
                position: relative;
                top: 20px;
                border: none;
                height: 7px;
                background: black;
                margin-bottom: 50px;
            }
            
           .table > tbody > tr > td, .table > tbody > tr > th, .table > tfoot > tr > td, .table > tfoot > tr > th, .table > thead > tr > td, .table > thead > tr > th {
  padding: 8px;
  line-height: 1.42857143;
  vertical-align: top;
  border-top: 2px solid #111010;
}
        </style>
    </head>
    <body>
        <div class="container">
            <form>
                <h2 style="text-align:center;">SERVICE PARTICULARS AND BIO-DATA</h2>
                <hr >
                <div class="form-group row">
                    <label for="staticEmail" class="col-sm-3 col-form-label">1. Name:</label>
                    <div class="col-sm-9">
                        ${employeeProfile.empName}
                    </div>
                </div>
                <div class="form-group row">
                    <label for="inputPassword" class="col-sm-3 col-form-label">2. Father’s name:</label>
                    <div class="col-sm-9">
                        ${employeeProfile.fathername}
                    </div>
                </div>
                <div class="form-group row">
                    <label for="inputPassword" class="col-sm-3 col-form-label">3. Date of birth:</label>
                    <div class="col-sm-9">
                        ${employeeProfile.dob}
                    </div>
                </div>
                <div class="form-group row">
                    <label for="inputPassword" class="col-sm-3 col-form-label">4. Date of superannuation:</label>
                    <div class="col-sm-9">
                        ${employeeProfile.dos}
                    </div>
                </div>
                <div class="form-group row">
                    <label for="inputPassword" class="col-sm-3 col-form-label">5. Date of joining in the Govt	Service with batch & rank:
                    </label>
                    <div class="col-sm-9">
                        ${employeeProfile.doeGov}
                    </div>
                </div>
                <div class="form-group row">
                    <label for="inputPassword" class="col-sm-3 col-form-label">6. Date of passing the S.I’s Course of training:</label>
                    <div class="col-sm-9">

                    </div>
                </div>
                <div class="form-group row">
                    <label for="inputPassword" class="col-sm-3 col-form-label">7. Whether General/OBC or SC/ST:</label>
                    <div class="col-sm-9">
                        ${employeeProfile.category}
                    </div>
                </div>
                <div class="form-group row">
                    <label for="inputPassword" class="col-sm-3 col-form-label">8. Date on which appointed in the feeder grade
                        :</label>
                    <div class="col-sm-9">

                    </div>
                </div>
                <div class="form-group row">
                    <label for="inputPassword" class="col-sm-3 col-form-label">9. Length of Service in the feeder grade as on 1st day of January of the year in which the DPC meets
                        :</label>
                    <div class="col-sm-9">

                    </div>
                </div>
                <div class="form-group row">
                    <label for="inputPassword" class="col-sm-3 col-form-label">10. Education:</label>
                    <div class="col-sm-9">
                        ${employeeProfile.qualification}
                    </div>
                </div>
                <div class="form-group row">
                    <label for="inputPassword" class="col-sm-3 col-form-label">11. Home District:</label>
                    <div class="col-sm-9">
                        ${employeeProfile.distName}
                    </div>
                </div>
                <div class="form-group row">
                    <label for="inputPassword" class="col-sm-3 col-form-label">12. Unit of posting:</label>
                    <div class="col-sm-9">
                        ${employeeProfile.placeposting} ( ${employeeProfile.postingunitdoj} )
                    </div>
                </div>
                <div class="form-group row">
                    <label for="inputPassword" class="col-sm-3 col-form-label">13. Date of continuous officiation in the rank of Dy.S.P.:</label>
                    <div class="col-sm-9">

                    </div>
                </div>
                <div class="form-group row">
                    <label for="inputPassword" class="col-sm-3 col-form-label">14. Date of confirmation:</label>
                    <div class="col-sm-9">

                    </div>
                </div>
                <div class="form-group row">
                    <label for="inputPassword" class="col-sm-3 col-form-label">15. Date of counting seniority in-</label>
                    <div class="col-sm-9">

                    </div>
                </div>
                <div class="form-group row">
                    <ol type="a">
                        <li>Substantive rank:</li>
                        <li>Officiating rank:</li>
                    </ol>
                </div>
                <div class="form-group row">
                    <label for="inputPassword" class="col-sm-3 col-form-label">16. Place of posting to different places (from Initial appointment) as :</label>
                    <div class="col-sm-9">
                        <table class="table" border="2px"   >
                            
                            <thead>
                                <tr border="2px"><td style="width:2%">Sl No.</td>
                                    <td style="width:10%">PLACE</td>
                                    <td style="width:10%">FROM</td>
                                    <td style="width:10%">TO</td>
                                    <td style="width:10%">JOIN DATE</td>
                                </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${serviceList}" var="servicelist" varStatus="count">
                                <tr>
                                    <td>${count.index + 1}</td>
                                    <td style="width:10%">${servicelist.placeOFposting}</td>
                                    <td style="width:10%" >${servicelist.serviceFrom}</td> 
                                    <td style="width:10%">${servicelist.serviceTo}</td>
                                    <td style="width:10%">${servicelist.joindategoo}</td>

                                </tr>
                            </c:forEach>                            
                            </tbody>
                        </table>
                    </div>
                </div>
                <div class="form-group row">
                    <label for="inputPassword" class="col-sm-3 col-form-label">
                        17. Period and particulars of training and Courses undergone:
                    </label>
                    <div class="col-sm-9">
                        ${employeeProfile.periodparticularstraining}
                    </div>
                </div>
                <div class="form-group row">
                    <label for="inputPassword" class="col-sm-3 col-form-label" >18. Power of decision and assuming responsibility:</label>
                    <div class="col-sm-9">
                        ${employeeProfile.powerofdecesion}
                    </div>
                </div>
                <div class="form-group row">
                    <div class="row">
                        
                        <label for="inputPassword" class="col-sm-3 col-form-label" style="padding-left:30px">19. Fitness (a) Bodily :</label>
                        <div class="col-sm-9">
                            ${employeeProfile.physicalfitness}
                        </div>
                    </div>
                    <div class="row">
                        <label for="inputPassword" class="col-sm-3 col-form-label">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(b) Mentally :</label>
                        <div class="col-sm-9">
                            ${employeeProfile.mentalfitness}
                        </div>
                    </div>
                </div>
                <div class="form-group row">
                    <label for="inputPassword" class="col-sm-3 col-form-label">20. Standard of honesty & integrity:</label>
                    <div class="col-sm-9">
                        ${employeeProfile.honestyintegrity}
                    </div>
                </div>
                <div class="form-group row">
                    <label for="inputPassword" class="col-sm-3 col-form-label">21. Professional ability (any special flair on professional subject) 
                        :</label>
                    <div class="col-sm-9">
                        ${employeeProfile.remarksnominatingprofessional}
                    </div>
                </div>
                <div class="form-group row">
                    <label for="inputPassword" class="col-sm-3 col-form-label">22. General suitability</label>
                    <div class="col-sm-9">

                    </div>
                </div>
                <div class="form-group row">
                    <label for="inputPassword" class="col-sm-3 col-form-label">23. Major/minor punishment awarded during last 5 years:</label>
                    <div class="col-sm-9">

                    </div>
                </div>
                <div class="form-group row">
                    <label for="inputPassword" class="col-sm-3 col-form-label">24. Whether up-to-date Property Statement Submitted (Yes or No):
                    </label>
                    <div class="col-sm-9">
                          ${employeeProfile.property_submitted_if_any}(${employeeProfile.property_submitted_on_hrms_byofficer})
                    </div>
                </div>
                <div class="form-group row">
                    <label for="inputPassword" class="col-sm-3 col-form-label">25. GPF No.:</label>
                    <div class="col-sm-9">
                        <div style="text-align:left;">
                           ${employeeProfile.gpfno}
                        </div>
                        <div style="text-align:right;">
                            Signature of the Commdt./ S.P.
                        </div>
                    </div>
                </div>
                <div class="form-group row">
                    <label for="inputPassword" class="col-sm-3 col-form-label">26. Views of the Range D.I.G/I.G.P/Addl.D.G.P.:</label>
                    <div class="col-sm-9">
                        <div style="text-align:right;">
                            Signature of Range D.I.G.P/I.G.P/ADGP/ Controlling Authority.
                        </div>
                    </div>
                </div>

            </form>  
            <form>
                <h2 style="text-align:center;">P R O F O R M A – II</h2>
                <div class="form-group row">
                    <label for="inputPassword" class="col-sm-3 col-form-label">1- Name of the Officer with rank:</label>
                    <div class="col-sm-9">
                         ${employeeProfile.empName} (${employeeProfile.present_rank}) 
                    </div>
                </div>
                <div class="form-group row">
                    <label for="inputPassword" class="col-sm-3 col-form-label"> 2- Present place of posting:</label>
                    <div class="col-sm-9">
                        ${employeeProfile.placeOFposting}
                    </div>
                </div>
                <div class="form-group row">
                    <label for="inputPassword" class="col-sm-3 col-form-label">  3- Details of CBI report If any against the officer:</label>
                    <div class="col-sm-9">

                    </div>
                </div>
                <div class="form-group row">
                    <label for="inputPassword" class="col-sm-3 col-form-label">4- Details of departmental Action pending, if any:</label>
                    <div class="col-sm-9">

                    </div>
                </div>
                <div class="form-group row">
                    <label for="inputPassword" class="col-sm-3 col-form-label">  5- Details of any other confidential	Report against the officer which Have not been recorded in the C.C.Roll:</label>
                    <div class="col-sm-9">
                        <div style="text-align:right;">Signature of Commdt./ S.P.</div>
                    </div>
                </div>
                <div class="form-group row">
                    <label for="inputPassword" class="col-sm-3 col-form-label"> 6- View of D.I.G./ I.G/Addl.D.G.P.</label>
                    <div class="col-sm-9">
                        <div style="text-align:right;">
                            Signature of Range D.I.G.P/I.G.P/ADGP/ Controlling Authority.
                        </div>
                    </div>
                </div>   

            </form>  
        </div>
    </body>
</html>
