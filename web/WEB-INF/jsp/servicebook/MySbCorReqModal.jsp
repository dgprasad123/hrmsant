<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<style>
    .panel-body{padding-bottom:0px ;}
    .form-control{height:29px;}
    .pageHeadingTxt{
        background-color: #DDEDE0;
        font-weight: bold;
        text-transform: uppercase;
        text-align: center;
        color:#000;
        font-size:18px;
        line-height: 20px;
        position: relative;
        letter-spacing: 0.2px;
        padding: 10px 0px;
    }
    .pageSubHeading{
        background-color: #DDEDE0;
        font-weight: bold;
        text-transform: uppercase;
        text-align: center;
        color:#0D8CE7;
        font-size:17px;
        line-height: 20px;
        position: relative;
        letter-spacing: 0.2px;
        padding: 10px 0px;
    }
    .star {
        color: #FF0000;
        font-size: 17px;
    }
    .imgStyle{
        border-radius: 50%;
        border:1px solid #a3a183;
        padding:3px;
        width: 150px;
        -moz-box-shadow: 0px 6px 5px #ccc;
        -webkit-box-shadow: 0px 6px 5px #ccc;
        box-shadow: 0px 6px 5px #ccc;
    }
</style>
<input type="hidden" name="hidNotId" value="${notid}"/>
<input type="hidden" name="hidNotType" value="${nottype}"/>
<div class="form-w3layouts">
    <div class="row">
        <div class="col-lg-12">
            <section class="panel" style="padding-bottom: 5px;">
                <header class="pageSubHeading"> Current Data </header>
                <div class="panel-body">
                    <div class="col-md-12 form-group"> ${SbCorReqBean.sbDescription} </div>
                    <span style="display:none;">
                        <textarea name="previousSBLanguage">${SbCorReqBean.sbDescription}</textarea>
                    </span>
                </div>
            </section>  
        </div>
    </div>

    <div class="row">
        <div class="col-lg-12">
            <section class="panel" style="padding-bottom: 10px;">
                <header class="pageSubHeading">Request for Update</header>
                <div class="panel-body">                    
                    <textarea name="sbLanguageRequested" rows="10" cols="110" style="resize: none;">${SbCorReqBean.sbDescription}</textarea>
                </div>  
            </section>  
        </div>
    </div>
</div>

