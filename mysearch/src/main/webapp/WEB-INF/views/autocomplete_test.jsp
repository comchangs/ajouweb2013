<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="keywords" content="" />
<meta name="description" content="" />
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>My Search</title>
<link href="${pageContext.request.contextPath}/resources/css/style.css" rel="stylesheet" type="text/css" media="screen" />
<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
  <script src="http://code.jquery.com/jquery-1.9.1.js"></script>
  <script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
  <style>
  .column { width: 400px; float: left; padding-bottom: 100px; }
  .portlet { margin: 0 1em 1em 0; }
  .portlet-header { margin: 0.3em; padding-bottom: 4px; padding-left: 0.2em; }
  .portlet-header .ui-icon { float: right; }
  .portlet-content { padding: 0.4em; }
  .ui-sortable-placeholder { border: 1px dotted black; visibility: visible !important; height: 50px !important; }
  .ui-sortable-placeholder * { visibility: hidden; }
  </style>
  <script>
  $(function() {
    $( ".column" ).sortable({
      connectWith: ".column"
    });
 
    $( ".portlet" ).addClass( "ui-widget ui-widget-content ui-helper-clearfix ui-corner-all" )
      .find( ".portlet-header" )
        .addClass( "ui-widget-header ui-corner-all" )
        .prepend( "<span class='ui-icon ui-icon-minusthick'></span>")
        .end()
      .find( ".portlet-content" );
 
    $( ".portlet-header .ui-icon" ).click(function() {
      $( this ).toggleClass( "ui-icon-minusthick" ).toggleClass( "ui-icon-plusthick" );
      $( this ).parents( ".portlet:first" ).find( ".portlet-content" ).toggle();
    });
 
    $( ".column" ).disableSelection();
  });
  </script>
  <script type="text/javascript">
 $(function() {
  $( "#searchKeyword" ).autocomplete({
   minLength: 1,
   source: function( request, response ) {
    $.ajax({
     url: "autocomplete",
     dataType: "json",
     data: "keyword=" + $("#searchKeyword").val(),
     success: function( data ) {
      response( $.map( data, function( item ) {
       return {
        label: item.label,
        value: item.value
       };
      }));
     }
    });
   },
   focus: function( event, ui )
   {
    $('#searchKeyword').val( ui.item.label );
    return false;
   },
  });
 });
 </script>
</head>
<body>
<div id="wrapper">
	<div id="menu-wrapper">
	</div>
	<div id="logo" class="container">
		<h1><a href="#">My Search</a></h1>
		<form method="get" action="SearchResult">
				<input type="text" id="searchKeyword" name="searchKeyword" value="${searchKeyword }" />
				<input type="submit" value="Search"/>
			</form>
	</div>
	<div id="page" class="container">
		<div id="content">
			<div class="column">
 
  <div class="portlet">
    <div class="portlet-header">Feeds</div>
    <div class="portlet-content">Lorem ipsum dolor sit amet, consectetuer adipiscing elit</div>
  </div>
 
  <div class="portlet">
    <div class="portlet-header">News</div>
    <div class="portlet-content">Lorem ipsum dolor sit amet, consectetuer adipiscing elit</div>
  </div>
 
</div>
 
<div class="column">
 
  <div class="portlet">
    <div class="portlet-header">Shopping</div>
    <div class="portlet-content">Lorem ipsum dolor sit amet, consectetuer adipiscing elit</div>
  </div>
 
</div>
 
<div class="column">
 
  <div class="portlet">
    <div class="portlet-header">Links</div>
    <div class="portlet-content">Lorem ipsum dolor sit amet, consectetuer adipiscing elit</div>
  </div>
 
  <div class="portlet">
    <div class="portlet-header">Images</div>
    <div class="portlet-content">Lorem ipsum dolor sit amet, consectetuer adipiscing elit</div>
  </div>
 
</div>
			<div style="clear: both;">&nbsp;</div>
		</div>
	</div>

</div>
<div id="footer">
	<p>� 2013 Ajou Webprogramming - Project team MySearch. All rights reserved.</p>
</div>
</body>
</html>