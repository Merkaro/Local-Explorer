import React, { useEffect, useState } from 'react';
import PhotoRetriever from "./PhotoRetriever";
import axios from "axios";
import Map from "./Map";
import '../css/App.css';
import {getLocalExplorerAttractionUrl, getMapsGeocodeUrl} from "../utils/constants";

const App = () => {
    const [userLatitude, setUserLatitude] = useState("");
    const [userLongitude, setUserLongitude] = useState("");
    const [userLocality, setUserLocality] = useState("");
    const [addresses, setAddresses] = useState([]);
    const [currentIndex, setCurrentIndex] = useState(0);
    const [attractions, setAttractions] = useState([]);

    useEffect(() => {
        const getUserLocation = async () => {
            try {
                const position = await new Promise((resolve, reject) => {
                    navigator.geolocation.getCurrentPosition(resolve, reject);
                });
                const { latitude, longitude } = position.coords;
                setUserLatitude(latitude)
                setUserLongitude(longitude);

                const response = await axios.get(getMapsGeocodeUrl(latitude, longitude));
                const data = response.data;
                const cityComponent = data.results.find(result =>
                    result.types.includes('locality')
                );
                if (cityComponent) {
                    setUserLocality(cityComponent.formatted_address);
                }
            } catch (error) {
                console.error('Error getting user location:', error);
            }
        };
        getUserLocation();
    }, []);

    useEffect(() => {
        const fetchData = async () => {
            const response = await axios.get(getLocalExplorerAttractionUrl(userLatitude, userLongitude, userLocality));
            if(response?.status === 200 && response.data?.attractions) {
                setAttractions(response.data.attractions);
                setAddresses(response.data.attractions.map((e) => e.address));
            }
        };
        if(userLatitude && userLongitude && userLocality) {
            fetchData();
        }
    }, [userLatitude, userLongitude, userLocality]);

    const handleNext = () => {
        setCurrentIndex((prevIndex) => (prevIndex + 1) % addresses.length);
    };

    const handlePrev = () => {
        setCurrentIndex((prevIndex) => (prevIndex - 1 + addresses.length) % addresses.length);
    };

    return (
        <div className="App">
            <h2>Explore your surroundings with Local Explorer</h2>
            { addresses.length > 0 && (
                <>
                    <h3>Here are 4 attractions we picked for you </h3>
                    <div style={{ display: 'flex', alignItems: 'flex-start' }}>
                        <div style={{ flex: 1 }}>
                            <div className={"section-container"}>
                                <PhotoRetriever address={attractions[currentIndex].name + ", " + userLocality} />
                                <h3>{attractions[currentIndex].name}</h3>
                                <p>{attractions[currentIndex].description}</p>
                                <p><b>{attractions[currentIndex].address}</b></p>
                                <div style={{ marginBottom: '40px' }}>
                                    <button className={"pretty-button"} onClick={handlePrev}>
                                        Previous
                                    </button>
                                    <button className={"pretty-button"} onClick={handleNext}>
                                        Next
                                    </button>
                                </div>
                            </div>

                        </div>
                        <div style={{ flex: 1 }}>
                            <div className={"section-container"}>
                                <Map address={addresses[currentIndex]} />
                            </div>
                        </div>
                    </div>

                </>
            )
            }
        </div>
    );
}

export default App;