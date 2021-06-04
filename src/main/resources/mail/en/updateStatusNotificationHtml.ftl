<html>
<style type="text/css">
h4{
 margin-bottom:5px;
 font-family: 'lucida grande',Tahoma,Geneva,sans-serif;padding: 0px;  line-height: 22px; border-bottom:2px solid #ddd; margin:10px;
}
h5{
 margin:0px;
 font-size:13px; font-family: 'lucida grande',Tahoma,Geneva,sans-serif;padding: 0px;  line-height: 22px; border-bottom:1px solid #ddd;
}
a{
 color:#000555;
}
.font{
font-family: 'lucida grande',Tahoma,Geneva,sans-serif;
}
.paragraph{
 font-family: 'lucida grande',Tahoma,Geneva,sans-serif; font-size: 13px; line-height: 1.28; text-align: left; color: #555; margin:10px;
}

.paragraphSmall{
	font-family: 'lucida grande',Tahoma,Geneva,sans-serif; font-size: 11px; line-height: 1.28; text-align: left; color: #555; margin:10px;
}

.header {
 font-family: 'lucida grande',Tahoma,Geneva,sans-serif;padding: 0px;  line-height: 22px; border-bottom:2px solid #ddd; margin:10px;
}
.body{
min-width: 280px; max-width:700px; width:700px; height: 100%; font-family: 'lucida grande',Tahoma,Geneva,sans-serif; font-size: 13px; line-height: 1.28; text-align: left; color: #555; margin:10px;
}
.statusInfo{ background-color:#F2B8CF; font-family: 'lucida grande',Tahoma,Geneva,sans-serif; font-size: 13px; line-height: 18px; text-align: left; color: #555;  margin:10px; padding:6px;border:solid 2px #ddd;
-moz-border-radius-topleft: 3px;
-moz-border-radius-topright:3px;
-moz-border-radius-bottomleft:3px;
-moz-border-radius-bottomright:3px;
-webkit-border-top-left-radius:3px;
-webkit-border-top-right-radius:3px;
-webkit-border-bottom-left-radius:3px;
-webkit-border-bottom-right-radius:3px;
border-top-left-radius:3px;
border-top-right-radius:3px;
border-bottom-left-radius:3px;
border-bottom-right-radius:3px;
}
.statusInfo p,h4{
 margin:0 0 2px;
}

.box-simple{
min-width: 280px; max-width:700px; width:700px; margin: 0 0 30px 0;padding: 10px; background: #fff; font-size: 13px; border:2px solid #ddd;overflow: hidden; margin:10px;
}
.table{
font-family: 'lucida grande',Tahoma,Geneva,sans-serif; font-size: 13px; line-height: 1.28; text-align: left; color: #555; margin:10px;width:100%;
}
.partnerLogo{
 text-align:left; font-size:20px; font-weight:bold; font-family: 'lucida grande',Tahoma,Geneva,sans-serif;  color: #555; padding:10px;
}
.logo{
 text-align:right; font-size:20px; font-weight:bold; font-family: 'lucida grande',Tahoma,Geneva,sans-serif;  color: #555; padding:10px;
}
.status{
font-family: 'lucida grande',Tahoma,Geneva,sans-serif; font-size: 13px; line-height: 1.28; text-align: left; color: #555; margin:10px;
}
</style>
<body class="body" style="min-width: 280px; max-width:700px; width:700px; height: 100%; font-family: 'lucida grande',Tahoma,Geneva,sans-serif; font-size: 13px; line-height: 1.28; text-align: left; color: #555; margin:10px;">
<table class="box-simple" style="min-width: 280px; max-width:700px; width:700px; margin: 0 0 30px 0;padding: 10px; background: #fff; font-size: 13px; border:2px solid #ddd;overflow: hidden; margin:10px;">


<tr>
<#if parnerId??>
<td class="partnerLogo" style="text-align:left; font-size:20px; font-weight:bold; font-family: 'lucida grande',Tahoma,Geneva,sans-serif;  color: #555; padding:10px;">
    <img src="${host}/logos/${parnerId}" alt="sicuro.com" title="sicuro.com">
</td>
</#if>

<td class="logo" style="text-align:right; font-size:20px; font-weight:bold; font-family: 'lucida grande',Tahoma,Geneva,sans-serif;  color: #555; padding:10px;">
<img src="http://www.sicuro.com/javax.faces.resource/logo.png.xhtml?ln=images" alt="sicuro.com" title="sicuro.com">
</td>
</tr>
<tr>
<td colspan="2">
<#if accountNumber??>
<h4 style="font-family: 'lucida grande',Tahoma,Geneva,sans-serif;padding: 0px;  line-height: 22px; border-bottom:2px solid #ddd; margin:10px;">
Account Number: ${accountNumber}
</h4>
</#if>

<div class="status" style="font-family: 'lucida grande',Tahoma,Geneva,sans-serif; font-size: 13px; line-height: 1.28; text-align: left; color: #555; margin:10px;">
<b>${transactionStatus}</b>
</div>

<p class="paragraph" style="font-family: 'lucida grande',Tahoma,Geneva,sans-serif; font-size: 13px; line-height: 1.28; text-align: left; color: #555; margin:10px;">
Dear ${name}
<br/>
You are the participant of the ${transactionStatus}.
We would like to inform you about the latest update of this transaction:
</p>

<div class="statusInfo"  style="background-color:#F2B8CF; font-family: 'lucida grande',Tahoma,Geneva,sans-serif;
font-size: 13px; line-height: 18px; text-align: left; color: #555;  margin:10px; padding:6px;border:solid 2px #ddd;
-moz-border-radius-topleft: 3px;
-moz-border-radius-topright:3px;
-moz-border-radius-bottomleft:3px;
-moz-border-radius-bottomright:3px;
-webkit-border-top-left-radius:3px;
-webkit-border-top-right-radius:3px;
-webkit-border-bottom-left-radius:3px;
-webkit-border-bottom-right-radius:3px;
border-top-left-radius:3px;
border-top-right-radius:3px;
border-bottom-left-radius:3px;
border-bottom-right-radius:3px;">

<h5 style="margin:0 0 2px;">Lates update: ${datemodified}</h5>
${transactionStatusInfo}
<#if transactionExtraInfo??>
<p style="margin:0 0 2px;">${transactionExtraInfo}</p>
</#if>
</div>

<p class="paragraph" style="font-family: 'lucida grande',Tahoma,Geneva,sans-serif; font-size: 13px; line-height: 1.28; text-align: left; color: #555; margin:10px;">
If any actions from your side are required to continue please login in your sicuro.com account at
<a href="${loginlink}"><b>login.</b></a>
</p>

<p class="paragraph" style="font-family: 'lucida grande',Tahoma,Geneva,sans-serif; font-size: 13px; line-height: 1.28; text-align: left; color: #555; margin:10px;">
<b>${firstParticipant}:</b> ${firstParticipantName}<br/>
<b>${firstParticipantEmailLabel}:</b> ${firstParticipantEmail}
</p>

<p class="paragraph" style="font-family: 'lucida grande',Tahoma,Geneva,sans-serif; font-size: 13px; line-height: 1.28; text-align: left; color: #555; margin:10px;">
<b>${secondParticipant}:</b> ${secondParticipantName}<br/>
<b>${secondParticipantEmailLabel}:</b> ${secondParticipantEmail}
</p>

<p class="paragraph" style="font-family: 'lucida grande',Tahoma,Geneva,sans-serif; font-size: 13px; line-height: 1.28; text-align: left; color: #555; margin:10px;">
Thank you for doing your business with us! We hope we can assist you with your future business needs.
</p>

<h4 class="header" style="font-family: 'lucida grande',Tahoma,Geneva,sans-serif;padding: 0px;  line-height: 22px; border-bottom:2px solid #ddd; margin:10px;">Your sicuro.com Team</h4>
<table class="table" style="font-family: 'lucida grande',Tahoma,Geneva,sans-serif; font-size: 13px; line-height: 1.28; text-align: left; color: #555; margin:10px;width:100%;">
<tr>
<td class="font" style="font-family: 'lucida grande',Tahoma,Geneva,sans-serif;">
sicuro.com GmbH<br/>
Blücherstr. 36<br/>
53115 Bonn
</td>

<td  class="font" style="font-family: 'lucida grande',Tahoma,Geneva,sans-serif;">
Email: contact@sicuro.com<br/>
VAT-ID: DE286950232<br/>
District Court Bonn: HRB 19587
</td>

<td  class="font" style="font-family: 'lucida grande',Tahoma,Geneva,sans-serif;">
<b>Board of management:</b><br/>
Khatuna Diasamidze<br/>
Stefano Ferracuti
</td>
</tr>
</table>

<h4  class="header" style="font-family: 'lucida grande',Tahoma,Geneva,sans-serif;padding: 0px;  line-height: 22px; border-bottom:2px solid #ddd; margin:10px;">Security Statement:</h4>
<p class="paragraphSmall" style="font-family: 'lucida grande',Tahoma,Geneva,sans-serif; font-size: 11px; line-height: 1.28; text-align: left; color: #555; margin:10px;">
We encourage our customers to use caution when conducting online transactions, even when using our trusting service.
We only offer our services at sicuro.com. You should make sure your browser shows the sicuro.com URL when logging in to your account.
We are not affiliated with any other trusting service providers, regardless of what others may claim. sicuro.com will
never provide payment instructions or tell you to ship merchandise via email. To avoid being deceived by fraudulent emails,
you should log in to sicuro.com to verify information in any email received. Your sicuro.com password should be different from
other passwords you use. Never share your password with anyone. sicuro.com will never ask you for your password in an email or
over the phone. If you have concerns about the legitimacy of a transaction, or if you receive suspicious information that appears
to conflict with other information you have received, please contact us immediately at: +49(0)228 38759255 (Germany).
</p>
</td></tr>
</table>
</body>
</html>