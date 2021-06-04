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
<h4 class="header" style="font-family: 'lucida grande',Tahoma,Geneva,sans-serif;padding: 0px;  line-height: 22px; border-bottom:2px solid #ddd; margin:10px;">
Account Numero: ${accountNumber}
</h4>
</#if>

<div class="status"  style="font-family: 'lucida grande',Tahoma,Geneva,sans-serif; font-size: 13px; line-height: 1.28; text-align: left; color: #555; margin:10px;">
<b>${transactionStatus}</b>
</div>

<p class="paragraph" style="font-family: 'lucida grande',Tahoma,Geneva,sans-serif; font-size: 13px; line-height: 1.28; text-align: left; color: #555; margin:10px;">
Gentile ${name}
<br/>
Stai partecipando alla "${transactionStatus}". Ti segnaliamo l’ultimo aggionamento:
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
<h5 style="margin:0 0 2px;">Ultimo Aggiornamento: ${datemodified}</h5>
${transactionStatusInfo}
<#if transactionExtraInfo??>
<p style="margin:0 0 2px;">${transactionExtraInfo}</p>
</#if>
</div>

<p class="paragraph" style="font-family: 'lucida grande',Tahoma,Geneva,sans-serif; font-size: 13px; line-height: 1.28; text-align: left; color: #555; margin:10px;">
Se per procedere nella transazione è richiesta la tua collaborazione, ti preghiamo di effettuare il
<a href="${loginlink}"><b>login</b></a> sul tuo account di sicuro.com.
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
Grazie per aver scelto di utilizzare il nostro servizio! Speriamo di poterti assistere ancora in futuro.
</p>

<h4 class="header" style="font-family: 'lucida grande',Tahoma,Geneva,sans-serif;padding: 0px;  line-height: 22px; border-bottom:2px solid #ddd; margin:10px;">Il Team di sicuro.com</h4>
<table class="table" style="font-family: 'lucida grande',Tahoma,Geneva,sans-serif; font-size: 13px; line-height: 1.28; text-align: left; color: #555; margin:10px;width:100%;">
<tr>
<td  class="font" style="font-family: 'lucida grande',Tahoma,Geneva,sans-serif;">
sicuro.com GmbH<br/>
Blücherstr. 36<br/>
53115 Bonn
</td>

<td  class="font" style="font-family: 'lucida grande',Tahoma,Geneva,sans-serif;">
Email: contact@sicuro.com<br/>
P.IVA: DE286950232<br/>
Tribunale distrettuale Bonn: HRB 19587
</td>

<td  class="font" style="font-family: 'lucida grande',Tahoma,Geneva,sans-serif;">
<b>Consiglio di Amministrazione:</b><br/>
Khatuna Diasamidze<br/>
Stefano Ferracuti
</td>
</tr>
</table>

<h4  class="header" style="font-family: 'lucida grande',Tahoma,Geneva,sans-serif;padding: 0px;  line-height: 22px; border-bottom:2px solid #ddd; margin:10px;">Informazioni di Sicurezza:</h4>
<p class="paragraphSmall" style="font-family: 'lucida grande',Tahoma,Geneva,sans-serif; font-size: 11px; line-height: 1.28; text-align: left; color: #555; margin:10px;">
Raccomandiamo ai nostri utenti la massima attenzione durante le transazioni online, anche quando si utilizza il nostro
servizio di garanzia. I nostri servizi vengono offerti soltanto all’indirizzo di sicuro.com. Sei pregato di assicurarti
di visualizzare sul tuo browser l'URL di sicuro.com quando effettui l'accesso al tuo account. Non abbiamo nessun partner
affiliato nella gestione dei nostri servizi, a prescindere da ciò che altri possano affermare. Sicuro.com non vi
chiederà mai tramite e-mail, di fornire istruzioni di pagamento o di inviare la vostra merce. Incoraggiamo i nostri
utenti ad effettuare il login e verificare ogni email ricevuta da sicuro.com per evitare di essere ingannati da e-mail
fraudolente. La password di sicuro.com dovrebbe essere diversa dalle altre password che utilizzi. Non condividere mai
la tua password con nessuno. Sicuro.com non ti chiederà mai di rivelare la tua password via e-mail o telefonicamente.
Se hai dubbi riguardo la legittimità di una transazione, o se ricevi informazioni sospette che ti sembrano in conflitto
con le informazioni in tuo possesso, sei pregato di contattarci immediatamente al numero:
 +49(0)228 38759255 (Germania).
</p>
</td></tr>
</table>
</body>
</html>