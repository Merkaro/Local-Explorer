import {getMapsScriptsUrl} from "../utils/constants";

const PropTypes = require("prop-types");
const {useState, useEffect} = require("react");
const React = require("react");

const Map = (props) => {
    const [map, setMap] = useState(null);

    useEffect(() => {
        // Checking if Google Maps API script has already been loaded
        if (!window.google) {
            // Loading Google Maps API script if not already loaded
            const googleMapScript = document.createElement('script');
            googleMapScript.src = getMapsScriptsUrl();
            googleMapScript.async = true;
            window.initMap = initMap;
            document.body.appendChild(googleMapScript);
        } else {
            // If Google Maps API script is already loaded, initializing map directly
            initMap();
        }
    }, [props.address]);

    useEffect(() => {
        if (!map) return;
        const geocoder = new window.google.maps.Geocoder();
        geocoder.geocode({ address: props.address }, (results, status) => {
            if (status === 'OK') {
                map.setCenter(results[0].geometry.location);
                new window.google.maps.Marker({
                    map: map,
                    position: results[0].geometry.location,
                });
            } else {
                console.error('Geocode was not successful for the following reason: ', status);
            }
        });
    }, [map, props.address]);

    const initMap = () => {
        const mapOptions = {
            zoom: 14,
            center: { lat: 0, lng: 0 },
        };
        const mapElement = document.getElementById('map');
        const newMap = new window.google.maps.Map(mapElement, mapOptions);
        setMap(newMap);
    };

    return <div id="map" style={{ width: '100%', height: '400px', marginBottom: '20px' }}></div>;

};

Map.propTypes = {
    address : PropTypes.string
};

Map.defaultProps = {
    address: ""
};

export default React.memo(Map);
