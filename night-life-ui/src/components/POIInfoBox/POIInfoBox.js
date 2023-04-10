import React from 'react';
import './POIInfoBox.css';

function POIInfoBox({ data, onClose }) {
  const { onHoverClub, partyEventDTO } = data;
  const { genre, averageCost, address } = onHoverClub;
  const { eventName, description, thumbnail, clubName } = partyEventDTO;

  if (thumbnail == null) {
    const thumbnailUrl = `data:image/png;base64,${process.env.PUBLIC_URL}/noEvents.png`;
    return (
        <div className="poi-info-box">
          <div className="poi-info-box-header">
            <h4>Event</h4>
            <button onClick={onClose}>&times;</button>
          </div>
          <div className="poi-info-box-content">
            <div className="poi-info-box-thumbnail">
              <img src={thumbnailUrl} alt="Event thumbnail" />
            </div>
            <div className="poi-info-box-details">
              <h4>{eventName}</h4>
              <p><strong>Club name:</strong> {clubName}</p>
              <p><strong>Genre:</strong> {genre}</p>
              <p><strong>Average cost:</strong> {averageCost}</p>
              <p><strong>Description:</strong> {description}</p>
              <p><strong>Address:</strong> {address}</p>
            </div>
          </div>
        </div> );
  } else {
    const thumbnailUrl = `data:` + getImageFormat(thumbnail) + `;base64,${thumbnail}`;
    return (
        <div className="poi-info-box">
          <div className="poi-info-box-header">
            <h4>Event</h4>
            <button onClick={onClose}>&times;</button>
          </div>
          <div className="poi-info-box-content">
            <div className="poi-info-box-thumbnail">
              <img src={thumbnailUrl} alt="Event thumbnail" />
            </div>
            <div className="poi-info-box-details">
              <h4>{eventName}</h4>
              <p><strong>Club name:</strong> {clubName}</p>
              <p><strong>Genre:</strong> {genre}</p>
              <p><strong>Average cost:</strong> {averageCost}</p>
              <p><strong>Description:</strong> {description}</p>
              <p><strong>Address:</strong> {address}</p>
            </div>
          </div>
        </div>
      );
  }
}

const getImageFormat = (base64String) => {
    if (base64String.startsWith('iVBORw0KGg')) {
      return 'image/png';
    } else if (base64String.startsWith('/9j/4AA')) {
      return 'image/jpeg';
    } else if (base64String.startsWith('R0lGODlh')) {
      return 'image/gif';
    } else {
      return 'image/unknown'; // Default fallback if format is unknown
    }
  };

export default POIInfoBox;
