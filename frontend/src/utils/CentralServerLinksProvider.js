class CentralServerLinksProvider {
    static async getLink(rel) {
        let response = await (await fetch('http://localhost:8081/api/', {'method': 'get'})).json();

        if ("_links" in response && rel in response._links && "href" in response._links[rel])
            return response._links[rel].href;
        else
            throw new Error("Link not found");
    }
}

export default CentralServerLinksProvider;