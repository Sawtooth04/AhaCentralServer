import getCookie from "./GetCookie";

function buildCsrfXhr(method, url) {
    let result = new XMLHttpRequest();
    result.open(method, url);
    result.setRequestHeader('X-XSRF-TOKEN', getCookie('XSRF-TOKEN'));
    return result;
}

export default buildCsrfXhr;