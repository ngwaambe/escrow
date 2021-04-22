<html>
<style type="text/css">
h4{
 margin-bottom:5px;
}
a{
 color:#000555;
}
</style>
<body style="min-width: 280px; max-width:700px;  width:700px;height: 100%; font-family: 'lucida grande',Tahoma,Geneva,sans-serif; font-size: 13px; line-height: 1.28; text-align: left; color: #555; margin:10px;">
<table style="min-width: 280px; max-width:700px; width:700px;margin: 0 0 30px 0;padding: 10px; background: #fff; font-size: 13px; border:2px solid #ddd;overflow: hidden; margin:10px;">
<tr>
<td style="text-align:right; font-size:20px; font-weight:bold; font-family: 'lucida grande',Tahoma,Geneva,sans-serif;  color: #555;">
<img src="http://www.sicuro.com/javax.faces.resource/logo.png.xhtml?ln=images" alt="sicuro.com" title="sicuro.com">
</d>
</tr>
<tr>
<td>
<#if accountNumber??>
<h4 style="font-family: 'lucida grande',Tahoma,Geneva,sans-serif;padding: 0px;  line-height: 22px; border-bottom:2px solid #ddd; margin:10px;">
Account Number: ${accountNumber}
</h4>
</#if>

<p style="font-family: 'lucida grande',Tahoma,Geneva,sans-serif; font-size: 13px; line-height: 1.28; text-align: left; color: #555; margin:10px;">
 Dear ${name},
 <br/>
 you recently requested a reset of your password for your Sicuro.com account.<br/>
</p>
<p style="font-family: 'lucida grande',Tahoma,Geneva,sans-serif; font-size: 13px; line-height: 1.28; text-align: left; color: #555; margin:10px;">
 The new temporary password for your account is: <strong>${password}</strong><br/>
 <p style="font-family: 'lucida grande',Tahoma,Geneva,sans-serif; font-size: 13px; line-height: 1.28; text-align: left; color: #555; margin:10px;">
 Please login with this temporary password to access your account and sicuro services.
 Subsequently change your password after login by clicking on the link
 <a href="${host}/changePassword.jsf"><b>change password</b></a>
 in your profile page
 </p>

<h4  style="font-family: 'lucida grande',Tahoma,Geneva,sans-serif;padding: 0px;  line-height: 22px; border-bottom:2px solid #ddd; margin:10px;">Your sicuro.com Team</h4>
<table width="100%" style="font-family: 'lucida grande',Tahoma,Geneva,sans-serif; font-size: 13px; line-height: 1.28; text-align: left; color: #555; margin:10px;">
<tr>
<td  style="font-family: 'lucida grande',Tahoma,Geneva,sans-serif;">
sicuro.com GmbH<br/>
Blücherstr. 36<br/>
53115 Bonn
</td>

<td  style="font-family: 'lucida grande',Tahoma,Geneva,sans-serif;">
Email: contact@sicuro.com<br/>
VAT-ID: DE286950232<br/>
District Court Bonn: HRB 19587
</td>

<td  style="font-family: 'lucida grande',Tahoma,Geneva,sans-serif;">
<b>Board of management:</b><br/>
Khatuna Diasamidze<br/>
Stefano Ferracuti
</td>
</tr>
</table>

<h4  style="font-family: 'lucida grande',Tahoma,Geneva,sans-serif;padding: 0px;  line-height: 22px; border-bottom:2px solid #ddd; margin:10px;">Security Statement:</h4>
<p style="font-family: 'lucida grande',Tahoma,Geneva,sans-serif; font-size: 11px; line-height: 1.28; text-align: left; color: #555; margin:10px;">
We encourage our customers to use caution when conducting online transactions, even when using our trusting service.
We only offer our services at sicuro.com. You should make sure your browser shows the sicuro.com URL when logging in to your account.
We are not affiliated with any other trusting service providers, regardless of what others may claim. sicuro.com will
never provide payment instructions or tell you to ship merchandise via email. To avoid being deceived by fraudulent emails,
you should log in to sicuro.com to verify information in any email received. Your sicuro.com password should be different from
other passwords you use. Never share your password with anyone. sicuro.com will never ask you for your password in an email or
over the phone. If you have concerns about the legitimacy of a transaction, or if you receive suspicious information that appears
to conflict with other information you have received, please contact us immediately at:  +49(0)228 38759255 (Germany).
</p>
</td>
</tr>
</table>
</body>
</html>