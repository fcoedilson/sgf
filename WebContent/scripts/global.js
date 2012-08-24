function initLoader() {

	resizeApp();

}

function openPentaho(){
    window.open();
	document.location.href='http://bi.cti.fortaleza.ce.gov.br:8084/pentaho/content/pentaho-cdf-dd/Render?solution=Frotas&amp;path=/Dashboard&amp;file=ug_periodo_consumo.wcdf';
}

function resizeApp() {

	document.getElementById('content').style.height = (currentHeight() - 50) + 'px';

	document.getElementById('contentMap').style.height = (currentHeight() - 50) + 'px';

	try {

		var nameSize = document.getElementById('userLogin').firstChild.nodeValue.length;

		if( document.getElementById('innerPopup_body') ){

			document.getElementById('innerPopup_body').height = (currentHeight() - 230) + 'px';
		}

		if( document.getElementById('innerPopup') ){

			document.getElementById('innerPopup').height = (currentHeight()) + 'px';
		}

	} catch (e) {}

	window.onresize = resizeApp;

	openCloseList(true);
}

function currentWidth() {

    var width = 0;

    if (typeof(window.innerWidth) == 'number') {

    	width = window.innerWidth;

    } else if (document.documentElement && document.documentElement.clientWidth) {

    	width = document.documentElement.clientWidth;

    } else if (document.body && document.body.clientWidth) {

    	width = document.body.clientWidth;
    }

    return width;
}

function currentHeight() {

    var height = 0;

    if (typeof(window.innerHeight) == 'number') {

    	height = window.innerHeight;

    } else if (document.documentElement && document.documentElement.clientHeight) {

    	height = document.documentElement.clientHeight;

    } else if (document.body && document.body.clientHeight) {

    	height = document.body.clientHeight;
    }

    return height;
}
