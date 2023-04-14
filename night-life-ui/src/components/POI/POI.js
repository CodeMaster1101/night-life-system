import React from 'react';
import { Marker, Tooltip } from 'react-leaflet';
import { redIcon, getTooltipFontSize, fetchClubData, fetchClubAndEvent } from '../../services/apiService';

function POI({ mapRef, position, onPOIClick }) {
  const handleMarkerMouseOver = async (e, marker) => {
    const { lat, lng } = e.target.getLatLng();
    const zoom = mapRef.current.getZoom();
    const fontSize = getTooltipFontSize(zoom);

    const data = await fetchClubData(lat, lng);

    if (data) {
      const clubName = data.name || 'Nightclub';
      const genre = data.genre || '';
      const averageCost = data.averageCost !== undefined ? data.averageCost : '';
      const address = data.address || '';

      const tooltipContent = `<b>${clubName}</b><br>Genre: ${genre}<br>Avg. cost: ${averageCost}<br>${address}`;
      marker.getTooltip().setContent(tooltipContent);
      marker.getTooltip().getElement().style.fontSize = `${fontSize}px`;
    } else {
      console.error('Error fetching data');
    }
  };

  const handleMarkerClick = async (e) => {
    const { lat, lng } = e.target.getLatLng();
    const coordinates = { latitude: lat, longitude: lng };
    const date = "2023-05-15"; // Get the current date in 'YYYY-MM-DD' format
    const data = await fetchClubAndEvent(coordinates, date);
    if (data) {
      onPOIClick(data);
    }
  };

  return (
    <Marker
      position={position}
      icon={redIcon(13)}
      eventHandlers={{
        mouseover: (e) => {
          handleMarkerMouseOver(e, e.target);
        },
        click: handleMarkerClick,
      }}
    >
      <Tooltip direction="top" offset={[0, -32]} permanent={false} />
    </Marker>
  );
}

export default POI;
