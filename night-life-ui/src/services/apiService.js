import L from 'leaflet';

export const getIconSize = (zoom) => {
  const baseSize = 10;
  const scaleFactor = 20;
  if (zoom < 14) {
    return [baseSize + scaleFactor / (14 - zoom), baseSize + scaleFactor / (14 - zoom)];
  } else {
    return [baseSize - scaleFactor / (zoom - 14), baseSize - scaleFactor / (zoom - 14)];
  }
};

export const redIcon = (zoom) =>
  L.icon({
    iconUrl: `${process.env.PUBLIC_URL}/icons8-map-marker-64.png`,
    iconSize: getIconSize(zoom),
    iconAnchor: [5, 5],
    tooltipAnchor: [0, -32],
  });

export const getTooltipFontSize = (zoom) => {
  const baseSize = 12;
  const scaleFactor = 1.5;
  return baseSize + scaleFactor * (zoom / 20);
};

export async function fetchClubData(lat, lng) {
  const apiUrl = 'https://localhost:8443/api/v1/map/on-hover';
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
    return await response.json();
  } else {
    console.error('Error fetching data:', response.statusText);
    return null;
  }
}

export async function fetchClubAndEvent(coordinates, date) {
  const response = await fetch(`https://localhost:8443/api/v1/map/place/on-click/${date}`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(coordinates),
  });

  if (response.ok) {
    return await response.json();
  } else {
    console.error('Error fetching data:', response.statusText);
    return null;
  }
}
