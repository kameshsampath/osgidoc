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
        <th colspan="2">IMPORTS</th>
    </tr>
    <tr class="tableSubHeading">
        <th>Package</th>
        <th>Version</th>
    </tr>
<#if imports??>
    <#assign pkgNames = imports?keys>
    <#list pkgNames as pkgName>
            <#if pkgName_index % 2 = 0 >
       		   <tr class="tableRow">
		     <#else>
			    <tr class="tableRow alt">
		    </#if>
			<td>${pkgName}</td>
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
<#include 'footer.ftl'/>