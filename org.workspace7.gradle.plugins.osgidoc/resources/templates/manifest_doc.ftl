<#--
* Copyright 2015-present Kamesh Sampath<kamesh.sampath@hotmail.com)
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*
-->
<#include 'header.ftl'/>
<body>
<#include 'navbar.ftl'/>
<#include 'bundle_info.ftl'/>
<p>&nbsp;
<table border="1" width="100%" cellpadding="3" cellspacing="0"
       SUMMARY="">
    <tr bgcolor="#CCCCFF" class="TableHeadingColor">
        <th align="left" colspan="2"><font size="+2"> <b>MANIFEST</b></font></th>
    </tr>
    <tr bgcolor="white" class="TableRowColor">
        <td width="20%"><b>Bnd-LastModified</b></td>
        <td>${bndLastModified}</td>
    </tr>
    <tr bgcolor="white" class="TableRowColor">
        <td width="20%"><b>Build-Jdk</b></td>
        <td>${buildJdk}</td>
    </tr>
    <tr bgcolor="white" class="TableRowColor">
        <td width="20%"><b>Bundle-ManifestVersion</b></td>
        <td>${bundleManifestVersion}</td>
    </tr>
    <tr bgcolor="white" class="TableRowColor">
        <td width="20%"><b>Bundle-SymbolicName</b></td>
        <td>${bundleSymbolicName}</td>
    </tr>
<#if bundleName??>
    <tr bgcolor="white" class="TableRowColor">
        <td width="20%"><b>Bundle-Name</b></td>
        <td>${bundleName}</td>
    </tr>
</#if>
<#if bundleCategory??>
    <tr bgcolor="white" class="TableRowColor">
        <td width="20%"><b>Bundle-Category</b></td>
        <td>${bundleCategory}</td>
    </tr>
</#if>
<#if bundleVendor??>
    <tr bgcolor="white" class="TableRowColor">
        <td width="20%"><b>BBundle-Vendor</b></td>
        <td>${bundleVendor}</td>
    </tr>
</#if>
    <tr bgcolor="white" class="TableRowColor">
        <td width="20%"><b>Bundle-Version</b></td>
        <td>${bundleVersion}</td>
    </tr>
<#if bundleActivator??>
    <tr bgcolor="white" class="TableRowColor">
        <td width="20%"><b>Bundle-Activator</b></td>
        <td>${bundleActivator}</td>
    </tr>
</#if>
<#if bundleDocURL??>
    <tr bgcolor="white" class="TableRowColor">
        <td width="20%"><b>Bundle-DocURL</b></td>
        <td><a
                href="${bundleDocURL}"><#if bundleName??>${bundleName}<#else>${bundleSymbolicName}</#if></a></td>
    </tr>
</#if>
<#if bundleLicense??>
    <tr bgcolor="white" class="TableRowColor">
        <td width="20%"><b>Bundle-License</b></td>
        <td>${bundleLicense}</td>
    </tr>
</#if>
    <tr bgcolor="white" class="TableRowColor">
        <td width="20%"><b>Manifest-Version</b></td>
        <td>${manifestVersion}</td>
    </tr>
    <tr bgcolor="white" class="TableRowColor">
        <td width="20%"><b>Tool</b></td>
        <td>${tool}</td>
    </tr>
</table>
</p>
</body>
<#include 'footer.ftl'/>