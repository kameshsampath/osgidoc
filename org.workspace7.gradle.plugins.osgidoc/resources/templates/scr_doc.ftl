<#include 'header.ftl'/>
<body>
<#include 'navbar.ftl'/>
	<div id="content">
		<div id="header">
			<#assign scrService = scr.service>
			<#if scrService??>
				<p>Implementation <b>${scrService.implementation}</b> provides the following services,</p>
				 <ul>
					<#list scrService.interfaces as service>
						<li>${service}</li>
					</#list>
				 </ul>
			</#if>
		</div>
		<br/>
		<#if scr.properties??>
		<div id="compProperties">
			<table border="1" width="100%" cellpadding="3" cellspacing="0">
				<tr bgcolor="#CCCCFF" class="TableHeadingColor">
				<th align="left" colspan="3"><font size="+2"> <b>Service Properties</b></font></th>
				</tr>
				<tr bgcolor="#CCCCFF" class="TableSubHeadingColor">
					<th>Name</th>
					<th>Type</th>
					<th>Value</th>
				</tr>
				<#list scr.properties as scrProp>
					<tr bgcolor="white" class="TableRowColor">
						<td>${scrProp.name}</td>					
						<td>${scrProp.type}</td>						
						<td>${scrProp.value}</td>
					</tr>
				</#list>
			</table>
		</div
		</#if>
	   <br/>
	   <#if scr.references??>
		<div id="compProperties">
			<table border="1" width="100%" cellpadding="3" cellspacing="0">
				<tr bgcolor="#CCCCFF" class="TableHeadingColor">
				<th align="left" colspan="3"><font size="+2"> <b>Refrences</b></font></th>
				</tr>
				<tr bgcolor="#CCCCFF" class="TableSubHeadingColor">
					<th>Name</th>
					<th>Interface</th>
					<th>bind</th>
				</tr>
				<#list scr.references as ref>
					<tr bgcolor="white" class="TableRowColor">
						<td>${ref.name}</td>					
						<td>${ref.refinterface}</td>						
						<td>${ref.bind}</td>
						<#-- TODO other properties-->
					</tr>
				</#list>
			</table>
		</div
		</#if>
	</div>
</body>
<#include 'footer.ftl'/>