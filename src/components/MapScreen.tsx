import React, {useEffect, useState} from 'react';
import {
  View,
  StyleSheet,
  Dimensions,
  Image,
  Text,
  NativeModules,
  AppState,
  DeviceEventEmitter,
} from 'react-native';

import MapView, {Marker} from 'react-native-maps';
import Geolocation from 'react-native-geolocation-service';

import MapDirections from './MapDirections';

import chevron from '@images/chevron.png';
import batterySaverIcon from '@images/ic_battery_saver.png';
import {GOOGLE_API_KEY} from '../../.env';

const Chevron: React.FC = () => {
  return <Image style={styles.chevron} source={chevron} />;
};

interface Coordinate {
  latitude: number;
  longitude: number;
  heading?: number;
}

const MapScreen: React.FC = () => {
  const [coordinates, setCoordinates] = useState<Coordinate[]>([]);
  const [saverStatus, setSaverStatus] = useState<boolean>(false);
  const {requestLocationPermission} = NativeModules.BatteryStatus;

  const positionLookup = async (watchId: number | null) => {
    watchId = Geolocation.watchPosition(
      position => {
        const {longitude, latitude, heading} = position.coords;
        setCoordinates(prevCo => [...prevCo, {longitude, latitude, heading}]);
      },
      error => console.log(error),
      {enableHighAccuracy: true, distanceFilter: 20, interval: 600000},
    );
  };

  // callback function to handle battery saver status change
  const handleBatteryStatusChange = (isPowerSaveMode: boolean) => {
    setSaverStatus(isPowerSaveMode);
  };

  useEffect(() => {
    requestLocationPermission();

    // adding listener for broadcaster
    DeviceEventEmitter.addListener(
      'BatteryStatusChanged',
      handleBatteryStatusChange,
    );

    let watchId: number | null = null;
    positionLookup(watchId);

    return () => {
      Geolocation.clearWatch(watchId!);
    };
  }, []);

  return (
    <View style={styles.container}>
      {coordinates.length > 0 && (
        <MapView
          style={styles.map}
          loadingEnabled={true}
          initialRegion={{
            latitude: coordinates[0]?.latitude || 0,
            longitude: coordinates[0]?.longitude || 0,
            latitudeDelta: 0.0622,
            longitudeDelta: 0.0121,
          }}>
          {coordinates.length > 1 && (
            <MapDirections coordinates={coordinates} apikey={GOOGLE_API_KEY} />
          )}

          {coordinates.length > 1 &&
            coordinates.map((latlng, index) => (
              <Marker
                key={index}
                coordinate={latlng}
                flat
                anchor={{x: 0.5, y: 0.5}}>
                <View style={{transform: [{rotate: `${latlng.heading}deg`}]}}>
                  <Chevron />
                </View>
              </Marker>
            ))}
        </MapView>
      )}
      <View style={styles.statusContainer}>
        <View>
          <Text style={styles.statusHeading}>Saver Status:</Text>
          <Text
            style={[styles.statusText, {color: saverStatus ? 'green' : 'red'}]}>
            {saverStatus ? 'ON' : 'OFF'}
          </Text>
        </View>
        <Image
          style={[
            styles.statusImage,
            {tintColor: saverStatus ? 'green' : 'red'},
          ]}
          source={batterySaverIcon}
        />
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  map: {
    width: Dimensions.get('window').width,
    height: Dimensions.get('window').height,
  },
  statusContainer: {
    position: 'absolute',
    width: 200,
    height: 50,
    borderRadius: 10,
    backgroundColor: '#BBB6DF',
    justifyContent: 'space-between',
    alignItems: 'center',
    paddingHorizontal: 10,
    right: '1%',
    flexDirection: 'row',
    marginVertical: 10,
  },
  statusText: {
    fontWeight: 'bold',
    color: 'black',
  },
  statusHeading: {
    color: 'black',
  },
  statusImage: {
    width: 40,
    height: 20,
  },
  chevron: {
    width: 20,
    height: 20,
    tintColor: 'red',
  },
});

export default MapScreen;
