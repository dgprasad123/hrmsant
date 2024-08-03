<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript">
            $(document).ready(function () {
                var url = "GetFinYearJSON.htm";
                $.getJSON(url, function (data) {
                    $.each(data, function (i, obj) {
                        $('#fyear').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                    });
                });
            });
            function validateForm(){
                if($('#fyear').val() == ""){
                    alert("Please select Financial Year");
                    return false;
                }
               return true; 
            }
        </script>
    </head>
    <body>
        <form action="AnnualIncomeTAXExcelReport.htm" method="POST">
            <input type="hidden" name="empid" value="${empid}"/>
            <div class="container-fluid" style="margin-top: 20px;">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        My Income TAX Report 
                    </div>
                </div>
                <div class="panel-body">
                    <div class="row" style="margin-bottom: 7px;">
                        <div class="col-lg-4">
                            Select Financial Year:
                        </div>
                        <div class="col-lg-4">   
                            <select name="fyear" id="fyear" class="form-control">
                                <option value="">--Select--</option>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="panel-footer">
                    <input type="submit" name="submit" value="Download" class="btn btn-primary" onclick="return validateForm();"/>
                </div>
            </div>
        </form>
    </body>
</html>
