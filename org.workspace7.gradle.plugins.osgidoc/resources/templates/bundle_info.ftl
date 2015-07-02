<hr>
<center>
<#if bundleName??>
    <h1>${bundleName}-${bundleVersion}</h1>
<#else>
    <h1>${bundleSymbolicName}-${bundleVersion}</h1>
</#if>
</center>
<div style="text-align: center;">&nbsp;</div>
<p style="text-align: justify;">
<#if bundleDocURL??>
    <a
            href="${bundleDocURL}"
            target="_top"><em>${bundleSymbolicName}</em></a>
<#else>
    <em>${bundleSymbolicName}</em>
</#if>
</p>
<#if bundleDescription??>
<p style="text-align: justify;">${bundleDescription}</p>
</#if>