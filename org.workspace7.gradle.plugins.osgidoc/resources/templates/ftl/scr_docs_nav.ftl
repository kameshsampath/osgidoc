<div id="scr-doc-nav">
     <table>
 		<#list scrList as scr>
		<#if scr_index % 2 = 0>
		 <tr class="tableRow">
		<#else>
		 <tr class="tableRow alt">
		</#if>
			<td>
				<a href="scr_${scr}.html">${scr}</a>
			</td>
		</tr>
	</#list>
	</table>
</div>