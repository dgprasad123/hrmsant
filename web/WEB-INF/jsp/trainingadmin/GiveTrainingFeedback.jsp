<%-- 
    Document   : EmpQtrAllotment
    Created on : 16 Apr, 2018, 3:25:47 PM
    Author     : Manoj PC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Employee Training Feedback</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>

        <script type="text/javascript" src="js/moment.js"></script>
        <script type="text/javascript" src="js/jquery.min.js"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script type="text/javascript" src="js/bootstrap-confirmation.min.js"></script>
        <style type="text/css">
            h1{font-size:15pt;font-weight:bold;margin-bottom:10px;}

            .apply-table td{padding:5px;}
            .tblres td{padding:5px;}
            a.star{float:left;width:40px;height:36px;background:url('images/star.png') no-repeat;}
			a.star_o{float:left;width:40px;height:36px;background:url('images/star.png') no-repeat 0px -36px;}
            a.gstar{float:left;width:40px;height:36px;background:url('images/gstar.png') no-repeat;}
			a.gstar_o{float:left;width:40px;height:36px;background:url('images/gstar.png') no-repeat 0px -36px;}                        
                        .star-hints{font-size: 16px;font-weight: 700;color: #f8b131;margin-top: 10px;}
        </style>
        <script type="text/javascript">
            var flag = 0;
var curRating = 0;
function getHint(idx)
{
	switch(idx)
	{
		case 1:
			return 'Average';
			break;
		case 2:
			return 'Good';
			break;
		case 3:
			return "Very Good";
			break;
		case 4:
			return "Excellent";
			break;
		case 5:
			return "Outstanding";
			break;
	}
}
function checkStar(num, ratingId, starType)
{
	for(var i = 1; i <= 5; i++)
	{
		document.getElementById(starType+'star'+i+'_'+ratingId).className = 'star';
	}
	for(var i = 1; i <= num; i++)
	{
		document.getElementById(starType+'star'+i+'_'+ratingId).className = 'star_o';
	}
	$('#'+starType+'starHints'+'_'+ratingId).html(getHint(num));
}
function uncheckStar(ratingId, starType)
{
	if(flag == 0 && (curRating == 1 || curRating == 0))
	{
		for(var i = 1; i <= 5; i++)
		{
			document.getElementById(starType+'star'+i+'_'+ratingId).className = 'star';
		}
		$('#'+starType+'starHints'+'_'+ratingId).html("Click a Star Rating");
	}
	if(flag == 1)
	{
		for(var i = 1; i <= 5; i++)
		{
			document.getElementById(starType+'star'+i+'_'+ratingId).className = 'star';
		}
		for(var i = 1; i <= curRating; i++)
		{
			document.getElementById(starType+'star'+i+'_'+ratingId).className = 'star_o';
		}
		('#'+starType+'starHints'+'_'+ratingId).html(getHint(curRating));
	}


}
function checkClickStar(idx, ratingId, starType)
{
    ratingType = 'Content';
    if(starType == 'p')
    {
        ratingType = 'Presentation';
    }
    if(confirm("Are you sure you want to give this rating? Once clicked it could not be reverted back."))
    {
$.ajax({
                    url: 'SaveFeedbackRating.htm',
                    type: 'get',
                    data: 'ratingType='+ratingType+'&rating='+idx+'&ratingId='+ratingId,
                    success: function(retVal) {
                        self.location = 'GiveTrainingFeedback.htm';
                    }
                });
       }
}
function redirectPage()
{
	if(curRating <= 3)
	{
		self.location = url1;
	}
	else if(curRating > 3)
	{
		self.location = url2;
	}
}
        </script>
    </head>
    <body>
        <div style="width:80%;margin:0px auto;">
            <h1>Submit Training Feedback</h1>

                <table border='0' cellspacing='1' width='100%' align='center' class='tblres table-bordered' style='font-size:10pt;margin-top:10px;margin-bottom:10px;'>
                    <tr style="font-weight:bold;background:#0D508E;color:#FFFFFF;">
                        <td>Training Program</td>
                        <td>Batch</td>
                        <td>Date & Time</td>
                        <td>Faculty</td>
                        <td>Content Rating</td>
                        <td>Presentation Rating</td>
                    </tr>
                <c:forEach items="${erList}" var="eList">
                    <tr>
                        <td>${eList.trainingProgram}</td>
                        <td>${eList.batchName}</td>
                        <td>${eList.trainingDate}<br />
                        ${eList.fromTime} to ${eList.toTime}</td>
                        <td>${eList.faculties}</td>
                        <td>
                            <c:if  test = "${eList.contentRating == 0}">
                            <div style="text-align:center;width:197px;margin:0px auto;" id="content_rating">
	  <a href="javascript:void(0)" id="star1_${eList.ratingId}" class="star" onmouseover="javascript: checkStar(1, ${eList.ratingId}, '')" onmouseout="javascript: uncheckStar(${eList.ratingId}, '')" onclick="javascript: checkClickStar(1, ${eList.ratingId});"></a>
	  <a href="javascript:void(0)" id="star2_${eList.ratingId}" class="star" onmouseover="javascript: checkStar(2, ${eList.ratingId}, '')" onmouseout="javascript: uncheckStar(${eList.ratingId}, '')" onclick="javascript: checkClickStar(2, ${eList.ratingId});"></a>
	  <a href="javascript:void(0)" id="star3_${eList.ratingId}" class="star" onmouseover="javascript: checkStar(3, ${eList.ratingId}, '')" onmouseout="javascript: uncheckStar(${eList.ratingId}, '')" onclick="javascript: checkClickStar(3, ${eList.ratingId});"></a>
	  <a href="javascript:void(0)" id="star4_${eList.ratingId}" class="star" onmouseover="javascript: checkStar(4, ${eList.ratingId}, '')" onmouseout="javascript: uncheckStar(${eList.ratingId}, '')" onclick="javascript: checkClickStar(4, ${eList.ratingId});"></a>
	  <a href="javascript:void(0)" id="star5_${eList.ratingId}" class="star" onmouseover="javascript: checkStar(5, ${eList.ratingId}, '')" onmouseout="javascript: uncheckStar(${eList.ratingId}, '')" onclick="javascript: checkClickStar(5, ${eList.ratingId});" style="width:37px;"></a>
	  <br clear="all" />
          <p id="starHints_${eList.ratingId}" class="star-hints">Click a Star Rating</p>
	</div></c:if>
                            <c:if  test = "${eList.contentRating > 0}">
                                <div style="text-align:center;width:197px;margin:0px auto;" id="content_rating">
                                    <a href="javascript:void(0)" style="cursor:default" id="star1_${eList.ratingId}" class="gstar<c:if  test = "${eList.contentRating >= 1}">_o</c:if>"></a>
	  <a href="javascript:void(0)" style="cursor:default" id="star2_${eList.ratingId}" class="gstar<c:if  test = "${eList.contentRating >= 2}">_o</c:if>"></a>
	  <a href="javascript:void(0)" style="cursor:default" id="star3_${eList.ratingId}" class="gstar<c:if  test = "${eList.contentRating >= 3}">_o</c:if>"></a>
	  <a href="javascript:void(0)" style="cursor:default" id="star4_${eList.ratingId}" class="gstar<c:if  test = "${eList.contentRating >= 4}">_o</c:if>"></a>
	  <a href="javascript:void(0)" id="star5_${eList.ratingId}" class="gstar<c:if  test = "${eList.contentRating >= 5}">_o</c:if>" style="width:37px;cursor:default;"></a>
	  <br clear="all" />
	</div>
                            </c:if>
                            </td>
                        <td><c:if  test = "${eList.presentationRating == 0}">
                            <div style="text-align:center;width:197px;margin:0px auto;" id="presentation_rating">
	  <a href="javascript:void(0)" id="pstar1_${eList.ratingId}" class="star" onmouseover="javascript: checkStar(1, ${eList.ratingId}, 'p')" onmouseout="javascript: uncheckStar(${eList.ratingId}, 'p')" onclick="javascript: checkClickStar(1, ${eList.ratingId}, 'p');"></a>
	  <a href="javascript:void(0)" id="pstar2_${eList.ratingId}" class="star" onmouseover="javascript: checkStar(2, ${eList.ratingId}, 'p')" onmouseout="javascript: uncheckStar(${eList.ratingId}, 'p')" onclick="javascript: checkClickStar(2, ${eList.ratingId}, 'p');"></a>
	  <a href="javascript:void(0)" id="pstar3_${eList.ratingId}" class="star" onmouseover="javascript: checkStar(3, ${eList.ratingId}, 'p')" onmouseout="javascript: uncheckStar(${eList.ratingId}, 'p')" onclick="javascript: checkClickStar(3, ${eList.ratingId}, 'p');"></a>
	  <a href="javascript:void(0)" id="pstar4_${eList.ratingId}" class="star" onmouseover="javascript: checkStar(4, ${eList.ratingId}, 'p')" onmouseout="javascript: uncheckStar(${eList.ratingId}, 'p')" onclick="javascript: checkClickStar(4, ${eList.ratingId}, 'p');"></a>
	  <a href="javascript:void(0)" id="pstar5_${eList.ratingId}" class="star" onmouseover="javascript: checkStar(5, ${eList.ratingId}, 'p')" onmouseout="javascript: uncheckStar(${eList.ratingId}, 'p')" onclick="javascript: checkClickStar(5, ${eList.ratingId}, 'p');" style="width:37px;"></a>
	  <br clear="all" />
          <p id="pstarHints_${eList.ratingId}" class="star-hints">Click a Star Rating</p>
	</div>
                        </c:if>
<c:if  test = "${eList.presentationRating > 0}">
                                <div style="text-align:center;width:197px;margin:0px auto;" id="content_rating">
                                    <a href="javascript:void(0)" style="cursor:default" id="star1_${eList.ratingId}" class="gstar<c:if  test = "${eList.presentationRating >= 1}">_o</c:if>"></a>
	  <a href="javascript:void(0)" style="cursor:default" id="star2_${eList.ratingId}" class="gstar<c:if  test = "${eList.presentationRating >= 2}">_o</c:if>"></a>
	  <a href="javascript:void(0)" style="cursor:default" id="star3_${eList.ratingId}" class="gstar<c:if  test = "${eList.presentationRating >= 3}">_o</c:if>"></a>
	  <a href="javascript:void(0)" style="cursor:default" id="star4_${eList.ratingId}" class="gstar<c:if  test = "${eList.presentationRating >= 4}">_o</c:if>"></a>
	  <a href="javascript:void(0)" id="star5_${eList.ratingId}" class="gstar<c:if  test = "${eList.presentationRating >= 5}">_o</c:if>" style="width:37px;cursor:default"></a>
	  <br clear="all" />
	</div>
                            </c:if>        
                        </td>
                            </tr>                            
                </c:forEach>
            </table>

        </div>



       


    </body>
</html>
