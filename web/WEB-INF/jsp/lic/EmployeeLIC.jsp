<%-- 
    Document   : SectionDefination
    Created on : Nov 21, 2016, 3:12:08 PM
    Author     : Manas Jena
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>      
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">
        <!-- LAYOUT v 1.3.0 -->
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>        

        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>



    </head>
    <body>
        <div class="container-fluid">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <div class="row">
                        <div class="col-lg-12">

                        </div>
                    </div>
                </div>
                <div class="panel-body">
                    <form class="form-inline" action="saveEmployeeLicData.htm" method="POST" >

                        <input type='hidden' name='empid' value='${EmpId}'/>
                        <input type='hidden' name='elId' value=''/>
                        <table class="table table-bordered">
                            <tr style="font-weight:bold;background:#EAEAEA">
                                <td colspan="4">Manage LIC Details
                            </tr>
                            <tr>
                                <td align="right">Policy Number :</td>
                                <td> <input name="policyNo" id="policyNo" class="form-control" required="true"  type='number'></td>
                                <td align="right">Insurance Type :</td>
                                <td>
                                    <select name="insuranceType" id="insuranceType"  size="1" class="form-control"  style="width:80%;"  required>
                                        <option value="">-Select-</option>
                                        <option value="LIC">LIC</option>
                                        <option value="TLIC">TLIC</option>
                                        <option value="PLI">PLI</option>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td align="right">Sub Amount :</td>
                                <td> <input name="subAmount" id="subAmount" class="form-control" required="true"  type='number' required></td>
                                <td align="right">WEF :</td>

                                <td> <input name="wef" id="wef" class="form-control" type='text' required readonly></td>

                            </tr>
                            <tr>
                                <td align="right">Month :</td>
                                <td>
                                    <Select name="month"  class="form-control" required>
                                        <option value="">Select Month</option>
                                        <option value="0">January </option>
                                        <option value="1">February</option>
                                        <option value="2">March</option>
                                        <option value="3">April</option>
                                        <option value="4">May</option>
                                        <option value="5">June</option>
                                        <option value="6">July</option>
                                        <option value="7">August</option>
                                        <option value="8">September</option>
                                        <option value="9">October</option>
                                        <option value="10">November</option>
                                        <option value="11">December</option>
                                    </select>

                                </td>
                                <td align="right">Year :</td>
                                <td>
                                    <Select name="year"  class="form-control" required>
                                        <option value="">Select Year</option>
                                        <option value="2010">2010 </option>
                                        <option value="2011">2011 </option>
                                        <option value="2012">2012 </option>
                                        <option value="2013">2013 </option>
                                        <option value="2014">2014 </option>
                                        <option value="2015">2015 </option>
                                        <option value="2016">2016 </option>
                                        <option value="2017">2017 </option>
                                        <option value="2018">2018 </option>
                                        <option value="2019">2019 </option>
                                        <option value="2020">2020 </option>
                                        <option value="2021">2021 </option>
                                        <option value="2022">2022 </option>
                                        <option value="2023">2023 </option>
                                        <option value="2024">2024 </option>
                                    </select>  
                                <td>

                                </td>
                            </tr>
                            <tr>
                                <td align="right">Notes :</td>
                                <td colspan=3>
                                    <input name="note" id="note" class="form-control"   type='text' style='width:300px' >
                                </td>

                            </tr>


                        </table>
                        <div class="panel-footer">                    
                            <input type="submit" value="Save LIC Details" class="btn btn-success" />
                        </div>
                    </form>
                </div>

            </div>
        </div>
        <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
        <link rel="stylesheet" href="/resources/demos/style.css">
        <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
        <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
        <script>
            $(function () {
                $('#wef').datepicker({dateFormat: 'd-MM-yy'});
            });
        </script>
    </body>
</html>
