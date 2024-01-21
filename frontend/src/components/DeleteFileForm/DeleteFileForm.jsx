import React from 'react';
import PopUpForm from "../UI/PopUpForm/PopUpForm";
import Button from "../UI/Button/Button";
import CsrfFetch from "../../utils/CsrfFetch";
import CentralServerLinksProvider from "../../utils/CentralServerLinksProvider";

const DeleteFileForm = ({ isHidden, setIsHidden, buildPath, selectedFilesBuffer, toPrevDirectory, onDelete }) => {
    async function deleteFile(file, path) {
        await CsrfFetch(`${await CentralServerLinksProvider.getLink("file-delete")}/${path}/${file.name}`, {
            method: 'delete'
        });
    }

    async function deleteFiles() {
        let path = buildPath();
        for (let file of selectedFilesBuffer)
            await deleteFile(file, path);
        setIsHidden(true);
        toPrevDirectory();
        await onDelete();
    }

    return (
        <PopUpForm header={`Вы действительно хотите удалить выбранные файлы?`} isHidden={isHidden}>
            <Button text={'Удалить'} onClick={deleteFiles}/>
            <Button text={'Отменить'} onClick={() => setIsHidden(true)}/>
        </PopUpForm>
    );
};

export default DeleteFileForm;