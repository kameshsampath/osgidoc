<#include 'header.ftl'/>
<body>
<#include 'navbar.ftl'/>
<#include 'bundle_info.ftl'/> 
	<table border="1" width="100%" cellpadding="3" cellspacing="0"">
	<#list scrList as scr>
		<tr bgcolor="white" class="TableRowColor">
			<td>
				<a href="scr_${scr}.html">${scr}</a>
			</td>
		</tr>
	</#list>
	</table>
</body>
<#include 'footer.ftl'/>