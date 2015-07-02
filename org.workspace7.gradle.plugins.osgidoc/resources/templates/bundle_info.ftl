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