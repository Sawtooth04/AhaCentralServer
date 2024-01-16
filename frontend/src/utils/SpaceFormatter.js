class DataLabelsEnum {
    _labels = ["PB", "TB", "GB", "MB", "KB", "B"];

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

function formatBytes(bytes) {
    let labelsEnum = new DataLabelsEnum();
    let result = bytes;

    for (let i = labelsEnum.count() - 1; i >= 0; i--)
        if (Math.log10(result) + 1 > 4)
            result /= 1024;
        else
            return `${Math.round(result)} ${labelsEnum.get(i)}`;
    return `${Math.round(result)} ${labelsEnum.get(0)}`;
}

export {formatSpace as default, formatBytes};