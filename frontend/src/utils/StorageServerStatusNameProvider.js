function GetStorageServerStatusName(name) {
    switch (name) {
        case 'storage':
            return 'Сервер хранения';
        case 'backup':
            return 'Сервер резервного копирования';
        default:
            return null;
    }
}

export default GetStorageServerStatusName;