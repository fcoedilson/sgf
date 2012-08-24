jQuery(document).ready(function() {
	
	$('select.autocomplete').select_autocomplete();
	
	$('input.maskData').datepicker({
		dayNamesMin: ['Dom', 'Seg', 'Ter', 'Qua', 'Qui', 'Sex', 'Sab'],
		monthNames: ['Janeiro','Fevereiro','Março','Abril','Maio','Junho','Julho','Agosto','Setembro','Outubro','Novembro','Dezembro'],
		dateFormat: 'dd/mm/yy', showOn: 'button', buttonImage: 'imagens/calendar.png', buttonImageOnly: true
	});
	$('input.maskCpf').setMask('cpf');	
	$('input.maskData').setMask('date');
	$('input.maskFone').setMask('phone');
	$('input.maskInteiro').setMask('integer');
	$('#login #login').focus();
	$("input[type=text], :radio").focus(function() {
         $(this).addClass("comFoco");
    });
	$("input[type=text], :radio").blur(function() {
         $(this).removeClass("comFoco");
    });
	
	$("table tr").hover(
	function() {
         $(this).addClass("trHover");
    },
	function() {
         $(this).removeClass("trHover");	
	});
	$("table tr:nth-child(even)").addClass("trDestaque");
	
	$("#cadastroPessoa").validate({
		rules: {
			email: {
				required: true,
				email: true
			}
		},
		messages: {
			email: {
				required: " Por favor informe o email!",
				email: " Por favor, informe um email válido!"
			}
		}
	});
});
