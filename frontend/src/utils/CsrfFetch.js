function getCookie(name) {
    if (document.cookie.length > 0) {
        let cookie = document.cookie.match(`${name}=[^;]*`);

        if (cookie != null)
            return cookie[0].split('=')[1];
    }
    return "";
}

function prepareParams(params) {
    if (typeof params === 'undefined')
        return {'headers': {'X-XSRF-TOKEN': getCookie('XSRF-TOKEN')}};
    if (typeof params.headers === 'undefined')
        return {
            ...params,
            'headers': {'X-XSRF-TOKEN': getCookie('XSRF-TOKEN')}
        };
    else {
        let newParams = {...params};
        newParams.headers = {...newParams.headers, 'X-XSRF-TOKEN': getCookie('XSRF-TOKEN')}
        return newParams;
    }
}

async function csrfFetch(url, params) {
    let newParams = prepareParams(params),
        response = await fetch(url, newParams);
    console.log(newParams);

    if (response.status === 403) {
        newParams = prepareParams(params);
        response = await fetch(url, newParams);
    }
    return response;
}

export default csrfFetch;