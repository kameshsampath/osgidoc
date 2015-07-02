<#include 'header.ftl'/>
<body>
<#include 'navbar.ftl'/>
<#include 'bundle_info.ftl'/>    <#-- Temp PlaceHolder -->
<p>&nbsp;
<table border="1" width="100%" cellpadding="3" cellspacing="0"
       SUMMARY="">
    <tr bgcolor="#CCCCFF" class="TableHeadingColor">
        <th align="left" colspan="2"><font size="+2"> <b>IMPORTS</b></font></th>
    </tr>
    <tr bgcolor="#CCCCFF" class="TableSubHeadingColor">
        <th align="left"><font size="+1"> <b>Package</b></font></th>
        <th align="left"><font size="+1"> <b>Version</b></font></th>
    </tr>
<#if imports??>
    <#assign pkgNames = imports?keys>
    <#list pkgNames as pkgName>
        <tr bgcolor="white" class="TableRowColor">
            <td width="20%"><b>${pkgName}</b></td>
            <#assign pkgVersions = imports[pkgName]>
			<#if pkgVersions?has_content>
          	  <#list pkgVersions as pkgVersion>
                <td>${pkgVersion}</td>
          	  </#list>
			<#else>
				<td>[1.0,&infin;)</td>
			</#if>
        </tr>
    </#list>
</#if>
</table>
</p>
</body>
<#include 'footer.ftl'/>