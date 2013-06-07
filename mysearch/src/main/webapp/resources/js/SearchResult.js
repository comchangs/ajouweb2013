/*		$(document).ready(function() {
			if("${numFound }" > 1)
				$("#DivPaging").paginate({
					count 		: Math.ceil("${numFound }"/10),
					start 		: Math.floor("${start}"/10) + 1,
					display     : 10,
					border					: true,
					border_color			: '#BEF8B8',
					text_color  			: '#68BA64',
					background_color    	: '#E3F2E1',	
					border_hover_color		: '#68BA64',
					text_hover_color  		: 'black',
					background_hover_color	: '#CAE6C6', 
					rotate      : false,
					images		: false,
					mouse		: 'press',
					onChange     			: function(page){
						var url = "" + "?searchKeyword=" + "${searchKeyword}" + "&start=" + ((page-1) * 10);
												$(location).attr('href', url); 
											  }
			});
			$('#listview img').click(function(){ 
				var img=$(this).attr("src");
				if(img =="imagename"){
					// Text you want
					}
				if(img =="otherimage"){
					// Text you want
					}
				});
							$.ajax({
		        url: '/GetResultJsonData',
		        type: 'POST',
		        dataType: 'json',
		        contentType: "application/json; charset=utf-8",
		        success: function (result) {
		        	alert("!!!!");
		        	alert(result.searchKeyword);
		        },
		        error: function (error) {
		            alert("error: " + error.responseText);
		        }
		     });

		});
		
*/