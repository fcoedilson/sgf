var markerlist = [];
var markerRoute = [];
var rotaArray = [];

function updateMap() {
	updateVehicles();
	if (currentVehicle && currentVehicle.id) {
		currentVehicle.showAndCenter();
		updateCurrentVehicle(currentVehicle);
	}
	calculateBounds();
}

function showAndSelect(id) {
	var oldVehicle = currentVehicle;
	currentVehicle = retrieveVehicle(id);
	currentVehicle.showAndCenter();
	updateCurrentVehicle(currentVehicle);
	checkRemoveCurrentVehicle(oldVehicle);
	var row = document.getElementById('m:'+ id + ':current');
	removeRoutePoints();
	if(row){
		if(row.checked == true){
			showRouteVehicleOnMap(id, true);
			calculateBoundsRoute();
		}
	}
	calculateBoundsRoute();
}

function showOnMap(id, checked) {
	if (checked) {
		retrieveVehicle(id).show();
	} else {
		retrieveVehicle(id).hide();
	}
	calculateBoundsRoute();
}

function removeRoutePoints(){
	if(markerRoute.length > 0){
		for ( var i = 0; i < markerRoute.length; i++) {
			map.removeOverlay(markerRoute[i]);
		}
	}
	document.getElementById("distanciaTempo").innerHTML = "";
	markerRoute = new Array();
	clearCurrenteRoute2();
}

function showAllOnMap(checked) {
	if (checked) {
		showAllVehicles();
	} else {
		hideAllVehicles();
	}
	calculateBounds();
}

function createIcon2(type) {
	var icon = new GIcon();
	icon.image = "../image?id=" + type;
	icon.iconSize = new GSize(32, 32);
	icon.iconAnchor = new GPoint(16, 16);
	icon.infoWindowAnchor = new GPoint(16, 16);
	return icon;
}

function createMarker2(marker, nome) {
	GEvent.addListener(marker, "click", function() {
		marker.openInfoWindowHtml("Point <b>" + nome + "</b>");
	});
	return marker;
}

function calculateBounds() {
	if (vehicles.length > 0 && (!currentVehicle)) {
		var mbr = new GLatLngBounds();
		for(var i = 0; i < vehicles.length; i++) {
			if (vehicles[i].isVisible()) {
				mbr.extend(vehicles[i].center);
			}
		}
		map.setCenter(mbr.getCenter(), map.getBoundsZoomLevel(mbr));
	}
}

function checkRemoveCurrentVehicle(oldVehicle) {
	if (currentVehicle && oldVehicle && (currentVehicle.id == oldVehicle.id)) {
		currentVehicle = null;
		removeRastro();
		document.getElementById("currentVehicle").value = "";
		document.getElementById("m:" + oldVehicle.id + ":current").checked = false;
	}
}

function updateCurrentVehicle(currentVehicle) {
	document.getElementById("currentVehicle").value = currentVehicle.id;
	document.getElementById("m:" + currentVehicle.id + ":current").checked = true;
}
