import React from 'react';
import CentralServerLinksProvider from "../../utils/CentralServerLinksProvider";
import PopUpForm from "../UI/PopUpForm/PopUpForm";
import Button from "../UI/Button/Button";

const DownloadFileForm = ({ isHidden, setIsHidden, buildPath, selectedFilesBuffer }) => {
    async function downloadFile(file, path) {
        window.open(`${CentralServerLinksProvider.backendOrigin}${await CentralServerLinksProvider.getLink("file-get")}/${path}/${file.name}`,
        '_blank');
    }

    async function downloadFiles() {
        let path = buildPath();
        for (let file of selectedFilesBuffer)
            await downloadFile(file, path);
        setIsHidden(true);
    }

    return (
        <PopUpForm header={`Вы действительно хотите скачать выбранные файлы?`} isHidden={isHidden}>
            <Button text={'Скачать'} onClick={downloadFiles}/>
            <Button text={'Отменить'} onClick={() => setIsHidden(true)}/>
        </PopUpForm>
    );
};

export default DownloadFileForm;