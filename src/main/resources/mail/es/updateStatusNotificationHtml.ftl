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
</d>
</tr>
<tr>
<td colspan="2">
<#if accountNumber??>
<h4 style="font-family: 'lucida grande',Tahoma,Geneva,sans-serif;padding: 0px;  line-height: 22px; border-bottom:2px solid #ddd; margin:10px;">
Número de cuenta: ${accountNumber}
</h4>
</#if>

<div class="status" style="font-family: 'lucida grande',Tahoma,Geneva,sans-serif; font-size: 13px; line-height: 1.28; text-align: left; color: #555; margin:10px;">
<b>${transactionStatus}</b>
</div>

<p class="paragraph" style="font-family: 'lucida grande',Tahoma,Geneva,sans-serif; font-size: 13px; line-height: 1.28; text-align: left; color: #555; margin:10px;">
Estimado/a ${name}
<br/>
Eres participante de la transacción ${transactionStatus}. Queremos informarte sobre las últimas novedades de la misma:
</p>

<div class="statusInfo"
style="background-color:#F2B8CF; font-family: 'lucida grande',Tahoma,Geneva,sans-serif; font-size: 13px; line-height: 18px; text-align: left; color: #555;  margin:10px; padding:6px;border:solid 2px #ddd;
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
Si se requiere que realices alguna acción para poder continuar con la transacción accede a tu cuenta de sicuro.com en
<a href="${loginlink}"><b>iniciar sesión.</b></a>
</p>

<p class="paragraph" style="font-family: 'lucida grande',Tahoma,Geneva,sans-serif; font-size: 13px; line-height: 1.28; text-align: left; color: #555; margin:10px;">
<b>${buyer}:</b> ${buyerName}<br/>
<b>${buyerEmailLabel}:</b> ${buyerEmail}
</p>

<p class="paragraph" style="font-family: 'lucida grande',Tahoma,Geneva,sans-serif; font-size: 13px; line-height: 1.28; text-align: left; color: #555; margin:10px;">
<b>${seller}:</b> ${sellerName}<br/>
<b>${sellerEmailLabel}:</b> ${sellerEmail}
</p>

<p class="paragraph" style="font-family: 'lucida grande',Tahoma,Geneva,sans-serif; font-size: 13px; line-height: 1.28; text-align: left; color: #555; margin:10px;">
Muchas gracias por usar sicuro.com. Esperamos poder asistirte muy pronto en tus compras y ventas online
</p>
<h4  class="header" style="font-family: 'lucida grande',Tahoma,Geneva,sans-serif;padding: 0px;  line-height: 22px; border-bottom:2px solid #ddd; margin:10px;">Tu equipo de sicuro.com</h4>
<table class="table">
<tr>
<td  class="font" style="font-family: 'lucida grande',Tahoma,Geneva,sans-serif;">
sicuro.com GmbH<br/>
Blücherstr. 36<br/>
53115 Bonn
</td>

<td  class="font" style="font-family: 'lucida grande',Tahoma,Geneva,sans-serif;">
Email: contact@sicuro.com<br/>
N° IVA: DE286950232<br/>
Tribunal de distrito de Bonn: HRB 19587
</td>

<td  class="font" style="font-family: 'lucida grande',Tahoma,Geneva,sans-serif;">
<b>Junta Directiva:</b><br/>
Khatuna Diasamidze<br/>
Stefano Ferracuti
</td>
</tr>
</table>

<h4  class="header" style="font-family: 'lucida grande',Tahoma,Geneva,sans-serif;padding: 0px;  line-height: 22px; border-bottom:2px solid #ddd; margin:10px;">Declaración de Seguridad:</h4>
<p class="paragraphSmall" style="font-family: 'lucida grande',Tahoma,Geneva,sans-serif; font-size: 11px; line-height: 1.28; text-align: left; color: #555; margin:10px;">
Animamos a nuestros clientes a tener precaución al realizar transacciones online, aunque utilice nuestro servicio de confianza.
Nosotros sólo ofrecemos nuestros servicios en sicuro.com. Usted debe asegurarse de que su navegador muestra la URL de sicuro.com al
iniciar sesión en su cuenta. No estamos afiliados con cualquier otro proveedor de servicios fiduciario, independientemente de lo que
otros puedan reclamar. sicuro.com nunca proporcionará las instrucciones de pago o pedirte que envíes la mercancía a través de correo
electrónico. Para evitar ser engañados por correos electrónicos fraudulentos, debes iniciar sesión en sicuro.com para verificar la
información de cualquier correo electrónico recibido. Tu contraseña de sicuro.com debe ser diferente de otras contraseñas que utilizas.
Nunca compartas tu contraseña con nadie. sicuro.com nunca te pedirá tu contraseña en un correo electrónico o por teléfono. Si tienes
preocupaciones acerca de la legitimidad de la transacción, o si recibe información sospechosa que entra en conflicto con otra información
que has recibido, por favor ponte en contacto con nosotros de inmediato al: +49 (0) 228 38759255 (Alemania).
</p>
</td></tr>
</table>
</body>
</html>