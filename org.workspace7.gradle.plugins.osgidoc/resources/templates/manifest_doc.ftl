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
<#include 'navbar.ftl'/>
<#include 'bundle_info.ftl'/>
<table>
    <tr class="tableHeading">
        <th colspan="3">MANIFEST</th>
    </tr>
    <tr class="tableRow">
        <td>Bnd-LastModified</td>
        <td>${bndLastModified}</td>
    </tr>
    <tr class="tableRow alt">
        <td>Build-Jdk</td>
        <td>${buildJdk}</td>
    </tr>
    <tr class="tableRow">
        <td>Bundle-ManifestVersion</td>
        <td>${bundleManifestVersion}</td>
    </tr>
    <tr class="tableRow alt">
        <td>Bundle-SymbolicName</td>
        <td>${bundleSymbolicName}</td>
    </tr>
<#if bundleName??>
    <tr class="tableRow">
        <td>Bundle-Name</td>
        <td>${bundleName}</td>
    </tr>
</#if>
<#if bundleCategory??>
    <tr class="tableRow alt">
        <td>Bundle-Category</td>
        <td>${bundleCategory}</td>
    </tr>
</#if>
<#if bundleVendor??>
    <tr class="tableRow">
        <td>BBundle-Vendor</td>
        <td>${bundleVendor}</td>
    </tr>
</#if>
    <tr class="tableRow alt">
        <td>Bundle-Version</td>
        <td>${bundleVersion}</td>
    </tr>
<#if bundleActivator??>
    <tr class="tableRow">
        <td>Bundle-Activator</td>
        <td>${bundleActivator}</td>
    </tr>
</#if>
<#if bundleDocURL??>
    <tr class="tableRow alt">
        <td>Bundle-DocURL</td>
        <td><a
                href="${bundleDocURL}"><#if bundleName??>${bundleName}<#else>${bundleSymbolicName}</#if></a></td>
    </tr>
</#if>
<#if bundleLicense??>
    <tr class="tableRow">
        <td>Bundle-License</td>
        <td>${bundleLicense}</td>
    </tr>
</#if>
    <tr class="tableRow alt">
        <td>Manifest-Version</td>
        <td>${manifestVersion}</td>
    </tr>
    <tr class="tableRow">
        <td>Tool</td>
        <td>${tool}</td>
    </tr>
</table>
<#include 'footer.ftl'/>