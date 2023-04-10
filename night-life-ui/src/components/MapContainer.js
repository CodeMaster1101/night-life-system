import React, { useState, useEffect, useRef } from 'react';
import { MapContainer as LeafletMapContainer, TileLayer } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';
import POI from './POI/POI.js';
import POIInfoBox from './POIInfoBox/POIInfoBox.js';

function MapContainer() {
  const [nightclubs, setNightclubs] = useState([]);
  const [poiInfo, setPoiInfo] = useState(null);
  const mapRef = useRef();

  useEffect(() => {
    async function fetchCoordinates() {
      const response = await fetch(
        'https://localhost:8443/api/v1/server-boot/coordinates'
      );
      setNightclubs(await response.json());
    }

    fetchCoordinates();
  }, []);

  const handlePOIClick = (data) => {
    setPoiInfo(data);
  };

  const handleCloseInfoBox = () => {
    setPoiInfo(null);
  };

  return (
    <div style={{ position: 'relative' }}>
      <LeafletMapContainer
        ref={mapRef}
        center={[41.9973, 21.4280]}
        zoom={13}
        scrollWheelZoom={false}
        style={{ height: '100vh', width: '100%' }}
      >
        <TileLayer
          url="https://{s}.basemaps.cartocdn.com/dark_all/{z}/{x}/{y}{r}.png"
        />
        {nightclubs.map((nightclub, index) => (
          <POI
            key={index}
            position={[nightclub.latitude, nightclub.longitude]}
            mapRef={mapRef}
            onPOIClick={handlePOIClick}
          />
        ))}
      </LeafletMapContainer>
      {poiInfo && <POIInfoBox data={poiInfo} onClose={handleCloseInfoBox} />}
    </div>
  );
}

export default MapContainer;
