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