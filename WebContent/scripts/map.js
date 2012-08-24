var map;
var mapp;
var mapa;
var GDir1;
var GDir2;
var ROUTES = [];
var GMAP_ROUTES = [];
var POLYLINES = [];
var listaDistancias = [];
var normalProj;
var waypoints = [];
var gpolys = [];
var routeNodes = [];
var myNode;
var newMarkers = [];
var markerDragged;
var isDragged;
var lastIndex;
var prevIndex;
var num;
var cleaned = false;
var geocoder = null;
var pointToRemove;
var color = ["#0000FF","#8A2BE2","#A52A2A","#DEB887","#5F9EA0","#7FFF00", "#D2691E","#6495ED","#DC143C", "#00FFFF","#00008B","#008B8B","#B8860B","#A9A9A9","#006400"];
var pointInterval = 30;

var markersClusterer = [];
var markerClusterer;

var styles = [[{
    url: '../images/people35.png',
    height: 35,
    width: 35,
    opt_anchor: [16, 0],
    opt_textColor: '#FF00FF'
  },
  {
    url: '../images/people45.png',
    height: 45,
    width: 45,
    opt_anchor: [24, 0],
    opt_textColor: '#FF0000'
  },
  {
    url: '../images/people55.png',
    height: 55,
    width: 55,
    opt_anchor: [32, 0]
  }],
  [{
    url: '../images/conv30.png',
    height: 27,
    width: 30,
    anchor: [3, 0],
    textColor: '#FF00FF'
  },
  {
    url: '../images/conv40.png', 
    height: 36,
    width: 40,
    opt_anchor: [6, 0],
    opt_textColor: '#FF0000'
  },
  {
    url: '../images/conv50.png',
    width: 50,
    height: 45,
    opt_anchor: [8, 0]
  }],
  [{
    url: '../images/heart30.png',
    height: 26,
    width: 30,
    opt_anchor: [4, 0],
    opt_textColor: '#FF00FF'
  },
  {
    url: '../images/heart40.png', 
    height: 35,
    width: 40,
    opt_anchor: [8, 0],
    opt_textColor: '#FF0000'
  },
  {
    url: '../images/heart50.png',
    width: 50,
    height: 44,
    opt_anchor: [12, 0]
  }]];


function refreshMap() {
	if (markerClusterer != null) {
		markerClusterer.clearMarkers();
	}
	var zoom = 14; //parseInt(document.getElementById("zoom").value, 10);
	var size =  40;//parseInt(document.getElementById("size").value, 10);
	var style = "-1";// document.getElementById("style").value;
	zoom = zoom == -1 ? null : zoom;
	size = size == -1 ? null : size;
	style = style == "-1" ? null: parseInt(style, 10);
	markerClusterer = new MarkerClusterer(map, markersClusterer, {maxZoom: zoom, gridSize: size, styles: styles[style]});
}

function loadMaps() {
	if (document.getElementById("maparea")) {
		createEditablePolygon();
		var pontos = document.getElementById('points').value;
		if(pontos){
			var points = pontos.split('$*@');
			for ( var j = 0; j < points.length; j++) {
				var p = points[j].split('#%');
				var mark = createMarker(p[0], p[1], p[2], p[3], p[4], p[5], p[6],p[7], p[8], true);
				mapa.addOverlay(mark);
			}
		}
	} else if (document.getElementById("mapp")) {
		createMapPoints();
		//showVehiclesOnMap();
	} else if (document.getElementById("mapEditablePoint")) {
		createEditablePoint();
	}
}

function calculateBoundsArea(){
	var bounds = new GLatLngBounds();
	var paths = polygon.getPaths();
	var path;
	for (var p = 0; p < paths.getLength(); p++) {
		path = paths.getAt(p);
		for (var i = 0; i < path.getLength(); i++) {
			bounds.extend(path.getAt(i));
		}
	}
	mapa.setCenter(bounds.getCenter(), mapa.getBoundsZoomLevel(bounds));
}

function showPointsOnMap(){
	map.clearOverlays();
	createMapPoints();
	var pontos = document.getElementById('points').value;
	var markers = new Array();
	if(pontos){
		var points = pontos.split('##$##');
		for (var j = 0; j < points.length; j++) {
			var p = points[j].split('##');
			if(p[0] && p[1]){
				var marker = new GLatLng(p[0], p[1]);
				markers.push(marker);
				var markerPoint = createMarkerSimple(p[0], p[1], p[2]);
				map.addOverlay(markerPoint);
			}
		}
	}
	calculateBoundsRoute(markers);
}

function showVehiclesOnMap(){
alert('OK');
	map.clearOverlays();
	if(document.getElementById('pontosVeiculos')){
		var pontos = document.getElementById('pontosVeiculos').value;
		var markers = new Array();
		var param = 1;
		if(pontos){
			var points = pontos.split('##$##');
			for (var j = 0; j < points.length; j++) {
				var p = points[j].split('##');
				if(p[0] && p[1]){
					var marker = new GLatLng(p[0], p[1]);
					markers.push(marker);
					if(j == 0) {
						param = 2;
					} else {
						param = 1;
					};
					var markerPoint = createMarker(p[0], p[1], p[2], p[3], p[4], p[5], p[6], p[7],p[8], p[9], false, param);
					map.addOverlay(markerPoint);
				}
			}
		}
		calculateBoundsRoute(markers);
	}

}

function initialize() {

	map = new google.maps.Map2(document.getElementById("mapp"));
	map.setCenter(new GLatLng(-3.7325370241018394, -38.51085662841797), 12);
	map.addControl(new GLargeMapControl());
	var icon = new GIcon(G_DEFAULT_ICON);
	icon.image = "http://chart.apis.google.com/chart?cht=mm&chs=24x32&chco=FFFFFF,008CFF,000000&ext=.png";
	var elem = document.getElementById('vehicleRoute').value;
	var points = elem.split('##$##');
	var markers = new Array();

	for (var j = 0; j < points.length; j++) {
		var p = points[j].split('##');
		var marker2 = new GMarker(new GLatLng(p[0], p[1]));
			//var markerPoint = createMarker(p[0], p[1], p[2], p[3], p[4], p[5], p[6], p[7],p[8], p[9], false, param);
			markers.push(marker2);
	}
	markerClusterer = new MarkerClusterer(map, markers);
}

function clusterCurrentMarker(markers){
    markerClusterer = new MarkerClusterer(map, markers, {maxZoom: 14, gridSize: 20, styles: styles["-1"]});
}

function mostrarRotaVeiculo(){
	map.clearOverlays();
	createMapPoints();
	var elem = document.getElementById('vehicleRoute').value;
	var points = elem.split('##$##');
	var markers = new Array();
	var polyline;
	var param = 1;
	for (var j = 0; j < points.length; j++) {
		var p = points[j].split('##');
		if(p[0] && p[1]){
			var marker = new GLatLng(p[0], p[1]);
			if(j == 0) {
				param = 2;
			} else {
				param = 1;
			};
			var markerPoint = createMarker(p[0], p[1], p[2], p[3], p[4], p[5], p[6], p[7],p[8], p[9], false, param);
			markers.push(marker);
			map.addOverlay(markerPoint);
		}
	}
	polyline = new GPolyline(markers);
	map.addOverlay(polyline);
	calculateBoundsRoute(markers);
}

function clicked(overlay, latlng) {
	mapp.clearOverlays();
	if (latlng) {
		geocoder.getLocations(latlng, function(addresses) {
			if(addresses.Status.code != 200) {
				alert("reverse geocoder failed to find an address for " + latlng.toUrlValue());
			} else {
				address = addresses.Placemark[0];
				var myHtml = address.address;
				//mapp.openInfoWindow(latlng, myHtml);
		    	mapp.setCenter(latlng, 15);
		        var marker = new GMarker(latlng);
		        mapp.addOverlay(marker);
				document.getElementById("lat").value = latlng.lat();
		        document.getElementById("lng").value = latlng.lng();
				document.getElementById("descricaoPonto").value = myHtml;
			}
		});
	}
} 

function showAddress() {
	mapp.clearOverlays();
	var address = document.getElementById('endereco').value;
	if (geocoder) {
	  geocoder.getLatLng(
      address,
      function(point) {
    	  mapp.setCenter(point, 15);
          var marker = new GMarker(point);
          mapp.addOverlay(marker);
          document.getElementById("lat").value = point.lat();
          document.getElementById("lng").value = point.lng();
      }
    );
  }
}

function getAddress(overlay, latlng) {
	if (latlng != null) {
		address = latlng;
		geocoder.getLocations(latlng, showAddress);
	}
}

function crossAddress(){
	var geocoder = new google.maps.Geocoder();
	var address = 'Fortaleza, BR';
	if (geocoder) {
		geocoder.geocode({ 'address': address }, function (results, status) {
			if (status == google.maps.GeocoderStatus.OK) {
				console.log(results[0].geometry.location);
			}else {
				console.log("Geocoding failed: " + status);
			}
		});
	}    
}

/*
 */
function createMarker(lat, lng, modelo, placa, velocidade, odometro, ignicao, pprox, dist,dataHora, markerOption, param) {
	var markerIcon = createIcon(param);
	var markerOptions = { icon:markerIcon };
	var marker;
	marker = new GMarker(new GLatLng(lat, lng), markerOptions);
	GEvent.addListener(marker, "click", function() {
	var html = "<table style='width:280px'>" +
				"<td>Veiculo: <b>" + modelo + 
				"</b><br/>Placa:<b>" + placa + 
				"</b><br/>Ignição:<b>" + ignicao + 
				"</b><br/>Odômetro:<b>" + odometro.substring(0, odometro.length-2).replace(".", ",")  + 
				"</b><br/>Velocidade:<b> " +  velocidade.substring(0, velocidade.length-2).replace(".", ",") +  "(Km/h)"+
				"</b><br/>Ponto ref.:<b>" + pprox + 
				"</b><br/>Dist. ref.:<b>" + dist.substring(0, dist.length-2) + "(m)" +
				"</b><br/>Data/Hora:<b>" + dataHora + 
				"</b></td></tr>" +
				"</table><br/><br/>";
		marker.openInfoWindowHtml(html);
	});
	return marker;
}

function createMarkerSimple(lat, lng, desc) {
	var marker;
	marker = new GMarker(new GLatLng(lat, lng));
	GEvent.addListener(marker, "click", function() {
	var html = "<table style='width:230px'>" +
				"<td><b>" + modelo + 
				"</b></td></tr>" +
				"</table><br/>";
		marker.openInfoWindowHtml(html);
	});
	return marker;
}

function createIcon(param) {
	var icon = new GIcon();
	icon.image = "../images/cars/car"+ param +".png";
	icon.iconSize = new GSize(20, 20);
	icon.iconAnchor = new GPoint(16, 16);
	icon.infoWindowAnchor = new GPoint(16, 16);
	return icon;
}

function createMapPoints(){
	map = new google.maps.Map2(document.getElementById("mapp"), {draggableCursor:"crosshair"});
	map.setCenter(new GLatLng(-3.7325370241018394, -38.51085662841797), 14);
	map.addControl(new GMapTypeControl());
	map.addControl(new GSmallMapControl());
	map.addControl(new GScaleControl());
	map.disableDoubleClickZoom();
	map.enableScrollWheelZoom();
	map.enableContinuousZoom();
	map.checkResize();
	geocoder = new GClientGeocoder();
	google.setOnLoadCallback(createMapPoints);
}

function createEditablePoint() {

	mapp = new google.maps.Map2(document.getElementById("mapEditablePoint"), {draggableCursor:"crosshair"});
	mapp.addControl(new GMapTypeControl());
	mapp.addControl(new GSmallMapControl());
	geocoder = new GClientGeocoder();
	mapp.disableDoubleClickZoom();
	var lat = document.getElementById("lat");
	var lng = document.getElementById("lng");
	GEvent.addListener(mapp, "click", clicked);
	GEvent.addListener(mapp, "dblclick", function(overlay, point) {
	mapp.clearOverlays();
		if(point) {
			pointToRemove = point;
			lat.value = point.lat();
			lng.value = point.lng();
			mapp.setCenter(new GLatLng(lat.value, lng.value), 15);
			mapp.addOverlay(new GMarker(new GLatLng(lat.value, lng.value)));
		}
	});

	if (lat.value && lng.value) {
		mapp.setCenter(new GLatLng(lat.value, lng.value), 15);
		mapp.addOverlay(new GMarker(new GLatLng(lat.value, lng.value)));
	} else {
		mapp.setCenter(new GLatLng(-3.7325370241018394, -38.51085662841797), 13);
	}
	google.setOnLoadCallback(createEditablePoint);
}



function getCirclePoints(center, radius){
	var circlePoints = Array();
	var searchPoints = Array();
	with (Math) {
		var rLat = (radius/3963.189) * (180/PI); // miles
		var rLng = rLat/cos(center.lat() * (PI/180));
		for (var a = 0 ; a < 361 ; a++ ) {
			var aRad = a*(PI/180);
			var x = center.lng() + (rLng * cos(aRad));
			var y = center.lat() + (rLat * sin(aRad));
			var point = new GLatLng(parseFloat(y), parseFloat(x),true);
			circlePoints.push(point);
			if (a % pointInterval == 0) {
				searchPoints.push(point);
			}
		}
	}
	searchPolygon = new GPolygon(hcirclePoints, '#0000ff', 1, 1, '#0000ff', 0.2);	
	map.addOverlay(searchPolygon);
	map.setCenter(searchPolygon.getBounds().getCenter(), map.getBoundsZoomLevel(searchPolygon.getBounds()));
	return searchPoints;
}

function createEditablePolygon() {
	mapa = new google.maps.Map2(document.getElementById("maparea"));
	mapa.addControl(new GMapTypeControl());
	mapa.addControl(new GSmallMapControl());
	mapa.disableDoubleClickZoom();
	mapa.setCenter(new GLatLng(-3.7325370241018394, -38.51085662841797), 15);
	var polygon = null;
	
	var element = document.getElementById('polygon');
	var points = Array();
	
	if (element && element.value) {
		var coordinates = element.value.split(',');
		for (var i = 0; i < coordinates.length; i++) {
			var coordinate = coordinates[i].split(' ');
			var point = new GLatLng(parseFloat(coordinate[0]), parseFloat(coordinate[1]), true);
			points.push(point);
		}
		polygon = new GPolygon(points, '#A50E0C', 2, 0.7, '#A50E0C', 0.2);
	} else {
		polygon = new GPolygon([], '#A50E0C', 2, 0.7, '#A50E0C', 0.2);
		polygon.enableDrawing();
	}
	
	polygon.enableEditing({onEvent: "mouseover"});
	polygon.disableEditing({onEvent: "mouseout"});
	
	GEvent.addListener(polygon, "click", function(latlng, index) {
    	if (typeof index == "number") {
    		polygon.deleteVertex(index);
    	}
    });
	GEvent.addListener(polygon, "endline", function() {
    	polygon.setStrokeStyle({weight: 4});
    });
	
	GEvent.addListener(polygon, "lineupdated", function() {
		var element = document.getElementById('polygon');
		var encode = "";
		for (var i = 0; i < polygon.getVertexCount(); i++) {
			var p = polygon.getVertex(i);
			encode += p.lat() + " " + p.lng();
			if (i != (polygon.getVertexCount() -1)) {
				encode += ",";
			}
		}
		element.value = encode;
	});
	mapa.addOverlay(polygon);
	mapa.setCenter(polygon.getBounds().getCenter(), mapa.getBoundsZoomLevel(polygon.getBounds()));
	google.setOnLoadCallback(createEditablePolygon);
}

function setRoutesListDirections(list){
	var d = new GDirections();
	this.ROUTES.push(d);
	this.ROUTES[ROUTES.length - 1].loadFromWaypoints(list, {getPolyline:true, getSteps:true});
}

function distancia(){
	var poliline;
	var dist = 0;
	var soma = 0;
	var tempo = 0;
	var tempoTotal = 0;
	var trechos = [];
	var info = "Informacoes da Rota \r\n\r\n Distancias estimadas(em Km):\r\n\r\n";
	for ( var j = 0; j < ROUTES.length; j++) {
		var poli = ROUTES[j].getPolyline();
		if(poli){
			for ( var k = 0; k < poli.getVertexCount(); k++) {
				trechos.push(poli.getVertex(k));
			}
			poliline = new GPolyline(trechos, color[j], 3, 1);
			this.POLYLINES.push(poliline);polygon
			map.addOverlay(poliline);
			trechos = [];
		}
	}
	var sumary;
	for (var p = 0; p < ROUTES.length; p++) {
		if(ROUTES[p].getDistance() && ROUTES[p].getDuration()){
			dist = ROUTES[p].getDistance().meters;
			tempo = ROUTES[p].getDuration().seconds/3600;
			dist = dist/1000;
			soma += dist;
			tempoTotal += tempo;
			sumary += ROUTES[p].getSummaryHtml() + "\r\n";
		}
	}
	//document.getElementById("distanciaTempo").innerHTML = "Distancia estimada: "+ soma.toString().substring(0,5) +" Km // Tempo aproximado: "+ Math.floor(tempoTotal) +" h " + Math.floor((tempoTotal - Math.floor(tempoTotal))*60) + " min" ;
}

function calculateBoundsRoute(){
	if(markerRoute){
		if (markerRoute.length > 0) {
			var mbr = new GLatLngBounds();
			for(var i = 0; i < markerRoute.length; i++) {
				mbr.extend(markerRoute[i]);
			}
			map.setCenter(mbr.getCenter(), map.getBoundsZoomLevel(mbr));
		}
	}
}

function calculateBoundsRoute(markers){
	if(markers){
		if (markers.length > 0) {
			var mbr = new GLatLngBounds();
			for(var i = 0; i < markers.length; i++) {
				mbr.extend(markers[i]);
			}
			map.setCenter(mbr.getCenter(), map.getBoundsZoomLevel(mbr));
		}
	}
}

function clearCurrenteRoute2(){
	for ( var j = 0; j < POLYLINES.length; j++) {
		map.removeOverlay(POLYLINES[j]);
	}
	POLYLINES = [];
}

function clearCurrenteRoute(){
	for ( var k = 0; k < this.gpolys.length; k++) {
		map.removeOverlay(this.gpolys[k]);
	}
	for ( var k = 0; k < this.waypoints.length; k++) {
		map.removeOverlay(this.waypoints[k]);
	}
	this.waypoints = new Array;
	this.gpolys = new Array;
	this.routeNodes = new Array;
	this.isDragged = 1;
	clearCurrenteRoute2();
}


function createMonitoringMap(){
	map = new google.maps.Map2(document.getElementById("map"), {draggableCursor:"crosshair"});
	map.setCenter(new GLatLng(-3.7325370241018394, -38.51085662841797), 12);
	map.addControl(new GMapTypeControl());
	map.addControl(new GSmallMapControl());
	map.addControl(new GScaleControl());
	//map.addMapType(G_SATELLITE_3D_MAP);
	normalProj = G_NORMAL_MAP.getProjection();
	map.disableDoubleClickZoom();
	map.enableScrollWheelZoom();
	map.enableContinuousZoom();
	GDir1 = new GDirections();
	GDir2 = new GDirections();
	GEvent.addListener(map, 'load', setRoutesListDirections);
	GEvent.addListener(map, 'mousemove', getProximity);
	GEvent.addListener(map, "zoomend", function() {
		routeNodes = new Array();
	});

	GEvent.addListener(map, "click", function(overlay, point) {
		if (point) {
			if (waypoints.length == 0) {
				GDir1.loadFromWaypoints([point.toUrlValue(6), point.toUrlValue(6)], {getPolyline:true});
			} else {
				GDir1.loadFromWaypoints([waypoints[waypoints.length-1].getPoint(), point.toUrlValue(6)], {getPolyline:true});
			}
		}
	});
	iconNode = new GIcon();
	iconNode.image = "../images/node.gif";
	iconNode.shadow = ''; iconNode.iconSize = new GSize(10,10); iconNode.shadowSize = new GSize(0,0);
	iconNode.iconAnchor = new GPoint(5,5);
	iconNode.infoWindowAnchor = new GPoint(5,5);
	iconNode.dragCrossImage = '../images/empty.gif';
	iconNode.dragCrossSize = GSize(1, 1);
	iconNode.maxHeight = 1;
	myNode = new GMarker(map.getCenter(), {icon:iconNode, draggable:true, bouncy:false, zIndexProcess:function(marker,b) {return 1;}});
	map.addOverlay(myNode);
	myNode.show();
	myNode.hide();

	GEvent.addListener(myNode, "drag", function() {
		myNode.show();
		if (isDragged == 2) {
			markerDragged = myNode;
			return;
		}
		if (myNode.MyIndex < waypoints.length) {
			isDragged = 2;
			markerDragged = false;
			lastIndex = myNode.MyIndex;
			prevIndex = -1;
			GDir2.loadFromWaypoints([waypoints[lastIndex].getPoint(), myNode.getPoint().toUrlValue(6), waypoints[lastIndex + 1].getPoint()], {getPolyline:true});
		}
	});

	GEvent.addListener(myNode, "dragend", function() {
		var point = myNode.getPoint();
		var marker = new GMarker(point, {icon:iconNode, draggable:true, dragCrossMove:false, bouncy:false, zIndexProcess:function(marker,b) {return 1;}});
		waypoints.splice(myNode.MyIndex + 1, 0, marker);
		for (var i = myNode.MyIndex; i < waypoints.length; i++){
			waypoints[i].MyIndex = i;
		}
		var dummy = new GPolyline([point]);
		map.addOverlay(dummy);
		gpolys.splice(myNode.MyIndex + 1, 0, dummy);
		GEvent.addListener(marker, "dragstart", function() { isDragged = 1; myNode.hide(); });
		GEvent.addListener(marker, "dragend", function() { isDragged = 0; } );
		GEvent.addListener(marker, "drag", dragMarker);
		map.addOverlay(marker);
		if (myNode.MyIndex < waypoints.length) {
			lastIndex = myNode.MyIndex + 1;
			prevIndex = lastIndex - 1;
			GDir2.loadFromWaypoints([waypoints[lastIndex - 1].getPoint(),point.toUrlValue(6), waypoints[lastIndex + 1].getPoint()], {getPolyline:true});
		}
	});

	GEvent.addListener(GDir1, "load", function() {
		var gp = GDir1.getPolyline();
		var point = gp.getVertex(gp.getVertexCount() - 1);
		var marker = new GMarker(point, {icon:iconNode, draggable:true, dragCrossMove:false, bouncy:false, zIndexProcess:function(marker,b) {return 1;}});
		if (waypoints.length == 0) {
			marker.title = GDir1.getRoute(0).getStartGeocode().address;
		} else {
			waypoints[waypoints.length-1].title = GDir1.getRoute(0).getStartGeocode().address;
			marker.title = GDir1.getRoute(0).getEndGeocode().address
		}

		GEvent.addListener(marker, "dragstart", function() { isDragged = 1; myNode.hide(); });
		GEvent.addListener(marker, "drag", dragMarker);
		GEvent.addListener(marker, "dragend", function() { isDragged = 0; });
		marker.MyIndex = waypoints.length;
		waypoints.push(marker);
		map.addOverlay(marker);
		if (waypoints.length > 1) {
			map.addOverlay(gp);
			gpolys.push(gp);
			routeNodes = [];
			getProximity();
		}
	});

	GEvent.addListener(GDir2, "load", function() {
		var gp = GDir2.getPolyline();
		map.removeOverlay(gpolys[lastIndex]);
		if (prevIndex >= 0) {
			map.removeOverlay(gpolys[lastIndex-1]);
			var minD, minI, points=[];
			var p0 = waypoints[lastIndex].getPoint();
			for (var i = 0; i < gp.getVertexCount(); i++) {
				var p = gp.getVertex(i);
				points.push(p);
				var d = p0.distanceFrom(p);
				if (i == 0 || minD > d) {
					minD = d;
					minI = i;
				}
			}

			gpolys[lastIndex - 1] = new GPolyline(points.slice(0, minI + 1));
			gpolys[lastIndex] = new GPolyline(points.slice(minI, points.length));
			map.addOverlay(gpolys[lastIndex - 1]);
			waypoints[lastIndex-1].title = GDir2.getRoute(0).getStartGeocode().address;
			waypoints[lastIndex].title = GDir2.getRoute(0).getEndGeocode().address;
			waypoints[lastIndex+1].title = GDir2.getRoute(1).getEndGeocode().address;
		} else {
			gpolys[lastIndex] = gp;
			waypoints[lastIndex].title = GDir2.getRoute(0).getStartGeocode().address;
			waypoints[lastIndex+1].title = GDir2.getRoute(0).getEndGeocode().address;
		}

		map.addOverlay(gpolys[lastIndex]);
		routeNodes = [];
		getProximity();
		isDragged = 0;
		if (markerDragged) {
			isDragged = 1;
			GEvent.trigger(markerDragged, 'drag');
		}
	});

	map.checkResize();
	google.setOnLoadCallback(createMonitoringMap);

//	try {
//		if (initWikiCrimes) {
//			initWikiCrimes(map,jQuery);
//		}
//	} catch (e) {}
}

function dragMarker() {
	if (isDragged == 2) {
		markerDragged = this;
		return;
	}
	isDragged = 2;
	if (markerDragged) {
		marker = markerDragged;
		markerDragged = false;
	} else {
		marker = this;
	}
	lastIndex = marker.MyIndex;
	var point = marker.getPoint();
	if (lastIndex > 0) {
		if (lastIndex < waypoints.length - 1) {
			prevIndex = lastIndex - 1;
			GDir2.loadFromWaypoints([waypoints[lastIndex - 1].getPoint(), point.toUrlValue(6), waypoints[lastIndex + 1].getPoint()],{getPolyline:true});
		} else {
			prevIndex = -1; lastIndex = lastIndex - 1;
			GDir2.loadFromWaypoints([waypoints[lastIndex].getPoint(),point.toUrlValue(6)],{getPolyline:true});
		}
	} else if (waypoints.length > 1) {
		prevIndex = -1;
		GDir2.loadFromWaypoints([point.toUrlValue(6),waypoints[1].getPoint()],{getPolyline:true});
	}
}

function getProximity(mouseLatLng, marker) {
	var dist, zoom;
	if (routeNodes.length == 0) {
		dist = 0;
		zoom = map.getZoom();
		if (gpolys.length > 0 && gpolys[0].getVertexCount() > 0 )
			routeNodes.push(normalProj.fromLatLngToPixel(gpolys[0].getVertex(0), zoom));
		for (var i = 0; i < gpolys.length; i++) {
			dist += gpolys[i].getLength();
			for (var j = 1; j < gpolys[i].getVertexCount(); j++) {
				var point = normalProj.fromLatLngToPixel(gpolys[i].getVertex(j), zoom)
				point.MyIndex = i;
				routeNodes.push(point);
			}
		}
		var panel = document.getElementById('panel');
		if (panel) {
			panel.innerHTML = (dist/1000).toFixed(1) + " km";
		}
	}

	if (!mouseLatLng || routeNodes.length <= 1 || isDragged > 0){
		return;
	}

	zoom = map.getZoom();
	var mousePx = normalProj.fromLatLngToPixel(mouseLatLng, zoom);
	var minDist = 999;
	var minX = mousePx.x;
	var minY = mousePx.y;
	if (routeNodes.length > 1) {
		var x,y, d1,d2,d;
		var dx = mousePx.x - routeNodes[0].x;
		var dy = mousePx.y - routeNodes[0].y;
		d2 = dx*dx + dy*dy;
		for (var n = 0 ; ++n < routeNodes.length; ) {
			d1 = d2;
			x = routeNodes[n].x; dx = mousePx.x - x;
			y = routeNodes[n].y; dy = mousePx.y - y;
			d2 = dx*dx + dy*dy;
			dx = x - routeNodes[n-1].x;
			dy = y - routeNodes[n-1].y;
			d = dx*dx + dy*dy;
			var u = ((mousePx.x - x) * dx + (mousePx.y - y) * dy) / d;
			x += (u*dx);
			y += (u*dy);
			dx = mousePx.x - x;
			dy = mousePx.y - y;
			dist = dx*dx + dy*dy;
			if ((d1 - dist) + (d2 - dist) > d) {
				if (d1 < d2) {
					dist = d1;
					x = routeNodes[n-1].x;
					y = routeNodes[n-1].y;
				} else {
					dist = d2;
					x = routeNodes[n].x;
					y = routeNodes[n].y;
				}
			};
			if (minDist > dist) {
				minDist = dist;
				minX = x;
				minY = y;
				myNode.MyIndex = routeNodes[n].MyIndex;
			}
		}var mbr = new GLatLngBounds();
		for(var i = 0; i < markerRoute.length; i++) {
			mbr.extend(markerRoute[i]);
		}
		map.setCenter(mbr.getCenter(), map.getBoundsZoomLevel(mbr));

		if (minDist > 25) {
			myNode.hide();
		} else {
			for (n = waypoints.length; --n >= 0;) {
				var markerPx = normalProj.fromLatLngToPixel(waypoints[n].getPoint(), zoom);
				dx = markerPx.x - minX;
				dy = markerPx.y - minY;
				if (dx*dx + dy*dy < 25) {
					myNode.hide();
					return;
				}
			}
			myNode.setPoint(normalProj.fromPixelToLatLng(new GPoint(minX, minY), zoom));
			myNode.show();
		}
	}
}

function createEditablePoint23() {
	mapp = new google.maps.Map2(document.getElementById("mapponto"));
	mapp.addControl(new GMapTypeControl());
	mapp.addControl(new GSmallMapControl());
	geocoder = new GClientGeocoder();
	mapp.disableDoubleClickZoom();
	var lat = document.getElementById("lat");
	var lng = document.getElementById("lng");
	GEvent.addListener(mapp, "click", function(overlay, point) {
	mapp.clearOverlays();
		if(point) {
			pointToRemove = point;
			lat.value = point.lat();
			lng.value = point.lng();
			mapp.setCenter(new GLatLng(lat.value, lng.value), 15);
			mapp.addOverlay(new GMarker(new GLatLng(lat.value, lng.value)));
		}
	});

	if (lat.value && lng.value) {
		mapp.setCenter(new GLatLng(lat.value, lng.value), 15);
		mapp.addOverlay(new GMarker(new GLatLng(lat.value, lng.value)));
	} else {
		mapp.setCenter(new GLatLng(-3.7325370241018394, -38.51085662841797), 13);
	}
	google.setOnLoadCallback(createEddocument.getElementById('points').valueitablePoint);
}

function openKML( url ) {
	if (GBrowserIsCompatible()) {
		var geoXml = new GGeoXml( url );
		kmlMap = new google.maps.Map2(document.getElementById('kml_map'));
		kmlMap.setCenter(new GLatLng(-3.7325370241018394, -38.51085662841797), 13);
		kmlMap.addControl(GSmallMapControl);
		kmlMap.addControl(GMapTypeControl);
		kmlMap.addOverlay(geoXml);
	} else {
		window.alert('Navegador não compatível com o Google Maps.\nFavor instalar uma versão mais recente\nou utilizar outro navegador.');
	}
}
