
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%
    String contextPath = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + contextPath + "/";
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:HRMS:</title>
        <link rel="stylesheet" type="text/css" href="css/hrmis.css"/>
        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css"/>
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css"/>
        
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
        <script type="text/javascript" src="js/ckeditor/ckeditor.js"></script>
        
        <script type="text/javascript">
            $(document).ready(function() {
                //$('#sltEmpId').combobox('reload','getEmpList.htm?HRMSID='+$('hidAnnx3HrmsId').val);
                $("#rule15Articles").attr('maxlength','98');
            });
            
            function saveCharge(){
                var article=$('#rule15Articles').val();
                if(article == ''){
                    alert("Please enter Articles of Charge");
                    return false;
                }
                if(article.length >=98){
                    alert("Please Enter value between 1 and 98");
                    return false;
                }
                
                
                var chrgDtls = CKEDITOR.instances.rule15ChargDtls.getData();
                if(chrgDtls.length >=995){
                    alert("Please Enter value between 0 and 998");
                    return false;
                }
                
                
                var doc=$('#rule15Document').val(); 
                var subDoc=$('#rule15SubDoc').val();
                if(doc == '' && subDoc == ''){
                    alert("Please Upload a Document or Substantiation Document");
                    return false;
                }else if(doc != '' && subDoc == ''){
                    var ext = $('#rule15Document').val().split('.').pop().toLowerCase();
                    var filesize = $("#rule15Document")[0].files[0].size;
                    if ($.inArray(ext, ['pdf']) == -1) {
                        alert('Pdf file only!');
                        return false;
                    }
                    if (filesize > 5242880) {
                        alert('File Size cannot be exceeded 5 MB!');
                        return false;
                    }
                }else if(doc == '' && subDoc != ''){
                    var ext = $('#rule15SubDoc').val().split('.').pop().toLowerCase();
                    var filesize = $("#rule15SubDoc")[0].files[0].size;
                    if ($.inArray(ext, ['pdf']) == -1) {
                        alert('Pdf file only!');
                        return false;
                    }
                    if (filesize > 5242880) {
                        alert('File Size cannot be exceeded 5 MB!');
                        return false;
                    }
                }else if(doc != '' && subDoc != ''){
                    
                    var ext = $('#rule15Document').val().split('.').pop().toLowerCase();
                    var filesize = $("#rule15Document")[0].files[0].size;
                    if ($.inArray(ext, ['pdf']) == -1) {
                        alert('Pdf file only!');
                        return false;
                    }
                    if (filesize > 5242880) {
                        alert('File Size cannot be exceeded 5 MB!');
                        return false;
                    }
                    
                    var ext1 = $('#rule15SubDoc').val().split('.').pop().toLowerCase();
                    var filesize1 = $("#rule15SubDoc")[0].files[0].size;
                    if ($.inArray(ext1, ['pdf']) == -1) {
                        alert('Pdf file only!');
                        return false;
                    }
                    if (filesize1 > 5242880) {
                        alert('File Size cannot be exceeded 5 MB!');
                        return false;
                    }
                }
            }    
            

        </script>
        <style type="text/css">
        .star{
            color:#FF0000;
            font-size:15px;
        }
        </style>
        
    </head>
    <body>
        <form action="saveRule15Charges.htm" method="POST" commandName="Rule15ChargeBean" enctype="multipart/form-data">
            <input type="hidden" name="hidChargeDaid" id="hidChargeDaid" value='${newChrgValue.daid}'/>
            <input type="hidden" name="hidChargeDoHrmsId" id="hidChargeDoHrmsId" value='${newChrgValue.doHrmsId}'/>
            <input type="hidden" name="hidChargeDacId" id="hidChargeDacId" value='${newChrgValue.dacId}'/>
            <input type="hidden" name="hidOffCode2" id="hidOffCode2" value='${newChrgValue.hidOffCode1}'/>
                                
            <div align="center" width="99%" style="margin-top:5px;margin-bottom:10px;">
                <div align="center" class="easyui-panel" title="Add Charge" width="99%" style="min-height: 90%;">
                    <table height="60%" width="100%" border="0" cellspacing="0" style="font-family: Verdana, Arial, Helvetica, sans-serif;font-size: 13px;border: 1px solid #81A2D6;">
                        <tr>
                            <td style="font-weight: bold;padding-left: 10px;"><span class="star">*</span>Articles of Charge</td>
                            <td><input type="text" name="rule15Articles" id="rule15Articles" class="easyui-validatebox"                                        
                                       data-options="required:true,validType:'length[1,98]'" value="${editChrgValue.rule15Articles}" style="width:70%;border:1px solid #000000;border-radius: 0.3em;"/>
                            </td>
                        </tr>
                        <tr>
                            <td style="font-weight: bold;padding-left: 10px;">Statement of Imputation</td>
                            <td>
                                <textarea name="rule15ChargDtls" id="rule15ChargDtls" style="width:400px;height:200px;border:1px solid #000000;">
                                              ${editChrgValue.rule15ChargDtls}
                                </textarea>
                                <script>
                                    CKEDITOR.replace('rule15ChargDtls', {
                                            //customConfig: 'custom/ckeditor_config.js'
                                    });
                                </script>
                            </td>
                        </tr>
                        
                        
                        <c:if test="${empty editChrgValue}">
                            <tr>
                                <td style="font-weight: bold;padding-left: 10px;"><span class="star">*</span>Document</td>
                                <td><input type="file" name="rule15Document" id="rule15Document" accept="pdf" style="width:55%;border:1px solid #000000;"/> (PDF Only)</td>
                            </tr>
                            <tr>
                                <td style="font-weight: bold;padding-left: 10px;">Substantiation<br/>(If no Document Exist)</td>
                                <td><input type="file" name="rule15SubDoc" id="rule15SubDoc" accept="pdf" style="width:55%;border:1px solid #000000;"/> (PDF Only)</td>
                            </tr>
                            
                        </c:if>
                        <c:if test="${not empty editChrgValue}">
                            <c:forEach var="eachDoc" items="${editChrgValue.documentList}">
                                <c:if test="${not empty eachDoc.docType}">
                                    <c:if test="${eachDoc.docType == 'D'}">
                                        <tr>
                                            <td style="font-weight: bold;padding-left: 10px;"><span class="star">*</span>Document</td>
                                            <td><c:out value="${eachDoc.orgFileName}"/></td>
                                        </tr>
                                    </c:if>
                                     <c:if test="${eachDoc.docType != 'D'}">
                                        <tr>
                                            <td style="font-weight: bold;padding-left: 10px;"><span class="star">*</span>Document</td>
                                            <td><input type="file" name="rule15Document" id="rule15Document" style="width:55%;border:1px solid #000000;"/></td>
                                        </tr>
                                    </c:if>
                                        
                                    <c:if test="${eachDoc.docType == 'S'}">
                                        <tr>
                                            <td style="font-weight: bold;padding-left: 10px;">Substantiation<br/>(If no Document Exist)</td>
                                            <td><c:out value="${eachDoc.orgFileName}"/></td>
                                        </tr>
                                    </c:if>
                                    <c:if test="${eachDoc.docType != 'S'}">
                                        <tr>
                                            <td style="font-weight: bold;padding-left: 10px;">Substantiation<br/>(If no Document Exist)</td>
                                            <td><input type="file" name="rule15SubDoc" id="rule15SubDoc" style="width:55%;border:1px solid #000000;"/></td>
                                        </tr>
                                    </c:if>
                                </c:if>
                                        
                                <c:if test="${empty eachDoc.docType}">
                                    inside the document is empty blk if
                                    <tr>
                                        <td style="font-weight: bold;padding-left: 10px;"><span class="star">*</span>Document</td>
                                        <td><input type="file" name="rule15Document" id="rule15Document" style="width:55%;border:1px solid #000000;"/></td>
                                    </tr>
                                    <tr>
                                        <td style="font-weight: bold;padding-left: 10px;">Substantiation<br/>(If no Document Exist)</td>
                                        <td><input type="file" name="rule15SubDoc" id="rule15SubDoc" style="width:55%;border:1px solid #000000;"/></td>
                                    </tr>
                                </c:if>
                            </c:forEach>
                        </c:if>    
                    </table>
                </div>
            </div>

            <div align="center" width="99%" style="margin-top:5px;margin-bottom:10px;">
                <div align="center" class="easyui-panel" width="99%">
                    <table border="0" width="100%">
                        <tr>
                            <td width="50%" align="left" style="padding-left:20px;">
                                <input type="submit" name="rule15AddCharge" value="Back" class="easyui-linkbutton" data-options="iconCls:'icon-remove'"/>
                            </td>
                            <td width="50%" align="right" style="padding-right:20px;">
                                <c:if test="${not empty UpdBtn}">
                                    <input type="submit" name="rule15AddCharge" onclick="return saveCharge()" value="Update" class="easyui-linkbutton" data-options="iconCls:'icon-add'"/>
                                </c:if>
                                <c:if test="${empty UpdBtn}">
                                    <input type="submit" name="rule15AddCharge" value="Save" class="easyui-linkbutton" 
                                           onclick="return saveCharge()" data-options="iconCls:'icon-add'"/>
                                </c:if>    
                            </td>
                        </tr>
                    </table>
                </div>
            </div>

        </form>
    </body>
</html>
