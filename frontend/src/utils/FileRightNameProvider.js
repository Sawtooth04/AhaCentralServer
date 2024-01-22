function GetFileRightName(name) {
    switch (name) {
        case 'read':
            return 'Чтение';
        case 'write':
            return 'Запись';
        default:
            return null;
    }
}

export default GetFileRightName;