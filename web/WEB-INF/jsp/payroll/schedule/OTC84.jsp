<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%
    int pageNo = 1;
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: OTC 84 ::</title>
        <style type="text/css" media="screen">
            .pgHeader{
                font-size:12px;
                font-family:verdana;
                font-weight: bold;
            }
            .tblHeader{
                font-size:12px;
                text-align:center;
                font-family:verdana;
                font-weight: bold;
                border-top:1px solid black;
                border-bottom:1px solid black;
                border-left:1px solid black;
                border-right:1px solid black;
            }
            .page{
                width:80%;
                border:1px solid #333333;
                padding:8px;
            }
        </style>
        <style type="text/css" media="printer">
        .page{
            width:100%;
            border:0px;
            padding:8px;
        }
        </style>
    </head>
    <body>
        
    <div align="center">
        <div class="page" align="center">
            <div><b>O.T.C. 84</b></div>
            <div>See Subsidary Rule(256)(1)(351)</div>
            <div>FORM OF ADVICE</div>
            <div>Letter No ____________/ Date</div>
            <div align="left">To</div>
            <div align="left">&nbsp;&nbsp;<b style="text-decoration:underline;">The Treasury officer, <c:out value="${OTC84Header.treasuryOffice}"/></b></div>
            
            <div align="left">
                <c:out value="${OTC84Header.branchManager}"/>
                    <c:if test="${not empty branchName}">
                        ,<c:out value="${OTC84Header.branchName}"/>
                    </c:if>
            </div>
            <div align="left" style="min-height: 20px;">&nbsp;</div>
            <div align="left">Sub-Advice for payment of salary for the month of <c:out value="${OTC84Header.billMonth}"/> &nbsp; <c:out value="${OTC84Header.billYear}"/></div>
            <div align="left" style="min-height: 20px;">&nbsp;</div>
            <div align="left">Sir,</div>
            <div align="left">
                &nbsp;&nbsp;I am to intimate that the Bill No :<b><c:out value="${OTC84Header.billDesc}"/>&nbsp;/&nbsp;<c:out value="${OTC84Header.billDate}"/></b> amounting to Rs 
                <b><c:out value="${OTC84Header.gross}"/></b> /- (Rupees <c:out value="${OTC84Header.grossAmountWord}"/> )only 
                has been counter signed by me today in favor of the <b><c:out value="${OTC84Header.offName}"/></b>.
            </div>
            <div align="right" style="margin-top:25px;">Yours Faithfully</div>
            <div align="right" style="margin-top:25px;">Signature of</div>
            <div align="right">Counter Signature Authority</div>
            <div align="left">Memo No.................</div>
            <div align="left">
                <p>
                    Copy with bill in original are sent to <b><c:out value="${OTC84Header.offName}"/></b>, &nbsp; for information and directed to 
                    send the monthly return of expenditure of each month along with bill no and date and TV No and date and amount of drawl
                    to the _______________________________________________. At the earliest possible under information to this office.
                </p>
            </div>
            
            <div align="right" style="margin-top:25px;">Yours Faithfully</div>
            <div align="right" style="margin-top:25px;">Signature of</div>
            <div align="right">Counter Signature Authority</div>
            <div align="left">Memo No.................</div>
            <div align="left">Copy forwarded to the ________________________________________________________.</div>
            <div align="right" style="margin-top:25px;">Yours Faithfully</div>
            <div align="right" style="margin-top:25px;">Signature of</div>
            <div align="right">Counter Signature Authority</div>
      </div>
    </div>
    </div>  
</body>
</html>
