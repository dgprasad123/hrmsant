<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<div class="container-fluid" style="padding-bottom: 125px;">
    <div class="panel panel-default">
        <div class="panel-heading">
            <div class="row">
                <div class="col-lg-12">
                    <h2 style="color:  #0071c5;" align="center">Message Inbox</h2>
                </div>
            </div>

        </div>
        <div class="panel panel-default">

            <div class="panel-body">
                <table class="table table-bordered">
                    <thead>
                        <tr bgcolor="#FAFAFA">
                             <th>Sl.No.</th>
                            <th>Date</th>
                            <th>Message</th>
                            
                        </tr>
                    </thead>
                    <tbody>                          
                        <tr>
                            <td>1.</td>
                            <td>20-Dec-2020</td>
                            <td>Sir/Madam, You are permitted to retain quarters till your continuance at KBK region
                                <br/>                                
                                RENT Officer
                                
                            </td>  
                        </tr>
                        <tr>
                            <td>2.</td>
                            <td>21-Dec-2020</td>
                            <td>Sir/Madam, your quarter retention period will over on 1st January 2020, Kindly vacate the quarter on or before due date
                                <br/>                                
                                RENT Officer
                               
                            </td>  
                        </tr>
                        <tr>
                            <td>3.</td>
                            <td>5th-January-2021</td>
                            <td>Sir/Madam, your quarter retention period is over, Kindly vacate the quarter immediately.
                                <br/>                                
                                RENT Officer
                               
                            </td>  
                        </tr>
                    </tbody>
                </table>

            </div>
        </div>
    </div>
</div>