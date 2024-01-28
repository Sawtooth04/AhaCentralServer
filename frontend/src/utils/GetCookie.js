function getCookie(name) {
    if (document.cookie.length > 0) {
        let cookie = document.cookie.match(`${name}=[^;]*`);

        if (cookie != null)
            return cookie[0].split('=')[1];
    }
    return "";
}

export default getCookie;