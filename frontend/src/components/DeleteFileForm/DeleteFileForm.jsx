import React from 'react';
import PopUpForm from "../UI/PopUpForm/PopUpForm";
import Button from "../UI/Button/Button";
import CsrfFetch from "../../utils/CsrfFetch";
import CentralServerLinksProvider from "../../utils/CentralServerLinksProvider";

const DeleteFileForm = ({ isHidden, setIsHidden, buildPath, selectedFilesBuffer, onDelete, forbidden }) => {
    async function deleteFile(file, path) {
        let response = await CsrfFetch(`${await CentralServerLinksProvider.getLink("file-delete")}/${path}/${file.name}`, {
            method: 'delete'
        });
        return response.status !== 403;
    }

    async function deleteFiles() {
        let path = buildPath();
        for (let file of selectedFilesBuffer)
            if (!(await deleteFile(file, path))) {
                forbidden();
                break;
            }
        setIsHidden(true);
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