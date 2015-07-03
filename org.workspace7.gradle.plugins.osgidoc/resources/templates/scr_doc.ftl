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
<#include 'scr_docs_nav.ftl'>
<div id="scr-doc-content">
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
			<table>
				<tr class="tableHeading">
				<th colspan="3">Service Properties</b></th>
				</tr>
				<tr class="tableSubHeading">
					<th>Name</th>
					<th>Type</th>
					<th>Value</th>
				</tr>
				<#list scr.properties as scrProp>
					<#if scrProp_index % 2 = 0>
					  <tr class="tableRow">
					<#else>
					  <tr class="tableRow alt">
					</#if>
						<td>${scrProp.name}</td>					
						<td>${scrProp.type}</td>						
						<td>${scrProp.value}</td>
					</tr>
				</#list>
			</table>
		</div>
		</#if>
	   <br/>
	   <#if scr.references??>
		<div id="compProperties">
			<table>
				<tr class="tableHeading">
				<th colspan="3">Refrences</th>
				</tr>
				<tr class="tableSubHeading">
					<th>Name</th>
					<th>Interface</th>
					<th>bind</th>
				</tr>
				<#list scr.references as ref>
					<#if ref_index % 2 = 0>
					  <tr class="tableRow">
					<#else>
					  <tr class="tableRow alt">
					</#if>
						<td>${ref.name}</td>					
						<td>${ref.refinterface}</td>						
						<td>${ref.bind}</td>
						<#-- TODO other properties-->
					</tr>
				</#list>
			</table>
		</div>
	</#if>
</div>
<#include 'footer.ftl'/>