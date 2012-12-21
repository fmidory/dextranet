function carregaDadosHomePage()
{
	$.holy("../template/carrega-menu-principal.xml", {});
	$.holy("../template/carrega-menu-lateral.xml", {});
	$.holy("../template/carrega-dados-usuario.xml", {});

	$.ajax( {
		type : 'GET',
		url : "/s/post",
		data : "max-results=20&q=",
		success : function(jsonArrayPost) {
			$.holy("../template/carrega-miolo-home-page.xml",
					{
						"jsonArrayPost" : jsonArrayPost,
						"sucesso" : false
					});
		}
	});

	$(document).delay(1000);
}

function carregaDadosHomePage2(temNovoPost)
{
	$.holy("../template/carrega-menu-principal.xml", {});
	$.holy("../template/carrega-menu-lateral.xml", {});
	$.holy("../template/carrega-dados-usuario.xml", {});

	$.ajax( {
		type : 'GET',
		url : "/s/post",
		data : "max-results=20&q=",
		success : function(jsonArrayPost) {
			$.holy("../template/carrega-miolo-home-page.xml",
					{
						"jsonArrayPost" : jsonArrayPost,
						"sucesso" : temNovoPost
					});
		}
	});

	$(document).delay(1000);
}