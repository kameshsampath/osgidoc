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

<div id="osgidoc-content" data-senna-surface>
  
  <div id="osgidoc-content-default">
	 
		<!-- HEADER and DESCRIPTION -->

        <div id="osgidoc-content-header" class="osgidoc-header">
           <p> 
			 <#if bundleName??>
                <span class="osgidoc-title">${bundleName}</span>
			<#else>
				<span class="osgidoc-title">${bundleSymbolicName}</span>
			</#if>
             <span class="osgidoc-version">${bundleVersion}</span>
           </p>
        </div>
   		
        <section id="osgidoc-content-header-desc">
		  <h1>Description:</h1>
		 <#if bundleDescription??>
           <p  class="osgidoc-description"> 
             <span>${bundleDescription}<span>
           <p>        
		 </#if>
        </section>
 <p>&nbsp;</p>
