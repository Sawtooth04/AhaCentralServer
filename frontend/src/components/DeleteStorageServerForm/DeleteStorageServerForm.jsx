import React from 'react';
import CsrfFetch from "../../utils/CsrfFetch";
import CentralServerLinksProvider from "../../utils/CentralServerLinksProvider";
import PopUpForm from "../UI/PopUpForm/PopUpForm";
import Button from "../UI/Button/Button";

const DeleteStorageServerForm = ({ isHidden, setIsHidden, selectedStorageServersBuffer, onDelete}) => {
    async function deleteStorageServer(server) {
        await CsrfFetch((await CentralServerLinksProvider.getLink("storage-server-delete")).replace('{storageServerID}', server.storageServerID), {
            method: 'delete'
        });
    }

    async function deleteStorageServers() {
        for (let server of selectedStorageServersBuffer)
            await deleteStorageServer(server);
        setIsHidden(true);
        await onDelete();
    }

    return (
        <PopUpForm header={`Вы действительно хотите удалить выбранные серверы хранения?`} isHidden={isHidden}>
            <Button text={'Удалить'} onClick={deleteStorageServers}/>
            <Button text={'Отменить'} onClick={() => setIsHidden(true)}/>
        </PopUpForm>
    );
};

export default DeleteStorageServerForm;