export const GOOGLE_APIKEY = "${APIKEY_GOOGLE}";
export function getMapsGeocodeUrl(latitude, longitude) {
    return `https://maps.googleapis.com/maps/api/geocode/json?latlng=${latitude},${longitude}&key=${GOOGLE_APIKEY}`;
}
export function getMapsScriptsUrl() {
    return `https://maps.googleapis.com/maps/api/js?key=${GOOGLE_APIKEY}&callback=initMap`;
}
export function getLocalExplorerAttractionUrl(latitude, longitude, locality) {
    return `http://localhost:8080/local-explorer/public/attractions?latitude=${latitude}&longitude=${longitude}&city=${locality}`;
}
export function getLocalExplorerPhotoUrl(address) {
    return `http://localhost:8080/local-explorer/public/attraction/photo?address=${encodeURIComponent(address)}`;
}
