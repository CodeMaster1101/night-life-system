import React, { useState, useEffect, useRef } from 'react';
import { MapContainer, TileLayer, Marker, Tooltip } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';
import L from 'leaflet';

function Map() {
  const [nightclubs, setNightclubs] = useState([]);
  const mapRef = useRef();

  useEffect(() => {
    async function fetchNightclubs() {
      const response = await fetch(
        'https://overpass-api.de/api/interpreter?data=[out:json][timeout:25];node["amenity"="nightclub"](41.9816, 21.4021, 42.0276, 21.4857);out;'
      );
      const data = await response.json();
      setNightclubs(data.elements);
    }

    fetchNightclubs();
  }, []);

  const getIconSize = (zoom) => {
    const baseSize = 10;
    const scaleFactor = 20;
    if (zoom < 14) {
      return [baseSize + scaleFactor / (14 - zoom), baseSize + scaleFactor / (14 - zoom)];
    } else {
      return [baseSize - scaleFactor / (zoom - 14), baseSize - scaleFactor / (zoom - 14)];
    }
  };

  const redIcon = (zoom) =>
    L.icon({
      iconUrl: `${process.env.PUBLIC_URL}/icons8-map-marker-64.png`,
      iconSize: getIconSize(zoom),
      iconAnchor: [5, 5],
      tooltipAnchor: [0, -32],
    });

  const getTooltipFontSize = (zoom) => {
    const baseSize = 12;
    const scaleFactor = 1.5;
    return baseSize + scaleFactor * (zoom / 20);
  };

  const handleMarkerMouseOver = async (e, marker) => {
    const { lat, lng } = e.latlng;
    const zoom = mapRef.current.getZoom();
    const fontSize = getTooltipFontSize(zoom);

    const geoapifyApiKey = '8e285b751550491eb8888a6f5c9af6b4';
    const apiUrl = `https://api.geoapify.com/v1/geocode/reverse?lat=${lat}&lon=${lng}&limit=1&apiKey=${geoapifyApiKey}`;
    const response = await fetch(apiUrl);
    const data = await response.json();
    const feature = data.features[0];
    const clubName = feature.properties.name || 'Nightclub';
    const address = feature.properties.address_line2 || '';

    const tooltipContent = `<b>${clubName}</b><br>${address}`;
    marker.getTooltip().setContent(tooltipContent);
    marker.getTooltip().getElement().style.fontSize = `${fontSize}px`;
  };

  const handleZoomEnd = () => {
    const zoom = mapRef.current.getZoom();
    nightclubs.forEach((nightclub) => {
      const marker = mapRef.current.leafletElement._layers[nightclub.id];
      if (marker) {
        marker.setIcon(redIcon(zoom));
      }
    });
  };

  return (
    <MapContainer
      ref={mapRef}
      center={[41.9973, 21.4280]}
      zoom={13}
      scrollWheelZoom={false}
      style={{ height: '100vh', width: '100%' }}
      eventHandlers={{ zoomend: handleZoomEnd }}
    >
      <TileLayer
        url="https://{s}.basemaps.cartocdn.com/dark_all/{z}/{x}/{y}{r}.png"
        attribution='&copy; <a href="https://carto.com/">CARTO</a> | <a href="https://openstreetmap.org">OpenStreetMap</a> contributors'
      />
      {nightclubs.map((nightclub) => (
        <Marker
          key={nightclub.id}
          position={[nightclub.lat, nightclub.lon]}
          icon={redIcon(13)}
          eventHandlers={{
            mouseover: (e) => {
              handleMarkerMouseOver(e, e.target);
            },
          }}
        >
          <Tooltip direction="top" offset={[0, -32]} permanent={false} />
        </Marker>
      ))}
    </MapContainer>
);
}

export default Map;
