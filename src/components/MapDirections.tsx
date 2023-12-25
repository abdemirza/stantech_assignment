import React from 'react';
import MapViewDirections from 'react-native-maps-directions';

interface MapDirectionsProps {
  coordinates: {latitude: number; longitude: number; heading?: number}[];
  apikey: string;
}

const MapDirections: React.FC<MapDirectionsProps> = ({coordinates, apikey}) => {
  return (
    <MapViewDirections
      origin={coordinates[0]}
      waypoints={coordinates.slice(1)}
      destination={coordinates[coordinates.length - 1]}
      apikey={apikey}
      strokeWidth={20}
      strokeColor="red"
    />
  );
};

export default MapDirections;
