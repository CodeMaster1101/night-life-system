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
        'https://MILEIP:8443/api/v1/server-boot/coordinates'
      );
      const data = await response.json();
      setNightclubs(data);
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
  const { lat, lng } = e.target.getLatLng();
  const zoom = mapRef.current.getZoom();
  const fontSize = getTooltipFontSize(zoom);

  const apiUrl = 'https://MILEIP:8443/api/v1/map/on-hover';
  const response = await fetch(apiUrl, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({
      latitude: lat,
      longitude: lng,
    }),
  });

  if (response.ok) {
    const data = await response.json();

    const clubName = data.name || 'Nightclub';
    const genre = data.genre || '';
    const averageCost = data.averageCost !== undefined ? data.averageCost : '';
    const address = data.address || '';

    const tooltipContent = `<b>${clubName}</b><br>Genre: ${genre}<br>Avg. cost: ${averageCost}<br>${address}`;
    marker.getTooltip().setContent(tooltipContent);
    marker.getTooltip().getElement().style.fontSize = `${fontSize}px`;
  } else {
    console.error('Error fetching data:', response.statusText);
  }
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
  
      />
      {nightclubs.map((nightclub, index) => (
        <Marker
          key={index}
          position={[nightclub.latitude, nightclub.longitude]}
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