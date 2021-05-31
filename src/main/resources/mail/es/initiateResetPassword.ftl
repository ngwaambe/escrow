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
Número de cuenta: ${accountNumber}
</h4>
</#if>

<p style="font-family: 'lucida grande',Tahoma,Geneva,sans-serif; font-size: 13px; line-height: 1.28; text-align: left; color: #555; margin:10px;">
 Estimado/a ${name},
 <br/>
 Has pedido recientemente restablecer tu contraseña en tu cuenta de sicuro.com.<br/>
</p>
<p  style="font-family: 'lucida grande',Tahoma,Geneva,sans-serif; font-size: 13px; line-height: 1.28; text-align: left; color: #555; margin:10px;">
 Para estar seguros de que eres titular de esta cuenta por favor entra en el siguiente
 <a href="${link}"><b>enlace</b></a>.  Una vez hecho tendrás que responder a tu pregunta de seguridad que elegiste al darte de alta con nosotros.
    Una vez te hayas identificado como titular de la cuenta recibirás un email con la información necesaria para restablecer tu contraseña.
 <p  style="font-family: 'lucida grande',Tahoma,Geneva,sans-serif; font-size: 13px; line-height: 1.28; text-align: left; color: #555; margin:10px;">
 En caso de que al pinchar sobre el enlace, éste no funcione, copia y pégalo en tu navegador web.<br/>
 ${link}
 </p>


<p class="paragraph" style="font-family: 'lucida grande',Tahoma,Geneva,sans-serif; font-size: 13px; line-height: 1.28; text-align: left; color: #555; margin:10px;">
Muchas gracias por tu confianza en nosotros. Esperamos poder asistirte muy pronto en futuras transacciones. 
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
</td>
</tr>
</table>
</body>
</html>