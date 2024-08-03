
<%@ page contentType="text/html;charset=windows-1252"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252"/>
        <title>Change Password</title>
        <link href="resources/css/bootstrap.min.css" rel="stylesheet">
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>


        <script language="JavaScript" type="text/javascript">

        </script>
    </head>
    <body style="padding:0px;">
        <form action="ChangePasswordAction.htm" data-toggle="validator" role="form">
           
            <div style="padding:10px;margin-top:100px;margin-left:50px;background:#E5F0C9;">
                <div align="center" style="color: red;"><span>${message}  </span></div>
                <div style="width:100%;"> 
                    <table id="dg" style="width:90%;">
                        <tr>
                            <td>Current Password:</td>
                            <td>
                                <input type="password" name="userPassword" id="userPassword" class="input-block-level" placeholder="Input Current Password" required=""/>
                            </td>
                        </tr>
                        <tr>
                            <td>New Password:</td>
                            <td><input type="password" name="newpassword" id="newpassword" class="input-block-level" placeholder="Input New Password" data-minlength="8" required/> 
                                
                            </td>
                        </tr>
                        <tr>
                            <td>Confirm Password:</td>
                            <td>
                                <input type="password" name="confirmpassword" id="confirmpassword" class="input-block-level" placeholder="Retype New Password" required data-match="#newpassword" data-match-error="Whoops, these don't match"/>
                            </td>
                        </tr>



                        <tr>
                            <td>  &nbsp;</td>
                            <td><input class="btn btn-primary" type="submit" name="submit" value="Change"/>  </td>
                        </tr>
                        <tr><td colspan="2"><span class="help-block" style="color: red;">password policy to match 8 characters with alphabets in combination with numbers and special characters. e.g Welcome@12</span></td></tr>
                    </table>

                </div>
            </div>
        </form>


    </body>
</html>