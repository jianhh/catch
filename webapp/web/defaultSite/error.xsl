<?xml version="1.0" encoding="utf-8"?>
<!-- Edited with XML Spy v2007 (http://www.altova.com) -->
<html xsl:version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns="http://www.w3.org/1999/xhtml">
  <head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>页面出错</title>
	<style type="text/css">.fontWhite{color: #FFFFFF;}
	.error{margin:0 auto; text-align:centre; width: 50%;}
	.error img { clear: both; display: block; margin: auto;}
	.error span { font-weight: bold; color: blue}
	</style>
  </head>
  <body >
    <xsl:for-each select="errors/error">
		<div class="error">
			<img src="http://odp.mmarket.com/defaultSite/images/common/button_xtjs.png" width="38" height="38" />
			<div><span>错误码：</span><xsl:value-of select="errorCode"/></div>
			<div><span>结果：</span><xsl:value-of select="errorMessage"/></div>
		</div>
	</xsl:for-each>
  </body>
</html>
