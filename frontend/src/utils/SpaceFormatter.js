class DataLabelsEnum {
    _labels = ["PB", "TB", "GB", "MB", "Bytes"];

    get(index) {
        return this._labels[index];
    }

    count() {
        return this._labels.length;
    }
}

function formatSpace(space) {
    let labelsEnum = new DataLabelsEnum();

    if (typeof space !== 'undefined') {
        let result = space.basis * Math.pow(10, space.degree);

        for (let i = 0; i < labelsEnum.count(); i++)
            if (Math.log10(result) + 1 < 2)
                result *= 1024;
            else
                return `${Math.round(result)} ${labelsEnum.get(i)}`;
    }
    return `0 ${labelsEnum.get(labelsEnum.count() - 1)}`;
}

export default formatSpace;