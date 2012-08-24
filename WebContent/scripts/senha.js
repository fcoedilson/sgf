var level= new Array(6);
var colors = new Array(6);

level[0] = "Muito Fraca";
level[1] = "Fraca";
level[2] = "Media";
level[3] = "Forte";
level[4] = "Muito Forte";

colors[0] = "#808080";
colors[1] = "#da5301";
colors[2] = "#cc9933";
colors[3] = "#1e91ce";
colors[4] = "#336600";
colors[5] = "#808080";

var regexPhone = /^\(?(\d{3})\)?[\.\-\/ ]? (\d{3})[\.\-\/ ]?(\d{4})$/;
var regexCell = /(([7-9])(\d{3})([-])(\d{4}))|(([7-9])(\d{7}))/;
var regexMail = /^\w+([\.-]?\w+)*@\w+ ([\.-]?\w+)*(\.\w{2,3})+$/;
var regexCpf = /^(\d{3}.\d{3}.\d{3}-\d{2})|(\d{11})$/;


function passwordChanged() {
	var strength = document.getElementById('password');
	var medium = /([a-z].*[A-Z])|([A-Z].*[a-z])/;
	var strong = /([a-zA-Z0-9].*[!,@,#,$,%,^,&,*,?,_,~])|([!,@,#,$,%,^,&,*,?,_,~].*[a-zA-Z0-9])/;
	var pwd = document.getElementById("userPasswd").value;
	if (pwd.length == 0) {
		strength.innerHTML = '';
	} else if (pwd.match(medium)) {
		strength.innerHTML = '<span style="color:blue">Médio</span>';
	} else if (pwd.match(strong)) {
		strength.innerHTML = '<span style="color:green">Forte</span>';
	} else {
		strength.innerHTML = '<span style="color:orange">Fraca</span>';
	}
}

function loadcurrentIpAddress(){
	var ip = location.host;
	alert(ip);
}

function exibirAvaliacao(){
	$(function() {
	$('#userPasswd').pstrength();
	});
}
