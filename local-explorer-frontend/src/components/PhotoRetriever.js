import React, { useState, useEffect } from 'react';
import PropTypes from "prop-types";
import {getLocalExplorerPhotoUrl} from "../utils/constants";

const PhotoRetriever = ( props ) => {
    const [imageSrc, setImageSrc] = useState();
    const { address } = props;
    console.log("MKA ");
    console.log(address);

    useEffect(() => {
        (async () => {
                try {
                    const imgBase64 = await getPhotoBase64(address);
                    setImageSrc(imgBase64)

                } catch (error) {
                    console.error(`Error fetching image for ${address}:`, error);
                }
        })();
    }, [address]);

    const getPhotoBase64 = async (address) => {
        try {
            const url = getLocalExplorerPhotoUrl(address);
            const response = await fetch(url);
            if(response.ok) {
                const blob = await response.blob();
                return URL.createObjectURL(blob);
            }
        } catch (error) {
            throw new Error(`Failed to fetch place ID for ${address}: ${error}`);
        }
        return null;
    };

    return (
        <div className="image-item">
            <img src={imageSrc} alt={address} />
        </div>
    );
};

PhotoRetriever.propTypes = {
    address : PropTypes.string
};

PhotoRetriever.defaultProps = {
    attraction: ""
};

export default React.memo(PhotoRetriever);
